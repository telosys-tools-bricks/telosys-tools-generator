package org.telosys.tools.generator.context.tools;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LinesBuilderTest {

	@Test
	public void test1WithTabs() {
		LinesBuilder lb = new LinesBuilder(); // TABS by default
		lb.append(1, "aaa");
		lb.append(2, "bbb");
		lb.append(1, "ccc");
		String s = lb.toString();

		assertEquals("\taaa\n\t\tbbb\n\tccc", s);
	}	

	@Test
	public void testWithSpaces() {
		LinesBuilder lb = new LinesBuilder("  "); // 2 spaces 
		lb.append(1, "aaa");
		lb.append(2, "bbb");
		lb.append(1, "ccc");
		String s = lb.toString();

		assertEquals("  aaa\n    bbb\n  ccc", s);
	}	

	@Test
	public void testWithVoidSpaces() {
		LinesBuilder lb = new LinesBuilder(""); // No indentation
		lb.append(1, "aaa");
		lb.append(2, "bbb");
		lb.append(1, "ccc");
		String s = lb.toString();

		assertEquals("aaa\nbbb\nccc", s); 
	}	
}
