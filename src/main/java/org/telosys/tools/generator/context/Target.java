/**
 *  Copyright (C) 2008-2015  Telosys project org. ( http://www.telosys.org/ )
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

import java.util.HashMap;

import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.bundles.TargetDefinition;
import org.telosys.tools.commons.variables.Variable;
import org.telosys.tools.commons.variables.VariablesManager;
import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityNoDoc;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.names.ContextName;
import org.telosys.tools.generic.model.Entity;

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
	//private final static String BEANNAME = "BEANNAME" ;
	
	private final VariablesManager variablesManager ;
	
	private final String    targetName ;
	
	//private final String    file ;
	private final String    originalFileDefinition ;
	
	private final String    folder ;
	
	private final String    template ;

	private final String    entityName ;

	private String forcedEntityName = null ;
	
	/**
	 * Constructor for a generation with an entity and a template
	 * @param targetDefinition
	 * @param entity
	 * @param variables
	 */
	public Target( TargetDefinition targetDefinition, Entity entity, Variable[] variables ) {
		super();
		//--- Generic target informations
		this.targetName = targetDefinition.getName();
		this.template = targetDefinition.getTemplate();
		
		//--- Specialization for the given entity
		this.entityName = entity.getClassName() ;
		this.forcedEntityName = null ;

		//--- Replace the "$" variables in _sFile and _sFolder
		//VariablesManager variablesManager = new VariablesManager( variables ); 
		this.variablesManager = new VariablesManager( variables ); 
		//this.file   = replaceVariables( targetDefinition.getFile(), variablesManager );
		this.originalFileDefinition = targetDefinition.getFile() ;
		
		this.variablesManager.transformPackageVariablesToDirPath(); // for each variable ${XXXX_PKG} : replace '.' by '/' 
		this.folder = replaceVariables( targetDefinition.getFolder(), variablesManager );
	}

	/**
	 * Constructor for a 'ONCE' target or a 'RESOURCE' target ( resource copy )
	 * @param targetDefinition
	 * @param variables
	 */
	public Target( TargetDefinition targetDefinition, Variable[] variables ) {
		super();
		//--- Generic target informations
		this.targetName = targetDefinition.getName();
		this.template = targetDefinition.getTemplate();
		
		//--- No current entity 
		this.entityName = "" ;

		//--- Replace the "$" variables in _sFile and _sFolder
		//VariablesManager variablesManager = new VariablesManager( variables ); 		
		this.variablesManager = new VariablesManager( variables ); 
		//this.file   = replaceVariables( targetDefinition.getFile(), variablesManager );
		this.originalFileDefinition = targetDefinition.getFile() ;
		
		this.variablesManager.transformPackageVariablesToDirPath(); // for each variable ${XXXX_PKG} : replace '.' by '/' 
		this.folder = replaceVariables( targetDefinition.getFolder(), variablesManager );
	}

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
			"Returns the original file definition for the generation in progress ",
			"(the file as defined in the bundle, before variables substitution) "
			}
	)
	public String getOriginalFileDefinition() {
		return originalFileDefinition ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the output file name for the generation in progress "
			}
	)
	public String getFile() {
		//return file;
		return replaceVariables( originalFileDefinition, variablesManager );
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
			"Returns the entity name for the generation in progress (entity class name : Book, Author, ...)"
			}
	)
	public String getEntityName() {
		return entityName ;
	}
	
	@VelocityMethod(
			text={	
				"Forces the entity name (to change dynamically the entity name)",
				"If a forced name has been defined it will be used as the 'BEANNAME' to build the target file name",
				"Returns a void string (so that it can be used easily in the template)"
				}
		)
	public String forceEntityName(String forcedName) {
		this.forcedEntityName = forcedName ;
		return "" ;
	}

	@VelocityMethod(
			text={	
				"Returns the 'forced entity name' (or '' if none)"
				}
		)
	public String getForcedEntityName() {
		return forcedEntityName != null ? forcedEntityName : "" ;
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
	
//	//-----------------------------------------------------------------------
//	private String replaceVariables( String originalString, String sBeanClass, VariablesManager varmanager ) {
//		//--- Replace the generic name "${BEANNAME}" if any
////		String s1 = replace(originalString, ConfigDefaults.BEANNAME, sBeanClass);
//		String s1 = replace(originalString, BEANNAME, sBeanClass);
//
//		//--- Replace the global project variables if any
//		if ( varmanager != null ) {
//			return varmanager.replaceVariables(s1);
//		}
//		else {
//			return s1 ;
//		}
//	}
//	
//	//-----------------------------------------------------------------------
//    private String replace(String sOriginal, String sSymbolicVar, String sValue) 
//    {
//    	String s   = "${" + sSymbolicVar + "}" ;
//    	String sUC = "${" + sSymbolicVar + "_UC}" ;
//    	String sLC = "${" + sSymbolicVar + "_LC}" ;
//    	
//		if ( sOriginal.indexOf(s) >= 0 )
//		{
//			return StrUtil.replaceVar(sOriginal, s, sValue);
//		}
//		else if ( sOriginal.indexOf(sUC) >= 0 )
//		{
//			return StrUtil.replaceVar(sOriginal, sUC, sValue.toUpperCase());
//		}
//		else if ( sOriginal.indexOf(sLC) >= 0 )
//		{
//			return StrUtil.replaceVar(sOriginal, sLC, sValue.toLowerCase());
//		}
//		return sOriginal ;
//    }

	private String replaceVariables( String originalString, VariablesManager varmanager ) {
		
		String s1 = originalString;
		if ( this.entityName != null && this.entityName.length() > 0 ) {
			//--- Replace the generic name "${BEANNAME}" if any
			s1 = replaceBEANNAME(originalString);
		}

		//--- Replace the global project variables if any
		if ( varmanager != null ) {
			return varmanager.replaceVariables(s1);
		}
		else {
			return s1 ;
		}
	}
    private String replaceBEANNAME(String originalString) {
    	
    	// Set the BEANNAME value (using entityName or forcedEntityName if any)
    	String beannameValue = this.entityName ;
    	if ( this.forcedEntityName != null ) {
    		beannameValue = this.forcedEntityName ;
    	}
    	
//    	// entityClassName is not defined => nothing to do
//    	if ( this.entityName == null ) return originalString ;
//    	if ( this.entityName.length() == 0 ) return originalString ;
//    	// replace "${BEANNAME}" if any
//    	HashMap<String,String> hm = new HashMap<String,String>();
//    	hm.put("${BEANNAME}",    this.entityName );		
//    	hm.put("${BEANNAME_LC}", this.entityName.toLowerCase() );
//    	hm.put("${BEANNAME_UC}", this.entityName.toUpperCase() );

    	// beannameValue is not defined => nothing to do
    	if ( beannameValue == null ) return originalString ;
    	if ( beannameValue.length() == 0 ) return originalString ;
    	// beannameValue is defined => replace "${BEANNAME}" 
    	HashMap<String,String> hm = new HashMap<String,String>();
    	hm.put("${BEANNAME}",    beannameValue );		
    	hm.put("${BEANNAME_LC}", beannameValue.toLowerCase() );
    	hm.put("${BEANNAME_UC}", beannameValue.toUpperCase() );

    	VariablesManager variablesManager = new VariablesManager(hm) ;
    	return variablesManager.replaceVariables(originalString);
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
		if ( folder.endsWith("/") || folder.endsWith("\\") ) {
			//s = folder + file ;
			s = folder + getFile() ;
		}
		else {
			//s = folder + "/" + file ;
			s = folder + "/" + getFile() ;
		}
		if ( s.startsWith("/") || s.startsWith("\\") ) {
			return s.substring(1);
		}
		return s ;
	}

	/**
	 * Returns the absolute full path of the generated file in the file system <br>
	 * using the given project location <br>
	 * Return example : "C:/tmp/project/src/org/demo/screen/employee/EmployeeData.java"
	 * @param destinationFolderFullPath the destination folder ( ie "C:/tmp/project" )
	 * @return
	 */
	@VelocityNoDoc
	public String getOutputFileNameInFileSystem(String destinationFolderFullPath)
	{
		String fileNameInProject = getOutputFileNameInProject() ;
		return FileUtil.buildFilePath(destinationFolderFullPath, fileNameInProject) ; // v 3.0.0
	}
	
	@VelocityNoDoc
	@Override
	public String toString() {
//		return "Target [targetName=" + targetName + ", file=" + file
//				+ ", folder=" + folder + ", template=" + template
//				+ ", entityName=" + entityName + "]";
		return "Target [targetName=" + targetName + ", file=" + getFile()
				+ ", folder=" + folder + ", template=" + template
				+ ", entityName=" + entityName + "]";
	}
	
	
}
