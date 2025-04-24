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
package org.telosys.tools.generator.context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.commons.XmlUtil;
import org.telosys.tools.generator.GenerationCancellationException;
import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityNoDoc;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.doc.VelocityReturnType;
import org.telosys.tools.generator.context.exceptions.GeneratorFunctionException;
import org.telosys.tools.generator.context.names.ContextName;
import org.telosys.tools.generator.engine.GeneratorContext;

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
public class FnInContext {

	private final GeneratorContext generatorContext ;
	private final EnvInContext     env ;
	
	/**
	 * Constructor
	 * @param generatorContext
	 * @param env
	 */
	public FnInContext(GeneratorContext generatorContext, EnvInContext env ) {
		super();
		this.generatorContext = generatorContext;
		this.env = env ;
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
			return s.trim().length() == 0;
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
	@VelocityMethod(
		text={	
			"Removes the double quote character at the beginning and at the end of the given string (if any)",
			"If the string is not quoted it is returned as is",
			"If there's a quote character only at the beginning or at the end the string is returned as is",
			""
			},
		parameters = { "s : the string to be unquoted" }
			)
	public String unquote(String s) {
		return StrUtil.removeQuotes(s, '"') ;
	}

	/**
	 * Returns the given string with a backslash before each given char
	 * @param s
	 * @return
	 */
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Protects each occurrence of the given char with a backslash in the given string "
			},
		example={ 
			"$fn.backslash( $myVar, \"$\" )"
			},
		parameters = { 
			"s : the string to be processed",
			"c : string containing a single character (the character to be protected with a backslash)" 
			}
	)
	public String backslash(String s, String c) {
		if ( c.length() != 1 ) {
			throw new GeneratorFunctionException("backslash", "invalid arg 2 : single character expected ");
		}
		char c2 = c.charAt(0);
		return StrUtil.backslash(s, c2);
	}

	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the XML string for the given string",
			"Replaces special characters (&, <, >, etc) by their corresponding XML notation "
			},
		parameters = { 
			"s : the string to be escaped" 
			}
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
	@VelocityMethod(
		text={	
			"Returns N tabulation characters "
			},
		parameters = { 
			"n : the number of tabulations to be returned" 
			}
	)
	public String tab(int n) {
		StringBuilder sb = new StringBuilder();
		for ( int i = 0 ; i < n ; i++ ) {
			sb.append("\t");
		}
		return sb.toString();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns a string containing a list of field names separated by a comma",
			"The resulting string depends on the current target language (eg: args are different for Java and PHP) ",
			"NB: do not forget to define the current language : ",
			"#set ( $env.language = '..' ) "
			},
		example={ 
			"$fn.argumentsList( $entity.attributes )",
			"Result example : 'id, firstName, lastName, age' (for most languages) ",
			"Result example : '$id, $firstName, $lastName, $age' (for languages with '$' prefix) "},
		parameters = { 
			"attributes : list of attributes to be added in the arguments list" },
		since = "2.0.5"
			)
	public String argumentsList( List<AttributeInContext> attributes ) {
		// since v 4.1.0 all the job is done in TargetLanguage
		if ( attributes != null ) {
			return env.getTargetLanguage().argumentsList(attributes);
		} 
		else {
			return "" ;
		}
	}


	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={
			"Returns a string containing a list of field names separated by a comma",
			"The resulting string depends on the current target language (eg: args are different for Java and PHP) ",
			"NB: do not forget to define the current language : ",
			"#set ( $env.language = '..' ) "
		},
		example={
			"$fn.argumentsListDbName( $entity.attributes )",
			"Result example : 'id, firstName, lastName, age' (for most languages) ",
			"Result example : '$id, $firstName, $lastName, $age' (for languages with '$' prefix) "},
		parameters = {
			"attributes : list of attributes to be added in the arguments list" },
		since = "2.0.5"
	)
	public String argumentsListDbName( List<AttributeInContext> attributes ) {
		// since v 4.1.0 all the job is done in TargetLanguage
		if ( attributes != null ) {
			return env.getTargetLanguage().argumentsListDbName(attributes);
		}
		else {
			return "" ;
		}
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns a string containing a list of arguments (usually a list of 'type and name' separated by a comma)",
			"The resulting string depends on the current target language (eg: args are different for Java and Go) ",
			"NB: do not forget to define the current language : ",
			"#set ( $env.language = '..' ) "
			},
		example={ 
			"$fn.argumentsListWithType( $entity.attributes )",
			"Result example for Java language : 'int id, String firstName, String lastName, int age' ",
			"Result example for Go language : 'id int32, firstName string, lastName string, age uint' "},
		parameters = { 
			"attributes : list of attributes to be added in the arguments list" },
		since = "2.0.5"
			)
	public String argumentsListWithType( List<AttributeInContext> attributes )  {
		// since v 4.1.0 all the job is done in TargetLanguage
		if ( attributes != null ) {
			return env.getTargetLanguage().argumentsListWithType(attributes);
		} 
		else {
			return "" ;
		}
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns a string containing a list of fields (wrapper type and name) separated by a comma",
			"The resulting string depends on the current target language (some languages don't have wrappers) ",
			"NB: do not forget to define the current language : ",
			"#set ( $env.language = '..' ) "
			},
		example={ 
			"$fn.argumentsListWithWrapperType( $entity.attributes )",
			"Returns : 'Integer id, String firstName, String lastName, Integer age' "},
		parameters = { 
			"attributes : list of attributes to be added in the arguments list" },
		since = "3.0.0"
			)
	public String argumentsListWithWrapperType( List<AttributeInContext> attributes )  {
		if ( attributes != null ) {
			// since v 4.1.0 all the job is done in TargetLanguage
			return env.getTargetLanguage().argumentsListWithWrapperType(attributes);
		} 
		else {
			return "" ;
		}
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={
			"Returns a string containing a list of fields (wrapper type and name) separated by a comma",
			"The resulting string depends on the current target language (some languages don't have wrappers) ",
			"NB: do not forget to define the current language : ",
			"#set ( $env.language = '..' ) "
		},
		example={
			"$fn.argumentsListDbNameWithWrapperType( $entity.attributes )",
			"Returns : 'Integer id, String firstName, String lastName, Integer age' "},
		parameters = {
			"attributes : list of attributes to be added in the arguments list" },
		since = "2025-04-10"
	)
	public String argumentsListDbNameWithWrapperType( List<AttributeInContext> attributes )  {
		if ( attributes != null ) {
			// since v 4.1.0 all the job is done in TargetLanguage
			return env.getTargetLanguage().argumentsListDbNameWithWrapperType(attributes);
		}
		else {
			return "" ;
		}
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns a string containing a list of fields getters separated by a comma",
			"The resulting string depends on the current target language (some languages don't have getters) ",
			"NB: do not forget to define the current language : ",
			"#set ( $env.language = '..' ) "
			},
		example={ 
			"$fn.argumentsListWithGetter( 'person', $entity.attributes )",
			"Returns : 'person.getId(), person.getFirstName(), person.getLastName(), person.getAge()' "},
		parameters = { 
			"objectName : name of the object providing the getters",
			"attributes : list of attributes to be added in the arguments list" },
		since = "2.0.5"
			)
	public String argumentsListWithGetter( String objectName, List<AttributeInContext> attributes )  {
		if ( attributes != null ) {
			// since v 4.1.0 all the job is done in TargetLanguage
			return env.getTargetLanguage().argumentsListFromObjectWithGetter(objectName, attributes);
		} 
		else {
			return "" ;
		}
	}
	
	//==============================================================================================
	// Version 2.0.7
	//==============================================================================================
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
			text={	
				"Returns TRUE if the given list/map/array is NOT VOID"
				},
			example={ 
				"#if ( $fn.isNotVoid( $entity.attributes ) ) "
				},
			parameters = { 	"list/map/array" },
			since = "2.0.7"
				)
	public boolean isNotVoid(Object o) {
		return ! isVoid(o, "isNotVoid" ) ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
			text={	
				"Returns TRUE if the given list/map/array is VOID"
				},
			example={ 
				"#if ( $fn.isVoid( $entity.attributes ) ) "
				},
			parameters = { 	"list/map/array" },
			since = "2.0.7"
				)
	public boolean isVoid(Object o) { 
		return isVoid(o, "isVoid" ) ;
	}

	//-------------------------------------------------------------------------------------
	private boolean isVoid(Object o, String functionName ) {
		if ( o != null ) {
			if ( o instanceof Collection ) {
				return ((Collection<?>) o).isEmpty() ;
			}
			else if ( o instanceof Map ) {
				return ((Map<?,?>) o).isEmpty() ;
			}
			else if ( o instanceof Object[] ) {
				return ((Object[]) o).length == 0;
			}
			else {
				throw new GeneratorFunctionException(functionName, "invalid arg : list/map/array expected");
			}
		}
		else {
			throw new GeneratorFunctionException(functionName, "argument is null");
		}
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
			text={	
				"Returns the SIZE of the given list/map/array"
				},
			example={ 
				"Number of attribute = $fn.size( $entity.attributes )  "
				},
			parameters = { 	"list/map/array" },
			since = "2.0.7"
				)
	public int size(Object o) { 
		if ( o != null ) {
			if ( o instanceof Collection ) {
				return ((Collection<?>) o).size() ;
			}
			else if ( o instanceof Map ) {
				return ((Map<?,?>) o).size() ;
			}
			else if ( o instanceof Object[] ) {
				return ((Object[]) o).length ;
			}
			else {
				throw new GeneratorFunctionException("size", "invalid arg : list/map/array expected");
			}
		}
		else {
			throw new GeneratorFunctionException("size", "argument is null");
		}
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the class name (CanonicalName) for the given object (or 'null' if undefined)"
		},
		parameters = { 
			"object : object reference for which to get the class name"
		},
		example = {
			"$fn.className($var)" 
		},
		since = "3.3.0"
		)
	public String className(Object o) { 
		if ( o != null ) {
			return o.getClass().getCanonicalName();
		}
		else {
			return "null";
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
		parameters = {
			"list1 : List of objects", 
			"list2 : List of objects to be added at the end of list1"  
		},
		since = "2.0.7"
		)
	public List<?> concatLists(List<?> list1, List<?> list2)  {
		List<Object> finalList = new LinkedList<>();
		finalList.addAll(list1);
		finalList.addAll(list2);
		return finalList ;
	}
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Converts  all of the characters in the given string to upper case"
		},
		parameters = { 
			"s : the string to be converted" 
		},
		since = "2.0.7"
		)
	public String toUpperCase(String s) {
		if ( s != null ) {
			return s.toUpperCase();
		}
		return "";
	}
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Converts  all of the characters in the given string to lower case"
		},
		parameters = { 
			"s : the string to be converted" 
		},
		since = "2.0.7"
		)
	public String toLowerCase(String s) {
		if ( s != null ) {
			return s.toLowerCase();
		}
		return "";
	}
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Converts the first character to upper case"
		},
		parameters = { 
			"s : the string to be converted" 
		},
		since = "2.0.7"
		)
	public String firstCharToUpperCase(String s) {
		if ( s != null ) {
			return StrUtil.firstCharUC( s );
		}
		return "";
	}

	//==============================================================================================
	// Version 2.1.0
	//==============================================================================================
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the given object name is defined in the Velocity Context"
		},
		parameters = { 
			"objectName : the name (or key) in the Velocity Context" 
		},
		example = {
			"#if ( $fn.isDefined('myvar') ) "
		},
		since = "2.1.0"
		)
	public boolean isDefined(String objectName) {
		Object o = generatorContext.get(objectName);
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
		Object o = generatorContext.get(objectName);
		return ( o != null ? o : defaultValue );
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(text={	
			"Capitalizes the given string",
			"changing the first letter to upper case"
			},
			parameters = { 
				"s : the string to be capitalized"
			},
			example = {
				"$fn.capitalize($var)" 
			},
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
				"s : the string to be uncapitalized"
			},
			example = {
				"$fn.uncapitalize($var) " 
			},
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


	//-------------------------------------------------------------------------------------
	@VelocityMethod(text={
		"Replaces a trailing hash (#) with Num"
	},
		parameters = {
			"s : the string to trimmed"
		},
		example = {
			"$fn.replaceHash($var) "
		},
		since = "2025-04-23"
	)
	public String replaceHash(String str) {
		if(str == null || str.length() == 0) {
			return str;
		}
		if (str.endsWith("#")) {
			if(str.length() == 1 && str.charAt(0) == '#') {
				return "Num";
			}
			return str.substring(0, str.length()-1) + "Num";
		}
		return str;
	}


	//==============================================================================================
	// Version 2.1.1
	//==============================================================================================
	//-------------------------------------------------------------------------------------
	@VelocityMethod(text={	
			"Builds a list of literal values.",
			"Returns a list with one literal value for each attribute according with the attribute type.",
			"e.g. : 12 for an integer, true for a boolean,  230L for a long, 'ABC' for a string, etc ",
			"Each value can be retrieved by its attribute's name, e.g. $values.getValue($attribute.name)",
			"A list of all values separated by a comma is returned by $values.allValues",
			"Those values are typically used to populate attributes in test cases"
			},
			parameters = { 
				"attributes : list of attributes requiring a literal value",
				"step : a step (from 1 to N) used to change the values builded "
			},
			example = {
			"#set( $values = $fn.buildValues($entity.attributes, 1) )",
			"#foreach( $attribute in $entity.attributes )",
			" Literal value for $attribute.name : $values.getValue($attribute.name)",
			"#end",
			" All values : $values.allValues"
			},
			since = "2.1.1"
			)
	public ValuesInContext buildValues(final List<AttributeInContext> attributes, final int step) {
		return new ValuesInContext( attributes, step, env ) ;
	}	
	
	/*** ORIGINAL METHOD DEFINED IN SPECIFIC CLASS
	public List<Object> randomKeyAttributesValues(EntityInContext entity) {
		List<AttributeInContext> keys = entity.getKeyAttributes();
		if(keys == null) {
			return new ArrayList<Object>();
		}
		List<Object> values = new ArrayList<Object>();
		int i=1;
		for(AttributeInContext key : keys) {
			values.add(String.valueOf(i));
			i++;
		}
		return values;
	}
	***/
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(text={	
			"Builds a list of N integer consecutive values starting with the given value"
			},
			parameters = { 
				"n : the number of values to be created",
				"firstValue : the first value of the list"
			},
			example = {
				"#set ( $values = $fn.buildIntValues(10,0) ) ## 10 values from 0 to 9",
				"Values size = $values.size() ",
				"#foreach( $v in $values )",
				" . value = $v",
				"#end",
				"#set($last = ( $values.size() - 1) )",
				"#foreach ( $i in [0..$last] )",
				" . value($i) = $values.get($i)" ,
				"#end"
			},
			since = "3.0.0"
			)
	public List<Integer> buildIntValues(final int n, final int firstValue) {
		List<Integer> values = new ArrayList<>();
		int currentValue = firstValue ;
		for ( int i = 0 ; i < n ; i++ ) {
			values.add( Integer.valueOf(currentValue) );
			currentValue++;
		}
		return values ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Builds a list of N integer consecutive values starting with 1"
		},
		parameters = { 
			"n : the number of values to be created"
		},
		example = {
			"#set ( $values = $fn.buildIntValues(5) ) ## 5 values from 1 to 5",
			"Values size = $values.size() ",
			"#foreach( $v in $values )",
			" . value = $v",
			"#end",
			"#set($last = ( $values.size() - 1) )",
			"#foreach ( $i in [0..$last] )",
			" . value($i) = $values.get($i)" ,
			"#end"
		},
		since = "3.0.0"
		)
	public List<Integer> buildIntValues(final int n) {
		return buildIntValues(n, 1);
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Builds a list of N integer consecutive values starting with the given value"
		},
		parameters = { 
			"collection : the collection determining the number of values (collection size)",
			"firstValue : the first value of the list"
		},
		example = {
			"#set ( $values = $fn.buildIntValues($entity.attributes, 0) ) ",
			"Values size = $values.size() ",
			"#foreach( $v in $values )",
			" . value = $v",
			"#end",
			"#set($last = ( $values.size() - 1) )",
			"#foreach ( $i in [0..$last] )",
			" . value($i) = $values.get($i)" ,
			"#end"
		},
		since = "3.0.0"
		)
	public List<Integer> buildIntValues(final Collection<?> collection, final int firstValue) {
		return buildIntValues(collection.size(), firstValue);
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(text={	
			"Builds a list of N integer consecutive values starting with 1"
			},
			parameters = { 
				"collection : the collection determining the number of values (collection size)"
			},
			example = {
				"#set ( $values = $fn.buildIntValues($entity.attributes) ) ",
				"Values size = $values.size() ",
				"#foreach( $v in $values )",
				" . value = $v",
				"#end",
				"#set($last = ( $values.size() - 1) )",
				"#foreach ( $i in [0..$last] )",
				" . value($i) = $values.get($i)" ,
				"#end"
			},
			since = "3.0.0"
			)
	public List<Integer> buildIntValues(final Collection<?> collection) {
		return buildIntValues(collection.size(), 1);		
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(text={	
			"Returns a list of strings from the given array of strings"
			},
			parameters = { 
				"array : the array of strings ( String[] )"
			},
			example = {
				"#set ( $mylist = $fn.toList( $mystring.split(\",\") ) ) "
			},
			since = "3.3.0"
			)
	public List<String> toList(String[] array) {
		return Arrays.asList(array);
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(text={	
			"Returns a string built by joining all the elements of the given collection",
			"the 'separator' is added between each element"
			},
			parameters = { 
				"collection : the collection (any kind of objects : Collection<?>)",
				"separator  : the separator string (void string if no separator required) "
			},
			example = {
				"#set ( $v = $fn.join( $myList, \",\" ) ) ",
				"#set ( $v = $fn.join( $myList, \"\" ) ) "
			},
			since = "3.3.0"
			)
	public String join(Collection<?> collection, String separator) {
		return joinWithPrefixSuffix(collection, separator, null, null);
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns a string built by joining all the elements of the given collection",
			"the 'separator' is added between each element",
			"the 'prefix' is added before each element",
			"the 'suffix' is added after each element"
		},
		parameters = { 
			"collection : the collection (any kind of objects : Collection<?>)",
			"separator  : the separator string (or void) ",
			"prefix     : the prefix string (or void) ",
			"suffix     : the suffix string (or void) "
		},
		example = {
			"#set ( $v = $fn.joinWithPrefixSuffix( $myList, \";\", \"[\", \"]\" ) ) ",
			"#set ( $v = $fn.joinWithPrefixSuffix( $myList, \", \", \"\", \".class\" ) ) "
		},
		since = "3.3.0"
		)
	public String joinWithPrefixSuffix(Collection<?> collection, String separator, 
			String prefix, String suffix) {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		if ( collection != null ) {
			for ( Object o : collection ) {
				if ( o != null ) {
					i++;
					if ( separator != null && i > 1) {
						sb.append(separator);
					}
					if ( prefix != null ) {
						sb.append(prefix);
					}
					sb.append(o.toString());
					if ( suffix != null ) {
						sb.append(suffix);
					}
				}
			}
		}
		return sb.toString();
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(text={	
			"Replaces all occurrences of an element with another in the given list"
			},
			parameters = { 
				"list : the list containing the elements",
				"oldElement : the element to be replaced ",
				"newElement : the new element replacing the original "
			},
			example = {
				"#set($list = ['a', 'b', 'cc', 'b', 'dd']) ",
				"$fn.replaceInList($list, 'b', 'BBB')##",
				" result : [a, BBB, cc, BBB, dd]",
				"",
				"#set($foo = [1, 0, 3, 0, 4, 0]) ",
				"$fn.replaceInList($foo, 0, 9)##",
				" result : [1, 9, 3, 9, 4, 9]",
				"",
				"#set($foo = [1, 'aa', 3.2, 'zz', true, 5]) ",
				"$fn.replaceInList($foo, 'zz', 99)##",
				" result : [1, aa, 3.2, 99, true, 5] ",
				""
			},
			since = "3.3.0"
			)	
	public <T> void replaceInList(List<T> list, T oldElement, T newElement) {
		if ( list != null && oldElement != null && newElement != null) {
			for ( int i = 0 ; i < list.size() ; i++) {
				Object o = list.get(i);
				if ( oldElement.equals(o) ) {
					list.set(i, newElement);
				}
			}
		}
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(text={	
			"Trims all string occurrences in the given list"
			},
			parameters = { 
				"list : the list containing the elements"
			},
			example = {
				"#set($list = ['a', '  b', ' c c ', 'd  ']) ",
				"$fn.trimAll($list)##",
				" result : [a, b, c c, d ]",
				"",
				"#set($foo = [1, ' a ', 3.2, '  zz' ]) ",
				"$fn.trimAll($foo)##",
				" result : [1, a, 3.2, zz] ",
				""
			},
			since = "3.3.0"
			)	
	@SuppressWarnings("unchecked")
	public <T> void trimAll(List<T> list) {
		if ( list != null ) {
			for ( int i = 0 ; i < list.size() ; i++) {
				Object o = list.get(i);
				if ( o instanceof String ) {
					String s = (String) o;
					list.set(i, (T)s.trim());
				}
			}
		}
	}
	//-------------------------------------------------------------------------------------
	// File management ( v 3.3.0 )
	//-------------------------------------------------------------------------------------
	@VelocityMethod(text={	
			"Returns a file object for the given file path"
		},
		parameters = { 
			"filePath : the file path (relative or absolute)",
			"if the file path is relative the current project folder is used as root path "
			},
		example = {
			"## relative path : for a file located in the current project : ",
			"#set( $file = $fn.file('myfile.csv') )",
			"#set( $file = $fn.file('dir/foo.txt') )",
			"## absolute path : for a file located anywhere : ",
			"#set( $file = $fn.file('C:\\Temp\\values.csv') )",
			"#set( $file = $fn.file('/tmp/values.csv') )"
		},
		since = "3.3.0"
		)
	public FileInContext file (String filePath) {
		FnFileUtil f = new FnFileUtil("file", generatorContext);
		return f.file(filePath);
	}
	
	@VelocityMethod(text={
			"Returns a file object for the given file path located in the current bundle folder"
		},
		parameters = { 
			"filePath : the file path (in the current bundle folder)"
			},
		example = {
			"#set( $file = $fn.fileFromBundle('myfile.csv') )",
			"#set( $file = $fn.fileFromBundle('dir/foo.txt') )"
		},
		since = "3.3.0"
		)
	public FileInContext fileFromBundle(String filePath) {
		FnFileUtil f = new FnFileUtil("fileFromBundle", generatorContext);
		return f.fileFromBundle(filePath);
	}

	@VelocityMethod(text={
			"Returns a file object for the given file path located in the current model folder"
		},
		parameters = { 
			"filePath : the file path (in the current model folder)"
			},
		example = {
			"#set( $file = $fn.fileFromModel('myfile.csv') )",
			"#set( $file = $fn.fileFromModel('dir/foo.txt') )"
		},
		since = "3.3.0"
		)
	public FileInContext fileFromModel(String filePath) {
		FnFileUtil f = new FnFileUtil("fileFromModel", generatorContext);
		return f.fileFromModel(filePath);
	}

	//-------------------------------------------------------------------------------------
	// TEST / THROW EXCEPTION ( v 3.3.0 )
	//-------------------------------------------------------------------------------------
	@VelocityNoDoc
	public void cancelGeneration(String message) throws GenerationCancellationException {
		throw new GenerationCancellationException(message);
	}
	@VelocityNoDoc
	public void throwGeneratorException(String message) throws GeneratorException{
		throw new GeneratorException(message);
	}

}
