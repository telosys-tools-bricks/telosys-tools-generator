package org.telosys.tools.generator.context;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LinkedHashSetTest  {

	@Test
	public void test1() {
		
		Set<String> set = new LinkedHashSet<>();

		set.add("A");
		set.add("B");
		set.add("C");
		set.add("A");
		set.add("A");
		set.add("D");
		
		assertEquals(4, set.size());
		assertTrue(set.contains("A"));
		
		int i = 0 ;
		for(String s: set) {
			i++;
			if ( i == 1 )  assertEquals("A", s);
			if ( i == 3 )  assertEquals("C", s);
			if ( i == 4 )  assertEquals("D", s);
		}
		
	}
}
