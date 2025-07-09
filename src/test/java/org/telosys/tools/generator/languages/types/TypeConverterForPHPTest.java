package org.telosys.tools.generator.languages.types;

import static org.telosys.tools.generator.languages.types.AttributeTypeConst.NONE;
import static org.telosys.tools.generator.languages.types.AttributeTypeConst.NOT_NULL;
import static org.telosys.tools.generator.languages.types.AttributeTypeConst.OBJECT_TYPE;
import static org.telosys.tools.generator.languages.types.AttributeTypeConst.PRIMITIVE_TYPE;
import static org.telosys.tools.generator.languages.types.AttributeTypeConst.UNSIGNED_TYPE;

import org.junit.Test;
import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generator.context.EnvInContext;
import org.telosys.tools.generic.model.types.NeutralType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TypeConverterForPHPTest extends AbstractTypeTest {

	//---------------------------------------------------------------
	@Override
	protected String getLanguageName() {
		return "PHP" ;
	}
	//---------------------------------------------------------------
	
	private void checkObjectType( LanguageType lt, String simpleType, String fullType) {
		assertNotNull(lt);
		assertFalse ( lt.isPrimitiveType() ) ;
		assertEquals(simpleType, lt.getSimpleType() );
		assertEquals(fullType,   lt.getFullType() );
		assertEquals(simpleType, lt.getWrapperType() );
	}

	private void checkPrimitiveType( LanguageType lt, String primitiveType) {
		assertNotNull(lt);
		assertTrue ( lt.isPrimitiveType() ) ;
		assertEquals(primitiveType, lt.getSimpleType() );
		assertEquals(primitiveType, lt.getFullType() );
		assertEquals(primitiveType, lt.getWrapperType() );
	}

	private static final String PHP_STRING          = "string";
	private static final String PHP_STRING_NULLABLE = "?string";
	@Test
	public void testString() {
		checkPrimitiveType( getType(NeutralType.STRING, NONE ),                           PHP_STRING_NULLABLE );
		checkPrimitiveType( getType(NeutralType.STRING, PRIMITIVE_TYPE ),                 PHP_STRING_NULLABLE );
		checkPrimitiveType( getType(NeutralType.STRING, UNSIGNED_TYPE ),                  PHP_STRING_NULLABLE );
		checkPrimitiveType( getType(NeutralType.STRING, PRIMITIVE_TYPE + UNSIGNED_TYPE ), PHP_STRING_NULLABLE );
		checkPrimitiveType( getType(NeutralType.STRING, OBJECT_TYPE),                     PHP_STRING_NULLABLE );
		checkPrimitiveType( getType(NeutralType.STRING, NOT_NULL ),                       PHP_STRING );
		// force $env.typeWithNullableMark = false
		EnvInContext env = getEnv();
		env.setTypeWithNullableMark(false);
		TypeConverter tc = env.getTypeConverter();
		checkPrimitiveType( tc.getType(new AttributeTypeInfoForTest(NeutralType.STRING, NONE)), PHP_STRING );
	}

	//----------------------------------------------------------------------------------
	private static final String PHP_BOOL          = "bool";
	private static final String PHP_BOOL_NULLABLE = "?bool";
	@Test
	public void testBoolean() {
		checkPrimitiveType( getType(NeutralType.BOOLEAN, NONE ),                           PHP_BOOL_NULLABLE );
		checkPrimitiveType( getType(NeutralType.BOOLEAN, PRIMITIVE_TYPE ),                 PHP_BOOL_NULLABLE );
		checkPrimitiveType( getType(NeutralType.BOOLEAN, UNSIGNED_TYPE ),                  PHP_BOOL_NULLABLE );
		checkPrimitiveType( getType(NeutralType.BOOLEAN, PRIMITIVE_TYPE + UNSIGNED_TYPE ), PHP_BOOL_NULLABLE );
		checkPrimitiveType( getType(NeutralType.BOOLEAN, OBJECT_TYPE),                     PHP_BOOL_NULLABLE );
		checkPrimitiveType( getType(NeutralType.BOOLEAN, NOT_NULL ),                       PHP_BOOL );
		// force $env.typeWithNullableMark = false
		EnvInContext env = getEnv();
		env.setTypeWithNullableMark(false);
		TypeConverter tc = env.getTypeConverter();
		checkPrimitiveType( tc.getType(new AttributeTypeInfoForTest(NeutralType.BOOLEAN, NONE)), PHP_BOOL );
	}

	//----------------------------------------------------------------------------------
	private static final String PHP_INT          = "int";
	private static final String PHP_INT_NULLABLE = "?int";
	private void checkIntTypeExpected(String neutralType) {
		checkPrimitiveType( getType(neutralType, NONE ),                           PHP_INT_NULLABLE );
		checkPrimitiveType( getType(neutralType, PRIMITIVE_TYPE ),                 PHP_INT_NULLABLE );
		checkPrimitiveType( getType(neutralType, UNSIGNED_TYPE ),                  PHP_INT_NULLABLE );
		checkPrimitiveType( getType(neutralType, PRIMITIVE_TYPE + UNSIGNED_TYPE ), PHP_INT_NULLABLE );
		checkPrimitiveType( getType(neutralType, OBJECT_TYPE),                     PHP_INT_NULLABLE );
		checkPrimitiveType( getType(neutralType, NOT_NULL ),                       PHP_INT );
	}
	@Test
	public void testByte() {
		checkIntTypeExpected(NeutralType.BYTE);
	}
	@Test
	public void testShort() {
		checkIntTypeExpected(NeutralType.SHORT);
	}
	@Test
	public void testInteger() {
		checkIntTypeExpected(NeutralType.INTEGER);
	}
	@Test
	public void testLong() {
		checkIntTypeExpected(NeutralType.LONG);
	}

	//----------------------------------------------------------------------------------
	private static final String PHP_FLOAT          = "float";
	private static final String PHP_FLOAT_NULLABLE = "?float";
	private void checkFloatTypeExpected(String neutralType) {
		checkPrimitiveType( getType(neutralType, NONE ),                           PHP_FLOAT_NULLABLE );
		checkPrimitiveType( getType(neutralType, PRIMITIVE_TYPE ),                 PHP_FLOAT_NULLABLE );
		checkPrimitiveType( getType(neutralType, UNSIGNED_TYPE ),                  PHP_FLOAT_NULLABLE );
		checkPrimitiveType( getType(neutralType, PRIMITIVE_TYPE + UNSIGNED_TYPE ), PHP_FLOAT_NULLABLE );
		checkPrimitiveType( getType(neutralType, OBJECT_TYPE),                     PHP_FLOAT_NULLABLE );
		checkPrimitiveType( getType(neutralType, NOT_NULL ),                       PHP_FLOAT );
	}
	@Test
	public void testFloat() {
		checkFloatTypeExpected(NeutralType.FLOAT);
	}
	@Test
	public void testDouble() {
		checkFloatTypeExpected(NeutralType.DOUBLE);
	}
	@Test
	public void testDecimal() {
		checkFloatTypeExpected(NeutralType.DECIMAL);
	}

	//----------------------------------------------------------------------------------
	private static final String PHP_DATETIME          = "DateTime";
	private static final String PHP_DATETIME_NULLABLE = "?DateTime";
	private void checkDateTimeExpected(String neutralType) {
		checkObjectType( getType(neutralType, NONE ),                           PHP_DATETIME_NULLABLE, PHP_DATETIME_NULLABLE );
		checkObjectType( getType(neutralType, PRIMITIVE_TYPE ),                 PHP_DATETIME_NULLABLE, PHP_DATETIME_NULLABLE );
		checkObjectType( getType(neutralType, UNSIGNED_TYPE ),                  PHP_DATETIME_NULLABLE, PHP_DATETIME_NULLABLE );
		checkObjectType( getType(neutralType, PRIMITIVE_TYPE + UNSIGNED_TYPE ), PHP_DATETIME_NULLABLE, PHP_DATETIME_NULLABLE );
		checkObjectType( getType(neutralType, OBJECT_TYPE),                     PHP_DATETIME_NULLABLE, PHP_DATETIME_NULLABLE );
		checkObjectType( getType(neutralType, NOT_NULL ),                  PHP_DATETIME, PHP_DATETIME );
		checkObjectType( getType(neutralType, NOT_NULL + PRIMITIVE_TYPE),  PHP_DATETIME, PHP_DATETIME );
		checkObjectType( getType(neutralType, NOT_NULL + OBJECT_TYPE),     PHP_DATETIME, PHP_DATETIME );
		checkObjectType( getType(neutralType, NOT_NULL + UNSIGNED_TYPE ),  PHP_DATETIME, PHP_DATETIME );
	}
	@Test
	public void testDate() {
		checkDateTimeExpected(NeutralType.DATE);
	}
	@Test
	public void testTime() {
		checkDateTimeExpected(NeutralType.TIME);
	}
	@Test
	public void testTimestamp() {
		checkDateTimeExpected(NeutralType.TIMESTAMP);
	}
	@Test
	public void testDatetime() {
		checkDateTimeExpected(NeutralType.DATETIME);
	}
	@Test
	public void testDatetimeTZ() {
		checkDateTimeExpected(NeutralType.DATETIMETZ);
	}
	@Test
	public void testTimeTZ() {
		checkDateTimeExpected(NeutralType.TIMETZ);
	}

	//----------------------------------------------------------------------------------
//	private void checkVoidExpected(String neutralType) {
//		checkObjectType( getType( neutralType, NONE ),                  "", "" );
//		checkObjectType( getType( neutralType, PRIMITIVE_TYPE ),        "", "" );
//		checkObjectType( getType( neutralType, OBJECT_TYPE),            "", "" );
//		checkObjectType( getType( neutralType, UNSIGNED_TYPE),          "", "" );
//
//		checkObjectType( getType( neutralType, NOT_NULL ),                  "", "");
//		checkObjectType( getType( neutralType, NOT_NULL + PRIMITIVE_TYPE ), "", "");
//		checkObjectType( getType( neutralType, NOT_NULL + OBJECT_TYPE ),    "", "");
//		checkObjectType( getType( neutralType, NOT_NULL + UNSIGNED_TYPE),   "", "");
//	}
	@Test
	public void testBinary() {
		checkPrimitiveType( getType(NeutralType.BINARY, NONE ),                           PHP_STRING_NULLABLE );
		checkPrimitiveType( getType(NeutralType.BINARY, PRIMITIVE_TYPE ),                 PHP_STRING_NULLABLE );
		checkPrimitiveType( getType(NeutralType.BINARY, UNSIGNED_TYPE ),                  PHP_STRING_NULLABLE );
		checkPrimitiveType( getType(NeutralType.BINARY, PRIMITIVE_TYPE + UNSIGNED_TYPE ), PHP_STRING_NULLABLE );
		checkPrimitiveType( getType(NeutralType.BINARY, OBJECT_TYPE),                     PHP_STRING_NULLABLE );
		checkPrimitiveType( getType(NeutralType.BINARY, NOT_NULL ),                       PHP_STRING );
	}
	
	//----------------------------------------------------------------------------------
	@Test
	public void testDefaultCollectionType() { 
		TypeConverter typeConverter = getTypeConverter();
		// Not applicable => void string
		assertEquals("", typeConverter.getCollectionType());
		assertEquals("", typeConverter.getCollectionType("Foo"));
	}

	@Test
	public void testSpecificCollectionType() throws GeneratorException {
		EnvInContext env = new EnvInContext();
		env.setLanguage(getLanguageName());
		env.setCollectionType("Set");
		TypeConverter typeConverter = env.getTypeConverter();
		// Not applicable => void string
		assertEquals("", typeConverter.getCollectionType());
		assertEquals("", typeConverter.getCollectionType("Foo"));
	}

}
