package org.telosys.tools.generator.task;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import junit.env.telosys.tools.generator.TestsProject;

import org.junit.Test;
import org.telosys.tools.commons.ConsoleLogger;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.TelosysToolsLogger;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.generator.target.TargetDefinition;
import org.telosys.tools.generic.model.Model;

public class GenerationTaskTest {

	private GenerationTaskResult launchGenerationTask(List<String> selectedEntities, 
			List<TargetDefinition> selectedTargets ) throws TelosysToolsException, Exception {
		
		TelosysProject telosysProject = TestsProject.initProjectEnv("myproject") ;
		
		//---------- Required files loading
		System.out.println("loading TelosysToolsCfg...");
		TelosysToolsCfg telosysToolsCfg = telosysProject.loadTelosysToolsCfg();
		
		System.out.println("loading model from 'dbrep' file : " + TestsProject.REPO_FILENAME );
		Model model = telosysProject.loadModelFromDbRep(TestsProject.REPO_FILENAME);

		List<TargetDefinition> resourcesTargets = null;
		
		TelosysToolsLogger logger = new ConsoleLogger() ;
		
		GenerationTaskWithProgress generationTask = new GenerationTaskWithProgress(
				model, selectedEntities, 
				TestsProject.BUNDLE_NAME, selectedTargets, resourcesTargets, 
				telosysToolsCfg, logger);
		
		GenerationTaskResult generationTaskResult = generationTask.run();
		System.out.println("Nb file(s) generated : " + generationTaskResult.getNumberOfFilesGenerated() );
		
		return generationTaskResult ;
	}
	
	@Test
	public void test1() throws TelosysToolsException, Exception {
		
		//--- List of entities to be generated
		List<String> selectedEntities = new LinkedList<String>() ;
		selectedEntities.add("Author");
		
		//--- List of targets
		List<TargetDefinition> selectedTargets = new LinkedList<TargetDefinition>();
		selectedTargets.add(new TargetDefinition("Entity Java Bean", "${BEANNAME}.java", "${SRC}/${ENTITY_PKG}", "java_bean.vm", ""));
		
		GenerationTaskResult generationTaskResult = launchGenerationTask(selectedEntities, selectedTargets);

		assertEquals(1, generationTaskResult.getNumberOfFilesGenerated());
		assertEquals(0, generationTaskResult.getNumberOfResourcesCopied());
	}

	@Test
	public void test2() throws TelosysToolsException, Exception {
		
		//--- List of entities to be generated
		List<String> selectedEntities = new LinkedList<String>() ;
		selectedEntities.add("Author");
		selectedEntities.add("Badge");
		
		//--- List of targets
		List<TargetDefinition> selectedTargets = new LinkedList<TargetDefinition>();
		selectedTargets.add(new TargetDefinition("Entity Java Bean", "${BEANNAME}.java", "${SRC}/${ENTITY_PKG}", "java_bean.vm", ""));
		
		GenerationTaskResult generationTaskResult = launchGenerationTask(selectedEntities, selectedTargets);

		assertEquals(2, generationTaskResult.getNumberOfFilesGenerated());
		assertEquals(0, generationTaskResult.getNumberOfResourcesCopied());
	}

}
