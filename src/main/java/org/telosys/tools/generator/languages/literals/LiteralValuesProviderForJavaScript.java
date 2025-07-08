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
import org.telosys.tools.generator.languages.types.LanguageType;
import org.telosys.tools.generic.model.types.NeutralType;

/**
 * Literal values provider for "JAVASCRIPT" language
 * 
 * @author Laurent GUERIN
 *
 */
public class LiteralValuesProviderForJavaScript extends LiteralValuesProvider {

	private static final String NULL_LITERAL  = "null" ; 
	private static final String TRUE_LITERAL  = "true" ; 
	private static final String FALSE_LITERAL = "false" ; 
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
		
		// For "JavaScript", the "neutral type" is the only information available in "LanguageType"
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

		//--- Temporal types: DATE, TIME, DATETIME, etc  ( "Temporal.xxx" supported by Node.js 20+ )
		else if ( NeutralType.DATETIME.equals(neutralType) || NeutralType.TIMESTAMP.equals(neutralType) ) {
			String value = buildDateTimeISO(step) ; // "2001-06-22" 
			return new LiteralValue("Temporal.PlainDateTime.from('" + value + "')", value );
		}
		else if ( NeutralType.DATETIMETZ.equals(neutralType) ) {
			// OK with :
			// Offset          '2025-07-08T14:30:00+01:00' 
			// or Offset+zone  '2025-07-08T14:30:00+01:00[Europe/London]'
			String value = buildDateTimeWithOffsetISO(step);
			return new LiteralValue("Temporal.ZonedDateTime.from('" + value + "')", value );
		}
		else if ( NeutralType.DATE.equals(neutralType)  ) {
			String value = buildDateISO(step) ; // "2001-06-22" 
			return new LiteralValue("Temporal.PlainDate.from('" + value + "')", value );
		}
		else if ( NeutralType.TIME.equals(neutralType) || NeutralType.TIMETZ.equals(neutralType) ) { // no Time with Offset/TZ
			String value = buildTimeISO(step) ; // "14:30:00" 
			return new LiteralValue("Temporal.PlainTime.from('" + value + "')", value );
		}
		
		//--- UUID - In JavaScript, there is no built-in primitive UUID type => use string 
		else if ( NeutralType.UUID.equals(neutralType)  ) {
			String value = buildUUID() ; // "2001-06-22" 
			return new LiteralValue("'" + value + "'", value );
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

		// Always "==" ( whatever the type ) 
		return " == " + value ;
	}
	
	private static final Map<String,String> notNullInitValues = new HashMap<>();	
	static {
		notNullInitValues.put(NeutralType.STRING,  EMPTY_STRING_LITERAL);  
		notNullInitValues.put(NeutralType.BOOLEAN, FALSE_LITERAL); 
		notNullInitValues.put(NeutralType.BYTE,    "0" );  
		notNullInitValues.put(NeutralType.SHORT,   "0" );  
		notNullInitValues.put(NeutralType.INTEGER, "0" );  
		notNullInitValues.put(NeutralType.LONG,    "0" );  
		notNullInitValues.put(NeutralType.FLOAT,   "0.0" );  
		notNullInitValues.put(NeutralType.DOUBLE,  "0.0" );  
		notNullInitValues.put(NeutralType.DECIMAL, "0.0" );  

		notNullInitValues.put(NeutralType.DATE,       "Temporal.PlainDate.from('0000-01-01')" ); // Year 0, Jan 1
		notNullInitValues.put(NeutralType.TIME,       "Temporal.PlainTime.from('00:00:00')"   ); // 00:00:00.000000000
		notNullInitValues.put(NeutralType.TIMETZ,     "Temporal.PlainTime.from('00:00:00')"   ); // 00:00:00.000000000
		notNullInitValues.put(NeutralType.TIMESTAMP,  "Temporal.PlainDateTime.from('0000-01-01T00:00:00')" ); 
		notNullInitValues.put(NeutralType.DATETIME,   "Temporal.PlainDateTime.from('0000-01-01T00:00:00')" ); 
		notNullInitValues.put(NeutralType.DATETIMETZ, "Temporal.ZonedDateTime.from('0000-01-01T00:00:00+00:00[UTC]')" ); 
		
		notNullInitValues.put(NeutralType.UUID,  EMPTY_STRING_LITERAL);
		
		// nothing for NeutralType BINARY 
	}
	@Override
	public String getInitValue(AttributeInContext attribute, LanguageType languageType) {
		if ( attribute.isNotNull() ) {
			// not null attribute 
			String initValue = notNullInitValues.get(languageType.getNeutralType());
			return initValue != null ? initValue : NULL_LITERAL ; 
		} else {
			// nullable attribute
			return NULL_LITERAL;
		}
	}

}
