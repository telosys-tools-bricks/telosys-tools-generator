package org.telosys.tools.generator.context;

import java.io.File;
import java.math.BigDecimal;

import org.junit.Test;
import org.telosys.tools.dsl.model.DslModelAttribute;
import org.telosys.tools.generator.GeneratorException;
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
	public void testPostgreSQL() throws GeneratorException {
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
		assertEquals("pkfoobar", sql.convertToPkName("PKFOOBAR") );
		
		//--- Foreign Key :
		assertEquals("fk_foo_bar", sql.convertToFkName("fkFooBar") );
		assertEquals("fk_foo_bar", sql.convertToFkName("FkFooBar") );

		/***
		ForeignKeyInContext fk ;
		ModelInContext model = buildModel();
		fk = buidForeignKey1(model);
		assertEquals("fk_driver_car", sql.fkName(fk) );
		assertEquals("fk_driver_car", fk.getSqlName() );
		//assertEquals("good_driver", sql.fkTable(fk) );
		assertEquals("good_driver", sql.fkOriginTable(fk));
		assertEquals("special_car", sql.fkReferencedTable(fk) );
		assertEquals("car_id", sql.fkColumns(fk) );
		assertEquals("id",     sql.fkReferencedColumns(fk) );
		
		fk = buidForeignKey2(model);
		assertEquals("fk_driver_car", sql.fkName(fk) );
		//assertEquals("good_driver", sql.fkTable(fk) );
		assertEquals("good_driver", sql.fkOriginTable(fk));
		assertEquals("special_car", sql.fkReferencedTable(fk) );
		assertEquals("car_id1, car_id2",  sql.fkColumns(fk) );
		assertEquals("my_id1, my_id2",    sql.fkReferencedColumns(fk) );
		***/
		
		//EntityInContext carEntity = buildEntity("Car", "CAR");
		EntityInContext carEntity = Builder.buildEntityInContext("Car", "CAR");
		//assertEquals("pk_car", sql.pkName(carEntity) );

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
		assertEquals("ANACONDA_CASE", sql.getConfigValue("conv.columnName"));
		assertEquals("CITY_CODE",   sql.convertToColumnName("cityCode") ) ;   // ANACONDA_CASE

		assertEquals("PascalCase", sql.getConfigValue("conv.tableName"));
		assertEquals("Employeejob", sql.convertToTableName("employeeJob") ) ; // PascalCase	

		assertEquals("camelCase", sql.getConfigValue("conv.pkName"));
		assertEquals("employeeJob", sql.convertToPkName("EMPLOYEE_JOB") ) ;	  // camelCase	
	}
	
	//------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------------
	private void print(String s) {
		System.out.println(s);
	}
	
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

/***
	private ForeignKeyInContext buidForeignKey1(ModelInContext model) {
		FakeForeignKey fakeFK = new FakeForeignKey("FK_DRIVER_CAR1", "GoodDriver", "SpecialCar");
//		fakeFK.addColumn(new FakeForeignKeyColumn("carId", "id", 1));
		fakeFK.addAttribute(new FakeForeignKeyAttribute(1, "carId", "id"));
		return new ForeignKeyInContext(fakeFK, model, new EnvInContext());
	}

	private ForeignKeyInContext buidForeignKey2(ModelInContext model) {
//		FakeForeignKey fakeFK = new FakeForeignKey("FK_DRIVER_CAR", "GOOD_DRIVER", "SPECIAL_CAR");
		FakeForeignKey fakeFK = new FakeForeignKey("FK_DRIVER_CAR2", "GoodDriver", "SpecialCar");
//		fakeFK.addColumn(new FakeForeignKeyColumn("carId1", "myId1", 1));
		fakeFK.addAttribute(new FakeForeignKeyAttribute(1, "carId1", "myId1"));
//		fakeFK.addColumn(new FakeForeignKeyColumn("carId2", "myId2", 2));
		fakeFK.addAttribute(new FakeForeignKeyAttribute(2, "carId2", "myId2"));
		return new ForeignKeyInContext(fakeFK, model, new EnvInContext());
	}
	
	private ModelInContext buildModel() {
		
	}
**/
}
