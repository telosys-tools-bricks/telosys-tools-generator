package org.telosys.tools.generator.context;

import org.junit.Test;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.cfg.TelosysToolsCfgManager;
import org.telosys.tools.generic.model.enums.Cardinality;
import org.telosys.tools.generic.model.enums.FetchType;
import org.telosys.tools.generic.model.enums.GeneratedValueStrategy;
import org.telosys.tools.generic.model.enums.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import junit.env.telosys.tools.generator.fakemodel.FakeAttribute;
import junit.env.telosys.tools.generator.fakemodel.FakeEntity;
import junit.env.telosys.tools.generator.fakemodel.FakeLink;
import junit.env.telosys.tools.generator.fakemodel.FakeModel;

public class JpaInContextTest {
	
	private String[] fieldAnnotations(AttributeInContext attribute, boolean withColumnDefinition) {
		JpaInContext jpa = new JpaInContext() ;
		jpa.setGenColumnDefinition(withColumnDefinition);
		String annotations = jpa.fieldAnnotations(0, attribute);
		print(annotations);
		return annotations.split("\n");
	}
	private String[] fieldAnnotations(AttributeInContext attribute) {
		return fieldAnnotations(attribute, false);
	}
	
	@Test // (expected = GeneratorSqlException.class)
	public void testColumnNotNullUnique() {
		FakeAttribute attrib = buildFakeAttribute("code", "string"); 
		attrib.setNotNull(true);
		attrib.setUnique(true);
		String[] a = fieldAnnotations(buildAttribute(attrib));
		// check result
		assertEquals(1, a.length);
		assertEquals("@Column(name=\"code\", nullable=false, unique=true)", a[0]);
	}

	@Test 
	public void testColumnNotNull() {
		FakeAttribute attrib = buildFakeAttribute("firstName", "string"); 
		attrib.setNotNull(true);
		String[] a = fieldAnnotations(buildAttribute(attrib));
		// check result
		assertEquals(1, a.length);
		assertEquals("@Column(name=\"first_name\", nullable=false)", a[0]);
	}

	@Test 
	public void testColumnDatabaseName() {
		FakeAttribute attrib = buildFakeAttribute("firstName", "string"); 
		attrib.setDatabaseName("FIRST_NAME");
		String[] a = fieldAnnotations(buildAttribute(attrib));
		// check result
		assertEquals(1, a.length);
		assertEquals("@Column(name=\"FIRST_NAME\")", a[0]);
	}

	@Test 
	public void testColumnId() {
		FakeAttribute attrib = buildFakeAttributeId("id", "int"); 
		String[] a = fieldAnnotations(buildAttribute(attrib));
		// check result
		assertEquals(2, a.length);
		assertEquals("@Id", a[0]);
		assertEquals("@Column(name=\"id\")", a[1]);
	}

	@Test 
	public void testColumnIdWithColDef() {
		FakeAttribute attrib = buildFakeAttributeId("id", "int"); 
		String[] a = fieldAnnotations(buildAttribute(attrib), true);
		// check result
		assertEquals(2, a.length);
		assertEquals("@Id", a[0]);
		assertEquals("@Column(name=\"id\", columnDefinition=\"INT\")", a[1]);
	}

	@Test 
	public void testColumnCodeWithColDef() {
		FakeAttribute attrib = buildFakeAttributeId("code", "string"); 
		attrib.setSize("20");
		String[] a = fieldAnnotations(buildAttribute(attrib), true);
		// check result
		assertEquals(2, a.length);
		assertEquals("@Id", a[0]);
		assertEquals("@Column(name=\"code\", length=20, columnDefinition=\"VARCHAR(20)\")", a[1]);
	}

	@Test 
	public void testColumnCodeWithColDefNotNull() {
		FakeAttribute attrib = buildFakeAttributeId("code", "string"); 
		attrib.setNotNull(true);
		attrib.setSize("20");
		String[] a = fieldAnnotations(buildAttribute(attrib), true);
		// check result
		assertEquals(2, a.length);
		assertEquals("@Id", a[0]);
		assertEquals("@Column(name=\"code\", nullable=false, length=20, columnDefinition=\"VARCHAR(20) NOT NULL\")", a[1]);
	}

	@Test 
	public void testColumnCodeWithColDefUnique() {
		FakeAttribute attrib = buildFakeAttributeId("code", "string"); 
		attrib.setUnique(true);
		attrib.setSize("20");
		String[] a = fieldAnnotations(buildAttribute(attrib), true);
		// check result
		assertEquals(2, a.length);
		assertEquals("@Id", a[0]);
		assertEquals("@Column(name=\"code\", length=20, unique=true, columnDefinition=\"VARCHAR(20) UNIQUE\")", a[1]);
	}

	@Test 
	public void testColumnIdUnique() {
		FakeAttribute attrib = buildFakeAttributeId("id", "int"); 
		attrib.setUnique(true);
		AttributeInContext attribInCtx = buildAttribute(attrib);
		// check result
		assertTrue(attribInCtx.isUnique());
		assertFalse(attribInCtx.isGeneratedValue());
		String[] a = fieldAnnotations(attribInCtx);
		assertEquals(2, a.length);
		assertEquals("@Id", a[0]);
		assertEquals("@Column(name=\"id\", unique=true)", a[1]);
	}

	@Test 
	public void testColumnIdAutoIncremented() {
		FakeAttribute attrib = buildFakeAttributeId("id", "int"); 
		// @AutoIncremented
		attrib.setAutoIncremented(true);
		AttributeInContext attribInCtx = buildAttribute(attrib);
		// check result
		assertTrue(attribInCtx.isAutoIncremented());
		assertFalse(attribInCtx.isGeneratedValue());
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
		attrib.setGeneratedValueStrategy(GeneratedValueStrategy.AUTO);
		AttributeInContext attribInCtx = buildAttribute(attrib);
		// check result
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
		// @GeneratedValue(IDENTITY)
		attrib.setGeneratedValueStrategy(GeneratedValueStrategy.IDENTITY);
		AttributeInContext attribInCtx = buildAttribute(attrib);
		// check result
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
		// @GeneratedValue(SEQUENCE)  
		attrib.setGeneratedValueStrategy(GeneratedValueStrategy.SEQUENCE);
		AttributeInContext attribInCtx = buildAttribute(attrib);
		// check result
		assertTrue(attribInCtx.isGeneratedValue());
		String[] a = fieldAnnotations(attribInCtx);
		assertEquals(3, a.length);
		assertEquals("@Id", a[0]);
		assertEquals("@GeneratedValue(strategy=GenerationType.SEQUENCE)", a[1]);
		assertEquals("@Column(name=\"id\")", a[2]);
	}
		
	@Test 
	public void testColumnIdGenValSEQUENCEwithGeneratorName() {
		FakeAttribute attrib = buildFakeAttributeId("id", "int"); 
		// @GeneratedValue(SEQUENCE, MyGeneratorName ) 
		attrib.setGeneratedValueStrategy(GeneratedValueStrategy.SEQUENCE);
		attrib.setGeneratedValueGeneratorName("MyGenerator");
		AttributeInContext attribInCtx = buildAttribute(attrib);
		// check result
		assertTrue(attribInCtx.isGeneratedValue());
		String[] a = fieldAnnotations(attribInCtx);
		assertEquals(4, a.length);
		assertEquals("@Id", a[0]);
		assertEquals("@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=\"MyGenerator\")", a[1]);
		assertEquals("@SequenceGenerator(name=\"MyGenerator\")", a[2]); // only 'name' is required in JPA spec
		assertEquals("@Column(name=\"id\")", a[3]);
	}

	@Test 
	public void testColumnIdGenValSEQUENCEwithGeneratorNameAndSequenceName() {
		FakeAttribute attrib = buildFakeAttributeId("id", "int"); 
		// @GeneratedValue(SEQUENCE, MyGeneratorName, MYSEQ ) 
		attrib.setGeneratedValueStrategy(GeneratedValueStrategy.SEQUENCE);
		attrib.setGeneratedValueGeneratorName("MyGenerator");
		attrib.setGeneratedValueSequenceName("MYSEQ");
		AttributeInContext attribInCtx = buildAttribute(attrib);
		// check result
		assertTrue(attribInCtx.isGeneratedValue());
		String[] a = fieldAnnotations(attribInCtx);
		assertEquals(4, a.length);
		assertEquals("@Id", a[0]);
		assertEquals("@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=\"MyGenerator\")", a[1]);
		assertEquals("@SequenceGenerator(name=\"MyGenerator\", sequenceName=\"MYSEQ\")", a[2]);
		assertEquals("@Column(name=\"id\")", a[3]);
	}
	
	@Test 
	public void testColumnIdGenValSEQUENCEwithGeneratorNameAndSequenceNameAndAllocationSize() {
		FakeAttribute attrib = buildFakeAttributeId("id", "int"); 
		// @GeneratedValue(SEQUENCE, MySeqGenName, MYSEQ, 2) 
		attrib.setGeneratedValueStrategy(GeneratedValueStrategy.SEQUENCE);
		attrib.setGeneratedValueGeneratorName("MyGenerator");
		attrib.setGeneratedValueSequenceName("MYSEQ");
		attrib.setGeneratedValueAllocationSize(2);
		AttributeInContext attribInCtx = buildAttribute(attrib);
		// check result
		assertTrue(attribInCtx.isGeneratedValue());
		String[] a = fieldAnnotations(attribInCtx);
		assertEquals(4, a.length);
		assertEquals("@Id", a[0]);
		assertEquals("@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=\"MyGenerator\")", a[1]);
		assertEquals("@SequenceGenerator(name=\"MyGenerator\", sequenceName=\"MYSEQ\", allocationSize=2)", a[2]);
		assertEquals("@Column(name=\"id\")", a[3]);
	}
	
	//---------------------------------------------------------------------------------------------
	// LINK CARDINALITY 
	//---------------------------------------------------------------------------------------------
	@Test 
	public void testLinkCardinalityOneToMany() {
		FakeLink fakeLink = new FakeLink("towns", "Town", Cardinality.ONE_TO_MANY);
		LinkInContext link = buildLink("Country", fakeLink);
		assertFalse(link.isOwningSide());
		JpaInContext jpa = new JpaInContext();
		assertEquals("@OneToMany", jpa.linkCardinalityAnnotation(0, link) );
		jpa.setGenTargetEntity(true);
		assertEquals("@OneToMany(targetEntity=Town.class)", jpa.linkCardinalityAnnotation(0, link) );
	}

	@Test 
	public void testLinkCardinalityOneToManyWithOptions() {
		FakeLink fakeLink = new FakeLink("towns", "Town", Cardinality.ONE_TO_MANY);
		fakeLink.setFetchType(FetchType.LAZY);
		fakeLink.setMappedBy("country");
		fakeLink.setOrphanRemoval(true);
		fakeLink.setOptional(Optional.TRUE); // ignored for OneToMany
		LinkInContext link = buildLink("Country", fakeLink);
		assertFalse(link.isOwningSide());
		JpaInContext jpa = new JpaInContext();
		assertEquals("@OneToMany(mappedBy=\"country\", fetch=FetchType.LAZY, orphanRemoval=true)", 
				jpa.linkCardinalityAnnotation(0, link) );
	}

	@Test 
	public void testLinkCardinalityManyToOne() {
		FakeLink fakeLink = new FakeLink("country", "Country", Cardinality.MANY_TO_ONE);
		LinkInContext link = buildLink("Town", fakeLink);
		assertTrue(link.isOwningSide());
		JpaInContext jpa = new JpaInContext();
		assertEquals("@ManyToOne", jpa.linkCardinalityAnnotation(0, link) );
		jpa.setGenTargetEntity(true);
		assertEquals("@ManyToOne(targetEntity=Country.class)", jpa.linkCardinalityAnnotation(0, link) );
	}

	@Test 
	public void testLinkCardinalityManyToOneWithOptions() {
		FakeLink fakeLink = new FakeLink("country", "Country", Cardinality.MANY_TO_ONE);
		fakeLink.setFetchType(FetchType.LAZY);
		fakeLink.setMappedBy("town"); // error : not used for ManyToOne
		fakeLink.setOrphanRemoval(true); // ignored for ManyToOne
		fakeLink.setOptional(Optional.TRUE);
		LinkInContext link = buildLink("Town", fakeLink);
		assertTrue(link.isOwningSide());
		JpaInContext jpa = new JpaInContext();
		assertEquals("@ManyToOne(fetch=FetchType.LAZY, optional=true)", jpa.linkCardinalityAnnotation(0, link) );
	}

	private LinkInContext buildLink(String entityName, FakeLink fakeLink ) {
		FakeEntity fakeOriginEntity = new FakeEntity(entityName, "");
		FakeEntity fakeTargetEntity = new FakeEntity(fakeLink.getReferencedEntityName(), "");
		FakeModel fakeModel = new FakeModel("mymodel");
		fakeModel.addEntity(fakeOriginEntity);
		fakeModel.addEntity(fakeTargetEntity);

		TelosysToolsCfgManager cfgManager = new TelosysToolsCfgManager("/tmp/foo");
		TelosysToolsCfg cfg = cfgManager.createDefaultTelosysToolsCfg();
		EnvInContext env = new EnvInContext();
		ModelInContext model = new ModelInContext(fakeModel, cfg, env);
		EntityInContext entity = new EntityInContext(fakeOriginEntity, "org.demo", model, env);
		return new LinkInContext(entity, fakeLink, model, env );
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
	
	/****
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
	****/
}
