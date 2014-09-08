/**
 *  Copyright (C) 2008-2013  Telosys project org. ( http://www.telosys.org/ )
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

import org.telosys.tools.commons.dbcfg.DatabaseConfiguration;
import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.names.ContextName;

/**
 * Class for a database defined in the "databases.dbcfg" file 
 * 
 * @author Laurent GUERIN
 * 
 */
//-------------------------------------------------------------------------------------
@VelocityObject(
		contextName= ContextName.DATABASE ,
		text = {
				"Object representing a database defined in the 'databases.dbcfg' file",
				""
		},
		since = "2.1.0"
 )
//-------------------------------------------------------------------------------------
public class DatabaseInContext
{	
    private final int        id  ;

    private final String     name  ;

    private final String     jdbcUrl ;
    private final String     driverClass ;
    private final String     user ;
    private final String     password ;

    private final String     type ;    // ver 2.1.0
    private final String     dialect ; // ver 2.1.0

    private final String     isolationLevel ;
    private final int        poolSize ;
   
    private final String     metadataCatalog ;
    private final String     metadataSchema ;

//    private String     metadataTableNamePattern = null;
//
//    private String     metadataTableTypes       = null;

    /**
     * Constructor
     */
    public DatabaseInContext( DatabaseConfiguration databaseConfiguration )
    {
    	id              = databaseConfiguration.getDatabaseId();
    	name            = databaseConfiguration.getDatabaseName();
    	driverClass     = databaseConfiguration.getDriverClass();
    	jdbcUrl         = databaseConfiguration.getJdbcUrl();
    	user            = databaseConfiguration.getUser();
    	password        = databaseConfiguration.getPassword();

    	type        = databaseConfiguration.getTypeName();
    	dialect         = databaseConfiguration.getDialect();
    	
    	isolationLevel  = databaseConfiguration.getIsolationLevel();
    	poolSize        = databaseConfiguration.getPoolSize();
    	
    	metadataCatalog = databaseConfiguration.getMetadataCatalog();
    	metadataSchema  = databaseConfiguration.getMetadataSchema();
    }

	//----------------------------------------------------------------------------------
	@VelocityMethod(
		text={ "Returns the database id" }
		)
    public int getId() {
        return id;
    }

	//----------------------------------------------------------------------------------
	@VelocityMethod(
		text={ "Returns the database name" }
		)
    public String getName() {
        return name;
    }

	//----------------------------------------------------------------------------------
	@VelocityMethod(
		text={ "Returns the JDBC driver class to be used to establish a JDBC connection " }
		)
    public String getDriverClass() {
        return driverClass;
    }

	//----------------------------------------------------------------------------------
	@VelocityMethod(
		text={ "Returns the URL to be used to establish a JDBC connection " }
		)
    public String getJdbcUrl() {
        return jdbcUrl;
    }

	//----------------------------------------------------------------------------------
	@VelocityMethod(
		text={ "Returns the USER to be used to establish a JDBC connection " }
		)
    public String getUser() {
    	return user ;
    }
    
	//----------------------------------------------------------------------------------
	@VelocityMethod(
		text={ "Returns the PASSWORD to be used to establish a JDBC connection " }
		)
    public String getPassword() {
    	return password ;
    }

	//----------------------------------------------------------------------------------
	@VelocityMethod(
		text={ "Returns the type name. Usually used to identify the type/vendor of the database ",
				"(e.g. 'DERBY', 'MYSQL', 'ORACLE', etc)"}
		)
    public String getType() {
    	return type ;
    }

	//----------------------------------------------------------------------------------
	@VelocityMethod(
		text={ "Returns the 'dialect' of the database. Can be used to identify the Hibernate dialect class ",
				"(e.g. 'org.hibernate.dialect.MySQLDialect', etc)"}
		)
    public String getDialect() {
    	return dialect ;
    }

    //----------------------------------------------------------------------------------
	@VelocityMethod(
			text={ "Returns the isolation level " }
			)
    public String getIsolationLevel() {
        return isolationLevel;
    }

    //----------------------------------------------------------------------------------
	@VelocityMethod(
			text={ "Returns the pool size " }
			)
    public int getPoolSize() {
        return poolSize;
    }

    //----------------------------------------------------------------------------------
	@VelocityMethod(
			text={ "Returns the CATALOG to be used to retrieve the meta-data " }
			)
    public String getCatalog() {
        return metadataCatalog;
    }

    //----------------------------------------------------------------------------------
	@VelocityMethod(
			text={ "Returns the SCHEMA to be used to retrieve the meta-data " }
			)
    public String getSchema() {
        return metadataSchema;
    }

    //----------------------------------------------------------------------------------
    @Override
    public String toString() {
        return name ;
    }
}