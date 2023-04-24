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

import java.util.LinkedList;
import java.util.List;

import org.telosys.tools.generic.model.types.NeutralType;

/**
 * Type converter for "PHP" language
 * 
 * @author Laurent Guerin
 *
 */
public class TypeConverterForPHP extends TypeConverter {

	private static final String PHP_STRING = "string" ;
	private static final String PHP_BOOL   = "bool" ;
	private static final String PHP_INT    = "int" ;
	private static final String PHP_FLOAT  = "float" ;

	public TypeConverterForPHP() {
		super("PHP");
		//--- PHP types added in ver 4.1.0 :
		//--- PHP types added in ver 4.1.0 :
		declarePrimitiveType( buildPrimitiveType(NeutralType.STRING,  PHP_STRING ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.BOOLEAN, PHP_BOOL   ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.BYTE,    PHP_INT) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.SHORT,   PHP_INT) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.INTEGER, PHP_INT) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.LONG,    PHP_INT ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.FLOAT,   PHP_FLOAT ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.DOUBLE,  PHP_FLOAT ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.DECIMAL, PHP_FLOAT ) );
		
		declareObjectType( buildObjectType(NeutralType.DATE,      "", "" ) ); 
		declareObjectType( buildObjectType(NeutralType.TIME,      "", "" ) );
		declareObjectType( buildObjectType(NeutralType.TIMESTAMP, "DateTime", "\\DateTime" ) ); // backslash is "global namespace" 
		declareObjectType( buildObjectType(NeutralType.BINARY,    "", "" ) ); 		
	}
	
	private LanguageType buildPrimitiveType(String neutralType, String primitiveType)  {
		return new LanguageType(neutralType, 
				primitiveType,   // String simpleType, 
				primitiveType,   // String fullType, 
				true, // boolean isPrimitiveType, 
				primitiveType ); // String wrapperType
	}

	private LanguageType buildObjectType(String neutralType, String simpleType, String fullType)  {
		return new LanguageType(neutralType, 
				simpleType,   // String simpleType, 
				fullType,   // String fullType, 
				false, // boolean isPrimitiveType, 
				simpleType ); // String wrapperType
	}

	@Override
	public List<String> getComments() {
		List<String> l = new LinkedList<>();
		l.add("Typed class properties have been added in PHP 7.4");
		l.add("Telosys returns the type usable for class properties");
		return l ;
	}
	
	@Override
	public LanguageType getType(AttributeTypeInfo attributeTypeInfo) {
		// Search a "primitive type" first 
		LanguageType languageType = getPrimitiveType(attributeTypeInfo.getNeutralType() ) ; 
		if ( languageType != null ) { // FOUND
			return languageType ;
		}
		else {
			// if "primitive type" not found search an "object type" : date, timestamp
			languageType = getObjectType(attributeTypeInfo.getNeutralType() ) ;
			if ( languageType != null ) { // FOUND
				return languageType ;
			}
		}
		throw new TelosysTypeNotFoundException(getLanguageName(), attributeTypeInfo);
	}
	
	//--------------------------------------------------------------------------------------------
	// Collection type ( since v 3.3.0 )
	//--------------------------------------------------------------------------------------------
	// NOT APPLICABLE FOR 'PHP' :
	// 
	private static final String STANDARD_COLLECTION_SIMPLE_TYPE = "" ; 
	private static final String STANDARD_COLLECTION_FULL_TYPE   = "" ; 
//	@Override
//	public void setSpecificCollectionType(String specificCollectionType) {
//		this.setSpecificCollectionFullType(specificCollectionType) ;
//		this.setSpecificCollectionSimpleType(specificCollectionType);
//	}

	@Override
	public String getCollectionType(String elementType) {
		return getCollectionSimpleType() ;
	}
	
	@Override
	public String getCollectionSimpleType() {
		return getCollectionSimpleType(STANDARD_COLLECTION_SIMPLE_TYPE);
	}

	@Override
	public String getCollectionFullType() {
		return getCollectionFullType(STANDARD_COLLECTION_FULL_TYPE);
	}
}
