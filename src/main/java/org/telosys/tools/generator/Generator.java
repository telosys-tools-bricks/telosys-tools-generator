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
package org.telosys.tools.generator;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.telosys.tools.commons.DirUtil;
import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.commons.TelosysToolsLogger;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.generator.context.Target;
import org.telosys.tools.generator.engine.GeneratorContext;
import org.telosys.tools.generator.engine.GeneratorEngine;
import org.telosys.tools.generator.engine.GeneratorTemplate;
import org.telosys.tools.generic.model.Model;

/**
 * This class is a Velocity generator ready to use. <br>
 * 
 * @author Laurent Guerin
 *  
 */
public class Generator {

	public static final boolean CREATE_DIR = true ;
	public static final boolean DO_NOT_CREATE_DIR = false ;
	
	private final TelosysToolsCfg          _telosysToolsCfg ; // v 3.0.0
	private final String                   _bundleName ; // v 3.0.0
	private final TelosysToolsLogger       _logger ;

	/**
	 * Constructor 
	 * @param telosysToolsCfg
	 * @param logger
	 */
	public Generator( TelosysToolsCfg telosysToolsCfg, String bundleName, TelosysToolsLogger logger)  { // v 3.0.0
		_logger = logger; 
		
		if ( telosysToolsCfg == null ) {
			throw new IllegalArgumentException("TelosysToolsCfg parameter is null");
		}
		_telosysToolsCfg = telosysToolsCfg;

		if ( bundleName == null ) {
			throw new IllegalArgumentException("Bundle name parameter is null");
		}
		_bundleName = bundleName ; // v 3.0.0
	}
	
	private void log(String s) {
		if (_logger != null) {
			_logger.log(s);
		}
	}
	
	//========================================================================
	// TEMPLATE MANAGEMENT
	//========================================================================
	private GeneratorTemplate loadTemplate(Target target) throws GeneratorException {
		
		String templateFileName  = target.getTemplate();
		String templateDirectory = _telosysToolsCfg.getTemplatesFolderAbsolutePath(); // v 3.0.0

		checkTemplate( templateDirectory, templateFileName);
		
		String bundleFolderAbsolutePath = _telosysToolsCfg.getTemplatesFolderAbsolutePath(_bundleName) ; 
		// Examples : 
		//  "/foo/bar/TelosysTools/templates/basic-templates", "myfile.vm"
		//  "/foo/bar/TelosysTools/templates/basic-templates", "subdir/myfile.vm"
		//  "/foo/bar/TelosysTools/templates/basic-templates", "/subdir/myfile.vm"
		return new GeneratorTemplate(bundleFolderAbsolutePath, templateFileName) ;
	}
	
	private File checkTemplate(String sTemplateDirectory, String sTemplateFileName) throws GeneratorException {
		if (sTemplateDirectory == null) {
			throw new GeneratorException("Template directory is null !");
		}
		if (sTemplateFileName == null) {
			throw new GeneratorException("Template file name is null !");
		}
		File dir = new File(sTemplateDirectory);
		if (!dir.exists()) {
			throw new GeneratorException("Template directory '"
					+ sTemplateDirectory + "' doesn't exist !");
		}
		if (!dir.isDirectory()) {
			throw new GeneratorException("Template directory '"
					+ sTemplateDirectory + "' is not a directory !");
		}

		//--- Templates directory full path ( with bundle name if any )
		String templatesFolderFullPath = sTemplateDirectory ;
		if ( ! StrUtil.nullOrVoid(_bundleName)) {
			templatesFolderFullPath = FileUtil.buildFilePath(sTemplateDirectory, _bundleName);
		}
		//--- Template file full path 
		String sTemplateFullPath = FileUtil.buildFilePath(templatesFolderFullPath, sTemplateFileName);
		
		//--- Check template file existence 
		File file = new File(sTemplateFullPath);
		if (!file.exists()) {
			throw new GeneratorException("Template file '" + sTemplateFullPath + "' doesn't exist !");
		}
		if (!file.isFile()) {
			throw new GeneratorException("Template file '" + sTemplateFullPath + "' is not a file !");
		}
		return file ;
	}
	
	/**
	 * Generates in memory and returns the InputStream on the generation result
	 * @return
	 * @throws GeneratorException
	 */
	private InputStream generateInMemory(Target target, GeneratorContext generatorContext) throws Exception
	{
		log("generateInMemory()...");
		
		//------------------------------------------------------------------
		// Workaround for Velocity error in OSGi environment 
		//------------------------------------------------------------------
		Thread currentThread = Thread.currentThread();
		ClassLoader originalClassLoader = currentThread.getContextClassLoader();
		currentThread.setContextClassLoader(this.getClass().getClassLoader()); // Set the context ClassLoader for this Thread
		String result = null ;
		try {
			//------------------------------------------------------------------
			//--- Load the TEMPLATE for the given TARGET
			GeneratorTemplate generatorTemplate = loadTemplate(target) ;
			//--- Create a new GENERATOR ENGINE
			GeneratorEngine generatorEngine = new GeneratorEngine();
			//--- GENERATION 
			result = generatorEngine.generate(generatorTemplate, generatorContext );
			//------------------------------------------------------------------
		}
		finally {
			currentThread.setContextClassLoader(originalClassLoader); // Restore the original classLoader
		}
		//------------------------------------------------------------------
		// End of Workaround for Velocity error in OSGi environment
		//------------------------------------------------------------------
			
		return new ByteArrayInputStream(result.getBytes());
	}

	//================================================================================================
	// generateTarget moved from GenerationManager to Generator 
	//================================================================================================
	/**
	 * Generates the given target 
	 * @param target the target to be generated
	 * @param model  the current 'model' with all the entities
	 * @param selectedEntitiesNames list of names for all the selected entities (or null if none)
	 * @param generatedTargets list of generated targets to be updated (or null if not useful)
	 * @throws GeneratorException
	 */
	public void generateTarget(Target target, Model model, 
			List<String> selectedEntitiesNames,
			List<Target> generatedTargets) throws GeneratorException
	{
		String entityName = target.getEntityName() ;
		if ( StrUtil.nullOrVoid(entityName) ) {
			entityName = "(no entity)" ;
		}
		
		_logger.info("Gen : " + target.getTemplate() + " : " +  entityName  );
		
		//--- Creation of a full context for the generator
		GeneratorContextBuilder generatorContextBuilder = new GeneratorContextBuilder(_telosysToolsCfg, _logger);
		GeneratorContext generatorContext = generatorContextBuilder.initFullContext(
				model, 
				//_databasesConfigurations, 
				_bundleName,
				selectedEntitiesNames, 
				target, 
				generatedTargets);

		//---------- ((( GENERATION ))) 
		InputStream is;
		try {
			is = generateInMemory(target, generatorContext);
		} catch (Exception e) {
			//_logger.error( ExceptionUtil.getStackTraceAsString(e) ); // Useless : "ASTMethod.handleInvocationException"
			String msg = "Entity '" + target.getEntityName() + "' - Template '" + target.getTemplate() + "'" ;
			_logger.error(msg);
			_logger.error(e.getMessage());
			throw new GeneratorException(msg + " : " + e.getMessage(), e);
		} // Generate the target in memory
		_logger.log("Generation OK.");

		//---------- Save the result in the file
		String outputFileName = target.getOutputFileNameInFileSystem( _telosysToolsCfg.getDestinationFolderAbsolutePath() ); // v 3.0.0
		_logger.log("Saving target file : " + outputFileName );
		saveStreamInFile(is, outputFileName, true );
		_logger.info("OK :  " + target.getOutputFileNameInProject() );
		
		//---------- Add the generated target in the list if any
		if ( generatedTargets != null ) {
			generatedTargets.add(target);
		}
	}
	
	private void saveStreamInFile(InputStream is, String fileName, boolean bCreateDir) throws GeneratorException
	{
		File file = new File(fileName);
		
		//--- Check if it's possible to write the file
		if ( file.exists() ) {
			if ( ! file.canWrite() ) {
				throw new GeneratorException("Cannot write on existing target file '"+ file.toString() + "' !");
			}
		}
		else {
			File parentFile = file.getParentFile();
			if ( ! parentFile.exists() ) {
				if ( bCreateDir == false ) {
					throw new GeneratorException("Target directory '"+ parentFile.toString() + "' not found !");
				}
				else {
					// Create the target file directory(ies)
					DirUtil.createDirectory(parentFile);
				}
			}
		}
		
		//--- Write the file
		try {
			OutputStream out = new FileOutputStream(file);
			byte buf[] = new byte[1024];
			int len;
			while ((len = is.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.close();
			is.close();
		} catch (IOException e) {
			throw new GeneratorException("Cannot save file "+fileName, e);
		}
	}
	
}