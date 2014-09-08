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

import org.telosys.tools.commons.variables.Variable;
import org.telosys.tools.generator.config.GeneratorConfig;
import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.names.ContextName;


/**
 * The current project configuration ( variables, packages, ... )
 *  
 * @author Laurent GUERIN
 *
 */
//-------------------------------------------------------------------------------------
@VelocityObject(
		contextName= ContextName.PROJECT ,
		text = "Current project configuration parameters ( variables, folders, ... )",
		since = ""
 )
//-------------------------------------------------------------------------------------
public class ProjectInContext
{
	private final GeneratorConfig _generatorConfig ;
	
    //---------------------------------------------------------------------------
	public ProjectInContext(GeneratorConfig generatorConfig)
	{
		super();
		_generatorConfig = generatorConfig ;
	}

	//--------------------------------------------------------------------------------------------------------------
	@VelocityMethod (
		text = { 
				"Returns the full path of the current project's location",
				"(just for information, not supposed to be used in generation)" },
		example = {
				"$project.locationFullPath"
			},
		since = "2.0.7"
	)
    public String getLocationFullPath()
    {
        return _generatorConfig.getProjectLocation() ;
    }

	//--------------------------------------------------------------------------------------------------------------
	@VelocityMethod (
		text = { 
				"Returns the specific variables defined for the current project",
				"( the specific variables defined for the project and the standard variables )"},
		example = {
				"#foreach( $var in $project.specificVariables )",
				"  $var.name = $var.value",
				"#end"
			},
		since = "2.0.7"
	)
    public Variable[] getSpecificVariables()
    {
		return _generatorConfig.getTelosysToolsCfg().getSpecificVariables();
    }
	//--------------------------------------------------------------------------------------------------------------
	@VelocityMethod (
		text = { 
				"Returns all the variables available for the current project",
				"( the specific variables defined for the project and the standard variables )"},
		example = {
				"#foreach( $var in $project.allVariables )",
				"  $var.name = $var.value",
				"#end"
			},
		since = "2.0.7"
	)
    public Variable[] getAllVariables()
    {
		return _generatorConfig.getTelosysToolsCfg().getAllVariables();
    }
}