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

/**
 * Generator context names ( objects names in the Velocity Context )
 * 
 * @author Laurent Guerin
 *  
 */
public class ContextName {

	//--- Special characters 
	public final static String  DOLLAR = "DOLLAR" ;
	public final static String  SHARP  = "SHARP" ;
	public final static String  AMP    = "AMP";     // ampersand 
	public final static String  QUOT   = "QUOT" ;   // double quotation mark
	public final static String  LT     = "LT" ;     // less-than sign
	public final static String  GT     = "GT" ;     // greater-than sign
	public final static String  LBRACE = "LBRACE" ; // left brace
	public final static String  RBRACE = "RBRACE" ; // right brace
	
// REPLACED BY VariablestNames.XXXX ( v 2.1.0 )	
//	//--- PACKAGES predefined variables names ( v 2.0.6 )		
//	public final static String  ROOT_PKG    = "ROOT_PKG" ;
//	public final static String  ENTITY_PKG  = "ENTITY_PKG" ;
//	
//	//--- FOLDERS predefined variables names ( v 2.0.3 )		
//	public final static String  SRC      = "SRC" ;
//	public final static String  RES      = "RES" ;
//	public final static String  WEB      = "WEB" ;
//	public final static String  TEST_SRC = "TEST_SRC" ;
//	public final static String  TEST_RES = "TEST_RES" ;
//	public final static String  DOC      = "DOC" ;
//	public final static String  TMP      = "TMP" ;
	
	//--- Standard objects names always in context
	public final static String  GENERATOR        = "generator" ;
	public final static String  TODAY            = "today" ;
	public final static String  CONST            = "const" ;
	public final static String  FN               = "fn" ;	
	public final static String  LOADER           = "loader" ;
	public final static String  PROJECT          = "project" ;
	public final static String  GENERATION       = "generation" ; // ver 2.1.0
	public final static String  JAVA             = "java" ; // ver 2.0.7
	public final static String  JPA              = "jpa" ; // ver 2.0.7
	public final static String  BEAN_VALIDATION  = "beanValidation" ; // ver 2.0.7
	public final static String  ENV              = "env" ; // ver 2.1.0
	//---  
	public final static String  ENTITY            = "entity" ;    // New name 
	//public final static String  BEAN_CLASS        = "beanClass" ; // Other name for "entity" removed in ver 2.1.0
	public final static String  SELECTED_ENTITIES = "selectedEntities" ;
	public final static String  TARGET            = "target" ;
	public final static String  MODEL             = "model" ; // ver 2.0.7

	//--- Template objects names retrieved from standard objects 
	public final static String  ATTRIBUTE         = "attribute" ;
	public final static String  ATTRIB            = "attrib" ; // Other name for "attribute"
	public final static String  FIELD             = "field" ;  // Other name for "attribute"
	
	public final static String  LINK              = "link" ;
	public final static String  LINK_ATTRIBUTE    = "linkAttribute" ; // v 2.1.0
	public final static String  JOIN_COLUMN       = "joinColumn" ; // v 2.1.0
	
	public final static String  FK                = "fk" ;    // v 2.0.7
	public final static String  FKCOL             = "fkcol" ; // v 2.0.7

	public final static String  DATABASE          = "database" ; // ver 2.1.0
	public final static String  DATABASES         = "databases" ; // ver 2.1.0
	
	//---  
//	public final static String  CLASS             = "class" ;
	
}