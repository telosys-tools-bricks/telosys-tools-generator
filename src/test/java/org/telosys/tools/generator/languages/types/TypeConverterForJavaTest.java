package org.telosys.tools.generator.languages.types;

import static org.telosys.tools.generator.languages.types.AttributeTypeConst.NONE;
import static org.telosys.tools.generator.languages.types.AttributeTypeConst.NOT_NULL;
import static org.telosys.tools.generator.languages.types.AttributeTypeConst.OBJECT_TYPE;
import static org.telosys.tools.generator.languages.types.AttributeTypeConst.PRIMITIVE_TYPE;
import static org.telosys.tools.generator.languages.types.AttributeTypeConst.UNSIGNED_TYPE;

import java.math.BigDecimal;

import org.junit.Test;
import org.telosys.tools.commons.JavaTypeUtil;
import org.telosys.tools.generic.model.types.NeutralType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TypeConverterForJavaTest { // specific test case 
	
	private static final String LOCAL_DATE      = TypeConverterForJava.LOCAL_DATE_CLASS; 
	private static final String LOCAL_TIME      = TypeConverterForJava.LOCAL_TIME_CLASS; 
	private static final String LOCAL_DATE_TIME = TypeConverterForJava.LOCAL_DATE_TIME_CLASS; 

	private LanguageType getType(TypeConverter tc, AttributeTypeInfo typeInfo ) {
		LanguageType lt = tc.getType(typeInfo);
		System.out.println( typeInfo + " --> " + lt );
		return lt ;
	}
	
	private LanguageType getType(TypeConverter tc, String neutralType, int typeInfo ) {
		AttributeTypeInfo attributeTypeInfo = new AttributeTypeInfoForTest(neutralType, typeInfo);
		return getType(tc, attributeTypeInfo);
	}
	
	private void check( LanguageType lt, Class<?> clazz ) {
		assertNotNull(lt);
		assertEquals(clazz.getSimpleName(), lt.getSimpleType() );
		assertEquals(clazz.getCanonicalName(), lt.getFullType() );
		if ( clazz.isPrimitive() ) {
			assertTrue ( lt.isPrimitiveType() ) ;
		}
		else {
			assertFalse ( lt.isPrimitiveType() ) ;
		}
	}
	private void checkLocalDate( LanguageType lt ) {
		assertNotNull(lt);
		assertEquals(JavaTypeUtil.shortType(LOCAL_DATE), lt.getSimpleType() );
		assertEquals(LOCAL_DATE, lt.getFullType() );
		assertFalse ( lt.isPrimitiveType() ) ;
	}
	private void checkLocalTime( LanguageType lt ) {
		assertNotNull(lt);
		assertEquals(JavaTypeUtil.shortType(LOCAL_TIME), lt.getSimpleType() );
		assertEquals(LOCAL_TIME, lt.getFullType() );
		assertFalse ( lt.isPrimitiveType() ) ;
	}
	private void checkLocalDateTime( LanguageType lt ) {
		assertNotNull(lt);
		assertEquals(JavaTypeUtil.shortType(LOCAL_DATE_TIME), lt.getSimpleType() );
		assertEquals(LOCAL_DATE_TIME, lt.getFullType() );
		assertFalse ( lt.isPrimitiveType() ) ;
	}
	
	@Test
	public void testString() {
		System.out.println("--- ");
		TypeConverter tc = new TypeConverterForJava() ;
		
		check( getType(tc, NeutralType.STRING, NONE ), String.class);
		check( getType(tc, NeutralType.STRING, NOT_NULL ), String.class);
		
		check( getType(tc, NeutralType.STRING, PRIMITIVE_TYPE ), String.class);
		check( getType(tc, NeutralType.STRING, UNSIGNED_TYPE ), String.class);
		check( getType(tc, NeutralType.STRING, PRIMITIVE_TYPE + UNSIGNED_TYPE ), String.class);
		
		check( getType(tc, NeutralType.STRING, OBJECT_TYPE), String.class);
//		check( getType(tc, NeutralType.STRING, SQL_TYPE ), String.class);
//		check( getType(tc, NeutralType.STRING, OBJECT_TYPE + SQL_TYPE), String.class);
	}

	@Test
	public void testBoolean() {
		System.out.println("--- ");
		TypeConverter tc = new TypeConverterForJava() ;
		
		// Default type :
		check( getType(tc, NeutralType.BOOLEAN, NONE ), Boolean.class);
		// Unsigned type : no effect
		check( getType(tc, NeutralType.BOOLEAN, UNSIGNED_TYPE ), Boolean.class);

		// Primitive type 
		check( getType(tc, NeutralType.BOOLEAN, PRIMITIVE_TYPE ), boolean.class);
		check( getType(tc, NeutralType.BOOLEAN, NOT_NULL ), boolean.class);
		check( getType(tc, NeutralType.BOOLEAN, PRIMITIVE_TYPE + NOT_NULL ), boolean.class);
		// not compatible (primitive type has priority)
//		check( getType(tc, NeutralType.BOOLEAN, PRIMITIVE_TYPE + SQL_TYPE  ), boolean.class);
		check( getType(tc, NeutralType.BOOLEAN, PRIMITIVE_TYPE + OBJECT_TYPE  ), boolean.class);
		// Unsigned type : no effect
		check( getType(tc, NeutralType.BOOLEAN, PRIMITIVE_TYPE + UNSIGNED_TYPE  ), boolean.class);

		
		// Object type ( wrapper ) 
		check( getType(tc, NeutralType.BOOLEAN, OBJECT_TYPE ), Boolean.class);
//		check( getType(tc, NeutralType.BOOLEAN, SQL_TYPE ), Boolean.class);		
		check( getType(tc, NeutralType.BOOLEAN, OBJECT_TYPE + NOT_NULL ), Boolean.class);
//		check( getType(tc, NeutralType.BOOLEAN, OBJECT_TYPE + SQL_TYPE ), Boolean.class);
//		check( getType(tc, NeutralType.BOOLEAN, SQL_TYPE + NOT_NULL ), Boolean.class);
		// Unsigned type : no effect
		check( getType(tc, NeutralType.BOOLEAN, OBJECT_TYPE + UNSIGNED_TYPE ), Boolean.class);
//		check( getType(tc, NeutralType.BOOLEAN, SQL_TYPE + UNSIGNED_TYPE ), Boolean.class);
	}

	@Test
	public void testShort() {
		System.out.println("--- ");
		TypeConverter tc = new TypeConverterForJava() ;
		
		// Default type :
		check( getType(tc, NeutralType.SHORT, NONE ), Short.class);
		// Unsigned type : no effect
		check( getType(tc, NeutralType.SHORT, UNSIGNED_TYPE ), Short.class);

		// Primitive type 
		check( getType(tc, NeutralType.SHORT, PRIMITIVE_TYPE   ), short.class);
		check( getType(tc, NeutralType.SHORT, NOT_NULL   ), short.class);
		check( getType(tc, NeutralType.SHORT, PRIMITIVE_TYPE + NOT_NULL  ), short.class);
		// not compatible (primitive type has priority)
		//check( getType(tc, NeutralType.SHORT, PRIMITIVE_TYPE + SQL_TYPE  ), short.class);
		check( getType(tc, NeutralType.SHORT, PRIMITIVE_TYPE + OBJECT_TYPE  ), short.class);
		// Unsigned type : no effect
		check( getType(tc, NeutralType.SHORT, PRIMITIVE_TYPE + UNSIGNED_TYPE  ), short.class);

		// Object type ( wrapper ) 
		check( getType(tc, NeutralType.SHORT, OBJECT_TYPE ), Short.class);
		check( getType(tc, NeutralType.SHORT, OBJECT_TYPE + NOT_NULL), Short.class);
//		check( getType(tc, NeutralType.SHORT, SQL_TYPE), Short.class);
//		check( getType(tc, NeutralType.SHORT, SQL_TYPE + NOT_NULL), Short.class);
//		check( getType(tc, NeutralType.SHORT, SQL_TYPE + OBJECT_TYPE), Short.class);
		// Unsigned type : no effect
		check( getType(tc, NeutralType.SHORT, OBJECT_TYPE + UNSIGNED_TYPE ), Short.class);
//		check( getType(tc, NeutralType.SHORT, SQL_TYPE + UNSIGNED_TYPE ), Short.class);
	}

	@Test
	public void testDecimal() {
		System.out.println("--- ");
		TypeConverter tc = new TypeConverterForJava() ;
		
		// Supposed to always return BigDecimal (in any cases) 
		check( getType(tc, NeutralType.DECIMAL, NONE ), BigDecimal.class);
		check( getType(tc, NeutralType.DECIMAL, NOT_NULL ), BigDecimal.class);
		
		check( getType(tc, NeutralType.DECIMAL, PRIMITIVE_TYPE ), BigDecimal.class);
		check( getType(tc, NeutralType.DECIMAL, UNSIGNED_TYPE ), BigDecimal.class);
		check( getType(tc, NeutralType.DECIMAL, PRIMITIVE_TYPE + UNSIGNED_TYPE ), BigDecimal.class);
		
		check( getType(tc, NeutralType.DECIMAL, OBJECT_TYPE ), BigDecimal.class);
//		check( getType(tc, NeutralType.DECIMAL, SQL_TYPE ), BigDecimal.class);		
		check( getType(tc, NeutralType.DECIMAL, NOT_NULL + OBJECT_TYPE ), BigDecimal.class);
//		check( getType(tc, NeutralType.DECIMAL, NOT_NULL + SQL_TYPE ), BigDecimal.class);
	}

	@Test
	public void testDate() {
		System.out.println("--- ");
		TypeConverter tc = new TypeConverterForJava() ;
		
		// Supposed to always return LocalDate (in any cases) 
		checkLocalDate( getType(tc, NeutralType.DATE, NONE ));
		checkLocalDate( getType(tc, NeutralType.DATE, NOT_NULL ));
		
		checkLocalDate( getType(tc, NeutralType.DATE, PRIMITIVE_TYPE ));
		checkLocalDate( getType(tc, NeutralType.DATE, UNSIGNED_TYPE ));
		checkLocalDate( getType(tc, NeutralType.DATE, PRIMITIVE_TYPE + UNSIGNED_TYPE ));
		
		checkLocalDate( getType(tc, NeutralType.DATE, OBJECT_TYPE ));
		checkLocalDate( getType(tc, NeutralType.DATE, NOT_NULL + OBJECT_TYPE ));
	}

	@Test
	public void testTime() {
		System.out.println("--- ");
		TypeConverter tc = new TypeConverterForJava() ;
		
		// Supposed to always return LocalTime (in any cases) 
		checkLocalTime( getType(tc, NeutralType.TIME, NONE ));
		checkLocalTime( getType(tc, NeutralType.TIME, NOT_NULL ));
		
		checkLocalTime( getType(tc, NeutralType.TIME, PRIMITIVE_TYPE ));
		checkLocalTime( getType(tc, NeutralType.TIME, UNSIGNED_TYPE ));
		checkLocalTime( getType(tc, NeutralType.TIME, PRIMITIVE_TYPE + UNSIGNED_TYPE ));
		
		checkLocalTime( getType(tc, NeutralType.TIME, OBJECT_TYPE ));
		checkLocalTime( getType(tc, NeutralType.TIME, NOT_NULL + OBJECT_TYPE ));
	}

	@Test
	public void testTimestamp() {
		System.out.println("--- ");
		TypeConverter tc = new TypeConverterForJava() ;
		
		// Supposed to always return LocalDateTime (in any cases) 
		checkLocalDateTime( getType(tc, NeutralType.TIMESTAMP, NONE ));
		checkLocalDateTime( getType(tc, NeutralType.TIMESTAMP, NOT_NULL ));
		
		checkLocalDateTime( getType(tc, NeutralType.TIMESTAMP, PRIMITIVE_TYPE ));
		checkLocalDateTime( getType(tc, NeutralType.TIMESTAMP, UNSIGNED_TYPE ));
		checkLocalDateTime( getType(tc, NeutralType.TIMESTAMP, PRIMITIVE_TYPE + UNSIGNED_TYPE ));
		
		checkLocalDateTime( getType(tc, NeutralType.TIMESTAMP, OBJECT_TYPE ));
		checkLocalDateTime( getType(tc, NeutralType.TIMESTAMP, NOT_NULL + OBJECT_TYPE ));
	}

//	@Test
//	public void testLongText() {
//		System.out.println("--- ");
//		TypeConverter tc = new TypeConverterForJava() ;
//		
//		// Supposed to always return BigDecimal (in any cases) 
//		check( getType(tc, NeutralType.LONGTEXT, NONE ), String.class);
//		check( getType(tc, NeutralType.LONGTEXT, NOT_NULL ), String.class);
//		
//		check( getType(tc, NeutralType.LONGTEXT, PRIMITIVE_TYPE ), String.class);
//		check( getType(tc, NeutralType.LONGTEXT, UNSIGNED_TYPE ), String.class);
//		check( getType(tc, NeutralType.LONGTEXT, PRIMITIVE_TYPE + UNSIGNED_TYPE ), String.class);
//		
//		check( getType(tc, NeutralType.LONGTEXT, OBJECT_TYPE ), String.class);
//		check( getType(tc, NeutralType.LONGTEXT, NOT_NULL + OBJECT_TYPE ), String.class);
//
//		check( getType(tc, NeutralType.LONGTEXT, SQL_TYPE ), java.sql.Clob.class);	 // SQL CLOB	
//		check( getType(tc, NeutralType.LONGTEXT, NOT_NULL + SQL_TYPE ), java.sql.Clob.class); // SQL CLOB	
//		check( getType(tc, NeutralType.LONGTEXT, OBJECT_TYPE + SQL_TYPE ), java.sql.Clob.class); // SQL CLOB	
//		
//		check( getType(tc, NeutralType.LONGTEXT, PRIMITIVE_TYPE + OBJECT_TYPE ), String.class); // not compatible (no Prim type => String)
//		check( getType(tc, NeutralType.LONGTEXT, PRIMITIVE_TYPE + SQL_TYPE ), java.sql.Clob.class); // not compatible (no Prim type => SQL CLOB)	
//	}

	@Test
	public void testBinary() {
		System.out.println("--- ");
		TypeConverter tc = new TypeConverterForJava() ;
		
		// 
		check( getType(tc, NeutralType.BINARY, NONE ),      byte[].class);
		check( getType(tc, NeutralType.BINARY, NOT_NULL ),  byte[].class);
		
		check( getType(tc, NeutralType.BINARY, PRIMITIVE_TYPE ),  byte[].class);
		check( getType(tc, NeutralType.BINARY, UNSIGNED_TYPE ),  byte[].class);
		check( getType(tc, NeutralType.BINARY, PRIMITIVE_TYPE + UNSIGNED_TYPE ),  byte[].class);
		
		check( getType(tc, NeutralType.BINARY, OBJECT_TYPE ),  byte[].class);
		check( getType(tc, NeutralType.BINARY, NOT_NULL + OBJECT_TYPE ),  byte[].class);

//		check( getType(tc, NeutralType.BINARY, SQL_TYPE ), java.sql.Blob.class);	 // SQL BLOB	
//		check( getType(tc, NeutralType.BINARY, NOT_NULL + SQL_TYPE ), java.sql.Blob.class); // SQL BLOB	
//		check( getType(tc, NeutralType.BINARY, OBJECT_TYPE + SQL_TYPE ), java.sql.Blob.class); // SQL BLOB	
//		
//		check( getType(tc, NeutralType.BINARY, PRIMITIVE_TYPE + SQL_TYPE ), byte[].class); // not compatible (primitive type has priority)	
	}

	@Test
	public void testPrimitiveTypes() {
		System.out.println("--- ");
		TypeConverter tc = new TypeConverterForJava() ;
		LanguageType lt ;
		
		lt = getType(tc, NeutralType.BOOLEAN, PRIMITIVE_TYPE ) ;
		assertEquals("boolean", lt.getSimpleType());
		assertEquals("boolean", lt.getFullType());
		assertEquals("Boolean", lt.getWrapperType());
		assertTrue(lt.isPrimitiveType());
		
		lt = getType(tc, NeutralType.SHORT, PRIMITIVE_TYPE ) ;
		assertEquals("short", lt.getSimpleType());
		assertEquals("short", lt.getFullType());
		assertEquals("Short", lt.getWrapperType());
		assertTrue(lt.isPrimitiveType());

		lt = getType(tc, NeutralType.INTEGER, PRIMITIVE_TYPE ) ;
		assertEquals("int", lt.getSimpleType());
		assertEquals("int", lt.getFullType());
		assertEquals("Integer", lt.getWrapperType());
		assertTrue(lt.isPrimitiveType());

		lt = getType(tc, NeutralType.LONG, PRIMITIVE_TYPE ) ;
		assertEquals("long", lt.getSimpleType());
		assertEquals("long", lt.getFullType());
		assertEquals("Long", lt.getWrapperType());
		assertTrue(lt.isPrimitiveType());
	}
	
	@Test
	public void testObjectTypes() {
		System.out.println("--- ");
		TypeConverter tc = new TypeConverterForJava() ;
		LanguageType lt ;
		
		lt = getType(tc, NeutralType.BOOLEAN, OBJECT_TYPE ) ;
		assertEquals("Boolean", lt.getSimpleType());
		assertEquals("java.lang.Boolean", lt.getFullType());
		assertEquals("Boolean", lt.getWrapperType());
		assertFalse(lt.isPrimitiveType());

		lt = getType(tc, NeutralType.SHORT, OBJECT_TYPE ) ;
		assertEquals("Short", lt.getSimpleType());
		assertEquals("java.lang.Short", lt.getFullType());
		assertEquals("Short", lt.getWrapperType());
		assertFalse(lt.isPrimitiveType());

		lt = getType(tc, NeutralType.INTEGER, OBJECT_TYPE ) ;
		assertEquals("Integer", lt.getSimpleType());
		assertEquals("java.lang.Integer", lt.getFullType());
		assertEquals("Integer", lt.getWrapperType());
		assertFalse(lt.isPrimitiveType());

	}
}
