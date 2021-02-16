package org.telosys.tools.generator.context.tools;

import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class AmbiguousTypesDetectorTest  {

	@Test
	public void test1() {		
		AmbiguousTypesDetector detector = new AmbiguousTypesDetector();
		detector.registerType("java.lang.Double");
		detector.registerType("java.util.Date");
		detector.registerType("java.math.BigDecimal");
		report(detector, 3, 0);
	}
	
	@Test
	public void test2() {		
		AmbiguousTypesDetector detector = new AmbiguousTypesDetector();
		detector.registerType("java.lang.Double");
		detector.registerType("java.util.Date");
		detector.registerType("java.math.BigDecimal");
		detector.registerType("java.sql.Date");
		report(detector, 4, 2);
	}
	
	@Test
	public void test3() {		
		AmbiguousTypesDetector detector = new AmbiguousTypesDetector();
		detector.registerType("java.lang.Double");
		detector.registerType("java.util.Date");
		detector.registerType("java.math.BigDecimal");
		detector.registerType("java.sql.Date");
		detector.registerType("foo.bar.MyClass");
		detector.registerType("foo.bar.foo.MyClass");
		report(detector, 6, 4);
	}
	
	private void report(AmbiguousTypesDetector detector, int countAll, int countAmbiguous) {
		List<String> allTypes = detector.getAllTypes();
		print(allTypes);
		assertTrue(allTypes.size() == countAll ) ;

		List<String> ambiguousTypes = detector.getAmbiguousTypes();
		print(ambiguousTypes);
		assertTrue(ambiguousTypes.size() == countAmbiguous ) ;
	}
	
	private void print(List<String> list) {
		System.out.println("SIZE = " + list.size());
		for ( String s : list ) {
			System.out.println(" . " + s);
		}
		System.out.flush();
	}
}
