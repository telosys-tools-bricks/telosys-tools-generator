package org.telosys.tools.test.velocity.context.doc;

import java.util.List;

import junit.framework.TestCase;

import org.telosys.tools.generator.context.doc.ClassInfo;
import org.telosys.tools.generator.context.doc.ContextInfo;
import org.telosys.tools.generator.context.doc.MethodInfo;
import org.telosys.tools.generator.context.names.ContextName;


public class ContextInfoTest  extends TestCase {

	private void print(String title, String[] names) {
		System.out.println("-----");
		System.out.println( title + " (" + names.length + ") : ");
		for ( String s : names ) {
			System.out.println(" . " + s );
		}
		System.out.println("-----");
	}
	
//	private void print(ClassInfo classInfo) {
//		if ( classInfo != null ) {
//			System.out.println("ClassInfo : " + classInfo.getContextName() );
//		}
//		else {
//			System.out.println("null");
//		}
//	}
	
	public void testVariableNames() {
		ContextInfo contextInfo = new ContextInfo();
		String[] names = contextInfo.getVariableNames() ;
		print("Variable names", names);
		
		//assertTrue ( names.length == 15 );
		assertTrue ( names.length == 17 ); // v 2.0.6
	}

	public void testObjectNames() {
		ContextInfo contextInfo = new ContextInfo();
		String[] names = contextInfo.getObjectNames();
		print("Object names", names);
		
		//assertTrue ( names.length == 11 );
		assertTrue ( names.length == 16 ); // v 2.1.0
	}

	public void testObjectAndVariableNames() {
		ContextInfo contextInfo = new ContextInfo();
		String[] names = contextInfo.getObjectAndVariableNames();
		print("Object and variable names", names);
		
		int n = contextInfo.getObjectNames().length + contextInfo.getVariableNames().length ;
		assertTrue ( names.length == n );
	}

	public void testPredefinedNames() {
		ContextInfo contextInfo = new ContextInfo();
		String[] names = contextInfo.getPredefinedNames();
		print("Predefined names", names);
		
		//assertTrue ( names.length == 6 );
		assertTrue ( names.length == 9 ); // ver 2.1.0
	}
	
	//------------------------------------------------------------------------------------------
	private ClassInfo getClassInfo(String name) {
		System.out.println("get classInfo for '" + name + "'");
		ContextInfo contextInfo = new ContextInfo();
		ClassInfo classInfo = contextInfo.getClassInfo(name);
		if ( classInfo != null ) {
			System.out.println("ClassInfo : " + classInfo.getContextName() );
		}
		else {
			System.out.println("ClassInfo not found.");
		}
		return classInfo ;
	}
	
	public void testGetClassInfo() {
		ClassInfo classInfo = getClassInfo("fn");
		assertNotNull( classInfo );
		
		classInfo = getClassInfo("entity");
		assertNotNull( classInfo );
//		classInfo = getClassInfo("beanClass"); // other name for 'entity'
//		assertNotNull( classInfo );
		
		classInfo = getClassInfo("foo");
		assertNull( classInfo );

		classInfo = getClassInfo(ContextName.LINK_ATTRIBUTE );
		assertNotNull( classInfo );
		List<MethodInfo> methodsInfo = classInfo.getMethodsInfo();
		for ( MethodInfo mi : methodsInfo ) {
			System.out.println(" . " + mi );
		}
		assertEquals(2, classInfo.getMethodsCount());
		
	}

	//------------------------------------------------------------------------------------------
	private void print(MethodInfo[] methodsInfo){
		for ( MethodInfo mi : methodsInfo ) {
			System.out.println(" . " + mi.toString() );
		}
	}
	private MethodInfo[] getAllMethodsInfo(String objectName) {
		System.out.println("get all MethodInfo for '" + objectName + "'");
		ContextInfo contextInfo = new ContextInfo();
		MethodInfo[] methodsInfo = contextInfo.getAllMethodsInfo(objectName);
		if ( methodsInfo != null ) {
			System.out.println("methodsInfo found : " + methodsInfo.length );
			print(methodsInfo);
		}
		else {
			System.out.println("methodsInfo not found.");
		}
		return methodsInfo ;
	}

	public void testGetAllMethodInfo() {
		MethodInfo[] methodsInfo = getAllMethodsInfo("fn");
		assertNotNull( methodsInfo );
		ContextInfo contextInfo = new ContextInfo();
		String signature = methodsInfo[0].getSignature() ;
		MethodInfo mi = contextInfo.getMethodInfo("fn", signature);
		assertNotNull( mi );
		System.out.println(" MethodInfo for '" + signature + "' : " + mi.toString() );
		
		methodsInfo = getAllMethodsInfo("foo");
		assertNull( methodsInfo );
	}
	
	public void testGetClassDocumentation() {
		System.out.println("---- contextInfo.getClassDocumentation('..');");
		ContextInfo contextInfo = new ContextInfo();
		
		System.out.println("$fn : ");
		String doc = contextInfo.getClassDocumentation("fn");
		System.out.println(doc);
		assertNotNull(doc);

		System.out.println("$foo : ");
		doc = contextInfo.getClassDocumentation("foo");
		System.out.println(doc);
		assertNull(doc);
		//assertTrue(doc.startsWith("Unknown"));
		
		System.out.println("$entity : ");
		doc = contextInfo.getClassDocumentation("entity");
		System.out.println(doc);
		assertNotNull(doc);

//		System.out.println("$beanClass : ");
//		doc = contextInfo.getClassDocumentation("beanClass");
//		System.out.println(doc);
//		assertNotNull(doc);
	}
	
	public void testGetMethodDocumentation() {
		System.out.println("---- contextInfo.getMethodDocumentation('..', '..');");
		ContextInfo contextInfo = new ContextInfo();
		
		System.out.println("$fn : ");
		MethodInfo[] methodsInfo = contextInfo.getAllMethodsInfo("fn");
		assertNotNull(methodsInfo);
		assertTrue(methodsInfo.length > 0 );
		String doc = contextInfo.getMethodDocumentation("fn", methodsInfo[0].getSignature() );
		System.out.println(doc);
		assertNotNull(doc);

		doc = contextInfo.getMethodDocumentation("fn", "xxxxx" );
		System.out.println(doc);
		assertNull(doc);

		doc = contextInfo.getMethodDocumentation("xxxx", "xxxxx" );
		System.out.println(doc);
		assertNull(doc);

		doc = contextInfo.getMethodDocumentation("xxxx", methodsInfo[0].getSignature() );
		System.out.println(doc);
		assertNull(doc);
	}
	
}
