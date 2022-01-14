package junit.env.telosys.tools.generator.fakemodel.entities;

import org.telosys.tools.generic.model.Attribute;
import org.telosys.tools.generic.model.Link;
import org.telosys.tools.generic.model.enums.Cardinality;
import org.telosys.tools.generic.model.types.NeutralType;

import junit.env.telosys.tools.generator.fakemodel.FakeAttribute;
import junit.env.telosys.tools.generator.fakemodel.FakeEntity;
import junit.env.telosys.tools.generator.fakemodel.FakeLink;
import junit.env.telosys.tools.generator.fakemodel.FakeTagContainer;
import junit.env.telosys.tools.generator.fakemodel.Tag;

public class Employee extends FakeEntity {

	public static final String ENTITY_NAME = "Employee";
	
	public Employee() {
		super(ENTITY_NAME, ""); //no database table
		// Entity tags
		defineTag(new Tag("Foo", "abc"));
		// Entity attributes
		storeAttribute(idAttribute());
		storeAttribute(firstNameAttribute());
		storeAttribute(lastNameAttribute());
		// Entity links
		storeLink(countryLink());
	}
	
	private Attribute idAttribute() {
		FakeAttribute attribute = new FakeAttribute("id", NeutralType.INTEGER, true);
		attribute.setDatabaseName("ID");
		attribute.setNotNull(true);
		return attribute ;
	}

	private Attribute firstNameAttribute() {
		FakeAttribute attribute = new FakeAttribute("firstName", NeutralType.STRING, false);
		attribute.setDatabaseDefaultValue("xy");
		return attribute ;
	}

	private Attribute lastNameAttribute() {
		FakeAttribute attribute = new FakeAttribute("lastName", NeutralType.STRING, false);
		attribute.setDatabaseName("LAST_NAME");
		attribute.setDatabaseType("VARCHAR");
		return attribute ;
	}
	
	private Link countryLink() {
		FakeLink link =  new FakeLink("country", "Country", Cardinality.MANY_TO_ONE);
		FakeTagContainer tags = (FakeTagContainer)link.getTagContainer();
		tags.addTag(new Tag("Foo", "aaa"));
		return link;
	}
}
