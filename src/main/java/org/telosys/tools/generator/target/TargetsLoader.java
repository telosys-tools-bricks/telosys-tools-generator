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
package org.telosys.tools.generator.target;

import java.util.LinkedList;
import java.util.List;

import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generator.target.TargetDefinition;
import org.telosys.tools.generator.target.TargetsFile;

public class TargetsLoader {
	
	public final static String TEMPLATES_CFG = "templates.cfg" ;

	private final String templatesFolderAbsolutePath ;
	
	public TargetsLoader(String templatesFolderAbsolutePath) {
		super();
		this.templatesFolderAbsolutePath = templatesFolderAbsolutePath;
	}

	public TargetsDefinitions loadTargetsDefinitions(String bundleName) throws GeneratorException
	{
		if ( StrUtil.nullOrVoid(bundleName) ) {
			throw new GeneratorException("Invalid bundle name (null or void) : '" + bundleName + "'  ");
		}

		String bundleFolder = FileUtil.buildFilePath(templatesFolderAbsolutePath, bundleName.trim() );
		// templates.cfg full path  
		String sFile = FileUtil.buildFilePath(bundleFolder, TEMPLATES_CFG );
		
		TargetsFile targetsFile = new TargetsFile(sFile) ;
		if ( targetsFile.exists() ) {
			//--- Try to load the targets 
			List<TargetDefinition> allTargetsList = targetsFile.load();
			//--- Build the two lists of targets : templates targets and resources targets 
			List<TargetDefinition> templatesTargets = new LinkedList<TargetDefinition>() ;
			List<TargetDefinition> resourcesTargets = new LinkedList<TargetDefinition>() ;
			for ( TargetDefinition t : allTargetsList ) {
				if ( t.isResource() ) {
					resourcesTargets.add(t) ;
				}
				else {
					templatesTargets.add(t);
				}
			}
			return new TargetsDefinitions(templatesTargets, resourcesTargets);
		}
		else {
			throw new GeneratorException("File not found '" + sFile + "'");
		}
	}	
}
