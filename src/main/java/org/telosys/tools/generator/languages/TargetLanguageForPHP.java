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
import org.telosys.tools.generator.languages.literals.LiteralValuesProviderForPHP;
import org.telosys.tools.generator.languages.types.TypeConverter;
import org.telosys.tools.generator.languages.types.TypeConverterForPHP;

/**
 *  
 * @author Laurent GUERIN
 *
 */
public class TargetLanguageForPHP extends TargetLanguage {
	
	private final LiteralValuesProvider literalValuesProvider ;

	/**
	 * Constructor
	 */
	protected TargetLanguageForPHP() {
		super();
		this.literalValuesProvider = new LiteralValuesProviderForPHP();
	}

	@Override
	public TypeConverter getTypeConverter() {
		// NB create a new instance for each "get" 
		// because it can be changed at run-time with setSpecificCollectionType(..)
		return new TypeConverterForPHP();
	}

	@Override
	public LiteralValuesProvider getLiteralValuesProvider() {
		return literalValuesProvider;
	}

	@Override
	public String argumentsList(List<AttributeInContext> attributes) {
		if ( attributes == null ) return "";
		StringBuilder sb = new StringBuilder();
		int n = 0 ;
		for ( AttributeInContext attribute : attributes ) {
			// example : "function add($a, $b, $x)"
			if ( n > 0 ) sb.append(", ");
			// AFTER : the name "$xx"
			sb.append( "$" ).append( attribute.getName() ) ;
			n++;
		}
		return sb.toString();
	}
	
	@Override
	public String argumentsListWithType(List<AttributeInContext> attributes) {
		if ( attributes == null ) return "";
		StringBuilder sb = new StringBuilder();
		int n = 0 ;
		for ( AttributeInContext attribute : attributes ) {
			// example : "function add(int $a, int $b, ?string $s)"
			if ( n > 0 ) sb.append(", ");
			// FIRST : the type 
			if ( ! attribute.isNotNull() ) {
				sb.append( "?" ) ;  // nullable => add '?' before the type, eg "?int"
			}
			sb.append( attribute.getType() ) ; 
			sb.append( " " ) ;
			// AFTER : the name "$xx"
			sb.append( "$" ).append( attribute.getName() ) ;
			n++;
		}
		return sb.toString();
	}
	
	@Override
	public String argumentsListWithWrapperType(List<AttributeInContext> attributes) {
		// No wrapper type => same behavior as with basic types
		return argumentsListWithType(attributes); 
	}

	@Override
	public String argumentsListFormObjectWithGetter(String objectName, List<AttributeInContext> attributes) {
		// no getters  => just '$obj->xxx, $obj->yyy' 
		if ( attributes == null ) return "";
		StringBuilder sb = new StringBuilder();
		int n = 0 ;
		for ( AttributeInContext attribute : attributes ) {
			if ( n > 0 ) sb.append(", ");
			sb.append( "$" ) ;
			sb.append( objectName ) ;
			sb.append( "->" ) ;
			sb.append( attribute.getName() ) ; 
			n++;
		}
		return sb.toString();
	}	

}
