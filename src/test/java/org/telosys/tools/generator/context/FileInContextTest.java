package org.telosys.tools.generator.context;

import java.io.File;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

public class FileInContextTest {
	
	private FileInContext getFile(String fileName) {
		File file = new File("src/test/resources/files/" + fileName);
		return new FileInContext(file);
	}
	
	@Test
	public void testFileFooTxt() throws Exception {
		FileInContext file = getFile("foo.txt") ;

		assertTrue(file.exists());
		assertTrue(file.isFile());
		assertFalse(file.isDirectory());
		assertTrue(file.getLength() > 0 );
		
		String content = file.loadContent();
		assertTrue(content.startsWith("aa"));
		
		content = file.loadContent(1);
		assertTrue(content.startsWith("bb"));
		
		List<String> lines = file.loadLines();
		assertEquals(3, lines.size());
		assertEquals("aa", lines.get(0));
		assertEquals("bb", lines.get(1));
		
		lines = file.loadLines(1);
		assertEquals(2, lines.size());
		assertEquals("bb", lines.get(0));
		
	}

	@Test
	public void testFileFooCSV() throws Exception {
		FileInContext file = getFile("foo-csv.txt") ;

		assertTrue(file.exists());
		assertTrue(file.isFile());
		assertFalse(file.isDirectory());
		assertTrue(file.getLength() > 0 );
		assertEquals("foo-csv.txt", file.getName());
		assertTrue(file.getAbsolutePath().endsWith("foo-csv.txt"));
		
		String content = file.loadContent();
		assertTrue(content.startsWith("a1;a2"));
		
		List<String> lines = file.loadLines(1);
		assertTrue(lines.get(0).startsWith("b1;b2"));
		
		List<List<String>> values = file.loadValues("; ");
		assertEquals(3, values.get(0).size());
		assertEquals("a1", values.get(0).get(0));
		assertEquals("a2", values.get(0).get(1));
		assertEquals("a3", values.get(0).get(2));

		assertEquals(4, values.get(1).size());
		assertEquals("b4", values.get(1).get(3));
		
		assertEquals(15, countValues(values));
	}

	@Test
	public void testFileFooCSV2() throws Exception {
		FileInContext file = getFile("foo-csv.txt") ;

		List<List<String>> values = file.loadValues(";", 2);
		assertEquals(3, values.get(0).size());
		assertEquals("c1", values.get(0).get(0));
		assertEquals("", values.get(0).get(1));
		assertEquals("c3", values.get(0).get(2));

		assertEquals(3, values.get(1).size());
		assertEquals("", values.get(1).get(0));
		assertEquals("d3", values.get(1).get(2));
		
		assertEquals(8, countValues(values));
	}
	
	private int countValues(List<List<String>> values) {
		int n = 0;
		for ( List<String> line : values ) {
			for ( String value : line ) {
				n++;
			}
		}
		return n;
	}
	
	@Test(expected = java.lang.Exception.class)
	public void testFileFooCSV_InvalidSeparator() throws Exception {
		FileInContext file = getFile("foo-csv.txt") ;
		file.loadValues("");
	}
	
	@Test(expected = java.lang.Exception.class)
	public void testFileFooCSV_InvalidSeparator2() throws Exception {
		FileInContext file = getFile("foo-csv.txt") ;
		file.loadValues(null);
	}
}
