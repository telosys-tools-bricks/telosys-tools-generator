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

import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.names.ContextName;
import org.telosys.tools.generic.model.ForeignKeyPart;

/**
 * "$fkPart" object exposed in the generator context
 * 
 * @author Laurent Guerin
 *
 */
//-------------------------------------------------------------------------------------
@VelocityObject(
		contextName = ContextName.FKPART ,
		text = {
				"This object provides a part of Foreign Key for the current attribute",
				""
		},
		since = "3.3.0",
		example= {
				"",
				"#foreach( $fkPart in $attribute.fkParts )",
				"    $fkPart.fkName : $fkPart.referencedEntityName / $fkPart.referencedAttributeName ",
				"#end"				
		}
 )
//-------------------------------------------------------------------------------------
public class ForeignKeyPartInContext
{
	private final String fkName ;
	private final String referencedEntityName ;
	private final String referencedAttributeName ;
	private final ModelInContext modelInContext ;  

	//-------------------------------------------------------------------------------------
	public ForeignKeyPartInContext( ForeignKeyPart fkPart, ModelInContext modelInContext ) {
		super();
		if ( StrUtil.nullOrVoid(fkPart.getFkName()) ) {
			throw new IllegalArgumentException("FK name is null or void");
		}
		if ( StrUtil.nullOrVoid(fkPart.getReferencedEntityName()) ) {
			throw new IllegalArgumentException("FK referenced entity name is null or void");
		}
		if ( StrUtil.nullOrVoid(fkPart.getReferencedAttributeName()) ) {
			throw new IllegalArgumentException("FK referenced attribute name is null or void");
		}
		if ( modelInContext == null ) {
			throw new IllegalArgumentException("model is null");
		}
		this.fkName   = fkPart.getFkName();
		this.referencedEntityName    = fkPart.getReferencedEntityName();
		this.referencedAttributeName = fkPart.getReferencedAttributeName();
		this.modelInContext = modelInContext;
	}
	 
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the name of the Foreign Key"
			}
	)
	public String getFkName() {
		return fkName;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the name of the ENTITY referenced by the Foreign Key"
			}
	)
	public String getReferencedEntityName() {
		return referencedEntityName;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the ENTITY object referenced by the Foreign Key"
			}
	)
	public EntityInContext getReferencedEntity() {
		EntityInContext entity = modelInContext.getEntityByClassName(this.referencedEntityName);
		if ( entity == null ) {
			throw new IllegalStateException(ContextName.FKPART + " : unknown entity '" + this.referencedEntityName + "'");
		}
		return entity;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the name of the ATTRIBUTE referenced by the Foreign Key"
			}
	)
	public String getReferencedAttributeName() {
		return referencedAttributeName;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the ATTRIBUTE object referenced by the Foreign Key"
			}
	)
	public AttributeInContext getReferencedAttribute() {
		EntityInContext entity = getReferencedEntity();
		try {
			return entity.getAttributeByName(this.referencedAttributeName);
		} catch (GeneratorException e) {
			throw new IllegalStateException(ContextName.FKPART + " : unknown attribute '" + this.referencedAttributeName + "'");
		}
	}

	//-------------------------------------------------------------------------------------
	@Override
	public String toString() {
		return fkName + ":" + referencedEntityName + "." + referencedAttributeName ;
	}

}
