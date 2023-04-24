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
package org.telosys.tools.generator.languages;

import java.util.HashMap;

import org.telosys.tools.commons.exception.TelosysRuntimeException;
import org.telosys.tools.generator.context.EnvInContext;

/**
 *  
 * @author Laurent GUERIN
 *
 */
public final class TargetLanguageProvider {
	
	private static final HashMap<String, TargetLanguage> languages = new HashMap<>();
	
	static {
		languages.put("C++",        new TargetLanguageForCPlusPlus() );
		languages.put("C#",         new TargetLanguageForCSharp() );
		languages.put("GO",         new TargetLanguageForGo() );
		languages.put("JAVA",       new TargetLanguageForJava() );
		languages.put("JAVASCRIPT", new TargetLanguageForJavaScript() );
		languages.put("KOTLIN",     new TargetLanguageForKotlin() );
		languages.put("PHP",        new TargetLanguageForPHP() );
		languages.put("PYTHON",     new TargetLanguageForPython() );
		languages.put("SCALA",      new TargetLanguageForScala() );
		languages.put("TYPESCRIPT", new TargetLanguageForTypeScript() );
	}
	
	/**
	 * Private constructor
	 */
	private TargetLanguageProvider() {
	}

	/** 
	 * Return the unique key for the given langauge name
	 * @param languageName
	 * @return
	 */
	private static String getLanguageKey(String languageName) {
		return languageName.toUpperCase().trim();
	}

	/**
	 * Returns true if the given language is known and defined in Telosys
	 * @param languageName
	 * @return
	 */
	public static boolean isDefinedLanguage(String languageName) {
		return languages.get(getLanguageKey(languageName)) != null ;
	}
	
	/**
	 * Returns the TargetLanguage for the language define in the given environment 
	 * Java TargetLanguage is the default language if the given name is unknown 
	 * @param languageName
	 * @return
	 */
	public static TargetLanguage getTargetLanguage(EnvInContext env) {
		if (env != null) {
			TargetLanguage targetLanguage = getTargetLanguage(env.getLanguage()) ;
			targetLanguage.setEnv(env); // Inject current "env" 
			return targetLanguage;
		}
		else {
			throw new TelosysRuntimeException("Cannot get target language : env is null") ;
		}
	}

	/**
	 * Returns the TargetLanguage for the given language name 
	 * Java is the default language if the given name is null or void
	 * @param languageName
	 * @return the target language (never null, exception if unknown language)
	 */
	private static TargetLanguage getTargetLanguage(String languageName) {
		if (languageName != null) {
			String languageKey = getLanguageKey(languageName);
			if ( languageKey.length() > 0 ) {
				TargetLanguage targetLanguage = languages.get( languageKey );
				if ( targetLanguage != null ) {
					// Target language found 
					return targetLanguage ;
				}
				else {
					throw new TelosysRuntimeException("Unknown target language : '" + languageKey + "'") ;
				}
			}
			else {
				// Language is blanc or void 
				return getDefaultTargetLanguage();
			}
		}
		else {
			// Language is null 
			return getDefaultTargetLanguage();
		}
	}
	
	private static TargetLanguage getDefaultTargetLanguage() {
		TargetLanguage targetLanguage = languages.get( "JAVA" );
		if ( targetLanguage != null ) {
			return targetLanguage;
		}
		else {
			throw new TelosysRuntimeException("Cannot get default target language (JAVA)") ;
		}
	}
//	/**
//	 * Returns the TypeConverter for the given language name (Java by default)
//	 * @param languageName
//	 * @return
//	 */
//	public static TypeConverter getTypeConverter(String languageName) {
//		return getTargetLanguage(languageName).getTypeConverter();
//	}
//	
//	/**
//	 * Returns the LiteralValuesProvider for the given language name (Java by default)
//	 * @param languageName
//	 * @return
//	 */
//	public static LiteralValuesProvider getLiteralValuesProvider(String languageName) {
//		return getTargetLanguage(languageName).getLiteralValuesProvider();
//	}

}
