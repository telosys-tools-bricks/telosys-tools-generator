package org.telosys.tools.test.generatorrunner;

import org.junit.Test;
import org.telosys.tools.commons.TelosysToolsLogger;
import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generator.util.GeneratorRunner;
import org.telosys.tools.test.velocity.LoggerProvider;
import org.telosys.tools.tests.TestsEnv;

public class GeneratorRunnerTest {

	private final static String REPO_FILENAME = "repos/DERBY-Tests-Jan-2014-10.dbrep" ; 
	
	private final static String OUTPUT_FOLDER = "GENERATED_FILES" ; // output folder in the project location
	
	private GeneratorRunner getGeneratorRunner() throws GeneratorException {
		System.out.println("Creating GeneratorRunner...");
		TelosysToolsLogger logger = LoggerProvider.getLogger();
		String repositoryFileName = TestsEnv.getTestFileAbsolutePath(REPO_FILENAME);
		String projectLocation = TestsEnv.getTestRootFolder().getAbsolutePath(); 
		System.out.println(" . Repository file  = " + repositoryFileName );
		System.out.println(" . Project location = " + projectLocation );
		
		GeneratorRunner generatorRunner = new GeneratorRunner(repositoryFileName, projectLocation, logger);
		return generatorRunner ;	
	}
	
	@Test
	public void generateAuthor() throws GeneratorException {
		GeneratorRunner generatorRunner = getGeneratorRunner() ;
		generatorRunner.generateEntity("AUTHOR",    "Author.java",    OUTPUT_FOLDER, "jpa_bean_with_links.vm" );
	}

	@Test
	public void generateEmployee() throws GeneratorException {
		GeneratorRunner generatorRunner = getGeneratorRunner() ;
		generatorRunner.generateEntity("EMPLOYEE",  "Employee.java",  OUTPUT_FOLDER, "jpa_bean_with_links.vm" );
	}

}
