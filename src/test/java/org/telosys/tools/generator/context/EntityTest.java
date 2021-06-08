package org.telosys.tools.generator.context;

import java.util.List;

import org.junit.Test;
import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generic.model.Entity;
import org.telosys.tools.generic.model.types.NeutralType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import junit.env.telosys.tools.generator.fakemodel.FakeAttribute;
import junit.env.telosys.tools.generator.fakemodel.FakeEntity;


public class EntityTest {

	@Test
	public void keyAttributesNamesAsString() throws GeneratorException {
		EntityInContext entityInContext = buildCarEntityInContext();
		checkCarEntityInContext(entityInContext);
		
		assertEquals("id", entityInContext.keyAttributesNamesAsString(","));
		assertEquals("id", entityInContext.keyAttributesNamesAsString(", "));
		assertEquals("$id", entityInContext.keyAttributesNamesAsString(", ", "$", ""));
		assertEquals("$id@", entityInContext.keyAttributesNamesAsString(", ", "$", "@"));
		assertEquals("{{id}}", entityInContext.keyAttributesNamesAsString(", ", "{{", "}}"));
	}

	@Test
	public void attributesNamesAsString() throws GeneratorException {
		EntityInContext entity = buildCarEntityInContext();
		checkCarEntityInContext(entity);

		assertEquals("id,name,desc", entity.attributesNamesAsString(","));
		assertEquals("id, name, desc", entity.attributesNamesAsString(", "));
	}

	@Test
	public void nonKeyAttributesNamesAsString() throws GeneratorException {
		EntityInContext entity = buildCarEntityInContext();
		checkCarEntityInContext(entity);
		
		assertEquals("name,desc", entity.nonKeyAttributesNamesAsString(","));
		assertEquals("name, desc", entity.nonKeyAttributesNamesAsString(", "));
		assertEquals("$name, $desc", entity.nonKeyAttributesNamesAsString(", ", "$", ""));
		assertEquals("$name@, $desc@", entity.nonKeyAttributesNamesAsString(", ", "$", "@"));
		assertEquals("{{name}}, {{desc}}", entity.nonKeyAttributesNamesAsString(", ", "{{", "}}"));
	}

	//---------------------------------------------------------------------------
	// Test with 'Foo1' entity : only 1 attribute (key element)
	//---------------------------------------------------------------------------
	@Test
	public void keyAttributesNamesAsStringFoo1() {
		EntityInContext entityInContext = buildFoo1EntityInContext();
		
		assertEquals("id", entityInContext.keyAttributesNamesAsString(","));
		assertEquals("id", entityInContext.keyAttributesNamesAsString(", "));
		assertEquals("$id", entityInContext.keyAttributesNamesAsString(", ", "$", ""));
		assertEquals("$id@", entityInContext.keyAttributesNamesAsString(", ", "$", "@"));
		assertEquals("{{id}}", entityInContext.keyAttributesNamesAsString(", ", "{{", "}}"));
	}

	@Test
	public void attributesNamesAsStringFoo1() {
		EntityInContext entityInContext = buildFoo1EntityInContext();
		
		assertEquals("id", entityInContext.attributesNamesAsString(","));
		assertEquals("id", entityInContext.attributesNamesAsString(", "));
		assertEquals("$id", entityInContext.attributesNamesAsString(", ", "$", ""));
		assertEquals("$id@", entityInContext.attributesNamesAsString(", ", "$", "@"));
		assertEquals("{{id}}", entityInContext.attributesNamesAsString(", ", "{{", "}}"));
	}

	@Test
	public void nonKeyAttributesNamesAsStringFoo1() {
		EntityInContext entityInContext = buildFoo1EntityInContext();
		
		assertEquals("", entityInContext.nonKeyAttributesNamesAsString(","));
		assertEquals("", entityInContext.nonKeyAttributesNamesAsString(", "));
		assertEquals("", entityInContext.nonKeyAttributesNamesAsString(", ", "$", ""));
		assertEquals("", entityInContext.nonKeyAttributesNamesAsString(", ", "$", "@"));
		assertEquals("", entityInContext.nonKeyAttributesNamesAsString(", ", "{{", "}}"));
	}

	//---------------------------------------------------------------------------
	// Entity and attributes builders
	//---------------------------------------------------------------------------
	private void checkCarEntityInContext(EntityInContext entityInContext) throws GeneratorException {
		assertEquals(3, entityInContext.getAttributes().size());
		assertEquals(1, entityInContext.getKeyAttributes().size());
		assertEquals(2, entityInContext.getNonKeyAttributes().size());
		assertTrue( entityInContext.getAttributeByColumnName("ID").isKeyElement() );
		assertFalse( entityInContext.getAttributeByColumnName("NAME").isKeyElement() );
		assertFalse( entityInContext.getAttributeByColumnName("DESC").isKeyElement() );
	}

	private EntityInContext buildCarEntityInContext() { 
		System.out.println("\nBuilding a new 'Car' entity ...");
		EnvInContext env = new EnvInContext();
		Entity entity = buildCarEntity();
		EntityInContext entityInContext = new EntityInContext(entity, "org.bean", null, env);
		
		printAttributes( "getAttributes()", entityInContext.getAttributes() );
		printAttributes( "getKeyAttributes()", entityInContext.getKeyAttributes() );
		printAttributes( "getNonKeyAttributes()", entityInContext.getNonKeyAttributes() );

		return entityInContext ;
	}
	
	private Entity buildCarEntity() { 
		FakeEntity entity = new FakeEntity("Car", "CAR");
		entity.storeAttribute(buildCarIdAttribute());
		entity.storeAttribute(buildCarNameAttribute());
		entity.storeAttribute(buildCarDescAttribute());
		return entity ;
	}
	private FakeAttribute buildCarIdAttribute() { 
		FakeAttribute a = new FakeAttribute("id", NeutralType.INTEGER, true);
		a.setDatabaseName("ID");
		a.setNotNull(true);
		return a ;
	}
	private FakeAttribute buildCarNameAttribute() { 
		FakeAttribute a = new FakeAttribute("name", NeutralType.STRING, false);
		a.setDatabaseName("NAME");
		a.setMaxLength(60);
		return a ;
	}
	private FakeAttribute buildCarDescAttribute() { 
		FakeAttribute a = new FakeAttribute("desc", NeutralType.STRING, false);
		a.setDatabaseName("DESC");
		a.setMaxLength(60);
		return a ;
	}

	private void printAttributes(String title, List<AttributeInContext> attributes) {
		System.out.println(title + " : ");
		for (AttributeInContext a : attributes ) {
			System.out.println(" . '" + a.getName() + "' isKeyElement : " + a.isKeyElement() );
		}
	}

	
	private EntityInContext buildFoo1EntityInContext() { 
		System.out.println("\nBuilding a new 'Foo1' entity ...");
		EnvInContext env = new EnvInContext();
		Entity entity = buildFoo1Entity();
		EntityInContext entityInContext = new EntityInContext(entity, "org.bean", null, env);
		
		printAttributes( "getAttributes()", entityInContext.getAttributes() );
		printAttributes( "getKeyAttributes()", entityInContext.getKeyAttributes() );
		printAttributes( "getNonKeyAttributes()", entityInContext.getNonKeyAttributes() );

		return entityInContext ;
	}
	private Entity buildFoo1Entity() { 
		FakeEntity entity = new FakeEntity("Foo1", "FOO1");
		entity.storeAttribute(buildFooIdAttribute());
		return entity ;
	}
	private FakeAttribute buildFooIdAttribute() { 
		FakeAttribute a = new FakeAttribute("id", NeutralType.INTEGER, true);
		a.setDatabaseName("ID");
		a.setNotNull(true);
		return a ;
	}
}
