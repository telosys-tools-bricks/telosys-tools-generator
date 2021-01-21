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
import org.telosys.tools.generic.model.ForeignKeyPart;

/**
 * Database Foreign Key Part exposed in the generator context
 * 
 * @author Laurent Guerin
 *
 */
//-------------------------------------------------------------------------------------
@VelocityObject(
		contextName = ContextName.FKCOL ,
		text = {
				"This object provides a part of Foreign Key for the current attribute",
				""
		},
		since = "3.3.0",
		example= {
				"",
				"#foreach( $fkPart in $attrib.fkParts )",
				"    $fkPart.fkName : $fkPart.referencedEntityName / $fkPart.referencedAttributeName ",
				"#end"				
		}
 )
//-------------------------------------------------------------------------------------
public class ForeignKeyPartInContext
{
	private final String fkName ;
	private final String referencedTableName ;
	private final String referencedColumnName ;
	private final String referencedEntityName ;
	private final String referencedAttributeName ;
	
	//-------------------------------------------------------------------------------------
	public ForeignKeyPartInContext( ForeignKeyPart fkPart ) {
		super();
		this.fkName   = fkPart.getFkName();
		this.referencedTableName     = fkPart.getReferencedTable();
		this.referencedColumnName    = fkPart.getReferencedColumn();
		this.referencedEntityName    = fkPart.getReferencedEntity();
		this.referencedAttributeName = fkPart.getReferencedAttribute();
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
			"Returns the name of the TABLE referenced by the Foreign Key"
			}
	)
	public String getReferencedTableName() {
		return referencedTableName;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the name of the COLUMN referenced by the Foreign Key"
			}
	)
	public String getReferencedColumnName() {
		return referencedColumnName;
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
			"Returns the name of the ATTRIBUTE referenced by the Foreign Key"
			}
	)
	public String getReferencedAttributeName() {
		return referencedAttributeName;
	}

}
