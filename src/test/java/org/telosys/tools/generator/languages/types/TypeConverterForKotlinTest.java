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

public class TypeConverterForKotlinTest extends AbstractTypeTest {

	//---------------------------------------------------------------
	@Override
	protected String getLanguageName() {
		return "Kotlin" ;
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
	
	private static final String KOTLIN_STRING          = "String";
	private static final String KOTLIN_STRING_NULLABLE = "String?";
	@Test
	public void testString() {
		checkPrimitiveType( getType(NeutralType.STRING, NONE ),                           KOTLIN_STRING_NULLABLE, KOTLIN_STRING_NULLABLE );
		checkPrimitiveType( getType(NeutralType.STRING, PRIMITIVE_TYPE ),                 KOTLIN_STRING_NULLABLE, KOTLIN_STRING_NULLABLE );
		checkPrimitiveType( getType(NeutralType.STRING, UNSIGNED_TYPE ),                  KOTLIN_STRING_NULLABLE, KOTLIN_STRING_NULLABLE );
		checkPrimitiveType( getType(NeutralType.STRING, PRIMITIVE_TYPE + UNSIGNED_TYPE ), KOTLIN_STRING_NULLABLE, KOTLIN_STRING_NULLABLE );
		checkPrimitiveType( getType(NeutralType.STRING, OBJECT_TYPE),                     KOTLIN_STRING_NULLABLE, KOTLIN_STRING_NULLABLE );
		checkPrimitiveType( getType(NeutralType.STRING, NOT_NULL ),                       KOTLIN_STRING, KOTLIN_STRING );
	}

	private static final String KOTLIN_BOOLEAN           = "Boolean";
	private static final String KOTLIN_BOOLEAN_NULLABLE  = "Boolean?";
	@Test
	public void testBoolean() {
		checkPrimitiveType( getType( NeutralType.BOOLEAN, NONE ),                           KOTLIN_BOOLEAN_NULLABLE, KOTLIN_BOOLEAN_NULLABLE );
		checkPrimitiveType( getType( NeutralType.BOOLEAN, PRIMITIVE_TYPE ),                 KOTLIN_BOOLEAN_NULLABLE, KOTLIN_BOOLEAN_NULLABLE );
		checkPrimitiveType( getType( NeutralType.BOOLEAN, UNSIGNED_TYPE ),                  KOTLIN_BOOLEAN_NULLABLE, KOTLIN_BOOLEAN_NULLABLE );
		checkPrimitiveType( getType( NeutralType.BOOLEAN, PRIMITIVE_TYPE + UNSIGNED_TYPE ), KOTLIN_BOOLEAN_NULLABLE, KOTLIN_BOOLEAN_NULLABLE );
		checkPrimitiveType( getType( NeutralType.BOOLEAN, OBJECT_TYPE),                     KOTLIN_BOOLEAN_NULLABLE, KOTLIN_BOOLEAN_NULLABLE );
		checkPrimitiveType( getType( NeutralType.BOOLEAN, NOT_NULL ),                       KOTLIN_BOOLEAN, KOTLIN_BOOLEAN );
		checkPrimitiveType( getType( NeutralType.BOOLEAN, NOT_NULL + OBJECT_TYPE),          KOTLIN_BOOLEAN, KOTLIN_BOOLEAN );
	}

	private static final String KOTLIN_SHORT           = "Short";
	private static final String KOTLIN_SHORT_NULLABLE  = "Short?";
	private static final String KOTLIN_USHORT          = "UShort";
	private static final String KOTLIN_USHORT_NULLABLE = "UShort?";
	@Test
	public void testShort() {
		checkPrimitiveType( getType( NeutralType.SHORT, NONE ),             KOTLIN_SHORT_NULLABLE, KOTLIN_SHORT_NULLABLE );
		checkPrimitiveType( getType( NeutralType.SHORT, OBJECT_TYPE ),      KOTLIN_SHORT_NULLABLE, KOTLIN_SHORT_NULLABLE);
		checkPrimitiveType( getType( NeutralType.SHORT, PRIMITIVE_TYPE ),   KOTLIN_SHORT_NULLABLE, KOTLIN_SHORT_NULLABLE );
		checkPrimitiveType( getType( NeutralType.SHORT, NOT_NULL ),         KOTLIN_SHORT, KOTLIN_SHORT );
		// UNSIGNED 
		checkPrimitiveType( getType( NeutralType.SHORT, UNSIGNED_TYPE ),                   KOTLIN_USHORT_NULLABLE, KOTLIN_USHORT_NULLABLE);
		checkPrimitiveType( getType( NeutralType.SHORT, UNSIGNED_TYPE + PRIMITIVE_TYPE  ), KOTLIN_USHORT_NULLABLE, KOTLIN_USHORT_NULLABLE);
		checkPrimitiveType( getType( NeutralType.SHORT, UNSIGNED_TYPE + OBJECT_TYPE  ),    KOTLIN_USHORT_NULLABLE, KOTLIN_USHORT_NULLABLE);
		checkPrimitiveType( getType( NeutralType.SHORT, UNSIGNED_TYPE + NOT_NULL ),        KOTLIN_USHORT, KOTLIN_USHORT);
	}

	private static final String KOTLIN_INT           = "Int";
	private static final String KOTLIN_INT_NULLABLE  = "Int?";
	private static final String KOTLIN_UINT          = "UInt";
	private static final String KOTLIN_UINT_NULLABLE = "UInt?";
	@Test
	public void testInt() {
		checkPrimitiveType( getType( NeutralType.INTEGER, NONE ),             KOTLIN_INT_NULLABLE, KOTLIN_INT_NULLABLE );
		checkPrimitiveType( getType( NeutralType.INTEGER, OBJECT_TYPE ),      KOTLIN_INT_NULLABLE, KOTLIN_INT_NULLABLE);
		checkPrimitiveType( getType( NeutralType.INTEGER, PRIMITIVE_TYPE ),   KOTLIN_INT_NULLABLE, KOTLIN_INT_NULLABLE );
		checkPrimitiveType( getType( NeutralType.INTEGER, NOT_NULL ),         KOTLIN_INT, KOTLIN_INT );
		// UNSIGNED 
		checkPrimitiveType( getType( NeutralType.INTEGER, UNSIGNED_TYPE ),                  KOTLIN_UINT_NULLABLE, KOTLIN_UINT_NULLABLE);
		checkPrimitiveType( getType( NeutralType.INTEGER, UNSIGNED_TYPE + PRIMITIVE_TYPE ), KOTLIN_UINT_NULLABLE, KOTLIN_UINT_NULLABLE);
		checkPrimitiveType( getType( NeutralType.INTEGER, UNSIGNED_TYPE + OBJECT_TYPE ),    KOTLIN_UINT_NULLABLE, KOTLIN_UINT_NULLABLE);
		checkPrimitiveType( getType( NeutralType.INTEGER, UNSIGNED_TYPE + NOT_NULL ),       KOTLIN_UINT, KOTLIN_UINT);
	}

	private static final String KOTLIN_LONG           = "Long";
	private static final String KOTLIN_LONG_NULLABLE  = "Long?";
	private static final String KOTLIN_ULONG          = "ULong";
	private static final String KOTLIN_ULONG_NULLABLE = "ULong?";
	@Test
	public void testLong() {
		checkPrimitiveType( getType( NeutralType.LONG, NONE ),             KOTLIN_LONG_NULLABLE, KOTLIN_LONG_NULLABLE );
		checkPrimitiveType( getType( NeutralType.LONG, OBJECT_TYPE ),      KOTLIN_LONG_NULLABLE, KOTLIN_LONG_NULLABLE);
		checkPrimitiveType( getType( NeutralType.LONG, PRIMITIVE_TYPE ),   KOTLIN_LONG_NULLABLE, KOTLIN_LONG_NULLABLE );
		checkPrimitiveType( getType( NeutralType.LONG, NOT_NULL ),         KOTLIN_LONG, KOTLIN_LONG );
		// UNSIGNED 
		checkPrimitiveType( getType( NeutralType.LONG, UNSIGNED_TYPE ),                  KOTLIN_ULONG_NULLABLE, KOTLIN_ULONG_NULLABLE);
		checkPrimitiveType( getType( NeutralType.LONG, UNSIGNED_TYPE + PRIMITIVE_TYPE ), KOTLIN_ULONG_NULLABLE, KOTLIN_ULONG_NULLABLE);
		checkPrimitiveType( getType( NeutralType.LONG, UNSIGNED_TYPE + OBJECT_TYPE ),    KOTLIN_ULONG_NULLABLE, KOTLIN_ULONG_NULLABLE);
		checkPrimitiveType( getType( NeutralType.LONG, UNSIGNED_TYPE + NOT_NULL ),       KOTLIN_ULONG, KOTLIN_ULONG);
	}

	private static final String KOTLIN_FLOAT          = "Float";
	private static final String KOTLIN_FLOAT_NULLABLE = "Float?";
	@Test
	public void testFloat() {
		checkPrimitiveType( getType( NeutralType.FLOAT, NONE ),                           KOTLIN_FLOAT_NULLABLE, KOTLIN_FLOAT_NULLABLE );
		checkPrimitiveType( getType( NeutralType.FLOAT, PRIMITIVE_TYPE ),                 KOTLIN_FLOAT_NULLABLE, KOTLIN_FLOAT_NULLABLE );
		checkPrimitiveType( getType( NeutralType.FLOAT, UNSIGNED_TYPE ),                  KOTLIN_FLOAT_NULLABLE, KOTLIN_FLOAT_NULLABLE );
		checkPrimitiveType( getType( NeutralType.FLOAT, PRIMITIVE_TYPE + UNSIGNED_TYPE ), KOTLIN_FLOAT_NULLABLE, KOTLIN_FLOAT_NULLABLE );
		checkPrimitiveType( getType( NeutralType.FLOAT, OBJECT_TYPE),                     KOTLIN_FLOAT_NULLABLE, KOTLIN_FLOAT_NULLABLE );
		checkPrimitiveType( getType( NeutralType.FLOAT, NOT_NULL ),                       KOTLIN_FLOAT, KOTLIN_FLOAT );
		checkPrimitiveType( getType( NeutralType.FLOAT, NOT_NULL + OBJECT_TYPE),          KOTLIN_FLOAT, KOTLIN_FLOAT );
	}

	private static final String KOTLIN_BIGDECIMAL           = "BigDecimal";
	private static final String KOTLIN_BIGDECIMAL_NULLABLE  = "BigDecimal?";
	private static final String KOTLIN_BIGDECIMAL_FULLTYPE           = "java.math.BigDecimal";
	private static final String KOTLIN_BIGDECIMAL_FULLTYPE_NULLABLE  = "java.math.BigDecimal?";
	@Test
	public void testDecimal() {
		checkObjectType( getType( NeutralType.DECIMAL, NONE ),                           KOTLIN_BIGDECIMAL_NULLABLE, KOTLIN_BIGDECIMAL_FULLTYPE_NULLABLE );
		checkObjectType( getType( NeutralType.DECIMAL, UNSIGNED_TYPE ),                  KOTLIN_BIGDECIMAL_NULLABLE, KOTLIN_BIGDECIMAL_FULLTYPE_NULLABLE );
		checkObjectType( getType( NeutralType.DECIMAL, OBJECT_TYPE ),                    KOTLIN_BIGDECIMAL_NULLABLE, KOTLIN_BIGDECIMAL_FULLTYPE_NULLABLE );
		checkObjectType( getType( NeutralType.DECIMAL, PRIMITIVE_TYPE ),                 KOTLIN_BIGDECIMAL_NULLABLE, KOTLIN_BIGDECIMAL_FULLTYPE_NULLABLE );
		checkObjectType( getType( NeutralType.DECIMAL, OBJECT_TYPE + UNSIGNED_TYPE ),    KOTLIN_BIGDECIMAL_NULLABLE, KOTLIN_BIGDECIMAL_FULLTYPE_NULLABLE );
		checkObjectType( getType( NeutralType.DECIMAL, PRIMITIVE_TYPE + UNSIGNED_TYPE ), KOTLIN_BIGDECIMAL_NULLABLE, KOTLIN_BIGDECIMAL_FULLTYPE_NULLABLE );
		checkObjectType( getType( NeutralType.DECIMAL, NOT_NULL ),                       KOTLIN_BIGDECIMAL,          KOTLIN_BIGDECIMAL_FULLTYPE );
	}

	private static final String KOTLIN_LOCALDATE           = "LocalDate";
	private static final String KOTLIN_LOCALDATE_NULLABLE  = "LocalDate?";
	private static final String KOTLIN_LOCALDATE_FULLTYPE           = "java.time.LocalDate";
	private static final String KOTLIN_LOCALDATE_FULLTYPE_NULLABLE  = "java.time.LocalDate?";
	@Test
	public void testDate() {
		checkObjectType( getType( NeutralType.DATE, NONE ),           KOTLIN_LOCALDATE_NULLABLE,  KOTLIN_LOCALDATE_FULLTYPE_NULLABLE );
		checkObjectType( getType( NeutralType.DATE, UNSIGNED_TYPE ),  KOTLIN_LOCALDATE_NULLABLE,  KOTLIN_LOCALDATE_FULLTYPE_NULLABLE );
		checkObjectType( getType( NeutralType.DATE, OBJECT_TYPE ),    KOTLIN_LOCALDATE_NULLABLE,  KOTLIN_LOCALDATE_FULLTYPE_NULLABLE );
		checkObjectType( getType( NeutralType.DATE, PRIMITIVE_TYPE ), KOTLIN_LOCALDATE_NULLABLE,  KOTLIN_LOCALDATE_FULLTYPE_NULLABLE );
		checkObjectType( getType( NeutralType.DATE, NOT_NULL ),       KOTLIN_LOCALDATE,           KOTLIN_LOCALDATE_FULLTYPE );
	}

	private static final String KOTLIN_LOCALTIME           = "LocalTime";
	private static final String KOTLIN_LOCALTIME_NULLABLE  = "LocalTime?";
	private static final String KOTLIN_LOCALTIME_FULLTYPE           = "java.time.LocalTime";
	private static final String KOTLIN_LOCALTIME_FULLTYPE_NULLABLE  = "java.time.LocalTime?";
	@Test
	public void testTime() {
		checkObjectType( getType( NeutralType.TIME, NONE ),           KOTLIN_LOCALTIME_NULLABLE,  KOTLIN_LOCALTIME_FULLTYPE_NULLABLE );
		checkObjectType( getType( NeutralType.TIME, UNSIGNED_TYPE ),  KOTLIN_LOCALTIME_NULLABLE,  KOTLIN_LOCALTIME_FULLTYPE_NULLABLE );
		checkObjectType( getType( NeutralType.TIME, OBJECT_TYPE ),    KOTLIN_LOCALTIME_NULLABLE,  KOTLIN_LOCALTIME_FULLTYPE_NULLABLE );
		checkObjectType( getType( NeutralType.TIME, PRIMITIVE_TYPE ), KOTLIN_LOCALTIME_NULLABLE,  KOTLIN_LOCALTIME_FULLTYPE_NULLABLE );
		checkObjectType( getType( NeutralType.TIME, NOT_NULL ),       KOTLIN_LOCALTIME,           KOTLIN_LOCALTIME_FULLTYPE );
	}

	private static final String KOTLIN_LOCALDATETIME           = "LocalDateTime";
	private static final String KOTLIN_LOCALDATETIME_NULLABLE  = "LocalDateTime?";
	private static final String KOTLIN_LOCALDATETIME_FULLTYPE           = "java.time.LocalDateTime";
	private static final String KOTLIN_LOCALDATETIME_FULLTYPE_NULLABLE  = "java.time.LocalDateTime?";
	@Test
	public void testTimestamp() {
		checkObjectType( getType( NeutralType.TIMESTAMP, NONE ),           KOTLIN_LOCALDATETIME_NULLABLE,  KOTLIN_LOCALDATETIME_FULLTYPE_NULLABLE );
		checkObjectType( getType( NeutralType.TIMESTAMP, UNSIGNED_TYPE ),  KOTLIN_LOCALDATETIME_NULLABLE,  KOTLIN_LOCALDATETIME_FULLTYPE_NULLABLE );
		checkObjectType( getType( NeutralType.TIMESTAMP, OBJECT_TYPE ),    KOTLIN_LOCALDATETIME_NULLABLE,  KOTLIN_LOCALDATETIME_FULLTYPE_NULLABLE );
		checkObjectType( getType( NeutralType.TIMESTAMP, NOT_NULL ),       KOTLIN_LOCALDATETIME,           KOTLIN_LOCALDATETIME_FULLTYPE );
	}

	private static final String KOTLIN_BYTEARRAY          = "ByteArray";
	private static final String KOTLIN_BYTEARRAY_NULLABLE = "ByteArray?";
	@Test
	public void testByteArray() {
		checkPrimitiveType( getType( NeutralType.BINARY, NONE ),                           KOTLIN_BYTEARRAY_NULLABLE, KOTLIN_BYTEARRAY_NULLABLE );
		checkPrimitiveType( getType( NeutralType.BINARY, PRIMITIVE_TYPE ),                 KOTLIN_BYTEARRAY_NULLABLE, KOTLIN_BYTEARRAY_NULLABLE );
		checkPrimitiveType( getType( NeutralType.BINARY, UNSIGNED_TYPE ),                  KOTLIN_BYTEARRAY_NULLABLE, KOTLIN_BYTEARRAY_NULLABLE );
		checkPrimitiveType( getType( NeutralType.BINARY, PRIMITIVE_TYPE + UNSIGNED_TYPE ), KOTLIN_BYTEARRAY_NULLABLE, KOTLIN_BYTEARRAY_NULLABLE );
		checkPrimitiveType( getType( NeutralType.BINARY, OBJECT_TYPE),                     KOTLIN_BYTEARRAY_NULLABLE, KOTLIN_BYTEARRAY_NULLABLE );
		
		checkPrimitiveType( getType( NeutralType.BINARY, NOT_NULL ),                       KOTLIN_BYTEARRAY, KOTLIN_BYTEARRAY );
		checkPrimitiveType( getType( NeutralType.BINARY, NOT_NULL + OBJECT_TYPE),          KOTLIN_BYTEARRAY, KOTLIN_BYTEARRAY );
	}
	
	@Test
	public void testDefaultCollectionType() {
		println("--- ");
		TypeConverter typeConverter = getTypeConverter();
		assertEquals("List", typeConverter.getCollectionType());
		assertEquals("List<Foo>", typeConverter.getCollectionType("Foo"));
	}

	@Test
	public void testSpecificCollectionType() throws GeneratorException {
		println("--- ");
		EnvInContext env = new EnvInContext();
		env.setLanguage(getLanguageName());
		env.setCollectionType("Set");
		TypeConverter typeConverter = env.getTypeConverter();
		
		assertEquals("Set", typeConverter.getCollectionType());
		assertEquals("Set<Foo>", typeConverter.getCollectionType("Foo"));
	}

}
