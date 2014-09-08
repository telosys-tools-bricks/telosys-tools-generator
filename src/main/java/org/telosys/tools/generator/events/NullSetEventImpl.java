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

import org.apache.velocity.app.event.NullSetEventHandler;
import org.telosys.tools.generator.GeneratorContextException;

/**
 * Velocity User Guide :
 * When a #set() rejects an assignment due to the right hand side being an invalid or null reference, 
 * this is normally logged. The NullSetEventHandler allows you to 'veto' the logging of this condition. 
 * Multiple NullSetEventHandler's can be chained; each event handler is called in sequence until a false is returned. 
 * 
 * @author Laurent Guerin
 *
 */
public class NullSetEventImpl implements NullSetEventHandler {

	public boolean shouldLogOnNullSet( String leftHandSide, String righHandSide ) {

		//--- Return value : true if log message should be written, false otherwise
		//return false;
		
		throw new GeneratorContextException( "#set( " + leftHandSide + " = " + righHandSide + " ) : Null value"  );
	}

}
