package org.telosys.tools.generator.context.doc.tooling;

import java.util.List;

import org.junit.Test;
import org.telosys.tools.generator.context.BeanValidation;
import org.telosys.tools.generator.context.Const;
import org.telosys.tools.generator.context.EntityInContext;
import org.telosys.tools.generator.context.FnInContext;
import org.telosys.tools.generator.context.Java;
import org.telosys.tools.generator.context.JpaInContext;
import org.telosys.tools.generator.context.Today;
import org.telosys.tools.generator.context.names.ContextName;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class DocBuilderTest {

	private void print(ClassInfo classInfo) {
		System.out.println(classInfo);
		System.out.println("-----");
		
		List<MethodInfo> methodsInfo = classInfo.getMethodsInfo() ;
		for ( MethodInfo methodInfo : methodsInfo ) {
//			System.out.println(" . Method : " + methodInfo.getJavaName() + " --> " + methodInfo.getVelocityName() );
//			System.out.println("     return      : " + methodInfo.getReturnType());
//			System.out.println("     param types : " + Arrays.toString( methodInfo.getParamTypes() ) );
			System.out.println("     " + methodInfo.getSimpleDescription() );
			System.out.println("     doc : "  );
			String docText[] = methodInfo.getDocText() ;
			for ( String s : docText ) {
				
				System.out.println("      " + s );
			}
			System.out.println("     parameters : "  );
			for ( MethodParameter p : methodInfo.getParameters() ) {
				System.out.println("      " + p.getName() + " : " + p.getDescription() );
			}
			System.out.println("     example : "  );
			String example[] = methodInfo.getExampleText() ;
			for ( String s : example ) {
				
				System.out.println("      " + s );
			}
			System.out.println("     deprecated  : " + methodInfo.isDeprecated() );
			System.out.println("     since       : " + methodInfo.getSince());
			System.out.println("     signature   : " + methodInfo.getSignature() );
			System.out.println("-----");
		}
	}
	
	@Test
	public void testClassConst() {
		DocBuilder docBuilder = new DocBuilder();
		ClassInfo classInfo = docBuilder.getClassInfo(Const.class);
		print(classInfo);

		assertTrue ( "Const".equals( classInfo.getJavaClassName() ) );
		assertTrue ( "const".equals( classInfo.getContextName() ) );
		assertTrue ( classInfo.getMethodsCount() == 12 );
	}

	@Test
	public void testClassFn() {
		DocBuilder docBuilder = new DocBuilder();
		ClassInfo classInfo = docBuilder.getClassInfo(FnInContext.class);
		print(classInfo);

		assertTrue ( "FnInContext".equals( classInfo.getJavaClassName() ) );
		assertTrue ( ContextName.FN.equals( classInfo.getContextName() ) );
		//assertTrue ( classInfo.getMethodsCount() == 12 );
	}

	@Test
	public void testClassJava() {
		DocBuilder docBuilder = new DocBuilder();
		ClassInfo classInfo = docBuilder.getClassInfo(Java.class);
		print(classInfo);

		assertTrue ( "Java".equals( classInfo.getJavaClassName() ) );
		assertTrue ( ContextName.JAVA.equals( classInfo.getContextName() ) );
		assertTrue ( classInfo.getMethodsCount() >= 4 );
	}

	@Test
	public void testClassJpa() {
		DocBuilder docBuilder = new DocBuilder();
		ClassInfo classInfo = docBuilder.getClassInfo(JpaInContext.class);
		print(classInfo);

		assertEquals("JpaInContext", classInfo.getJavaClassName() );
		assertEquals( ContextName.JPA, classInfo.getContextName() );
		//assertTrue ( classInfo.getMethodsCount() == 0 );
	}

	@Test
	public void testClassBeanValidation() {
		DocBuilder docBuilder = new DocBuilder();
		ClassInfo classInfo = docBuilder.getClassInfo(BeanValidation.class);
		print(classInfo);

		assertTrue ( "BeanValidation".equals( classInfo.getJavaClassName() ) );
		assertTrue ( ContextName.BEAN_VALIDATION.equals( classInfo.getContextName() ) );
		//assertTrue ( classInfo.getMethodsCount() == 0 );
	}

	@Test
	public void testClassToday() {
		DocBuilder docBuilder = new DocBuilder();
		ClassInfo classInfo = docBuilder.getClassInfo(Today.class);
		print(classInfo);

		assertTrue ( "Today".equals( classInfo.getJavaClassName() ) );
		assertTrue ( ContextName.TODAY.equals( classInfo.getContextName() ) );
		assertTrue ( classInfo.getMethodsCount() == 4 );
	}

	@Test
	public void testClassJavaBeanClass() {
		DocBuilder docBuilder = new DocBuilder();
		//ClassInfo classInfo = docBuilder.getClassInfo(JavaBeanClass.class);
		ClassInfo classInfo = docBuilder.getClassInfo(EntityInContext.class);
		print(classInfo);

		assertTrue ( "EntityInContext".equals( classInfo.getJavaClassName() ) );
		assertTrue ( ContextName.ENTITY.equals( classInfo.getContextName() ) );
		System.out.println("Methods count = " + classInfo.getMethodsCount() );
		//assertTrue ( classInfo.getMethodsCount() == 35 );
		//assertTrue ( classInfo.getMethodsCount() == 40 );
	}
}
