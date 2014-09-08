package org.telosys.tools.test.velocity.context;

import junit.framework.TestCase;

import org.telosys.tools.commons.variables.Variable;
import org.telosys.tools.generator.context.Target;
import org.telosys.tools.generator.target.TargetDefinition;

public class TargetTest extends TestCase {

	public Variable[] getVariables() {
		Variable[] variables = new Variable[5] ;
		int i = 0 ;
		variables[i++] = new Variable("ROOT_PKG",   "org.demo.foo.bar");
		variables[i++] = new Variable("ENTITY_PKG", "org.demo.foo.bar.bean");
		variables[i++] = new Variable("VAR1",       "VALUE1");
		variables[i++] = new Variable("VAR2",       "VALUE2");
		variables[i++] = new Variable("SRC",        "/src");
		return variables ;
	}
	
	public void testTargetCreation1() {
		
		TargetDefinition targetDef = new TargetDefinition(
				"Target 1", 
				"${BEANNAME}.java", 
				"${SRC}/${ROOT_PKG}/persistence/services", 
				"bean.vm", 
				"*");
		
		Target target = new Target( targetDef, "AUTHOR", "Author", getVariables() ); 
		
		String file   = target.getFile();
		String folder = target.getFolder();
		System.out.println(" . file   = " + file );
		System.out.println(" . folder = " + folder );
		
		assertEquals("Author.java", file);
		assertEquals("/src/org/demo/foo/bar/persistence/services", folder);
		
	}

	public void testTargetCreation2() {
		
		TargetDefinition targetDef = new TargetDefinition(
				"Target 2", 
				"${BEANNAME}.java", 
				"${SRC}/${ENTITY_PKG}", 
				"bean.vm", 
				"*");
		
		Target target = new Target( targetDef, "AUTHOR", "Author", getVariables() ); 
		
		String file   = target.getFile();
		String folder = target.getFolder();
		System.out.println(" . file   = " + file );
		System.out.println(" . folder = " + folder );
		
		assertEquals("Author.java", file);
		assertEquals("/src/org/demo/foo/bar/bean", folder);
		
	}
	
}
