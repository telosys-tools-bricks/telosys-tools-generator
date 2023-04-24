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

import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.commons.exception.TelosysRuntimeException;
import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.names.ContextName;
import org.telosys.tools.generator.context.tools.SqlInContextBuilder;
import org.telosys.tools.generator.languages.TargetLanguage;
import org.telosys.tools.generator.languages.TargetLanguageProvider;
import org.telosys.tools.generator.languages.literals.LiteralValuesProvider;
import org.telosys.tools.generator.languages.types.TypeConverter;

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
	
	private String entityClassNamePrefix = "" ;
	private String entityClassNameSuffix = "" ;
	
	private String language = "Java" ; // v 3.0.0
	
	private String specificCollectionType = null ; // v 3.3.0

	private String database = "" ; // v 3.4.0
	private File   databaseConvFile = null ;  // v 3.4.0
	private SqlInContext sqlInContext = null;   // v 3.4.0
	
	private boolean typeWithNullableMark = true ; // v 4.1.0 
	
	//-------------------------------------------------------------------------------------
	// CONSTRUCTOR
	//-------------------------------------------------------------------------------------
	public EnvInContext() {
		super();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"'entityClassNamePrefix' property defines the prefix to add before entity class name",
			"Once the 'prefix' is defined in the 'environment object' ",
			"it is automatically applied when the entity class name is retrieved"
			},
		example={ 
			"#set ( $env.entityClassNamePrefix = 'Bean' )" 
			},
		since = "2.1.0"
			)
	public void setEntityClassNamePrefix( String prefix ) {
		this.entityClassNamePrefix = prefix ;
	}
	public String getEntityClassNamePrefix() {
		return this.entityClassNamePrefix;
	}
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"'entityClassNameSuffix' property defines the suffix to add after entity class name",
			"Once the 'suffix' is defined in the 'environment object' ",
			"it is automatically applied when the entity class name is retrieved"
			},
		example={ 
			"#set ( $env.entityClassNameSuffix = 'Entity' )" 
			},
		since = "2.1.0"
			)
	public void setEntityClassNameSuffix( String suffix ) {
		this.entityClassNameSuffix = suffix ;
	}
	public String getEntityClassNameSuffix() {
		return this.entityClassNameSuffix;
	}
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"'language' property defines the target language for the current code generation",
			"See documentation for all supported languages ('Java', 'C#', 'Go', 'Python', 'JavaScript', 'TypeScript', etc) ",
			"The current language is 'Java' by default ",
			"This information is used determine language peculiarities like types and literal values",
			"(throws an exception if the language is unknown)"
			},
		example={ 
			"#set ( $env.language = 'C#' ) ",
			"#set ( $env.language = 'PHP' ) ",
			"Current target language is $env.language"
			},
		since = "3.0.0"
			)
	public void setLanguage( String language ) throws GeneratorException {
		if ( TargetLanguageProvider.isDefinedLanguage(language) ) {
			this.language = language ;
		}
		else {
			// Unknown language
			throw new GeneratorException("Unknown language '" + language + "'");			
		}
	}
	public String getLanguage() {
		return language;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"'collectionType' property defines a specific collection type for the current code generation",
			"The given type will be used as the new default type for collections "
			},
		example={ 
			"#set ( $env.collectionType = 'java.util.Set' )    ## for Java ",
			"#set ( $env.collectionType = 'java.util.Vector' ) ## for Java ",
			"#set ( $env.collectionType = 'Collection' )       ## for C# ",
			"Current collection type is $env.collectionType"
			},
		since = "3.3.0"
			)
	public void setCollectionType(String specificCollectionType) {
		this.specificCollectionType = specificCollectionType;
	}
	public String getCollectionType() {
		return this.specificCollectionType != null ? this.specificCollectionType : "" ;
	}
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"'database' property defines the target database for SQL generation",
			"The given database name will be used for SQL conversions (types and names)",
			"(throws an exception if the database is unknown)"
			},
		example={ 
			"#set ( $env.database = 'postgresql' )  ",
			"#set ( $env.database = 'mysql' )  ",
			},
		since = "3.4.0"
			)
	public void setDatabase(String dbName) {
		SqlInContextBuilder.checkDbName(dbName);
		this.database = dbName;
		this.sqlInContext = null; // Reset 
	}
	public String getDatabase() {
		return this.database;
	}	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"'databaseConvFile' property defines the file containing the specific rules to be used for SQL generation",
			"The given rules will be used for SQL conversions (types and names)",
			"(throws an exception if the file cannot be found)"
			},
		example={ 
			"#set ( $env.databaseConvFile = $fn.fileFromBundle('oracle.properties') )  ",
			"#set ( $env.databaseConvFile = $fn.fileFromBundle('mydb.properties') )  ",
			},
		since = "3.4.0"
			)
	public void setDatabaseConvFile(FileInContext fileInContext) {
		File dbFile = fileInContext.getFile();
		SqlInContextBuilder.checkDbFile(dbFile);
		this.databaseConvFile = dbFile ;
		this.sqlInContext = null; // Reset 
	}
	public File getDatabaseConvFile() {
		return this.databaseConvFile;
	}
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"'typeWithNullableMark' property defines if the 'nullable mark' must be used or not",
			"This property is useful only for target languages like C#, Kotlin or PHP.", 
			"With this kind of languages a '?' is added at the end the type (eg C# : 'string?')",
			"or at the beginning (eg PHP: '?string') if the attribute is nullable",
			"The default value is 'true', it can be set to 'false' to avoid the 'nullable mark' in the type"
			},
		example={ 
			"#set ( $env.typeWithNullableMark = false ) ",
			"Use type with nullable mark : $env.typeWithNullableMark"
			},
		since = "4.1.0"
			)
	public void setTypeWithNullableMark(boolean v) {
		this.typeWithNullableMark = v;
	}
	public boolean getTypeWithNullableMark() {
		return this.typeWithNullableMark ;
	}
	//-------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------
	/**
	 * Returns the TargetLanguage for the current language defined in '$env'
	 * @return the TargetLanguage (never null)
	 */
	public TargetLanguage getTargetLanguage() { // v 4.1.0 
//		TargetLanguage targetLanguage = TargetLanguageProvider.getTargetLanguage(this.language);
//		if ( targetLanguage == null ) {
//			// by default use JAVA
//			targetLanguage = TargetLanguageProvider.getTargetLanguage("JAVA");
//			if ( targetLanguage == null ) {
//				// cannot happen
//				throw new TelosysRuntimeException("Cannot get TargetLanguage (even with default language name)");
//			}
//		}
//		return targetLanguage ;
		return TargetLanguageProvider.getTargetLanguage(this); // v 4.1.0
	}

	/**
	 * Returns the TypeConverter corresponding to the current language <br>
	 * and with the specific collection type if any
	 * @return
	 * @since ver 3.0.0
	 */
	public TypeConverter getTypeConverter() { // keep 'public' for debug in '.vm' files
//		TypeConverter typeConverter = getTargetLanguage().getTypeConverter(); // v 4.1.0
//		// set specific collection type if any 
//		if ( specificCollectionType != null ) {
//			typeConverter.setSpecificCollectionType(specificCollectionType);
//		}
//		return typeConverter;		
		return getTargetLanguage().getTypeConverter(); // v 4.1.0
	}
	
	/**
	 * Returns the LiteralValuesProvider for the current language
	 * @return
	 * @since ver 3.0.0
	 */
	public LiteralValuesProvider getLiteralValuesProvider()  { // keep 'public' for debug in '.vm' files
		return getTargetLanguage().getLiteralValuesProvider(); // v 4.1.0
	}
	
	
	public SqlInContext getSql() {
		// the current "sql" is reset whenever the database name/file changes
		if ( this.sqlInContext == null ) {
			this.sqlInContext = buildSqlInContext();
		}
		return this.sqlInContext;
    }
	
	private SqlInContext buildSqlInContext() {
		if ( this.databaseConvFile != null ) {
			// a specific db file has been defined => use it first
			return SqlInContextBuilder.buildFromDbFile(this.databaseConvFile);
		}
		else if ( ! StrUtil.nullOrVoid(this.database) ) {
			return SqlInContextBuilder.buildFromDbName(this.database);
		}
		else {
			return SqlInContextBuilder.buildDefault();
		}
	}
}
