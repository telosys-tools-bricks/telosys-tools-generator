package junit.env.telosys.tools.generator;

import java.io.File;
import java.util.Properties;

import org.telosys.tools.commons.PropertiesManager;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.cfg.TelosysToolsCfgManager;

public class TestsEnv {

	private static final String SRC_TEST_RESOURCES = "src/test/resources/" ;

	private TestsEnv() {
	}
	
	private static void log(String msg) {
		// No log
	}
	
	/**
	 * Creates a File instance for the given name in 'src/test/resources' without checking existence
	 * @param fileName
	 * @return
	 */
	public static File buildTestFileOrFolder(String fileName ) {
		return new File(SRC_TEST_RESOURCES + fileName);
	}

	/**
	 * Returns the absolute path for the given test file name without checking existence
	 * @param fileName
	 * @return
	 */
	public static final String buildAbsolutePath(String fileName) {
		File file = buildTestFileOrFolder(fileName);
		return file.getAbsolutePath();
	}
	
	public static final String getTestFileAbsolutePath(String fileName) {
		File file = getTestFile( fileName ); 
		return file.getAbsolutePath();
	}
	
	public static final File getTestFile(String fileName) {
		File file = buildTestFileOrFolder(fileName);
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

	public static final File getTestRootFolder() {
		File folder = new File(SRC_TEST_RESOURCES);
		checkFolder(folder);
		return folder ;
	}

	public static final File getTestFolder(String folderName) {
		File folder = new File(SRC_TEST_RESOURCES + folderName);
		checkFolder(folder);
		return folder ;
	}

	private static final void checkFolder(File folder) {
		if ( folder.exists() ) {
			if ( ! folder.isDirectory() ) {
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
	private static final String TARGET_TESTS_TMP_DIR = "target/tests-tmp/" ;
	
	public static final String getTmpRootFolder() {
		return getTmpExistingFolderFullPath( "" ) ;
	}
	
	public static final String getTmpBundlesFolder() {
		return getTmpExistingFolderFullPath( "/TelosysTools/templates" ) ;
	}
	
	public static final String getTmpDownloadFolder() {
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
		return new File(TARGET_TESTS_TMP_DIR + fileName);
	}

	public static File createTmpProjectFolders(String projectName ) {
		log("Creating project folders ( project = '" + projectName + "' ) ...");
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
	public static final Properties loadSpecificProxyProperties()  {
		
		File file = getTestFile("proxy.properties");
		log("Loading http properties from " + file.getAbsolutePath() +" ...");
		PropertiesManager pm = new PropertiesManager(file.getAbsolutePath());
		Properties proxyProperties;
		try {
			proxyProperties = pm.load();
		} catch (Exception e) {
			throw new RuntimeException("ERROR : Cannot load proxy properties", e);
		}
		if ( proxyProperties == null ) {
			throw new RuntimeException("ERROR : No proxy properties");
		}
		return proxyProperties ;
	}
	
	/**
	 * Returns the 'telosys-tools.cfg' file 
	 * @return
	 */
	public static final File getTelosysToolsCfgFile()  {
		File file = getTestFile("/telosys-tools.cfg");
		log("TelosysToolsCfgFile = '" + file.getAbsolutePath() + "' ");
		if ( file.exists() != true ) {
			throw new RuntimeException("TelosysToolsCfgFile : '" + file.getAbsolutePath() + "' doesn't exist !");
		}
		return file ;
	}
	
	/**
	 * Returns the properties loaded from the 'telosys-tools.cfg' file 
	 * @return
	 */
	public static final Properties loadTelosysToolsCfgProperties()  {
		
		PropertiesManager pm = new PropertiesManager( getTelosysToolsCfgFile() );
		Properties properties;
		try {
			properties = pm.load();
		} catch (Exception e) {
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
	public static final TelosysToolsCfg loadTelosysToolsCfg(File projectFolder)  {
		TelosysToolsCfg telosysToolsCfg = null ;
		log("Loading configuration from folder : " + projectFolder.getAbsolutePath() );
		TelosysToolsCfgManager cfgManager = new TelosysToolsCfgManager( projectFolder.getAbsolutePath() );
		try {
			telosysToolsCfg = cfgManager.loadTelosysToolsCfg();
		} catch (TelosysToolsException e) {
			e.printStackTrace();
			throw new RuntimeException("Cannot load 'TelosysToolsCfg'", e);
		}
		return telosysToolsCfg ;
	}
	
}
