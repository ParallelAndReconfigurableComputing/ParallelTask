package sp.processors;

import java.util.ArrayList;
import java.util.List;

import sp.annotations.ParaTask;
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

public class PtParaTaskProcessor extends AbstractAnnotationProcessor<ParaTask, CtMethodImpl<?>> {

	ParaTask thisAnnotation = null;
	CtMethodImpl<?> thisAnnotatedMethod = null;
	String thisSchedulingPolicy = null;
	String thisNumberOfThreads = null;
	
	@Override
	public void process(ParaTask annotation, CtMethodImpl<?> method) {
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
