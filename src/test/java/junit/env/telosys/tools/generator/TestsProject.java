package junit.env.telosys.tools.generator;

import java.io.File;

import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.generator.task.TelosysProject;

public class TestsProject {
	
	public final static String REPO_FILENAME = "DERBY-Tests-Jan-2014-10.dbrep" ; 
	public final static String BUNDLE_NAME   = "basic-templates-TT210" ; 


	public static TelosysProject initProjectEnv(String projectName, String bundleName) throws TelosysToolsException, Exception {
		
		System.out.println("Getting TelosysProject, project name = '" + projectName + "'");
		TelosysProject telosysProject = getTelosysProject(projectName) ;
		
		//---------- Initialization 
		System.out.println("initializing project...");
		String initInfo = telosysProject.initProject();
		System.out.println(initInfo);
		
		//--- Initialize project with 'dbrep' files copied from "src/test/resources" 
		System.out.println("initializing 'dbrep' file : " + REPO_FILENAME );
		initDbRepFile(telosysProject, REPO_FILENAME);
		
		//--- Initialize project with templates copied from "src/test/resources" 
		System.out.println("initializing templates for bundle : " + bundleName );
		initBundle(telosysProject, bundleName);
		
		return telosysProject ;
	}
	
	public static TelosysProject getTelosysProject(String projectName) {
		String projectFolderAbsolutePath = TestsEnv.getTmpExistingFolderFullPath(projectName);
		System.out.println("get project folder : " + projectFolderAbsolutePath );
		return new TelosysProject(projectFolderAbsolutePath);
	}
	
	public static void initDbRepFile(TelosysProject telosysProject, String dbrepFileName) throws TelosysToolsException, Exception {
		System.out.println("initializing 'dbrep' in project from file '" + dbrepFileName +"'" );
		// original file is stored in "src/test/resources/"
		String originalFileAbsolutePath = TestsEnv.buildAbsolutePath(FileUtil.buildFilePath("repos", dbrepFileName ));
		File dbrepInputFile = new File( originalFileAbsolutePath ) ;
		// destination folder
		TelosysToolsCfg telosysToolsCfg = telosysProject.loadTelosysToolsCfg();
		File destDir = new File(telosysToolsCfg.getModelsFolderAbsolutePath());
		
		System.out.println(" copy " + dbrepInputFile ); 
		System.out.println("   to " + destDir ); 
		FileUtil.copyToDirectory(dbrepInputFile, destDir, true);
	}

	public static void initBundle(TelosysProject telosysProject, String bundleName) throws TelosysToolsException, Exception {
		System.out.println("initializing bundle in project templates '" + bundleName +"'" );
		// original file is stored in "src/test/resources/"
		String originalFolderAbsolutePath = TestsEnv.buildAbsolutePath(FileUtil.buildFilePath("templates", bundleName ));
		File sourceFolder = new File( originalFolderAbsolutePath ) ;
		// destination folder
		TelosysToolsCfg telosysToolsCfg = telosysProject.loadTelosysToolsCfg();
		File destFolder = new File(telosysToolsCfg.getTemplatesFolderAbsolutePath(bundleName));
		
		
		//--- Copy 
		System.out.println(" from " + sourceFolder ); 
		System.out.println("   to " + destFolder ); 
		FileUtil.copyFolder(sourceFolder, destFolder, true);
	}

}
