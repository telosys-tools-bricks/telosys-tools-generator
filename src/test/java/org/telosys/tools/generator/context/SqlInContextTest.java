package org.telosys.tools.generator.context;

import java.io.File;
import java.math.BigDecimal;

import org.junit.Test;
import org.telosys.tools.generator.context.exceptions.GeneratorSqlException;
import org.telosys.tools.generic.model.types.NeutralType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import junit.env.telosys.tools.generator.fakemodel.FakeAttribute;
import junit.env.telosys.tools.generator.fakemodel.FakeForeignKey;
import junit.env.telosys.tools.generator.fakemodel.FakeForeignKeyColumn;

public class SqlInContextTest {
	
	private static final String SNAKE_CASE    = "snake_case" ;
	private static final String ANACONDA_CASE = "ANACONDA_CASE";
		
	@Test
	public void testPostgreSQL() {
		AttributeInContext attribute ;
		
		SqlInContext sql = new SqlInContext("PostgreSQL") ;
		
		assertEquals("PostgreSQL", sql.getDatabaseName());
		assertEquals("target-db/postgresql.properties", sql.getDatabaseConfigFile());
		
		// Low level methods (without attribute)
		
		// Name conversion with specific style name
		assertEquals("city_code",    sql.convertName("cityCode", SNAKE_CASE) ) ;
		assertEquals("employee_job", sql.convertName("EmployeeJob", SNAKE_CASE) ) ;
		
		assertEquals("CITY_CODE",    sql.convertName("cityCode", ANACONDA_CASE) ) ;
		assertEquals("EMPLOYEE_JOB", sql.convertName("EmployeeJob", ANACONDA_CASE) ) ;

		// Name conversion as defined for PostgreSQL
		assertEquals("city_code",    sql.convertToColumnName("cityCode") ) ;
		assertEquals("employee_job", sql.convertToColumnName("EmployeeJob") ) ;
		
		// Without explicit db name (attribute name as default )
		attribute = buildAttribute("firstName", NeutralType.STRING, null); 
		assertEquals("first_name", sql.columnName(attribute) ) ;
		// With explicit db name
		attribute = buildAttribute("firstName", NeutralType.STRING, "FIRST_NAME"); 
		assertEquals("first_name", sql.columnName(attribute) ) ; // convert even if explicit db name


		// Type conversion 
		assertEquals("integer", sql.getConfigType("int", false) );
		assertEquals("serial",  sql.getConfigType("int", true) );
		assertEquals("varchar(%s)", sql.getConfigType("string", false) );
		assertEquals("varchar(%s)", sql.getConfigType("string", true) ); // OK : auto-incr ignored
		
		// Var replacement  
		assertEquals("varchar(26)", sql.replaceVar("varchar(%s)", Integer.valueOf(26), null) );
		assertEquals("varchar(26)", sql.replaceVar("varchar(%s)", Integer.valueOf(26), new BigDecimal("0")) );
		assertEquals("varchar",     sql.replaceVar("varchar(%s)", null, null));

		assertEquals("NUMBER(2)",    sql.replaceVar("NUMBER(%p)", null, new BigDecimal("2")) );
		assertEquals("NUMBER(10.2)", sql.replaceVar("NUMBER(%p)", null, new BigDecimal("10.2")) );
		assertEquals("NUMBER",       sql.replaceVar("NUMBER(%p)", null, null ) );
		
		//--- Primary Key :
		assertEquals("pk_foo_bar", sql.convertToPkName("pkFooBar") );
		assertEquals("pk_foo_bar", sql.convertToPkName("PkFooBar") );
		assertEquals("pk_foo_bar", sql.convertToPkName("Pk_Foo_Bar") );
		assertEquals("pk_foo_bar", sql.convertToPkName("PK_foo_BAR") );
		
		//--- Foreign Key :
		assertEquals("fk_foo_bar", sql.convertToFkName("fkFooBar") );
		assertEquals("fk_foo_bar", sql.convertToFkName("FkFooBar") );

		ForeignKeyInContext fk = buidForeignKey1();
		assertEquals("fk_driver_car", sql.fkName(fk) );
		assertEquals("good_driver", sql.fkTable(fk) );
		assertEquals("special_car", sql.fkReferencedTable(fk) );
		assertEquals("car_id", sql.fkColumns(fk) );
		assertEquals("id",     sql.fkReferencedColumns(fk) );
		
		fk = buidForeignKey2();
		assertEquals("fk_driver_car", sql.fkName(fk) );
		assertEquals("good_driver", sql.fkTable(fk) );
		assertEquals("special_car", sql.fkReferencedTable(fk) );
		assertEquals("car_id1, car_id2",  sql.fkColumns(fk) );
		assertEquals("my_id1, my_id2",    sql.fkReferencedColumns(fk) );
		
	}

	@Test(expected = GeneratorSqlException.class)
	public void testPostgreSQLVarSizeMandatoryError() {
		SqlInContext sql = new SqlInContext("PostgreSQL") ;
		sql.replaceVar("varchar(%S)", null, null); // Mandatory
	}
	
	@Test(expected = GeneratorSqlException.class)
	public void testPostgreSQLVarSizeValueError() {
		SqlInContext sql = new SqlInContext("PostgreSQL") ;
		sql.replaceVar("varchar(%s)", Integer.valueOf(-2), null);
	}
	
	@Test(expected = GeneratorSqlException.class)
	public void testPostgreSQLVarPrecisionMandatoryError() {
		SqlInContext sql = new SqlInContext("PostgreSQL") ;
		sql.replaceVar("numeric(%P)", null, null); // Mandatory
	}
	
	@Test(expected = GeneratorSqlException.class)
	public void testPostgreSQLVarPrecisionValueError() {
		SqlInContext sql = new SqlInContext("PostgreSQL") ;
		sql.replaceVar("numeric(%p)", null, new BigDecimal("0"));
	}

	@Test(expected = GeneratorSqlException.class)
	public void testInvalidSpecificDbConfigFile() {
		String fileName = "src/test/resources/" + "target-db/nofile.properties" ;		
		new SqlInContext("MyDatabase", fileName) ;
	}
	
	@Test
	public void testSpecificDbConfigFile() {
		
		String fileName = "src/test/resources/" + "target-db/test-db.properties" ;
		
		SqlInContext sql = new SqlInContext("MyDatabase", fileName) ;
		
		assertEquals("MyDatabase", sql.getDatabaseName());
		assertEquals(fileName, sql.getDatabaseConfigFile());
		
		// Name conversion as defined in the file
		assertEquals("CITY_CODE",   sql.convertToColumnName("cityCode") ) ;
		assertEquals("EmployeeJob", sql.convertToTableName("employeeJob") ) ;		
		assertEquals("employeeJob", sql.convertToPkName("EMPLOYEE_JOB") ) ;		
	}
	
	@Test
	public void testSpecificDbConfigFile2() {
		
		String fileName = "src/test/resources/" + "target-db/test-db.properties" ;
		
		SqlInContext sql = new SqlInContext("MyDatabase", new File(fileName) );
		
		assertEquals("MyDatabase", sql.getDatabaseName());
		//print(sql.getDatabaseConfigFile());
		assertTrue(sql.getDatabaseConfigFile().endsWith("test-db.properties"));
		
		// Name conversion as defined in the file
		assertEquals("CITY_CODE",   sql.convertToColumnName("cityCode") ) ;
		assertEquals("EmployeeJob", sql.convertToTableName("employeeJob") ) ;		
		assertEquals("employeeJob", sql.convertToPkName("EMPLOYEE_JOB") ) ;		
	}
	
	//------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------------
	private void print(String s) {
		System.out.println(s);
	}

	private AttributeInContext buildAttribute(String attribName, String neutralType, String dbName) {
		FakeAttribute fakeAttribute = new FakeAttribute(attribName, neutralType, false);
		if ( dbName != null ) {
			fakeAttribute.setDatabaseName(dbName);
		}
		else {
			fakeAttribute.setDatabaseName(attribName); // as in DSL model default value
		}
		return new AttributeInContext(null, fakeAttribute, null, null);
	}
	
	private ForeignKeyInContext buidForeignKey1() {
		FakeForeignKey fakeFK = new FakeForeignKey("FkDriverCar", "GoodDriver", "SpecialCar");
		fakeFK.addColumn(new FakeForeignKeyColumn("carId", "id", 1));
		return new ForeignKeyInContext(fakeFK);
	}

	private ForeignKeyInContext buidForeignKey2() {
		FakeForeignKey fakeFK = new FakeForeignKey("FK_DRIVER_CAR", "GOOD_DRIVER", "SPECIAL_CAR");
		fakeFK.addColumn(new FakeForeignKeyColumn("carId1", "myId1", 1));
		fakeFK.addColumn(new FakeForeignKeyColumn("carId2", "myId2", 2));
		return new ForeignKeyInContext(fakeFK);
	}
}
