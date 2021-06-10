package junit.env.telosys.tools.generator.fakemodel.entities;

import org.telosys.tools.generic.model.Attribute;
import org.telosys.tools.generic.model.types.NeutralType;

import junit.env.telosys.tools.generator.fakemodel.FakeAttribute;
import junit.env.telosys.tools.generator.fakemodel.FakeEntity;

public class Author extends FakeEntity {

	public static final String ENTITY_NAME = "Author";

	public Author() {
		super(ENTITY_NAME, "AUTHOR");
		storeAttribute(idAttribute());
		storeAttribute(firstNameAttribute());
		storeAttribute(lastNameAttribute());
	}
	
	private Attribute idAttribute() {
		FakeAttribute attribute = new FakeAttribute("id", NeutralType.INTEGER, true);
		attribute.setDatabaseName("ID");
		attribute.setDatabaseTypeName("NUMBER");
		return attribute ;
	}

	private Attribute firstNameAttribute() {
		FakeAttribute attribute = new FakeAttribute("firstName", NeutralType.STRING, false);
		attribute.setDatabaseName("FIRST_NAME");
		attribute.setDatabaseTypeName("VARCHAR");
		return attribute ;
	}

	private Attribute lastNameAttribute() {
		FakeAttribute attribute = new FakeAttribute("lastName", "string", false);
		attribute.setDatabaseName("LAST_NAME");
		attribute.setDatabaseTypeName("VARCHAR");
		return attribute ;
	}

}
