package junit.env.telosys.tools.generator.fakemodel;

import org.telosys.tools.generic.model.Attribute;
import org.telosys.tools.generic.model.Entity;
import org.telosys.tools.generic.model.Model;

import junit.env.telosys.tools.generator.fakemodel.entities.Author;
import junit.env.telosys.tools.generator.fakemodel.entities.Composite;
import junit.env.telosys.tools.generator.fakemodel.entities.Employee;

public class FakeModelProvider {

	private FakeModelProvider() {
	}
	
	public static Model buildModel() {
		FakeModel model = new FakeModel("FakeModel");
		model.addEntity( checkEntity( new Employee() ) );
		model.addEntity( checkEntity( new Author() ) );
		model.addEntity( checkEntity( new Composite() ) );
		return model ;
	}

	private static Entity checkEntity(Entity entity) {
		if ( entity == null ) throw new IllegalStateException("Entity not found");
		if ( entity.getAttributes() == null ) throw new IllegalStateException("Attributes == null");
		if ( entity.getAttributes().isEmpty() ) throw new IllegalStateException("Attributes is empty");
		int k = 0 ;
		for ( Attribute a : entity.getAttributes() ) {
			if ( a.isKeyElement() ) k++;
		}
		if ( k == 0 ) throw new IllegalStateException("No key element in entity");
		return entity; // OK
	}
	

}
