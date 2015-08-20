package junit.env.telosys.tools.generator;

import java.io.File;

import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generator.config.GeneratorConfig;
import org.telosys.tools.generator.config.GeneratorConfigManager;
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
	protected GeneratorConfig getGeneratorConfig(String bundleName) {
		
		String projectLocation = TestsEnv.getTestRootFolder().getAbsolutePath(); 
		System.out.println(" . Project location = " + projectLocation );

		TelosysToolsCfg telosysToolsCfg = TestsEnv.loadTelosysToolsCfg(TestsEnv.getTestRootFolder());
		System.out.println(" . TelosysToolsCfg ready. File = " + telosysToolsCfg.getCfgFileAbsolutePath() );

		// return new GeneratorConfig(projectLocation, telosysToolsCfg, "fake-bundle");
		GeneratorConfigManager generatorConfigManager = new GeneratorConfigManager(null);
		GeneratorConfig generatorConfig ;
    	try {
    		generatorConfig = generatorConfigManager.initFromDirectory( 
    				projectLocation, //projectConfig.getProjectFolder(), 
    				bundleName );
		} catch (GeneratorException e) {
        	//MsgBox.error("GenerationTask constructor : Cannot initialize the generator configuration", e);
        	throw new RuntimeException("Cannot initialize the generator configuration");
		}
    	return generatorConfig ;
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
