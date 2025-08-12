package org.telosys.tools.generator.context.tools;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AnnotationsBuilderTest {

	@Test
	public void test1() {
		AnnotationsBuilder annotationsBuilder = new AnnotationsBuilder(4);
		
		assertEquals(0, annotationsBuilder.getCount() );
		assertEquals("", annotationsBuilder.getMultiLineAnnotations() );
		assertEquals("", annotationsBuilder.getSingleLineAnnotations() );
		
		annotationsBuilder.addAnnotation("@Aaa");
		assertEquals(1, annotationsBuilder.getCount() );
		assertEquals("    @Aaa", annotationsBuilder.getMultiLineAnnotations() );
		assertEquals("    @Aaa", annotationsBuilder.getSingleLineAnnotations() );
		
		
		annotationsBuilder.addAnnotation("@Bbb(123)");
		annotationsBuilder.addAnnotation("@Ccc(value=12,foo=\"abc\")");
		
		assertEquals(3, annotationsBuilder.getCount() );
		assertEquals("    @Aaa\n    @Bbb(123)\n    @Ccc(value=12,foo=\"abc\")", annotationsBuilder.getMultiLineAnnotations() );
		assertEquals("    @Aaa @Bbb(123) @Ccc(value=12,foo=\"abc\")", annotationsBuilder.getSingleLineAnnotations() );

		annotationsBuilder.addAnnotation("@Ddd");
		assertEquals(4, annotationsBuilder.getCount() );
		assertEquals("    @Aaa\n    @Bbb(123)\n    @Ccc(value=12,foo=\"abc\")\n    @Ddd", annotationsBuilder.getMultiLineAnnotations() );
		assertEquals("    @Aaa @Bbb(123) @Ccc(value=12,foo=\"abc\") @Ddd", annotationsBuilder.getSingleLineAnnotations() );
	}
}
