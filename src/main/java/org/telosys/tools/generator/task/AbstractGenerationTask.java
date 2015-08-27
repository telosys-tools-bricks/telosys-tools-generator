/**
 *  Copyright (C) 2008-2015  Telosys project org. ( http://www.telosys.org/ )
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
package org.telosys.tools.generator.task;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.TelosysToolsLogger;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.io.CopyHandler;
import org.telosys.tools.commons.io.OverwriteChooser;
import org.telosys.tools.commons.variables.Variable;
import org.telosys.tools.generator.BundleResourcesManager;
import org.telosys.tools.generator.Generator;
import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generator.context.Target;
import org.telosys.tools.generator.engine.GeneratorContextException;
import org.telosys.tools.generator.engine.directive.DirectiveException;
import org.telosys.tools.generator.target.TargetDefinition;
import org.telosys.tools.generic.model.Entity;
import org.telosys.tools.generic.model.Model;


/**
 * Generator runnable task (design to be used with a GUI progress bar) <br>
 * This task is used to generated a set of target in the current bundle
 *  
 * @author Laurent Guerin
 *
 */
public abstract class AbstractGenerationTask
{
	private final static String ENTITY_NONE = "(no entity)" ;
	private final static String NO_TEMPLATE = "(no template)" ;
	
	private final List<String>            _selectedEntities ;
	private final List<TargetDefinition>  _selectedTargets ;
	private final List<TargetDefinition>  _resourcesTargets ;
	private final Model                   _model ;
	//private final GeneratorConfig         _generatorConfig ; // removed in v 3.0.0
	private final TelosysToolsCfg         _telosysToolsCfg ; // v 3.0.0
	private final String                  _bundleName ;
	private final TelosysToolsLogger      _logger ;
	
	private Target                _currentTarget = null ;
	
	private GenerationTaskResult  _result = null ;
	
	//--------------------------------------------------------------------------------------------------
	/**
	 * Constructor
	 * @param model
	 * @param selectedEntities
	 * @param bundleName
	 * @param selectedTargets
	 * @param resourcesTargets
	 * @param telosysToolsCfg
	 * @param logger
	 * @throws TelosysToolsException
	 */
	public AbstractGenerationTask(
			Model                     model,
			List<String>              selectedEntities, 
			String                    bundleName, // v 3.0.0
			List<TargetDefinition>    selectedTargets,
			List<TargetDefinition>    resourcesTargets,
			// GeneratorConfig           generatorConfig,  // removed in v 3.0.0
			TelosysToolsCfg           telosysToolsCfg, // v 3.0.0
			TelosysToolsLogger        logger
			) throws TelosysToolsException
	{
		super();
		
		if ( model  == null ) throw new TelosysToolsException("model param is null ");
		if ( selectedEntities == null ) throw new TelosysToolsException("selectedEntities param is null ");
		if ( bundleName  == null ) throw new TelosysToolsException("bundle name param is null ");
		if ( selectedTargets  == null ) throw new TelosysToolsException("selectedTargets param is null ");
		// resourcesTargets : can be null
		//if ( generatorConfig  == null ) throw new TelosysToolsException("generatorConfig param is null ");
		if ( telosysToolsCfg  == null ) throw new TelosysToolsException("TelosysToolsCfg param is null ");
		if ( logger  == null )  throw new TelosysToolsException("logger param is null ");

		_model            = model;
		_selectedEntities = selectedEntities ;
		_selectedTargets  = selectedTargets ;
		_resourcesTargets = resourcesTargets ; // can be null
		//_generatorConfig  = generatorConfig ;
		_telosysToolsCfg  = telosysToolsCfg ; // v 3.0.0
		//_bundleName       = generatorConfig.getBundleName() ; 
		_bundleName       = bundleName ;  // v 3.0.0
		_logger           = logger ;
		
		_logger.log(this, "Task created");
		
	}
	
	//--------------------------------------------------------------------------------------------------
	// ABSTRACT METHODS
	//--------------------------------------------------------------------------------------------------
	/**
	 * Method used to run the task
	 * @return
	 */
	public abstract GenerationTaskResult run() ;
	
	/**
	 * Method called after each file generation <br>
	 * Typically used for refreshing generated files in Eclipse 
	 * @param fullFileName
	 */
	protected abstract void afterFileGeneration(String fullFileName) ;

	/**
	 * Method used to show an error message
	 * @param message
	 * @param exception
	 */
	protected abstract void showErrorMessage(String message, Throwable exception) ;

	/**
	 * Method used to show an error message
	 * @param message1
	 * @param message2
	 */
	protected abstract void showErrorMessage(String message1, String message2) ;

	//--------------------------------------------------------------------------------------------------
	protected void log(String msg) {
		if ( _logger != null ) {
			_logger.log(this, msg);
		}
	}
	
	protected Variable[] getAllProjectVariables() {
//		return _generatorConfig.getTelosysToolsCfg().getAllVariables() ;
		return _telosysToolsCfg.getAllVariables() ;
	}
	
	//--------------------------------------------------------------------------------------------------
	/**
	 * Copy the static resources if any 
	 * @param overwriteChooser
	 * @param copyHandler
	 * @return
	 * @throws InvocationTargetException
	 */
	protected int copyResourcesIfAny(OverwriteChooser overwriteChooser, CopyHandler copyHandler) throws InvocationTargetException {

		List<TargetDefinition> resourcesTargetsDefinitions = this._resourcesTargets ;
		int count = 0 ;
		if ( resourcesTargetsDefinitions != null ) {
			_logger.log(this, "run : copy resources " );
			
//			BundleResourcesManager resourcesManager = new BundleResourcesManager( _generatorConfig.getTelosysToolsCfg(), _bundleName, _logger);
			BundleResourcesManager resourcesManager = new BundleResourcesManager( _telosysToolsCfg, _bundleName, _logger);
			try {
				count = resourcesManager.copyTargetsResourcesInProject(resourcesTargetsDefinitions, overwriteChooser, copyHandler);
			} catch (Exception e) {
				throw new InvocationTargetException(e);
			}
		}
		else {
			_logger.log(this, "run : no resources to be copied" );
		}
		return count ;
	}
	
	//--------------------------------------------------------------------------------------------------
	/**
	 * Generates all the "selected targets" ( once or for each entity depending on the target's type ) 
	 * @param progressMonitor
	 * @param variables
	 * @return
	 * @throws InvocationTargetException
	 * @throws InterruptedException
	 */
	protected int generateSelectedTargets( ITaskMonitor progressMonitor, Variable[] variables ) 
				throws InvocationTargetException, InterruptedException 
	{
		//--- Separate targets in 2 list : "ONCE" and "ENTITY"
		List<TargetDefinition> onceTargets   = new LinkedList<TargetDefinition>() ; 
		List<TargetDefinition> entityTargets = new LinkedList<TargetDefinition>() ; 
		for ( TargetDefinition targetDefinition : _selectedTargets ) {
			if ( targetDefinition.isOnce() ) {
				onceTargets.add(targetDefinition); 
			}
			else {
				entityTargets.add(targetDefinition);
			}
		}
		
		//--- Number of generations expected
		int totalWorkTasks = ( _selectedEntities.size() * entityTargets.size() ) + onceTargets.size() ;

		progressMonitor.beginTask("Generation in progress", totalWorkTasks ); 
				
		int numberOfFilesGenerated = 0 ; 
		//--- For each entity
		for ( String entityName : _selectedEntities ) {
			
			_logger.log(this, "run : entity " + entityName );
			// Entity entity = _repositoryModel.getEntityByName(entityName);
			// EntityInDbModel entity = _repositoryModel.getEntityByTableName(entityName);
			Entity entity = _model.getEntityByClassName(entityName);
			if ( entity != null ) {
				//--- For each "entity target" 
				for ( TargetDefinition targetDefinition : entityTargets ) {
					
					//--- Get a specialized target for the current entity
//					Target target = new Target( targetDefinition, entity.getName(), 
//							entity.getBeanJavaClass(), variables );
					Target target = new Target( targetDefinition, entity, variables ); // v 3.0.0
					
					numberOfFilesGenerated = numberOfFilesGenerated + generateTarget(progressMonitor, target, _selectedEntities); 
					
				}
				//--- One TARGET done
			}
			else {
				_logger.error("Entity '" + entityName + "' not found in the repository") ;
			}
			//--- One ENTITY done
		} // end of "For each entity"
		
		//--- Finally, generate the "ONCE" targets ( NEW in version 2.0.3 / Feb 2013 )
		for ( TargetDefinition targetDefinition : onceTargets ) {
			//--- Target without current entity
			// Target target = new Target( targetDefinition, "", "", variables );
			Target target = new Target( targetDefinition, variables ); // v 3.0.0
			numberOfFilesGenerated = numberOfFilesGenerated + generateTarget(progressMonitor, target, _selectedEntities); 
		}
		
		//--- Notifies that the work is done; that is, either the main task is completed or the user canceled it.
		progressMonitor.done();
		
		if (progressMonitor.isCanceled()) // Returns whether cancellation of current operation has been requested
		{
			throw new InterruptedException("The bulk generation was cancelled");
		}
		
		return numberOfFilesGenerated ;
	}
	//--------------------------------------------------------------------------------------------------
	/**
	 * Generates the given target. <br>
	 * More than one file can be generated if the embedded generator is used in the template.
	 * @param progressMonitor
	 * @param target
	 * @param selectedEntitiesNames
	 * @return
	 * @throws InvocationTargetException
	 * @throws InterruptedException
	 */
	private int generateTarget(ITaskMonitor progressMonitor, // IProgressMonitor progressMonitor, 
			Target target, List<String> selectedEntitiesNames) 
					throws InvocationTargetException, InterruptedException 
	{

		int count = 0 ;
		_logger.log(this, "Generate TARGET : entity name '" + target.getEntityName() + "' - target file '" + target.getFile() + "' ");
		
		_currentTarget = target ;
		
		progressMonitor.subTask("Entity '" + target.getEntityName() + "' : target file '" + target.getFile() + "' ");
		
		//--- Possible multiple generated targets for one main target (with embedded generator)
		LinkedList<Target> generatedTargets = new LinkedList<Target>();
		try {
			//Generator generator = new Generator(target, _generatorConfig, _repositoryModel, _logger); // v 2.0.7
//			Generator generator = new Generator( _generatorConfig, _logger); // v 3.0.0
			Generator generator = new Generator( _telosysToolsCfg, _bundleName, _logger); // v 3.0.0
			generator.generateTarget(target, _model, selectedEntitiesNames, generatedTargets);						
			
		} catch (GeneratorException e) {
			// if the "run" method must propagate a checked exception, 
			// it should wrap it inside an InvocationTargetException; 
			throw new InvocationTargetException(e);
		}

		//--- Refresh the generated files
		for ( Target generatedTarget : generatedTargets ) {
			_logger.log(this, "Refresh generated target : " + generatedTarget.getFile() );

			//String outputFileNameInProject = generatedTarget.getOutputFileNameInProject() ;
//			String projectLocation = _generatorConfig.getTelosysToolsCfg().getProjectAbsolutePath();
			String projectLocation = _telosysToolsCfg.getProjectAbsolutePath();
			String outputFileNameInFileSystem = generatedTarget.getOutputFileNameInFileSystem(projectLocation);
			_logger.log(this, "Call afterFileGeneration(" + outputFileNameInFileSystem + ")...");
			
			afterFileGeneration(outputFileNameInFileSystem);
			
			//--- One more file : increment result count
			count++ ;
		}
		
		//--- One TARGET done
		// Notifies that a given number of work unit of the main task has been completed. 
		// Note that this amount represents an installment, as opposed to a cumulative amount of work done to date.
		progressMonitor.worked(1); // One unit done (not cumulative)
		
		return count ;
	}
	
	//--------------------------------------------------------------------------------------------------
	/**
	 * Returns the name of the entity currently under generation 
	 * @return
	 */
	public String getCurrentEntityName() {
		if ( _currentTarget == null ) return ENTITY_NONE ;
		String entityName = _currentTarget.getEntityName() ;
		if ( entityName == null ) {
			return ENTITY_NONE ;
		}
		else if ( entityName.trim().length() == 0 ) {
			return ENTITY_NONE ;
		}
		else {
			return entityName ;
		}
	}
	
	//--------------------------------------------------------------------------------------------------
	/**
	 * Returns the name of the template currently in use for generation 
	 * @return
	 */
	public String getCurrentTemplateName() {
		if ( _currentTarget == null ) return NO_TEMPLATE ;
		return _currentTarget.getTemplate() ;
	}
	
	//--------------------------------------------------------------------------------------------------
	/**
	 * Returns the result (number of files generated, ... )
	 * @return
	 */
	public GenerationTaskResult getResult() {
		return _result != null ? _result : new GenerationTaskResult() ;
	}
	
	/**
	 * Defines the task result
	 * @param numberOfResourcesCopied
	 * @param numberOfFilesGenerated
	 */
	protected void setResult(int numberOfResourcesCopied, int numberOfFilesGenerated) {
		this._result = new GenerationTaskResult(numberOfResourcesCopied, numberOfFilesGenerated);
	}
	
	//-------------------------------------------------------------------------------------------------------------
	/**
	 * Specific message depending on the type of exception
	 * @param invocationTargetException
	 * @since 2.0.7
	 */
	//protected void showGenerationError(InvocationTargetException invocationTargetException, String templateName, String entityName) {
	protected void showGenerationError(InvocationTargetException invocationTargetException ) {
		String templateName = this.getCurrentTemplateName() ;
		String entityName = this.getCurrentEntityName() ;
		
		Throwable cause = invocationTargetException.getCause();
		if ( cause instanceof GeneratorException ) {
			GeneratorException generatorException = (GeneratorException) cause ;
			Throwable generatorExceptionCause = generatorException.getCause() ;
			
			if ( generatorExceptionCause instanceof DirectiveException ) {
				//--- DIRECTIVE ERROR ( Telosys Tools exception )
				// eg : #using ( "varNotDefined" )
				DirectiveException directiveException = (DirectiveException) generatorExceptionCause ;
				String msg1 = buildErrorMessageHeader( directiveException.getTemplateName(), 
						directiveException.getLineNumber(), entityName);
				
				String msg2 = "Directive  #" + directiveException.getDirectiveName() + " \n\n" 
					+ directiveException.getMessage() ;

				//MsgBox.error( "Directive error", msg1 + msg2 );
				showErrorMessage( "Directive error", msg1 + msg2 );
			}
			else if ( generatorExceptionCause instanceof ParseErrorException ) {
				//--- TEMPLATE PARSING ERROR ( Velocity exception )
				// eg : #set(zzz)
				ParseErrorException parseErrorException = (ParseErrorException) generatorExceptionCause ;
				String msg1 = buildErrorMessageHeader( parseErrorException.getTemplateName(), 
						parseErrorException.getLineNumber(), entityName);
				String msg2 = parseErrorException.getMessage() ;
				//MsgBox.error( "Template parsing error", msg1 + msg2 );
				showErrorMessage( "Template parsing error", msg1 + msg2 );
			}
			else if ( generatorExceptionCause instanceof MethodInvocationException ) {
				//--- METHOD INVOCATION ( Velocity exception )
				// eg : $fn.isNotVoid("") : collection argument expected 
				MethodInvocationException methodInvocationException = (MethodInvocationException) generatorExceptionCause ;
				String msg1 = buildErrorMessageHeader( methodInvocationException.getTemplateName(), 
						methodInvocationException.getLineNumber(), entityName);
				String msg2 =  methodInvocationException.getMessage() 
					+ "\n\n" 
					+ "Reference name : '" + methodInvocationException.getReferenceName() + "'"
					+ "\n" 
					+ "Method name : '" + methodInvocationException.getMethodName() + "'"
					+ "\n\n" 
					+ getCauseMessage(generatorExceptionCause) 
					;
				//MsgBox.error( "Method invocation error", msg1 + msg2 );
				showErrorMessage( "Method invocation error", msg1 + msg2 );
			}			
			else if ( generatorExceptionCause instanceof ResourceNotFoundException ) {
				//--- RESOURCE NOT FOUND ( Velocity exception )
				ResourceNotFoundException resourceNotFoundException = (ResourceNotFoundException) generatorExceptionCause ;
				String msg1 = buildErrorMessageHeader( templateName, 0, entityName);
				String msg2 = resourceNotFoundException.getMessage(); 
				//MsgBox.error( "Resource not found", msg1 + msg2 );
				showErrorMessage( "Resource not found", msg1 + msg2 );
			}			
			else if ( generatorExceptionCause instanceof GeneratorContextException ) {
				//--- CONTEXT ERROR ( Telosys Tools exception )
				// Reflection error encapsulation
				// eg : $entity.tototo / $entity.getTTTTTTTTT() / $entity.name.toAAAAA()
				// or errors due to invalid model 
				GeneratorContextException generatorContextException = (GeneratorContextException) generatorExceptionCause ;
				// generatorContextException.getTemplateName() not always know the template => use templateName arg
				String msg1 = buildErrorMessageHeader( templateName,  // keep templateName here
						generatorContextException.getLineNumber(), entityName); 
				String msg2 = generatorContextException.getMessage() ;
				//MsgBox.error( "Context error", msg1 + msg2 );
				showErrorMessage( "Context error", msg1 + msg2 );
				
			}
			else {
				//MsgBox.error("Error during generation", cause );
				showErrorMessage("Error during generation", cause );
			}
		}
		else {
			
			//MsgBox.error("Error during generation", cause );
			showErrorMessage("Error during generation", cause );
		}
		
	}
	
	//-------------------------------------------------------------------------------------------------------------
	private String buildErrorMessageHeader(String template, int line, String entity ) {
		String lineMsg = "" ;
		if ( line > 0 ) {
			lineMsg = "  ( line " + line + " )" ;
		}
		return "Template \"" + template + "\"" + lineMsg + "  -  Entity : \"" 
				+ entity + "\" \n\n" ;
	}
	//-------------------------------------------------------------------------------------------------------------
	private String getCauseMessage(Throwable exception) {
		Throwable cause = exception.getCause();
		if ( cause != null ) {
			StringBuilder sb = new StringBuilder() ;
			int n = 0 ;
			while ( cause != null ) {
				n++ ;
				sb.append( "Cause #" + n + " : " );
				sb.append( cause.getClass().getSimpleName() );
				sb.append( "\n" );
				sb.append( cause.getMessage()  );
				sb.append( "\n" );
				sb.append( "\n" );
				cause = cause.getCause() ;
			}
			return sb.toString();
		}
		else {
			return "No cause.\n" ;
		}
	}
}
