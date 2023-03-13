package org.telosys.tools.generator.context;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.telosys.tools.dsl.model.DslModelAttribute;
import org.telosys.tools.generator.GeneratorException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PhpInContextTest {
	
	private PhpInContext getPhpObject() {
		return new PhpInContext();
	}
	
	private AttributeInContext buildAttributeNotNull(String attribName, String neutralType, boolean notNull) throws GeneratorException {
		DslModelAttribute fakeAttribute = new DslModelAttribute(attribName, neutralType);
		fakeAttribute.setNotNull(notNull); // @NotNull
		EnvInContext env = new EnvInContext() ;
		env.setLanguage("php");
		return new AttributeInContext(null, fakeAttribute, null, env );
	}
	private AttributeInContext buildAttributeNotNull(String attribName, String neutralType) throws GeneratorException {
		return buildAttributeNotNull(attribName, neutralType, true);
	}
	private AttributeInContext buildAttributeNullable(String attribName, String neutralType) throws GeneratorException {
		return buildAttributeNotNull(attribName, neutralType, false);
	}

	@Test 
	public void testAttributeTypeNotNull() throws GeneratorException {
		assertEquals("string",   getPhpObject().nullableType( buildAttributeNotNull("x", "string") ) );
		assertEquals("int",      getPhpObject().nullableType( buildAttributeNotNull("x", "int") ) );
		assertEquals("int",      getPhpObject().nullableType( buildAttributeNotNull("x", "short") ) );
		assertEquals("DateTime", getPhpObject().nullableType( buildAttributeNotNull("x", "timestamp") ) );
	}

	@Test 
	public void testAttributeTypeNotNullNoType() throws GeneratorException {
		assertEquals("",         getPhpObject().nullableType( buildAttributeNotNull("x", "date") ) );
		assertEquals("",         getPhpObject().nullableType( buildAttributeNotNull("x", "time") ) );
	}

	@Test 
	public void testAttributeTypeNullable() throws GeneratorException {
		assertEquals("?string",   getPhpObject().nullableType( buildAttributeNullable("x", "string") ) );
		assertEquals("?int",      getPhpObject().nullableType( buildAttributeNullable("x", "int") ) );
		assertEquals("?int",      getPhpObject().nullableType( buildAttributeNullable("x", "short") ) );
		assertEquals("?DateTime", getPhpObject().nullableType( buildAttributeNullable("x", "timestamp") ) );
	}

	@Test 
	public void testAttributeTypeNullableNoType() throws GeneratorException {
		assertEquals("",         getPhpObject().nullableType( buildAttributeNullable("x", "date") ) );
		assertEquals("",         getPhpObject().nullableType( buildAttributeNullable("x", "time") ) );
	}
	
	private static final String PUBLIC_FUNCTION_TOSTRING = "    public function __toString() {" ;
	private static final String CLOSING_BRACE = "    }";
	
	@Test 
	public void testToString() throws GeneratorException {
		List<AttributeInContext> attributes = new LinkedList<>();
		attributes.add(buildAttributeNotNull("id", "int") );
		attributes.add(buildAttributeNotNull("name", "string") );
		attributes.add(buildAttributeNullable("surname", "string") );
		
		String s = getPhpObject().toStringMethod(attributes, 4);
		System.out.println(s);
		assertTrue(s.startsWith(PUBLIC_FUNCTION_TOSTRING));
		assertTrue(s.contains(  "        return"));
		assertTrue(s.contains(  "          \"[\" . $this->id"));
		assertTrue(s.contains(  "        . \"|\" . $this->name"));
		assertTrue(s.contains(  CLOSING_BRACE));
	}
	
	@Test 
	public void testToStringSingleAttribute() throws GeneratorException {
		List<AttributeInContext> attributes = new LinkedList<>();
		attributes.add(buildAttributeNotNull("id", "int") );
		
		String s = getPhpObject().toStringMethod(attributes, 4);
		System.out.println(s);
		assertTrue(s.startsWith(PUBLIC_FUNCTION_TOSTRING));
		assertTrue(s.contains(  "        return"));
		assertTrue(s.contains(  "          \"[\" . $this->id"));
		assertTrue(s.contains(  CLOSING_BRACE));
	}

	@Test 
	public void testToStringNoAttribute() {
		List<AttributeInContext> attributes = new LinkedList<>();
		
		String s = getPhpObject().toStringMethod(attributes, 4);
		System.out.println(s);
		assertTrue(s.startsWith(PUBLIC_FUNCTION_TOSTRING));
		assertTrue(s.contains(  "        return \"\""));
		assertTrue(s.contains(  CLOSING_BRACE));
	}

}
