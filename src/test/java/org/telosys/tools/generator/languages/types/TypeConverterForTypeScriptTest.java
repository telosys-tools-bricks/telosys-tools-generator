package org.telosys.tools.generator.languages.types;

import static org.telosys.tools.generator.languages.types.AttributeTypeConst.NONE;
import static org.telosys.tools.generator.languages.types.AttributeTypeConst.NOT_NULL;
import static org.telosys.tools.generator.languages.types.AttributeTypeConst.OBJECT_TYPE;
import static org.telosys.tools.generator.languages.types.AttributeTypeConst.PRIMITIVE_TYPE;
import static org.telosys.tools.generator.languages.types.AttributeTypeConst.UNSIGNED_TYPE;

import org.junit.Test;
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
	
	private void checkStringPrimitiveType(int typeInfo ) {
		checkPrimitiveType( getType(NeutralType.STRING, typeInfo ), "string", "String");
	}
	private void checkBooleanPrimitiveType(int typeInfo ) {
		checkPrimitiveType( getType(NeutralType.BOOLEAN, typeInfo ), "boolean", "Boolean");
	}
	private void checkShortPrimitiveType(int typeInfo ) {
		checkPrimitiveType( getType(NeutralType.SHORT, typeInfo ), "number", "Number");
	}
	private void checkDecimalPrimitiveType(int typeInfo ) {
		checkPrimitiveType( getType(NeutralType.DECIMAL, typeInfo ), "number", "Number");
	}
	private void checkDateObjectType(int typeInfo ) {
		checkObjectType( getType(NeutralType.DATE, typeInfo ), "Date", "Date");
	}
	private void checkTimeObjectType(int typeInfo ) {
		checkObjectType( getType(NeutralType.TIME, typeInfo ), "Date", "Date");
	}
	private void checkTimestampObjectType(int typeInfo ) {
		checkObjectType( getType(NeutralType.TIMESTAMP, typeInfo ), "Date", "Date");
	}
	
	private void checkPrimitiveType( LanguageType lt, String primitiveType, String wrapperType) {
		assertNotNull(lt);
		assertEquals(primitiveType, lt.getSimpleType() );
		assertEquals(primitiveType, lt.getFullType() );
		assertTrue ( lt.isPrimitiveType() ) ;
		assertEquals(wrapperType, lt.getWrapperType() );
	}

	private void checkObjectType( LanguageType lt, String simpleType, String fullType) {
		assertNotNull(lt);
		assertEquals(simpleType, lt.getSimpleType() );
		assertEquals(fullType,   lt.getFullType() );
		assertFalse ( lt.isPrimitiveType() ) ;
		assertEquals(fullType,   lt.getWrapperType() );
	}
	
	@Test
	public void testString() {
		println("--- ");
		
		// Primitive type expected
		checkStringPrimitiveType(NONE);
		checkStringPrimitiveType(NOT_NULL);
		checkStringPrimitiveType(PRIMITIVE_TYPE);
		checkStringPrimitiveType(UNSIGNED_TYPE);
		checkStringPrimitiveType(PRIMITIVE_TYPE + UNSIGNED_TYPE);
//		checkStringPrimitiveType(SQL_TYPE);
//		checkStringPrimitiveType(NOT_NULL + SQL_TYPE);
		checkStringPrimitiveType(PRIMITIVE_TYPE + OBJECT_TYPE );
		
		// Object type expected
		checkStringPrimitiveType( OBJECT_TYPE );
//		checkStringPrimitiveType( OBJECT_TYPE + SQL_TYPE );
		checkStringPrimitiveType( OBJECT_TYPE + NOT_NULL );
//		checkStringPrimitiveType( OBJECT_TYPE + NOT_NULL + SQL_TYPE );
		checkStringPrimitiveType( OBJECT_TYPE + UNSIGNED_TYPE );
//		checkStringPrimitiveType( OBJECT_TYPE + UNSIGNED_TYPE + SQL_TYPE );
	}

	@Test
	public void testBoolean() {
		println("--- ");

		// Primitive type expected
		checkBooleanPrimitiveType(NONE);
		checkBooleanPrimitiveType(NOT_NULL);
		checkBooleanPrimitiveType(PRIMITIVE_TYPE);
		checkBooleanPrimitiveType(UNSIGNED_TYPE);
		checkBooleanPrimitiveType(PRIMITIVE_TYPE + UNSIGNED_TYPE);
//		checkBooleanPrimitiveType(SQL_TYPE);
//		checkBooleanPrimitiveType(NOT_NULL + SQL_TYPE);
		checkBooleanPrimitiveType(PRIMITIVE_TYPE + OBJECT_TYPE );
				
		// Object type expected
		checkBooleanPrimitiveType( OBJECT_TYPE );
//		checkBooleanPrimitiveType( OBJECT_TYPE + SQL_TYPE );
		checkBooleanPrimitiveType( OBJECT_TYPE + NOT_NULL );
//		checkBooleanPrimitiveType( OBJECT_TYPE + NOT_NULL + SQL_TYPE );
		checkBooleanPrimitiveType( OBJECT_TYPE + UNSIGNED_TYPE );
//		checkBooleanPrimitiveType( OBJECT_TYPE + UNSIGNED_TYPE + SQL_TYPE );
	}

	@Test
	public void testShort() {
		println("--- ");
		
		// Primitive type expected
		checkShortPrimitiveType(NONE);
		checkShortPrimitiveType(NOT_NULL);
		checkShortPrimitiveType(PRIMITIVE_TYPE);
		checkShortPrimitiveType(UNSIGNED_TYPE);
		checkShortPrimitiveType(PRIMITIVE_TYPE + UNSIGNED_TYPE);
//		checkShortPrimitiveType(SQL_TYPE);
//		checkShortPrimitiveType(NOT_NULL + SQL_TYPE);
		checkShortPrimitiveType(PRIMITIVE_TYPE + OBJECT_TYPE );
				
		// Object type expected
		checkShortPrimitiveType( OBJECT_TYPE );
//		checkShortPrimitiveType( OBJECT_TYPE + SQL_TYPE );
		checkShortPrimitiveType( OBJECT_TYPE + NOT_NULL );
//		checkShortPrimitiveType( OBJECT_TYPE + NOT_NULL + SQL_TYPE );
		checkShortPrimitiveType( OBJECT_TYPE + UNSIGNED_TYPE );
//		checkShortPrimitiveType( OBJECT_TYPE + UNSIGNED_TYPE + SQL_TYPE );
	}

	@Test
	public void testDecimal() {
		println("--- ");
		
		// Primitive type expected
		checkDecimalPrimitiveType(NONE);
		checkDecimalPrimitiveType(NOT_NULL);
		checkDecimalPrimitiveType(PRIMITIVE_TYPE);
		checkDecimalPrimitiveType(UNSIGNED_TYPE);
		checkDecimalPrimitiveType(PRIMITIVE_TYPE + UNSIGNED_TYPE);
//		checkDecimalPrimitiveType(SQL_TYPE);
//		checkDecimalPrimitiveType(NOT_NULL + SQL_TYPE);
		checkDecimalPrimitiveType(PRIMITIVE_TYPE + OBJECT_TYPE );
				
		// Object type expected
		checkDecimalPrimitiveType( OBJECT_TYPE );
//		checkDecimalPrimitiveType( OBJECT_TYPE + SQL_TYPE );
		checkDecimalPrimitiveType( OBJECT_TYPE + NOT_NULL );
//		checkDecimalPrimitiveType( OBJECT_TYPE + NOT_NULL + SQL_TYPE );
		checkDecimalPrimitiveType( OBJECT_TYPE + UNSIGNED_TYPE );
//		checkDecimalPrimitiveType( OBJECT_TYPE + UNSIGNED_TYPE + SQL_TYPE );
	}

	@Test
	public void testDate() {
		println("--- DATE --> Date");
		
		// Supposed to always return Date (in any cases) 
		checkDateObjectType(NONE);
		checkDateObjectType(NOT_NULL);
		checkDateObjectType(PRIMITIVE_TYPE);
		checkDateObjectType(UNSIGNED_TYPE);
		checkDateObjectType(PRIMITIVE_TYPE + UNSIGNED_TYPE);
//		checkDateObjectType(SQL_TYPE);
//		checkDateObjectType(NOT_NULL + SQL_TYPE);
		checkDateObjectType(PRIMITIVE_TYPE + OBJECT_TYPE );

		checkDateObjectType( OBJECT_TYPE );
//		checkDateObjectType( OBJECT_TYPE + SQL_TYPE );
		checkDateObjectType( OBJECT_TYPE + NOT_NULL );
//		checkDateObjectType( OBJECT_TYPE + NOT_NULL + SQL_TYPE );
		checkDateObjectType( OBJECT_TYPE + UNSIGNED_TYPE );
//		checkDateObjectType( OBJECT_TYPE + UNSIGNED_TYPE + SQL_TYPE );
	}

	@Test
	public void testTime() {
		println("--- TIME --> Date");
		// Supposed to always return Date (in any cases) 
		checkTimeObjectType(NONE);
		checkTimeObjectType(NOT_NULL);
		checkTimeObjectType(PRIMITIVE_TYPE);
		checkTimeObjectType(UNSIGNED_TYPE);
		checkTimeObjectType(PRIMITIVE_TYPE + UNSIGNED_TYPE);
//		checkTimeObjectType(SQL_TYPE);
//		checkTimeObjectType(NOT_NULL + SQL_TYPE);
		checkTimeObjectType(PRIMITIVE_TYPE + OBJECT_TYPE );

		checkTimeObjectType( OBJECT_TYPE );
//		checkTimeObjectType( OBJECT_TYPE + SQL_TYPE );
		checkTimeObjectType( OBJECT_TYPE + NOT_NULL );
//		checkTimeObjectType( OBJECT_TYPE + NOT_NULL + SQL_TYPE );
		checkTimeObjectType( OBJECT_TYPE + UNSIGNED_TYPE );
//		checkTimeObjectType( OBJECT_TYPE + UNSIGNED_TYPE + SQL_TYPE );
	}

	@Test
	public void testTimestamp() {
		println("--- TIMESTAMP --> Date");
		// Supposed to always return Date (in any cases) 
		checkTimestampObjectType(NONE);
		checkTimestampObjectType(NOT_NULL);
		checkTimestampObjectType(PRIMITIVE_TYPE);
		checkTimestampObjectType(UNSIGNED_TYPE);
		checkTimestampObjectType(PRIMITIVE_TYPE + UNSIGNED_TYPE);
//		checkTimestampObjectType(SQL_TYPE);
//		checkTimestampObjectType(NOT_NULL + SQL_TYPE);
		checkTimestampObjectType(PRIMITIVE_TYPE + OBJECT_TYPE );

		checkTimestampObjectType( OBJECT_TYPE );
//		checkTimestampObjectType( OBJECT_TYPE + SQL_TYPE );
		checkTimestampObjectType( OBJECT_TYPE + NOT_NULL );
//		checkTimestampObjectType( OBJECT_TYPE + NOT_NULL + SQL_TYPE );
		checkTimestampObjectType( OBJECT_TYPE + UNSIGNED_TYPE );
//		checkTimestampObjectType( OBJECT_TYPE + UNSIGNED_TYPE + SQL_TYPE );
	}

	@Test
	public void testPrimitiveTypes() {
		println("--- ");
		LanguageType lt ;
		
		lt = getType(NeutralType.BOOLEAN, PRIMITIVE_TYPE ) ;
		assertEquals("boolean", lt.getSimpleType());
		assertEquals("boolean", lt.getFullType());
		assertEquals("Boolean", lt.getWrapperType());
		assertTrue(lt.isPrimitiveType());
		
		lt = getType(NeutralType.SHORT, PRIMITIVE_TYPE ) ;
		assertEquals("number", lt.getSimpleType());
		assertEquals("number", lt.getFullType());
		assertEquals("Number", lt.getWrapperType());
		assertTrue(lt.isPrimitiveType());

		lt = getType(NeutralType.INTEGER, PRIMITIVE_TYPE ) ;
		assertEquals("number", lt.getSimpleType());
		assertEquals("number", lt.getFullType());
		assertEquals("Number", lt.getWrapperType());
		assertTrue(lt.isPrimitiveType());

		lt = getType(NeutralType.LONG, PRIMITIVE_TYPE ) ;
		assertEquals("number", lt.getSimpleType());
		assertEquals("number", lt.getFullType());
		assertEquals("Number", lt.getWrapperType());
		assertTrue(lt.isPrimitiveType());
	}
	
	@Test
	public void testObjectTypes() {
		println("--- ");
		LanguageType lt ;
		
		lt = getType(NeutralType.DATE, OBJECT_TYPE ) ;
		assertEquals("Date", lt.getSimpleType());
		assertEquals("Date", lt.getFullType());
		assertEquals("Date", lt.getWrapperType());
		assertFalse(lt.isPrimitiveType());

		lt = getType(NeutralType.TIME, OBJECT_TYPE ) ;
		assertEquals("Date", lt.getSimpleType());
		assertEquals("Date", lt.getFullType());
		assertEquals("Date", lt.getWrapperType());
		assertFalse(lt.isPrimitiveType());

		lt = getType(NeutralType.TIMESTAMP, OBJECT_TYPE ) ;
		assertEquals("Date", lt.getSimpleType());
		assertEquals("Date", lt.getFullType());
		assertEquals("Date", lt.getWrapperType());
		assertFalse(lt.isPrimitiveType());
	}
}
