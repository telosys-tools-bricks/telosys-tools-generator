/**
 *  Copyright (C) 2008-2017  Telosys project org. ( http://www.telosys.org/ )
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

import java.io.File;
import java.util.List;
import java.util.Map;

import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.commons.bundles.TargetDefinition;
import org.telosys.tools.commons.variables.VariablesManager;
import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityNoDoc;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.names.ContextName;
import org.telosys.tools.generic.model.Entity;

/**
 * The current target during generation 
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
		},
		since = "2.0.3"
 )
//-------------------------------------------------------------------------------------
public class Target {
	
	public static final String VAR_BUN = "BUN" ;
	public static final String VAR_MOD = "MOD" ;
	public static final String VAR_ENT = "ENT" ;
	public static final String VAR_BEANNAME = "BEANNAME" ; // keep "BEANNAME" only for backward compatibility
	
	private final String destinationDirAbsolutePath ; // v 4.2.0 (instead of TelosysToolsCfg)
	private final VariablesManager variablesManager ; // v 4.2.0 (instead of TelosysToolsCfg)
	
	// Target definition in templates bundle :
	private final String    targetName ; // Col 1
	private final String    originalFileDefinition ; // Col 2
	private final String    originalFolderDefinition; // Col 3
	private final String    template ; // Col 4
	private final String    type ; // Col 5
	
	private final String    entityName ;
	private String forcedEntityName = null ; // can be changed dynamically in the template file

	/**
	 * Constructor
	 * @param destinationDirAbsolutePath
	 * @param targetDefinition
	 * @param variables
	 * @param entityName (or "" if no entity for this target)
	 */
	private Target(String destinationDirAbsolutePath, TargetDefinition targetDefinition, Map<String,String> variables, String entityName ) {
		super();
		this.destinationDirAbsolutePath = destinationDirAbsolutePath; // v 4.2.0 (instead of TelosysToolsCfg)
		this.variablesManager = new VariablesManager(variables);  // v 4.2.0 (instead of TelosysToolsCfg)
		applyLowerCaseAndUpperCase(VAR_BUN); // BUN, BUN_UC, BUN_LC
		applyLowerCaseAndUpperCase(VAR_MOD); // MOD, MOD_UC, MOD_LC
		
		//--- Keep target definition
		this.targetName = targetDefinition.getName();
		this.originalFileDefinition   = targetDefinition.getFile() ;
		this.originalFolderDefinition = targetDefinition.getFolder();
		this.template = targetDefinition.getTemplate();
		if ( targetDefinition.isResource() ) {
			this.type = "R" ;
		} else if ( targetDefinition.isOnce() ) {
			this.type = "1" ;
		} else {
			this.type = "*" ;
		}
		
		//--- Specialization for the given entity
		this.entityName = entityName ;
		this.forcedEntityName = null ;
	}
	
	/**
	 * Constructor for a generation with an entity and a template
	 * @param destinationDirAbsolutePath
	 * @param targetDefinition
	 * @param variables
	 * @param entity
	 */
	public Target(String destinationDirAbsolutePath, TargetDefinition targetDefinition, Map<String,String> variables, Entity entity) {
		this(destinationDirAbsolutePath, targetDefinition, variables, entity.getClassName());
	}
	
	/**
	 * Constructor for a 'ONCE' target or a 'RESOURCE' target ( resource copy )
	 * @param destinationDirAbsolutePath
	 * @param targetDefinition
	 * @param variables
	 */
	public Target(String destinationDirAbsolutePath, TargetDefinition targetDefinition, Map<String,String> variables) {
		this(destinationDirAbsolutePath, targetDefinition, variables, "");
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
			"Returns the original folder definition for the generation in progress ",
			"(the folder as defined in the bundle, before variables substitution) "
			},
		since="3.3.0"
	)
	public String getOriginalFolderDefinition() {
		return originalFolderDefinition ;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the output file name for the generation in progress "
			}
	)
	public String getFile() {
		// Keep variable substitution here to use 'forcedEntityName' if defined
		updateEntityNameInVariablesManager(); // to be sure to be up-to-date for ENTITY NAME
		return variablesManager.replaceVariables(this.originalFileDefinition);
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the output file folder for the generation in progress "
			}
	)
	public String getFolder() {
		// Keep variable substitution here to use 'forcedEntityName' if defined
		updateEntityNameInVariablesManager(); // to be sure to be up-to-date for ENTITY NAME
		// New VariablesManager with '/' instead of '.' in each '*_PKG' variable
		VariablesManager vmWithDirPath = transformPackageVariablesToDirPath(this.variablesManager);
		return vmWithDirPath.replaceVariables(this.originalFolderDefinition);
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
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text = {
			"Forces the entity name (to change dynamically the entity name)",
			"If a forced name has been defined it will be used as the 'BEANNAME' to build the target file name",
			"Returns a void string (so that it can be used easily in the template)"
		},
		parameters = {
			"forcedName : the new entity name"
		}
	)
	public String forceEntityName(String forcedName) {
		this.forcedEntityName = forcedName ;
		updateEntityNameInVariablesManager(); 
		return "" ;
	}
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if there's a 'forced entity name'"
			}
	)
    public boolean hasForcedEntityName() {
		return ! StrUtil.nullOrVoid(forcedEntityName);
    }
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the 'forced entity name' (or '' if none)"
			}
	)
	public String getForcedEntityName() {
		return hasForcedEntityName() ? forcedEntityName : "" ;
	}
		
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text = {	
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
		String folder = getFolder();
		if ( null == srcFolder ) {
			// Use the folder as is
			return folderToPackage(folder) ;
		}
		String trimmedSrcFolder = srcFolder.trim() ;
		if ( trimmedSrcFolder.length() == 0 ) {
			// Use the folder as is
			return folderToPackage(folder) ;
		}
		
		String folder2 = removeFirstSlashIfAny(folder);
		String srcFolder2 = removeFirstSlashIfAny(trimmedSrcFolder);
		
		if ( folder2.startsWith(srcFolder2) ) {
			String subFolder = folder2.substring( srcFolder2.length() ); // Remove the beginning
			return folderToPackage(subFolder) ;
		}
		else {
			return "error.folder.not.started.with.the.given.src.folder" ;
		}
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text = {	
			"Returns the full path of the file that will be generated",
			"(it uses the 'SpecificDestinationFolder' if defined in configuration)",
			" "
		},
		since = "3.3.0"
		)
	public String getOutputFileFullPath() {
		return FileUtil.buildFilePath(this.destinationDirAbsolutePath, getOutputFileNameInProject()) ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text = {	
			"Returns true if the file to generate already exists",
			" "
		},
		since = "3.3.0"
		)
	public boolean outputFileExists() {
		File file = new File(getOutputFileFullPath());
		return file.exists();
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text = {	
			"Returns the target type as in templates configuration file ",
			"The type is '*', '1' or 'R'",
			" "
		},
		since = "3.3.0"
		)
	public String getType() {
		return this.type;
	}
	
	//-------------------------------------------------------------------------------------
	// END OF VELOCITY METHODS
	//-------------------------------------------------------------------------------------
	
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
	
	/**
	 * Returns the full path of the of the generated file in the project<br>
	 * by combining the folder and the basic file name
	 * ie : "src/org/demo/screen/employee/EmployeeData.java"
	 * @return
	 */
	@VelocityNoDoc
	public String getOutputFileNameInProject() {
		String folder = getFolder();
		String s = null ;
		if ( folder.endsWith("/") || folder.endsWith("\\") ) {
			s = folder + getFile() ;
		}
		else {
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
		return "Target [targetName=" + targetName + ", file=" + getFile()
				+ ", folder=" + getFolder() + ", template=" + template
				+ ", entityName=" + entityName + "]";
	}
	
	/**
	 * Update entity name values in variables "ENT[_LC|_UC]" and "BEANNAME[_LC|_UC]" <br>
	 * Supposed to be called each time 'forcedEntityName' is changed
	 */
	private void updateEntityNameInVariablesManager() {
		String currentEntityName = this.entityName;
		if ( hasForcedEntityName() ) {
			currentEntityName = this.forcedEntityName;
		}
		updateVariablesManager(VAR_ENT, currentEntityName); // new in v 4.2.0
		updateVariablesManager(VAR_BEANNAME, currentEntityName); // keep "BEANNAME" for backward compatibility
	}
	private void updateVariablesManager(String varName, String varValue) {
		variablesManager.setVariable(varName,       varValue);
		applyLowerCaseAndUpperCase(varName);
	}
	private void applyLowerCaseAndUpperCase(String varName) {
		String varValue = variablesManager.getVariableValue(varName);
		if ( varValue != null) {
			variablesManager.setVariable(varName+"_LC", varValue.toLowerCase());
			variablesManager.setVariable(varName+"_UC", varValue.toUpperCase());
		}
	}
	
	protected VariablesManager transformPackageVariablesToDirPath(VariablesManager originalVariablesManager ) {
		VariablesManager vmCopy = originalVariablesManager.copy();
		List<String> names = originalVariablesManager.getVariablesNames();
		for ( String name : names ) {
			String value = vmCopy.getVariableValue(name) ;
			if ( name.endsWith("_PKG" ) ) {
				vmCopy.setVariable(name, value.replace('.', '/') );
			}
			else {
				vmCopy.setVariable(name, value); 
			}
		}
		return vmCopy;
	}
}
