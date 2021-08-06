/**
 *  Copyright (C) 2008-2017  Telosys project org. ( http://www.telosys.org/ )
 *
 *  Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.gnu.org/licenses/lgpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.telosys.tools.generator.context;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.names.ContextName;

//-------------------------------------------------------------------------------------
@VelocityObject(
		contextName=ContextName.FACTORY,
		text = { 
				"Factory providing new objects instances",
				""
		},
		since = "3.4.0"
 )
//-------------------------------------------------------------------------------------
public class FactoryInContext {
	
	/**
	 * Constructor
	 */
	public FactoryInContext() {
		super();
	}
	
	//-------------------------------------------------------------------------------------
	// BigDecimal
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Creates a new instance of BigDecimal object with the given string value",
			""
		},
		parameters = {
			"value : the value to be converted in BigDecimal "
		},		
		example={	
			"#set( $var = $factory.newBigDecimal(\"12.4\") )"
		},
		since = "3.4.0"
	)
	public BigDecimal newBigDecimal(String value) {
		return new BigDecimal(value); 
    }
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Creates a new instance of BigDecimal object with the given double value",
			""
		},
		parameters = {
			"value : the value to be converted in BigDecimal "
		},		
		example={	
			"#set( $var = $factory.newBigDecimal(12.4) )"
		},
		since = "3.4.0"
	)
	public BigDecimal newBigDecimal(Double value) {
		return BigDecimal.valueOf(value); 
    }
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Creates a new instance of BigDecimal object with the given integer value",
			""
		},
		parameters = {
			"value : the value to be converted in BigDecimal "
		},		
		example={	
			"#set( $var = $factory.newBigDecimal(20) )"
		},
		since = "3.4.0"
	)
	public BigDecimal newBigDecimal(Integer value) {
		return BigDecimal.valueOf(value); 
    }

	//-------------------------------------------------------------------------------------
	// BigInteger
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Creates a new instance of BigInteger object with the given string value",
			""
		},
		parameters = {
			"value : the value to be converted in BigInteger "
		},		
		example={	
			"#set( $var = $factory.newBigInteger(\"1234\") )"
		},
		since = "3.4.0"
	)
	public BigInteger newBigInteger(String value) {
		return new BigInteger(value); 
    }
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Creates a new instance of BigInteger object with the given integer value",
			""
		},
		parameters = {
			"value : the value to be converted in BigInteger "
		},		
		example={	
			"#set( $var = $factory.newBigInteger(123) )"
		},
		since = "3.4.0"
	)
	public BigInteger newBigInteger(Integer value) {
		return BigInteger.valueOf(value); 
    }
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Creates a new instance of the JDBC tool object for the given entity",
			""
		},
		parameters = {
			"entity : the entity to be used (to create CRUD SQL requests, mapping, etc) "
		},		
		example={	
			"#set( $jdbc = $factory.newJdbc($entity) )"
		},
		since = "3.4.0"
	)
	public JdbcInContext newJdbc(EntityInContext entity)
    {
		if ( entity == null ) {
			throw new IllegalArgumentException("$jdbcFactory.getInstance($entity) : $entity is null");
		}
		return new JdbcInContext(entity, false); 
    }

	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Creates a new instance of SQL tool object using the default database definition (if any)",
			"for the given target database name ",
			" "
		},
		parameters = {
			"targetDbName : target database name (not case sensitive, eg :'Postgresql', 'MySQL', etc ) "
		},		
		example = {	
			"#set( $sql = $factory.newSql('PostgreSQL') )"
		},
		since = "3.4.0"
	)
	public SqlInContext newSql(String targetDbName) {
		return new SqlInContext(targetDbName); 
    }
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Creates a new instance of SQL tool object using a specific database definition file",
			"for the given target database name",
			""
		},
		parameters = {
			"targetDbName : target database name ('postgresql', 'mysql', etc ) ",
			"targetDbConfigFile : target database configuration file "
		},		
		example = {	
			"#set( $sql = $factory.newSql('PostgreSQL', $fn.fileFromBundle('postgresql.properties') ) )"
		},
		since = "3.4.0"
	)
	public SqlInContext newSql(String targetDbName, FileInContext fileInContext) {
		return new SqlInContext(targetDbName, fileInContext.getFile()); 
    }
	
}
