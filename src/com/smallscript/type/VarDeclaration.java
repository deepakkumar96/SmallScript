package com.smallscript.type;

public abstract class VarDeclaration extends Declaration{
	protected int stackId;
	protected int nestingDepth;
	protected Object value;
	public int ii; //just for testing
	
	public VarDeclaration(String name, Object value, Type type, int depth){
		super(name, type);
		this.setValue(value);
		this.setNestingDepth(depth);
	}
	
	@Override public String toString(){
		return name + "(" + value + ", " + getNestingDepth() + ", i: " + stackId + ")" + " : " + type;
	}
	
	public int getStackId() {
		return stackId;
	}

	public void setStackId(int stackId) {
		this.stackId = stackId;
	}

	@Override public int getNestingDepth() {
		return nestingDepth;
	}

	public void setNestingDepth(int nestingDepth) {
		this.nestingDepth = nestingDepth;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
	

}
