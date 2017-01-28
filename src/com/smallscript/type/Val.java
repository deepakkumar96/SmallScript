package com.smallscript.type;

public class Val extends VarDeclaration{ 
	
	public Val(String name, Object value, Type type, int depth){
		super(name, value, type, depth);
	}
	
	@Override public String toString(){
		return "(*)" + name + "(" + value + ", " + getNestingDepth() + ", i: " + stackId + ")" + " : " + type;
	}
	
}
