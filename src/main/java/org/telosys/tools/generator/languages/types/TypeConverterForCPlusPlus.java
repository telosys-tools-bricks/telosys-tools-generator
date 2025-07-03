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
 * C++ notes:
 * - Prefix usage ("using namespace NAMESPACE_NAME;" and prefix)
 *     If you use "using namespace std;", the compiler still recognizes fully qualified names like "std::cout", and it actually prefers them for disambiguation.
 *     Using std:: explicitly can help avoid ambiguity if you also have similarly named identifiers
 *     => keep prefix in the type 
 * 
 * @author Laurent Guerin
 *
 */
public class TypeConverterForCPlusPlus extends TypeConverter {

	//--- Primitive types (built-in keywords) :
	// built-in keywords are not part of the "std" namespace (int, float, double, char, bool, void, long, short, unsigned)
	// => do not use "std::" prefix
	private static final String CPP_BOOL   = "bool" ;
	private static final String CPP_CHAR   = "char" ;
	private static final String CPP_SHORT  = "short" ;
	private static final String CPP_INT    = "int" ;
	private static final String CPP_LONG   = "long" ;
	private static final String CPP_FLOAT  = "float" ;
	private static final String CPP_DOUBLE = "double" ;
	private static final String CPP_UNSIGNED_CHAR  = "unsigned char" ;
	private static final String CPP_UNSIGNED_SHORT = "unsigned short" ;
	private static final String CPP_UNSIGNED_INT   = "unsigned int" ;
	private static final String CPP_UNSIGNED_LONG  = "unsigned long" ;

	//--- Other types (with namespace prefix) :
	private static final String CPP_STRING = "std::string" ;
	private static final String CPP_YEAR_MONTH_DAY = "std::chrono::year_month_day" ; // Just the date, no time or time zone
	private static final String CPP_HH_MM_SS       = "std::chrono::hh_mm_ss" ;       // Just the time-of-day, no date or time zone
	private static final String CPP_LOCAL_TIME     = "std::chrono::local_time" ;     // Naive date-time, no time zone
	private static final String CPP_ZONED_TIME     = "std::chrono::zoned_time" ;     // Date-time with time zone
	
	private static final String CPP_ARRAY_UINT8_T_16 = "std::array<std::uint8_t, 16>"; // '#include <array>'  for 'std::array'  and '#include <cstdint>' for 'std::uint8_t'
	private static final String CPP_VECTOR_BYTE      = "std::vector<std::byte>";       // '#include <vector>' for 'std::vector' and '#include <cstddef>' for 'std::byte'
	
	public TypeConverterForCPlusPlus() {
		super("C++");
		
		//--- Primitive types :
		declarePrimitiveType( buildPrimitiveType(NeutralType.BOOLEAN,  CPP_BOOL,    CPP_BOOL   ) );
		
		declarePrimitiveType( buildPrimitiveType(NeutralType.BYTE,     CPP_CHAR,    CPP_CHAR   ) );		
		declarePrimitiveType( buildPrimitiveType(NeutralType.SHORT,    CPP_SHORT,   CPP_SHORT  ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.INTEGER,  CPP_INT,     CPP_INT    ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.LONG,     CPP_LONG,    CPP_LONG   ) );
		
		declarePrimitiveType( buildPrimitiveType(NeutralType.FLOAT,    CPP_FLOAT,   CPP_FLOAT  ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.DOUBLE,   CPP_DOUBLE,  CPP_DOUBLE ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.DECIMAL,  CPP_DOUBLE,  CPP_DOUBLE ) );

		//--- Unsigned primitive types : 
		declarePrimitiveUnsignedType( buildPrimitiveType(NeutralType.BYTE,    CPP_UNSIGNED_CHAR,   CPP_UNSIGNED_CHAR  ) );
		declarePrimitiveUnsignedType( buildPrimitiveType(NeutralType.SHORT,   CPP_UNSIGNED_SHORT,  CPP_UNSIGNED_SHORT ) );
		declarePrimitiveUnsignedType( buildPrimitiveType(NeutralType.INTEGER, CPP_UNSIGNED_INT,    CPP_UNSIGNED_INT   ) );
		declarePrimitiveUnsignedType( buildPrimitiveType(NeutralType.LONG,    CPP_UNSIGNED_LONG,   CPP_UNSIGNED_LONG  ) );

		//--- String type
		declarePrimitiveType( buildPrimitiveType(NeutralType.STRING,   CPP_STRING,  CPP_STRING  ) );
		
		//--- Temporal types with basic C++ types 
		//declarePrimitiveType( buildPrimitiveType(NeutralType.DATE,      "std::tm",     "std::tm"     ) ); // Date + Hour => #include <chrono> 
		//declarePrimitiveType( buildPrimitiveType(NeutralType.TIME,      "std::time_t", "std::time_t" ) ); // Time        => #include <chrono> 
		//declarePrimitiveType( buildPrimitiveType(NeutralType.TIMESTAMP, "std::tm",     "std::tm"     ) ); // Date + Hour => #include <chrono> 
		//--- Temporal types (for C++ 20 with "#include <chrono>" for "std::chrono::TYPE" ) 
		declarePrimitiveType( buildPrimitiveType(NeutralType.DATE,       CPP_YEAR_MONTH_DAY, CPP_YEAR_MONTH_DAY ) ); // Just the date, no time or time zone
		declarePrimitiveType( buildPrimitiveType(NeutralType.TIME,       CPP_HH_MM_SS,       CPP_HH_MM_SS       ) ); // Just the time-of-day, no date or time zone
		declarePrimitiveType( buildPrimitiveType(NeutralType.TIMESTAMP,  CPP_LOCAL_TIME,     CPP_LOCAL_TIME     ) ); // Naive date-time, no time zone
		// v 4.3.0 new types
		declarePrimitiveType( buildPrimitiveType(NeutralType.DATETIME,   CPP_LOCAL_TIME,     CPP_LOCAL_TIME     ) ); // Naive date-time, no time zone
		declarePrimitiveType( buildPrimitiveType(NeutralType.DATETIMETZ, CPP_ZONED_TIME,     CPP_ZONED_TIME     ) ); // Date-time with time zone
		declarePrimitiveType( buildPrimitiveType(NeutralType.TIMETZ,     CPP_HH_MM_SS,       CPP_HH_MM_SS       ) ); // No Time with TZ in C++ => like "time"

		//--- UUID (v 4.3.0 )
		// Option 1 : array of 16 bytes "std::array<uint8_t, 16>"  ( '#include <array>'  for 'std::array' and '#include <cstdint>' for 'std::uint8_t' )
		// Some compilers also define the C versions (::uint8_t) in the global namespace as an extension, but the standard says to use std::uint8_t in C++.
		declarePrimitiveType( buildPrimitiveType(NeutralType.UUID,     CPP_ARRAY_UINT8_T_16,  CPP_ARRAY_UINT8_T_16  ) ); 		
		// Option 2 : string of 32 characters : "std::string"
		// Option 3 : library (GitHub project: mariusbancila/stduuid  or  Boost.UUID)
		
		//--- Byte array -> 2 options:
		// std::vector<std::byte>       (resizable, include <vector> and <cstddef>) 
		// std::array<std::byte, SIZE>  (not resizable, include <array> and <cstddef>)
		// NB: 'byte' is not a built-in keyword like int or char => use 'std::' prefix 
		declarePrimitiveType( buildPrimitiveType(NeutralType.BINARY,    CPP_VECTOR_BYTE, CPP_VECTOR_BYTE )  ); // #include <vector> + <cstddef> for std::byte
		
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
