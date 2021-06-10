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

import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.names.ContextName;

/**
 * Pair of link attributes (origin and target) exposed in the Velocity Context 
 *  
 * @author L.Guerin
 */
//-------------------------------------------------------------------------------------
@VelocityObject(
		contextName = ContextName.LINK_ATTRIBUTE ,
		text = {
				"This object provides a pair of link attributes",
				"Each pair contains the 'origin attibute' and the 'referenced attibute' ",
				""
		},
		since = "2.1.0",
		example= {
				"",
				"#foreach( $attributesPair in $link.attributes )",
				" from $attributesPair.originAttribute.name ",
				" to   $attributesPair.targetAttribute.name ",
				"#end"				
		}
		
 )
//-------------------------------------------------------------------------------------
public class LinkAttributesPairInContext {
	
	private final AttributeInContext  originAttribute;
	private final AttributeInContext  targetAttribute;

	//-------------------------------------------------------------------------------------
	/**
	 * Constructor
	 * @param originAttribute
	 * @param targetAttribute
	 */
	public LinkAttributesPairInContext(
			AttributeInContext originAttribute, AttributeInContext targetAttribute ) 
	{
		this.originAttribute = originAttribute;
		this.targetAttribute = targetAttribute;		
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the link's origin attribute ",
			"The attribute in the 'owning side' of the link (in the FK if any)"
			}
	)
	public AttributeInContext getOriginAttribute() 
    {
        return originAttribute ;
    }

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the link's target attribute (referenced attribute) ",
			"The attribute in the 'inverse side' of the link "
			}
	)
	public AttributeInContext getTargetAttribute() 
    {
        return targetAttribute ;
    }
}
