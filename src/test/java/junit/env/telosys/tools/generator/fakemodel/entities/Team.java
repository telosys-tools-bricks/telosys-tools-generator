package junit.env.telosys.tools.generator.fakemodel.entities;

import org.telosys.tools.generic.model.Attribute;
import org.telosys.tools.generic.model.types.NeutralType;

import junit.env.telosys.tools.generator.fakemodel.FakeAttribute;
import junit.env.telosys.tools.generator.fakemodel.FakeEntity;

public class Team extends FakeEntity {

	public static final String ENTITY_NAME = "Team";
	
	public Team() {
		super(ENTITY_NAME, "TEAM");
		storeAttribute(codeAttribute());
		storeAttribute(nameAttribute());
	}
	
	private Attribute codeAttribute() {
		FakeAttribute attribute = new FakeAttribute("code", NeutralType.STRING, true);
		attribute.setDatabaseName("CODE");
		attribute.setDatabaseType("VARCHAR");
		return attribute ;
	}

	private Attribute nameAttribute() {
		FakeAttribute attribute = new FakeAttribute("name", NeutralType.STRING, false);
		attribute.setDatabaseName("NAME");
		attribute.setDatabaseType("VARCHAR");
		return attribute ;
	}

}
