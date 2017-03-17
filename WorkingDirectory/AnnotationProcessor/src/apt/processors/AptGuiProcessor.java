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
		if(!validate()){
			return;
		}
		
		inspectAnnotation();
	}
	
	private void inspectAnnotation(){
		notifierFutures = thisAnnotation.notifiedBy();
		modifySourceCode();
	}
	
	private boolean validate(){
		//first check if the right-hand side of the annotated element is an invocation
		CtExpression<?> defaultExpression = thisAnnotatedElement.getDefaultExpression();
		if(!(defaultExpression instanceof CtInvocation<?>)){
			System.err.println("THE RIGHT-HAND SIDE OF THE DECLARAION " + thisAnnotatedElement 
					+ " CAN ONLY BE AN INVOCATION.");
			return false;
		}
		
		//second, ensure that there is no return value specified for the invocation
		CtTypeReference<?> elementType = thisAnnotatedElement.getType();
		if(!(elementType.toString().equals("Void"))){
			System.err.println("A HANDLER METHOD CANNOT HAVE A RETURN TYPE!");
			return false;
		}
				
		//if notifiers are specified for the GUI handler, ensure that they are all futures
		for(String notifier : notifierFutures){
			if(!APTUtils.isFutureVariable(thisAnnotatedElement, notifier)){
				System.err.println("THE PARAMETER " + notifier + " PROVIDED FOR @Gui IN " + thisAnnotatedElement.toString()
				  + "DOES NOT REPRESESNT A FUTURE!");
				return false;
			}
		}
		
		thisInvocation = (CtInvocation<?>) defaultExpression;
		invocationArguments = thisInvocation.getArguments();
		//if there is no argument in the invocation, then it is valid
		if(invocationArguments.size() == 0)
			return true;
		
		distinguishVariablesFromFuturesInArguments();
		//else if there are more than one future arguments, then it is not valid
		if(futureArguments.size() > 1){
			System.err.println("A GUI HANDLER MAY NOT ");
		}
		
		return false;
	}
	
	private void distinguishVariablesFromFuturesInArguments(){
		for(CtExpression<?> argument : invocationArguments){
			if(APTUtils.isFutureVariable(thisAnnotatedElement, argument.toString())){
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
