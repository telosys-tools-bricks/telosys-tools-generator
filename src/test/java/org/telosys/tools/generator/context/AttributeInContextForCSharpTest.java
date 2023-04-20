package org.telosys.tools.generator.context;

import org.junit.Test;
import org.telosys.tools.commons.exception.TelosysRuntimeException;
import org.telosys.tools.dsl.model.DslModelAttribute;
import org.telosys.tools.generator.GeneratorException;

import static org.junit.Assert.assertEquals;

public class AttributeInContextForCSharpTest {
	
	private static final String STRING = "string";
	/**
	private static final String BYTE = "byte";
	private static final String SHORT = "short";
	private static final String INT = "int";
	private static final String LONG = "long";
	private static final String FLOAT = "float";
	private static final String DOUBLE = "double";
	private static final String DECIMAL = "decimal";
	**/
	//------------------------------------------------------------------------------------
	private EnvInContext buildEnv() {
		EnvInContext env = new EnvInContext();
		try {
			env.setLanguage("C#");
		} catch (GeneratorException e) {
			throw new TelosysRuntimeException(e.getMessage());
		}
		return env;
	}
	//------------------------------------------------------------------------------------
	private AttributeInContext buildAttribute(String attribName, String neutralType, boolean notNull, boolean objectType) {
		DslModelAttribute fakeAttribute = new DslModelAttribute(attribName, neutralType);
		fakeAttribute.setNotNull(notNull);
		fakeAttribute.setObjectTypeExpected(objectType);
		return new AttributeInContext(null, fakeAttribute, null, buildEnv() );
	}
	//------------------------------------------------------------------------------------

	@Test
	public void testString() {
		AttributeInContext attrib = buildAttribute("firstName", "string", true, false); // NOT NULL
		assertEquals("string",      attrib.getType() );
		assertEquals("string    ",  attrib.formattedType(10) );
		assertEquals("String",      attrib.getWrapperType() );
		assertEquals("String    ",  attrib.formattedWrapperType(10) );
		assertEquals("string",      attrib.getSimpleType() );
		assertEquals("string",      attrib.getFullType() );

		attrib = buildAttribute("firstName", STRING, false, false); // NULLABLE
		assertEquals("string?",     attrib.getType() );
		assertEquals("string?   ",  attrib.formattedType(10) );
		assertEquals("String?",     attrib.getWrapperType() );
		assertEquals("String?   ",  attrib.formattedWrapperType(10) );
		assertEquals("string?",     attrib.getSimpleType() );
		assertEquals("string?",     attrib.getFullType() );
		
		attrib = buildAttribute("firstName", STRING, true, true); // NOT NULL + OBJECT
		assertEquals("String",        attrib.getType() );
		assertEquals("String    ",    attrib.formattedType(10) );
		assertEquals("String",        attrib.getWrapperType() );
		assertEquals("String    ",    attrib.formattedWrapperType(10) );
		assertEquals("String",        attrib.getSimpleType() );
		assertEquals("System.String", attrib.getFullType() );

		attrib = buildAttribute("firstName", STRING, false, true); // NULLABLE + OBJECT
		assertEquals("String?",        attrib.getType() );
		assertEquals("String?   ",     attrib.formattedType(10) );
		assertEquals("String?",        attrib.getWrapperType() );
		assertEquals("String?   ",     attrib.formattedWrapperType(10) );
		assertEquals("String?",        attrib.getSimpleType() );
		assertEquals("System.String?", attrib.getFullType() );

	}
	
}
