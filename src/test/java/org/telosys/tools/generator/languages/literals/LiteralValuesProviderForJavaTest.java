package org.telosys.tools.generator.languages.literals;

import static org.telosys.tools.generator.languages.types.AttributeTypeConst.NOT_NULL;
import static org.telosys.tools.generator.languages.types.AttributeTypeConst.OBJECT_TYPE;
import static org.telosys.tools.generator.languages.types.AttributeTypeConst.PRIMITIVE_TYPE;

import java.math.BigDecimal;
import java.util.Calendar;

import org.junit.Assert;
import org.junit.Test;
import org.telosys.tools.generator.languages.types.LanguageType;
import org.telosys.tools.generator.languages.types.TypeConverter;
import org.telosys.tools.generator.languages.types.TypeConverterForJava;
import org.telosys.tools.generic.model.types.NeutralType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LiteralValuesProviderForJavaTest extends AbstractLiteralsTest {
	
	//----------------------------------------------------------------------------------
	@Override
	protected TypeConverter getTypeConverter() {
		return new TypeConverterForJava() ;
	}
	@Override
	protected LiteralValuesProvider getLiteralValuesProvider() {
		return new LiteralValuesProviderForJava() ;
	}
	//----------------------------------------------------------------------------------

	private void setByte(byte v)   { /* nothing */ } 
	private void setShort(short v) { /* nothing */ } 
	private void setInt(int v)     { /* nothing */ } 
	private void setLong(long v)   { /* nothing */ } 

	private void setByteObj(Byte v)   { /* nothing */ } 
	private void setShortObj(Short v) { /* nothing */ } 
	private void setIntObj(Integer v)     { /* nothing */ } 
	private void setLongObj(Long v)   { /* nothing */ } 

	@Test
	public void testJavaLiteralExamples() {
		setByte((byte)12);
		setShort((short)12);
		setInt(12);
		setLong(12L);
		
		setByteObj((byte)12);
		setShortObj((short)12);
		setIntObj(12);
		setLongObj(12L);
		
		byte  b =  12;
		Byte  b2 =  127 ; // Byte.MAX_VALUE;
		//Byte  b3 =  128 ; // Too high
		
		short sh = (short)12;
		long   l = 12345L ;
		
		float f1 = 12345.60F ;
		float f2 = 12345F ;
		double d = 456.89D ;
		
		BigDecimal bigDecimal = (new BigDecimal(12345678.5));
		java.util.Date utilDate = Calendar.getInstance().getTime() ;
		java.sql.Date sqlDate = (new java.sql.Date(Calendar.getInstance().getTime().getTime()));
		java.sql.Time sqlTime = (new java.sql.Time(Calendar.getInstance().getTime().getTime()));
		java.sql.Timestamp sqlTimestamp = (new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
	}

	@Test
	public void testJavaEqualsExamples() {
		
		byte  b =  12;
		Byte  b2 =  34;
		Assert.assertTrue( b2 == 34 ) ;
		
		Float  f = 12345.60F ;
		Assert.assertTrue( f == 12345.60F ) ;
		
		Double d = 456.89D ;
		Assert.assertTrue( d == 456.89D ) ;

		Long   l = 12345L ;
		Assert.assertTrue( l == 12345L ) ;

		BigDecimal bigDecimal = (new BigDecimal(12345678.5));
		Assert.assertTrue( bigDecimal.equals( (new BigDecimal(12345678.5)) ) ) ;
	}

	@Test
	public void testLiteralValueForNULL() {
		assertEquals("null", getLiteralValuesProvider().getLiteralNull() );
	}
	@Test
	public void testLiteralValueForTRUE() {
		assertEquals("true", getLiteralValuesProvider().getLiteralTrue() );
	}
	@Test
	public void testLiteralValueForFALSE() {
		assertEquals("false", getLiteralValuesProvider().getLiteralFalse() );
	}
	
	@Test
	public void testEqualsStatement() {
		// strings
//		String s = "ABC" ;
//		assertTrue( s.equals("ABC") ) ;
//		assertEquals(".equals(\"ABC\")", literalValuesProvider.getEqualsStatement("\"ABC\"", buildFakeLanguageTypeObject())  ) ;
		
		assertEquals(".equals(s)",       getLiteralValuesProvider().getEqualsStatement("s",       getLanguageType(NeutralType.STRING )) );
		assertEquals(".equals(\"ABC\")", getLiteralValuesProvider().getEqualsStatement("\"ABC\"", getLanguageType(NeutralType.STRING )) );
//		assertEquals(" == foo", getLiteralValuesProvider().getEqualsStatement("foo", getLanguageType(NeutralType.BOOLEAN )) );
//		assertEquals(" == foo", getLiteralValuesProvider().getEqualsStatement("foo", getLanguageType(NeutralType.INTEGER )) );
//		assertEquals(" == foo", getLiteralValuesProvider().getEqualsStatement("foo", getLanguageType(NeutralType.FLOAT )) );

		//-------------
		// boolean
		boolean  bool1 = false ;  
		assertTrue( bool1 == false ) ;
//		assertEquals(" == true", literalValuesProvider.getEqualsStatement("true", buildFakeLanguageTypePrimitive())  ) ;
		assertEquals(" == true", getLiteralValuesProvider().getEqualsStatement("true", getLanguageType(NeutralType.BOOLEAN, PRIMITIVE_TYPE )) );
		
		// Boolean
		Boolean  bool2 = true ;   
		assertTrue( bool2 == true ) ;
		assertTrue( bool2.equals(true)) ;
//		assertEquals(".equals(true)", literalValuesProvider.getEqualsStatement("true", buildFakeLanguageTypeObject())  ) ;
		assertEquals(".equals(true)", getLiteralValuesProvider().getEqualsStatement("true", getLanguageType(NeutralType.BOOLEAN, OBJECT_TYPE )) );
		assertEquals(".equals(x)",    getLiteralValuesProvider().getEqualsStatement("x",    getLanguageType(NeutralType.BOOLEAN, OBJECT_TYPE )) );

		// int
		int i = 123 ; 
		assertTrue( i == 123 ) ;
//		assertEquals(" == 123", literalValuesProvider.getEqualsStatement("123", buildFakeLanguageTypePrimitive())  ) ;
		assertEquals(" == 123", getLiteralValuesProvider().getEqualsStatement("123", getLanguageType(NeutralType.INTEGER, PRIMITIVE_TYPE )) );
		assertEquals(" == x",   getLiteralValuesProvider().getEqualsStatement("x",   getLanguageType(NeutralType.INTEGER, PRIMITIVE_TYPE )) );

		// Integer
		Integer i2 = 456 ;
		assertTrue( i2 == 456 ) ;
		assertTrue( i2.equals(456) ) ;
//		assertEquals(".equals(456)", literalValuesProvider.getEqualsStatement("456", buildFakeLanguageTypeObject())  ) ;
		assertEquals(".equals(456)", getLiteralValuesProvider().getEqualsStatement("456", getLanguageType(NeutralType.INTEGER, OBJECT_TYPE )) );
		assertEquals(".equals(x)",   getLiteralValuesProvider().getEqualsStatement("x",   getLanguageType(NeutralType.INTEGER, OBJECT_TYPE )) );
		
		// BigDecimal
		java.math.BigDecimal bd = new BigDecimal(1234567890);
		assertTrue( bd.equals((new BigDecimal(1234567890))) ) ;
//		assertEquals(".equals((new BigDecimal(1234567890)))", literalValuesProvider.getEqualsStatement("(new BigDecimal(1234567890))", buildFakeLanguageTypeObject())  ) ;
		assertEquals(".equals(x)",   getLiteralValuesProvider().getEqualsStatement("x",   getLanguageType(NeutralType.DECIMAL, OBJECT_TYPE )) );
		assertEquals(".equals(x)",   getLiteralValuesProvider().getEqualsStatement("x",   getLanguageType(NeutralType.DECIMAL, PRIMITIVE_TYPE )) );

	}

	//-------------------------------------------------------------------------------------------
	
//	public LanguageType build(String neutralType, String simpleType, String fullType, boolean primitiveType, String wrapperType ) {
//		return new LanguageType( neutralType, simpleType, fullType, primitiveType, wrapperType );
//	}
	
//	private void checkLiteralValue(Class<?> clazz, int maxLength, int step, String expected ) {
//		checkLiteralValue("", clazz, maxLength, step, expected );
//	}
//	private void checkLiteralValue(String neutralType, Class<?> clazz, int maxLength, int step, String expected ) {
//		// LanguageType languageType = buildLanguageObjectType(neutralType, clazz);
//		LanguageType languageType = build(neutralType, clazz.getSimpleName(), clazz.getCanonicalName(), clazz.isPrimitive(), clazz.getSimpleName() );
//		checkLiteralValue(languageType, maxLength, step, expected );
//	}
//	private void checkLiteralValue(LanguageType languageType, int maxLength, int step, String expected ) {
//		LiteralValuesProvider  literalValuesProvider = new LiteralValuesProviderForJava();
//		LiteralValue value = literalValuesProvider.generateLiteralValue(languageType, maxLength, step);
//		System.out.println("Literal value : '" + value + "'" );
//		assertEquals(expected, value.getCurrentLanguageValue() ) ;
//	}

//	@Test
//	public void testBooleanValues() {
//
//		checkLiteralValue(boolean.class, 0, 1, "true");
//		checkLiteralValue(boolean.class, 0, 2, "false");
//		checkLiteralValue(boolean.class, 0, 3, "true");
//		checkLiteralValue(boolean.class, 0, 4, "false");
//		checkLiteralValue(boolean.class, 0, 5, "true");
//
//		checkLiteralValue(Boolean.class, 0, 1, "Boolean.valueOf(true)"  );
//		checkLiteralValue(Boolean.class, 0, 2, "Boolean.valueOf(false)" );
//		checkLiteralValue(Boolean.class, 0, 3, "Boolean.valueOf(true)"  );
//	}
	@Test
	public void testLiteralValuesForBOOLEAN() {
		LanguageType lt = getLanguageType(NeutralType.BOOLEAN, PRIMITIVE_TYPE );
		assertEquals("true",  getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("false", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
		assertEquals("true",  getLiteralValuesProvider().generateLiteralValue(lt, 0, 3).getCurrentLanguageValue() );
		assertEquals("false", getLiteralValuesProvider().generateLiteralValue(lt, 0, 4).getCurrentLanguageValue() );
		lt = getLanguageType(NeutralType.BOOLEAN, OBJECT_TYPE );
		assertEquals("Boolean.valueOf(true)",  getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("Boolean.valueOf(false)", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
	}
	
//	@Test
//	public void testStringValues() {
//		checkLiteralValue(String.class, 5,  0, "\"AAAAA\""); // step 0 
//		
//		checkLiteralValue(String.class, 5,  1, "\"AAAAA\""); // start with "A"
//		checkLiteralValue(String.class, 5,  2, "\"BBBBB\"");
//		checkLiteralValue(String.class, 5, 26, "\"ZZZZZ\"");  // MAX STEP
//		
//		checkLiteralValue(String.class, 5, 27, "\"AAAAA\"");  // restart with "A"
//		checkLiteralValue(String.class, 5, 28, "\"BBBBB\"");
//		checkLiteralValue(String.class, 5, 52, "\"ZZZZZ\"");  // MAX STEP
//		
//		checkLiteralValue(String.class, 5, 53, "\"AAAAA\"");  // restart with "A"		
//
//		checkLiteralValue(String.class, 0,  1, "\"\""); 
//		checkLiteralValue(String.class, 1,  1, "\"A\""); 
//		checkLiteralValue(String.class, 2,  1, "\"AA\""); 
//	}
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
		assertEquals("java.math.BigDecimal.valueOf(0.77)",     getLiteralValuesProvider().generateLiteralValue(lt, 0, 0).getCurrentLanguageValue() );
		lt = getLanguageType(NeutralType.DECIMAL, OBJECT_TYPE );
		assertEquals("java.math.BigDecimal.valueOf(0.77)",     getLiteralValuesProvider().generateLiteralValue(lt, 0, 0).getCurrentLanguageValue() );
	}
	
	@Test
	public void testNumberValues() {
		
		// primitive types
//
//		checkLiteralValue(byte.class, 0, 0, "(byte)0");
//		checkLiteralValue(byte.class, 0, 1, "(byte)1");
//		checkLiteralValue(byte.class, 0, 2, "(byte)2");
//
//		checkLiteralValue(short.class, 0, 1, "(short)1");
//		checkLiteralValue(short.class, 0, 2, "(short)2");
//		checkLiteralValue(short.class, 0, 32767, "(short)32767"); // MAX VALUE
//		checkLiteralValue(short.class, 0, 32768, "(short)1"); // restart with % 32767 --> 1
//
//		checkLiteralValue(int.class, 0, 0, "0");
//		checkLiteralValue(int.class, 0, 1, "100");
//		checkLiteralValue(int.class, 0, 2, "200");
//
//		checkLiteralValue(long.class, 0, 0, "0L");
//		checkLiteralValue(long.class, 0, 1, "1000L");
//		checkLiteralValue(long.class, 0, 2, "2000L");
//
//		checkLiteralValue(float.class, 0, 0, "0.5F");
//		checkLiteralValue(float.class, 0, 1, "1000.5F");
//		checkLiteralValue(float.class, 0, 2, "2000.5F");

//		checkLiteralValue(double.class, 0, 0, "0.66D");
//		checkLiteralValue(double.class, 0, 1, "1000.66D");
//		checkLiteralValue(double.class, 0, 2, "2000.66D");
		
		// wrapper types
//		
//		checkLiteralValue(Byte.class, 0,   0, "Byte.valueOf((byte)0)");
//		checkLiteralValue(Byte.class, 0,   1, "Byte.valueOf((byte)1)");
//		checkLiteralValue(Byte.class, 0,   2, "Byte.valueOf((byte)2)");
//		checkLiteralValue(Byte.class, 0, 127, "Byte.valueOf((byte)127)"); // MAX VALUE
//		checkLiteralValue(Byte.class, 0, 128, "Byte.valueOf((byte)1)"); // restart with % 127 --> 1
//		checkLiteralValue(Byte.class, 0, 253, "Byte.valueOf((byte)126)"); 
//		checkLiteralValue(Byte.class, 0, 254, "Byte.valueOf((byte)127)"); // MAX VALUE BIS 
//		checkLiteralValue(Byte.class, 0, 255, "Byte.valueOf((byte)1)"); // restart with % 127 --> 1
//
//		checkLiteralValue(Short.class, 0, 0, "Short.valueOf((short)0)" );
//		checkLiteralValue(Short.class, 0, 1, "Short.valueOf((short)1)" );
//		checkLiteralValue(Short.class, 0, 2, "Short.valueOf((short)2)" );
//
//		checkLiteralValue(Integer.class, 0, 0, "Integer.valueOf(0)");
//		checkLiteralValue(Integer.class, 0, 1, "Integer.valueOf(100)");
//		checkLiteralValue(Integer.class, 0, 2, "Integer.valueOf(200)");
//		checkLiteralValue(Integer.class, 0, 2000, "Integer.valueOf(200000)");
//		checkLiteralValue(Integer.class, 0, 2000000, "Integer.valueOf(200000000)");
//
//		checkLiteralValue(Long.class, 0, 0, "Long.valueOf(0L)"     );
//		checkLiteralValue(Long.class, 0, 1, "Long.valueOf(1000L)"  );
//		checkLiteralValue(Long.class, 0, 2, "Long.valueOf(2000L)"  );
//
//		checkLiteralValue(Float.class, 0, 0, "Float.valueOf(0.5F)"    );
//		checkLiteralValue(Float.class, 0, 1, "Float.valueOf(1000.5F)" );
//		checkLiteralValue(Float.class, 0, 2, "Float.valueOf(2000.5F)" );

//		checkLiteralValue(Double.class, 0, 0, "Double.valueOf(0.66D)"    );
//		checkLiteralValue(Double.class, 0, 1, "Double.valueOf(1000.66D)" );
//		checkLiteralValue(Double.class, 0, 2, "Double.valueOf(2000.66D)" );
		
//		checkLiteralValue(BigDecimal.class, 0, 0, "java.math.BigDecimal.valueOf(0.77)"     );
//		checkLiteralValue(BigDecimal.class, 0, 1, "java.math.BigDecimal.valueOf(10000.77)" );
//		checkLiteralValue(BigDecimal.class, 0, 2, "java.math.BigDecimal.valueOf(20000.77)" );
	}

//	@Test
//	public void testUtilDateValues() {
//		
//		java.util.Date d = java.sql.Date.valueOf("2000-06-22");
//		System.out.println(" d = " + d);
//		checkLiteralValue(java.util.Date.class, 0,    0, "java.sql.Date.valueOf(\"2000-06-22\")");
//		checkLiteralValue(java.util.Date.class, 0,    1, "java.sql.Date.valueOf(\"2001-06-22\")");
//		checkLiteralValue(java.util.Date.class, 0,    2, "java.sql.Date.valueOf(\"2002-06-22\")");
//		checkLiteralValue(java.util.Date.class, 0,  999, "java.sql.Date.valueOf(\"2999-06-22\")");
//		checkLiteralValue(java.util.Date.class, 0, 1000, "java.sql.Date.valueOf(\"2000-06-22\")");
//		checkLiteralValue(java.util.Date.class, 0, 1256, "java.sql.Date.valueOf(\"2256-06-22\")");
//
//		checkLiteralValue(java.util.Date.class, 0, 10000589, "java.sql.Date.valueOf(\"2589-06-22\")");
//
//		checkLiteralValue(NeutralType.DATE, java.util.Date.class, 0,    0, "java.sql.Date.valueOf(\"2000-06-22\")");
//		checkLiteralValue(NeutralType.DATE, java.util.Date.class, 0,    1, "java.sql.Date.valueOf(\"2001-06-22\")");
//		checkLiteralValue(NeutralType.DATE, java.util.Date.class, 0,    2, "java.sql.Date.valueOf(\"2002-06-22\")");
//		checkLiteralValue(NeutralType.DATE, java.util.Date.class, 0,  999, "java.sql.Date.valueOf(\"2999-06-22\")");
//		checkLiteralValue(NeutralType.DATE, java.util.Date.class, 0, 1000, "java.sql.Date.valueOf(\"2000-06-22\")");
//
//		java.util.Date t1 = java.sql.Time.valueOf("00:46:52");
//		System.out.println(" t1 = " + t1);
//		SimpleDateFormat fmt = new SimpleDateFormat("dd MMM yyyy HH:mm:ss"); // 01 janv. 1970 00:46:52
//		System.out.println(" t1 = " + fmt.format(t1));
//
//		java.sql.Time t2 = java.sql.Time.valueOf("00:46:52") ;
//		System.out.println(" t2 = " + t2);
//		checkLiteralValue(NeutralType.TIME, java.util.Date.class, 0,    0, "java.sql.Time.valueOf(\"00:46:52\")");
//		checkLiteralValue(NeutralType.TIME, java.util.Date.class, 0,    1, "java.sql.Time.valueOf(\"01:46:52\")");
//		checkLiteralValue(NeutralType.TIME, java.util.Date.class, 0,    2, "java.sql.Time.valueOf(\"02:46:52\")");
//		checkLiteralValue(NeutralType.TIME, java.util.Date.class, 0,   23, "java.sql.Time.valueOf(\"23:46:52\")");
//		checkLiteralValue(NeutralType.TIME, java.util.Date.class, 0,   24, "java.sql.Time.valueOf(\"00:46:52\")");
//		checkLiteralValue(NeutralType.TIME, java.util.Date.class, 0,   25, "java.sql.Time.valueOf(\"01:46:52\")");
//		checkLiteralValue(NeutralType.TIME, java.util.Date.class, 0,   47, "java.sql.Time.valueOf(\"23:46:52\")");
//		checkLiteralValue(NeutralType.TIME, java.util.Date.class, 0,   48, "java.sql.Time.valueOf(\"00:46:52\")");
//
//		java.util.Date ts = java.sql.Timestamp.valueOf("2000-05-21 00:46:52");
//		System.out.println(" ts = " + ts);
//		checkLiteralValue(NeutralType.TIMESTAMP, java.util.Date.class, 0,    0, "java.sql.Timestamp.valueOf(\"2000-05-21 00:46:52\")");
//		checkLiteralValue(NeutralType.TIMESTAMP, java.util.Date.class, 0,    1, "java.sql.Timestamp.valueOf(\"2001-05-21 01:46:52\")");
//		checkLiteralValue(NeutralType.TIMESTAMP, java.util.Date.class, 0,    2, "java.sql.Timestamp.valueOf(\"2002-05-21 02:46:52\")");
//		checkLiteralValue(NeutralType.TIMESTAMP, java.util.Date.class, 0,   23, "java.sql.Timestamp.valueOf(\"2023-05-21 23:46:52\")");
//		checkLiteralValue(NeutralType.TIMESTAMP, java.util.Date.class, 0,   24, "java.sql.Timestamp.valueOf(\"2024-05-21 00:46:52\")");
//	}

//	@Test
//	public void testSqlDateValues() {
//		
//		java.sql.Date d2 = java.sql.Date.valueOf("2001-06-22");
//		System.out.println(" d2 = " + d2);
//		checkLiteralValue(java.sql.Date.class, 0,    0, "java.sql.Date.valueOf(\"2000-06-22\")");
//		checkLiteralValue(java.sql.Date.class, 0,    1, "java.sql.Date.valueOf(\"2001-06-22\")");
//		checkLiteralValue(java.sql.Date.class, 0,    2, "java.sql.Date.valueOf(\"2002-06-22\")");
//		checkLiteralValue(java.sql.Date.class, 0,  999, "java.sql.Date.valueOf(\"2999-06-22\")");
//		checkLiteralValue(java.sql.Date.class, 0, 1000, "java.sql.Date.valueOf(\"2000-06-22\")");
//
//		java.sql.Time t1 = java.sql.Time.valueOf("00:46:52");
//		System.out.println(" t1 = " + t1);
//		SimpleDateFormat fmt = new SimpleDateFormat("dd MMM yyyy HH:mm:ss"); // 01 janv. 1970 00:46:52
//		System.out.println(" t1 = " + fmt.format(t1));
//
//		java.sql.Time t2 = java.sql.Time.valueOf("00:46:52") ;
//		System.out.println(" t2 = " + t2);
//		checkLiteralValue(java.sql.Time.class, 0,    0, "java.sql.Time.valueOf(\"00:46:52\")");
//		checkLiteralValue(java.sql.Time.class, 0,    1, "java.sql.Time.valueOf(\"01:46:52\")");
//		checkLiteralValue(java.sql.Time.class, 0,    2, "java.sql.Time.valueOf(\"02:46:52\")");
//		checkLiteralValue(java.sql.Time.class, 0,   23, "java.sql.Time.valueOf(\"23:46:52\")");
//		checkLiteralValue(java.sql.Time.class, 0,   24, "java.sql.Time.valueOf(\"00:46:52\")");
//		checkLiteralValue(java.sql.Time.class, 0,   25, "java.sql.Time.valueOf(\"01:46:52\")");
//		checkLiteralValue(java.sql.Time.class, 0,   47, "java.sql.Time.valueOf(\"23:46:52\")");
//		checkLiteralValue(java.sql.Time.class, 0,   48, "java.sql.Time.valueOf(\"00:46:52\")");
//
//		java.sql.Timestamp ts = java.sql.Timestamp.valueOf("2000-05-21 00:46:52");
//		System.out.println(" ts = " + ts);
//		checkLiteralValue(java.sql.Timestamp.class, 0,    0, "java.sql.Timestamp.valueOf(\"2000-05-21 00:46:52\")");
//		checkLiteralValue(java.sql.Timestamp.class, 0,    1, "java.sql.Timestamp.valueOf(\"2001-05-21 01:46:52\")");
//		checkLiteralValue(java.sql.Timestamp.class, 0,    2, "java.sql.Timestamp.valueOf(\"2002-05-21 02:46:52\")");
//		checkLiteralValue(java.sql.Timestamp.class, 0,   23, "java.sql.Timestamp.valueOf(\"2023-05-21 23:46:52\")");
//		checkLiteralValue(java.sql.Timestamp.class, 0,   24, "java.sql.Timestamp.valueOf(\"2024-05-21 00:46:52\")");
//	}


//	private LanguageType getLanguageType(AttributeTypeInfo typeInfo ) {
//		System.out.println( typeInfo + " --> " + typeInfo );
//		return getTypeConverter().getType(typeInfo);
//	}
//	protected LanguageType getLanguageType(String neutralType, int typeInfo ) {
//		AttributeTypeInfo attributeTypeInfo = new AttributeTypeInfoForTest(neutralType, typeInfo);
//		System.out.println("AttributeTypeInfo : " + attributeTypeInfo);
//		return getLanguageType(attributeTypeInfo);
//	}
//
//	private LanguageType buildLanguageObjectType(String neutralType, String javaClassCanonicalName) {
//		String javaClassSimpleName = JavaTypeUtil.shortType(javaClassCanonicalName);
//		return build(neutralType, javaClassSimpleName, javaClassCanonicalName, false, javaClassSimpleName );
//	}
//	private LanguageType buildFakeLanguageTypePrimitive() {
//		return build("neutralType", "xxx", "xxx", true, "Xxx" );
//	}
//	private LanguageType buildFakeLanguageTypeObject() {
//		//return build("neutralType", "xxx", "xxx", false, "Xxx" );
//		return getLanguageType(NeutralType.STRING );
//	}
	
//	@Test
//	public void testLocalDateValues() {
//		LanguageType languageType = buildLanguageObjectType("date", TypeConverterForJava.LOCAL_DATE_CLASS );
//		checkLiteralValue(languageType, 0, 0, "java.time.LocalDate.parse(\"2000-06-22\")" );
//		checkLiteralValue(languageType, 0, 1, "java.time.LocalDate.parse(\"2001-06-22\")" );
//	}
	@Test
	public void testLiteralValuesForDATE() {
		LanguageType lt = getLanguageType(NeutralType.DATE );
		int maxlen = 999; // not used
		assertEquals("java.time.LocalDate.parse(\"2000-06-22\")", getLiteralValuesProvider().generateLiteralValue(lt, maxlen,  0).getCurrentLanguageValue() );
		assertEquals("java.time.LocalDate.parse(\"2001-06-22\")", getLiteralValuesProvider().generateLiteralValue(lt, maxlen,  1).getCurrentLanguageValue() );
		assertEquals("java.time.LocalDate.parse(\"2002-06-22\")", getLiteralValuesProvider().generateLiteralValue(lt, maxlen,  2).getCurrentLanguageValue() );
	}
	
//	@Test
//	public void testLocalTimeValues() {
//		LanguageType languageType = buildLanguageObjectType("time", TypeConverterForJava.LOCAL_TIME_CLASS );
//		checkLiteralValue(languageType, 0, 0, "java.time.LocalTime.parse(\"00:46:52\")" );
//		checkLiteralValue(languageType, 0, 1, "java.time.LocalTime.parse(\"01:46:52\")" );
//	}
	@Test
	public void testLiteralValuesForTIME() {
		LanguageType lt = getLanguageType(NeutralType.TIME );
		int maxlen = 999; // not used
		assertEquals("java.time.LocalTime.parse(\"00:46:52\")", getLiteralValuesProvider().generateLiteralValue(lt, maxlen,  0).getCurrentLanguageValue() );
		assertEquals("java.time.LocalTime.parse(\"01:46:52\")", getLiteralValuesProvider().generateLiteralValue(lt, maxlen,  1).getCurrentLanguageValue() );
	}
	
//	@Test
//	public void testLocalDateTimeValues() {
//		LanguageType languageType = buildLanguageObjectType("timestamp", TypeConverterForJava.LOCAL_DATE_TIME_CLASS );
//		checkLiteralValue(languageType, 0, 0, "java.time.LocalDateTime.parse(\"2000-05-21T00:46:52\")" );
//		checkLiteralValue(languageType, 0, 1, "java.time.LocalDateTime.parse(\"2001-05-21T01:46:52\")" );
//	}
	@Test
	public void testLiteralValuesForTIMESTAMP() {
		LanguageType lt = getLanguageType(NeutralType.TIMESTAMP );
		int maxlen = 999; // not used
		assertEquals("java.time.LocalDateTime.parse(\"2000-05-21T00:46:52\")", getLiteralValuesProvider().generateLiteralValue(lt, maxlen,  0).getCurrentLanguageValue() );
		assertEquals("java.time.LocalDateTime.parse(\"2001-05-21T01:46:52\")", getLiteralValuesProvider().generateLiteralValue(lt, maxlen,  1).getCurrentLanguageValue() );
	}
	
//	@Test
//	public void testInvalidTypes() {
//		checkLiteralValue(java.math.BigInteger.class, 0,    0, "null");
//		checkLiteralValue(java.util.Calendar.class,   0,    0, "null");
//		checkLiteralValue(byte[].class,               0,    0, "null");
//	}
	
}
