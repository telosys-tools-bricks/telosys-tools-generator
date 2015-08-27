package junit.env.telosys.tools.generator;

import java.io.File;

import junit.framework.TestCase;

import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generator.context.EnvInContext;
import org.telosys.tools.generator.task.TelosysProject;

public abstract class AbstractTest  extends TestCase  {

	private final static String REPO_FILENAME = "repos/DERBY-Tests-Jan-2014-10.dbrep" ; 

//	protected TelosysProject getTelosysProject(String projectName) {
//		String projectFolderAbsolutePath = TestsEnv.getTmpExistingFolderFullPath(projectName);
//		System.out.println("get project folder : " + projectFolderAbsolutePath );
//		return new TelosysProject(projectFolderAbsolutePath);
//	}
//	
//	protected void initDbRepFile(TelosysProject telosysProject) throws TelosysToolsException, Exception {
//		System.out.println("initializing 'dbrep' file... "  );
//		File dbrepInputFile = new File( TestsEnv.getTestFileAbsolutePath(REPO_FILENAME) ) ;
//		TelosysToolsCfg telosysToolsCfg = telosysProject.loadTelosysToolsCfg();
//		File destDir = new File(telosysToolsCfg.getRepositoriesFolderAbsolutePath());
//		System.out.println(" copy " + dbrepInputFile ); 
//		System.out.println("   to " + destDir ); 
//		FileUtil.copyToDirectory(dbrepInputFile, destDir, true);
//	}
	
	protected File getDbRepositoryFile() {
		return TestsEnv.getTestFile(REPO_FILENAME);
	}
	
//	/**
//	 * Creates an instance of GeneratorConfig initialized with the 'telosys-tool.cfg' file
//	 * @return
//	 */
//	protected GeneratorConfig getGeneratorConfig(String bundleName) {
//		
//		String projectLocation = TestsEnv.getTestRootFolder().getAbsolutePath(); 
//		System.out.println(" . Project location = " + projectLocation );
//
//		TelosysToolsCfg telosysToolsCfg = TestsEnv.loadTelosysToolsCfg(TestsEnv.getTestRootFolder());
//		System.out.println(" . TelosysToolsCfg ready. File = " + telosysToolsCfg.getCfgFileAbsolutePath() );
//
//		// return new GeneratorConfig(projectLocation, telosysToolsCfg, "fake-bundle");
//		GeneratorConfigManager generatorConfigManager = new GeneratorConfigManager(null);
//		GeneratorConfig generatorConfig ;
//    	try {
//    		generatorConfig = generatorConfigManager.initFromDirectory( 
//    				projectLocation, //projectConfig.getProjectFolder(), 
//    				bundleName );
//		} catch (GeneratorException e) {
//        	//MsgBox.error("GenerationTask constructor : Cannot initialize the generator configuration", e);
//        	throw new RuntimeException("Cannot initialize the generator configuration");
//		}
//    	return generatorConfig ;
//	}

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
