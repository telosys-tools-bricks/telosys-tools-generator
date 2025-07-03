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
 * Type converter for "Go" language
 * 
 * @author Laurent Guerin
 *
 */
public class TypeConverterForGo extends TypeConverter {

	public TypeConverterForGo() {
		super("Go");
		
		//--- Primitive types :
		declarePrimitiveType( buildPrimitiveType(NeutralType.STRING,   "string"  ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.BOOLEAN,  "bool"    ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.BYTE,     "byte"    ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.SHORT,    "int16"   ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.INTEGER,  "int32"   ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.LONG,     "int64"   ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.FLOAT,    "float32" ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.DOUBLE,   "float64" ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.DECIMAL,  "float64" ) );
		// DATE => No primitive type
		// TIME => No primitive type
		// TIMESTAMP => No primitive type
		declarePrimitiveType( buildPrimitiveType(NeutralType.BINARY, "[]byte" )  ); // No Wrapper type for binary / byte[] ?
		
		//--- Unsigned primitive types : 
		declarePrimitiveUnsignedType( buildPrimitiveType(NeutralType.BYTE,    "uint8"  ) );
		declarePrimitiveUnsignedType( buildPrimitiveType(NeutralType.SHORT,   "uint16" ) );
		declarePrimitiveUnsignedType( buildPrimitiveType(NeutralType.INTEGER, "uint32" ) );
		declarePrimitiveUnsignedType( buildPrimitiveType(NeutralType.LONG,    "uint64" ) );

		//--- Object types : for GO "object types" are used for "structures" define in a "package" ( when "import" is required )
		declareObjectType( buildObjectType(NeutralType.DATE,      "time.Time" ) );
		declareObjectType( buildObjectType(NeutralType.TIME,      "time.Time" ) );
		declareObjectType( buildObjectType(NeutralType.TIMESTAMP, "time.Time" ) );
	}

	private LanguageType buildPrimitiveType(String neutralType, String type) {
		return new LanguageType(neutralType, type,  type, true, type );
	}

	private LanguageType buildObjectType(String neutralType, String type) {
		return new LanguageType(neutralType, type,  type, false, type );
	}
	
	@Override
	public List<String> getComments() {
		List<String> l = new LinkedList<>();
		l.add("'@UnsignedType'  has effect only for: byte, short, int, long ");
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
	// In the future : changeable type (via $env) ???
	// Collections for Go :
	//  - Array : not applicable
	//  - Slice : []Type
	//  - Map   : not applicable

	@Override
	public String getCollectionType() {
		return "[]" ; 
	}

	@Override
	public String getCollectionType(String elementType) {
		// not applicable : syntax "int myarray[]" => just return "int[]"
		return elementType + "[]";  
	}
	
}
