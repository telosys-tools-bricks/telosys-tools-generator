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
 * Type converter for "C++" language
 * 
 * @author Laurent Guerin
 *
 */
public class TypeConverterForCPlusPlus extends TypeConverter {

	public TypeConverterForCPlusPlus() {
		super("C++");
		
		//--- Primitive types :
		declarePrimitiveType( buildPrimitiveType(NeutralType.STRING,   "string",  "string"  ) );
		
		declarePrimitiveType( buildPrimitiveType(NeutralType.BOOLEAN,  "bool",    "bool"    ) );
		
		declarePrimitiveType( buildPrimitiveType(NeutralType.BYTE,     "char",    "char"    ) );		
		declarePrimitiveType( buildPrimitiveType(NeutralType.SHORT,    "short",   "short"   ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.INTEGER,  "int",     "int"     ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.LONG,     "long",    "long"    ) );
		
		declarePrimitiveType( buildPrimitiveType(NeutralType.FLOAT,    "float",   "float"   ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.DOUBLE,   "double",  "double"  ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.DECIMAL,  "double",  "double"  ) );

		declarePrimitiveType( buildPrimitiveType(NeutralType.DATE,      "std::tm", "std::tm" ) ); // struct tm (https://www.cplusplus.com/reference/ctime/tm/)
		declarePrimitiveType( buildPrimitiveType(NeutralType.TIME,      "",   ""   ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.TIMESTAMP, "",   ""   ) );

		declarePrimitiveType( buildPrimitiveType(NeutralType.BINARY, "", "" )  ); // example : "unsigned char abc[3];"
		
		//--- Unsigned primitive types : 
		declarePrimitiveUnsignedType( buildPrimitiveType(NeutralType.BYTE,    "unsigned char",   "unsigned char"  ) );
		declarePrimitiveUnsignedType( buildPrimitiveType(NeutralType.SHORT,   "unsigned short",  "unsigned short" ) );
		declarePrimitiveUnsignedType( buildPrimitiveType(NeutralType.INTEGER, "unsigned int",    "unsigned int"   ) );
		declarePrimitiveUnsignedType( buildPrimitiveType(NeutralType.LONG,    "unsigned long",   "unsigned long"  ) );

		//--- Object types : 
		declareObjectType( buildObjectType(NeutralType.DATE,      "std::tm", "std::tm"      ) );
		declareObjectType( buildObjectType(NeutralType.TIME,      "",  "" ) );
		declareObjectType( buildObjectType(NeutralType.TIMESTAMP, "",  "" ) );
	}

	private LanguageType buildPrimitiveType(String neutralType, String primitiveType, String wrapperType) {
		return new LanguageType(neutralType, primitiveType,  primitiveType, true, wrapperType );
	}

	private LanguageType buildObjectType(String neutralType, String simpleType, String fullType) {
		return new LanguageType(neutralType, simpleType,  fullType, false, simpleType );
	}
	
	@Override
	public List<String> getComments() {
		List<String> l = new LinkedList<>();
		l.add("'@UnsignedType'  has effect only for char, short, int, long ");
		l.add("");
		l.add("'@NotNull'  has no effect ");
		l.add("'@PrimitiveType'  has no effect ");
		l.add("'@ObjectType'  has no effect  ");
		return l ;
	}

	@Override
	public LanguageType getType(AttributeTypeInfo attributeTypeInfo) {
		
		String neutralType = attributeTypeInfo.getNeutralType() ;
		// Search a "primitive type" first 
		LanguageType lt = getPrimitiveType(neutralType, attributeTypeInfo.isUnsignedTypeExpected() ) ;
		if ( lt != null ) {
			return lt ;
		}
		// if "primitive type" not found search an "object type" : date, time, timestamp
		lt = getObjectType( neutralType ) ;
		if ( lt != null ) {
			return lt;
		}
		// Still not found !!!
		throw new TelosysTypeNotFoundException(getLanguageName(), attributeTypeInfo);
	}
	
	//--------------------------------------------------------------------------------------------
	// Collection type ( since v 3.3.0 )
	//--------------------------------------------------------------------------------------------	
	// Collections for C++ :
	//  - standard array : "int myarray[]" or "int myarray[40]"
	//  - array library  : "#include <array>" / "array<int,3> myarray {10,20,30};"
	private static final String STANDARD_COLLECTION_SIMPLE_TYPE = "[]" ;  // not applicable
	private static final String STANDARD_COLLECTION_FULL_TYPE   = "[]" ;  // not applicable
	
	@Override
	public void setSpecificCollectionType(String specificCollectionType) {
		this.setSpecificCollectionFullType(specificCollectionType) ;
		this.setSpecificCollectionSimpleType(specificCollectionType);
	}

	@Override
	public String getCollectionType(String elementType) {
		return elementType + getCollectionSimpleType()  ;  // not applicable : syntax "int myarray[]" => just return "int[]"
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
