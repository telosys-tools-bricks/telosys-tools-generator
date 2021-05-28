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

import java.io.File;

import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.generator.context.exceptions.GeneratorFunctionException;
import org.telosys.tools.generator.context.names.ContextName;
import org.telosys.tools.generator.engine.GeneratorContext;

/**
 * File functions
 *  
 * @author Laurent GUERIN
 *
 */
public class FnFileUtil {
	
	private final String functionName;
	private final GeneratorContext generatorContext ;
	
	/**
	 * Constructor
	 * @param functionName  the "$fn" function name
	 * @param generatorContext
	 */
	public FnFileUtil(String functionName, GeneratorContext generatorContext) {
		super();
		this.functionName = functionName ;
		this.generatorContext = generatorContext ;
	}

	//-------------------------------------------------------------------------------------
	// FUNCTIONS EXPOSED IN VELOCITY CONTEXT ( "$fn" )
	//-------------------------------------------------------------------------------------
	public FileInContext file (String filePath) {
		File file = getFileFromPath(filePath) ;
		return new FileInContext(file);
	}
	
	public FileInContext fileFromBundle(String filePath) {
		String dir = getBundleLocationFullPath();
		String fullPath = FileUtil.buildFilePath(dir, filePath);
		File file = new File(fullPath) ;
		return new FileInContext(file);
	}

	public FileInContext fileFromModel(String filePath) {
		String dir = getModelLocationFullPath();
		String fullPath = FileUtil.buildFilePath(dir, filePath);
		File file = new File(fullPath) ;
		return new FileInContext(file);
	}

	//-------------------------------------------------------------------------------------
	// PRIVATE METHODS
	//-------------------------------------------------------------------------------------
	/**
	 * Returns a File for the given path, if path is relative the project location is used as root path 
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	private File getFileFromPath(String filePath) {
		File file = new File(filePath) ;
		if ( file.isAbsolute()) {
			return file;
		}
		else {
			String dir = getProjectLocationFullPath();
			String fullPath = FileUtil.buildFilePath(dir, filePath);
			return new File(fullPath) ;
		}
	}
	
	private String getProjectLocationFullPath() {
		ProjectInContext project = getProjectFromGeneratorContext() ;
		return project.getLocationFullPath();
	}
	
	private String getBundleLocationFullPath() {
		ProjectInContext project = getProjectFromGeneratorContext() ;
		BundleInContext bundle = getBundleFromGeneratorContext() ;
		return FileUtil.buildFilePath(project.getTemplatesFolderFullPath(), bundle.getName());
	}
	
	private String getModelLocationFullPath() {
		ProjectInContext project = getProjectFromGeneratorContext() ;
		String projectModelsFolderFullPath = project.getModelsFolderFullPath();
		
		ModelInContext model = getModelFromGeneratorContext() ;
		String modelFolderName = model.getFolderName();
		
		if ( StrUtil.nullOrVoid(modelFolderName) ) {
			return projectModelsFolderFullPath ;
		}
		else {
			return FileUtil.buildFilePath(projectModelsFolderFullPath, modelFolderName);
		}
	}

	private ProjectInContext getProjectFromGeneratorContext() {
		return (ProjectInContext) getFromGeneratorContext(ContextName.PROJECT);
	}
	
	private BundleInContext getBundleFromGeneratorContext() {
		return (BundleInContext) getFromGeneratorContext(ContextName.BUNDLE);
	}
	
	private ModelInContext getModelFromGeneratorContext() {
		return (ModelInContext) getFromGeneratorContext(ContextName.MODEL);
	}
	
	private Object getFromGeneratorContext(String nameInContext) {
		Object o = generatorContext.get(nameInContext);
		if ( o != null ) {
			return o ;
		}
		else {
			String msg = "Cannot found '" + nameInContext + "' in generator context" ;
			throw new GeneratorFunctionException(functionName, msg);
		}
	}
	
}