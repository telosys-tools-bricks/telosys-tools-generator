package junit.env.telosys.tools.generator.context;

import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.cfg.TelosysToolsCfgManager;
import org.telosys.tools.generator.context.EntityInContext;
import org.telosys.tools.generator.context.EnvInContext;
import org.telosys.tools.generator.context.ModelInContext;
import org.telosys.tools.generic.model.Entity;
import org.telosys.tools.generic.model.Model;

import junit.env.telosys.tools.generator.fakemodel.FakeEntity;
import junit.env.telosys.tools.generator.fakemodel.FakeModel;

public class Builder {
	
	private static final String FAKE_MODEL = "FakeModel";

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

	public static Model buildVoidModel() {
		return new FakeModel(FAKE_MODEL);
	}
	
	public static ModelInContext buildVoidModelInContext() {
		Model model = buildVoidModel();
		return new ModelInContext(model, telosysToolsCfg, envInContext);
	}

	public static ModelInContext buildModelInContext(Model model) {
		return new ModelInContext(model, telosysToolsCfg, envInContext);
	}
	
	
	public static EntityInContext buildEntityInContext(String entityName, String tableName) {
		FakeModel fakeModel = new FakeModel(FAKE_MODEL);
		Entity fakeEntity = new FakeEntity(entityName, tableName);
		fakeModel.addEntity(fakeEntity);
		ModelInContext modelInContext = buildModelInContext(fakeModel);
		return modelInContext.getEntityByClassName(entityName);
	}

	public static EntityInContext buildEntityInContext(Entity entity) {
		FakeModel fakeModel = new FakeModel(FAKE_MODEL);
		fakeModel.addEntity(entity);
		ModelInContext modelInContext = buildModelInContext(fakeModel);
		return modelInContext.getEntityByClassName(entity.getClassName());
	}
}
