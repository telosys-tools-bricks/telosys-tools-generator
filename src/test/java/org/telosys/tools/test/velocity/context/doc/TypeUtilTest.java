package org.telosys.tools.test.velocity.context.doc;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import junit.framework.TestCase;

import org.telosys.tools.generator.context.doc.TypeUtil;


public class TypeUtilTest  extends TestCase {

	private String convert(Type type ) {
		System.out.println("-----");
		System.out.println("Type   : " + type );
		String s = TypeUtil.typeToString( type ) ;
		System.out.println("Result : " + s );
		return s ;
	}
	
	public String convertMethodReturnType(Class<?> clazz, String methodName) {
		System.out.println("----------");
		try {
			Method m = clazz.getMethod(methodName, (Class[])null);
			Type type = m.getGenericReturnType();
			System.out.println("Method : " + methodName + " return type : " + type );
			return convert(type);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void testWithMethods() {
		//ClassForTypeTest o = new ClassForTypeTest();
		Class<?> clazz = ClassForTypeTest.class ;
//		for ( Method m : ClassForTypeTest.class.getDeclaredMethods() ) {
//			Type type = m.getGenericReturnType() ;
//			convert(type);
//		}
		String s ;
		s = convertMethodReturnType(clazz, "v");
		assertTrue ( "void".equals(s) ) ;
		
		s = convertMethodReturnType(clazz, "s");
		assertTrue ( "String".equals(s) ) ;
		
		s = convertMethodReturnType(clazz, "listString");
		assertTrue ( "List<String>".equals(s) ) ;
		s = convertMethodReturnType(clazz, "listNumber");
		assertTrue ( "List<Number>".equals(s) ) ;
		s = convertMethodReturnType(clazz, "listJoker"); // return List<?>  ( same as "? extends Object" )
		assertTrue ( "List<?>".equals(s) ) ;
		s = convertMethodReturnType(clazz, "listJoker2");  // return List<? extends Object>
		assertTrue ( "List<?>".equals(s) ) ;
		s = convertMethodReturnType(clazz, "listJoker3");  // return List<? super Object>
		assertTrue ( "List<? super Object>".equals(s) ) ;
		s = convertMethodReturnType(clazz, "listJoker4");  // return List<? super Number>
		assertTrue ( "List<? super Number>".equals(s) ) ;
		
		s = convertMethodReturnType(clazz, "mapStringNumber");
		assertTrue ( "Map<String,Number>".equals(s) ) ;
		s = convertMethodReturnType(clazz, "mapStringJoker"); // return Map<String,?>  ( same as "? extends Object" )
		assertTrue ( "Map<String,?>".equals(s) ) ;
		s = convertMethodReturnType(clazz, "mapStringJoker2"); // return Map<String,? extends Object>
		assertTrue ( "Map<String,?>".equals(s) ) ;
		s = convertMethodReturnType(clazz, "mapStringJoker3");
		assertTrue ( "Map<String,? extends Number>".equals(s) ) ;
		
	}
	
//	public void testList() {
//
//		LinkedList<String> instance = new LinkedList<String>();
//		String s = convert( instance.getClass() ) ;
//		
////		assertTrue ( "Const".equals( classInfo.getName() ) );
////		assertTrue ( classInfo.getMethodsCount() == 8 );
//	}

}
