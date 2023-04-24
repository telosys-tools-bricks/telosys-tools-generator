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

import org.telosys.tools.commons.JavaTypeUtil;
import org.telosys.tools.generic.model.types.NeutralType;

/**
 * Type converter for "JAVA" language
 * 
 * @author Laurent Guerin
 *
 */
public class TypeConverterForJava extends TypeConverter {

	public static final String LOCAL_DATE_CLASS      = "java.time.LocalDate" ;
	public static final String LOCAL_TIME_CLASS      = "java.time.LocalTime" ;
	public static final String LOCAL_DATE_TIME_CLASS = "java.time.LocalDateTime" ;	
	
	public TypeConverterForJava() {
		super("Java");
		
		//--- Object types 
		declareObjectType( buildJavaType(NeutralType.STRING,    java.lang.String.class) );
		declareObjectType( buildJavaType(NeutralType.BOOLEAN,   java.lang.Boolean.class) );
		declareObjectType( buildJavaType(NeutralType.BYTE,      java.lang.Byte.class) );
		declareObjectType( buildJavaType(NeutralType.SHORT,     java.lang.Short.class) );
		declareObjectType( buildJavaType(NeutralType.INTEGER,   java.lang.Integer.class) );
		declareObjectType( buildJavaType(NeutralType.LONG,      java.lang.Long.class) );
		declareObjectType( buildJavaType(NeutralType.FLOAT,     java.lang.Float.class) );
		declareObjectType( buildJavaType(NeutralType.DOUBLE,    java.lang.Double.class) );
		declareObjectType( buildJavaType(NeutralType.DECIMAL,   java.math.BigDecimal.class) );
		
// New temporal types since ver 3.4.0
//		declareObjectType( buildJavaType(NeutralType.DATE,      java.util.Date.class) );
//		declareObjectType( buildJavaType(NeutralType.TIME,      java.util.Date.class) );
//		declareObjectType( buildJavaType(NeutralType.TIMESTAMP, java.util.Date.class) );
		declareObjectType( buildJavaType(NeutralType.DATE,      LOCAL_DATE_CLASS) );
		declareObjectType( buildJavaType(NeutralType.TIME,      LOCAL_TIME_CLASS) );
		declareObjectType( buildJavaType(NeutralType.TIMESTAMP, LOCAL_DATE_TIME_CLASS) );
		//Nothing for BINARY

		//--- Primitive types 
		// STRING => No primitive type
		declarePrimitiveType( buildJavaType(NeutralType.BOOLEAN, boolean.class) );
		declarePrimitiveType( buildJavaType(NeutralType.BYTE,    byte.class) );
		declarePrimitiveType( buildJavaType(NeutralType.SHORT,   short.class) );
		declarePrimitiveType( buildJavaType(NeutralType.INTEGER, int.class) );
		declarePrimitiveType( buildJavaType(NeutralType.LONG,    long.class) );
		declarePrimitiveType( buildJavaType(NeutralType.FLOAT,   float.class) );
		declarePrimitiveType( buildJavaType(NeutralType.DOUBLE,  double.class) );
		// DECIMAL => No primitive type
		// DATE => No primitive type
		// TIME => No primitive type
		// TIMESTAMP => No primitive type
		declarePrimitiveType( buildJavaType(NeutralType.BINARY,  byte[].class) );
		
		//--- Unsigned primitive types : No unsigned primitive types in Java
	}

	private LanguageType buildJavaType(String neutralType, Class<?> clazz) {
		if ( clazz.isPrimitive() ) {
			// Primitive type
			return new LanguageType( neutralType, clazz.getSimpleName(), clazz.getSimpleName(), true, getWrapperType(clazz.getSimpleName()) );
		}
		else {
			// Object type => the wrapper type is the same 
			return new LanguageType( neutralType, clazz.getSimpleName(), clazz.getCanonicalName(), false, clazz.getSimpleName() );
		}
	}
	private LanguageType buildJavaType(String neutralType, String javaClassCanonicalName) {
		String javaClassSimpleName = JavaTypeUtil.shortType(javaClassCanonicalName);
		return new LanguageType( neutralType, javaClassSimpleName, javaClassCanonicalName, false, javaClassSimpleName );
	}
	
	/**
	 * Returns the Java wrapper type for the given primitive type
	 * @param primitiveType
	 * @return
	 */
	private String getWrapperType(String primitiveType) {
		
		if ( boolean.class.getSimpleName().equals(primitiveType) ) {
			return Boolean.class.getSimpleName() ;
		}
		else if ( char.class.getSimpleName().equals(primitiveType) ) {
			return Character.class.getSimpleName() ;
		}
		else if ( byte.class.getSimpleName().equals(primitiveType) ) {
			return Byte.class.getSimpleName() ;
		}
		else if ( short.class.getSimpleName().equals(primitiveType) ) {
			return Short.class.getSimpleName() ;
		}
		else if ( int.class.getSimpleName().equals(primitiveType) ) {
			return Integer.class.getSimpleName() ;
		}
		else if ( long.class.getSimpleName().equals(primitiveType) ) {
			return Long.class.getSimpleName() ;
		}
		else if ( float.class.getSimpleName().equals(primitiveType) ) {
			return Float.class.getSimpleName() ;
		}
		else if ( double.class.getSimpleName().equals(primitiveType) ) {
			return Double.class.getSimpleName() ;
		}
		else {
			return primitiveType ; // Never happens
		}
	}
	
	@Override
	public List<String> getComments() {
		List<String> l = new LinkedList<>();
		l.add("'@UnsignedType' : <br>&nbsp;  has no effect (all numeric types are signed in Java)");
		l.add("'@NotNull' : <br>&nbsp;  switches to primitive type if possible (in order to avoid 'null' value) ");
		l.add("'@PrimitiveType' : <br>&nbsp;  switches to primitive type if possible (short, int, boolean, etc) ");
		l.add("'@ObjectType' : <br>&nbsp;  switches primitive types to standard wrapper types (Short, Integer, Long, Float, etc)  ");
		return l ;
	}

	@Override
	public LanguageType getType(AttributeTypeInfo attributeTypeInfo) {
		
		log("type info : " + attributeTypeInfo );
		
		log("STEP 1" );
		//--- 1) Process explicit requirements first (if any)
		// A primitive type is explicitly required ( @PrimitiveType or @UnsignedType )
		if ( attributeTypeInfo.isPrimitiveTypeExpected()  ) { // "UnsignedType" no effect in Java => ignore
			LanguageType lt = getPrimitiveType(attributeTypeInfo.getNeutralType(), false ) ; 
			if ( lt != null ) { // FOUND
				log("1) primitive type found" );
				return lt ;
			}
		}
		log("1) primitive type not found" );
		
		// An object type is explicitly required ( @ObjectType )
		if ( attributeTypeInfo.isObjectTypeExpected() ) {
			LanguageType lt = getObjectType(attributeTypeInfo.getNeutralType() ) ;
			if ( lt != null ) { // FOUND
				log("1) object type found" );
				return lt ;
			}
		}
		log("1) object type not found" );

		log("STEP 2 " );
		//--- 2) Process standard type conversion
		if ( attributeTypeInfo.isNotNull() ) {
			log("2) Not Null : TRUE" );
			// Try to found a primitive type first
			LanguageType lt = getPrimitiveType(attributeTypeInfo.getNeutralType(), false ) ;
			if ( lt != null ) { // FOUND
				return lt ;
			}
			// Still not found : try to found an object type
			return getObjectType(attributeTypeInfo.getNeutralType() ) ;
		}
		else {
			log("2) Not Null : FALSE" );
			// Try to found an object type first
			LanguageType lt = getObjectType(attributeTypeInfo.getNeutralType() ) ;
			if ( lt != null ) { // FOUND
				return lt ;
			}
			// Still not found : try to found a primitive type
			lt = getPrimitiveType(attributeTypeInfo.getNeutralType(), false ) ;
			if ( lt != null ) { // FOUND
				return lt ;
			}
		}
		// Still not found !!!
		throw new TelosysTypeNotFoundException(getLanguageName(), attributeTypeInfo);
	}
	
	//--------------------------------------------------------------------------------------------
	// Collection type ( since v 3.3.0 )
	//--------------------------------------------------------------------------------------------	
	private static final String STANDARD_COLLECTION_SIMPLE_TYPE = "List" ;
	private static final String STANDARD_COLLECTION_FULL_TYPE   = "java.util.List" ;
	
//	@Override
//	public void setSpecificCollectionType(String specificCollectionType) {
//		this.setSpecificCollectionFullType(specificCollectionType) ;
//		this.setSpecificCollectionSimpleType(JavaTypeUtil.shortType(specificCollectionType));
//	}

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
