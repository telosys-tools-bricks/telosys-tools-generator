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

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.telosys.tools.generator.GeneratorUtil;

/**
 * Tool for multiple annotations ( 0 .. N lines )
 * 
 * @author Laurent GUERIN
 *
 */
public class AnnotationsBuilder {

	private final List<String> annotations = new LinkedList<>() ;

	private final String       leftMarginString ;
	
	
	/**
	 * Constructor
	 * @param leftMargin
	 */
	public AnnotationsBuilder(int leftMargin) {
		super();
		this.leftMarginString = GeneratorUtil.blanks(leftMargin);		
	}

	/**
	 * Get the current number of annotations
	 * @return
	 */
	public int getCount() {
		return annotations.size();
	}
	
	/**
	 * Add a new annotation
	 * @param annotation
	 */
	public void addAnnotation(String annotation) {
		annotations.add(annotation);
	}

	/**
	 * Returns a string containing all the annotations with one annotation per line
	 * @return
	 */
	public String getMultiLineAnnotations() {
        return annotations.stream()
                .map(s -> leftMarginString + s) // add left margin before each 
                .collect(Collectors.joining("\n")); // join with newline as separator
	}

	/**
	 * Returns a string containing all the annotations in a single line (separated by a space)
	 * @return
	 */
	public String getSingleLineAnnotations() {
		if ( annotations.isEmpty() ) {
			return "";
		}
		else {
	        return leftMarginString + String.join(" ", annotations); 
		}
	}

}
