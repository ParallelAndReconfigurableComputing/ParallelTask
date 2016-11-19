package sp.processors;

import java.lang.annotation.Annotation;
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
	protected List<String> listOfTypesForReduction = null;
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
	
	protected void breakDownTypesForReduction(){
		String typeString = thisElementType.toString();
		typeString = APTUtils.getType(typeString);
		
		if(typeString.contains("<") && typeString.contains(">")){
			processAggregateType(typeString);
		}else{
			if(matchTypeWithReduction(typeString, thisElementReductionString))
				processPrimitiveType(typeString);
		}
	}
	
	private boolean matchTypeWithReduction(String type, String reduction){
		if(typeToAvailableReductions.containsKey(type)){
			Map<String, String> supportedReductions = typeToAvailableReductions.get(type);
			if(supportedReductions.containsKey(reduction))
				return true;
		}
		return false;
	}
		
	private void processPrimitiveType(String typeString){

	}
	
	private void processAggregateType(String typeString){
		
	}
	
//----------------------------------------------------HELPER METHODS---------------------------------------------------
	
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
	private void initializeReductionMap(){
		//values added to the lists are all small case, because user input is turned into lower-case
		//when processed, to reduce to chance of incompatibility.
		Map<String, String> booleanMap = new HashMap<>();
		booleanMap.put("and", "BooleanAND"); booleanMap.put("or", "BooleanOR"); booleanMap.put("xor", "BooleanXOR");
		booleanMap.put("bitand", "BooleanBitwiseAND"); booleanMap.put("bitor", "BooleanBitwiseOR"); booleanMap.put("bitxor", "BooleanBitwiseXOR");
		
		Map<String, String> byteMap = new HashMap<>();
		byteMap.put("bitand", "ByteBitwiseAND"); byteMap.put("bitor", "ByteBitwiseOR"); byteMap.put("bitxor", "ByteBitwiseXOR");
		
		Map<String, String> doubleMap = new HashMap<>();
		doubleMap.put("max", "DoubleMaximum"); doubleMap.put("min", "DoubleMinimum"); doubleMap.put("mult", "DoubleMultiplication"); doubleMap.put("sum", "DoubleSum");
		
		Map<String, String> floatMap = new HashMap<>();
		floatMap.put("max", "FloatMaximum"); floatMap.put("min", "FloatMinimum"); floatMap.put("mult", "FloatMultiplication"); floatMap.put("sum", "FloatSum");
		
		Map<String, String> integerMap = new HashMap<>();
		integerMap.put("max", "IntegerMaximum"); integerMap.put("min", "IntegerMinimum"); integerMap.put("mult", "IntegerMultiplication"); integerMap.put("sum", "IntegerSum");
		integerMap.put("bitand", "IntegerBitwiseAND"); integerMap.put("bitor", "IntegerBitwiseOR"); integerMap.put("bitxor", "IntegerBitwiseXOR");
		
		Map<String, String> longMap = new HashMap<>();
		longMap.put("max", "LongMaximum"); longMap.put("min", "LongMinimum"); longMap.put("mult", "LongMultiplication"); longMap.put("sum", "LongSum");
		longMap.put("bitand", "LongBitwiseAND"); longMap.put("bitor", "LongBitwiseOR"); longMap.put("bitxor", "LongBitwiseXOR");
		
		Map<String, String> shortMap = new HashMap<>();
		shortMap.put("max", "ShortMaximum"); shortMap.put("min", "ShortMinimum"); shortMap.put("multi", "ShortMultiplication"); shortMap.put("sum", "ShortSum");
		shortMap.put("bitand", "ShortBitwiseAND"); shortMap.put("bitor", "ShortBitwiseOR"); shortMap.put("bitxor", "ShortBitwiseXOR");
		
		Map<String, String> collectionMap = new HashMap<>();
		collectionMap.put("intersection","CollectionIntersection"); collectionMap.put("join", "CollectionJoin"); collectionMap.put("union", "CollectionUnion");
		
		Map<String, String> listMap = new HashMap<>();
		listMap.put("intersection", "ListIntersection"); listMap.put("join", "ListJoin"); listMap.put("union", "ListUnion");
		
		Map<String, String> setMap = new HashMap<>();
		setMap.put("intersection", "SetIntersection"); setMap.put("union", "SetUnion");
		
		Map<String, String> mapMap = new HashMap<>();
		mapMap.put("intersection", "MapIntersection"); mapMap.put("union", "MapUnion");
		
		//......................................................................................................
		
		typeToAvailableReductions = new HashMap<>();
		typeToAvailableReductions.put("Boolean", booleanMap);
		typeToAvailableReductions.put("Byte", byteMap);
		typeToAvailableReductions.put("Collection", collectionMap);
		typeToAvailableReductions.put("Double", doubleMap);
		typeToAvailableReductions.put("Float", floatMap);
		typeToAvailableReductions.put("Integer", integerMap);
		typeToAvailableReductions.put("List", listMap);
		typeToAvailableReductions.put("Long", longMap);
		typeToAvailableReductions.put("Map", mapMap);
		typeToAvailableReductions.put("Set", setMap);
		typeToAvailableReductions.put("Short", shortMap);
	}
	
}
