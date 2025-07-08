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

import org.telosys.tools.commons.JavaTypeUtil;
import org.telosys.tools.generic.model.types.NeutralType;

/**
 * Type converter for "KOTLIN" language
 * 
 * @author Laurent Guerin
 *
 */
public class TypeConverterForKotlin extends TypeConverter {

	// Pseudo "primitive types" (no import)
	public static final String KOTLIN_STRING    = "String" ;
	public static final String KOTLIN_BOOLEAN   = "Boolean" ;
	public static final String KOTLIN_BYTE      = "Byte" ;
	public static final String KOTLIN_UBYTE     = "UByte" ;  // literal with "u" : 12u
	public static final String KOTLIN_SHORT     = "Short" ;
	public static final String KOTLIN_USHORT    = "UShort" ; // literal with "u" : 12u
	public static final String KOTLIN_INT       = "Int" ;
	public static final String KOTLIN_UINT      = "UInt" ; // literal with "u" : 12u
	public static final String KOTLIN_LONG      = "Long" ; // literal (nothing special) : 12
	public static final String KOTLIN_ULONG     = "ULong" ; // literal 12u
	public static final String KOTLIN_FLOAT     = "Float" ; // literal with "f" : var ff: Float = 123.45f
	public static final String KOTLIN_DOUBLE    = "Double" ; // literal (nothing special) : var dd: Double = 123.45
	public static final String KOTLIN_BYTEARRAY = "ByteArray" ;

	// Object types ( Java objects => import required )	
	public static final String JAVA_BIGDECIMAL     = "java.math.BigDecimal" ; // import java.math.BigDecimal
	public static final String JAVA_LOCALDATE      = "java.time.LocalDate" ;  // import java.time.LocalDateTime
	public static final String JAVA_LOCALTIME      = "java.time.LocalTime" ;
	public static final String JAVA_LOCALDATETIME  = "java.time.LocalDateTime" ;	
	public static final String JAVA_OFFSETDATETIME = "java.time.OffsetDateTime" ;
	public static final String JAVA_OFFSETTIME     = "java.time.OffsetTime" ;
	public static final String JAVA_UUID           = "java.util.UUID" ;
	
	private final HashMap<String, LanguageType> unsignedTypes = new HashMap<>();
	
	public TypeConverterForKotlin() {
		super("Kotlin");
		
		//--- Primitive types : Kotlin basic types (non Java types) are considered as "primitive types" (no import)
		declarePrimitiveType( buildPrimitiveType(NeutralType.STRING,  KOTLIN_STRING) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.BOOLEAN, KOTLIN_BOOLEAN) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.BYTE,    KOTLIN_BYTE) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.SHORT,   KOTLIN_SHORT) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.INTEGER, KOTLIN_INT) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.LONG,    KOTLIN_LONG) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.FLOAT,   KOTLIN_FLOAT) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.DOUBLE,  KOTLIN_DOUBLE) );
		declarePrimitiveType( buildPrimitiveType(NeutralType.BINARY,  KOTLIN_BYTEARRAY) );

		//--- Object types : Java types used in Kotlin are considered as "object types"		
		declareObjectType( buildObjectType(NeutralType.DECIMAL,    JAVA_BIGDECIMAL) );
		declareObjectType( buildObjectType(NeutralType.DATE,       JAVA_LOCALDATE) );
		declareObjectType( buildObjectType(NeutralType.TIME,       JAVA_LOCALTIME) );
		declareObjectType( buildObjectType(NeutralType.TIMESTAMP,  JAVA_LOCALDATETIME) );
		declareObjectType( buildObjectType(NeutralType.DATETIME,   JAVA_LOCALDATETIME) );  // ver 4.3.0
		declareObjectType( buildObjectType(NeutralType.DATETIMETZ, JAVA_OFFSETDATETIME) ); // ver 4.3.0
		declareObjectType( buildObjectType(NeutralType.TIMETZ,     JAVA_OFFSETTIME) );     // ver 4.3.0

		declareObjectType( buildObjectType(NeutralType.UUID,       JAVA_UUID) );  // ver 4.3.0
		
		//--- Unsigned primitive types : 
		unsignedTypes.put( KOTLIN_BYTE,  buildPrimitiveType(NeutralType.BYTE,    KOTLIN_UBYTE  ) );
		unsignedTypes.put( KOTLIN_SHORT, buildPrimitiveType(NeutralType.SHORT,   KOTLIN_USHORT ) );
		unsignedTypes.put( KOTLIN_INT,   buildPrimitiveType(NeutralType.INTEGER, KOTLIN_UINT   ) );
		unsignedTypes.put( KOTLIN_LONG,  buildPrimitiveType(NeutralType.LONG,    KOTLIN_ULONG  ) );
	}

	private LanguageType buildPrimitiveType(String neutralType, String primitiveType)  {
		return new LanguageType(neutralType, 
				primitiveType,   // String simpleType, 
				primitiveType,   // String fullType, 
				true, // boolean isPrimitiveType, 
				primitiveType ); // String wrapperType
	}

	private LanguageType buildObjectType(String neutralType, String fullType)  {
		String simpleType = JavaTypeUtil.shortType(fullType);
		return new LanguageType(neutralType, 
				simpleType,   // String simpleType, 
				fullType,   // String fullType, 
				false, // boolean isPrimitiveType, 
				simpleType ); // String wrapperType
	}
	
	@Override
	public List<String> getComments() {
		List<String> l = new LinkedList<>();
		l.add("'@UnsignedType' :  <br>&nbsp;  has effect only for 'byte', 'short', 'int' and 'long' ");
		l.add("'@NotNull' :       <br>&nbsp;  no effect on types, use '$kotlin' object for 'nullable type' with '?'  ");
		l.add("'@PrimitiveType' : <br>&nbsp;  no effect ");
		l.add("'@ObjectType' :    <br>&nbsp;  no effect ");
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
	private static final String STANDARD_COLLECTION_TYPE = "List" ; // no import required in Kotlin
	
	@Override
	public String getCollectionType() {
		return determineCollectionTypeToUse(STANDARD_COLLECTION_TYPE) ; 
	}
	
	@Override
	public String getCollectionType(String elementType) {
		return determineCollectionTypeToUse(STANDARD_COLLECTION_TYPE) + "<" + elementType + ">" ; 
	}
	
}
