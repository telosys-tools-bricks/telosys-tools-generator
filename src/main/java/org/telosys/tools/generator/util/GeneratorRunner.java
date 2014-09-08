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
package org.telosys.tools.generator.util;

import java.util.LinkedList;
import java.util.List;

import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.TelosysToolsLogger;
import org.telosys.tools.commons.variables.Variable;
import org.telosys.tools.generator.Generator;
import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generator.config.GeneratorConfig;
import org.telosys.tools.generator.config.GeneratorConfigManager;
import org.telosys.tools.generator.context.Target;
import org.telosys.tools.generator.target.TargetDefinition;
import org.telosys.tools.repository.model.Entity;
import org.telosys.tools.repository.model.RepositoryModel;
import org.telosys.tools.repository.persistence.StandardFilePersistenceManager;

/**
 * Utility class to launch generation (for tests) 
 * 
 * @author Laurent Guerin
 *
 */
public class GeneratorRunner {

	private final RepositoryModel     repositoryModel  ;

	private final GeneratorConfig    generatorConfig ;
	
	private final TelosysToolsLogger  logger ;
	
	/**
	 * Constructor 
	 * 
	 * @param repositoryModel
	 * @param generatorConfig
	 * @param logger
	 */
	public GeneratorRunner(RepositoryModel repositoryModel, GeneratorConfig generatorConfig, TelosysToolsLogger logger) throws GeneratorException
	{
		super();
		if ( null == repositoryModel ) {
			throw new GeneratorException("Illegal argument : Repository model is null" );
		}
		if ( null == generatorConfig ) {
			throw new GeneratorException("Illegal argument : Generator configuration is null" );
		}
		this.repositoryModel = repositoryModel;
		this.generatorConfig = generatorConfig;
		this.logger = logger;
	}

	/**
	 * Constructor
	 * 
	 * @param repositoryFileName
	 * @param projectLocation
	 * @param logger
	 * @throws GeneratorException
	 */
	public GeneratorRunner(String repositoryFileName, String projectLocation, TelosysToolsLogger logger) throws GeneratorException
	{
		super();
		this.logger = logger;
		
		//--- Load the repository 
		StandardFilePersistenceManager pm = new StandardFilePersistenceManager( repositoryFileName, logger );		
		RepositoryModel repositoryModel = null ;
		try {
			repositoryModel = pm.load();
		} catch (TelosysToolsException e) {
			throw new GeneratorException("Cannot load the repository from file '" + repositoryFileName + "'", e);
		}
		this.repositoryModel = repositoryModel;
		
		//--- Load the configuration
		GeneratorConfigManager configManager = new GeneratorConfigManager(logger);
		GeneratorConfig config = configManager.initFromDirectory(projectLocation, null);
		this.generatorConfig = config ;
 
	}


	/**
	 * Generate a file with the given entity and the given target
	 * 
	 * @param entityName - the entity to be used (loaded from the repository)
	 * @param outputFile - the file to be generated
	 * @param outputFolder - the folder where to put the generated file
	 * @param templateFileName - the Velocity template to be used
	 */
	public void generateEntity(String entityName, 
			String outputFile, 
			String outputFolder, 
			String templateFileName
			) 
	{

		try {
			//----------------------------------------------------------------
			// 1) Build the target
			//----------------------------------------------------------------
			
			//----------------------------------------------------------------
			// Option 1 : Test OK
			//----------------------------------------------------------------
			// Target target = new Target("TEST_TARGET", outputFile, outputFolder, templateFileName, entityName) ;
			
			//----------------------------------------------------------------
			// Option 2 : 
			//----------------------------------------------------------------
			String err = "ERROR " ;

			//ProjectConfiguration projectConfiguration = generatorConfig.getProjectConfiguration();
			
			Entity entity = repositoryModel.getEntityByName(entityName.trim());
			if ( null == entity ) {
				throw new GeneratorException( err + "(entity '" + entityName + "' not found in repository)");
			}

			TargetDefinition genericTarget = new TargetDefinition("Dynamic target", outputFile, outputFolder, templateFileName, "");
			//Target target = new Target( genericTarget, entity.getName(), entity.getBeanJavaClass(), projectConfiguration.getAllVariables() );
			
			Variable[] allVariables = this.generatorConfig.getTelosysToolsCfg().getAllVariables(); // ver 2.1.0
			Target target = new Target( genericTarget, entity.getName(), entity.getBeanJavaClass(), allVariables );
			
			//----------------------------------------------------------------
			// 2) Launch the generation 
			//----------------------------------------------------------------

			List<Target> generatedTargets = new LinkedList<Target>();
			//Generator generator = new Generator(target, generatorConfig, logger);
			Generator generator = new Generator(target, generatorConfig, repositoryModel, logger); // v 2.0.7
			generator.generateTarget(target, repositoryModel, null, generatedTargets);
			
		} catch (GeneratorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}	
}
