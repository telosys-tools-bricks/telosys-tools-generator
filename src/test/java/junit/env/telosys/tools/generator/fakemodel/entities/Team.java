package junit.env.telosys.tools.generator.fakemodel.entities;

import org.telosys.tools.dsl.model.DslModelAttribute;
import org.telosys.tools.dsl.model.DslModelEntity;
import org.telosys.tools.generic.model.Attribute;
import org.telosys.tools.generic.model.types.NeutralType;

public class Team extends DslModelEntity {

	public static final String ENTITY_NAME = "Team";
	
	public Team() {
		super(ENTITY_NAME);
		addAttribute(codeAttribute());
		addAttribute(nameAttribute());
	}
	
	private Attribute codeAttribute() {
		DslModelAttribute attribute = new DslModelAttribute("code", NeutralType.STRING);
		attribute.setKeyElement(true);
		attribute.setDatabaseName("CODE");
		attribute.setDatabaseType("VARCHAR");
		return attribute ;
	}

	private Attribute nameAttribute() {
		DslModelAttribute attribute = new DslModelAttribute("name", NeutralType.STRING);
		attribute.setDatabaseName("NAME");
		attribute.setDatabaseType("VARCHAR");
		return attribute ;
	}

}
