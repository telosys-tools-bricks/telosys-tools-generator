package org.telosys.tools.generator.context;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class BigDecimalTest  {

	@Test
	public void test1() {
		BigDecimal d ;
		
		// any fractional part of this BigDecimal will be discarded
		d = new BigDecimal("123") ;
		assertEquals(BigInteger.valueOf(123), d.toBigInteger());
		assertEquals(123, d.intValue());
		
		d = new BigDecimal("123.456") ;
		assertEquals(BigInteger.valueOf(123), d.toBigInteger());
		assertEquals(123, d.intValue());
		
		//d = new BigDecimal("  123   ") ; // NumberFormatException
		d = new BigDecimal("  123   ".trim()) ;  // OK		
		// d = new BigDecimal(" 123.456  ") ; // NumberFormatException
		assertEquals(123, d.intValue());

		d = new BigDecimal("+123.456") ; // OK
		assertEquals(123, d.intValue());
		assertEquals(6, d.precision());
		assertEquals(3, d.scale());

		d = new BigDecimal("-123.456") ; // OK
		assertEquals(-123, d.intValue());

		d = new BigDecimal("000123.456") ; // OK		
		assertEquals(BigInteger.valueOf(123), d.toBigInteger());
		
		try {
			d = new BigDecimal("1AA23.456") ; // OK
			// We should not execute code below as an exception should have been raised
			fail("An exception should have been raised due to invalid BigDecimal value");
		} catch (NumberFormatException e) {
			// Getting an exception is the expected result
		}
	}
	
	@Test
	public void testInteger() {
		Integer i = Integer.valueOf("1234");
		assertEquals(1234, i.intValue());
		
		// i = Integer.valueOf("123.456"); // Number format exception 
	}		

	@Test
	public void testStringBuilder() {
		
		StringBuilder sb = new StringBuilder() ;
		assertEquals(0, sb.length());
		
		sb.append("abc");
		assertEquals(3, sb.length());
		
		sb.append('d');
		assertEquals(4, sb.length());
		
		sb.append(true); // add "true"
		assertEquals(8, sb.length());
		
		assertEquals(2, sb.indexOf("cd"));
		sb.deleteCharAt(2); // remove char at position 2 : 'c'
		assertEquals(7, sb.length());
	}		
}
