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

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;

/**
 * Utility class designed to load the bundles names and targets (templates) for a given bundle
 * 
 * @author L. Guerin
 *
 */
public class TargetsLoader {
	
	public final static String TEMPLATES_CFG = "templates.cfg" ;

	private final String  templatesFolderAbsolutePath ;
	private final boolean specificTemplatesFolder ;
	
	/**
	 * Constructor
	 * @param telosysToolsCfg
	 */
	public TargetsLoader(TelosysToolsCfg telosysToolsCfg) {
		super();
//		// Templates folder : standard or specific ?
//		if ( telosysToolsCfg.hasSpecificTemplatesFolders() ) {
//			// specific folder defined => use it 
//			this.templatesFolderAbsolutePath = telosysToolsCfg.getSpecificTemplatesFolderAbsolutePath();
//		}
//		else {
//			// no specific folder defined => use the standard folder (in the project)
//			this.templatesFolderAbsolutePath = telosysToolsCfg.getTemplatesFolderAbsolutePath();
//		}
//		specificTemplatesFolder = true ;
		this.specificTemplatesFolder = telosysToolsCfg.hasSpecificTemplatesFolders() ;
		this.templatesFolderAbsolutePath = telosysToolsCfg.getTemplatesFolderAbsolutePath();
	}
	
	/**
	 * Returns the list of bundles defined in the current templates folder <br>
	 * @return
	 * @throws TelosysToolsException
	 */
	public List<String> loadBundlesList() throws TelosysToolsException {
		File dir = new File(this.templatesFolderAbsolutePath);			
		if ( dir.exists() ) {
			if ( dir.isDirectory() ) {
				List<String> bundles = new LinkedList<String>();
				for ( File bundleDir : dir.listFiles() ) {
					if ( bundleDir.isDirectory() ) {
						if ( this.specificTemplatesFolder ) {
							// Can contains any kind of folders => check existence of "templates.cfg" 
							if ( isTemplatesCfgExist(bundleDir) ) {
								bundles.add(bundleDir.getName());
							}
						}
						else {
							bundles.add(bundleDir.getName());
						}
					}
				}
				return bundles ;
			}
			else {
				throw new TelosysToolsException("Templates folder '" + templatesFolderAbsolutePath + "' is not a folder.");
			}
		}
		else {
			throw new TelosysToolsException("Templates folder '" + templatesFolderAbsolutePath + "' not found.");
		}
	}
	
	private boolean isTemplatesCfgExist(File dir ) {
    	String filePath = FileUtil.buildFilePath(dir.getAbsolutePath(), TargetsLoader.TEMPLATES_CFG );
    	File file = new File(filePath);
    	return file.exists();
	}

	public TargetsDefinitions loadTargetsDefinitions(String bundleName) throws TelosysToolsException
	{
		if ( StrUtil.nullOrVoid(bundleName) ) {
			throw new TelosysToolsException("Invalid bundle name (null or void) : '" + bundleName + "'  ");
		}

		String bundleFolderAbsolutePath = FileUtil.buildFilePath(templatesFolderAbsolutePath, bundleName.trim() );
		// templates.cfg full path  
		String templatesCfgAbsolutepath = FileUtil.buildFilePath(bundleFolderAbsolutePath, TEMPLATES_CFG );
		
		TargetsFile targetsFile = new TargetsFile(templatesCfgAbsolutepath) ;
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
			throw new TelosysToolsException("File not found '" + templatesCfgAbsolutepath + "'");
		}
	}	
}
