package org.telosys.tools.generator.context;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.telosys.tools.dsl.model.DslModelAttribute;
import org.telosys.tools.generator.GeneratorException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import junit.env.telosys.tools.generator.fakemodel.FakeEntityBuilder;

public class CsharpInContextTest {
	
	private CsharpInContext getCsharpObject() {
		return new CsharpInContext();
	}
	
	private AttributeInContext buildAttribute(String attribName, String neutralType, boolean notNull) throws GeneratorException {
		DslModelAttribute fakeAttribute = new DslModelAttribute(attribName, neutralType);
		fakeAttribute.setNotNull(notNull); // @NotNull
		EnvInContext env = new EnvInContext() ;
		env.setLanguage("c#");
		return new AttributeInContext(null, fakeAttribute, null, env );
	}
	private AttributeInContext buildAttributeNotNull(String attribName, String neutralType) throws GeneratorException {
		return buildAttribute(attribName, neutralType, true);
	}
	private AttributeInContext buildAttributeNullable(String attribName, String neutralType) throws GeneratorException {
		return buildAttribute(attribName, neutralType, false);
	}

	@Test 
	public void testAttributeTypeNotNull() throws GeneratorException {
		assertEquals("string",   getCsharpObject().nullableType( buildAttributeNotNull("x", "string") ) );
		assertEquals("int",      getCsharpObject().nullableType( buildAttributeNotNull("x", "int") ) );
		assertEquals("short",    getCsharpObject().nullableType( buildAttributeNotNull("x", "short") ) );
		assertEquals("DateTime", getCsharpObject().nullableType( buildAttributeNotNull("x", "timestamp") ) );
		assertEquals("DateOnly", getCsharpObject().nullableType( buildAttributeNotNull("x", "date") ) );
		assertEquals("TimeOnly", getCsharpObject().nullableType( buildAttributeNotNull("x", "time") ) );
	}

	@Test 
	public void testAttributeTypeNullable() throws GeneratorException {
		assertEquals("string?",   getCsharpObject().nullableType( buildAttributeNullable("x", "string") ) );
		assertEquals("int?",      getCsharpObject().nullableType( buildAttributeNullable("x", "int") ) );
		assertEquals("short?",    getCsharpObject().nullableType( buildAttributeNullable("x", "short") ) );
		assertEquals("DateTime?", getCsharpObject().nullableType( buildAttributeNullable("x", "timestamp") ) );
		assertEquals("DateOnly?", getCsharpObject().nullableType( buildAttributeNullable("x", "date") ) );
		assertEquals("TimeOnly?", getCsharpObject().nullableType( buildAttributeNullable("x", "time") ) );
	}
	
	private static final String TABS_PUBLIC_FUNCTION_TOSTRING = "\t\tpublic override string ToString()" ;
	private static final String TABS_CLOSING_BRACE = "\t\t}";
	private static final String SPACES_PUBLIC_FUNCTION_TOSTRING = "    public override string ToString()" ;
	private static final String SPACES_CLOSING_BRACE = "    }";
	
	@Test 
	public void testToString() throws GeneratorException {
		List<AttributeInContext> allAttributes = new LinkedList<>();
		allAttributes.add(buildAttributeNotNull("id", "int") );
		allAttributes.add(buildAttributeNotNull("name", "string") );
		allAttributes.add(buildAttributeNullable("surname", "string") );
		List<AttributeInContext> keyAttributes = new LinkedList<>();
		keyAttributes.add(allAttributes.get(0) );

		CsharpInContext csharp = getCsharpObject();
		
		EntityInContext entity = FakeEntityBuilder.buildEntityInContext("Foo");
		String s = csharp.toStringMethod(entity, allAttributes, 2);
		System.out.println(s);
		assertTrue(s.startsWith(TABS_PUBLIC_FUNCTION_TOSTRING));
		assertTrue(s.contains(  "\t\t\tsb.Append(\"Foo[\");"               )); // Indent = 3 TABS ( 2 +1 ) 
		assertTrue(s.contains(  "\t\t\tsb.Append(\"id=\").Append(id);"     )); // Indent = 3 TABS ( 2 +1 ) 
		assertTrue(s.contains(  "\t\t\tsb.Append(separator).Append(\"name=\").Append(name);" )); // Indent = 3 TABS ( 2 +1 ) 
		assertTrue(s.endsWith(TABS_CLOSING_BRACE));

		s = csharp.toStringMethod(entity, keyAttributes, 2);
		System.out.println(s);
		assertTrue(s.startsWith(TABS_PUBLIC_FUNCTION_TOSTRING));
		assertTrue(s.contains(  "\t\t\tsb.Append(\"Foo[\");"               )); // Indent = 3 TABS ( 2 +1 ) 
		assertTrue(s.contains(  "\t\t\tsb.Append(\"id=\").Append(id);"     )); // Indent = 3 TABS ( 2 +1 ) 
		assertTrue(s.endsWith(TABS_CLOSING_BRACE));

		s = csharp.toStringMethod(entity, allAttributes, 2, "  ");
		System.out.println(s);
		assertTrue(s.startsWith(SPACES_PUBLIC_FUNCTION_TOSTRING));
		assertTrue(s.contains(  "      sb.Append(\"Foo[\");"               )); // Indent = 6 SPACES (level = 2 +1 ) 
		assertTrue(s.contains(  "      sb.Append(\"id=\").Append(id);"     ));  
		assertTrue(s.contains(  "      sb.Append(separator).Append(\"name=\").Append(name);" )); 
		assertTrue(s.endsWith(SPACES_CLOSING_BRACE));
	}
	
	@Test 
	public void testToStringSingleAttribute() throws GeneratorException {
		List<AttributeInContext> attributes = new LinkedList<>();
		attributes.add(buildAttributeNotNull("id", "int") );
		
		String s = getCsharpObject().toStringMethod(FakeEntityBuilder.buildEntityInContext("Foo"), attributes, 2);
		System.out.println(s);
		assertTrue(s.startsWith(TABS_PUBLIC_FUNCTION_TOSTRING));
		assertTrue(s.contains(  "\t\t\tsb.Append(\"Foo[\");"            )); // Indent = 3 TABS ( 2 +1 ) 
		assertTrue(s.contains(  "\t\t\tsb.Append(\"id=\").Append(id);"  )); // Indent = 3 TABS ( 2 +1 ) 
		assertTrue(s.endsWith(  TABS_CLOSING_BRACE));
	}

	@Test 
	public void testToStringNoAttribute() {
		List<AttributeInContext> attributes = new LinkedList<>();
		
		String s = getCsharpObject().toStringMethod(FakeEntityBuilder.buildEntityInContext("Foo"), attributes, 2);
		System.out.println(s);
		assertTrue(s.startsWith(TABS_PUBLIC_FUNCTION_TOSTRING));
		assertTrue(s.contains(  "\t\t\treturn \"Foo [no attribute]\" ;")); // Indent = 3 TABS ( 2 +1 ) 
		assertTrue(s.endsWith(  TABS_CLOSING_BRACE));
	}

}
