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
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generator.engine.GeneratorContextException;
import org.telosys.tools.generator.engine.directive.DirectiveException;


/**
 * Generation task error processor <br>
 * Used to build an 'ErrorReport'
 *  
 * @author Laurent Guerin
 *
 */
public abstract class ErrorProcessor
{	
	/**
	 * Builds an ErrorReport for the given Exception without template/entity
	 * @param specificErrorType
	 * @param exception
	 * @return
	 */
	public static ErrorReport buildErrorReport(String specificErrorType, Throwable exception ) {
		String msg = buildStandardMessage( "", -1, "", exception) ; 
		return new ErrorReport( specificErrorType, msg, exception);		
	}
	
	/**
	 * Builds an ErrorReport for the given Exception 
	 * @param exception
	 * @param entityName
	 * @param templateName
	 * @return
	 */
	public static ErrorReport buildErrorReport(Throwable exception, String entityName, String templateName ) {
		
		if ( exception instanceof ParseException ) { // Velocity Checked Exceptions
			ParseException parseException = (ParseException) exception ;
			String msg = processVelocityParseException(parseException, templateName, entityName);
			return new ErrorReport( "Velocity error (ParseException)", msg, parseException);
		}
		else if ( exception instanceof VelocityException ) { // Velocity Runtime Exceptions
			VelocityException velocityException = (VelocityException) exception ;
			String msg = processVelocityException(velocityException, templateName, entityName);
			return new ErrorReport( "Velocity error (VelocityException)", msg, velocityException);
		}
		else if ( exception instanceof RuntimeException ) { // Telosys Runtime Exceptions
			RuntimeException runtimeException = (RuntimeException) exception ;
			String msg = processRuntimeException(runtimeException, templateName, entityName);
			return new ErrorReport( "Generator error (RuntimeException)", msg, runtimeException);
		}
		else if ( exception instanceof GeneratorException ) { // Telosys Generator Exception (without cause)
			GeneratorException generatorException = (GeneratorException) exception ;
			String msg = buildStandardMessage( templateName, -1, entityName, generatorException) ; 
			return new ErrorReport( "Generator error (GeneratorException)", msg, generatorException);
		}
		else {
			// Supposed to never happen
			String msg = buildStandardMessage( templateName, -1, entityName, exception) ; 
			String exceptionName = exception.getClass().getCanonicalName();
			return new ErrorReport( "Unknown error ("+exceptionName+")", msg, exception);
		}
	}
	
	//-------------------------------------------------------------------------------------------------------------
	/**
	 * Process the Telosys Runtime exceptions
	 * @param telosysException
	 * @param templateName
	 * @param entityName
	 * @return
	 */
	private static String processRuntimeException(RuntimeException telosysException, String templateName, String entityName) {
		
		if ( telosysException instanceof DirectiveException ) {
			//--- DIRECTIVE ERROR ( Telosys Tools exception )
			// eg : #using ( "varNotDefined" )
			DirectiveException e = (DirectiveException) telosysException ;
			StringBuilder sb = new StringBuilder();
			sb.append( buildStandardMessage( templateName, e.getLineNumber(), entityName, e) );
			sb.append( "Directive '#" );
			sb.append( e.getDirectiveName() );
			sb.append( "'");
			sb.append( "\n");
			sb.append( "\n");
			return sb.toString();
		}
		if ( telosysException instanceof GeneratorContextException ) {
			//--- CONTEXT ERROR ( Telosys Tools exception )
			// Reflection error encapsulation
			// eg : $entity.tototo / $entity.getTTTTTTTTT() / $entity.name.toAAAAA()
			// or errors due to invalid model 
			GeneratorContextException e = (GeneratorContextException) telosysException ;
			return buildStandardMessage( templateName, e.getLineNumber(), entityName, e) ;
		}
		else {
			// Supposed to never happen
			return buildStandardMessage( templateName, -1, entityName, telosysException) ; 
		}
	}
	
	//-------------------------------------------------------------------------------------------------------------
	/**
	 * Process all subclasses of ParseException : <br> 
	 * org.apache.velocity.runtime.parser.ParseException ( Velocity 1.7 Checked Exceptions, extends 'java.lang.Exception' ) :
	 *   - org.apache.velocity.runtime.directive.MacroParseException
	 *   - org.apache.velocity.runtime.parser.TemplateParseException
	 * @param parseException
	 * @param templateName
	 * @param entityName
	 * @return
	 */
	private static String processVelocityParseException(ParseException parseException, String templateName, String entityName) {
		
		if ( parseException instanceof MacroParseException ) { // Test OK ( Message Ok ) 
			MacroParseException e = (MacroParseException) parseException ;
			// No more information about the error
			return buildStandardMessage( templateName, e.getLineNumber(), entityName, e) ;
		}
		else if ( parseException instanceof TemplateParseException ) {  // Test OK ( Message Ok ) 
			TemplateParseException e = (TemplateParseException) parseException ;
			// No more information about the error
			return buildStandardMessage( templateName, e.getLineNumber(), entityName, e) ;
		}
		else {
			// supposed to never happen
			return buildStandardMessage( templateName, -1, entityName, parseException) ; 
		}
	}
	
	//-------------------------------------------------------------------------------------------------------------
	/**
	 * Process all subclasses of VelocityException : <br> 
	 * org.apache.velocity.exception.VelocityException ( Velocity 1.7 Runtime Exceptions ) :  <br> 
	 *  - org.apache.velocity.exception.MacroOverflowException  <br> 
	 *  - org.apache.velocity.exception.MathException  <br> 
	 *  - org.apache.velocity.exception.MethodInvocationException  <br> 
	 *  - org.apache.velocity.exception.ParseErrorException <br> 
	 *  - org.apache.velocity.exception.ResourceNotFoundException <br> 
	 *  - org.apache.velocity.exception.TemplateInitException <br> 
	 *  
	 * @param velocityException
	 * @param templateName
	 * @param entityName
	 * @return
	 */
	private static String processVelocityException(VelocityException velocityException, String templateName, String entityName) {
		
		if ( velocityException instanceof MacroOverflowException ) {
			// Application-level exception thrown when macro calls within macro calls exceeds the maximum allowed depth. 
			// The maximum allowable depth is given in the configuration as velocimacro.max.depth.
			MacroOverflowException e = (MacroOverflowException) velocityException ;
			// No more information about the error
			return buildStandardMessage( templateName, -1, entityName, e) ;
		}
		
		else if ( velocityException instanceof MathException ) {
			// Separate exception class to distinguish math problems.
			MathException e = (MathException) velocityException ;
			// No more information about the error
			return buildStandardMessage( templateName, -1, entityName, e) ;
		}
		
		else if ( velocityException instanceof MethodInvocationException ) {
			//--- METHOD INVOCATION ( Velocity exception )
			// e.g. : $fn.isNotVoid("") : collection argument expected 
			MethodInvocationException e = (MethodInvocationException) velocityException ;
			StringBuilder sb = new StringBuilder();
			sb.append( buildStandardMessage( templateName, e.getLineNumber(), entityName, e) );
			sb.append( "Reference name : '" );
			sb.append( e.getReferenceName() );
			sb.append( "'");
			sb.append( "\n");
			sb.append( "Method name : '" );
			sb.append( e.getMethodName() );
			sb.append( "'");
			sb.append( "\n");
			sb.append( "\n");
			//+ buildMessageForExceptionCause(generatorExceptionCause) 
			return sb.toString();
		}			
		
		else if ( velocityException instanceof ParseErrorException ) {
			//--- TEMPLATE PARSING ERROR ( Velocity exception )
			// Application-level exception thrown when a resource of any type has a syntax 
			// or other error which prevents it from being parsed. 
			// When this resource is thrown, a best effort will be made to have useful information in the exception's message.
			// e.g. : #set(zzz)
			ParseErrorException e = (ParseErrorException) velocityException ;
			//e.getLineNumber()
			//e.getInvalidSyntax()
			StringBuilder sb = new StringBuilder();
			sb.append( buildStandardMessage( templateName, e.getLineNumber(), entityName, e) );
			sb.append( "Invalid syntax : " );
			sb.append( "\n");
			sb.append( e.getInvalidSyntax() );
			sb.append( "\n");
			sb.append( "\n");
			return sb.toString() ;
		}
		
		else if ( velocityException instanceof ResourceNotFoundException ) {
			//--- RESOURCE NOT FOUND ( Velocity exception )
			// Application-level exception thrown when a resource of any type isn't found by the Velocity engine. 
			// When this exception is thrown, a best effort will be made to have useful information in the exception's message.
			ResourceNotFoundException e = (ResourceNotFoundException) velocityException ;
			// No more information about the error
			return buildStandardMessage( templateName, -1, entityName, e) ;
		}			

		else if ( velocityException instanceof TemplateInitException ) {
			//--- RESOURCE NOT FOUND ( Velocity exception )
			// Application-level exception thrown when a resource of any type isn't found by the Velocity engine. 
			// When this exception is thrown, a best effort will be made to have useful information in the exception's message.
			TemplateInitException e = (TemplateInitException) velocityException ;
			// No more information about the error
			return buildStandardMessage( templateName, e.getLineNumber(), entityName, e) ;
		}
		else {
			// supposed to never happen
			return buildStandardMessage( templateName, -1, entityName, velocityException) ; 
		}
	}
	
	//-------------------------------------------------------------------------------------------------------------
	private static String buildStandardMessage(String template, int line, String entity, Throwable exception ) {
		StringBuilder sb = new StringBuilder();

		if ( ! StrUtil.nullOrVoid(template) ) {
			sb.append("Template \"");
			sb.append(template);
			sb.append("\"");
			if ( line > 0 ) {
				sb.append(" ( line " );
				sb.append(line );
				sb.append(" )" );
			}
			sb.append(" - ");
			sb.append("Entity \"");
			sb.append(entity);
			sb.append("\"");
			sb.append("\n\n");
		}
		
		sb.append("Exception '");
		sb.append( exception.getClass().getSimpleName() );
		sb.append("' : \n");
		sb.append( exception.getMessage() );
		sb.append("\n\n");
		
		return sb.toString();
	}
	
	//-------------------------------------------------------------------------------------------------------------
	protected static String buildMessageForExceptionCause(Throwable exception) {
		Throwable cause = exception.getCause();
		if ( cause != null ) {
			StringBuilder sb = new StringBuilder() ;
			int n = 0 ;
			while ( cause != null ) {
				n++ ;
				sb.append( "Cause #" + n + " : " );
				sb.append( cause.getClass().getSimpleName() );
				sb.append( " - " );
				sb.append( cause.getMessage()  );
				sb.append( "\n" );
				StackTraceElement[] stackTrace = cause.getStackTrace() ;
				if ( stackTrace != null ) {
					for ( int i = 0 ; ( i < stackTrace.length ) && ( i < 5 ) ; i++ ) {
						StackTraceElement e = stackTrace[i];
						sb.append( e.getFileName() ) ;
						sb.append( " - " ) ;
						sb.append( e.getMethodName() ) ;
						sb.append( " - line " ) ;
						sb.append( e.getLineNumber() ) ;
						sb.append( "\n" );
					}
				}
				sb.append( "\n" );
				sb.append( "\n" );
				cause = cause.getCause() ;
			}
			return sb.toString();
		}
		else {
			return "No cause.\n" ;
		}
	}
}
