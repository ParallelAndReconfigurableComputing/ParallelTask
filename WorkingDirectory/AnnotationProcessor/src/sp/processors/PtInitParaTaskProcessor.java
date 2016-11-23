package sp.processors;

import java.util.ArrayList;
import java.util.List;

import sp.annotations.InitParaTask;
import sp.annotations.TaskScheduingPolicy;
import spoon.processing.AbstractAnnotationProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtCodeSnippetExpression;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtExecutableReference;
import spoon.support.reflect.code.CtExpressionImpl;
import spoon.support.reflect.code.CtInvocationImpl;
import spoon.support.reflect.declaration.CtMethodImpl;

/**
 * This annotation processor, specifically processes the annotation for initializing ParaTask with 
 * user-specified scheduling policy and number of threads. If these attributes are not specified for
 * the annotation, the processor will consider the default values, which are <code>MixedScheduling</code>
 * for task scheduling policy and <b>the number of available processor</b> on a system for the number
 * of threads. 
 * @author Mostafa Mehrabi
 * @since  2016
 */
public class PtInitParaTaskProcessor extends AbstractAnnotationProcessor<InitParaTask, CtMethodImpl<?>> {

	InitParaTask thisAnnotation = null;
	CtMethodImpl<?> thisAnnotatedMethod = null;
	String thisSchedulingPolicy = null;
	String thisNumberOfThreads = null;
	
	@Override
	public void process(InitParaTask annotation, CtMethodImpl<?> method) {
		thisAnnotation = annotation;
		thisAnnotatedMethod = method;
		inspectAnnotation();
		modifySourceCode();		
	}
	
	private void inspectAnnotation(){
		TaskScheduingPolicy schedulingPolicy = thisAnnotation.schedulingPolicy();
		int numberOfThreads = thisAnnotation.numberOfThreads();
		
		thisSchedulingPolicy = APTUtils.getScheduleType(schedulingPolicy);
		
		if(numberOfThreads == 0){
			thisNumberOfThreads = APTUtils.getDefaultNumberOfThreads();
		}else{
			thisNumberOfThreads = Integer.toString(numberOfThreads);
		}
	}
	
	private void modifySourceCode(){
		CtBlock<?> methodBody = thisAnnotatedMethod.getBody();
		methodBody.insertBegin(createInitStatement());
	}
	
	private CtInvocationImpl<?> createInitStatement(){
		CtInvocationImpl<?> initStatement = (CtInvocationImpl<?>) getFactory().Core().createInvocation();
		
		CtExecutableReference executable = getFactory().Core().createExecutableReference();
		executable.setSimpleName(paraTaskInitPhrase());
		
		CtCodeSnippetExpression<?> target = getFactory().Core().createCodeSnippetExpression();
		target.setValue(APTUtils.getParaTaskSyntax());
		
		CtCodeSnippetExpression<?> scheduleType = getFactory().Core().createCodeSnippetExpression();
		scheduleType.setValue(thisSchedulingPolicy);
		
		CtCodeSnippetExpression<?> numberOfThreads = getFactory().Core().createCodeSnippetExpression();
		numberOfThreads.setValue(thisNumberOfThreads);
		
		List<CtExpression<?>> arguments = new ArrayList<>();
		arguments.add(scheduleType);
		arguments.add(numberOfThreads);
		
		initStatement.setExecutable(executable);
		initStatement.setTarget(target);
		initStatement.setArguments(arguments);
		return initStatement;
	}
	
	private String paraTaskInitPhrase(){
		return "init";
	}
}
