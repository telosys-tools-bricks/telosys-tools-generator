package org.telosys.tools.generator.languages.literals;

import java.util.UUID;

import org.junit.Test;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.generator.languages.types.LanguageType;
import org.telosys.tools.generic.model.types.NeutralType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LiteralValuesProviderForPHPTest extends AbstractLiteralsTest {
	
	//----------------------------------------------------------------------------------
	@Override
	protected String getLanguageName() {
		return "PHP" ;
	}	
	//----------------------------------------------------------------------------------

	@Test
	public void testLiteralNull() {
		assertEquals("null", getLiteralValuesProvider().getLiteralNull() );
	}

	@Test
	public void testLiteralValuesForBOOLEAN() {
		LanguageType lt = getLanguageType(NeutralType.BOOLEAN );
		assertEquals("false", getLiteralValuesProvider().generateLiteralValue(lt, 0, 0).getCurrentLanguageValue() );
		assertEquals("true",  getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("false", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
		assertEquals("true",  getLiteralValuesProvider().generateLiteralValue(lt, 0, 3).getCurrentLanguageValue() );
	}

	@Test
	public void testLiteralValuesForSTRING() {
		LanguageType lt = getLanguageType(NeutralType.STRING );
		assertEquals("\"AA\"",    getLiteralValuesProvider().generateLiteralValue(lt, 2, 0).getCurrentLanguageValue() );
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
	public void testLiteralValuesForDATETIME() {
		LanguageType lt = getLanguageType(NeutralType.DATETIME );
		assertEquals("new DateTime('2000-05-21T00:47:53')", getLiteralValuesProvider().generateLiteralValue(lt, 0, 0).getCurrentLanguageValue() );
		assertEquals("new DateTime('2001-05-21T01:47:53')", getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("new DateTime('2002-05-21T02:47:53')", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
	}
	@Test
	public void testLiteralValuesForDATETIMETZ() {
		LanguageType lt = getLanguageType(NeutralType.DATETIMETZ );
		assertEquals("new DateTime('2000-05-21T00:47:53+00:00')", getLiteralValuesProvider().generateLiteralValue(lt, 0,    0).getCurrentLanguageValue() );
		assertEquals("new DateTime('2001-05-21T01:47:53+01:00')", getLiteralValuesProvider().generateLiteralValue(lt, 0,    1).getCurrentLanguageValue() );
		assertEquals("new DateTime('2004-05-21T04:47:53+04:00')", getLiteralValuesProvider().generateLiteralValue(lt, 0,    4).getCurrentLanguageValue() );
		assertEquals("new DateTime('2005-05-21T05:47:53+00:00')", getLiteralValuesProvider().generateLiteralValue(lt, 0,    5).getCurrentLanguageValue() );
		assertEquals("new DateTime('2024-05-21T00:47:53+04:00')", getLiteralValuesProvider().generateLiteralValue(lt, 0,   24).getCurrentLanguageValue() );
		assertEquals("new DateTime('2025-05-21T01:47:53+00:00')", getLiteralValuesProvider().generateLiteralValue(lt, 0,   25).getCurrentLanguageValue() );
		assertEquals("new DateTime('2543-05-21T07:47:53+03:00')", getLiteralValuesProvider().generateLiteralValue(lt, 0, 4543).getCurrentLanguageValue() );
	}
	@Test
	public void testLiteralValuesForDATE() {
		LanguageType lt = getLanguageType(NeutralType.DATE );
		assertEquals("new DateTime('2000-06-22')", getLiteralValuesProvider().generateLiteralValue(lt, 0, 0).getCurrentLanguageValue() );
		assertEquals("new DateTime('2001-06-22')", getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("new DateTime('2002-06-22')", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
	}
	@Test
	public void testLiteralValuesForTIME() {
		LanguageType lt = getLanguageType(NeutralType.TIME );
		assertEquals("new DateTime('00:46:52')", getLiteralValuesProvider().generateLiteralValue(lt, 0, 0).getCurrentLanguageValue() );
		assertEquals("new DateTime('01:46:52')", getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("new DateTime('02:46:52')", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
	}
	@Test
	public void testLiteralValuesForTIMETZ() {
		LanguageType lt = getLanguageType(NeutralType.TIMETZ );
		assertEquals("new DateTime('00:46:52+00:00')", getLiteralValuesProvider().generateLiteralValue(lt, 0, 0).getCurrentLanguageValue() );
		assertEquals("new DateTime('01:46:52+01:00')", getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("new DateTime('02:46:52+02:00')", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
	}
	@Test
	public void testLiteralValuesForUUID() {
		LanguageType lt = getLanguageType(NeutralType.UUID );
		String uuidValue;
		
		uuidValue = getLiteralValuesProvider().generateLiteralValue(lt, 0, 0).getCurrentLanguageValue();
		assertEquals(38, uuidValue.length()); // 36 + 2 quotes
		assertNotNull( UUID.fromString(StrUtil.removeQuotes(uuidValue,'\'')) );
		
		uuidValue = getLiteralValuesProvider().generateLiteralValue(lt, 0, 123).getCurrentLanguageValue();
		assertEquals(38, uuidValue.length()); // 36 + 2 quotes
		assertNotNull( UUID.fromString(StrUtil.removeQuotes(uuidValue,'\'')) );
	}


	@Test
	public void testEqualsStatement() {
		assertEquals(" == foo", getLiteralValuesProvider().getEqualsStatement("foo", getLanguageType(NeutralType.STRING )) );
		assertEquals(" == foo", getLiteralValuesProvider().getEqualsStatement("foo", getLanguageType(NeutralType.BOOLEAN )) );
		assertEquals(" == foo", getLiteralValuesProvider().getEqualsStatement("foo", getLanguageType(NeutralType.INTEGER )) );
		assertEquals(" == foo", getLiteralValuesProvider().getEqualsStatement("foo", getLanguageType(NeutralType.FLOAT )) );
	}

}
