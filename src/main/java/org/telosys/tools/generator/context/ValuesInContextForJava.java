/**
 *  Copyright (C) 2008-2015  Telosys project org. ( http://www.telosys.org/ )
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
package org.telosys.tools.generator.context;

import java.util.List;

import org.telosys.tools.commons.StrUtil;

/**
 * 
 * @author Laurent GUERIN
 *
 */
public class ValuesInContextForJava extends ValuesInContext {

	/**
	 * Constructor
	 * @param attributes
	 * @param step
	 */
	public ValuesInContextForJava( final List<AttributeInContext> attributes, int step ) {
		super(attributes, step );
	}
	
	@Override
	protected String generateLiteralValue(AttributeInContext attrib, int step) {

		if ( attrib.isNumberType() ) {
			//if ( attrib.isBigDecimalType()) {
			if ( attrib.isDecimalType()) { // v 3.0.0
				return "(new BigDecimal(" + (step*10000) + "))" ;
			}
			else if ( attrib.isByteType() ) {
				return "(byte)" + step ;
			}
			else if ( attrib.isShortType() ) {
				return "(short)" + (step*10) ;
			}
			else if ( attrib.isIntegerType() ) {
				return "" + (step*100);
			}
			else if ( attrib.isLongType() ) {
				return (step*1000) + "L" ;
			}
			else if ( attrib.isFloatType() ) {
				return (step*1000) + ".5F" ;
			}
			else if ( attrib.isDoubleType() ) {
				return (step*1000) + ".66D" ;
			}
		}
		else if ( attrib.isStringType() ) {
			int maxLength = StrUtil.getInt(attrib.getMaxLength(), 1) ;
			return "\"" + buildStringValue(maxLength, step) + "\"" ;
		}
		else if ( attrib.isBooleanType() ) {
			Boolean val = step % 2 != 0 ? Boolean.TRUE : Boolean.FALSE ;
			return val.toString() ;
		}
		else if ( attrib.isDateType() ) {
//			String fullType = attrib.getFullType() ; 
//			if ( "java.util.Date".equals(fullType) || "java.sql.Date".equals(fullType) ) {
//				String dateISO = (2000+step) + "-06-22" ; // "2001-06-22" 
//				return "java.sql.Date.valueOf(\"" + dateISO + "\")" ; 
//			}
			// The "java.sql.Date" is OK with both "java.util.Date" and "java.sql.Date"
			String dateISO = (2000+step) + "-06-22" ; // "2001-06-22" 
			return "java.sql.Date.valueOf(\"" + dateISO + "\")" ; 
		}
		else if ( attrib.isTimeType() ) {
			String timeISO =  String.format("%02d", (step%24) ) + ":46:52" ; // "15:46:52"
			return "java.sql.Time.valueOf(\"" + timeISO + "\")" ; // "15:46:52"
		}
		else if ( attrib.isTimestampType() ) {
			String timestampISO = (2000+step) + "-05-21" + " " + String.format("%02d", (step%24) ) + ":46:52" ; // "2001-05-21 15:46:52" 
			return "java.sql.Timestamp.valueOf(\"" + timestampISO + "\")" ; // e.g. "2001-05-21 15:46:52"
		}
		else {
			// v 3.0.0
			String fullType = attrib.getFullType() ; 
			if ( "java.util.Date".equals(fullType) || "java.sql.Date".equals(fullType) ) {
				String dateISO = (2000+step) + "-06-22" ; // "2001-06-22" 
				return "java.sql.Date.valueOf(\"" + dateISO + "\")" ; 
			}
//			else if ( "java.sql.Date".equals(fullType) ) {
//				String dateISO = ( 2000 + step )  + "-05-21" ; // "2001-05-21" 
//				return "java.sql.Date.valueOf(\"" + dateISO + "\")" ; // "2001-05-21"				
//			}
			else if ( "java.sql.Time".equals(fullType) ) {
				String timeISO =  String.format("%02d", (step%24) ) + ":46:52" ; // "15:46:52"
				return "java.sql.Time.valueOf(\"" + timeISO + "\")" ; // "15:46:52"
			}
			else if ( "java.sql.Timestamp".equals(fullType) ) {
				String timestampISO = (2000+step) + "-05-21" + " " + String.format("%02d", (step%24) ) + ":46:52" ; // "2001-05-21 15:46:52" 
				return "java.sql.Timestamp.valueOf(\"" + timestampISO + "\")" ; // e.g. "2001-05-21 15:46:52"
			}
		}
		return null ;
	}

	private final static String NULL_LITERAL = "null" ; 
	
	@Override
	public String getValue(String attributeName) {
		String value = _values.get(attributeName) ;
		if ( value != null ) {
			return value;
		}
		else {
			return NULL_LITERAL ;
		}
	}
	
	/**
	 * Returns a string containing all the java values separated by a comma.<br>
	 * e.g. : ' "AAAA", (short)10, true '
	 * @return
	 */
	@Override
	public String getAllValues() {
		StringBuilder sb = new StringBuilder();
		int n = 0 ;
		for ( String name : _attributeNames ) {
			if ( n > 0 ) {
				sb.append(", ");
			}
			sb.append(getValue(name));
			n++ ;
		}
		return sb.toString();
	}
	
	/* 
	 * Return something like that : 
	 *   book.getId() == 100 
	 *   book.getFirstName().equals("xxx")
	 */
	@Override
	public String comparisonStatement(String entityVariableName, AttributeInContext attribute) {
		StringBuilder sb = new StringBuilder();
		sb.append( entityVariableName ) ;
		sb.append( "." ) ;
		sb.append( attribute.getGetter() ) ;
		sb.append( "()" ) ;
		
		String value = _values.get( attribute.getName() ) ; // Value for the given attribute
		if ( attribute.isNumberType() ) {
			//if ( attribute.isBigDecimalType() ) { 
			if ( attribute.isDecimalType() ) { // v 3.0.0
				sb.append( buildObjectEqualsValue(value) ) ;
			}
			else {
				// int, long, double, float, ...
				sb.append( buildStandardEqualsValue(value) );
			}
		}
		else if ( attribute.isBooleanType() ) {
			sb.append( buildStandardEqualsValue(value) );
		}
		else {
			sb.append( buildObjectEqualsValue(value) ) ;
		}
		return sb.toString();
	}

	private String buildStringValue(int maxLength, int step) {
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
		
	private String buildObjectEqualsValue(String value) {
		StringBuilder sb = new StringBuilder();
		sb.append( ".equals(" );
		sb.append( value );
		sb.append( ")" );
		return sb.toString();
	}
	
	private String buildStandardEqualsValue(String value) {
		StringBuilder sb = new StringBuilder();
		sb.append( " == " );
		sb.append( value );
		sb.append( " " );
		return sb.toString();
	}
}
