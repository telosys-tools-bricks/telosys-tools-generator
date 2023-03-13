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

import java.util.List;

import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.names.ContextName;
import org.telosys.tools.generator.context.tools.LinesBuilder;

//-------------------------------------------------------------------------------------
@VelocityObject(
	contextName=ContextName.PHP,
	text = { 
		"Object providing a set of utility functions for PHP language code generation",
		""
	},
	since = "4.1.0"
 )
//-------------------------------------------------------------------------------------
public class PhpInContext {

	@VelocityMethod(
		text={	
			"Returns the PHP type with a question mark ('?') at the beginning if the attribute is nullable",
			"for example: '?string' if nullable, else 'string' "
			},
		parameters = { 
				"attribute : the attribute (nullable or not nullable)"
			},
		example = {
				"$php.nullableType($attribute) " 
			}
		)
    public String nullableType( AttributeInContext attribute ) {
		if ( attribute != null ) {
			String type = attribute.getType();
			// add "?" if "nullable"			
			if ( attribute.isNotNull() ) {
				// NOT NULL => return type as is : "string", "int", etc
				return type;				
			}
			else {
				// NULLABLE => add "?" before the php type
				if ( ! StrUtil.nullOrVoid(type) ) {
					return "?" + type;
				}
				else {
					return type;
				}
			}
		}
		else {	
			throw new IllegalArgumentException("$php.nullableType(attribute) : attribute is null");
		}
	}
	
	//-------------------------------------------------------------------------------------
	// toString METHOD GENERATION
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns a string containing all the code for a PHP '__toString()' method",
			"Generates a 'toString' method using all the attributes of the given entity",
			"(except non-printable attributes)"
			},
		example={ 
			"$php.toStringMethod( $entity, 4 )" },
		parameters = { 
			"entity : the entity providing the attributes to be used in the 'toString' method",
			"indentSpaces : number of spaces to be used for each indentation level"},
		since = "4.1.0"
			)
	public String toStringMethod( EntityInContext entity, int indentSpaces ) {
		return toStringMethod(entity.getAttributes(), indentSpaces );
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns a string containing all the code for a PHP '__toString()' method",
			"Generates a 'toString' method using all the given attributes ",
			"(except non-printable attributes)"
			},
		example={ 
			"$php.toStringMethod( $attributes, 4 )" },
		parameters = { 
			"attributes : list of attributes to be used in the 'toString' method",
			"indentSpaces : number of spaces to be used for each indentation level"},
		since = "4.1.0"
			)
	public String toStringMethod( List<AttributeInContext> attributes, int indentSpaces ) {

    	if ( attributes == null ) {
    		throw new IllegalArgumentException("$php.toStringMethod(..) : attributes arg is null");
    	}
		LinesBuilder lb = new LinesBuilder(indentSpaces) ;
		int indent = 1 ;
		lb.append(indent, "public function __toString() { ");
		indent++;
    	if ( attributes.isEmpty() ) {
    		lb.append(indent, "return \"\" ;");
    	}
    	else {
    		lb.append(indent, "return ");
    		//--- All the given attributes 
   			toStringForAttributes( attributes, lb, indent );
    	}
		indent--;
		lb.append(indent, "}");
		return lb.toString();
	}
    
	//-------------------------------------------------------------------------------------
    /**
     * Uses the given attributes except if their type is not usable   
     * @param attributes
     * @param lb
     * @param indent
     * @return
     */
    private void toStringForAttributes( List<AttributeInContext> attributes, LinesBuilder lb, int indent  ) 
    {    	
    	if ( null == attributes ) return ;
    	int count = 0 ;
    	for ( AttributeInContext attribute : attributes ) {
    		if ( usableInToString( attribute ) ) {
    			String leftPart ;
                if ( count == 0 ) {
                	// first line
                	leftPart = "  \"[\" . " ;
                }
                else {
                	// not the first one => add separator
        			leftPart = ". \"|\" . " ;
                }
    			lb.append(indent, leftPart + "$this->" + attribute.getName() );
    			count++ ;
    		}
    		else {
    			lb.append(indent, "// attribute '" + attribute.getName() + "' not in toString ");
    		}
    	}
    	// last line
		lb.append(indent, ". \"]\" ; "  );
    }
    
    /**
     * Returns true if the given type is usable in a 'toString' method
     * @param sType
     * @return
     */
    private boolean usableInToString( AttributeInContext attribute ) {
    	return ! attribute.isBinaryType() && ! attribute.isLongText() ;
    }
	
}
