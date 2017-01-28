package com.smallscript.symboltable.listofhashtable;

public class SymbolTable {
	private Table globalTable;
	private Table currentTable;
	private int currentLocalScopeStackId = 0;
	
	public SymbolTable(){
		globalTable = new Table();
		currentTable = globalTable;
	}
	
	public void reset(){
		currentLocalScopeStackId = 0;
	}
	
	public boolean contains(String id){
		return currentTable.contains(id);
	}
	
	public Symbol get(String id){
		return currentTable.get(id);
	}
	
	public void set(String id, Symbol symbol){
		currentTable.put(id, symbol);
	}
}
