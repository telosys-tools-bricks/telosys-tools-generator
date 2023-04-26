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
package org.telosys.tools.generator.context.tools;

public class LinesBuilder {
	
	private final StringBuilder sb ;	
	private final String indentationValue ;
	
	/**
	 * Default constructor (use TABS indentation)
	 */
	public LinesBuilder() {
		this("\t");
	}
	
	/**
	 * Constructor with indentation value to use
	 * @param indentationValue
	 */
	public LinesBuilder(String indentationValue) {
		super();
		this.sb = new StringBuilder() ;
		this.indentationValue = indentationValue ;
	}

	/**
	 * Appends the given line after the given indentation string
	 * @param indentationString  a string to be put before the given line 
	 * @param line
	 */
	public void append(String indentationString, String line) {
		sb.append( indentationString );
		sb.append( line );
		sb.append( "\n" );
	}

	/**
	 * Appends the given line after the indentation for the given level
	 * @param indentationLevel
	 * @param line
	 */
	public void append(int indentationLevel, String line) {
		sb.append( getIndentationForLevel(indentationLevel) );
		sb.append( line );
		sb.append( "\n" );
	}
	
	/**
	 * Returns indentation to use for the given level
	 * @param indentLevel
	 * @return
	 */
	private String getIndentationForLevel(int indentLevel) {
		StringBuilder sbIndent = new StringBuilder();
		for ( int level = 0 ; level < indentLevel ; level++ ) {
			sbIndent.append(this.indentationValue);
		}
		return sbIndent.toString();
	}

	@Override
	public String toString() {
		if ( sb.length() > 0 ) {
			// remove last "\n" if any
			int last = sb.length() - 1;
			if ( sb.charAt(last) == '\n' ) {
				sb.setLength(last);
			}
		}
		return sb.toString();
	}

}
