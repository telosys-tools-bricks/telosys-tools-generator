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
package org.telosys.tools.generator.languages;

import java.util.List;

import org.telosys.tools.generator.context.AttributeInContext;
import org.telosys.tools.generator.languages.literals.LiteralValuesProvider;
import org.telosys.tools.generator.languages.types.TypeConverter;

/**
 *  
 * @author Laurent GUERIN
 *
 */
public abstract class TargetLanguage {
	
	
	/**
	 * Constructor
	 * @param typeConverter
	 * @param literalValuesProvider
	 */
	protected TargetLanguage() {
		super();
	}

	/**
	 * Returns the TypeConverter 
	 * @return
	 */
	public abstract TypeConverter getTypeConverter();

	/**
	 * Returns the LiteralValuesProvider 
	 * @return
	 */
	public abstract LiteralValuesProvider getLiteralValuesProvider();

	/**
	 * Builds a simple arguments list with only arguments name (no type)
	 * @param attributes
	 * @return
	 */
	protected String argumentsList(List<AttributeInContext> attributes) {
		if ( attributes == null ) return "";
		// No type, example : "name, age"
		StringBuilder sb = new StringBuilder();
		int n = 0 ;
		for ( AttributeInContext field : attributes ) {
			// example : "name string, age int"
			if ( n > 0 ) sb.append(", ");
			sb.append( field.getName() ) ; 
			n++;
		}
		return sb.toString();
	}
	
	/**
	 * Build arguments list (override this method in language if necessary)<br>
	 * Returns a list of arguments with : argument type and argument name <br>
	 * @param attributes
	 * @return
	 */
	public String argumentsListWithType( List<AttributeInContext> attributes ) {
		if ( attributes == null ) return "";
		StringBuilder sb = new StringBuilder();
		int n = 0 ;
		for ( AttributeInContext attribute : attributes ) {
			if ( n > 0 ) sb.append(", ");
			sb.append( attribute.getType() ) ; // arg type first
			sb.append( " " ) ;
			sb.append( attribute.getName() ) ; // arg name after
			n++;
		}
		return sb.toString();
	}

	/**
	 * Build arguments list (override this method in language if necessary)<br>
	 * Returns a list of arguments with : argument type and argument name <br>
	 * @param attributes
	 * @return
	 */
	public String argumentsListWithWrapperType( List<AttributeInContext> attributes ) {
		if ( attributes == null ) return "";
		StringBuilder sb = new StringBuilder();
		int n = 0 ;
		for ( AttributeInContext attribute : attributes ) {
			if ( n > 0 ) sb.append(", ");
			sb.append( attribute.getWrapperType() ) ; //  arg type first : WRAPPER type 
			sb.append( " " ) ;
			sb.append( attribute.getName() ) ; // arg name after
			n++;
		}
		return sb.toString();		
	}
}
