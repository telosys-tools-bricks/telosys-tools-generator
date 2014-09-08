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
package org.telosys.tools.generator;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.telosys.tools.commons.TelosysToolsLogger;
import org.telosys.tools.commons.variables.Variable;
import org.telosys.tools.generator.config.GeneratorConfig;
import org.telosys.tools.generator.context.BeanValidation;
import org.telosys.tools.generator.context.Const;
import org.telosys.tools.generator.context.DatabasesInContext;
import org.telosys.tools.generator.context.EmbeddedGenerator;
import org.telosys.tools.generator.context.EntityInContext;
import org.telosys.tools.generator.context.EnvInContext;
import org.telosys.tools.generator.context.Fn;
import org.telosys.tools.generator.context.GenerationInContext;
import org.telosys.tools.generator.context.Java;
import org.telosys.tools.generator.context.Jpa;
import org.telosys.tools.generator.context.Loader;
import org.telosys.tools.generator.context.ModelInContext;
import org.telosys.tools.generator.context.ProjectInContext;
import org.telosys.tools.generator.context.Target;
import org.telosys.tools.generator.context.Today;
import org.telosys.tools.generator.context.names.ContextName;
import org.telosys.tools.generator.directive.AssertFalseDirective;
import org.telosys.tools.generator.directive.AssertTrueDirective;
import org.telosys.tools.generator.directive.DirectiveException;
import org.telosys.tools.generator.directive.ErrorDirective;
import org.telosys.tools.generator.directive.UsingDirective;
import org.telosys.tools.generator.events.GeneratorEvents;
import org.telosys.tools.repository.model.RepositoryModel;

/**
 * This class is a Velocity generator ready to use. <br>
 * It is not supposed to be used directly by the application ( visibility "package" ) <br>
 * It is designed to be used only by the GenerationManager <br>
 * 
 * It holds : <br>
 * . the template file to use <br>
 * . the Velocity Engine <br>
 * . the Velocity Context  <br>
 * <br> 
 * After creation, each instance of this class has a Velocity Context initialized with <br>
 * . the generator variables : $generator, $today <br>
 * . the project variables <br>
 * . etc
 * 
 * @author Laurent Guerin
 *  
 */
public class Generator {

	public final static boolean CREATE_DIR = true ;
	public final static boolean DO_NOT_CREATE_DIR = false ;
	
	//private final RepositoryModel     _repositoryModel ; // v 2.0.7
	//private final List<JavaBeanClass> _allEntities ; // v 2.0.7
	
	private final VelocityEngine     _velocityEngine ;

	private final VelocityContext    _velocityContext ;

	private final GeneratorConfig    _generatorConfig ;
	
	private final TelosysToolsLogger _logger ;

	private final String             _sTemplateFileName ;

	/**
	 * Constructor
	 * @param target the target to be generated
	 * @param generatorConfig the generator configuration
	 * @param repositoryModel the current repository model
	 * @param logger
	 * @throws GeneratorException
	 */
	public Generator( Target target, GeneratorConfig generatorConfig, 
						RepositoryModel repositoryModel, TelosysToolsLogger logger) throws GeneratorException 
	{
		_logger = logger;
		
		if ( null == target) {
			throw new GeneratorException("Target is null (Generator constructor argument)");
		}
		String sTemplateFileName = target.getTemplate(); 
		
		log("Generator constructor (" + sTemplateFileName + ")");

		if ( null == sTemplateFileName) {
			throw new GeneratorException("Template file name is null (Generator constructor argument)");
		}
		if ( null == generatorConfig) {
			throw new GeneratorException("Generator configuration is null (Generator constructor argument)");
		}
		
		_generatorConfig = generatorConfig ;
		
//		//_repositoryModel = repositoryModel ;
//		// Build the list of all the entities defined in the repository  
//		if ( repositoryModel != null ) {
//			//_allEntities = RepositoryModelUtil.buildAllJavaBeanClasses(repositoryModel,	_generatorConfig.getProjectConfiguration() );
//			_allEntities = RepositoryModelUtil.buildAllJavaBeanClasses(repositoryModel,	_generatorConfig ); // v 2.1.0
//		}
//		else {
//			_allEntities = new LinkedList<JavaBeanClass>();
//		}
		
		//------------------------------------------------------------------
		// Workaround for Velocity error in OSGi environment
		// "The specified class for ResourceManager (ResourceManagerImpl) does not implement ResourceManager"
		// ( see https://github.com/whitesource/whitesource-bamboo-agent/issues/9 )
		//------------------------------------------------------------------
		Thread currentThread = Thread.currentThread();
		ClassLoader originalClassLoader = currentThread.getContextClassLoader();
		currentThread.setContextClassLoader(this.getClass().getClassLoader()); // Set the context ClassLoader for this Thread
		try {
			
			//------------------------------------------------------------------
			// 1) Init Velocity context
			//------------------------------------------------------------------
			//--- Create a context
			log("Generator constructor : VelocityContext creation ...");
			_velocityContext = new VelocityContext();
			log("Generator constructor : VelocityContext created.");
			
			log("Generator constructor : VelocityContext events attachment ...");
			GeneratorEvents.attachEvents(_velocityContext);
			log("Generator constructor : VelocityContext events attached.");
	
			log("Generator constructor : VelocityContext initialization ...");
			initContext(generatorConfig, repositoryModel, logger); 
			log("Generator constructor : VelocityContext initialized.");
			
			//------------------------------------------------------------------
			// 2) Init Velocity engine
			//------------------------------------------------------------------
			//--- Get the templates directory and use it to initialize the engine		
			String sTemplateDirectory = generatorConfig.getTemplatesFolderFullPath();		
			log("Templates Directory : '" + sTemplateDirectory + "'");
	
			//--- Check template file existence		
			checkTemplate(sTemplateDirectory, sTemplateFileName);
			_sTemplateFileName  = sTemplateFileName;
	
			log("Generator constructor : VelocityEngine initialization ...");
			_velocityEngine = new VelocityEngine();
			_velocityEngine.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, sTemplateDirectory);
			try {
				// init() : 
				//   initialize the Velocity runtime engine, using the default properties of the Velocity distribution
				// _velocityEngine.init();

				// init(Properties p) : 
				//    initialize the Velocity runtime engine, using default properties 
				//    plus the properties in the passed in java.util.Properties object
				_velocityEngine.init( getSpecificVelocityProperties() ); // ver 2.0.7
				
			} catch (Exception e) {
				throw new GeneratorException("Cannot init VelocityEngine", e );
			}
			log("Generator constructor : VelocityEngine initialized.");
		}
		finally {
			currentThread.setContextClassLoader(originalClassLoader); // Restore the original classLoader
		}
		//------------------------------------------------------------------
		// End of Workaround for Velocity error in OSGi environment
		//------------------------------------------------------------------
	}

	private void log(String s) {
		if (_logger != null) {
			_logger.log(s);
		}
	}

	private void checkTemplate(String sTemplateDirectory,
			String sTemplateFileName) throws GeneratorException {
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

		String sTemplateFullPath = null;
		if (sTemplateDirectory.endsWith("/")) {
			sTemplateFullPath = sTemplateDirectory + sTemplateFileName;
		} else {
			sTemplateFullPath = sTemplateDirectory + "/" + sTemplateFileName;
		}
		File file = new File(sTemplateFullPath);
		if (!file.exists()) {
			throw new GeneratorException("Template file '" + sTemplateFullPath
					+ "' doesn't exist !");
		}
		if (!file.isFile()) {
			throw new GeneratorException("Template file '" + sTemplateFullPath
					+ "' is not a file !");
		}
	}
	
	//========================================================================
	// CONTEXT MANAGEMENT
	//========================================================================
	/**
	 * Returns the Specific Velocity properties to be added at the default Velocity runtime properties
	 * @return
	 */
	private Properties getSpecificVelocityProperties()
	{
		Properties p = new Properties();
		
		// User Directives 
		// userdirective=com.example.MyDirective1, com.example.MyDirective2
		p.setProperty("userdirective", 
				  UsingDirective.class.getCanonicalName() 
				+ ", " 
				+ AssertTrueDirective.class.getCanonicalName() 
				+ ", " 
				+ AssertFalseDirective.class.getCanonicalName() 
				+ ", " 
				+ ErrorDirective.class.getCanonicalName() 
				); // one or n directive(s) separated by a comma 
		
		return p;
	}
	
	//========================================================================
	// CONTEXT MANAGEMENT
	//========================================================================
	private void initContext( GeneratorConfig generatorConfig, RepositoryModel repositoryModel, TelosysToolsLogger logger)
		throws GeneratorException
	{
		log("initContext()..." );

		//--- Special Characters  [LGU 2012-11-29 ]
		_velocityContext.put(ContextName.DOLLAR , "$"  );
		_velocityContext.put(ContextName.SHARP,   "#"  );
		_velocityContext.put(ContextName.AMP,     "&"  ); // ampersand 
		_velocityContext.put(ContextName.QUOT,    "\"" ); // double quotation mark
		_velocityContext.put(ContextName.LT,      "<"  ); // less-than sign
		_velocityContext.put(ContextName.GT,      ">"  ); // greater-than sign
		_velocityContext.put(ContextName.LBRACE,  "{"  ); // left brace
		_velocityContext.put(ContextName.RBRACE,  "}"  ); // right brace
		
		//--- Set the standard Velocity variables in the context
		_velocityContext.put(ContextName.GENERATOR,       new EmbeddedGenerator());  // Limited generator without generation capability 
		_velocityContext.put(ContextName.TODAY,           new Today()); // Current date and time 
		_velocityContext.put(ContextName.CONST,           new Const()); // Constants (static values)
		_velocityContext.put(ContextName.FN,              new Fn(_velocityContext));    // Utility function
		_velocityContext.put(ContextName.JAVA,            new Java());  // Java utility functions
		_velocityContext.put(ContextName.JPA,             new Jpa());   // JPA utility functions
		_velocityContext.put(ContextName.BEAN_VALIDATION, new BeanValidation()); // Bean Validation utility functions

		_velocityContext.put(ContextName.DATABASES,
							new DatabasesInContext( generatorConfig.getDatabasesConfigurations() ) ); // ver 2.1.0
				
		//_velocityContext.put(ContextName.CLASS, null);
		
		//--- Set the dynamic class loader 
		//Loader loader = new Loader(projectConfiguration, _velocityContext);
		Loader loader = new Loader( generatorConfig.getTemplatesFolderFullPath() ); // ver 2.1.0
		_velocityContext.put(ContextName.LOADER, loader);
		
		//--- Set the "$project" variable in the context
//		ProjectConfiguration projectConfiguration = generatorConfig.getProjectConfiguration();
//		_velocityContext.put(ContextName.PROJECT, projectConfiguration);
		_velocityContext.put(ContextName.PROJECT, new ProjectInContext(generatorConfig)); // ver 2.1.0

		//--- Set the "$generation" variable in the context
		_velocityContext.put(ContextName.GENERATION, new GenerationInContext(generatorConfig)); // ver 2.1.0
		
		//--- Get all the project variables and put them in the context	
		//Variable[] projectVariables = projectConfiguration.getAllVariables();
		Variable[] projectVariables = generatorConfig.getTelosysToolsCfg().getAllVariables();
		log("initContext() : Project variables count = " + ( projectVariables != null ? projectVariables.length : 0 ) );

		//--- Set the project variables in the context ( if any )
		if ( projectVariables != null )
		{
			for ( int i = 0 ; i < projectVariables.length ; i++ )
			{
				Variable var = projectVariables[i];
				_velocityContext.put( var.getName(), var.getValue() );
			}
		}
	}

// REMOVED in v 2.1.0
//	/**
//	 * Set the selected entities Java Bean class in the context <br>
//	 * Useful for "Multi-Entities" targets 
//	 * @param javaBeanClasses
//	 * @since Version 2.0.3 ( 2013-Feb )
//	 */
//	//public void setSelectedEntitiesInContext( List<JavaBeanClass> javaBeanClasses )
//	public void setSelectedEntitiesInContext( List<EntityInContext> javaBeanClasses )
//	{
//		if ( javaBeanClasses != null ) {
//			_velocityContext.put(ContextName.SELECTED_ENTITIES, javaBeanClasses);
//		}
//	}

//	/**
//	 * Set the current JavaClass target in the context ( the "$class" variable ) <br>
//	 * Useful for WIZARDS to set the current "$class"
//	 * 
//	 * @param javaClass
//	 */
//	public void setJavaClassTargetInContext(JavaClass javaClass) 
//	{
//		_velocityContext.put(ContextName.CLASS, javaClass);
//	}
	
	/**
	 * Set a new attribute (variable) in the Velocity Context <br>
	 * Useful for WIZARDS to add specific variables if necessary 
	 * 
	 * @param sName
	 * @param oValue
	 */
	public void setContextAttribute(String sName, Object oValue) 
	{
		_velocityContext.put(sName, oValue);
	}

	/**
	 * Returns the Velocity Template instance
	 * @return
	 * @throws GeneratorException
	 */
	private Template getTemplate() throws GeneratorException {
		if (_velocityEngine == null) {
			throw new GeneratorException("Velocity engine is null!");
		}
		log("getTemplate() : Template file name = '" + _sTemplateFileName + "'");
		Template template = null;
		try {
			template = _velocityEngine.getTemplate(_sTemplateFileName);
		} catch (ResourceNotFoundException e) {
			throw new GeneratorException("Cannot get template : ResourceNotFoundException ! ", e );
		} catch (ParseErrorException e) {
			throw new GeneratorException("Cannot get template : Velocity ParseErrorException ! ", e );
		} catch (Exception e) {
			throw new GeneratorException("Cannot get template : Exception ! ", e );
		}
		return template;
	}

	private void generate(Writer writer, Template template)
			throws GeneratorException {
		log("generate(writer, template)...");
		try {
			//--- Generate in a Writer
			template.merge(_velocityContext, writer);
		} catch (ResourceNotFoundException e) {
			throw new GeneratorException("Generation error : ResourceNotFoundException ", e);
		} catch (ParseErrorException e) {
			throw new GeneratorException("Generation error : ParseErrorException ", e);
		} catch (MethodInvocationException e) {
			throw new GeneratorException("Generation error : MethodInvocationException ", e);
		} catch (GeneratorContextException e) {
			throw new GeneratorException("Generation error : GeneratorContextException ", e);
		} catch (DirectiveException e) {
			throw new GeneratorException("Generation error : DirectiveException ", e);
		} catch (Exception e) {
			throw new GeneratorException("Generation error : Exception ", e);
		}
	}

	private void generate(Writer writer) throws GeneratorException {
		log("generate(writer) : getTemplate() ...");
		Template template = getTemplate();
		log("generate(writer) : generate(writer, template) ...");		
		generate(writer, template);
	}

	/**
	 * Generates in memory and returns the InputStream on the generation result
	 * @return
	 * @throws GeneratorException
	 */
	private InputStream generateInMemory() throws GeneratorException {
		log("generateInMemory()...");
		StringWriter stringWriter = new StringWriter();
		
		//------------------------------------------------------------------
		// Workaround for Velocity error in OSGi environment 
		//------------------------------------------------------------------
		Thread currentThread = Thread.currentThread();
		ClassLoader originalClassLoader = currentThread.getContextClassLoader();
		currentThread.setContextClassLoader(this.getClass().getClassLoader()); // Set the context ClassLoader for this Thread
		try {
			//--- Call VELOCITY ENGINE
			generate(stringWriter);
		}
		finally {
			currentThread.setContextClassLoader(originalClassLoader); // Restore the original classLoader
		}
		//------------------------------------------------------------------
		// End of Workaround for Velocity error in OSGi environment
		//------------------------------------------------------------------
			
		byte[] bytes = stringWriter.toString().getBytes();
		return new ByteArrayInputStream(bytes);
	}

	//================================================================================================
	// generateTarget moved from GenerationManager to Generator 
	//================================================================================================
	/**
	 * Generates the given target 
	 * @param target the target to be generated
	 * @param repositoryModel the 'repository model'
	 * @param selectedEntitiesNames list of names for all the selected entities (or null if none)
	 * @param generatedTargets list of generated targets to be updated (or null if not useful)
	 * @throws GeneratorException
	 */
	public void generateTarget(Target target, 
			RepositoryModel repositoryModel, 
			List<String> selectedEntitiesNames,
			List<Target> generatedTargets) throws GeneratorException
	{
		_logger.info("Generation in progress : target = " + target.getTargetName() + " / entity = " + target.getEntityName() );
		
		//--- "$env" object : Environment configuration
		EnvInContext env = new EnvInContext() ;
		_velocityContext.put(ContextName.ENV, env);   // ver 2.1.0
		
		EntitiesManager entitiesManager = new EntitiesManager(repositoryModel, _generatorConfig, env);
		
		//--- "$model" object : it provides all the entities (v 2.0.7)
		ModelInContext model = new ModelInContext(repositoryModel, entitiesManager );
		_velocityContext.put(ContextName.MODEL, model); 
		
		//--- Set the "$target"  in the context 
		_velocityContext.put(ContextName.TARGET, target);

		//ProjectConfiguration projectConfiguration = _generatorConfig.getProjectConfiguration();
		
//		// 2013-02-04
//		//JavaBeanClass javaBeanClass = RepositoryModelUtil.buildJavaBeanClass(target, repositoryModel, projectConfiguration) ;
//		JavaBeanClass javaBeanClass = null ;
//		if ( target.getEntityName().trim().length() > 0 ) {
//			//--- Target with entity ( classical target )
//			//javaBeanClass = RepositoryModelUtil.buildJavaBeanClass(target.getEntityName(), repositoryModel, projectConfiguration) ;
//			javaBeanClass = RepositoryModelUtil.buildJavaBeanClass(target.getEntityName(), repositoryModel, _generatorConfig) ; // v 2.1.0
//		}
//		else {
//			//--- Target without entity ( e.g. "once" target )
//			javaBeanClass = null ;
//		}

		//--- List of selected entities ( $selectedEntities )
		List<EntityInContext> selectedEntities = entitiesManager.getEntities( selectedEntitiesNames );
		_velocityContext.put(ContextName.SELECTED_ENTITIES, selectedEntities);
		
		//--- Current entity : "$entity" in context
		EntityInContext entity = null ;
		if ( target.getEntityName().trim().length() > 0 ) {
			//--- Target with entity ( classical target )
			//javaBeanClass = RepositoryModelUtil.buildJavaBeanClass(target.getEntityName(), repositoryModel, _generatorConfig) ; // v 2.1.0
			entity = entitiesManager.getEntity(target.getEntityName() );
		}
		else {
			//--- Target without entity ( e.g. "once" target )
			entity = null ;
		}

		//--- Set the "$entity"  in the context ( the Java Bean Class for this target )
		//_velocityContext.put(ContextName.ENTITY, javaBeanClass ); 
		_velocityContext.put(ContextName.ENTITY, entity ); 
		//_velocityContext.put(ContextName.BEAN_CLASS, javaBeanClass ); // OLD NAME remove in ver 2.1.0
		
		//--- Set the "$generator"  in the context ( "real" embedded generator )
		EmbeddedGenerator embeddedGenerator = new EmbeddedGenerator(
				repositoryModel, _generatorConfig, _logger, selectedEntitiesNames, generatedTargets );
		_velocityContext.put(ContextName.GENERATOR, embeddedGenerator );
		
		//---------- ((( GENERATION ))) 
		InputStream is = generateInMemory(); // Generate the target in memory
		_logger.info("Generation done.");

		//---------- Save the result in the file
		String outputFileName = target.getOutputFileNameInFileSystem( _generatorConfig.getProjectLocation() );
		_logger.info("Saving target file : " + outputFileName );
		saveStreamInFile(is, outputFileName, true );
		_logger.info("Target file saved." );
		
		//---------- Add the generated target in the list if any
		if ( generatedTargets != null ) {
			generatedTargets.add(target);
		}
	}
	
	private void saveStreamInFile(InputStream is, String fileName, boolean bCreateDir) throws GeneratorException
	{
		File f = new File(fileName);
		
		//--- Check if it's possible to write the file
		if ( f.exists() )
		{
			if ( ! f.canWrite() )				
			{
				throw new GeneratorException("Cannot write on existing target file '"+ f.toString() + "' !");
			}
		}
		else
		{
			File parent = f.getParentFile();
			if ( ! parent.exists() )
			{
				if ( bCreateDir == false )
				{
					throw new GeneratorException("Target directory '"+ parent.toString() + "' not found !");
				}
				else
				{
					// Create the target file directory(ies)
					parent.mkdirs();				
				}
			}
		}
		
		//--- Write the file
		try {
			OutputStream out = new FileOutputStream(f);
			byte buf[] = new byte[1024];
			int len;
			while ((len = is.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.close();
			is.close();
		} catch (FileNotFoundException e) {
			throw new GeneratorException("Cannot save file "+fileName, e);
		} catch (IOException e) {
			throw new GeneratorException("Cannot save file "+fileName, e);
		}
	}
	
}