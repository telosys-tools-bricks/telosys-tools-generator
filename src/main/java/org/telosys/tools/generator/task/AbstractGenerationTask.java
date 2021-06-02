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
	private static final String ENTITY_NONE = "(no entity)" ;
	private static final String NO_TEMPLATE = "(no template)" ;
	
	private final List<String>            selectedEntities ;
	private final List<TargetDefinition>  selectedTargets ;
	private final List<TargetDefinition>  resourcesTargets ;
	private final Model                   model ;
	private final TelosysToolsCfg         telosysToolsCfg ;
	private final String                  bundleName ;
	private final TelosysToolsLogger      logger ;
	private final GenerationTaskResult    genTaskResult  ;
	
	private Target                currentTarget = null ;

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
	protected AbstractGenerationTask(
			Model                     model,
			List<String>              selectedEntities, 
			String                    bundleName, // v 3.0.0
			List<TargetDefinition>    selectedTargets,
			List<TargetDefinition>    resourcesTargets,
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
		if ( telosysToolsCfg  == null ) throw new TelosysToolsException("TelosysToolsCfg param is null ");
		if ( logger  == null )  throw new TelosysToolsException("logger param is null ");

		this.model            = model;
		this.selectedEntities = selectedEntities ;
		this.selectedTargets  = selectedTargets ;
		this.resourcesTargets = resourcesTargets ; // can be null
		this.telosysToolsCfg  = telosysToolsCfg ; // v 3.0.0
		this.bundleName       = bundleName ;  // v 3.0.0
		this.logger           = logger ;
		
		this.logger.log(this, "Task created");
		this.genTaskResult = new GenerationTaskResult();
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
		if ( logger != null ) {
			logger.log(this, msg);
		}
	}
	
	private Variable[] getAllProjectVariables() {
		return telosysToolsCfg.getAllVariables() ;
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
		logger.info("----- Copy static resources if any " );
		List<TargetDefinition> resourcesTargetsDefinitions = this.resourcesTargets ;
		if ( resourcesTargetsDefinitions != null ) {
			logger.log(this, "run : copy resources " );
			
			BundleResourcesManager resourcesManager = new BundleResourcesManager( telosysToolsCfg, bundleName, logger);
			int numberOfResourcesCopied = 0 ;
			try {
				numberOfResourcesCopied = resourcesManager.copyTargetsResourcesInProject(
						resourcesTargetsDefinitions, overwriteChooser, copyHandler);
			} catch (Exception e) {
				//ErrorReport errorReport = ErrorProcessor.buildErrorReport("Resources copy error", e); // v 3.0.0
				ErrorReport errorReport = new ErrorReport(e, null, null); // v 3.3.0
				manageError(errorReport); // throws InterruptedException if 'canceled'
			}
			genTaskResult.setNumberOfResourcesCopied(numberOfResourcesCopied);
			logger.info(numberOfResourcesCopied + " resource(s) copied" );
		}
		else {
			logger.log(this, "run : no resources to copy" );
			logger.info("No resources to copy" );
		}
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
		List<TargetDefinition> onceTargets   = new LinkedList<>() ; 
		List<TargetDefinition> entityTargets = new LinkedList<>() ; 
		for ( TargetDefinition targetDefinition : selectedTargets ) {
			if ( targetDefinition.isOnce() ) {
				onceTargets.add(targetDefinition); 
			}
			else {
				entityTargets.add(targetDefinition);
			}
		}
		
		//--- Number of generations expected
		int totalWorkTasks = ( selectedEntities.size() * entityTargets.size() ) + onceTargets.size() ;

		progressMonitor.beginTask("Generation in progress", totalWorkTasks ); 
				
		//--- For each entity
		for ( String entityName : selectedEntities ) {
			
			logger.info("----- Generation for entity " + entityName );
			Entity entity = model.getEntityByClassName(entityName);
			if ( entity != null ) {
				//--- For each "entity target" 
				for ( TargetDefinition targetDefinition : entityTargets ) {
					
					//--- Get a specialized target for the current entity
					Target target = new Target( targetDefinition, entity, variables ); // v 3.0.0
					
					generateTarget(progressMonitor, target, selectedEntities); // throws InterruptedException if error + 'cancel'
				}
				//--- One TARGET done 
			}
			else {
				String msg = "Generation error : entity '" + entityName + "' not found in the repository";
				ErrorReport errorReport = new ErrorReport(msg);
				logger.error("Entity '" + entityName + "' not found in the repository") ;
				manageError(errorReport); // throws InterruptedException if 'canceled'
			}
			//--- One ENTITY done
		} // end of "For each entity"
		
		//--- Finally, generate the "ONCE" targets ( NEW in version 2.0.3 / Feb 2013 )
		logger.info("----- Generation without entity" );
		for ( TargetDefinition targetDefinition : onceTargets ) {
			//--- Target without current entity
			Target target = new Target( targetDefinition, variables ); // v 3.0.0
			generateTarget(progressMonitor, target, selectedEntities);  // throws InterruptedException if error + 'cancel'
		}
		
		//--- Notifies that the work is done; that is, either the main task is completed or the user canceled it.
		progressMonitor.done();
		
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
		logger.log(this, "Generate TARGET : entity name '" + target.getEntityName() + "' - target file '" + target.getFile() + "' ");
		
		currentTarget = target ;
		
		progressMonitor.subTask("Entity '" + target.getEntityName() + "' : target file '" + target.getFile() + "' ");
		
		//--- Possible multiple generated targets for one main target (with embedded generator)
		LinkedList<Target> generatedTargets = new LinkedList<>();
		
		Generator generator = new Generator( telosysToolsCfg, bundleName, logger); // v 3.0.0
		try {
			generator.generateTarget(target, model, selectedEntitiesNames, generatedTargets);
		} catch (GeneratorException e) {
			genTaskResult.addGenerationError(target);
			ErrorReport errorReport = buildErrorReportForGeneratorException(e);
			manageError(errorReport); // throws InterruptedException if 'canceled'
		}

		//--- After normal end of generation : refresh the generated files and update count
		for ( Target generatedTarget : generatedTargets ) {
			logger.log(this, "generated target : " + generatedTarget.getFile() );

			String generatedFileAbsolutePath = generatedTarget.getOutputFileNameInFileSystem(telosysToolsCfg.getDestinationFolderAbsolutePath());
			
			//--- One more file : increment result count
			genTaskResult.incrementNumberOfFilesGenerated();

			logger.log(this, "Call afterFileGeneration(" + generatedFileAbsolutePath + ")...");
			afterFileGeneration(generatedTarget, generatedFileAbsolutePath); // Abstract method
		}
		
		//--- One TARGET done
		// Notifies that a given number of work unit of the main task has been completed. 
		// Note that this amount represents an installment, as opposed to a cumulative amount of work done to date.
		progressMonitor.worked(1); // One unit done (not cumulative)
		
	}
	
	//--------------------------------------------------------------------------------------------------
	/**
	 * Returns the name of the entity currently under generation 
	 * @return
	 */
	private String getCurrentEntityName() {
		if ( currentTarget == null ) return ENTITY_NONE ;
		String entityName = currentTarget.getEntityName() ;
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
		if ( currentTarget == null ) return NO_TEMPLATE ;
		return currentTarget.getTemplate() ;
	}
	
	//--------------------------------------------------------------------------------------------------
	/**
	 * Returns the result (number of files generated, ... )
	 * @return
	 */
	protected GenerationTaskResult getResult() {
		return genTaskResult != null ? genTaskResult : new GenerationTaskResult() ;
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
		genTaskResult.addError(errorReport);
		//--- Open the dialog box (the user can choose to continue or to cancel)
		boolean continueTask = onError(errorReport);
		//--- If 'cancel' : throw InterruptedException
		if ( ! continueTask ) {
			throw new InterruptedException("Generation task cancelled");
		}
	}
	//--------------------------------------------------------------------------------------------------
	/**
	 * Build a new ErrorReport from the given exception and add it in the TaskResult <br>
	 * NB : used by Eclipse Plugin in 'GenerationTaskWithProgress' 
	 * TODO : move in Eclipse Plugin ??
	 * @param exception
	 * @return
	 */
	protected ErrorReport buildErrorReport(InvocationTargetException exception ) {
		String entityName = this.getCurrentEntityName();
		String templateName = this.getCurrentTemplateName();	
		//ErrorReport errorReport = ErrorProcessor.buildErrorReport(exception, entityName, templateName); // v 3.0.0
		ErrorReport errorReport = new ErrorReport(exception, templateName, entityName); // v 3.3.0
		genTaskResult.addError(errorReport);
		return errorReport ;
	}
	//--------------------------------------------------------------------------------------------------
	private ErrorReport buildErrorReportForGeneratorException(GeneratorException generatorException ) {
		String entityName = this.getCurrentEntityName();
		String templateName = this.getCurrentTemplateName();	
		Throwable generatorExceptionCause = generatorException.getCause() ;
		if ( generatorExceptionCause != null ) {
			//return ErrorProcessor.buildErrorReport(generatorExceptionCause, entityName, templateName); // v 3.0.0
			return new ErrorReport(generatorExceptionCause, templateName, entityName); // v 3.3.0
		}
		else {
			//return ErrorProcessor.buildErrorReport(generatorException, entityName, templateName); // v 3.0.0
			return new ErrorReport(generatorException, templateName, entityName); // v 3.3.0
		}
	}
}
