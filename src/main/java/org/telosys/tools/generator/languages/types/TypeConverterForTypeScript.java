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

	private static final String TS_STRING_PRIMITIVE_TYPE = "string";
	private static final String TS_STRING_WRAPPER_TYPE   = "String";

	private static final String TS_BOOLEAN_PRIMITIVE_TYPE = "boolean";
	private static final String TS_BOOLEAN_WRAPPER_TYPE   = "Boolean";
	// "number" : 64-bit floating point (IEEE 754) - Number.MAX_SAFE_INTEGER === 9,007,199,254,740,991  // (2^53 - 1)
	private static final String TS_NUMBER_PRIMITIVE_TYPE = "number";
	private static final String TS_NUMBER_WRAPPER_TYPE   = "Number";
	// TypeScript native Date
	private static final String TS_DATE   = "Date";
	
	public TypeConverterForTypeScript() {
		super("TypeScript");
		// cf : https://www.typescriptlang.org/docs/handbook/basic-types.html

		//--- Primitive types :
		declarePrimitiveType( buildPrimitiveType(NeutralType.STRING,  TS_STRING_PRIMITIVE_TYPE,  TS_STRING_WRAPPER_TYPE  ) );
		
		declarePrimitiveType( buildPrimitiveType(NeutralType.BOOLEAN, TS_BOOLEAN_PRIMITIVE_TYPE, TS_BOOLEAN_WRAPPER_TYPE ) );
		
		declarePrimitiveType( buildPrimitiveType(NeutralType.BYTE,    TS_NUMBER_PRIMITIVE_TYPE,  TS_NUMBER_WRAPPER_TYPE  ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.SHORT,   TS_NUMBER_PRIMITIVE_TYPE,  TS_NUMBER_WRAPPER_TYPE  ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.INTEGER, TS_NUMBER_PRIMITIVE_TYPE,  TS_NUMBER_WRAPPER_TYPE  ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.LONG,    TS_NUMBER_PRIMITIVE_TYPE,  TS_NUMBER_WRAPPER_TYPE  ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.FLOAT,   TS_NUMBER_PRIMITIVE_TYPE,  TS_NUMBER_WRAPPER_TYPE  ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.DOUBLE,  TS_NUMBER_PRIMITIVE_TYPE,  TS_NUMBER_WRAPPER_TYPE  ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.DECIMAL, TS_NUMBER_PRIMITIVE_TYPE,  TS_NUMBER_WRAPPER_TYPE  ) );
		
		//--- Object types = Wrapper type
		declareObjectType( buildObjectType(NeutralType.STRING,    TS_STRING_WRAPPER_TYPE ) );
		
		declareObjectType( buildObjectType(NeutralType.BOOLEAN,   TS_BOOLEAN_WRAPPER_TYPE ) );
		
		declareObjectType( buildObjectType(NeutralType.BYTE,      TS_NUMBER_WRAPPER_TYPE ) );
		declareObjectType( buildObjectType(NeutralType.SHORT,     TS_NUMBER_WRAPPER_TYPE ) );
		declareObjectType( buildObjectType(NeutralType.INTEGER,   TS_NUMBER_WRAPPER_TYPE ) );
		declareObjectType( buildObjectType(NeutralType.LONG,      TS_NUMBER_WRAPPER_TYPE ) );
		declareObjectType( buildObjectType(NeutralType.FLOAT,     TS_NUMBER_WRAPPER_TYPE ) );
		declareObjectType( buildObjectType(NeutralType.DOUBLE,    TS_NUMBER_WRAPPER_TYPE ) );
		declareObjectType( buildObjectType(NeutralType.DECIMAL,   TS_NUMBER_WRAPPER_TYPE ) );
		
		// Without any external libraries, TypeScript (JavaScript) only provides the built-in "Date" object, 
		// which is fairly limited compared to Java's rich java.time types.
		declareObjectType( buildObjectType(NeutralType.DATE,       TS_DATE ) );
		declareObjectType( buildObjectType(NeutralType.TIME,       TS_DATE ) );
		declareObjectType( buildObjectType(NeutralType.TIMESTAMP,  TS_DATE ) );
		declareObjectType( buildObjectType(NeutralType.DATETIME,   TS_DATE ) ); // v 4.3
		declareObjectType( buildObjectType(NeutralType.DATETIMETZ, TS_DATE ) ); // v 4.3
		declareObjectType( buildObjectType(NeutralType.TIMETZ,     TS_DATE ) ); // v 4.3

		// UUID : In TypeScript, there is no built-in UUID type => use "string" (eg "550e8400-e29b-41d4-a716-446655440000")
		declareObjectType( buildObjectType(NeutralType.UUID,      TS_STRING_PRIMITIVE_TYPE ) ); // v 4.3
		
		// BINARY : you can store an image or a sound in a "Uint8Array" in TypeScript (that’s one of its primary use cases)
		//  . "Blob" (Binary Large Object) : Represents immutable raw data, usually files or media.
		//  . "Uint8Array" : Typed array of bytes (numbers 0–255). Mutable and used for manipulating binary data in memory. Good for low-level operations: processing bytes, crypto, decoding/encoding.
		declareObjectType( buildObjectType(NeutralType.BINARY,   "Uint8Array"  ) ); 
		
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
		
		// Always use "primitive types" 
		// Avoid using wrapper types (like "Number" and "String") unless you have a very specific reason.

		// 1) Try to found a primitive type first
		LanguageType lt = getPrimitiveType(attributeTypeInfo.getNeutralType(), false ) ;
		if ( lt != null ) { // FOUND
			return lt ;
		}
		// 2) Still not found => try to found an object type
		lt = getObjectType(attributeTypeInfo.getNeutralType() ) ;
		if ( lt != null ) { // FOUND
			return lt ;
		}

		// Still not found !!!
		throw new TelosysTypeNotFoundException(getLanguageName(), attributeTypeInfo);
	}
	
	//--------------------------------------------------------------------------------------------
	// Collection type ( since v 3.3.0 )
	//--------------------------------------------------------------------------------------------
	// 'Array' :  
	//    "Foo[]" and "Array<Foo>" are exactly the same type in TypeScript.
	//      Example 1 : " let   num:    number[]      = [1, 2, 3]; "
	//      Example 2 : " const numToo: Array<number> = [1, 2, 3]; " 
	//    Type[] is the shorthand syntax for an array of Type. 
	//    Array<Type> is the generic syntax. 
	//    They are completely equivalent.
	// 'Set' : A set is an ordered list of values with no duplicates
	//    Example: " const planet = new Set<string>(); "
	
	private static final String BRACKETS = "[]";
	private static final String ARRAY    = "Array";
	private static final String SET      = "Set";
	
	@Override
	public String getCollectionType() {
		// get specific collection type if any (set with "$env.collectionType")
		String collectionType = determineCollectionTypeToUse(BRACKETS);
		if ( BRACKETS.equals(collectionType) ) {
			return BRACKETS;
		}
		else if ( ARRAY.equalsIgnoreCase(collectionType) ) {
			return ARRAY;
		}
		else if ( SET.equalsIgnoreCase(collectionType) ) {
			return SET;
		}
		else {
			// Invalid collection type => use default 
			return BRACKETS;
		}
	}

	@Override
	public String getCollectionType(String elementType) {
		String collectionType = getCollectionType();
		if ( ARRAY.equalsIgnoreCase(collectionType) ) {
			// Example: "Array<Foo>"
			return ARRAY + "<" + elementType + ">";
		}
		else if ( SET.equalsIgnoreCase(collectionType) ) {
			// Example: "Set<Foo>"
			return SET + "<" + elementType + ">";
		}
		else {
			// Example: "Foo[]"
			return elementType + BRACKETS;
		}
	}
	
}
