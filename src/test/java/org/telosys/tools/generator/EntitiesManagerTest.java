package org.telosys.tools.generator;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.telosys.tools.commons.ConsoleLogger;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.generator.context.EntityInContext;
import org.telosys.tools.repository.model.RepositoryModel;
import org.telosys.tools.repository.persistence.PersistenceManager;
import org.telosys.tools.repository.persistence.PersistenceManagerFactory;
import org.telosys.tools.tests.AbstractTest;

public class EntitiesManagerTest extends AbstractTest {
	
	@Test
	public void test1() throws TelosysToolsException, GeneratorException {
		
		File file = getDbRepositoryFile() ; 
		
		PersistenceManager pm = PersistenceManagerFactory.createPersistenceManager(file, new ConsoleLogger());
		RepositoryModel repositoryModel = pm.load();
		
		EntitiesManager entitiesManager = new EntitiesManager(repositoryModel, getGeneratorConfig(), getEnvInContext() );
		
		List<EntityInContext> allEntities = entitiesManager.getAllEntities();
		System.out.println("All entities : "  );
		//--- For each entity 
		for ( EntityInContext e : allEntities ) {
			System.out.println(" . " + e );
		}
		
		System.out.println("Get entity by name "  );
		//entitiesManager.getEntity("AUTHOR"); // ERROR : Not found
		EntityInContext e = entitiesManager.getEntity("Author"); 
		System.out.println(" . " + e );
		
	}

}
