package junit.env.telosys.tools.generator.fakemodel;

import org.telosys.tools.generic.model.Attribute;
import org.telosys.tools.generic.model.Entity;
import org.telosys.tools.generic.model.Model;

public class FakeModelProvider {

	public static final String EMPLOYEE_CLASS_NAME = "Employee";
	public static final String AUTHOR_CLASS_NAME   = "Author";
	
	private FakeModelProvider() {
	}
	
	public static void checkEntity(Model model, String entityClassName) {
		Entity entity = model.getEntityByClassName(entityClassName);
		if ( entity == null ) throw new IllegalStateException("Entity not found");
		if ( entity.getAttributes() == null ) throw new IllegalStateException("Attributes == null");
		if ( entity.getAttributes().isEmpty() ) throw new IllegalStateException("Attributes is empty");
		int k = 0 ;
		for ( Attribute a : entity.getAttributes() ) {
			if ( a.isKeyElement() ) k++;
		}
		if ( k == 0 ) throw new IllegalStateException("No key element in entity");
	}
	
	public static Model buildModel() {
		FakeModel model = new FakeModel("FakeModel");
		model.addEntity(buildEmployeeEntity());
		model.addEntity(buildAuthorEntity());
		checkEntity(model, EMPLOYEE_CLASS_NAME);
		checkEntity(model, AUTHOR_CLASS_NAME);
		return model ;
	}

	private static FakeEntity buildEmployeeEntity() {
		FakeEntity entity = new FakeEntity(EMPLOYEE_CLASS_NAME, "EMPLOYEE");
		entity.storeAttribute(buildIdAttribute());
		entity.storeAttribute(buildFirstNameAttribute());
		return entity ;
	}

	private static FakeEntity buildAuthorEntity() {
		FakeEntity entity = new FakeEntity(AUTHOR_CLASS_NAME, "AUTHOR");
		entity.storeAttribute(buildIdAttribute());
		entity.storeAttribute(buildFirstNameAttribute());
		return entity ;
	}

	private static Attribute buildIdAttribute() {
		FakeAttribute attribute = new FakeAttribute("id", "int", true);
		attribute.setDatabaseName("ID");
		attribute.setDatabaseTypeName("NUMBER");
		return attribute ;
	}

	private static Attribute buildFirstNameAttribute() {
		FakeAttribute attribute = new FakeAttribute("firstName", "string", false);
		attribute.setDatabaseName("FIRST_NAME");
		attribute.setDatabaseTypeName("VARCHAR");
		return attribute ;
	}
}
