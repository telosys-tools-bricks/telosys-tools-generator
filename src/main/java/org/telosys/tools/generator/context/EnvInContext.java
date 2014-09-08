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
		contextName=ContextName.ENV,
		text = { 
				"Object for environment configuration",
				"The 'environement' object is reset for each generation."
		},
		since = "2.1.0"
 )
//-------------------------------------------------------------------------------------
public class EnvInContext {

	private String _entityClassNamePrefix = "" ;
	private String _entityClassNameSuffix = "" ;
	
	
	//-------------------------------------------------------------------------------------
	// CONSTRUCTOR
	//-------------------------------------------------------------------------------------
	public EnvInContext() {
		super();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Set the entity class name prefix",
			"Once the 'prefix' is defined in the 'environment object' ",
			"it is automatically applied when the entity class name is retrieved"
			},
		example={ 
			"#set ( $env.entityClassNamePrefix = 'Bean' )" },
		parameters = { 
			"prefix : the prefix to be used" 
			},
		since = "2.1.0"
			)
	public void setEntityClassNamePrefix( String prefix ) {
		_entityClassNamePrefix = prefix ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the current entity class name prefix.",
			"The default value is a void string (never null)"
			},
		example={ 
			"$env.entityClassNamePrefix" 
			},
		since = "2.1.0"
			)
	public String getEntityClassNamePrefix() {
		return _entityClassNamePrefix;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Set the entity class name suffix",
			"Once the 'suffix' is defined in the 'environment object' ",
			"it is automatically applied when the entity class name is retrieved"
			},
		example={ 
			"#set ( $env.entityClassNameSuffix = 'Entity' )" },
		parameters = { 
			"suffix : the suffix to be used" 
			},
		since = "2.1.0"
			)
	public void setEntityClassNameSuffix( String suffix ) {
		_entityClassNameSuffix = suffix ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the current entity class name suffix.",
			"The default value is a void string (never null)"
			},
		example={ 
			"$env.entityClassNameSuffix" 
			},
		since = "2.1.0"
			)
	public String getEntityClassNameSuffix() {
		return _entityClassNameSuffix;
	}

	//-------------------------------------------------------------------------------------
	
}
