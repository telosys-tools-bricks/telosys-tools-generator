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
import org.telosys.tools.commons.variables.Variable;
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
	
	//--------------------------------------------------------------------------------------
	// Methods implementation for super class 'AbstractGenerationTask'
	//--------------------------------------------------------------------------------------
	@Override  // Implementation for AbstractGenerationTask
	protected void showErrorMessage(String message, Throwable exception) {
		//MsgBox.error( message, exception );
		System.out.println("ERROR");
		System.out.println(" msg : " + message);
		System.out.println(" exc : " + exception.getMessage() );
	}

	@Override  // Implementation for AbstractGenerationTask
	protected void showErrorMessage(String message1, String message2) {
		//MsgBox.error( message1, message2 );
		System.out.println("ERROR");
		System.out.println(" msg1 : " + message1);
		System.out.println(" msg2 : " + message2 );
	}
	
	@Override  // Implementation for AbstractGenerationTask
	protected void afterFileGeneration(String fullFileName) {
		log("afterFileGeneration(" + fullFileName + ")");
		// Refresh the Eclipse Workspace 
		//EclipseWksUtil.refresh( new File(fullFileName) );	
	}

	@Override  // Implementation for AbstractGenerationTask
	public GenerationTaskResult launch() { 
		log("launch");
		
		//-----------------------------------------------------------------------------------
		// BULK GENERATION ENTRY POINT 
		// Creates a 'ProgressMonitor (Eclipse object)' and use it to run this task instance
		//-----------------------------------------------------------------------------------

//		GenerationTaskResult generationTaskResult = null ;
		//GenerationTaskWithProgress generationTaskWithProgress = this ;
		
		//--- Run the generation task via the progress monitor 
		//ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog( Util.getActiveWindowShell() ) ;
		try {
			log("Run generation task ..."  );
			//--- RUN THE ECLIPSE TASK ( 'this' task ) ....
			//progressMonitorDialog.run(false, false, this);  
			
			run(); // 
			log("End of generation task."  );
			
			GenerationTaskResult generationTaskResult = super.getResult() ;
//			MsgBox.info("Normal end of generation." 
//					+ "\n\n" + generationTaskResult.getNumberOfResourcesCopied() + " resources(s) copied."
//					+ "\n\n" + generationTaskResult.getNumberOfFilesGenerated() + " file(s) generated.");
			System.out.println(
					"Normal end of generation." 
					+ "\n\n" + generationTaskResult.getNumberOfResourcesCopied() + " resources(s) copied."
					+ "\n\n" + generationTaskResult.getNumberOfFilesGenerated() + " file(s) generated.");
			
		} catch (InvocationTargetException invocationTargetException) {
//			showGenerationError(invocationTargetException, 
//					generationTaskWithProgress.getCurrentTemplateName(), generationTaskWithProgress.getCurrentEntityName() ); // v 2.0.7
			super.showGenerationError(invocationTargetException);
		} catch (InterruptedException e) {
//			MsgBox.info("Generation interrupted");
		}
		
//    	return generationTaskResult;		
    	return super.getResult();		
	}
	
	//--------------------------------------------------------------------------------------
	// Methods implementation for Eclipse interface 'IRunnableWithProgress'
	//--------------------------------------------------------------------------------------
//	@Override
//	public void run(IProgressMonitor progressMonitor) throws InvocationTargetException,
//			InterruptedException {
	private void run() throws InvocationTargetException, InterruptedException {
		log("run");

		//---------------------------------------------------------------------------
		// BULK GENERATION STEPS ( called by the Eclipse 'ProgressMonitorDialog' )
		// It copies the required resources and generates the selected targets 
		// by calling the super class standard methods
		//---------------------------------------------------------------------------
		
		Variable[] projectVariables = super.getAllProjectVariables(); // call SUPER CLASS
		
		//--- 1) Copy the given resources (or do nothing if null)
//		OverwriteChooser overwriteChooser = new OverwriteChooserDialogBox() ; 
//		CopyHandler copyHandler = new CopyHandlerForRefresh() ;
//
//		int numberOfResourcesCopied = super.copyResourcesIfAny(overwriteChooser, copyHandler); // call SUPER CLASS
		int numberOfResourcesCopied = super.copyResourcesIfAny(null, null); // call SUPER CLASS

		//--- 2) Launch the generation
		ITaskMonitor defaultTaskMonitor = new DefaultTaskMonitor();
		int numberOfFilesGenerated = super.generateSelectedTargets(defaultTaskMonitor, projectVariables); // call SUPER CLASS
		
		//--- Task result
		super.setResult(numberOfResourcesCopied, numberOfFilesGenerated); // call SUPER CLASS
	}
}
