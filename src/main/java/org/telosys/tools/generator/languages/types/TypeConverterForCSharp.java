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
import java.util.LinkedList;
import java.util.List;

import org.telosys.tools.generic.model.types.NeutralType;

/**
 * Type converter for "C#" language
 * 
 * Tableau des types :
 * https://docs.microsoft.com/fr-fr/dotnet/csharp/language-reference/keywords/integral-types-table 
 * 
 * Nullable Types :
 * https://stack247.wordpress.com/2011/04/19/list-of-nullable-types-in-c/ 
 * 
 * @author Laurent Guerin
 *
 */
public class TypeConverterForCSharp extends TypeConverter {

	private final HashMap<String, LanguageType> unsignedTypes = new HashMap<>(); 

	public TypeConverterForCSharp() {
		super("C#");
		
		//--- Object types 
		declareObjectType( buildObjectType(NeutralType.STRING,    "String",   "System.String"  ) );
		declareObjectType( buildObjectType(NeutralType.BOOLEAN,   "Boolean",  "System.Boolean" ) );
		declareObjectType( buildObjectType(NeutralType.BYTE,      "SByte",    "System.SByte"   ) );
		declareObjectType( buildObjectType(NeutralType.SHORT,     "Int16",    "System.Int16"   ) );
		declareObjectType( buildObjectType(NeutralType.INTEGER,   "Int32",    "System.Int32"   ) );
		declareObjectType( buildObjectType(NeutralType.LONG,      "Int64",    "System.Int64"   ) );
		declareObjectType( buildObjectType(NeutralType.FLOAT,     "Single",   "System.Single"  ) );
		declareObjectType( buildObjectType(NeutralType.DOUBLE,    "Double",   "System.Double"  ) );
		declareObjectType( buildObjectType(NeutralType.DECIMAL,   "Decimal",  "System.Decimal" ) );
		declareObjectType( buildObjectType(NeutralType.DATE,      "DateOnly", "System.DateOnly" ) ); // DateOnly : v 4.1.0 ( since .Net 6 )
		declareObjectType( buildObjectType(NeutralType.TIME,      "TimeOnly", "System.TimeOnly" ) ); // TimeOnly : v 4.1.0 ( since .Net 6 )
		declareObjectType( buildObjectType(NeutralType.TIMESTAMP, "DateTime", "System.DateTime" ) );  
		// DateTimeOffset : comming soon..
		// no ObjectType for NeutralType.BINARY

		//--- Primitive types :
		declarePrimitiveType( buildPrimitiveType(NeutralType.STRING,   "string",  "String"  ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.BOOLEAN,  "bool",    "Boolean" ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.BYTE,     "sbyte",   "SByte"   ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.SHORT,    "short",   "Int16"   ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.INTEGER,  "int",     "Int32"   ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.LONG,     "long",    "Int64"   ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.FLOAT,    "float",   "Single"  ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.DOUBLE,   "double",  "Double"  ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.DECIMAL,  "decimal", "Decimal" ) );
		// DATE => No primitive type
		// TIME => No primitive type
		// TIMESTAMP => No primitive type
		declarePrimitiveType( buildPrimitiveType(NeutralType.BINARY, "byte[]", "byte[]" )  ); // No Wrapper type for binary / byte[] ?
		
		//--- Unsigned primitive types : 
		unsignedTypes.put( "sbyte", buildPrimitiveType(NeutralType.BYTE,    "byte",   "Byte"   ) );
		unsignedTypes.put( "short", buildPrimitiveType(NeutralType.SHORT,   "ushort", "UInt16" ) );
		unsignedTypes.put( "int",   buildPrimitiveType(NeutralType.INTEGER, "uint",   "UInt32" ) );
		unsignedTypes.put( "long",  buildPrimitiveType(NeutralType.LONG,    "ulong",  "UInt64" ) );

		//--- Unsigned object types : 
		unsignedTypes.put( "SByte", buildObjectType(NeutralType.BYTE,    "Byte",    "System.Byte"   ) );
		unsignedTypes.put( "Int16", buildObjectType(NeutralType.SHORT,   "UInt16",  "System.UInt16" ) );
		unsignedTypes.put( "Int32", buildObjectType(NeutralType.INTEGER, "UInt32",  "System.UInt32" ) );
		unsignedTypes.put( "Int64", buildObjectType(NeutralType.LONG,    "UInt64",  "System.UInt64" ) );
	}

	private LanguageType buildPrimitiveType(String neutralType, String primitiveType, String wrapperType) {
		return new LanguageType(neutralType, primitiveType,  primitiveType, true, wrapperType );
	}

	private LanguageType buildObjectType(String neutralType, String simpleType, String fullType) {
		return new LanguageType(neutralType, simpleType,  fullType, false, simpleType ); // wrapper type = simple type
	}
	
	@Override
	public List<String> getComments() {
		List<String> l = new LinkedList<>();
		l.add("'@UnsignedType'  has effect only for byte, short, int, long ");
		l.add("'@ObjectType'  switches to .Net types ( System.Int64, System.Boolean, etc) ");
		l.add("");
		l.add("'@NotNull'  has no effect, use '$csharp' object for 'nullable type' with '?' ");
		l.add("'@PrimitiveType'  has no effect ");
		return l ;
	}

	@Override
	public LanguageType getType(AttributeTypeInfo attributeTypeInfo) {		
		// Get the standard type 
		LanguageType languageType = getStandardType(attributeTypeInfo);
		
		// An object type is explicitly required ( @ObjectType )
		if ( attributeTypeInfo.isObjectTypeExpected() ) {
			languageType = getObjectType(languageType );
		}
		
		// An unsigned type is explicitly required ( @UnsignedType )
		if ( attributeTypeInfo.isUnsignedTypeExpected() ) {
			languageType = getUnsignedType(languageType);
		}

		// If attribute is 'nullable' and '$env.typeWithNullableMark' is TRUE
		if ( ( nullableMarkCanBeUsed(attributeTypeInfo) ) ) {
			// Nullable => nullable type with '?' at the end
			languageType = getNullableType(languageType);
		}

		// Return resulting type
		return languageType;
	}
	
	private LanguageType getStandardType(AttributeTypeInfo attributeTypeInfo) {
		// Try to get primitive type 
		LanguageType lt = getPrimitiveType(attributeTypeInfo.getNeutralType(), false ) ;
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

	/**
	 * Try to get an object type for the given current type (return same current type if no object type)
	 * @param currentType
	 * @return
	 */
	private LanguageType getObjectType(LanguageType currentType ) {
		LanguageType objectType = getObjectType(currentType.getNeutralType() ) ;
		if ( objectType != null ) {
			return objectType ; // FOUND
		}
		else {
			return currentType; // Not found : keep current type
		}
	}
	/**
	 * Try to get an unsigned type for the given current type (return same current type if no object type)
	 * @param currentType
	 * @return
	 */
	private LanguageType getUnsignedType(LanguageType currentType ) {
		LanguageType unsignedType = unsignedTypes.get( currentType.getSimpleType() );
		if ( unsignedType != null ) {
			return unsignedType ; // FOUND
		}
		else {
			return currentType; // Not found : keep current type
		}
	}
	
	private static final String NULLABLE_MARK = "?";
	private LanguageType getNullableType(LanguageType currentType ) {
		return new LanguageType(
				currentType.getNeutralType(),
				currentType.getSimpleType() + NULLABLE_MARK,
				currentType.getFullType() + NULLABLE_MARK,
				currentType.isPrimitiveType(),
				currentType.getWrapperType() + NULLABLE_MARK);
	}
	
	//--------------------------------------------------------------------------------------------
	// Collection type ( since v 3.3.0 )
	//--------------------------------------------------------------------------------------------	
	// Collections for C# :
	//  - List<T>
	//  - Collection<T>
	private static final String STANDARD_COLLECTION_TYPE = "List" ; // or "Collection" ?
	
	@Override
	public String getCollectionType() {
		return determineCollectionTypeToUse(STANDARD_COLLECTION_TYPE) ; 
	}

	@Override
	public String getCollectionType(String elementType) {
		return determineCollectionTypeToUse(STANDARD_COLLECTION_TYPE)  + "<" + elementType + ">" ; 
	}
	
}
