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
import org.telosys.tools.generator.context.doc.VelocityObject;
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
		contextName = "no_name_in_context" ,
		text = { 
				"xxxx",
				""
		},
		since = "2.0.0"
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
	/**
	 * Returns the size of the values list
	 * @return
	 */
	public int size() {
		return values.size();
	}
	
	//----------------------------------------------------------------------------------------
	/**
	 * Returns a string containing the literal value for the given attribute's name <br>
	 * e.g. : '"AAAA"' or '(short)10' or 'true' etc... <br>
	 * <br>
	 * Usage example in Velocity template : <br>
	 *   $values.getValue($attribute.name) <br>
	 *   
	 * @param attributeName
	 * @return
	 */
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
	/**
	 * Returns a string containing all the literal values separated by a comma. <br>
	 * e.g. : ' "AAAA", (short)10, true ' <br>
	 * <br>
	 * Usage example in Velocity template : <br>
	 *   $values.allValues <br>
	 *   or <br>
	 *   $values.getAllValues()  <br>
	 *   
	 * @return
	 */
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
