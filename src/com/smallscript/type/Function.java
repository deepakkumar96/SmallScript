package com.smallscript.type;

import java.util.Arrays;

import com.smallscript.util.Util;

public class Function extends Declaration{
	
	private Type[] parameters;
	
	public Function(String name, Type retType, Type[] parameters){
		super(name, retType);
		this.setParameters(parameters);
	}
	
	public Type[] getParameters(){
		return parameters;
	}
	
	/**
	 * Return false if either name of arguments are different
	 * else it return true
	 */
	@Override public boolean equals(Object o){
		//System.out.println("Testing Equality : " + this + ", " + o);
		if(!(o instanceof Function)) return false;
		Function that = (Function) o;
		
		if(!this.name.equals(that.name) ||
		   !Arrays.equals(this.parameters, that.parameters))
			return false;
		else
			return true;
	}
	
	/**
	 * Always return 0 as functions are always global
	 * @ return current nesting depths
	 */
	@Override public int getNestingDepth(){
		return 0;
	}

	public void setParameters(Type[] parameters) {
		this.parameters = parameters;
	}
	
	@Override public String toString(){
		return name+":"+type+((parameters != null)?Arrays.toString(parameters): "[]");
	}
	
	
}