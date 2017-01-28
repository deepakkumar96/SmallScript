package com.smallscript.type.keywords;

public class SmallScriptKeywords {
	
	private String[] keywords = {
		"var", "val", "if", "while", "loop", "end",
		"def", "break", "return", "print", "Int",
		"Char", "String"
	};
	
	public boolean contains(String id){
		for(String str: keywords)
			if(str.equals(id))
				return true;
		return false;
	}

}
