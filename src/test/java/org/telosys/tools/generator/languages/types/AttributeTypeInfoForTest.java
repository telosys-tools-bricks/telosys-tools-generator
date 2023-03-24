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

/**
 * Synthetic type information 
 * 
 * @author Laurent Guerin
 *
 */
public class AttributeTypeInfoForTest implements AttributeTypeInfo {

//	public static final int     NONE           =  0 ;
//	public static final int     NOT_NULL       =  1 ;
//	public static final int     PRIMITIVE_TYPE =  2 ;
//	public static final int     OBJECT_TYPE    =  4 ;
//	public static final int     UNSIGNED_TYPE  =  8 ;
	
	private final String  neutralType ;

	private final boolean notNull ;

	private final boolean primitiveTypeExpected ;
	private final boolean objectTypeExpected ;
	
	private final boolean unsignedTypeExpected ;
	
	/**
	 * Constructor
	 * @param neutralType
	 * @param typeInfo
	 */
	public AttributeTypeInfoForTest(String neutralType, int typeInfo) {
		super();
		this.neutralType = neutralType;
		
		this.notNull               = ( typeInfo & AttributeTypeConst.NOT_NULL ) != 0 ;
		this.primitiveTypeExpected = ( typeInfo & AttributeTypeConst.PRIMITIVE_TYPE ) != 0 ;
		this.objectTypeExpected    = ( typeInfo & AttributeTypeConst.OBJECT_TYPE ) != 0 ;
		this.unsignedTypeExpected  = ( typeInfo & AttributeTypeConst.UNSIGNED_TYPE ) != 0 ;
	}

	public String getNeutralType() {
		return neutralType;
	}

	public boolean isNotNull() {
		return notNull;
	}

	public boolean isPrimitiveTypeExpected() {
		return primitiveTypeExpected;
	}

	public boolean isObjectTypeExpected() {
		return objectTypeExpected;
	}

	public boolean isUnsignedTypeExpected() {
		return unsignedTypeExpected;
	}

	@Override
	public String toString() {
		StringBuilder sb  = new StringBuilder();
		sb.append("'" + neutralType + "' " );
		if ( notNull || primitiveTypeExpected || objectTypeExpected || unsignedTypeExpected ) {
			sb.append("( " );
			if ( notNull ) {
				sb.append("notNull " );
			}
			if ( primitiveTypeExpected ) {
				sb.append("primitiveType " );
			}
			if ( unsignedTypeExpected ) {
				sb.append("unsignedType " );
			}
			if ( objectTypeExpected ) {
				sb.append("objectType " );
			}
			sb.append(")" );
		}
		return sb.toString();
	}

}
