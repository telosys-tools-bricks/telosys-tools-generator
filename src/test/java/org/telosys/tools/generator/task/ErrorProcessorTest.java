package org.telosys.tools.generator.task;

import static org.junit.Assert.assertTrue;

import org.apache.velocity.exception.ExtendedParseException;
import org.apache.velocity.runtime.parser.ParseException;
import org.apache.velocity.runtime.parser.TemplateParseException;
import org.junit.Test;

public class ErrorProcessorTest {
	

	@Test
	public void test1() {
		
		// extends ParseException implements ExtendedParseException
		TemplateParseException e = new TemplateParseException("My TemplateParseException");
		System.out.println("Msg = " + e.getMessage() + " line = " + e.getLineNumber() );
		assertTrue(e instanceof TemplateParseException);
		assertTrue(e instanceof ParseException);
		assertTrue(e instanceof ExtendedParseException);
		
		ErrorReport errorReport = ErrorProcessor.buildErrorReport(e, "Car", "foo.vm");
		System.out.println("ErrorReport ----------------------------- " );
		System.out.println("Error type = " + errorReport.getErrorType() );
		System.out.println("Message    = " + errorReport.getMessage() );
		System.out.println("Exception  = " + errorReport.getException() );
	}

}
