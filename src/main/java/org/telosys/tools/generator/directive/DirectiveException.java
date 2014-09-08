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
package org.telosys.tools.generator.directive;

/**
 * Exception thrown by a specific directive  <br>
 * 
 * @author Laurent GUERIN
 * 
 */
public class DirectiveException extends RuntimeException 
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String directiveName ;
	private final String template ;
	private final int    line ;
	
	/**
     * @param message
     */
    public DirectiveException(String message, String directiveName, String template, int line )
    {
        super(message);
        this.directiveName = directiveName ;
        this.template = template ;
        this.line = line ;
    }

    public String getDirectiveName() {
    	return directiveName ;
    }

    public String getTemplateName() {
    	return template ;
    }

    public int getLineNumber() {
    	return line ;
    }
}