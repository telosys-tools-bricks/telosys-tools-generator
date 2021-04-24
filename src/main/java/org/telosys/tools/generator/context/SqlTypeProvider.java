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
public class SqlTypeProvider {
	
	private static final String ANSI       = "ANSI";
	private static final String ORACLE     = "ORACLE";
	private static final String POSTGRESQL = "POSTGRESQL";
	private static final String MYSQL      = "MYSQL";
	
	private final static Map<String,Map<String,String>> maps = new HashMap<>(); 
	{
		maps.put(ANSI,       typesANSI());
		maps.put(ORACLE,     typesORACLE() );
		maps.put(POSTGRESQL, typesPOSTGRESQL() );
		maps.put(MYSQL,      typesMYSQL() );
	}

	public static final String getSqlType(AttributeInContext attribute, EnvInContext env) {

		attribute.getDatabaseTypeWithSize();
		
		Map<String,String> mapping = getTypesMapping(env);
		if ( mapping != null ) {
			String sqlType = mapping.get(attribute.getNeutralType());
			if ( sqlType != null ) {
				return applySize(sqlType, attribute);
			}
			return "ERROR_NO_SQL_TYPE";
		}
		return "ERROR_NO_SQL_TYPE_MAPPING";
	}
	
	private static final Map<String,String> getTypesMapping(EnvInContext env) {
		Map<String,String> map ;
		// Specific database types mapping 
		map = env.getDatabaseTypesMapping();
		if ( map != null && map.size() != 0 ) {
			return map;
		}
		// Mapping for current database 
		map = maps.get(env.getDatabase());
		if ( map != null && map.size() != 0 ) {
			return map;
		}
		// Use default mapping
		return maps.get(ANSI);
	}
	
	private static final String applySize(String sqlType, AttributeInContext attribute) {
		if ( sqlType.contains("%") ) {
			String v = "" ;
			if ( ! StrUtil.nullOrVoid(attribute.getDatabaseSize()) ) {
				v = attribute.getDatabaseSize().trim();
			} 
			else if ( ! StrUtil.nullOrVoid(attribute.getMaxLength()) ) {
				v = attribute.getMaxLength().trim();
			}
			return StrUtil.replaceVar(sqlType, "%", v);
		}
		else {
			return sqlType;
		}
	}

	//------------------------------------------------------------------------------------
	//  TYPES MAPPINGS
	//------------------------------------------------------------------------------------
	private static Map<String, String> typesANSI() {
	    Map<String,String> map = new HashMap<String,String>();
	    map.put(NeutralType.STRING,  "VARCHAR(%)");
	    
	    map.put(NeutralType.SHORT,   "SMALLINT"); 
	    map.put(NeutralType.INTEGER, "INTEGER");
	    map.put(NeutralType.LONG,    "BIGINT");
	    
	    map.put(NeutralType.DECIMAL, "DECIMAL(%)");
	    map.put(NeutralType.FLOAT,   "REAL");
	    map.put(NeutralType.DOUBLE,  "DOUBLE PRECISION");

	    map.put(NeutralType.BOOLEAN, "BOOLEAN");
	    map.put(NeutralType.BYTE,    "CHAR(1)");
	    map.put(NeutralType.BINARY,  "BLOB");
	    
	    map.put(NeutralType.DATE,      "DATE");
	    map.put(NeutralType.TIME,      "TIME");
	    map.put(NeutralType.TIMESTAMP, "TIMESTAMP");
	    
	    return map;
	}
	
	private static Map<String, String> typesPOSTGRESQL() {
	    Map<String,String> map = new HashMap<String,String>();
	    map.put(NeutralType.STRING,  "varchar(%)");
	    
	    map.put(NeutralType.SHORT,   "smallint"); // 2 bytes
	    map.put(NeutralType.INTEGER, "integer");  // 4 bytes
	    map.put(NeutralType.LONG,    "bigint");   // 8 bytes
	    
	    map.put(NeutralType.DECIMAL, "decimal(%)"); // "decimal" or "numeric" : NUMERIC(precision, scale) or NUMERIC(precision)
	    map.put(NeutralType.FLOAT,   "real");    // 	4 bytes
	    map.put(NeutralType.DOUBLE,  "double precision"); // 8 bytes

	    map.put(NeutralType.BOOLEAN, "boolean");
	    map.put(NeutralType.BYTE,    "char(1)");
	    map.put(NeutralType.BINARY,  "bytea");
	    
	    map.put(NeutralType.DATE,      "date");
	    map.put(NeutralType.TIME,      "time");
	    map.put(NeutralType.TIMESTAMP, "timestamp");
	    
	    return map;
	}
	
	private static Map<String, String> typesMYSQL() {
	    Map<String,String> map = new HashMap<String,String>();
	    map.put(NeutralType.STRING,  "VARCHAR(%)");
	    
	    map.put(NeutralType.SHORT,   "SMALLINT"); // SMALLINT[(M)] [UNSIGNED] [ZEROFILL]
	    // A small integer. The signed range is -32768 to 32767. The unsigned range is 0 to 65535. 
	    		
	    map.put(NeutralType.INTEGER, "INT"); // INT[(M)] [UNSIGNED] [ZEROFILL]
	    // A normal-size integer. The signed range is -2147483648 to 2147483647. The unsigned range is 0 to 4294967295.
	    // INTEGER  is a synonym for INT. 
	    
	    map.put(NeutralType.LONG,    "BIGINT"); // BIGINT[(M)] [UNSIGNED] [ZEROFILL]
	    // A large integer. The signed range is -9223372036854775808 to 9223372036854775807. 
	    // The unsigned range is 0 to 18446744073709551615. 
	    
	    map.put(NeutralType.DECIMAL, "DECIMAL(%)"); // DECIMAL[(M[,D])] [UNSIGNED] [ZEROFILL]
	    // DEC NUMERIC FIXED are synomyms
	    
	    map.put(NeutralType.FLOAT,   "FLOAT(%)"); // FLOAT[(M,D)] [UNSIGNED] [ZEROFILL]
	    
	    map.put(NeutralType.DOUBLE,  "DOUBLE"); // DOUBLE[(M,D)] [UNSIGNED] [ZEROFILL]
	    // "DOUBLE PRECISION" and "REAL" are synonyms for DOUBLE.

	    map.put(NeutralType.BOOLEAN, "BOOLEAN"); // BOOL, BOOLEAN synonyms for TINYINT(1). 
	    //A value of zero is considered false. Nonzero values are considered true
	    
	    map.put(NeutralType.BYTE,    "TINYINT");
	    // TINYINT A very small integer. The signed range is -128 to 127. The unsigned range is 0 to 255.
	    
	    map.put(NeutralType.BINARY,  "BLOB");
	    
	    map.put(NeutralType.DATE,      "DATE");
	    map.put(NeutralType.TIME,      "TIME");
	    map.put(NeutralType.TIMESTAMP, "TIMESTAMP");
	    
	    return map;
	}
	
	private static Map<String, String> typesORACLE() {
	    Map<String,String> map = new HashMap<String,String>();
	    map.put(NeutralType.STRING,  "VARCHAR2(%)");
	    
	    map.put(NeutralType.SHORT,   "SMALLINT"); // alias for "SMALLINT" ANSI data type = NUMBER(38)
	    map.put(NeutralType.INTEGER, "INT");      // alias for "INT" ANSI data type = NUMBER(38)
	    map.put(NeutralType.LONG,    "INT");      // alias for "INT" ANSI data type = NUMBER(38)
	    
	    map.put(NeutralType.DECIMAL, "NUMBER(%)");
	    map.put(NeutralType.DOUBLE,  "NUMBER(%)");
	    map.put(NeutralType.FLOAT,   "NUMBER(%)");

	    map.put(NeutralType.BOOLEAN, "CHAR(1)");
	    map.put(NeutralType.BYTE,    "CHAR(1)");
	    map.put(NeutralType.BINARY,  "BLOB");
	    
	    map.put(NeutralType.DATE,      "DATE");
	    map.put(NeutralType.TIME,      "DATE");
	    map.put(NeutralType.TIMESTAMP, "DATE");
	    
	    return map;
	}
	
}