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
package org.telosys.tools.generator.context;

import java.util.LinkedList;
import java.util.List;

import org.telosys.tools.commons.JavaTypeUtil;

/**
 * List of full java class names that need imports<br>
 * eg : "java.util.Date", "java.math.BigDecimal", etc ...
 * 
 * @author Laurent GUERIN
 *
 */
public class ImportsList {

	private LinkedList<String> _list = new LinkedList<String>() ; // List of Java "full types" to import ( eg : "java.math.BigDecimal" )
	
	public ImportsList() 
	{
		super();
	}

	public List<String> getList()
	{
		return _list ;
	}
	
	/**
	 * Returns true if the given type is already declared ( stored in the list )
	 * @param type
	 * @return
	 */
	public boolean isDeclared(String type)
	{
		if ( type != null )
		{
//			for ( String s2 : _list ) {
//				if ( s2.equals(type))
//				{
//					return true ; // Found = declared
//				}
//			}
			return _list.contains(type);
		}
		return false ;
	}
	
	/**
	 * Declare the given type : stored only if needed ( requires import and not yet stored )
	 * @param type 
	 */
	public void declareType(String type )
	{
		if ( JavaTypeUtil.needsImport(type) != true )
		{
			return ;
		}
		if ( isDeclared(type) )
		{
			return ;
		}
		_list.add(type);
	}
	
//	private int shortNameCount(String shortName)
//	{
//		int count = 0 ;
//		String end = "." + shortName ;
//		for ( String s : _list ) {
//			if ( s.endsWith(end))
//			{
//				count++ ;
//			}
//		}
//		return count ;
//	}
	
//	private void registerCollidedTypes(String shortName, LinkedList<String> listExtracted )
//	{
//		String end = "." + shortName ;
//		//--- store the types to extract 
//		for ( String sFullType : _list ) {
//			if ( sFullType.endsWith(end))
//			{
//				listExtracted.add(sFullType);
//			}
//		}
//	}
	
//	/**
//	 * Extract the duplicated short names from the imports list to avoid imports collision
//	 * eg : "import java.util.Date;" collides with "import java.sql.Date;"
//	 * @return list of all the collided full types (those types are already extracted from imports),<br>
//	 * or null if nothing has been extracted
//	 */
//	public LinkedList<String> extractDuplicatedShortNames()
//	{
//		LinkedList<String> collidedTypes = null ;
//
//		//--- Build the list of types to extract
//		for ( String s : _list ) {
//			String shortName = JavaClassUtil.shortName(s);
//			
//			// if more than one occurrence of this short name in the imports 
//			// ( eg  2 occurrences : "java.util.Date" and "java.sql.Date" for the "Date" short name)
//			if ( shortNameCount(shortName) > 1 ) 
//			{
//				if ( collidedTypes == null )
//				{
//					collidedTypes = new LinkedList<String>() ;
//				}
//				registerCollidedTypes(shortName, collidedTypes );
//			}
//		}
//		
//		//--- Remove all the types to extract (if any)
//		if ( collidedTypes != null )
//		{
//			_list.removeAll(collidedTypes);
//		}
//		
//		//--- Return the list of types extracted
//		return collidedTypes ;
//	}
}
