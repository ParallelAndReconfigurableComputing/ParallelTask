package apt.processors;

import java.lang.instrument.IllegalClassFormatException;
import java.util.ArrayList;
import java.util.List;

import apt.annotations.Gui;
import spoon.processing.AbstractAnnotationProcessor;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.reference.CtTypeReference;

public class AptGuiProcessor extends AbstractAnnotationProcessor<Gui, CtLocalVariable<?>> {

	Gui thisAnnotation = null;
	CtLocalVariable<?> thisAnnotatedElement = null;
	CtInvocation<?> thisInvocation = null;
	List<CtExpression<?>> invocationArguments = null;
	List<CtExpression<?>> futureArguments = null;
	List<CtExpression<?>> variableArguments = null;
	boolean interimHandler = false;
	String[] notifierFutures;
	
	public AptGuiProcessor() {
		invocationArguments = new ArrayList<>();
		futureArguments = new ArrayList<>();
		variableArguments = new ArrayList<>();
	}
	
	@Override
	public void process(Gui annotation, CtLocalVariable<?> element) {
		thisAnnotation = annotation;
		thisAnnotatedElement = element;
		inspectAnnotation();
		if(notifierFutures.length == 0){
			interimHandler = true;
		}
		else{
			interimHandler = false;
			if(!validate()){
				return;
			}
		}		
		modifySourceCode();
	}
	
	private void inspectAnnotation(){
		//first check if the right-hand side of the annotated element is an invocation
		CtExpression<?> defaultExpression = thisAnnotatedElement.getDefaultExpression();
		if(!(defaultExpression instanceof CtInvocation<?>)){
			throw new IllegalArgumentException("THE RIGHT-HAND SIDE OF THE DECLARAION " + thisAnnotatedElement 
					+ " CAN ONLY BE AN INVOCATION.");
		}
		//second, ensure that there is no return value specified for the invocation
		CtTypeReference<?> elementType = thisAnnotatedElement.getType();
				if(!(elementType.toString().equals("Void"))){
					throw new IllegalArgumentException("A HANDLER METHOD CANNOT HAVE A RETURN TYPE!");
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
		notifierFutures = (String[]) temp.toArray();
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
			System.err.println("A GUI HANDLER MAY NOT DEPEND ON THE TERMINATION OF TWO FUTURES AT THE SAME TIME.\n"
					+ "THERE ARE MORE THAN ONE FUTURES AS ARGUMENTS: " + futureArguments);
			return false;
		}
		
		//else, if there is only one future argument
		if(futureArguments.size() == 1){
			if(notifierFutures.length > 1){
				System.err.println("HANDLER METHOD THAT DEPENDS ON THE TERMINATION OF FUTUER " + APTUtils.getOrigName(futureArguments.get(0).toString())
				 		+ " MAY NOT BE NOTIFIED BY OTHER FUTURES. SPECIFIED NOTIFIERS: " + notifierFutures.toString()); 
				return false;
			}
			else{
				String futureObj = futureArguments.get(0).toString();
				String notifierFuture = notifierFutures[0];
				if(!futureObj.equals(notifierFuture)){
					System.err.println("HANDLER METHOD DEPENDS ON THE TERMINATION OF FUTURE " + futureObj +
							", AND CAN BE NOTIFIED BY THE SAME FUTURE OBJECT ONLY! THE SPECIFIED NOTIFIER: " + notifierFuture);
					return false;
				}
			}
		}
		return true;
	}
	
	private void distinguishVariablesFromFuturesInArguments(){
		for(CtExpression<?> argument : invocationArguments){
			//if argument "a" is a future object, then it has been replaced with "a_id.getReturnResult()"
			//that means, its taskID has replaced the actual future object after being processed by the 
			//@Future processor.
			if(APTUtils.isTaskIDReplacement(thisAnnotatedElement, argument.toString())){
				futureArguments.add(argument);
			}
			else{
				variableArguments.add(argument);
			}
		}
	}
	
	private void modifySourceCode(){
		
	}
}
