package junit.env.telosys.tools.generator;

import java.io.File;

import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.generator.config.GeneratorConfig;
import org.telosys.tools.generator.context.EnvInContext;

public abstract class AbstractTest {

	private final static String REPO_FILENAME = "repos/DERBY-Tests-Jan-2014-10.dbrep" ; 

	protected File getDbRepositoryFile() {
		return TestsEnv.getTestFile(REPO_FILENAME);
	}
	
	/**
	 * Creates an instance of GeneratorConfig initialized with the 'telosys-tool.cfg' file
	 * @return
	 */
	protected GeneratorConfig getGeneratorConfig() {
		
		String projectLocation = TestsEnv.getTestRootFolder().getAbsolutePath(); 
		System.out.println(" . Project location = " + projectLocation );

		TelosysToolsCfg telosysToolsCfg = TestsEnv.loadTelosysToolsCfg(TestsEnv.getTestRootFolder());
		System.out.println(" . TelosysToolsCfg ready. File = " + telosysToolsCfg.getCfgFileAbsolutePath() );

		return new GeneratorConfig(projectLocation, telosysToolsCfg, "fake-bundle");
	}

	/**
	 * Creates a default instance of EnvInContext (without specific prefix/suffix)
	 * @return
	 */
	protected EnvInContext getEnvInContext() {
		EnvInContext env = new EnvInContext();
		// env.setEntityClassNamePrefix("");
		// env.setEntityClassNameSuffix(suffix);
		return env;
	}
}
