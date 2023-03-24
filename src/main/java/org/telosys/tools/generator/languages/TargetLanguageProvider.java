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

import org.telosys.tools.generator.languages.literals.LiteralValuesProvider;
import org.telosys.tools.generator.languages.types.TypeConverter;

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
	private static String languageKey(String languageName) {
		return languageName.toUpperCase().trim();
	}

	/**
	 * Returns true if the given language is known and defined in Telosys
	 * @param languageName
	 * @return
	 */
	public static boolean isDefinedLanguage(String languageName) {
		return languages.get(languageKey(languageName)) != null ;
	}
	
	/**
	 * Returns the TargetLanguage for the given language name 
	 * Java TargetLanguage is the default language if the given name is unknown 
	 * @param languageName
	 * @return
	 */
	public static TargetLanguage getTargetLanguage(String languageName) {
		if (languageName != null) {
			TargetLanguage targetLanguage = languages.get( languageKey(languageName) );
			if ( targetLanguage == null ) {
				// by default use JAVA
				targetLanguage = languages.get( "JAVA" );
			}
			return targetLanguage ;
		}
		else {
			throw new IllegalArgumentException("getTargetLanguage(..) : language name is null") ;
		}
	}

	/**
	 * Returns the TypeConverter for the given language name (Java by default)
	 * @param languageName
	 * @return
	 */
	public static TypeConverter getTypeConverter(String languageName) {
		return getTargetLanguage(languageName).getTypeConverter();
	}
	
	/**
	 * Returns the LiteralValuesProvider for the given language name (Java by default)
	 * @param languageName
	 * @return
	 */
	public static LiteralValuesProvider getLiteralValuesProvider(String languageName) {
		return getTargetLanguage(languageName).getLiteralValuesProvider();
	}

}
