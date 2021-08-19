package junit.env.telosys.tools.generator.fakemodel.entities;

import org.telosys.tools.generic.model.Attribute;
import org.telosys.tools.generic.model.types.NeutralType;

import junit.env.telosys.tools.generator.fakemodel.FakeAttribute;
import junit.env.telosys.tools.generator.fakemodel.FakeEntity;

public class Foo2 extends FakeEntity {

	public static final String ENTITY_NAME = Foo2.class.getSimpleName();
	
	public Foo2() {
		super(ENTITY_NAME, "TEAM");
		storeAttribute(idAttribute());
		storeAttribute(nameAttribute());
	}
	
	private Attribute idAttribute() {
		FakeAttribute a = new FakeAttribute("id", NeutralType.INTEGER, true);
		a.setDatabaseName("ID");
		a.setNotNull(true);
		return a ;
	}

	private Attribute nameAttribute() {
		FakeAttribute attribute = new FakeAttribute("name", NeutralType.STRING, false);
		attribute.setDatabaseName("NAME");
		attribute.setDatabaseType("VARCHAR");
		return attribute ;
	}

}
