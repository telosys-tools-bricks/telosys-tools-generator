package org.telosys.tools.generator.context.tools;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JavaTypeUtilTest {

	@Test
	public void testIsNumberType() {
		assertTrue ( JavaTypeUtil.isNumberType("byte") );
		assertTrue ( JavaTypeUtil.isNumberType("java.lang.Byte") );
		assertTrue ( JavaTypeUtil.isNumberType("short") );
		assertTrue ( JavaTypeUtil.isNumberType("java.lang.Short") );
		assertTrue ( JavaTypeUtil.isNumberType("int") );
		assertTrue ( JavaTypeUtil.isNumberType("java.lang.Integer") );
		assertTrue ( JavaTypeUtil.isNumberType("long") );
		assertTrue ( JavaTypeUtil.isNumberType("java.lang.Long") );

		assertTrue ( JavaTypeUtil.isNumberType("float") );
		assertTrue ( JavaTypeUtil.isNumberType("java.lang.Float") );
		assertTrue ( JavaTypeUtil.isNumberType("double") );
		assertTrue ( JavaTypeUtil.isNumberType("java.lang.Double") );
		
		assertFalse( JavaTypeUtil.isNumberType("") );
		assertFalse( JavaTypeUtil.isNumberType(null) );
		assertFalse( JavaTypeUtil.isNumberType("Byte") );
		assertFalse( JavaTypeUtil.isNumberType("Short") );
		assertFalse( JavaTypeUtil.isNumberType("Integer") );
	}	

	@Test
	public void testIsPrimitiveType() {
		assertTrue( JavaTypeUtil.isPrimitiveType("char"));
		assertTrue( JavaTypeUtil.isPrimitiveType("boolean"));
		assertTrue( JavaTypeUtil.isPrimitiveType("byte"));
		assertTrue( JavaTypeUtil.isPrimitiveType("short"));
		assertTrue( JavaTypeUtil.isPrimitiveType("int"));
		assertTrue( JavaTypeUtil.isPrimitiveType("long"));
		assertTrue( JavaTypeUtil.isPrimitiveType("float"));
		assertTrue( JavaTypeUtil.isPrimitiveType("double"));

		assertFalse( JavaTypeUtil.isPrimitiveType(""));
		assertFalse( JavaTypeUtil.isPrimitiveType(null));
		assertFalse( JavaTypeUtil.isPrimitiveType("Char"));
		assertFalse( JavaTypeUtil.isPrimitiveType("Boolean"));
		assertFalse( JavaTypeUtil.isPrimitiveType("Byte"));
		assertFalse( JavaTypeUtil.isPrimitiveType("Short"));
		assertFalse( JavaTypeUtil.isPrimitiveType("Integer"));
		assertFalse( JavaTypeUtil.isPrimitiveType("Long"));
	}	

	@Test
	public void testIsStringType() {
		assertTrue ( JavaTypeUtil.isStringType("java.lang.String") );

		assertFalse( JavaTypeUtil.isStringType("") );
		assertFalse( JavaTypeUtil.isStringType(null) );
		assertFalse( JavaTypeUtil.isStringType("String") );
	}

	@Test
	public void testIsTemporalType() {
		assertTrue ( JavaTypeUtil.isTemporalType("java.time.LocalDate") );
		assertTrue ( JavaTypeUtil.isTemporalType("java.time.LocalTime") );
		assertTrue ( JavaTypeUtil.isTemporalType("java.time.LocalDateTime") );
		assertTrue ( JavaTypeUtil.isTemporalType("java.time.OffsetDateTime") );
		assertTrue ( JavaTypeUtil.isTemporalType("java.time.OffsetTime") );

		assertTrue ( JavaTypeUtil.isTemporalType("java.time.ZonedDateTime") );
		assertTrue ( JavaTypeUtil.isTemporalType("java.time.Instant") );
		assertTrue ( JavaTypeUtil.isTemporalType("java.util.Date") );

		assertFalse( JavaTypeUtil.isNumberType("") );
		assertFalse( JavaTypeUtil.isNumberType(null) );
		assertFalse( JavaTypeUtil.isNumberType("Byte") );
		assertFalse( JavaTypeUtil.isNumberType("Short") );
		assertFalse( JavaTypeUtil.isNumberType("Integer") );
	}	

	@Test
	public void testNeedsImport() {
		// No import needed for primitive types
		assertFalse ( JavaTypeUtil.needsImport("byte") );
		assertFalse ( JavaTypeUtil.needsImport("int") );
		// No import needed for "java.lang" package
		assertFalse ( JavaTypeUtil.needsImport("java.lang.Byte") );
		assertFalse ( JavaTypeUtil.needsImport("java.lang.Integer") );
		assertFalse ( JavaTypeUtil.needsImport("java.lang.String") );
		// Import needed
		assertTrue ( JavaTypeUtil.needsImport("java.util.UUID") );
		assertTrue ( JavaTypeUtil.needsImport("java.math.BigDecimal") );
		assertTrue ( JavaTypeUtil.needsImport("java.time.LocalDate") );
		assertTrue ( JavaTypeUtil.needsImport("java.time.LocalTime") );
		assertTrue ( JavaTypeUtil.needsImport("java.time.Instant") );
		assertTrue ( JavaTypeUtil.needsImport("java.util.Date") );
		assertTrue ( JavaTypeUtil.needsImport("java.sql.Date") );
		assertTrue ( JavaTypeUtil.needsImport("java.sql.Timestamp") );
	}
	
}
