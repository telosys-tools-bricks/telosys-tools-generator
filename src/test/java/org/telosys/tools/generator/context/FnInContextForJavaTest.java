package org.telosys.tools.generator.context;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.telosys.tools.fake.generic.model.FakeAttribute;
import org.telosys.tools.fake.generic.model.FakeEntity;

public class FnInContextForJavaTest {
	
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
	public void testIsVoid() throws Exception {
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
	public void testQuote() throws Exception {
		FnInContext fn = new FnInContext(null, null);
		Assert.assertEquals("\"a\"", fn.quote("a") );
		Assert.assertEquals("\"abcd\"", fn.quote("abcd") );
		Assert.assertEquals("\"\"", fn.quote("") );
		Assert.assertEquals("\"null\"", fn.quote(null) );
	}

	@Test
	public void testUnquote() throws Exception {
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

	private List<AttributeInContext> buildAttributes(EnvInContext envInContext) {
		EntityInContext entityInContext = new EntityInContext(new FakeEntity(), null, null, envInContext);
		List<AttributeInContext> attributes = new LinkedList<>();
		attributes.add( new AttributeInContext(entityInContext, new FakeAttribute("id", "int"), null, envInContext ) );
		attributes.add( new AttributeInContext(entityInContext, new FakeAttribute("name", "string"), null, envInContext ) );
		attributes.add( new AttributeInContext(entityInContext, new FakeAttribute("flag", "boolean"), null, envInContext ) );
		return attributes;
	}

	//-----------------------------------------------------------------------------------
	// fn.argumentsList
	//-----------------------------------------------------------------------------------
	@Test
	public void testArgumentsList_Default() throws Exception { // Default = Java
		EnvInContext envInContext = new EnvInContext();
		Assert.assertEquals("JAVA", envInContext.getLanguage().toUpperCase() );
		Assert.assertTrue(envInContext.languageIsJava());
		
		List<AttributeInContext> attributes = buildAttributes(envInContext);
		FnInContext fn = new FnInContext(null, envInContext);
		Assert.assertEquals("id, name, flag", fn.argumentsList(attributes) );
		Assert.assertEquals("", fn.argumentsList(null) );
		Assert.assertEquals("", fn.argumentsList(new LinkedList<AttributeInContext>()) );
	}
	@Test
	public void testArgumentsList_CSharp() throws Exception { // C#
		EnvInContext envInContext = new EnvInContext();
		envInContext.setLanguage("C#");
		
		List<AttributeInContext> attributes = buildAttributes(envInContext);
		FnInContext fn = new FnInContext(null, envInContext);
		Assert.assertEquals("id, name, flag", fn.argumentsList(attributes) );
		Assert.assertEquals("", fn.argumentsList(null) );
		Assert.assertEquals("", fn.argumentsList(new LinkedList<AttributeInContext>()) );
	}
	@Test
	public void testArgumentsList_GoLang() throws Exception { // Go
		EnvInContext envInContext = new EnvInContext();
		envInContext.setLanguage("Go");
		Assert.assertTrue(envInContext.languageIsGo());
		Assert.assertEquals("GO", envInContext.getLanguage().toUpperCase() );

		List<AttributeInContext> attributes = buildAttributes(envInContext);
		FnInContext fn = new FnInContext(null, envInContext);
		Assert.assertEquals("id, name, flag", fn.argumentsList(attributes) );
		Assert.assertEquals("", fn.argumentsList(null) );
		Assert.assertEquals("", fn.argumentsList(new LinkedList<AttributeInContext>()) );
	}

	//-----------------------------------------------------------------------------------	
	// fn.argumentsListWithType
	//-----------------------------------------------------------------------------------
	@Test
	public void testArgumentsListWithType_Default() throws Exception { // Java : standard
		EnvInContext envInContext = new EnvInContext();
		List<AttributeInContext> attributes = buildAttributes(envInContext);
		FnInContext fn = new FnInContext(null, envInContext);
		Assert.assertEquals("Integer id, String name, Boolean flag", fn.argumentsListWithType(attributes) );
		Assert.assertEquals("", fn.argumentsListWithType(null) );
		Assert.assertEquals("", fn.argumentsListWithType(new LinkedList<AttributeInContext>()) );
	}
	@Test
	public void testArgumentsListWithType_CSharp() throws Exception { // C# : standard
		EnvInContext envInContext = new EnvInContext();
		envInContext.setLanguage("C#");
		List<AttributeInContext> attributes = buildAttributes(envInContext);
		FnInContext fn = new FnInContext(null, envInContext);
		Assert.assertEquals("int id, string name, bool flag", fn.argumentsListWithType(attributes) );
		Assert.assertEquals("", fn.argumentsListWithType(null) );
		Assert.assertEquals("", fn.argumentsListWithType(new LinkedList<AttributeInContext>()) );
	}
	@Test
	public void testArgumentsListWithType_TypeScript() throws Exception { // TypeScript
		EnvInContext envInContext = new EnvInContext();
		envInContext.setLanguage("TypeScript");
		List<AttributeInContext> attributes = buildAttributes(envInContext);
		FnInContext fn = new FnInContext(null, envInContext);
		Assert.assertEquals("number id, string name, boolean flag", fn.argumentsListWithType(attributes) );
		Assert.assertEquals("", fn.argumentsListWithType(null) );
		Assert.assertEquals("", fn.argumentsListWithType(new LinkedList<AttributeInContext>()) );
	}
	@Test
	public void testArgumentsListWithType_GoLang() throws Exception { // Go : inversion 
		EnvInContext envInContext = new EnvInContext();
		envInContext.setLanguage("Go");
		List<AttributeInContext> attributes = buildAttributes(envInContext);
		FnInContext fn = new FnInContext(null, envInContext);
		Assert.assertEquals("id int32, name string, flag bool", fn.argumentsListWithType(attributes) );
		Assert.assertEquals("", fn.argumentsListWithType(null) );
		Assert.assertEquals("", fn.argumentsListWithType(new LinkedList<AttributeInContext>()) );
	}

	//-----------------------------------------------------------------------------------
	// fn.argumentsListWithGetter
	//-----------------------------------------------------------------------------------	
	@Test
	public void testArgumentsListWithGetter_Default() throws Exception { // Default = Java
		EnvInContext envInContext = new EnvInContext();
		List<AttributeInContext> attributes = buildAttributes(envInContext);
		FnInContext fn = new FnInContext(null, envInContext);
		Assert.assertEquals("obj.getId(), obj.getName(), obj.getFlag()", fn.argumentsListWithGetter("obj", attributes) );
		Assert.assertEquals("", fn.argumentsListWithGetter("obj", null) );
		Assert.assertEquals("", fn.argumentsListWithGetter("obj", new LinkedList<AttributeInContext>()) );
	}
	@Test
	public void testArgumentsListWithGetter_GoLang() throws Exception {  // Go : no change
		EnvInContext envInContext = new EnvInContext();
		envInContext.setLanguage("Go");
		List<AttributeInContext> attributes = buildAttributes(envInContext);
		FnInContext fn = new FnInContext(null, envInContext);
		Assert.assertEquals("obj.getId(), obj.getName(), obj.getFlag()", fn.argumentsListWithGetter("obj", attributes) );
		Assert.assertEquals("", fn.argumentsListWithGetter("obj", null) );
		Assert.assertEquals("", fn.argumentsListWithGetter("obj", new LinkedList<AttributeInContext>()) );
	}

	//-----------------------------------------------------------------------------------	
	// fn.argumentsListWithWrapperType
	//-----------------------------------------------------------------------------------	
	@Test
	public void testArgumentsListWithWrapperType_Default() throws Exception { 
		EnvInContext envInContext = new EnvInContext();
		List<AttributeInContext> attributes = buildAttributes(envInContext);
		FnInContext fn = new FnInContext(null, envInContext);
		Assert.assertEquals("Integer id, String name, Boolean flag", fn.argumentsListWithWrapperType(attributes) );
		Assert.assertEquals("", fn.argumentsListWithWrapperType(null) );
		Assert.assertEquals("", fn.argumentsListWithWrapperType(new LinkedList<AttributeInContext>()) );
	}
	@Test
	public void testArgumentsListWithWrapperType_CSharp() throws Exception { // C#
		EnvInContext envInContext = new EnvInContext();
		envInContext.setLanguage("C#");
		List<AttributeInContext> attributes = buildAttributes(envInContext);
		FnInContext fn = new FnInContext(null, envInContext);
		Assert.assertEquals("System.Int32 id, System.String name, System.Boolean flag", fn.argumentsListWithWrapperType(attributes) );
		Assert.assertEquals("", fn.argumentsListWithWrapperType(null) );
		Assert.assertEquals("", fn.argumentsListWithWrapperType(new LinkedList<AttributeInContext>()) );
	}
	@Test
	public void testArgumentsListWithWrapperType_TypeScript() throws Exception { // TypeScript // TODO ????
		EnvInContext envInContext = new EnvInContext();
		envInContext.setLanguage("TypeScript");
		List<AttributeInContext> attributes = buildAttributes(envInContext);
		FnInContext fn = new FnInContext(null, envInContext);
		Assert.assertEquals("Number id, String name, Boolean flag", fn.argumentsListWithWrapperType(attributes) );
		Assert.assertEquals("", fn.argumentsListWithWrapperType(null) );
		Assert.assertEquals("", fn.argumentsListWithWrapperType(new LinkedList<AttributeInContext>()) );
	}
	@Test
	public void testArgumentsListWithWrapperType_GoLang() throws Exception { // Go
		EnvInContext envInContext = new EnvInContext();
		envInContext.setLanguage("Go");
		List<AttributeInContext> attributes = buildAttributes(envInContext);
		FnInContext fn = new FnInContext(null, envInContext);
		Assert.assertEquals("id int32, name string, flag bool", fn.argumentsListWithWrapperType(attributes) );
		Assert.assertEquals("", fn.argumentsListWithWrapperType(null) );
		Assert.assertEquals("", fn.argumentsListWithWrapperType(new LinkedList<AttributeInContext>()) );
	}
}
