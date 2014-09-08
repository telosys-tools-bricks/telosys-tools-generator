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

import org.telosys.tools.generator.GeneratorUtil;

/**
 * Tool for multiple annotations ( 0 .. N lines )
 * 
 * @author Laurent GUERIN
 *
 */
public class AnnotationsBuilder {

	private StringBuffer _sbAnnotationsBuffer = null;

	private String       _sLeftMargin = "" ;
	
	private int          _iAnnotationsCount = 0 ;
	
	
	/**
	 * Constructor
	 * @param iLeftMargin
	 */
	public AnnotationsBuilder(int iLeftMargin) {
		super();
		_sbAnnotationsBuffer = new StringBuffer();
		_iAnnotationsCount = 0 ;
		_sLeftMargin = GeneratorUtil.blanks(iLeftMargin);		
	}

	/**
	 * Add a new line of annotation  <br>
	 * Put the left margin before the given string and add a CR-LF at the end
	 * @param sAnnotation
	 */
	public void addLine(String sAnnotation)
	{
		if ( _iAnnotationsCount > 0 ) {
			_sbAnnotationsBuffer.append( "\n" );
		}
		_sbAnnotationsBuffer.append( _sLeftMargin );
		_sbAnnotationsBuffer.append( sAnnotation );
		_iAnnotationsCount++;
	}

	/**
	 * Returns a string containing all the annotations built ( one annotation per line )
	 * @return
	 */
	public String getAnnotations()
	{
		return _sbAnnotationsBuffer.toString() ;
	}

}
