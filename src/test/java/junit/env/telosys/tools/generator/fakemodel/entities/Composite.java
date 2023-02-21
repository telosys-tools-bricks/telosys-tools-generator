package junit.env.telosys.tools.generator.fakemodel.entities;

import org.telosys.tools.dsl.model.DslModelAttribute;
import org.telosys.tools.dsl.model.DslModelEntity;
import org.telosys.tools.generic.model.Attribute;
import org.telosys.tools.generic.model.types.NeutralType;

public class Composite extends DslModelEntity {

	public static final String ENTITY_NAME = "Composite";

	public Composite() {
		super(ENTITY_NAME);
		addAttribute(grpAttribute());
		addAttribute(subGroupAttribute());
		addAttribute(nameAttribute());
	}
	
	private Attribute grpAttribute() {
		DslModelAttribute attribute = new DslModelAttribute("grp", NeutralType.STRING);
		attribute.setKeyElement(true);
		attribute.setNotNull(true);
		return attribute ;
	}

	private Attribute subGroupAttribute() {
		DslModelAttribute attribute = new DslModelAttribute("subGrp", NeutralType.INTEGER);
		attribute.setKeyElement(true);
		attribute.setNotNull(true);
		return attribute ;
	}

	private Attribute nameAttribute() {
		DslModelAttribute attribute = new DslModelAttribute("name", NeutralType.STRING);
		return attribute ;
	}

}
