package org.telosys.tools.generator.context;

import java.math.BigDecimal;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import junit.env.telosys.tools.generator.fakemodel.FakeAttribute;

public class AttributeInContextTest {
	
	@Test
	public void testSqlColumnName() {
		
		// Test with default $env => default $sql => ANSI-SQL file => snake_case
		AttributeInContext attribute;
		
		attribute = buildAttribute("firstName", "string", "", "", ""); 
		assertEquals("first_name", attribute.getSqlColumnName());

		attribute = buildAttribute("firstName", "string", "MY_FIRST_NAME", "", ""); // keep dbName as is
		assertEquals("MY_FIRST_NAME", attribute.getSqlColumnName());
	}
	
	@Test
	public void testSqlColumnType() {
		
		// Test with default $env => default $sql => ANSI-SQL file 
		AttributeInContext attribute;
		
		attribute = buildAttribute("firstName", "string", "first_name", "", "");  // infer dbType
		assertEquals("VARCHAR", attribute.getSqlColumnType());

		attribute = buildAttribute("firstName", "string", "first_name", "", "40"); // infer dbType
		assertEquals("VARCHAR(40)", attribute.getSqlColumnType());

		attribute = buildAttribute("firstName", "string", "first_name", "varchar", "40");  // keep dbType as is
		assertEquals("varchar", attribute.getSqlColumnType());

		attribute = buildAttribute("firstName", "string", "first_name", "varchar(30)", "40");  // keep dbType as is
		assertEquals("varchar(30)", attribute.getSqlColumnType());
	}
	
	@Test
	public void testSqlColumnConstraints() {
		
		// Test with default $env => default $sql => ANSI-SQL file 
		AttributeInContext attribute;
		
		attribute = buildAttributeWithConstraints("firstName", "string", false, "", "");
		assertEquals("", attribute.getSqlColumnConstraints());

		attribute = buildAttributeWithConstraints("firstName", "string", true, "", "");
		assertEquals("NOT NULL", attribute.getSqlColumnConstraints());

		attribute = buildAttributeWithConstraints("firstName", "string", true, "def1", "");
		assertEquals("NOT NULL DEFAULT 'def1'", attribute.getSqlColumnConstraints());

		attribute = buildAttributeWithConstraints("age", "int", true, "20", ""); // no DB default
		assertEquals("NOT NULL DEFAULT 20", attribute.getSqlColumnConstraints());

		attribute = buildAttributeWithConstraints("age", "int", true, "20", "30"); // DB default is priority 
		assertEquals("NOT NULL DEFAULT 30", attribute.getSqlColumnConstraints());
	}
	
	@Test
	public void testSizeAndDatabaseSize() {
		
		AttributeInContext attribute;
		
		attribute = buildAttribute("firstName", "string", "", "varchar( 20)", "");
		assertEquals("",   attribute.getSize());
		assertEquals("20", attribute.getDatabaseSize()); // (!) Deprecated

		attribute = buildAttribute("firstName", "string", "", "varchar( 20)", "40");
		assertEquals("40", attribute.getSize());
		assertEquals("20", attribute.getDatabaseSize()); // (!) Deprecated

		attribute = buildAttribute("firstName", "string", "", "varchar", "45");
		assertEquals("45", attribute.getSize());
		assertEquals("45", attribute.getDatabaseSize()); 

		attribute = buildAttribute("firstName", "string", "", "varchar", "");
		assertEquals("", attribute.getSize());
		assertEquals("", attribute.getDatabaseSize()); 

		attribute = buildAttribute("firstName", "string", "", "varchar(  )", "");
		assertEquals("", attribute.getSize());
		assertEquals("", attribute.getDatabaseSize()); 

		attribute = buildAttribute("firstName", "string", "", "varchar  )", "");
		assertEquals("", attribute.getSize());
		assertEquals("", attribute.getDatabaseSize()); 

		attribute = buildAttribute("firstName", "string", "", "varchar(((12 )", " ");
		assertEquals("", attribute.getSize());
		assertEquals("12", attribute.getDatabaseSize()); // (!) Deprecated

		attribute = buildAttribute("firstName", "string", "", "varchar(12)(43)", "  ");
		assertEquals("", attribute.getSize());
		assertEquals("12", attribute.getDatabaseSize()); // (!) Deprecated
		
		attribute = buildAttribute("firstName", "string", "", "varchar(12(43)", "");
		assertEquals("", attribute.getSize());
		assertEquals("1243", attribute.getDatabaseSize()); // (!) Deprecated

		attribute = buildAttribute("firstName", "float", "", "numeric( 8,2 )", "");
		assertEquals("", attribute.getSize());
		assertEquals("8,2", attribute.getDatabaseSize()); // (!) Deprecated

		attribute = buildAttribute("firstName", "float", "", "numeric( 8,2 )", "10,4");
		assertEquals("10,4", attribute.getSize());
		assertEquals("8,2", attribute.getDatabaseSize()); // (!) Deprecated
	}
	
	@Test
	public void testSizeAsBigDecimal() {
		
		AttributeInContext attribute;
		
		attribute = buildAttribute("firstName", "string", "", "", null);
		assertEquals("", attribute.getSize());
		assertEquals(BigDecimal.valueOf(0), attribute.getSizeAsDecimal());
		
		attribute = buildAttribute("firstName", "string", "", "", "");
		assertEquals("", attribute.getSize());
		assertEquals(BigDecimal.valueOf(0), attribute.getSizeAsDecimal());
		
		attribute = buildAttribute("firstName", "string", "", "", "  ");
		assertEquals("", attribute.getSize());
		assertEquals(BigDecimal.valueOf(0), attribute.getSizeAsDecimal());
		
		attribute = buildAttribute("firstName", "string", "", "", "12");
		assertEquals("12", attribute.getSize());
		assertEquals(BigDecimal.valueOf(12), attribute.getSizeAsDecimal());
		
		attribute = buildAttribute("price", "double", "", "", "10,2");
		assertEquals("10,2", attribute.getSize());
		assertEquals(BigDecimal.valueOf(10.2), attribute.getSizeAsDecimal());
		
		attribute = buildAttribute("price", "double", "", "", " 10,2 ");
		assertEquals(" 10,2 ", attribute.getSize());
		assertEquals(BigDecimal.valueOf(10.2), attribute.getSizeAsDecimal());
	}
	
	//------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------------
	private AttributeInContext buildAttribute(String attribName, String neutralType,
			String dbName, String dbType, String dbSize) {
		FakeAttribute fakeAttribute = new FakeAttribute(attribName, neutralType, false);
		fakeAttribute.setDatabaseName(dbName);
		fakeAttribute.setDatabaseType(dbType);
		fakeAttribute.setDatabaseSize(dbSize);
		return new AttributeInContext(null, fakeAttribute, null, new EnvInContext()  );
	}
	//------------------------------------------------------------------------------------
	private AttributeInContext buildAttributeWithConstraints(String attribName, String neutralType,
			boolean notNull, String defaultValue, String dbDefaultValue) {
		FakeAttribute fakeAttribute = new FakeAttribute(attribName, neutralType, false);
		fakeAttribute.setNotNull(notNull);
		fakeAttribute.setDefaultValue(defaultValue);
		fakeAttribute.setDatabaseDefaultValue(dbDefaultValue);
		return new AttributeInContext(null, fakeAttribute, null, new EnvInContext()  );
	}
	//------------------------------------------------------------------------------------
	
}
