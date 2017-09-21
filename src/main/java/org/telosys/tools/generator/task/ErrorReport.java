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


/**
 * Generator error report
 * 
 * @author L. Guerin
 *
 */
public class ErrorReport {

//	private final String messageTitle ;
	private final String    errorType ;
	private final String    message ;
	private final Throwable exception ;
		
//	//--------------------------------------------------------------------------------------
//	public ErrorReport(String messageTitle, String messageBody, Throwable exception) {
//		super();
//		this.messageTitle = messageTitle;
//		this.messageBody = messageBody;
//		this.exception = exception;
//	}
	//--------------------------------------------------------------------------------------
	public ErrorReport( String errorType, String errorMessage, Throwable exception) {
		super();
		this.errorType = errorType ;
		this.message   = errorMessage;
		this.exception = exception;
	}

	//--------------------------------------------------------------------------------------
//	public String getMessageTitle() {
//		return messageTitle;
//	}

	public String getErrorType() {
		return errorType;
	}

	public String getMessage() {
		return message;
	}

	public Throwable getException() {
		return exception;
	}	

}
