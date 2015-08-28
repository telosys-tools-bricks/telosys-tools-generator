package org.telosys.tools.generator.task;

import java.util.LinkedList;
import java.util.List;

import junit.env.telosys.tools.generator.TestsProject;

import org.telosys.tools.commons.ConsoleLogger;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.TelosysToolsLogger;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.generator.target.TargetDefinition;
import org.telosys.tools.generic.model.Model;

public class GenerationMain {
	
	public static void main(String[] args) throws TelosysToolsException, Exception {

		TelosysProject telosysProject = TestsProject.initProjectEnv("myproject", TestsProject.BUNDLE_NAME) ;
		
		//---------- Required files loading
		System.out.println("loading TelosysToolsCfg...");
		TelosysToolsCfg telosysToolsCfg = telosysProject.loadTelosysToolsCfg();
		
		System.out.println("loading model from 'dbrep' file : " + TestsProject.REPO_FILENAME );
		Model model = telosysProject.loadModelFromDbRep(TestsProject.REPO_FILENAME);

		//--- List of entities to be generated
		List<String> selectedEntities = new LinkedList<String>() ;
		selectedEntities.add("Author");
		
		List<TargetDefinition> selectedTargets = new LinkedList<TargetDefinition>();
		selectedTargets.add(new TargetDefinition("Entity Java Bean", "${BEANNAME}.java", "${SRC}/${ENTITY_PKG}", "java_bean.vm", ""));
		
		List<TargetDefinition> resourcesTargets = null;
		
		TelosysToolsLogger logger = new ConsoleLogger() ;
		
		GenerationTaskWithProgress generationTask = new GenerationTaskWithProgress(
				model, selectedEntities, 
				TestsProject.BUNDLE_NAME, selectedTargets, resourcesTargets, 
				telosysToolsCfg, logger);
		
		GenerationTaskResult generationTaskResult = generationTask.launch();
		System.out.println("Nb file(s) generated : " + generationTaskResult.getNumberOfFilesGenerated() );
	}
}
