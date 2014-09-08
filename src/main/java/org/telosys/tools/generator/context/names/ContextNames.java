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
package org.telosys.tools.generator.context.names;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.telosys.tools.commons.variables.Variable;
import org.telosys.tools.commons.variables.VariablesNames;
import org.telosys.tools.generator.context.names.ContextName;

/**
 * Reserved context names
 *  
 * @author L. Guerin
 *
 */
public class ContextNames {
	
	private final static String[] VOID_STRING_ARRAY = {} ;
	
	private static List<String> VARIABLES_LIST = new LinkedList<String>();
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

	private static List<String> GENERATOR_OBJECTS_LIST = new LinkedList<String>();
	static {
		//--- Invariable objects 
		GENERATOR_OBJECTS_LIST.add( ContextName.CONST ); 
		GENERATOR_OBJECTS_LIST.add( ContextName.FN );
		GENERATOR_OBJECTS_LIST.add( ContextName.JAVA ); // ver 2.0.7
		GENERATOR_OBJECTS_LIST.add( ContextName.JPA ); // ver 2.0.7
		GENERATOR_OBJECTS_LIST.add( ContextName.BEAN_VALIDATION ); // ver 2.0.7
		GENERATOR_OBJECTS_LIST.add( ContextName.GENERATION );  // ver 2.1.0
		GENERATOR_OBJECTS_LIST.add( ContextName.GENERATOR ); 
		GENERATOR_OBJECTS_LIST.add( ContextName.LOADER );
		GENERATOR_OBJECTS_LIST.add( ContextName.PROJECT );
		GENERATOR_OBJECTS_LIST.add( ContextName.MODEL ); // ver 2.0.7
		GENERATOR_OBJECTS_LIST.add( ContextName.DATABASES ); // ver 2.1.0
		GENERATOR_OBJECTS_LIST.add( ContextName.TODAY );
		GENERATOR_OBJECTS_LIST.add( ContextName.ENV ); // ver 2.1.0

		//--- Current Entity/Target objects
		GENERATOR_OBJECTS_LIST.add( ContextName.TARGET      );
		//GENERATOR_OBJECTS_LIST.add( ContextName.BEAN_CLASS  ); // old name
		GENERATOR_OBJECTS_LIST.add( ContextName.ENTITY      ); // new name
		GENERATOR_OBJECTS_LIST.add( ContextName.SELECTED_ENTITIES  );

		Collections.sort(GENERATOR_OBJECTS_LIST);
	}

	private static List<String> PREDEFINED_NAMES_LIST = new LinkedList<String>();
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
	}

//	private static List<String> WIZARDS_OBJECTS_LIST = new LinkedList<String>();
//	static {
//		//--- Invariable objects 
//		WIZARDS_OBJECTS_LIST.add( ContextName.CLASS  );
////		"context", 
////		"screendata",
////		"triggers"
//
//	}
	
	private static List<String> RESERVED_NAMES_LIST = new LinkedList<String>();
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
//		for ( String s : WIZARDS_OBJECTS_LIST ) {
//			RESERVED_NAMES_LIST.add(s);
//		}
		Collections.sort(RESERVED_NAMES_LIST);
	}
	
	private static List<String> VARIABLE_AND_OBJECT_NAMES_LIST = new LinkedList<String>();
	static {
		for ( String s : VARIABLES_LIST ) {
			VARIABLE_AND_OBJECT_NAMES_LIST.add(s);
		}
		for ( String s : GENERATOR_OBJECTS_LIST ) {
			VARIABLE_AND_OBJECT_NAMES_LIST.add(s);
		}
		Collections.sort(VARIABLE_AND_OBJECT_NAMES_LIST);
	}
	
	public final static String[] getVariableNames()
	{
		return VARIABLES_LIST.toArray( VOID_STRING_ARRAY );
	}
	
	public final static String[] getObjectNames()
	{
		return GENERATOR_OBJECTS_LIST.toArray( VOID_STRING_ARRAY );
	}
	
	public final static String[] getObjectAndVariableNames()
	{
		return VARIABLE_AND_OBJECT_NAMES_LIST.toArray( VOID_STRING_ARRAY );
	}
	
	public final static String[] getPredefinedNames()
	{
		return PREDEFINED_NAMES_LIST.toArray( VOID_STRING_ARRAY );
	}
	
	/**
	 * Returns a copy of all the variable names used in the Velocity Context
	 * @return
	 */
	public final static String[] getReservedNames()
	{
		return RESERVED_NAMES_LIST.toArray( VOID_STRING_ARRAY );
	}
	
	/**
	 * Returns a sorted copy of all the variable names used in the Velocity Context
	 * @return
	 */
	public final static String[] getSortedReservedNames()
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
	public final static boolean isReservedName(String s)
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
	 * Returns an array containing the invalid variable names, <br>
	 * or null if all the names are valid.
	 * @param variables
	 * @return
	 */
	public final static String[] getInvalidVariableNames(Variable[] variables)
	{
		LinkedList<String> invalidVariables = null ; 
		if ( null == variables ) return null ;
		for ( int i = 0 ; i < variables.length ; i++ )
		{
			Variable v = variables[i];
			String sVarName = v.getName();
			if ( isReservedName(sVarName) )
			{
				if ( null == invalidVariables )
				{
					invalidVariables = new LinkedList<String>();
				}
				invalidVariables.add(sVarName);
			}
		}
		if ( invalidVariables != null ) return (String[]) invalidVariables.toArray(new String[0]);
		else return null ;
	}
}
