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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.names.ContextName;
import org.telosys.tools.generic.model.types.LanguageType;
import org.telosys.tools.generic.model.types.LiteralValuesProvider;

/**
 * This object holds a set of generated literal values for the given attributes <br>
 * Values instances are created by '$fn.builValues' <br>
 * Example : <br>
 *   #set( $values = $fn.buildValues($entity.attributes, 1) ) <br>
 * 
 * @author Laurent GUERIN
 *
 */
//-------------------------------------------------------------------------------------
@VelocityObject(
		contextName = ContextName.VALUES,
		otherContextNames= { ContextName.KEY_VALUES, ContextName.DATA_VALUES },		
		text = { 
				"This object provides a set literal values",
				"Each literal value is associated with an attribute's name and can be assigned to this attribute",
				""
		},
		since = "3.0.0",
		example= {
				"",
				"#set( $values = $fn.buildValues($entity.attributes, 1) )",
				"count = $values.size()",
				"#foreach( $attribute in $entity.attributes )",
				"  Value for attribute \"${attribute.name}\" = $values.getValue($attribute.name) ",
				"#end"
		}		
 )
//-------------------------------------------------------------------------------------
public class ValuesInContext {
	
	private final LiteralValuesProvider  literalValuesProvider ;
	private final LinkedList<String>     attributeNames ; // to keep the original list order
	private final Map<String, String>    values ; // attribute name --> java literal value
	private final String                 nullLiteral ;
	
	//----------------------------------------------------------------------------------------
	/**
	 * Constructor
	 * @param attributes
	 * @param step
	 * @param env
	 */
	protected ValuesInContext( List<AttributeInContext> attributes, int step, EnvInContext env ) {
		
		// this.literalValuesProvider = new LiteralValuesProviderForJava() ; 
		this.literalValuesProvider = env.getLiteralValuesProvider() ; 
		
		values = new HashMap<String, String>();
		attributeNames = new LinkedList<String>();
		
		for ( AttributeInContext attrib : attributes ) {
			// Generates and stores the literal value
			//_values.put ( attrib.getName() , generateLiteralValue(attrib, step)  ) ;
			LanguageType languageType = attrib.getLanguageType() ;
			int maxLength = StrUtil.getInt(attrib.getMaxLength(), 1) ;
			String literalValue = literalValuesProvider.generateLiteralValue(languageType, maxLength, step);
			values.put ( attrib.getName(), literalValue ) ;
			// Keep the attribute name
			attributeNames.add( attrib.getName() );
		}
		//this.nullLiteral = nullLiteral ;
		this.nullLiteral = literalValuesProvider.getLiteralNull() ;
	}
	
	//----------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the size of the values list (the number of values)"
			},
		since = "3.0.0"
	)
	public int size() {
		return values.size();
	}
	
	//----------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns a string containing the literal value for the given attribute's name ",
			"e.g. for Java : '\"AAAA\"' or '(short)10' or 'true' etc...  ",
			" ",
			"Usage example in Velocity template :",
			" $values.getValue($attribute.name) ",
			" "
			},
		parameters = { 
			"attributeName : the name of the attribute  " 
			},
		since = "3.0.0"
	)
	public String getValue(String attributeName) {
		String value = values.get(attributeName) ;
		if ( value != null ) {
			return value;
		}
		else {
			return nullLiteral ;
		}
	}
	
	//----------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns a string containing all the literal values separated by a comma. ",
			"e.g. for Java : ' \"AAAA\", (short)10, true '  ",
			" ",
			"Usage example in Velocity template :",
			" $values.allValues  ",
			" or  ",
			" $values.getAllValues()  ",
			" "
			},
		since = "3.0.0"
	)
	public String getAllValues() {
		StringBuilder sb = new StringBuilder();
		int n = 0 ;
		for ( String name : attributeNames ) {
			if ( n > 0 ) {
				sb.append(", ");
			}
			sb.append(getValue(name));
			n++ ;
		}
		return sb.toString();
	}
	
	//----------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns a string containing ALL the literal values in JSON format. ",
			"NB : this method is usable only with 'JAVASCRIPT values'.",
			"e.g. : '{\"id\":1, \"name\":\"AAAA\"} ",
			" ",
			"Usage examples in Velocity template :",
			" $values.toJSON()  ",
			" $values.toJSON(\"${NEWLINE}\")  ",
			" $values.toJSON(\"${NEWLINE}${TAB}\", \"${NEWLINE}\")  ",
			" "
			},
		parameters = { 
			"separator1 : the separator to be put before each value (optional) ",
			"separator2 : the separator to be put before the ending '}' (optional) "
			},
		since = "3.0.0"
	)
	public String toJSON() {
		return buildJSON(attributeNames, null, null);
	}
	public String toJSON(String separator1) {
		return buildJSON(attributeNames, separator1, null);
	}
	public String toJSON(String separator1, String separator2) {
		return buildJSON(attributeNames, separator1, separator2);
	}

	//----------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns a string containing the literal values in JSON format for the given list of attributes. ",
			"NB : this method is usable only with 'JAVASCRIPT values'.",
			"e.g. : '{\"id\":1, \"name\":\"AAAA\"} ",
			" ",
			"Usage example in Velocity template :",
			" $values.toJSON($entity.attributes) ",
			" $values.toJSON($entity.attributes, \"${NEWLINE}\")  ",
			" $values.toJSON($entity.attributes, \"${NEWLINE}${TAB}\", \"${NEWLINE}\")  ",
			" "
			},
		parameters = { 
			"attributes : list of attributes to be put in the JSON string ",
			"separator1 : the separator to be put before each value (optional) ",
			"separator2 : the separator to be put before the ending '}' (optional) "
			},
		since = "3.0.0"
	)
	public String toJSON(List<AttributeInContext> attributes ) {
		return buildJSON(buildNames(attributes), null, null);
	}
	public String toJSON(List<AttributeInContext> attributes, String separator1) {
		return buildJSON(buildNames(attributes), separator1, null);
	}
	public String toJSON(List<AttributeInContext> attributes, String separator1, String separator2) {
		return buildJSON(buildNames(attributes), separator1, separator2);
	}

	private List<String> buildNames(List<AttributeInContext> attributes) {
		List<String> names = new LinkedList<String>() ;
		for ( AttributeInContext attrib : attributes ) {
			names.add(attrib.getName());
		}
		return names;
	}
	private String buildJSON(List<String> names, String separator1, String separator2) {
		StringBuilder sb = new StringBuilder();
		int n = 0 ;
		sb.append("{");
		for ( String name : names ) {
			if ( n > 0 ) {
				sb.append(", ");
			}
			if ( separator1 != null ) {
				sb.append(separator1);
			}
			sb.append("\"" + name + "\"" );
			sb.append(":");
			sb.append(getValue(name));
			n++ ;
		}
		if ( separator2 != null ) {
			sb.append(separator2);
		}
		sb.append("}");
		return sb.toString();
	}
	
	//----------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns a comparison statement between the attribute's current value and its associated literal value ",
			"Example of strings returned for Java language :  ",
			"  book.getId() == 100 ",
			"  book.getFirstName().equals(\"abcd\") ",
			" ",
			"Usage example in Velocity template :",
			"  $values.comparisonStatement(\"book\", $attribute)   ",
			" "
			},
		parameters = { 
				"entityVariableName : the variable name used before the 'getter' ",
				"attribute : the attribute instance (used to retrieve the 'getter' and the 'literal value')"
			},
		since = "3.0.0"
	)
	/**
	 * Returns a comparison statement between the attribute's current value and its associated literal value <br>
	 * Example of strings returned for Java language : <br>
	 *   book.getId() == 100  <br>
	 *   book.getFirstName().equals("abcd") <br>
	 * Usage example in Velocity template : <br>
	 *   $values.comparisonStatement("book", $attribute) <br>
	 *   or <br>
	 *   
	 * @param entityVariableName the variable name used in the 'left part' with the 'getter'
	 * @param attribute the attribute to be used to retrieve the 'right part' (the 'literal value')
	 * @return
	 */
	public String comparisonStatement(String entityVariableName, AttributeInContext attribute) {
		StringBuilder sb = new StringBuilder();
		sb.append( entityVariableName ) ;
		sb.append( "." ) ;
		sb.append( attribute.getGetter() ) ;
		sb.append( "()" ) ;
		
		String value = values.get( attribute.getName() ) ; // Value for the given attribute

		String equalsStatement = literalValuesProvider.getEqualsStatement(value, attribute.getLanguageType() );
		
		sb.append( equalsStatement ) ;
		
		return sb.toString();

	}
}
