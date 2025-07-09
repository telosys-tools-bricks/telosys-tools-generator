package org.telosys.tools.generator.languages.literals;

import org.junit.Test;
import org.telosys.tools.generator.languages.types.LanguageType;
import org.telosys.tools.generic.model.types.NeutralType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LiteralValuesProviderForPythonTest extends AbstractLiteralsTest {
	
	//----------------------------------------------------------------------------------
	@Override
	protected String getLanguageName() {
		return "Python" ;
	}	
	//----------------------------------------------------------------------------------

	@Test
	public void testLiteralNull() {
		assertEquals("None", getLiteralValuesProvider().getLiteralNull() );
	}

	@Test
	public void testLiteralValuesForBOOLEAN() {
		LanguageType lt = getLanguageType(NeutralType.BOOLEAN );
		assertEquals("True",  getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("False", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
		assertEquals("True",  getLiteralValuesProvider().generateLiteralValue(lt, 0, 3).getCurrentLanguageValue() );
	}

	@Test
	public void testLiteralValuesForSTRING() {
		LanguageType lt = getLanguageType(NeutralType.STRING );
		assertEquals("\"AAA\"",   getLiteralValuesProvider().generateLiteralValue(lt, 3, 1).getCurrentLanguageValue() );
		assertEquals("\"BBB\"",   getLiteralValuesProvider().generateLiteralValue(lt, 3, 2).getCurrentLanguageValue() );
		assertEquals("\"CCCCC\"", getLiteralValuesProvider().generateLiteralValue(lt, 5, 3).getCurrentLanguageValue() );
	}

	@Test
	public void testLiteralValuesForBYTE() {
		LanguageType lt = getLanguageType(NeutralType.BYTE );
		assertEquals("1", getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("2", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
	}

	@Test
	public void testLiteralValuesForSHORT() {
		LanguageType lt = getLanguageType(NeutralType.SHORT );
		assertEquals("1", getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("2", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
	}

	@Test
	public void testLiteralValuesForINTEGER() {
		LanguageType lt = getLanguageType(NeutralType.INTEGER );
		assertEquals("100", getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("200", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
	}

	@Test
	public void testLiteralValuesForLONG() {
		LanguageType lt = getLanguageType(NeutralType.LONG );
		assertEquals("1000", getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("2000", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
	}

	@Test
	public void testLiteralValuesForFLOAT() {
		LanguageType lt = getLanguageType(NeutralType.FLOAT );
		assertEquals("1000.5", getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("2000.5", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
	}

	@Test
	public void testLiteralValuesForDOUBLE() {
		LanguageType lt = getLanguageType(NeutralType.DOUBLE );
		assertEquals("1000.66", getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("2000.66", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
	}

	@Test
	public void testLiteralValuesForDECIMAL() {
		LanguageType lt = getLanguageType(NeutralType.DECIMAL );
		assertEquals("10000.77", getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("20000.77", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
	}
	
	@Test
	public void testLiteralValuesForDATE() {
		LanguageType lt = getLanguageType(NeutralType.DATE );
		assertEquals("date.fromisoformat(\"2000-06-22\")", getLiteralValuesProvider().generateLiteralValue(lt, 0,  0).getCurrentLanguageValue() );
		assertEquals("date.fromisoformat(\"2001-06-22\")", getLiteralValuesProvider().generateLiteralValue(lt, 0,  1).getCurrentLanguageValue() );
		assertEquals("date.fromisoformat(\"2002-06-22\")", getLiteralValuesProvider().generateLiteralValue(lt, 0,  2).getCurrentLanguageValue() );
		lt = getLanguageTypeNotNull(NeutralType.DATE );
		assertEquals("date.fromisoformat(\"2000-06-22\")", getLiteralValuesProvider().generateLiteralValue(lt, 0,  0).getCurrentLanguageValue() );
		assertEquals("date.fromisoformat(\"2001-06-22\")", getLiteralValuesProvider().generateLiteralValue(lt, 0,  1).getCurrentLanguageValue() );
	}

	@Test
	public void testLiteralValuesForTIME() {
		LanguageType lt = getLanguageType(NeutralType.TIME );
		assertEquals("time.fromisoformat(\"00:46:52\")", getLiteralValuesProvider().generateLiteralValue(lt, 0,  0).getCurrentLanguageValue() );
		assertEquals("time.fromisoformat(\"01:46:52\")", getLiteralValuesProvider().generateLiteralValue(lt, 0,  1).getCurrentLanguageValue() );
		lt = getLanguageTypeNotNull(NeutralType.TIME );
		assertEquals("time.fromisoformat(\"00:46:52\")", getLiteralValuesProvider().generateLiteralValue(lt, 0,  0).getCurrentLanguageValue() );
		assertEquals("time.fromisoformat(\"01:46:52\")", getLiteralValuesProvider().generateLiteralValue(lt, 0,  1).getCurrentLanguageValue() );
	}

	@Test
	public void testLiteralValuesForDATETIME() {
		LanguageType lt = getLanguageType(NeutralType.DATETIME );
		assertEquals("datetime.fromisoformat(\"2000-05-21T00:47:53\")", getLiteralValuesProvider().generateLiteralValue(lt, 0,  0).getCurrentLanguageValue() );
		assertEquals("datetime.fromisoformat(\"2001-05-21T01:47:53\")", getLiteralValuesProvider().generateLiteralValue(lt, 0,  1).getCurrentLanguageValue() );
		assertEquals("datetime.fromisoformat(\"2002-05-21T02:47:53\")", getLiteralValuesProvider().generateLiteralValue(lt, 0,  2).getCurrentLanguageValue() );
		lt = getLanguageTypeNotNull(NeutralType.DATETIME );
		assertEquals("datetime.fromisoformat(\"2000-05-21T00:47:53\")", getLiteralValuesProvider().generateLiteralValue(lt, 0,  0).getCurrentLanguageValue() );
		assertEquals("datetime.fromisoformat(\"2001-05-21T01:47:53\")", getLiteralValuesProvider().generateLiteralValue(lt, 0,  1).getCurrentLanguageValue() );
	}

	@Test
	public void testLiteralValuesForDATETIMETZ() {
		LanguageType lt = getLanguageType(NeutralType.DATETIMETZ );
		assertEquals("datetime.fromisoformat(\"2000-05-21T00:47:53+00:00\")", getLiteralValuesProvider().generateLiteralValue(lt, 0,  0).getCurrentLanguageValue() );
		assertEquals("datetime.fromisoformat(\"2001-05-21T01:47:53+01:00\")", getLiteralValuesProvider().generateLiteralValue(lt, 0,  1).getCurrentLanguageValue() );
		assertEquals("datetime.fromisoformat(\"2002-05-21T02:47:53+02:00\")", getLiteralValuesProvider().generateLiteralValue(lt, 0,  2).getCurrentLanguageValue() );
		lt = getLanguageTypeNotNull(NeutralType.DATETIMETZ );
		assertEquals("datetime.fromisoformat(\"2000-05-21T00:47:53+00:00\")", getLiteralValuesProvider().generateLiteralValue(lt, 0,  0).getCurrentLanguageValue() );
		assertEquals("datetime.fromisoformat(\"2001-05-21T01:47:53+01:00\")", getLiteralValuesProvider().generateLiteralValue(lt, 0,  1).getCurrentLanguageValue() );
	}

	@Test
	public void testLiteralValuesForUUID() {
		LanguageType lt = getLanguageType(NeutralType.UUID );
		String v = getLiteralValuesProvider().generateLiteralValue(lt, 0,  0).getCurrentLanguageValue();
		assertEquals(44, v.length() );
		assertTrue(v.startsWith( "UUID(\"" ) );
		assertTrue(v.endsWith( "\")" ) );
	}

	@Test
	public void testEqualsStatement() {
		assertEquals(" == foo", getLiteralValuesProvider().getEqualsStatement("foo", getLanguageType(NeutralType.STRING )) );
		assertEquals(" == foo", getLiteralValuesProvider().getEqualsStatement("foo", getLanguageType(NeutralType.BOOLEAN )) );
		assertEquals(" == foo", getLiteralValuesProvider().getEqualsStatement("foo", getLanguageType(NeutralType.INTEGER )) );
		assertEquals(" == foo", getLiteralValuesProvider().getEqualsStatement("foo", getLanguageType(NeutralType.FLOAT )) );
	}

}
