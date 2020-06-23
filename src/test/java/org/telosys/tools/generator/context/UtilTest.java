package org.telosys.tools.generator.context;

import junit.framework.TestCase;

import org.telosys.tools.generator.context.Util;


public class UtilTest extends TestCase {

	public void testBuildGetter1() {
		String s ;
		
		s = Util.buildGetter("foo" );
		System.out.println("Getter : " + s);
		assertEquals("getFoo", s);

		s = Util.buildGetter("firstName" );
		System.out.println("Getter : " + s);
		assertEquals("getFirstName", s);
	}

	public void testBuildGetter2() {
		String s ;
		
		//s = Util.buildGetter("foo", "String");
		s = Util.buildGetter("foo", false);
		assertEquals("getFoo", s);

		//s = Util.buildGetter("foo", "boolean");
		s = Util.buildGetter("foo", true);
		assertEquals("isFoo", s);
	}

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
