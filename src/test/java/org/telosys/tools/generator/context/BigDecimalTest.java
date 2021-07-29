package org.telosys.tools.generator.context;

import java.math.BigDecimal;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BigDecimalTest  {

	@Test
	public void test1() {
		BigDecimal d ;
		
		// any fractional part of this BigDecimal will be discarded
		d = new BigDecimal("123") ;
		System.out.println(d + " toBigInteger : " + d.toBigInteger() );
		System.out.println(d + " intValue     : " + d.intValue() );
		
		d = new BigDecimal("123.456") ;
		System.out.println(d + " toBigInteger : " + d.toBigInteger() );
		System.out.println(d + " intValue     : " + d.intValue() );
		
		//d = new BigDecimal("  123   ") ; // NumberFormatException
		d = new BigDecimal("  123   ".trim()) ;  // OK
		
		// d = new BigDecimal(" 123.456  ") ; // NumberFormatException

		d = new BigDecimal("+123.456") ; // OK
		d = new BigDecimal("-123.456") ; // OK
		d = new BigDecimal("000123.456") ; // OK
		
		System.out.println(d + " toBigInteger : " + d.toBigInteger() );
		
		try {
			d = new BigDecimal("1AA23.456") ; // OK
		} catch (NumberFormatException e) {
			System.out.println("ERROR : " + e.getMessage()); // no message
		}
		
		
		assertEquals("a", "a");
	}
	
	@Test
	public void testInteger() {
		Integer i ;
		
		i = Integer.valueOf("1234");
		// i = Integer.valueOf("123.456"); // Number format exception 
	}		

	@Test
	public void testStringBuilder() {
		
		StringBuilder sb = new StringBuilder() ;
		assertEquals(0, sb.length());
		
		sb.append("abc");
		System.out.println(sb);
		assertEquals(3, sb.length());
		
		sb.append('d');
		System.out.println(sb);
		assertEquals(4, sb.length());
		
		sb.append(true);
		System.out.println(sb);
		assertEquals(8, sb.length());
		
		System.out.println(sb.indexOf("cd"));
		sb.deleteCharAt(2);
		System.out.println(sb);
		assertEquals(7, sb.length());
		
		
	}		

}
