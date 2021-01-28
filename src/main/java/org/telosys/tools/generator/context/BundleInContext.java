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
 * This class give access to the current bundle of templates
 *  
 * @author Laurent GUERIN
 *
 */
//-------------------------------------------------------------------------------------
@VelocityObject(
		contextName = ContextName.BUNDLE ,
		text = "Object giving access to the current bundle of templates ",
		since = "3.3.0"
 )
//-------------------------------------------------------------------------------------
public class BundleInContext
{
	private final String   bundleName ;
	
	/**
	 * Constructor
	 * @param bundleName
	 */
	public BundleInContext( String bundleName ) { 
		super();
		this.bundleName = bundleName ;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the bundle name"
			},
		since="3.3.0"
	)
    public String getName() {
        return bundleName;
    }

}