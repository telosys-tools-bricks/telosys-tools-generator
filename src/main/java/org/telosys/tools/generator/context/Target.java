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
package org.telosys.tools.generator.context;

import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.commons.config.ConfigDefaults;
import org.telosys.tools.commons.variables.Variable;
import org.telosys.tools.commons.variables.VariablesManager;
import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityNoDoc;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.names.ContextName;
import org.telosys.tools.generator.target.TargetDefinition;

/**
 * The generation target file <br>  
 * 
 * @author L. Guerin
 *
 */
//-------------------------------------------------------------------------------------
@VelocityObject(
		contextName= ContextName.TARGET ,
		text = { 
				"The current target for the generation in progress",
				"",
				"Example when using $generator : ",
				"$generator.generate($target.entityName, \"${beanClass.name}Key.java\", $target.folder, \"jpa_bean_pk.vm\"  )"
		}
 )
//-------------------------------------------------------------------------------------
public class Target 
{
	private final String    targetName ;
	
	private final String    file ;
	
	private final String    folder ;
	
	private final String    template ;

	private final String    entityName ;

//	private final boolean   templateOnly ; // v 2.1.1

	// Removed in v 2.1.1
//	/**
//	 * Constructor for special target containing only the template <br>
//	 * Used by Wizards generators
//	 * 
//	 * @param template
//	 */
//	public Target( String template ) {
//		super();
//		this.targetName = null;
//		this.file = null ;
//		this.folder = null;
//		this.template = template.trim();
//		this.entityName = null ;
////		this.templateOnly = true ; // v 2.1.1
//	}
	
	/**
	 * Constructor
	 * @param targetDefinition  the initial target as defined in the targets configuration file
	 * @param entityName  the name of the entity (as defined in the repository model)
	 * @param entityJavaClassName 
	 * @param variables  the project's specific variables to be applied 
	 */
	public Target( TargetDefinition targetDefinition, String entityName, String entityJavaClassName, Variable[] variables ) 
	{
		super();
		
		//--- Generic target informations
		this.targetName = targetDefinition.getName();
		this.template = targetDefinition.getTemplate();
		
		//--- Specialization for the given entity
		this.entityName = entityName ;

//		this.templateOnly = false ; // v 2.1.1
		
		//--- Replace the "$" variables in _sFile and _sFolder
		VariablesManager variablesManager = null ;
		if ( variables != null ) {
			variablesManager = new VariablesManager( variables );
		}
		this.file   = replaceVariables( targetDefinition.getFile(),   entityJavaClassName, variablesManager );
		
		variablesManager.transformPackageVariablesToDirPath(); // for each variable ${XXXX_PKG} : replace '.' by '/' 
		this.folder = replaceVariables( targetDefinition.getFolder(), entityJavaClassName, variablesManager );
	}

	// removed in v 2.1.1	
//	/**
//	 * Returns true if this target contains only the template file <br>
//	 * ( not a "real target", true for Wizards generation ) 
//	 * 
//	 * @return
//	 */
//	@VelocityNoDoc
//	public boolean isTemplateOnly() {
//		return this.templateOnly ;
//	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the target's name (as defined in the targets file) for the generation in progress "
			}
	)
	public String getTargetName() {
		return targetName;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the output file name for the generation in progress "
			}
	)
	public String getFile() {
		return file;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the output file folder for the generation in progress "
			}
	)
	public String getFolder() {
		return folder;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the template file name (.vm) for the generation in progress "
			}
	)
	public String getTemplate() {
		return template;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the entity name for the generation in progress "
			}
	)
	public String getEntityName()
	{
		return entityName ;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the Java package corresponding to the file path after removing the given source folder "
			},
		parameters = {
				"srcFolder : the source folder (the beginning of path to be removed to get the package folder)"
		},
		example = {
			"package ${target.javaPackageFromFolder($SRC)};"
		}
	)
	public String javaPackageFromFolder(String srcFolder) {
		
		if ( null == srcFolder ) {
			// Use the folder as is
			return folderToPackage(this.folder) ;
		}
		String trimmedSrcFolder = srcFolder.trim() ;
		if ( trimmedSrcFolder.length() == 0 ) {
			// Use the folder as is
			return folderToPackage(this.folder) ;
		}
		
		String folder2 = removeFirstSlashIfAny(this.folder);
		String srcFolder2 = removeFirstSlashIfAny(trimmedSrcFolder);
		
		if ( folder2.startsWith(srcFolder2) ) {
			String subFolder = folder2.substring( srcFolder2.length() ); // Remove the beginning
			return folderToPackage(subFolder) ;
		}
		else {
			return "error.folder.not.started.with.the.given.src.folder" ;
		}
	}

	private String removeFirstSlashIfAny(String s) {
		if ( s.startsWith("/") ) {
			return s.substring(1);
		}
		if ( s.startsWith("\\") ) {
			return s.substring(1);
		}
		return s;
	}
	
	private String folderToPackage(String folder) {
		if ( null == folder ) {
			return "" ;
		}
		char[] chars = folder.toCharArray();
		for ( int i = 0 ; i < chars.length ; i++ ) {
			char c = chars[i] ;
			if ( c == '/' || c == '\\' ) {
				chars[i] = '.';
			}
		}
		//--- Avoid starting "."
		String s2 = new String(chars);
		if ( s2.startsWith(".") ) {
			s2 = s2.substring(1);
		}
		//--- Avoid ending "."
		if ( s2.endsWith(".") ) {
			s2 = s2.substring(0, s2.length()-1);
		}
		return s2 ;
	}
	
	//-----------------------------------------------------------------------
	private String replaceVariables( String originalString, String sBeanClass, VariablesManager varmanager )
	{
		//--- Replace the generic name "${BEANNAME}" if any
		String s1 = replace(originalString, ConfigDefaults.BEANNAME, sBeanClass);

		//--- Replace the global project variables if any
		if ( varmanager != null ) {
			return varmanager.replaceVariables(s1);
		}
		else {
			return s1 ;
		}
	}
	
	//-----------------------------------------------------------------------
    private String replace(String sOriginal, String sSymbolicVar, String sValue) 
    {
    	String s   = "${" + sSymbolicVar + "}" ;
    	String sUC = "${" + sSymbolicVar + "_UC}" ;
    	String sLC = "${" + sSymbolicVar + "_LC}" ;
    	
		if ( sOriginal.indexOf(s) >= 0 )
		{
			return StrUtil.replaceVar(sOriginal, s, sValue);
		}
		else if ( sOriginal.indexOf(sUC) >= 0 )
		{
			return StrUtil.replaceVar(sOriginal, sUC, sValue.toUpperCase());
		}
		else if ( sOriginal.indexOf(sLC) >= 0 )
		{
			return StrUtil.replaceVar(sOriginal, sLC, sValue.toLowerCase());
		}
		return sOriginal ;
    }
    
	/**
	 * Returns the full path of the of the generated file in the project<br>
	 * by combining the folder and the basic file name
	 * ie : "src/org/demo/screen/employee/EmployeeData.java"
	 * @return
	 */
	@VelocityNoDoc
	public String getOutputFileNameInProject()
	{
		String s = null ;
		if ( folder.endsWith("/") || folder.endsWith("\\") )
		{
			s = folder + file ;
		}
		else
		{
			s = folder + "/" + file ;
		}
		if ( s.startsWith("/") || s.startsWith("\\") )
		{
			return s.substring(1);
		}
		return s ;
	}

	/**
	 * Returns the absolute full path of the generated file in the file system <br>
	 * using the given project location
	 * ie : "C:/tmp/project/src/org/demo/screen/employee/EmployeeData.java"
	 * @param projectLocation the project location ( ie "C:/tmp/project" )
	 * @return
	 */
	@VelocityNoDoc
	public String getOutputFileNameInFileSystem(String projectLocation)
	{
//		String s = getOutputFileNameInProject();
//		if ( projectLocation != null )
//		{
//			if ( projectLocation.endsWith("/") || projectLocation.endsWith("\\") )
//			{
//				return projectLocation + s ;
//			}
//			else
//			{
//				return projectLocation + "/" + s ;
//			}
//		}
//		return "/" + s ;
		String fileInProject = getOutputFileNameInProject() ;
		return buildFullPath(projectLocation, fileInProject ) ;
	}
	
	/**
	 * Returns the absolute full path of the folder in the file system <br>
	 * where to generate the target (using the given project location)
	 * ie : "C:/tmp/project/src/org/demo"
	 * @param projectLocation the project location ( ie "C:/tmp/project" )
	 * @return
	 * @since 2.0.7
	 */
	@VelocityNoDoc
	public String getOutputFolderInFileSystem(String projectLocation)
	{
		String folderInProject = getFolder() ;
		return buildFullPath(projectLocation, folderInProject ) ;
	}

	private String buildFullPath(String projectLocation, String fileOrFolder)
	{
		if ( projectLocation != null )
		{
			if ( projectLocation.endsWith("/") || projectLocation.endsWith("\\") )
			{
				return projectLocation + fileOrFolder ;
			}
			else
			{
				return projectLocation + "/" + fileOrFolder ;
			}
		}
		return "/" + fileOrFolder ;
	}
	
	@VelocityNoDoc
	@Override
	public String toString() {
		return "Target [targetName=" + targetName + ", file=" + file
				+ ", folder=" + folder + ", template=" + template
				+ ", entityName=" + entityName + "]";
//				+ ", templateOnly=" + templateOnly + "]"; // v 2.1.1
	}
	
	
}
