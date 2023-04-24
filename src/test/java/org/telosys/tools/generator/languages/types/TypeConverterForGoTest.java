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

public class TypeConverterForGoTest extends AbstractTypeTest {

	//---------------------------------------------------------------
	@Override
	protected String getLanguageName() {
		return "Go" ;
	}
	//---------------------------------------------------------------
	
	private void checkPrimitiveType( LanguageType lt, String primitiveType, String wrapperType) {
		assertNotNull(lt);
		assertTrue ( lt.isPrimitiveType() ) ;
		assertEquals(primitiveType, lt.getSimpleType() );
		assertEquals(primitiveType, lt.getFullType() );
		assertEquals(wrapperType, lt.getWrapperType() );
	}

	private void checkObjectType( LanguageType lt, String simpleType, String fullType) {
		assertNotNull(lt);
		assertFalse ( lt.isPrimitiveType() ) ;
		assertEquals(simpleType, lt.getSimpleType() );
		assertEquals(fullType,   lt.getFullType() );
		assertEquals(simpleType, lt.getWrapperType() );
	}
	
	@Test
	public void testString() {
		System.out.println("--- ");
		checkPrimitiveType( getType(NeutralType.STRING, NONE ),            "string", "string");
		checkPrimitiveType( getType(NeutralType.STRING, NOT_NULL ),        "string", "string");
		checkPrimitiveType( getType(NeutralType.STRING, PRIMITIVE_TYPE ),  "string", "string");
		checkPrimitiveType( getType(NeutralType.STRING, UNSIGNED_TYPE ),   "string", "string");
		checkPrimitiveType( getType(NeutralType.STRING, PRIMITIVE_TYPE + UNSIGNED_TYPE ), "string", "string");
		checkPrimitiveType( getType(NeutralType.STRING, OBJECT_TYPE),            "string", "string" );
	}

	@Test
	public void testBoolean() {
		System.out.println("--- ");
		checkPrimitiveType( getType( NeutralType.BOOLEAN, NONE ),                   "bool", "bool" );
		checkPrimitiveType( getType( NeutralType.BOOLEAN, NOT_NULL ),               "bool", "bool" );
		checkPrimitiveType( getType( NeutralType.BOOLEAN, PRIMITIVE_TYPE ),         "bool", "bool" );
		checkPrimitiveType( getType( NeutralType.BOOLEAN, UNSIGNED_TYPE ),          "bool", "bool" );
		checkPrimitiveType( getType( NeutralType.BOOLEAN, PRIMITIVE_TYPE + UNSIGNED_TYPE ), "bool", "bool");
		checkPrimitiveType( getType( NeutralType.BOOLEAN, OBJECT_TYPE),             "bool", "bool" );
		checkPrimitiveType( getType( NeutralType.BOOLEAN, NOT_NULL + OBJECT_TYPE),  "bool", "bool" );
	}

	@Test
	public void testByte() {
		System.out.println("--- ");
		checkPrimitiveType( getType( NeutralType.BYTE, NONE ),                  "byte", "byte" );
		checkPrimitiveType( getType( NeutralType.BYTE, NOT_NULL ),              "byte", "byte" );
		checkPrimitiveType( getType( NeutralType.BYTE, PRIMITIVE_TYPE ),        "byte", "byte" );
		checkPrimitiveType( getType( NeutralType.BYTE, OBJECT_TYPE),            "byte", "byte" );
		checkPrimitiveType( getType( NeutralType.BYTE, NOT_NULL + OBJECT_TYPE), "byte", "byte" );
		// UNSIGNED 
		checkPrimitiveType( getType( NeutralType.BYTE, UNSIGNED_TYPE ),                  "uint8", "uint8");
		checkPrimitiveType( getType( NeutralType.BYTE, PRIMITIVE_TYPE + UNSIGNED_TYPE ), "uint8", "uint8");
		checkPrimitiveType( getType( NeutralType.BYTE, OBJECT_TYPE + UNSIGNED_TYPE ),    "uint8", "uint8");
	}

	@Test
	public void testShort() {
		System.out.println("--- ");
		checkPrimitiveType( getType( NeutralType.SHORT, NONE ),                  "int16", "int16" );
		checkPrimitiveType( getType( NeutralType.SHORT, NOT_NULL ),              "int16", "int16" );
		checkPrimitiveType( getType( NeutralType.SHORT, PRIMITIVE_TYPE ),        "int16", "int16" );
		checkPrimitiveType( getType( NeutralType.SHORT, OBJECT_TYPE),            "int16", "int16" );
		checkPrimitiveType( getType( NeutralType.SHORT, NOT_NULL + OBJECT_TYPE), "int16", "int16" );
		// UNSIGNED 
		checkPrimitiveType( getType( NeutralType.SHORT, UNSIGNED_TYPE ),                  "uint16", "uint16");
		checkPrimitiveType( getType( NeutralType.SHORT, PRIMITIVE_TYPE + UNSIGNED_TYPE ), "uint16", "uint16");
		checkPrimitiveType( getType( NeutralType.SHORT, OBJECT_TYPE + UNSIGNED_TYPE ),    "uint16", "uint16");
	}

	@Test
	public void testInteger() {
		System.out.println("--- ");
		checkPrimitiveType( getType( NeutralType.INTEGER, NONE ),                  "int32", "int32" );
		checkPrimitiveType( getType( NeutralType.INTEGER, NOT_NULL ),              "int32", "int32" );
		checkPrimitiveType( getType( NeutralType.INTEGER, PRIMITIVE_TYPE ),        "int32", "int32" );
		checkPrimitiveType( getType( NeutralType.INTEGER, OBJECT_TYPE),            "int32", "int32" );
		checkPrimitiveType( getType( NeutralType.INTEGER, NOT_NULL + OBJECT_TYPE), "int32", "int32" );
		// UNSIGNED 
		checkPrimitiveType( getType( NeutralType.INTEGER, UNSIGNED_TYPE ),                  "uint32", "uint32");
		checkPrimitiveType( getType( NeutralType.INTEGER, PRIMITIVE_TYPE + UNSIGNED_TYPE ), "uint32", "uint32");
		checkPrimitiveType( getType( NeutralType.INTEGER, OBJECT_TYPE + UNSIGNED_TYPE ),    "uint32", "uint32");
	}

	@Test
	public void testLong() {
		System.out.println("--- ");
		checkPrimitiveType( getType( NeutralType.LONG, NONE ),                  "int64", "int64" );
		checkPrimitiveType( getType( NeutralType.LONG, NOT_NULL ),              "int64", "int64");
		checkPrimitiveType( getType( NeutralType.LONG, PRIMITIVE_TYPE ),        "int64", "int64");
		checkPrimitiveType( getType( NeutralType.LONG, OBJECT_TYPE),            "int64", "int64" );
		checkPrimitiveType( getType( NeutralType.LONG, NOT_NULL + OBJECT_TYPE), "int64", "int64" );
		// UNSIGNED 
		checkPrimitiveType( getType( NeutralType.LONG, UNSIGNED_TYPE ),                  "uint64", "uint64");
		checkPrimitiveType( getType( NeutralType.LONG, PRIMITIVE_TYPE + UNSIGNED_TYPE ), "uint64", "uint64");
		checkPrimitiveType( getType( NeutralType.LONG, OBJECT_TYPE + UNSIGNED_TYPE ),    "uint64", "uint64");
	}

	@Test
	public void testFloat() {
		System.out.println("--- ");
		checkPrimitiveType( getType( NeutralType.FLOAT, NONE ),                  "float32", "float32" );
		checkPrimitiveType( getType( NeutralType.FLOAT, NOT_NULL ),              "float32", "float32");
		checkPrimitiveType( getType( NeutralType.FLOAT, PRIMITIVE_TYPE ),        "float32", "float32");
		checkPrimitiveType( getType( NeutralType.FLOAT, OBJECT_TYPE),            "float32", "float32" );
		checkPrimitiveType( getType( NeutralType.FLOAT, NOT_NULL + OBJECT_TYPE), "float32", "float32" );
		// UNSIGNED : no difference 
		checkPrimitiveType( getType( NeutralType.FLOAT, UNSIGNED_TYPE ),                  "float32", "float32");
		checkPrimitiveType( getType( NeutralType.FLOAT, PRIMITIVE_TYPE + UNSIGNED_TYPE ), "float32", "float32");
		checkPrimitiveType( getType( NeutralType.FLOAT, OBJECT_TYPE + UNSIGNED_TYPE ),    "float32", "float32");
	}

	@Test
	public void testDouble() {
		System.out.println("--- ");
		checkPrimitiveType( getType( NeutralType.DOUBLE, NONE ),                  "float64", "float64" );
		checkPrimitiveType( getType( NeutralType.DOUBLE, NOT_NULL ),              "float64", "float64");
		checkPrimitiveType( getType( NeutralType.DOUBLE, PRIMITIVE_TYPE ),        "float64", "float64");
		checkPrimitiveType( getType( NeutralType.DOUBLE, OBJECT_TYPE),            "float64", "float64" );
		checkPrimitiveType( getType( NeutralType.DOUBLE, NOT_NULL + OBJECT_TYPE), "float64", "float64" );
		// UNSIGNED  : no difference 
		checkPrimitiveType( getType( NeutralType.DOUBLE, UNSIGNED_TYPE ),                  "float64", "float64");
		checkPrimitiveType( getType( NeutralType.DOUBLE, PRIMITIVE_TYPE + UNSIGNED_TYPE ), "float64", "float64");
		checkPrimitiveType( getType( NeutralType.DOUBLE, OBJECT_TYPE + UNSIGNED_TYPE ),    "float64", "float64");
	}

	@Test
	public void testDecimal() {
		System.out.println("--- ");
		checkPrimitiveType( getType( NeutralType.DECIMAL, NONE ),                  "float64", "float64" );
		checkPrimitiveType( getType( NeutralType.DECIMAL, NOT_NULL ),              "float64", "float64");
		checkPrimitiveType( getType( NeutralType.DECIMAL, PRIMITIVE_TYPE ),        "float64", "float64");
		checkPrimitiveType( getType( NeutralType.DECIMAL, OBJECT_TYPE),            "float64", "float64" );
		checkPrimitiveType( getType( NeutralType.DECIMAL, NOT_NULL + OBJECT_TYPE), "float64", "float64" );
		// UNSIGNED  : no difference 
		checkPrimitiveType( getType( NeutralType.DECIMAL, UNSIGNED_TYPE ),                  "float64", "float64");
		checkPrimitiveType( getType( NeutralType.DECIMAL, PRIMITIVE_TYPE + UNSIGNED_TYPE ), "float64", "float64");
		checkPrimitiveType( getType( NeutralType.DECIMAL, OBJECT_TYPE + UNSIGNED_TYPE ),    "float64", "float64");
	}

	@Test
	public void testBinary() {
		System.out.println("--- ");
		checkPrimitiveType( getType( NeutralType.BINARY, NONE ),                  "[]byte", "[]byte" );
		checkPrimitiveType( getType( NeutralType.BINARY, NOT_NULL ),              "[]byte", "[]byte");
		checkPrimitiveType( getType( NeutralType.BINARY, PRIMITIVE_TYPE ),        "[]byte", "[]byte");
		checkPrimitiveType( getType( NeutralType.BINARY, OBJECT_TYPE),            "[]byte", "[]byte" );
		checkPrimitiveType( getType( NeutralType.BINARY, NOT_NULL + OBJECT_TYPE), "[]byte", "[]byte" );
		// UNSIGNED  : no difference 
		checkPrimitiveType( getType( NeutralType.BINARY, UNSIGNED_TYPE ),                  "[]byte", "[]byte");
		checkPrimitiveType( getType( NeutralType.BINARY, PRIMITIVE_TYPE + UNSIGNED_TYPE ), "[]byte", "[]byte");
		checkPrimitiveType( getType( NeutralType.BINARY, OBJECT_TYPE + UNSIGNED_TYPE ),    "[]byte", "[]byte");
	}

	@Test
	public void testDate() {
		System.out.println("--- ");
		checkObjectType( getType( NeutralType.DATE, NONE ),                           "time.Time", "time.Time" );
		checkObjectType( getType( NeutralType.DATE, NOT_NULL ),                       "time.Time", "time.Time" );
		checkObjectType( getType( NeutralType.DATE, PRIMITIVE_TYPE ),                 "time.Time", "time.Time" );
		checkObjectType( getType( NeutralType.DATE, OBJECT_TYPE),                     "time.Time", "time.Time" );
		checkObjectType( getType( NeutralType.DATE, NOT_NULL + OBJECT_TYPE),          "time.Time", "time.Time" );
		checkObjectType( getType( NeutralType.DATE, UNSIGNED_TYPE ),                  "time.Time", "time.Time" );
		checkObjectType( getType( NeutralType.DATE, PRIMITIVE_TYPE + UNSIGNED_TYPE ), "time.Time", "time.Time" );
		checkObjectType( getType( NeutralType.DATE, OBJECT_TYPE + UNSIGNED_TYPE ),    "time.Time", "time.Time" );
	}

	@Test
	public void testTime() {
		System.out.println("--- ");
		checkObjectType( getType( NeutralType.TIME, NONE ),                           "time.Time", "time.Time" );
		checkObjectType( getType( NeutralType.TIME, NOT_NULL ),                       "time.Time", "time.Time" );
		checkObjectType( getType( NeutralType.TIME, PRIMITIVE_TYPE ),                 "time.Time", "time.Time" );
		checkObjectType( getType( NeutralType.TIME, OBJECT_TYPE),                     "time.Time", "time.Time" );
		checkObjectType( getType( NeutralType.TIME, NOT_NULL + OBJECT_TYPE),          "time.Time", "time.Time" );
		checkObjectType( getType( NeutralType.TIME, UNSIGNED_TYPE ),                  "time.Time", "time.Time" );
		checkObjectType( getType( NeutralType.TIME, PRIMITIVE_TYPE + UNSIGNED_TYPE ), "time.Time", "time.Time" );
		checkObjectType( getType( NeutralType.TIME, OBJECT_TYPE + UNSIGNED_TYPE ),    "time.Time", "time.Time" );
	}

	@Test
	public void testTimestamp() {
		System.out.println("--- ");
		checkObjectType( getType( NeutralType.TIMESTAMP, NONE ),                           "time.Time", "time.Time" );
		checkObjectType( getType( NeutralType.TIMESTAMP, NOT_NULL ),                       "time.Time", "time.Time" );
		checkObjectType( getType( NeutralType.TIMESTAMP, PRIMITIVE_TYPE ),                 "time.Time", "time.Time" );
		checkObjectType( getType( NeutralType.TIMESTAMP, OBJECT_TYPE),                     "time.Time", "time.Time" );
		checkObjectType( getType( NeutralType.TIMESTAMP, NOT_NULL + OBJECT_TYPE),          "time.Time", "time.Time" );
		checkObjectType( getType( NeutralType.TIMESTAMP, UNSIGNED_TYPE ),                  "time.Time", "time.Time" );
		checkObjectType( getType( NeutralType.TIMESTAMP, PRIMITIVE_TYPE + UNSIGNED_TYPE ), "time.Time", "time.Time" );
		checkObjectType( getType( NeutralType.TIMESTAMP, OBJECT_TYPE + UNSIGNED_TYPE ),    "time.Time", "time.Time" );
	}

}
