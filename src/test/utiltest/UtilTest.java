package test.utiltest;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.smallscript.type.Type;
import com.smallscript.util.Util;
import com.smallscript.symboltable.*;


public class UtilTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		System.out.println("Util Test");
		Type[] t1 = new Type[]{Type.INT, Type.CHAR};
		Type[] t2 = new Type[]{Type.INT, Type.INT};
		
		assertEquals(false, Util.isEquals(t1, new Type[]{}));
		assertEquals(false, Util.isEquals(t1, t2));
		
		assertEquals(true, Util.isEquals(t1, new Type[]{Type.INT, Type.CHAR}));
		assertEquals(true, Util.isEquals(t2, new Type[]{Type.INT, Type.INT}));
		new SymbolTable().nextStackIndex();
	}

}
