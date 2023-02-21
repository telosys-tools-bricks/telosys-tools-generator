package junit.env.telosys.tools.generator.fakemodel.entities;

import org.telosys.tools.dsl.model.DslModelAttribute;
import org.telosys.tools.dsl.model.DslModelEntity;
import org.telosys.tools.generic.model.Attribute;
import org.telosys.tools.generic.model.types.NeutralType;

public class Foo1 extends DslModelEntity {

	public static final String ENTITY_NAME = Foo1.class.getSimpleName();
	
	public Foo1() {
		super(ENTITY_NAME);
		addAttribute(idAttribute());
	}
	
	private Attribute idAttribute() {
		DslModelAttribute a = new DslModelAttribute("id", NeutralType.INTEGER);
		a.setKeyElement(true);
		a.setDatabaseName("ID");
		a.setNotNull(true);
		return a ;
	}

}
