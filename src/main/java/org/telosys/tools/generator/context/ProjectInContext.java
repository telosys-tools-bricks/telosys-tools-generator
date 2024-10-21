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

import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.variables.Variable;
import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.names.ContextName;

/**
 * The current project (with variables, packages, ... )
 *  
 * @author Laurent GUERIN
 *
 */
//-------------------------------------------------------------------------------------
@VelocityObject(
		contextName= ContextName.PROJECT ,
		text = "Current project with configuration parameters ( variables, folders, ... )",
		since = "2.0.7"
 )
//-------------------------------------------------------------------------------------
public class ProjectInContext
{
	private final TelosysToolsCfg telosysToolsCfg ;
	
	/**
	 * Constructor
	 * @param telosysToolsCfg
	 */
	public ProjectInContext(TelosysToolsCfg telosysToolsCfg) {
		super();
		this.telosysToolsCfg = telosysToolsCfg ;
	}

	//---------------------------------------------------------------------------------------------------
	@VelocityMethod (
		text = { 
				"Returns the current project location ( full path )"
			},
		example = {
				"$project.locationFullPath"
			},
		since = "2.0.7"
	)
    public String getLocationFullPath()
    {
        return telosysToolsCfg.getProjectAbsolutePath(); 
    }
	
	//-------------------------------------------------------------------------------------------------
	@VelocityMethod (
		text = { 
				"Returns templates folder ( full path ) for the current project "
			},
		example = {
				"$project.templatesFolderFullPath"
			},
		since = "3.0.0"
	)
    public String getTemplatesFolderFullPath()
    {
        return telosysToolsCfg.getTemplatesFolderAbsolutePath() ; 
    }

	//-------------------------------------------------------------------------------------------------
	@VelocityMethod (
		text = { 
				"Returns models folder ( full path ) for the current project "
			},
		example = {
				"$project.modelsFolderFullPath"
			},
		since = "3.3.0"
	)
    public String getModelsFolderFullPath()
    {
        return telosysToolsCfg.getModelsFolderAbsolutePath();
    }

	//---------------------------------------------------------------------------------------------------
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
		return telosysToolsCfg.getSpecificVariables();
    }

	//---------------------------------------------------------------------------------------------------
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
		return telosysToolsCfg.getAllVariables();
    }
}