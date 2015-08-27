package org.telosys.tools.generator.api;

import junit.env.telosys.tools.generator.AbstractTest;
import junit.env.telosys.tools.generator.LoggerProvider;
import junit.env.telosys.tools.generator.TestsProject;

import org.junit.Test;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.TelosysToolsLogger;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generator.task.TelosysProject;
import org.telosys.tools.generic.model.Model;

public class GeneratorRunnerTest extends AbstractTest {

//	private final static String REPO_FILENAME = "DERBY-Tests-Jan-2014-10.dbrep" ; 
	private final static String OUTPUT_FOLDER = "GENERATED_FILES" ; // output folder in the project location
//	private final static String BUNDLE_NAME   = "basic-templates-TT210" ; 

	
	private GeneratorRunner getGeneratorRunner() throws TelosysToolsException,  Exception {
		System.out.println("Creating GeneratorRunner...");
		
		TelosysProject telosysProject = TestsProject.initProjectEnv("myproject") ;

		TelosysToolsLogger logger = LoggerProvider.getLogger();
		
//		System.out.println("Getting TelosysProject...");
//		TelosysProject telosysProject = TestsProject.getTelosysProject("myproject") ;
//		
//		System.out.println("initializing project...");
//		String initInfo = telosysProject.initProject();
//		System.out.println(initInfo);
//		
		System.out.println("loading TelosysToolsCfg...");
		TelosysToolsCfg telosysToolsCfg = telosysProject.loadTelosysToolsCfg();
		//GeneratorRunner generatorRunner = new GeneratorRunner(repositoryFileName, projectLocation, logger);
		
//		System.out.println("initializing 'dbrep' file : " + REPO_FILENAME );
//		TestsProject.initDbRepFile(telosysProject, REPO_FILENAME);
		
		System.out.println("loading model from 'dbrep' file : " + TestsProject.REPO_FILENAME );
		Model model = telosysProject.loadModelFromDbRep(TestsProject.REPO_FILENAME);
		//GeneratorConfig generatorConfig = getGeneratorConfig("fake-bundle") ;
		GeneratorRunner generatorRunner = new GeneratorRunner(model, telosysToolsCfg, TestsProject.BUNDLE_NAME, logger);
		return generatorRunner ;	
	}
	
	@Test
	public void testGenerateAuthor() throws TelosysToolsException, GeneratorException, Exception {
		GeneratorRunner generatorRunner = getGeneratorRunner() ;
		generatorRunner.generateEntity("Author",    "Author.java",    OUTPUT_FOLDER, "java_bean.vm" );
		//assertEquals("A", "A");
	}

	@Test
	public void testGenerateBadge() throws TelosysToolsException, GeneratorException, Exception {
		GeneratorRunner generatorRunner = getGeneratorRunner() ;
		generatorRunner.generateEntity("Badge",    "Badge.java",    OUTPUT_FOLDER, "java_bean.vm" );
	}

}
