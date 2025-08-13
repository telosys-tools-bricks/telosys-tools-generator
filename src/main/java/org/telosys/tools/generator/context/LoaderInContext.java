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
import java.net.URL;

import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.classloader.SpecificClassLoader;
import org.telosys.tools.commons.classloader.SpecificClassPath;
import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.names.ContextName;

/**
 * Special class used as a specific class loader <br> 
 * Used to load a specific Java Class or instance in the Velocity Context
 * 
 * @author Laurent GUERIN
 *
 */
//-------------------------------------------------------------------------------------
@VelocityObject(
		contextName= ContextName.LOADER ,
		text = {
				"Special object used as a specific class loader by the generator",
				"",
				"Can be used to load a specific Java Class tool in the Velocity Context",
				"Allows users to create their own classes and to use them in the templates"
		},
		since = "2.0.3"
 )
//-------------------------------------------------------------------------------------
public class LoaderInContext {
	
    private final SpecificClassLoader    specificClassLoader ; // Specific Class Loader instance

	private final String                 templatesFolderFullPath ; // Full templates full path with bundle name
	private final File                   classesFolder ; // "templates/(bundle)/classes"
	private final File                   libFolder ;     // "templates/(bundle)/lib"
	
	/**
	 * Constructor ( ver 2.1.0 )
	 * @param templatesFolderFullPath
	 * @param velocityContext
	 */
	public LoaderInContext(String templatesFolderFullPath) {
		super();
		this.templatesFolderFullPath = templatesFolderFullPath;
		this.classesFolder = new File ( FileUtil.buildFilePath(this.templatesFolderFullPath, "classes" ) );
		this.libFolder     = new File ( FileUtil.buildFilePath(this.templatesFolderFullPath, "lib"     ) );
		this.specificClassLoader = buildClassLoader() ;
	}
	
	private SpecificClassLoader buildClassLoader() {
		SpecificClassPath specificClassPath = buildClassPath();
		ClassLoader currentClassLoader = this.getClass().getClassLoader();

		return new SpecificClassLoader(specificClassPath, currentClassLoader);
	}
	
	private SpecificClassPath buildClassPath() {
		SpecificClassPath classpath = new SpecificClassPath();
		classpath.addDirectory(this.classesFolder);        // "templates/(bundle)/classes"
		classpath.addJarFilesInDirectory(this.libFolder);  // "templates/(bundle)/lib/*.jar"
		return classpath ;
	}
	
	//--------------------------------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the full file path of the folder where the specific classes are searched by the loader"
			},
		since="2.0.5"
	)
	public String getClassesFolder() {
		return this.classesFolder.getAbsolutePath();
	}
	
	//--------------------------------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the full file path of the folder where the specific libraries (jar) are searched by the loader"
			},
		since="3.0.0"
	)
	public String getLibFolder() {
		return this.libFolder.getAbsolutePath();
	}
	
	//--------------------------------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns an array containing all the URLs used by the class loader"
			},
		since="3.0.0"
	)
	public URL[] getURLs() {
		return this.specificClassLoader.getURLs();
	}
	
	//--------------------------------------------------------------------------------------------------------------
	@VelocityMethod (
		text = {
				"Loads the given java class and return it (no instance created).",
				"It can be a standard Java class (class of the JDK) or a specific class.",
				"The specific classes must be located in the 'classes' folder (in the templates bundle)",
				"or in a jar located in the 'lib' folder (in the templates bundle)"
		},
		parameters = {
				"javaClassName : the name of the Java class to be loaded "
		},
		example = {
				"## load the class and put an instance in the context",
				"#set( $Math = $loader.loadClass(\"java.lang.Math\") ",
				"## use the static methods of this class ",
				"$Math.random()"
			},
		since="2.1.0"
		
	)
	public Class<?> loadClass( String javaClassName ) throws GeneratorException {
		return loadJavaClassFromFile( javaClassName ) ;
	}
	
	//--------------------------------------------------------------------------------------------------------------
	@VelocityMethod (
		text = {
				"Loads the given java class, creates a new instance and return it",
				"It can be a standard Java class (class of the JDK) or a specific class.",
				"The specific classes must be located in the 'classes' folder (in the templates bundle)",
				"or in a jar located in the 'lib' folder (in the templates bundle)",
				"NB : The Java class must have a default constructor (in order to be created by 'javaClass.newInstance()'"
		},
		parameters = {
				"javaClassName : the name of the Java class to be loaded and used to create the instance"
		},
		example = {
				"## create an instance of StringBuilder and put it in the context with #set",
				"#set( $strBuilder = $loader.newInstance('java.lang.StringBuilder') )",
				"## use the instance ",
				"$strBuilder.append('aa')",
				"",
				"## create new instance of a specific class",
				"#set( $tool = $loader.newInstance('MyTool') )"
		},
		since="2.1.0"
		
	)
	public Object newInstance(String javaClassName ) throws GeneratorException
	{
		Class<?> javaClass = loadJavaClassFromFile( javaClassName ) ;
		
		//--- New instance
		Object instance = null ;
		try {
			instance = javaClass.newInstance() ;
		} catch (InstantiationException e) {
			throw new GeneratorException("Cannot create instance for " + javaClassName + " (InstantiationException)", e);
		} catch (IllegalAccessException e) {
			throw new GeneratorException("Cannot create instance for " + javaClassName + " (IllegalAccessException)", e);
		}
		return instance ;
	}
	
	//--------------------------------------------------------------------------------------------------------------
	private Class<?> loadJavaClassFromFile( String javaClassName ) throws GeneratorException
	{
		Class<?> javaClass = null ;
		try {
			javaClass = specificClassLoader.loadClass(javaClassName);
		} catch (ClassNotFoundException e) {
			throw new GeneratorException("Cannot load class " + javaClassName + " (ClassNotFoundException)", e);
		}
		return javaClass ;
	}
	
}
