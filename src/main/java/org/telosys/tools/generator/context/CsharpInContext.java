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
	contextName=ContextName.CSHARP,
	text = { 
		"Object providing a set of utility functions for C# language code generation",
		""
	},
	since = "4.1.0"
 )
//-------------------------------------------------------------------------------------
public class CsharpInContext {

	@VelocityMethod(
		text={	
			"Returns the C# type with a question mark ('?') at the end if the attribute is nullable",
			"for example: 'string?' if nullable, else 'string' "
			},
		parameters = { 
				"attribute : the attribute (nullable or not nullable)"
			},
		example = {
				"$csharp.nullableType($attribute) " 
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
				// NULLABLE => add "?" after the csharp type
				if ( ! StrUtil.nullOrVoid(type) ) {
					return type + "?" ;
				}
				else {
					return type;
				}
			}
		}
		else {	
			throw new IllegalArgumentException("$csharp.nullableType(attribute) : attribute is null");
		}
	}
	
	//-------------------------------------------------------------------------------------
	// toString METHOD GENERATION
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns a string containing all the code for a C# 'ToString()' method",
			"Generates a 'ToString' method using all the attributes of the given entity",
			"(except non-printable attributes)"
			},
		example={ 
			"$csharp.toStringMethod( $entity, 4 )" },
		parameters = { 
			"entity : the entity for which to generate the 'ToString' method",
			"indentSpaces : number of spaces to be used for each indentation level"},
		since = "4.1.0"
			)
	public String toStringMethod( EntityInContext entity, int indentSpaces ) {
		return toStringMethod(entity, entity.getAttributes(), indentSpaces );
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns a string containing all the code for a C# 'ToString()' method",
			"Generates a 'ToString' method using all the given attributes ",
			"(except non-printable attributes)"
			},
		example={ 
			"$csharp.toStringMethod( $entity, $attributes, 4 )" },
		parameters = { 
			"entity : the entity for which to generate the 'ToString' method",
			"attributes : list of attributes to be used in the 'ToString' method",
			"indentSpaces : number of spaces to be used for each indentation level"},
		since = "4.1.0"
			)
	public String toStringMethod( EntityInContext entity, List<AttributeInContext> attributes, int indentSpaces ) {

    	if ( entity == null ) {
    		throw new IllegalArgumentException("$csharp.toStringMethod(..) : entity arg is null");
    	}
    	if ( attributes == null ) {
    		throw new IllegalArgumentException("$csharp.toStringMethod(..) : attributes arg is null");
    	}
		LinesBuilder lb = new LinesBuilder(indentSpaces) ;
		int indent = 1 ;
		lb.append(indent, "public override string ToString()");
		lb.append(indent, "{");
		indent++;
    	if ( attributes.isEmpty() ) {
    		//--- No attributes
    		lb.append(indent, "return \"" + entity.getName() + " [no attribute]\" ;");
    	}
    	else {
    		//--- Build return concat with all the given attributes 
    		toStringForAttributesWithStringBuilder( entity, attributes, lb, indent );
    	}
		indent--;
		lb.append(indent, "}");
		return lb.toString();
	}
    
	//-------------------------------------------------------------------------------------
    /**
     * Builds the string to be returned using the given attributes
     * @param entity
     * @param attributes
     * @param lb
     * @param indent
     */
    private void toStringForAttributes( EntityInContext entity, List<AttributeInContext> attributes, LinesBuilder lb, int indent  ) 
    {    	
    	if ( null == attributes ) return ;
    	int count = 0 ;
    	for ( AttributeInContext attribute : attributes ) {
    		if ( usableInToString( attribute ) ) {
    			String leftPart ;
                if ( count == 0 ) {
                	// first line
                	leftPart = "return \"" + entity.getName() + " [\"" ; // 'return "EntityName ["' 
                }
                else {
                	// not the first one 
        			leftPart = "+ \"|\"" ; //  '+ "|"'
                }
    			lb.append(indent, leftPart + " + " + attribute.getName() );
    			count++ ;
    		}
    		else {
    			lb.append(indent, "// attribute '" + attribute.getName() + "' not in ToString() ");
    		}
    	}
    	// last line
		lb.append(indent, "+ \"]\" ; "  ); //  '+ "]" ;'
    }
    
    /**
     * Builds the string to be returned using the given attributes
     * @param entity
     * @param attributes
     * @param lb
     * @param indent
     */
    private void toStringForAttributesWithStringBuilder( EntityInContext entity, List<AttributeInContext> attributes, LinesBuilder lb, int indent  ) 
    {    	
    	if ( null == attributes ) return ;
    	int count = 0 ;
    	// first lines
		lb.append(indent, "System.Text.StringBuilder sb = new System.Text.StringBuilder();"); 
		lb.append(indent, "sb.Append(\"" + entity.getName() + "[\");");  // example: sb.Append("Employee[");	
    	for ( AttributeInContext attribute : attributes ) {
    		if ( usableInToString( attribute ) ) {
                if ( count > 0 ) {
                	lb.append(indent, "sb.Append(\"|\");"); // not the first one => append separator before
                }
    			lb.append(indent, "sb.Append(\"" + attribute.getName() + "=\").Append(" + attribute.getName() + ");"); 
    			// example: sb.Append("firstName=").Append(firstName);
    			count++ ;
    		}
    		else {
    			lb.append(indent, "// attribute '" + attribute.getName() + "' (type " + attribute.getType() + ") not usable in ToString() " );
    		}
    	}
    	// last line
    	lb.append(indent, "sb.Append(\"]\");" ); 
		lb.append(indent, "return sb.ToString();" );
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
