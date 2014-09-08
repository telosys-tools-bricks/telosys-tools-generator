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

import org.telosys.tools.generator.config.GeneratorConfig;
import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.names.ContextName;


/**
 * Information about the generation in progress
 *  
 * @author Laurent GUERIN
 */
//-------------------------------------------------------------------------------------
@VelocityObject(
		contextName= ContextName.GENERATION ,
		text = "Information about the generation in progress",
		since = "2.1.0"
 )
//-------------------------------------------------------------------------------------
public class GenerationInContext
{
	private final GeneratorConfig _generatorConfig ;
	
    //---------------------------------------------------------------------------
	public GenerationInContext(GeneratorConfig generatorConfig)
	{
		super();
		_generatorConfig = generatorConfig ;
	}

	//--------------------------------------------------------------------------------------------------------------
	@VelocityMethod (
		text = { 
				"Returns the current bundle name",
				"(just for information, not supposed to be used in generation)" },
		example = {
				"$generation.bundleName"
			}
		)
    public String getBundleName()
    {
        return _generatorConfig.getBundleName() ;
    }

	//--------------------------------------------------------------------------------------------------------------
	@VelocityMethod (
		text = { 
				"Returns the full path of current templates folder with the bundle name",
				"(just for information, not supposed to be used in generation)" },
		example = {
				"$generation.templatesFolderFullPath"
			}
	)
    public String getTemplatesFolderFullPath()
    {
        return _generatorConfig.getTemplatesFolderFullPath() ;
    }

}