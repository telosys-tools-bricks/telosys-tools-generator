package junit.env.telosys.tools.generator.fakemodel.entities;

import org.telosys.tools.generic.model.Attribute;
import org.telosys.tools.generic.model.types.NeutralType;

import junit.env.telosys.tools.generator.fakemodel.FakeAttribute;
import junit.env.telosys.tools.generator.fakemodel.FakeEntity;

public class Car extends FakeEntity {

	public static final String ENTITY_NAME = "Car";
	
	public Car() {
		super(ENTITY_NAME, "CAR");
		storeAttribute(idAttribute());
		storeAttribute(nameAttribute());
		storeAttribute(descAttribute());
	}
	
	private Attribute idAttribute() {
		FakeAttribute a = new FakeAttribute("id", NeutralType.INTEGER, true);
		a.setDatabaseName("ID");
		a.setNotNull(true);
		return a ;
	}

	private Attribute nameAttribute() {
		FakeAttribute a = new FakeAttribute("name", NeutralType.STRING, false);
		a.setDatabaseName("NAME");
		a.setMaxLength(60);
		return a ;
	}
	
	private Attribute descAttribute() { 
		FakeAttribute a = new FakeAttribute("desc", NeutralType.STRING, false);
		a.setDatabaseName("DESC");
		a.setMaxLength(60);
		return a ;
	}
}
