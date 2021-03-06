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
package org.telosys.tools.generator.api;

import java.util.LinkedList;
import java.util.List;

import org.telosys.tools.commons.TelosysToolsLogger;
import org.telosys.tools.commons.bundles.TargetDefinition;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.variables.Variable;
import org.telosys.tools.generator.Generator;
import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generator.context.Target;
import org.telosys.tools.generic.model.Entity;
import org.telosys.tools.generic.model.Model;

/**
 * Utility class to launch generation (for tests) 
 * 
 * @author Laurent Guerin
 *
 */
public class GeneratorRunner {

	private final Model              model  ;

	//private final GeneratorConfig    generatorConfig ; 
	private final TelosysToolsCfg    telosysToolsCfg; // v 3.0.0
	private final String             bundleName; // v 3.0.0
	
	private final TelosysToolsLogger logger ;
	
	/**
	 * Constructor 
	 * 
	 * @param model
	 * @param generatorConfig
	 * @param logger
	 */
//	public GeneratorRunner(Model model, GeneratorConfig generatorConfig, TelosysToolsLogger logger) throws GeneratorException
	public GeneratorRunner(Model model, TelosysToolsCfg telosysToolsCfg, String bundleName, TelosysToolsLogger logger) throws GeneratorException
	{
		super();
		if ( null == model ) {
			throw new GeneratorException("Illegal argument : Repository model is null" );
		}
//		if ( null == generatorConfig ) {
//			throw new GeneratorException("Illegal argument : Generator configuration is null" );
//		}
		if ( null == telosysToolsCfg ) {
			throw new GeneratorException("Illegal argument : TelosysToolsCfg is null" );
		}
		this.model = model;
//		this.generatorConfig = generatorConfig;
		this.telosysToolsCfg = telosysToolsCfg ;
		this.bundleName = bundleName ;
		this.logger = logger;
	}

	/**
	 * Generates a file from the given entity and using the given target
	 * 
	 * @param entityClassName - the entity to be used (loaded from the repository)
	 * @param outputFile - the file to be generated
	 * @param outputFolder - the folder where to put the generated file
	 * @param templateFileName - the Velocity template to be used
	 */
	public void generateEntity(String entityClassName, 
			String outputFile, 
			String outputFolder, 
			String templateFileName
			) throws GeneratorException
	{

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

			//Entity entity = repositoryModel.getEntityByName(entityName.trim());
			Entity entity = model.getEntityByClassName(entityClassName.trim());
			if ( null == entity ) {
				throw new GeneratorException( err + "(entity '" + entityClassName + "' not found in repository)");
			}

			TargetDefinition genericTarget = new TargetDefinition("Dynamic target", outputFile, outputFolder, templateFileName, "");
			//Target target = new Target( genericTarget, entity.getName(), entity.getBeanJavaClass(), projectConfiguration.getAllVariables() );
			
//			Variable[] allVariables = this.generatorConfig.getTelosysToolsCfg().getAllVariables(); // ver 2.1.0
			Variable[] allVariables = telosysToolsCfg.getAllVariables(); // ver 3.0.0
			//Target target = new Target( genericTarget, entity.getName(), entity.getBeanJavaClass(), allVariables );
			Target target = new Target( genericTarget, entity, allVariables ); // v 3.0.0
			
			//----------------------------------------------------------------
			// 2) Launch the generation 
			//----------------------------------------------------------------

//			Generator generator = new Generator(target, generatorConfig, model, logger); // v 2.0.7
//			Generator generator = new Generator(generatorConfig, logger); // v 3.0.0
			Generator generator = new Generator(telosysToolsCfg, bundleName, logger); // v 3.0.0

			List<Target> generatedTargets = new LinkedList<Target>();
			generator.generateTarget(target, model, null, generatedTargets);
			
	}	
}
