package test.typesystem;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Test;

import com.smallscript.type.*;
import com.smallscript.util.TypeConverter;
import java.util.*;

public class FunctionConversionTest {

	@Test
	public void test() throws UnSupportedTypeException{
		try{
			Assert.assertEquals(TypeConverter.classToType(String.class), Type.STRING);
			Assert.assertEquals(TypeConverter.classToType(int.class), Type.INT);
			Assert.assertEquals(TypeConverter.classToType(char.class), Type.CHAR);
			Assert.assertEquals(TypeConverter.classToType(int.class), Type.INT);
		}catch(UnSupportedTypeException uex){
			uex.printStackTrace();
		}
		Class<?>[] classes = new Class<?>[]{int.class, int.class};
		Type[] types = new Type[]{Type.INT, Type.INT};
		
		System.out.println(Arrays.toString(TypeConverter.classToType(classes)));
		
		Assert.assertEquals(Arrays.toString(TypeConverter.classToType(classes)),
						    Arrays.toString(types));
		
		Assert.assertEquals(Arrays.toString(TypeConverter.typeToClass(types)),
			    Arrays.toString(classes));
		
		Function ff = new Function("hello", Type.INT, new Type[]{Type.INT, Type.INT});
		System.out.println(ff);
		try{
			System.out.println((TypeConverter.fromJavaFunction(Math.class.getMethod("max", TypeConverter.typeToClass(types)))));
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

}
