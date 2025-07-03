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
 *  
 * 
 * @author Laurent Guerin
 *
 */
public class AttributeTypeConst {

	/**
	 * Private constructor
	 */
	private AttributeTypeConst() {
	}
	
	public static final int     NONE           =  0 ;
	public static final int     NOT_NULL       =  1 ;
	public static final int     PRIMITIVE_TYPE =  2 ;
	public static final int     OBJECT_TYPE    =  4 ;
	public static final int     UNSIGNED_TYPE  =  8 ;
	
}
