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

import org.telosys.tools.generator.GeneratorUtil;

/**
 * Tool for multiple annotations ( 0 .. N lines )
 * 
 * @author Laurent GUERIN
 *
 */
public class AnnotationsBuilder {

	private final StringBuilder stringBuilder ;

	private final String        leftMarginString ;
	
	private int           annotationsCount = 0 ;
	
	
	/**
	 * Constructor
	 * @param leftMargin
	 */
	public AnnotationsBuilder(int leftMargin) {
		super();
		this.stringBuilder = new StringBuilder();
		this.annotationsCount = 0 ;
		this.leftMarginString = GeneratorUtil.blanks(leftMargin);		
	}

	/**
	 * Add a new line of annotation  <br>
	 * Put the left margin before the given string and add a CR-LF at the end
	 * @param sAnnotation
	 */
	public void addLine(String sAnnotation)
	{
		if ( annotationsCount > 0 ) {
			stringBuilder.append( "\n" );
		}
		stringBuilder.append( leftMarginString );
		stringBuilder.append( sAnnotation );
		annotationsCount++;
	}

	/**
	 * Returns a string containing all the annotations built ( one annotation per line )
	 * @return
	 */
	public String getAnnotations()
	{
		return stringBuilder.toString() ;
	}

}
