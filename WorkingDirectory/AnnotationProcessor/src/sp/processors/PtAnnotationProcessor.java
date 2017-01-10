package sp.processors;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import sp.annotations.Future;
import sp.processors.APTUtils.ExpressionRole;
import spoon.reflect.factory.Factory;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtCodeSnippetExpression;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtStatement;
import spoon.reflect.code.CtVariableAccess;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtVariable;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.reference.CtTypeReference;

/**
 * This abstract class must be implemented by every annotation processor. 
 * 
 * @author Mostafa Mehrabi
 * @since  2016
 */
public abstract class PtAnnotationProcessor {
	
	protected CtLocalVariable<?> thisAnnotatedElement = null;
	protected CtField<?> thisAnnotatedField = null;
	protected String thisElementName = null;
	protected CtTypeReference<?> thisElementType = null;
	protected Factory thisFactory = null;
	protected List<ASTNode> listOfContainingNodes = null;
	protected List<TypeElement> listOfTypesForReduction = null;
	protected String thisElementReductionString = "";
	protected String thisReductionObjectName = null;
	private Map<String, Map<String, String>> typeToAvailableReductions = null;
	
	protected PtAnnotationProcessor(){}
	
	/**
	 * each sub-class implements this method, as a starting
	 * point for processing the annotated elements
	 */
	public abstract void process();
		
	/*
	 * each sub-class modifies statements in its own way!
	 */
	protected abstract void modifySourceCode(); 
	
	/*
	 * declaring individual future variables cannot be global (i.e., they cannot be fields),
	 * therefore, only consider inserting this statement for local variable declarations. 
	 */
	protected List<CtStatement> getReductionStatements(String elementType, String idName){
		CtLocalVariable<?> reductionDeclaration = processReduction(elementType);
		if(reductionDeclaration == null)
			return null;
		
		List<CtStatement> reductionStatements = new ArrayList<>();
	
		reductionStatements.add(reductionDeclaration);
		reductionStatements.add(getReductionSettingStatement(idName));
		return reductionStatements;
	}
	
	protected CtLocalVariable<?> processReduction(String elementType){
		if(thisElementReductionString.isEmpty()){
			return null;
		}
		
		initializeReductionMap();
		listOfTypesForReduction = new ArrayList<>();
		
		breakDownElementType(elementType);
		breakDownReductionString();
		createReductionDeclarations(); 
		String finalReductionPhrase = createReductionPhrase(0); 
	//	System.out.println("final reduction string: " + finalReductionPhrase);
		CtLocalVariable<?> finalReductionDeclaration = createFinalReductionDeclaration(finalReductionPhrase);
		return finalReductionDeclaration;
	}
	
	protected CtInvocation<?> getReductionSettingStatement(String idName){
		CtInvocation<?> setReductionStatement = thisFactory.Core().createInvocation();
		CtCodeSnippetExpression reductionObject = thisFactory.Core().createCodeSnippetExpression();
		CtCodeSnippetExpression taskIDName = thisFactory.Core().createCodeSnippetExpression();
		CtExecutableReference   executable  = thisFactory.Core().createExecutableReference();
		
		List<CtExpression<?>> arguments = new ArrayList<>();
		reductionObject.setValue(thisReductionObjectName);
		taskIDName.setValue(idName);
		executable.setSimpleName(APTUtils.getSetReductionSyntax());
		setReductionStatement.setExecutable(executable);
		
		arguments.add(taskIDName);
		arguments.add(reductionObject);
		
		setReductionStatement.setArguments(arguments);
		return setReductionStatement;
	}
	
//----------------------------------------------------HELPER METHODS---------------------------------------------------
	
	/*
	 * Returns true if RedLib already supports the user-specified reduction.
	 * Note that, user-specified reduction string is converted to lower case
	 * since the reductions registered in the map are all lower case. This 
	 * offers more convenience for a user, since they don't have to worry about
	 * case sensitivity of the reduction names.  
	 * If the reduction is supported by RedLib, based on the reduction that is
	 * registered for an element, and the element's type, the corresponding RedLib
	 * reduction replaces the user-specified syntax for that element in the type
	 * hierarchy that is extracted by the program.
	 */
	private boolean matchTypeWithReduction(TypeElement element){
		String type = element.elementType;
		String reduction = element.reduction.toLowerCase();
		
		//in case the specified type is fully qualified e.g., java.util.Map
		String[] typeQualifiedPaths = type.split("\\.");
		type = typeQualifiedPaths[typeQualifiedPaths.length-1];
		
		if(typeToAvailableReductions.containsKey(type)){
			Map<String, String> supportedReductions = typeToAvailableReductions.get(type);
			if(supportedReductions.containsKey(reduction)){
				element.reduction = APTUtils.getRedLibPackageSyntax() + supportedReductions.get(reduction);
				element.declaredObject = false;
				return true;
			}
		}
		return false;
	}
	
	private CtLocalVariable<?> createFinalReductionDeclaration(String reductionPhrase){
		thisReductionObjectName = APTUtils.getTaskReductionName(thisElementName);
		TypeElement topElement = listOfTypesForReduction.get(0);
		
		String topElementReductionType = topElement.reduction;
		String topElementGenericType = topElement.genericType;
		String reductionType = topElementReductionType + topElementGenericType;
		
		CtCodeSnippetExpression defaultExpression = thisFactory.Core().createCodeSnippetExpression();
		defaultExpression.setValue(reductionPhrase);
		
		CtTypeReference reductionTypeRef = thisFactory.Core().createTypeReference();
		reductionTypeRef.setSimpleName(reductionType);
		
		CtLocalVariable<?> reductionDeclaration = thisFactory.Core().createLocalVariable();
		reductionDeclaration.setType(reductionTypeRef);
		reductionDeclaration.setSimpleName(thisReductionObjectName);
		reductionDeclaration.setDefaultExpression(defaultExpression);
		return reductionDeclaration;
	}
	
	/*
	 * Go through every reduction, and add corresponding declaration phrase. 
	 */
	private void createReductionDeclarations(){
		for(TypeElement element : listOfTypesForReduction){
			if(!isRedLibReduction(element)){
				if(isDeclaredReductionObject(element)){
					processDeclaredReductionObject(element);
				}
				
				else if(isUserDefinedReductionClass(element)){
					processUserDefinedReductionClass(element);
				}
			}
		}
	}
	
	/*
	 * Recursively creates the nested reduction phrases. 
	 */
	private String createReductionPhrase(int index){
		if(index >= listOfTypesForReduction.size())
			return "";
		TypeElement element = listOfTypesForReduction.get(index);
		return "new " + element.reduction + element.genericType + "(" 
				+ createReductionPhrase(index+1) + ")";
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
		
		type = APTUtils.getType(type);
		TypeElement obj = new TypeElement(type, generic);
		listOfTypesForReduction.add(obj);
		
		//in case the specified type is fully qualified e.g., java.util.Map
		String[] typeQualifiedPaths = type.split("\\.");
		type = typeQualifiedPaths[typeQualifiedPaths.length-1];
		
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
		try{
			if(!validateReductionPhrase(thisElementReductionString)){
				throw new Exception("ERROR OCCURED WHEN PROCESSING THE FOLLOWING"
						+ "	REDUCTION PHRASE: " + thisElementReductionString);
			}
			
			List<String> reductionPhrases = decomposeReductions(thisElementReductionString);
			if(reductionPhrases.size() != listOfTypesForReduction.size()){
				throw new Exception("MISMATCH BETWEEN THE NUMBER OF SPECIFIED REDUCTIONS, " + reductionPhrases.size() 
						+ ", AND THE NUMBER OF REDUCTIONS REQUIRED, " + listOfTypesForReduction.size() + ".");
			}
			
			int index = 0;
			for(TypeElement element : listOfTypesForReduction){
				element.reduction = reductionPhrases.get(index);
				index++;
			}
		}catch(Exception e){
			System.err.println("ERROR OCCURED WHILE PROCESSING REDUCTIONS:\n");
			e.printStackTrace();
		}
	}
	
	/*
	 * A nested reduction phrase is valid if nesting is done correctly. In this effect
	 * the number of right and left parentheses must be equal, and all right parentheses
	 * must occur consecutively. Thus a valid example is:
	 * union(intersection(sum))
	 * invalid examples are: (union(intersection(sum)) or (union(intersection)sum) 
	 */
	private boolean validateReductionPhrase(String phrase){
		if(!equalNumOfParanthesis(phrase)){
			System.err.println("Reduction phrase " + phrase + " is not valid!");
			System.err.println("The number of left and right parentheses are not equal");
			return false;
		}
		if(!consecutiveRightParanthesis(phrase)){	
			System.err.println("Reduction phrase " + phrase + " is not valid!");
			System.err.println("Incorrect nesting of reductions");
			return false;
		}
		return true;
	}
	
	private boolean equalNumOfParanthesis(String phrase){
		String tempPhrase = phrase;
		int numOfLeftPhrase = 0, numOfRightPhrase = 0;
		while(tempPhrase.contains("(")){
			numOfLeftPhrase++;
			tempPhrase = tempPhrase.substring((tempPhrase.indexOf("(")+1));
		}
		
		tempPhrase = phrase;
		while(tempPhrase.contains(")")){
			numOfRightPhrase++;
			tempPhrase = tempPhrase.substring((tempPhrase.indexOf(")")+1));
		}
	
		if(numOfLeftPhrase != numOfRightPhrase)
			return false;
		return true;
	}
		
	private boolean consecutiveRightParanthesis(String phrase){
		
		if(!phrase.contains("("))
			return true;
		
		if(phrase.contains(")") && (phrase.charAt(phrase.length()-1) !=')'))
			return false;
		
		String tempPhrase = phrase.substring((phrase.indexOf(")")+1));
		while(tempPhrase.contains(")")){
			if(tempPhrase.indexOf(")") != 0)
				return false;
			tempPhrase = tempPhrase.substring((tempPhrase.indexOf(")")+1));
		}
		return true;
	}
	
	/*
	 * Decomposes the reductions specified by the programmer in a hierarchy that 
	 * correspond them to the types in order. For example:
	 * Map<Strin, Map<String, Integer>> expects a reduction phrase like: union(intersection(sum))
	 * where reductions must correspond to types as follows:
	 * Map     <--> union
	 * Map     <--> intersection
	 * Integer <--> sum
	 */
	private List<String> decomposeReductions(String reduction){
		validateReductionPhrase(reduction);
		String[] reductionPhrases = reduction.split("\\(");
		for(int i = 0; i< reductionPhrases.length; i++){
			reductionPhrases[i] = reductionPhrases[i].replace(")", "");
			reductionPhrases[i] = reductionPhrases[i].trim();
		}
		
		List<String> reductions = new ArrayList<>();
		
		for(String phrase : reductionPhrases){
			if (!phrase.isEmpty())
				reductions.add(phrase);
		}
		
		return reductions;
	}
	
	private boolean isRedLibReduction(TypeElement element){
		if(!matchTypeWithReduction(element)){
			return false;
		}
		return true;
	}
	
	/*
	 * If the reduction object is already declared by a user, it
	 * needs to be the inner-most reduction, it cannot nest any other
	 * reduction, because it is an object.  
	 */
	private boolean isDeclaredReductionObject(TypeElement element){
		if(isLocalDeclaration(element.reduction))
			return true;
		else if (isFieldDeclaration(element.reduction))
			return true;
		return false;
	}
	
	private boolean isUserDefinedReductionClass(TypeElement element){
		return false;
	}
	
	private boolean isLocalDeclaration(String reductionName){
		if(thisAnnotatedElement != null){
			CtStatement declarationStatement = APTUtils.getDeclarationStatement(thisAnnotatedElement, reductionName);
			if(declarationStatement != null)
				return true;
		}
		return false;
	}
	
	private boolean isFieldDeclaration(String reductionName){
		if(thisAnnotatedField != null){
			CtVariable<?> declarationStatement = APTUtils.getDeclarationStatement(thisAnnotatedField, reductionName);
			System.out.println("Field column: " + thisAnnotatedField.getPosition().getColumn());
			System.out.println("Field end column: " + thisAnnotatedElement.getPosition().getEndColumn());
			System.out.println("Field line: " + thisAnnotatedElement.getPosition().getLine());
		}
		return false;
	}
	
	private void processDeclaredReductionObject(TypeElement element){
				
	}
	
	private void processUserDefinedReductionClass(TypeElement element){
		
	}
	
	private void processPrimitiveType(String typeString){

	}
	
	private void processAggregateType(String typeString){
		
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
			elementType = type;
			genericType = generic;
		}
		
		boolean declaredObject = false;
		String 	elementType = "";
		String 	genericType = "";
		String 	reduction = "";
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
	
}
