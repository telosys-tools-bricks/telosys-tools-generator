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
	
	private Target getTargetOnce(String templateFile, String generatedFile, Entity entity) {
		TargetDefinition targetDefinition = new TargetDefinition(
				"Fake target", 
				generatedFile, 
				"generated-files", 
				templateFile, 
				"1");
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
		Target target = getTargetOnce(templateFile, generatedFile, entity);
		List<String> selectedEntitiesNames = getSelectedEntities();
		generator.generateTarget(target, model, selectedEntitiesNames, null);
	}

	@Test
	public void testSqlExample() throws GeneratorException {
		launchGeneration("sql_example.vm", "sql_example.sql");
	}

	@Test
	public void testCreateDbDefault() throws GeneratorException {
		launchGeneration("create_db_default.vm", "create_db_default.sql");
	}

	@Test
	public void testCreateDbPostgresql() throws GeneratorException {
		launchGeneration("create_db_postgresql.vm", "create_db_postgresql.sql");
	}

	@Test
	public void testCreateDbOracle() throws GeneratorException {
		launchGeneration("create_db_oracle.vm", "create_db_oracle.sql");
	}

}
