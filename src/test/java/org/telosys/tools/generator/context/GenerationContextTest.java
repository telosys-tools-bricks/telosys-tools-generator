package org.telosys.tools.generator.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.telosys.tools.commons.TelosysToolsLogger;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.generator.GeneratorContextBuilder;
import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generator.context.names.ContextName;
import org.telosys.tools.generator.engine.GeneratorContext;
import org.telosys.tools.generator.task.TelosysProject;
import org.telosys.tools.generic.model.Model;

import junit.env.telosys.tools.generator.LoggerProvider;
import junit.env.telosys.tools.generator.TestsEnv;
import junit.env.telosys.tools.generator.TestsProject;

public class GenerationContextTest {

	private GeneratorContext buildGeneratorContext(String projectName, String modelFile, String bundleName) throws Exception {
		
		//TelosysProject telosysProject = TestsProject.initProjectEnv(projectName, bundleName) ;
		String projectFolderAbsolutePath = TestsEnv.buildAbsolutePath("myproject");
		TelosysProject telosysProject = new TelosysProject(projectFolderAbsolutePath);
		
		System.out.println("Project folder : " + telosysProject.getProjectFolder() ) ;
		
		System.out.println("loading configuration file 'TelosysToolsCfg'...");
		TelosysToolsCfg telosysToolsCfg = telosysProject.loadTelosysToolsCfg();
		System.out.println("Project absolute path : " + telosysToolsCfg.getProjectAbsolutePath() );
		
		
		System.out.println("loading model from 'dbrep' file : " + modelFile );
		Model model = telosysProject.loadModelFromDbRep(modelFile);
		assertNotNull(model);
		
		//TelosysToolsLogger logger = new ConsoleLogger();
		TelosysToolsLogger logger = LoggerProvider.getLogger();
		
		GeneratorContextBuilder generatorContextBuilder = new GeneratorContextBuilder(telosysToolsCfg, logger);
		
//		DatabasesConfigurations databasesConfigurations = new DatabasesConfigurations();
//		return generatorContextBuilder.initBasicContext(model, databasesConfigurations, bundleName);
		return generatorContextBuilder.initBasicContext(model, bundleName);
	}
	
	private final static String DBREP_FILE = "BookStore-with-JC.dbrep" ;
	@Test
	public void testContextDbRep1() throws Exception {
		
		//GeneratorContext generatorContext = buildGeneratorContext("myproject", TestsProject.REPO_FILENAME, TestsProject.BUNDLE_NAME);
		GeneratorContext generatorContext = buildGeneratorContext("myproject", DBREP_FILE, TestsProject.BUNDLE_NAME);
		
		check$project( generatorContext ) ;
		check$model( generatorContext ) ;
		check$entity_book(generatorContext) ;
		
	}

	private void check$project(GeneratorContext generatorContext) {
		Object o = generatorContext.get(ContextName.PROJECT) ;
		assertNotNull( o ) ;
		assertTrue( o instanceof ProjectInContext );
		ProjectInContext projectInContext = (ProjectInContext) o ;
		assertNotNull( projectInContext.getAllVariables() ) ;
	}

	private void check$model(GeneratorContext generatorContext) {
		Object o = generatorContext.get(ContextName.MODEL) ;
		assertNotNull( o ) ;
		assertTrue( o instanceof ModelInContext );
		ModelInContext model = (ModelInContext) o ;
		System.out.println("" + model.getNumberOfEntities() + " entities : ");
		for ( EntityInContext entity : model.getAllEntites() ) {
			System.out.println(" . " + entity.getName() );
		}
		assertEquals( 21, model.getNumberOfEntities() );
		model.getEntityByClassName("Book");
	}

	private void check$entity_book(GeneratorContext generatorContext) throws GeneratorException {
		System.out.println("Check entity 'Book' " );
		ModelInContext model = (ModelInContext) generatorContext.get(ContextName.MODEL) ;
		EntityInContext book = model.getEntityByClassName("Book");
		assertNotNull(book);
		assertEquals("BOOK", book.getDatabaseTable() );
		assertEquals(10, book.getAttributesCount() );
		book.getKeyAttributesCount() ;
		book.getNonKeyAttributesCount();
		assertEquals(book.getAttributesCount(), book.getKeyAttributesCount() + book.getNonKeyAttributesCount() );
		for ( AttributeInContext a : book.getAttributes() ) {
			print(a);
		}
		
		System.out.println("getAttributesByCriteria(KEY)  (" + book.getAttributesByCriteria(Const.KEY).size() + ") : ");
		for ( AttributeInContext a : book.getAttributesByCriteria(Const.KEY) ) {
			print(a);
		}
		assertEquals(book.getKeyAttributesCount(), book.getAttributesByCriteria(Const.KEY).size());

		System.out.println("getAttributesByCriteria(NOT_KEY) (" + book.getAttributesByCriteria(Const.NOT_KEY).size() + ") : ");
		for ( AttributeInContext a : book.getAttributesByCriteria(Const.NOT_KEY) ) {
			print(a);
		}
		assertEquals(book.getNonKeyAttributesCount(), book.getAttributesByCriteria(Const.NOT_KEY).size());

		List<AttributeInContext> notKeyNotInLinks = book.getAttributesByCriteria(Const.NOT_KEY,Const.NOT_IN_LINKS );
		System.out.println("getAttributesByCriteria(NOT_KEY, NOT_IN_LINKS) (" + notKeyNotInLinks.size() + ") : ");
		for ( AttributeInContext a : notKeyNotInLinks) {
			print(a);
		}

		List<AttributeInContext> notKeyNotInSelectedLinks = book.getAttributesByCriteria(Const.NOT_KEY,Const.NOT_IN_SELECTED_LINKS );
		System.out.println("getAttributesByCriteria(NOT_KEY, NOT_IN_SELECTED_LINKS) (" + notKeyNotInSelectedLinks.size() + ") : ");
		for ( AttributeInContext a : notKeyNotInSelectedLinks) {
			print(a);
		}
		
		System.out.println("--- Links : ");
		for ( LinkInContext link : book.getLinks() ) {
			System.out.println(" . " + link.getId() + " : " + link.getCardinality() + " selected = " + link.isSelected() );
			System.out.println("   attributesCount = " + link.getAttributesCount() + "" ); 
			System.out.println("   attributes.size = " + link.getAttributes().size() ); 
			for ( LinkAttributesPairInContext pair : link.getAttributes() ) {
				System.out.println("   - " + pair.getOriginAttribute().getDatabaseName()
						+ " : " + pair.getTargetAttribute().getDatabaseName() );
			}
			System.out.println("   joinColumns.size = " + link.getJoinColumns().size()); 
			for ( JoinColumnInContext jc : link.getJoinColumns() ) {
				System.out.println("   - " + jc.getName() + " --> " + jc.getReferencedColumnName() );
			}
		}

		AttributeInContext authorId = book.getAttributeByColumnName("AUTHOR_ID");
		assertTrue( authorId.isUsedInLinks() );
		assertTrue( authorId.isUsedInSelectedLinks() );
		
		AttributeInContext publisherId = book.getAttributeByColumnName("PUBLISHER_ID");
		assertTrue( publisherId.isUsedInLinks() );
		assertTrue( publisherId.isUsedInSelectedLinks() );
		
	}
	
	
	private void print(AttributeInContext a ) {
		String s = " " ;
		if ( a.isKeyElement() ) s = s + "PK " ;
		if ( a.isFK() ) s = s + "FK " ;
		if ( a.isFKSimple() ) s = s + "FK simple " ;
		if ( a.isFKComposite() ) s = s + "FK composite " ;
		if ( a.isUsedInLinks() ) s = s + "Used in links " ;
		if ( a.isUsedInSelectedLinks() ) s = s + "Used in selected links " ;
		
		System.out.println(" . " + a.getName() + "(DB name '" + a.getDatabaseName() + "') - " + s);
	}
}
