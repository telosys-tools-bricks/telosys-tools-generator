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
import org.telosys.tools.generator.languages.literals.LiteralValuesProviderForCSharp;
import org.telosys.tools.generator.languages.types.TypeConverter;
import org.telosys.tools.generator.languages.types.TypeConverterForCSharp;

/**
 *  
 * @author Laurent GUERIN
 *
 */
public class TargetLanguageForCSharp extends TargetLanguage {
	
	private final LiteralValuesProvider literalValuesProvider ;

	/**
	 * Constructor
	 */
	protected TargetLanguageForCSharp() {
		super();
		this.literalValuesProvider = new LiteralValuesProviderForCSharp();
	}

	@Override
	public TypeConverter getTypeConverter() {
		// NB create a new instance for each "get" 
		// because it can be changed at run-time with setSpecificCollectionType(..)
		return new TypeConverterForCSharp();
	}

	@Override
	public LiteralValuesProvider getLiteralValuesProvider() {
		return literalValuesProvider;
	}

	@Override
	public String argumentsList(List<AttributeInContext> attributes) {
		return commonArgumentsListWithoutType(attributes);
	}
	
	@Override
	public String argumentsListWithType(List<AttributeInContext> attributes) {
		if ( attributes == null ) return "";
		// example : "name string, age int"
		StringBuilder sb = new StringBuilder();
		int n = 0 ;
		for ( AttributeInContext attribute : attributes ) {
			// example : "name string, age int"
			if ( n > 0 ) sb.append(", ");
			sb.append( attribute.getType() ) ; // arg type FIRST 
			if ( ! attribute.isNotNull() ) {
				sb.append( "?" ) ;  // nullable => add '?' at the end of the type, eg "int?"
			}
			sb.append( " " ) ;
			sb.append( attribute.getName() ) ; // arg name AFTER
			n++;
		}
		return sb.toString();
	}
	
	@Override
	public String argumentsListWithWrapperType(List<AttributeInContext> attributes) {
		if ( attributes == null ) return "";
		StringBuilder sb = new StringBuilder();
		int n = 0 ;
		for ( AttributeInContext attribute : attributes ) {
			if ( n > 0 ) sb.append(", ");
			sb.append( attribute.getWrapperType() ) ; //  arg type first : WRAPPER type 
			if ( ! attribute.isNotNull() ) {
				sb.append( "?" ) ;  // nullable => add '?' at the end of the type, eg "String?"
			}
			sb.append( " " ) ;
			sb.append( attribute.getName() ) ; // arg name after
			n++;
		}
		return sb.toString();		
	}

}
