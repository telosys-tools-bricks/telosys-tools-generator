package junit.env.telosys.tools.generator.fakemodel.entities;

import org.telosys.tools.generic.model.Attribute;
import org.telosys.tools.generic.model.types.NeutralType;

import junit.env.telosys.tools.generator.fakemodel.FakeAttribute;
import junit.env.telosys.tools.generator.fakemodel.FakeEntity;

public class Foo1 extends FakeEntity {

	public static final String ENTITY_NAME = Foo1.class.getSimpleName();
	
	public Foo1() {
		super(ENTITY_NAME, "TEAM");
		storeAttribute(idAttribute());
	}
	
	private Attribute idAttribute() {
		FakeAttribute a = new FakeAttribute("id", NeutralType.INTEGER, true);
		a.setDatabaseName("ID");
		a.setNotNull(true);
		return a ;
	}

}
