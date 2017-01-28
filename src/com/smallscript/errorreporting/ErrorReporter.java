package com.smallscript.errorreporting;

import java.io.OutputStream;
import java.util.*;

public class ErrorReporter {
	
	private final List<CompilationError> errors = new LinkedList<>();
	
	public boolean containsError(){
		return errors.size() != 0;
	}
	
	public void dumpErrors(OutputStream out){
		for(CompilationError err: errors){
			System.err.println(err.getLineNumber() + ": " + err.getMessage());
		}
	}
	
	public List<CompilationError> getErrors(){
		return errors;
	}
	
	
	public void addError(CompilationError error){
		errors.add(error);
	}
	
}
