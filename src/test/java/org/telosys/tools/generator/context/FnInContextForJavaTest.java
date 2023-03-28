package org.telosys.tools.generator.context;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.telosys.tools.dsl.model.DslModelAttribute;
import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generic.model.Attribute;

import junit.env.telosys.tools.generator.context.Builder;

public class FnInContextForJavaTest {
	
	private static final List<AttributeInContext> ATTRIBUTES_VOID_LIST = new LinkedList<>();
	
	@Test
	public void testBackslash() {
		FnInContext fn = new FnInContext(null, null);
		Assert.assertEquals("abc", fn.backslash("abc", "$") );
		Assert.assertEquals("abc\\$de", fn.backslash("abc$de", "$") );
		Assert.assertEquals("abc\\$de\\$fg", fn.backslash("abc$de$fg", "$") );
	}

	@Test
	public void testTab() {
		FnInContext fn = new FnInContext(null, null);
		Assert.assertEquals("\t\t", fn.tab(2) );
		Assert.assertEquals("", fn.tab(0) );
		Assert.assertEquals("", fn.tab(-1) );
	}

	@Test
	public void testFirstCharToUpperCase() {
		FnInContext fn = new FnInContext(null, null);
		Assert.assertEquals("", fn.firstCharToUpperCase("") );
		Assert.assertEquals("A", fn.firstCharToUpperCase("a") );
		Assert.assertEquals("A", fn.firstCharToUpperCase("A") );
		Assert.assertEquals("Abcd", fn.firstCharToUpperCase("abcd") );
		Assert.assertEquals("Abcd", fn.firstCharToUpperCase("ABCD") ); 
	}

	@Test
	public void testCapitalize() {
		FnInContext fn = new FnInContext(null, null);
		Assert.assertEquals("", fn.capitalize("") );
		Assert.assertEquals("A", fn.capitalize("a") );
		Assert.assertEquals("A", fn.capitalize("A") );
		Assert.assertEquals("Abcd", fn.capitalize("abcd") );
		Assert.assertEquals("ABCD", fn.capitalize("ABCD") ); 
		Assert.assertEquals(" abc", fn.capitalize(" abc") ); 
		Assert.assertEquals("Abc ", fn.capitalize("abc ") );
	}

	@Test
	public void testUncapitalize() {
		FnInContext fn = new FnInContext(null, null);
		Assert.assertEquals("", fn.uncapitalize("") );
		Assert.assertEquals("a", fn.uncapitalize("A") );
		Assert.assertEquals("a", fn.uncapitalize("a") );
		Assert.assertEquals("abcd", fn.uncapitalize("Abcd") );
		Assert.assertEquals("aBCD", fn.uncapitalize("ABCD") ); 
		Assert.assertEquals(" abc", fn.uncapitalize(" abc") ); 
		Assert.assertEquals("abc ", fn.uncapitalize("Abc ") );
	}

	@Test
	public void testIsBlank() {
		FnInContext fn = new FnInContext(null, null);
		Assert.assertTrue( fn.isBlank("") );
		Assert.assertTrue( fn.isBlank(" ") );
		Assert.assertTrue( fn.isBlank("  ") );
		
		Assert.assertFalse( fn.isBlank("a") );
		Assert.assertFalse( fn.isBlank(" a") );
		Assert.assertFalse( fn.isBlank(" a ") );
		Assert.assertFalse( fn.isBlank("a ") );
		Assert.assertFalse( fn.isBlank("a b c") );
	}

	@Test
	public void testIsVoid() {
		FnInContext fn = new FnInContext(null, null);
		String[] a = {} ;
		Assert.assertTrue( fn.isVoid(a) );
		
		Assert.assertTrue( fn.isVoid(new LinkedList<Integer>()) );

		Integer[] c = { 1, 2, 3 };  
		Assert.assertFalse( fn.isVoid(c) );

		List<Integer> d = new ArrayList<>();  
		d.add(12);
		Assert.assertFalse( fn.isVoid(d) );
	}

	@Test
	public void testQuote() {
		FnInContext fn = new FnInContext(null, null);
		Assert.assertEquals("\"a\"", fn.quote("a") );
		Assert.assertEquals("\"abcd\"", fn.quote("abcd") );
		Assert.assertEquals("\"\"", fn.quote("") );
		Assert.assertEquals("\"null\"", fn.quote(null) );
	}

	@Test
	public void testUnquote() {
		FnInContext fn = new FnInContext(null, null);
		Assert.assertEquals("a", fn.unquote("\"a\"") );
		Assert.assertEquals("abcd", fn.unquote("\"abcd\"") );
		Assert.assertEquals("ab\"cd", fn.unquote("\"ab\"cd\"") );
		Assert.assertEquals("", fn.unquote("\"\"") );
		Assert.assertNull( fn.unquote(null) );
		Assert.assertEquals("\"abcd", fn.unquote("\"abcd") ); // Unchanged
		Assert.assertEquals("abcd\"", fn.unquote("abcd\"") ); // Unchanged
		Assert.assertEquals("ab\"cd", fn.unquote("ab\"cd") ); // Unchanged
		Assert.assertEquals(" \"abcd\"", fn.unquote(" \"abcd\"") ); // Unchanged
		Assert.assertEquals(" \"abcd\" ", fn.unquote(" \"abcd\" ") ); // Unchanged
	}

	private AttributeInContext buildAttribute(EntityInContext entityInContext, String name, String type, EnvInContext envInContext) {
		Attribute attribute = new DslModelAttribute(name, type);
		return new AttributeInContext(entityInContext, 
				attribute, 
				null, 
				envInContext ) ;
	}
	private List<AttributeInContext> buildAttributes(EnvInContext envInContext) {
		//EntityInContext entityInContext = new EntityInContext(new FakeEntity(), null, null, envInContext);
		//Entity entity = new FakeEntity("Foo", "FOO");
		//EntityInContext entityInContext = new EntityInContext(entity, null, null, envInContext);
		//EntityInContext entityInContext = Builder.buildEntityInContext(entity);
		EntityInContext entityInContext = Builder.buildEntityInContext("Foo", "FOO");
		List<AttributeInContext> attributes = new LinkedList<>();
		attributes.add( buildAttribute(entityInContext, "id", "int", envInContext) );
		attributes.add( buildAttribute(entityInContext, "name", "string", envInContext ) );
		attributes.add( buildAttribute(entityInContext, "flag", "boolean", envInContext ) );
		return attributes;
	}

	//-----------------------------------------------------------------------------------
	// fn.argumentsList
	//-----------------------------------------------------------------------------------
	@Test
	public void testArgumentsListForDefault() { // Default = Java
		EnvInContext envInContext = new EnvInContext();
		Assert.assertEquals("JAVA", envInContext.getLanguage().toUpperCase() );
		
		List<AttributeInContext> attributes = buildAttributes(envInContext);
		FnInContext fn = new FnInContext(null, envInContext);
		Assert.assertEquals("id, name, flag", fn.argumentsList(attributes) );
		Assert.assertEquals("", fn.argumentsList(null) );
		Assert.assertEquals("", fn.argumentsList(ATTRIBUTES_VOID_LIST) );
	}
	@Test
	public void testArgumentsListForCPlusPlus() throws GeneratorException { // C++
		EnvInContext envInContext = new EnvInContext();
		envInContext.setLanguage("C++");
		Assert.assertEquals("C++", envInContext.getLanguage().toUpperCase() );
		
		List<AttributeInContext> attributes = buildAttributes(envInContext);
		FnInContext fn = new FnInContext(null, envInContext);
		Assert.assertEquals("id, name, flag", fn.argumentsList(attributes) );
		Assert.assertEquals("", fn.argumentsList(null) );
		Assert.assertEquals("", fn.argumentsList(ATTRIBUTES_VOID_LIST) );
	}
	@Test
	public void testArgumentsListForCSharp() throws GeneratorException { // C#
		EnvInContext envInContext = new EnvInContext();
		envInContext.setLanguage("C#");
		Assert.assertEquals("C#", envInContext.getLanguage().toUpperCase() );
		
		List<AttributeInContext> attributes = buildAttributes(envInContext);
		FnInContext fn = new FnInContext(null, envInContext);
		Assert.assertEquals("id, name, flag", fn.argumentsList(attributes) );
		Assert.assertEquals("", fn.argumentsList(null) );
		Assert.assertEquals("", fn.argumentsList(ATTRIBUTES_VOID_LIST) );
	}
	@Test
	public void testArgumentsListForGoLang() throws GeneratorException { // Go
		EnvInContext envInContext = new EnvInContext();
		envInContext.setLanguage("Go");
		Assert.assertEquals("GO", envInContext.getLanguage().toUpperCase() );

		List<AttributeInContext> attributes = buildAttributes(envInContext);
		FnInContext fn = new FnInContext(null, envInContext);
		Assert.assertEquals("id, name, flag", fn.argumentsList(attributes) );
		Assert.assertEquals("", fn.argumentsList(null) );
		Assert.assertEquals("", fn.argumentsList(ATTRIBUTES_VOID_LIST) );
	}
	@Test
	public void testArgumentsListForPHP() throws GeneratorException { // PHP
		EnvInContext envInContext = new EnvInContext();
		envInContext.setLanguage("PHP");
		Assert.assertEquals("PHP", envInContext.getLanguage().toUpperCase() );

		List<AttributeInContext> attributes = buildAttributes(envInContext);
		FnInContext fn = new FnInContext(null, envInContext);
		Assert.assertEquals("$id, $name, $flag", fn.argumentsList(attributes) );
		Assert.assertEquals("", fn.argumentsList(null) );
		Assert.assertEquals("", fn.argumentsList(ATTRIBUTES_VOID_LIST) );
	}

	//-----------------------------------------------------------------------------------	
	// fn.argumentsListWithType
	//-----------------------------------------------------------------------------------
	@Test
	public void testArgumentsListWithTypeForDefault() { // Java : standard
		EnvInContext envInContext = new EnvInContext();
		List<AttributeInContext> attributes = buildAttributes(envInContext);
		FnInContext fn = new FnInContext(null, envInContext);
		Assert.assertEquals("Integer id, String name, Boolean flag", fn.argumentsListWithType(attributes) );
		Assert.assertEquals("", fn.argumentsListWithType(null) );
		Assert.assertEquals("", fn.argumentsListWithType(ATTRIBUTES_VOID_LIST) );
	}
	@Test
	public void testArgumentsListWithTypeForCSharp() throws GeneratorException { // C# : standard
		EnvInContext envInContext = new EnvInContext();
		envInContext.setLanguage("C#");
		List<AttributeInContext> attributes = buildAttributes(envInContext);
		FnInContext fn = new FnInContext(null, envInContext);
		Assert.assertEquals("int? id, string? name, bool? flag", fn.argumentsListWithType(attributes) );
		Assert.assertEquals("", fn.argumentsListWithType(null) );
		Assert.assertEquals("", fn.argumentsListWithType(ATTRIBUTES_VOID_LIST) );
	}
	@Test
	public void testArgumentsListWithTypeForTypeScript() throws GeneratorException { // TypeScript
		EnvInContext envInContext = new EnvInContext();
		envInContext.setLanguage("TypeScript");
		List<AttributeInContext> attributes = buildAttributes(envInContext);
		FnInContext fn = new FnInContext(null, envInContext);
		Assert.assertEquals("id: number, name: string, flag: boolean", fn.argumentsListWithType(attributes) );
		Assert.assertEquals("", fn.argumentsListWithType(null) );
		Assert.assertEquals("", fn.argumentsListWithType(ATTRIBUTES_VOID_LIST) );
	}
	@Test
	public void testArgumentsListWithTypeForGoLang() throws GeneratorException { // Go : inversion 
		EnvInContext envInContext = new EnvInContext();
		envInContext.setLanguage("Go");
		List<AttributeInContext> attributes = buildAttributes(envInContext);
		FnInContext fn = new FnInContext(null, envInContext);
		Assert.assertEquals("id int32, name string, flag bool", fn.argumentsListWithType(attributes) );
		Assert.assertEquals("", fn.argumentsListWithType(null) );
		Assert.assertEquals("", fn.argumentsListWithType(ATTRIBUTES_VOID_LIST) );
	}

	//-----------------------------------------------------------------------------------
	// fn.argumentsListWithGetter
	//-----------------------------------------------------------------------------------	
	@Test
	public void testArgumentsListWithGetterForDefault() { // Default = Java
		EnvInContext envInContext = new EnvInContext();
		List<AttributeInContext> attributes = buildAttributes(envInContext);
		FnInContext fn = new FnInContext(null, envInContext);
		Assert.assertEquals("obj.getId(), obj.getName(), obj.getFlag()", fn.argumentsListWithGetter("obj", attributes) );
		Assert.assertEquals("", fn.argumentsListWithGetter("obj", null) );
		Assert.assertEquals("", fn.argumentsListWithGetter("obj", ATTRIBUTES_VOID_LIST) );
	}
	@Test
	public void testArgumentsListWithGetterForGoLang() throws GeneratorException {  // Go : no change
		EnvInContext envInContext = new EnvInContext();
		envInContext.setLanguage("Go");
		FnInContext fn = new FnInContext(null, envInContext);
		Assert.assertEquals("", fn.argumentsListWithGetter("obj", null) );
		Assert.assertEquals("", fn.argumentsListWithGetter("obj", ATTRIBUTES_VOID_LIST) );
	}

	//-----------------------------------------------------------------------------------	
	// fn.argumentsListWithWrapperType
	//-----------------------------------------------------------------------------------	
	@Test
	public void testArgumentsListWithWrapperTypeForDefault() { 
		EnvInContext envInContext = new EnvInContext();
		List<AttributeInContext> attributes = buildAttributes(envInContext);
		FnInContext fn = new FnInContext(null, envInContext);
		Assert.assertEquals("Integer id, String name, Boolean flag", fn.argumentsListWithWrapperType(attributes) );
		Assert.assertEquals("", fn.argumentsListWithWrapperType(null) );
		Assert.assertEquals("", fn.argumentsListWithWrapperType(ATTRIBUTES_VOID_LIST) );
	}
	@Test
	public void testArgumentsListWithWrapperTypeForCSharp() throws GeneratorException { // C#
		EnvInContext envInContext = new EnvInContext();
		envInContext.setLanguage("C#");
		List<AttributeInContext> attributes = buildAttributes(envInContext);
		FnInContext fn = new FnInContext(null, envInContext);
		Assert.assertEquals("Int32? id, String? name, Boolean? flag", fn.argumentsListWithWrapperType(attributes) );
		Assert.assertEquals("", fn.argumentsListWithWrapperType(null) );
		Assert.assertEquals("", fn.argumentsListWithWrapperType(ATTRIBUTES_VOID_LIST) );
	}
	@Test
	public void testArgumentsListWithWrapperTypeForTypeScript() throws GeneratorException { // TypeScript
		EnvInContext envInContext = new EnvInContext();
		envInContext.setLanguage("TypeScript");
		List<AttributeInContext> attributes = buildAttributes(envInContext);
		FnInContext fn = new FnInContext(null, envInContext);
		Assert.assertEquals("id: number, name: string, flag: boolean", fn.argumentsListWithWrapperType(attributes) );
		Assert.assertEquals("", fn.argumentsListWithWrapperType(null) );
		Assert.assertEquals("", fn.argumentsListWithWrapperType(ATTRIBUTES_VOID_LIST) );
	}
	@Test
	public void testArgumentsListWithWrapperTypeForGoLang() throws GeneratorException { // Go
		EnvInContext envInContext = new EnvInContext();
		envInContext.setLanguage("Go");
		List<AttributeInContext> attributes = buildAttributes(envInContext);
		FnInContext fn = new FnInContext(null, envInContext);
		Assert.assertEquals("id int32, name string, flag bool", fn.argumentsListWithWrapperType(attributes) );
		Assert.assertEquals("", fn.argumentsListWithWrapperType(null) );
		Assert.assertEquals("", fn.argumentsListWithWrapperType(ATTRIBUTES_VOID_LIST) );
	}
}
