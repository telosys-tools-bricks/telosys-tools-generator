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

		declarePrimitiveType( buildPrimitiveType(NeutralType.DATE,      "std::tm",     "std::tm"     ) ); // Date + Hour => #include <chrono> 
		declarePrimitiveType( buildPrimitiveType(NeutralType.TIME,      "std::time_t", "std::time_t" ) ); // Time  => #include <chrono> 
		declarePrimitiveType( buildPrimitiveType(NeutralType.TIMESTAMP, "std::tm",     "std::tm"     ) ); // Date + Hour => #include <chrono> 

		declarePrimitiveType( buildPrimitiveType(NeutralType.BINARY,    "std::vector<unsigned char>", "std::vector<unsigned char>" )  ); // #include <vector>
		
		//--- Unsigned primitive types : 
		declarePrimitiveUnsignedType( buildPrimitiveType(NeutralType.BYTE,    "unsigned char",   "unsigned char"  ) );
		declarePrimitiveUnsignedType( buildPrimitiveType(NeutralType.SHORT,   "unsigned short",  "unsigned short" ) );
		declarePrimitiveUnsignedType( buildPrimitiveType(NeutralType.INTEGER, "unsigned int",    "unsigned int"   ) );
		declarePrimitiveUnsignedType( buildPrimitiveType(NeutralType.LONG,    "unsigned long",   "unsigned long"  ) );

	}

	private LanguageType buildPrimitiveType(String neutralType, String primitiveType, String wrapperType) {
		return new LanguageType(neutralType, primitiveType,  primitiveType, true, wrapperType );
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
		// Still not found !!!
		throw new TelosysTypeNotFoundException(getLanguageName(), attributeTypeInfo);
	}
	
	//--------------------------------------------------------------------------------------------
	// Collection type ( since v 3.3.0 )
	//--------------------------------------------------------------------------------------------	
	// Collections for C++ : see : https://www.educba.com/c-plus-plus-vector-vs-list/ 
	// std::list<int>   x;   // #include <list> 
	// list<int> x;          // #include <list>   + "using namespace std;"
	// std::vector<int> x;   // #include <vector>
	// vector<int> x;        // #include <vector> + "using namespace std;"
	
	private static final String STANDARD_COLLECTION_TYPE = "std::list" ; // #include <list> 
	
	@Override
	public String getCollectionType() {
		return determineCollectionTypeToUse(STANDARD_COLLECTION_TYPE) ; 
	}

	@Override
	public String getCollectionType(String elementType) {
		return determineCollectionTypeToUse(STANDARD_COLLECTION_TYPE) + "<" + elementType + ">" ; 
	}
}
