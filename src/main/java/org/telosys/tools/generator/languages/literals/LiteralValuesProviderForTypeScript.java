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
import java.util.Map;

import org.telosys.tools.generator.context.AttributeInContext;
import org.telosys.tools.generator.languages.types.AttributeTypeInfo;
import org.telosys.tools.generator.languages.types.LanguageType;
import org.telosys.tools.generic.model.types.NeutralType;

/**
 * Literal values provider for "TYPESCRIPT" language <br>
 * See https://www.typescriptlang.org/docs/handbook/basic-types.html <br>
 * 
 * @author Laurent GUERIN
 *
 */
public class LiteralValuesProviderForTypeScript extends LiteralValuesProvider {
	
	private static final String NULL_LITERAL  = "null" ; 
	private static final String TRUE_LITERAL  = "true" ; 
	private static final String FALSE_LITERAL = "false" ; 
	private static final String EMPTY_STRING_LITERAL = "''" ; 

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
		
		// For "TypeScript", the "neutral type" is the only information available in "LanguageType"
		String neutralType = languageType.getNeutralType(); 
		
		//--- STRING
		if ( NeutralType.STRING.equals(neutralType) ) {
			String value = buildStringValue(maxLength, step);
			// In TypeScript (and JavaScript), you can use either single (') or double (") quotes for string literals — both are valid and functionally identical
			// Many developers and tools (like ESLint + Prettier) prefer 'single quotes' for strings
			return new LiteralValue("'" + value + "'", value) ;			
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
		else if (  NeutralType.FLOAT.equals(neutralType) 
				|| NeutralType.DOUBLE.equals(neutralType) 
				|| NeutralType.DECIMAL.equals(neutralType) ) {
			BigDecimal value = buildDecimalValue(neutralType, step);
			return new LiteralValue(value.toString(), value) ; // eg :  123.77
		}

		//--- BOOLEAN
		else if ( NeutralType.BOOLEAN.equals(neutralType)  ) {
			boolean value = buildBooleanValue(step);
			return new LiteralValue(value ? TRUE_LITERAL : FALSE_LITERAL, Boolean.valueOf(value)) ;
		}
		
		//--- Temporal types 
		else if ( NeutralType.DATE.equals(neutralType) ) {
			String dateISO = buildDateISO(step) ; // "2001-06-22"  ( time is "00:00:00" )
			return new LiteralValue(buildNewDateString(dateISO), dateISO );
		}
		else if ( NeutralType.TIME.equals(neutralType) || NeutralType.TIMETZ.equals(neutralType) ) {
			// NB: cannot initialize a JavaScript/TypeScript Date object using only a time string like "12:34:56" or "12:34:56Z" (it will result in an invalid Date)
			// JavaScript’s Date constructor requires a full date-time string 
			// For "time-only" store it in a Date and use getHours(), getMinutes(), getSeconds() and getTime() (rem: getTimezoneOffset() allways return -60 => do not use)
			String dateTimeISO = buildDateTimeISO(step, "1970-01-01"); // "1970-01-01Txx:xx:xx"
			return new LiteralValue(buildNewDateString(dateTimeISO), dateTimeISO );
		}
		else if ( NeutralType.TIMESTAMP.equals(neutralType) || NeutralType.DATETIME.equals(neutralType) ) {
			String dateTimeISO = buildDateTimeISO(step); // "2017-11-15T08:22:12"
			return new LiteralValue(buildNewDateString(dateTimeISO), dateTimeISO );
		}
		else if ( NeutralType.DATETIMETZ.equals(neutralType) ) {
			// NB: the built-in Date object doesn't retain the original time zone offset — it parses it, converts to UTC, and forgets it.
			// Offset conversion changes the original Hour/Min ! => do not use Offset
			String value = buildDateTimeISO(step); // Basic date-time ISO  "2017-11-15T08:22:12"
			return new LiteralValue(buildNewDateString(value), value );
		}

		//--- UUID - In TypeScript, there is no built-in primitive UUID type => use string 
		else if ( NeutralType.UUID.equals(neutralType)  ) {
			String value = buildUUID() ;
			// In TypeScript (and JavaScript), you can use either single (') or double (") quotes for string literals — both are valid and functionally identical
			// Many developers and tools (like ESLint + Prettier) prefer 'single quotes' for strings
			return new LiteralValue("'" + value + "'", value );
		}

		//--- BINARY
		else if ( NeutralType.BINARY.equals(neutralType)  ) {
			// empty Uint8Array
			return new LiteralValue("new Uint8Array(0)", Integer.valueOf(0) ); 
		}
		
		return new LiteralValue(NULL_LITERAL, null);
	}
	private String buildNewDateString(String dateValue) {
		// In TypeScript (and JavaScript), you can use either single (') or double (") quotes for string literals — both are valid and functionally identical
		// Many developers and tools (like ESLint + Prettier) prefer 'single quotes' for strings
		return "new Date('" + dateValue + "')";
	}
	
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
	
	private static final String NEW_DATE_ZERO = "new Date(0)" ; // Date with 'zero-value' = "1970-01-01T00:00:00.000Z"
	private static final Map<String,String> notNullInitValues = new HashMap<>();
	static {
		notNullInitValues.put(NeutralType.STRING,  EMPTY_STRING_LITERAL);  // string / String 
		notNullInitValues.put(NeutralType.BOOLEAN, FALSE_LITERAL);  // boolean / Boolean
		notNullInitValues.put(NeutralType.BYTE,    "0");  // number, Number
		notNullInitValues.put(NeutralType.SHORT,   "0");  // number, Number 
		notNullInitValues.put(NeutralType.INTEGER, "0");  // number, Number 
		notNullInitValues.put(NeutralType.LONG,    "0");  // number, Number 
		notNullInitValues.put(NeutralType.FLOAT,   "0");  // number, Number
		notNullInitValues.put(NeutralType.DOUBLE,  "0");  // number, Number
		notNullInitValues.put(NeutralType.DECIMAL, "0");  // number, Number

		notNullInitValues.put(NeutralType.DATE,       NEW_DATE_ZERO);  // Zero-value: "1970-01-01T00:00:00.000Z"
		notNullInitValues.put(NeutralType.TIME,       NEW_DATE_ZERO); 
		notNullInitValues.put(NeutralType.TIMETZ,     NEW_DATE_ZERO);  
		notNullInitValues.put(NeutralType.TIMESTAMP,  NEW_DATE_ZERO);  
		notNullInitValues.put(NeutralType.DATETIME,   NEW_DATE_ZERO);  
		notNullInitValues.put(NeutralType.DATETIMETZ, NEW_DATE_ZERO);  
		
		notNullInitValues.put(NeutralType.UUID, 	  "'" + UUID_ZERO_VALUE_STRING + "'" );   	
		notNullInitValues.put(NeutralType.BINARY,     "new Uint8Array(0)" ); // empty Uint8Array   	
	}
//	@Override
//	public String getInitValue(AttributeInContext attribute, LanguageType languageType) {
//		return getInitValue(languageType.getNeutralType(), attribute.isNotNull());
//	}
//
////	@Override
//	private String getInitValue(String neutralType, boolean notNull) {
//		if (notNull) {
//			// not null attribute
//			String defaultValue = notNullInitValues.get(neutralType);
//			return defaultValue != null ? defaultValue : NULL_LITERAL;
//		} else {
//			// nullable attribute
//			return NULL_LITERAL;
//		}
//	}

	@Override
	protected String getInitValue(AttributeTypeInfo attributeTypeInfo, LanguageType languageType) {
		if (attributeTypeInfo.isNotNull()) {
			// not null attribute
			String defaultValue = notNullInitValues.get(languageType.getNeutralType());
			return defaultValue != null ? defaultValue : NULL_LITERAL;
		} else {
			// nullable attribute
			return NULL_LITERAL;
		}
	}
}
