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
package org.telosys.tools.generator.context.names;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.telosys.tools.commons.variables.Variable;
import org.telosys.tools.commons.variables.VariablesNames;

/**
 * Reserved context names
 *  
 * @author L. Guerin
 *
 */
public class ContextNames {
	
	private static final String[] VOID_STRING_ARRAY = {} ;
	
	private static final List<String> VARIABLES_LIST = new LinkedList<>();
	static {
		//--- Special characters 
		VARIABLES_LIST.add( ContextName.DOLLAR );
		VARIABLES_LIST.add( ContextName.SHARP  );
		VARIABLES_LIST.add( ContextName.AMP    );
		VARIABLES_LIST.add( ContextName.QUOT   );
		VARIABLES_LIST.add( ContextName.LT     );
		VARIABLES_LIST.add( ContextName.GT     );
		VARIABLES_LIST.add( ContextName.LBRACE );
		VARIABLES_LIST.add( ContextName.RBRACE );
		
		//--- PACKAGES predefined variables names ( v 2.0.6 )
		VARIABLES_LIST.add( VariablesNames.ROOT_PKG );
		VARIABLES_LIST.add( VariablesNames.ENTITY_PKG );
		
		//--- FOLDERS predefined variables names ( v 2.0.3 )
		VARIABLES_LIST.add( VariablesNames.SRC );
		VARIABLES_LIST.add( VariablesNames.RES );
		VARIABLES_LIST.add( VariablesNames.WEB );
		VARIABLES_LIST.add( VariablesNames.TEST_SRC );
		VARIABLES_LIST.add( VariablesNames.TEST_RES );
		VARIABLES_LIST.add( VariablesNames.DOC );
		VARIABLES_LIST.add( VariablesNames.TMP );
		
		Collections.sort(VARIABLES_LIST);
	}

	
	private static final List<String> GENERATOR_OBJECTS_LIST = new LinkedList<>();
	static {
		//--- Invariable objects 
		GENERATOR_OBJECTS_LIST.add( ContextName.CONST ); 
		GENERATOR_OBJECTS_LIST.add( ContextName.FN );
		GENERATOR_OBJECTS_LIST.add( ContextName.JAVA ); // ver 2.0.7
		GENERATOR_OBJECTS_LIST.add( ContextName.JPA ); // ver 2.0.7
		GENERATOR_OBJECTS_LIST.add( ContextName.BEAN_VALIDATION ); // ver 2.0.7
		GENERATOR_OBJECTS_LIST.add( ContextName.GENERATOR ); 
		GENERATOR_OBJECTS_LIST.add( ContextName.LOADER );
		GENERATOR_OBJECTS_LIST.add( ContextName.PROJECT );
		GENERATOR_OBJECTS_LIST.add( ContextName.MODEL ); // ver 2.0.7
		GENERATOR_OBJECTS_LIST.add( ContextName.DATABASES ); // ver 2.1.0
		GENERATOR_OBJECTS_LIST.add( ContextName.TODAY );
		GENERATOR_OBJECTS_LIST.add( ContextName.ENV ); // ver 2.1.0
		GENERATOR_OBJECTS_LIST.add( ContextName.JDBC_FACTORY ); // ver 2.1.1
		GENERATOR_OBJECTS_LIST.add( ContextName.H2 ); // ver 2.1.1
		GENERATOR_OBJECTS_LIST.add( ContextName.HTML ); // ver 3.0.0

		//--- Current Entity/Target objects
		GENERATOR_OBJECTS_LIST.add( ContextName.TARGET      );
		GENERATOR_OBJECTS_LIST.add( ContextName.ENTITY      ); // new name
		GENERATOR_OBJECTS_LIST.add( ContextName.SELECTED_ENTITIES  );
		
		//--- Version 3.4.0
		GENERATOR_OBJECTS_LIST.add( ContextName.FACTORY ); // v 3.4.0


		Collections.sort(GENERATOR_OBJECTS_LIST);
	}

	private static final List<String> PREDEFINED_NAMES_LIST = new LinkedList<>();
	static {
		PREDEFINED_NAMES_LIST.add( ContextName.ATTRIBUTE ); 
		PREDEFINED_NAMES_LIST.add( ContextName.ATTRIB ); 
		PREDEFINED_NAMES_LIST.add( ContextName.FIELD ); 
				
		PREDEFINED_NAMES_LIST.add( ContextName.LINK ); 

		PREDEFINED_NAMES_LIST.add( ContextName.FK ); // v 2.0.7
		PREDEFINED_NAMES_LIST.add( ContextName.FKCOL ); // v 2.0.7

		PREDEFINED_NAMES_LIST.add( ContextName.DATABASE ); // v 2.1.0
		PREDEFINED_NAMES_LIST.add( ContextName.LINK_ATTRIBUTE ); // v 2.1.0
		PREDEFINED_NAMES_LIST.add( ContextName.JOIN_COLUMN ); // v 2.1.0

		PREDEFINED_NAMES_LIST.add( ContextName.JDBC ); // v 2.1.1

		PREDEFINED_NAMES_LIST.add( ContextName.VALUES ); // v 3.0.0
		PREDEFINED_NAMES_LIST.add( ContextName.KEY_VALUES ); // v 3.0.0
		PREDEFINED_NAMES_LIST.add( ContextName.DATA_VALUES ); // v 3.0.0

		PREDEFINED_NAMES_LIST.add( ContextName.JOIN_TABLE ); // v 3.3.0
		PREDEFINED_NAMES_LIST.add( ContextName.FILE );   // v 3.3.0 
		PREDEFINED_NAMES_LIST.add( ContextName.FKPART ); // v 3.3.0
		
		PREDEFINED_NAMES_LIST.add( ContextName.SQL );     // v 3.4.0		
	}

	private static final List<String> RESERVED_NAMES_LIST = new LinkedList<>();
	static {
		for ( String s : VARIABLES_LIST ) {
			RESERVED_NAMES_LIST.add(s);
		}
		for ( String s : GENERATOR_OBJECTS_LIST ) {
			RESERVED_NAMES_LIST.add(s);
		}
		for ( String s : PREDEFINED_NAMES_LIST ) {
			RESERVED_NAMES_LIST.add(s);
		}
		Collections.sort(RESERVED_NAMES_LIST);
	}
	
	private static final List<String> VARIABLE_AND_OBJECT_NAMES_LIST = new LinkedList<>();
	static {
		for ( String s : VARIABLES_LIST ) {
			VARIABLE_AND_OBJECT_NAMES_LIST.add(s);
		}
		for ( String s : GENERATOR_OBJECTS_LIST ) {
			VARIABLE_AND_OBJECT_NAMES_LIST.add(s);
		}
		Collections.sort(VARIABLE_AND_OBJECT_NAMES_LIST);
	}
	
	/**
	 * Private constructor
	 */
	private ContextNames() {
		
	}
	
	public static final String[] getVariableNames()
	{
		return VARIABLES_LIST.toArray( VOID_STRING_ARRAY );
	}
	
	public static final String[] getObjectNames()
	{
		return GENERATOR_OBJECTS_LIST.toArray( VOID_STRING_ARRAY );
	}
	
	public static final String[] getObjectAndVariableNames()
	{
		return VARIABLE_AND_OBJECT_NAMES_LIST.toArray( VOID_STRING_ARRAY );
	}
	
	public static final String[] getPredefinedNames()
	{
		return PREDEFINED_NAMES_LIST.toArray( VOID_STRING_ARRAY );
	}
	
	/**
	 * Returns a copy of all the variable names used in the Velocity Context
	 * @return
	 */
	public static final String[] getReservedNames()
	{
		return RESERVED_NAMES_LIST.toArray( VOID_STRING_ARRAY );
	}
	
	/**
	 * Returns a sorted copy of all the variable names used in the Velocity Context
	 * @return
	 */
	public static final String[] getSortedReservedNames()
	{
		String[] names = getReservedNames() ;
		Arrays.sort(names);
		return names ;
	}
	
	/**
	 * Returns true if the given string is a variable name used in the Velocity Context
	 * @param s
	 * @return
	 */
	public static final boolean isReservedName(String s)
	{
		if ( s != null ) {
			for ( String reserved : RESERVED_NAMES_LIST ) {
				if ( s.equals( reserved ) ) {
					return true ;
				}
			}
		}
		return false ;
	}
	
	/**
	 * Returns a list of invalid variable names, <br>
	 * or null if all the names are valid.
	 * @param variables
	 * @return
	 */
	public static final List<String> getInvalidVariableNames(List<Variable> variables) // v 3.0.0  (use List)
	{
		if ( variables == null ) return null ;
		LinkedList<String> invalidVariableNames = null ; 
		for ( Variable v : variables ) {
			String variableName = v.getName();
			if ( isReservedName(variableName) ) { // invalid
				// add the variable name in the list
				if ( invalidVariableNames == null ) {
					invalidVariableNames = new LinkedList<>();
				}
				invalidVariableNames.add(variableName);
			}
		}
		return invalidVariableNames ;
	}
}
