package com.smallscript.symboltable.listofhashtable;
import com.smallscript.type.*;

/*
 * A Symbol represents a particular variable or function
 * with its type and access and value
 */
public class Symbol{}
/*
public class Symbol {
	private int id;
	private String name;
	private Object value;
	private IType itype;
	private Access access;
	private boolean isShared = false;
	
	public Symbol(int id, String name, Object Value){
		setId(id);
		setName(name);
		setValue(value);
	}
	
	public Symbol(int id, String name, Object value, IType itype){
		this(id, name, value);
		
		// A function is by-default shared whereas variable is not shared 
		isShared = (itype == IType.FUNCTION)? true: false;
	}
	
	public boolean isGlobal(){
		return access == Access.GLOBAL;
	}
	
	public boolean isLocal(){
		return !isGlobal();
	}
	
	public boolean isVar(){
		return itype == IType.VAR;
	}
	
	public boolean isVal(){
		return itype == IType.VAL;
	}
	
	public boolean isFunction(){
		return itype == IType.FUNCTION;
	}
	
	public boolean isShared(){
		return isShared == true;
	}
	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public IType getType() {
		return itype;
	}

	public void setType(IType type) {
		this.itype = type;
	}

	public Access getAccess() {
		return access;
	}

	public void setAccess(Access access) {
		this.access = access;
	}
}
*/