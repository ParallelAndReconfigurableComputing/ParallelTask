package pt.processors;

import spoon.reflect.Factory;
import spoon.support.reflect.code.CtCodeSnippetStatementImpl;

public class CtFutureDefCodeSnippetStatement extends CtCodeSnippetStatementImpl {

	private static final long serialVersionUID = 3423445619699391064L;

	private String orignalVarName;
	public CtFutureDefCodeSnippetStatement(String orignalVarName, Factory fac) {
		this.orignalVarName = orignalVarName;
		
		this.setFactory(fac);
	}
	
	public String getOrignalVarName() {
		return this.orignalVarName;
	}
}
