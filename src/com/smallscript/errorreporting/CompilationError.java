package com.smallscript.errorreporting;


/*
 * Represent An Error With Its Line Number
 */
public class CompilationError {
	
	private final String msg;
	private int lineNumber = -1;
	
	public CompilationError(String msg, int lineNumber){
		this.msg = msg;
		this.lineNumber = lineNumber;
	}
	
	public CompilationError(String msg){
		this.msg = msg;
	}
	
	public String getMessage(){
		return msg;
	}
	
	public int getLineNumber(){
		return lineNumber;
	}
	
	@Override public String toString(){
		return msg;
	}

}

