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
package org.telosys.tools.generator ;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.TelosysToolsLogger;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.io.CopyHandler;
import org.telosys.tools.commons.io.OverwriteChooser;
import org.telosys.tools.commons.io.ResourcesCopier;
import org.telosys.tools.commons.variables.Variable;
import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generator.context.Target;
import org.telosys.tools.generator.target.TargetDefinition;


public class BundleResourcesManager {

	private final TelosysToolsCfg    _telosysToolsCfg ;
	private final String             _bundleName ;
	private final TelosysToolsLogger _logger;
	
	//----------------------------------------------------------------------------------------------------
	/**
	 * Constructor
	 * @param projectCfg
	 * @param bundleName
	 * @param logger
	 */
	public BundleResourcesManager(TelosysToolsCfg projectCfg, String bundleName, TelosysToolsLogger logger) {
		super();
		_telosysToolsCfg  = projectCfg ;
		_bundleName       = bundleName ;
		_logger           = logger ;
		log("created.");
	}
	
	//----------------------------------------------------------------------------------------------------
	private void log(String s) {
		if (_logger != null) {
			_logger.log( this.getClass().getSimpleName() + " : " + s);
		}
	}
	//----------------------------------------------------------------------------------------------------
	/**
	 * Copy the resources for each target definition
	 * @param targetsDefinitions list of target definitions 
	 * @return
	 */
	private List<Target> getResourcesTargets(List<TargetDefinition> targetsDefinitions ) {
		log("getResourcesTargets()... " );
		
		Variable[] projectVariables = _telosysToolsCfg.getAllVariables();
		LinkedList<Target> targets = new LinkedList<Target>();
		if ( targetsDefinitions != null ) {
			for ( TargetDefinition targetDefinition : targetsDefinitions ) {
				Target target = new Target ( targetDefinition, "", "", projectVariables );
				targets.add(target);
			}
		}
		log("getResourcesTargets() : return " + targets.size() + " target(s)");
		return targets ;
	}
	
	
	//----------------------------------------------------------------------------------------------------
	/**
	 * Copy all the given resources targets definitions
	 * @param targetsDefinitions list of the resources targets definition
	 * @throws Exception
	 */
	public int copyTargetsResourcesInProject( List<TargetDefinition> targetsDefinitions, OverwriteChooser overwriteChooser, CopyHandler copyHandler ) throws Exception {
		log("copyResourcesInProject()... " );
		
		int count = 0 ;
		
		//--- Build the real resources targets from the targets definitions 
		List<Target> resourcesTargets = getResourcesTargets( targetsDefinitions ) ;
		//--- For each target 
		for ( Target target : resourcesTargets ) {
			int n = copyTargetResourcesInProject(target, overwriteChooser, copyHandler );
			if ( n < 0 ) {
				// Copy canceled 
				return -1 ;
			}
			count = count + n ;
		}
		return count ;
	}

	//----------------------------------------------------------------------------------------------------
	private int copyTargetResourcesInProject( Target target, OverwriteChooser overwriteChooser, CopyHandler copyHandler ) throws Exception {
		log("copyTargetResourcesInProject() : target = " + target );

		File origin = getOrigin(target); 
		log("origin      : " + origin);
		File destination = getDestination(target); 
		log("destination : " + destination);
		int count = copy(origin, destination, overwriteChooser, copyHandler);
		log(count + "file(s) copied");
		
		return count ;
	}
	
	//----------------------------------------------------------------------------------------------------
	private File getOrigin(Target target) throws Exception {
		// "resources to be copied" = "template file" in .cfg file 
		String resourceName = target.getTemplate(); 
		log("resource name = " + resourceName );
		String bundleResourcesFolder = getBundleResourcesFolder() ;
		log("bundle resources folder = " + bundleResourcesFolder );
		String originResourceFullPath = FileUtil.buildFilePath(bundleResourcesFolder, resourceName );
		log("resource full path = " + originResourceFullPath );
		File originResourceFile = new File(originResourceFullPath);
		if ( originResourceFile.exists() == false ) {
			throw new GeneratorException("Resource file or folder '" + originResourceFullPath + "' not found " );
		}
		return originResourceFile ;
	}
	//----------------------------------------------------------------------------------------------------
	private File getDestination(Target target) {
		// "resources destination" = "project folder where to generate" in .cfg file 
		String projectLocation = _telosysToolsCfg.getProjectAbsolutePath() ;
		String destinationFullPath = target.getOutputFileNameInFileSystem(projectLocation) ;
		File destinationFile = new File(destinationFullPath);
		return destinationFile ;
	}
	//----------------------------------------------------------------------------------------------------
	private int copy(File origin, File destination, OverwriteChooser overwriteChooser, CopyHandler copyHandler ) {
		int n = 0 ;
		log("copy from '" + origin + "' to '" + destination + "'...");
		ResourcesCopier copier = new ResourcesCopier(overwriteChooser, copyHandler );
		try {
			n = copier.copy(origin, destination);
			log(n + " file(s) copied");
		} catch (Exception e) {
			log("Error : exception " + e);
		}
		return n ;
	}	
	
	//----------------------------------------------------------------------------------------------------
	/**
	 * Returns the folder full path where the static resources are located
	 * @return
	 * @throws Exception
	 */
	private String getBundleResourcesFolder() throws Exception {
		String projectTemplatesFolder = _telosysToolsCfg.getTemplatesFolderAbsolutePath();
		log("project templates folder = " + projectTemplatesFolder );
		
		String bundleResourcesFolder = FileUtil.buildFilePath(projectTemplatesFolder, _bundleName + "/resources");
		log("bundle resources folder = " + bundleResourcesFolder );
		
		File file = new File(bundleResourcesFolder);
		if ( file.exists() && file.isDirectory() ) {
			return bundleResourcesFolder ;
		}
		else {
			throw new Exception("Resources folder '" + bundleResourcesFolder + "' not found or not a directory");
		}
	}
	//----------------------------------------------------------------------------------------------------
}
