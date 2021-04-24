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

import java.util.HashMap;
import java.util.Map;

import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.generic.model.types.NeutralType;

/**
 * 
 *  
 * @author Laurent GUERIN
 */

//-------------------------------------------------------------------------------------
public class AttributeSqlType {
	
	private final Map<String,String> defaultTypes ;
	private final Map<String,String> oracleTypes ;
	private final Map<String,String> postgresqlTypes;
	
	private final AttributeInContext attribute ;
	
	public AttributeSqlType(AttributeInContext attribute) {
		super();
		this.attribute = attribute;
		this.defaultTypes = initDefaultTypes();
		this.oracleTypes  = initOracleTypes();
		this.postgresqlTypes  = initPostgresqlTypes();
	}
	
	private static Map<String, String> initDefaultTypes() {
	    Map<String,String> myMap = new HashMap<String,String>();
	    myMap.put(NeutralType.STRING,  "VARCHAR%");
	    
	    myMap.put(NeutralType.SHORT,   "smallint"); 
	    myMap.put(NeutralType.INTEGER, "integer");
	    myMap.put(NeutralType.LONG,    "bigint");
	    
	    myMap.put(NeutralType.DECIMAL, "decimal%");
	    myMap.put(NeutralType.DOUBLE,  "NUMBER");
	    myMap.put(NeutralType.FLOAT,   "NUMBER");

	    myMap.put(NeutralType.BOOLEAN, "CHAR(1)");
	    myMap.put(NeutralType.BYTE,    "CHAR(1)");
	    myMap.put(NeutralType.BINARY,  "BLOB");
	    
	    myMap.put(NeutralType.DATE,      "DATE");
	    myMap.put(NeutralType.TIME,      "DATE");
	    myMap.put(NeutralType.TIMESTAMP, "DATE");
	    
	    return myMap;
	}
	
	private static Map<String, String> initPostgresqlTypes() {
	    Map<String,String> myMap = new HashMap<String,String>();
	    myMap.put(NeutralType.STRING,  "varchar%");
	    
	    myMap.put(NeutralType.SHORT,   "smallint"); // 2 bytes
	    myMap.put(NeutralType.INTEGER, "integer");  // 4 bytes
	    myMap.put(NeutralType.LONG,    "bigint");   // 8 bytes
	    
	    myMap.put(NeutralType.DECIMAL, "decimal%"); // "decimal" or "numeric" : NUMERIC(precision, scale) or NUMERIC(precision)
	    myMap.put(NeutralType.FLOAT,   "real");    // 	4 bytes
	    myMap.put(NeutralType.DOUBLE,  "double precision"); // 8 bytes

	    myMap.put(NeutralType.BOOLEAN, "boolean");
	    myMap.put(NeutralType.BYTE,    "char(1)");
	    myMap.put(NeutralType.BINARY,  "bytea");
	    
	    myMap.put(NeutralType.DATE,      "date");
	    myMap.put(NeutralType.TIME,      "time");
	    myMap.put(NeutralType.TIMESTAMP, "timestamp");
	    
	    return myMap;
	}
	
	private static Map<String, String> initOracleTypes() {
	    Map<String,String> myMap = new HashMap<String,String>();
	    myMap.put(NeutralType.STRING,  "VARCHAR");
	    
	    myMap.put(NeutralType.SHORT,   "NUMBER");
	    myMap.put(NeutralType.INTEGER, "NUMBER");
	    myMap.put(NeutralType.LONG,    "NUMBER");
	    
	    myMap.put(NeutralType.DECIMAL, "NUMBER");
	    myMap.put(NeutralType.DOUBLE,  "NUMBER");
	    myMap.put(NeutralType.FLOAT,   "NUMBER");

	    myMap.put(NeutralType.BOOLEAN, "CHAR(1)");
	    myMap.put(NeutralType.BYTE,    "CHAR(1)");
	    myMap.put(NeutralType.BINARY,  "BLOB");
	    
	    myMap.put(NeutralType.DATE,      "DATE");
	    myMap.put(NeutralType.TIME,      "DATE");
	    myMap.put(NeutralType.TIMESTAMP, "DATE");
	    
	    return myMap;
	}
	
	public String getSqlDDL(AttributeInContext attribute, String database) {
		String t = getDatabaseType(database, attribute.getNeutralType());
		if ( t.contains("%") ) {
			String v = null ;
			if ( ! StrUtil.nullOrVoid(attribute.getDatabaseSize()) ) {
				v = attribute.getDatabaseSize();
			} 
			else if ( ! StrUtil.nullOrVoid(attribute.getMaxLength()) ) {
				v = attribute.getMaxLength();
			}
			if ( v != null ) {
				v = "("+v+")";
			} 
			else {
				v = "";
			}
			return StrUtil.replaceVar(t, "%", v);
		}
		else {
			return t;
		}
	}

	private String getDatabaseType(String database, String neutralType) {
		if ( ! StrUtil.nullOrVoid( attribute.getDatabaseType() ) ) {
			return attribute.getDatabaseType();
		}
		else {
			Map<String,String> dbTypes = getTypesMap(database);
			return dbTypes.get(neutralType);
		}
	}
	
	public Map<String,String> getTypesMap(String database) {
		if ( "ORACLE".equalsIgnoreCase(database) ) {
			return this.oracleTypes;
		}
		if ( "POSTGRESQL".equalsIgnoreCase(database) ) {
			return this.postgresqlTypes;
		}
		return this.defaultTypes; 
	}

}