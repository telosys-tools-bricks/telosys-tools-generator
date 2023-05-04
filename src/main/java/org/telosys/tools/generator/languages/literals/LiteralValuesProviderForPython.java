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
 * Literal values provider for "PYTHON" language
 * 
 * Python samples :
 * 
 * a = False
 * b = True
 * 
 * print(a)
 * if  a == True :
 *     print("TRUE")
 *     
 * a = "aa"
 * if ( a == "aa") :
 *    print ("YES")
 *    
 * @author Laurent GUERIN
 *
 */
public class LiteralValuesProviderForPython extends LiteralValuesProvider {
	
	private static final String NULL_LITERAL  = "None" ; // In Python, the 'null' object is the singleton 'None'.
	private static final String TRUE_LITERAL  = "True" ; 
	private static final String FALSE_LITERAL = "False" ; 
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
		
		// The "neutral type" is the only usable information
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
		notNullInitValues.put(NeutralType.STRING,  EMPTY_STRING_LITERAL);  // string 
		notNullInitValues.put(NeutralType.BOOLEAN, FALSE_LITERAL);  
		// Python numeric data types: int, float, complex
		notNullInitValues.put(NeutralType.BYTE,    "0"); // int
		notNullInitValues.put(NeutralType.SHORT,   "0"); // int 
		notNullInitValues.put(NeutralType.INTEGER, "0"); // int 
		notNullInitValues.put(NeutralType.LONG,    "0"); // int 
		notNullInitValues.put(NeutralType.FLOAT,   "0.0");  // float
		notNullInitValues.put(NeutralType.DOUBLE,  "0.0");  // float
		notNullInitValues.put(NeutralType.DECIMAL, "0.0");  // float

//		notNullInitValues.put(NeutralType.DATE,      "?");  
//		notNullInitValues.put(NeutralType.TIME,      "?"); 
//		notNullInitValues.put(NeutralType.TIMESTAMP, "?"); 
//		defaultValues.put(NeutralType.BINARY,    "?"); 
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
