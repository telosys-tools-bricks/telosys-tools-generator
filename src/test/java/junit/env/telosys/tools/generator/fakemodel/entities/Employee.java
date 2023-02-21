package junit.env.telosys.tools.generator.fakemodel.entities;

import org.telosys.tools.dsl.model.DslModelAttribute;
import org.telosys.tools.dsl.model.DslModelEntity;
import org.telosys.tools.dsl.model.DslModelLink;
import org.telosys.tools.generic.model.Attribute;
import org.telosys.tools.generic.model.Link;
import org.telosys.tools.generic.model.enums.Cardinality;
import org.telosys.tools.generic.model.types.NeutralType;

public class Employee extends DslModelEntity {

	public static final String ENTITY_NAME = "Employee";
	
	public Employee() {
		super(ENTITY_NAME); 
		// Entity tags
		Builder.tag(this, "Foo", "abc");
		// Entity attributes
		addAttribute(idAttribute());
		addAttribute(firstNameAttribute());
		addAttribute(lastNameAttribute());
		// Entity links
		addLink(countryLink());
	}
	
	private Attribute idAttribute() {
		DslModelAttribute attribute = new DslModelAttribute("id", NeutralType.INTEGER);
		attribute.setKeyElement(true);
		attribute.setDatabaseName("ID");
		attribute.setNotNull(true);
		return attribute ;
	}

	private Attribute firstNameAttribute() {
		DslModelAttribute attribute = new DslModelAttribute("firstName", NeutralType.STRING);
		attribute.setDatabaseDefaultValue("xy");
		return attribute ;
	}

	private Attribute lastNameAttribute() {
		DslModelAttribute attribute = new DslModelAttribute("lastName", NeutralType.STRING);
		attribute.setDatabaseName("LAST_NAME");
		attribute.setDatabaseType("VARCHAR");
		return attribute ;
	}
	
	private Link countryLink() {
		DslModelLink link =  new DslModelLink("country");
		link.setReferencedEntityName("Country");
		link.setCardinality(Cardinality.MANY_TO_ONE);
		
		Builder.tag(link, "Foo", "aaa");
		return link;
	}
}
