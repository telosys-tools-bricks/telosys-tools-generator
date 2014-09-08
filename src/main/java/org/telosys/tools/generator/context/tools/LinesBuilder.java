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
package org.telosys.tools.generator.context.tools;

public class LinesBuilder {
	
	final static int TYPE_TABS   = 1 ;
	final static int TYPE_SPACES = 2 ;

	private final StringBuilder sb ;
	
	private final int type ;
	private final int indentationSize ;
	
	/**
	 * Constructor for TABS indentation
	 */
	public LinesBuilder() {
		super();
		this.sb = new StringBuilder() ;
		this.type = TYPE_TABS ;
		this.indentationSize = 0; // Not used
	}
	
	/**
	 * Constructor for SPACES indentation
	 * @param indentationSize
	 */
	public LinesBuilder(int indentationSize) {
		super();
		this.sb = new StringBuilder() ;
		this.type = TYPE_SPACES ;
		this.indentationSize = indentationSize; // Number of spaces
	}

	public void append(int indentationLevel, String line) {
		sb.append( getIndentation(indentationLevel) );
		sb.append( line );
		sb.append( "\n" );
	}

	@Override
	public String toString() {
		return sb.toString();
	}

	public String getIndentation(int indentLevel) {
		if ( TYPE_SPACES == type ) {
			return getIndentationWithSpaces(indentLevel);
		}
		else {
			return getIndentationWithTabulations(indentLevel);
		}
	}

	private String getIndentationWithSpaces(int indentLevel) {
		StringBuilder sb = new StringBuilder();
		for ( int level = 0 ; level < indentLevel ; level++ ) {
			for ( int n = 0 ; n < indentationSize ; n++ ) {
				sb.append(" ");
			}
		}
		return sb.toString();
	}

	private String getIndentationWithTabulations(int indentLevel) {
		StringBuilder sb = new StringBuilder();
		for ( int level = 0 ; level < indentLevel ; level++ ) {
			sb.append("\t");
		}
		return sb.toString();
	}
	
}
