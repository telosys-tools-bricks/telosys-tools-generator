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
 * Type converter for "Scala" language
 * 
 * @author Laurent Guerin
 *
 */
public class TypeConverterForScala extends TypeConverter {

	public TypeConverterForScala() {
		super("Scala");
		
		//--- Primitive types : "AnyVal" Scala types are considered as "primitive types" => NOT NULLABLE
		declarePrimitiveType( buildPrimitiveType(NeutralType.BOOLEAN,  "Boolean" ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.BYTE,     "Byte"    ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.SHORT,    "Short"   ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.INTEGER,  "Int"     ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.LONG,     "Long"    ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.FLOAT,    "Float"   ) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.DOUBLE,   "Double"  ) );		
		declarePrimitiveType( buildPrimitiveType(NeutralType.BINARY,   "Array[Byte]" )  ); 
		// No primitive type for STRING, DECIMAL, DATE, TIME, TIMESTAMP
		
		//--- No unsigned primitive types : 

		//--- Object types : 
		// "String" is an alias for "java.lang.String" (defined in "scala.Predef" )
		// See https://stackoverflow.com/questions/6559938/scala-string-vs-java-lang-string-type-inference
		// See https://www.scala-lang.org/api/current/scala/Predef$.html
		declareObjectType( buildObjectType(NeutralType.STRING,    "String",     "java.lang.String"  ) );
		
		// "scala.math.BigDecimal" is only a wrapper around "java.math.BigDecimal"  
		declareObjectType( buildObjectType(NeutralType.DECIMAL,   "BigDecimal", "scala.math.BigDecimal" ) );
		
		declareObjectType( buildObjectType(NeutralType.DATE,       "LocalDate",      "java.time.LocalDate" ) );
		declareObjectType( buildObjectType(NeutralType.TIME,       "LocalTime",      "java.time.LocalTime" ) );
		declareObjectType( buildObjectType(NeutralType.TIMESTAMP,  "LocalDateTime",  "java.time.LocalDateTime"  ) );
		declareObjectType( buildObjectType(NeutralType.DATETIME,   "LocalDateTime",  "java.time.LocalDateTime"  ) );  // ver 4.3
		declareObjectType( buildObjectType(NeutralType.DATETIMETZ, "OffsetDateTime", "java.time.OffsetDateTime" ) );  // ver 4.3
		declareObjectType( buildObjectType(NeutralType.TIMETZ,     "OffsetTime",     "java.time.OffsetTime"     ) );  // ver 4.3
		declareObjectType( buildObjectType(NeutralType.UUID,       "UUID",           "java.util.UUID"     ) );  // ver 4.3
	}

	private LanguageType buildPrimitiveType(String neutralType, String primitiveType) {
		// same type for all (simpleType = fullType = wrapperType)
		return new LanguageType(neutralType, primitiveType,  primitiveType, true, primitiveType );
	}

	private LanguageType buildObjectType(String neutralType, String simpleType, String fullType) {
		return new LanguageType(neutralType, simpleType,  fullType, false, simpleType );
	}
	
	@Override
	public List<String> getComments() {
		List<String> l = new LinkedList<>();
		l.add("'@UnsignedType'  has no effect");
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
	// The "scala.List" class is a pointer to the "scala.collection.immutable.List" class
	private static final String STANDARD_COLLECTION_TYPE = "List" ;
	
	@Override
	public String getCollectionType() {
		return determineCollectionTypeToUse(STANDARD_COLLECTION_TYPE) ; 
	}
	@Override
	public String getCollectionType(String elementType) {
		// Scala uses "[xx]" instead of "<xx>" in Java
		return determineCollectionTypeToUse(STANDARD_COLLECTION_TYPE) + "[" + elementType + "]" ; 
	}
	
}
