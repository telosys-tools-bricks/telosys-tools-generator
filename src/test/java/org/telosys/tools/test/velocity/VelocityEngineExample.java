package org.telosys.tools.test.velocity;

import java.io.StringWriter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;


public class VelocityEngineExample {

	private final static String TEMPLATES_DIR = "" ;
	
	public static Template getTemplate(String templateFileName, String encoding) {
		
		VelocityEngine  engine = new VelocityEngine();
		engine.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, TEMPLATES_DIR);
		try {
			engine.init();
		} catch (Exception e) {
			throw new RuntimeException("Cannot init VelocityEngine", e );
		}

		
		Template template = null;
		try
		{
			if ( encoding != null ) {
				// Specific encoding
				template = engine.getTemplate(templateFileName, encoding);
			}
			else {
				// Default encoding
				template = engine.getTemplate(templateFileName);				
			}
		}
		catch( ResourceNotFoundException rnfe )
		{
		   // couldn't find the template
			throw new RuntimeException(rnfe);
		}
		catch( ParseErrorException pee )
		{
		  // syntax error: problem parsing the template
			throw new RuntimeException(pee);
		}
		catch( MethodInvocationException mie )
		{
		  // something invoked in the template
		  // threw an exception
			throw new RuntimeException(mie);
		}
		catch( Exception e )
		{
			throw new RuntimeException(e);
		}
		return template ;
	}
	
	public static VelocityContext getContext() {
		VelocityContext context = new VelocityContext();
		context.put( "name", new String("Velocity") );
		return context ;
	}
	
	public static void generate(String templateFileName, String encoding) {
		
		VelocityContext context = getContext();
		Template template = getTemplate(templateFileName, null) ;
		
		StringWriter sw = new StringWriter();
		template.merge( context, sw );
		
		System.out.println("RESULT : ");
		System.out.println(sw.toString());
	}
	
	public static void main(String[] args) {
		generate("template1.vm", null);
		generate("template1.vm", "UTF-8");
	}	
	
}
