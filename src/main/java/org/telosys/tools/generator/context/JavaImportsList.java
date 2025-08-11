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

import org.telosys.tools.generator.context.tools.JavaTypeUtil;

/**
 * List of full java class names that need imports<br>
 * eg : "java.util.Date", "java.math.BigDecimal", etc ...
 * 
 * @author Laurent GUERIN
 *
 */
public class JavaImportsList {

	private final LinkedList<String> imports = new LinkedList<>() ; // List of Java "full types" to import ( eg : "java.math.BigDecimal" )
	
	/**
	 * Constructor
	 */
	public JavaImportsList() {
		super();
	}
	
	/**
	 * Declare the given type : stored only if needed ( requires import and not yet stored )
	 * @param fullTypeName 
	 */
	public void declareType(String fullTypeName ) {
		if ( JavaTypeUtil.needsImport(fullTypeName) && ( ! imports.contains(fullTypeName) ) ) {
			imports.add(fullTypeName);
		}
	}
	
	private static final String JAVA_UTIL_DATE = "java.util.Date" ;
	private static final String JAVA_SQL_DATE  = "java.sql.Date" ;
	/**
	 * Removes the "collided types" <br>
	 * e.g. it's impossible to import both "java.util.Date" and "java.sql.Date" 
	 */
	private void removeCollidedTypes() {
		if ( imports.contains(JAVA_UTIL_DATE) && imports.contains(JAVA_SQL_DATE) ) {
			imports.remove(JAVA_UTIL_DATE) ;
			imports.remove(JAVA_SQL_DATE) ;
		}		
	}
	
	/**
	 * Returns a list with all imports "ready to use" <br>
	 * ( sorted and without collided types )
	 * @return
	 */
	public List<String> getFinalImportsList() {
		removeCollidedTypes();
		java.util.Collections.sort(imports);
		return imports ;		
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
	
	public void buildImports( List<String> basicAttributeTypes, List<String> linkAttributeTypes) {
		for ( String type : basicAttributeTypes ) {
			declareType(type); 
		}
		for ( String type : linkAttributeTypes ) {
			declareLinkType(type); 
		}
	}
	
}
