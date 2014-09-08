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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.names.ContextName;

/**
 * Special class used as a specific class loader <br> 
 * Used to load a specific Java Class tool in the Velocity Context
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
		since = ""
 )
//-------------------------------------------------------------------------------------
public class Loader {

	private final static String CLASSES = "classes" ;
	
//	private final ProjectConfiguration  projectConfig ;
//	
//	public Loader(ProjectConfiguration projectConfig, VelocityContext velocityContext) {
//		super();
//		this.projectConfig   = projectConfig;
//	}

	private final String templatesFolderFullPath ; // Full templates full path with bundle name
	
	/**
	 * Constructor ( ver 2.1.0 )
	 * @param templatesFolderFullPath
	 * @param velocityContext
	 */
	public Loader(String templatesFolderFullPath) {
		super();
		this.templatesFolderFullPath = templatesFolderFullPath;
	}

	//--------------------------------------------------------------------------------------------------------------
	/**
	 * Returns the folder where to search the classes to be loaded <br>
	 * The folder is "classes" in the project templates folder
	 * @return
	 */
	private File getClassesFolderAsFile()
	{
		//String templatesFolder = projectConfig.getTemplatesFolderFullPath();
		String templatesFolder = this.templatesFolderFullPath ;
		String javaClassFolder ;		
		if ( templatesFolder.endsWith("/") || templatesFolder.endsWith("\\") ) {
			javaClassFolder = templatesFolder + CLASSES ;
		}
		else {
			javaClassFolder = templatesFolder + "/" + CLASSES ;
		}
		
		// Create a File object on the root of the directory containing the class file
		File file = new File(javaClassFolder);
		return file ;
	}
	
	//--------------------------------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the full file path of the folder where the specific classes are searched by the loader"
			},
		since="2.0.5"
	)
	public String getClassesFolder()
	{
		File folder = getClassesFolderAsFile();
		return folder.toString() ;
	}
	
	//--------------------------------------------------------------------------------------------------------------
	@VelocityMethod (
		text = {
				"Loads the given java class and return it (no instance created).",
				"It can be a standard Java class (class of the JDK) or a specific class.",
				"The specific classes must be located in the 'classes' folder of the templates"
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
	public Class<?> loadClass( String javaClassName ) throws GeneratorException
	{
		Class<?> javaClass = loadJavaClassFromFile( javaClassName ) ;
		
//		if ( javaClass != null ) {
//			//--- Put the class in the Velocity context
//			velocityContext.put(nameInContext, javaClass);
//		}
		
		return javaClass ;
	}
	
	//--------------------------------------------------------------------------------------------------------------
	@VelocityMethod (
		text = {
				"Loads the given java class, creates a new instance and return it",
				"It can be a standard Java class (class of the JDK) or a specific class.",
				"The specific classes must be located in the 'classes' folder of the templates",
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
//			//--- Put the new instance in the Velocity context
//			if ( instance != null ) {
//				velocityContext.put(nameInContext, instance);
//			}
		return instance ;
	}
	
	//--------------------------------------------------------------------------------------------------------------
	private Class<?> loadJavaClassFromFile( String javaClassName ) throws GeneratorException
	{
		ClassLoader currentClassLoader = this.getClass().getClassLoader();
		File file = getClassesFolderAsFile();
		
		Class<?> javaClass = null ;
		
		try {
		    // Convert File to URL
		    URL url = file.toURI().toURL();    //  "file:/c:/templatesFolder/"
		    URL[] urls = new URL[]{url}; // the URLs from which to load classes and resources

		    // Create a new class loader with the given directory and the current class loader as parent class loader
		    ClassLoader classLoader = new URLClassLoader(urls, currentClassLoader );

		    // Load the class ( should be located in "file:/c:/templatesFolder/" )
		    javaClass = classLoader.loadClass(javaClassName);
		} catch (MalformedURLException e) {
			throw new GeneratorException("Cannot load class " + javaClassName + " (MalformedURLException)", e);
		} catch (ClassNotFoundException e) {
			throw new GeneratorException("Cannot load class " + javaClassName + " (ClassNotFoundException)", e);
		}
		
		return javaClass ;
	}
	
}
