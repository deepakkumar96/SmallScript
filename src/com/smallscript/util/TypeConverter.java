package com.smallscript.util;
import java.lang.reflect.Method;

import com.smallscript.type.*;

public class TypeConverter {
	public static String ii;
	
	/**
	 * Converts a java Class<?> to equivalent SmallScript Type
	 * else it throws UnsupportedTypeException
	 * @param classType
	 * @return
	 */
	public static Type classToType(Class<?> classType) throws UnSupportedTypeException{
		return toType(classType.getSimpleName());
	}
	
	public static Type toType(String strType) throws UnSupportedTypeException{
		switch(strType){
			case "int":
			case "Int": 
			case "Integer":
				return Type.INT;
			case "char":
			case "Char":
			case "Character":
				return Type.CHAR;
			case "String":
			case "java.lang.String":
				return Type.STRING;
			case "void":
			case "Void":
				return Type.VOID;
		}
		if(strType.endsWith("[]"))
			return Type.ARRAY;
		throw new UnSupportedTypeException(strType + " is not a valid type.");
	}
	
	public static Class<?> typeToClass(Type type){
		switch(type){
			case INT:
					return int.class;
			case CHAR: 
					return char.class;
			case STRING:
					return String.class;
			default:
				return null;
		}
	}
	
	public static String toByteCodeType(Type type){
		switch(type){
			case INT:
					return "I";
			case CHAR: 
					return "C";
			case STRING:
					return "Ljava/lang/String;";
			case VOID:
					return "V";
			default:
				return null;
		}
	}
	
	public static Type[] classToType(Class<?>[] types) throws UnSupportedTypeException{
		Type[] resultTypes = (types.length != 0)? new Type[types.length]: null;
		for(int i=0; i<types.length; ++i){
			resultTypes[i] = classToType(types[i]);
		}
		return resultTypes;
	}
	
	public static Class<?>[] typeToClass(Type[] types){
		Class<?>[] resultTypes = (types.length != 0)? new Class<?>[types.length]: null;
		for(int i=0; i<types.length; ++i){
			resultTypes[i] = typeToClass(types[i]);
		}
		return resultTypes;
	}
	
	public static Function fromJavaFunction(Method method) throws UnSupportedTypeException{
		Function f = new Function(method.getName(),
								  classToType(method.getReturnType()),
								  classToType(method.getParameterTypes()));
		return f;
	}
	
	public static Function fromJavaFunctionWithClassHeader(Method method) throws UnSupportedTypeException{
		Function f = fromJavaFunction(method);
		f.setName(method.getDeclaringClass().getSimpleName() + "."+ f.getName()); //(*)
		//For The Above Code Class Name Can be Taken As Argument So That Some Reflection Calls 
		//Can Be Reduced
		return f;
	}
	
	public static Method toJavaFunction(Function f){
		return null;
	}
	
	/**
	 * This function returns the priority level of the type
	 * that starts from 0
	 */
	public static short getTypePriority(Type type){
		switch(type){
			case CHAR:
				return 0;
			case INT:
				return 1;
			case STRING:
				return 2;
			default:
				return -1;
		}
	}
	
	public static Type typeOf(String value){
		try{
			int intValue = Integer.parseInt(value);
			return Type.INT;
		}catch(NumberFormatException nfex){
			return Type.STRING;
		}
	}
	
	
	
}
