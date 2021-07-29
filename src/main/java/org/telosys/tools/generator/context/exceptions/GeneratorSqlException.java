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
package org.telosys.tools.generator.context.exceptions;

/**
 * Exception thrown by '$fn' object
 * 
 * @author Laurent GUERIN
 * 
 */
public class GeneratorSqlException extends RuntimeException 
{
	private static final long serialVersionUID = 1L;
	
    /**
     * @param message
     */
    public GeneratorSqlException(String message) {
        super("$sql : " + message);
    }

    /**
     * @param functionName
     * @param message
     */
    public GeneratorSqlException(String functionName, String message) {
        super("$sql."+functionName + " : " + message);
    }

}