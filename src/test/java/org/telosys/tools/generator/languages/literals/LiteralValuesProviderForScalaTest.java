package org.telosys.tools.generator.languages.literals;

import org.junit.Test;
import org.telosys.tools.generator.languages.types.LanguageType;
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
	
	@Test
	public void testInitValue() {
		String literalNull = getLiteralValuesProvider().getLiteralNull();
		assertEquals(literalNull, getLiteralValuesProvider().getInitValue(NeutralType.STRING, NULLABLE) );
		assertEquals("\"\"",      getLiteralValuesProvider().getInitValue(NeutralType.STRING, NOT_NULL) );

		assertEquals(literalNull, getLiteralValuesProvider().getInitValue(NeutralType.BOOLEAN, NULLABLE) );
		assertEquals("false",     getLiteralValuesProvider().getInitValue(NeutralType.BOOLEAN, NOT_NULL) );

		assertEquals(literalNull, getLiteralValuesProvider().getInitValue(NeutralType.BYTE, NULLABLE) );
		assertEquals("0",         getLiteralValuesProvider().getInitValue(NeutralType.BYTE, NOT_NULL) );
		assertEquals(literalNull, getLiteralValuesProvider().getInitValue(NeutralType.SHORT, NULLABLE) );
		assertEquals("0",         getLiteralValuesProvider().getInitValue(NeutralType.SHORT, NOT_NULL) );
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
	}

}
