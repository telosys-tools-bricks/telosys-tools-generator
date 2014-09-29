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
package org.telosys.tools.generator.context.tools;

import java.math.BigDecimal;
import java.util.Hashtable;

import org.telosys.tools.generator.context.AttributeInContext;

/**
 * xx
 * 
 * @author Laurent GUERIN
 *
 */
public class JdbcTypesMapper {
	
	private final static Hashtable<String,String> preparedStatementSetters = new Hashtable<String,String>() ;
	static {
		preparedStatementSetters.put(String.class.getCanonicalName(),  "setString" );
		
		preparedStatementSetters.put(Byte.class.getCanonicalName(),    "setByte"  );
		preparedStatementSetters.put(byte.class.getCanonicalName(),    "setByte"  );
		
		preparedStatementSetters.put(Short.class.getCanonicalName(),   "setShort" );
		preparedStatementSetters.put(short.class.getCanonicalName(),   "setShort" );
		
		preparedStatementSetters.put(Integer.class.getCanonicalName(), "setInt"   );
		preparedStatementSetters.put(int.class.getCanonicalName(),     "setInt"   );
		
		preparedStatementSetters.put(Long.class.getCanonicalName(),    "setLong"  );
		preparedStatementSetters.put(long.class.getCanonicalName(),    "setLong"  );

		preparedStatementSetters.put(Float.class.getCanonicalName(),   "setFloat"  );
		preparedStatementSetters.put(float.class.getCanonicalName(),   "setFloat"  );
		
		preparedStatementSetters.put(Double.class.getCanonicalName(),  "setDouble"  );
		preparedStatementSetters.put(double.class.getCanonicalName(),  "setDouble"  );

		// BigInteger.class.getCanonicalName() : not supported in JDBC
		preparedStatementSetters.put(BigDecimal.class.getCanonicalName(), "setBigDecimal"  );

		// NB : java.util.Date value is supposed to be converted in java.sql.Date
		preparedStatementSetters.put(java.util.Date.class.getCanonicalName(),     "setDate"  );
		
		preparedStatementSetters.put(java.sql.Date.class.getCanonicalName(),      "setDate"  );
		preparedStatementSetters.put(java.sql.Time.class.getCanonicalName(),      "setTime"  );
		preparedStatementSetters.put(java.sql.Timestamp.class.getCanonicalName(), "setTimestamp"  );
		
		preparedStatementSetters.put(Boolean.class.getCanonicalName(),  "setBoolean"  );
		preparedStatementSetters.put(boolean.class.getCanonicalName(),  "setBoolean"  );
		
		preparedStatementSetters.put(byte[].class.getCanonicalName(),   "setBytes"  );
		
	}
	
	/**
	 * No Constructor
	 */
	private JdbcTypesMapper() {
		super();
	}
	

	public static String getPreparedStatementSetter(AttributeInContext attribute) {
		
		String setter = preparedStatementSetters.get( attribute.getFullType() );
		if ( setter == null ) {
			return "setUnknown" ;
		}
		return setter ;
	}

	public static String getValueForPreparedStatement(AttributeInContext attribute, String name) {
		
		// The basic getter, e.g. 'book.getCode()' 
		String getter = name + "." + attribute.getGetter()+"()";
		if ( java.util.Date.class.getCanonicalName().equals( attribute.getFullType() ) ) {
			// The attribute Java type is "java.util.Date" => needs to be converted to java.sql.Date
			return getter + " != null ? new java.sql.Date(" + getter + ".getTime()) : null";
		}
		else {
			// Just return the simple getter 
			return getter;
		}
	}

}
