package org.telosys.tools.generator.context;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JavaImportsListTest  {

	@Test
	public void testDeclareType() {
		JavaImportsList imports = new JavaImportsList();

		imports.declareType(null);
		assertEquals(0, imports.getFinalImportsList().size() );
		
		imports.declareType("xxx");
		assertEquals(0, imports.getFinalImportsList().size() );
		
		imports.declareType("float");
		assertEquals(0, imports.getFinalImportsList().size() );
		
		imports.declareType("BigDecimal");
		assertEquals(0, imports.getFinalImportsList().size() );
		
		imports.declareType("String");
		assertEquals(0, imports.getFinalImportsList().size() );
		
		imports.declareType("java.math.BigDecimal");
		assertEquals(1, imports.getFinalImportsList().size() );

		imports.declareType("java.util.List");
		assertEquals(2, imports.getFinalImportsList().size() );
	}

	@Test
	public void testDeclareLinkType() {
		JavaImportsList imports = new JavaImportsList();

		imports.declareLinkType(null);
		assertEquals(0, imports.getFinalImportsList().size() );
		
		imports.declareLinkType("xxx");
		assertEquals(0, imports.getFinalImportsList().size() );

		imports.declareLinkType("java.util.List");
		assertEquals(0, imports.getFinalImportsList().size() );

		imports.declareLinkType("List<Foo>");
		assertEquals(1, imports.getFinalImportsList().size() );
	}

	@Test
	public void testDeclareLinkType2() {
		JavaImportsList imports = new JavaImportsList();
		imports.declareLinkType("List<Foo>");
		int n = 0;
		assertEquals(++n, imports.getFinalImportsList().size() );
		imports.declareLinkType(" Set< Foo > ");
		assertEquals(++n, imports.getFinalImportsList().size() );
		imports.declareLinkType(" ArrayList < Foo >");
		assertEquals(++n, imports.getFinalImportsList().size() );
		
		imports.declareLinkType("Vector< XxxxYyyy >");
		assertEquals(++n, imports.getFinalImportsList().size() );
		imports.declareLinkType("Vector< XxxxYyyy >");
		assertEquals(n, imports.getFinalImportsList().size() );
		
		imports.declareLinkType("Collection< XxxxYyyy >");
		assertEquals(++n, imports.getFinalImportsList().size() );

		imports.declareLinkType("Map< XxxxYyyy >");
		assertEquals(++n, imports.getFinalImportsList().size() );
		
	}
}
