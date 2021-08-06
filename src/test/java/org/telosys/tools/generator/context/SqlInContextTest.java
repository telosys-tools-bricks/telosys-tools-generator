package org.telosys.tools.generator.context;

import java.io.File;
import java.math.BigDecimal;

import org.junit.Test;
import org.telosys.tools.generator.context.exceptions.GeneratorSqlException;
import org.telosys.tools.generic.model.types.NeutralType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import junit.env.telosys.tools.generator.fakemodel.FakeAttribute;
import junit.env.telosys.tools.generator.fakemodel.FakeEntity;
import junit.env.telosys.tools.generator.fakemodel.FakeForeignKey;
import junit.env.telosys.tools.generator.fakemodel.FakeForeignKeyColumn;

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
//		assertEquals("varchar(26)", sql.replaceVar("varchar(%s)", Integer.valueOf(26), null) );
//		assertEquals("varchar(26)", sql.replaceVar("varchar(%s)", Integer.valueOf(26), new BigDecimal("0")) );
//		assertEquals("varchar",     sql.replaceVar("varchar(%s)", null, null));

		assertEquals("varchar(26)", sql.replaceVar("varchar(%s)", new BigDecimal("26")) );
		assertEquals("varchar(20)", sql.replaceVar("varchar(%s)", new BigDecimal("20.98")) );
		assertEquals("varchar",     sql.replaceVar("varchar(%s)", null) );

//		assertEquals("NUMBER(2)",    sql.replaceVar("NUMBER(%p)", null, new BigDecimal("2")) );
//		assertEquals("NUMBER(10.2)", sql.replaceVar("NUMBER(%p)", null, new BigDecimal("10.2")) );
//		assertEquals("NUMBER",       sql.replaceVar("NUMBER(%p)", null, null ) );
		
		assertEquals("NUMBER(2)",    sql.replaceVar("NUMBER(%p)", new BigDecimal("2")) );
		assertEquals("NUMBER(10.2)", sql.replaceVar("NUMBER(%p)", new BigDecimal("10.2")) );
		assertEquals("NUMBER",       sql.replaceVar("NUMBER(%p)", null ) );
		
		//--- Primary Key :
		assertEquals("pk_foo_bar", sql.convertToPkName("pkFooBar") );
		assertEquals("pk_foo_bar", sql.convertToPkName("PkFooBar") );
		assertEquals("pk_foo_bar", sql.convertToPkName("Pk_Foo_Bar") );
		assertEquals("pk_foo_bar", sql.convertToPkName("PK_foo_BAR") );
		
		assertEquals("pk_car", sql.pkName( buildEntity("Car", "CAR")) );

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

	//---- (%s) and (%S)
	@Test(expected = GeneratorSqlException.class)
	public void testPostgreSQLVarSizeMandatoryError1() {
		getSql().replaceVar("varchar(%S)", null); // Mandatory
	}
	@Test(expected = GeneratorSqlException.class)
	public void testPostgreSQLVarSizeMandatoryError2() {
		getSql().replaceVar("varchar(%S)", new BigDecimal("0")); // Mandatory
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
	@Test(expected = GeneratorSqlException.class)
	public void testPostgreSQLVarPrecisionMandatoryError1() {
		getSql().replaceVar("numeric(%P)", null); // Mandatory
	}
	@Test(expected = GeneratorSqlException.class)
	public void testPostgreSQLVarPrecisionMandatoryError2() {
		getSql().replaceVar("numeric(%P)", new BigDecimal("0")); // Mandatory
	}
	@Test(expected = GeneratorSqlException.class)
	public void testPostgreSQLVarPrecisionMandatoryError3() {
		getSql().replaceVar("numeric(%P)", new BigDecimal("0.4")); // Mandatory
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

	private EntityInContext buildEntity(String entityName, String tableName) {
		FakeEntity fakeEntity = new FakeEntity(entityName, tableName);
		return new EntityInContext(fakeEntity, null, null, null);
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
