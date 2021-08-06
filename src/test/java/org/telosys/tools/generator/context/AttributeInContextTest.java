package org.telosys.tools.generator.context;

import java.math.BigDecimal;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import junit.env.telosys.tools.generator.fakemodel.FakeAttribute;

public class AttributeInContextTest {
	
	@Test
	public void testSizeAsBigDecimal() {
		
		AttributeInContext attribute;
		
		attribute = buildAttribute("firstName", "string", null);
		assertEquals("", attribute.getSize());
		assertEquals(BigDecimal.valueOf(0), attribute.getSizeAsDecimal());
		
		attribute = buildAttribute("firstName", "string", "");
		assertEquals("", attribute.getSize());
		assertEquals(BigDecimal.valueOf(0), attribute.getSizeAsDecimal());
		
		attribute = buildAttribute("firstName", "string", "  ");
		assertEquals("", attribute.getSize());
		assertEquals(BigDecimal.valueOf(0), attribute.getSizeAsDecimal());
		
		attribute = buildAttribute("firstName", "string", "12");
		assertEquals("12", attribute.getSize());
		assertEquals(BigDecimal.valueOf(12), attribute.getSizeAsDecimal());
		
		attribute = buildAttribute("price", "double", "10,2");
		assertEquals("10,2", attribute.getSize());
		assertEquals(BigDecimal.valueOf(10.2), attribute.getSizeAsDecimal());
		
		attribute = buildAttribute("price", "double", " 10,2 ");
		assertEquals(" 10,2 ", attribute.getSize());
		assertEquals(BigDecimal.valueOf(10.2), attribute.getSizeAsDecimal());
	}
	
	//------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------------
	private AttributeInContext buildAttribute(String attribName, String neutralType, String dbSize) {
		FakeAttribute fakeAttribute = new FakeAttribute(attribName, neutralType, false);
		if ( dbSize != null ) {
			fakeAttribute.setDatabaseSize(dbSize);
		}
		else {
			fakeAttribute.setDatabaseName(attribName); // as in DSL model default value
		}
		return new AttributeInContext(null, fakeAttribute, null, null);
	}
	
}
