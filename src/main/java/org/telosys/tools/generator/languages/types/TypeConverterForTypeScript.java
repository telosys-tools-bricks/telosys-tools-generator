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
 * Type converter for "TypeScript" language
 * 
 * See : https://www.typescriptlang.org/docs/handbook/basic-types.html 
 * 
 * @author Laurent Guerin
 *
 */
public class TypeConverterForTypeScript extends TypeConverter {

	public TypeConverterForTypeScript() {
		super("TypeScript");
		// cf : https://www.typescriptlang.org/docs/handbook/basic-types.html

		//--- Object types 
		declareObjectType( buildObjectType(NeutralType.STRING,    "String" ) );
		
		declareObjectType( buildObjectType(NeutralType.BOOLEAN,   "Boolean" ) );
		
		declareObjectType( buildObjectType(NeutralType.BYTE,      "Number" ) );
		declareObjectType( buildObjectType(NeutralType.SHORT,     "Number" ) );
		declareObjectType( buildObjectType(NeutralType.INTEGER,   "Number" ) );
		declareObjectType( buildObjectType(NeutralType.LONG,      "Number" ) );
		declareObjectType( buildObjectType(NeutralType.FLOAT,     "Number" ) );
		declareObjectType( buildObjectType(NeutralType.DOUBLE,    "Number" ) );
		declareObjectType( buildObjectType(NeutralType.DECIMAL,   "Number" ) );
		
		declareObjectType( buildObjectType(NeutralType.DATE,      "Date" ) );
		declareObjectType( buildObjectType(NeutralType.TIME,      "Date" ) );
		declareObjectType( buildObjectType(NeutralType.TIMESTAMP, "Date" ) );

		//--- Object SQL types :
		// No specific SQL types 
		
		//--- Primitive types :
		declarePrimitiveType( buildPrimitiveType(NeutralType.STRING,  "string",  "String"  ) );
		
		declarePrimitiveType( buildPrimitiveType(NeutralType.BOOLEAN, "boolean", "Boolean" ) );
		
		declarePrimitiveType( buildPrimitiveType(NeutralType.BYTE,    "number",  "Number"  ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.SHORT,   "number",  "Number"  ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.INTEGER, "number",  "Number"  ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.LONG,    "number",  "Number"  ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.FLOAT,   "number",  "Number"  ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.DOUBLE,  "number",  "Number"  ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.DECIMAL, "number",  "Number"  ) );
		
		// DATE => No primitive type
		// TIME => No primitive type
		// TIMESTAMP => No primitive type
		// BINARY => No primitive type
		declarePrimitiveType( buildPrimitiveType(NeutralType.BINARY,  "any",  "Number"  ) ); // ?????
		
		//--- Unsigned primitive types : 
		// No unsigned types
	}

	private LanguageType buildPrimitiveType(String neutralType, String primitiveType, String wrapperType) {
		return new LanguageType(neutralType, primitiveType,  primitiveType, true, wrapperType );
	}

	private LanguageType buildObjectType(String neutralType, String objectType) {
		// simple type = full type = wrapper type
		return new LanguageType( neutralType, objectType, objectType, false, objectType );
	}

	@Override
	public List<String> getComments() {
		List<String> l = new LinkedList<>();
		l.add("All annotations have no effect for TypeScript");
		return l ;
	}

	@Override
	public LanguageType getType(AttributeTypeInfo attributeTypeInfo) {
		log("type info : " + attributeTypeInfo );
		
		LanguageType lt ;
		// Return an Object "Date" type only for DATE, TIME or TIMESTAMP
		// for all other types return the primitive type
		if ( NeutralType.DATE.equals(attributeTypeInfo.getNeutralType()) ||
			 NeutralType.TIME.equals(attributeTypeInfo.getNeutralType()) ||
			 NeutralType.TIMESTAMP.equals(attributeTypeInfo.getNeutralType()) ) {
			lt = getObjectType(attributeTypeInfo.getNeutralType() ) ;
		}
		else {
			lt = getPrimitiveType(attributeTypeInfo.getNeutralType() ) ;
		}
		if ( lt != null ) {
			return lt ;
		}
		// Still not found !!!
		throw new TelosysTypeNotFoundException(getLanguageName(), attributeTypeInfo);
	}
	
	//--------------------------------------------------------------------------------------------
	// Collection type ( since v 3.3.0 )
	//--------------------------------------------------------------------------------------------
	// 'Array' : 
	//    let   num:    number[]      = [1, 2, 3];
	//    const numToo: Array<number> = [1, 2, 3];
	//    Type[] is the shorthand syntax for an array of Type. 
	//    Array<Type> is the generic syntax. 
	//    They are completely equivalent.
	// 'Set' : A set is an ordered list of values with no duplicates
	//    const planet = new Set<string>();
	//    planet.add("earth");
	
	private static final String BRACKETS = "[]";
	private static final String ARRAY    = "Array";
	
	@Override
	public String getCollectionType() {
		String collectionType = determineCollectionTypeToUse(BRACKETS);
		if ( BRACKETS.equals(collectionType) ) {
			return BRACKETS;
		}
		else if ( ARRAY.equalsIgnoreCase(collectionType) ) {
			return ARRAY;
		}
		else {
			return BRACKETS;
		}
	}

	@Override
	public String getCollectionType(String elementType) {
		String collectionType = getCollectionType();
		if ( ARRAY.equalsIgnoreCase(collectionType) ) {
			return ARRAY + "<" + elementType + ">";
		}
		else {
			return elementType + BRACKETS;
		}
	}
	
}
