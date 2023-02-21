package junit.env.telosys.tools.generator.context;

import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.cfg.TelosysToolsCfgManager;
import org.telosys.tools.dsl.model.DslModel;
import org.telosys.tools.dsl.model.DslModelEntity;
import org.telosys.tools.generator.context.EntityInContext;
import org.telosys.tools.generator.context.EnvInContext;
import org.telosys.tools.generator.context.ModelInContext;
import org.telosys.tools.generic.model.Entity;
import org.telosys.tools.generic.model.Model;

public class Builder {
	
	private static final String FAKE_MODEL_NAME = "FakeModel";

	/**
	 * Build TelosysToolsCfg instance with default values
	 * @return
	 */
	private static final TelosysToolsCfg telosysToolsCfg = buildTelosysToolsCfg();

	public static TelosysToolsCfg buildTelosysToolsCfg() {
		TelosysToolsCfgManager cfgManager = new TelosysToolsCfgManager("projectAbsolutePath");
		return cfgManager.createDefaultTelosysToolsCfg();
	}
	
	private static final EnvInContext envInContext = new EnvInContext() ; 
			
	private Builder() {
	}

//	public static Model buildVoidModel() {
//		return new DslModel(FAKE_MODEL);
//	}
	
	public static ModelInContext buildVoidModelInContext() {
//		Model model = buildVoidModel();
		DslModel model = new DslModel(FAKE_MODEL_NAME);
		return new ModelInContext(model, telosysToolsCfg, envInContext);
	}

	public static ModelInContext buildModelInContext(Model model) {
		return new ModelInContext(model, telosysToolsCfg, envInContext);
	}
	
	
	public static EntityInContext buildEntityInContext(String entityName, String tableName) {
//		DslModel fakeModel = new DslModel(FAKE_MODEL);
//		DslModelEntity entity = new DslModelEntity(entityName);
//		entity.setDatabaseTable(tableName);
//		fakeModel.addEntity(entity);
//		ModelInContext modelInContext = buildModelInContext(fakeModel);
//		return modelInContext.getEntityByClassName(entityName);
		DslModelEntity entity = new DslModelEntity(entityName);
		entity.setDatabaseTable(tableName);
		return buildEntityInContext(entity);
	}

	public static EntityInContext buildEntityInContext(Entity entity) {
		DslModel fakeModel = new DslModel(FAKE_MODEL_NAME);
		fakeModel.addEntity(entity);
		ModelInContext modelInContext = buildModelInContext(fakeModel);
		return modelInContext.getEntityByClassName(entity.getClassName());
	}
}
