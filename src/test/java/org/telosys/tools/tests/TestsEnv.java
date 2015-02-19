package org.telosys.tools.tests;

import java.io.File;
import java.util.Properties;

import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.cfg.PropertiesManager;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.cfg.TelosysToolsCfgManager;

public class TestsEnv {

//	public final static String getTestsProxyPropertiesFile() {
//		//return TESTS_ROOT_FOLDER + "/proxy.properties";
//		return getExistingFileFullPath( "/proxy.properties" ) ;
//	}
	
//	public final static String getFullFileName(String fileName) {
//		return TESTS_ROOT_FOLDER + "/" + fileName ;
//	}
	
	private final static String SRC_TEST_RESOURCES = "src/test/resources/" ;

	/**
	 * Returns the absolute path for the given test file name without checking existence
	 * @param fileName
	 * @return
	 */
	public final static String buildAbsolutePath(String fileName) {
		File file = new File(SRC_TEST_RESOURCES + fileName);
		return file.getAbsolutePath();
	}
	
	public final static String getTestFileAbsolutePath(String fileName) {
		File file = getTestFile( fileName ); 
		return file.getAbsolutePath();
	}
	
	public final static File getTestFile(String fileName) {
		File file = new File(SRC_TEST_RESOURCES + fileName);
		if ( file.exists() ) {
			if ( file.isFile() ) {
				return file ;
			}
			else {
				throw new RuntimeException("Test resource file '"+ file.getName() + "' is not a file");
			}
		}
		else {
			throw new RuntimeException("Test resource file '"+ file.getName() + "' not found");
		}
	}

	public final static File getTestRootFolder() {
		File folder = new File(SRC_TEST_RESOURCES);
		checkFolder(folder);
		return folder ;
	}

	public final static File getTestFolder(String folderName) {
		File folder = new File(SRC_TEST_RESOURCES + folderName);
		checkFolder(folder);
		return folder ;
//		if ( folder.exists() ) {
//			if ( folder.isDirectory() ) {
//				return folder ;
//			}
//			else {
//				throw new RuntimeException("Test resource '"+ folder.getName() + "' is not a folder");
//			}
//		}
//		else {
//			throw new RuntimeException("Test resource '"+ folder.getName() + "' not found");
//		}
	}

	private final static void checkFolder(File folder) {
		if ( folder.exists() ) {
			if ( folder.isDirectory() ) {
				return ;
			}
			else {
				throw new RuntimeException("Folder '"+ folder.getName() + "' is not a folder");
			}
		}
		else {
			throw new RuntimeException("Folder '"+ folder.getName() + "' doesn't exist");
		}
	}
	//-----------------------------------------------------------------------------------------
	// TEMPORARY FILES AND FOLDERS
	//-----------------------------------------------------------------------------------------
	private final static String TARGET_TESTS_TMP_DIR = "target/tests-tmp/" ;
	
	//private final static String TESTS_ROOT_FOLDER = "D:/tmp/telosys-tools-tests" ;
	
	public final static String getTmpRootFolder() {
		//return TESTS_ROOT_FOLDER ;
		return getTmpExistingFolderFullPath( "" ) ;
	}
	
	public final static String getTmpBundlesFolder() {
		//return TESTS_ROOT_FOLDER + "/TelosysTools/templates";
		return getTmpExistingFolderFullPath( "/TelosysTools/templates" ) ;
	}
	
	public final static String getTmpDownloadFolder() {
		//return TESTS_ROOT_FOLDER + "/TelosysTools/downloads";
		return getTmpExistingFolderFullPath( "/TelosysTools/downloads" ) ;
	}
	
	public static String getTmpFileFullPath(String fileName ) {
		File file = getTmpFile(fileName) ;
		return file.getAbsolutePath();
	}
	
	public static String getTmpFileOrFolderFullPath(String fileName ) {
		File file = getTmpFileOrFolder(fileName) ;
		return file.getAbsolutePath();
	}
	
	public static String getTmpExistingFileFullPath(String fileName ) {
		File file = getTmpExistingFile( fileName ); 
		return file.getAbsolutePath();
	}
	
	public static String getTmpExistingFolderFullPath(String folderName ) {
		File file = getTmpExistingFolder( folderName ); 
		return file.getAbsolutePath();
	}
	
	/**
	 * Return an existing temporary folder (the folder is created if necessary)
	 * @param folderName
	 * @return
	 */
	public static File getTmpExistingFolder(String folderName ) {
		File folder = new File(TARGET_TESTS_TMP_DIR + folderName);
		if ( folder.exists() ) {
			if ( folder.isDirectory() ) {
				return folder ;
			}
			else {
				throw new RuntimeException("'"+ folder.getName() + "' is not a folder");
			}
		}
		else {
			if ( folder.mkdirs() ) {
				return folder ;
			}
			else {
				throw new RuntimeException("Cannot create folder '"+ folder.getName() + "' ");
			}
		}
	}
	
	public static File getTmpExistingFile(String fileName ) {
		File file = new File(TARGET_TESTS_TMP_DIR + fileName);
		if ( file.exists() ) {
			if ( file.isFile() ) {
				return file ;
			}
			else {
				throw new RuntimeException("'"+ file.getName() + "' exists and is not a file");
			}
		}
		else {
			throw new RuntimeException("'"+ file.getName() + "' not found");
		}
	}
	
	public static File getTmpFile(String fileName ) {
		File file = new File(TARGET_TESTS_TMP_DIR + fileName);
		if ( file.exists() && ! file.isFile()) {
				throw new RuntimeException("'"+ file.getName() + "' exists and is not a file");
		}
		return file ;
	}
	
	public static File getTmpFileOrFolder(String fileName ) {
		File file = new File(TARGET_TESTS_TMP_DIR + fileName);
		return file ;
	}

	public static File createTmpProjectFolders(String projectName ) {
		System.out.println("Creating project folders ( project = '" + projectName + "' ) ...");
		File projectFolder = TestsEnv.getTmpExistingFolder(projectName);
		checkFolder ( projectFolder ) ;
		checkFolder ( TestsEnv.getTmpExistingFolder(projectName + "/TelosysTools/downloads") ) ;
		checkFolder ( TestsEnv.getTmpExistingFolder(projectName + "/TelosysTools/templates") ) ;
		return projectFolder;
	}
	
	/**
	 * Returns the HTTP proxy properties defined in the file 'proxy.properties' <br>
	 * 
	 * @return
	 */
	public final static Properties loadSpecificProxyProperties()  {
		
		//File file = new File("src/test/resources/proxy.properties");
		File file = getTestFile("proxy.properties");
		System.out.println("Loading http properties from " + file.getAbsolutePath() +" ...");
		//PropertiesManager pm = new PropertiesManager(TestsFolders.getTestsProxyPropertiesFile());
		PropertiesManager pm = new PropertiesManager(file.getAbsolutePath());
		Properties proxyProperties;
		try {
			proxyProperties = pm.load();
		} catch (TelosysToolsException e) {
			throw new RuntimeException("ERROR : Cannot load proxy properties", e);
		}
		if ( proxyProperties == null ) {
			throw new RuntimeException("ERROR : No proxy properties");
		}
		return proxyProperties ;
	}
	
//	/**
//	 * Returns the 'telosys-tools.cfg' full file name 
//	 * @return
//	 */
//	public final static String getTelosysToolsCfgFileName()  {
//		return getTmpRootFolder() + "/telosys-tools.cfg" ;
//	}
	
	/**
	 * Returns the 'telosys-tools.cfg' file 
	 * @return
	 */
	public final static File getTelosysToolsCfgFile()  {
//		String fileName = getTelosysToolsCfgFileName();
//		System.out.println("telosys-tool.cfg file : '" + fileName  + "'");
//		File file = new File( fileName ) ;
		File file = getTestFile("/telosys-tools.cfg");
		System.out.println("TelosysToolsCfgFile = '" + file.getAbsolutePath() + "' ");
		if ( file.exists() != true ) {
			throw new RuntimeException("TelosysToolsCfgFile : '" + file.getAbsolutePath() + "' doesn't exist !");
		}
		return file ;
	}
	
	/**
	 * Returns the properties loaded from the 'telosys-tools.cfg' file 
	 * @return
	 */
	public final static Properties loadTelosysToolsCfgProperties()  {
		
		PropertiesManager pm = new PropertiesManager( getTelosysToolsCfgFile() );
		Properties properties;
		try {
			properties = pm.load();
		} catch (TelosysToolsException e) {
			throw new RuntimeException("ERROR : Cannot load 'telosys-tools.cfg' properties", e);
		}
		if ( properties == null ) {
			throw new RuntimeException("ERROR : No 'telosys-tools.cfg' properties");
		}
		return properties ;
	}
	
	/**
	 * Returns the TelosysToolsCfg instance loaded from the 'telosys-tools.cfg' using 'TelosysToolsCfgManager' 
	 * @return
	 */
	public final static TelosysToolsCfg loadTelosysToolsCfg(File projectFolder)  {
		TelosysToolsCfg telosysToolsCfg = null ;
		System.out.println("Loading configuration from folder : " + projectFolder.getAbsolutePath() );
		TelosysToolsCfgManager cfgManager = new TelosysToolsCfgManager( projectFolder.getAbsolutePath() );
		try {
			telosysToolsCfg = cfgManager.loadProjectConfig();
		} catch (TelosysToolsException e) {
			e.printStackTrace();
			throw new RuntimeException("Cannot load 'TelosysToolsCfg'", e);
		}
		return telosysToolsCfg ;
	}
	
}
