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
package org.telosys.tools.generator.languages.literals;

import java.math.BigDecimal;

import org.telosys.tools.generator.languages.types.LanguageType;
import org.telosys.tools.generator.languages.types.TypeConverterForJava;
import org.telosys.tools.generic.model.types.NeutralType;

/**
 * Literal values provider for "JAVA" language
 * 
 * @author Laurent GUERIN
 *
 */
public class LiteralValuesProviderForJava extends LiteralValuesProvider {
	
	private static final String NULL_LITERAL  = "null" ; // null in Java
	private static final String TRUE_LITERAL  = "true" ; 
	private static final String FALSE_LITERAL = "false" ; 
	
	@Override
	public String getLiteralNull() {
		return NULL_LITERAL;
	}
	
	@Override
	public String getLiteralTrue() {
		return TRUE_LITERAL;
	}

	@Override
	public String getLiteralFalse() {
		return FALSE_LITERAL;
	}
			
	@Override
	public LiteralValue generateLiteralValue(LanguageType languageType, int maxLength, int step) {
		
		String javaFullType = languageType.getFullType() ;
		//--- STRING
		if ( java.lang.String.class.getCanonicalName().equals(javaFullType) ) {
			String value = buildStringValue(maxLength, step);
			return new LiteralValue("\"" + value + "\"", value) ;			
		}
		
		//--- BYTE
		else if ( byte.class.getCanonicalName().equals(javaFullType) ) {
			Long value = Long.valueOf(checkThreshold(step, Byte.MAX_VALUE)) ;  
			return new LiteralValue("(byte)" + value.toString(), value) ; // eg : (byte)123
		}
		else if ( java.lang.Byte.class.getCanonicalName().equals(javaFullType) ) {
			Long value = Long.valueOf(checkThreshold(step, Byte.MAX_VALUE)) ;  
			return new LiteralValue("Byte.valueOf((byte)" + value.toString() + ")", value) ; // eg : Byte.valueOf((byte)123)
		}
		
		//--- SHORT
		else if ( short.class.getCanonicalName().equals(javaFullType) ) {
			Long value = Long.valueOf(checkThreshold(step, Short.MAX_VALUE)) ;  
			return new LiteralValue("(short)" + value.toString(), value) ;
		}
		else if ( java.lang.Short.class.getCanonicalName().equals(javaFullType) ) {
			Long value = Long.valueOf(checkThreshold(step, Short.MAX_VALUE)) ;  
			return new LiteralValue("Short.valueOf((short)" + value.toString() + ")", value) ; // eg : Short.valueOf((short)123)
		}
		
		//--- INT		
		else if ( int.class.getCanonicalName().equals(javaFullType) ) {
			Long value = Long.valueOf(step*100L) ;  
			return new LiteralValue(value.toString(), value) ;
			
		}
		else if ( java.lang.Integer.class.getCanonicalName().equals(javaFullType) ) {
			Long value = Long.valueOf(step*100L) ;  
			return new LiteralValue("Integer.valueOf(" + value.toString() + ")", value) ; // eg : Integer.valueOf(123)
		}
		
		//--- LONG
		else if ( long.class.getCanonicalName().equals(javaFullType) ) {
			Long value = Long.valueOf(step*1000L) ;  
			return new LiteralValue(value.toString() + "L", value) ; // eg : 123L
		}
		else if ( java.lang.Long.class.getCanonicalName().equals(javaFullType) ) {
			Long value = Long.valueOf(step*1000L) ;  
			return new LiteralValue("Long.valueOf(" + value.toString() + "L)", value) ; // eg :  Long.valueOf(123L)
		}
		
		//--- FLOAT
		else if ( float.class.getCanonicalName().equals(javaFullType) ) {
			BigDecimal value = BigDecimal.valueOf((step * 1000) + 0.5);
			return new LiteralValue(value.toString()+"F", value) ; //  eg :  123.5F
		}
		else if ( java.lang.Float.class.getCanonicalName().equals(javaFullType) ) {
			BigDecimal value = BigDecimal.valueOf((step * 1000) + 0.5);
			return new LiteralValue("Float.valueOf(" + value.toString() + "F)", value) ; //  eg :  Float.valueOf(123.5F)
		}
		
		//--- DOUBLE
		else if ( double.class.getCanonicalName().equals(javaFullType) ) {
			BigDecimal value = BigDecimal.valueOf((step * 1000) + 0.66);
			return new LiteralValue(value.toString()+"D", value) ; // eg :  123.66D
		}
		else if ( java.lang.Double.class.getCanonicalName().equals(javaFullType) ) {
			BigDecimal value = BigDecimal.valueOf((step * 1000) + 0.66);
			return new LiteralValue("Double.valueOf("+value.toString()+"D)", value) ; // eg : Double.valueOf(123.66D)
		}
		
		//--- BIG DECIMAL
		else if ( java.math.BigDecimal.class.getCanonicalName().equals(javaFullType) ) {
			BigDecimal value = BigDecimal.valueOf((step * 10000) + 0.77);
			return new LiteralValue("java.math.BigDecimal.valueOf(" + value.toString() + ")", value) ; // eg : java.math.BigDecimal.valueOf(15000.77)
		}
		
		//--- BOOLEAN
		else if ( boolean.class.getCanonicalName().equals(javaFullType) ) {
			// Primitive boolean type
			boolean value = step % 2 != 0 ;
			return new LiteralValue(value ? TRUE_LITERAL : FALSE_LITERAL, Boolean.valueOf(value)) ;
		}
		else if ( java.lang.Boolean.class.getCanonicalName().equals(javaFullType) ) {
			boolean value = step % 2 != 0 ;
			String s = value ? TRUE_LITERAL : FALSE_LITERAL ;
			return new LiteralValue("Boolean.valueOf("+s+")", Boolean.valueOf(value)) ; // eg : Boolean.valueOf(true)
		}
		
		// DATE & TIME : OLD "java.util.Date" ( not supposed to be used since v 3.4.0 ) 
		else if ( java.util.Date.class.getCanonicalName().equals(javaFullType) ) {
			String neutralType = languageType.getNeutralType();
			// A standard "java.util.Date" can be used to store Date/Time/Timestamp
			if ( NeutralType.DATE.equals(neutralType) ) {
				return generateSqlDateValue(step);
			}
			else if ( NeutralType.TIME.equals(neutralType) ) {
				return generateSqlTimeValue(step);
			}
			else if ( NeutralType.TIMESTAMP.equals(neutralType) ) {
				return generateSqlTimestampValue(step);
			}
			else {
				// by default generate a DATE
				return generateSqlDateValue(step);
			}
		}
		// DATE & TIME : "java.time.*"  (NEW since v 3.4.0 )
		else if ( TypeConverterForJava.LOCAL_DATE_CLASS.equals(javaFullType) ) {
			return generateLocalDateValue(step);
		}
		else if ( TypeConverterForJava.LOCAL_TIME_CLASS.equals(javaFullType) ) {
			return generateLocalTimeValue(step);
		}
		else if ( TypeConverterForJava.LOCAL_DATE_TIME_CLASS.equals(javaFullType) ) {
			return generateLocalDateTimeValue(step);
		}
		// DATE & TIME : "java.sql.*"
		else if ( java.sql.Date.class.getCanonicalName().equals(javaFullType) ) {
			return generateSqlDateValue(step);
		}
		else if ( java.sql.Time.class.getCanonicalName().equals(javaFullType) ) {
			return generateSqlTimeValue(step);
		}
		else if ( java.sql.Timestamp.class.getCanonicalName().equals(javaFullType) ) {
			return generateSqlTimestampValue(step);
		}
		
		return new LiteralValue(NULL_LITERAL, null);
	}
	
	private String generateYearValue(int step) {
		int year = 2000 + ( step % 1000 ) ;  // between 2000 and 2999 
		return "" + year ;
	}
	private String generateHourValue(int step) {
		int hour = step % 24 ;
		return String.format("%02d", hour) ; // between 0 and 23		
	}
	//--- SQL date/time/timestamp
	private LiteralValue generateSqlDateValue(int step) {
		String dateISO = generateYearValue(step) + "-06-22" ; // "2001-06-22" 
		java.sql.Date value = java.sql.Date.valueOf(dateISO);
		return new LiteralValue("java.sql.Date.valueOf(\"" + dateISO + "\")", value) ; 
	}
	private LiteralValue generateSqlTimeValue(int step) {
		String timeISO =  String.format("%02d", (step%24) ) + ":46:52" ; // "15:46:52"
		java.sql.Time value = java.sql.Time.valueOf(timeISO);
		return new LiteralValue("java.sql.Time.valueOf(\"" + timeISO + "\")", value) ; 
	}
	private LiteralValue generateSqlTimestampValue(int step) {
		String timestampISO = generateYearValue(step) + "-05-21" + " " + String.format("%02d", (step%24) ) + ":46:52" ; // "2001-05-21 15:46:52" 
		java.sql.Timestamp value = java.sql.Timestamp.valueOf(timestampISO);
		return new LiteralValue("java.sql.Timestamp.valueOf(\"" + timestampISO + "\")", value) ; 
	}
	private LiteralValue generateLocalDateValue(int step) { // v 3.4.0
		String dateISO = generateYearValue(step) + "-06-22" ; // "2001-06-22" 
		return new LiteralValue("java.time.LocalDate.parse(\"" + dateISO + "\")", dateISO );
	}
	private LiteralValue generateLocalTimeValue(int step) { // v 3.4.0
		String timeISO = generateHourValue(step) + ":46:52" ; // "15:46:52"
		return new LiteralValue("java.time.LocalTime.parse(\"" + timeISO + "\")", timeISO );
	}
	private LiteralValue generateLocalDateTimeValue(int step) { // v 3.4.0
		// expected : LocalDateTime.parse("2017-11-15T08:22:12")
		String timestampISO = 
				generateYearValue(step) + "-05-21" 
				+ "T" 
				+ generateHourValue(step) + ":46:52" ; // "2001-05-21T15:46:52" 
		return new LiteralValue("java.time.LocalDateTime.parse(\"" + timestampISO + "\")", timestampISO );
	}

	/* 
	 * Returns something like that : 
	 *   ' == 100' 
	 *   '.equals("xxx")'
	 */
	@Override
	public String getEqualsStatement(String value, LanguageType languageType) {
		
		if ( languageType.isPrimitiveType() ) {
			// equals for primitive type
			StringBuilder sb = new StringBuilder();
			sb.append( " == " );
			sb.append( value );
			return sb.toString();
		}
		else {
			// equals for object instance
			StringBuilder sb = new StringBuilder();
			sb.append( ".equals(" );
			sb.append( value );
			sb.append( ")" );
			return sb.toString();
		}
	}

//	private String buildStringValue(int maxLength, int step) {
//		int maxLimit = 100 ;
//		// 'A'-'Z' : 65-90 
//		// 'a'-'z' : 97-122 
//		char c = 'A' ; 
//		if ( step > 0 ) {
//			int delta = (step-1) % 26;
//			c = (char)('A' + delta );
//		}
//		StringBuilder sb = new StringBuilder();
//		for ( int i = 0 ; i < maxLength && i < maxLimit ; i++) {
//			sb.append(c);
//		}
//		return sb.toString();
//	}
	@Override
	public String getDefaultValueNotNull(LanguageType languageType) {
		String type = languageType.getSimpleType();
//		String defaultValue = defaultValues.get(type);
		String defaultValue = null;
		return defaultValue != null ? defaultValue : "(unknown type '" + type + "')" ; 
	}

}
