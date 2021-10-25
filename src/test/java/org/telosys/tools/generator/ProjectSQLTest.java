package org.telosys.tools.generator;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.telosys.tools.commons.bundles.TargetDefinition;
import org.telosys.tools.generator.context.Target;
import org.telosys.tools.generic.model.Entity;
import org.telosys.tools.generic.model.Model;

import junit.env.telosys.tools.generator.FakeProject;
import junit.env.telosys.tools.generator.fakemodel.FakeModelProvider;
import junit.env.telosys.tools.generator.fakemodel.entities.Employee;


public class ProjectSQLTest {

	private static final String BUNDLE = "bundle-sql";
	private FakeProject fakeProject = new FakeProject("proj-sql");
	
	private Target getTarget(String templateFile, String generatedFile, Entity entity) {
		TargetDefinition targetDefinition = new TargetDefinition(
				"Fake target", 
				generatedFile, 
				"generated-files", 
				templateFile, 
				"*");
		return new Target( fakeProject.getTelosysToolsCfg(), targetDefinition, entity );  // v 3.3.0
	}
	
	private List<String> getSelectedEntities() {
		List<String> list = new LinkedList<>();
		list.add("Author");
		return list;
	}
	
	private void launchGeneration(String templateFile, String generatedFile) throws GeneratorException {
		Generator generator = fakeProject.getGenerator(BUNDLE) ;
		Model model = FakeModelProvider.buildModel();
		Entity entity = model.getEntityByClassName(Employee.ENTITY_NAME);
		Target target = getTarget(templateFile, generatedFile, entity);
		List<String> selectedEntitiesNames = getSelectedEntities();
		generator.generateTarget(target, model, selectedEntitiesNames, null);
	}

	@Test
	public void testSqlDefault() throws GeneratorException {
		launchGeneration("create_table_default.vm", "create_${BEANNAME}_default.sql");
	}

	@Test
	public void testSqlPostgresql() throws GeneratorException {
		launchGeneration("create_table_postgresql.vm", "create_${BEANNAME}_postgresql.sql");
	}


}
