package org.telosys.tools.generator.api;


import junit.env.telosys.tools.generator.LoggerProvider;
import junit.env.telosys.tools.generator.TestsEnv;
import junit.env.telosys.tools.generator.fakemodel.FakeModelProvider;

import org.junit.Test;
import org.telosys.tools.commons.TelosysToolsLogger;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generator.api.GeneratorRunner;
import org.telosys.tools.generator.config.GeneratorConfig;
import org.telosys.tools.generic.model.Model;

public class GeneratorRunnerTest {

	private final static String REPO_FILENAME = "repos/DERBY-Tests-Jan-2014-10.dbrep" ; 
	
	private final static String OUTPUT_FOLDER = "GENERATED_FILES" ; // output folder in the project location
	
	private GeneratorConfig getGeneratorConfig() {
		
		String projectLocation = TestsEnv.getTestRootFolder().getAbsolutePath(); 
		System.out.println(" . Project location = " + projectLocation );

		TelosysToolsCfg telosysToolsCfg = TestsEnv.loadTelosysToolsCfg(TestsEnv.getTestRootFolder());
		System.out.println(" . TelosysToolsCfg ready. File = " + telosysToolsCfg.getCfgFileAbsolutePath() );

		return new GeneratorConfig(projectLocation, telosysToolsCfg, "fake-bundle");
	}
	
	private Model getModel()  {
		
		String repositoryFileName = TestsEnv.getTestFileAbsolutePath(REPO_FILENAME);
		System.out.println(" . Repository file  = " + repositoryFileName );
		Model model = FakeModelProvider.buildModel() ;
		return model ;
	}
	
	private GeneratorRunner getGeneratorRunner() throws GeneratorException {
		System.out.println("Creating GeneratorRunner...");
		TelosysToolsLogger logger = LoggerProvider.getLogger();
		//String repositoryFileName = TestsEnv.getTestFileAbsolutePath(REPO_FILENAME);
		//String projectLocation = TestsEnv.getTestRootFolder().getAbsolutePath(); 
		//System.out.println(" . Repository file  = " + repositoryFileName );
		//System.out.println(" . Project location = " + projectLocation );
		
		//GeneratorRunner generatorRunner = new GeneratorRunner(repositoryFileName, projectLocation, logger);
		Model model = getModel() ;
		GeneratorConfig generatorConfig = getGeneratorConfig() ;
		GeneratorRunner generatorRunner = new GeneratorRunner(model, generatorConfig, logger);
		return generatorRunner ;	
	}
	
	@Test
	public void generateAuthor() throws GeneratorException {
		GeneratorRunner generatorRunner = getGeneratorRunner() ;
		//generatorRunner.generateEntity("AUTHOR",    "Author.java",    OUTPUT_FOLDER, "jpa_bean_with_links.vm" );
		generatorRunner.generateEntity("Author",    "Author.java",    OUTPUT_FOLDER, "jpa_bean_with_links.vm" );
	}

	@Test
	public void generateEmployee() throws GeneratorException {
		GeneratorRunner generatorRunner = getGeneratorRunner() ;
		//generatorRunner.generateEntity("EMPLOYEE",  "Employee.java",  OUTPUT_FOLDER, "jpa_bean_with_links.vm" );
		generatorRunner.generateEntity("Employee",  "Employee.java",  OUTPUT_FOLDER, "jpa_bean_with_links.vm" );
	}

}
