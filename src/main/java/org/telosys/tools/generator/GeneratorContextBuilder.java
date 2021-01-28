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

import java.util.List;

import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.TelosysToolsLogger;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.dbcfg.DatabasesConfigurations;
import org.telosys.tools.commons.dbcfg.DbConfigManager;
import org.telosys.tools.commons.variables.Variable;
import org.telosys.tools.generator.context.BeanValidation;
import org.telosys.tools.generator.context.BundleInContext;
import org.telosys.tools.generator.context.Const;
import org.telosys.tools.generator.context.DatabasesInContext;
import org.telosys.tools.generator.context.EmbeddedGenerator;
import org.telosys.tools.generator.context.EntityInContext;
import org.telosys.tools.generator.context.EnvInContext;
import org.telosys.tools.generator.context.FnInContext;
import org.telosys.tools.generator.context.H2InContext;
import org.telosys.tools.generator.context.HtmlInContext;
import org.telosys.tools.generator.context.Java;
import org.telosys.tools.generator.context.JdbcFactoryInContext;
import org.telosys.tools.generator.context.Jpa;
import org.telosys.tools.generator.context.Loader;
import org.telosys.tools.generator.context.ModelInContext;
import org.telosys.tools.generator.context.ProjectInContext;
import org.telosys.tools.generator.context.Target;
import org.telosys.tools.generator.context.Today;
import org.telosys.tools.generator.context.names.ContextName;
import org.telosys.tools.generator.engine.GeneratorContext;
import org.telosys.tools.generic.model.Model;

/**
 * Utility class to construct a GeneratorContext 
 * 
 * @author Laurent Guerin
 *  
 */
public class GeneratorContextBuilder {

	private final TelosysToolsCfg     _telosysToolsCfg ;
	private final TelosysToolsLogger  _logger ;
	private final GeneratorContext    generatorContext ;
	
	private Model                     model = null ;
	private ModelInContext            modelInContext = null ;
	
	private void log(String s) {
		if (_logger != null) {
			_logger.log(s);
		}
	}

	/**
	 * Constructor <br>
	 * @param telosysToolsCfg
	 * @param logger
	 */
	public GeneratorContextBuilder( TelosysToolsCfg telosysToolsCfg, TelosysToolsLogger logger)  {
		_logger = logger; 
		
		if ( telosysToolsCfg == null ) {
			throw new IllegalArgumentException("TelosysToolsCfg parameter is null");
		}
		_telosysToolsCfg = telosysToolsCfg;

		generatorContext = new GeneratorContext(); 		
	}
	
	public GeneratorContext getGeneratorContext() {
		return generatorContext ;		
	}

	/**
	 * Initializes a "basic generator context" with the given model <br>
	 * without embedded generator, targets and selected entities <br>
	 * @param model
	 * @param bundleName
	 * @return
	 */
	public GeneratorContext initBasicContext( Model model, String bundleName ) { // databasesConfigurations removed in V 3.0.0 #LGU
		
		log("GeneratorContextBuilder : initContext() ...");

		// Since v 3.0 _velocityContext has been replaced by _generatorContext
		//--- Special Characters  [LGU 2012-11-29 ]
		generatorContext.put(ContextName.DOLLAR , "$"  );
		generatorContext.put(ContextName.SHARP,   "#"  );
		generatorContext.put(ContextName.AMP,     "&"  ); // ampersand 
		generatorContext.put(ContextName.QUOT,    "\"" ); // double quotation mark
		generatorContext.put(ContextName.LT,      "<"  ); // less-than sign
		generatorContext.put(ContextName.GT,      ">"  ); // greater-than sign
		generatorContext.put(ContextName.LBRACE,  "{"  ); // left brace
		generatorContext.put(ContextName.RBRACE,  "}"  ); // right brace
		
		generatorContext.put(ContextName.NEWLINE, "\n"  ); // #LGU 2017-08-16
		generatorContext.put(ContextName.TAB,     "\t"  ); // #LGU 2017-08-16
		
		//--- Get all the project variables and put them in the context	
		Variable[] projectVariables = _telosysToolsCfg.getAllVariables(); // v 3.0.0
		log("initContext() : Project variables count = " + ( projectVariables != null ? projectVariables.length : 0 ) );
		//--- Set the project variables in the context ( if any )
		if ( projectVariables != null ) {
			for ( int i = 0 ; i < projectVariables.length ; i++ ) {
				Variable var = projectVariables[i];
				generatorContext.put( var.getName(), var.getValue() );
			}
		}
		
		//--- Set "$env" object ( environment configuration )
		EnvInContext env = new EnvInContext() ;
		generatorContext.put(ContextName.ENV, env);  

		//--- Set the standard Velocity variables in the context
		generatorContext.put(ContextName.GENERATOR,       new EmbeddedGenerator());  // Limited generator without generation capability 
		generatorContext.put(ContextName.TODAY,           new Today()); // Current date and time 
		generatorContext.put(ContextName.CONST,           new Const()); // Constants (static values)
		generatorContext.put(ContextName.FN,              new FnInContext(generatorContext, env)); // Utility functions
		generatorContext.put(ContextName.JAVA,            new Java());  // Java utility functions
		generatorContext.put(ContextName.JPA,             new Jpa());   // JPA utility functions
//		_velocityContext.put(ContextName.JDBC,            new JdbcInContext());  // JDBC utility functions ( ver 2.1.1 )
		generatorContext.put(ContextName.JDBC_FACTORY,    new JdbcFactoryInContext());  // JDBC factory ( ver 2.1.1 )
		generatorContext.put(ContextName.BEAN_VALIDATION, new BeanValidation()); // Bean Validation utility functions
		generatorContext.put(ContextName.H2,              new H2InContext());  // JDBC factory ( ver 2.1.1 )
		generatorContext.put(ContextName.HTML,            new HtmlInContext());  // HTML utilities ( ver 3.0.0 )

		// generatorContext.put(ContextName.DATABASES,	new DatabasesInContext(databasesConfigurations) );
		generatorContext.put(ContextName.DATABASES,	buildDatabasesInContext(_telosysToolsCfg) );
				
		//--- Set the dynamic class loader 
		Loader loader = new Loader( _telosysToolsCfg.getTemplatesFolderAbsolutePath(bundleName) ); 
		generatorContext.put(ContextName.LOADER, loader);
		
		//--- Set the "$project" variable in the context
		generatorContext.put(ContextName.PROJECT, new ProjectInContext(_telosysToolsCfg)); // ver 3.0.0

// removed in v 3.0.0
//		//--- Set the "$generation" variable in the context
//		generatorContext.put(ContextName.GENERATION, new GenerationInContext(generatorConfig)); // ver 2.1.0
		
		//--- Set "$model" object : full model with  all the entities (v 2.0.7)
		this.model = model ;
		//this.modelInContext = new ModelInContext(model, _telosysToolsCfg.getEntityPackage(), env ); 
		this.modelInContext = new ModelInContext(model, _telosysToolsCfg, env ); // v 3.3.0
		generatorContext.put(ContextName.MODEL, modelInContext); 
		
		//--- Set "$bundle" object ( new in v 3.3.0 ) 
		BundleInContext bundle = new BundleInContext(bundleName); // v 3.3.0
		generatorContext.put(ContextName.BUNDLE, bundle); // v 3.0.0
		
		return generatorContext ;
	}
	
	/**
	 * Initializes a "full generator context" usable by the generator <br>
	 * @param model
	 * @param bundleName
	 * @param selectedEntitiesNames
	 * @param target
	 * @param generatedTargets
	 * @return
	 * @throws GeneratorException
	 */
	public GeneratorContext initFullContext( Model model, String bundleName,
			List<String> selectedEntitiesNames, Target target, List<Target> generatedTargets ) throws GeneratorException {
		
		//--- Initialize a basic context
		initBasicContext(model, bundleName); // databasesConfigurations removed in V 3.0.0 #LGU
		
		//--- Add further elements
		setEmbeddedGenerator(selectedEntitiesNames, bundleName, generatedTargets);
		setSelectedEntities(selectedEntitiesNames);
		setTargetAndCurrentEntity(target);

		return generatorContext ;
	}
	
	/**
	 * Creates a DatabasesInContext instance for the given project configuration
	 * @param telosysToolsCfg
	 * @return
	 */
	private DatabasesInContext buildDatabasesInContext( TelosysToolsCfg telosysToolsCfg ) {
		return new DatabasesInContext( loadDatabasesConfigurations(telosysToolsCfg));
	}
	
	/**
	 * Loads DatabasesConfigurations if any for the given project configuration
	 * @param telosysToolsCfg
	 * @return
	 */
	private DatabasesConfigurations loadDatabasesConfigurations( TelosysToolsCfg telosysToolsCfg ) {
		try {
			// Try to load the DatabasesConfigurations
			return new DbConfigManager(telosysToolsCfg).load();
		} catch (TelosysToolsException e) {
			// If the DatabasesConfigurations cannot be loaded just return a void set of configurations
			return new DatabasesConfigurations() ; // Void
		}
	}

	//-------------------------------------------------------------------------------------------------------
	private void setSelectedEntities(List<String> selectedEntitiesNames) throws GeneratorException {
		//--- Set "$selectedEntities" ( list of all the selected entities )
		List<EntityInContext> selectedEntities = modelInContext.getEntities(selectedEntitiesNames); 
		generatorContext.put(ContextName.SELECTED_ENTITIES, selectedEntities);
	}
	
	//-------------------------------------------------------------------------------------------------------
	private void setTargetAndCurrentEntity(Target target) {
		//--- Set "$target" object in the context 
		generatorContext.put(ContextName.TARGET, target);
		
		//--- Set "$entity" object in the context ( if the target is "for N entities" )
		EntityInContext entity = null ;
		if ( ! StrUtil.nullOrVoid( target.getEntityName() ) ) { 
			//--- Target with entity ( classical target )
			entity = modelInContext.getEntityByClassName( target.getEntityName() );
		}
		else {
			//--- Target without entity ( e.g. "once" target )
			entity = null ;
		}
		generatorContext.put(ContextName.ENTITY, entity ); 
	}
	
	//-------------------------------------------------------------------------------------------------------
	private void setEmbeddedGenerator(List<String> selectedEntitiesNames, String bundleName, List<Target> generatedTargets) {
		//--- Set the "$generator"  in the context ( "real" embedded generator )
		EmbeddedGenerator embeddedGenerator = new EmbeddedGenerator( _telosysToolsCfg, bundleName, _logger,
				this.model, selectedEntitiesNames, generatedTargets );
		generatorContext.put(ContextName.GENERATOR, embeddedGenerator );
	}		
}