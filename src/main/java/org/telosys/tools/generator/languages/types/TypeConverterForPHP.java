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
		declarePrimitiveType( buildPrimitiveType(NeutralType.STRING,  PHP_STRING ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.BOOLEAN, PHP_BOOL   ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.BYTE,    PHP_INT) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.SHORT,   PHP_INT) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.INTEGER, PHP_INT) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.LONG,    PHP_INT ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.FLOAT,   PHP_FLOAT ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.DOUBLE,  PHP_FLOAT ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.DECIMAL, PHP_FLOAT ) );
		
		declareObjectType( buildObjectType(NeutralType.DATE,      "DateTime", "DateTime" ) ); 
		declareObjectType( buildObjectType(NeutralType.TIME,      "DateTime", "DateTime" ) );
		declareObjectType( buildObjectType(NeutralType.TIMESTAMP, "DateTime", "DateTime" ) );  
		
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
		// Get the standard type 
		LanguageType languageType = getStandardType(attributeTypeInfo);
		
		// No wrapper object type in PHP ( @ObjectType )
		// No unsigned type in PHP( @UnsignedType )
		
		// If attribute is 'nullable' and '$env.typeWithNullableMark' is TRUE
		if ( ( nullableMarkCanBeUsed(attributeTypeInfo) ) ) {
			// Nullable => nullable type with '?' at the beginning
			languageType = getNullableType(languageType);
		}

		// Return resulting type
		return languageType;

	}

	private LanguageType getStandardType(AttributeTypeInfo attributeTypeInfo) {
		// Try to get primitive type 
		LanguageType lt = getPrimitiveType(attributeTypeInfo.getNeutralType() ) ;
		if ( lt != null ) {
			return lt;
		}
		// Try to get object type 
		lt = getObjectType(attributeTypeInfo.getNeutralType() ) ;
		if ( lt != null ) {
			return lt;
		}
		// Still not found => ERROR !!!
		throw new TelosysTypeNotFoundException(getLanguageName(), attributeTypeInfo);
	}
	private static final String NULLABLE_MARK = "?";
	private LanguageType getNullableType(LanguageType currentType ) {
		if ( currentType.isEmpty() ) {
			// empty type => keep it void
			return currentType;
		}
		else {
			// not empty type => add '?'
			return new LanguageType(
					currentType.getNeutralType(),
					NULLABLE_MARK + currentType.getSimpleType(),
					NULLABLE_MARK + currentType.getFullType(),
					currentType.isPrimitiveType(),
					NULLABLE_MARK + currentType.getWrapperType());
		}
	}
	
	//--------------------------------------------------------------------------------------------
	// Collection type ( since v 3.3.0 )
	//--------------------------------------------------------------------------------------------
	// NOT APPLICABLE FOR 'PHP' :
	@Override
	public String getCollectionType() {
		return "" ;
	}
	@Override
	public String getCollectionType(String elementType) {
		return "" ;
	}
}
