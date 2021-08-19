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
package org.telosys.tools.generator.context.tools;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.generator.context.AttributeInContext;
import org.telosys.tools.generator.context.EnvInContext;
import org.telosys.tools.generator.context.SqlInContext;

/**
 * Utility tool to convert model information to SQL database type, name, etc
 *  
 * @author Laurent GUERIN
 */

//-------------------------------------------------------------------------------------
public class SqlConverter {
	
	public static final String ANSISQL    = "ansisql";
	public static final String ORACLE     = "oracle";
	public static final String POSTGRESQL = "postgresql";
	public static final String MYSQL      = "mysql";
	public static final String SQLSERVER  = "sqlserver";
	
	private static final List<String> KNOWN_DB = new LinkedList<>();
	static {
		KNOWN_DB.add(ANSISQL);
		KNOWN_DB.add(ORACLE);
		KNOWN_DB.add(POSTGRESQL);
		KNOWN_DB.add(MYSQL);
		KNOWN_DB.add(SQLSERVER);
	}
	
	private SqlInContext sqlInContext ;

	public SqlConverter(EnvInContext env) {
		super();
		String database = env.getDatabase();
		File databaseConvfile = env.getDatabaseConvFile();
		if ( databaseConvfile != null ) {
			// Specific file => use it
			if ( StrUtil.nullOrVoid(database) ) {
				database = "undefined" ;
			}
			this.sqlInContext = new SqlInContext(database, databaseConvfile);
		}
		else {
			if ( StrUtil.nullOrVoid(database) ) {
				// No file & no database name => default => ANSI-SQL
				this.sqlInContext = new SqlInContext(ANSISQL);
			}
			else {
				if ( knownDatabase(database) ) {
					// No file & valid database name => use it
					this.sqlInContext = new SqlInContext(database);
				}
				else {
					// No file & unknown database name => default => ANSI-SQL
					this.sqlInContext = new SqlInContext(ANSISQL);
				}
			}
		}
	}

	private boolean knownDatabase(String dbName) {
		String dbname = dbName.toLowerCase();
		for ( String s : KNOWN_DB ) {
			if ( s.equals(dbname) ) {
				return true ;
			}
		}
		return false;
	}
	
	/**
	 * Returns the SQL database column type for the given attribute
	 * For example 'varchar(12)' for an attribute with neutral type 'string' 
	 * @param attribute
	 * @return
	 */
	public String getSqlColumnType(AttributeInContext attribute) {
		return sqlInContext.columnType(attribute);
	}
	
	/**
	 * Returns the SQL database column name for the given attribute
	 * For example 'city_code' for an attribute named 'cityCode'
	 * @param attribute
	 * @return
	 */
	public String getSqlColumnName(AttributeInContext attribute) {
		return sqlInContext.columnName(attribute);
	}
	
	/**
	 * Returns the SQL database column constraints for the given attribute
	 * For example 'NOT NULL DEFAULT 123'
	 * @param attribute
	 * @return
	 */
	public String getSqlColumnConstraints(AttributeInContext attribute) {
		return sqlInContext.columnConstraints(attribute);
	}
}