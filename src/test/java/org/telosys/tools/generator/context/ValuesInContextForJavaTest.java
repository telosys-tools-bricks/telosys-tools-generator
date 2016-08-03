package org.telosys.tools.generator.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import junit.env.telosys.tools.generator.fakemodel.AttributeInFakeModel;

import org.junit.Assert;
import org.junit.Test;
import org.telosys.tools.generator.context.AttributeInContext;
import org.telosys.tools.generator.context.ValuesInContext;
import org.telosys.tools.generic.model.Attribute;

public class ValuesInContextForJavaTest {
	
	private void setByte(byte v)   { /* nothing */ } 
	private void setShort(short v) { /* nothing */ } 
	private void setInt(int v)     { /* nothing */ } 
	private void setLong(long v)   { /* nothing */ } 

	private void setByteObj(Byte v)   { /* nothing */ } 
	private void setShortObj(Short v) { /* nothing */ } 
	private void setIntObj(Integer v)     { /* nothing */ } 
	private void setLongObj(Long v)   { /* nothing */ } 

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
		System.out.println("b = " + b);
		Byte  b2 =  34;
		System.out.println("b2 = " + b2);
		
		short sh = (short)12;
		System.out.println("sh = " + sh);
		
		float  f = 12345.60F ;
		System.out.println("f = " + f);
		
		double d = 456.89D ;
		System.out.println("d = " + d);

		long   l = 12345L ;
		System.out.println("l = " + l);

		
		BigDecimal bigDecimal = (new BigDecimal(12345678.5));
		System.out.println("bigDecimal = " + bigDecimal);

		
		java.util.Date utilDate = Calendar.getInstance().getTime() ;
		System.out.println("utilDate = " + utilDate);
		//(new SimpleDateFormat("yyyy-MM-dd")).parse("1901-01-01");

		java.sql.Date sqlDate = (new java.sql.Date(Calendar.getInstance().getTime().getTime()));
		System.out.println("sqlDate = " + sqlDate);

		java.sql.Time sqlTime = (new java.sql.Time(Calendar.getInstance().getTime().getTime()));
		System.out.println("sqlTime = " + sqlTime);

		java.sql.Timestamp sqlTimestamp = (new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
		System.out.println("sqlTimestamp = " + sqlTimestamp);
	}

	@Test
	public void testCompare() {
		
		byte  b =  12;
		System.out.println("b = " + b);
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
		//System.out.println("bigDecimal = " + bigDecimal);

		Boolean  bool1 = false ;
		Assert.assertTrue( bool1 == false ) ;
		Boolean  bool2 = true ;
		Assert.assertTrue( bool2 == true ) ;
//		
//		java.util.Date utilDate = Calendar.getInstance().getTime() ;
//		System.out.println("utilDate = " + utilDate);
//
//		java.sql.Date sqlDate = (new java.sql.Date(Calendar.getInstance().getTime().getTime()));
//		System.out.println("sqlDate = " + sqlDate);
//
//		java.sql.Time sqlTime = (new java.sql.Time(Calendar.getInstance().getTime().getTime()));
//		System.out.println("sqlTime = " + sqlTime);
//
//		java.sql.Timestamp sqlTimestamp = (new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
//		System.out.println("sqlTimestamp = " + sqlTimestamp);
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
		EnvInContext env = getEnvInContext() ;
		
		
		
//		AttributeInContext attribFirstName =  new AttributeInContext(null, buildAttribute("firstName", "java.lang.String", 10)) ;
//		attributes.add(attribFirstName);		
//		attributes.add( new AttributeInContext(null, buildAttribute("age",       "java.lang.Short")) ) ;
//		attributes.add( new AttributeInContext(null, buildAttribute("num1",      "long")) ) ;
//		attributes.add( new AttributeInContext(null, buildAttribute("num2",      "java.lang.Double")) ) ;
//		attributes.add( new AttributeInContext(null, buildAttribute("date1",     "java.util.Date")) ) ;
//		attributes.add( new AttributeInContext(null, buildAttribute("date2",     "java.sql.Date")) ) ;
//		attributes.add( new AttributeInContext(null, buildAttribute("flag",      "boolean")) ) ;
//		attributes.add( new AttributeInContext(null, buildAttribute("flag2",     "boolean")) ) ;
//		attributes.add( new AttributeInContext(null, buildAttribute("time",     "java.sql.Time")) ) ;

		// with neutral types
		AttributeInContext id        = new AttributeInContext(null, buildAttribute("id",        "int"), env) ;
		AttributeInContext code      = new AttributeInContext(null, buildAttribute("code",      "int", true), env) ; // NOT NULL
		AttributeInContext firstName = new AttributeInContext(null, buildAttribute("firstName", "string", 10), env) ;
		AttributeInContext age       = new AttributeInContext(null, buildAttribute("age",       "short"), env) ;
		AttributeInContext num1      = new AttributeInContext(null, buildAttribute("num1",      "long"), env)  ;
		AttributeInContext num2      = new AttributeInContext(null, buildAttribute("num2",      "double"), env)  ;
		AttributeInContext dec       = new AttributeInContext(null, buildAttribute("dec",       "decimal"), env)  ;
		AttributeInContext date1     = new AttributeInContext(null, buildAttribute("date1",     "date"), env)  ;
		AttributeInContext date2     = new AttributeInContext(null, buildAttribute("date2",     "date"), env) ;
		AttributeInContext flag      = new AttributeInContext(null, buildAttribute("flag",      "boolean"), env) ;
		AttributeInContext flag2     = new AttributeInContext(null, buildAttribute("flag2",     "boolean", true), env) ;
		AttributeInContext time      = new AttributeInContext(null, buildAttribute("time",      "time"), env) ;
				
		List<AttributeInContext> attributes = new LinkedList<AttributeInContext>() ;
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
		attributes.add( flag2 ) ;
		attributes.add( time ) ;
		
		ValuesInContext values = new ValuesInContext( attributes, 1, env );
		String listOfValues = values.getAllValues();
		System.out.println("List of values :");
		System.out.println(listOfValues);
		
		assertEquals("int", id.getNeutralType() );
		assertFalse( id.isNotNull() );
		checkValue(values, "id",        "100") ;
		checkCompareValue(values, "book", id,        "book.getId().equals(100)");

		assertEquals("int", code.getNeutralType() );
		assertTrue( code.isNotNull() );
		checkValue(values, "code",        "100") ;
		checkCompareValue(values, "book", code,        "book.getCode() == 100"); // NOT NULL => primitive type comparison

		checkValue(values, "firstName", buildString('A', 10)) ;
		checkCompareValue(values, "book", firstName, "book.getFirstName().equals(" + buildString('A', 10) + ")");
		
		assertEquals("short", age.getNeutralType() );
		checkValue(values, "age",       "(short)1") ;
		assertFalse( age.isNotNull() );
		checkCompareValue(values, "book", age,        "book.getAge().equals((short)1)"); 
		
		assertEquals("boolean", flag.getNeutralType() );
		checkValue(values, "flag",      "true") ;
		assertFalse( flag.isNotNull() );
		checkCompareValue(values, "book", flag,        "book.getFlag().equals(true)"); 
		
		assertEquals("boolean", flag2.getNeutralType() );
		checkValue(values, "flag2",      "true") ;
		assertTrue( flag2.isNotNull() );
		checkCompareValue(values, "book", flag2,        "book.isFlag2() == true"); 
		
		assertEquals("date", date1.getNeutralType() );
		assertEquals("java.util.Date", date1.getLanguageType().getFullType() );
		checkValue(values, "date1",     "java.sql.Date.valueOf(\"2001-06-22\")") ;
		checkCompareValue(values, "book", date1,        "book.getDate1().equals(java.sql.Date.valueOf(\"2001-06-22\"))"); 
		
		assertEquals("date", date2.getNeutralType() );
		assertEquals("java.util.Date", date2.getLanguageType().getFullType() );
		checkValue(values, "date2",     "java.sql.Date.valueOf(\"2001-06-22\")") ;
		
		assertEquals("time", time.getNeutralType() );
		assertEquals("java.util.Date", time.getLanguageType().getFullType() );
		checkValue(values, "time",      "java.sql.Time.valueOf(\"01:46:52\")") ;
		checkCompareValue(values, "book", time,        "book.getTime().equals(java.sql.Time.valueOf(\"01:46:52\"))"); 
		
		assertEquals("long", num1.getNeutralType() );
		checkValue(values, "num1",      "1000L") ;
		checkCompareValue(values, "book", num1,        "book.getNum1().equals(1000L)"); 
		
		assertEquals("double", num2.getNeutralType() );
		checkValue(values, "num2",      "1000.66D") ;
		checkCompareValue(values, "book", num2,        "book.getNum2().equals(1000.66D)"); 
		
		assertEquals("decimal", dec.getNeutralType() );
		checkValue(values, "dec",      "(new BigDecimal(10000))") ;
		checkCompareValue(values, "book", dec,        "book.getDec().equals((new BigDecimal(10000)))"); 
		BigDecimal foo = new BigDecimal(2.3);
		assertTrue ( foo.equals((new BigDecimal(2.3))) ) ;
		
		checkValue(values, "inex",      "null") ;
	}

	@Test
	public void testValuesStep2() {
		EnvInContext env = getEnvInContext() ;

		List<AttributeInContext> attributes = new LinkedList<AttributeInContext>() ;
		
//		attributes.add( new AttributeInContext(null, buildAttribute("id",        "int")) ) ;
//		attributes.add( new AttributeInContext(null, buildAttribute("firstName", "java.lang.String", 3)) ) ;
//		attributes.add( new AttributeInContext(null, buildAttribute("age",       "java.lang.Short")) ) ;
//		attributes.add( new AttributeInContext(null, buildAttribute("date1",     "java.util.Date")) ) ;
//		attributes.add( new AttributeInContext(null, buildAttribute("date2",     "java.sql.Date")) ) ;

		// with neutral types
		attributes.add( new AttributeInContext(null, buildAttribute("id",        "int"), env) ) ;
		attributes.add( new AttributeInContext(null, buildAttribute("firstName", "string", 3), env) ) ;
		attributes.add( new AttributeInContext(null, buildAttribute("age",       "short"), env) ) ;
		attributes.add( new AttributeInContext(null, buildAttribute("date1",     "date"), env) ) ;
		attributes.add( new AttributeInContext(null, buildAttribute("date2",     "date"), env ) ) ;
		
		ValuesInContext values = new ValuesInContext( attributes, 2, env );
		
		checkValue(values, "id",        "200") ;
		checkValue(values, "firstName", buildString('B', 3)) ;
		checkValue(values, "age",       "(short)2") ;
	}
	
	@Test
	public void testValuesStep3() {
		
		EnvInContext env = getEnvInContext() ;

		List<AttributeInContext> attributes = new LinkedList<AttributeInContext>() ;
		
//		attributes.add( new AttributeInContext(null, buildAttribute("id",        "short")) ) ;
//		attributes.add( new AttributeInContext(null, buildAttribute("firstName", "java.lang.String", 3)) ) ;
//		attributes.add( new AttributeInContext(null, buildAttribute("flag1",     "java.lang.Boolean")) ) ;
//		attributes.add( new AttributeInContext(null, buildAttribute("flag2",     "boolean")) ) ;
//		attributes.add( new AttributeInContext(null, buildAttribute("byteVal",   "byte")) ) ;

		// with neutral types
		attributes.add( new AttributeInContext(null, buildAttribute("id",        "short"), env) ) ;
		attributes.add( new AttributeInContext(null, buildAttribute("firstName", "string", 3), env) ) ;
		attributes.add( new AttributeInContext(null, buildAttribute("flag1",     "boolean"), env) ) ;
		attributes.add( new AttributeInContext(null, buildAttribute("flag2",     "boolean"), env) ) ;
		attributes.add( new AttributeInContext(null, buildAttribute("byteVal",   "byte"), env) ) ;
		
		ValuesInContext values = new ValuesInContext( attributes, 3, env );
		
		checkValue(values, "id",        "(short)3") ;
		checkValue(values, "firstName", buildString('C', 3)) ;
		checkValue(values, "flag1",     "true") ;
		checkValue(values, "flag2",     "true") ;
		checkValue(values, "byteVal",   "(byte)3") ;
	}
	
	private void checkValue(ValuesInContext values, String attributeName, String expectedValue) {
		String v = values.getValue(attributeName);
		System.out.println(". value for '" + attributeName + "' = '" + v + "' (expected : '" + expectedValue + "')");
		//Assert.assertTrue(v.equals(expectedValue));
		Assert.assertEquals(expectedValue, v);
	}
	
	private void checkCompareValue(ValuesInContext values, String entityVariableName, AttributeInContext attribute, String expectedValue) {
		String v = values.comparisonStatement(entityVariableName, attribute);
		String v2 = v.trim();
		System.out.println(". comparison : '" + v + "' (expected : '" + expectedValue + "')");
		//Assert.assertTrue(v2.equals(expectedValue.trim()));
		Assert.assertEquals(expectedValue.trim(), v2);
	}
	
	private Attribute buildAttribute(String attributeName, String neutralType) {
		return buildAttribute( attributeName, neutralType, 0, false);
	}
	private Attribute buildAttribute(String attributeName, String neutralType, int maxLength) {
		return buildAttribute( attributeName, neutralType, maxLength, false);
	}
	private Attribute buildAttribute(String attributeName, String neutralType, boolean notNull) {
		return buildAttribute( attributeName, neutralType, 0, notNull);
	}
	
	private Attribute buildAttribute(String attributeName, String neutralType, int maxLength, boolean notNull) {
		AttributeInFakeModel attribute = new AttributeInFakeModel(attributeName, neutralType );
//		attribute.setName(attributeName);
//		attribute.setFullType(javaType);
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
//	private Column getColumnForIntegerType(String attributeName) {
//		Column column = new Column();
//		column.setJavaName(attributeName);
//		column.setJavaType("java.lang.Integer");
//		return column ;
//	}
}
