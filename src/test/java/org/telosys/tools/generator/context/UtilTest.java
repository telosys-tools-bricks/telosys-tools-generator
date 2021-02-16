package org.telosys.tools.generator.context;

import org.junit.Test;

import static org.junit.Assert.*;

public class UtilTest  {

	@Test
	public void testBuildGetter1() {
		String s ;
		
		s = Util.buildGetter("foo" );
		System.out.println("Getter : " + s);
		assertEquals("getFoo", s);

		s = Util.buildGetter("firstName" );
		System.out.println("Getter : " + s);
		assertEquals("getFirstName", s);
	}

	@Test
	public void testBuildGetter2() {
		String s ;
		
		//s = Util.buildGetter("foo", "String");
		s = Util.buildGetter("foo", false);
		assertEquals("getFoo", s);

		//s = Util.buildGetter("foo", "boolean");
		s = Util.buildGetter("foo", true);
		assertEquals("isFoo", s);
	}

	@Test
	public void testBuildSetter() {
		String s ;
		
		s = Util.buildSetter("foo" );
		System.out.println("Setter : " + s);
		assertEquals("setFoo", s);

		s = Util.buildSetter("firstName" );
		System.out.println("Setter : " + s);
		assertEquals("setFirstName", s);
	}

}
