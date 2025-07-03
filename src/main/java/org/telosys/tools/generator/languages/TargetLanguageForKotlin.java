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
import org.telosys.tools.generator.languages.literals.LiteralValuesProviderForKotlin;
import org.telosys.tools.generator.languages.types.TypeConverterForKotlin;

/**
 *  
 * @author Laurent GUERIN
 *
 */
public class TargetLanguageForKotlin extends TargetLanguage {
	
	/**
	 * Constructor
	 */
	protected TargetLanguageForKotlin() {
		super(new TypeConverterForKotlin(), new LiteralValuesProviderForKotlin());
	}

	@Override
	public String argumentsList(List<AttributeInContext> attributes) {
		return commonArgumentsListWithoutType(attributes);
	}
	
	@Override
	public String argumentsListWithType(List<AttributeInContext> attributes) {
		if ( attributes == null ) return "";
		StringBuilder sb = new StringBuilder();
		int n = 0 ;
		for ( AttributeInContext attribute : attributes ) {
			// example : "name: string?, age: int"
			if ( n > 0 ) sb.append(", ");
			sb.append( attribute.getName() ) ; // arg name first
			sb.append( ": " ) ;
			sb.append( attribute.getType() ) ; // arg type after 
			n++;
		}
		return sb.toString();
	}

	@Override
	public String argumentsListWithWrapperType( List<AttributeInContext> attributes ) {
		// No wrapper type in Kotlin => same behavior as with basic types
		return argumentsListWithType(attributes);
	}

	@Override
	public String argumentsListFromObjectWithGetter(String objectName, List<AttributeInContext> attributes) {
		// no getters => just 'obj.xxx, obj.yyy' 
		return commonArgumentsListFromObject(objectName, attributes);
	}

}
