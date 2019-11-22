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

import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.names.ContextName;
import org.telosys.tools.generic.model.types.NeutralType;

//-------------------------------------------------------------------------------------
@VelocityObject(
	contextName=ContextName.HTML,
	text = { 
		"Object providing a set of utility functions for HTML language code generation",
		""
	},
	since = "3.0.0"
 )
//-------------------------------------------------------------------------------------
public class HtmlInContext {

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the HTML5 'type' attribute for 'input' tag ",
			"The 'type' is determinded from the 'input type' if any or computed from the model type ",
			"e.g : type='text', type='number', type='date', type='time', etc "
			},
		parameters = { 
				"attribute : the attribute holding the 'type' information "
			},
		example = {
				"$html.type($attribute) " 
			}
		)
    public String type( AttributeInContext attribute ) {
		String type = attribute.getInputType() ; // by default
		if ( StrUtil.nullOrVoid(type) ) {
			// input type not defined => try to compute it
			type = computeType( attribute ); 
		}
		if ( ! StrUtil.nullOrVoid(type) ) {
			return "type=\"" + type + "\"" ;
		}
		return "";
    }
    
	private String computeType( AttributeInContext attribute  ) {
		
		// HTML5 types examples : 
		// <input type="text" > 
		// <input type="checkbox" value="Bike"> 
		// <input type="number" step="8"> 
		// <input type="number" step="8" value="0" min="0" max="64">
		// , number, date, time, datetime
		String attributeType = attribute.getNeutralType();
		String type = "text" ; // "text" by default
		if (    NeutralType.BYTE.equals(attributeType) 
			 || NeutralType.SHORT.equals(attributeType) 	
			 || NeutralType.INTEGER.equals(attributeType) 	
			 || NeutralType.LONG.equals(attributeType) 	
			 ) {
			type = "number" ;
		}
		else if ( NeutralType.DATE.equals(attributeType) ) {
			type = "date" ;
		}
		else if ( NeutralType.TIME.equals(attributeType) ) {
			type = "time" ;
		}
		else if ( NeutralType.TIMESTAMP.equals(attributeType) ) {
			type = "datetime" ;
		}
		else if ( NeutralType.BOOLEAN.equals(attributeType) ) {
			type = "checkbox" ;
		}
		return type;
    }
    
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the HTML5 'maxlength' attribute if any or if computable from type",
			"(else returns a void string)",
			"e.g : maxlength='20' "
			},
		parameters = { 
				"attribute : the attribute holding the 'maxlength' information "
			},
		example = {
				"$html.maxlength($attribute) " 
			}
		)
    public String maxlength( AttributeInContext attribute ) {
		String maxlengthValue = attribute.getMaxLength();
		if ( StrUtil.nullOrVoid(maxlengthValue) ) {
			// max length not defined => try to compute it
			maxlengthValue = computeMaxlength( attribute.getNeutralType() );
		}
		if ( ! StrUtil.nullOrVoid(maxlengthValue) ) {
			return "maxlength=\"" + maxlengthValue + "\"" ;
		}
		return "";
    }
    private String computeMaxlength( String attributeType ) {
    	//--- Max length depending on the Java type
    	if ( NeutralType.BYTE.equals(attributeType) ) return  "4" ; // -128 to +127
    	if ( NeutralType.SHORT.equals(attributeType) ) return  "6" ; // -32768 to +32767
    	if ( NeutralType.INTEGER.equals(attributeType) ) return "11" ; // -2147483648 to +2147483647
    	if ( NeutralType.LONG.equals(attributeType) ) return "20" ; // -9223372036854775808 to +9223372036854775807
    	
    	if ( NeutralType.FLOAT.equals(attributeType) ) return "20" ; // Arbitrary fixed value
    	if ( NeutralType.DOUBLE.equals(attributeType) ) return "20" ; // Arbitrary fixed value
    	if ( NeutralType.DECIMAL.equals(attributeType) ) return "20" ; // Arbitrary fixed value
    	
    	if ( NeutralType.DATE.equals(attributeType) ) return "10" ; // "YYYY-MM-DD", "DD/MM/YYYY", etc ...
    	if ( NeutralType.TIME.equals(attributeType)) return "8" ; // "HH:MM:SS"
    	return "";
    }
    
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the HTML5 'min' attribute for minimum values (or void if none)",
			"e.g : min='10' "
			},
		parameters = { 
				"attribute : the attribute holding the 'min' information "
			},
		example = {
				"$html.min($attribute) " 
			}
	)
    public String min( AttributeInContext attribute ) {
		String minValue = attribute.getMinValue();
		if ( ! StrUtil.nullOrVoid(minValue) ) {
			return "min=\"" + minValue + "\"" ;
		}
		return "";
    }

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the HTML5 'max' attribute for maximum values (or void if none)",
			"e.g : max='10' "
			},
		parameters = { 
				"attribute : the attribute holding the 'max' information "
			},
		example = {
				"$html.max($attribute) " 
			}
	)
    public String max( AttributeInContext attribute ) {
		String maxValue = attribute.getMaxValue();
		if ( ! StrUtil.nullOrVoid(maxValue) ) {
			return "max=\"" + maxValue + "\"" ;
		}
		return "";
    }
}
