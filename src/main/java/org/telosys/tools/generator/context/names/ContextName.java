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

/**
 * Generator context names ( objects names in the Velocity Context )
 * 
 * @author Laurent Guerin
 *  
 */
public class ContextName {
	
	/**
	 * Private constructor
	 */
	private ContextName() {
	}

	//--- Special characters 
	public static final String  DOLLAR  = "DOLLAR" ;
	public static final String  SHARP   = "SHARP" ;
	public static final String  AMP     = "AMP";     // ampersand 
	public static final String  QUOT    = "QUOT" ;   // double quotation mark
	public static final String  LT      = "LT" ;     // less-than sign
	public static final String  GT      = "GT" ;     // greater-than sign
	public static final String  LBRACE  = "LBRACE" ; // left brace
	public static final String  RBRACE  = "RBRACE" ; // right brace
	public static final String  NEWLINE = "NEWLINE" ; // new line character ( '\n' in Java ) #LGU 2017-08-16
	public static final String  TAB     = "TAB" ;     // tabulation character ( '\t' in Java ) #LGU 2017-08-16
		
	//--- Standard objects names always in context
	public static final String  GENERATOR         = "generator" ;
	public static final String  TODAY             = "today" ; // Deprecated since "now" in v 3.3.0
	public static final String  CONST             = "const" ;
	public static final String  FN                = "fn" ;	
	public static final String  LOADER            = "loader" ;
	public static final String  PROJECT           = "project" ;
	public static final String  HTML              = "html" ; // ver 3.0.0
	public static final String  JAVA              = "java" ; // ver 2.0.7
	public static final String  JPA               = "jpa" ; // ver 2.0.7
	public static final String  BEAN_VALIDATION   = "beanValidation" ; // ver 2.0.7
	public static final String  ENV               = "env" ; // ver 2.1.0
	public static final String  JDBC              = "jdbc" ; // ver 2.1.1
	public static final String  JDBC_FACTORY      = "jdbcFactory" ; // ver 2.1.1
	public static final String  H2                = "h2" ;	// ver 2.1.1
	public static final String  NOW               = "now" ; // ver 3.3.0
	//---  
	public static final String  ENTITY            = "entity" ;   
	public static final String  SELECTED_ENTITIES = "selectedEntities" ;
	public static final String  TARGET            = "target" ;
	public static final String  MODEL             = "model" ; // ver 2.0.7
	public static final String  BUNDLE            = "bundle" ; // ver 3.3.0

	//--- Template objects names retrieved from standard objects 
	public static final String  ATTRIBUTE         = "attribute" ;
	public static final String  ATTRIB            = "attrib" ; // Other name for "attribute"
	public static final String  FIELD             = "field" ;  // Other name for "attribute"
	
	public static final String  LINK              = "link" ;
	public static final String  LINK_ATTRIBUTE    = "linkAttribute" ; // v 2.1.0
	public static final String  JOIN_COLUMN       = "joinColumn" ; // v 2.1.0 (TODO : remove)
	public static final String  JOIN_ATTRIBUTE    = "joinAttribute" ; // v 3.4.0 
	public static final String  JOIN_TABLE        = "joinTable" ; // v 3.3.0 (added for documentation)
	
	public static final String  FK                = "fk" ;    // v 2.0.7
	public static final String  FKCOL             = "fkcol" ; // v 2.0.7  (TODO : remove)
	public static final String  FK_ATTRIBUTE      = "fkAttribute" ; // v 3.4.0

	public static final String  DATABASE          = "database" ; // ver 2.1.0
	public static final String  DATABASES         = "databases" ; // ver 2.1.0
	
	public static final String  VALUES            = "values" ; // ver 3.0.0
	public static final String  KEY_VALUES        = "keyValues" ; // ver 3.0.0
	public static final String  DATA_VALUES       = "dataValues" ; // ver 3.0.0

	public static final String  FILE              = "file" ; // ver 3.3.0
	public static final String  FKPART            = "fkPart" ; // ver 3.3.0
	
	public static final String  SQL               = "sql" ;     // ver 3.4.0
	public static final String  FACTORY           = "factory" ; // ver 3.4.0
	public static final String  REFERENCE         = "reference" ; // ver 3.4.0
	
	// Don't forget to also add :
	//   - the name in "ContextNames"
	//   - the class in "doc.tooling.ObjectsList"
	
}