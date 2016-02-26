package pt.processors;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

	public static CtStatement findVarAccessOtherThanFutureDefinition(CtBlock<?> block,
			CtLocalVariable<?> varDefinition) {
		List<CtStatement> ls = block.getStatements();
		boolean foundDef = false;
		String varName = varDefinition.getSimpleName();
		for (CtStatement s : ls) {
			if (!foundDef) {
				if (s == varDefinition) {
					foundDef = true;
				}
			} else {
				String code = s.toString();
				//System.out.println("findVarAccessStatement check: \n" + code);
				
				Pattern regex = Pattern.compile(
					    "\\b     # word boundary\n" +
					    varName+"# var name\n" +
					    "\\b     # word boundary\n" +
					    "(?!     # Lookahead assertion: Make sure there is no...\n" +
					    " \\s*   # optional whitespace\n" +
					    " \\(    # opening parenthesis\n" +
					    ")       # ...at this position in the string", 
					    Pattern.COMMENTS);
				
				if (regex.matcher(code).find() && !hasAnnotation(s, Future.class)) {
					return s;
				}
			}
		}

		return null;
	}
	
	public static boolean hasAnnotation(CtStatement s, Class<?> clazz) {
		Set<CtAnnotation<? extends Annotation>> ats = s.getAnnotations();
		for(CtAnnotation<? extends Annotation> a : ats) {
			if(clazz.isInstance(a.getActualAnnotation()))
				return true;
		}
		return false;
	}
	
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
	
	public static String getFutureName(String name) {
		return "__" + name + "__";
	}
	
	public static String getOrigName(String futureName) {
		if(!futureName.startsWith("__") || !futureName.endsWith("__"))
			throw new IllegalArgumentException("not a valid future name: " + futureName);
		return futureName.substring(2, futureName.length() - 2);
	}
	
	public static String getLambdaArgName(String name) {
		return "__" + name + "__arg__";
	}
}
