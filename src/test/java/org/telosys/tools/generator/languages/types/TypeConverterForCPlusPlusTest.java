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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TypeConverterForCPlusPlusTest extends AbstractTypeTest {

	//---------------------------------------------------------------
	@Override
	protected String getLanguageName() {
		return "C++" ;
	}
	//---------------------------------------------------------------
	
	private void checkPrimitiveType( LanguageType lt, String primitiveType) {
		assertNotNull(lt);
		assertTrue ( lt.isPrimitiveType() ) ;
		assertEquals(primitiveType, lt.getSimpleType() );
		assertEquals(primitiveType, lt.getFullType() );
		assertEquals(primitiveType, lt.getWrapperType() );
	}

	private static final String STD_STRING = "std::string";
	@Test
	public void testString() {
		println("--- ");
		
		checkPrimitiveType( getType(NeutralType.STRING, NONE ),            STD_STRING);
		checkPrimitiveType( getType(NeutralType.STRING, NOT_NULL ),        STD_STRING);
		checkPrimitiveType( getType(NeutralType.STRING, PRIMITIVE_TYPE ),  STD_STRING);
		checkPrimitiveType( getType(NeutralType.STRING, UNSIGNED_TYPE ),   STD_STRING);
		checkPrimitiveType( getType(NeutralType.STRING, PRIMITIVE_TYPE + UNSIGNED_TYPE ), STD_STRING);
		
		checkPrimitiveType( getType(NeutralType.STRING, OBJECT_TYPE),                 STD_STRING);
		checkPrimitiveType( getType(NeutralType.STRING, OBJECT_TYPE + UNSIGNED_TYPE), STD_STRING );
		checkPrimitiveType( getType(NeutralType.STRING, OBJECT_TYPE + NOT_NULL),      STD_STRING );
	}

	private static final String BOOL = "bool";
	@Test
	public void testBoolean() {
		println("--- ");
				
		checkPrimitiveType( getType( NeutralType.BOOLEAN, NONE ),                  BOOL );
		checkPrimitiveType( getType( NeutralType.BOOLEAN, NOT_NULL ),              BOOL);
		checkPrimitiveType( getType( NeutralType.BOOLEAN, PRIMITIVE_TYPE ),        BOOL);
		checkPrimitiveType( getType( NeutralType.BOOLEAN, UNSIGNED_TYPE ),         BOOL);
		checkPrimitiveType( getType( NeutralType.BOOLEAN, PRIMITIVE_TYPE + UNSIGNED_TYPE ), BOOL);
		
		checkPrimitiveType( getType( NeutralType.BOOLEAN, OBJECT_TYPE),            BOOL );
		checkPrimitiveType( getType( NeutralType.BOOLEAN, NOT_NULL + OBJECT_TYPE), BOOL );
	}

	private static final String CHAR = "char";
	private static final String UNSIGNED_CHAR = "unsigned char";
	@Test
	public void testByte() {
		println("--- ");
		checkPrimitiveType( getType( NeutralType.BYTE, NONE ),                        CHAR);
		checkPrimitiveType( getType( NeutralType.BYTE, OBJECT_TYPE ),                 CHAR);
		checkPrimitiveType( getType( NeutralType.BYTE, NOT_NULL ),                    CHAR);
		checkPrimitiveType( getType( NeutralType.BYTE, NOT_NULL + OBJECT_TYPE ),      CHAR);
		
		checkPrimitiveType( getType( NeutralType.BYTE, UNSIGNED_TYPE ),               UNSIGNED_CHAR  );
		checkPrimitiveType( getType( NeutralType.BYTE, OBJECT_TYPE + UNSIGNED_TYPE ),   UNSIGNED_CHAR);
		checkPrimitiveType( getType( NeutralType.BYTE, NOT_NULL + UNSIGNED_TYPE ),        UNSIGNED_CHAR );
		checkPrimitiveType( getType( NeutralType.BYTE, NOT_NULL + OBJECT_TYPE + UNSIGNED_TYPE ), UNSIGNED_CHAR);
	}

	private static final String SHORT = "short";
	private static final String UNSIGNED_SHORT = "unsigned short";
	@Test
	public void testShort() {
		println("--- ");
		checkPrimitiveType( getType( NeutralType.SHORT, NONE ),                    SHORT);
		checkPrimitiveType( getType( NeutralType.SHORT, OBJECT_TYPE ),             SHORT);
		checkPrimitiveType( getType( NeutralType.SHORT, NOT_NULL ),                SHORT );
		checkPrimitiveType( getType( NeutralType.SHORT, NOT_NULL + OBJECT_TYPE ),  SHORT );

		checkPrimitiveType( getType( NeutralType.SHORT, UNSIGNED_TYPE ),                UNSIGNED_SHORT);	
		checkPrimitiveType( getType( NeutralType.SHORT, UNSIGNED_TYPE + OBJECT_TYPE ),  UNSIGNED_SHORT);
		checkPrimitiveType( getType( NeutralType.SHORT, UNSIGNED_TYPE + NOT_NULL  ),     UNSIGNED_SHORT);		
		checkPrimitiveType( getType( NeutralType.SHORT, UNSIGNED_TYPE + NOT_NULL + OBJECT_TYPE), UNSIGNED_SHORT);
	}

	private static final String INT = "int";
	private static final String UNSIGNED_INT = "unsigned int";
	@Test
	public void testInteger() {
		println("--- ");
		checkPrimitiveType( getType( NeutralType.INTEGER, NONE ),                    INT);
		checkPrimitiveType( getType( NeutralType.INTEGER, OBJECT_TYPE ),             INT);
		checkPrimitiveType( getType( NeutralType.INTEGER, NOT_NULL ),                INT );
		checkPrimitiveType( getType( NeutralType.INTEGER, NOT_NULL + OBJECT_TYPE ),  INT );

		checkPrimitiveType( getType( NeutralType.INTEGER, UNSIGNED_TYPE ),                UNSIGNED_INT);	
		checkPrimitiveType( getType( NeutralType.INTEGER, UNSIGNED_TYPE + OBJECT_TYPE ),  UNSIGNED_INT);
		checkPrimitiveType( getType( NeutralType.INTEGER, UNSIGNED_TYPE + NOT_NULL  ),     UNSIGNED_INT);		
		checkPrimitiveType( getType( NeutralType.INTEGER, UNSIGNED_TYPE + NOT_NULL + OBJECT_TYPE), UNSIGNED_INT);
	}

	private static final String LONG = "long";
	private static final String UNSIGNED_LONG = "unsigned long";
	@Test
	public void testLong() {
		println("--- ");
		checkPrimitiveType( getType( NeutralType.LONG, NONE ),                    LONG);
		checkPrimitiveType( getType( NeutralType.LONG, OBJECT_TYPE ),             LONG);
		checkPrimitiveType( getType( NeutralType.LONG, NOT_NULL ),                LONG );
		checkPrimitiveType( getType( NeutralType.LONG, NOT_NULL + OBJECT_TYPE ),  LONG );

		checkPrimitiveType( getType( NeutralType.LONG, UNSIGNED_TYPE ),                UNSIGNED_LONG);	
		checkPrimitiveType( getType( NeutralType.LONG, UNSIGNED_TYPE + OBJECT_TYPE ),  UNSIGNED_LONG);
		checkPrimitiveType( getType( NeutralType.LONG, UNSIGNED_TYPE + NOT_NULL  ),     UNSIGNED_LONG);		
		checkPrimitiveType( getType( NeutralType.LONG, UNSIGNED_TYPE + NOT_NULL + OBJECT_TYPE), UNSIGNED_LONG);
	}

	private static final String DOUBLE = "double";
	@Test
	public void testDecimal() {
		println("--- ");
		checkPrimitiveType( getType( NeutralType.DECIMAL, NONE ),                    DOUBLE);
		checkPrimitiveType( getType( NeutralType.DECIMAL, OBJECT_TYPE ),             DOUBLE);
		checkPrimitiveType( getType( NeutralType.DECIMAL, NOT_NULL ),                DOUBLE );
		checkPrimitiveType( getType( NeutralType.DECIMAL, NOT_NULL + OBJECT_TYPE ),  DOUBLE );

		checkPrimitiveType( getType( NeutralType.DECIMAL, UNSIGNED_TYPE ),                 DOUBLE);	
		checkPrimitiveType( getType( NeutralType.DECIMAL, UNSIGNED_TYPE + OBJECT_TYPE ),   DOUBLE);
		checkPrimitiveType( getType( NeutralType.DECIMAL, UNSIGNED_TYPE + NOT_NULL  ),     DOUBLE);		
		checkPrimitiveType( getType( NeutralType.DECIMAL, UNSIGNED_TYPE + NOT_NULL + OBJECT_TYPE), DOUBLE);
	}
	
	private static final String FLOAT = "float";
	@Test
	public void testFloat() {
		println("--- ");
		checkPrimitiveType( getType( NeutralType.FLOAT, NONE ),                    FLOAT);
		checkPrimitiveType( getType( NeutralType.FLOAT, OBJECT_TYPE ),             FLOAT);
	}
	
	@Test
	public void testDouble() {
		println("--- ");
		checkPrimitiveType( getType( NeutralType.DOUBLE, NONE ),                    DOUBLE);
		checkPrimitiveType( getType( NeutralType.DOUBLE, OBJECT_TYPE ),             DOUBLE);
	}

	private static final String EXPECTED_TYPE_FOR_DATE = "std::chrono::year_month_day";
	@Test
	public void testDate() {
		println("--- ");
		checkPrimitiveType( getType( NeutralType.DATE, NONE ),                    EXPECTED_TYPE_FOR_DATE);
	}

	private static final String EXPECTED_TYPE_FOR_TIME = "std::chrono::hh_mm_ss";
	@Test
	public void testTime() {
		println("--- ");
		checkPrimitiveType( getType( NeutralType.TIME, NONE ),                    EXPECTED_TYPE_FOR_TIME);
	}

	private static final String EXPECTED_TYPE_FOR_DATETIME = "std::chrono::local_time";
	@Test
	public void testTimestamp() {
		println("--- ");
		checkPrimitiveType( getType( NeutralType.TIMESTAMP, NONE ),               EXPECTED_TYPE_FOR_DATETIME);
	}
	@Test
	public void testDateTime() {
		println("--- ");
		checkPrimitiveType( getType( NeutralType.DATETIME, NONE ),                   EXPECTED_TYPE_FOR_DATETIME);
		checkPrimitiveType( getType( NeutralType.DATETIME, OBJECT_TYPE ),            EXPECTED_TYPE_FOR_DATETIME);
		checkPrimitiveType( getType( NeutralType.DATETIME, NOT_NULL ),               EXPECTED_TYPE_FOR_DATETIME);
		checkPrimitiveType( getType( NeutralType.DATETIME, NOT_NULL + OBJECT_TYPE ), EXPECTED_TYPE_FOR_DATETIME);
		checkPrimitiveType( getType( NeutralType.DATETIME, UNSIGNED_TYPE ),          EXPECTED_TYPE_FOR_DATETIME);
	}
	@Test
	public void testDateTimeTZ() {
		println("--- ");
		checkPrimitiveType( getType( NeutralType.DATETIMETZ, NONE ),            "std::chrono::zoned_time");
	}
	@Test
	public void testTimeTZ() {
		println("--- ");
		checkPrimitiveType( getType( NeutralType.TIMETZ, NONE ), EXPECTED_TYPE_FOR_TIME); // No Time with TZ in CPP => like Time
	}
	
	private static final String  EXPECTED_TYPE_FOR_UUID = STD_STRING; // UUID is stored as a string
	@Test
	public void testUUID() {
		println("--- ");
		checkPrimitiveType( getType( NeutralType.UUID, NONE ),                    EXPECTED_TYPE_FOR_UUID);
		checkPrimitiveType( getType( NeutralType.UUID, OBJECT_TYPE ),             EXPECTED_TYPE_FOR_UUID);
		checkPrimitiveType( getType( NeutralType.UUID, NOT_NULL ),                EXPECTED_TYPE_FOR_UUID);
		checkPrimitiveType( getType( NeutralType.UUID, NOT_NULL + OBJECT_TYPE ),  EXPECTED_TYPE_FOR_UUID);
		checkPrimitiveType( getType( NeutralType.UUID, UNSIGNED_TYPE ),           EXPECTED_TYPE_FOR_UUID);
	}

	private static final String  EXPECTED_TYPE_FOR_BYNARY = "std::vector<std::byte>";
	@Test
	public void testBinary() {
		println("--- ");
		checkPrimitiveType( getType( NeutralType.BINARY, NONE ),    EXPECTED_TYPE_FOR_BYNARY );
	}

	@Test
	public void testDefaultCollectionType() {
		println("--- ");
		TypeConverter typeConverter = getTypeConverter();
		assertNull(typeConverter.getSpecificCollectionType());
		assertEquals("std::list", typeConverter.getCollectionType());
		assertEquals("std::list<int>", typeConverter.getCollectionType("int"));
	}

	private static final String STD_VECTOR = "std::vector";
	@Test
	public void testSpecificCollectionType() throws GeneratorException {
		println("--- ");
		EnvInContext env = new EnvInContext();
		env.setLanguage(getLanguageName());
		env.setCollectionType(STD_VECTOR);
		TypeConverter typeConverter = env.getTypeConverter();
		
		assertNotNull(typeConverter.getSpecificCollectionType());
		assertEquals(STD_VECTOR, typeConverter.getSpecificCollectionType());
		assertEquals(STD_VECTOR, typeConverter.getCollectionType());
		assertEquals("std::vector<int>", typeConverter.getCollectionType("int"));
	}
	
}
