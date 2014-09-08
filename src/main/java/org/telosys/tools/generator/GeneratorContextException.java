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
package org.telosys.tools.generator;

import org.apache.velocity.util.introspection.Info;

/**
 * Exception thrown if an error occurs when using the context variables  <br>
 * in the Velocity Template (invalid method invocation, etc )
 * 
 * @author Laurent GUERIN
 * 
 */
public class GeneratorContextException extends RuntimeException 
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final int    line ;
	private final String templateName ;

	/**
     * @param message
     */
    public GeneratorContextException(String message)
    {
        super(message);
        line = 0 ;
        templateName = "" ;
    }

//    /**
//     * @param message
//     * @param cause
//     */
//    public GeneratorContextException(String message, Throwable cause)
//    {
//        super(message, cause);
//    }

    /**
     * @param message
     * @param introspectionInfo
     * @since 2.0.7
     */
    public GeneratorContextException(String message, Info introspectionInfo )
    {
        super(message);
        this.line = introspectionInfo.getLine() ;
        this.templateName = introspectionInfo.getTemplateName() ;
    }
    
    /**
     * Returns the template line number where the error occurred ( or "" if the template is unknown )
     * @return
     * @since 2.0.7
     */
    public String getTemplateName() {
    	return this.templateName ; 
    }
    
    /**
     * Returns the template line number where the error occurred ( or 0 if the line is unknown )
     * @return
     * @since 2.0.7
     */
    public int getLineNumber() {
    	return this.line ;
    }
}