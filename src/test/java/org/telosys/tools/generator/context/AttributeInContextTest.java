package org.telosys.tools.generator.context;

import java.math.BigDecimal;

import org.junit.Test;
import org.telosys.tools.dsl.model.DslModelAttribute;
import org.telosys.tools.generic.model.types.NeutralType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AttributeInContextTest {
	
	private static final String STRING = "string";
	private static final String BYTE = "byte";
	private static final String SHORT = "short";
	private static final String INT = "int";
	private static final String LONG = "long";
	private static final String FLOAT = "float";
	private static final String DOUBLE = "double";
	private static final String DECIMAL = "decimal";
	
	
	@Test
	public void testIsNumberType() {
		assertFalse( buildAttribute("a", "boolean").isNumberType() );
		assertFalse( buildAttribute("a", "binary").isNumberType() );
		assertFalse( buildAttribute("a", STRING).isNumberType() );
		assertFalse( buildAttribute("a", "date").isNumberType() );
		assertFalse( buildAttribute("a", "time").isNumberType() );
		assertFalse( buildAttribute("a", "timestamp").isNumberType() );
		
		assertTrue( buildAttribute("a", BYTE).isNumberType() );
		assertTrue( buildAttribute("a", SHORT).isNumberType() );
		assertTrue( buildAttribute("a", INT).isNumberType() );
		assertTrue( buildAttribute("a", LONG).isNumberType() );
		assertTrue( buildAttribute("a", FLOAT).isNumberType() );
		assertTrue( buildAttribute("a", DOUBLE).isNumberType() );
		assertTrue( buildAttribute("a", DECIMAL).isNumberType() );
	}

	@Test
	public void testIsTemporalType() {
		assertFalse( buildAttribute("a", NeutralType.BOOLEAN).isTemporalType() );
		assertFalse( buildAttribute("a", NeutralType.STRING).isTemporalType() );

		assertTrue( buildAttribute("a", NeutralType.DATE).isTemporalType() );
		assertTrue( buildAttribute("a", NeutralType.TIME).isTemporalType() );
		assertTrue( buildAttribute("a", NeutralType.DATETIME).isTemporalType() );
		assertTrue( buildAttribute("a", NeutralType.TIMESTAMP).isTemporalType() );
		assertTrue( buildAttribute("a", NeutralType.DATETIMETZ).isTemporalType() );
		assertTrue( buildAttribute("a", NeutralType.TIMETZ).isTemporalType() );

		assertTrue( buildAttribute("a", NeutralType.DATE).isDateType() );
		assertTrue( buildAttribute("a", NeutralType.TIME).isTimeType() );
		assertTrue( buildAttribute("a", NeutralType.DATETIME).isDatetimeType() );
		assertTrue( buildAttribute("a", NeutralType.TIMESTAMP).isTimestampType() );
		assertTrue( buildAttribute("a", NeutralType.DATETIMETZ).isDatetimetzType() );
		assertTrue( buildAttribute("a", NeutralType.TIMETZ).isTimetzType() );
	}
	
	@Test
	public void testIsUuidType() {
		assertFalse( buildAttribute("a", NeutralType.BOOLEAN).isUuidType() );
		assertFalse( buildAttribute("a", NeutralType.STRING).isUuidType() );

		assertTrue( buildAttribute("a", NeutralType.UUID).isUuidType() );
	}
	
	@Test
	public void testSqlColumnName() {
		
		// Test with default $env => default $sql => ANSI-SQL file => snake_case
		AttributeInContext attribute;
		
		attribute = buildAttribute("firstName", STRING, "", "", ""); 
		assertEquals("first_name", attribute.getSqlColumnName());

		attribute = buildAttribute("firstName", STRING, "MY_FIRST_NAME", "", ""); // keep dbName as is
		assertEquals("MY_FIRST_NAME", attribute.getSqlColumnName());
	}
	
	@Test
	public void testSqlColumnType() {
		
		// Test with default $env => default $sql => ANSI-SQL file 
		AttributeInContext attribute;
		
		attribute = buildAttribute("firstName", STRING, "first_name", "", "");  // infer dbType
		assertEquals("VARCHAR", attribute.getSqlColumnType());

		attribute = buildAttribute("firstName", STRING, "first_name", "", "40"); // infer dbType
		assertEquals("VARCHAR(40)", attribute.getSqlColumnType());

		attribute = buildAttribute("firstName", STRING, "first_name", "varchar", "40");  // keep dbType as is
		assertEquals("varchar", attribute.getSqlColumnType());

		attribute = buildAttribute("firstName", STRING, "first_name", "varchar(30)", "40");  // keep dbType as is
		assertEquals("varchar(30)", attribute.getSqlColumnType());
	}
	
	@Test
	public void testSqlColumnConstraints() {
		
		// Test with default $env => default $sql => ANSI-SQL file 
		AttributeInContext attribute;
		
		attribute = buildAttributeWithConstraints("firstName", STRING, false, "", "");
		assertEquals("", attribute.getSqlColumnConstraints());

		attribute = buildAttributeWithConstraints("firstName", STRING, true, "", "");
		assertEquals("NOT NULL", attribute.getSqlColumnConstraints());

		attribute = buildAttributeWithConstraints("firstName", STRING, true, "def1", "");
		assertEquals("NOT NULL DEFAULT 'def1'", attribute.getSqlColumnConstraints());

		attribute = buildAttributeWithConstraints("age", INT, true, "20", ""); // no DB default
		assertEquals("NOT NULL DEFAULT 20", attribute.getSqlColumnConstraints());

		attribute = buildAttributeWithConstraints("age", INT, true, "20", "30"); // DB default is priority 
		assertEquals("NOT NULL DEFAULT 30", attribute.getSqlColumnConstraints());
	}
	
	@Test
	public void testSizeAndDatabaseSize() {
		
		AttributeInContext attribute;
		
		attribute = buildAttribute("firstName", STRING, "", "varchar( 20)", "");
		assertEquals("",   attribute.getSize());
		assertEquals("20", attribute.getDatabaseSize()); // (!) Deprecated

		attribute = buildAttribute("firstName", STRING, "", "varchar( 20)", "40");
		assertEquals("40", attribute.getSize());
		assertEquals("20", attribute.getDatabaseSize()); // (!) Deprecated

		attribute = buildAttribute("firstName", STRING, "", "varchar", "45");
		assertEquals("45", attribute.getSize());
		assertEquals("45", attribute.getDatabaseSize()); 

		attribute = buildAttribute("firstName", STRING, "", "varchar", "");
		assertEquals("", attribute.getSize());
		assertEquals("", attribute.getDatabaseSize()); 

		attribute = buildAttribute("firstName", STRING, "", "varchar(  )", "");
		assertEquals("", attribute.getSize());
		assertEquals("", attribute.getDatabaseSize()); 

		attribute = buildAttribute("firstName", STRING, "", "varchar  )", "");
		assertEquals("", attribute.getSize());
		assertEquals("", attribute.getDatabaseSize()); 

		attribute = buildAttribute("firstName", STRING, "", "varchar(((12 )", " ");
		assertEquals("", attribute.getSize());
		assertEquals("12", attribute.getDatabaseSize()); // (!) Deprecated

		attribute = buildAttribute("firstName", STRING, "", "varchar(12)(43)", "  ");
		assertEquals("", attribute.getSize());
		assertEquals("12", attribute.getDatabaseSize()); // (!) Deprecated
		
		attribute = buildAttribute("firstName", STRING, "", "varchar(12(43)", "");
		assertEquals("", attribute.getSize());
		assertEquals("1243", attribute.getDatabaseSize()); // (!) Deprecated

		attribute = buildAttribute("firstName", FLOAT, "", "numeric( 8,2 )", "");
		assertEquals("", attribute.getSize());
		assertEquals("8,2", attribute.getDatabaseSize()); // (!) Deprecated

		attribute = buildAttribute("firstName", FLOAT, "", "numeric( 8,2 )", "10,4");
		assertEquals("10,4", attribute.getSize());
		assertEquals("8,2", attribute.getDatabaseSize()); // (!) Deprecated
	}
	
	@Test
	public void testSizeAsBigDecimal() {
		
		AttributeInContext attribute;
		
		attribute = buildAttribute("firstName", STRING, "", "", null);
		assertEquals("", attribute.getSize());
		assertEquals(BigDecimal.valueOf(0), attribute.getSizeAsDecimal());
		
		attribute = buildAttribute("firstName", STRING, "", "", "");
		assertEquals("", attribute.getSize());
		assertEquals(BigDecimal.valueOf(0), attribute.getSizeAsDecimal());
		
		attribute = buildAttribute("firstName", STRING, "", "", "  ");
		assertEquals("", attribute.getSize());
		assertEquals(BigDecimal.valueOf(0), attribute.getSizeAsDecimal());
		
		attribute = buildAttribute("firstName", STRING, "", "", "12");
		assertEquals("12", attribute.getSize());
		assertEquals(BigDecimal.valueOf(12), attribute.getSizeAsDecimal());
		
		attribute = buildAttribute("price", DOUBLE, "", "", "10,2");
		assertEquals("10,2", attribute.getSize());
		assertEquals(BigDecimal.valueOf(10.2), attribute.getSizeAsDecimal());
		
		attribute = buildAttribute("price", DOUBLE, "", "", " 10,2 ");
		assertEquals(" 10,2 ", attribute.getSize());
		assertEquals(BigDecimal.valueOf(10.2), attribute.getSizeAsDecimal());
	}
	
	//------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------------
	private AttributeInContext buildAttribute(String attribName, String neutralType) {
		DslModelAttribute fakeAttribute = new DslModelAttribute(attribName, neutralType);
		return new AttributeInContext(null, fakeAttribute, null, new EnvInContext() );
	}
	//------------------------------------------------------------------------------------
	private AttributeInContext buildAttribute(String attribName, String neutralType,
			String dbName, String dbType, String size) {
		DslModelAttribute fakeAttribute = new DslModelAttribute(attribName, neutralType);
		fakeAttribute.setDatabaseName(dbName);
		fakeAttribute.setDatabaseType(dbType);
		fakeAttribute.setSize(size); // since v 3.4.0 'size' is the reference  (instead of 'dbSize' )
		return new AttributeInContext(null, fakeAttribute, null, new EnvInContext()  );
	}
	//------------------------------------------------------------------------------------
	private AttributeInContext buildAttributeWithConstraints(String attribName, String neutralType,
			boolean notNull, String defaultValue, String dbDefaultValue) {
		DslModelAttribute fakeAttribute = new DslModelAttribute(attribName, neutralType);
		fakeAttribute.setNotNull(notNull);
		fakeAttribute.setDefaultValue(defaultValue);
		fakeAttribute.setDatabaseDefaultValue(dbDefaultValue);
		return new AttributeInContext(null, fakeAttribute, null, new EnvInContext()  );
	}
	//------------------------------------------------------------------------------------
	
}
