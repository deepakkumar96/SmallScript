package com.smallscript.symboltable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import com.smallscript.type.*;
import com.smallscript.util.TypeConverter;

public class SymbolTable {
	
	private Map<String, List<Declaration>> symbols;
	private Stack<Module> externalModules = new Stack<>(); // to store imported modules
	private int currentStackIndex = -1;
	private int prevStackIndex;
	private int currentNestingDepth = 0;
	
	public SymbolTable(){
		symbols = new HashMap<>();
	}
	
	
	/**
	 * It returns first element of key binding or throws NoSuchElementException
	 * Exception if binding to key is not available
	 * @param key
	 * @return declaration
	 * @throws NoSuchElementException
	 */
	public Declaration get(String id) throws NoSuchElementException{
		List<Declaration> bindings = symbols.get(id);
		if(bindings != null)
			return bindings.get(0);
		else{
			/*check inside modules
			Declaration externalDecl = getFieldFromImport(id);
			if(externalDecl != null)
				return externalDecl;
			else*/
			throw new NoSuchElementException();
		}
	}
	
	/**
	 * This Function takes a function and search through its binding
	 * and finds if given Function f is equal to any function in
	 * bindings if present then it returns that else throws Exception
	 * @param f as Function
	 * @return Function
	 * @throws NoSuchElementException
	 */
	public Declaration get(Function f) throws NoSuchElementException{
		List<Declaration> bindings = symbols.get(f.getName());
		if(bindings != null){
			for(Declaration decl: bindings){
				if(decl instanceof Function)
					if(decl.equals(f))
						return decl;
			}
		}
		/*else{
			Declaration externalDecl = getFunctionFromImport(f);
			if(externalDecl != null)
				return externalDecl;
		}*/
		throw new NoSuchElementException("Function " + f.getName() + " is not defined.");
	}
	
	public void put(String id, VarDeclaration symbol) throws DuplicateElementException{
		//System.out.println(symbols.get(id) + ", Put");
		List<Declaration> bindings = symbols.get(id);
		//System.out.println("Getting List");
		if(bindings != null){
			if(bindings.get(0).getNestingDepth() == currentNestingDepth)
				throw new DuplicateElementException(id + " is already declared in the current scope.");
			bindings.add(0, symbol);
		}
		else{
			List<Declaration> newBinding = new LinkedList<>();
			newBinding.add(0, symbol);
			symbols.put(id, newBinding);
		}
	}
	
	public void put(String id, Function symbol) throws DuplicateElementException{
		//System.out.println(symbols.get(id) + ", Put");
		List<Declaration> bindings = symbols.get(id);
		//System.out.println("Getting List");
		if(bindings != null){
			if(bindings.contains(symbol))
				throw new DuplicateElementException("Function " + symbol.getName() + " is already declared with same prototype.");
			else	
				bindings.add(0, symbol);
		}
		else{
			List<Declaration> newBinding = new LinkedList<>();
			newBinding.add(0, symbol);
			symbols.put(id, newBinding);
		}
	}
	
	public void createNewScope(){
		currentNestingDepth++;
	}
	
	public void exitCurrentScope(){
		/*symbols = symbols.entrySet()
			   .stream()
			   .map(e -> {
				   if(e.getValue().get(0).getNestingDepth() == currentNestingDepth){
					   e.getValue().remove(0);
				   }
				   return e;
			   })
			   .filter(e -> !e.getValue().isEmpty())
			   .collect(Collectors.toMap(e -> e.));*/
		Iterator<Map.Entry<String, List<Declaration>>> it = symbols.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String, List<Declaration>> e = it.next();
			if(e.getValue().get(0).getNestingDepth() == currentNestingDepth)
				e.getValue().remove(0);
			if(e.getValue().isEmpty())
				it.remove(); // remove the key if its bindings are empty
		}
		currentNestingDepth--;
	}
	
	
	
	public void dump(){
		System.out.println("\nBindings : ");
		for(Map.Entry<String, List<Declaration>> m : symbols.entrySet()){
			System.out.print(m.getKey() + " => ");
			m.getValue().forEach(e -> System.out.print(e+", "));
			System.out.println();
		}
		System.out.println("Imports : " + externalModules);
	}
	
	@Override public String toString(){
		return "SymbolsTable";
	}
	
	public void importModule(String moduleName) throws ClassNotFoundException{
		Class.forName(moduleName);
		externalModules.push(new Module(moduleName));
	}
	
	public Declaration getFieldFromImport(String id) throws NoSuchElementException{
		String[] idParts = id.split("\\.");
		Module module = null;
		for(Module m : externalModules){
			if(m.getName().equals(idParts[0]) || m.getSimpleName().equals(idParts[0])) //[0] -> class part of id
				module = m;
		}
		if(module != null){
			Class<?> moduleClass = null;
			try{
					moduleClass = Class.forName(module.getName()); //accessing class part
					Field field = moduleClass.getField(idParts[1]); //accessing field part
					field.setAccessible(true);
					return new Var(moduleClass.getSimpleName()+"."+field.getName(),
							   	   null,
							   	   TypeConverter.classToType(field.getType()),
							   	   0); // 0 => global var|val
			}
			catch(UnSupportedTypeException uex){
				System.out.println("UnSupportedtypeException");
			}
			catch(NoSuchFieldException nex){
				//System.out.println(id + " Not Found In " + moduleClass.getName());
				throw new NoSuchElementException(idParts[1] + " is not defined in " + idParts[0]);
			}
			catch(ClassNotFoundException ex){
				ex.printStackTrace();
			}
		}
		throw new NoSuchElementException("Undefined Module " + idParts[0]);
	}
	
	/**
	 * 
	 * @param f as Function
	 * @return Function if found in any of the module or throw Exception
	 * @throws NoSuchElementException
	 */
	public Declaration getFunctionFromImport(Function f) throws NoSuchElementException{
		String[] idParts = f.getName().split("\\.");
		Module module = null;
		for(Module m : externalModules){
			if(m.getName().equals(idParts[0]) || m.getSimpleName().equals(idParts[0])) //[0] -> class part of id
				module = m;
		}
		if(module != null){
			Class<?> moduleClass = null;
			try{
				moduleClass = Class.forName(module.getName());
				Class<?>[] fParams = TypeConverter.typeToClass(f.getParameters());
				Method method = moduleClass.getMethod(idParts[1], fParams);
				//System.out.println("M : " + method);
				/*
				return new Function(moduleClass.getSimpleName()+"."+method.getName(),
								    TypeConverter.classToType(method.getReturnType()),
								    TypeConverter.classToType(method.getParameterTypes()));*/
				return TypeConverter.fromJavaFunctionWithClassHeader(method); //(*Conversion Not necessary)converting Method to SmallScript Function
			}
			catch(UnSupportedTypeException uex){
				uex.printStackTrace();
				
			}
			catch(NoSuchMethodException nex){
				//System.out.println(f.getName() + " Not Found In " + moduleClass.getName());
				throw new NoSuchElementException(idParts[1] + " is not defined in " + idParts[0]);
			}
			catch(ClassNotFoundException ex){
				//ex.printStackTrace();
			}
		}
		throw new NoSuchElementException("Undefined module " + idParts[0]);
	}
	
	public int getCurrentNestingDepth(){
		return currentNestingDepth;
	}
	
	public int nextStackIndex(){
		return ++currentStackIndex;
	}
	
	public void resetStackIndex(){
		currentStackIndex = -1;
	}
	
	public boolean contains(String id){
		return true;//currentTable.contains(id);
	}
	
	public void saveStackIndex(){
		prevStackIndex = currentStackIndex;
	}
	
	public void restoreStackIndex(){
		currentStackIndex = prevStackIndex;
	}
	
}
