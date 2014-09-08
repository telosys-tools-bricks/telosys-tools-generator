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

import org.apache.velocity.app.event.EventCartridge;
import org.apache.velocity.context.Context;

/**
 * Velocity Generator EVENT HANDLERS 
 * 
 * @author Laurent GUERIN 
 *
 */
public class GeneratorEvents {

	public final static void attachEvents(Context context) {
		
		//--- Make a cartridge to hold the event handlers 
		EventCartridge ec = new EventCartridge();
		
		//--- Event handler for "Invalid Reference"
		ec.addInvalidReferenceEventHandler( new InvalidReferenceEventImpl() );
		
		//ec.addNullSetEventHandler( new NullSetEventImpl() );
		
		//--- Finally let it attach itself to the context
		ec.attachToContext( context );
		
	}
}
