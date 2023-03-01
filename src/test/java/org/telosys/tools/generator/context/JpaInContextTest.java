package org.telosys.tools.generator.context;

import org.junit.Test;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.cfg.TelosysToolsCfgManager;
import org.telosys.tools.dsl.model.DslModel;
import org.telosys.tools.dsl.model.DslModelAttribute;
import org.telosys.tools.dsl.model.DslModelEntity;
import org.telosys.tools.dsl.model.DslModelLink;
import org.telosys.tools.generic.model.Attribute;
import org.telosys.tools.generic.model.enums.Cardinality;
import org.telosys.tools.generic.model.enums.FetchType;
import org.telosys.tools.generic.model.enums.GeneratedValueStrategy;
import org.telosys.tools.generic.model.enums.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JpaInContextTest {
	
	private String[] jpaFieldAnnotations(AttributeInContext attribute) {
		return jpaFieldAnnotations(attribute, false);
	}
	private String[] jpaFieldAnnotations(AttributeInContext attribute, boolean withColumnDefinition) {
		JpaInContext jpa = new JpaInContext() ;
		jpa.setGenColumnDefinition(withColumnDefinition);
		String annotations = jpa.fieldAnnotations(0, attribute);
		print(annotations);
		return annotations.split("\n");
	}
	
	@Test // (expected = GeneratorSqlException.class)
	public void testColumnNotNullUnique() {
		DslModelAttribute attribute = new DslModelAttribute("code", "string");
		attribute.setNotNull(true); // @NotNull
		attribute.setUnique(true); // @Unique
		AttributeInContext attribInCtx = buildAttributeInContext(buildEntityInContext("City"), attribute);
		String[] a = jpaFieldAnnotations(attribInCtx);
		// check result
		assertEquals(1, a.length);
		assertEquals("@Column(name=\"code\", nullable=false, unique=true)", a[0]);
	}

	@Test 
	public void testColumnNotNull() {
		DslModelAttribute attribute = new DslModelAttribute("firstName", "string");
		attribute.setNotNull(true); // @NotNull
		AttributeInContext attribInCtx = buildAttributeInContext(buildEntityInContext("City"), attribute);
		String[] a = jpaFieldAnnotations(attribInCtx);
		// check result
		assertEquals(1, a.length);
		assertEquals("@Column(name=\"first_name\", nullable=false)", a[0]);
	}

	@Test 
	public void testColumnDatabaseName() {
		DslModelAttribute attribute = new DslModelAttribute("firstName", "string");
		attribute.setDatabaseName("FIRST_NAME");
		AttributeInContext attribInCtx = buildAttributeInContext(buildEntityInContext("City", "CITY"), attribute);
		String[] a = jpaFieldAnnotations(attribInCtx);
		// check result
		assertEquals(1, a.length);
		assertEquals("@Column(name=\"FIRST_NAME\")", a[0]);
	}

	@Test 
	public void testColumnId() {
		DslModelAttribute attribute = new DslModelAttribute("id", "int");
		attribute.setKeyElement(true); // @Id
		AttributeInContext attribInCtx = buildAttributeInContext(buildEntityInContext("City", "CITY"), attribute);
		String[] a = jpaFieldAnnotations(attribInCtx);
		// check result
		assertEquals(2, a.length);
		assertEquals("@Id", a[0]);
		assertEquals("@Column(name=\"id\")", a[1]);
	}

	@Test 
	public void testColumnIdWithColDef() {
		DslModelAttribute attribute = new DslModelAttribute("id", "int");
		attribute.setKeyElement(true); // @Id
		attribute.setSize("20");
		AttributeInContext attribInCtx = buildAttributeInContext(buildEntityInContext("City", "CITY"), attribute);
		String[] a = jpaFieldAnnotations(attribInCtx, true);
		// check result
		assertEquals(2, a.length);
		assertEquals("@Id", a[0]);
		assertEquals("@Column(name=\"id\", columnDefinition=\"INT\")", a[1]); // "int" type => no length=20 
	}

	@Test 
	public void testColumnCodeWithColDef() {
		DslModelAttribute attribute = new DslModelAttribute("code", "string");
		attribute.setKeyElement(true); // @Id
		attribute.setSize("20");
		AttributeInContext attribInCtx = buildAttributeInContext(buildEntityInContext("City", "CITY"), attribute);
		String[] a = jpaFieldAnnotations(attribInCtx, true);
		// check result
		assertEquals(2, a.length);
		assertEquals("@Id", a[0]);
		assertEquals("@Column(name=\"code\", length=20, columnDefinition=\"VARCHAR(20)\")", a[1]);
	}

	@Test 
	public void testColumnCodeWithColDefNotNull() {
		DslModelAttribute attribute = new DslModelAttribute("code", "string");
		attribute.setKeyElement(true); // @Id
		attribute.setNotNull(true);
		attribute.setSize("20");
		AttributeInContext attribInCtx = buildAttributeInContext(buildEntityInContext("City", "CITY"), attribute);
		String[] a = jpaFieldAnnotations(attribInCtx, true);
		// check result
		assertEquals(2, a.length);
		assertEquals("@Id", a[0]);
		assertEquals("@Column(name=\"code\", nullable=false, length=20, columnDefinition=\"VARCHAR(20) NOT NULL\")", a[1]);
	}

	@Test 
	public void testColumnCodeWithColDefUnique() {
		DslModelAttribute attribute = new DslModelAttribute("code", "string");
		attribute.setKeyElement(true); // @Id
		attribute.setUnique(true);
		attribute.setSize("20");
		AttributeInContext attribInCtx = buildAttributeInContext(buildEntityInContext("City", "CITY"), attribute);
		// check result
		String[] a = jpaFieldAnnotations(attribInCtx, true);
		assertEquals(2, a.length);
		assertEquals("@Id", a[0]);
		assertEquals("@Column(name=\"code\", length=20, unique=true, columnDefinition=\"VARCHAR(20) UNIQUE\")", a[1]);
	}

	@Test 
	public void testColumnIdUnique() {
		DslModelAttribute attribute = new DslModelAttribute("id", "int");
		attribute.setKeyElement(true); // @Id
		attribute.setUnique(true); // @Unique
		AttributeInContext attribInCtx = buildAttributeInContext(buildEntityInContext("City", "CITY"), attribute);
		// check result
		assertTrue(attribInCtx.isUnique());
		assertFalse(attribInCtx.isGeneratedValue());
		String[] a = jpaFieldAnnotations(attribInCtx);
		assertEquals(2, a.length);
		assertEquals("@Id", a[0]);
		assertEquals("@Column(name=\"id\", unique=true)", a[1]);
	}

	//---------------------------------------------------------------------------------------------
	// AUTOINCREMENTED
	//---------------------------------------------------------------------------------------------
	@Test 
	public void testColumnAutoIncrementedWithoutId() { // NO @Id (NO PRIAMRY KEY) 
		DslModelAttribute attribute = new DslModelAttribute("foo", "long");
		//attribute.setAutoIncremented(true); // @AutoIncremented
		attribute.setGeneratedValueStrategy(GeneratedValueStrategy.IDENTITY); // auto-incremented
		AttributeInContext attribInCtx = buildAttributeInContext(buildEntityInContext("City"), attribute);
		// check result
		assertTrue(attribInCtx.isAutoIncremented());
		assertTrue(attribInCtx.isGeneratedValue());
		String[] a = jpaFieldAnnotations(attribInCtx);
		assertEquals(1, a.length);
		assertEquals("@Column(name=\"foo\")", a[0]);
	}

	@Test 
	public void testColumnIdAutoIncremented() {
		DslModelAttribute attribute = new DslModelAttribute("id", "int");
		attribute.setKeyElement(true); // @Id
		//attribute.setAutoIncremented(true); // @AutoIncremented
		attribute.setGeneratedValueStrategy(GeneratedValueStrategy.IDENTITY); // auto-incremented
		AttributeInContext attribInCtx = buildAttributeInContext(buildEntityInContext("City", "CITY"), attribute);
		// check result
		assertTrue(attribInCtx.isAutoIncremented());
		assertTrue(attribInCtx.isGeneratedValue());
		String[] a = jpaFieldAnnotations(attribInCtx);
		assertEquals(3, a.length);
		assertEquals("@Id", a[0]);
		assertEquals("@GeneratedValue(strategy=GenerationType.IDENTITY)", a[1]);
		assertEquals("@Column(name=\"id\")", a[2]);
	}

	//---------------------------------------------------------------------------------------------
	// ID with GENERATED VALUE / AUTO
	//---------------------------------------------------------------------------------------------
	@Test 
	public void testColumnIdGenValAUTO() {
		DslModelAttribute attribute = new DslModelAttribute("id", "int");
		attribute.setKeyElement(true); // @Id
		attribute.setGeneratedValueStrategy(GeneratedValueStrategy.AUTO);		
		AttributeInContext attribInCtx = buildAttributeInContext(buildEntityInContext("City", "CITY"), attribute);
		// check result
		assertTrue(attribInCtx.isGeneratedValue());
		String[] a = jpaFieldAnnotations(attribInCtx);
		assertEquals(3, a.length);
		assertEquals("@Id", a[0]);
		assertEquals("@GeneratedValue(strategy=GenerationType.AUTO)", a[1]);
		assertEquals("@Column(name=\"id\")", a[2]);
	}
	
	//---------------------------------------------------------------------------------------------
	// ID with GENERATED VALUE / IDENTITY
	//---------------------------------------------------------------------------------------------
	@Test 
	public void testColumnIdGenValIDENTITY() {
		DslModelAttribute attribute = new DslModelAttribute("id", "int");
		attribute.setKeyElement(true); // @Id
		attribute.setGeneratedValueStrategy(GeneratedValueStrategy.IDENTITY);		
		AttributeInContext attribInCtx = buildAttributeInContext(buildEntityInContext("City", "CITY"), attribute);

		// check result
		assertTrue(attribInCtx.isGeneratedValue());
		String[] a = jpaFieldAnnotations(attribInCtx);
		assertEquals(3, a.length);
		assertEquals("@Id", a[0]);
		assertEquals("@GeneratedValue(strategy=GenerationType.IDENTITY)", a[1]);
		assertEquals("@Column(name=\"id\")", a[2]);
	}
	
	//---------------------------------------------------------------------------------------------
	// ID with GENERATED VALUE / SEQUENCE
	//---------------------------------------------------------------------------------------------
	@Test 
	public void testColumnIdGenValSEQUENCE() {
		DslModelAttribute attribute = new DslModelAttribute("id", "int");
		attribute.setKeyElement(true); // @Id
		attribute.setGeneratedValueStrategy(GeneratedValueStrategy.SEQUENCE);		
		AttributeInContext attribInCtx = buildAttributeInContext(buildEntityInContext("City", "CITY"), attribute);
		
		// check result
		assertTrue(attribInCtx.isGeneratedValue());
		String[] a = jpaFieldAnnotations(attribInCtx);
		assertEquals(4, a.length);
		int i=0;
		assertEquals("@Id", a[i++]);
		assertEquals("@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=\"City_id_gen\")", a[i++]);
		assertEquals("@SequenceGenerator(name=\"City_id_gen\")", a[i++] ); // NO 'sequenceName' (only 'name' is required in JPA spec)
		assertEquals("@Column(name=\"id\")", a[i++]);
	}
		
	@Test 
	public void testColumnIdGenValSequenceWithName() {
		DslModelAttribute attribute = new DslModelAttribute("id", "int");
		attribute.setKeyElement(true); // @Id
		attribute.setGeneratedValueStrategy(GeneratedValueStrategy.SEQUENCE);
		attribute.setGeneratedValueSequenceName("MY_SEQUENCE");
		AttributeInContext attribInCtx = buildAttributeInContext(buildEntityInContext("Book", "BOOK"), attribute);
		
		// check result
		assertTrue(attribInCtx.isGeneratedValue());
		String[] a = jpaFieldAnnotations(attribInCtx);
		assertEquals(4, a.length);
		int i=0;
		assertEquals("@Id", a[i++]);
		assertEquals("@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=\"Book_id_gen\")", a[i++]);
		assertEquals("@SequenceGenerator(name=\"Book_id_gen\", sequenceName=\"MY_SEQUENCE\")", a[i++]);
		assertEquals("@Column(name=\"id\")", a[i++]);
	}

	@Test 
	public void testColumnIdGenValSequenceWithNameAndAllocationSize() {
		DslModelAttribute attribute = new DslModelAttribute("id", "int");
		attribute.setKeyElement(true); // @Id
		attribute.setGeneratedValueStrategy(GeneratedValueStrategy.SEQUENCE);
		attribute.setGeneratedValueSequenceName("MY_SEQUENCE");
		attribute.setGeneratedValueAllocationSize(20);
		AttributeInContext attribInCtx = buildAttributeInContext(buildEntityInContext("Book", "BOOK"), attribute);
		
		// check result
		assertTrue(attribInCtx.isGeneratedValue());
		String[] a = jpaFieldAnnotations(attribInCtx);
		assertEquals(4, a.length);
		int i=0;
		assertEquals("@Id", a[i++]);
		assertEquals("@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=\"Book_id_gen\")", a[i++]);
		assertEquals("@SequenceGenerator(name=\"Book_id_gen\", sequenceName=\"MY_SEQUENCE\", allocationSize=20)", a[i++]);
		assertEquals("@Column(name=\"id\")", a[i++]);
	}

	@Test 
	public void testColumnIdGenValSequenceWithNameAndInitialValue() {
		DslModelAttribute attribute = new DslModelAttribute("id", "int");
		attribute.setKeyElement(true); // @Id
		attribute.setGeneratedValueStrategy(GeneratedValueStrategy.SEQUENCE);
		attribute.setGeneratedValueSequenceName("MY_SEQUENCE");
		attribute.setGeneratedValueInitialValue(1);
		AttributeInContext attribInCtx = buildAttributeInContext(buildEntityInContext("Book", "BOOK"), attribute);
		
		// check result
		assertTrue(attribInCtx.isGeneratedValue());
		String[] a = jpaFieldAnnotations(attribInCtx);
		assertEquals(4, a.length);
		int i=0;
		assertEquals("@Id", a[i++]);
		assertEquals("@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=\"Book_id_gen\")", a[i++]);
		assertEquals("@SequenceGenerator(name=\"Book_id_gen\", sequenceName=\"MY_SEQUENCE\", initialValue=1)", a[i++]);
		assertEquals("@Column(name=\"id\")", a[i++]);
	}

	@Test 
	public void testColumnIdGenValSequenceWithNameAndAllocationSizeAndInitialValue() {
		DslModelAttribute attribute = new DslModelAttribute("id", "int");
		attribute.setKeyElement(true); // @Id
		attribute.setGeneratedValueStrategy(GeneratedValueStrategy.SEQUENCE);
		attribute.setGeneratedValueSequenceName("MY_SEQUENCE");
		attribute.setGeneratedValueAllocationSize(20);
		attribute.setGeneratedValueInitialValue(1);
		AttributeInContext attribInCtx = buildAttributeInContext(buildEntityInContext("Book", "BOOK"), attribute);
		
		// check result
		assertTrue(attribInCtx.isGeneratedValue());
		String[] a = jpaFieldAnnotations(attribInCtx);
		assertEquals(4, a.length);
		int i=0;
		assertEquals("@Id", a[i++]);
		assertEquals("@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=\"Book_id_gen\")", a[i++]);
		assertEquals("@SequenceGenerator(name=\"Book_id_gen\", sequenceName=\"MY_SEQUENCE\", allocationSize=20, initialValue=1)", a[i++]);
		assertEquals("@Column(name=\"id\")", a[i++]);
	}
	
	//---------------------------------------------------------------------------------------------
	// LINK CARDINALITY 
	//---------------------------------------------------------------------------------------------
	@Test 
	public void testLinkCardinalityOneToMany() {
		DslModelLink fakeLink = buildDslModelLink("towns", "Town", Cardinality.ONE_TO_MANY);
		LinkInContext link = buildLinkInContext("Country", fakeLink);
		assertFalse(link.isOwningSide());
		JpaInContext jpa = new JpaInContext();
		assertEquals("@OneToMany", jpa.linkCardinalityAnnotation(0, link) );
		jpa.setGenTargetEntity(true);
		assertEquals("@OneToMany(targetEntity=Town.class)", jpa.linkCardinalityAnnotation(0, link) );
	}

	@Test 
	public void testLinkCardinalityOneToManyWithOptions() {
		DslModelLink fakeLink = buildDslModelLink("towns", "Town", Cardinality.ONE_TO_MANY);
		fakeLink.setFetchType(FetchType.LAZY);
		fakeLink.setMappedBy("country");
		fakeLink.setOrphanRemoval(true);
		fakeLink.setOptional(Optional.TRUE); // ignored for OneToMany
		LinkInContext link = buildLinkInContext("Country", fakeLink);
		assertFalse(link.isOwningSide());
		JpaInContext jpa = new JpaInContext();
		assertEquals("@OneToMany(mappedBy=\"country\", fetch=FetchType.LAZY, orphanRemoval=true)", 
				jpa.linkCardinalityAnnotation(0, link) );
	}

	@Test 
	public void testLinkCardinalityManyToOne() {
		DslModelLink fakeLink = buildDslModelLink("country", "Country", Cardinality.MANY_TO_ONE);
		LinkInContext link = buildLinkInContext("Town", fakeLink);
		assertTrue(link.isOwningSide());
		JpaInContext jpa = new JpaInContext();
		assertEquals("@ManyToOne", jpa.linkCardinalityAnnotation(0, link) );
		jpa.setGenTargetEntity(true);
		assertEquals("@ManyToOne(targetEntity=Country.class)", jpa.linkCardinalityAnnotation(0, link) );
	}

	@Test 
	public void testLinkCardinalityManyToOneWithOptions() {
		DslModelLink fakeLink = buildDslModelLink("country", "Country", Cardinality.MANY_TO_ONE);
		fakeLink.setFetchType(FetchType.LAZY);
		fakeLink.setMappedBy("town"); // error : not used for ManyToOne
		fakeLink.setOrphanRemoval(true); // ignored for ManyToOne
		fakeLink.setOptional(Optional.TRUE);
		LinkInContext link = buildLinkInContext("Town", fakeLink);
		assertTrue(link.isOwningSide());
		JpaInContext jpa = new JpaInContext();
		assertEquals("@ManyToOne(fetch=FetchType.LAZY, optional=true)", jpa.linkCardinalityAnnotation(0, link) );
	}

	private DslModelLink buildDslModelLink(String fielName, String referencedEntityName, Cardinality cardinality) {
		DslModelLink fakeLink = new DslModelLink(fielName);
		fakeLink.setReferencedEntityName(referencedEntityName);
		fakeLink.setCardinality(cardinality);
		return fakeLink;
	}
	private LinkInContext buildLinkInContext(String entityName, DslModelLink fakeLink ) {
		DslModelEntity fakeOriginEntity = new DslModelEntity(entityName);
		DslModelEntity fakeTargetEntity = new DslModelEntity(fakeLink.getReferencedEntityName());
		DslModel fakeModel = new DslModel("mymodel");
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

	private EntityInContext buildEntityInContext(String entityName) {
		return buildEntityInContext(entityName, "NO_TABLE");
	}
	private EntityInContext buildEntityInContext(String entityName, String tableName) {
		DslModel fakeModel = new DslModel("FakeModel");
		DslModelEntity fakeEntity = new DslModelEntity(entityName);
		fakeModel.addEntity(fakeEntity);

		TelosysToolsCfgManager cfgManager = new TelosysToolsCfgManager("projectAbsolutePath");
		TelosysToolsCfg telosysToolsCfg = cfgManager.createDefaultTelosysToolsCfg();
		EnvInContext envInContext = new EnvInContext() ; 
		
		ModelInContext modelInContext = new ModelInContext(fakeModel, telosysToolsCfg, envInContext);
		return new EntityInContext(fakeEntity, "org.foo.pkg", modelInContext, envInContext);
	}
	
	private AttributeInContext buildAttributeInContext(EntityInContext entityInContext, Attribute attribute) {
		return new AttributeInContext(entityInContext, attribute, null, new EnvInContext() );
	}
}
