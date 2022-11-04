package org.telosys.tools.generator.context.tools;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

public class ListBuilderTest {

	@Test
	public void test1() {
		ListBuilder lb = new ListBuilder();
		assertTrue(lb.isEmpty());
		lb.append("aa");
		assertFalse(lb.isEmpty());
		assertEquals("aa", lb.toString());
		lb.append("bb");
		assertEquals("aa, bb", lb.toString());
	}	

	@Test
	public void test2() {
		ListBuilder lb = new ListBuilder(":");
		assertTrue(lb.isEmpty());
		lb.append("aa");
		assertFalse(lb.isEmpty());
		assertEquals("aa", lb.toString());
		lb.append("bb");
		assertEquals("aa:bb", lb.toString());
	}	

	@Test
	public void test3() {
		ListBuilder lb = new ListBuilder(":");
		assertTrue(lb.isEmpty());
		lb.append(null);
		assertTrue(lb.isEmpty());
		lb.append("");
		assertTrue(lb.isEmpty());
		lb.append(" ");
		assertTrue(lb.isEmpty());
		lb.append("  ");
		assertTrue(lb.isEmpty());
		lb.append("aa");		
		assertFalse(lb.isEmpty());
		assertEquals("aa", lb.toString());
		lb.append("  ");		
		assertEquals("aa", lb.toString());
		lb.append("bb");
		assertEquals("aa:bb", lb.toString());
	}	
}
