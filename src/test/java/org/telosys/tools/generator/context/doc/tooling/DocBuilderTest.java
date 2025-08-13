package org.telosys.tools.generator.context.doc.tooling;

import java.util.List;

import org.junit.Test;
import org.telosys.tools.generator.context.BeanValidation;
import org.telosys.tools.generator.context.ConstInContext;
import org.telosys.tools.generator.context.CsharpInContext;
import org.telosys.tools.generator.context.EntityInContext;
import org.telosys.tools.generator.context.FnInContext;
import org.telosys.tools.generator.context.GeneratorInContext;
import org.telosys.tools.generator.context.JavaInContext;
import org.telosys.tools.generator.context.JpaInContext;
import org.telosys.tools.generator.context.LoaderInContext;
import org.telosys.tools.generator.context.PhpInContext;
import org.telosys.tools.generator.context.Today;
import org.telosys.tools.generator.context.names.ContextName;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DocBuilderTest {

	private void println(String s) {
		System.out.println(s);
	}
	private void print(ClassInfo classInfo) {
		println(classInfo.toString());
		println("-----");
		
		List<MethodInfo> methodsInfo = classInfo.getMethodsInfo() ;
		for ( MethodInfo methodInfo : methodsInfo ) {
			println("  " + methodInfo.getSimpleDescription() );
			println("     doc : "  );
			String docText[] = methodInfo.getDocText() ;
			for ( String s : docText ) {
				
				println("      " + s );
			}
			println("     parameters : "  );
			for ( MethodParameter p : methodInfo.getParameters() ) {
				println("      " + p.getName() + " : " + p.getDescription() );
			}
			println("     example : "  );
			String example[] = methodInfo.getExampleText() ;
			for ( String s : example ) {
				
				println("      " + s );
			}
			println("     deprecated  : " + methodInfo.isDeprecated() );
			println("     since       : " + methodInfo.getSince());
			println("     signature   : " + methodInfo.getSignature() );
			println("-----");
		}
	}
	
	@Test
	public void testClassConst() {
		DocBuilder docBuilder = new DocBuilder();
		ClassInfo classInfo = docBuilder.getClassInfo(ConstInContext.class);
		print(classInfo);
		assertEquals("ConstInContext",  classInfo.getJavaClassName() );
		assertEquals(ContextName.CONST, classInfo.getContextName() );
		assertEquals(12, classInfo.getMethodsCount() );
	}

	@Test
	public void testClassFn() {
		DocBuilder docBuilder = new DocBuilder();
		ClassInfo classInfo = docBuilder.getClassInfo(FnInContext.class);
		print(classInfo);
		assertEquals("FnInContext",  classInfo.getJavaClassName() );
		assertEquals(ContextName.FN, classInfo.getContextName() );
		assertEquals(37, classInfo.getMethodsCount() );
	}

	@Test
	public void testClassGenerator() {
		DocBuilder docBuilder = new DocBuilder();
		ClassInfo classInfo = docBuilder.getClassInfo(GeneratorInContext.class);
		print(classInfo);
		assertEquals("GeneratorInContext",  classInfo.getJavaClassName() );
		assertEquals(ContextName.GENERATOR, classInfo.getContextName() );
		assertEquals(3, classInfo.getMethodsCount() );
	}

	@Test
	public void testClassToday() { // (!) DEPRECATED
		DocBuilder docBuilder = new DocBuilder();
		ClassInfo classInfo = docBuilder.getClassInfo(Today.class);
		print(classInfo);

		assertTrue ( "Today".equals( classInfo.getJavaClassName() ) );
		assertTrue ( ContextName.TODAY.equals( classInfo.getContextName() ) );
		assertTrue ( classInfo.getMethodsCount() == 4 );
		assertTrue ( classInfo.isDeprecated() );
	}

	@Test
	public void testClassEntity() {
		DocBuilder docBuilder = new DocBuilder();
		ClassInfo classInfo = docBuilder.getClassInfo(EntityInContext.class);
		print(classInfo);

		assertEquals ( "EntityInContext",  classInfo.getJavaClassName() );
		assertEquals ( ContextName.ENTITY, classInfo.getContextName() );
		assertTrue ( classInfo.getMethodsCount() >= 75 );
	}

	@Test
	public void testClassLoader() {
		DocBuilder docBuilder = new DocBuilder();
		ClassInfo classInfo = docBuilder.getClassInfo(LoaderInContext.class);
		print(classInfo);

		assertEquals( "LoaderInContext",  classInfo.getJavaClassName() );
		assertEquals( ContextName.LOADER, classInfo.getContextName() );
		assertEquals( 5, classInfo.getMethodsCount() );
	}

	//----------------------------------------------------------------------------

	@Test
	public void testClassJava() {
		DocBuilder docBuilder = new DocBuilder();
		ClassInfo classInfo = docBuilder.getClassInfo(JavaInContext.class);
		print(classInfo);

		assertEquals ( "JavaInContext",  classInfo.getJavaClassName() );
		assertEquals ( ContextName.JAVA, classInfo.getContextName() );
		assertTrue ( classInfo.getMethodsCount() >= 4 );
	}
	@Test
	public void testClassJpa() {
		DocBuilder docBuilder = new DocBuilder();
		ClassInfo classInfo = docBuilder.getClassInfo(JpaInContext.class);
		print(classInfo);

		assertEquals("JpaInContext", classInfo.getJavaClassName() );
		assertEquals( ContextName.JPA, classInfo.getContextName() );
	}

	@Test
	public void testClassBeanValidation() {  // (!) DEPRECATED
		DocBuilder docBuilder = new DocBuilder();
		ClassInfo classInfo = docBuilder.getClassInfo(BeanValidation.class);
		print(classInfo);

		assertTrue ( "BeanValidation".equals( classInfo.getJavaClassName() ) );
		assertTrue ( ContextName.BEAN_VALIDATION.equals( classInfo.getContextName() ) );
		assertTrue ( classInfo.isDeprecated() );
	}

	@Test
	public void testClassCsharp() {
		DocBuilder docBuilder = new DocBuilder();
		ClassInfo classInfo = docBuilder.getClassInfo(CsharpInContext.class);
		print(classInfo);

		assertEquals ( "CsharpInContext",  classInfo.getJavaClassName() );
		assertEquals ( ContextName.CSHARP, classInfo.getContextName() );
		assertTrue ( classInfo.getMethodsCount() >= 4 );
	}

	@Test
	public void testClassPhp() {
		DocBuilder docBuilder = new DocBuilder();
		ClassInfo classInfo = docBuilder.getClassInfo(PhpInContext.class);
		print(classInfo);

		assertEquals ( "PhpInContext",  classInfo.getJavaClassName() );
		assertEquals ( ContextName.PHP, classInfo.getContextName() );
		assertTrue ( classInfo.getMethodsCount() >= 4 );
	}
}
