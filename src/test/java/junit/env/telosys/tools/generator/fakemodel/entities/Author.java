package junit.env.telosys.tools.generator.fakemodel.entities;

import org.telosys.tools.generic.model.Attribute;
import org.telosys.tools.generic.model.types.NeutralType;

import junit.env.telosys.tools.generator.fakemodel.FakeAttribute;
import junit.env.telosys.tools.generator.fakemodel.FakeEntity;

public class Author extends FakeEntity {

	public static final String ENTITY_NAME = "Author";

	public Author() {
		//super(ENTITY_NAME, "AUTHOR");
		super(ENTITY_NAME, ""); // no table name
		storeAttribute(idAttribute());
		storeAttribute(firstNameAttribute());
		storeAttribute(lastNameAttribute());
	}
	
	private Attribute idAttribute() {
		FakeAttribute attribute = new FakeAttribute("id", NeutralType.INTEGER, true);
		attribute.setDatabaseName("ID");
		attribute.setDatabaseType("NUMBER");
		//attribute.setDatabaseNotNull(true);
		attribute.setNotNull(true);
		//attribute.isUnique(true); // TODO
		attribute.setDatabaseDefaultValue("0");
		return attribute ;
	}

	private Attribute firstNameAttribute() {
		FakeAttribute attribute = new FakeAttribute("firstName", NeutralType.STRING, false);
		attribute.setDatabaseName("FIRST_NAME");
		attribute.setDatabaseType("VARCHAR");
		attribute.setDatabaseDefaultValue("default-firts-name");
		return attribute ;
	}

	private Attribute lastNameAttribute() {
		FakeAttribute attribute = new FakeAttribute("lastName", "string", false);
		attribute.setDatabaseName("LAST_NAME");
		attribute.setDatabaseType("VARCHAR");
		return attribute ;
	}

}
