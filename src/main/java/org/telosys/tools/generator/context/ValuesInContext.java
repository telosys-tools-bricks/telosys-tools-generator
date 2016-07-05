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

import org.telosys.tools.generator.context.doc.VelocityObject;

/**
 * This object holds a set of generated literal values for the given attributes <br>
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
public abstract class ValuesInContext {
	
	protected final LinkedList<String>  _attributeNames ; // to keep the original list order
	protected final Map<String, String> _values ; // attribute name --> java literal value
	
	//----------------------------------------------------------------------------------------
	/**
	 * Constructor
	 * @param attributes
	 * @param step
	 */
	public ValuesInContext( final List<AttributeInContext> attributes, int step ) {
		_values = new HashMap<String, String>();
		_attributeNames = new LinkedList<String>();
		
		for ( AttributeInContext attrib : attributes ) {
			// Generates and stores the literal value
			_values.put ( attrib.getName() , generateLiteralValue(attrib, step)  ) ;
			// Keep the attribute name
			_attributeNames.add( attrib.getName() );
		}
	}
	
	//----------------------------------------------------------------------------------------
	/**
	 * Generates a literal value for the given attribute
	 * @param attribute
	 * @param step
	 * @return
	 */
	protected abstract String generateLiteralValue(AttributeInContext attribute, int step) ;

	//----------------------------------------------------------------------------------------
	/**
	 * Returns a string containing the value for the given attribute <br>
	 * e.g. : '"AAAA"' or '(short)10' or 'true' etc... <br>
	 * Usage example in Velocity template : $keyValues.getValue($attribute.name) <br>
	 * @param attributeName
	 * @return
	 */
	public abstract String getValue(String attributeName) ;
	
	//----------------------------------------------------------------------------------------
	/**
	 * Returns a string containing all the values separated by a comma. <br>
	 * e.g. : ' "AAAA", (short)10, true ' <br>
	 * Usage example in Velocity template : $keyValues.allValues ( or $keyValues.getAllValues() ) <br>
	 * @return
	 */
	public abstract String getAllValues() ;
	
	//----------------------------------------------------------------------------------------
	protected abstract String valueComparedTo(String entityVariableName, AttributeInContext attribute);
}
