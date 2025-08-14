package org.telosys.tools.generator.languages.literals;

import org.junit.Test;
import org.telosys.tools.generator.languages.types.AttributeTypeInfo;
import org.telosys.tools.generator.languages.types.LanguageType;
import org.telosys.tools.generator.languages.types.TypeConverterForScala;
import org.telosys.tools.generic.model.types.NeutralType;

import static org.junit.Assert.assertEquals;

public class LiteralValuesProviderForScalaTest extends AbstractLiteralsTest {
	
	//----------------------------------------------------------------------------------
	@Override
	protected String getLanguageName() {
		return "Scala" ;
	}
	//----------------------------------------------------------------------------------

	@Test
	public void testLiteralNull() {
		assertEquals("null", getLiteralValuesProvider().getLiteralNull() );
	}

	@Test
	public void testLiteralValuesForBOOLEAN() {
		LanguageType lt = getLanguageType(NeutralType.BOOLEAN );
		assertEquals("true",  getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("false", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
		assertEquals("true",  getLiteralValuesProvider().generateLiteralValue(lt, 0, 3).getCurrentLanguageValue() );
	}

	@Test
	public void testLiteralValuesForSTRING() {
		LanguageType lt = getLanguageType(NeutralType.STRING );
		assertEquals("\"AAA\"",   getLiteralValuesProvider().generateLiteralValue(lt, 3, 1).getCurrentLanguageValue() );
		assertEquals("\"BBB\"",   getLiteralValuesProvider().generateLiteralValue(lt, 3, 2).getCurrentLanguageValue() );
		assertEquals("\"CCCCC\"", getLiteralValuesProvider().generateLiteralValue(lt, 5, 3).getCurrentLanguageValue() );
		lt = getLanguageTypeNotNull(NeutralType.STRING );
		assertEquals("\"AAA\"",   getLiteralValuesProvider().generateLiteralValue(lt, 3, 1).getCurrentLanguageValue() );
		assertEquals("\"BBB\"",   getLiteralValuesProvider().generateLiteralValue(lt, 3, 2).getCurrentLanguageValue() );
	}

	@Test
	public void testLiteralValuesForBYTE() {
		LanguageType lt = getLanguageType(NeutralType.BYTE );
		assertEquals("1", getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("2", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
	}

	@Test
	public void testLiteralValuesForSHORT() {
		LanguageType lt = getLanguageType(NeutralType.SHORT );
		assertEquals("1", getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("2", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
	}

	@Test
	public void testLiteralValuesForINTEGER() {
		LanguageType lt = getLanguageType(NeutralType.INTEGER );
		assertEquals("100", getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("200", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
	}

	@Test
	public void testLiteralValuesForLONG() {
		LanguageType lt = getLanguageType(NeutralType.LONG );
		assertEquals("1000", getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("2000", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
	}

	@Test
	public void testLiteralValuesForFLOAT() {
		LanguageType lt = getLanguageType(NeutralType.FLOAT );
		assertEquals("1000.5f", getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("2000.5f", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
	}

	@Test
	public void testLiteralValuesForDOUBLE() {
		LanguageType lt = getLanguageType(NeutralType.DOUBLE );
		assertEquals("1000.66", getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("2000.66", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
	}

	@Test
	public void testLiteralValuesForDECIMAL() {
		LanguageType lt = getLanguageType(NeutralType.DECIMAL );
		assertEquals("java.math.BigDecimal.valueOf(10000.77)", getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("java.math.BigDecimal.valueOf(20000.77)", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
	}
	
	@Test
	public void testLiteralValuesForDATE() {
		LanguageType lt = getLanguageType(NeutralType.DATE );
		int maxlen = 999; // not used
		assertEquals("java.time.LocalDate.parse(\"2000-06-22\")", getLiteralValuesProvider().generateLiteralValue(lt, maxlen,  0).getCurrentLanguageValue() );
		assertEquals("java.time.LocalDate.parse(\"2001-06-22\")", getLiteralValuesProvider().generateLiteralValue(lt, maxlen,  1).getCurrentLanguageValue() );
		assertEquals("java.time.LocalDate.parse(\"2002-06-22\")", getLiteralValuesProvider().generateLiteralValue(lt, maxlen,  2).getCurrentLanguageValue() );
		lt = getLanguageTypeNotNull(NeutralType.DATE );
		assertEquals("java.time.LocalDate.parse(\"2000-06-22\")", getLiteralValuesProvider().generateLiteralValue(lt, maxlen,  0).getCurrentLanguageValue() );
		assertEquals("java.time.LocalDate.parse(\"2001-06-22\")", getLiteralValuesProvider().generateLiteralValue(lt, maxlen,  1).getCurrentLanguageValue() );
	}

	@Test
	public void testLiteralValuesForTIME() {
		LanguageType lt = getLanguageType(NeutralType.TIME );
		int maxlen = 999; // not used
		assertEquals("java.time.LocalTime.parse(\"00:46:52\")", getLiteralValuesProvider().generateLiteralValue(lt, maxlen,  0).getCurrentLanguageValue() );
		assertEquals("java.time.LocalTime.parse(\"01:46:52\")", getLiteralValuesProvider().generateLiteralValue(lt, maxlen,  1).getCurrentLanguageValue() );
		lt = getLanguageTypeNotNull(NeutralType.TIME );
		assertEquals("java.time.LocalTime.parse(\"00:46:52\")", getLiteralValuesProvider().generateLiteralValue(lt, maxlen,  0).getCurrentLanguageValue() );
		assertEquals("java.time.LocalTime.parse(\"01:46:52\")", getLiteralValuesProvider().generateLiteralValue(lt, maxlen,  1).getCurrentLanguageValue() );
	}

	@Test
	public void testLiteralValuesForBINARY() {
		LanguageType lt = getLanguageType(NeutralType.BINARY );
		assertEquals("null", getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("null", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
	}

	@Test
	public void testEqualsStatement() {
		assertEquals(" == foo", getLiteralValuesProvider().getEqualsStatement("foo", getLanguageType(NeutralType.STRING )) );
		assertEquals(" == foo", getLiteralValuesProvider().getEqualsStatement("foo", getLanguageType(NeutralType.BOOLEAN )) );
		assertEquals(" == foo", getLiteralValuesProvider().getEqualsStatement("foo", getLanguageType(NeutralType.INTEGER )) );
		assertEquals(" == foo", getLiteralValuesProvider().getEqualsStatement("foo", getLanguageType(NeutralType.FLOAT )) );
	}

	protected static final boolean NULLABLE = false ;
	protected static final boolean NOT_NULL = true ;
	
	
	private String getInitValue(AttributeTypeInfo attributeTypeInfo) {
		LanguageType languageType = getTypeConverter().getType(attributeTypeInfo);
		return getLiteralValuesProvider().getInitValue(attributeTypeInfo, languageType );
	}
	
	@Test
	public void testInitValue() {
		String literalNull = getLiteralValuesProvider().getLiteralNull();
//		TypeConverterForScala typeConverter = new TypeConverterForScala();
		
//		EnvInContext envInContext = new EnvInContext();
//		envInContext.setLanguage(getLanguageName());
//		AttributeInContext attribute = FakeAttributeBuilder.buildAttribute("Car", "foo", NeutralType.STRING, envInContext);
//		LanguageType languageType = getLanguageType(attribute.getNeutralType(), typeInfo);
		
//		AttributeTypeInfo attributeTypeInfo = new FakeAttributeTypeInfo_BAK.Builder()
//		        .neutralType(NeutralType.STRING)
//		        .notNull()
//		        .primitiveTypeExpected()
//		        .objectTypeExpected()
//		        .unsignedTypeExpected()
//		        .build();
//		AttributeTypeInfo attributeTypeInfo = new FakeAttributeTypeInfo(NeutralType.STRING).notNull() ;
//		
//		assertEquals(literalNull, getLiteralValuesProvider().getInitValue(attributeTypeInfo, typeConverter.getType(attributeTypeInfo) ) );
//		assertEquals(literalNull, getInitValue( new FakeAttributeTypeInfo(NeutralType.STRING).notNull() ) );
		
//		assertEquals(literalNull, getLiteralValuesProvider().getInitValue(NeutralType.STRING, NULLABLE) );
		assertEquals(literalNull, getInitValue( new FakeAttributeTypeInfo(NeutralType.STRING) ) );
		assertEquals(literalNull, getInitValue( new FakeAttributeTypeInfo(NeutralType.STRING).nullable() ) );
//		assertEquals("\"\"",      getLiteralValuesProvider().getInitValue(NeutralType.STRING, NOT_NULL) );
		assertEquals("\"\"",      getInitValue( new FakeAttributeTypeInfo(NeutralType.STRING).notNull() ) );

		//--- "AnyVal" Scala types are considered as "primitive types" => NOT NULLABLE
		//    Boolean, Byte, Short, Int, Long, Float, Double
		assertEquals("false",     getInitValue( new FakeAttributeTypeInfo(NeutralType.BOOLEAN) ) );
		assertEquals("false",     getInitValue( new FakeAttributeTypeInfo(NeutralType.BOOLEAN).notNull() ) );
		assertEquals("false",     getInitValue( new FakeAttributeTypeInfo(NeutralType.BOOLEAN).unsignedTypeExpected() ) );
		assertEquals("false",     getInitValue( new FakeAttributeTypeInfo(NeutralType.BOOLEAN).objectTypeExpected() ) );

		assertEquals("0",  getInitValue( new FakeAttributeTypeInfo(NeutralType.BYTE) ) );
		assertEquals("0",  getInitValue( new FakeAttributeTypeInfo(NeutralType.BYTE).notNull() ) );
		assertEquals("0",  getInitValue( new FakeAttributeTypeInfo(NeutralType.BYTE).unsignedTypeExpected() ) );
		assertEquals("0",  getInitValue( new FakeAttributeTypeInfo(NeutralType.BYTE).objectTypeExpected() ) );
		
		assertEquals("0",  getInitValue( new FakeAttributeTypeInfo(NeutralType.SHORT) ) );
		assertEquals("0",  getInitValue( new FakeAttributeTypeInfo(NeutralType.SHORT).notNull() ) );
		assertEquals("0",  getInitValue( new FakeAttributeTypeInfo(NeutralType.SHORT).unsignedTypeExpected() ) );
		assertEquals("0",  getInitValue( new FakeAttributeTypeInfo(NeutralType.SHORT).objectTypeExpected() ) );
		assertEquals("0",  getInitValue( new FakeAttributeTypeInfo(NeutralType.SHORT).primitiveTypeExpected() ) );

		assertEquals("0", getInitValue( new FakeAttributeTypeInfo(NeutralType.INTEGER)  ) );
		assertEquals("0", getInitValue( new FakeAttributeTypeInfo(NeutralType.INTEGER).notNull() ) );
		assertEquals("0", getInitValue( new FakeAttributeTypeInfo(NeutralType.INTEGER).unsignedTypeExpected() ) );
		assertEquals("0", getInitValue( new FakeAttributeTypeInfo(NeutralType.INTEGER).objectTypeExpected() ) );
		assertEquals("0", getInitValue( new FakeAttributeTypeInfo(NeutralType.INTEGER).primitiveTypeExpected() ) );

		assertEquals("0L", getInitValue( new FakeAttributeTypeInfo(NeutralType.LONG)  ) );
		assertEquals("0L", getInitValue( new FakeAttributeTypeInfo(NeutralType.LONG).notNull() ) );
		
		assertEquals("0.0F", getInitValue( new FakeAttributeTypeInfo(NeutralType.FLOAT)  ) );
		assertEquals("0.0F", getInitValue( new FakeAttributeTypeInfo(NeutralType.FLOAT).notNull() ) );

		assertEquals("0.0D", getInitValue( new FakeAttributeTypeInfo(NeutralType.DOUBLE)  ) );
		assertEquals("0.0D", getInitValue( new FakeAttributeTypeInfo(NeutralType.DOUBLE).notNull() ) );
		
		//--- Decimal is nullable
		assertEquals(literalNull,       getInitValue( new FakeAttributeTypeInfo(NeutralType.DECIMAL)  ) );
		assertEquals(literalNull,       getInitValue( new FakeAttributeTypeInfo(NeutralType.DECIMAL).unsignedTypeExpected() ) );
		assertEquals("BigDecimal(0.0)", getInitValue( new FakeAttributeTypeInfo(NeutralType.DECIMAL).notNull() ) );
		assertEquals("BigDecimal(0.0)", getInitValue( new FakeAttributeTypeInfo(NeutralType.DECIMAL).notNull().unsignedTypeExpected() ) );
		
		//--- Temporal types
		assertEquals(literalNull,       getInitValue( new FakeAttributeTypeInfo(NeutralType.DATE)  ) );
		assertEquals("LocalDate.now()", getInitValue( new FakeAttributeTypeInfo(NeutralType.DATE).notNull() ) );
		
		assertEquals(literalNull,       getInitValue( new FakeAttributeTypeInfo(NeutralType.TIME)  ) );
		assertEquals("LocalTime.now()", getInitValue( new FakeAttributeTypeInfo(NeutralType.TIME).notNull() ) );

		assertEquals(literalNull,           getInitValue( new FakeAttributeTypeInfo(NeutralType.TIMESTAMP)  ) );
		assertEquals("LocalDateTime.now()", getInitValue( new FakeAttributeTypeInfo(NeutralType.TIMESTAMP).notNull() ) );
		
		assertEquals(literalNull,           getInitValue( new FakeAttributeTypeInfo(NeutralType.DATETIME)  ) ); // v 4.3
		assertEquals("LocalDateTime.now()", getInitValue( new FakeAttributeTypeInfo(NeutralType.DATETIME).notNull() ) ); // v 4.3
		
		assertEquals(literalNull,            getInitValue( new FakeAttributeTypeInfo(NeutralType.DATETIMETZ)  ) ); // v 4.3
		assertEquals("OffsetDateTime.now()", getInitValue( new FakeAttributeTypeInfo(NeutralType.DATETIMETZ).notNull() ) ); // v 4.3

		assertEquals(literalNull,         getInitValue( new FakeAttributeTypeInfo(NeutralType.TIMETZ)  ) ); // v 4.3
		assertEquals("OffsetTime.now()",  getInitValue( new FakeAttributeTypeInfo(NeutralType.TIMETZ).notNull() ) ); // v 4.3

		//---
		assertEquals(literalNull,        getInitValue( new FakeAttributeTypeInfo(NeutralType.UUID)  ) ); // v 4.3
		assertEquals("new UUID(0L,0L)",  getInitValue( new FakeAttributeTypeInfo(NeutralType.UUID).notNull() ) ); // v 4.3

		assertEquals(literalNull,          getInitValue( new FakeAttributeTypeInfo(NeutralType.BINARY)  ) ); // v 4.3
		assertEquals("Array.empty[Byte]",  getInitValue( new FakeAttributeTypeInfo(NeutralType.BINARY).notNull() ) ); // v 4.3
		
/**
		
		assertEquals(literalNull, getLiteralValuesProvider().getInitValue(NeutralType.INTEGER, NULLABLE) );
		assertEquals("0",         getLiteralValuesProvider().getInitValue(NeutralType.INTEGER, NOT_NULL) );
		assertEquals(literalNull, getLiteralValuesProvider().getInitValue(NeutralType.LONG, NULLABLE) );
		assertEquals("0L",        getLiteralValuesProvider().getInitValue(NeutralType.LONG, NOT_NULL) );
		
		assertEquals(literalNull,   getLiteralValuesProvider().getInitValue(NeutralType.FLOAT, NULLABLE) );
		assertEquals("0.0F",        getLiteralValuesProvider().getInitValue(NeutralType.FLOAT, NOT_NULL) );
		assertEquals(literalNull,   getLiteralValuesProvider().getInitValue(NeutralType.DOUBLE, NULLABLE) );
		assertEquals("0.0D",        getLiteralValuesProvider().getInitValue(NeutralType.DOUBLE, NOT_NULL) );
		assertEquals(literalNull,       getLiteralValuesProvider().getInitValue(NeutralType.DECIMAL, NULLABLE) );
		assertEquals("BigDecimal(0.0)", getLiteralValuesProvider().getInitValue(NeutralType.DECIMAL, NOT_NULL) );
		
		assertEquals(literalNull,       getLiteralValuesProvider().getInitValue(NeutralType.DATE, NULLABLE) );
		assertEquals("LocalDate.now()", getLiteralValuesProvider().getInitValue(NeutralType.DATE, NOT_NULL) );
		assertEquals(literalNull,       getLiteralValuesProvider().getInitValue(NeutralType.TIME, NULLABLE) );
		assertEquals("LocalTime.now()", getLiteralValuesProvider().getInitValue(NeutralType.TIME, NOT_NULL) );
		assertEquals(literalNull,           getLiteralValuesProvider().getInitValue(NeutralType.TIMESTAMP, NULLABLE) );
		assertEquals("LocalDateTime.now()", getLiteralValuesProvider().getInitValue(NeutralType.TIMESTAMP, NOT_NULL) );
		assertEquals(literalNull,           getLiteralValuesProvider().getInitValue(NeutralType.DATETIME, NULLABLE) ); // v 4.3
		assertEquals("LocalDateTime.now()", getLiteralValuesProvider().getInitValue(NeutralType.DATETIME, NOT_NULL) ); // v 4.3
		assertEquals(literalNull,            getLiteralValuesProvider().getInitValue(NeutralType.DATETIMETZ, NULLABLE) ); // v 4.3
		assertEquals("OffsetDateTime.now()", getLiteralValuesProvider().getInitValue(NeutralType.DATETIMETZ, NOT_NULL) ); // v 4.3
		assertEquals(literalNull,            getLiteralValuesProvider().getInitValue(NeutralType.TIMETZ, NULLABLE) ); // v 4.3
		assertEquals("OffsetTime.now()",     getLiteralValuesProvider().getInitValue(NeutralType.TIMETZ, NOT_NULL) ); // v 4.3
		
		assertEquals(literalNull,         getLiteralValuesProvider().getInitValue(NeutralType.UUID, NULLABLE) ); // v 4.3
		assertEquals("new UUID(0L,0L)",   getLiteralValuesProvider().getInitValue(NeutralType.UUID, NOT_NULL) ); // v 4.3
		
		assertEquals(literalNull,         getLiteralValuesProvider().getInitValue(NeutralType.BINARY, NULLABLE) ); 
		assertEquals("Array.empty[Byte]", getLiteralValuesProvider().getInitValue(NeutralType.BINARY, NOT_NULL) ); 
**/
	}

}
