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

import org.telosys.tools.generator.context.doc.VelocityConstant;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.names.ContextName;
import org.telosys.tools.generic.model.enums.DateType;

/**
 * Constants usable in a Velocity template
 * 
 * @author Laurent GUERIN
 *
 */
//-------------------------------------------------------------------------------------
@VelocityObject(
		contextName= ContextName.CONST ,
		text = "Object providing a set of constants ",
		since = "2.0.3"
 )
//-------------------------------------------------------------------------------------
public class ConstInContext {

	//--- Attributes filter criteria
	public static final int KEY      = 1 ;
	public static final int NOT_KEY  = 2 ;
	
	public static final int TEXT     = 4 ;
	public static final int NOT_TEXT = 8 ;
	
	public static final int IN_LINKS     = 16 ;
	public static final int NOT_IN_LINKS = 32 ;
	
	public static final int IN_SELECTED_LINKS      =  64 ;
	public static final int NOT_IN_SELECTED_LINKS  = 128 ;
	
	@VelocityConstant
	public int getKEY() {
		return KEY ;
	}
	@VelocityConstant
	public int getNOT_KEY() {
		return NOT_KEY ;
	}

	@VelocityConstant
	public int getTEXT() {
		return    TEXT ;
	}
	@VelocityConstant
	public int getNOT_TEXT() {
		return    NOT_TEXT ;
	}
	
	@VelocityConstant
	public int getIN_LINKS() {
		return    IN_LINKS ;
	}
	@VelocityConstant
	public int getNOT_IN_LINKS() {
		return    NOT_IN_LINKS ;
	}
	
	@VelocityConstant
	public int getIN_SELECTED_LINKS() {
		return    IN_SELECTED_LINKS ;
	}
	@VelocityConstant
	public int getNOT_IN_SELECTED_LINKS() {
		return    NOT_IN_SELECTED_LINKS ;
	}
	
	
	@VelocityConstant
	public int getNO_DATE_TYPE() {
		return DateType.UNDEFINED.getValue() ; // v 3.0.0
	}
	@VelocityConstant
	public int getDATE_ONLY() {
		return DateType.DATE_ONLY.getValue() ; // v 3.0.0
	}
	@VelocityConstant
	public int getTIME_ONLY() {
		return DateType.TIME_ONLY.getValue() ; // v 3.0.0
	}
	@VelocityConstant
	public int getDATE_AND_TIME() {
		return DateType.DATE_AND_TIME.getValue() ; // v 3.0.0
	}

}
