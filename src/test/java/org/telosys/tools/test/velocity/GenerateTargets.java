package org.telosys.tools.test.velocity;

import org.telosys.tools.commons.TelosysToolsLogger;
import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generator.util.GeneratorRunner;

public class GenerateTargets {

	private final static String OUTPUT_FOLDER = "GENERATED_FILES" ; // output folder in the project location
	
	public static void main(String[] args) throws GeneratorException {

		TelosysToolsLogger logger = LoggerProvider.getLogger();
		
		GeneratorRunner generatorRunner = new GeneratorRunner(Const.REPOSITORY_FILE2, Const.PROJECT_LOCATION, logger);
			
		//--- The "project folder" must be set in the project configuration
		
		//--- Launch generation
//		generatorRunner.generateEntity("AUTHOR",   "Author.java",   OUTPUT_FOLDER, "bean_jpa.vm" );
//		generatorRunner.generateEntity("BOOK",     "Book.java",     OUTPUT_FOLDER, "bean_jpa.vm" );
//		generatorRunner.generateEntity("EMPLOYEE", "Employee.java", OUTPUT_FOLDER, "bean_jpa.vm" );
//		generatorRunner.generateEntity("REVIEW",   "Review.java",   OUTPUT_FOLDER, "bean_jpa.vm" );
			
			
		generatorRunner.generateEntity("AUTHOR",          "Author.java",        OUTPUT_FOLDER, "jpa_bean_with_links.vm" );
		generatorRunner.generateEntity("BOOK_ORDER_ITEM", "BookOrderItem.java", OUTPUT_FOLDER, "jpa_bean_with_links.vm" );
			
	}
}
