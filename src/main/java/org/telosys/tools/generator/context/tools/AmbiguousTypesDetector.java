/**
 *  Copyright (C) 2008-2013  Telosys project org. ( http://www.telosys.org/ )
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
package org.telosys.tools.generator.context.tools;

import java.util.LinkedList;
import java.util.List;

import org.telosys.tools.commons.JavaClassUtil;
import org.telosys.tools.generator.context.AttributeInContext;

public class AmbiguousTypesDetector {

	//private final List<JavaBeanClassAttribute> _attributes ;
	private final List<String>                 _fullTypesList ;

	//-----------------------------------------------------------------------------------------------
	/**
	 * Constructor <br>
	 * Register all the attributes full types
	 * @param attributes 
	 */
	public AmbiguousTypesDetector(List<AttributeInContext> attributes) {
		super();
		//this._attributes = attributes;
		
		_fullTypesList = new LinkedList<String>();
		for ( AttributeInContext attribute : attributes ) {
			if ( attribute.isPrimitiveType() != true ) {
				registerType( attribute.getFullType() ); // "java.math.BigDecimal", "java.util.Date", ...
			}
		}
	}

	//-----------------------------------------------------------------------------------------------
	/**
	 * Constructor
	 */
	public AmbiguousTypesDetector() {
		super();
		_fullTypesList = new LinkedList<String>();
	}
	
	//-----------------------------------------------------------------------------------------------
	/**
	 * Register the given full type <br>
	 * Store it in the list if not yet present
	 * @param fullType
	 */
	public void registerType(String fullType)
	{
		// Store it only if not yet present in the list
		if ( _fullTypesList.contains(fullType) == false ) {
			_fullTypesList.add(fullType);
		}
	}
	
	//-----------------------------------------------------------------------------------------------
	/**
	 * Returns a list containing all the registered types 
	 * @return list of types (never null)
	 */
	public List<String> getAllTypes()
	{
		LinkedList<String> list = new LinkedList<String>() ;
		for ( String fullType : _fullTypesList ) {
			list.add(fullType);
		}
		return list ;
	}
	
	//-----------------------------------------------------------------------------------------------
	/**
	 * Returns a list with all the ambiguous types registered <br>
	 * e.g.  "java.util.Date" and "java.sql.Date" ( same short name "Date" )
	 * @return list of types (never null)
	 */
	public List<String> getAmbiguousTypes()
	{
		LinkedList<String> ambiguousTypes = new LinkedList<String>() ;

		for ( String fullType : _fullTypesList ) {
			String shortName = JavaClassUtil.shortName(fullType);
			
			// if more than one occurrence of this short name in the list 
			// ( eg  2 occurrences : "java.util.Date" and "java.sql.Date" for the "Date" short name )
			if ( shortNameCount(shortName) > 1 ) 
			{
//				if ( collidedTypes.contains(fullType) != true ) {
//					collidedTypes.add(fullType);
//				}
				// Each type is unique in the original list : no risk of duplication
				ambiguousTypes.add(fullType);
			}
		}
		return ambiguousTypes ;
	}
	
	//-----------------------------------------------------------------------------------------------
	/**
	 * Count the number of occurrences of the given short name in the list of types
	 * @param shortName
	 * @return
	 */
	private int shortNameCount(String shortName)
	{
		int count = 0 ;
		String end = "." + shortName ;
		for ( String s : _fullTypesList ) {
			if ( s.endsWith(end))
			{
				count++ ;
			}
		}
		return count ;
	}
}
