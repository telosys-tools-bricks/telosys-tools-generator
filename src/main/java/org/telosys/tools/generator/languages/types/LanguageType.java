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
package org.telosys.tools.generator.languages.types;

/**
 * Language type with its simple/basic type and full type if any <br>
 * The simple and full type have the same value if there's no notion of "package", "namespace", etc <br>
 * Examples : <br>
 * For Java language : "String" and "java.lang.String" or "short" and "short" <br>
 * For c# language : "Int16" and "System.Int16"<br>
 * etc <br>
 * 
 * @author Laurent Guerin
 *
 */
public class LanguageType {

	private final String neutralType ;

	private final String simpleType ;

	private final String fullType ;

	private final String wrapperType ;

	private final boolean isPrimitiveType ;

	/**
	 * Constructor
	 * @param neutralType the original neutral type before conversion to language type
	 * @param simpleType the language simple type
	 * @param fullType the language full type
	 * @param isPrimitiveType
	 * @param wrapperType
	 */
	protected LanguageType(String neutralType, String simpleType, String fullType, boolean isPrimitiveType, String wrapperType) {
		super();
		if ( neutralType == null || simpleType == null || fullType == null || wrapperType == null ) {
			throw new IllegalArgumentException("LanguageType constructor : null argument");
		}
		this.neutralType = neutralType;
		this.simpleType = simpleType;
		this.fullType = fullType;
		this.isPrimitiveType = isPrimitiveType;
		this.wrapperType = wrapperType ;
	}

	public boolean isEmpty() {
		return simpleType.isEmpty() && fullType.isEmpty() && wrapperType.isEmpty();
	}
	
	/**
	 * Returns the original neutral type (before conversion to specific language type)
	 * @return
	 */
	public String getNeutralType() {
		return neutralType;
	}
	
	/**
	 * Returns the 'simple type' name <br>
	 * e.g. 'Long' for a Java 'java.lang.Long' type <br>
	 * or 'long' for a Java primitive type (same name for simple and full name ) <br>
	 * @return
	 */
	public String getSimpleType() {
		return simpleType;
	}

	/**
	 * Returns the 'full type' name <br>
	 * e.g. 'java.lang.Long' in Java <br>
	 * or 'long' for a Java primitive type (same name for simple and full name ) <br>
	 * @return
	 */
	public String getFullType() {
		return fullType;
	}

	/**
	 * Returns true if the type is a primitive type (  e.g. 'long' in Java )
	 * @return
	 */
	public boolean isPrimitiveType() {
		return isPrimitiveType;
	}
	
	/**
	 * Returns the wrapper type for current type <br>
	 * e.g. in Java : 'Long' for a 'long' type  <br>
	 * or 'Long' for a 'Long' type
	 * @return
	 */
	public String getWrapperType() {
		return wrapperType ;
	}
	
	@Override
	public String toString() {
		String s = isPrimitiveType() ? "PRIMITIVE-TYPE" : "OBJECT-TYPE" ;
		return "LanguageType : '" + simpleType + "', '"	+ fullType + "' " + s + ", wrapper : " + wrapperType ;
	}
}
