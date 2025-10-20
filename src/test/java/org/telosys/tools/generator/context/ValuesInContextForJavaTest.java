package org.telosys.tools.generator.context;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.telosys.tools.dsl.model.DslModelAttribute;
import org.telosys.tools.generic.model.Attribute;
import org.telosys.tools.generic.model.types.NeutralType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ValuesInContextForJavaTest {
	private static final String LOCAL_DATE = "java.time.LocalDate" ;
	private static final String LOCAL_TIME = "java.time.LocalTime"  ;
	
	private void setByte(byte v)   { /* nothing */ } 
	private void setShort(short v) { /* nothing */ } 
	private void setInt(int v)     { /* nothing */ } 
	private void setLong(long v)   { /* nothing */ } 

	private void setByteObj(Byte v)   { /* nothing */ } 
	private void setShortObj(Short v) { /* nothing */ } 
	private void setIntObj(Integer v)     { /* nothing */ } 
	private void setLongObj(Long v)   { /* nothing */ } 
	
	private void println(String msg) {
		System.out.println(msg);
	}

	@Test
	public void testLiteral() {
		setByte((byte)12);
		setShort((short)12);
		setInt(12);
		setLong(12L);
		
		setByteObj((byte)12);
		setShortObj((short)12);
		setIntObj(12);
		setLongObj(12L);
		
		byte  b =  12;
		println("b = " + b);
		Byte  b2 =  34;
		println("b2 = " + b2);
		
		short sh = (short)12;
		println("sh = " + sh);
		
		float  f = 12345.60F ;
		println("f = " + f);
		
		double d = 456.89D ;
		println("d = " + d);

		long   l = 12345L ;
		println("l = " + l);

		
		java.math.BigDecimal bigDecimal = (java.math.BigDecimal.valueOf(12345678.5));
		println("bigDecimal = " + bigDecimal);

		
		java.util.Date utilDate = Calendar.getInstance().getTime() ;
		println("utilDate = " + utilDate);

		java.sql.Date sqlDate = (new java.sql.Date(Calendar.getInstance().getTime().getTime()));
		println("sqlDate = " + sqlDate);

		java.sql.Time sqlTime = (new java.sql.Time(Calendar.getInstance().getTime().getTime()));
		println("sqlTime = " + sqlTime);

		java.sql.Timestamp sqlTimestamp = (new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
		println("sqlTimestamp = " + sqlTimestamp);
	}

	@Test
	public void testCompare() {
		
		byte  b =  12;
		println("b = " + b);
		Byte  b2 =  34;
		Assert.assertTrue( b2 == 34 ) ;
		
		Float  f = 12345.60F ;
		Assert.assertTrue( f == 12345.60F ) ;
		
		Double d = 456.89D ;
		Assert.assertTrue( d == 456.89D ) ;

		Long   l = 12345L ;
		Assert.assertTrue( l == 12345L ) ;

		java.math.BigDecimal bigDecimal = (java.math.BigDecimal.valueOf(12345678.5));
		Assert.assertTrue( bigDecimal.equals( (java.math.BigDecimal.valueOf(12345678.5)) ) ) ;

		Boolean  bool1 = false ;
		Assert.assertTrue( bool1 == false ) ;
		Boolean  bool2 = true ;
		Assert.assertTrue( bool2 == true ) ;
	}
	
	private EnvInContext getEnvInContext() {
		EnvInContext env = new EnvInContext() ;
		try {
			env.setLanguage("Java"); // Just for tests (Java by default)
		} catch (Exception e) {
			throw new RuntimeException("Cannot set language in $env", e);
		}
		return env ;
	}
	
	@Test
	public void testValuesStep1() {
		AttributeInContext id          = buildAttributeInContext("id",          NeutralType.INTEGER  ) ;
		AttributeInContext code        = buildAttributeInContext("code",        NeutralType.INTEGER, true) ; // NOT NULL
		AttributeInContext firstName   = buildAttributeInContext("firstName",   NeutralType.STRING,  10) ;
		AttributeInContext age         = buildAttributeInContext("age",         NeutralType.SHORT    ) ;
		AttributeInContext num1        = buildAttributeInContext("num1",        NeutralType.LONG     ) ;
		AttributeInContext num2        = buildAttributeInContext("num2",        NeutralType.DOUBLE   ) ;
		AttributeInContext dec         = buildAttributeInContext("dec",         NeutralType.DECIMAL  ) ;
		AttributeInContext date1       = buildAttributeInContext("date1",       NeutralType.DATE     ) ;
		AttributeInContext date2       = buildAttributeInContext("date2",       NeutralType.DATE     ) ;
		AttributeInContext flag        = buildAttributeInContext("flag",        NeutralType.BOOLEAN  ) ;
		AttributeInContext flagNotNull = buildAttributeInContext("flagNotNull", NeutralType.BOOLEAN, true) ;
		AttributeInContext time        = buildAttributeInContext("time",        NeutralType.TIME     ) ;
				
		List<AttributeInContext> attributes = new LinkedList<>() ;
		attributes.add( id );
		attributes.add( code );
		attributes.add( firstName );		
		attributes.add( age ) ;
		attributes.add( num1 ) ;
		attributes.add( num2 ) ;
		attributes.add( dec ) ;
		attributes.add( date1 ) ;
		attributes.add( date2 ) ;
		attributes.add( flag ) ;
		attributes.add( flagNotNull ) ;
		attributes.add( time ) ;

		
		EnvInContext    env = getEnvInContext() ;
		ValuesInContext values = new ValuesInContext( attributes, 1, env );
		String listOfValues = values.getAllValues();
		println("List of values :");
		println(listOfValues);
		
		assertEquals(NeutralType.INTEGER, id.getNeutralType() );
		assertFalse( id.isPrimitiveType() ) ;
		assertFalse( id.isNotNull() );
		checkValue(values, "id",        "Integer.valueOf(100)") ;
		checkCompareValue(values, "book", id,   "book.getId().equals(Integer.valueOf(100))");

		assertEquals(NeutralType.INTEGER, code.getNeutralType() );
		assertTrue( code.isNotNull() );
		checkValue(values, "code",        "100") ;
		checkCompareValue(values, "book", code,        "book.getCode() == 100"); // NOT NULL => primitive type comparison

		checkValue(values, "firstName", buildString('A', 10)) ;
		checkCompareValue(values, "book", firstName, "book.getFirstName().equals(" + buildString('A', 10) + ")");
		
		assertEquals(NeutralType.SHORT, age.getNeutralType() );
		assertFalse( age.isNotNull() );
		assertFalse( id.isPrimitiveType() ) ;
		checkValue(values, "age",       "Short.valueOf((short)1)") ;
		checkCompareValue(values, "book", age,  "book.getAge().equals(Short.valueOf((short)1))"); 
		
		assertEquals(NeutralType.BOOLEAN, flag.getNeutralType() );
		assertFalse( flag.isNotNull() );
		assertFalse( id.isPrimitiveType() ) ;
		checkValue(values, "flag",      "Boolean.valueOf(true)") ;
		checkCompareValue(values, "book", flag,        "book.getFlag().equals(Boolean.valueOf(true))"); 
		
		assertEquals(NeutralType.BOOLEAN, flagNotNull.getNeutralType() );
		checkValue(values, "flagNotNull",      "true") ;
		assertTrue( flagNotNull.isNotNull() );
		checkCompareValue(values, "book", flagNotNull,  "book.isFlagNotNull() == true"); 
		
		//---- DATE
		assertEquals(NeutralType.DATE, date1.getNeutralType() );
		assertEquals(LOCAL_DATE, date1.getLanguageType().getFullType() ); // v 3.4.0
		
		checkValue(values, "date1",     "java.time.LocalDate.parse(\"2001-06-22\")" ); // v 3.4.0
		checkCompareValue(values, "book", date1,        
				"book.getDate1().equals(java.time.LocalDate.parse(\"2001-06-22\"))");  // v 3.4.0
		
		assertEquals(NeutralType.DATE, date2.getNeutralType() );
		assertEquals(LOCAL_DATE, date2.getLanguageType().getFullType() ); // v 3.4.0
		checkValue(values, "date2",     "java.time.LocalDate.parse(\"2001-06-22\")") ; // v 3.4.0
		
		//---- TIME
		assertEquals(NeutralType.TIME, time.getNeutralType() );
		assertEquals(LOCAL_TIME, time.getLanguageType().getFullType() ); // v 3.4.0
		checkValue(values, "time",      "java.time.LocalTime.parse(\"01:46:52\")") ; // v 3.4.0
		checkCompareValue(values, "book", time, "book.getTime().equals(java.time.LocalTime.parse(\"01:46:52\"))");  // v 3.4.0
		
		assertEquals(NeutralType.LONG, num1.getNeutralType() );
		checkValue(values, "num1",      "Long.valueOf(1000L)") ;
		checkCompareValue(values, "book", num1,        "book.getNum1().equals(Long.valueOf(1000L))"); 
		
		assertEquals(NeutralType.DOUBLE, num2.getNeutralType() );
		checkValue(values, "num2",      "Double.valueOf(1000.66D)") ;
		checkCompareValue(values, "book", num2,        "book.getNum2().equals(Double.valueOf(1000.66D))"); 
		
		assertEquals(NeutralType.DECIMAL, dec.getNeutralType() );
		checkValue(values, "dec",      "java.math.BigDecimal.valueOf(10000.77)") ;
		checkCompareValue(values, "book", dec,        "book.getDec().equals(java.math.BigDecimal.valueOf(10000.77))"); 
		java.math.BigDecimal foo = java.math.BigDecimal.valueOf(2.3);
		assertTrue ( foo.equals((java.math.BigDecimal.valueOf(2.3))) ) ;
		
		checkValue(values, "inex",      "null") ;

		toJSON(values, attributes);
		toURI(values, attributes);
		println("----------" );

		List<AttributeInContext> keyAttributes = new LinkedList<>() ;
		keyAttributes.add( id );
		keyAttributes.add( code );
		String keyValues = values.getValues(keyAttributes, ", ");
		assertEquals("Integer.valueOf(100), 100", keyValues);
	}

	@Test
	public void testValuesStep2() {
		EnvInContext env = getEnvInContext() ;

		List<AttributeInContext> attributes = new LinkedList<>() ;
		attributes.add( buildAttributeInContext("id",         NeutralType.INTEGER, true ) ); // Not Null
		attributes.add( buildAttributeInContext("firstName",  NeutralType.STRING, 3  ) );
		attributes.add( buildAttributeInContext("age",        NeutralType.SHORT      ) );
		attributes.add( buildAttributeInContext("date1",      NeutralType.DATE       ) );
		attributes.add( buildAttributeInContext("date2",      NeutralType.DATE       ) );
		attributes.add( buildAttributeInContext("datetime",   NeutralType.DATETIME   ) );
		attributes.add( buildAttributeInContext("datetimeTZ", NeutralType.DATETIMETZ ) );
		attributes.add( buildAttributeInContext("time1",      NeutralType.TIME       ) );
		attributes.add( buildAttributeInContext(new DslModelAttribute("time3", NeutralType.TIME )) );
		
		ValuesInContext values = new ValuesInContext(attributes, 2, env );
		
		checkValue(values, "id",         "200") ;
		checkValue(values, "firstName",  buildString('B', 3)) ;
		checkValue(values, "age",        "Short.valueOf((short)2)") ;
		checkValue(values, "datetime",   "java.time.LocalDateTime.parse(\"2002-05-21T02:47:53\")") ;
		checkValue(values, "datetimeTZ", "java.time.OffsetDateTime.parse(\"2002-05-21T02:47:53+02:00\")") ;

		toJSON(values, attributes);
		println("----------" );

		List<AttributeInContext> keyAttributes = new LinkedList<>() ;
		keyAttributes.add( buildAttributeInContext("id", NeutralType.INTEGER, true ) );
		String keyValues = values.getValues(keyAttributes, ", ");
		assertEquals("200", keyValues);
	}
	
	@Test
	public void testValuesStep3() {
		
		EnvInContext env = getEnvInContext() ;

		List<AttributeInContext> attributes = new LinkedList<>() ;
		attributes.add( buildAttributeInContext("id",        NeutralType.SHORT    ) );
		attributes.add( buildAttributeInContext("firstName", NeutralType.STRING, 3) );
		attributes.add( buildAttributeInContext("flag1",     NeutralType.BOOLEAN  ) );
		attributes.add( buildAttributeInContext("flag2",     NeutralType.BOOLEAN  ) );
		attributes.add( buildAttributeInContext("byteVal",   NeutralType.BYTE     ) );
		
		
		ValuesInContext values = new ValuesInContext( attributes, 3, env );
		
		checkValue(values, "id",        "Short.valueOf((short)3)") ;
		checkValue(values, "firstName", buildString('C', 3)) ;
		checkValue(values, "flag1",     "Boolean.valueOf(true)") ;
		checkValue(values, "flag2",     "Boolean.valueOf(true)") ;
		checkValue(values, "byteVal",   "Byte.valueOf((byte)3)") ;

		toJSON(values, attributes);
		toURI(values, attributes);
		println("----------" );
	}
	
	private void toJSON(ValuesInContext values, List<AttributeInContext> attributes) {
		println("---> values.toJSON() : \n" + values.toJSON());
		println("---> values.toFormattedJSON() : \n" + values.toFormattedJSON());
		println("---> values.toJSON(attributes) : \n" + values.toJSON(attributes));
		println("---> values.toFormattedJSON(attributes) : \n" + values.toFormattedJSON(attributes));
	}
	private void toURI(ValuesInContext values, List<AttributeInContext> attributes) {
		println("---> values.toURI() : \n" + values.toURI());
		println("---> values.toURI(attributes) : \n" + values.toURI(attributes));
	}
	
	private void checkValue(ValuesInContext values, String attributeName, String expectedValue) {
		String v = values.getValue(attributeName);
		println(". value for '" + attributeName + "' = '" + v + "' (expected : '" + expectedValue + "')");
		Assert.assertEquals(expectedValue, v);
	}
	
	private void checkCompareValue(ValuesInContext values, String entityVariableName, AttributeInContext attribute, String expectedValue) {
		String v = values.comparisonStatement(entityVariableName, attribute);
		String v2 = v.trim();
		println(". comparison : '" + v + "' (expected : '" + expectedValue + "')");
		Assert.assertEquals(expectedValue.trim(), v2);
	}
	
	//------------------------
	private AttributeInContext buildAttributeInContext(Attribute attribute)  {
		EnvInContext envInContext = getEnvInContext();
		return new AttributeInContext(null, attribute, null, envInContext);
	}
	private AttributeInContext buildAttributeInContext(String attributeName, String neutralType)  {
		EnvInContext envInContext = getEnvInContext();
		Attribute attribute = buildAttribute(attributeName, neutralType);
		return new AttributeInContext(null, attribute, null, envInContext);
	}
	private AttributeInContext buildAttributeInContext(String attributeName, String neutralType, int maxLength)  {
		EnvInContext envInContext = getEnvInContext();
		Attribute attribute = buildAttribute(attributeName, neutralType, maxLength);
		return new AttributeInContext(null, attribute, null, envInContext);
	}
	private AttributeInContext buildAttributeInContext(String attributeName, String neutralType, boolean notNull)  {
		EnvInContext envInContext = getEnvInContext();
		Attribute attribute = buildAttribute(attributeName, neutralType, notNull);
		return new AttributeInContext(null, attribute, null, envInContext);
	}
	//------------------------	
	private Attribute buildAttribute(String attributeName, String neutralType) {
		return buildAttribute( attributeName, neutralType, 0, false);
	}
	private Attribute buildAttribute(String attributeName, String neutralType, int maxLength) {
		return buildAttribute( attributeName, neutralType, maxLength, false);
	}
	private Attribute buildAttribute(String attributeName, String neutralType, boolean notNull) {
		return buildAttribute( attributeName, neutralType, 0, notNull);
	}
	//------------------------
	private Attribute buildAttribute(String attributeName, String neutralType, int maxLength, boolean notNull) {
		DslModelAttribute attribute = new DslModelAttribute(attributeName, neutralType );
		attribute.setMaxLength(maxLength);
		attribute.setNotNull(notNull);
		return attribute ;
	}
	
	private String buildString(char c, int n) {
		StringBuilder sb = new StringBuilder();
		sb.append('"');
		for ( int i = 0 ; i < n ; i++ ) {
			sb.append(c);
		}
		sb.append('"');
		return sb.toString();
	}
}
