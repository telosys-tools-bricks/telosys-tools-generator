package org.telosys.tools.generator.languages;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generator.context.AttributeInContext;
import org.telosys.tools.generator.context.EnvInContext;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import junit.env.telosys.tools.generator.fakemodel.FakeAttributeBuilder;

public class TargetLanguageProviderTest  {

	private static final List<AttributeInContext> ATTRIBUTES_VOID_LIST = new LinkedList<>();

	private void check(String languageName) {
		assertTrue(TargetLanguageProvider.isDefinedLanguage(languageName));
		assertNotNull( TargetLanguageProvider.getTargetLanguage(languageName) );
		assertNotNull( TargetLanguageProvider.getTargetLanguage(languageName).getTypeConverter() );
		assertNotNull( TargetLanguageProvider.getTargetLanguage(languageName).getLiteralValuesProvider() );
		assertNotNull( TargetLanguageProvider.getTypeConverter(languageName) );
		assertNotNull( TargetLanguageProvider.getLiteralValuesProvider(languageName) );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testGetTargetLanguageWithNull() {
		TargetLanguageProvider.getTargetLanguage(null);
	}
	@Test(expected=IllegalArgumentException.class)
	public void testGetTypeConverterWithNull() {
		TargetLanguageProvider.getTypeConverter(null);
	}
	@Test(expected=IllegalArgumentException.class)
	public void testGetLiteralValuesProviderWithNull() {
		TargetLanguageProvider.getLiteralValuesProvider(null);
	}

	@Test
	public void testCPlusPlus() {
		check("C++");
		check(" c++");
		check("C++ ");
	}

	@Test
	public void testCSharp() throws GeneratorException {
		check("C#");
		check(" c#");
		check(" C# ");

		EnvInContext env = new EnvInContext();
		env.setLanguage("C#"); // required for test 
		TargetLanguage tl = env.getTargetLanguage();
		Assert.assertEquals(TargetLanguageForCSharp.class.getSimpleName(), tl.getClass().getSimpleName());
		
		List<AttributeInContext> attributes = FakeAttributeBuilder.buildAttributes(env);		
		//
		Assert.assertEquals("", tl.argumentsListWithType(ATTRIBUTES_VOID_LIST) );
		Assert.assertEquals("", tl.argumentsListWithType(null) );
		// id is not null => no '?'
		Assert.assertEquals("int id, string? name, bool? flag, DateOnly? birthDate", tl.argumentsListWithType(attributes) );
		//
		Assert.assertEquals("", tl.argumentsListWithWrapperType(ATTRIBUTES_VOID_LIST) );
		Assert.assertEquals("", tl.argumentsListWithWrapperType(null) );
		Assert.assertEquals("Int32 id, String? name, Boolean? flag, DateOnly? birthDate", tl.argumentsListWithWrapperType(attributes) );
	}

	@Test
	public void testGo() throws GeneratorException {
		check("Go");
		check(" go");
		check("GO ");		

		EnvInContext env = new EnvInContext();
		env.setLanguage("Go"); // required for test 
		TargetLanguage tl = env.getTargetLanguage();
		Assert.assertEquals(TargetLanguageForGo.class.getSimpleName(), tl.getClass().getSimpleName());
		
		List<AttributeInContext> attributes = FakeAttributeBuilder.buildAttributes(env);		
		//
		Assert.assertEquals("", tl.argumentsListWithType(ATTRIBUTES_VOID_LIST) );
		Assert.assertEquals("", tl.argumentsListWithType(null) );
		Assert.assertEquals("id int32, name string, flag bool, birthDate time.Time", tl.argumentsListWithType(attributes) );
		//
		Assert.assertEquals("", tl.argumentsListWithWrapperType(ATTRIBUTES_VOID_LIST) );
		Assert.assertEquals("", tl.argumentsListWithWrapperType(null) );
		Assert.assertEquals("id int32, name string, flag bool, birthDate time.Time", tl.argumentsListWithWrapperType(attributes) );
	}

	@Test
	public void testJava() {
		check("Java");
		check(" java");
		check("JAVA  ");
		EnvInContext env = new EnvInContext();
		TargetLanguage tl = env.getTargetLanguage(); // Java by default
		Assert.assertEquals(TargetLanguageForJava.class.getSimpleName(), tl.getClass().getSimpleName());
		
		List<AttributeInContext> attributes = FakeAttributeBuilder.buildAttributes(env);		
		//
		Assert.assertEquals("", tl.argumentsListWithType(ATTRIBUTES_VOID_LIST) );
		Assert.assertEquals("", tl.argumentsListWithType(null) );
		// id is not null => int 
		Assert.assertEquals("int id, String name, Boolean flag, LocalDate birthDate", tl.argumentsListWithType(attributes) );
		//
		Assert.assertEquals("", tl.argumentsListWithWrapperType(ATTRIBUTES_VOID_LIST) );
		Assert.assertEquals("", tl.argumentsListWithWrapperType(null) );
		Assert.assertEquals("Integer id, String name, Boolean flag, LocalDate birthDate", tl.argumentsListWithWrapperType(attributes) );
	}

	@Test
	public void testJavaScript() {
		check("JavaScript");
		check(" javascript");
		check("JAVASCRIPT  ");
	}

	@Test
	public void testKotlin() throws GeneratorException {
		check("KOTLIN");
		check(" Kotlin");
		check("kotlin ");
		EnvInContext env = new EnvInContext();
		env.setLanguage("Kotlin"); // required for test 
		TargetLanguage tl = env.getTargetLanguage();
		Assert.assertEquals(TargetLanguageForKotlin.class.getSimpleName(), tl.getClass().getSimpleName());
		
		List<AttributeInContext> attributes = FakeAttributeBuilder.buildAttributes(env);		
		//
		Assert.assertEquals("", tl.argumentsListWithType(ATTRIBUTES_VOID_LIST) );
		Assert.assertEquals("", tl.argumentsListWithType(null) );
		// id is not null => no '?'
		Assert.assertEquals("id: Int, name: String?, flag: Boolean?, birthDate: LocalDate?", tl.argumentsListWithType(attributes) );
//		//
//		Assert.assertEquals("", tl.argumentsListWithWrapperType(ATTRIBUTES_VOID_LIST) );
//		Assert.assertEquals("", tl.argumentsListWithWrapperType(null) );
//		Assert.assertEquals("Int32 id, String? name, Boolean? flag", tl.argumentsListWithWrapperType(attributes) );
	}

	@Test
	public void testPHP() {
		check("PHP");
		check(" Php");
		check("php ");
	}

	@Test
	public void testPython() {
		check("PYTHON");
		check(" Python");
		check("python ");
	}

	@Test
	public void testScala() {
		check("SCALA");
		check(" Scala");
		check("scala ");
	}

	@Test
	public void testTypeScript() {
		check("TypeScript");
		check(" typescript");
		check("TYPESCRIPT  ");
	}
}
