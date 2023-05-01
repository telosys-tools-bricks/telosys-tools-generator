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
import org.telosys.tools.generic.model.types.NeutralType;

/**
 * Abstract literal values provider
 * 
 * @author Laurent GUERIN
 *
 */
public abstract class LiteralValuesProvider {
	
	/**
	 * Ensures the value is always less than or equal at the given threshold 
	 * @param value
	 * @param max
	 * @return
	 */
	protected int checkThreshold(int value, int max) {
		if ( value <= max ) {
			// OK ( 0 to MAX )
			return value ;
		}
		else {
			int modulo = value % max ;
			if ( modulo == 0 ) {
				return max ;
			}
			else {
				return modulo ;
			}
		}
	}
	
	/**
	 * Builds a string with the given length 
	 * @param maxLength
	 * @param step the step used for randomization
	 * @return
	 */
	protected String buildStringValue(int maxLength, int step) {
		int maxLimit = 100 ;
		// 'A'-'Z' : 65-90 
		// 'a'-'z' : 97-122 
		char c = 'A' ; 
		if ( step > 0 ) {
			int delta = (step-1) % 26;
			c = (char)('A' + delta );
		}
		StringBuilder sb = new StringBuilder();
		for ( int i = 0 ; i < maxLength && i < maxLimit ; i++) {
			sb.append(c);
		}
		return sb.toString();
	}

	protected Long buildIntegerValue(String neutralType, int step) {
		//--- BYTE
		if ( NeutralType.BYTE.equals(neutralType) ) {
			return Long.valueOf(checkThreshold(step, Byte.MAX_VALUE)) ;  
		}
		
		//--- SHORT
		else if ( NeutralType.SHORT.equals(neutralType) ) {
			return Long.valueOf(checkThreshold(step, Short.MAX_VALUE)) ;  
		}
		
		//--- INT		
		else if ( NeutralType.INTEGER.equals(neutralType)  ) {
			return Long.valueOf(step*100L) ;  
		}
		
		//--- LONG
		else if ( NeutralType.LONG.equals(neutralType)  ) {
			return Long.valueOf(step*1000L) ;  
		}

		//--- INVALID TYPE
		else {
			throw new IllegalArgumentException("Invalid neutral type " + neutralType);
		}
	}
	
	protected BigDecimal buildDecimalValue(String neutralType, int step) {
		//--- FLOAT
		if ( NeutralType.FLOAT.equals(neutralType)  ) {
			return BigDecimal.valueOf((step * 1000) + 0.5);
		}
		
		//--- DOUBLE
		else if ( NeutralType.DOUBLE.equals(neutralType) ) {
			return BigDecimal.valueOf((step * 1000) + 0.66);
		}
		
		//--- BIG DECIMAL
		else if ( NeutralType.DECIMAL.equals(neutralType) ) {
			return BigDecimal.valueOf((step * 10000) + 0.77);
		}
		
		//--- INVALID TYPE
		else {
			throw new IllegalArgumentException("Invalid neutral type " + neutralType);
		}
	}
	
	protected boolean buildBooleanValue(int step) {
		return step % 2 != 0 ;
	}

	/**
	 * Builds a date in ISO format, eg "2001-06-22" 
	 * @param step
	 * @return
	 */
	protected String buildDateISO(int step) {
		return buildYearValue(step) + "-06-22" ; 
	}
	
	/**
	 * Builds a time in ISO format, eg "15:46:52"
	 * @param step
	 * @return
	 */
	protected String buildTimeISO(int step) {
		return buildHourValue(step) + ":46:52" ; 
	}
	
	/**
	 * Builds a date+time in ISO format, eg "2001-05-21T15:47:53"
	 * @param step
	 * @return
	 */
	protected String buildDateTimeISO(int step) {
		return buildYearValue(step) + "-05-21" 
				+ "T" 
				+ buildHourValue(step) + ":47:53" ;  
	}
	
	private String buildYearValue(int step) {
		int year = 2000 + ( step % 1000 ) ;  // between 2000 and 2999 
		return "" + year ;
	}
	private String buildHourValue(int step) {
		int hour = step % 24 ;
		return String.format("%02d", hour) ; // between 0 and 23		
	}
	
	
	//------------------------------------------------------------------------------------
	// ABSTRACT METHODS
	//------------------------------------------------------------------------------------

	/**
	 * Returns the literal 'null' for the current language
	 * @return
	 */
	public abstract String getLiteralNull() ;

	/**
	 * Returns the literal 'TRUE' for the current language
	 * @return
	 */
	public abstract String getLiteralTrue() ;

	/**
	 * Returns the literal 'FALSE' for the current language
	 * @return
	 */
	public abstract String getLiteralFalse() ;

	/**
	 * Generates a literal value for the given language type <br>
	 * @param languageType
	 * @param maxLength
	 * @param step
	 * @return
	 */
	public abstract LiteralValue generateLiteralValue(LanguageType languageType, int maxLength, int step) ;

	/**
	 * Returns the ad hoc equals statement for a given literal value according with the language type<br>
	 * Example in Java : <br>
	 *   ' == 100'  <br>
	 *   '.equals("abcd")' <br>
	 *   
	 * @param value
	 * @param languageType
	 * @return
	 */
	public abstract String getEqualsStatement(String value, LanguageType languageType) ;
	
	
	public abstract String getDefaultValueNotNull(LanguageType languageType) ;


}
