package org.telosys.tools.generator.languages;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.telosys.tools.commons.exception.TelosysRuntimeException;
import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generator.context.AttributeInContext;
import org.telosys.tools.generator.context.EnvInContext;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import junit.env.telosys.tools.generator.fakemodel.FakeAttributeBuilder;

public class TargetLanguageProviderTest  {

	private static final List<AttributeInContext> ATTRIBUTES_VOID_LIST = new LinkedList<>();
	private static final String LIST_OF_ARG_NAMES = "id, name, flag, birthDate";


	private void check(String languageName) {
		assertTrue(TargetLanguageProvider.isDefinedLanguage(languageName));
		EnvInContext env = new EnvInContext();
		assertNotNull( TargetLanguageProvider.getTargetLanguage(env) );
		assertNotNull( TargetLanguageProvider.getTargetLanguage(env).getTypeConverter() );
		assertNotNull( TargetLanguageProvider.getTargetLanguage(env).getLiteralValuesProvider() );
	}
	
	@Test(expected=TelosysRuntimeException.class)
	public void testGetTargetLanguageWithNull() {
		TargetLanguageProvider.getTargetLanguage(null);
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
		Assert.assertEquals("", tl.argumentsListWithWrapperType(ATTRIBUTES_VOID_LIST) );
		Assert.assertEquals("", tl.argumentsListWithType(null) );
		Assert.assertEquals("", tl.argumentsListWithWrapperType(null) );
		// $env.typeWithNullableMark = default value = true => '?' at the end of type if nullable  ( id is not null => no '?' )
		Assert.assertEquals("int id, string? name, bool? flag, DateOnly? birthDate", tl.argumentsListWithType(attributes) );
		Assert.assertEquals("Int32 id, String? name, Boolean? flag, DateOnly? birthDate", tl.argumentsListWithWrapperType(attributes) );

		// $env.typeWithNullableMark = false =>  no '?' at the end of type
		env.setTypeWithNullableMark(false); 
		Assert.assertEquals("int id, string name, bool flag, DateOnly birthDate", tl.argumentsListWithType(attributes) );
		Assert.assertEquals("Int32 id, String name, Boolean flag, DateOnly birthDate", tl.argumentsListWithWrapperType(attributes) );		
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
	public void testJavaScript() throws GeneratorException {
		check("JavaScript");
		check(" javascript");
		check("JAVASCRIPT  ");
		EnvInContext env = new EnvInContext();
		env.setLanguage("JavaScript"); // required for test 
		TargetLanguage tl = env.getTargetLanguage();
		Assert.assertEquals(TargetLanguageForJavaScript.class.getSimpleName(), tl.getClass().getSimpleName());
		
		List<AttributeInContext> attributes = FakeAttributeBuilder.buildAttributes(env);		
		//
		Assert.assertEquals("", tl.argumentsListWithType(ATTRIBUTES_VOID_LIST) );
		Assert.assertEquals("", tl.argumentsListWithType(null) );
		// no type => just arg names
		Assert.assertEquals(LIST_OF_ARG_NAMES, tl.argumentsListWithType(attributes) );
		// same result with wrapper type
		Assert.assertEquals(tl.argumentsListWithType(attributes), tl.argumentsListWithWrapperType(attributes) );
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
		
		// $env.typeWithNullableMark = default value = true => '?' at the end of type if nullable  ( id is not null => no '?' )
		Assert.assertEquals("id: Int, name: String?, flag: Boolean?, birthDate: LocalDate?", tl.argumentsListWithType(attributes) );
		Assert.assertEquals(tl.argumentsListWithType(attributes), tl.argumentsListWithWrapperType(attributes) ); // same result with wrapper type

		// $env.typeWithNullableMark = false =>  no '?' at the end of type
		env.setTypeWithNullableMark(false); 
		Assert.assertEquals("id: Int, name: String, flag: Boolean, birthDate: LocalDate", tl.argumentsListWithType(attributes) );
		Assert.assertEquals(tl.argumentsListWithType(attributes), tl.argumentsListWithWrapperType(attributes) ); // same result with wrapper type
	}

	@Test
	public void testPHP() throws GeneratorException {
		check("PHP");
		check(" Php");
		check("php ");
		EnvInContext env = new EnvInContext();
		env.setLanguage("Php"); // required for test 
		TargetLanguage tl = env.getTargetLanguage();
		Assert.assertEquals(TargetLanguageForPHP.class.getSimpleName(), tl.getClass().getSimpleName());
		
		List<AttributeInContext> attributes = FakeAttributeBuilder.buildAttributes(env);		
		//
		Assert.assertEquals("", tl.argumentsListWithType(ATTRIBUTES_VOID_LIST) );
		Assert.assertEquals("", tl.argumentsListWithType(null) );
		// $env.typeWithNullableMark = default value = true => '?' at the end of type if nullable  ( id is not null => no '?' )
		Assert.assertEquals("int $id, string $name, bool $flag, $birthDate", tl.argumentsListWithType(attributes) );
		Assert.assertEquals(tl.argumentsListWithType(attributes), tl.argumentsListWithWrapperType(attributes) ); // same result with wrapper type
		
	}

	@Test
	public void testPython() throws GeneratorException {
		check("PYTHON");
		check(" Python");
		check("python ");
		EnvInContext env = new EnvInContext();
		env.setLanguage("Python"); // required for test 
		TargetLanguage tl = env.getTargetLanguage();
		Assert.assertEquals(TargetLanguageForPython.class.getSimpleName(), tl.getClass().getSimpleName());
		
		List<AttributeInContext> attributes = FakeAttributeBuilder.buildAttributes(env);		
		//
		Assert.assertEquals("", tl.argumentsListWithType(ATTRIBUTES_VOID_LIST) );
		Assert.assertEquals("", tl.argumentsListWithType(null) );
		// no type => just arg names
		Assert.assertEquals(LIST_OF_ARG_NAMES, tl.argumentsListWithType(attributes) );
		// same result with wrapper type
		Assert.assertEquals(tl.argumentsListWithType(attributes), tl.argumentsListWithWrapperType(attributes) );
	}

	@Test
	public void testScala() throws GeneratorException {
		check("SCALA");
		check(" Scala");
		check("scala ");
		EnvInContext env = new EnvInContext();
		env.setLanguage("Scala"); // required for test 
		TargetLanguage tl = env.getTargetLanguage();
		Assert.assertEquals(TargetLanguageForScala.class.getSimpleName(), tl.getClass().getSimpleName());
		
		List<AttributeInContext> attributes = FakeAttributeBuilder.buildAttributes(env);		
		// test : argumentsListWithType
		Assert.assertEquals("", tl.argumentsListWithType(ATTRIBUTES_VOID_LIST) );
		Assert.assertEquals("", tl.argumentsListWithType(null) );
		Assert.assertEquals("id: Int, name: String, flag: Boolean, birthDate: LocalDate", tl.argumentsListWithType(attributes) );
		// same result with wrapper type
		Assert.assertEquals(tl.argumentsListWithType(attributes), tl.argumentsListWithWrapperType(attributes) );
	}

	@Test
	public void testTypeScript() {
		check("TypeScript");
		check(" typescript");
		check("TYPESCRIPT  ");
	}
}
