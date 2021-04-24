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

import java.util.LinkedHashMap;
import java.util.Map;

import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.names.ContextName;
import org.telosys.tools.generic.model.types.LiteralValuesProvider;
import org.telosys.tools.generic.model.types.LiteralValuesProviderForCPlusPlus;
import org.telosys.tools.generic.model.types.LiteralValuesProviderForCSharp;
import org.telosys.tools.generic.model.types.LiteralValuesProviderForGo;
import org.telosys.tools.generic.model.types.LiteralValuesProviderForJava;
import org.telosys.tools.generic.model.types.LiteralValuesProviderForJavaScript;
import org.telosys.tools.generic.model.types.LiteralValuesProviderForPHP;
import org.telosys.tools.generic.model.types.LiteralValuesProviderForPython;
import org.telosys.tools.generic.model.types.LiteralValuesProviderForTypeScript;
import org.telosys.tools.generic.model.types.TypeConverter;
import org.telosys.tools.generic.model.types.TypeConverterForCPlusPlus;
import org.telosys.tools.generic.model.types.TypeConverterForCSharp;
import org.telosys.tools.generic.model.types.TypeConverterForGo;
import org.telosys.tools.generic.model.types.TypeConverterForJava;
import org.telosys.tools.generic.model.types.TypeConverterForJavaScript;
import org.telosys.tools.generic.model.types.TypeConverterForPHP;
import org.telosys.tools.generic.model.types.TypeConverterForPython;
import org.telosys.tools.generic.model.types.TypeConverterForTypeScript;

//-------------------------------------------------------------------------------------
@VelocityObject(
		contextName=ContextName.ENV,
		text = { 
				"Object for environment configuration",
				"The 'environement' object is reset for each generation."
		},
		since = "2.1.0"
 )
//-------------------------------------------------------------------------------------
public class EnvInContext {
	
	private static final String JAVA       = "JAVA" ;
	private static final String CSHARP     = "C#" ;
	private static final String GO         = "GO" ;
	private static final String TYPESCRIPT = "TYPESCRIPT" ;
	private static final String JAVASCRIPT = "JAVASCRIPT" ;
	private static final String PYTHON     = "PYTHON" ;
	private static final String PHP        = "PHP" ;
	private static final String CPLUSPLUS  = "C++" ;
	
	private String entityClassNamePrefix = "" ;
	private String entityClassNameSuffix = "" ;
	
	private String language = "Java" ; // v 3.0.0
	
	private String specificCollectionType = null ; // v 3.3.0

	private Map<String,String> databaseTypesMapping = new LinkedHashMap<>() ; // v 3.3.0
	private String database = "default" ; // v 3.3.0
	
	//-------------------------------------------------------------------------------------
	// CONSTRUCTOR
	//-------------------------------------------------------------------------------------
	public EnvInContext() {
		super();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Set the entity class name prefix",
			"Once the 'prefix' is defined in the 'environment object' ",
			"it is automatically applied when the entity class name is retrieved"
			},
		example={ 
			"#set ( $env.entityClassNamePrefix = 'Bean' )" },
		parameters = { 
			"prefix : the prefix to be used" 
			},
		since = "2.1.0"
			)
	public void setEntityClassNamePrefix( String prefix ) {
		this.entityClassNamePrefix = prefix ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the current entity class name prefix.",
			"The default value is a void string (never null)"
			},
		example={ 
			"$env.entityClassNamePrefix" 
			},
		since = "2.1.0"
			)
	public String getEntityClassNamePrefix() {
		return this.entityClassNamePrefix;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Set the entity class name suffix",
			"Once the 'suffix' is defined in the 'environment object' ",
			"it is automatically applied when the entity class name is retrieved"
			},
		example={ 
			"#set ( $env.entityClassNameSuffix = 'Entity' )" },
		parameters = { 
			"suffix : the suffix to be used" 
			},
		since = "2.1.0"
			)
	public void setEntityClassNameSuffix( String suffix ) {
		this.entityClassNameSuffix = suffix ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the current entity class name suffix.",
			"The default value is a void string (never null)"
			},
		example={ 
			"$env.entityClassNameSuffix" 
			},
		since = "2.1.0"
			)
	public String getEntityClassNameSuffix() {
		return this.entityClassNameSuffix;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Set the language for the current code generation",
			"Supported languages are 'Java', 'C#', 'Go', 'Python', 'JavaScript', 'TypeScript',  ",
			"( the default language is 'Java' )",
			"This information is used dermine language peculiarities like types and literal values"
			},
		example={ 
			"#set ( $env.language = 'C#' ) ",
			"$env.setLanguage('C#') " 
			},
		parameters = { 
			"language : the language to be used (not case sensitive) " 
			},
		since = "3.0.0"
			)
	public void setLanguage( String language ) throws GeneratorException {
		checkLanguageValidity(language);
		this.language = language ;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the current language",
			"The default value is 'Java' (never null)"
			},
		example={ 
			"$env.language" 
			},
		since = "3.0.0"
			)
	public String getLanguage() {
		return language;
	}
	
	//-------------------------------------------------------------------------------------
	private void checkLanguageValidity(String language) throws GeneratorException {
		String languageUC = language.toUpperCase() ;
		if ( JAVA.equals(languageUC) ) return ;
		if ( CSHARP.equals(languageUC) ) return ;
		if ( GO.equals(languageUC) ) return ;
		if ( TYPESCRIPT.equals(languageUC) ) return ;
		if ( JAVASCRIPT.equals(languageUC) ) return ;
		if ( PYTHON.equals(languageUC) ) return ;
		if ( PHP.equals(languageUC) ) return ;
		if ( CPLUSPLUS.equals(languageUC) ) return ;
		// Unknown language
		throw new GeneratorException("Unknown language '" + language + "'");
	}
	
	/**
	 * Returns TRUE if the current language is Java
	 * @return
	 */
	protected boolean languageIsJava() {
		return JAVA.equalsIgnoreCase(this.language) ;
	}
	/**
	 * Returns TRUE if the current language is GoLang
	 * @return
	 */
	protected boolean languageIsGo() {
		return GO.equalsIgnoreCase(this.language) ;
	}
	
	//-------------------------------------------------------------------------------------
	/**
	 * Returns the TypeConverter corresponding to the current language <br>
	 * and with the specific collection type if any
	 * @return
	 * @since ver 3.0.0
	 */
	public TypeConverter getTypeConverter() { // keep 'public' for debug in '.vm' files
		TypeConverter typeConverter = createTypeConverterForCurrentLanguage();
		// set specific collection type if any 
		if ( specificCollectionType != null ) {
			typeConverter.setSpecificCollectionType(specificCollectionType);
		}
		return typeConverter;
	}
	
	private TypeConverter createTypeConverterForCurrentLanguage()  {
		String languageUC = this.language.toUpperCase() ;
		if ( JAVA.equals(languageUC) ) {
			return new TypeConverterForJava() ;
		}
		else if ( CSHARP.equals(languageUC) ) {
			return new TypeConverterForCSharp() ; 
		}
		else if ( GO.equals(languageUC) ) {
			return new TypeConverterForGo() ; 
		}
		else if ( TYPESCRIPT.equals(languageUC) ) {
			return new TypeConverterForTypeScript() ;
		}
		else if ( JAVASCRIPT.equals(languageUC) ) {
			return new TypeConverterForJavaScript() ;
		}
		else if ( PYTHON.equals(languageUC) ) {
			return new TypeConverterForPython() ;
		}
		else if ( PHP.equals(languageUC) ) {
			return new TypeConverterForPHP() ;
		}
		else if ( CPLUSPLUS.equals(languageUC) ) {
			return new TypeConverterForCPlusPlus() ;
		}
		else {
			// By default : Java  ( not supposed to happen ) 
			return new TypeConverterForJava() ;
		}
	}

	/**
	 * Returns the LiteralValuesProvider for the current language
	 * @return
	 * @since ver 3.0.0
	 */
	public LiteralValuesProvider getLiteralValuesProvider()  {
		String languageUC = this.language.toUpperCase() ;
		if ( JAVA.equals(languageUC) ) {
			return new LiteralValuesProviderForJava() ;
		}
		else if ( CSHARP.equals(languageUC) ) {
			return new LiteralValuesProviderForCSharp() ;
		}
		else if ( GO.equals(languageUC) ) {
			return new LiteralValuesProviderForGo() ;
		}
		else if ( JAVASCRIPT.equals(languageUC) ) {
			return new LiteralValuesProviderForJavaScript() ;
		}
		else if ( TYPESCRIPT.equals(languageUC) ) {
			return new LiteralValuesProviderForTypeScript() ;
		}
		else if ( PYTHON.equals(languageUC) ) {
			return new LiteralValuesProviderForPython();
		}
		else if ( PHP.equals(languageUC) ) {
			return new LiteralValuesProviderForPHP();
		}
		else if ( CPLUSPLUS.equals(languageUC) ) {
			return new LiteralValuesProviderForCPlusPlus() ;
		}
		else {
			// By default : Java  ( not supposed to happen ) 
			return new LiteralValuesProviderForJava() ;
		}
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Set a specific collection type for the current code generation",
			"The given type will be used as the new default type for the collections "
			},
		example={ 
			"#set ( $env.collectionType = 'java.util.Set' )    ## for Java ",
			"#set ( $env.collectionType = 'java.util.Vector' ) ## for Java ",
			"#set ( $env.collectionType = 'Collection' )       ## for C# ",
			},
		parameters = { 
			"specificCollectionType : the type to be used (full type if any) " 
			},
		since = "3.3.0"
			)
	public void setCollectionType(String specificCollectionType) {
		this.specificCollectionType = specificCollectionType;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the current specific collection type"
			},
		example={ 
			"$env.language" 
			},
		since = "3.3.0"
			)
	public String getCollectionType() {
		return this.specificCollectionType != null ? this.specificCollectionType : "" ;
	}
	
	
	//-------------------------------------------------------------------------------------
	public void setDatabaseTypesMapping(Map<String,String> map) {
		this.databaseTypesMapping = map;
	}
	
//	public String getDatabaseTypesMapping() {
//		int i = 0;
//		StringBuilder sb = new StringBuilder("{\n");
//	    for (String key : databaseTypesMapping.keySet()) {
//	    	if ( i > 0 ) {
//	    		sb.append(", \n");
//	    	}
//	        sb.append(" \"" + key + "\" : \"" + databaseTypesMapping.get(key) + "\"");
//	        i++;
//	    }
//		sb.append("\n}");
//	    return sb.toString();	
//	}

	public Map<String,String> getDatabaseTypesMapping() {
		return databaseTypesMapping;
	}

	//-------------------------------------------------------------------------------------
	public void setDatabase(String db) {
		this.database = db;
	}
	public String getDatabase() {
		return this.database;
	}
	
}
