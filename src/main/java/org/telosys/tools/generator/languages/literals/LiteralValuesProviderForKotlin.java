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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.telosys.tools.generator.context.AttributeInContext;
import org.telosys.tools.generator.languages.types.LanguageType;
import org.telosys.tools.generator.languages.types.TypeConverterForKotlin;
import org.telosys.tools.generic.model.types.NeutralType;

/**
 * Literal values provider for "Kotlin" language
 * 
 * @author Laurent GUERIN
 *
 */
public class LiteralValuesProviderForKotlin extends LiteralValuesProvider {
	
	private static final String NULL_LITERAL  = "null" ; 
	private static final String TRUE_LITERAL  = "true" ; 
	private static final String FALSE_LITERAL = "false" ; 
	private static final String EMPTY_STRING_LITERAL = "\"\"" ; 
	
	private static final String[] SIGNED_TYPES_ARRAY = new String[] { 
			TypeConverterForKotlin.KOTLIN_BYTE,     TypeConverterForKotlin.KOTLIN_SHORT,     TypeConverterForKotlin.KOTLIN_INT,     TypeConverterForKotlin.KOTLIN_LONG,
			TypeConverterForKotlin.KOTLIN_BYTE+"?", TypeConverterForKotlin.KOTLIN_SHORT+"?", TypeConverterForKotlin.KOTLIN_INT+"?", TypeConverterForKotlin.KOTLIN_LONG+"?"};

	private static final String[] UNSIGNED_TYPES_ARRAY = new String[] { 
			TypeConverterForKotlin.KOTLIN_UBYTE,     TypeConverterForKotlin.KOTLIN_USHORT,     TypeConverterForKotlin.KOTLIN_UINT,     TypeConverterForKotlin.KOTLIN_ULONG,
			TypeConverterForKotlin.KOTLIN_UBYTE+"?", TypeConverterForKotlin.KOTLIN_USHORT+"?", TypeConverterForKotlin.KOTLIN_UINT+"?", TypeConverterForKotlin.KOTLIN_ULONG+"?"};

	private static final Set<String> SIGNED_TYPES   = new HashSet<>(Arrays.asList(SIGNED_TYPES_ARRAY));
	private static final Set<String> UNSIGNED_TYPES = new HashSet<>(Arrays.asList(UNSIGNED_TYPES_ARRAY));

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
		String simpleType = languageType.getSimpleType(); 
		
		//--- STRING
		if ( NeutralType.STRING.equals(neutralType) ) {
			String value = buildStringValue(maxLength, step);
			return new LiteralValue("\"" + value + "\"", value) ;			
		}
		
		//--- STANDARD INTEGERS ( signed integer nullable or not : Short, Short?, Long, Long? )=> NO SUFIX
		else if ( SIGNED_TYPES.contains(simpleType) ) {
			Long value = buildIntegerValue(neutralType, step);  
			return new LiteralValue(value.toString(), value) ; // eg : 123
		}
		
		//--- UNSIGNED INTEGERS ( unsigned integer nullable or not : UShort, UShort?, ULong, ULong?  ) => WITH "u" SUFIX		
		else if ( UNSIGNED_TYPES.contains(simpleType) ) {
			Long value = buildIntegerValue(neutralType, step);  
			return new LiteralValue(value.toString() + "u", value) ; // eg : 123u
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

		//--- DATE & TIME : "java.time.*"  
		else if ( NeutralType.DATE.equals(neutralType)  ) {
			String dateISO = buildDateISO(step) ; // "2001-06-22" 
			return new LiteralValue("java.time.LocalDate.parse(\"" + dateISO + "\")", dateISO );
		}
		else if ( NeutralType.TIME.equals(neutralType)  ) {
			String timeISO = buildTimeISO(step) ; // "15:46:52"
			return new LiteralValue("java.time.LocalTime.parse(\"" + timeISO + "\")", timeISO );
		}
		else if ( NeutralType.DATETIME.equals(neutralType) || NeutralType.TIMESTAMP.equals(neutralType) ) { // ver 4.3.0
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
		//--- Noting for BINARY		
		return new LiteralValue(NULL_LITERAL, null);
	}
	
	/* 
	 * Returns something like that : 
	 *   ' == 100' 
	 *   '.equals("xxx")'
	 */
	@Override
	public String getEqualsStatement(String value, LanguageType languageType) {

		// Always "==" ( whatever the type ?? ) 
		return " == " + value ;
	}
	
	private static final Map<String,String> notNullInitValues = new HashMap<>();	
	static {
		notNullInitValues.put(NeutralType.STRING,  EMPTY_STRING_LITERAL);  
		notNullInitValues.put(NeutralType.BOOLEAN, FALSE_LITERAL); 
		notNullInitValues.put(NeutralType.BYTE,    "0" );  
		notNullInitValues.put(NeutralType.SHORT,   "0" );  
		notNullInitValues.put(NeutralType.INTEGER, "0" );  
		notNullInitValues.put(NeutralType.LONG,    "0L" );  // 'L' suffix for Long
		notNullInitValues.put(NeutralType.FLOAT,   "0.0F" );  // 'F' or 'f' suffix
		notNullInitValues.put(NeutralType.DOUBLE,  "0.0"  );  // no suffix 
		notNullInitValues.put(NeutralType.DECIMAL, "BigDecimal.ZERO" );  // BigDecimal (java.math.BigDecimal)

		notNullInitValues.put(NeutralType.DATE,       "LocalDate.now()"); 
		notNullInitValues.put(NeutralType.TIME,       "LocalTime.now()"); 
		notNullInitValues.put(NeutralType.TIMESTAMP,  "LocalDateTime.now()"); 
		notNullInitValues.put(NeutralType.DATETIME,   "LocalDateTime.now()");  // v 4.3.0
		notNullInitValues.put(NeutralType.DATETIMETZ, "OffsetDateTime.now()"); // v 4.3.0
		notNullInitValues.put(NeutralType.TIMETZ,     "OffsetTime.now()");     // v 4.3.0

		notNullInitValues.put(NeutralType.UUID, 	 "UUID(0L,0L)"); // v 4.3.0  (Kotlin syntax != Java syntax, no "new" keyword)
		
		notNullInitValues.put(NeutralType.BINARY,    "ByteArray(0)"); // ByteArray (Kotlin syntax != Java syntax)
	}
	@Override
	public String getInitValue(AttributeInContext attribute, LanguageType languageType) {
//		if ( attribute.isNotNull() ) {
//			// not null attribute 
//			String initValue = notNullInitValues.get(languageType.getNeutralType());
//			return initValue != null ? initValue : NULL_LITERAL ; 
//		} else {
//			// nullable attribute
//			return NULL_LITERAL;
//		}
		// In Kotlin, primitive types cannot be null by default
		return getInitValue(languageType.getNeutralType(), attribute.isNotNull() || languageType.isPrimitiveType() );
	}
	@Override
	public String getInitValue(String neutralType, boolean notNull) {
		if (notNull) {
			// not null attribute
			String defaultValue = notNullInitValues.get(neutralType);
			return defaultValue != null ? defaultValue : NULL_LITERAL ; 
		} else {
			// nullable attribute
			return NULL_LITERAL;
		}
	}

}
