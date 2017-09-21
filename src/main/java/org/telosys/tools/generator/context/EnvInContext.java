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

import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.names.ContextName;
import org.telosys.tools.generic.model.types.LiteralValuesProvider;
import org.telosys.tools.generic.model.types.LiteralValuesProviderForCSharp;
import org.telosys.tools.generic.model.types.LiteralValuesProviderForJava;
import org.telosys.tools.generic.model.types.LiteralValuesProviderForJavaScript;
import org.telosys.tools.generic.model.types.LiteralValuesProviderForPython;
import org.telosys.tools.generic.model.types.LiteralValuesProviderForTypeScript;
import org.telosys.tools.generic.model.types.TypeConverter;
import org.telosys.tools.generic.model.types.TypeConverterForCSharp;
import org.telosys.tools.generic.model.types.TypeConverterForJava;
import org.telosys.tools.generic.model.types.TypeConverterForJavaScript;
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
	
	private final static String JAVA       = "JAVA" ;
	private final static String CSHARP     = "C#" ;
	private final static String TYPESCRIPT = "TYPESCRIPT" ;
	private final static String JAVASCRIPT = "JAVASCRIPT" ;
	private final static String PYTHON     = "PYTHON" ;
	
	private String _entityClassNamePrefix = "" ;
	private String _entityClassNameSuffix = "" ;
	
	private String _language = "Java" ; // v 3.0.0
	
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
		_entityClassNamePrefix = prefix ;
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
		return _entityClassNamePrefix;
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
		_entityClassNameSuffix = suffix ;
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
		return _entityClassNameSuffix;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Set the language for the current code generation",
			"Supported languages are 'Java', 'C#', 'Python', 'JavaScript', 'TypeScript',  ",
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
		_language = language ;
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
		return _language;
	}
	
	//-------------------------------------------------------------------------------------
	protected void checkLanguageValidity(String language) throws GeneratorException {
		String languageUC = language.toUpperCase() ;
		if ( JAVA.equals(languageUC) ) return ;
		if ( CSHARP.equals(languageUC) ) return ;
		if ( TYPESCRIPT.equals(languageUC) ) return ;
		if ( JAVASCRIPT.equals(languageUC) ) return ;
		if ( PYTHON.equals(languageUC) ) return ;
		// Unknown language
		throw new GeneratorException("Unknown language '" + language + "'");
	}
	//-------------------------------------------------------------------------------------
	/**
	 * Returns the TypeConverter corresponding to the current language
	 * @return
	 * @since ver 3.0.0
	 */
	protected TypeConverter getTypeConverter()  {
		String languageUC = this._language.toUpperCase() ;
		if ( JAVA.equals(languageUC) ) {
			return new TypeConverterForJava() ;
		}
		else if ( CSHARP.equals(languageUC) ) {
			return new TypeConverterForCSharp() ; 
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
	protected LiteralValuesProvider getLiteralValuesProvider()  {
		String languageUC = this._language.toUpperCase() ;
		if ( JAVA.equals(languageUC) ) {
			return new LiteralValuesProviderForJava() ;
		}
		else if ( CSHARP.equals(languageUC) ) {
			return new LiteralValuesProviderForCSharp() ;
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
		else {
			// By default : Java  ( not supposed to happen ) 
			return new LiteralValuesProviderForJava() ;
		}
	}
}
