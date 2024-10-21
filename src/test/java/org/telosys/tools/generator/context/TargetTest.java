package org.telosys.tools.generator.context;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.telosys.tools.commons.bundles.TargetDefinition;
import org.telosys.tools.commons.variables.VariablesManager;
import org.telosys.tools.dsl.model.DslModelEntity;
import org.telosys.tools.generic.model.Entity;

import static org.junit.Assert.assertEquals;

public class TargetTest {
	
	private static final String DEST_DIR = "C:\\FOO\\BAR" ;
	
	private void println(String s) {
		System.out.println( s );
	}
	
	private Map<String,String> getVariables() {
		Map<String,String> variables = new HashMap<>() ;
		variables.put("SRC", "/src");
		variables.put("RES", "src/main/resources");
		variables.put("WEB", "src/main/webapp");
		variables.put("TEST_SRC", "src/test/java");
		variables.put("TEST_RES", "src/test/resources");
		variables.put("DOC", "doc");
		variables.put("TMP", "tmp");
		variables.put("ROOT_PKG", "org.demo.foo.bar");
		variables.put("ENTITY_PKG", "org.demo.foo.bar.bean");
		return variables;
	}
	
	private Entity buildEntity(String className) { 
		return new DslModelEntity(className ) ;
	}

	private Target createTargetForEntity(String name, String file, String folder, String template, String type) {
		TargetDefinition targetDef = new TargetDefinition(name, file, folder, template, type);
		return new Target( DEST_DIR, targetDef, getVariables(), buildEntity("Author") ); 
	}
	private Target createTargetWithoutEntity(String name, String file, String folder, String template, String type, Map<String,String> variables) {
		TargetDefinition targetDef = new TargetDefinition(name, file, folder, template, type);
		return new Target( DEST_DIR, targetDef, variables ); 
	}
	private Target createTargetWithoutEntity(String name, String file, String folder, String template, String type) {
		return createTargetWithoutEntity(name, file, folder, template, type, getVariables());
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
		// ENT (As is)
		Target target = createTargetForEntity("Target 2", "${ENT}.java", "${SRC}/${ENTITY_PKG}", "xxx.vm",	"*");
		assertEquals("Author.java", target.getFile());
		assertEquals("/src/org/demo/foo/bar/bean", target.getFolder());
		
		// ENT_LC (Lower Case)
		target = createTargetForEntity("Target 2", "${ENT_LC}.txt", "${SRC}/${ENTITY_PKG}/${ENT_LC}", "xxx.vm",	"*");
		assertEquals("author.txt", target.getFile());
		assertEquals("/src/org/demo/foo/bar/bean/author", target.getFolder());
		assertEquals("C:\\FOO\\BAR/src/org/demo/foo/bar/bean/author/author.txt", target.getOutputFileFullPath());

		// ENT_UC (Upper Case)
		target = createTargetForEntity("Target 2", "${ENT_UC}.txt", "${SRC}/${ENTITY_PKG}/${ENT_UC}", "xxx.vm",	"*");
		assertEquals("AUTHOR.txt", target.getFile());
		assertEquals("/src/org/demo/foo/bar/bean/AUTHOR", target.getFolder());
		assertEquals("C:\\FOO\\BAR/src/org/demo/foo/bar/bean/AUTHOR/AUTHOR.txt", target.getOutputFileFullPath());
	}

	@Test
	public void testTargetWithoutEntity1() {
		
		Target target = createTargetWithoutEntity("Target XML", "config.xml", "${RES}/foo", "config_xml.vm", "1");
		
		assertEquals("config.xml", target.getFile());
		assertEquals("src/main/resources/foo", target.getFolder());
		assertEquals("1", target.getType());
		assertEquals("C:\\FOO\\BAR/src/main/resources/foo/config.xml", target.getOutputFileFullPath());
	}
	
	@Test
	public void testTargetWithoutEntity2BUN() {
		
		// BUN undefined 
		Target target = createTargetWithoutEntity("x", "foo.txt", "${RES}/${BUN}/foo", "foo.vm", "1");
		assertEquals("foo.txt", target.getFile());
		assertEquals("src/main/resources/${BUN}/foo", target.getFolder());
		assertEquals("C:\\FOO\\BAR/src/main/resources/${BUN}/foo/foo.txt", target.getOutputFileFullPath());
		
		// BUN defined 
		Map<String,String> variables = getVariables();
		variables.put("BUN", "MyBundle");
		target = createTargetWithoutEntity("xx", "foo.txt", "${RES}/${BUN}/foo", "foo.vm", "1", variables);
		assertEquals("src/main/resources/MyBundle/foo", target.getFolder());
		assertEquals("C:\\FOO\\BAR/src/main/resources/MyBundle/foo/foo.txt", target.getOutputFileFullPath());

		target = createTargetWithoutEntity("xxx", "foo-${BUN_UC}.txt", "${RES}/${BUN_UC}/foo", "foo.vm", "1", variables);
		assertEquals("foo-MYBUNDLE.txt", target.getFile());
		assertEquals("src/main/resources/MYBUNDLE/foo", target.getFolder());
		assertEquals("C:\\FOO\\BAR/src/main/resources/MYBUNDLE/foo/foo-MYBUNDLE.txt", target.getOutputFileFullPath());

		target = createTargetWithoutEntity("xxxx", "foo-${BUN_LC}.txt", "${RES}/${BUN_LC}/foo", "foo.vm", "1", variables);
		assertEquals("foo-mybundle.txt", target.getFile());
		assertEquals("src/main/resources/mybundle/foo", target.getFolder());
		assertEquals("C:\\FOO\\BAR/src/main/resources/mybundle/foo/foo-mybundle.txt", target.getOutputFileFullPath());
	}
	
	@Test
	public void testTargetWithoutEntity2MOD() {
		
		// MOD undefined 
		Target target = createTargetWithoutEntity("x", "foo.txt", "${RES}/${MOD}/foo", "foo.vm", "1");
		assertEquals("foo.txt", target.getFile());
		assertEquals("src/main/resources/${MOD}/foo", target.getFolder());
		assertEquals("C:\\FOO\\BAR/src/main/resources/${MOD}/foo/foo.txt", target.getOutputFileFullPath());
		
		// MOD defined 
		Map<String,String> variables = getVariables();
		variables.put("MOD", "MyModel");
		target = createTargetWithoutEntity("xx", "foo.txt", "${RES}/${MOD}/foo", "foo.vm", "1", variables);
		assertEquals("src/main/resources/MyModel/foo", target.getFolder());
		assertEquals("C:\\FOO\\BAR/src/main/resources/MyModel/foo/foo.txt", target.getOutputFileFullPath());

		target = createTargetWithoutEntity("xxx", "foo-${MOD_UC}.txt", "${RES}/${MOD_UC}/foo", "foo.vm", "1", variables);
		assertEquals("foo-MYMODEL.txt", target.getFile());
		assertEquals("src/main/resources/MYMODEL/foo", target.getFolder());
		assertEquals("C:\\FOO\\BAR/src/main/resources/MYMODEL/foo/foo-MYMODEL.txt", target.getOutputFileFullPath());

		target = createTargetWithoutEntity("xxxx", "foo-${MOD_LC}.txt", "${RES}/${MOD_LC}/foo", "foo.vm", "1", variables);
		assertEquals("foo-mymodel.txt", target.getFile());
		assertEquals("src/main/resources/mymodel/foo", target.getFolder());
		assertEquals("C:\\FOO\\BAR/src/main/resources/mymodel/foo/foo-mymodel.txt", target.getOutputFileFullPath());
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
