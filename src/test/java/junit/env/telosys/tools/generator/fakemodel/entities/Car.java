package junit.env.telosys.tools.generator.fakemodel.entities;

import org.telosys.tools.dsl.model.DslModelAttribute;
import org.telosys.tools.dsl.model.DslModelEntity;
import org.telosys.tools.dsl.model.DslModelLink;
import org.telosys.tools.generic.model.Attribute;
import org.telosys.tools.generic.model.Link;
import org.telosys.tools.generic.model.enums.Cardinality;
import org.telosys.tools.generic.model.types.NeutralType;

public class Car extends DslModelEntity {

	public static final String ENTITY_NAME = "Car";
	
	public Car() {
		super(ENTITY_NAME);
		setDatabaseTable("CAR");
		addAttribute(idAttribute());
		addAttribute(nameAttribute());
		addAttribute(descAttribute());
		addLink(brandLink());
	}
	
	private Attribute idAttribute() {
		DslModelAttribute a = new DslModelAttribute("id", NeutralType.INTEGER);
		a.setKeyElement(true);
		a.setDatabaseName("ID");
		a.setNotNull(true);
		return a ;
	}

	private Attribute nameAttribute() {
		DslModelAttribute a = new DslModelAttribute("name", NeutralType.STRING);
		a.setDatabaseName("NAME");
		a.setMaxLength(60);
		return a ;
	}
	
	private Attribute descAttribute() { 
		DslModelAttribute a = new DslModelAttribute("desc", NeutralType.STRING);
		a.setDatabaseName("DESC");
		a.setMaxLength(60);
		return a ;
	}

	private Link brandLink() { 
		DslModelLink link = new DslModelLink("brand");
		link.setReferencedEntityName("Brand");
		link.setCardinality(Cardinality.MANY_TO_ONE);
		return link;
	}
}
