package org.telosys.tools.generator.context;

import java.io.File;

import org.junit.Test;
import org.telosys.tools.commons.bundles.TargetDefinition;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.dsl.model.DslModelEntity;
import org.telosys.tools.generic.model.Entity;

import static org.junit.Assert.assertEquals;

import junit.env.telosys.tools.generator.TestsEnv;

public class TargetTest {
	
	private void println(String s) {
		System.out.println( s );
	}
	
	private TelosysToolsCfg getTelosysToolsCfg() {
		File projectFolder = TestsEnv.getTestFolder("proj-target-tests");
		TelosysToolsCfg telosysToolsCfg = TestsEnv.loadTelosysToolsCfg(projectFolder);
		println( "TelosysToolsCfg loaded from : " + telosysToolsCfg.getCfgFileAbsolutePath() );
		assertEquals("/src", telosysToolsCfg.getSRC() );
		return telosysToolsCfg;
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
		TelosysToolsCfg telosysToolsCfg = getTelosysToolsCfg();
//		Target target = new Target( telosysToolsCfg, targetDef, buildEntity("AUTHOR", "Author") ); 
		Target target = new Target( telosysToolsCfg, targetDef, buildEntity("Author") ); 
		print(target);
		
		assertEquals("Target 1",         target.getTargetName());
		assertEquals("${BEANNAME}.java", target.getOriginalFileDefinition());
		assertEquals("${SRC}/${ROOT_PKG}/persistence/services", target.getOriginalFolderDefinition());
		assertEquals("bean.vm",          target.getTemplate());
		assertEquals("*",                target.getType());

		// current entity :
		assertEquals("Author",           target.getEntityName());
		assertEquals("",                 target.getForcedEntityName());
		
		// after variable substitution :
		assertEquals("Author.java",      target.getFile());
		assertEquals("/src/org/demo/foo/bar/persistence/services", target.getFolder());
		assertEquals("src/org/demo/foo/bar/persistence/services/Author.java",target.getOutputFileNameInProject());

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
//		Target target = new Target( getTelosysToolsCfg(), targetDef, buildEntity("BOOK", "Book") ); // v 3.3.0
		Target target = new Target( getTelosysToolsCfg(), targetDef, buildEntity("Book") ); // v 3.3.0
		print(target);
		
		assertEquals("Book.java", target.getFile());
		assertEquals("/src/org/demo/foo/bar/bean", target.getFolder());
	}

	@Test
	public void testTargetCreation3() {
		
		TargetDefinition targetDef = new TargetDefinition(
				"Target 3", 
				"config.xml", 
				"${RES}/foo", 
				"config_xml.vm", 
				"1");
		Target target = new Target( getTelosysToolsCfg(), targetDef ); // v 3.3.0
		print(target);
		
		assertEquals("config.xml", target.getFile());
		assertEquals("src/main/resources/foo", target.getFolder());
		assertEquals("1", target.getType());
		assertEquals("C:\\FOO\\BAR/src/main/resources/foo/config.xml", target.getOutputFileFullPath());
	}

	private void print(Target target) {
		println("Target : " );
		println(" . targetName               = " + target.getTargetName() );
		println(" . originalFileDefinition   = " + target.getOriginalFileDefinition() );
		println(" . originalFolderDefinition = " + target.getOriginalFolderDefinition() );
		println(" . template                 = " + target.getTemplate() );
		println(" . type                     = " + target.getType() );
		println(" . folder    = " + target.getFolder() );
		println(" . file      = " + target.getFile() );
		println(" . full path = " + target.getOutputFileFullPath() ); // v 3.3.0
		println(" . exists ?  = " + target.outputFileExists() ); // v 3.3.0

	}
	
//	private Entity buildEntity(String tableName, String className) { 
	private Entity buildEntity(String className) { 
		DslModelEntity entity = new DslModelEntity(className );
//		entity.setDatabaseTable(tableName);
//		entity.setClassName(className);
		return entity ;
	}
}
