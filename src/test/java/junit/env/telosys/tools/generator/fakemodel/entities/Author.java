package junit.env.telosys.tools.generator.fakemodel.entities;

import org.telosys.tools.dsl.model.DslModelAttribute;
import org.telosys.tools.dsl.model.DslModelEntity;
import org.telosys.tools.generic.model.Attribute;
import org.telosys.tools.generic.model.types.NeutralType;

public class Author extends DslModelEntity {

	public static final String ENTITY_NAME = "Author";

	public Author() {
		super(ENTITY_NAME); 
		this.addAttribute(idAttribute());
		this.addAttribute(firstNameAttribute());
		this.addAttribute(lastNameAttribute());
	}
	
	private Attribute idAttribute() {
		DslModelAttribute attribute = new DslModelAttribute("id", NeutralType.INTEGER);
		attribute.setKeyElement(true);
		attribute.setDatabaseName("ID");
		attribute.setDatabaseType("NUMBER");
		attribute.setNotNull(true);
		attribute.setDatabaseDefaultValue("0");
		return attribute ;
	}

	private Attribute firstNameAttribute() {
		DslModelAttribute attribute = new DslModelAttribute("firstName", NeutralType.STRING);
		attribute.setDatabaseName("FIRST_NAME");
		attribute.setDatabaseType("VARCHAR");
		attribute.setDatabaseDefaultValue("default-firts-name");
		return attribute ;
	}

	private Attribute lastNameAttribute() {
		DslModelAttribute attribute = new DslModelAttribute("lastName", NeutralType.STRING);
		attribute.setDatabaseName("LAST_NAME");
		attribute.setDatabaseType("VARCHAR");
		return attribute ;
	}

}
