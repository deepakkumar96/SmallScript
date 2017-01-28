package test.symboltable;

import java.lang.reflect.Field;

import org.junit.Test;
import org.junit.Assert;

import com.smallscript.symboltable.*;
import com.smallscript.type.*;

public class SymbolTableScopeTest {
	
	SymbolTable smt = new SymbolTable();
	
	@Test
	public void testSimpleScope() throws DuplicateElementException{
		System.out.println("Adding Variable: ");
		
		smt.put("a", new Var("a", 10, Type.INT, smt.getCurrentNestingDepth()));
		smt.put("b", new Val("b", 20, Type.INT, smt.getCurrentNestingDepth()));
		
		smt.createNewScope();
			smt.put("c", new Var("c", 30, Type.INT, smt.getCurrentNestingDepth()));
			
			Declaration varA = smt.get("a");
			System.out.println(varA);
			Assert.assertEquals(smt.get("a").getNestingDepth(), 0);
			
			smt.createNewScope(); //loop
				smt.put("a", new Var("a", 40, Type.INT, smt.getCurrentNestingDepth()));
				smt.put("c", new Var("c", 50, Type.INT, smt.getCurrentNestingDepth()));
				smt.dump();
			smt.exitCurrentScope();
		
		smt.exitCurrentScope();
		//smt.put("c", new Var("c", 30, Type.INT, smt.getCurrentNestingDepth()));
		System.out.println(smt.get("b"));
			
			
		System.out.print("End : ");
		smt.dump();
		
		Assert.assertEquals(smt.getCurrentNestingDepth(), 0);
		try{
			smt.importModule("com.smallscript.type.VarDeclaration");
			smt.importModule("com.smallscript.util.TypeConverter");
			smt.importModule("java.lang.Integer");
		}catch(Exception ex){
			ex.printStackTrace();
		}
		smt.dump();
		System.out.println("Module Test:");
		System.out.println(smt.getFieldFromImport("VarDeclaration.ii"));
		Type[] ftypes = new Type[]{Type.INT, Type.INT};
		System.out.println("Function : " + smt.getFunctionFromImport(new Function("Integer.intValue", Type.INT, new Type[]{})));
		
	}
	
}
