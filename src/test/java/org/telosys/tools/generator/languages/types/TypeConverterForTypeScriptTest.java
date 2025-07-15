package org.telosys.tools.generator.languages.types;

import static org.telosys.tools.generator.languages.types.AttributeTypeConst.NONE;
import static org.telosys.tools.generator.languages.types.AttributeTypeConst.NOT_NULL;
import static org.telosys.tools.generator.languages.types.AttributeTypeConst.OBJECT_TYPE;
import static org.telosys.tools.generator.languages.types.AttributeTypeConst.PRIMITIVE_TYPE;
import static org.telosys.tools.generator.languages.types.AttributeTypeConst.UNSIGNED_TYPE;

import org.junit.Test;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generator.context.EnvInContext;
import org.telosys.tools.generic.model.types.NeutralType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TypeConverterForTypeScriptTest extends AbstractTypeTest {

	//---------------------------------------------------------------
	@Override
	protected String getLanguageName() {
		return "TypeScript" ;
	}
	//---------------------------------------------------------------
	
	private void checkPrimitiveType(String neutralType, String expectedPrimitiveType) {
		println("--- checkPrimitiveType : " + neutralType + " -> " + expectedPrimitiveType );
		// Primitive type expected
		checkPrimitiveType( getType(neutralType, NONE ),                            expectedPrimitiveType );
		checkPrimitiveType( getType(neutralType, NOT_NULL ),                        expectedPrimitiveType );
		checkPrimitiveType( getType(neutralType, PRIMITIVE_TYPE ),                  expectedPrimitiveType );
		checkPrimitiveType( getType(neutralType, UNSIGNED_TYPE ),                   expectedPrimitiveType );
		checkPrimitiveType( getType(neutralType, PRIMITIVE_TYPE + UNSIGNED_TYPE ),  expectedPrimitiveType );
		// Primitive type even if @ObjectType (no Object type expected)
		checkPrimitiveType( getType(neutralType, OBJECT_TYPE ),                     expectedPrimitiveType ); 
		checkPrimitiveType( getType(neutralType, OBJECT_TYPE + PRIMITIVE_TYPE ),    expectedPrimitiveType ); 
		checkPrimitiveType( getType(neutralType, OBJECT_TYPE + NOT_NULL ),          expectedPrimitiveType ); 
	}
	private void checkPrimitiveType( LanguageType lt, String expectedPrimitiveType) {
		assertNotNull(lt);
		assertEquals(expectedPrimitiveType, lt.getSimpleType() );
		assertEquals(expectedPrimitiveType, lt.getFullType() );
		assertTrue ( lt.isPrimitiveType() ) ;
		assertEquals(StrUtil.capitalize(expectedPrimitiveType), lt.getWrapperType() );
	}

	private void checkObjectType(String neutralType, String expectedType) {
		println("--- checkObjectType : " + neutralType + " -> " + expectedType );
		// Object type expected
		checkObjectType( getType(neutralType, NONE ),                            expectedType );
		checkObjectType( getType(neutralType, NOT_NULL ),                        expectedType );
		checkObjectType( getType(neutralType, PRIMITIVE_TYPE ),                  expectedType );
		checkObjectType( getType(neutralType, UNSIGNED_TYPE ),                   expectedType );
		checkObjectType( getType(neutralType, PRIMITIVE_TYPE + UNSIGNED_TYPE ),  expectedType );
		checkObjectType( getType(neutralType, OBJECT_TYPE ),                     expectedType ); 
		checkObjectType( getType(neutralType, OBJECT_TYPE + PRIMITIVE_TYPE ),    expectedType ); 
		checkObjectType( getType(neutralType, OBJECT_TYPE + NOT_NULL ),          expectedType ); 
	}
	private void checkObjectType( LanguageType lt, String expectedType) {
		assertNotNull(lt);
		assertEquals(expectedType, lt.getSimpleType() );
		assertEquals(expectedType, lt.getFullType() );
		assertEquals(expectedType, lt.getWrapperType() );
		assertFalse ( lt.isPrimitiveType() ) ;
	}

	private static final String TS_STRING = "string" ;
	@Test
	public void testString() {
		checkPrimitiveType(NeutralType.STRING, TS_STRING);
	}
	
	private static final String TS_BOOLEAN = "boolean" ;
	@Test
	public void testBoolean() {
		checkPrimitiveType(NeutralType.BOOLEAN, TS_BOOLEAN);
	}

	private static final String TS_NUMBER = "number" ;
	@Test
	public void testByte() {
		checkPrimitiveType(NeutralType.BYTE, TS_NUMBER);
	}
	@Test
	public void testShort() {
		checkPrimitiveType(NeutralType.SHORT, TS_NUMBER);
	}
	@Test
	public void testInt() {
		checkPrimitiveType(NeutralType.INTEGER, TS_NUMBER);
	}
	@Test
	public void testLong() {
		checkPrimitiveType(NeutralType.LONG, TS_NUMBER);
	}
	@Test
	public void testDecimal() {
		checkPrimitiveType(NeutralType.DECIMAL, TS_NUMBER);
	}
	@Test
	public void testFloat() {
		checkPrimitiveType(NeutralType.FLOAT, TS_NUMBER);
	}
	@Test
	public void testDouble() {
		checkPrimitiveType(NeutralType.DOUBLE, TS_NUMBER);
	}

	private static final String TS_DATE = "Date" ;
	@Test
	public void testDate() {
		checkObjectType(NeutralType.DATE, TS_DATE);
	}
	@Test
	public void testTime() {
		// Supposed to always return Date (in any cases) 
		checkObjectType(NeutralType.TIME, TS_DATE);
	}
	@Test
	public void testTimestamp() {
		// Supposed to always return Date (in any cases) 
		checkObjectType(NeutralType.TIMESTAMP, TS_DATE);
	}
	@Test
	public void testDatetime() {
		// Supposed to always return Date (in any cases) 
		checkObjectType(NeutralType.DATETIME, TS_DATE);
	}
	@Test
	public void testDatetimeTZ() {
		// Supposed to always return Date (in any cases) 
		checkObjectType(NeutralType.DATETIMETZ, TS_DATE);
	}
	@Test
	public void testTimeTZ() {
		// Supposed to always return Date (in any cases) 
		checkObjectType(NeutralType.TIMETZ, TS_DATE);
	}

	@Test
	public void testUUID() {
		checkObjectType(NeutralType.UUID, TS_STRING);
	}
	
	@Test
	public void testBinary() {
		checkObjectType(NeutralType.BINARY, "Uint8Array");
	}
	
	@Test
	public void testDefaultCollectionType() { 
		TypeConverter typeConverter = getTypeConverter();
		// "[]" shorthand syntax by default 
		assertEquals("[]", typeConverter.getCollectionType());
		assertEquals("Car[]", typeConverter.getCollectionType("Car"));
	}

	@Test
	public void testSpecificCollectionType() throws GeneratorException {
		EnvInContext env = new EnvInContext();
		env.setLanguage(getLanguageName());
		TypeConverter typeConverter = env.getTypeConverter();
		
		// "Array" is the same as "[]" in TypeScript 
		env.setCollectionType("Array");
		assertEquals("Array", typeConverter.getCollectionType());
		assertEquals("Array<Foo>", typeConverter.getCollectionType("Foo"));
		// "Array" is not case sensitive 
		env.setCollectionType("ARRAY");
		assertEquals("Array", typeConverter.getCollectionType());
		assertEquals("Array<Student>", typeConverter.getCollectionType("Student"));

		// Reset 
		env.setCollectionType(""); 
		assertEquals("[]", typeConverter.getCollectionType());
		assertEquals("Foo[]", typeConverter.getCollectionType("Foo"));

		// "Set" is valid
		env.setCollectionType("Set");
		assertEquals("Set", typeConverter.getCollectionType());
		assertEquals("Set<Foo>", typeConverter.getCollectionType("Foo"));
		// "Set" is not case sensitive 
		env.setCollectionType("SET");
		assertEquals("Set", typeConverter.getCollectionType());
		assertEquals("Set<Bar>", typeConverter.getCollectionType("Bar"));

		// "Other" is invalid => default collection type
		env.setCollectionType("Other");
		assertEquals("[]", typeConverter.getCollectionType());
		assertEquals("Foo[]", typeConverter.getCollectionType("Foo"));
	}

}
