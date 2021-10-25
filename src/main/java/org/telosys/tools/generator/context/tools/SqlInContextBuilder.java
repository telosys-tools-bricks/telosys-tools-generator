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

import org.telosys.tools.generator.context.SqlInContext;
import org.telosys.tools.generator.context.exceptions.GeneratorSqlException;

/**
 * Utility tool to convert model information to SQL database type, name, etc
 * 
 * @author Laurent GUERIN
 * @since 3.4.0
 */

// -------------------------------------------------------------------------------------
public class SqlInContextBuilder {

	private SqlInContextBuilder() {
		super();
	}

	public static final String ANSISQL = "ansisql";
	public static final String ORACLE = "oracle";
	public static final String POSTGRESQL = "postgresql";
	public static final String MYSQL = "mysql";
	public static final String SQLSERVER = "sqlserver";

	private static final List<String> KNOWN_DB = new LinkedList<>();
	static {
		KNOWN_DB.add(ANSISQL);
		KNOWN_DB.add(ORACLE);
		KNOWN_DB.add(POSTGRESQL);
		KNOWN_DB.add(MYSQL);
		KNOWN_DB.add(SQLSERVER);
	}

	public static boolean knownDatabase(String dbName) {
		String dbname = dbName.toLowerCase();
		for (String s : KNOWN_DB) {
			if (s.equals(dbname)) {
				return true;
			}
		}
		return false;
	}

	public static SqlInContext buildDefault() {
		// No file & no database name => default => ANSI-SQL
		return new SqlInContext(ANSISQL);
	}

	public static void checkDbName(String dbName) {
		if (!knownDatabase(dbName)) {
			throw new GeneratorSqlException("Unknown database '" + dbName + "'");
		}
	}

	public static SqlInContext buildFromDbName(String dbName) {
		checkDbName(dbName);
		// valid database name => use it
		return new SqlInContext(dbName);
	}

	public static void checkDbFile(File file) {
		if (!file.exists()) {
			throw new GeneratorSqlException("Database file not found '" + file.getAbsolutePath() + "'");
		}
	}

	public static SqlInContext buildFromDbFile(File file) {
		checkDbFile(file);
		String dbName = file.getName(); // "xxxx.properties"
		return new SqlInContext(dbName, file);
	}

}