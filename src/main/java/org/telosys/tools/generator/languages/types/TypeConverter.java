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

import java.util.HashMap;
import java.util.List;

import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.commons.logger.ConsoleLogger;
import org.telosys.tools.generator.context.EnvInContext;
import org.telosys.tools.generic.model.Attribute;

/**
 * Abstract type converter <br>
 * Each implementation converts a "neutral type" to a specific "language type" 
 *  
 * @author Laurent GUERIN
 *
 */
public abstract class TypeConverter {
	
	private static final boolean LOG = false ;
	private static final ConsoleLogger logger = new ConsoleLogger();
	protected void log(String msg) {
		if ( LOG ) {
			logger.log(this, msg);
		}
	}

	private final String languageName ;
	private EnvInContext env ;
	
	private final HashMap<String, LanguageType> primitiveTypes         = new HashMap<>();
	private final HashMap<String, LanguageType> primitiveUnsignedTypes = new HashMap<>();
	
	private final HashMap<String, LanguageType> objectTypes         = new HashMap<>();
	
//	private String specificCollectionFullType   = null ;
//	private String specificCollectionSimpleType = null ;
	
	/**
	 * Constructor
	 * @param languageName
	 */
	protected TypeConverter(String languageName) {
		super();
		this.languageName = languageName;
	}
	
	public void setEnv(EnvInContext env) {
		this.env = env;
	}
	protected EnvInContext getEnv() {
		return env;
	}

//	//--- Standard/Specific COLLECTION SIMPLE TYPE
//	protected void setSpecificCollectionSimpleType(String type) {
//		specificCollectionSimpleType = type;
//	}
//	protected String getCollectionSimpleType(String standardCollectionSimpleType) {
//		String specificCollectionType = getEnv().getCollectionType(); 
//		if ( StrUtil.nullOrVoid(specificCollectionType) ) {
//			return specificCollectionType ; 
//		}
//		else {
//			return standardCollectionSimpleType ; 
//		}
//	}
//	//--- Standard/Specific COLLECTION FULL TYPE
//	protected void setSpecificCollectionFullType(String type) {
//		specificCollectionFullType = type;
//	}
//	protected String getCollectionFullType(String standardCollectionFullType) {
//		if ( specificCollectionFullType != null ) {
//			return specificCollectionFullType ; 
//		}
//		else {
//			return standardType ; 
//		}
//	}

	/**
	 * Returns the language associated with this type converter
	 * @return
	 */
	public String getLanguageName() {
		return languageName;
	}
	
	/**
	 * Returns a list of comment lines
	 * @return
	 */
	public abstract List<String> getComments() ;
	

	/**
	 * Declares a regular primitive type
	 * @param languageType
	 */
	protected void declarePrimitiveType(LanguageType languageType) {
		primitiveTypes.put(languageType.getNeutralType(), languageType);
	}
	
	/**
	 * Declares an unsigned primitive type
	 * @param languageType
	 */
	protected void declarePrimitiveUnsignedType(LanguageType languageType) {
		primitiveUnsignedTypes.put(languageType.getNeutralType(), languageType);
	}
	
	/**
	 * Declares a regular object type
	 * @param languageType
	 */
	protected void declareObjectType(LanguageType languageType) {
		objectTypes.put(languageType.getNeutralType(), languageType);
	}
	
	//--------------------------------------------------------------------------------------------
	// Get "PRIMITIVE TYPE" with or without "unsigned" option
	//--------------------------------------------------------------------------------------------
	/**
	 * Returns a primitive type for the given neutral type (or null if none)
	 * @param neutralType
	 * @return
	 */
	protected LanguageType getPrimitiveType(String neutralType ) {
		// Try to get a regular primitive type
		return primitiveTypes.get(neutralType);
	}
	
	/**
	 * Returns a primitive type for the given neutral type (or null if none)
	 * @param neutralType
	 * @param isUnsignedTypeExpected
	 * @return
	 */
	protected LanguageType getPrimitiveType(String neutralType, boolean isUnsignedTypeExpected ) {
		if ( isUnsignedTypeExpected ) {
			// Try to get an unsigned primitive type
			LanguageType lt = primitiveUnsignedTypes.get(neutralType);
			if ( lt != null ) {
				// unsigned type FOUND
				return lt ;
			}
			else {
				// unsigned type NOT FOUND => try to get a regular primitive type
				return primitiveTypes.get(neutralType);
			}
		}
		else {
			// Try to get a regular primitive type
			return primitiveTypes.get(neutralType);
		}
	}
	
	//--------------------------------------------------------------------------------------------
	// Get "OBJECT TYPE" with or without "SQL TYPE" option
	//--------------------------------------------------------------------------------------------
	/**
	 * Returns an object type for the given neutral type (or null if none)
	 * @param neutralType
	 * @return
	 */
	protected LanguageType getObjectType(String neutralType ) {
		// Try to get a regular object type
		return objectTypes.get(neutralType);
	}

	//--------------------------------------------------------------------------------------------
	// Get type for attribute or attribute info 
	//--------------------------------------------------------------------------------------------
	/**
	 * Returns the LanguageType that suits as well as possible with the given type information
	 * @param attributeTypeInfo
	 * @return 
	 */
	public abstract LanguageType getType(AttributeTypeInfo attributeTypeInfo) ;
	
	/**
	 * Returns the LanguageType that suits as well as possible with the given attribute's characteristics
	 * @param attribute
	 * @return
	 */
	public final LanguageType getType(Attribute attribute) {		
		AttributeTypeInfo attributeTypeInfo = new AttributeTypeInfoImpl(attribute);
		return getType(attributeTypeInfo);
	}
	
	//--------------------------------------------------------------------------------------------
	// Collection type ( since v 3.3.0 )
	//--------------------------------------------------------------------------------------------
	
//	/**
//	 * Force a specific collection type (instead of standard type used by default)
//	 * @param specificCollectionType
//	 */
//	public abstract void setSpecificCollectionType(String specificCollectionType);

	/**
	 * Returns the active collection type (standard or specific) <br>
	 * Example for Java : returns "List" or any other specific type like "Set", "Collection", etc
	 * @return
	 */
	public abstract String getCollectionType() ;

	/**
	 * Returns the type for a collection of the given element <br>
	 * For example : returns "List<Book>" for a "Book" element in Java
	 * @param elementType
	 * @return
	 */
	public abstract String getCollectionType(String elementType) ;
	
	/**
	 * Determines what type of collection should be used : <br>
	 * standard type (default) or specific type set via $env (if any) 
	 * @param standardCollectionType default standard type
	 * @return
	 */
	protected String determineCollectionTypeToUse(String standardCollectionType) {
		String specificCollectionType = getSpecificCollectionType();
		if ( ! StrUtil.nullOrVoid( specificCollectionType ) ) {
			return specificCollectionType ;
		}
		else {
			return standardCollectionType ;
		}
	}

	/**
	 * Returns the specific collection type as set via $env object
	 * @return
	 */
	protected String getSpecificCollectionType() {
		String specificCollectionType = getEnv().getCollectionType(); 
		if ( StrUtil.nullOrVoid(specificCollectionType) ) {
			return null ; 
		}
		else {
			return specificCollectionType ; 
		}
	}
	
	/**
	 * Returns TRUE if a nullable mark ('?' at the end/beginning of the type) can be used <br>
	 * TRUE if the attribute is 'nullable' and the 'nullableMark' flag is active in $env 
	 * @param attributeTypeInfo
	 * @return
	 */
	protected boolean nullableMarkCanBeUsed(AttributeTypeInfo attributeTypeInfo) {
		// If attribute is nullable and 'typeWithNullableMark' env property is TRUE
		if ( ( ! attributeTypeInfo.isNotNull() ) && ( getEnv().getTypeWithNullableMark() == true ) ) {
			// Nullable => nullable type with '?' at the end
			return true;
		}
		else {
			return false;
		}
	}
	
//	/**
//	 * Returns the 'simple type' used by default for a collection <br>
//	 * For example : returns "List" in Java
//	 * @return
//	 */
//	public abstract String getCollectionSimpleType() ;
//
//	/**
//	 * Returns the 'full type' used by default for a collection <br>
//	 * For example : returns "java.util.List" in Java
//	 * @return
//	 */
//	public abstract String getCollectionFullType() ;
	
}
