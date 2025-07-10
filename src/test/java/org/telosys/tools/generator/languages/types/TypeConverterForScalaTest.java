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

public class TypeConverterForScalaTest extends AbstractTypeTest {

	//---------------------------------------------------------------
	@Override
	protected String getLanguageName() {
		return "Scala" ;
	}
	//---------------------------------------------------------------
		
	private void checkPrimitiveType( LanguageType lt, String primitiveType) {
		assertNotNull(lt);
		assertTrue ( lt.isPrimitiveType() ) ;
		assertEquals(primitiveType, lt.getSimpleType() );
		assertEquals(primitiveType, lt.getFullType() );
		assertEquals(primitiveType,   lt.getWrapperType() );
	}

	private void checkObjectType( LanguageType lt, String simpleType, String fullType) {
		assertNotNull(lt);
		assertFalse ( lt.isPrimitiveType() ) ;
		assertEquals(simpleType, lt.getSimpleType() );
		assertEquals(fullType,   lt.getFullType() );
		assertEquals(simpleType, lt.getWrapperType() );
	}
	
	private static final String STRING           = "String";
	private static final String STRING_FULL_TYPE = "java.lang.String";
	@Test
	public void testString() {
		checkObjectType( getType(NeutralType.STRING, NONE ),                           STRING, STRING_FULL_TYPE );
		checkObjectType( getType(NeutralType.STRING, PRIMITIVE_TYPE ),                 STRING, STRING_FULL_TYPE );
		checkObjectType( getType(NeutralType.STRING, UNSIGNED_TYPE ),                  STRING, STRING_FULL_TYPE );
		checkObjectType( getType(NeutralType.STRING, PRIMITIVE_TYPE + UNSIGNED_TYPE ), STRING, STRING_FULL_TYPE );
		checkObjectType( getType(NeutralType.STRING, OBJECT_TYPE),                     STRING, STRING_FULL_TYPE );
		checkObjectType( getType(NeutralType.STRING, NOT_NULL ),                       STRING, STRING_FULL_TYPE );
	}

	private static final String BOOLEAN           = "Boolean";
	@Test
	public void testBoolean() {
		checkPrimitiveType( getType( NeutralType.BOOLEAN, NONE ),                           BOOLEAN );
		checkPrimitiveType( getType( NeutralType.BOOLEAN, PRIMITIVE_TYPE ),                 BOOLEAN );
		checkPrimitiveType( getType( NeutralType.BOOLEAN, UNSIGNED_TYPE ),                  BOOLEAN );
		checkPrimitiveType( getType( NeutralType.BOOLEAN, PRIMITIVE_TYPE + UNSIGNED_TYPE ), BOOLEAN );
		checkPrimitiveType( getType( NeutralType.BOOLEAN, OBJECT_TYPE),                     BOOLEAN );
		checkPrimitiveType( getType( NeutralType.BOOLEAN, NOT_NULL ),                       BOOLEAN );
		checkPrimitiveType( getType( NeutralType.BOOLEAN, NOT_NULL + OBJECT_TYPE),          BOOLEAN );
	}

	private static final String BYTE           = "Byte";
	@Test
	public void testByte() {
		checkPrimitiveType( getType( NeutralType.BYTE, NONE ),             BYTE );
		checkPrimitiveType( getType( NeutralType.BYTE, OBJECT_TYPE ),      BYTE );
		checkPrimitiveType( getType( NeutralType.BYTE, PRIMITIVE_TYPE ),   BYTE );
		checkPrimitiveType( getType( NeutralType.BYTE, NOT_NULL ),         BYTE );
		// UNSIGNED 
		checkPrimitiveType( getType( NeutralType.BYTE, UNSIGNED_TYPE ),                   BYTE );
		checkPrimitiveType( getType( NeutralType.BYTE, UNSIGNED_TYPE + PRIMITIVE_TYPE  ), BYTE );
		checkPrimitiveType( getType( NeutralType.BYTE, UNSIGNED_TYPE + OBJECT_TYPE  ),    BYTE );
		checkPrimitiveType( getType( NeutralType.BYTE, UNSIGNED_TYPE + NOT_NULL ),        BYTE );
	}
	private static final String SHORT           = "Short";
	@Test
	public void testShort() {
		checkPrimitiveType( getType( NeutralType.SHORT, NONE ),             SHORT );
		checkPrimitiveType( getType( NeutralType.SHORT, OBJECT_TYPE ),      SHORT );
		checkPrimitiveType( getType( NeutralType.SHORT, PRIMITIVE_TYPE ),   SHORT );
		checkPrimitiveType( getType( NeutralType.SHORT, NOT_NULL ),         SHORT );
		// UNSIGNED 
		checkPrimitiveType( getType( NeutralType.SHORT, UNSIGNED_TYPE ),                   SHORT );
		checkPrimitiveType( getType( NeutralType.SHORT, UNSIGNED_TYPE + PRIMITIVE_TYPE  ), SHORT );
		checkPrimitiveType( getType( NeutralType.SHORT, UNSIGNED_TYPE + OBJECT_TYPE  ),    SHORT );
		checkPrimitiveType( getType( NeutralType.SHORT, UNSIGNED_TYPE + NOT_NULL ),        SHORT );
	}
	private static final String INT           = "Int";
	@Test
	public void testInt() {
		checkPrimitiveType( getType( NeutralType.INTEGER, NONE ),             INT );
		checkPrimitiveType( getType( NeutralType.INTEGER, OBJECT_TYPE ),      INT );
		checkPrimitiveType( getType( NeutralType.INTEGER, PRIMITIVE_TYPE ),   INT );
		checkPrimitiveType( getType( NeutralType.INTEGER, NOT_NULL ),         INT );
		// UNSIGNED INT
		checkPrimitiveType( getType( NeutralType.INTEGER, UNSIGNED_TYPE ),                  INT);
		checkPrimitiveType( getType( NeutralType.INTEGER, UNSIGNED_TYPE + PRIMITIVE_TYPE ), INT);
		checkPrimitiveType( getType( NeutralType.INTEGER, UNSIGNED_TYPE + OBJECT_TYPE ),    INT);
		checkPrimitiveType( getType( NeutralType.INTEGER, UNSIGNED_TYPE + NOT_NULL ),       INT);
	}
	private static final String LONG           = "Long";
	@Test
	public void testLong() {
		checkPrimitiveType( getType( NeutralType.LONG, NONE ),             LONG );
		checkPrimitiveType( getType( NeutralType.LONG, OBJECT_TYPE ),      LONG);
		checkPrimitiveType( getType( NeutralType.LONG, PRIMITIVE_TYPE ),   LONG );
		checkPrimitiveType( getType( NeutralType.LONG, NOT_NULL ),         LONG );
		// UNSIGNED 
		checkPrimitiveType( getType( NeutralType.LONG, UNSIGNED_TYPE ),                  LONG);
		checkPrimitiveType( getType( NeutralType.LONG, UNSIGNED_TYPE + PRIMITIVE_TYPE ), LONG);
		checkPrimitiveType( getType( NeutralType.LONG, UNSIGNED_TYPE + OBJECT_TYPE ),    LONG);
		checkPrimitiveType( getType( NeutralType.LONG, UNSIGNED_TYPE + NOT_NULL ),       LONG);
	}

	private static final String FLOAT          = "Float";
	@Test
	public void testFloat() {
		checkPrimitiveType( getType( NeutralType.FLOAT, NONE ),                           FLOAT );
		checkPrimitiveType( getType( NeutralType.FLOAT, PRIMITIVE_TYPE ),                 FLOAT );
		checkPrimitiveType( getType( NeutralType.FLOAT, UNSIGNED_TYPE ),                  FLOAT );
		checkPrimitiveType( getType( NeutralType.FLOAT, PRIMITIVE_TYPE + UNSIGNED_TYPE ), FLOAT );
		checkPrimitiveType( getType( NeutralType.FLOAT, OBJECT_TYPE),                     FLOAT );
		checkPrimitiveType( getType( NeutralType.FLOAT, NOT_NULL ),                       FLOAT );
		checkPrimitiveType( getType( NeutralType.FLOAT, NOT_NULL + OBJECT_TYPE),          FLOAT );
	}
	private static final String DOUBLE           = "Double";
	@Test
	public void testDouble() {
		checkPrimitiveType( getType( NeutralType.DOUBLE, NONE ),             DOUBLE );
		checkPrimitiveType( getType( NeutralType.DOUBLE, OBJECT_TYPE ),      DOUBLE);
		checkPrimitiveType( getType( NeutralType.DOUBLE, PRIMITIVE_TYPE ),   DOUBLE );
		checkPrimitiveType( getType( NeutralType.DOUBLE, NOT_NULL ),         DOUBLE );
		// UNSIGNED 
		checkPrimitiveType( getType( NeutralType.DOUBLE, UNSIGNED_TYPE ),                  DOUBLE);
		checkPrimitiveType( getType( NeutralType.DOUBLE, UNSIGNED_TYPE + PRIMITIVE_TYPE ), DOUBLE);
		checkPrimitiveType( getType( NeutralType.DOUBLE, UNSIGNED_TYPE + OBJECT_TYPE ),    DOUBLE);
		checkPrimitiveType( getType( NeutralType.DOUBLE, UNSIGNED_TYPE + NOT_NULL ),       DOUBLE);
	}

	private static final String BIGDECIMAL           = "BigDecimal";
	private static final String BIGDECIMAL_FULLTYPE  = "scala.math.BigDecimal";
	@Test
	public void testDecimal() {
		checkObjectType( getType( NeutralType.DECIMAL, NONE ),                           BIGDECIMAL, BIGDECIMAL_FULLTYPE );
		checkObjectType( getType( NeutralType.DECIMAL, UNSIGNED_TYPE ),                  BIGDECIMAL, BIGDECIMAL_FULLTYPE );
		checkObjectType( getType( NeutralType.DECIMAL, OBJECT_TYPE ),                    BIGDECIMAL, BIGDECIMAL_FULLTYPE );
		checkObjectType( getType( NeutralType.DECIMAL, PRIMITIVE_TYPE ),                 BIGDECIMAL, BIGDECIMAL_FULLTYPE );
		checkObjectType( getType( NeutralType.DECIMAL, OBJECT_TYPE + UNSIGNED_TYPE ),    BIGDECIMAL, BIGDECIMAL_FULLTYPE );
		checkObjectType( getType( NeutralType.DECIMAL, PRIMITIVE_TYPE + UNSIGNED_TYPE ), BIGDECIMAL, BIGDECIMAL_FULLTYPE );
		checkObjectType( getType( NeutralType.DECIMAL, NOT_NULL ),                       BIGDECIMAL, BIGDECIMAL_FULLTYPE );
	}

	private static final String LOCALDATE           = "LocalDate";
	private static final String LOCALDATE_FULLTYPE  = "java.time.LocalDate";
	@Test
	public void testDate() {
		checkObjectType( getType( NeutralType.DATE, NONE ),           LOCALDATE,  LOCALDATE_FULLTYPE );
		checkObjectType( getType( NeutralType.DATE, UNSIGNED_TYPE ),  LOCALDATE,  LOCALDATE_FULLTYPE );
		checkObjectType( getType( NeutralType.DATE, OBJECT_TYPE ),    LOCALDATE,  LOCALDATE_FULLTYPE );
		checkObjectType( getType( NeutralType.DATE, PRIMITIVE_TYPE ), LOCALDATE,  LOCALDATE_FULLTYPE );
		checkObjectType( getType( NeutralType.DATE, NOT_NULL ),       LOCALDATE,  LOCALDATE_FULLTYPE );
	}

	private static final String LOCALTIME           = "LocalTime";
	private static final String LOCALTIME_FULLTYPE  = "java.time.LocalTime";
	@Test
	public void testTime() {
		checkObjectType( getType( NeutralType.TIME, NONE ),           LOCALTIME,  LOCALTIME_FULLTYPE );
		checkObjectType( getType( NeutralType.TIME, UNSIGNED_TYPE ),  LOCALTIME,  LOCALTIME_FULLTYPE );
		checkObjectType( getType( NeutralType.TIME, OBJECT_TYPE ),    LOCALTIME,  LOCALTIME_FULLTYPE );
		checkObjectType( getType( NeutralType.TIME, PRIMITIVE_TYPE ), LOCALTIME,  LOCALTIME_FULLTYPE );
		checkObjectType( getType( NeutralType.TIME, NOT_NULL ),       LOCALTIME,  LOCALTIME_FULLTYPE );
	}

	private static final String LOCALDATETIME                    = "LocalDateTime";
	private static final String LOCALDATETIME_FULLTYPE           = "java.time.LocalDateTime";
	@Test
	public void testTimestamp() {
		checkObjectType( getType( NeutralType.TIMESTAMP, NONE ),           LOCALDATETIME,  LOCALDATETIME_FULLTYPE );
		checkObjectType( getType( NeutralType.TIMESTAMP, UNSIGNED_TYPE ),  LOCALDATETIME,  LOCALDATETIME_FULLTYPE );
		checkObjectType( getType( NeutralType.TIMESTAMP, OBJECT_TYPE ),    LOCALDATETIME,  LOCALDATETIME_FULLTYPE );
		checkObjectType( getType( NeutralType.TIMESTAMP, NOT_NULL ),       LOCALDATETIME,  LOCALDATETIME_FULLTYPE );
	}
	@Test
	public void testDateTime() { // v 4.3.0
		checkObjectType( getType( NeutralType.DATETIME, NONE ),           LOCALDATETIME,  LOCALDATETIME_FULLTYPE );
		checkObjectType( getType( NeutralType.DATETIME, UNSIGNED_TYPE ),  LOCALDATETIME,  LOCALDATETIME_FULLTYPE );
		checkObjectType( getType( NeutralType.DATETIME, OBJECT_TYPE ),    LOCALDATETIME,  LOCALDATETIME_FULLTYPE );
		checkObjectType( getType( NeutralType.DATETIME, NOT_NULL ),       LOCALDATETIME,  LOCALDATETIME_FULLTYPE );
	}
	
	private static final String OFFSETDATETIME                   = "OffsetDateTime";
	private static final String OFFSETDATETIME_FULLTYPE          = "java.time.OffsetDateTime";
	@Test
	public void testDateTimeTZ() {  // v 4.3.0
		checkObjectType( getType( NeutralType.DATETIMETZ, NONE ),           OFFSETDATETIME,  OFFSETDATETIME_FULLTYPE );
		checkObjectType( getType( NeutralType.DATETIMETZ, UNSIGNED_TYPE ),  OFFSETDATETIME,  OFFSETDATETIME_FULLTYPE );
		checkObjectType( getType( NeutralType.DATETIMETZ, OBJECT_TYPE ),    OFFSETDATETIME,  OFFSETDATETIME_FULLTYPE );
		checkObjectType( getType( NeutralType.DATETIMETZ, NOT_NULL ),       OFFSETDATETIME,  OFFSETDATETIME_FULLTYPE );
	}

	private static final String OFFSETTIME                   = "OffsetTime";
	private static final String OFFSETTIME_FULLTYPE          = "java.time.OffsetTime";
	@Test
	public void testTimeTZ() {  // v 4.3.0
		checkObjectType( getType( NeutralType.TIMETZ, NONE ),           OFFSETTIME,  OFFSETTIME_FULLTYPE );
		checkObjectType( getType( NeutralType.TIMETZ, UNSIGNED_TYPE ),  OFFSETTIME,  OFFSETTIME_FULLTYPE );
		checkObjectType( getType( NeutralType.TIMETZ, OBJECT_TYPE ),    OFFSETTIME,  OFFSETTIME_FULLTYPE );
		checkObjectType( getType( NeutralType.TIMETZ, NOT_NULL ),       OFFSETTIME,  OFFSETTIME_FULLTYPE );
	}

	private static final String UUID                   = "UUID";
	private static final String UUID_FULLTYPE          = "java.util.UUID";
	@Test
	public void testUUID() {  // v 4.3.0
		checkObjectType( getType( NeutralType.UUID, NONE ),           UUID,  UUID_FULLTYPE );
		checkObjectType( getType( NeutralType.UUID, UNSIGNED_TYPE ),  UUID,  UUID_FULLTYPE );
		checkObjectType( getType( NeutralType.UUID, OBJECT_TYPE ),    UUID,  UUID_FULLTYPE );
		checkObjectType( getType( NeutralType.UUID, NOT_NULL ),       UUID,  UUID_FULLTYPE );
	}

	
	private static final String BYTEARRAY          = "Array[Byte]";
	@Test
	public void testByteArray() {
		checkPrimitiveType( getType( NeutralType.BINARY, NONE ),                           BYTEARRAY);
		checkPrimitiveType( getType( NeutralType.BINARY, PRIMITIVE_TYPE ),                 BYTEARRAY);
		checkPrimitiveType( getType( NeutralType.BINARY, UNSIGNED_TYPE ),                  BYTEARRAY);
		checkPrimitiveType( getType( NeutralType.BINARY, PRIMITIVE_TYPE + UNSIGNED_TYPE ), BYTEARRAY);
		checkPrimitiveType( getType( NeutralType.BINARY, OBJECT_TYPE),                     BYTEARRAY);
		
		checkPrimitiveType( getType( NeutralType.BINARY, NOT_NULL ),                       BYTEARRAY);
		checkPrimitiveType( getType( NeutralType.BINARY, NOT_NULL + OBJECT_TYPE),          BYTEARRAY);
	}
	
	@Test
	public void testDefaultCollectionType() {
		println("--- ");
		TypeConverter typeConverter = getTypeConverter();
		assertEquals("List", typeConverter.getCollectionType());
		assertEquals("List[String]", typeConverter.getCollectionType("String"));
		assertEquals("List[Foo]", typeConverter.getCollectionType("Foo"));
	}

	@Test
	public void testSpecificCollectionType() throws GeneratorException {
		println("--- ");
		EnvInContext env = new EnvInContext();
		env.setLanguage(getLanguageName());
		env.setCollectionType("Set");
		TypeConverter typeConverter = env.getTypeConverter();
		
		assertEquals("Set", typeConverter.getCollectionType());
		assertEquals("Set[String]", typeConverter.getCollectionType("String"));
		assertEquals("Set[Foo]", typeConverter.getCollectionType("Foo"));
	}

}
