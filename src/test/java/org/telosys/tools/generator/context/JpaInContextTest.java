package org.telosys.tools.generator.context;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.telosys.tools.generator.context.exceptions.GeneratorSqlException;
import org.telosys.tools.generic.model.Attribute;

import junit.env.telosys.tools.generator.fakemodel.FakeAttribute;
import junit.env.telosys.tools.generator.fakemodel.FakeForeignKey;
import junit.env.telosys.tools.generator.fakemodel.FakeForeignKeyColumn;

public class JpaInContextTest {
	
	private String[] fieldAnnotations(AttributeInContext attribute) {
		JpaInContext jpa = new JpaInContext() ;
		String annotations = jpa.fieldAnnotations(0, attribute);
		print(annotations);
		return annotations.split("\n");
	}
	
	@Test // (expected = GeneratorSqlException.class)
	public void testColumnNotNullUnique() {
		FakeAttribute attrib = buildFakeAttribute("code", "string"); 
		attrib.setNotNull(true);
		attrib.setUnique(true);
		String[] a = fieldAnnotations(buildAttribute(attrib));
		assertEquals(1, a.length);
		assertEquals("@Column(name=\"code\", nullable=false, unique=true)", a[0]);
	}

	@Test 
	public void testColumnNotNull() {
		FakeAttribute attrib = buildFakeAttribute("firstName", "string"); 
		attrib.setNotNull(true);
		String[] a = fieldAnnotations(buildAttribute(attrib));
		assertEquals(1, a.length);
		assertEquals("@Column(name=\"first_name\", nullable=false)", a[0]);
	}

	@Test 
	public void testColumnDatabaseName() {
		FakeAttribute attrib = buildFakeAttribute("firstName", "string"); 
		attrib.setDatabaseName("FIRST_NAME");
		String[] a = fieldAnnotations(buildAttribute(attrib));
		assertEquals(1, a.length);
		assertEquals("@Column(name=\"FIRST_NAME\")", a[0]);
	}

	@Test 
	public void testColumnId() {
		FakeAttribute attrib = buildFakeAttributeId("id", "int"); 
		String[] a = fieldAnnotations(buildAttribute(attrib));
		assertEquals(2, a.length);
		assertEquals("@Id", a[0]);
		assertEquals("@Column(name=\"id\")", a[1]);
	}

	@Test 
	public void testColumnIdAutoIncremented() {
		FakeAttribute attrib = buildFakeAttributeId("id", "int"); 
		
		// @AutoIncremented
		attrib.setAutoIncremented(true);
		AttributeInContext attribInCtx = buildAttribute(attrib);
		
		assertTrue(attribInCtx.isAutoIncremented());
		assertTrue(attribInCtx.isGeneratedValue());
		String[] a = fieldAnnotations(attribInCtx);
		assertEquals(3, a.length);
		assertEquals("@Id", a[0]);
		assertEquals("@GeneratedValue(strategy=GenerationType.IDENTITY)", a[1]);
		assertEquals("@Column(name=\"id\")", a[2]);
	}

	@Test 
	public void testColumnIdGenValAUTO() {
		FakeAttribute attrib = buildFakeAttributeId("id", "int"); 
		
		// @GeneratedValue(AUTO)
		attrib.setGeneratedValue(true);
		attrib.setGeneratedValueStrategy("AUTO");
		AttributeInContext attribInCtx = buildAttribute(attrib);
		
		//assertTrue(attribInCtx.isAutoIncremented());
		assertTrue(attribInCtx.isGeneratedValue());
		String[] a = fieldAnnotations(attribInCtx);
		assertEquals(3, a.length);
		assertEquals("@Id", a[0]);
		assertEquals("@GeneratedValue(strategy=GenerationType.AUTO)", a[1]);
		assertEquals("@Column(name=\"id\")", a[2]);
	}
	
	@Test 
	public void testColumnIdGenValIDENTITY() {
		FakeAttribute attrib = buildFakeAttributeId("id", "int"); 
		
		// @GeneratedValue(AUTO)
		attrib.setGeneratedValue(true);
		attrib.setGeneratedValueStrategy("IDENTITY");
		AttributeInContext attribInCtx = buildAttribute(attrib);
		
		//assertTrue(attribInCtx.isAutoIncremented());
		assertTrue(attribInCtx.isGeneratedValue());
		String[] a = fieldAnnotations(attribInCtx);
		assertEquals(3, a.length);
		assertEquals("@Id", a[0]);
		assertEquals("@GeneratedValue(strategy=GenerationType.IDENTITY)", a[1]);
		assertEquals("@Column(name=\"id\")", a[2]);
	}
	
	@Test 
	public void testColumnIdGenValSEQUENCE() {
		FakeAttribute attrib = buildFakeAttributeId("id", "int"); 
		
		// @GeneratedValue(SEQUENCE, MySeqGenName, MYSEQ ) 
		// @GeneratedValue(SEQUENCE, MySeqGenName, MYSEQ, allocationSize) 
		attrib.setGeneratedValue(true);
		attrib.setGeneratedValueStrategy("SEQUENCE");
		attrib.setHasSequenceGenerator(true);
		attrib.setSequenceGeneratorName("MySeqGenName");
		attrib.setSequenceGeneratorSequenceName("MYSEQ");
		AttributeInContext attribInCtx = buildAttribute(attrib);
		// check result
		assertTrue(attribInCtx.isGeneratedValue());
		String[] a = fieldAnnotations(attribInCtx);
		assertEquals(4, a.length);
		assertEquals("@Id", a[0]);
		assertEquals("@GeneratedValue(strategy=GenerationType.SEQUENCE)", a[1]);
		assertEquals("@SequenceGenerator(name=\"MySeqGenName\", sequenceName=\"MYSEQ\")", a[2]);
		assertEquals("@Column(name=\"id\")", a[3]);
	}
		
	@Test 
	public void testColumnIdGenValSEQUENCEwithGeneratorName() {
		FakeAttribute attrib = buildFakeAttributeId("id", "int"); 
		// @GeneratedValue(SEQUENCE)
		attrib.setGeneratedValue(true);
		attrib.setGeneratedValueStrategy("SEQUENCE");
		attrib.setGeneratedValueGenerator("MyGenerator");
		AttributeInContext attribInCtx = buildAttribute(attrib);
		// check result
		assertTrue(attribInCtx.isGeneratedValue());
		String[] a = fieldAnnotations(attribInCtx);
		assertEquals(3, a.length);
		assertEquals("@Id", a[0]);
		assertEquals("@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=\"MyGenerator\")", a[1]);
		assertEquals("@Column(name=\"id\")", a[2]);
	}
	
	//------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------------
	private void print(String s) {
		System.out.println(s);
	}

//	private EntityInContext buildEntity(String entityName, String tableName) {
//		
//		FakeModel fakeModel = new FakeModel("FakeModel");
//		Entity fakeEntity = new FakeEntity(entityName, tableName);
//		fakeModel.addEntity(fakeEntity);
//
//		TelosysToolsCfgManager cfgManager = new TelosysToolsCfgManager("projectAbsolutePath");
//		TelosysToolsCfg telosysToolsCfg = cfgManager.createDefaultTelosysToolsCfg();
//		EnvInContext envInContext = new EnvInContext() ; 
//		
//		ModelInContext modelInContext = new ModelInContext(fakeModel, telosysToolsCfg, envInContext);
//		return new EntityInContext(fakeEntity, "org.foo.pkg", modelInContext, envInContext);
//	}
	
	private FakeAttribute buildFakeAttribute(String attribName, String neutralType) {
		return new FakeAttribute(attribName, neutralType, false);
	}
	private FakeAttribute buildFakeAttributeId(String attribName, String neutralType) {
		return new FakeAttribute(attribName, neutralType, true);
	}
	private AttributeInContext buildAttribute(FakeAttribute attribute) {
		return new AttributeInContext(null, attribute, null, new EnvInContext() );
	}
	
	private AttributeInContext buildAttribute(String attribName, String neutralType, String dbName) {
		FakeAttribute fakeAttribute = new FakeAttribute(attribName, neutralType, false);
		if ( dbName != null ) {
			fakeAttribute.setDatabaseName(dbName);
		}
		else {
			fakeAttribute.setDatabaseName(""); // no database name
			// as in DSL model default value
		}
		return new AttributeInContext(null, fakeAttribute, null, new EnvInContext() );
	}
	
	private ForeignKeyInContext buidForeignKey1() {
		FakeForeignKey fakeFK = new FakeForeignKey("FkDriverCar", "GoodDriver", "SpecialCar");
		fakeFK.addColumn(new FakeForeignKeyColumn("carId", "id", 1));
		return new ForeignKeyInContext(fakeFK, new EnvInContext());
	}

	private ForeignKeyInContext buidForeignKey2() {
		FakeForeignKey fakeFK = new FakeForeignKey("FK_DRIVER_CAR", "GOOD_DRIVER", "SPECIAL_CAR");
		fakeFK.addColumn(new FakeForeignKeyColumn("carId1", "myId1", 1));
		fakeFK.addColumn(new FakeForeignKeyColumn("carId2", "myId2", 2));
		return new ForeignKeyInContext(fakeFK, new EnvInContext());
	}
}
