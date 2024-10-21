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

import java.util.LinkedList;
import java.util.List;

import org.apache.velocity.exception.MacroOverflowException;
import org.apache.velocity.exception.MathException;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.exception.TemplateInitException;
import org.apache.velocity.exception.VelocityException;
import org.apache.velocity.runtime.directive.MacroParseException;
import org.apache.velocity.runtime.parser.ParseException;
import org.apache.velocity.runtime.parser.TemplateParseException;
import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generator.context.GeneratorContextException;
import org.telosys.tools.generator.engine.directive.DirectiveException;
import org.telosys.tools.generator.engine.events.InvalidReferenceException;

/**
 * Generator error report
 * 
 * @author L. Guerin
 *
 */
public class ErrorReport {

	private final String    errorMessage ;
	private final Throwable exception ;
	private final List<String> errorDetails ;
		
	/**
	 * Constructor for basic error with only error type and error message
	 * @param errorType
	 * @param errorMessage
	 */
	public ErrorReport( String errorMessage) {
		super();
		this.errorMessage   = errorMessage != null ? errorMessage : "(no error message)";
		this.exception = null;
		this.errorDetails = null;
	}

	/**
	 * Constructor with exception
	 * @param exception  required 
	 * @param templateName can be null
	 * @param entityName can be null
	 */
	public ErrorReport(Throwable exception, String templateName, String entityName) {
		super();
		this.errorMessage = buildErrorMessage(exception);
		this.exception = exception; 
		this.errorDetails = buildErrorDetails(exception, templateName, entityName);
	}

	//--------------------------------------------------------------------------------------
	/**
	 * Returns the error message (exception message or message set specifically) <br>
	 * Never null
	 * @return
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * Return the exception associated with the error report (if any) <br>
	 * Can be null
	 * @return
	 */
	public Throwable getException() {
		return exception;
	}

	/**
	 * Return all the error details (if any) <br>
	 * @return
	 */
	public List<String> getErrorDetails() {
		return errorDetails;
	}

	private String buildErrorMessage(Throwable exception) {
		return exception.getMessage();
	}
	
	private List<String> buildErrorDetails(Throwable exception, String templateName, String entityName) {
		List<String> list = new LinkedList<>();
		String s = buildTemplateAndEntity(templateName, entityName);
		if ( s != null ) {
			list.add(s);
		}
		addAllExceptionsDetails(list, exception);
		return list;
	}
	
	private String buildTemplateAndEntity(String templateName, String entityName) {
		if ( templateName != null || entityName != null ) {
			StringBuilder sb = new StringBuilder();
			if ( templateName != null ) {
				sb.append("Error in template '").append(templateName).append("'");
			}
			if ( entityName != null ) {
				if ( templateName != null ) {
					sb.append(" / ");
				}
				sb.append("current entity '").append(entityName).append("' ");
			}
			return sb.toString();
		}
		return null ;
	}
	
	private void addAllExceptionsDetails(List<String> list, Throwable exception) {
		// Dive in exceptions stack ...
		Throwable e = exception;
		while ( e != null ) {
			// Exception name 
			list.add("-> " +  e.getClass().getCanonicalName() + " : " ) ;
			// Exception message
			addExceptionMessage(list, e);
			// Exception details if any
			addExceptionDetails(list, e);
			e = e.getCause() ;
		}
	}
	
	/**
	 * Add exception message if usefull 
	 * @param list
	 * @param exception
	 */
	private void addExceptionMessage(List<String> list, Throwable exception) {
		if ( ! ( exception instanceof MethodInvocationException ) ) {
			// No message for Velocity MethodInvocationException (message useless and too long)
			list.add(exception.getMessage() ) ;
		}
	}
	
	/**
	 * Add specific exception details if any
	 * @param list
	 * @param exception
	 */
	private void addExceptionDetails(List<String> list, Throwable exception) {
		//--- Telosys exceptions
		if ( exception instanceof DirectiveException ) {
			addTelosysDirectiveException(list, (DirectiveException)exception);
		}
		else if ( exception instanceof GeneratorContextException ) {
			addTelosysGeneratorContextException(list, (GeneratorContextException) exception);
		}
		else if ( exception instanceof GeneratorException ) { // Telosys Generator Exception (without cause)
			// nothing special
		}
		else if ( exception instanceof InvalidReferenceException ) {
			// no more information (message is explicit enough)
		}
		//--- Velocity exceptions
		else if ( exception instanceof ParseException ) { // Velocity Checked Exceptions
			addVelocityParseException(list, (ParseException) exception);
		}
		else if ( exception instanceof VelocityException ) { // Velocity Runtime Exceptions
			addVelocityException(list, (VelocityException) exception);
		}
	}

	//---------------------------------------------------------------------------------------------
	// Specific exceptions details
	//---------------------------------------------------------------------------------------------

	/**
	 * Telosys context error (reflection error encapsulation) <br>
	 * eg :  $entity.tototo / $entity.getTTTTTTTTT() / $entity.name.toAAAAA() <br>
	 * or errors due to invalid model 
	 * @param list
	 * @param e
	 */
	private void addTelosysGeneratorContextException(List<String> list, GeneratorContextException e) {
		list.add("Context error ");
		list.add(inTemplate(e.getTemplateName(), e.getLineNumber(), -1 ));
	}
	
	/**
	 * Telosys directive error, eg : #using("varNotDefined")
	 * @param list
	 * @param e
	 */
	private void addTelosysDirectiveException(List<String> list, DirectiveException e) {
		list.add("Directive error : '#" + e.getDirectiveName() + "'");
		list.add(inTemplate(e.getTemplateName(), e.getLineNumber(), -1 ));
	}

	/**
	 * Velocity parsing error <br>
	 * org.apache.velocity.runtime.parser.ParseException <br>
	 * ( Velocity 1.7 Checked Exceptions, extends 'java.lang.Exception' ) : <br>
	 *   - org.apache.velocity.runtime.directive.MacroParseException <br>
	 *   - org.apache.velocity.runtime.parser.TemplateParseException <br>
	 *   
	 * @param list
	 * @param parseException
	 */
	private void addVelocityParseException(List<String> list, ParseException parseException) {
		if ( parseException instanceof MacroParseException ) {
			MacroParseException e = (MacroParseException) parseException;
			list.add("Velocity macro parsing error" );
			list.add(inTemplate(e.getTemplateName(), e.getLineNumber(), e.getColumnNumber() ));
		}
		else if ( parseException instanceof TemplateParseException ) { 
			TemplateParseException e = (TemplateParseException) parseException;
			list.add("Velocity template parsing error" );
			list.add(inTemplate(e.getTemplateName(), e.getLineNumber(), e.getColumnNumber() ));
		}
		else {
			// supposed to never happen
			list.add("Velocity parsing error (ParseException)" );
			list.add("unknown exception " + parseException.getClass().getCanonicalName());
		}
	}
	
	/**
	 * Velocity exceptions : <br> 
	 * org.apache.velocity.exception.VelocityException <br>
	 * ( Velocity 1.7 Runtime Exceptions ) :  <br> 
	 *  - org.apache.velocity.exception.MacroOverflowException  <br> 
	 *  - org.apache.velocity.exception.MathException  <br> 
	 *  - org.apache.velocity.exception.MethodInvocationException  <br> 
	 *  - org.apache.velocity.exception.ParseErrorException <br> 
	 *  - org.apache.velocity.exception.ResourceNotFoundException <br> 
	 *  - org.apache.velocity.exception.TemplateInitException <br> 
	 *  
	 * @param list
	 * @param velocityException Base class for Velocity runtime exceptions thrown to the application layer
	 */
	private void addVelocityException(List<String> list, VelocityException velocityException) {
		if ( velocityException instanceof MacroOverflowException ) {
			/*
			 * MacroOverflowException ( Velocity exception )
			 * Application-level exception thrown when macro calls within macro calls exceeds the maximum allowed depth. 
			 * The maximum allowable depth is given in the configuration as 'velocimacro.max.depth'.
			 */
			list.add("Velocity macro overflow error");
			list.add(" macro calls within macro calls exceeds the maximum allowed depth");
			// No more information about the error
		}
		else if ( velocityException instanceof MathException ) {
			/*
			 * MathException ( Velocity exception )
			 * Separate exception class to distinguish math problems.
			 */
			list.add("Velocity math error");
			// No more information about the error
		}
		else if ( velocityException instanceof MethodInvocationException ) {
			//--- METHOD INVOCATION ( Velocity exception )
			// e.g. : $fn.isNotVoid("") : collection argument expected 
			list.add("Velocity method invocation error");
			MethodInvocationException e = (MethodInvocationException) velocityException ;
			list.add(inTemplate(e.getTemplateName(), e.getLineNumber(), e.getColumnNumber() ));
			list.add(" '" + e.getReferenceName() + "." + e.getMethodName() +  "()' "); 
			list.add(" ( reference name : '" + e.getReferenceName() + "'" 
					+ " method name : '" + e.getMethodName() + "' )" );
		}
		else if ( velocityException instanceof ParseErrorException ) {
			/*
			 * ParseErrorException ( Velocity exception )
			 * Application-level exception thrown when a resource of any type has a syntax 
			 * or other error which prevents it from being parsed. 
			 * When this resource is thrown, a best effort will be made to have useful information in the exception's message.
			 * For complete information, consult the runtime log.
			 * ---
			 * e.g. : #set(zzz)
			 */
			list.add("Velocity parsing error (e.g. syntax error)");
			ParseErrorException e = (ParseErrorException) velocityException ;
			list.add(inTemplate(e.getTemplateName(), e.getLineNumber(), e.getColumnNumber() ));
			list.add(" invalid syntax : " );
			list.add(" " + e.getInvalidSyntax() );
		}
		else if ( velocityException instanceof ResourceNotFoundException ) {
			/*
			 * ResourceNotFoundException ( Velocity exception )
			 * Application-level exception thrown when a resource of any type isn't found by the Velocity engine. 
			 * When this exception is thrown, a best effort will be made to have useful information in the exception's message.
			 * For complete information, consult the runtime log.
			 */
			list.add("Velocity resource not found");
			list.add(" a resource (of any type) hasn't been found by the Velocity engine");			
			// No more information about the error
		}
		else if ( velocityException instanceof TemplateInitException ) {
			/*
			 * TemplateInitException ( Velocity exception )
			 * Exception generated to indicate parse errors caught during directive initialization 
			 * (e.g. wrong number of arguments)
			 */
			list.add("Velocity template init error");
			TemplateInitException e = (TemplateInitException) velocityException ;
			list.add(inTemplate(e.getTemplateName(), e.getLineNumber(), e.getColumnNumber() ));
			list.add(" error during directive initialization (e.g. wrong number of arguments)");
		}
		else {
			// supposed to never happen
			list.add("Velocity error (VelocityException)" );
			list.add(" unknown exception " + velocityException.getClass().getCanonicalName());			
		}
	}
	
	/**
	 * Builds the string 'in template xxx line xxx column xxx'
	 * @param templateName
	 * @param lineNumber
	 * @param columnNumber
	 * @return
	 */
	private String inTemplate(String templateName, int lineNumber, int columnNumber) {
		StringBuilder sb = new StringBuilder();
		sb.append("in template '").append(templateName).append("'");
		if ( lineNumber > -1 || columnNumber > -1) {
			sb.append(" [ " );
			if ( lineNumber > -1 ) {
				sb.append("line ").append(lineNumber);
			}
			if ( lineNumber > -1 && columnNumber > -1) {
				sb.append(", " );
			}
			if ( columnNumber > -1 ) {
				sb.append("column ").append(columnNumber);
			}
			sb.append(" ]" );
		}
		
		return sb.toString();
	}
}
