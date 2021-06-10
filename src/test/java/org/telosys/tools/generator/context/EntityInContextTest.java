package org.telosys.tools.generator.context;

import java.util.List;

import org.junit.Test;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generic.model.Attribute;
import org.telosys.tools.generic.model.Entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import junit.env.telosys.tools.generator.fakemodel.entities.Car;
import junit.env.telosys.tools.generator.fakemodel.entities.EmpTeam;
import junit.env.telosys.tools.generator.fakemodel.entities.Employee;
import junit.env.telosys.tools.generator.fakemodel.entities.Foo1;


public class EntityInContextTest {

	private void checkEntityInContext(EntityInContext entityInContext, Entity entity) throws GeneratorException {

		assertEquals(entity.getClassName(), entityInContext.getName());
		// cannot check getFullName() : in entity 'null' / in EntityInContext "org.bean.Xxxx"

		// If null converted to ""
		assertNotNull(entityInContext.getDatabaseCatalog()); 
		assertNotNull(entityInContext.getDatabaseComment()); 
		assertNotNull(entityInContext.getDatabaseSchema()); 
		assertNotNull(entityInContext.getDatabaseTable()); 
		assertNotNull(entityInContext.getDatabaseType()); 

		//assertEquals(entity.getDatabaseCatalog(),  entityInContext.getDatabaseCatalog()); // null -> ""
		//assertEquals(entity.getDatabaseComment(),  entityInContext.getDatabaseComment()); // null -> ""
		//assertEquals(entity.getDatabaseSchema(),  entityInContext.getDatabaseSchema());  // null -> ""
		assertEquals(entity.getDatabaseTable(),  entityInContext.getDatabaseTable());
		//assertEquals(entity.getDatabaseType(),  entityInContext.getDatabaseType()); // null -> ""
		
		// Check collections size
		assertEquals(entity.getAttributes().size(), entityInContext.getAttributes().size() );
		assertEquals(entity.getAttributes().size(), entityInContext.getAttributesCount() );
		assertEquals(entity.getLinks().size(), entityInContext.getLinks().size() );
		assertEquals(entity.getDatabaseForeignKeys().size(), entityInContext.getDatabaseForeignKeys().size() );
		assertEquals(entity.getDatabaseForeignKeys().size(), entityInContext.getDatabaseForeignKeysCount() );
		
		for ( Attribute a : entity.getAttributes() ) {
			String dbName = a.getDatabaseName();
			if ( ! StrUtil.nullOrVoid(dbName) ) {
				AttributeInContext ac = entityInContext.getAttributeByColumnName(dbName);
				assertEquals(a.getName(), ac.getName());
				assertEquals(a.getNeutralType(), ac.getNeutralType());
				assertEquals(a.isKeyElement(), ac.isKeyElement());
			}
		}
		assertTrue( entityInContext.getAttributeByColumnName("ID").isKeyElement() );

	}

	@Test
	public void carEntityTest() throws GeneratorException {
		Car car = new Car();
		EntityInContext entityInContext = buildEntityInContext(car);
		checkEntityInContext(entityInContext, car);
		
		assertEquals("Car", entityInContext.getName() );
		assertEquals("org.bean", entityInContext.getPackage() );
		assertEquals("org.bean.Car", entityInContext.getFullName() );
		
		// Primary Key
		assertTrue(entityInContext.hasPrimaryKey() );
		assertFalse(entityInContext.hasCompositePrimaryKey() );
		assertFalse(entityInContext.hasAutoIncrementedKey());
		assertNull(entityInContext.getAutoincrementedKeyAttribute() );
		
		// Database info 
		assertEquals("CAR", entityInContext.getDatabaseTable() );
		assertEquals("", entityInContext.getDatabaseCatalog());
		assertEquals("", entityInContext.getDatabaseComment());
		assertEquals("", entityInContext.getDatabaseSchema());
		assertEquals("", entityInContext.getDatabaseType()); // "TABLE" or "VIEW"
		assertFalse(entityInContext.isTableType());
		assertFalse(entityInContext.isViewType());
		
		// Key attributes
		assertEquals(1, entityInContext.getKeyAttributesCount());
		assertEquals(1, entityInContext.getKeyAttributes().size());

		// Non Key attributes
		assertEquals(2, entityInContext.getNonKeyAttributesCount());
		assertEquals(2, entityInContext.getNonKeyAttributes().size());
				
		assertEquals(0, entityInContext.getLinks().size());
		assertEquals(0, entityInContext.referencedEntityTypes().size());

		assertFalse(entityInContext.isJoinEntity());
		
		// Attributes
		assertTrue( entityInContext.getAttributeByColumnName("ID").isKeyElement() );
		assertFalse( entityInContext.getAttributeByColumnName("NAME").isKeyElement() );
		assertFalse( entityInContext.getAttributeByColumnName("DESC").isKeyElement() );
	}
	
	@Test
	public void joinEntityTest() throws GeneratorException {
		EntityInContext entityInContext ;
		
		// Not join entity
		entityInContext = buildEntityInContext(new Employee());
		assertFalse(entityInContext.isJoinEntity());

		// Join entity
		entityInContext = buildEntityInContext(new EmpTeam());
		assertEquals(2, entityInContext.getDatabaseForeignKeysCount()); // 2 FK
		assertEquals(2, entityInContext.getKeyAttributesCount()); // 2 attributes in PK 
		assertEquals(0, entityInContext.getNonKeyAttributesCount()); // no attribute out of PK
		assertTrue(entityInContext.getAttributeByColumnName("TEAM_CODE").isFK());
		assertTrue(entityInContext.getAttributeByColumnName("EMP_ID").isFK());
		assertTrue(entityInContext.isJoinEntity());
	}
	
	@Test
	public void keyAttributesNamesAsString() {
		EntityInContext entityInContext = buildEntityInContext(new Car());
		assertEquals("id", entityInContext.keyAttributesNamesAsString(","));
		assertEquals("id", entityInContext.keyAttributesNamesAsString(", "));
		assertEquals("$id", entityInContext.keyAttributesNamesAsString(", ", "$", ""));
		assertEquals("$id@", entityInContext.keyAttributesNamesAsString(", ", "$", "@"));
		assertEquals("{{id}}", entityInContext.keyAttributesNamesAsString(", ", "{{", "}}"));
	}

	@Test
	public void attributesNamesAsString() {
		EntityInContext e = buildEntityInContext(new Car());
		assertEquals("id,name,desc", e.attributesNamesAsString(","));
		assertEquals("id, name, desc", e.attributesNamesAsString(", "));
	}

	@Test
	public void nonKeyAttributesNamesAsString() {
		EntityInContext e = buildEntityInContext(new Car());
		assertEquals("name,desc", e.nonKeyAttributesNamesAsString(","));
		assertEquals("name, desc", e.nonKeyAttributesNamesAsString(", "));
		assertEquals("$name, $desc", e.nonKeyAttributesNamesAsString(", ", "$", ""));
		assertEquals("$name@, $desc@", e.nonKeyAttributesNamesAsString(", ", "$", "@"));
		assertEquals("{{name}}, {{desc}}", e.nonKeyAttributesNamesAsString(", ", "{{", "}}"));
	}

	//---------------------------------------------------------------------------
	// Test with 'Foo1' entity : only 1 attribute (key element)
	//---------------------------------------------------------------------------
	@Test
	public void keyAttributesNamesAsStringFoo1() {
		EntityInContext entityInContext = buildEntityInContext(new Foo1());
		assertEquals("id", entityInContext.keyAttributesNamesAsString(","));
		assertEquals("id", entityInContext.keyAttributesNamesAsString(", "));
		assertEquals("$id", entityInContext.keyAttributesNamesAsString(", ", "$", ""));
		assertEquals("$id@", entityInContext.keyAttributesNamesAsString(", ", "$", "@"));
		assertEquals("{{id}}", entityInContext.keyAttributesNamesAsString(", ", "{{", "}}"));
	}

	@Test
	public void attributesNamesAsStringFoo1() {
		EntityInContext entityInContext = buildEntityInContext(new Foo1());
		assertEquals("id", entityInContext.attributesNamesAsString(","));
		assertEquals("id", entityInContext.attributesNamesAsString(", "));
		assertEquals("$id", entityInContext.attributesNamesAsString(", ", "$", ""));
		assertEquals("$id@", entityInContext.attributesNamesAsString(", ", "$", "@"));
		assertEquals("{{id}}", entityInContext.attributesNamesAsString(", ", "{{", "}}"));
	}

	@Test
	public void nonKeyAttributesNamesAsStringFoo1() {
		EntityInContext entityInContext = buildEntityInContext(new Foo1());
		assertEquals("", entityInContext.nonKeyAttributesNamesAsString(","));
		assertEquals("", entityInContext.nonKeyAttributesNamesAsString(", "));
		assertEquals("", entityInContext.nonKeyAttributesNamesAsString(", ", "$", ""));
		assertEquals("", entityInContext.nonKeyAttributesNamesAsString(", ", "$", "@"));
		assertEquals("", entityInContext.nonKeyAttributesNamesAsString(", ", "{{", "}}"));
	}

	//---------------------------------------------------------------------------
	// Tooling
	//---------------------------------------------------------------------------
	private EntityInContext buildEntityInContext(Entity entity) { 
		EnvInContext env = new EnvInContext();
		EntityInContext entityInContext = new EntityInContext(entity, "org.bean", null, env);
		
		printAttributes( "getAttributes()", entityInContext.getAttributes() );
		printAttributes( "getKeyAttributes()", entityInContext.getKeyAttributes() );
		printAttributes( "getNonKeyAttributes()", entityInContext.getNonKeyAttributes() );

		return entityInContext ;
	}

	private void printAttributes(String title, List<AttributeInContext> attributes) {
		System.out.println(title + " : ");
		for (AttributeInContext a : attributes ) {
			System.out.println(" . '" + a.getName() + "' isKeyElement : " + a.isKeyElement() );
		}
	}

}
