package org.telosys.tools.generator.languages.literals;

import static org.telosys.tools.generator.languages.types.AttributeTypeConst.NOT_NULL;
import static org.telosys.tools.generator.languages.types.AttributeTypeConst.OBJECT_TYPE;
import static org.telosys.tools.generator.languages.types.AttributeTypeConst.PRIMITIVE_TYPE;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.generator.languages.types.LanguageType;
import org.telosys.tools.generic.model.types.NeutralType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class LiteralValuesProviderForJavaTest extends AbstractLiteralsTest {
	
	private static final String JAVA_TRUE  = "true" ;
	private static final String JAVA_FALSE = "false" ;

	//----------------------------------------------------------------------------------
	@Override
	protected String getLanguageName() {
		return "Java" ;
	}	
	//----------------------------------------------------------------------------------

	private void setByte(byte v)   { /* nothing */ } 
	private void setByteObj(Byte v)   { /* nothing */ } 
	private void setShort(short v) { /* nothing */ } 
	private void setShortObj(Short v) { /* nothing */ } 
	private void setInt(int v)     { /* nothing */ } 
	private void setIntObj(Integer v)     { /* nothing */ } 
	private void setLong(long v)   { /* nothing */ } 
	private void setLongObj(Long v)   { /* nothing */ } 

	@Test
	public void testJavaLiteralExamples() {
		//--- byte
		byte  b =  12;
		Byte  b2 =  127 ; // Byte.MAX_VALUE;  ( 128 => Too high )
		assertEquals(12, b);
		assertNotNull(b2);
		assertNotNull( Byte.valueOf((byte)23) ); // "(byte)" is required
		setByte((byte)12); // "(byte)" is required for a method parameter, error if setByte(12)
		setByteObj((byte)12);

		//--- short : always cast with "(short)" 
		short sh = 12; // No suffix (like s or S) is needed for a short variable init  
		assertEquals(12, sh);
		assertNotNull( Short.valueOf((short)0) ); // "(short)" is required, error if "Short.valueOf(0)"
		setShort((short)12); // "(short)" is required for a method parameter, error if setShort(12)
		setShortObj((short)12);
		
		//--- int
		int i = 123;  // error if long literal like 123L
		assertEquals(123, i);
		int i2 = (short)12; // short to int is OK 
		assertEquals(12, i2);
		setInt(12);
		setIntObj(12);
		
		//--- long
		long   l = 12345L ; // for a long you must append an 'L' or 'l' suffix to the number 
		assertNotNull(l);
		setLong(12L);
		setLong(12);
		setLongObj(12L);
		
		//--- float
		float f1 =   12345.60F ; // for a float 'F' suffix is required
		assertEquals(12345.60F, f1, 0.0001);
		float f2 = 12345F ;
		assertEquals(12345F, f2, 0.0001);
		
		//--- double
		double d = 456.89D ;
		assertEquals(456.89D, d, 0.0001);
		
		BigDecimal bigDecimal = BigDecimal.valueOf(12345678.5);
		assertNotNull(bigDecimal);
	}

	@Test
	public void testJavaEqualsExamples() {
		
		byte  b =  12;
		Assert.assertTrue( b == 12 ) ;
		Byte  b2 =  34;
		Assert.assertTrue( b2 == 34 ) ;
		
		Float  f = 12345.60F ;
		Assert.assertTrue( f == 12345.60F ) ;
		
		Double d = 456.89D ;
		Assert.assertTrue( d == 456.89D ) ;

		Long   l = 12345L ;
		Assert.assertTrue( l == 12345L ) ;

		BigDecimal bigDecimal = BigDecimal.valueOf(12345678.5) ;
		Assert.assertTrue( bigDecimal.equals( BigDecimal.valueOf(12345678.5) ) ) ;
	}

	@Test
	public void testLiteralValueForNULL() {
		assertEquals("null", getLiteralValuesProvider().getLiteralNull() );
	}
	@Test
	public void testLiteralValueForTRUE() {
		assertEquals(JAVA_TRUE, getLiteralValuesProvider().getLiteralTrue() );
	}
	@Test
	public void testLiteralValueForFALSE() {
		assertEquals(JAVA_FALSE, getLiteralValuesProvider().getLiteralFalse() );
	}
	
	@Test
	public void testEqualsStatement() {
		// strings
		assertEquals(".equals(s)",       getLiteralValuesProvider().getEqualsStatement("s",       getLanguageType(NeutralType.STRING )) );
		assertEquals(".equals(\"ABC\")", getLiteralValuesProvider().getEqualsStatement("\"ABC\"", getLanguageType(NeutralType.STRING )) );

		//-------------
		// boolean
		boolean  bool1 = false ;  
		assertTrue( bool1 == false ) ;
		assertEquals(" == true", getLiteralValuesProvider().getEqualsStatement(JAVA_TRUE, getLanguageType(NeutralType.BOOLEAN, PRIMITIVE_TYPE )) );		
		// Boolean
		Boolean  bool2 = true ;   
		assertTrue( bool2 == true ) ;
		assertTrue( bool2.equals(true)) ;
		assertEquals(".equals(true)", getLiteralValuesProvider().getEqualsStatement(JAVA_TRUE, getLanguageType(NeutralType.BOOLEAN, OBJECT_TYPE )) );
		assertEquals(".equals(b1)",   getLiteralValuesProvider().getEqualsStatement("b1",    getLanguageType(NeutralType.BOOLEAN, OBJECT_TYPE )) );

		// int
		int i = 123 ; 
		assertTrue( i == 123 ) ;
		assertEquals(" == 123", getLiteralValuesProvider().getEqualsStatement("123", getLanguageType(NeutralType.INTEGER, PRIMITIVE_TYPE )) );
		assertEquals(" == i1",  getLiteralValuesProvider().getEqualsStatement("i1",  getLanguageType(NeutralType.INTEGER, PRIMITIVE_TYPE )) );
		// Integer
		Integer i2 = 456 ;
		assertTrue( i2 == 456 ) ;
		assertTrue( i2.equals(456) ) ;
		assertEquals(".equals(456)", getLiteralValuesProvider().getEqualsStatement("456", getLanguageType(NeutralType.INTEGER, OBJECT_TYPE )) );
		assertEquals(".equals(val)", getLiteralValuesProvider().getEqualsStatement("val", getLanguageType(NeutralType.INTEGER, OBJECT_TYPE )) );
		
		// BigDecimal
		java.math.BigDecimal bd = new BigDecimal(1234567890);
		assertTrue( bd.equals((new BigDecimal(1234567890))) ) ;
		assertEquals(".equals(big1)", getLiteralValuesProvider().getEqualsStatement("big1", getLanguageType(NeutralType.DECIMAL, OBJECT_TYPE )) );
		assertEquals(".equals(big2)", getLiteralValuesProvider().getEqualsStatement("big2", getLanguageType(NeutralType.DECIMAL, PRIMITIVE_TYPE )) );
	}

	//-------------------------------------------------------------------------------------------
	
	@Test
	public void testLiteralValuesForBOOLEAN() {
		LanguageType lt = getLanguageType(NeutralType.BOOLEAN, PRIMITIVE_TYPE );
		assertEquals(JAVA_TRUE,  getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals(JAVA_FALSE, getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
		assertEquals(JAVA_TRUE,  getLiteralValuesProvider().generateLiteralValue(lt, 0, 3).getCurrentLanguageValue() );
		assertEquals(JAVA_FALSE, getLiteralValuesProvider().generateLiteralValue(lt, 0, 4).getCurrentLanguageValue() );
		lt = getLanguageType(NeutralType.BOOLEAN, OBJECT_TYPE );
		assertEquals("Boolean.valueOf(true)",  getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("Boolean.valueOf(false)", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
	}

	@Test
	public void testLiteralValuesForSTRING() {
		LanguageType lt = getLanguageType(NeutralType.STRING );
		int maxlen = 3;
		assertEquals("\"AAA\"", getLiteralValuesProvider().generateLiteralValue(lt, maxlen,  1).getCurrentLanguageValue() );
		assertEquals("\"BBB\"", getLiteralValuesProvider().generateLiteralValue(lt, maxlen,  2).getCurrentLanguageValue() );
		assertEquals("\"CCC\"", getLiteralValuesProvider().generateLiteralValue(lt, maxlen,  3).getCurrentLanguageValue() );
		assertEquals("\"ZZZ\"", getLiteralValuesProvider().generateLiteralValue(lt, maxlen, 26).getCurrentLanguageValue() ); // 26 : "Z" = MAX
		assertEquals("\"AAA\"", getLiteralValuesProvider().generateLiteralValue(lt, maxlen, 27).getCurrentLanguageValue() ); // 27 restart with "A"
		assertEquals("\"ZZZ\"", getLiteralValuesProvider().generateLiteralValue(lt, maxlen, 26).getCurrentLanguageValue() ); // 26 : "Z" = MAX
		// length = 0
		assertEquals("\"\"",   getLiteralValuesProvider().generateLiteralValue(lt, 0,  1).getCurrentLanguageValue() );
		assertEquals("\"\"",   getLiteralValuesProvider().generateLiteralValue(lt, 0, 26).getCurrentLanguageValue() );
	}

	@Test
	public void testLiteralValuesForBYTE() {
		LanguageType lt = getLanguageType(NeutralType.BYTE, PRIMITIVE_TYPE );
		assertEquals("(byte)0", getLiteralValuesProvider().generateLiteralValue(lt, 0, 0).getCurrentLanguageValue() );
		assertEquals("(byte)1", getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("(byte)2", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
		lt = getLanguageType(NeutralType.BYTE, OBJECT_TYPE );
		assertEquals("Byte.valueOf((byte)0)", getLiteralValuesProvider().generateLiteralValue(lt, 0, 0).getCurrentLanguageValue() );
		assertEquals("Byte.valueOf((byte)1)", getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("Byte.valueOf((byte)2)", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
		lt = getLanguageType(NeutralType.BYTE, NOT_NULL ); // Not null => primitive type
		assertEquals("(byte)1", getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		lt = getLanguageType(NeutralType.BYTE ); // nullable by default => wrapper type
		assertEquals("Byte.valueOf((byte)1)", getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
	}

	@Test
	public void testLiteralValuesForSHORT() {
		LanguageType lt = getLanguageType(NeutralType.SHORT, PRIMITIVE_TYPE );
		assertEquals("(short)0", getLiteralValuesProvider().generateLiteralValue(lt, 0, 0).getCurrentLanguageValue() );
		assertEquals("(short)1", getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("(short)2", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
		// max value
		assertEquals("(short)32767", getLiteralValuesProvider().generateLiteralValue(lt, 0, 32767).getCurrentLanguageValue() );
		// restart with % 32767 --> 1
		assertEquals("(short)1",     getLiteralValuesProvider().generateLiteralValue(lt, 0, 32768).getCurrentLanguageValue() );
		lt = getLanguageType(NeutralType.SHORT, OBJECT_TYPE );
		assertEquals("Short.valueOf((short)0)", getLiteralValuesProvider().generateLiteralValue(lt, 0, 0).getCurrentLanguageValue() );
		assertEquals("Short.valueOf((short)1)", getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("Short.valueOf((short)2)", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
		lt = getLanguageType(NeutralType.SHORT, NOT_NULL ); // Not null => primitive
		assertEquals("(short)0", getLiteralValuesProvider().generateLiteralValue(lt, 0, 0).getCurrentLanguageValue() );
		lt = getLanguageType(NeutralType.SHORT ); // nullable by default => wrapper type
		assertEquals("Short.valueOf((short)0)", getLiteralValuesProvider().generateLiteralValue(lt, 0, 0).getCurrentLanguageValue() );
	}
	
	@Test
	public void testLiteralValuesForINT() {
		LanguageType lt = getLanguageType(NeutralType.INTEGER, PRIMITIVE_TYPE );
		assertEquals(  "0", getLiteralValuesProvider().generateLiteralValue(lt, 0, 0).getCurrentLanguageValue() );
		assertEquals("100", getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("200", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
		lt = getLanguageType(NeutralType.INTEGER, OBJECT_TYPE );
		assertEquals("Integer.valueOf(0)",   getLiteralValuesProvider().generateLiteralValue(lt, 0, 0).getCurrentLanguageValue() );
		assertEquals("Integer.valueOf(100)", getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("Integer.valueOf(200)", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
		lt = getLanguageType(NeutralType.INTEGER, NOT_NULL );
		assertEquals(  "0", getLiteralValuesProvider().generateLiteralValue(lt, 0, 0).getCurrentLanguageValue() );
		lt = getLanguageType(NeutralType.INTEGER ); // Object by default
		assertEquals("Integer.valueOf(0)",   getLiteralValuesProvider().generateLiteralValue(lt, 0, 0).getCurrentLanguageValue() );
	}
	
	@Test
	public void testLiteralValuesForLONG() {
		LanguageType lt = getLanguageType(NeutralType.LONG, PRIMITIVE_TYPE );
		assertEquals(   "0L", getLiteralValuesProvider().generateLiteralValue(lt, 0, 0).getCurrentLanguageValue() );
		assertEquals("1000L", getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("2000L", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
		lt = getLanguageType(NeutralType.LONG, OBJECT_TYPE );
		assertEquals("Long.valueOf(0L)",    getLiteralValuesProvider().generateLiteralValue(lt, 0, 0).getCurrentLanguageValue() );
		assertEquals("Long.valueOf(1000L)", getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("Long.valueOf(2000L)", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
		lt = getLanguageType(NeutralType.LONG, NOT_NULL );
		assertEquals(   "0L", getLiteralValuesProvider().generateLiteralValue(lt, 0, 0).getCurrentLanguageValue() );
		lt = getLanguageType(NeutralType.LONG );
		assertEquals("Long.valueOf(0L)",    getLiteralValuesProvider().generateLiteralValue(lt, 0, 0).getCurrentLanguageValue() );
	}
	
	@Test
	public void testLiteralValuesForFLOAT() {
		LanguageType lt = getLanguageType(NeutralType.FLOAT, PRIMITIVE_TYPE );
		assertEquals(   "0.5F", getLiteralValuesProvider().generateLiteralValue(lt, 0, 0).getCurrentLanguageValue() );
		assertEquals("1000.5F", getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("2000.5F", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
		lt = getLanguageType(NeutralType.FLOAT, OBJECT_TYPE );
		assertEquals("Float.valueOf(0.5F)",    getLiteralValuesProvider().generateLiteralValue(lt, 0, 0).getCurrentLanguageValue() );
		assertEquals("Float.valueOf(1000.5F)", getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("Float.valueOf(2000.5F)", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
		lt = getLanguageType(NeutralType.FLOAT, NOT_NULL );
		assertEquals(   "0.5F", getLiteralValuesProvider().generateLiteralValue(lt, 0, 0).getCurrentLanguageValue() );
		lt = getLanguageType(NeutralType.FLOAT );
		assertEquals("Float.valueOf(0.5F)",    getLiteralValuesProvider().generateLiteralValue(lt, 0, 0).getCurrentLanguageValue() );
	}

	@Test
	public void testLiteralValuesForDOUBLE() {
		LanguageType lt = getLanguageType(NeutralType.DOUBLE, PRIMITIVE_TYPE );
		assertEquals(   "0.66D", getLiteralValuesProvider().generateLiteralValue(lt, 0, 0).getCurrentLanguageValue() );
		assertEquals("1000.66D", getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("2000.66D", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
		lt = getLanguageType(NeutralType.DOUBLE, OBJECT_TYPE );
		assertEquals("Double.valueOf(0.66D)",    getLiteralValuesProvider().generateLiteralValue(lt, 0, 0).getCurrentLanguageValue() );
		assertEquals("Double.valueOf(1000.66D)", getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("Double.valueOf(2000.66D)", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
		lt = getLanguageType(NeutralType.DOUBLE, NOT_NULL );
		assertEquals(   "0.66D", getLiteralValuesProvider().generateLiteralValue(lt, 0, 0).getCurrentLanguageValue() );
		lt = getLanguageType(NeutralType.DOUBLE );
		assertEquals("Double.valueOf(0.66D)",    getLiteralValuesProvider().generateLiteralValue(lt, 0, 0).getCurrentLanguageValue() );
	}

	@Test
	public void testLiteralValuesForDECIMAL() {
		LanguageType lt = getLanguageType(NeutralType.DECIMAL );
		assertEquals("java.math.BigDecimal.valueOf(0.77)",     getLiteralValuesProvider().generateLiteralValue(lt, 0, 0).getCurrentLanguageValue() );
		assertEquals("java.math.BigDecimal.valueOf(10000.77)", getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("java.math.BigDecimal.valueOf(20000.77)", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
		lt = getLanguageType(NeutralType.DECIMAL, PRIMITIVE_TYPE );
		assertEquals("java.math.BigDecimal.valueOf(30000.77)", getLiteralValuesProvider().generateLiteralValue(lt, 0, 3).getCurrentLanguageValue() );
		lt = getLanguageType(NeutralType.DECIMAL, OBJECT_TYPE );
		assertEquals("java.math.BigDecimal.valueOf(40000.77)", getLiteralValuesProvider().generateLiteralValue(lt, 0, 4).getCurrentLanguageValue() );
	}

	@Test
	public void testLiteralValuesForDATE() {
		LanguageType lt = getLanguageType(NeutralType.DATE );
		int maxlen = 999; // not used
		assertEquals("java.time.LocalDate.parse(\"2000-06-22\")", getLiteralValuesProvider().generateLiteralValue(lt, maxlen,  0).getCurrentLanguageValue() );
		assertEquals("java.time.LocalDate.parse(\"2001-06-22\")", getLiteralValuesProvider().generateLiteralValue(lt, maxlen,  1).getCurrentLanguageValue() );
		assertEquals("java.time.LocalDate.parse(\"2002-06-22\")", getLiteralValuesProvider().generateLiteralValue(lt, maxlen,  2).getCurrentLanguageValue() );
	}
	
	@Test
	public void testLiteralValuesForTIME() {
		// Check literal expression  
		assertNotNull( java.time.LocalTime.parse("01:46:52") );
		// Check expected values 
		LanguageType lt = getLanguageType(NeutralType.TIME );
		int maxlen = 999; // not used
		assertEquals("java.time.LocalTime.parse(\"00:46:52\")", getLiteralValuesProvider().generateLiteralValue(lt, maxlen,  0).getCurrentLanguageValue() );
		assertEquals("java.time.LocalTime.parse(\"01:46:52\")", getLiteralValuesProvider().generateLiteralValue(lt, maxlen,  1).getCurrentLanguageValue() );
	}
	@Test
	public void testLiteralValuesForTIMETZ() {
		// Check literal expression 
		assertNotNull( java.time.OffsetTime.parse("01:46:52+02:00") );
		// Check expected 
		LanguageType lt = getLanguageType(NeutralType.TIMETZ );
		int maxlen = 999; // not used
		assertEquals("java.time.OffsetTime.parse(\"00:46:52+00:00\")", getLiteralValuesProvider().generateLiteralValue(lt, maxlen,  0).getCurrentLanguageValue() );
		assertEquals("java.time.OffsetTime.parse(\"01:46:52+01:00\")", getLiteralValuesProvider().generateLiteralValue(lt, maxlen,  1).getCurrentLanguageValue() );
		assertEquals("java.time.OffsetTime.parse(\"02:46:52+02:00\")", getLiteralValuesProvider().generateLiteralValue(lt, maxlen,  2).getCurrentLanguageValue() );
	}
	
	@Test
	public void testLiteralValuesForTIMESTAMP() {
		// Check literal expression  
		assertNotNull( java.time.LocalDateTime.parse("2001-05-21T01:46:52") );
		// Check expected 
		LanguageType lt = getLanguageType(NeutralType.TIMESTAMP );
		int maxlen = 999; // not used
		assertEquals("java.time.LocalDateTime.parse(\"2000-05-21T00:47:53\")", getLiteralValuesProvider().generateLiteralValue(lt, maxlen,  0).getCurrentLanguageValue() );
		assertEquals("java.time.LocalDateTime.parse(\"2001-05-21T01:47:53\")", getLiteralValuesProvider().generateLiteralValue(lt, maxlen,  1).getCurrentLanguageValue() );
	}

	@Test
	public void testLiteralValuesForDATETIME() {
		// Check literal expression 
		assertNotNull( java.time.LocalDateTime.parse("2001-05-21T01:46:52") );
		// Check expected 
		LanguageType lt = getLanguageType(NeutralType.DATETIME );
		int maxlen = 999; // not used
		assertEquals("java.time.LocalDateTime.parse(\"2000-05-21T00:47:53\")", getLiteralValuesProvider().generateLiteralValue(lt, maxlen,  0).getCurrentLanguageValue() );
		assertEquals("java.time.LocalDateTime.parse(\"2001-05-21T01:47:53\")", getLiteralValuesProvider().generateLiteralValue(lt, maxlen,  1).getCurrentLanguageValue() );
		assertEquals("java.time.LocalDateTime.parse(\"2002-05-21T02:47:53\")", getLiteralValuesProvider().generateLiteralValue(lt, maxlen,  2).getCurrentLanguageValue() );
	}
	@Test
	public void testLiteralValuesForDATETIMETZ() {
		// Check literal expression 
		assertNotNull( java.time.OffsetDateTime.parse("2001-05-21T01:46:52+02:00") );
		// Check expected 
		LanguageType lt = getLanguageType(NeutralType.DATETIMETZ );
		int maxlen = 999; // not used
		assertEquals("java.time.OffsetDateTime.parse(\"2000-05-21T00:47:53+00:00\")", getLiteralValuesProvider().generateLiteralValue(lt, maxlen,  0).getCurrentLanguageValue() );
		assertEquals("java.time.OffsetDateTime.parse(\"2001-05-21T01:47:53+01:00\")", getLiteralValuesProvider().generateLiteralValue(lt, maxlen,  1).getCurrentLanguageValue() );
		assertEquals("java.time.OffsetDateTime.parse(\"2002-05-21T02:47:53+02:00\")", getLiteralValuesProvider().generateLiteralValue(lt, maxlen,  2).getCurrentLanguageValue() );
	}
	
	@Test
	public void testLiteralValuesForUUID() {
		// Check literal expression 
		assertNotNull( java.util.UUID.fromString("123e4567-e89b-12d3-a456-426614174000") );
		// Check expected 
		LanguageType lt = getLanguageType(NeutralType.UUID );
		checkUUID( getLiteralValuesProvider().generateLiteralValue(lt, 0,   0).getCurrentLanguageValue() ) ;
		checkUUID( getLiteralValuesProvider().generateLiteralValue(lt, 0,   1).getCurrentLanguageValue() ) ;
		checkUUID( getLiteralValuesProvider().generateLiteralValue(lt, 0, 123).getCurrentLanguageValue() ) ;
	}
	private void checkUUID(String v) {
		assertTrue(v.startsWith("java.util.UUID.fromString(\""));
		assertTrue(v.endsWith( "\")" ) );
		String uuidValue = StrUtil.extractStringBetween(v, "(\"", "\")");
		assertEquals(36, uuidValue.length() );
		java.util.UUID uuid = java.util.UUID.fromString(uuidValue);
		assertNotNull(uuid);
	}
}
