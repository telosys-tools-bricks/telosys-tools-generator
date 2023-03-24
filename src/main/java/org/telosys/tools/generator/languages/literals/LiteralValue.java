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
package org.telosys.tools.generator.languages.literals;

/**
 * Literal value <br>
 * This class is used in generator context (keep it public)
 * 
 * @author Laurent GUERIN
 *
 */
public class LiteralValue {
	
	private final String languageLiteralValue ;

	private final Object basicValue ;

	/**
	 * Constructor
	 * @param literalValue 
	 * @param basicValue  real value to be used to build JSON, URI, etc (can be null if not applicable)
	 */
	protected LiteralValue(String literalValue, Object basicValue) {
		super();
		this.languageLiteralValue = literalValue;
		this.basicValue = basicValue;
	}

	/**
	 * Returns the literal value adapted for the current target language 
	 * @return
	 */
	public String getCurrentLanguageValue() {
		return languageLiteralValue;
	}

	/**
	 * Returns the basic value used to build the literal value <br>
	 * The actual value is used in some cases by the generator : <br>
	 *  - $values.toJSON ( build an JSON flow containing the value )<br>
	 *  - $values.toURI  ( build an URI using the value ) <br>
	 * @return object representing the value (String, Short, Float, Boolean, etc) or null if none
	 */
	public Object getBasicValue() {
		return basicValue;
	}

	@Override
	public String toString() {
		// returns the original single value for backward compatibility
		return languageLiteralValue ;
	}
	
}
