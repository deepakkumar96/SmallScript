package test.symboltable;

import static org.junit.Assert.*;

import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.smallscript.symboltable.*;

import test.symboltable.MemberTestClass;

import com.smallscript.type.*;

public class MemberAccessTest {

	SymbolTable smt;
	
	@Before
	public void setUp() throws Exception {
		smt = new SymbolTable();
		smt.importModule("test.symboltable.MemberTestClass");
		smt.importModule("java.lang.Math");
		smt.dump();
	}

	@Test
	public void test() {
		
		Declaration d1 = null;
		try{
			d1 = smt.getFieldFromImport("MemberTestClass.i");
			System.out.println(d1);
		}catch(NoSuchElementException nex){
			System.out.println(nex.getMessage());
		}
		Assert.assertNotEquals(d1, null);
		
		Declaration f1 = null;
		try{
			Function fun = new Function("Math.max", Type.INT, new Type[]{Type.INT, Type.INT});
			f1 = smt.getFunctionFromImport(fun);
			System.out.println(f1);
		}catch(NoSuchElementException nex){
			System.out.println(nex.getMessage());
		}
		Assert.assertNotEquals(f1, null);
	}

}
