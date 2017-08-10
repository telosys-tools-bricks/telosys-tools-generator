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

import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.TelosysToolsLogger;
import org.telosys.tools.commons.bundles.TargetDefinition;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.io.CopyHandler;
import org.telosys.tools.commons.io.OverwriteChooser;
import org.telosys.tools.commons.variables.Variable;
import org.telosys.tools.generator.BundleResourcesManager;
import org.telosys.tools.generator.Generator;
import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generator.context.Target;
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
	
	private final GenerationTaskResult  _result  ;
	
	//--------------------------------------------------------------------------------------------------
	/**
	 * Constructor
	 * @param model
	 * @param selectedEntities list of entities names to be used for code generation
	 * @param bundleName
	 * @param selectedTargets templates targets to be used for code generation
	 * @param resourcesTargets resources targets to be copied (or null if none)
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
		_result = new GenerationTaskResult();
	}
	
	//--------------------------------------------------------------------------------------------------
	// ABSTRACT METHODS
	//--------------------------------------------------------------------------------------------------
	/**
	 * Method called after each file generation <br>
	 * Typically used for refreshing generated files in Eclipse 
	 * @param target
	 * @param fullFileName
	 */
	protected abstract void afterFileGeneration(Target target, String fullFileName) ;

	/**
	 * Method called after each error during the task <br>
	 * @param errorReport
	 * @return true to continue the current task, or false to interrupt the task
	 */
	protected abstract boolean onError(ErrorReport errorReport) ;
	
	//--------------------------------------------------------------------------------------------------
	protected void log(String msg) {
		if ( _logger != null ) {
			_logger.log(this, msg);
		}
	}
	
	private Variable[] getAllProjectVariables() {
//		return _generatorConfig.getTelosysToolsCfg().getAllVariables() ;
		return _telosysToolsCfg.getAllVariables() ;
	}
	
	/**
	 * Run the task : <br>
	 *  1) copy the resources if any<br>
	 *  2) launch the generation<br>
	 * @param taskMonitor
	 * @param overwriteChooser
	 * @param copyHandler
	 * @throws InterruptedException
	 */
	protected void runTask(ITaskMonitor taskMonitor, OverwriteChooser overwriteChooser, CopyHandler copyHandler) 
			throws InterruptedException {
		
		//--- 1) Copy the static resources of the bundle if any (if cancelled : 'InterruptedException' is thrown )
		copyResourcesIfAny(overwriteChooser, copyHandler);
		
		//--- 2) Launch the generation (if cancelled : 'InterruptedException' is thrown )
		generateSelectedTargets(taskMonitor, getAllProjectVariables());
	}
	
	//--------------------------------------------------------------------------------------------------
	/**
	 * Copy the static resources if any 
	 * @param overwriteChooser
	 * @param copyHandler
	 * @throws InterruptedException
	 */
	private void copyResourcesIfAny(OverwriteChooser overwriteChooser, CopyHandler copyHandler) 
			throws InterruptedException { 

//		boolean continueTask = true ; 

		List<TargetDefinition> resourcesTargetsDefinitions = this._resourcesTargets ;
		if ( resourcesTargetsDefinitions != null ) {
			_logger.log(this, "run : copy resources " );
			
			BundleResourcesManager resourcesManager = new BundleResourcesManager( _telosysToolsCfg, _bundleName, _logger);
			int numberOfResourcesCopied = 0 ;
			try {
				numberOfResourcesCopied = resourcesManager.copyTargetsResourcesInProject(
						resourcesTargetsDefinitions, overwriteChooser, copyHandler);
			} catch (Exception e) {
//				ErrorReport errorReport = new ErrorReport("Resources copy error", 
//						buildMessageForException(e), e);
				ErrorReport errorReport = ErrorProcessor.buildErrorReport("Resources copy error", e); // v 3.0.0
				//continueTask = onError(errorReport);
				manageError(errorReport); // throws InterruptedException if 'canceled'
			}
			_result.setNumberOfResourcesCopied(numberOfResourcesCopied);
		}
		else {
			_logger.log(this, "run : no resources to be copied" );
		}
		
//		if ( continueTask == false ) // An error has occurred and the user choose "Cancel"
//		{
//			throw new InterruptedException("The generation task was cancelled");
//		}
	}
	
	//--------------------------------------------------------------------------------------------------
	/**
	 * Generates all the "selected targets" ( once or for each entity depending on the target's type ) 
	 * @param progressMonitor
	 * @param variables
	 * @return true to continue, false to interrupt the task
	 * @throws InterruptedException
	 */
	private void generateSelectedTargets( ITaskMonitor progressMonitor, Variable[] variables ) throws InterruptedException
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
				
		//boolean continueTask = true ; 
		//--- For each entity
		for ( String entityName : _selectedEntities ) {
			
			_logger.log(this, "run : entity " + entityName );
			Entity entity = _model.getEntityByClassName(entityName);
			if ( entity != null ) {
				//--- For each "entity target" 
				for ( TargetDefinition targetDefinition : entityTargets ) {
					
					//--- Get a specialized target for the current entity
					Target target = new Target( targetDefinition, entity, variables ); // v 3.0.0
					
					//continueTask = generateTarget(progressMonitor, target, _selectedEntities); 
					generateTarget(progressMonitor, target, _selectedEntities); // throws InterruptedException if error + 'cancel'
				}
				//--- One TARGET done 
			}
			else {
				ErrorReport errorReport = new ErrorReport("Generation error", 
						"Entity '" + entityName + "' not found in the repository", null);
				//continueTask = onError(errorReport);
				_logger.error("Entity '" + entityName + "' not found in the repository") ;
				manageError(errorReport); // throws InterruptedException if 'canceled'
			}
			//--- One ENTITY done
		} // end of "For each entity"
		
		//--- Finally, generate the "ONCE" targets ( NEW in version 2.0.3 / Feb 2013 )
		for ( TargetDefinition targetDefinition : onceTargets ) {
			//--- Target without current entity
			Target target = new Target( targetDefinition, variables ); // v 3.0.0
			//continueTask = generateTarget(progressMonitor, target, _selectedEntities); 
			generateTarget(progressMonitor, target, _selectedEntities);  // throws InterruptedException if error + 'cancel'
		}
		
		//--- Notifies that the work is done; that is, either the main task is completed or the user canceled it.
		progressMonitor.done();
		
//		if ( 	progressMonitor.isCanceled() // Cancellation of current operation has been requested
//			|| 	continueTask == false ) // An error has occurred and the user choose "Cancel"
//		{
//			throw new InterruptedException("The generation task was cancelled");
//		}
		if ( progressMonitor.isCanceled() ) { // Cancellation of current operation has been requested
			throw new InterruptedException("The generation task was cancelled");
		}
		
	}
	//--------------------------------------------------------------------------------------------------
	/**
	 * Generates the given target. <br>
	 * More than one file can be generated if the embedded generator is used in the template.
	 * @param progressMonitor
	 * @param target
	 * @param selectedEntitiesNames
	 * @throws InterruptedException
	 */
	private void generateTarget(ITaskMonitor progressMonitor, Target target, List<String> selectedEntitiesNames) 
			throws InterruptedException
	{
		//boolean continueTask = true ;
		
		_logger.log(this, "Generate TARGET : entity name '" + target.getEntityName() + "' - target file '" + target.getFile() + "' ");
		
		_currentTarget = target ;
		
		progressMonitor.subTask("Entity '" + target.getEntityName() + "' : target file '" + target.getFile() + "' ");
		
		//--- Possible multiple generated targets for one main target (with embedded generator)
		LinkedList<Target> generatedTargets = new LinkedList<Target>();
		
		Generator generator = new Generator( _telosysToolsCfg, _bundleName, _logger); // v 3.0.0
		try {
			generator.generateTarget(target, _model, selectedEntitiesNames, generatedTargets);
		} catch (GeneratorException e) {
			_result.addGenerationError(target);
			//continueTask = onError(buildErrorReportForGeneratorException(e));
			ErrorReport errorReport = buildErrorReportForGeneratorException(e);
			manageError(errorReport); // throws InterruptedException if 'canceled'
		}

		//--- After normal end of generation : refresh the generated files and update count
		for ( Target generatedTarget : generatedTargets ) {
			_logger.log(this, "generated target : " + generatedTarget.getFile() );

			String generatedFileAbsolutePath = generatedTarget.getOutputFileNameInFileSystem(_telosysToolsCfg.getDestinationFolderAbsolutePath());
			
			//--- One more file : increment result count
			_result.incrementNumberOfFilesGenerated();

			_logger.log(this, "Call afterFileGeneration(" + generatedFileAbsolutePath + ")...");
			afterFileGeneration(generatedTarget, generatedFileAbsolutePath); // Abstract method
		}
		
		//--- One TARGET done
		// Notifies that a given number of work unit of the main task has been completed. 
		// Note that this amount represents an installment, as opposed to a cumulative amount of work done to date.
		progressMonitor.worked(1); // One unit done (not cumulative)
		
		//return continueTask ;
	}
	
	//--------------------------------------------------------------------------------------------------
	/**
	 * Returns the name of the entity currently under generation 
	 * @return
	 */
	private String getCurrentEntityName() {
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
	private String getCurrentTemplateName() {
		if ( _currentTarget == null ) return NO_TEMPLATE ;
		return _currentTarget.getTemplate() ;
	}
	
	//--------------------------------------------------------------------------------------------------
	/**
	 * Returns the result (number of files generated, ... )
	 * @return
	 */
	protected GenerationTaskResult getResult() {
		return _result != null ? _result : new GenerationTaskResult() ;
	}
	
	//--------------------------------------------------------------------------------------------------
	/**
	 * Open a dialog box to show the error <br>
	 * The user can choose to continue or to cancel <br>
	 * If 'cancel' : throws InterruptedException
	 * @param errorReport
	 * @throws InterruptedException 
	 */
	private void manageError( ErrorReport errorReport ) throws InterruptedException {
		_result.addError(errorReport);
		//--- Open the dialog box (the user can choose to continue or to cancel)
		boolean continueTask = onError(errorReport);
		//--- If 'cancel' : throw InterruptedException
		if ( continueTask == false ) {
			throw new InterruptedException("Generation task cancelled");
		}
	}
	//--------------------------------------------------------------------------------------------------
	/**
	 * Build a new ErrorReport from the given exception and add it in the TaskResult
	 * @param exception
	 * @return
	 */
	protected ErrorReport buildErrorReport(InvocationTargetException exception ) {
//		String msg = buildMessageForException(exception)
//					+ buildMessageForExceptionCause(exception);
//
//		ErrorReport errorReport = new ErrorReport( "InvocationTargetException", msg, exception);
		String entityName = this.getCurrentEntityName();
		String templateName = this.getCurrentTemplateName();	
		ErrorReport errorReport = ErrorProcessor.buildErrorReport(exception, entityName, templateName); // v 3.0.0
		_result.addError(errorReport);
		return errorReport ;
	}
	//--------------------------------------------------------------------------------------------------
	private ErrorReport buildErrorReportForGeneratorException(GeneratorException generatorException ) {
		String entityName = this.getCurrentEntityName();
		String templateName = this.getCurrentTemplateName();	
		Throwable generatorExceptionCause = generatorException.getCause() ;
		if ( generatorExceptionCause != null ) {
//			String templateName = this.getCurrentTemplateName() ;
//			String entityName = this.getCurrentEntityName() ;
			//return buildErrorReportForGeneratorExceptionCause(generatorExceptionCause, entityName, templateName);
			return ErrorProcessor.buildErrorReport(generatorExceptionCause, entityName, templateName); // v 3.0.0
		}
		else {
//			String msg = 
//					  buildMessageForTemplateAndEntity( this.getCurrentTemplateName(), 0, this.getCurrentEntityName())
//					+ buildMessageForException(generatorException);
//
//			return new ErrorReport( "Generator error (no cause)", msg, generatorException);
			return ErrorProcessor.buildErrorReport(generatorException, entityName, templateName); // v 3.0.0
		}
	}
	//--------------------------------------------------------------------------------------------------
	// Velocity 1.7 Exceptions 
	// org.apache.velocity.exception.VelocityException ( Runtime Exceptions ) :
	//  - org.apache.velocity.exception.MacroOverflowException
	//  - org.apache.velocity.exception.MathException
	//  - org.apache.velocity.exception.MethodInvocationException
	//  - org.apache.velocity.exception.ParseErrorException
	//  - org.apache.velocity.exception.ResourceNotFoundException
	//  - org.apache.velocity.exception.TemplateInitException
	// org.apache.velocity.runtime.parser.ParseException ( Checked Exceptions ) :
	//  - org.apache.velocity.runtime.directive.MacroParseException
	//  - org.apache.velocity.runtime.parser.TemplateParseException
	//    
	//--------------------------------------------------------------------------------------------------
//	private ErrorReport buildErrorReportForGeneratorExceptionCause(Throwable generatorExceptionCause, String entityName, String templateName ) {
//		
//		if ( generatorExceptionCause instanceof DirectiveException ) {
//			//--- DIRECTIVE ERROR ( Telosys Tools exception )
//			// eg : #using ( "varNotDefined" )
//			DirectiveException directiveException = (DirectiveException) generatorExceptionCause ;
//			String msg = 
//				  buildMessageForTemplateAndEntity( directiveException.getTemplateName(), directiveException.getLineNumber(), entityName)
//				+ buildMessageForException(directiveException)
//				+ "Directive  #" + directiveException.getDirectiveName() + " \n\n" 
//				+ directiveException.getMessage() ;
//
//			return new ErrorReport( "Directive error", msg, directiveException);
//		}
//		else if ( generatorExceptionCause instanceof ParseErrorException ) {
//			//--- TEMPLATE PARSING ERROR ( Velocity exception )
//			// eg : #set(zzz)
//			ParseErrorException parseErrorException = (ParseErrorException) generatorExceptionCause ;
//			String msg = 
//				  buildMessageForTemplateAndEntity( parseErrorException.getTemplateName(), parseErrorException.getLineNumber(), entityName)
//				+ buildMessageForException(parseErrorException);
//			return new ErrorReport( "Template parsing error", msg, parseErrorException );
//		}
//		else if ( generatorExceptionCause instanceof MethodInvocationException ) {
//			//--- METHOD INVOCATION ( Velocity exception )
//			// eg : $fn.isNotVoid("") : collection argument expected 
//			MethodInvocationException methodInvocationException = (MethodInvocationException) generatorExceptionCause ;
//			String msg = 
//				  buildMessageForTemplateAndEntity( methodInvocationException.getTemplateName(), methodInvocationException.getLineNumber(), entityName)
//				+ buildMessageForException(methodInvocationException)
//				+ "Reference name : '" + methodInvocationException.getReferenceName() + "'"
//				+ "\n" 
//				+ "Method name : '" + methodInvocationException.getMethodName() + "'"
//				+ "\n\n" 
//				+ buildMessageForExceptionCause(generatorExceptionCause) 
//				;
//			return new ErrorReport( "Method invocation error", msg, methodInvocationException );
//		}			
//		else if ( generatorExceptionCause instanceof ResourceNotFoundException ) {
//			//--- RESOURCE NOT FOUND ( Velocity exception )
//			ResourceNotFoundException resourceNotFoundException = (ResourceNotFoundException) generatorExceptionCause ;
//			String msg = 
//				  buildMessageForTemplateAndEntity( templateName, 0, entityName )
//				+ buildMessageForException(resourceNotFoundException); 
//			return new ErrorReport( "Resource not found", msg, resourceNotFoundException );
//		}			
//		else if ( generatorExceptionCause instanceof GeneratorContextException ) {
//			//--- CONTEXT ERROR ( Telosys Tools exception )
//			// Reflection error encapsulation
//			// eg : $entity.tototo / $entity.getTTTTTTTTT() / $entity.name.toAAAAA()
//			// or errors due to invalid model 
//			GeneratorContextException generatorContextException = (GeneratorContextException) generatorExceptionCause ;
//			// generatorContextException.getTemplateName() not always know the template => use templateName arg
//			String msg = 
//				  buildMessageForTemplateAndEntity( templateName, generatorContextException.getLineNumber(), entityName)
//				+ buildMessageForException(generatorContextException) ;
//			return new ErrorReport( "Context error", msg, generatorContextException );
//		}
//		else if ( generatorExceptionCause instanceof VelocityException ) {
//			//--- Generic Velocity exception (eg "OutOfBoud" )
//			VelocityException velocityException = (VelocityException) generatorExceptionCause ;
//			String msg = 
//				  buildMessageForTemplateAndEntity( templateName, 0, entityName)
//				+ buildMessageForException(velocityException) 
//				+ buildMessageForExceptionCause(velocityException);
//			return new ErrorReport( "Velocity error", msg, velocityException);
//		}
//		else {
//			String msg = 
//				  buildMessageForTemplateAndEntity( templateName, 0, entityName)
//				+ buildMessageForException(generatorExceptionCause) ;
//			return new ErrorReport("Unknown error", msg, generatorExceptionCause );
//		}
//	}
//	
//	//-------------------------------------------------------------------------------------------------------------
//	private String buildMessageForTemplateAndEntity(String template, int line, String entity ) {
//		String lineMsg = "" ;
//		if ( line > 0 ) {
//			lineMsg = "  ( line " + line + " )" ;
//		}
//		return "Template \"" + template + "\"" + lineMsg + "  -  Entity : \"" 
//				+ entity + "\" \n\n" ;
//	}
//	//-------------------------------------------------------------------------------------------------------------
//	private String buildMessageForException( Throwable exception ) {
//		String s = exception.getClass().getSimpleName() + " : \n" 
//					+ exception.getMessage() + "\n"
//					+ "\n" ;
//		return s ;
//	}
//	//-------------------------------------------------------------------------------------------------------------
//	private String buildMessageForExceptionCause(Throwable exception) {
//		Throwable cause = exception.getCause();
//		if ( cause != null ) {
//			StringBuilder sb = new StringBuilder() ;
//			int n = 0 ;
//			while ( cause != null ) {
//				n++ ;
//				sb.append( "Cause #" + n + " : " );
//				sb.append( cause.getClass().getSimpleName() );
//				sb.append( " - " );
//				sb.append( cause.getMessage()  );
//				sb.append( "\n" );
//				StackTraceElement[] stackTrace = cause.getStackTrace() ;
//				if ( stackTrace != null ) {
//					for ( int i = 0 ; ( i < stackTrace.length ) && ( i < 5 ) ; i++ ) {
//						StackTraceElement e = stackTrace[i];
//						sb.append( e.getFileName() ) ;
//						sb.append( " - " ) ;
//						sb.append( e.getMethodName() ) ;
//						sb.append( " - line " ) ;
//						sb.append( e.getLineNumber() ) ;
//						sb.append( "\n" );
//					}
//				}
//				sb.append( "\n" );
//				sb.append( "\n" );
//				cause = cause.getCause() ;
//			}
//			return sb.toString();
//		}
//		else {
//			return "No cause.\n" ;
//		}
//	}
}
