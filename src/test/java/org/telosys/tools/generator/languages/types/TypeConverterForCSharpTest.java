package org.telosys.tools.generator.languages.types;

import org.junit.Test;
import org.telosys.tools.generic.model.types.NeutralType;

import static org.telosys.tools.generator.languages.types.AttributeTypeConst.NONE;
import static org.telosys.tools.generator.languages.types.AttributeTypeConst.NOT_NULL;
import static org.telosys.tools.generator.languages.types.AttributeTypeConst.OBJECT_TYPE;
import static org.telosys.tools.generator.languages.types.AttributeTypeConst.PRIMITIVE_TYPE;
import static org.telosys.tools.generator.languages.types.AttributeTypeConst.UNSIGNED_TYPE;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TypeConverterForCSharpTest extends AbstractTypeTest {

	@Override
	protected TypeConverter getTypeConverter() {
		return new TypeConverterForCSharp() ;
	}
	
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
		
		checkPrimitiveType( getType(NeutralType.STRING, NONE ),            "string", "String");
		checkPrimitiveType( getType(NeutralType.STRING, NOT_NULL ),        "string", "String");
		checkPrimitiveType( getType(NeutralType.STRING, PRIMITIVE_TYPE ),  "string", "String");
		checkPrimitiveType( getType(NeutralType.STRING, UNSIGNED_TYPE ),   "string", "String");
		checkPrimitiveType( getType(NeutralType.STRING, PRIMITIVE_TYPE + UNSIGNED_TYPE ), "string", "String");
		
		checkObjectType( getType(NeutralType.STRING, OBJECT_TYPE),                 "String", "System.String" );
		checkObjectType( getType(NeutralType.STRING, OBJECT_TYPE + UNSIGNED_TYPE), "String", "System.String" );
	}

	@Test
	public void testBoolean() {
		System.out.println("--- ");
				
		checkPrimitiveType( getType( NeutralType.BOOLEAN, NONE ),                  "bool", "Boolean" );
		checkPrimitiveType( getType( NeutralType.BOOLEAN, NOT_NULL ),              "bool", "Boolean");
		checkPrimitiveType( getType( NeutralType.BOOLEAN, PRIMITIVE_TYPE ),        "bool", "Boolean");
		checkPrimitiveType( getType( NeutralType.BOOLEAN, UNSIGNED_TYPE ),         "bool", "Boolean");
		checkPrimitiveType( getType( NeutralType.BOOLEAN, PRIMITIVE_TYPE + UNSIGNED_TYPE ), "bool", "Boolean");
		
		checkObjectType( getType( NeutralType.BOOLEAN, OBJECT_TYPE),            "Boolean", "System.Boolean" );
		checkObjectType( getType( NeutralType.BOOLEAN, NOT_NULL + OBJECT_TYPE), "Boolean", "System.Boolean" );

	}

	@Test
	public void testByte() {
		System.out.println("--- ");
		checkPrimitiveType( getType( NeutralType.BYTE, NONE ),              "sbyte", "SByte" );
		checkPrimitiveType( getType( NeutralType.BYTE, UNSIGNED_TYPE ),     "byte",  "Byte"  );
		
		checkObjectType( getType( NeutralType.BYTE, OBJECT_TYPE ),                 "SByte", "System.SByte");
		checkObjectType( getType( NeutralType.BYTE, OBJECT_TYPE + UNSIGNED_TYPE ), "Byte",  "System.Byte");
	}

	@Test
	public void testShort() {
		System.out.println("--- ");
		checkPrimitiveType( getType( NeutralType.SHORT, NONE ),              "short",  "Int16" );
		checkPrimitiveType( getType( NeutralType.SHORT, UNSIGNED_TYPE ),     "ushort", "UInt16");
		
		checkObjectType( getType( NeutralType.SHORT, OBJECT_TYPE ),                 "Int16",  "System.Int16");
		checkObjectType( getType( NeutralType.SHORT, OBJECT_TYPE + UNSIGNED_TYPE ), "UInt16", "System.UInt16");
	}

	@Test
	public void testInteger() {
		System.out.println("--- ");
		checkPrimitiveType( getType( NeutralType.INTEGER, NONE ),              "int",  "Int32" );
		checkPrimitiveType( getType( NeutralType.INTEGER, UNSIGNED_TYPE ),     "uint", "UInt32");
		
		checkObjectType( getType( NeutralType.INTEGER, OBJECT_TYPE ),                 "Int32",  "System.Int32" );
		checkObjectType( getType( NeutralType.INTEGER, OBJECT_TYPE + UNSIGNED_TYPE ), "UInt32", "System.UInt32");
	}

	@Test
	public void testLong() {
		System.out.println("--- ");
		checkPrimitiveType( getType( NeutralType.LONG, NONE ),              "long",  "Int64" );
		checkPrimitiveType( getType( NeutralType.LONG, UNSIGNED_TYPE ),     "ulong", "UInt64");
		
		checkObjectType( getType( NeutralType.LONG, OBJECT_TYPE ),                 "Int64",  "System.Int64" );
		checkObjectType( getType( NeutralType.LONG, OBJECT_TYPE + UNSIGNED_TYPE ), "UInt64", "System.UInt64");
	}

	@Test
	public void testDecimal() {
		System.out.println("--- ");

		checkPrimitiveType( getType( NeutralType.DECIMAL, NONE ),              "decimal", "Decimal" );
		checkPrimitiveType( getType( NeutralType.DECIMAL, UNSIGNED_TYPE ),     "decimal", "Decimal");
		
		checkObjectType( getType( NeutralType.DECIMAL, OBJECT_TYPE ),                  "Decimal",  "System.Decimal");
		checkObjectType( getType( NeutralType.DECIMAL, OBJECT_TYPE  + UNSIGNED_TYPE ), "Decimal",  "System.Decimal");
	}

	@Test
	public void testDate() {
		System.out.println("--- ");
		// since ver 4.1.0 : DateOnly instead of DateTime
		checkObjectType( getType( NeutralType.DATE, NONE ),           "DateOnly",  "System.DateOnly" );
		checkObjectType( getType( NeutralType.DATE, UNSIGNED_TYPE ),  "DateOnly",  "System.DateOnly" );
		checkObjectType( getType( NeutralType.DATE, OBJECT_TYPE ),    "DateOnly",  "System.DateOnly" );
		checkObjectType( getType( NeutralType.DATE, NOT_NULL ),       "DateOnly",  "System.DateOnly" );
	}

	@Test
	public void testTime() {
		System.out.println("--- ");
		// since ver 4.1.0 : TimeOnly instead of DateTime
		checkObjectType( getType( NeutralType.TIME, NONE ),           "TimeOnly",  "System.TimeOnly" );
		checkObjectType( getType( NeutralType.TIME, UNSIGNED_TYPE ),  "TimeOnly",  "System.TimeOnly" );
		checkObjectType( getType( NeutralType.TIME, OBJECT_TYPE ),    "TimeOnly",  "System.TimeOnly" );
		checkObjectType( getType( NeutralType.TIME, NOT_NULL ),       "TimeOnly",  "System.TimeOnly" );
	}

	@Test
	public void testTimestamp() {
		System.out.println("--- ");
		checkObjectType( getType( NeutralType.TIMESTAMP, NONE ),           "DateTime",  "System.DateTime" );
		checkObjectType( getType( NeutralType.TIMESTAMP, UNSIGNED_TYPE ),  "DateTime",  "System.DateTime" );
		checkObjectType( getType( NeutralType.TIMESTAMP, OBJECT_TYPE ),    "DateTime",  "System.DateTime" );
		checkObjectType( getType( NeutralType.TIMESTAMP, NOT_NULL ),       "DateTime",  "System.DateTime" );
	}
	
	@Test
	public void testBinary() {
		System.out.println("--- ");
		checkPrimitiveType( getType( NeutralType.BINARY, NONE ),           "byte[]",  "byte[]" );
		checkPrimitiveType( getType( NeutralType.BINARY, UNSIGNED_TYPE ),  "byte[]",  "byte[]" );
		checkPrimitiveType( getType( NeutralType.BINARY, OBJECT_TYPE ),    "byte[]",  "byte[]" );
		checkPrimitiveType( getType( NeutralType.BINARY, NOT_NULL ),       "byte[]",  "byte[]" );
	}
}
