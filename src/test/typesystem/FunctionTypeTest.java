package test.typesystem;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.smallscript.type.*;

public class FunctionTypeTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		Function f1 = new Function("f1", Type.INT, new Type[]{Type.INT, Type.CHAR});
		
		Declaration f2 = new Function("f1", Type.INT, new Type[]{Type.INT, Type.CHAR});
		
		Assert.assertEquals(f1, f2);
		
	}

}
