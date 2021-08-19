package junit.env.telosys.tools.generator.fakemodel.entities;

import org.telosys.tools.generic.model.Attribute;
import org.telosys.tools.generic.model.types.NeutralType;

import junit.env.telosys.tools.generator.fakemodel.FakeAttribute;
import junit.env.telosys.tools.generator.fakemodel.FakeEntity;
import junit.env.telosys.tools.generator.fakemodel.FakeForeignKey;

public class EmpTeam extends FakeEntity {

	public static final String ENTITY_NAME  = "EmpTeam";
	public static final String ENTITY_TABLE = "EMP_TEAM";

	public EmpTeam() {
		super(ENTITY_NAME, ENTITY_TABLE);
		storeAttribute(teamCodeAttribute());
		storeAttribute(empIdAttribute());
		
		storeForeignKey(new FakeForeignKey("FK1", ENTITY_TABLE, "EMPLOYEE"));
		storeForeignKey(new FakeForeignKey("FK2", ENTITY_TABLE, "TEAM"));
	}
	
	private Attribute teamCodeAttribute() {
		FakeAttribute attribute = new FakeAttribute("teamCode", NeutralType.STRING, true);
		attribute.setDatabaseName("TEAM_CODE");
		attribute.setDatabaseType("VARCHAR");
		attribute.setFKSimple(true);
		return attribute ;
	}

	private Attribute empIdAttribute() {
		FakeAttribute attribute = new FakeAttribute("empId", NeutralType.INTEGER, true);
		attribute.setDatabaseName("EMP_ID");
		attribute.setDatabaseType("NUMBER");
		attribute.setFKSimple(true);
		return attribute ;
	}

}
