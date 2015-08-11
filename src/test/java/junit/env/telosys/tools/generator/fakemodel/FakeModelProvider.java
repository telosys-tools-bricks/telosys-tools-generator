package junit.env.telosys.tools.generator.fakemodel;

import org.telosys.tools.generic.model.Attribute;
import org.telosys.tools.generic.model.Model;

public class FakeModelProvider {

	public static Model buildModel() {
		ModelInFakeModel model = new ModelInFakeModel();
		model.setName("FakeModel");
		model.storeEntity(buildEntity_Employee());
		model.storeEntity(buildEntity_Author());
		return model ;
	}

	public static EntityInFakeModel buildEntity_Employee() {
		EntityInFakeModel entity = new EntityInFakeModel();
		entity.setClassName("Employee");
		entity.setDatabaseTable("EMPLOYEE");
		entity.storeAttribute(buildAttribute_FirstName());
		return entity ;
	}

	public static EntityInFakeModel buildEntity_Author() {
		EntityInFakeModel entity = new EntityInFakeModel();
		entity.setClassName("Author");
		entity.setDatabaseTable("AUTHOR");
		entity.storeAttribute(buildAttribute_FirstName());
		return entity ;
	}

	public static Attribute buildAttribute_FirstName() {
		AttributeInFakeModel attribute = new AttributeInFakeModel();
		attribute.setDatabaseName("FIRST_NAME");
		attribute.setDatabaseTypeName("VARCHAR");
		attribute.setName("firstName");
		attribute.setFullType("java.lang.String");
		return attribute ;
	}
}
