package org.telosys.tools.test.velocity.context;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.telosys.tools.generator.context.AttributeInContext;
import org.telosys.tools.generator.context.ValuesInContext;
import org.telosys.tools.repository.model.Column;

public class ValuesInContextTest {
	
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

	@Test
	public void testValuesStep1() {
		
		List<AttributeInContext> attributes = new LinkedList<AttributeInContext>() ;
		
		attributes.add( new AttributeInContext(null, buildColumn("id",        "int")) ) ;
		attributes.add( new AttributeInContext(null, buildColumn("firstName", "java.lang.String", 10)) ) ;
		attributes.add( new AttributeInContext(null, buildColumn("age",       "java.lang.Short")) ) ;
		attributes.add( new AttributeInContext(null, buildColumn("date1",     "java.util.Date")) ) ;
		attributes.add( new AttributeInContext(null, buildColumn("date2",     "java.sql.Date")) ) ;
		
		ValuesInContext values = new ValuesInContext( attributes, 1 );
		
		checkValue(values, "id",        "100") ;
		checkValue(values, "firstName", buildString('A', 10)) ;
		checkValue(values, "age",       "(short)10") ;
//		checkValue(values, "date1",     "Calendar.getInstance().getTime()") ;
//		checkValue(values, "date2",     "(new java.sql.Date(Calendar.getInstance().getTime().getTime()))") ;
		checkValue(values, "date1",     "null") ;
		checkValue(values, "date2",     "null") ;
		
		checkValue(values, "inex",      "null") ;
		
		String listOfValues = values.getJavaValues();
		System.out.println("list of values :");
		System.out.println(listOfValues);
	}

	@Test
	public void testValuesStep2() {
		
		List<AttributeInContext> attributes = new LinkedList<AttributeInContext>() ;
		
		attributes.add( new AttributeInContext(null, buildColumn("id",        "int")) ) ;
		attributes.add( new AttributeInContext(null, buildColumn("firstName", "java.lang.String", 3)) ) ;
		attributes.add( new AttributeInContext(null, buildColumn("age",       "java.lang.Short")) ) ;
		attributes.add( new AttributeInContext(null, buildColumn("date1",     "java.util.Date")) ) ;
		attributes.add( new AttributeInContext(null, buildColumn("date2",     "java.sql.Date")) ) ;
		
		ValuesInContext values = new ValuesInContext( attributes, 2 );
		
		checkValue(values, "id",        "200") ;
		checkValue(values, "firstName", buildString('B', 3)) ;
		checkValue(values, "age",       "(short)20") ;
//		checkValue(values, "date1",     "Calendar.getInstance().getTime()") ;
//		checkValue(values, "date2",     "(new java.sql.Date(Calendar.getInstance().getTime().getTime()))") ;
	}
	
	@Test
	public void testValuesStep3() {
		
		List<AttributeInContext> attributes = new LinkedList<AttributeInContext>() ;
		
		attributes.add( new AttributeInContext(null, buildColumn("id",        "short")) ) ;
		attributes.add( new AttributeInContext(null, buildColumn("firstName", "java.lang.String", 3)) ) ;
		attributes.add( new AttributeInContext(null, buildColumn("flag1",     "java.lang.Boolean")) ) ;
		attributes.add( new AttributeInContext(null, buildColumn("flag2",     "boolean")) ) ;
		attributes.add( new AttributeInContext(null, buildColumn("byteVal",   "byte")) ) ;
		
		ValuesInContext values = new ValuesInContext( attributes, 3 );
		
		checkValue(values, "id",        "(short)30") ;
		checkValue(values, "firstName", buildString('C', 3)) ;
		checkValue(values, "flag1",     "true") ;
		checkValue(values, "flag2",     "true") ;
		checkValue(values, "byteVal",   "(byte)3") ;
	}
	
	private void checkValue(ValuesInContext values, String attributeName, String expectedValue) {
		String v = values.javaValue(attributeName);
		System.out.println(". value for '" + attributeName + "' = " + v + " (expected : '" + expectedValue + "')");
		Assert.assertTrue(v.equals(expectedValue));
	}
	
	private Column buildColumn(String attributeName, String javaType) {
		return buildColumn( attributeName, javaType, 0);
	}
	private Column buildColumn(String attributeName, String javaType, int maxLength) {
		Column column = new Column();
		column.setJavaName(attributeName);
		column.setJavaType(javaType);
		column.setMaxLength(""+maxLength);
		return column ;
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
