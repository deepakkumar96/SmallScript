package com.smallscript.symboltable;

public class DuplicateElementException extends Exception{

	private static final long serialVersionUID = -4741997342809735694L;
	
	public DuplicateElementException(){}
	
	public DuplicateElementException(String msg){
		super(msg);
	}
	
}
