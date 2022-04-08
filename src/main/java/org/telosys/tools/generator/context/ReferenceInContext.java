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

//-------------------------------------------------------------------------------------
@VelocityObject(
		contextName=ContextName.REFERENCE,
		text = { 
				"Entity referenced by another entity  ",
				"Provides references count for 'toMany' and 'toOne' links",
				""
		},
		since = "3.4.0",
		example= {
				"",
				"#foreach($reference in $entity.references )",
				"  ${reference.entityName}",
				"  ${reference.toOneCount}",
				"#end",
				""
		}		
)
//-------------------------------------------------------------------------------------
public class ReferenceInContext {

	private final EntityInContext entity ;
	private int  toOneCount ;
	private int  toManyCount ;
	
	/**
	 * Constructor
	 * @param entity
	 */
	public ReferenceInContext(EntityInContext entity) {
		if ( entity == null ) {
			throw new IllegalArgumentException("entity is null");
		}
		this.entity = entity;
		this.toOneCount = 0;
		this.toManyCount = 0;
	}
	
	protected void incrementToOne() {
		this.toOneCount++;
	}
	protected void incrementToMany() {
		this.toManyCount++;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(text={
		"Referenced entity"
		})
	public EntityInContext getEntity() {
		return this.entity;
	}
	//-------------------------------------------------------------------------------------
	@VelocityMethod(text={
		"Name of the referenced entity"
		})
	public String getEntityName() {
		return this.entity.getName();
	}
	//-------------------------------------------------------------------------------------
	@VelocityMethod(text={
		"Number of 'to one' references (0..N)"
		})
	public int getToOneCount() {
		return this.toOneCount;
	}
	//-------------------------------------------------------------------------------------
	@VelocityMethod(text={
		"Number of 'to many' references (0..N)"
		})
	public int getToManyCount() {
		return this.toManyCount;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(text={
		"Total number of references (1..N)"
		})
	public int getTotalCount() {
		return this.toOneCount + this.toManyCount;
	}
	//-------------------------------------------------------------------------------------
	@VelocityMethod(text={
		"true if has 'to one' reference(s) (one or more)"
		})
	public boolean getToOne() {
		return this.toOneCount > 0 ;
	}
	//-------------------------------------------------------------------------------------
	@VelocityMethod(text={
		"true if has 'to many' reference(s) (one or more)"
		})
	public boolean getToMany() {
		return this.toManyCount > 0 ;
	}

	
}
