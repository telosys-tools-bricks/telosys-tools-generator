package org.telosys.tools.generator.context;

import java.io.File;
import java.math.BigDecimal;

import org.junit.Test;
import org.telosys.tools.dsl.model.DslModelAttribute;
import org.telosys.tools.generator.context.exceptions.GeneratorSqlException;
import org.telosys.tools.generic.model.types.NeutralType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import junit.env.telosys.tools.generator.context.Builder;

public class SqlInContextTest {
	
	private static final String SNAKE_CASE    = "snake_case" ;
	private static final String ANACONDA_CASE = "ANACONDA_CASE";
	
	private SqlInContext getSql() {
		return new SqlInContext("PostgreSQL") ;
	}
	
	@Test
	public void testBigDecimalConversion() {
		BigDecimal size ;
		size = new BigDecimal("10.2") ;
		assertEquals( 10, size.toBigInteger().longValue() );
		size = new BigDecimal("8.9") ;
		assertEquals( 8, size.toBigInteger().intValue() );
		size = new BigDecimal("20") ;
		assertEquals( 20, size.toBigInteger().intValue() );
		size = new BigDecimal("0") ;
		assertEquals( 0, size.toBigInteger().intValue() );
		
		size = new BigDecimal("10.2") ;
		assertEquals(  10,  size.toBigInteger().longValue() );
		assertEquals( "10", size.toBigInteger().toString() );
		
		size = new BigDecimal("8.9") ;
		assertEquals(  8,  size.toBigInteger().longValue() );
		assertEquals( "8", size.toBigInteger().toString() );
				
	}
	
	@Test
	public void testConvertName() {
		SqlInContext sql = new SqlInContext("ansisql") ;
		// Name conversion with specific style name
		assertEquals("city_code",    sql.convertName("cityCode", SNAKE_CASE) ) ;
		assertEquals("employee_job", sql.convertName("EmployeeJob", SNAKE_CASE) ) ;
		
		assertEquals("CITY_CODE",    sql.convertName("cityCode", ANACONDA_CASE) ) ;
		assertEquals("EMPLOYEE_JOB", sql.convertName("EmployeeJob", ANACONDA_CASE) ) ;
	}
	
	@Test
	public void testPostgreSQL() {
		AttributeInContext attribute ;
		
		SqlInContext sql = new SqlInContext("PostgreSQL") ;
		
		assertEquals("PostgreSQL", sql.getDatabaseName());
		assertEquals("target-db/postgresql.properties", sql.getDatabaseConfigFile());
		
		// Low level methods (without attribute)
		
		// Name conversion as defined for PostgreSQL
		assertEquals("city_code",    sql.convertToColumnName("cityCode") ) ;
		assertEquals("employee_job", sql.convertToColumnName("EmployeeJob") ) ;
		
		// Without explicit db name (attribute name as default )
		attribute = buildAttribute("firstName", NeutralType.STRING, null); 
		assertEquals("", attribute.getDatabaseName() ); 
		assertEquals("first_name", sql.columnName(attribute) ) ; // converted from attribute name
		// With explicit db name
		attribute = buildAttribute("firstName", NeutralType.STRING, "MY_FIRST_NAME"); 
		assertEquals("MY_FIRST_NAME", attribute.getDatabaseName() ); 
		assertEquals("MY_FIRST_NAME", sql.columnName(attribute) ) ; // not converted if explicit db name


		// Type conversion 
		assertEquals("integer", sql.getConfigType("int", false) );
		assertEquals("serial",  sql.getConfigType("int", true) );
		assertEquals("varchar(%s)", sql.getConfigType("string", false) );
		assertEquals("varchar(%s)", sql.getConfigType("string", true) ); // OK : auto-incr ignored
		// New types in ver 4.3.0
		assertEquals("timestamp",  sql.getConfigType("datetime", true) );
		assertEquals("timestamp",  sql.getConfigType("datetime", false) );
		assertEquals("timestamp with time zone",  sql.getConfigType("datetimetz", true) );
		assertEquals("timestamp with time zone",  sql.getConfigType("datetimetz", false) );
		assertEquals("time with time zone",  sql.getConfigType("timetz", true) );
		assertEquals("time with time zone",  sql.getConfigType("timetz", false) );
		assertEquals("uuid",  sql.getConfigType("uuid", true) );
		assertEquals("uuid",  sql.getConfigType("uuid", false) );
		
		// Var replacement  
		assertEquals("varchar(26)", sql.replaceVar("varchar(%s)", new BigDecimal("26")) );
		assertEquals("varchar(20)", sql.replaceVar("varchar(%s)", new BigDecimal("20.98")) );
		assertEquals("varchar",     sql.replaceVar("varchar(%s)", null) );

		assertEquals("NUMBER(2)",    sql.replaceVar("NUMBER(%p)", new BigDecimal("2")) );
		assertEquals("NUMBER(10,2)", sql.replaceVar("NUMBER(%p)", new BigDecimal("10.2")) );
		assertEquals("NUMBER",       sql.replaceVar("NUMBER(%p)", null ) );
		
		//--- Primary Key :
		assertEquals("pk_foo_bar", sql.convertToPkName("pkFooBar") );
		assertEquals("pk_foo_bar", sql.convertToPkName("PkFooBar") );
		assertEquals("pk_foo_bar", sql.convertToPkName("Pk_Foo_Bar") );
		assertEquals("pk_foo_bar", sql.convertToPkName("PK_foo_BAR") );
		assertEquals("pkfoobar", sql.convertToPkName("PKFOOBAR") );
		
		//--- Foreign Key :
		assertEquals("fk_foo_bar", sql.convertToFkName("fkFooBar") );
		assertEquals("fk_foo_bar", sql.convertToFkName("FkFooBar") );

		//--- Table name with explicite table name
		EntityInContext carEntity = Builder.buildEntityInContext("Car", "T_CAR");
		assertEquals("T_CAR", carEntity.getDatabaseTable());
		assertEquals("T_CAR", sql.tableName(carEntity));

		//--- Table name without explicite table name
		carEntity = Builder.buildEntityInContext("Car");
		assertEquals("", carEntity.getDatabaseTable());
		assertEquals("car", sql.tableName(carEntity));
	}

	//---- (%s) and (%S)
	@Test 
	public void testPostgreSQLVarSizeMandatoryIsNull() {
		assertEquals("varchar(255)", getSql().replaceVar("varchar(%S)", null) ); // Mandatory
	}
	@Test 
	public void testPostgreSQLVarSizeMandatoryIsZero() {
		assertEquals("varchar(255)", getSql().replaceVar("varchar(%S)", new BigDecimal("0")) ); // Mandatory
	}
	@Test(expected = GeneratorSqlException.class)
	public void testPostgreSQLVarSizeValueError() {
		getSql().replaceVar("varchar(%s)", new BigDecimal("-2")); // Invalid value
	}
	@Test
	public void testPostgreSQLVarSizeOptional() {
		assertEquals("varchar", getSql().replaceVar("varchar(%s)", null) );
		assertEquals("varchar", getSql().replaceVar("varchar(%s)", new BigDecimal("0")) );
	}
	
	//---- (%P) and (%p)
	@Test  
	public void testPostgreSQLVarPrecisionMandatory() {
		// Mandatory, no size => default value
		assertEquals("numeric(16,4)", getSql().replaceVar("numeric(%P)", new BigDecimal("16.4")) );
	}
	@Test  
	public void testPostgreSQLVarPrecisionMandatoryIsNull() {
		// Mandatory, no size => default value
		assertEquals("numeric(10,2)", getSql().replaceVar("numeric(%P)", null) );
	}
	@Test 
	public void testPostgreSQLVarPrecisionMandatoryIsZero() {
		// Mandatory, precision = 0 => default value
		assertEquals("numeric(10,2)", getSql().replaceVar("numeric(%P)", new BigDecimal("0")) ); // Mandatory
	}
	@Test 
	public void testPostgreSQLVarPrecisionMandatoryIsZeroWithScale() {
		// Mandatory, precision = 0 => default value
		assertEquals("numeric(10,2)", getSql().replaceVar("numeric(%P)", new BigDecimal("0.4")) ); // Mandatory
	}
	@Test(expected = GeneratorSqlException.class)
	public void testPostgreSQLVarPrecisionValueError() {
		getSql().replaceVar("varchar(%P)", new BigDecimal("-2")); // Invalid value
	}
	@Test
	public void testPostgreSQLVarPrecisionOptional() {
		assertEquals("varchar", getSql().replaceVar("varchar(%p)", null) );
		assertEquals("varchar", getSql().replaceVar("varchar(%p)", new BigDecimal("0")) );
		assertEquals("varchar", getSql().replaceVar("varchar(%p)", new BigDecimal("0.3")) );
	}

	@Test
	public void testAnsiSql() {
		SqlInContext sql = new SqlInContext("AnsiSql") ;
		// check current SQL language and config file
		assertEquals("AnsiSql", sql.getDatabaseName());
		assertEquals("target-db/ansisql.properties", sql.getDatabaseConfigFile());
		// check type conversion 
		assertEquals("INT",           sql.getConfigType("int", false) );
		assertEquals("INT GENERATED ALWAYS AS IDENTITY",  sql.getConfigType("int", true) );
		assertEquals("VARCHAR(%s)",   sql.getConfigType("string", false) );
		assertEquals("VARCHAR(%s)",   sql.getConfigType("string", true) ); // OK : auto-incr ignored
		assertEquals("TIMESTAMP",  sql.getConfigType("timestamp", true) );
		assertEquals("TIMESTAMP",  sql.getConfigType("timestamp", false) );
		assertEquals("TIME",  sql.getConfigType("time", true) );
		assertEquals("TIME",  sql.getConfigType("time", false) );
		// New types in ver 4.3.0
		assertEquals("TIMESTAMP",                sql.getConfigType("datetime", true) );
		assertEquals("TIMESTAMP",                sql.getConfigType("datetime", false) );
		assertEquals("TIMESTAMP WITH TIME ZONE", sql.getConfigType("datetimetz", true) );
		assertEquals("TIMESTAMP WITH TIME ZONE", sql.getConfigType("datetimetz", false) );
		assertEquals("TIME WITH TIME ZONE",      sql.getConfigType("timetz", true) );
		assertEquals("TIME WITH TIME ZONE",      sql.getConfigType("timetz", false) );
		assertEquals("CHAR(36)", sql.getConfigType("uuid", true) );
		assertEquals("CHAR(36)", sql.getConfigType("uuid", false) );
	}
	
	@Test
	public void testMySQL() {
		AttributeInContext attribute ;
		SqlInContext sql = new SqlInContext("MySQL") ;
		// check current SQL language and config file
		assertEquals("MySQL", sql.getDatabaseName());
		assertEquals("target-db/mysql.properties", sql.getDatabaseConfigFile());
		// check type conversion 
		assertEquals("TINYINT",                 sql.getConfigType("byte", false) );
		assertEquals("TINYINT AUTO_INCREMENT",  sql.getConfigType("byte", true) );
		assertEquals("SMALLINT",                sql.getConfigType("short", false) );
		assertEquals("SMALLINT AUTO_INCREMENT", sql.getConfigType("short", true) );
		assertEquals("INT",                     sql.getConfigType("int", false) );
		assertEquals("INT AUTO_INCREMENT",      sql.getConfigType("int", true) );
		assertEquals("BIGINT",                  sql.getConfigType("long", false) );
		assertEquals("BIGINT AUTO_INCREMENT",   sql.getConfigType("long", true) );
		
		assertEquals("VARCHAR(%S)",   sql.getConfigType("string", false) );
		assertEquals("VARCHAR(%S)",   sql.getConfigType("string", true) ); // OK : auto-incr ignored
		assertEquals("DATETIME",  sql.getConfigType("timestamp", true) );
		assertEquals("DATETIME",  sql.getConfigType("timestamp", false) );
		assertEquals("TIME",      sql.getConfigType("time", true) );
		assertEquals("TIME",      sql.getConfigType("time", false) );
		// New types in ver 4.3.0
		assertEquals("DATETIME",  sql.getConfigType("datetime", true) );
		assertEquals("DATETIME",  sql.getConfigType("datetime", false) );
		assertEquals("DATETIME",  sql.getConfigType("datetimetz", true) );
		assertEquals("DATETIME",  sql.getConfigType("datetimetz", false) );
		assertEquals("TIME",      sql.getConfigType("timetz", true) );
		assertEquals("TIME",      sql.getConfigType("timetz", false) );
		assertEquals("CHAR(36)",  sql.getConfigType("uuid", true) );
		assertEquals("CHAR(36)",  sql.getConfigType("uuid", false) );
		
		// Test "VARCHAR(%S)" -> Size is mandatory 
		attribute = buildAttributeWithMaxLength("firstName", NeutralType.STRING, null);
		assertEquals("VARCHAR(255)", sql.columnType(attribute)); // no size => 255 as default value
		attribute = buildAttributeWithMaxLength("firstName", NeutralType.STRING, 34);
		assertEquals("VARCHAR(34)", sql.columnType(attribute)); // use MaxLength for size 
		attribute = buildAttributeWithSize("firstName", NeutralType.STRING, "48");
		assertEquals("VARCHAR(48)", sql.columnType(attribute)); // use Size 

		// Test "NUMERIC(%p)" -> Precision is not mandatory 
		attribute = buildAttributeWithSize("price", NeutralType.DECIMAL, null);
		assertEquals("NUMERIC", sql.columnType(attribute)); // no size => no precision
		attribute = buildAttributeWithSize("price", NeutralType.DECIMAL, "10");
		assertEquals("NUMERIC(10)", sql.columnType(attribute)); // size => used for precision
		attribute = buildAttributeWithSize("price", NeutralType.DECIMAL, "10,2");
		assertEquals("NUMERIC(10,2)", sql.columnType(attribute)); // size => used for precision
	}
	
	@Test
	public void testSqlServer() {
		AttributeInContext attribute ;
		
		SqlInContext sql = new SqlInContext("SqlServer") ;
		
		assertEquals("SqlServer", sql.getDatabaseName());
		assertEquals("target-db/sqlserver.properties", sql.getDatabaseConfigFile());
		
		// Name conversion as defined for PostgreSQL
		assertEquals("city_code",    sql.convertToColumnName("cityCode") ) ;
		assertEquals("employee_job", sql.convertToColumnName("EmployeeJob") ) ;
		
		// Without explicit db name (attribute name as default )
		attribute = buildAttribute("firstName", NeutralType.STRING, null); 
		assertEquals("", attribute.getDatabaseName() ); 
		assertEquals("first_name", sql.columnName(attribute) ) ; // converted from attribute name
		// With explicit db name
		attribute = buildAttribute("firstName", NeutralType.STRING, "MY_FIRST_NAME"); 
		assertEquals("MY_FIRST_NAME", attribute.getDatabaseName() ); 
		assertEquals("MY_FIRST_NAME", sql.columnName(attribute) ) ; // not converted if explicit db name


		// Type conversion 
		assertEquals("int",           sql.getConfigType("int", false) );
		assertEquals("int identity",  sql.getConfigType("int", true) );
		assertEquals("varchar(%S)",   sql.getConfigType("string", false) );
		assertEquals("varchar(%S)",   sql.getConfigType("string", true) );
		assertEquals("time",          sql.getConfigType("time", true) );
		assertEquals("time",          sql.getConfigType("time", false) );
		assertEquals("datetime2",     sql.getConfigType("timestamp", true) );
		assertEquals("datetime2",     sql.getConfigType("timestamp", false) );
		// New types in ver 4.3.0
		assertEquals("datetime2",        sql.getConfigType("datetime", true) );
		assertEquals("datetime2",        sql.getConfigType("datetime", false) );
		assertEquals("datetimeoffset",   sql.getConfigType("datetimetz", true) );
		assertEquals("datetimeoffset",   sql.getConfigType("datetimetz", false) );
		assertEquals("time",             sql.getConfigType("timetz", true) );
		assertEquals("time",             sql.getConfigType("timetz", false) );
		assertEquals("uniqueidentifier", sql.getConfigType("uuid", true) );
		assertEquals("uniqueidentifier", sql.getConfigType("uuid", false) );

		// Test "VARCHAR(%S)" -> Size is mandatory 
		attribute = buildAttributeWithMaxLength("firstName", NeutralType.STRING, null);
		assertEquals("varchar(255)", sql.columnType(attribute)); // no size => 255 as default value
		attribute = buildAttributeWithMaxLength("firstName", NeutralType.STRING, 34);
		assertEquals("varchar(34)", sql.columnType(attribute)); // use MaxLength for size 
		attribute = buildAttributeWithSize("firstName", NeutralType.STRING, "48");
		assertEquals("varchar(48)", sql.columnType(attribute)); // use Size 

		// Test "NUMERIC(%p)" -> Precision is not mandatory 
		attribute = buildAttributeWithSize("price", NeutralType.DECIMAL, null);
		assertEquals("numeric", sql.columnType(attribute)); // no size => no precision
		attribute = buildAttributeWithSize("price", NeutralType.DECIMAL, "10");
		assertEquals("numeric(10)", sql.columnType(attribute)); // size => used for precision
		attribute = buildAttributeWithSize("price", NeutralType.DECIMAL, "10,2");
		assertEquals("numeric(10,2)", sql.columnType(attribute)); // size => used for precision
	}

	static final String TEXT = "TEXT";
	static final String REAL = "REAL";
	static final String INTEGER = "INTEGER";
	@Test
	public void testSQLite() {
		SqlInContext sql = new SqlInContext("SQLite") ;
		// check current SQL language and config file
		assertEquals("SQLite", sql.getDatabaseName());
		assertEquals("target-db/sqlite.properties", sql.getDatabaseConfigFile());
		// check type conversion 
		assertEquals(INTEGER,  sql.getConfigType("byte", false) );
		assertEquals(INTEGER,  sql.getConfigType("byte", true) );
		assertEquals(INTEGER,  sql.getConfigType("short", false) );
		assertEquals(INTEGER,  sql.getConfigType("short", true) );
		assertEquals(INTEGER,  sql.getConfigType("int", false) );
		assertEquals(INTEGER,  sql.getConfigType("int", true) );
		assertEquals(INTEGER,  sql.getConfigType("long", false) );
		assertEquals(INTEGER,  sql.getConfigType("long", true) );

		assertEquals(REAL,  sql.getConfigType("float", false) );
		assertEquals(REAL,  sql.getConfigType("float", true) );
		
		assertEquals(TEXT,  sql.getConfigType("string", false) );
		assertEquals(TEXT,  sql.getConfigType("string", true) ); 
		assertEquals(TEXT,  sql.getConfigType("time", true) );
		assertEquals(TEXT,  sql.getConfigType("time", false) );
		assertEquals(TEXT,  sql.getConfigType("timestamp", true) );
		assertEquals(TEXT,  sql.getConfigType("timestamp", false) );
		// New types in ver 4.3.0
		assertEquals(TEXT,  sql.getConfigType("datetime", true) );
		assertEquals(TEXT,  sql.getConfigType("datetime", false) );
		assertEquals(TEXT,  sql.getConfigType("datetimetz", true) );
		assertEquals(TEXT,  sql.getConfigType("datetimetz", false) );
		assertEquals(TEXT,  sql.getConfigType("timetz", true) );
		assertEquals(TEXT,  sql.getConfigType("timetz", false) );
		assertEquals(TEXT,  sql.getConfigType("uuid", true) );
		assertEquals(TEXT,  sql.getConfigType("uuid", false) );
	}
	
	@Test
	public void testOracle() {
		SqlInContext sql = new SqlInContext("Oracle") ;
		// check current SQL language and config file
		assertEquals("Oracle", sql.getDatabaseName());
		assertEquals("target-db/oracle.properties", sql.getDatabaseConfigFile());
		
		// check type conversion 
		assertEquals("NUMBER",  sql.getConfigType("byte", false) );
		assertEquals("NUMBER",  sql.getConfigType("byte", true) );
		assertEquals("NUMBER",  sql.getConfigType("short", false) );
		assertEquals("NUMBER",  sql.getConfigType("short", true) );
		assertEquals("NUMBER",  sql.getConfigType("int", false) );
		assertEquals("NUMBER",  sql.getConfigType("int", true) );
		assertEquals("NUMBER",  sql.getConfigType("long", false) );
		assertEquals("NUMBER",  sql.getConfigType("long", true) );

		assertEquals("NUMBER(%p)",  sql.getConfigType("float", false) );
		assertEquals("NUMBER(%p)",  sql.getConfigType("float", true) );
		
		assertEquals("VARCHAR2(%s)",  sql.getConfigType("string", false) );
		assertEquals("VARCHAR2(%s)",  sql.getConfigType("string", true) ); 
		
		assertEquals("DATE",  sql.getConfigType("date", true) );
		assertEquals("DATE",  sql.getConfigType("date", false) );

		assertEquals("NUMBER(1)",  sql.getConfigType("boolean", true) );
		assertEquals("NUMBER(1)",  sql.getConfigType("boolean", false) );
		
		assertEquals("TIMESTAMP",  sql.getConfigType("time", true) );
		assertEquals("TIMESTAMP",  sql.getConfigType("time", false) );
		
		assertEquals("TIMESTAMP",  sql.getConfigType("timestamp", true) );
		assertEquals("TIMESTAMP",  sql.getConfigType("timestamp", false) );
		// New types in ver 4.3.0
		assertEquals("TIMESTAMP",  sql.getConfigType("datetime", true) );
		assertEquals("TIMESTAMP",  sql.getConfigType("datetime", false) );
		assertEquals("TIMESTAMP WITH TIME ZONE",  sql.getConfigType("datetimetz", true) );
		assertEquals("TIMESTAMP WITH TIME ZONE",  sql.getConfigType("datetimetz", false) );
		assertEquals("TIMESTAMP WITH TIME ZONE",  sql.getConfigType("timetz", true) );
		assertEquals("TIMESTAMP WITH TIME ZONE",  sql.getConfigType("timetz", false) );
		assertEquals("CHAR(36)",  sql.getConfigType("uuid", true) );
		assertEquals("CHAR(36)",  sql.getConfigType("uuid", false) );
	}
	
	@Test(expected = GeneratorSqlException.class)
	public void testInvalidSpecificDbConfigFile() {
		String fileName = "src/test/resources/" + "target-db/nofile.properties" ;		
		new SqlInContext("MyDatabase", new File(fileName)) ;
	}
	
	@Test
	public void testSpecificDbConfigFile2() {
		
		String fileName = "src/test/resources/" + "target-db/test-db.properties" ;
		
		SqlInContext sql = new SqlInContext("MyDatabase", new File(fileName) );
		
		assertEquals("MyDatabase", sql.getDatabaseName());
		assertTrue(sql.getDatabaseConfigFile().endsWith("test-db.properties"));
		
		// Name conversion as defined in the file
		assertEquals("ANACONDA_CASE", sql.getConfigValue("conv.columnName"));
		assertEquals("CITY_CODE",   sql.convertToColumnName("cityCode") ) ;   // ANACONDA_CASE

		assertEquals("PascalCase", sql.getConfigValue("conv.tableName"));
		assertEquals("Employeejob", sql.convertToTableName("employeeJob") ) ; // PascalCase	

		assertEquals("camelCase", sql.getConfigValue("conv.pkName"));
		assertEquals("employeeJob", sql.convertToPkName("EMPLOYEE_JOB") ) ;	  // camelCase	
	}
	
	//------------------------------------------------------------------------------------
	
	private AttributeInContext buildAttribute(String attribName, String neutralType, String dbName) {
		DslModelAttribute fakeAttribute = new DslModelAttribute(attribName, neutralType);
		if ( dbName != null ) {
			fakeAttribute.setDatabaseName(dbName);
		}
		else {
			fakeAttribute.setDatabaseName(""); // no database name
			// as in DSL model default value
		}
		return new AttributeInContext(null, fakeAttribute, null, new EnvInContext() );
	}
	
	private AttributeInContext buildAttributeWithMaxLength(String attribName, String neutralType, Integer maxLength) {
		DslModelAttribute fakeAttribute = new DslModelAttribute(attribName, neutralType);
		if ( maxLength != null ) {
			fakeAttribute.setMaxLength(maxLength);
		}
		return new AttributeInContext(null, fakeAttribute, null, new EnvInContext() );
	}
	
	private AttributeInContext buildAttributeWithSize(String attribName, String neutralType, String size) {
		DslModelAttribute fakeAttribute = new DslModelAttribute(attribName, neutralType);
		if ( size != null ) {
			fakeAttribute.setSize(size);
		}
		return new AttributeInContext(null, fakeAttribute, null, new EnvInContext() );
	}

}
