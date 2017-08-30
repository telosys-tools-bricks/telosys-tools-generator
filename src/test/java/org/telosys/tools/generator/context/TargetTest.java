package org.telosys.tools.generator.context;

import junit.env.telosys.tools.generator.fakemodel.EntityInFakeModel;
import junit.framework.TestCase;

import org.telosys.tools.commons.bundles.TargetDefinition;
import org.telosys.tools.commons.variables.Variable;
import org.telosys.tools.generic.model.Entity;


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
		
		//Target target = new Target( targetDef, "AUTHOR", "Author", getVariables() ); 
		Target target = new Target( targetDef, buildEntity("AUTHOR", "Author"), getVariables() );  // v 3.0.0
		
		print(target);		
		assertEquals("${BEANNAME}.java", target.getOriginalFileDefinition());
		assertEquals("Author.java", target.getFile());
		assertEquals("/src/org/demo/foo/bar/persistence/services", target.getFolder());
		
		target.forceEntityName("AuthorFoo");
		
		print(target);		
		assertEquals("${BEANNAME}.java", target.getOriginalFileDefinition());
		assertEquals("AuthorFoo.java", target.getFile());
		assertEquals("/src/org/demo/foo/bar/persistence/services", target.getFolder());
	}

	public void testTargetCreation2() {
		
		TargetDefinition targetDef = new TargetDefinition(
				"Target 2", 
				"${BEANNAME}.java", 
				"${SRC}/${ENTITY_PKG}", 
				"bean.vm", 
				"*");
		
		//Target target = new Target( targetDef, "AUTHOR", "Author", getVariables() ); 
		Target target = new Target( targetDef, buildEntity("AUTHOR", "Author"), getVariables() ); // v 3.0.0
		
		print(target);
		
		assertEquals("Author.java", target.getFile());
		assertEquals("/src/org/demo/foo/bar/bean", target.getFolder());
		
	}

	private void print(Target target) {
		System.out.println("Target : " );
		System.out.println(" . originalFileDefinition = " + target.getOriginalFileDefinition() );
		System.out.println(" . file     = " + target.getFile() );
		System.out.println(" . folder   = " + target.getFolder() );
		System.out.println(" . template = " + target.getTemplate() );
	}
	
	private Entity buildEntity(String tableName, String className) { // v 3.0.0
		EntityInFakeModel entity = new EntityInFakeModel();
		entity.setDatabaseTable(tableName);
		entity.setClassName(className);
		return entity ;
	}
}
