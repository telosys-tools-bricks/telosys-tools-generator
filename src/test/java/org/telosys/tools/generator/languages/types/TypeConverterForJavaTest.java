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

public class TypeConverterForJavaTest extends AbstractTypeTest {

	private static final String STRING = "String";
	private static final String JAVA_LANG_STRING = "java.lang.String";
	
	private static final String BOOLEAN_PRIMITIVE = "boolean" ;
	private static final String BOOLEAN = "Boolean" ;
	private static final String JAVA_LANG_BOOLEAN = "java.lang.Boolean";

	private static final String BYTE_PRIMITIVE = "byte" ;
	private static final String BYTE = "Byte" ;
	private static final String JAVA_LANG_BYTE = "java.lang.Byte";
	
	private static final String SHORT_PRIMITIVE = "short" ;
	private static final String SHORT = "Short" ;
	private static final String JAVA_LANG_SHORT = "java.lang.Short";
	
	private static final String INTEGER_PRIMITIVE = "int" ;
	private static final String INTEGER = "Integer" ;
	private static final String JAVA_LANG_INTEGER = "java.lang.Integer";

	private static final String LONG_PRIMITIVE = "long" ;
	private static final String LONG = "Long" ;
	private static final String JAVA_LANG_LONG = "java.lang.Long";
	
	private static final String FLOAT_PRIMITIVE = "float" ;
	private static final String FLOAT = "Float" ;
	private static final String JAVA_LANG_FLOAT = "java.lang.Float";
	
	private static final String DOUBLE_PRIMITIVE = "double" ;
	private static final String DOUBLE = "Double" ;
	private static final String JAVA_LANG_DOUBLE = "java.lang.Double";

	private static final String DECIMAL = "BigDecimal" ;
	private static final String JAVA_LANG_DECIMAL = "java.math.BigDecimal";
	
	private static final String LOCALDATE = "LocalDate" ;
	private static final String JAVA_TIME_LOCALDATE = "java.time.LocalDate";

	private static final String LOCALTIME = "LocalTime" ;
	private static final String JAVA_TIME_LOCALTIME = "java.time.LocalTime";

	private static final String LOCALDATETIME = "LocalDateTime" ;
	private static final String JAVA_TIME_LOCALDATETIME = "java.time.LocalDateTime";

	private static final String BYTE_ARRAY = "byte[]" ;
	
	//---------------------------------------------------------------
	@Override
	protected String getLanguageName() {
		return "Java" ;
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
		println("--- ");
		checkObjectType( getType(NeutralType.STRING, NONE ),                            STRING, JAVA_LANG_STRING);
		checkObjectType( getType(NeutralType.STRING, NOT_NULL ),                        STRING, JAVA_LANG_STRING);
		checkObjectType( getType(NeutralType.STRING, PRIMITIVE_TYPE ),                  STRING, JAVA_LANG_STRING);
		checkObjectType( getType(NeutralType.STRING, UNSIGNED_TYPE ),                   STRING, JAVA_LANG_STRING);
		checkObjectType( getType(NeutralType.STRING, PRIMITIVE_TYPE + UNSIGNED_TYPE ),  STRING, JAVA_LANG_STRING);
		checkObjectType( getType(NeutralType.STRING, OBJECT_TYPE),                      STRING, JAVA_LANG_STRING );
	}

	@Test
	public void testBoolean() { 
		println("--- ");
		checkObjectType( getType( NeutralType.BOOLEAN, NONE ),                   BOOLEAN, JAVA_LANG_BOOLEAN );
		checkObjectType( getType( NeutralType.BOOLEAN, UNSIGNED_TYPE ),          BOOLEAN, JAVA_LANG_BOOLEAN );
		checkObjectType( getType( NeutralType.BOOLEAN, OBJECT_TYPE),             BOOLEAN, JAVA_LANG_BOOLEAN );
		checkObjectType( getType( NeutralType.BOOLEAN, NOT_NULL + OBJECT_TYPE),  BOOLEAN, JAVA_LANG_BOOLEAN );
		
		checkPrimitiveType( getType( NeutralType.BOOLEAN, NOT_NULL ),                       BOOLEAN_PRIMITIVE, BOOLEAN );
		checkPrimitiveType( getType( NeutralType.BOOLEAN, PRIMITIVE_TYPE ),                 BOOLEAN_PRIMITIVE, BOOLEAN );
		checkPrimitiveType( getType( NeutralType.BOOLEAN, PRIMITIVE_TYPE + UNSIGNED_TYPE ), BOOLEAN_PRIMITIVE, BOOLEAN );
	}

	@Test
	public void testByte() {
		println("--- ");
		checkObjectType( getType( NeutralType.BYTE, NONE ),                        BYTE, JAVA_LANG_BYTE );
		checkObjectType( getType( NeutralType.BYTE, UNSIGNED_TYPE ),               BYTE, JAVA_LANG_BYTE );		
		checkObjectType( getType( NeutralType.BYTE, OBJECT_TYPE),                  BYTE, JAVA_LANG_BYTE );
		checkObjectType( getType( NeutralType.BYTE, OBJECT_TYPE + NOT_NULL),       BYTE, JAVA_LANG_BYTE );
		checkObjectType( getType( NeutralType.BYTE, OBJECT_TYPE + UNSIGNED_TYPE ), BYTE, JAVA_LANG_BYTE );

		checkPrimitiveType( getType( NeutralType.BYTE, NOT_NULL ),                       BYTE_PRIMITIVE, BYTE );
		checkPrimitiveType( getType( NeutralType.BYTE, PRIMITIVE_TYPE ),                 BYTE_PRIMITIVE, BYTE );
		checkPrimitiveType( getType( NeutralType.BYTE, PRIMITIVE_TYPE + NOT_NULL),       BYTE_PRIMITIVE, BYTE );
		checkPrimitiveType( getType( NeutralType.BYTE, PRIMITIVE_TYPE + UNSIGNED_TYPE ), BYTE_PRIMITIVE, BYTE );
	}

	@Test
	public void testShort() {
		println("--- ");
		checkObjectType( getType( NeutralType.SHORT, NONE ),                        SHORT, JAVA_LANG_SHORT );
		checkObjectType( getType( NeutralType.SHORT, UNSIGNED_TYPE ),               SHORT, JAVA_LANG_SHORT );		
		checkObjectType( getType( NeutralType.SHORT, OBJECT_TYPE),                  SHORT, JAVA_LANG_SHORT );
		checkObjectType( getType( NeutralType.SHORT, OBJECT_TYPE + NOT_NULL),       SHORT, JAVA_LANG_SHORT );
		checkObjectType( getType( NeutralType.SHORT, OBJECT_TYPE + UNSIGNED_TYPE ), SHORT, JAVA_LANG_SHORT );

		checkPrimitiveType( getType( NeutralType.SHORT, NOT_NULL ),                       SHORT_PRIMITIVE, SHORT );
		checkPrimitiveType( getType( NeutralType.SHORT, PRIMITIVE_TYPE ),                 SHORT_PRIMITIVE, SHORT );
		checkPrimitiveType( getType( NeutralType.SHORT, PRIMITIVE_TYPE + NOT_NULL),       SHORT_PRIMITIVE, SHORT );
		checkPrimitiveType( getType( NeutralType.SHORT, PRIMITIVE_TYPE + UNSIGNED_TYPE ), SHORT_PRIMITIVE, SHORT );
	}

	@Test
	public void testInteger() {
		println("--- ");
		checkObjectType( getType( NeutralType.INTEGER, NONE ),                        INTEGER, JAVA_LANG_INTEGER );
		checkObjectType( getType( NeutralType.INTEGER, UNSIGNED_TYPE ),               INTEGER, JAVA_LANG_INTEGER );		
		checkObjectType( getType( NeutralType.INTEGER, OBJECT_TYPE),                  INTEGER, JAVA_LANG_INTEGER );
		checkObjectType( getType( NeutralType.INTEGER, OBJECT_TYPE + NOT_NULL),       INTEGER, JAVA_LANG_INTEGER );
		checkObjectType( getType( NeutralType.INTEGER, OBJECT_TYPE + UNSIGNED_TYPE ), INTEGER, JAVA_LANG_INTEGER );

		checkPrimitiveType( getType( NeutralType.INTEGER, NOT_NULL ),                       INTEGER_PRIMITIVE, INTEGER );
		checkPrimitiveType( getType( NeutralType.INTEGER, PRIMITIVE_TYPE ),                 INTEGER_PRIMITIVE, INTEGER );
		checkPrimitiveType( getType( NeutralType.INTEGER, PRIMITIVE_TYPE + NOT_NULL),       INTEGER_PRIMITIVE, INTEGER );
		checkPrimitiveType( getType( NeutralType.INTEGER, PRIMITIVE_TYPE + UNSIGNED_TYPE ), INTEGER_PRIMITIVE, INTEGER );
	}

	@Test
	public void testLong() {
		println("--- ");
		checkObjectType( getType( NeutralType.LONG, NONE ),                        LONG, JAVA_LANG_LONG );
		checkObjectType( getType( NeutralType.LONG, UNSIGNED_TYPE ),               LONG, JAVA_LANG_LONG );		
		checkObjectType( getType( NeutralType.LONG, OBJECT_TYPE),                  LONG, JAVA_LANG_LONG );
		checkObjectType( getType( NeutralType.LONG, OBJECT_TYPE + NOT_NULL),       LONG, JAVA_LANG_LONG );
		checkObjectType( getType( NeutralType.LONG, OBJECT_TYPE + UNSIGNED_TYPE ), LONG, JAVA_LANG_LONG );

		checkPrimitiveType( getType( NeutralType.LONG, NOT_NULL ),                       LONG_PRIMITIVE, LONG );
		checkPrimitiveType( getType( NeutralType.LONG, PRIMITIVE_TYPE ),                 LONG_PRIMITIVE, LONG );
		checkPrimitiveType( getType( NeutralType.LONG, PRIMITIVE_TYPE + NOT_NULL),       LONG_PRIMITIVE, LONG );
		checkPrimitiveType( getType( NeutralType.LONG, PRIMITIVE_TYPE + UNSIGNED_TYPE ), LONG_PRIMITIVE, LONG );
	}

	@Test
	public void testFloat() {
		println("--- ");
		checkObjectType( getType( NeutralType.FLOAT, NONE ),                        FLOAT, JAVA_LANG_FLOAT );
		checkObjectType( getType( NeutralType.FLOAT, UNSIGNED_TYPE ),               FLOAT, JAVA_LANG_FLOAT );		
		checkObjectType( getType( NeutralType.FLOAT, OBJECT_TYPE),                  FLOAT, JAVA_LANG_FLOAT );
		checkObjectType( getType( NeutralType.FLOAT, OBJECT_TYPE + NOT_NULL),       FLOAT, JAVA_LANG_FLOAT );
		checkObjectType( getType( NeutralType.FLOAT, OBJECT_TYPE + UNSIGNED_TYPE ), FLOAT, JAVA_LANG_FLOAT );

		checkPrimitiveType( getType( NeutralType.FLOAT, NOT_NULL ),                       FLOAT_PRIMITIVE, FLOAT );
		checkPrimitiveType( getType( NeutralType.FLOAT, PRIMITIVE_TYPE ),                 FLOAT_PRIMITIVE, FLOAT );
		checkPrimitiveType( getType( NeutralType.FLOAT, PRIMITIVE_TYPE + NOT_NULL),       FLOAT_PRIMITIVE, FLOAT );
		checkPrimitiveType( getType( NeutralType.FLOAT, PRIMITIVE_TYPE + UNSIGNED_TYPE ), FLOAT_PRIMITIVE, FLOAT );
	}

	@Test
	public void testDouble() {
		println("--- ");
		checkObjectType( getType( NeutralType.DOUBLE, NONE ),                        DOUBLE, JAVA_LANG_DOUBLE );
		checkObjectType( getType( NeutralType.DOUBLE, UNSIGNED_TYPE ),               DOUBLE, JAVA_LANG_DOUBLE );		
		checkObjectType( getType( NeutralType.DOUBLE, OBJECT_TYPE),                  DOUBLE, JAVA_LANG_DOUBLE );
		checkObjectType( getType( NeutralType.DOUBLE, OBJECT_TYPE + NOT_NULL),       DOUBLE, JAVA_LANG_DOUBLE );
		checkObjectType( getType( NeutralType.DOUBLE, OBJECT_TYPE + UNSIGNED_TYPE ), DOUBLE, JAVA_LANG_DOUBLE );

		checkPrimitiveType( getType( NeutralType.DOUBLE, NOT_NULL ),                       DOUBLE_PRIMITIVE, DOUBLE );
		checkPrimitiveType( getType( NeutralType.DOUBLE, PRIMITIVE_TYPE ),                 DOUBLE_PRIMITIVE, DOUBLE );
		checkPrimitiveType( getType( NeutralType.DOUBLE, PRIMITIVE_TYPE + NOT_NULL),       DOUBLE_PRIMITIVE, DOUBLE );
		checkPrimitiveType( getType( NeutralType.DOUBLE, PRIMITIVE_TYPE + UNSIGNED_TYPE ), DOUBLE_PRIMITIVE, DOUBLE );
	}

	@Test
	public void testDecimal() {
		println("--- ");
		checkObjectType( getType(NeutralType.DECIMAL, NONE ),                           DECIMAL, JAVA_LANG_DECIMAL);
		checkObjectType( getType(NeutralType.DECIMAL, NOT_NULL ),                       DECIMAL, JAVA_LANG_DECIMAL);
		checkObjectType( getType(NeutralType.DECIMAL, UNSIGNED_TYPE ),                  DECIMAL, JAVA_LANG_DECIMAL);
		checkObjectType( getType(NeutralType.DECIMAL, PRIMITIVE_TYPE ),                 DECIMAL, JAVA_LANG_DECIMAL);
		checkObjectType( getType(NeutralType.DECIMAL, PRIMITIVE_TYPE + UNSIGNED_TYPE ), DECIMAL, JAVA_LANG_DECIMAL);
		checkObjectType( getType(NeutralType.DECIMAL, PRIMITIVE_TYPE + NOT_NULL ),      DECIMAL, JAVA_LANG_DECIMAL);
		checkObjectType( getType(NeutralType.DECIMAL, OBJECT_TYPE),                     DECIMAL, JAVA_LANG_DECIMAL);
		checkObjectType( getType(NeutralType.DECIMAL, OBJECT_TYPE + UNSIGNED_TYPE ),    DECIMAL, JAVA_LANG_DECIMAL);
		checkObjectType( getType(NeutralType.DECIMAL, OBJECT_TYPE + NOT_NULL ),         DECIMAL, JAVA_LANG_DECIMAL);
	}

	@Test
	public void testDate() {
		println("--- ");
		checkObjectType( getType(NeutralType.DATE, NONE ),                           LOCALDATE, JAVA_TIME_LOCALDATE);
		checkObjectType( getType(NeutralType.DATE, NOT_NULL ),                       LOCALDATE, JAVA_TIME_LOCALDATE);
		checkObjectType( getType(NeutralType.DATE, UNSIGNED_TYPE ),                  LOCALDATE, JAVA_TIME_LOCALDATE);
		checkObjectType( getType(NeutralType.DATE, PRIMITIVE_TYPE ),                 LOCALDATE, JAVA_TIME_LOCALDATE);
		checkObjectType( getType(NeutralType.DATE, PRIMITIVE_TYPE + UNSIGNED_TYPE ), LOCALDATE, JAVA_TIME_LOCALDATE);
		checkObjectType( getType(NeutralType.DATE, PRIMITIVE_TYPE + NOT_NULL ),      LOCALDATE, JAVA_TIME_LOCALDATE);
		checkObjectType( getType(NeutralType.DATE, OBJECT_TYPE),                     LOCALDATE, JAVA_TIME_LOCALDATE);
		checkObjectType( getType(NeutralType.DATE, OBJECT_TYPE + UNSIGNED_TYPE ),    LOCALDATE, JAVA_TIME_LOCALDATE);
		checkObjectType( getType(NeutralType.DATE, OBJECT_TYPE + NOT_NULL ),         LOCALDATE, JAVA_TIME_LOCALDATE);
	}

	@Test
	public void testTime() {
		println("--- ");
		checkObjectType( getType(NeutralType.TIME, NONE ),                           LOCALTIME, JAVA_TIME_LOCALTIME);
		checkObjectType( getType(NeutralType.TIME, NOT_NULL ),                       LOCALTIME, JAVA_TIME_LOCALTIME);
		checkObjectType( getType(NeutralType.TIME, UNSIGNED_TYPE ),                  LOCALTIME, JAVA_TIME_LOCALTIME);
		checkObjectType( getType(NeutralType.TIME, PRIMITIVE_TYPE ),                 LOCALTIME, JAVA_TIME_LOCALTIME);
		checkObjectType( getType(NeutralType.TIME, PRIMITIVE_TYPE + UNSIGNED_TYPE ), LOCALTIME, JAVA_TIME_LOCALTIME);
		checkObjectType( getType(NeutralType.TIME, PRIMITIVE_TYPE + NOT_NULL ),      LOCALTIME, JAVA_TIME_LOCALTIME);
		checkObjectType( getType(NeutralType.TIME, OBJECT_TYPE),                     LOCALTIME, JAVA_TIME_LOCALTIME);
		checkObjectType( getType(NeutralType.TIME, OBJECT_TYPE + UNSIGNED_TYPE ),    LOCALTIME, JAVA_TIME_LOCALTIME);
		checkObjectType( getType(NeutralType.TIME, OBJECT_TYPE + NOT_NULL ),         LOCALTIME, JAVA_TIME_LOCALTIME);
	}

	@Test
	public void testTimestamp() {
		println("--- ");
		checkObjectType( getType(NeutralType.TIMESTAMP, NONE ),                           LOCALDATETIME, JAVA_TIME_LOCALDATETIME);
		checkObjectType( getType(NeutralType.TIMESTAMP, NOT_NULL ),                       LOCALDATETIME, JAVA_TIME_LOCALDATETIME);
		checkObjectType( getType(NeutralType.TIMESTAMP, UNSIGNED_TYPE ),                  LOCALDATETIME, JAVA_TIME_LOCALDATETIME);
		checkObjectType( getType(NeutralType.TIMESTAMP, PRIMITIVE_TYPE ),                 LOCALDATETIME, JAVA_TIME_LOCALDATETIME);
		checkObjectType( getType(NeutralType.TIMESTAMP, PRIMITIVE_TYPE + UNSIGNED_TYPE ), LOCALDATETIME, JAVA_TIME_LOCALDATETIME);
		checkObjectType( getType(NeutralType.TIMESTAMP, PRIMITIVE_TYPE + NOT_NULL ),      LOCALDATETIME, JAVA_TIME_LOCALDATETIME);
		checkObjectType( getType(NeutralType.TIMESTAMP, OBJECT_TYPE),                     LOCALDATETIME, JAVA_TIME_LOCALDATETIME);
		checkObjectType( getType(NeutralType.TIMESTAMP, OBJECT_TYPE + UNSIGNED_TYPE ),    LOCALDATETIME, JAVA_TIME_LOCALDATETIME);
		checkObjectType( getType(NeutralType.TIMESTAMP, OBJECT_TYPE + NOT_NULL ),         LOCALDATETIME, JAVA_TIME_LOCALDATETIME);
	}

	@Test
	public void testBinary() {
		println("--- ");
		checkObjectType( getType(NeutralType.BINARY, NONE ),                           BYTE_ARRAY, BYTE_ARRAY);
		checkObjectType( getType(NeutralType.BINARY, NOT_NULL ),                       BYTE_ARRAY, BYTE_ARRAY);
		checkObjectType( getType(NeutralType.BINARY, UNSIGNED_TYPE ),                  BYTE_ARRAY, BYTE_ARRAY);
		checkObjectType( getType(NeutralType.BINARY, PRIMITIVE_TYPE ),                 BYTE_ARRAY, BYTE_ARRAY);
		checkObjectType( getType(NeutralType.BINARY, PRIMITIVE_TYPE + UNSIGNED_TYPE ), BYTE_ARRAY, BYTE_ARRAY);
		checkObjectType( getType(NeutralType.BINARY, PRIMITIVE_TYPE + NOT_NULL ),      BYTE_ARRAY, BYTE_ARRAY);
		checkObjectType( getType(NeutralType.BINARY, OBJECT_TYPE),                     BYTE_ARRAY, BYTE_ARRAY);
		checkObjectType( getType(NeutralType.BINARY, OBJECT_TYPE + UNSIGNED_TYPE ),    BYTE_ARRAY, BYTE_ARRAY);
		checkObjectType( getType(NeutralType.BINARY, OBJECT_TYPE + NOT_NULL ),         BYTE_ARRAY, BYTE_ARRAY);
	}

}
