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
 * Literal values provider for "C#" language
 * 
 * @author Laurent GUERIN
 *
 */
public class LiteralValuesProviderForCSharp extends LiteralValuesProvider {
	
	private static final String NULL_LITERAL  = "null" ; 
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
		
		String neutralType = languageType.getNeutralType(); 
		
		// cf https://stackoverflow.com/questions/5820721/c-sharp-short-long-int-literal-format 
		
		//--- STRING
		if ( NeutralType.STRING.equals(neutralType) ) {
			String value = buildStringValue(maxLength, step);
			return new LiteralValue("\"" + value + "\"", value) ;			
		}
		
		//--- NUMBER / INTEGER without SUFIX
		else if (  NeutralType.BYTE.equals(neutralType) 
				|| NeutralType.SHORT.equals(neutralType)
				|| NeutralType.INTEGER.equals(neutralType) ) {
			Long value = buildIntegerValue(neutralType, step);  
			return new LiteralValue(value.toString(), value) ; // eg : 123
		}
		//--- NUMBER / INTEGER with SUFIX		
		else if ( NeutralType.LONG.equals(neutralType)  ) {
			Long value = buildIntegerValue(neutralType, step);  
			return new LiteralValue(value.toString() + "L", value) ; // + "L"
		}

		//--- NUMBER (NOT INTEGER)
		else if ( NeutralType.FLOAT.equals(neutralType)  ) {
			BigDecimal value = buildDecimalValue(neutralType, step);
			return new LiteralValue(value.toString() + "F", value) ; // + "F"
		}
		else if ( NeutralType.DOUBLE.equals(neutralType) ) {
			BigDecimal value = buildDecimalValue(neutralType, step);
			return new LiteralValue(value.toString() + "D", value) ; // + "D"
		} 
		else if ( NeutralType.DECIMAL.equals(neutralType) ) {
			BigDecimal value = buildDecimalValue(neutralType, step);
			return new LiteralValue(value.toString() + "M", value) ; // + "M"
		}
		
		//--- BOOLEAN
		else if ( NeutralType.BOOLEAN.equals(neutralType)  ) {
			boolean value = buildBooleanValue(step);
			return new LiteralValue(value ? TRUE_LITERAL : FALSE_LITERAL, Boolean.valueOf(value)) ;
		}
		
		//--- UUID
		else if ( NeutralType.UUID.equals(neutralType)  ) {
			String uuidString = buildUUID();
			return new LiteralValue("Guid.Parse(\""+uuidString+"\")", uuidString) ; // v 4.3
		}
		

		//--- Noting for DATE, TIME and TIMESTAMP, BINARY
		
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
		notNullInitValues.put(NeutralType.STRING,  "\"\"");  // string, String
		notNullInitValues.put(NeutralType.BOOLEAN, FALSE_LITERAL); // bool, Boolean
		notNullInitValues.put(NeutralType.BYTE,    "0");   // sbyte, SByte, byte, Byte
		notNullInitValues.put(NeutralType.SHORT,   "0");   // short, Int16, ushort, UInt16
		notNullInitValues.put(NeutralType.INTEGER, "0");   // int, Int32, uint, UInt32
		notNullInitValues.put(NeutralType.LONG,    "0L");  // long, Int64, ulong, UInt64 (long requires 'L' suffix (optional but recommended for clarity)
		notNullInitValues.put(NeutralType.FLOAT,   "0.0f");  // float, Single (float requires 'f' suffix)
		notNullInitValues.put(NeutralType.DOUBLE,  "0.0" );   // double, Double (double is default for floating-point literals)
		notNullInitValues.put(NeutralType.DECIMAL, "0.0m");  // decimal, Decimal (decimal requires 'm' suffix)

		// for temporal types 3 options : "ClassName.MinValue" or "new ClassName" (for all) and "ClassName.Now" (only for DateTime and DateTimeOffset)
		notNullInitValues.put(NeutralType.DATE,       "DateOnly.MinValue" ); // 01/01/0001
		notNullInitValues.put(NeutralType.TIME,       "TimeOnly.MinValue" ); // 00:00:00.0000000 
		notNullInitValues.put(NeutralType.TIMETZ,     "TimeOnly.MinValue" ); // 00:00:00.0000000 
		notNullInitValues.put(NeutralType.TIMESTAMP,  "DateTime.MinValue" ); // 01/01/0001 00:00:00
		notNullInitValues.put(NeutralType.DATETIME,   "DateTime.MinValue" ); // 01/01/0001 00:00:00
		notNullInitValues.put(NeutralType.DATETIMETZ, "DateTimeOffset.MinValue"); // current local time with the local timezone offset
		
		notNullInitValues.put(NeutralType.BINARY,    "new byte[0]"); // void array

		notNullInitValues.put(NeutralType.UUID,      "Guid.Empty"); // "new Guid" or "Guid.Empty" (kind of "zero-value")
	}

	@Override
	public String getInitValue(AttributeInContext attribute, LanguageType languageType) {
//		if (attribute.isNotNull()) {
//			// not null attribute
//			String defaultValue = defaultValues.get(languageType.getNeutralType());
//			return defaultValue != null ? defaultValue : NULL_LITERAL ; 
//		} else {
//			// nullable attribute
//			return NULL_LITERAL;
//		}
		return getInitValue(languageType.getNeutralType(), attribute.isNotNull());
	}
	
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
