/**
 *  Copyright (C) 2008-2013  Telosys project org. ( http://www.telosys.org/ )
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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.names.ContextName;

/**
 * 
 * @author Laurent GUERIN
 *
 */
//-------------------------------------------------------------------------------------
@VelocityObject(
		contextName= ContextName.ENTITY ,
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
	private final Map<String, Object> _values ; 
	
	//-----------------------------------------------------------------------------------------------
	public ValuesInContext( final List<AttributeInContext> attributes, int step ) //throws GeneratorException
	{
		_values = new HashMap<String, Object>();
		
		for ( AttributeInContext attrib : attributes ) {
			_values.put ( attrib.getName() , generateValue(attrib, step)  ) ;
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
	
	private Object generateValue(AttributeInContext attrib, int step) {

		if ( attrib.isNumberType() ) {
			if ( attrib.isBigDecimalType()) {
				return new BigDecimal(step*10000);
			}
			else if ( attrib.isLongType() ) {
				return new Long(step*1000);
			}
			else if ( attrib.isIntegerType() ) {
				return new Integer(step*100);
			}
			else if ( attrib.isShortType() ) {
				return new Short((short)(step*10));
			}
			else if ( attrib.isByteType() ) {
				return new Byte((byte)step);
			}
			else if ( attrib.isFloatType() ) {
				return new Float(step*1000+0.5);
			}
			else if ( attrib.isDoubleType() ) {
				return new Double(step*1000+0.6);
			}
		}
		else if ( attrib.isStringType() ) {
			int maxLength = StrUtil.getInt(attrib.getMaxLength(), 1) ;
			return buildStringValue(maxLength, step);
		}
		else if ( attrib.isBooleanType() ) {
			return step % 2 != 0 ? Boolean.TRUE : Boolean.FALSE ;
		}
		else if ( attrib.isSqlDateType() ) {
			java.util.Date now = new java.util.Date ();
			return new java.sql.Date(now.getTime()) ;
		}
		else if ( attrib.isSqlTimeType() ) {
			java.util.Date now = new java.util.Date ();
			return new java.sql.Time(now.getTime()) ;
		}
		else if ( attrib.isSqlTimestampType() ) {
			java.util.Date now = new java.util.Date ();
			return new java.sql.Timestamp(now.getTime()) ;
		}
		else if ( attrib.isUtilDateType() ) {
			return new java.util.Date ();
		}
		return null ;
	}

	private final static String NULL_LITERAL = "null" ; 
	
	public String javaValue(String attributeName) {
		Object value = _values.get(attributeName) ;
		if ( value != null ) {
			return javaLiteralValue(value);
		}
		else {
			return NULL_LITERAL ;
		}
	}
	
	private String javaLiteralValue(Object value) {
			
		if ( value != null ) {
			if ( value instanceof String ) {
				return "\"" + value + "\"" ;
			}
			else if ( value instanceof Byte ) {
				return "(byte)" + value.toString()  ;
			}
			else if ( value instanceof Short ) {
				return "(short)" + value.toString() ;
			}
			else if ( value instanceof Integer ) {
				return value.toString() ;
			}
			else if ( value instanceof Long ) {
				return value.toString() + "L" ;
			}
			else if ( value instanceof Float ) {
				return value.toString() + "F" ;
			}
			else if ( value instanceof Double ) {
				return value.toString() + "D" ;
			}
			else if ( value instanceof BigDecimal ) {
				return "(new BigDecimal(" + value.toString() + "))" ;
			}
			else if ( value instanceof Boolean ) {
				return value.toString() ;
			}
			else if ( value instanceof java.sql.Date ) {
//				return "(new java.sql.Date(Calendar.getInstance().getTime().getTime()))" ;
				// must be invariant 
				return NULL_LITERAL;
			}
			else if ( value instanceof java.sql.Time ) {
//				return "(new java.sql.Time(Calendar.getInstance().getTime().getTime()))" ;
				// must be invariant 
				return NULL_LITERAL;
			}
			else if ( value instanceof java.sql.Timestamp ) {
//				return "(new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()))" ;
				// must be invariant 
				return NULL_LITERAL;
			}
			else if ( value instanceof java.util.Date ) { // NB : keep at the end (because oh inheritance)
//				return "Calendar.getInstance().getTime()" ;
				// must be invariant 
				return NULL_LITERAL;
			}
			else {
				return value.toString() ;
			}
		}
		else {
			return NULL_LITERAL ;
		}
	}

	public String getJavaValues() {
		StringBuilder sb = new StringBuilder();
		int n = 0 ;
		for ( String name : _values.keySet() ) {
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
		
		Object value = _values.get( attribute.getName() ) ;
		if ( value != null ) {
			if ( value instanceof String ) {
				sb.append( buildObjectEqualsValue(value) ) ;
			}
			else if ( value instanceof Byte ) {
				sb.append( buildStandardEqualsValue(value) ) ;
			}
			else if ( value instanceof Short || value instanceof Integer ) {
				sb.append( buildStandardEqualsValue(value) ) ;
			}
			else if ( value instanceof Long ) {
				sb.append( buildStandardEqualsValue(value) ) ;
			}
			else if ( value instanceof Float ) {
				sb.append( buildStandardEqualsValue(value) ) ;
			}
			else if ( value instanceof Double ) {
				sb.append( buildStandardEqualsValue(value) ) ;
			}
			else if ( value instanceof BigDecimal ) {
				sb.append( buildObjectEqualsValue(value) ) ;
			}
			else if ( value instanceof Boolean ) {
				sb.append( buildStandardEqualsValue(value) ) ;
			}
			else if ( value instanceof java.sql.Date ) {
				sb.append( buildObjectEqualsValue(value) ) ;
			}
			else if ( value instanceof java.sql.Time ) {
				sb.append( buildObjectEqualsValue(value) ) ;
			}
			else if ( value instanceof java.sql.Timestamp ) {
				sb.append( buildObjectEqualsValue(value) ) ;
			}
			else if ( value instanceof java.util.Date ) { // NB : keep at the end (because oh inheritance)
				sb.append( buildObjectEqualsValue(value) ) ;
			}
			else {
				sb.append( buildObjectEqualsValue(value) ) ;
			}
		}
		else {
			sb.append( buildObjectEqualsValue(value) ) ;
		}
		return sb.toString();
	}
	public String buildObjectEqualsValue(Object value) {
		StringBuilder sb = new StringBuilder();
		sb.append( ".equals(" );
		sb.append( javaLiteralValue(value) );
		sb.append( ")" );
		return sb.toString();
	}
	public String buildStandardEqualsValue(Object value) {
		StringBuilder sb = new StringBuilder();
		sb.append( " == " );
		sb.append( value.toString() );
		sb.append( " " );
		return sb.toString();
	}
}
