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
		declarePrimitiveUnsignedType( buildPrimitiveType(NeutralType.BYTE,    "byte",   "Byte"   ) );
		declarePrimitiveUnsignedType( buildPrimitiveType(NeutralType.SHORT,   "ushort", "UInt16" ) );
		declarePrimitiveUnsignedType( buildPrimitiveType(NeutralType.INTEGER, "uint",   "UInt32" ) );
		declarePrimitiveUnsignedType( buildPrimitiveType(NeutralType.LONG,    "ulong",  "UInt64" ) );
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
		
		log("type info : " + attributeTypeInfo );
		
		log("STEP 1" );
		//--- 1) Process explicit requirements first (if any)
		// An object type is explicitly required ( @ObjectType )
		if ( attributeTypeInfo.isObjectTypeExpected() ) {
			LanguageType lt = getObjectType(attributeTypeInfo.getNeutralType() ) ;
			if ( lt != null ) {
				// FOUND
				log("1) object type found" );
				return lt ;
			}
		}
		log("1) object type not found" );

		// An unsigned type is explicitly required ( @UnsignedType )
		if ( attributeTypeInfo.isUnsignedTypeExpected() ) {
			LanguageType lt = getPrimitiveType(attributeTypeInfo.getNeutralType(), true ) ;
			if ( lt != null ) {
				// FOUND
				log("1) primitive type found" );
				return lt ;
			}
		}
		log("1) primitive type not found" );

		log("STEP 2 " );
		//--- 2) Process standard type conversion
		// return the standard "pseudo primitive type" : string, short, int, bool, float, ...
		LanguageType lt = getPrimitiveType(attributeTypeInfo.getNeutralType(), false ) ;
		if ( lt != null ) {
			return lt;
		}
		lt = getObjectType(attributeTypeInfo.getNeutralType() ) ;
		if ( lt != null ) {
			return lt;
		}
		// Still not found !!!
		throw new TelosysTypeNotFoundException(getLanguageName(), attributeTypeInfo);
	}
	
	//--------------------------------------------------------------------------------------------
	// Collection type ( since v 3.3.0 )
	//--------------------------------------------------------------------------------------------	
	// TODO : changeable type (via $env)
	// Collections for C# :
	//  - List<T>
	//  - Collection<T>
	private static final String STANDARD_COLLECTION_SIMPLE_TYPE = "List" ; // or "Collection" ?
	private static final String STANDARD_COLLECTION_FULL_TYPE   = "List" ; // or "Collection" ?
	
	@Override
	public void setSpecificCollectionType(String specificCollectionType) {
		this.setSpecificCollectionFullType(specificCollectionType) ;
		this.setSpecificCollectionSimpleType(specificCollectionType);
	}

	@Override
	public String getCollectionType(String elementType) {
		return getCollectionSimpleType() + "<" + elementType + ">" ; 
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
