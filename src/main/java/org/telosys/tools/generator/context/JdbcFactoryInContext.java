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

//-------------------------------------------------------------------------------------
@VelocityObject(
		contextName=ContextName.JDBC_FACTORY,
		text = { 
				"Object providing the JDBC factory",
				""
		},
		since = "2.1.1"
 )
//-------------------------------------------------------------------------------------
public class JdbcFactoryInContext {
	
	private boolean useSchema = false ;

	//-------------------------------------------------------------------------------------
	// CONSTRUCTOR
	//-------------------------------------------------------------------------------------
	public JdbcFactoryInContext() {
		super();
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Defines if the 'schema' must be used in the table name",
			"Must be set before 'getInstance()' ",
			"Default value is FALSE"
		},
		parameters = {
			"value : true = use schema, false = do not use schema "
		},		
		example={	
			"$jdbc.useSchema()"
		},
		since = "2.1.1"
	)
	public void useSchema(boolean value)
    {
		this.useSchema = value ;
    }
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Creates a new instance of the JDBC tool for the given entity",
			""
		},
		parameters = {
			"entity : the entity to be used (to create CRUD SQL requests, mapping, etc) "
		},		
		example={	
			"$jdbcFactory.getInstance($entity)"
		},
		since = "2.1.1"
	)
	public JdbcInContext getInstance(EntityInContext entity)
    {
		if ( entity == null ) {
			throw new IllegalArgumentException("$jdbcFactory.getInstance($entity) : $entity is null");
		}
		return new JdbcInContext(entity, this.useSchema); 
    }
	
	//-------------------------------------------------------------------------------------
}
