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
import java.util.List;

import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.TelosysToolsLogger;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.generator.context.Target;
import org.telosys.tools.generator.target.TargetDefinition;
import org.telosys.tools.generic.model.Model;


/**
 * Runnable task with a progress bar (Eclipse like)
 * for code generation 
 *  
 * @author Laurent Guerin
 *
 */
public class StandardGenerationTask extends AbstractGenerationTask implements GenerationTask // implements IRunnableWithProgress 
{
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
	public StandardGenerationTask(
			Model                  model,
			List<String>           selectedEntities,
			String                 bundleName, // v 3.0.0
			List<TargetDefinition> selectedTargets,
			List<TargetDefinition> resourcesTargets,
			// GeneratorConfig generatorConfig, 
			TelosysToolsCfg        telosysToolsCfg, // v 3.0.0			
			TelosysToolsLogger     logger)
			throws TelosysToolsException 
	{
		// Just call the super class constructor
		//super(repositoryModel, selectedEntities, selectedTargets, resourcesTargets,	generatorConfig, logger);
		super(model, selectedEntities, bundleName, selectedTargets, resourcesTargets, telosysToolsCfg, logger); // v 3.0.0
	}
	
//	/**
//	 * Eclipse like 'MsgBox.error' 
//	 * @param messageTitle
//	 * @param messageBody
//	 * @param exception
//	 */
//	private void msgBoxError(String messageTitle, String messageBody, Throwable exception) {
//		System.out.println("ERROR");
//		System.out.println(" title   : " + messageTitle );
//		System.out.println(" message : " + messageBody );
//		if ( exception != null ) {
//			System.out.println(" exception : " + exception.getMessage() );
//		}
//	}

	/**
	 * Eclipse like 'MsgBox.error' 
	 * @param message
	 * @param exception
	 */
	private void msgBoxError(String message, Throwable exception) {
		System.out.println("ERROR");
		System.out.println(" message : " + message );
		if ( exception != null ) {
			System.out.println(" exception : " + exception.getMessage() );
		}
	}

	/**
	 * Eclipse like 'MsgBox.info' 
	 * @param message
	 */
	private void msgBoxInfo(String message) {
		System.out.println("INFORMATION");
		System.out.println(" message : " + message );
	}
	
	//--------------------------------------------------------------------------------------
	// Methods implementation for super class 'AbstractGenerationTask'
	//--------------------------------------------------------------------------------------

	@Override  // Implementation for AbstractGenerationTask
	protected boolean onError(ErrorReport errorReport) {
//		msgBoxError(errorReport.getMessageTitle(), errorReport.getMessageBody(), errorReport.getException() );
		msgBoxError(errorReport.getMessage(), errorReport.getException() );
		return false ; // continue the task
	}
	
	@Override  // Implementation for AbstractGenerationTask
	protected void afterFileGeneration(Target target, String fullFileName) {
		log("afterFileGeneration : " + target.getTemplate() + " --> " + fullFileName + "");
		// Refresh the Eclipse Workspace 
		//EclipseWksUtil.refresh( new File(fullFileName) );	
	}
	
	@Override  // Implementation for GenerationTask
	public GenerationTaskResult launch() { 
		log("launch");
		
		//-----------------------------------------------------------------------------------
		// BULK GENERATION ENTRY POINT 
		// Creates a 'ProgressMonitor (Eclipse object)' and use it to run this task instance
		//-----------------------------------------------------------------------------------
		
		//--- Run the generation task via the progress monitor 
		//ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog( Util.getActiveWindowShell() ) ;
		try {
			log("Run generation task ..."  );
			
			//--- RUN THE TASK ( 'this' task ) 
			// the 'run' method must be conformed to Eclipse 'IRunnableWithProgress' implementation			
			run(); // 
			// NB :
			// All the exceptions are wrapped in a 'InvocationTargetException'
			
			log("End of generation task."  );
			
			GenerationTaskResult generationTaskResult = super.getResult() ;
			
			msgBoxInfo("END OF GENERATION" 
					+ "\n\n" + generationTaskResult.getNumberOfResourcesCopied() + " resources(s) copied."
					+ "\n\n" + generationTaskResult.getNumberOfFilesGenerated() + " file(s) generated."
					+ "\n\n" + generationTaskResult.getNumberOfGenerationErrors() + " generation error(s).");
			
		} catch (InvocationTargetException invocationTargetException) {
			ErrorReport errorReport = buildErrorReport(invocationTargetException);
			onError( errorReport ) ;
			
		} catch (InterruptedException interruptedException) {
			GenerationTaskResult generationTaskResult = super.getResult() ;
			msgBoxInfo("GENERATION CANCELED" 
					+ "\n\n" + generationTaskResult.getNumberOfResourcesCopied() + " resources(s) copied."
					+ "\n\n" + generationTaskResult.getNumberOfFilesGenerated() + " file(s) generated."
					+ "\n\n" + generationTaskResult.getNumberOfGenerationErrors() + " generation error(s).");
		}
		
    	return super.getResult();		
	}
	
	//--------------------------------------------------------------------------------------
	// Methods implementation for Eclipse interface 'IRunnableWithProgress'
	//--------------------------------------------------------------------------------------
//	@Override
	private void run() throws InvocationTargetException, // just for Eclipse like behavior
							InterruptedException {
		log("run");

		//---------------------------------------------------------------------------
		// BULK GENERATION STEPS ( called by the Eclipse 'ProgressMonitorDialog' )
		// It copies the required resources and generates the selected targets 
		// by calling the super class standard methods
		//---------------------------------------------------------------------------

		ITaskMonitor defaultTaskMonitor = new DefaultTaskMonitor();
		super.runTask(defaultTaskMonitor, null, null);
	}
}
