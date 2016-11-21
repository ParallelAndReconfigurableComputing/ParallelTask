package sp.processors;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import pu.RedLib.ShortSum;

import sp.annotations.Future;
import sp.processors.APTUtils.ExpressionRole;
import spoon.reflect.factory.Factory;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtStatement;
import spoon.reflect.code.CtVariableAccess;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.reference.CtTypeReference;

/**
 * This abstract class must be implemented by every annotation processor. 
 * 
 * @author Mostafa Mehrabi
 *
 */
public abstract class PtAnnotationProcessor {
	
	protected CtLocalVariable<?> thisAnnotatedElement = null;
	protected String thisElementName = null;
	protected CtTypeReference<?> thisElementType = null;
	protected Factory thisFactory = null;
	protected List<ASTNode> listOfContainingNodes = null;
	protected List<TypeElement> listOfTypesForReduction = null;
	protected String thisElementReductionString = null;
	private Map<String, Map<String, String>> typeToAvailableReductions = null;
	
	protected PtAnnotationProcessor(){
		initializeReductionMap();
		
	}
	
	/**
	 * each sub-class implements this method, as a starting
	 * point for processing the annotated elements
	 */
	public abstract void process();
		
	/*
	 * each sub-class modifies statements in its own way!
	 */
	protected abstract void modifySourceCode(); 
	
	protected void processReduction(){
		listOfTypesForReduction = new ArrayList<>();
		breakDownElementType(thisElementReductionString);
		breakDownReductionString();
		if(isRedLibReduction()){
			processRedLibReduction();
		}
		else if(isDeclaredReductionObject()){
			processDeclaredReductionObject();
		}
		else if(isUserDefinedReductionClass()){
			processUserDefinedReductionClass();
		}
	}
	
//----------------------------------------------------HELPER METHODS---------------------------------------------------
	
	private boolean matchTypeWithReduction(String type, String reduction){
		if(typeToAvailableReductions.containsKey(type)){
			Map<String, String> supportedReductions = typeToAvailableReductions.get(type);
			if(supportedReductions.containsKey(reduction))
				return true;
		}
		return false;
	}
	
	/*
	 * Breaks down return type to see if it requires nested reductions.
	 */
	private void breakDownElementType(String returnType){
		String type = "";
		String generic = "";
		if(returnType.contains("<") && returnType.contains(">")){
			type = returnType.substring(0, returnType.indexOf("<"));
			generic = returnType.substring(returnType.indexOf("<"), (returnType.lastIndexOf(">")+1));
		}
		else{
			type = returnType;
			generic = "";
		}
		
		TypeElement obj = new TypeElement(type, generic);
		listOfTypesForReduction.add(obj);
		
		if(generic.contains(",") && type.equals("Map")){
			breakDownElementType(getNestedType(generic));
		}
	}
	
	private String getNestedType(String type){
		String nestedType = type.substring((type.indexOf(",")+1), type.lastIndexOf(">"));
		nestedType = nestedType.trim();
		return nestedType;
	}
	
	/*
	 * breaks down the reduction string provided in the annotation.
	 */
	private void breakDownReductionString(){
		
	}
	
	private boolean isRedLibReduction(){
		return false;
	}
	
	private boolean isDeclaredReductionObject(){
		return false;
	}
	
	private boolean isUserDefinedReductionClass(){
		return false;
	}
	
	private void processRedLibReduction(){
		
	}
	
	private void processDeclaredReductionObject(){
				
	}
	
	private void processUserDefinedReductionClass(){
		
	}
	
	private void processPrimitiveType(String typeString){

	}
	
	private void processAggregateType(String typeString){
		
	}
	
	protected CtStatement getParentInEnclosingBlock(CtExpression<?> expression){
		CtStatement parentStatement = expression.getParent(CtStatement.class);
		while(!(parentStatement.getParent() instanceof CtBlock))
			parentStatement = parentStatement.getParent(CtStatement.class);
		return parentStatement;
	}
	
	protected CtStatement getParentInEnclosingBlock(CtStatement statement){
		CtStatement parentStatement = statement;
		while(!(parentStatement.getParent() instanceof CtBlock))
			parentStatement = parentStatement.getParent(CtStatement.class);
		return parentStatement;
	}

	protected Future hasFutureAnnotation(CtStatement declarationStatement){
		//CtLocalVariable<?> declarationStatement = (CtLocalVariable<?>) statement;
		List<CtAnnotation<? extends Annotation>> annotations = declarationStatement.getAnnotations();
		for(CtAnnotation<? extends Annotation> annotation : annotations){
			Annotation actualAnnotation = annotation.getActualAnnotation();
			if(actualAnnotation instanceof Future){
				Future future = (Future) actualAnnotation;
				return future;
			}
		}
		return null;
	}
	
	protected void printIncludingExpressions(){
		for(ASTNode node : listOfContainingNodes){
			System.out.println("--------------------------------------------------------");
			CtStatement statement = node.getStatement();
			System.out.println("Inspecting statement: " + statement + ", type: " + statement.getClass());
						
			for(int index = 0; index < node.numberOfExpressions(); index++){
				CtExpression<?> expression = node.getExpression(index);
				ExpressionRole role = node.getExpressionRole(index);
				System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
				System.out.print("Expression: " + expression.toString());
				System.out.println(", role: " + role + ", class: " + expression.getClass());
			}					
		}
	}
	
	protected void printVarAccessComponents(CtVariableAccess<?> variableAccess){
		System.out.println("Variable: " + variableAccess.getVariable().toString());
	}
	
	protected void printInvocationComponents(CtInvocation<?> invocation){
		System.out.println("Signature: " + invocation.getExecutable().getSignature());
		System.out.println("label: " + invocation.getLabel());
		System.out.println("Arguments: " + invocation.getArguments());
		System.out.println("Executable: " + invocation.getExecutable());
		System.out.println("Target: " + invocation.getTarget());
		System.out.println("Type: " + invocation.getType());
	}
	
	/*
	 * each sub-class can implement this method,
	 * that allows printing the components of an annotated
	 * element, in order to help with identifying the components
	 */
	protected void printLocalVariableComponents(CtLocalVariable<?> localVariable){
		System.out.println("SimpleName: " + localVariable.getSimpleName());
		System.out.println("Class: " + localVariable.getClass().toString());
		System.out.println("Default Expression: " + localVariable.getDefaultExpression());
		System.out.println("Position: " + localVariable.getPosition().toString());
		System.out.println("Reference: " + localVariable.getReference().toString());
		System.out.println("Reference Type: " + localVariable.getReferencedTypes().toString());
		System.out.println("Type: " + localVariable.getType().toString());
	}	
	
	protected void printReductionMap(){
		
		System.out.println(" Current state of AnnotationParallelTask (APT) supports the following\n"
			+ " reductions for the following types:");
		
		for(Entry<String, Map<String, String>> entry : typeToAvailableReductions.entrySet()){
			String type = entry.getKey();
			System.out.println("Reductions supportef for type: " + type);
			Map<String, String> availableReductions = entry.getValue();
			for(Entry<String, String> innerEntry : availableReductions.entrySet()){
				String abbreviate = innerEntry.getKey();
				String reduction = innerEntry.getValue();
				
				System.out.println("|----------------------------------------------------");
				System.out.println("|word: " + abbreviate + " for: " + reduction);
			}
			System.out.println("|----------------------------------------------------");
		}
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
	}
	
	/*
	 * In order to facilitate usage for end-users, reduction words are shortened and abbreviated in the
	 * programming interface. The background process will map each of these words to its corresponding
	 * RedLib class name, based on the type of user-defined future variables. 
	 * Reductions must be commutative and associative. Abbreviated words are all small letters to reduce
	 * the possibility of typographic errors. In this effect, reductions are abbreviated as follows:
	 * 
	 *   bitand = BitwiseAND | bitor = BitwiseOR | bitxor = BitwiseXOR   | and = AND     | or = OR    | 
	 *   max = Maximum       | min = Minimum     | mult = Multiplication | min = Minimum | xor = XOR  |
	 *   union = Union       | join = Join       | intersection = Intersection
	 */
	@SuppressWarnings("unchecked")
	private void initializeReductionMap(){
		//values added to the lists are all small case, because user input is turned into lower-case
		//when processed, to reduce to chance of incompatibility.
		String booleanStr = "Boolean"; String byteStr = "Byte"; String collectionStr = "Collection";
		String doubleStr = "Double"; String floatStr = "Float"; String integerStr = "Integer"; String setStr = "Set";
		String listStr = "List"; String longStr = "Long"; String shortStr = "Short"; String mapStr = "Map";
		
		
		Map<String, String> bitWiseMap = new HashMap<>();
		bitWiseMap.put("bitand", "BitwiseAND");
		bitWiseMap.put("bitor",  "BitwiseOR" );
		bitWiseMap.put("bitxor", "BitwiseXOR");
		
		Map<String, String> logicMap = new HashMap<>();
		logicMap.put("and", "AND");
		logicMap.put("or",  "OR" );
		logicMap.put("xor", "XOR");
		
		Map<String, String> primitiveMap = new HashMap<>();
		primitiveMap.put("min",  "Minimum");
		primitiveMap.put("max",  "Maximum");
		primitiveMap.put("sum",  "Sum" 	  );
		primitiveMap.put("mult", "Multiplication");
		
		Map<String, String> aggregateMap = new HashMap<>();
		aggregateMap.put("union", "Union");
		aggregateMap.put("intersection", "Intersection");
		
		Map<String, String> collectionsMap = new HashMap<>();
		collectionsMap.put("join", "Join");

		typeToAvailableReductions = new HashMap<>();
		concatenate(booleanStr, bitWiseMap, logicMap);
		concatenate(byteStr, bitWiseMap);
		concatenate(collectionStr, aggregateMap, collectionsMap);
		concatenate(doubleStr, primitiveMap);
		concatenate(floatStr, primitiveMap);
		concatenate(integerStr, primitiveMap, bitWiseMap);
		concatenate(listStr, aggregateMap, collectionsMap);
		concatenate(longStr, primitiveMap, bitWiseMap);
		concatenate(mapStr, aggregateMap);
		concatenate(setStr, aggregateMap);
		concatenate(shortStr, primitiveMap, bitWiseMap);
	}
	
	@SuppressWarnings("unchecked")
	private void concatenate(String type, Map<String, String>... maps){
		Map<String, String> typeToReductions = new HashMap<>();
		for(Map<String, String> map : maps){
			for(Entry<String, String> entry : map.entrySet()){
				String abbreviation = entry.getKey();
				String reduction = entry.getValue();
				typeToReductions.put(abbreviation, type+reduction);
			}
		}
		typeToAvailableReductions.put(type, typeToReductions);
	}
	
	private class TypeElement{
		TypeElement(String type, String generic){
			reductionType = type;
			genericType = generic;
		}
		String reductionType = "";
		String genericType = "";
		String reductionClass = "";
	}
}
