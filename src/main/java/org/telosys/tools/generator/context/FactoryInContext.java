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

import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.names.ContextName;

//-------------------------------------------------------------------------------------
@VelocityObject(
		contextName=ContextName.FACTORY,
		text = { 
				"Factory providing objects instances",
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
			"#set( $sql = $factory.getSql('PostgreSQL') )"
		},
		since = "3.4.0"
	)
	public SqlInContext getSql(String targetDbName) {
		return new SqlInContext(targetDbName); 
    }
	
//	//-------------------------------------------------------------------------------------
//	@VelocityMethod ( 
//		text= { 
//			"Creates a new instance of SQL tool object",
//			"for the given target database name",
//			"using the given specific database definition file"
//		},
//		parameters = {
//			"targetDbName : target database name ('postgresql', 'mysql', etc ) ",
//			"targetDbConfigFile : target database configuration file located in the bundle folder  "
//		},		
//		example = {	
//			"$factory.getSql('postgresql', 'db-postgresql.properties')"
//		},
//		since = "3.4.0"
//	)
//	public SqlInContext getSql(String targetDbName, String targetDbConfigFile) {
//		return new SqlInContext(targetDbName, targetDbConfigFile); 
//    }
//	
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
			"#set( $sql = $factory.getSql('PostgreSQL', $fn.fileFromBundle('db-postgresql.properties') ) )"
		},
		since = "3.4.0"
	)
	public SqlInContext getSql(String targetDbName, FileInContext fileInContext) {
		return new SqlInContext(targetDbName, fileInContext.getFile()); 
    }
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Creates a new instance of the JDBC tool for the given entity",
			""
		},
		parameters = {
			"entity : the entity to be used (to create CRUD SQL requests, mapping, etc) ",
			"useSchema : use schema name in requests if true"
		},		
		example={	
			"#set( $jdbc = $factory.getJdbc($entity, true) )",
			"#set( $jdbc = $factory.getJdbc($entity, false) )"
		},
		since = "3.4.0"
	)
	public JdbcInContext getJdbc(EntityInContext entity, boolean useSchema)
    {
		if ( entity == null ) {
			throw new IllegalArgumentException("$jdbcFactory.getInstance($entity) : $entity is null");
		}
		return new JdbcInContext(entity, useSchema); 
    }
	//-------------------------------------------------------------------------------------
}
