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
package org.telosys.tools.generator.task;

import java.util.List;

/**
 * 'MsgBox like' for CLI  <br>
 *  
 * @author Laurent Guerin
 *
 */
public class MsgBox {

	private static final String DASHES = "+-------------------" ;
	
	private static void println(String s) {
		System.out.println(s);
	}

	private MsgBox() {
	}

	/**
	 * GUI like 'Message Box / Info' 
	 * @param message
	 * @param lines
	 */
	public static void info(String msg, List<String> lines) {
		println(DASHES);
		println("| (i) INFORMATION ");
		println("| " + msg );
		println("| ");
		for ( String l : lines ) {
			println("|  " + l );
		}
		println(DASHES);
	}
	
	/**
	 * GUI like 'Message Box / Error' 
	 * @param errorReport
	 */
	public static void error(ErrorReport errorReport) {
		println(DASHES);
		println("| (!) ERROR : " ); 
		println("| " + errorReport.getErrorMessage() );
		println("| ");
		for ( String l : errorReport.getErrorDetails() ) {
			println("|  " + l );
		}
		println(DASHES);
	}

}
