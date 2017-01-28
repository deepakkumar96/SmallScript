package test.symboltable;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.smallscript.symboltable.*;
import com.smallscript.type.Function;
import com.smallscript.type.Type;
import com.smallscript.type.Var;

public class FunctionTest {

	SymbolTable smt;
	
	@Before
	public void setUp() throws Exception {
		smt = new SymbolTable();
	}

	@Test
	public void test() {
		
		try{
			Function addF = new Function("addF", Type.INT, new Type[]{Type.CHAR, Type.INT});
			smt.put(addF.getName(), addF);
			
		}catch(DuplicateElementException dex){
			System.out.println(dex.getMessage());
		}
		
		try{
			Function addF = new Function("addF", Type.INT, new Type[]{Type.INT, Type.INT});
			smt.put(addF.getName(), addF);
			//addF.isLocal();
		}catch(DuplicateElementException dex){
			System.out.println(dex.getMessage());
		}
		
		try{
			smt.put("varA", new Var("varA", 1, Type.INT, smt.getCurrentNestingDepth()));
			
		}catch(DuplicateElementException dex){
			System.out.println(dex.getMessage());
		}
		
		try{
			smt.put("varB", new Var("varA", 1, Type.INT, smt.getCurrentNestingDepth()));
			
		}catch(DuplicateElementException dex){
			System.out.println(dex.getMessage());
		}
	}

}
