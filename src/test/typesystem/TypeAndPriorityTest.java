package test.typesystem;

import static org.junit.Assert.*;
import org.junit.Test;
import com.smallscript.util.*;
import com.smallscript.type.*;

public class TypeAndPriorityTest {

	@Test
	public void test() {
		//fail("Not yet implemented");
	}
	
	@Test
	public void testPriority(){
		
		assertEquals(0, TypeConverter.getTypePriority(Type.CHAR));
		assertEquals(2, TypeConverter.getTypePriority(Type.STRING));
	}
	
	@Test
	public void conversionTest(){
		
		System.out.println(TypeConverter.typeOf("24"));
		assertEquals(TypeConverter.typeOf("23"), Type.INT);
		assertEquals(TypeConverter.typeOf("Hello"), Type.STRING);
		assertEquals(TypeConverter.typeOf("235645"), Type.INT);
	}
	
}
