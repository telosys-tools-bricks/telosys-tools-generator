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
public class JavaImportsList {

	private LinkedList<String> _list = new LinkedList<String>() ; // List of Java "full types" to import ( eg : "java.math.BigDecimal" )
	
	/**
	 * Constructor
	 */
	public JavaImportsList() {
		super();
	}

//	public List<String> getList() {
//		return _list ;
//	}
	
//	/**
//	 * Returns true if the given type is already declared ( stored in the list )
//	 * @param type
//	 * @return
//	 */
//	private boolean isDeclared(String type) {
//		if ( type != null ) {
//			return _list.contains(type);
//		}
//		return false ;
//	}
	
	/**
	 * Declare the given type : stored only if needed ( requires import and not yet stored )
	 * @param fullTypeName 
	 */
	public void declareType(String fullTypeName ) {
		if ( JavaTypeUtil.needsImport(fullTypeName) ) {
			if ( ! _list.contains(fullTypeName) ) {
				_list.add(fullTypeName);
			}
		}
	}
	
	private final static String JAVA_UTIL_DATE = "java.util.Date" ;
	private final static String JAVA_SQL_DATE  = "java.sql.Date" ;
	/**
	 * Removes the "collided types" <br>
	 * e.g. it's impossible to import both "java.util.Date" and "java.sql.Date" 
	 */
	private void removeCollidedTypes() {
		if ( _list.contains(JAVA_UTIL_DATE) && _list.contains(JAVA_SQL_DATE) ) {
			_list.remove(JAVA_UTIL_DATE) ;
			_list.remove(JAVA_SQL_DATE) ;
		}		
	}
	
	/**
	 * Returns a list with all imports "ready to use" <br>
	 * ( sorted and without collided types )
	 * @return
	 */
	public List<String> getFinalImportsList() {
		removeCollidedTypes();
		java.util.Collections.sort(_list);
		return _list ;		
	}

	private static final Class<?>[] COLLECTIONS = {
			//----- Collection interface
			java.util.Collection.class,  
			// List interface
			java.util.List.class,
			java.util.ArrayList.class,
			java.util.Vector.class,
			java.util.LinkedList.class,
			// Set interface
			java.util.Set.class, 
			java.util.HashSet.class,
			java.util.LinkedHashSet.class,
			java.util.SortedSet.class,
			java.util.TreeSet.class,
			// Other
			java.util.Queue.class,
			java.util.PriorityQueue.class,
			//----- Map interface
			java.util.Map.class,
			java.util.Hashtable.class,
			java.util.HashMap.class,
			java.util.SortedMap.class,
			java.util.TreeMap.class
			};
	
//	private void declareCollectionType(String type) {
//		for ( Class<?> clazz : COLLECTIONS ) {
//			// "Collection<Type>", "List<Type>", "Set<Type>"
//			if ( type.contains(clazz.getSimpleName()) && type.contains("<") && type.contains(">") ) {
//				declareType(clazz.getCanonicalName());
//			} 
//		}
//	}
	
	private void declareLinkType(String inputType) {
		String type = inputType.trim();
		if ( type.contains("<") && type.endsWith(">") ) {
			for ( Class<?> clazz : COLLECTIONS ) {
				// "Collection<Type>", "List<Type>", "Set<Type>"
				if ( type.startsWith(clazz.getSimpleName()) ) {
					declareType(clazz.getCanonicalName());
				} 
			}
		}
	}
	
	public void buildImports( List<String> basicAttributeTypes, List<String> linkAttributeTypes) { //throws GeneratorException {
		for ( String type : basicAttributeTypes ) {
			declareType(type); 
		}
		for ( String type : linkAttributeTypes ) {
			declareLinkType(type); 
		}
	}
	
	/**
	public void buildImports( EntityInContext entity ) throws GeneratorException {
		if ( entity != null ) {
			//--- All the attributes
			for ( AttributeInContext attribute : entity.getAttributes() ) {
				// register the type to be imported if necessary
				declareType( attribute.getFullType() ); 
			}
			//--- All the links 
			for ( LinkInContext link : entity.getLinks() ) {
				if ( link.isCardinalityOneToMany() || link.isCardinalityManyToMany() ) {
					// collection types used in your JPA 
					// "java.util.List", "java.util.Set", "java.util.Collection" 
//					imports.declareType( link.getFieldFullType() ); 
					// NEW in v 3.3.0
					String type = link.getFieldType();
					declareCollectionType(type);
					jpa.fieldType(LinkInContext link) ;
				}
				else {
					// ManyToOne or OneToOne => bean ( "Book", "Person", ... )
					// Supposed to be in the same package
				}
			}
		}
	}	
	**/
}
