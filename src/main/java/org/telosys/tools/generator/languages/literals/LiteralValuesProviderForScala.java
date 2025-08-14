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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.telosys.tools.generator.context.AttributeInContext;
import org.telosys.tools.generator.languages.types.AttributeTypeInfo;
import org.telosys.tools.generator.languages.types.LanguageType;
import org.telosys.tools.generic.model.types.NeutralType;

/**
 * Literal values provider for "SCALA" language
 * 
 * @author Laurent GUERIN
 *
 */
public class LiteralValuesProviderForScala extends LiteralValuesProvider {
	
	private static final String NULL_LITERAL  = "null" ;   // acceptable to the compiler but discouraged
	// "null" is not supposed to be used in Scala
	// see https://docs.scala-lang.org/overviews/scala-book/no-null-values.html
	// Scala’s solution is to use constructs like the Option/Some/None classes
	// "None" y: Option[Int] = None
	
	private static final String TRUE_LITERAL  = "true" ;   // Example : b1: Boolean = true 
	private static final String FALSE_LITERAL = "false" ;  // Example : b2: Boolean = false 
	private static final String EMPTY_STRING_LITERAL = "\"\"" ; 
	
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
		
		String neutralType = languageType.getNeutralType(); 

		//--- STRING
		if ( NeutralType.STRING.equals(neutralType) ) {
			String value = buildStringValue(maxLength, step);
			return new LiteralValue("\"" + value + "\"", value) ;			
		}
		
		//--- NUMBER / INTEGER
		else if (  NeutralType.BYTE.equals(neutralType) 
				|| NeutralType.SHORT.equals(neutralType)
				|| NeutralType.INTEGER.equals(neutralType) 
				|| NeutralType.LONG.equals(neutralType) ) {
			Long value = buildIntegerValue(neutralType, step);  
			return new LiteralValue(value.toString(), value) ; // eg : 123
		}
		
		//--- NUMBER (NOT INTEGER)
		else if ( NeutralType.FLOAT.equals(neutralType)  ) {
			BigDecimal value = buildDecimalValue(neutralType, step);
			return new LiteralValue(value.toString() + "f", value) ; // + "f"
		}
		else if ( NeutralType.DOUBLE.equals(neutralType) ) {
			BigDecimal value = buildDecimalValue(neutralType, step);
			return new LiteralValue(value.toString(), value) ; // no suffix
		} 
		else if ( NeutralType.DECIMAL.equals(neutralType) ) {
			BigDecimal value = buildDecimalValue(neutralType, step);
			return new LiteralValue("java.math.BigDecimal.valueOf(" + value.toString() + ")", value) ; // eg : java.math.BigDecimal.valueOf(15000.77)
		}

		//--- BOOLEAN
		else if ( NeutralType.BOOLEAN.equals(neutralType)  ) {
			boolean value = buildBooleanValue(step);
			return new LiteralValue(value ? TRUE_LITERAL : FALSE_LITERAL, Boolean.valueOf(value)) ;
		}

		//--- DATE, TIME and TIMESTAMP : 
		else if ( NeutralType.DATE.equals(neutralType)  ) {
			String dateISO = buildDateISO(step) ; // "2001-06-22" 
			return new LiteralValue("java.time.LocalDate.parse(\"" + dateISO + "\")", dateISO );
		}
		else if ( NeutralType.TIME.equals(neutralType)  ) {
			String timeISO = buildTimeISO(step) ; // "15:46:52"
			return new LiteralValue("java.time.LocalTime.parse(\"" + timeISO + "\")", timeISO );
		}
		else if ( NeutralType.DATETIME.equals(neutralType) || NeutralType.TIMESTAMP.equals(neutralType)  ) { // DATETIME ver 4.3.0
			String dateTimeISO = buildDateTimeISO(step); // "2017-11-15T08:22:12"
			return new LiteralValue("java.time.LocalDateTime.parse(\"" + dateTimeISO + "\")", dateTimeISO );
		}
		else if ( NeutralType.DATETIMETZ.equals(neutralType) ) { // ver 4.3.0
			String value = buildDateTimeWithOffsetISO(step); 
			return new LiteralValue("java.time.OffsetDateTime.parse(\"" + value + "\")", value );
		}
		else if ( NeutralType.TIMETZ.equals(neutralType) ) { // ver 4.3.0
			String value = buildTimeWithOffsetISO(step);
			return new LiteralValue("java.time.OffsetTime.parse(\"" + value + "\")", value );
		}
		//--- UUID
		else if ( NeutralType.UUID.equals(neutralType) ) { // ver 4.3.0
			String uuidString = buildUUID(); 
			return new LiteralValue("java.util.UUID.fromString(\""+uuidString+"\")", uuidString);
		}		
		//--- No literal value for BINARY
		
		return new LiteralValue(NULL_LITERAL, null);
	}
	
//	private int generateYearValue(int step) {
//		return 2000 + ( step % 1000 ) ;  // between 2000 and 2999 
//	}
//	private LiteralValue generateJavaLocalDateLiteralValue(int step) {
//		int year = generateYearValue(step);
//		int month = 6;
//		int dayOfMonth = 22;
//		String s = "java.time.LocalDate.of(" + year + "," + month + "," + dayOfMonth + ")";
//		
//		return new LiteralValue(s, null) ; // null : no basic value for JSON, URL
//	}
//	private LiteralValue generateJavaLocalTimeValue(int step) {
//		int hour = step % 24 ;
//		int minute = 46 ;
//		int second = 52 ;
//		String s = "java.time.LocalTime.of(" + hour + "," + minute + "," + second + ")";
//		return new LiteralValue(s, null) ;  // null : no basic value for JSON, URL
//	}
//	private LiteralValue generateJavaLocalDateTimeValue(int step) {
//		int year = generateYearValue(step);
//		int month = 5;
//		int dayOfMonth = 21;
//		int hour = step % 24 ;
//		int minute = 46 ;
//		int second = 52 ;
//		String s = "java.time.LocalDateTime.of(" + year + "," + month + "," + dayOfMonth + ","
//				+ hour + "," + minute + "," + second + ")";
//		return new LiteralValue(s, null) ;  // null : no basic value for JSON, URL		
//	}

	/* 
	 * Returns something like that : 
	 *   ' == 100' 
	 *   '.equals("xxx")'
	 */
	@Override
	public String getEqualsStatement(String value, LanguageType languageType) {

		// Always "==" ( whatever the type ) 
		return " == " + value ;
	}

	/* Scala Language Specification:
		0      if T is Int or one of its subrange types,
		0L     if T is Long,
		0.0f   if T is Float,
		0.0d   if T is Double,
		false  if T is Boolean,
		()     if T is Unit,
		null for all other types T.
	 */
	private static final Map<String,String> notNullInitValues = new HashMap<>();	
	static {
		// see https://scalaexplained.github.io/scala-explained/literals.html 
		// Types derived from “AnyVal” => NOT NULLABLE
		notNullInitValues.put(NeutralType.BOOLEAN, FALSE_LITERAL); // Boolean : derived from “AnyVal” => NOT NULLABLE
		notNullInitValues.put(NeutralType.BYTE,    "0" );          // Byte    : derived from “AnyVal” => NOT NULLABLE
		notNullInitValues.put(NeutralType.SHORT,   "0" );          // Short   : derived from “AnyVal” => NOT NULLABLE
		notNullInitValues.put(NeutralType.INTEGER, "0" );          // Int     : derived from “AnyVal” => NOT NULLABLE 
		notNullInitValues.put(NeutralType.LONG,    "0L" );         // Long    : derived from “AnyVal” => NOT NULLABLE ('L' or 'l' suffix )
		notNullInitValues.put(NeutralType.FLOAT,   "0.0F" );       // Float   : derived from “AnyVal” => NOT NULLABLE ('F' or 'f' suffix )
		notNullInitValues.put(NeutralType.DOUBLE,  "0.0D" );       // Double  : derived from “AnyVal” => NOT NULLABLE ('D' or 'd' suffix )
		
		// Types derived from “AnyRef” => NULLABLE
		notNullInitValues.put(NeutralType.STRING,     EMPTY_STRING_LITERAL);   //                           NULLABLE
		notNullInitValues.put(NeutralType.DECIMAL,    "BigDecimal(0.0)" );     // scala.math.BigDecimal   : NULLABLE
		notNullInitValues.put(NeutralType.DATE,       "LocalDate.now()");      // java.time.LocalDate     : NULLABLE
		notNullInitValues.put(NeutralType.TIME,       "LocalTime.now()");      // java.time.LocalTime     : NULLABLE
		notNullInitValues.put(NeutralType.TIMESTAMP,  "LocalDateTime.now()");  // java.time.LocalDateTime : NULLABLE
		notNullInitValues.put(NeutralType.DATETIME,   "LocalDateTime.now()");  // v 4.3.0
		notNullInitValues.put(NeutralType.DATETIMETZ, "OffsetDateTime.now()"); // v 4.3.0
		notNullInitValues.put(NeutralType.TIMETZ,     "OffsetTime.now()");     // v 4.3.0
		notNullInitValues.put(NeutralType.UUID, 	  "new UUID(0L,0L)");      // v 4.3.0  ("new" keyword required in Scala)		
		notNullInitValues.put(NeutralType.BINARY,     "Array.empty[Byte]");    // empty byte array        : NULLABLE		
	}
	private static final Set<String> notNullableScalaTypes = new HashSet<>();
	static {
		// Scala types derived from “AnyVal” are NOT NULLABLE
		notNullableScalaTypes.add("Boolean");
		notNullableScalaTypes.add("Byte");
		notNullableScalaTypes.add("Short");
		notNullableScalaTypes.add("Int");
		notNullableScalaTypes.add("Long");
		notNullableScalaTypes.add("Float");
		notNullableScalaTypes.add("Double");
	}
	private boolean isNotNullable(LanguageType languageType) {
		return notNullableScalaTypes.contains(languageType.getSimpleType());
	}
	
//	@Override
//	public String getInitValue(AttributeInContext attribute, LanguageType languageType) {
//		return getInitValue(attribute.getAttributeTypeInfo(), languageType);
//	}

	// in Scala "_" initialises means "default value" when used in variable initialization
	private static final String SCALA_DEFAULT_VALUE = "_"; 

	@Override
	protected String getInitValue(AttributeTypeInfo attributeTypeInfo, LanguageType languageType) {
		if ( attributeTypeInfo.isNotNull() || isNotNullable(languageType) ) {
			// not null attribute
			String defaultValue = notNullInitValues.get(attributeTypeInfo.getNeutralType());
			return defaultValue != null ? defaultValue : SCALA_DEFAULT_VALUE ; 
		} else {
			// nullable attribute
			return NULL_LITERAL;
		}
	}
	
}
