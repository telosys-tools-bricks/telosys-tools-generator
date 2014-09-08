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

import java.util.LinkedList;
import java.util.List;

import org.apache.velocity.VelocityContext;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.commons.XmlUtil;
import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.doc.VelocityReturnType;
import org.telosys.tools.generator.context.names.ContextName;

/**
 * Set of functions usable in Velocity template with $fn.functionName(...) 
 * 
 * @author Laurent Guerin
 *
 */
//-------------------------------------------------------------------------------------
@VelocityObject(
		contextName=ContextName.FN,
		text = { 
				"Object providing a set of utility functions ",
				""
		},
		since = "2.0.3"
 )
//-------------------------------------------------------------------------------------
public class Fn {

// USELESS 
//	public boolean isNull (Object o) {
//		return null == o ;
//	}
//	
//	public boolean isNotNull (Object o) {
//		return ! isNull(o);
//	}
	
	private final VelocityContext _velocityContext ;
	
	
	public Fn(VelocityContext velocityContext) {
		super();
		this._velocityContext = velocityContext;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the given string is 'blank' ",
			"(true if the string is null or void or only composed of blanks)"
			},
		parameters = { 
			"s : the string to be tested" 
			},
		since = "2.0.3"
	)
	public boolean isBlank (String s) {
		if ( s != null ) {
			if ( s.trim().length() > 0 ) {
				return false ; 
			}
		}
		return true ;
	}
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the given string is not 'blank' ",
			"(true if the string is not null, not void and not only composed of blanks)"},
		parameters = { "s : the string to be tested" }
			)
	public boolean isNotBlank (String s) {
		return ! isBlank (s)  ;
	}
	
	/**
	 * Returns the same string with a double quote at the beginning and at the end
	 * @param s
	 * @return
	 */
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Adds a double quote character at the beginning and at the end of the given string "
			},
		parameters = { "s : the string to be quoted" }
			)
	public String quote(String s) {
		return "\"" + s + "\"" ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(text={	
			"Returns the XML string for the given string",
			"Replaces special characters (&, <, >, etc) by their corresponding XML notation "
			},
			parameters = { "s : the string to be escaped" },
			deprecated=false
			)
	public String escapeXml(String s) {
		return XmlUtil.escapeXml(s) ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(text={	
			"Returns a single tabulation character " 
			}
	)
	public String getTab() {
		return "\t" ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(text={	
			"Returns N tabulation characters "
			},
			parameters = { "n : the number of tabulations to be returned" }
			)
	public String tab(int n) {
		StringBuffer sb = new StringBuffer();
		for ( int i = 0 ; i < n ; i++ ) {
			sb.append("\t");
		}
		return sb.toString();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns a string containing a list of field names separated by a comma"
			},
		example={ 
				"$fn.argumentsList( $entity.attributes )",
				"Returns : 'id, firstName, lastName, age' "},
		parameters = { "fields : list of fields to be added in the arguments list" },
		since = "2.0.5"
			)
	public String argumentsList( List<AttributeInContext> fieldsList ) {
		if ( fieldsList != null ) {
			StringBuilder sb = new StringBuilder();
			int n = 0 ;
			for ( AttributeInContext field : fieldsList ) {
				if ( n > 0 ) sb.append(", ");
				sb.append( field.getName() ) ;
				n++;
			}
			return sb.toString();
		} 
		else {
			return "null" ;
		}
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns a string containing a list of fields (type and name) separated by a comma"
			},
		example={ 
			"$fn.argumentsListWithType( $entity.attributes )",
			"Returns : 'int id, String firstName, String lastName, int age' "},
		parameters = { "fields : list of fields to be added in the arguments list" },
		since = "2.0.5"
			)
	public String argumentsListWithType( List<AttributeInContext> fieldsList ) {
		if ( fieldsList != null ) {
			StringBuilder sb = new StringBuilder();
			int n = 0 ;
			for ( AttributeInContext field : fieldsList ) {
				if ( n > 0 ) sb.append(", ");
				sb.append( field.getType() ) ;
				sb.append( " " ) ;
				sb.append( field.getName() ) ;
				n++;
			}
			return sb.toString();
		} 
		else {
			return "null" ;
		}
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns a string containing a list of fields getters separated by a comma"
			},
		example={ 
			"$fn.argumentsListWithGetter( 'person', $entity.attributes )",
			"Returns : 'person.getId(), person.getFirstName(), person.getLastName(), person.getAge()' "},
		parameters = { 
			"object : name of the object providing the getters",
			"fields : list of fields to be added in the arguments list" },
		since = "2.0.5"
			)
	public String argumentsListWithGetter( String objectName, List<AttributeInContext> fieldsList ) {
		if ( fieldsList != null ) {
			StringBuilder sb = new StringBuilder();
			int n = 0 ;
			for ( AttributeInContext field : fieldsList ) {
				if ( n > 0 ) sb.append(", ");
				sb.append( objectName ) ;
				sb.append( "." ) ;
				sb.append( field.getGetter() ) ;
				sb.append( "()" ) ;
				n++;
			}
			return sb.toString();
		} 
		else {
			return "null" ;
		}
	}
	
	//==============================================================================================
	// Version 2.0.7
	//==============================================================================================
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
			text={	
				"Returns TRUE if the given List/Array is NOT VOID"
				},
			example={ 
				"#if ( $fn.isNotVoid( $entity.attributes ) ) "
				},
			parameters = { 	"collection : List or Array" },
			since = "2.0.7"
				)
	public boolean isNotVoid(Object o) throws Exception {
		return ! isVoid(o, "isNotVoid" ) ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
			text={	
				"Returns TRUE if the given List/Array is VOID"
				},
			example={ 
				"#if ( $fn.isVoid( $entity.attributes ) ) "
				},
			parameters = { 	"collection : List or Array" },
			since = "2.0.7"
				)
	public boolean isVoid(Object o) throws Exception {
		return isVoid(o, "isVoid" ) ;
	}

	//-------------------------------------------------------------------------------------
//	public boolean isVariableDefined (String variableName) {
//		return true ;
//	}
	
	//-------------------------------------------------------------------------------------
	private boolean isVoid(Object o, String functionName ) throws Exception {
		if ( o != null ) {
			if ( o instanceof List ) {
				List<?> list = (List<?>) o ;
				return list.size() == 0 ;
			}
			else if ( o instanceof Object[] ) {
				Object[] array = (Object[]) o ;
				return array.length == 0 ;
			}
			else {
				throw new Exception(functionName + " : list or array expected") ;
			}
		}
		else {
			throw new Exception(functionName + " : list or array is null") ;
		}
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
			text={	
				"Returns the SIZE of the given List or Array"
				},
			example={ 
				"Number of attribute = $fn.size( $entity.attributes )  "
				},
			parameters = { 	"collection : List or Array" },
			since = "2.0.7"
				)
	public int size(Object o) throws Exception {
		final String functionName = "size" ;
		if ( o != null ) {
			if ( o instanceof List ) {
				return ((List<?>) o).size() ;
			}
			else if ( o instanceof Object[] ) {
				return ((Object[]) o).length ;
			}
			else {
				throw new Exception(functionName + " : list or array expected") ;
			}
		}
		else {
			throw new Exception(functionName + " : list or array is null") ;
		}
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
			text={	
				"Concatenates 2 lists ( add all the elements of the second list at the end of the first one ) ",
				"The 2 given lists remain unchanged. The result is stored in a new list."
				},
			example={ 
				"#set ( $list3 = $fn.concatLists( $list1, $list2 )  "
				},
			parameters = { 	"list1 : List of objects", 
							"list2 : List of objects to be added at the end of list1"  },
			since = "2.0.7"
				)
	public List<?> concatLists(List<?> list1, List<?> list2)  {
		List<Object> finalList = new LinkedList<Object>();
		finalList.addAll(list1);
		finalList.addAll(list2);
		return finalList ;
	}
	//-------------------------------------------------------------------------------------
	@VelocityMethod(text={	
			"Converts  all of the characters in the given string to upper case"
			},
			parameters = { "s : the string to be converted" },
			since = "2.0.7"
			)
	public String toUpperCase(String s) {
		if ( s != null ) {
			return s.toUpperCase();
		}
		return "";
	}
	//-------------------------------------------------------------------------------------
	@VelocityMethod(text={	
			"Converts  all of the characters in the given string to lower case"
			},
			parameters = { "s : the string to be converted" },
			since = "2.0.7"
			)
	public String toLowerCase(String s) {
		if ( s != null ) {
			return s.toLowerCase();
		}
		return "";
	}
	//-------------------------------------------------------------------------------------
	@VelocityMethod(text={	
			"Converts the first character to upper case"
			},
			parameters = { "s : the string to be converted" },
			since = "2.0.7"
			)
	public String firstCharToUpperCase(String s) {
		if ( s != null ) {
			return StrUtil.firstCharUC( s );
		}
		return "";
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(text={	
			"Returns TRUE if the given object name is defined in the Velocity Context"
			},
			parameters = { "objectName : the name (or key) in the Velocity Context" },
			example = {
				"#if ( $fn.isDefined('myvar') ) "
			},
			since = "2.1.0"
			)
	public boolean isDefined(String objectName) {
		Object o = _velocityContext.get(objectName);
		return ( o != null );
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(text={	
			"Returns the object stored with the given name in the Velocity Context",
			"If there's no object for the given name the default value is returned"
			},
			parameters = { 
				"objectName : the name (or key) in the Velocity Context",
				"defaultValue : the value to be returned if the object is not defined"},
			example = {
				"$fn.get('groupId','defaultValue') " },
			since = "2.1.0"
			)
	@VelocityReturnType("Any kind of object ")
	public Object get(String objectName, Object defaultValue) {
		Object o = _velocityContext.get(objectName);
		return ( o != null ? o : defaultValue );
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(text={	
			"Capitalizes the given string",
			"changing the first letter to upper case"
			},
			parameters = { 
				"string : the string to be capitalized"
			},
			example = {
				"$fn.capitalize($var)" },
			since = "2.1.0"
			)
	public String capitalize(String str) {
		if(str == null || str.length() == 0) {
			return str;
		}
		if(str.length() == 1) {
			return str.toUpperCase();
		}
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(text={	
			"Uncapitalizes the given string",
			"changing the first letter to lower case"
			},
			parameters = { 
				"string : the string to be uncapitalized"
			},
			example = {
				"$fn.uncapitalize($var) " },
			since = "2.1.0"
			)
	public String uncapitalize(String str) {
		if(str == null || str.length() == 0) {
			return str;
		}
		if(str.length() == 1) {
			return str.toLowerCase();
		}
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}	
}
