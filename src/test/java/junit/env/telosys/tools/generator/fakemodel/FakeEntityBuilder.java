package junit.env.telosys.tools.generator.fakemodel;

import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.cfg.TelosysToolsCfgManager;
import org.telosys.tools.dsl.model.DslModel;
import org.telosys.tools.dsl.model.DslModelEntity;
import org.telosys.tools.generator.context.EntityInContext;
import org.telosys.tools.generator.context.EnvInContext;
import org.telosys.tools.generator.context.ModelInContext;

public class FakeEntityBuilder {

	private FakeEntityBuilder() {
	}
	
	public static final EntityInContext buildEntityInContext(String entityName) {
		return buildEntityInContext(entityName, entityName.toUpperCase());
	}
	
	public static final EntityInContext buildEntityInContext(String entityName, String tableName) {
		DslModel fakeModel = new DslModel("FakeModel");
		DslModelEntity fakeEntity = new DslModelEntity(entityName);
		if ( tableName != null ) {
			fakeEntity.setDatabaseTable(tableName);
		}
		fakeModel.addEntity(fakeEntity);

		TelosysToolsCfgManager cfgManager = new TelosysToolsCfgManager("projectAbsolutePath");
		TelosysToolsCfg telosysToolsCfg = cfgManager.createDefaultTelosysToolsCfg();
		EnvInContext envInContext = new EnvInContext() ; 
		
		ModelInContext modelInContext = new ModelInContext(fakeModel, telosysToolsCfg, envInContext);
		return new EntityInContext(fakeEntity, "org.foo.pkg", modelInContext, envInContext);
	}

}
