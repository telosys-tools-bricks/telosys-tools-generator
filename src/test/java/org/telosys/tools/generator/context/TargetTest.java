package org.telosys.tools.generator.context;

import java.io.File;

import org.junit.Test;
import org.telosys.tools.commons.bundles.TargetDefinition;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.generic.model.Entity;

import static org.junit.Assert.assertEquals;

import junit.env.telosys.tools.generator.TestsEnv;
import junit.env.telosys.tools.generator.fakemodel.FakeEntity;

public class TargetTest {

	private TelosysToolsCfg getTelosysToolsCfg() {
		File projectFolder = TestsEnv.getTestFolder("proj-target-tests");
		return TestsEnv.loadTelosysToolsCfg(projectFolder);
	}

//	private Variable[] getVariables() {
//		Variable[] variables = new Variable[5] ;
//		int i = 0 ;
//		variables[i++] = new Variable("ROOT_PKG",   "org.demo.foo.bar");
//		variables[i++] = new Variable("ENTITY_PKG", "org.demo.foo.bar.bean");
//		variables[i++] = new Variable("VAR1",       "VALUE1");
//		variables[i++] = new Variable("VAR2",       "VALUE2");
//		variables[i++] = new Variable("SRC",        "/src");
//		return variables ;
//	}
	
	@Test
	public void testTargetCreation1() {
		
		TargetDefinition targetDef = new TargetDefinition(
				"Target 1", 
				"${BEANNAME}.java", 
				"${SRC}/${ROOT_PKG}/persistence/services", 
				"bean.vm", 
				"*");
		
//		Target target = new Target( targetDef, buildEntity("AUTHOR", "Author"), getVariables() );  // v 3.0.0
		TelosysToolsCfg telosysToolsCfg = getTelosysToolsCfg();
		System.out.println( telosysToolsCfg.getCfgFileAbsolutePath() );
		assertEquals("/src", telosysToolsCfg.getSRC() );
		
		Target target = new Target( telosysToolsCfg, targetDef, buildEntity("AUTHOR", "Author") );  // v 3.0.0
		
		print(target);		
		assertEquals("Target 1",         target.getTargetName());
		assertEquals("${BEANNAME}.java", target.getOriginalFileDefinition());
		assertEquals("${SRC}/${ROOT_PKG}/persistence/services", target.getOriginalFolderDefinition());
		assertEquals("bean.vm",          target.getTemplate());

		// current entity :
		assertEquals("Author",           target.getEntityName());
		assertEquals("",                 target.getForcedEntityName());
		
		// after variable substitution :
		assertEquals("Author.java",      target.getFile());
		assertEquals("/src/org/demo/foo/bar/persistence/services", target.getFolder());
		assertEquals("src/org/demo/foo/bar/persistence/services/Author.java",target.getOutputFileNameInProject());

		System.out.println( target.getOutputFileFullPath() ); // v 3.3.0
		assertEquals("C:\\FOO\\BAR/src/org/demo/foo/bar/persistence/services/Author.java", target.getOutputFileFullPath());
		
		// force entity name :
		target.forceEntityName("AuthorFoo");
		assertEquals("AuthorFoo", target.getForcedEntityName());
		
		print(target);		
		assertEquals("${BEANNAME}.java", target.getOriginalFileDefinition());
		assertEquals("AuthorFoo.java", target.getFile()); // File name based on 'forced entity name'
		assertEquals("/src/org/demo/foo/bar/persistence/services", target.getFolder());
	}

	@Test
	public void testTargetCreation2() {
		
		TargetDefinition targetDef = new TargetDefinition(
				"Target 2", 
				"${BEANNAME}.java", 
				"${SRC}/${ENTITY_PKG}", 
				"bean.vm", 
				"*");
		
//		Target target = new Target( targetDef, buildEntity("AUTHOR", "Author"), getVariables() ); // v 3.0.0
		Target target = new Target( getTelosysToolsCfg(), targetDef, buildEntity("AUTHOR", "Author") ); // v 3.3.0
		
		print(target);
		
		assertEquals("Author.java", target.getFile());
		assertEquals("/src/org/demo/foo/bar/bean", target.getFolder());
		
	}

	private void print(Target target) {
		System.out.println("Target : " );
		System.out.println(" . targetName = " + target.getTargetName() );
		System.out.println(" . originalFileDefinition = " + target.getOriginalFileDefinition() );
		System.out.println(" . file     = " + target.getFile() );
		System.out.println(" . folder   = " + target.getFolder() );
		System.out.println(" . template = " + target.getTemplate() );
	}
	
	private Entity buildEntity(String tableName, String className) { 
		FakeEntity entity = new FakeEntity(className, tableName );
//		entity.setDatabaseTable(tableName);
//		entity.setClassName(className);
		return entity ;
	}
}
