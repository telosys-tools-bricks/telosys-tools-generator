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
package org.telosys.tools.generator.events;

import org.apache.velocity.app.event.InvalidReferenceEventHandler;
import org.apache.velocity.context.Context;
import org.apache.velocity.util.introspection.Info;
import org.telosys.tools.generator.GeneratorContextException;

/**
 * Velocity User Guide :
 * Normally, when a template contains a bad reference an error message is logged and 
 * (unless it is part of a #set or #if), the reference is included verbatim in a page. 
 * With the InvalidReferenceEventHandler this behavior can be changed. 
 * Substitute values can be inserted, invalid references may be logged, or an exception can be thrown. 
 * Multiple InvalidReferenceEventHandler's may be chained. 
 * The exact manner in which chained method calls behave will differ per method. (See the javadoc for the details).
 * 
 * @author Laurent Guerin
 *
 */
public class InvalidReferenceEventImpl implements InvalidReferenceEventHandler {

	/**
	 * Called when object is null or there is no getter for the given property. 
	 * Also called for invalid references without properties. 
	 * invalidGetMethod() will be called in sequence for each link in the chain until the first non-null value is returned. 
	 * REM LGU : Also called when the getter returns null
	 */
	public Object invalidGetMethod(Context context, String reference,
			Object object, String property, Info info) {
		if ( "$const.NULL".equals(reference) ) return null ;
		throw new GeneratorContextException( errorMsg(info) + reference + " : get '" + property + "'", info);
	}

	/**
	 * Called when object is null or there is no setter for the given property. 
	 * invalidSetMethod() will be called in sequence for each link in the chain until a true value is returned. 
	 * It's recommended that false be returned as a default to allow for easy chaining. 
	 */
	public boolean invalidSetMethod(Context context, String leftreference,
			String rightreference, Info info) {
		throw new GeneratorContextException( errorMsg(info) + " : set '" + leftreference + "' - '" + rightreference + "'", info);
	}

	/**
	 * Called when object is null or the given method does not exist. 
	 * invalidMethod() will be called in sequence for each link in the chain until the first non-null value is returned. 
	 */
	public Object invalidMethod(Context context, String reference, Object object,
			String method, Info info) {
		throw new GeneratorContextException( errorMsg(info) + reference + " : method '" + method + "'", info );
	}
	
	private String errorMsg(Info info) {
		return "Invalid reference (" + info.getTemplateName() + " line "+ info.getLine() + ") " ;
	}
}
