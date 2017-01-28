package com.smallscript.symboltable;

public class Module {
	private String moduleName;
	
	public Module(String name){
		this.moduleName = name;
	}
	
	public String getName(){
		return moduleName;
	}
	
	public String getSimpleName(){
		return moduleName.substring(moduleName.lastIndexOf(".")+1);
	}
	
	@Override public boolean equals(Object obj){
		if(!(obj instanceof Module))
			return false;
		return this.moduleName.equals(((Module)obj).getName());
	}
	
	@Override public String toString(){
		return getName();
	}
}
