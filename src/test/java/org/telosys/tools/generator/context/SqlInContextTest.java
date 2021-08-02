package org.telosys.tools.generator.context;

import java.math.BigDecimal;

import org.junit.Test;
import org.telosys.tools.generator.context.exceptions.GeneratorSqlException;
import org.telosys.tools.generic.model.types.NeutralType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import junit.env.telosys.tools.generator.fakemodel.FakeAttribute;
import junit.env.telosys.tools.generator.fakemodel.FakeForeignKey;

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
		
		ForeignKeyInContext fk = buidForeignKey();
		// TODO
//		assertEquals("xx",    sql.fkColumns(fk) );
//		assertEquals("xx",    sql.fkReferencedColumns(fk) );
		
		
		assertTrue(true);
		assertFalse(false);
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
	
	private ForeignKeyInContext buidForeignKey() {
		FakeForeignKey fakeFK = new FakeForeignKey("FK_DRIVER_CAR", "DRIVER", "CAR");
		// TODO : fakeFK.addColumn(new );
		return new ForeignKeyInContext(fakeFK);
	}
}
