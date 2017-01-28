package com.smallscript.type;

public abstract class Declaration {
	protected String name;
	protected Type type;
	
	public Declaration(String name, Type type){
		this.setName(name);
		this.setType(type);
	}
	
	@Override public String toString(){
		return name + " : " + getNestingDepth();
	}
	
	/**
	 * 
	 * @return the nesting depth of declaration
	 */
	public abstract int getNestingDepth();

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(Type type) {
		this.type = type;
	}
	
	/**
	 * Return whether declaration is local or global
	 */
	public boolean isLocal(){
		return !isGlobal();
	}
	
	public boolean isGlobal(){
		return getNestingDepth() == 0;
	}
}
