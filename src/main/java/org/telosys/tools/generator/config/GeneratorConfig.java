/**
 *  Copyright (C) 2008-2013  Telosys project org. ( http://www.telosys.org/ )
 *
 *  Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.gnu.org/licenses/lgpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.telosys.tools.generator.config;

import java.io.File;

import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.dbcfg.DatabasesConfigurations;
import org.telosys.tools.commons.dbcfg.DbConfigManager;

/**
 * Generator configuration implementation 
 * 
 * @author Laurent GUERIN
 *
 */
public class GeneratorConfig //implements IGeneratorConfig
{
//	public static final String PROJECT_CONFIG_FILE = "telosys-tools.cfg" ;
	
	private final String     _sProjectLocation ; // Project folder ( absolute path )
	
//	private final String     _sTemplatesFolder ;
//	
//	private final String     _sEntityClassPackage ;

	private final String     _sBundleName ;

//	private final Properties           _projectProperties ;  // v 2.0.7
//	private final ProjectConfiguration _projectConfiguration ;  // v 2.0.7
	private final TelosysToolsCfg      _telosysToolsCfg ; // v 2.1.0

//	/**
//	 * @param sProjectLocation project location (project folder)
//	 * @param prop project configuration (project properties)
//	 * @param bundleName current bundle name if any (or null if none)
//	 */
//	public GeneratorConfig(String sProjectLocation, Properties prop, String bundleName) 
//	{
//		_sProjectLocation = sProjectLocation;
//		
//		//--- Files folders
//    	//_sTemplatesFolder     = prop.getProperty(GeneratorConfigConst.TEMPLATES_FOLDER, null);
//    	_sTemplatesFolder     = prop.getProperty(TelosysToolsCfg.TEMPLATES_FOLDER, null); // v 2.1.0
//		
//		//--- Packages names
//    	//_sEntityClassPackage  = prop.getProperty(GeneratorConfigConst.ENTITIES_PACKAGE, null);
//    	_sEntityClassPackage  = prop.getProperty(VariablesNames.ENTITY_PKG, null); // v 2.0.6
//    	
//    	//--- Bundle name to use (can be null or void)
//    	_sBundleName = bundleName ; // v 2.0.7
//    	
//    	//_projectVariables = VariablesUtil.getAllVariablesFromProperties(prop);  // v 2.0.7
//    	_projectProperties = prop ; // v 2.0.7
//    	
//    	_projectConfiguration = new ProjectConfiguration( 
//				getTemplatesFolderFullPath(),
//				_sEntityClassPackage, 
//				_projectProperties ); // v 2.0.7
//	}

	public GeneratorConfig(String sProjectLocation, TelosysToolsCfg telosysToolsCfg, String bundleName) 
	{
		_sProjectLocation = sProjectLocation;
		_telosysToolsCfg = telosysToolsCfg ;
		
//		//--- Files folders
//    	//_sTemplatesFolder     = prop.getProperty(GeneratorConfigConst.TEMPLATES_FOLDER, null);
//    	_sTemplatesFolder     = prop.getProperty(TelosysToolsCfg.TEMPLATES_FOLDER, null); // v 2.1.0
//		
//		//--- Packages names
//    	//_sEntityClassPackage  = prop.getProperty(GeneratorConfigConst.ENTITIES_PACKAGE, null);
//    	_sEntityClassPackage  = prop.getProperty(VariablesNames.ENTITY_PKG, null); // v 2.0.6
    	
    	//--- Bundle name to use (can be null or void)
    	_sBundleName = bundleName ; // v 2.0.7
    	
//    	//_projectVariables = VariablesUtil.getAllVariablesFromProperties(prop);  // v 2.0.7
//    	_projectProperties = prop ; // v 2.0.7
    	
//    	_projectConfiguration = new ProjectConfiguration( 
//				getTemplatesFolderFullPath(),
//				_sEntityClassPackage, 
//				_projectProperties ); // v 2.0.7
	}

//	//---------------------------------------------------------------------
//	// Specific variables
//	//---------------------------------------------------------------------
//	public Variable[] getProjectVariables() 
//	{
//		return _projectVariables ;
//	}
	
	/**
	 * Returns the bundle name for the current generation
	 * @return
	 * @since 2.0.7
	 */
	public String getBundleName() {
		return _sBundleName ;
	}
	
//	//---------------------------------------------------------------------
//	// Project Configuration for Generator context
//	//---------------------------------------------------------------------
//	/**
//	 * Returns the project configuration to be set in the generator's context 
//	 * @return
//	 */
//	public ProjectConfiguration getProjectConfiguration() 
//	{
////		ProjectConfiguration projectConfiguration = new ProjectConfiguration( 
////				//_sSourceFolder, _sWebContentFolder, 
////				getTemplatesFolderFullPath(),
////				_sEntityClassPackage, 
////				_projectVariables );
////		
////		return projectConfiguration;
//		
//		return _projectConfiguration ;
//	}

	//---------------------------------------------------------------------
	// Folders
	//---------------------------------------------------------------------
    /**
     * Returns the full path directory where the current project is located
     * ( e.g. : "X:/dir/workspace/myproject" ) <br>
     * @return
     */
    public String getProjectLocation()
	{
    	return _sProjectLocation ;
	}
    
    //------------------------------------------------------------------------------------------------------
    public TelosysToolsCfg getTelosysToolsCfg()
	{
    	return _telosysToolsCfg ;
	}

//    /**
//     * Returns the "Project Full Path" by adding the given subpath at the end of the project location
//     * @param sSubPath the "sub path" to add
//     * @return
//     */
//    private String getProjectFullPath(String sSubPath)
//	{
//    	if ( sSubPath != null )
//    	{
//    		String s = sSubPath.trim() ;
//    		if ( s.startsWith("/") || s.startsWith("\\") )
//    		{
//    	    	return _sProjectLocation + s ;
//    		}
//    		else
//    		{
//    	    	return _sProjectLocation + "/" + s ;
//    		}
//    	}
//    	else
//    	{
//    		return _sProjectLocation ;
//    	}
//	}
    
	
	/**
     * Returns the "full path" templates folder defined in the project properties <br>
     * with the bundle name if any (returns null if templates folder is not defined).<br>
     * ( e.g. : "X:/dir/workspace/myproject/TelosysTools/templates" ) <br>
     * If a current bundle is defined it is added at the end of the path <br>
     * ( e.g. : "X:/dir/workspace/myproject/TelosysTools/templates/mybundle" )
	 * @return
	 */
    public String getTemplatesFolderFullPath()
	{
    	String templateFolderFullPath = _telosysToolsCfg.getTemplatesFolderAbsolutePath() ;
    	if ( StrUtil.nullOrVoid(templateFolderFullPath) ) return null ;

    	String templateFolderFullPathTrim = templateFolderFullPath.trim() ;
    			// Templates folder is set => build the full path
    			//return getProjectFullPath(_sTemplatesFolder) ;
		// new in v 2.0.7
		//String templateFolderFullPath = getProjectFullPath(_sTemplatesFolder) ;
		if ( StrUtil.nullOrVoid(_sBundleName) == false ) {
			// There's a bundle defined => use it
			return FileUtil.buildFilePath(templateFolderFullPathTrim, _sBundleName.trim() );
		}
		else {
			// No current bundle => use the standard templates folder as is
			return templateFolderFullPathTrim ;
		}
    	
//    	if ( _sTemplatesFolder != null )
//    	{
//    		String s = _sTemplatesFolder.trim() ;
//    		if ( s.length() == 0 )
//    		{
//    			// No templates folder 
//    			return null ;
//    		}
//    		else
//    		{
//    			// Templates folder is set => build the full path
//    			//return getProjectFullPath(_sTemplatesFolder) ;
//    			// new in v 2.0.7
//    			String templateFolderFullPath = getProjectFullPath(_sTemplatesFolder) ;
//    			if ( StrUtil.nullOrVoid(_sBundleName) == false ) {
//    				// There's a bundle defined => use it
//    				return FileUtil.buildFilePath(templateFolderFullPath, _sBundleName.trim() );
//    			}
//    			else {
//    				// No current bundle => use the standard templates folder as is
//    				return templateFolderFullPath ;
//    			}
//    		}
//    	}
//    	else
//    	{
//    		return null ;
//    	}
	}

    //------------------------------------------------------------------------------------------------------
    // DATABASES CONFIGURATIONS (since ver 2.1.0 )
    //------------------------------------------------------------------------------------------------------
    
    private DatabasesConfigurations _databasesConfigurations = null ;
    
	/**
	 * Returns the databases configurations loaded from the "databases.dbcfg" file
	 * @return
	 */
	public DatabasesConfigurations getDatabasesConfigurations() 
	{
		if ( _databasesConfigurations == null ) {
			// Not yet loaded
			_databasesConfigurations = loadDatabasesConfigurations() ;
		}
		return _databasesConfigurations ;
	}
	
	private DatabasesConfigurations loadDatabasesConfigurations() 
	{
		DatabasesConfigurations databasesConfigurations = null ;
		String dbcfgFileName = _telosysToolsCfg.getDatabasesDbCfgFileAbsolutePath();
		File dbcfgFile = new File(dbcfgFileName);
		if ( dbcfgFile.exists() ) {
			try {
				DbConfigManager dbConfigManager = new DbConfigManager( dbcfgFile );
				databasesConfigurations = dbConfigManager.load() ;
			} catch (TelosysToolsException e) {
				databasesConfigurations = new DatabasesConfigurations() ; // Void
			}
			return databasesConfigurations ;
		}
		else {
			return new DatabasesConfigurations() ; // Void
		}
	}

}
