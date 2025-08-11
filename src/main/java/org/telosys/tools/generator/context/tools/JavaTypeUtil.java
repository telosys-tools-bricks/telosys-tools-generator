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

import java.util.HashSet;
import java.util.Set;

/**
 * @author Laurent GUERIN
 * 
 */
public final class JavaTypeUtil {
	
    /**
     * Private constructor
     */
    private JavaTypeUtil() {
    }
    
	//---------------------------------------------------------------------------------------
    private static final Set<String> numericTypes = new HashSet<>();
    static {
    	// integer types
    	numericTypes.add("byte");
    	numericTypes.add("java.lang.Byte");
    	numericTypes.add("short");
    	numericTypes.add("java.lang.Short");
    	numericTypes.add("int");
    	numericTypes.add("java.lang.Integer");
    	numericTypes.add("long");
    	numericTypes.add("java.lang.Long");
    	numericTypes.add("java.math.BigInteger");  // Not a Java type converted from neutral type but keep it here
    	// decimal types
    	numericTypes.add("float");
    	numericTypes.add("java.lang.Float");
    	numericTypes.add("double");
    	numericTypes.add("java.lang.Double");
    	numericTypes.add("java.math.BigDecimal");
    }
	/**
	 * @param javaType
	 * @return
	 */
	public static boolean isNumberType(String javaType) {
		return numericTypes.contains(javaType);
	}

	//---------------------------------------------------------------------------------------
    private static final Set<String> primitiveTypes = new HashSet<>();
    static {
    	primitiveTypes.add("boolean");
    	primitiveTypes.add("byte");
    	primitiveTypes.add("char");
    	primitiveTypes.add("double");
    	primitiveTypes.add("float");
    	primitiveTypes.add("int");
    	primitiveTypes.add("long");
    	primitiveTypes.add("short");
    }
    /**
     * Returns true if the given type is a Java primitive type ( int, float, boolean, ... )
     * @param javaType
     * @return
     */
    public static boolean isPrimitiveType(String javaType) {
		return primitiveTypes.contains(javaType);
	}

	//---------------------------------------------------------------------------------------
	/**
	 * @param javaType
	 * @return
	 */
	public static boolean isStringType(String javaType) {
		if ( null == javaType ) return false ;
		return "java.lang.String".equals(javaType) ;
	}
    
	//---------------------------------------------------------------------------------------
    private static final Set<String> temporalTypes = new HashSet<>();
    static {
    	temporalTypes.add("java.time.LocalDate");       // date
    	temporalTypes.add("java.time.LocalDateTime");   // datetime 
    	temporalTypes.add("java.time.LocalTime");       // time
    	temporalTypes.add("java.time.OffsetDateTime");  // datetimetz (v 4.3.0)
    	temporalTypes.add("java.time.OffsetTime");      // timetz (v 4.3.0)
    	
    	//--- Other temporal types (not from neutral type, but kept to cover all cases)
    	temporalTypes.add("java.time.ZonedDateTime");
    	temporalTypes.add("java.time.Instant");
    	temporalTypes.add("java.util.Date");
    	temporalTypes.add("java.sql.Date");
    	temporalTypes.add("java.sql.Time");
    	temporalTypes.add("java.sql.Timestamp");
    }
    /**
     * Returns true if the given type is a Java primitive type ( int, float, boolean, ... )
     * @param javaType
     * @return
     */
    public static boolean isTemporalType(String javaType) {
		return temporalTypes.contains(javaType);
	}

    //-----------------------------------------------------------------------------------
    /**
     * Returns true if the given Java type needs an import
     * @param javaType
     * @return
     */
    public static boolean needsImport(String javaType) {
    	if ( javaType == null ) return false ;
    	String s2 = javaType.trim() ;
    	//--- Contains package in the type name ?
    	if ( s2.indexOf('.') < 0 ) {
    		return false ; // no '.' in the string => no package => no import
    	}
    	else {
    		// Import needed if the package is not "java.lang" 
    		return ! s2.startsWith("java.lang."); 
    	}
    }

	//----------------------------------------------------------------------------------
	/**
	 * Returns the short type of the given type <br>
	 * e.g. : "java.lang.Boolean" returns "Boolean" ("Boolean" returns "Boolean" )
	 * @param sType : input type long or short ( "int", "String", "java.lang.String" ) 
	 * @return
	 */
	public static String shortType(String sType) {
		if ( sType != null )
		{
			String s = sType.trim();
			int i = s.lastIndexOf('.');
			if ( i >= 0 )
			{
				return s.substring(i+1);
			}
			return s ;
		}
		return null ;
	}

}