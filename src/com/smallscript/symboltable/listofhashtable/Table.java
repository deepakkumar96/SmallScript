package com.smallscript.symboltable.listofhashtable;
import java.util.*;

/*
 * A Table represent a scope such as global or a function scope
 * or even scope for looping and branching.
 */

public class Table {
	private Table parent; //a reference to parent scope if available
	private Map<String, Symbol> symbols = new HashMap<>();
	
	/*
	 * Searches for a identifier in its own scope as well
	 * as in its parent scopes.
	 * @param table
	 * @param id
	 * @return boolean
	 */
	public boolean contains(Table table, String id){
		if(table.symbols.containsKey(id))
			return true;
		else
			if(parent != null)
				return contains(parent, id); //recursive-call to check symbol in parent scopes
			else
				return false;
	}
	
	public boolean contains(String id){
		return symbols.containsKey(id);
	}
	
	public Symbol get(String id){
		return symbols.get(id);
	}
	
	public void put(String id, Symbol symbol){
		symbols.put(id, symbol);
	}
	
}	
