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
 * Type converter for "Python" language
 * 
 * Python notes:
 *  Everything in Python is an object, even what you think of as “primitive” types.
 *  So, int, float, bool, str, etc., are classes (types) and all their instances are objects.
 *  Python only has objects.
 *  Common built-in types available without import : 
 *    int, float, str, bool, list, dict, tuple, set, bytes, complex, None (Special value for “no value”)
 *  Import required for:
 *    datetime.datetime, UUID, Decimal, Optional, Union, etc
 * 
 * 
 * @author Laurent Guerin
 *
 */
public class TypeConverterForPython extends TypeConverter {

	private static final String PYTHON_INT      = "int";
	private static final String PYTHON_FLOAT    = "float";
	
	private static final String PYTHON_DATETIME = "datetime";
	private static final String PYTHON_IMPORT_DATETIME = "from datetime import datetime";
	private static final String PYTHON_DATE = "date";
	private static final String PYTHON_IMPORT_DATE = "from datetime import date";
	private static final String PYTHON_TIME = "time";
	private static final String PYTHON_IMPORT_TIME = "from datetime import time";
	
	public TypeConverterForPython() {
		super("Python");
		// No type required for Python, but "type hints" are available since Python 3.5 and now usable in 
		//  . Function Parameters and Return Types
		//  . Variables (local or global)
		//  . Class Attributes
		//  . List and dict type 
		//  . etc 
		// Python type hints were introduced in Python 3.5, but the feature has evolved significantly in later versions (3.5 -> 3.12)
		
		//--- For Python basic types without import are considered as "primitive types" 
		declarePrimitiveType( buildPseudoPrimitiveType(NeutralType.STRING,  "str"      ) );
		declarePrimitiveType( buildPseudoPrimitiveType(NeutralType.BOOLEAN, "bool"     ) );
		declarePrimitiveType( buildPseudoPrimitiveType(NeutralType.BYTE,    PYTHON_INT   ) );
		declarePrimitiveType( buildPseudoPrimitiveType(NeutralType.SHORT,   PYTHON_INT   ) );
		declarePrimitiveType( buildPseudoPrimitiveType(NeutralType.INTEGER, PYTHON_INT   ) );
		declarePrimitiveType( buildPseudoPrimitiveType(NeutralType.LONG,    PYTHON_INT   ) );
		declarePrimitiveType( buildPseudoPrimitiveType(NeutralType.FLOAT,   PYTHON_FLOAT ) );
		declarePrimitiveType( buildPseudoPrimitiveType(NeutralType.DOUBLE,  PYTHON_FLOAT ) ); // in Python, the equivalent of Java’s double (64-bit floating point number) is "float"
		// Python's built-in float type is implemented using a C 'double' under the hood
		
		declarePrimitiveType( buildObjectType(NeutralType.DECIMAL,    "Decimal",  "from decimal import Decimal" ) );
		
		declarePrimitiveType( buildObjectType(NeutralType.DATE,       PYTHON_DATE,     PYTHON_IMPORT_DATE     ) );
		declarePrimitiveType( buildObjectType(NeutralType.TIME,       PYTHON_TIME,     PYTHON_IMPORT_TIME     ) );
		declarePrimitiveType( buildObjectType(NeutralType.TIMETZ,     PYTHON_TIME,     PYTHON_IMPORT_TIME     ) ); // v 4.3.0
		declarePrimitiveType( buildObjectType(NeutralType.TIMESTAMP,  PYTHON_DATETIME, PYTHON_IMPORT_DATETIME ) );
		declarePrimitiveType( buildObjectType(NeutralType.DATETIME,   PYTHON_DATETIME, PYTHON_IMPORT_DATETIME ) ); // v 4.3.0
		declarePrimitiveType( buildObjectType(NeutralType.DATETIMETZ, PYTHON_DATETIME, PYTHON_IMPORT_DATETIME ) ); // v 4.3.0
		
		declarePrimitiveType( buildObjectType(NeutralType.UUID,       "UUID",   "from uuid import UUID" ) ); // v 4.3.0
		
		// 'bytearray' is a built-in type in Python, it does not require an import
		declarePrimitiveType( buildPseudoPrimitiveType(NeutralType.BINARY, "bytearray" ) );  
		
	}

	private LanguageType buildPseudoPrimitiveType(String neutralType, String basicType)  {
		return new LanguageType(neutralType, 
				basicType,   // String simpleType, 
				basicType,   // String fullType, 
				true, // boolean isPrimitiveType, 
				basicType ); // String wrapperType
	}
	private LanguageType buildObjectType(String neutralType, String simpleType,  String fullType)  {
		return new LanguageType(neutralType, 
				simpleType,   // String simpleType, 
				fullType,   // String fullType, 
				false, // boolean isPrimitiveType, 
				simpleType ); // String wrapperType
	}
	
	@Override
	public List<String> getComments() {
		List<String> l = new LinkedList<>();
		l.add("Python does not require types — it's a dynamically typed language.");
		l.add("But 'type hints' are strongly recommended (especially for modern Python)");
		l.add("So Telosys converts model neutral types to Python 'type hints'.");
		return l ;
	}

	@Override
	public LanguageType getType(AttributeTypeInfo attributeTypeInfo) {
		log("type info : " + attributeTypeInfo );		
		// Return always the same "void" type
		return getPrimitiveType( attributeTypeInfo.getNeutralType() );
	}
	
	//--------------------------------------------------------------------------------------------
	// Collection type ( since v 3.3.0 )
	//--------------------------------------------------------------------------------------------
	// "list" is the equivalent of Java "List", example: list[str]
	// From Python 3.9+ native generic support. No import required 
	// Alternative(s):
	// "set" can also be used for unicity (no import required)
	private static final String STANDARD_COLLECTION_TYPE = "list" ; 

	@Override
	public String getCollectionType() {
		return determineCollectionTypeToUse(STANDARD_COLLECTION_TYPE) ; 
	}
	@Override
	public String getCollectionType(String elementType) {
		return determineCollectionTypeToUse(STANDARD_COLLECTION_TYPE) + "[" + elementType + "]" ; 
	}
	
}
