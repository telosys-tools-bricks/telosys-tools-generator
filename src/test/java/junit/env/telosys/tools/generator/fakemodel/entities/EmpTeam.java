package junit.env.telosys.tools.generator.fakemodel.entities;

import org.telosys.tools.dsl.model.DslModelAttribute;
import org.telosys.tools.dsl.model.DslModelEntity;
import org.telosys.tools.dsl.model.DslModelForeignKeyAttribute;
import org.telosys.tools.generic.model.Attribute;
import org.telosys.tools.generic.model.types.NeutralType;

public class EmpTeam extends DslModelEntity {

	public static final String ENTITY_NAME  = "EmpTeam";
	public static final String ENTITY_TABLE = "EMP_TEAM";

	public EmpTeam() {
		super(ENTITY_NAME);
		setDatabaseTable(ENTITY_TABLE);
		addAttribute(teamCodeAttribute());
		addAttribute(empIdAttribute());
		// Foreign Keys
		Builder.foreignKey(this, "FK1", "EmpTeam", "Employee", new DslModelForeignKeyAttribute(1, "empId",    "id"));
		Builder.foreignKey(this, "FK2", "EmpTeam", "Team",     new DslModelForeignKeyAttribute(1, "teamCode", "code") );
	}
	
	private Attribute teamCodeAttribute() {
		DslModelAttribute attribute = new DslModelAttribute("teamCode", NeutralType.STRING);
		attribute.setKeyElement(true);
		attribute.setDatabaseName("TEAM_CODE");
		attribute.setDatabaseType("VARCHAR");
		attribute.setFKSimple(true);
		return attribute ;
	}

	private Attribute empIdAttribute() {
		DslModelAttribute attribute = new DslModelAttribute("empId", NeutralType.INTEGER);
		attribute.setKeyElement(true);
		attribute.setDatabaseName("EMP_ID");
		attribute.setDatabaseType("NUMBER");
		attribute.setFKSimple(true);
		return attribute ;
	}

}
