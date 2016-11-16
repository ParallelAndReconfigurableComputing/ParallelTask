package sp.processors;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.compiler.InvalidInputException;

import sp.annotations.Future;
import sp.annotations.StatementMatcherFilter;
import sp.annotations.Task;
import spoon.reflect.factory.Factory;
import spoon.reflect.code.CtArrayAccess;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtCodeSnippetExpression;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtStatement;
import spoon.reflect.code.CtUnaryOperator;
import spoon.reflect.code.CtVariableAccess;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.reference.CtVariableReference;
import spoon.support.reflect.code.CtInvocationImpl;
import spoon.support.reflect.code.CtVariableAccessImpl;

/**
 * This annotation processor processes the <code>Future</code> annotations that appear
 * at the declaration of a collection (i.e., List, Set, Map, Collection for other
 * types), in order to obtain collection wrappers that can contain both variables and 
 * future variables.</br>
 * For example: <b>myList</b> is a <code>List</code> that calls on <code>ParaTask</code>
 * for a collection wrapper. The object can either add future variables, or make direct
 * calls on <b>only one</b> method (e.g., <code>myList.add(foo(a)</code>). That method
 * <b>MUST</b> be annotated with the <code>Task</code> annotation. If <code>'myList'</code>
 * invokes a method (e.g., <code>add</code>), for which the argument is a statement and 
 * not an invocation expression (e.g., <code>myList.add(foo(2) + foo(3))</code>), the operation
 * is performed sequentially iff the methods are not annotated with <code>Task</code>. However,
 * if a method is annotated with <code>Task</code> it is executed asynchronously, but the 
 * invocation blocks until the result from that asynchronous task is back. That is, the 
 * corresponding future variable will not be added to the container, rather the container
 * waits until the result from the asynchronous task is back.  
 * 
 * @author Mostafa Mehrabi
 * @since  2016
 */
public class CollectionWrapperProcessor extends PtAnnotationProcessor {
	
	private List<CtVariableAccess<?>> variableAccessArgumentsToBeProcessed = null;
	private List<CtInvocation<?>> invocationsToBeProcessed = null;
	private Map<CtStatement, List<CtInvocation<?>>> invocationArguments = null;
	private Future  thisFutureAnnotation = null;
	private boolean insideCollectionStatement = false;
	private int     encounteredInvocationArguments = 0;
	private int     newLocalVariableIndex = 0;
	private CtTypeReference<?> thisCollectionGenericType = null;
	private CtTypeReference<?> thisCollectionType = null;
		
	public CollectionWrapperProcessor(Factory factory, Future future, CtLocalVariable<?> annotatedElement){
		thisAnnotatedElement = annotatedElement;
		thisFactory = factory;
		thisFutureAnnotation = future;
		thisElementName = thisAnnotatedElement.getSimpleName();
	}

	@Override
	public void process() {
		if(!validate())
			return;
		inspectElement();
		findCollectionInvocationArguments();
		modifySourceCode();
	}
	
	private void inspectElement(){		
		String thisStatement = thisAnnotatedElement.toString();
		String collectionType = thisStatement.substring(thisStatement.indexOf("<")+1, thisStatement.indexOf(">"));
		collectionType = APTUtils.getType(collectionType);
		thisCollectionGenericType = thisFactory.Core().createTypeReference();
		thisCollectionGenericType.setSimpleName(collectionType);
		thisCollectionType = thisAnnotatedElement.getType();
	}
	
	
	private boolean validate(){
		String defaultExpression = thisAnnotatedElement.getDefaultExpression().toString();
		if(!(defaultExpression.contains(APTUtils.getParaTaskWrapperSyntax()))){
			System.out.println("ANNOTATION PROCESSING ERROR FOR:\n" + thisAnnotatedElement);
			System.out.println("\nFUTURE ANNOTATION MUST BE USED FOR DECLARING COLLECTIONS THAT INVOKE: pt.runtime.ParaTask.getPtWrapper"
					+ "\nEXAMPLE: List<Integer> myList = ParaTask.getPtWrapper(new ArrayList<Integer>())");
			return false;
		}
		return true;
	}
	

	/*
	 * This method first modifies every invocation on a collection wrapper that involves variable access expressions.
	 * That is, directly using variables or future variables for invocations on the collection wrapper. 
	 * In the next stage, the invocations that use method invocation as their arguments are processed, and compiled. 
	 * At the end, the declaration statement is changed to declaring and casting the collection wrapper. 
	 * Note, that the order of processes is very important. 
	 * 
	 * @see sp.processors.PtAnnotationProcessor#modifySourceCode()
	 */
	@Override
	protected void modifySourceCode() {
		/*
		 * order of modifications is important. Modify collection declaration
		 * changes AST node configurations apparently, and causes some of the 
		 * collected information to become null!
		 */
		modifyVarAccessExpressions();
		modifyInvocationExpressions();
		modifyCollectionDeclaration();
	}
	
	private void modifyCollectionDeclaration(){
		insertStatementBeforeDeclaration();
		System.out.println("pre-statement modification done");
		changeDeclarationName();
		System.out.println("Statement modification done");
		insertStatementAfterDeclaration();
		System.out.println("Post statement modification done");
	}
		
	/*
	 * Finds all the arguments, both variable access and method invocations, that are used 
	 * in an invocation on the collection wrapper. That is, finding arguments that fit within
	 * one of the following invocation formats:
	 * 
	 * myList.add(_taskIDReplacement_.getReturnResult())
	 * 
	 * or
	 * 
	 * @Future
	 * Obj a = foo();
	 * myList.add( a ) --> where "a" is a future variable that is not processed yet
	 * 
	 * or
	 * 
	 * myList.add(foo(a)) --> where "foo" is a function annotated with @Task
	 * 
	 * The rest of the cases do not need to be processed. 
	 */
	private void findCollectionInvocationArguments(){
		System.out.println("/\\/\\/\\/\\/\\FINDCOLLECTIONARGUMENTS/\\/\\/\\/\\");
		List<CtStatement> containingStatements = APTUtils.findVarAccessOtherThanFutureDefinition(thisAnnotatedElement.getParent(CtBlock.class), thisAnnotatedElement);
		mapOfContainingStatements = APTUtils.listAllExpressionsOfStatements(containingStatements);
		//System.out.println("containing statements: " + mapOfContainingStatements); 

		
		variableAccessArgumentsToBeProcessed = new ArrayList<>(); //lists all variable access arguments in an invocation on collection wrapper, that may comply with the second case
		invocationArguments = new HashMap<>(); //lists all invocations encountered within an invocation on collection wrapper, that may comply with first and third cases 
		
		Set<CtStatement> statements = mapOfContainingStatements.keySet();
		for(CtStatement statement : statements){
			//System.out.println("inspecting: " + statement + " elements: " + mapOfContainingStatements.get(statement));

			/*
			 * collects the invocations that are used within an invocation on the collection (e.g., myList.add(foo(a) + foox(b)); )
			 * for further investigations, if any of the methods is supposed to be processed asynchronously (i.e., has @Task annotation).
			*/
			invocationsToBeProcessed = new ArrayList<>(); 
			/*
			 * indicates if we have found an invocation statement on the collection, 
			 * in which case, the arguments are collected to see if they fit within 
			 * any of the formats mentioned above.
			*/
			insideCollectionStatement = false; 
		
			if(statement instanceof CtInvocationImpl<?>){
				CtInvocationImpl<?> invocation = (CtInvocationImpl<?>) statement;
				CtExpression<?> target = invocation.getTarget();
				if(target != null){
					if(targetIsThisElement(target)){
						insideCollectionStatement = true;
						List<CtExpression<?>> arguments = invocation.getArguments();
						for(CtExpression<?> argument : arguments)
							findArgumentsToProcess(argument);
					}					
				}
			}
			
			/*
			 * If the statement is not an invocation on the collection, then it may contain expressions that are
			 * invocations on the collection. Therefore, its expressions need to be further inspected. 
			 */
			if(!insideCollectionStatement){
				Set<CtExpression<?>> statementExpressions = mapOfContainingStatements.get(statement).keySet();
				for (CtExpression<?> expression : statementExpressions){
					findArgumentsToProcess(expression);
				}
			}
			
			// if there are invocations within the statement that must be inspected, then add them to the list. 
			if(invocationsToBeProcessed.size() != 0){
				//System.out.println("adding " + invocationsToBeProcessed + " for " + statement);
				invocationArguments.put(statement, invocationsToBeProcessed);
			}
		}	
	}
	
	/*
	 * Modifies expressions of variables, which are sent as arguments to method invocations on the collection object. 
	 * Variables that are sent as arguments will be changed to taskID equivalent, if the variable is declared as a
	 * future variable by the programmer. 
	 * 
	 * For example:
	 * @Future
	 * int a = foo(x); 
	 * myList.add(a);
	 * 
	 * turns into:
	 * TaskID _aTaskID_ = _aTaskInfo_.start(x);
	 * myList.add(_aTaskID_);
	 * 
	 * myList is a TaskID aware collection. 
	 */
	private void modifyVarAccessExpressions(){
		for(CtVariableAccess<?> varAccess : variableAccessArgumentsToBeProcessed){
			String varName = varAccess.toString();
			boolean expressionModified = false;
			
			if(APTUtils.isTaskIDReplacement(thisAnnotatedElement, varName)){
				modifyWithTaskIDReplacement(varAccess);
				expressionModified = true;
			}
			
			else{
				CtStatement declarationStatement = APTUtils.getDeclarationStatement(varAccess.getParent(CtStatement.class)
													, varName);
				if(declarationStatement != null){
					Future future = hasFutureAnnotation(declarationStatement);
					if(future != null){
						modifyWithFutuerObject(future, declarationStatement, varAccess);
						expressionModified = true;
					}
				}
			}			
		}
		System.out.println("modifyVarAccessExpression done");
	}
	
	/*
	 * Modifies future variables that are declared after the declaration of the this collection object; therefore they
	 * are not processed by the annotation processor yet. So, they have to be processed manually.
	 */
	private void modifyWithFutuerObject(Future future, CtStatement declarationStatement, CtVariableAccess<?> varAccess){
		CtLocalVariable<?> annotatedElement = (CtLocalVariable<?>) declarationStatement;
		InvocationProcessor processor = new InvocationProcessor(thisFactory, future, annotatedElement);
		processor.process();
		
		//Annotation processed, so remove it!
		List<CtAnnotation<? extends Annotation>> annotations = new ArrayList<>();
		List<CtAnnotation<? extends Annotation>> actualAnnotations = declarationStatement.getAnnotations();
		for(CtAnnotation<? extends Annotation> annotation : actualAnnotations){
			Annotation actualAnnotation = annotation.getActualAnnotation();
			if(!(actualAnnotation instanceof Future))
				annotations.add(annotation);
		}
		
		declarationStatement.setAnnotations(annotations);
		modifyWithTaskIDReplacement(varAccess);
	}
	
	/*
	 * Modifies future variables that are declared before the declaration of this collection object; therefore they
	 * are already processed. Their variable syntax is changed from <varName> to <varNameTaskID>.getReturnResult();
	 * This method changes <varNameTaskID>.getReturnResult() to <varNameTaskID>
	 * 
	 * For example:
	 * myList.add(varNameTaskID.getReturnResult()); to myList.add(varNameTaskID);
	 */
	private void modifyWithTaskIDReplacement(CtVariableAccess<?> varAccess){
		if(!(varAccess.getParent() instanceof CtInvocation<?>))
			return;
		String varName = varAccess.toString();
		varName = APTUtils.getOrigName(varName);
		varName = varName.trim();
		varName = APTUtils.getTaskIDName(varName);
		CtVariableReference varRef = thisFactory.Core().createFieldReference();
		varRef.setSimpleName(varName);
		varAccess.setVariable(varRef);
	}
	
	/*
	 * Processes every method invocation that is used as an argument in invocations
	 * made on the collection wrapper. 
	 * That is, it sends invocations one by one to the 'modifyWithInvocation' method.
	 */
	private void modifyInvocationExpressions(){
		Set<CtStatement> statements = invocationArguments.keySet();
		for(CtStatement statement : statements){
			int annotatedInvocations = 0;
			List<CtInvocation<?>> invocations = invocationArguments.get(statement);
		
			for(CtInvocation<?> invocation : invocations){
				if(hasTaskAnnotation(invocation)){
					annotatedInvocations++;
				}			
			}
			modifyWithInvocation(annotatedInvocations, statement, invocations);
		}
	}
	
	/*
	 * Creates a future variable declaration for the method that is invoked (e.g., "foo(a) in myList.add(foo(a))")
	 * and alters that argument accordingly (i.e., changes that to the corresponding TaskID object), iff that method
	 * call is the only argument; otherwise the program blocks until the result is back for that asynchronous task. 
	 * This is the case only when the method is annotated with @Task. 
	 */
	private void modifyWithInvocation(int annotatedInvocations, CtStatement parentStatement, List<CtInvocation<?>> invocations){
		for(CtInvocation<?> invocation : invocations){
			if(hasTaskAnnotation(invocation)){
				newLocalVariableIndex++;
				CtLocalVariable<?> newLocalVariable = thisFactory.Core().createLocalVariable();

				CtTypeReference newVariableType = invocation.getExecutable().getType();
				newLocalVariable.setType(newVariableType);
			
				String newVariableName = invocation.getExecutable().getSimpleName() + "_" + newLocalVariableIndex;
				String newVariableTaskIDName = "";
				
				if(!(invocation.getParent() instanceof CtInvocation<?>))
					newVariableTaskIDName = APTUtils.getTaskIDName(newVariableName)+".getReturnResult()";
				else
					newVariableTaskIDName = APTUtils.getTaskIDName(newVariableName);
				
				newLocalVariable.setSimpleName(newVariableName);	
				newLocalVariable.setDefaultExpression((CtExpression)invocation);
				
				StatementMatcherFilter<CtStatement> filter = new StatementMatcherFilter<CtStatement>(parentStatement);
				CtBlock<?> parentBlock = thisAnnotatedElement.getParent(CtBlock.class);
				parentBlock.insertBefore(filter, newLocalVariable);
				
				InvocationProcessor processor = new InvocationProcessor(thisFactory, thisFutureAnnotation, newLocalVariable);
				processor.process();
				
				CtVariableAccess<?> varAccess = thisFactory.Core().createVariableRead();
				CtVariableReference varReference = thisFactory.Core().createFieldReference();
				varReference.setSimpleName(newVariableTaskIDName);
				varAccess.setVariable(varReference);
				invocation.replace(varAccess);
			}
		}		
	}
	
	
//----------------------------------------------------HELPER METHODS---------------------------------------------------
		
	private boolean hasTaskAnnotation(CtInvocation<?> methodInovcation){
		List<CtAnnotation<? extends Annotation>> annotations = methodInovcation.getExecutable().getAnnotations();
		for(CtAnnotation<? extends Annotation> anno : annotations){
			Annotation annotation = anno.getActualAnnotation();
			if(annotation instanceof Task){
				return true;
			}
		}
		return false;
	}
	
	protected void printVariableAccessArguments(){
		System.out.println("Printing Variable Access Expressions");
		for(CtVariableAccess<?> variableAccess : variableAccessArgumentsToBeProcessed){
			System.out.println("----------------------------------------------------");
			printVarAccessComponents(variableAccess);
			System.out.println("----------------------------------------------------");
		}
	}
	
	protected void printStatementInvocations(){
		System.out.println("Printing Invocations That Were Found In Statements");
		if(invocationsToBeProcessed.size() == 0){
			System.out.println("no invocations to be processed");
			return;
		}
		for(CtInvocation<?> invocation : invocationsToBeProcessed){		
			System.out.println("----------------------------------------------------");
			System.out.println(invocation);
			System.out.println("----------------------------------------------------");		
		}
	}
	
	/*
	 * Indicates if the initial statement being inspected is an invocation on the collection wrapper,
	 * or if an invocation on the collection wrapper has been encountered within a statement or expression.
	 */
	private boolean isInsideCollection(){
		if(insideCollectionStatement || (encounteredInvocationArguments != 0))
			return true;
		return false;
	}
	
	private void insertStatementBeforeDeclaration(){
		System.out.println("Inserting statement before collection declaration");
		CtInvocation invokePT = thisFactory.Core().createInvocation();
		CtExecutableReference executable = thisFactory.Core().createExecutableReference();
		CtCodeSnippetExpression invocationTarget = thisFactory.Core().createCodeSnippetExpression();
		CtCodeSnippetExpression invocationArgument = thisFactory.Core().createCodeSnippetExpression();
		
		executable.setSimpleName("processingInParallel");
		invocationTarget.setValue(APTUtils.getParaTaskSyntax());
		invocationArgument.setValue("true");
		List<CtExpression<?>> arguments = new ArrayList<>();
		arguments.add(invocationArgument);
		
		invokePT.setExecutable(executable); //processingInParallel
		invokePT.setTarget(invocationTarget);//pt.runtime.ParaTask
		invokePT.setArguments(arguments);//true
		
		CtBlock<?> parentBlock = thisAnnotatedElement.getParent(CtBlock.class);
		StatementMatcherFilter<CtStatement> filter = new StatementMatcherFilter<CtStatement>(thisAnnotatedElement);
		parentBlock.insertBefore(filter, invokePT);
	}
	
	private void changeDeclarationName(){
		String newName = APTUtils.getTaskName(thisElementName);
		thisAnnotatedElement.setSimpleName(newName);
	}
	
	private void insertStatementAfterDeclaration(){
		String newTypeString = getCollectionType() + "<" + thisCollectionGenericType + ">";
		CtTypeReference newType = thisFactory.Core().createTypeReference();
		newType.setSimpleName(newTypeString);
		List<CtTypeReference> typeCast = new ArrayList<>();
		typeCast.add(newType);
		
		CtLocalVariable<?> castedColleciton = thisFactory.Core().createLocalVariable();
	
		CtVariableAccess varAccess = thisFactory.Core().createVariableRead();
		
		CtVariableReference varRef = thisFactory.Core().createFieldReference();
		varRef.setSimpleName(APTUtils.getTaskName(thisElementName));
		varAccess.setVariable(varRef);
		varAccess.setTypeCasts(typeCast);
		
		castedColleciton.setType(newType);
		castedColleciton.setSimpleName(thisElementName);
		castedColleciton.setDefaultExpression(varAccess);
		
		CtBlock<?> parentBlock = thisAnnotatedElement.getParent(CtBlock.class);
		StatementMatcherFilter<CtStatement> filter = new StatementMatcherFilter<CtStatement>(thisAnnotatedElement);
		parentBlock.insertAfter(filter, castedColleciton);
	}	
	
	private String getCollectionType(){
		String currentType = thisCollectionType.toString();
		if(currentType.contains("List"))
			return APTUtils.getListWrapperSyntax();
		else if (currentType.contains("Set"))
			return APTUtils.getSetWrapperSyntax();
		else if (currentType.contains("Map"))
			return APTUtils.getMapWrapperSyntax();
		else
			return APTUtils.getCollecitonWrapperSyntax();
	}		

	/*
	 * Finds all variable-access arguments, as well as the method calls that are used
	 * within the invocation on a collection wrapper, for further processing.
	 */
	private void findArgumentsToProcess(CtExpression<?> expression){
		/*
		 * if the expression is a variable-access, and it is within an invocation on the collection, 
		 * it needs to be remembered for further inspections. 
		 */
		if(expression instanceof CtVariableAccess<?>){
			if(isInsideCollection()){
				CtVariableAccess<?> variableAccess = (CtVariableAccess<?>) expression;
				variableAccessArgumentsToBeProcessed.add(variableAccess);
			}
		}
		
		else if (expression instanceof CtBinaryOperator<?>){
			CtBinaryOperator<?> binaryOperator = (CtBinaryOperator<?>) expression;
			findArgumentsToProcess(binaryOperator.getLeftHandOperand());
			findArgumentsToProcess(binaryOperator.getRightHandOperand());
		}
		
		else if(expression instanceof CtInvocationImpl<?>){
			CtInvocationImpl<?> invocation = (CtInvocationImpl<?>) expression;
			boolean thisCollectionInvocation = false;
			
			if(isInsideCollection()){
				/*
				 * There might be methods inside the collection invocation, that are supposed
				 * to be processed in parallel (i.e., with @Task annotation). So, given that 
				 * a method call inside collection invocation (e.g., list.add(foo(3));) is not
				 * targeting the collection itself, we add it for further inspections. 
				 */
				CtExpression<?> target = invocation.getTarget();
				
				if(target != null){//if method call is on an object
					if(!targetIsThisElement(target)){
						//System.out.println("invocation: " + invocation + " added");
						invocationsToBeProcessed.add(invocation);
					}
				}
				else{
					//System.out.println("invocation: " + invocation + " added");
					invocationsToBeProcessed.add(invocation);
				}
			}
			
			CtExpression<?> target = invocation.getTarget();
			if(target != null){
				if(targetIsThisElement(target)){
					encounteredInvocationArguments++; //we use a global counter because collectionInvocations may be nested. 
					thisCollectionInvocation = true; //every cycle of recursion uses its own boolean flag!
				}
			}
			
			List<CtExpression<?>> arguments = invocation.getArguments();
			for(CtExpression<?> argument : arguments){
				findArgumentsToProcess(argument);
			}
			
			/*
			 * if we deduct the global value for every invocation, even those invocation that are not 
			 * on the current collection, then we loose track of the number of nested collection invocations.  
			 */
			if(thisCollectionInvocation)
				encounteredInvocationArguments--;
		}
		else if (expression instanceof CtUnaryOperator<?>){
			CtUnaryOperator<?> unaryOperator = (CtUnaryOperator<?>) expression;
			findArgumentsToProcess(unaryOperator.getOperand());
		}
		else if (expression instanceof CtArrayAccess<?, ?>){
			CtArrayAccess<?, ?> arrayAccess = (CtArrayAccess<?, ?>) expression;
			findArgumentsToProcess(arrayAccess.getIndexExpression());
		}		
	}
	
	private boolean targetIsThisElement(CtExpression<?> target){
		String targetString = target.toString();
		String[] targetElements = targetString.split("\\.");
		if((targetElements[targetElements.length-1].trim()).equals(thisElementName)){
			return true;
		}
		return false;
	}
//----------------------------------------------------HELPER METHODS---------------------------------------------------
}
