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
		
		//ErrorReport errorReport = ErrorProcessor.buildErrorReport(e, "Car", "foo.vm");
		ErrorReport errorReport = new ErrorReport(e, "my-test-template.vm", "MyTestEntity");
		
		System.out.println("ErrorReport ----------------------------- " );
		System.out.println("Message    = '" + errorReport.getErrorMessage() +"'");
		System.out.println("Exception  = '" + errorReport.getException() +"'");
		System.out.println("Error details : "  );
		for ( String s : errorReport.getErrorDetails() ) {
			System.out.println(" -> " + s);
		}
	}

}
