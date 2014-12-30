/**
 *  Copyright (C) 2008-2014  Telosys project org. ( http://www.telosys.org/ )
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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.generator.context.doc.VelocityObject;

/**
 * 
 * @author Laurent GUERIN
 *
 */
//-------------------------------------------------------------------------------------
@VelocityObject(
		contextName = "no_name_in_context" ,
		//otherContextNames=ContextName.BEAN_CLASS,
		text = { 
				"xxxx",
				"",
				" xxxx ",
				""
		},
		since = "2.0.0"
 )
//-------------------------------------------------------------------------------------
public class ValuesInContext 
{
	private final LinkedList<String>  _attributeNames ; // to keep the original list order
	private final Map<String, String> _values ; // attribute name --> java literal value
	
	//-----------------------------------------------------------------------------------------------
	public ValuesInContext( final List<AttributeInContext> attributes, int step ) //throws GeneratorException
	{
		_values = new HashMap<String, String>();
		_attributeNames = new LinkedList<String>();
		
		for ( AttributeInContext attrib : attributes ) {
			//_values.put ( attrib.getName() , generateValue(attrib, step)  ) ;
			_values.put ( attrib.getName() , generateJavaLiteralValue(attrib, step)  ) ;
			
			_attributeNames.add( attrib.getName() );
		}

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
	
//	private Object generateValue(AttributeInContext attrib, int step) {
//
//		if ( attrib.isNumberType() ) {
//			if ( attrib.isBigDecimalType()) {
//				return new BigDecimal(step*10000);
//			}
//			else if ( attrib.isLongType() ) {
//				return new Long(step*1000);
//			}
//			else if ( attrib.isIntegerType() ) {
//				return new Integer(step*100);
//			}
//			else if ( attrib.isShortType() ) {
//				return new Short((short)(step*10));
//			}
//			else if ( attrib.isByteType() ) {
//				return new Byte((byte)step);
//			}
//			else if ( attrib.isFloatType() ) {
//				return new Float(step*1000+0.5);
//			}
//			else if ( attrib.isDoubleType() ) {
//				return new Double(step*1000+0.6);
//			}
//		}
//		else if ( attrib.isStringType() ) {
//			int maxLength = StrUtil.getInt(attrib.getMaxLength(), 1) ;
//			return buildStringValue(maxLength, step);
//		}
//		else if ( attrib.isBooleanType() ) {
//			return step % 2 != 0 ? Boolean.TRUE : Boolean.FALSE ;
//		}
//		else if ( attrib.isSqlDateType() ) {
//			return ( 2000 + step )  + "-05-21" ; // "2001-05-21" 
//		}
//		else if ( attrib.isSqlTimeType() ) {
//			return String.format("%02d", (step%24) ) + ":46:52" ; // "15:46:52"
//		}
//		else if ( attrib.isSqlTimestampType() ) {
//			//java.util.Date now = new java.util.Date ();
//			//return new java.sql.Timestamp(now.getTime()) ;
//			return (2000+step) + "-05-21" + " " + String.format("%02d", (step%24) ) + ":46:52" ; // "2001-05-21 15:46:52" 
//		}
//		else if ( attrib.isUtilDateType() ) {
//			return (2000+step) + "-06-22" ; // "2001-06-22" 
//		}
//		return null ;
//	}
	
	private String generateJavaLiteralValue(AttributeInContext attrib, int step) {

		if ( attrib.isNumberType() ) {
			if ( attrib.isBigDecimalType()) {
				//return new BigDecimal(step*10000);
				return "(new BigDecimal(" + (step*10000) + "))" ;
			}
			else if ( attrib.isByteType() ) {
				//byte val = (byte) step ;
				return "(byte)" + step ;
			}
			else if ( attrib.isShortType() ) {
				//return new Short((short)(step*10));
				return "(short)" + (step*10) ;
			}
			else if ( attrib.isIntegerType() ) {
				//return new Integer(step*100);
				return "" + (step*100);
			}
			else if ( attrib.isLongType() ) {
				//return new Long(step*1000);
				return (step*1000) + "L" ;
			}
			else if ( attrib.isFloatType() ) {
				//return new Float(step*1000+0.5);
				return (step*1000) + ".5F" ;
			}
			else if ( attrib.isDoubleType() ) {
				//return new Double(step*1000+0.6);
				return (step*1000) + ".66D" ;
			}
		}
		else if ( attrib.isStringType() ) {
			int maxLength = StrUtil.getInt(attrib.getMaxLength(), 1) ;
			//return buildStringValue(maxLength, step);
			return "\"" + buildStringValue(maxLength, step) + "\"" ;
		}
		else if ( attrib.isBooleanType() ) {
			//return step % 2 != 0 ? Boolean.TRUE : Boolean.FALSE ;
			Boolean val = step % 2 != 0 ? Boolean.TRUE : Boolean.FALSE ;
			return val.toString() ;
		}
		else if ( attrib.isSqlDateType() ) {
			String dateISO = ( 2000 + step )  + "-05-21" ; // "2001-05-21" 
			return "java.sql.Date.valueOf(\"" + dateISO + "\")" ; // "2001-05-21"
		}
		else if ( attrib.isSqlTimeType() ) {
			String timeISO =  String.format("%02d", (step%24) ) + ":46:52" ; // "15:46:52"
			return "java.sql.Time.valueOf(\"" + timeISO + "\")" ; // "15:46:52"
		}
		else if ( attrib.isSqlTimestampType() ) {
			String timestampISO = (2000+step) + "-05-21" + " " + String.format("%02d", (step%24) ) + ":46:52" ; // "2001-05-21 15:46:52" 
			return "java.sql.Timestamp.valueOf(\"" + timestampISO + "\")" ; // e.g. "2001-05-21 15:46:52"
		}
		else if ( attrib.isUtilDateType() ) {
			String dateISO = (2000+step) + "-06-22" ; // "2001-06-22" 
			return "java.sql.Date.valueOf(\"" + dateISO + "\")" ; 
		}
		return null ;
	}

	private final static String NULL_LITERAL = "null" ; 
	
	public String javaValue(String attributeName) {
		//Object value = _values.get(attributeName) ;
		String value = _values.get(attributeName) ;
		if ( value != null ) {
			//return javaLiteralValue(value);
			return value;
		}
		else {
			return NULL_LITERAL ;
		}
	}
	
//	private String javaLiteralValue(Object value) {
//			
//		if ( value != null ) {
//			if ( value instanceof String ) {
//				return "\"" + value + "\"" ;
//			}
//			else if ( value instanceof Byte ) {
//				return "(byte)" + value.toString()  ;
//			}
//			else if ( value instanceof Short ) {
//				return "(short)" + value.toString() ;
//			}
//			else if ( value instanceof Integer ) {
//				return value.toString() ;
//			}
//			else if ( value instanceof Long ) {
//				return value.toString() + "L" ;
//			}
//			else if ( value instanceof Float ) {
//				return value.toString() + "F" ;
//			}
//			else if ( value instanceof Double ) {
//				return value.toString() + "D" ;
//			}
//			else if ( value instanceof BigDecimal ) {
//				return "(new BigDecimal(" + value.toString() + "))" ;
//			}
//			else if ( value instanceof Boolean ) {
//				return value.toString() ;
//			}
//			else if ( value instanceof java.sql.Date ) {
////				return "(new java.sql.Date(Calendar.getInstance().getTime().getTime()))" ;
//				// must be invariant 
//				//return NULL_LITERAL;
//				return "java.sql.Date.valueOf(\"" + value.toString() + "\")" ; // "2001-05-21"
//			}
//			else if ( value instanceof java.sql.Time ) {
////				return "(new java.sql.Time(Calendar.getInstance().getTime().getTime()))" ;
//				// must be invariant 
//				//return NULL_LITERAL;
//				return "java.sql.Time.valueOf(\"" + value.toString() + "\")" ; // "15:46:52"
//			}
//			else if ( value instanceof java.sql.Timestamp ) {
////				return "(new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()))" ;
//				// must be invariant 
////				return NULL_LITERAL;
//				return "java.sql.Timestamp.valueOf(\"" + value.toString() + "\")" ; // e.g. "2001-05-21 15:46:52"
//			}
//			else if ( value instanceof java.util.Date ) { // NB : keep at the end (because of inheritance)
////				return "Calendar.getInstance().getTime()" ;
//				// must be invariant 
////				return NULL_LITERAL;
//				return "java.sql.Date.valueOf(\"" + value.toString() + "\")" ; // "2001-05-21"
//			}
//			else {
//				return value.toString() ;
//			}
//		}
//		else {
//			return NULL_LITERAL ;
//		}
//	}

	/**
	 * Returns a string containing all the java values separated by a comma.<br>
	 * e.g. : ' "AAAA", (short)10, true '
	 * @return
	 */
	public String getJavaValues() {
		StringBuilder sb = new StringBuilder();
		int n = 0 ;
		//for ( String name : _values.keySet() ) {
		for ( String name : _attributeNames ) {
			if ( n > 0 ) {
				sb.append(", ");
			}
			sb.append(javaValue(name));
			n++ ;
		}
		return sb.toString();
	}
	
	public String javaValueComparedTo(String entityVariableName, AttributeInContext attribute) {
		StringBuilder sb = new StringBuilder();
		sb.append( entityVariableName ) ;
		sb.append( "." ) ;
		sb.append( attribute.getGetter() ) ;
		sb.append( "()" ) ;
		
		//Object value = _values.get( attribute.getName() ) ;
		String value = _values.get( attribute.getName() ) ;
		if ( attribute.isNumberType() ) {
			if ( attribute.isBigDecimalType() ) {
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
//		if ( value != null ) {
//			if ( value instanceof String ) {
//				sb.append( buildObjectEqualsValue(value) ) ;
//			}
//			else if ( value instanceof Byte ) {
//				sb.append( buildStandardEqualsValue(value) ) ;
//			}
//			else if ( value instanceof Short || value instanceof Integer ) {
//				sb.append( buildStandardEqualsValue(value) ) ;
//			}
//			else if ( value instanceof Long ) {
//				sb.append( buildStandardEqualsValue(value) ) ;
//			}
//			else if ( value instanceof Float ) {
//				sb.append( buildStandardEqualsValue(value) ) ;
//			}
//			else if ( value instanceof Double ) {
//				sb.append( buildStandardEqualsValue(value) ) ;
//			}
//			else if ( value instanceof BigDecimal ) {
//				sb.append( buildObjectEqualsValue(value) ) ;
//			}
//			else if ( value instanceof Boolean ) {
//				sb.append( buildStandardEqualsValue(value) ) ;
//			}
//			else if ( value instanceof java.sql.Date ) {
//				sb.append( buildObjectEqualsValue(value) ) ;
//			}
//			else if ( value instanceof java.sql.Time ) {
//				sb.append( buildObjectEqualsValue(value) ) ;
//			}
//			else if ( value instanceof java.sql.Timestamp ) {
//				sb.append( buildObjectEqualsValue(value) ) ;
//			}
//			else if ( value instanceof java.util.Date ) { // NB : keep at the end (because oh inheritance)
//				sb.append( buildObjectEqualsValue(value) ) ;
//			}
//			else {
//				sb.append( buildObjectEqualsValue(value) ) ;
//			}
//		}
//		else {
//			sb.append( buildObjectEqualsValue(value) ) ;
//		}
		return sb.toString();
	}
	
	//public String buildObjectEqualsValue(Object value) {
	public String buildObjectEqualsValue(String value) {
		StringBuilder sb = new StringBuilder();
		sb.append( ".equals(" );
		//sb.append( javaLiteralValue(value) );
		sb.append( value );
		sb.append( ")" );
		return sb.toString();
	}
	
	//public String buildStandardEqualsValue(Object value) {
	public String buildStandardEqualsValue(String value) {
		StringBuilder sb = new StringBuilder();
		sb.append( " == " );
		sb.append( value );
		sb.append( " " );
		return sb.toString();
	}
}
