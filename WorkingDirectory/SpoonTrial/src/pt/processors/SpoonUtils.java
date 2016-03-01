package pt.processors;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.annotations.Future;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtStatement;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtVariable;

public class SpoonUtils {

	private static Map<String, String> primitiveMap = new HashMap<String, String>();

	static {
		primitiveMap.put("int", "Integer");
		primitiveMap.put("double", "Double");
	}

	public static String getType(String type) {
		String newType = primitiveMap.get(type);
		if (newType != null)
			return newType;
		else
			return type;
	}

	public static void replace(CtBlock<?> block, CtStatement old,
			CtStatement newStatement) {
		List<CtStatement> ls = block.getStatements();
		List<CtStatement> newLs = new ArrayList<CtStatement>();
		boolean replaced = false;
		for (CtStatement s : ls) {
			if (s == old) {
				newLs.add(newStatement);
				replaced = true;
			} else
				newLs.add(s);
		}

		if (!replaced)
			throw new IllegalArgumentException("cannot find " + old
					+ " in the given block!");

		block.setStatements(newLs);
	}

	/**
	 * Finds the occurrence of a future variable within the block
	 * of that future variable. However, this only finds the first
	 * occurrence of that future variable, and not the others.
	 * @param block
	 * @param element
	 * @return
	 */
	public static List<CtStatement> findVarAccessOtherThanFutureDefinition(CtBlock<?> block,
			CtLocalVariable<?> element) {
		List<CtStatement> blockStatements = block.getStatements();
		List<CtStatement> statementsWithThisFutureVariable = new ArrayList<>();
		boolean foundDef = false;
		
		String varName = element.getSimpleName();
		
		for (CtStatement statement : blockStatements) {
			String statementString = statement.toString();
			System.out.println("statement: " + statementString + " and variable name: " + varName);
			
			if (!foundDef){
				if (statement.equals(element))
					foundDef = true;
			}
			
			else {
				String regex = "\\b" + varName + "\\b";
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(statementString);
				if(matcher.find()){
					statementsWithThisFutureVariable.add(statement);
				}
			}
		}
//			if (!foundDef) {
//				if (s == varDefinition) {
//					foundDef = true;
//				}
//			} else {
//				String code = s.toString();
//				//System.out.println("findVarAccessStatement check: \n" + code);
//				
//				Pattern regex = Pattern.compile(
//					    "\\b     # word boundary\n" +
//					    varName+"# var name\n" +
//					    "\\b     # word boundary\n" +
//					    "(?!     # Lookahead assertion: Make sure there is no...\n" +
//					    " \\s*   # optional whitespace\n" +
//					    " \\(    # opening parenthesis\n" +
//					    ")       # ...at this position in the string", 
//					    Pattern.COMMENTS);
//				
//				if (regex.matcher(code).find() && !hasAnnotation(s, Future.class)) {
//					statementsWithThisFutureVariable.add(s);
//				}
//			}
//		}

		return statementsWithThisFutureVariable;
	}
	
	public static boolean hasAnnotation(CtStatement s, Class<?> clazz) {
		Set<CtAnnotation<? extends Annotation>> ats = s.getAnnotations();
		for(CtAnnotation<? extends Annotation> a : ats) {
			if(clazz.isInstance(a.getActualAnnotation()))
				return true;
		}
		return false;
	}
	
	/**
	 * Finds out whether an argument used within an invocation is itself a
	 * future variable. 
	 * 
	 * @param element
	 * @param argName
	 * @return
	 */
	public static boolean isFutureArgument(CtVariable<?> element, String argName) {
		if (!argName.matches("[a-zA-Z0-9_]+")) {
			return false;
		}
		
		CtBlock<?> block = (CtBlock<?>)element.getParent();
		List<CtStatement> ls = block.getStatements();
		for(CtStatement s : ls) {
			if(s == element)
				break;
			
			if(s instanceof CtFutureDefCodeSnippetStatement) {
				CtFutureDefCodeSnippetStatement futureDef = (CtFutureDefCodeSnippetStatement)s;
				if(futureDef.getOrignalVarName().equals(argName))
					return true;
			}
		}		
		return false;
	}
	
	public static String getTaskIDName(String name) {
		return "__" + name + "TaskID__";
	}
	
	public static String getOrigName(String taskIDName) {
		if(!taskIDName.startsWith("__") || !taskIDName.endsWith("__"))
			throw new IllegalArgumentException("not a valid future name: " + taskIDName);
		return taskIDName.substring(2, (taskIDName.length() - "TaskID__".length()));
	}
	
	public static String getLambdaArgName(String name) {
		return "__" + name + "Arg__";
	}
	
	public static String getTaskName(String name){
		return "__" + name + "Task__";
	}
	
}
