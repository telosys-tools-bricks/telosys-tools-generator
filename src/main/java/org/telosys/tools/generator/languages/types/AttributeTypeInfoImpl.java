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

import org.telosys.tools.generic.model.Attribute;

/**
 * Synthetic type information 
 * 
 * @author Laurent Guerin
 *
 */
public class AttributeTypeInfoImpl implements AttributeTypeInfo {

	private final String  neutralType ;

	private final boolean notNull ;

	private final boolean primitiveTypeExpected ;
	private final boolean objectTypeExpected ;
	
	private final boolean unsignedTypeExpected ;
	
	/**
	 * Constructor
	 * 
	 * @param attribute
	 */
	public AttributeTypeInfoImpl(Attribute attribute) {
		super();
		this.neutralType           = attribute.getNeutralType();
		this.notNull               = attribute.isNotNull();
		this.primitiveTypeExpected = attribute.isPrimitiveTypeExpected();
		this.objectTypeExpected    = attribute.isObjectTypeExpected();
		this.unsignedTypeExpected  = attribute.isUnsignedTypeExpected();
	}

	@Override
	public String getNeutralType() {
		return neutralType;
	}

	@Override
	public boolean isNotNull() {
		return notNull;
	}

	@Override
	public boolean isPrimitiveTypeExpected() {
		return primitiveTypeExpected;
	}

	@Override
	public boolean isObjectTypeExpected() {
		return objectTypeExpected;
	}

	@Override
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
