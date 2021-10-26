package junit.env.telosys.tools.generator.fakemodel.entities;

import org.telosys.tools.generic.model.Attribute;
import org.telosys.tools.generic.model.types.NeutralType;

import junit.env.telosys.tools.generator.fakemodel.FakeAttribute;
import junit.env.telosys.tools.generator.fakemodel.FakeEntity;

public class Composite extends FakeEntity {

	public static final String ENTITY_NAME = "Composite";

	public Composite() {
		super(ENTITY_NAME, ""); // no table name
		storeAttribute(grpAttribute());
		storeAttribute(subGroupAttribute());
		storeAttribute(nameAttribute());
	}
	
	private Attribute grpAttribute() {
		FakeAttribute attribute = new FakeAttribute("grp", NeutralType.STRING, true);
		//attribute.setDatabaseName("xxx");
		//attribute.setDatabaseType("xxx");
		//attribute.setDatabaseNotNull(true);
		attribute.setNotNull(true);
		//attribute.isUnique(true); // TODO
		// attribute.setDatabaseDefaultValue("XX");
		return attribute ;
	}

	private Attribute subGroupAttribute() {
		FakeAttribute attribute = new FakeAttribute("subGrp", NeutralType.INTEGER, true);
		//attribute.setDatabaseName("xxx");
		//attribute.setDatabaseType("xxx");
		//attribute.setDatabaseNotNull(true);
		attribute.setNotNull(true);
		//attribute.isUnique(true); // TODO
		// attribute.setDatabaseDefaultValue("XX");
		return attribute ;
	}

	private Attribute nameAttribute() {
		FakeAttribute attribute = new FakeAttribute("name", NeutralType.STRING, false);
		return attribute ;
	}

}
