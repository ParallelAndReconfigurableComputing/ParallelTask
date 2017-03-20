package apt.processors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import apt.annotations.Gui;
import spoon.processing.AbstractAnnotationProcessor;
import spoon.reflect.code.CtCodeSnippetExpression;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtStatement;
import spoon.reflect.code.CtVariableRead;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.reference.CtTypeReference;

/**
 * This processor translates <b>local-variable</b> declarations that are annotated with <code>Gui</code>, into 
 * GUI handler methods that are either invoked after termination of a future object, or are 
 * enqueued for the event-dispatch thread as interim GUI events. However, the processor applies
 * strict validation checks for declarations that are annotated with <code>Gui</code>, and rejects the
 * validation tests in one of the following cases:
 * 
 * <ul>
 *  <li>If the right-hand side of the declaration is not a method invocation.</li>
 *  <li>If the handler method returns a value. That is, handler objects must be of type <b><code>Void</code></b>.</li>
 *  <li>If non-future objects are passed to the <code>notifiedBy</code> parameter.</li>
 *  <li>If more than one future objects are sent as arguments to the method being invoked. 
 *  That is, the handler depends on the termination of multiple future objects.</li>
 *  <li>If there is one future object sent as argument, but more than one future objects are specified as notifiers.
 *  In such a case, one future object intends to retrieve the result of another future object for its own handler.</li>
 *  <li>If there is exactly one future argument and one future notifier specified for a handler, but they refer
 *  to different future objects.</li>
 *  <li>If a variable that is sent as argument is not resolved for one of the notifier future objects.</li>
 * </ul>
 * 
 * The declaration may be notified by more than one future object, in which case every argument that is sent to a handler
 * method must be a variable (i.e., not a future object), and must be resolvable for all notifier future objects. 
 * It is important to note that if the <code>Gui</code> annotation does not specify the <code>notifiedBy</code>
 * parameter, then it is interpreted as an interim handler by the processor. Interim handlers are immediately enqueued to
 * the EDT event queue, therefore, they do not depend on the termination of any futures, and if future objects are sent as
 * arguments to a corresponding method, its value is retrieved first.
 * </br></br>
 *  
 * @author Mostafa Mehrabi
 * @since  2017
 *
 */
public class AptGuiProcessor extends AbstractAnnotationProcessor<Gui, CtLocalVariable<?>> {

	Gui thisAnnotation = null;
	CtLocalVariable<?> thisAnnotatedElement = null;
	CtInvocation<?> thisInvocation = null;
	List<CtExpression<?>> invocationArguments = null;
	List<CtExpression<?>> futureArguments = null;
	List<CtExpression<?>> variableArguments = null;
	boolean interimHandler = false;
	String[] notifierFutures;
	Factory thisFactory = null;
	
	@Override
	public void process(Gui annotation, CtLocalVariable<?> element) {
		try{
			invocationArguments = new ArrayList<>();
			futureArguments = new ArrayList<>();
			variableArguments = new ArrayList<>();
			thisFactory = getFactory();
			thisAnnotation = annotation;
			thisAnnotatedElement = element;
			inspectAnnotation();
			if(notifierFutures.length == 0){
				interimHandler = true;
			}
			else{
				interimHandler = false;
				if(!validate()){
					throw new IllegalArgumentException("THE COMBINATION OF PARAMETERS AND ARGUMENTS PROVIDED FOR GUI HANDLER " + thisAnnotatedElement
							+ " IS NOT VALID. PLEASE READ THE ERROR MESSAGE CORRESPONDINGLY!");
				}
			}		
			modifySourceCode();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void inspectAnnotation() throws Exception{
		//first check if the right-hand side of the annotated element is an invocation
		CtExpression<?> defaultExpression = thisAnnotatedElement.getDefaultExpression();
		if(!(defaultExpression instanceof CtInvocation<?>)){			
			throw new Exception("THE RIGHT-HAND SIDE OF THE STATEMENT " + thisAnnotatedElement 
					+ " CAN ONLY BE AN INVOCATION.");
		}
		//second, ensure that there is no return value specified for the invocation
		CtTypeReference<?> elementType = thisAnnotatedElement.getType();
				if(!(elementType.toString().contains("Void"))){
					throw new Exception("A HANDLER METHOD CANNOT HAVE A RETURN TYPE! DETECTED RETURN TYPE: " + elementType.toString());
		}
					
		notifierFutures = thisAnnotation.notifiedBy();
		removeRepeatedNotifiers();
		thisInvocation = (CtInvocation<?>) defaultExpression;
		invocationArguments = thisInvocation.getArguments();
	}
	
	private void removeRepeatedNotifiers(){
		List<String> temp = new ArrayList<>();
		for(String notifier : notifierFutures){
			if(!temp.contains(notifier))
				temp.add(notifier);
		}
		notifierFutures = new String[temp.size()];
		for(int i = 0; i < notifierFutures.length; i++){
			notifierFutures[i] = temp.get(i);
		}
	}
	
	private boolean validate(){			
		//if notifiers are specified for the GUI handler, ensure that they are all futures
		for(String notifier : notifierFutures){
			if(!APTUtils.isFutureVariable(thisAnnotatedElement, notifier)){
				System.err.println("THE PARAMETER " + notifier + " PROVIDED FOR @Gui IN " + thisAnnotatedElement.toString()
				  + " DOES NOT REPRESESNT A FUTURE!");
				return false;
			}
		}		
	
		//if there is no argument in the invocation, then it is valid
		if(invocationArguments.size() == 0)
			return true;
		
		distinguishVariablesFromFuturesInArguments();
		
		//else if there are more than one future arguments, then it is not valid
		if(futureArguments.size() > 1){
			System.err.print("A GUI HANDLER MAY NOT DEPEND ON THE TERMINATION OF MULTIPLE FUTURES AT THE SAME TIME.\n"
					+ "THERE ARE MORE THAN ONE FUTURES AS ARGUMENTS: ");
			for(CtExpression<?> futureArgument : futureArguments){
				System.err.print(APTUtils.getOrigName(futureArgument.toString()) + " ");
			}
			System.err.println();
			return false;
		}
		
		//else, if there is only one future argument
		if(futureArguments.size() == 1){
			if(notifierFutures.length > 1){
				System.err.print("HANDLER METHOD DEPENDS ON THE TERMINATION OF FUTUER " + APTUtils.getOrigName(futureArguments.get(0).toString())
				 		+ ", AND CAN NOT BE NOTIFIED BY OTHER FUTURES. SPECIFIED NOTIFIER FUTURES: " );
				for(String notifier : notifierFutures){
					System.err.print(notifier + " "); 
				}
				System.err.println();
				return false;
			}
			else{
				String futureObj = futureArguments.get(0).toString();
				futureObj = APTUtils.getOrigName(futureObj);
				String notifierFuture = notifierFutures[0];
				if(!futureObj.equals(notifierFuture)){
					System.err.println("HANDLER METHOD DEPENDS ON THE TERMINATION OF FUTURE " + futureObj +
							", AND CAN BE NOTIFIED BY THE SAME FUTURE OBJECT ONLY! THE SPECIFIED NOTIFIER: " + notifierFuture);
					return false;
				}
			}
		}
		
		if(!variableArgumentsValidForAllNotifiers())
			return false;
		
		return true;
	}
	
	private void distinguishVariablesFromFuturesInArguments(){
		for(CtExpression<?> argument : invocationArguments){
			//if argument "a" is a future object, then it has been replaced with "a_id.getReturnResult()"
			//that means, its taskID has replaced the actual future object after being processed by the 
			//@Future processor.
			System.out.println("inspecting expression: " + argument.toString() + " in invocation " + thisAnnotatedElement.getDefaultExpression().toString());
			if(APTUtils.isTaskIDReplacement(thisAnnotatedElement, argument.toString())){
				futureArguments.add(argument);
			}
			else if (argument instanceof CtVariableRead<?>){
				variableArguments.add(argument);
			}
		}
	}
	
	private boolean variableArgumentsValidForAllNotifiers(){
		for(String notifierFuture : notifierFutures){
			String taskInfoName = APTUtils.getTaskName(notifierFuture);
			CtStatement notifierDeclarationStatement = APTUtils.getDeclarationStatement(thisAnnotatedElement, taskInfoName);
			for(CtExpression<?> variableArgument : variableArguments){
				CtStatement variableDeclarationStatement = APTUtils.getDeclarationStatement(notifierDeclarationStatement, variableArgument.toString());	
				if(variableDeclarationStatement == null){
					System.err.println("VARIABLE " + variableArgument.toString() + " IS NOT RESOLVED FOR FUTURE " + notifierFuture);
					return false;
				}
			}
		}
		return true;
	}
	
	private void modifySourceCode(){
		if(interimHandler)
			insertInterimHandler();
		else
			registerHandlerForTaskInfos();
	}
	
	private void insertInterimHandler(){
		List<CtExpression<?>> executeInterimHandlerArguments = new ArrayList<>();
	
		CtInvocation<?> executeInterimHandler = thisFactory.Core().createInvocation();
		CtCodeSnippetExpression invocationTarget = thisFactory.Core().createCodeSnippetExpression();
		CtCodeSnippetExpression invocatrionArgument = thisFactory.Core().createCodeSnippetExpression();
		CtExecutableReference executable = thisFactory.Core().createExecutableReference();
		
		invocationTarget.setValue(APTUtils.getParaTaskSyntax());
		executable.setSimpleName(APTUtils.getExecuteInterimHandlerSyntax());
		invocatrionArgument.setValue("()->" + getFunctorStatement());
		executeInterimHandlerArguments.add(invocatrionArgument);
		
		executeInterimHandler.setTarget(invocationTarget);
		executeInterimHandler.setExecutable(executable);
		executeInterimHandler.setArguments(executeInterimHandlerArguments);
		thisAnnotatedElement.replace(executeInterimHandler);
	}
	
	private void registerHandlerForTaskInfos(){
		if(!futureArguments.isEmpty())
			proceedWithFunctorOneArg();
		else
			proceedWithFunctorNoArg();
		
		thisAnnotatedElement.delete();
	}
	
	private void proceedWithFunctorOneArg(){
		String futureName = notifierFutures[0];
		String taskInfoName = APTUtils.getTaskName(futureName);
		String futureArgName = APTUtils.getNonLambdaArgName(futureName);
		
		//updateGui(a_id.getReturnResult) --> updateGui(a_id_argName) Replacement starts
		CtCodeSnippetExpression newFutureArgExpression = thisFactory.Core().createCodeSnippetExpression();
		newFutureArgExpression.setValue(futureArgName);
		
		CtExpression<?> futureArgument = futureArguments.get(0);
		futureArgument.replace(newFutureArgExpression);
		//Replacement ends here
		//create the declaration of the functor (a_id_argName) -> updateGui(a_id_argName)
		String functorDeclarationPhrase = "(" + futureArgName + ")->" + getFunctorStatement();
		registerHandlerForTaskInfo(taskInfoName, functorDeclarationPhrase);		
	}
	
	private void proceedWithFunctorNoArg(){
		for(String notifierFuture : notifierFutures){
			String futureName = APTUtils.getOrigName(notifierFuture);
			String taskInfoName = APTUtils.getTaskName(futureName);
			String functorDeclarationPhrase = "()->" + getFunctorStatement();
			registerHandlerForTaskInfo(taskInfoName, functorDeclarationPhrase);		
		}
	}
	
	private void registerHandlerForTaskInfo(String taskInfoName, String functorDeclarationPhrase){
		//create the handler registering statement
		List<CtExpression<?>> invocationArguments = new ArrayList<>();
		CtInvocation<?> registerHandler = thisFactory.Core().createInvocation(); //the main invocation statement
		CtCodeSnippetExpression target = thisFactory.Core().createCodeSnippetExpression(); //target of the invocation (i.e., ParaTask)
		CtExecutableReference executable = thisFactory.Core().createExecutableReference(); //executable of the invocation (i.e., registerSlotToNotify)
		CtCodeSnippetExpression taskInfoArg = thisFactory.Core().createCodeSnippetExpression(); //the first argument of the invocation (taskInfoName)
		CtCodeSnippetExpression functorArg = thisFactory.Core().createCodeSnippetExpression(); //the second argument of the invocation (functor declaration)
		CtTypeReference<?> functorCast = thisFactory.Core().createTypeReference(); //casting type for functor (FunctorOneArgNoReturn)

		target.setValue(APTUtils.getParaTaskSyntax());
		executable.setSimpleName(APTUtils.getRegisterSlotToNotifySyntax());		
			
		taskInfoArg.setValue(taskInfoName);
		functorArg.setValue(functorDeclarationPhrase);
						
		invocationArguments.add(taskInfoArg);
		invocationArguments.add(functorArg);
			
		registerHandler.setTarget(target);
		registerHandler.setExecutable(executable);
		registerHandler.setArguments(invocationArguments);
				
		CtStatement futureDeclaration = APTUtils.getDeclarationStatement(thisAnnotatedElement, taskInfoName);
		futureDeclaration.insertAfter(registerHandler);
	}
	
	private String getFunctorStatement(){
		CtExpression<?> defaultExpression = thisAnnotatedElement.getDefaultExpression();
		String mainStatement = defaultExpression.toString();
		
		CtInvocation<?> invocation = (CtInvocation<?>) defaultExpression;
		Set<CtTypeReference<? extends Throwable>> throwables = invocation.getExecutable().getExecutableDeclaration().getThrownTypes();
		if(!throwables.isEmpty()){
			mainStatement = "{try{" + 
					mainStatement + ";" +
					"}catch(Throwable e){"+
					"e.printStackTrace();"+
					"}}";
		}
			
		return mainStatement;
	}
}
