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

import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.names.ContextName;

/**
 * Link exposed in the Velocity Context 
 *  
 * @author L.Guerin
 */
//-------------------------------------------------------------------------------------
@VelocityObject(
		contextName = ContextName.LINK_ATTRIBUTE ,
		text = {
				"This object provides a link attribute",
				"Each link attribute contains the 'origin attibute' and the 'referenced attibute' ",
				""
		},
		since = "2.1.0",
		example= {
				"",
				"#foreach( $linkAttribute in $link.attributes )",
				" from $linkAttribute.originAttribute.name ",
				" to   $linkAttribute.targetAttribute.name ",
				"#end"				
		}
		
 )
//-------------------------------------------------------------------------------------
public class LinkAttributeInContext {
	
	private final AttributeInContext  _originAttribute;
	private final AttributeInContext  _targetAttribute;

	//-------------------------------------------------------------------------------------
	/**
	 * Constructor
	 * @param originAttribute
	 * @param targetAttribute
	 */
	public LinkAttributeInContext(
			final AttributeInContext originAttribute, final AttributeInContext targetAttribute ) 
	{
		this._originAttribute = originAttribute;
		this._targetAttribute = targetAttribute;		
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the link's origin attribute "
			}
	)
	public AttributeInContext getOriginAttribute() 
    {
        return _originAttribute ;
    }

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the link's target attribute (referenced attribute) "
			}
	)
	public AttributeInContext getTargetAttribute() 
    {
        return _targetAttribute ;
    }
}
