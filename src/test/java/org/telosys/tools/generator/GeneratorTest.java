package org.telosys.tools.generator;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.telosys.tools.commons.TelosysToolsLogger;
import org.telosys.tools.commons.bundles.TargetDefinition;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.variables.Variable;
import org.telosys.tools.generator.context.Target;
import org.telosys.tools.generic.model.Entity;
import org.telosys.tools.generic.model.Model;

import junit.env.telosys.tools.generator.LoggerProvider;
import junit.env.telosys.tools.generator.TestsEnv;
import junit.env.telosys.tools.generator.fakemodel.FakeModelProvider;


public class GeneratorTest {

	private Generator getGenerator(String bundleName) {
		File projectFolder = TestsEnv.getTestFolder("proj-utf8");
		TelosysToolsCfg telosysToolsCfg = TestsEnv.loadTelosysToolsCfg(projectFolder);
		TelosysToolsLogger logger = LoggerProvider.getLogger();
		return new Generator(telosysToolsCfg,  bundleName,  logger) ;
	}
	private Variable[] getVariables() {
		Variable[] variables = new Variable[5] ;
		int i = 0 ;
		variables[i++] = new Variable("ROOT_PKG",   "org.foo.bar");
		variables[i++] = new Variable("ENTITY_PKG", "org.foo.bar.bean");
		variables[i++] = new Variable("VAR1",       "VALUE1");
		variables[i++] = new Variable("VAR2",       "VALUE2");
		variables[i++] = new Variable("SRC",        "/src");
		return variables ;
	}

	private Target getTarget(String templateFile, String generatedFile, Entity entity) {
		
		TargetDefinition targetDefinition = new TargetDefinition(
				"Fake target", 
				generatedFile, // "utf8.txt", 
				"generated-files", 
				templateFile, //"utf8_txt.vm", 
				"*");
		return new Target( targetDefinition, entity, getVariables() ); 
	}
	
	private List<String> getSelectedEntities() {
		List<String> list = new LinkedList<>();
		list.add("Author");
		return list;
	}
	
	private void launchGeneration(String templateFile, String generatedFile) throws GeneratorException {
		Generator generator = getGenerator("bundle-utf8");
		
		Model model = FakeModelProvider.buildModel();
		Entity entity = model.getEntityByClassName(FakeModelProvider.EMPLOYEE_CLASS_NAME);
		Target target = getTarget(templateFile, generatedFile, entity);
		List<String> selectedEntitiesNames = getSelectedEntities();
		generator.generateTarget(target, model, selectedEntitiesNames, null);
	}
	
	@Test
	public void testUtf8Txt() throws GeneratorException {
		launchGeneration("utf8_txt.vm", "utf8.txt");
	}

	@Test
	public void testOpenapiYamlTxt() throws GeneratorException {
		launchGeneration("openapi_yaml.vm", "openapi_yaml.txt");
	}
}
