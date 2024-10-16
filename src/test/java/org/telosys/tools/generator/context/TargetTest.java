package org.telosys.tools.generator.context;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.telosys.tools.commons.bundles.TargetDefinition;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.variables.VariablesManager;
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
		Map<String,String> variables = telosysToolsCfg.getAllVariablesMap();
		assertEquals("org.demo.foo.bar", variables.get("ROOT_PKG"));
		return telosysToolsCfg;
	}
	private Entity buildEntity(String className) { 
		return new DslModelEntity(className ) ;
	}

	private Target createTargetForEntity(String name, String file, String folder, String template, String type) {
		TargetDefinition targetDef = new TargetDefinition(name, file, folder, template, type);
		return new Target( getTelosysToolsCfg(), targetDef, buildEntity("Author") ); 
	}
	private Target createTargetWithoutEntity(String name, String file, String folder, String template, String type) {
		TargetDefinition targetDef = new TargetDefinition(name, file, folder, template, type);
		return new Target( getTelosysToolsCfg(), targetDef ); 
	}
	
	@Test
	public void testTargetForEntity1() {
		Target target = createTargetForEntity("Target 1", "${BEANNAME}.java", "${SRC}/${ROOT_PKG}/persistence/services", "bean.vm", "*");
		
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
		
		assertEquals("${BEANNAME}.java", target.getOriginalFileDefinition());
		assertEquals("AuthorFoo.java", target.getFile()); // File name based on 'forced entity name'
	}

	@Test
	public void testTargetForEntity2() {
		Target target = createTargetForEntity("Target 2", "${ENT}.java", "${SRC}/${ENTITY_PKG}", "bean.vm",	"*");
		assertEquals("Author.java", target.getFile());
		assertEquals("/src/org/demo/foo/bar/bean", target.getFolder());
		
		target = createTargetForEntity("Target 2", "${ENT_LC}.txt", "${SRC}/${ENTITY_PKG}/${ENT_LC}", "bean.vm",	"*");
		assertEquals("author.txt", target.getFile());
		assertEquals("/src/org/demo/foo/bar/bean/author", target.getFolder());

		target = createTargetForEntity("Target 2", "${ENT_UC}.txt", "${SRC}/${ENTITY_PKG}/${ENT_UC}", "bean.vm",	"*");
		assertEquals("AUTHOR.txt", target.getFile());
		assertEquals("/src/org/demo/foo/bar/bean/AUTHOR", target.getFolder());
	}

	@Test
	public void testTargetCreation3() {
		
		Target target = createTargetWithoutEntity("Target XML", "config.xml", "${RES}/foo", "config_xml.vm", "1");
		
		assertEquals("config.xml", target.getFile());
		assertEquals("src/main/resources/foo", target.getFolder());
		assertEquals("1", target.getType());
		assertEquals("C:\\FOO\\BAR/src/main/resources/foo/config.xml", target.getOutputFileFullPath());
	}
	
	@Test
	public void testTransformPackageVariablesToDirPath() {
		Target target = createTargetWithoutEntity("Target XML", "config.xml", "${RES}/foo", "config_xml.vm", "1");
		HashMap<String,String> hm = new HashMap<>();
		hm.put("ROOT_PKG",   "org.demo.foo.bar");
		hm.put("ENTITY_PKG", "org.demo.foo.bar.bean");
		hm.put("VAR1",       "VALUE1");
		hm.put("VAR2",       "VALUE2");
		hm.put("VAR3",       "VALUE3");
		VariablesManager vm1 = new VariablesManager(hm);
		VariablesManager vm2 = target.transformPackageVariablesToDirPath(vm1);
		
		assertEquals("org.demo.foo.bar", vm1.getVariableValue("ROOT_PKG"));
		assertEquals("org/demo/foo/bar", vm2.getVariableValue("ROOT_PKG"));
		
		assertEquals("org.demo.foo.bar.bean", vm1.getVariableValue("ENTITY_PKG"));
		assertEquals("org/demo/foo/bar/bean", vm2.getVariableValue("ENTITY_PKG"));

		assertEquals("VALUE1", vm2.getVariableValue("VAR1"));
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
	
}
