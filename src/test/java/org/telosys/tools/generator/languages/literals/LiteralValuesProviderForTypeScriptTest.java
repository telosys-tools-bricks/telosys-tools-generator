package org.telosys.tools.generator.languages.literals;

import org.junit.Test;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.generator.languages.types.LanguageType;
import org.telosys.tools.generic.model.types.NeutralType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LiteralValuesProviderForTypeScriptTest extends AbstractLiteralsTest {
	
	//----------------------------------------------------------------------------------
	@Override
	protected String getLanguageName() {
		return "TypeScript" ;
	}	
	//----------------------------------------------------------------------------------

	@Test
	public void testLiteralNull() {
		assertEquals("null", getLiteralValuesProvider().getLiteralNull() );
	}

	@Test
	public void testLiteralValuesForBOOLEAN() {
		LanguageType lt = getLanguageType(NeutralType.BOOLEAN );
		assertEquals("true",  getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("false", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
		assertEquals("true",  getLiteralValuesProvider().generateLiteralValue(lt, 0, 3).getCurrentLanguageValue() );
	}

	@Test
	public void testLiteralValuesForSTRING() {
		LanguageType lt = getLanguageType(NeutralType.STRING );
		assertEquals("'AAA'",   getLiteralValuesProvider().generateLiteralValue(lt, 3, 1).getCurrentLanguageValue() );
		assertEquals("'BBB'",   getLiteralValuesProvider().generateLiteralValue(lt, 3, 2).getCurrentLanguageValue() );
		assertEquals("'CCCCC'", getLiteralValuesProvider().generateLiteralValue(lt, 5, 3).getCurrentLanguageValue() );
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
		assertEquals("new Date('2000-06-22')", getLiteralValuesProvider().generateLiteralValue(lt, 0,  0).getCurrentLanguageValue() );
		assertEquals("new Date('2001-06-22')", getLiteralValuesProvider().generateLiteralValue(lt, 0,  1).getCurrentLanguageValue() );
		assertEquals("new Date('2002-06-22')", getLiteralValuesProvider().generateLiteralValue(lt, 0,  2).getCurrentLanguageValue() );
		lt = getLanguageTypeNotNull(NeutralType.DATE );
		assertEquals("new Date('2000-06-22')", getLiteralValuesProvider().generateLiteralValue(lt, 0,  0).getCurrentLanguageValue() );
		assertEquals("new Date('2001-06-22')", getLiteralValuesProvider().generateLiteralValue(lt, 0,  1).getCurrentLanguageValue() );
	}
	@Test
	public void testLiteralValuesForTIME() {
		LanguageType lt = getLanguageType(NeutralType.TIME );
		assertEquals("new Date('1970-01-01T00:47:53')", getLiteralValuesProvider().generateLiteralValue(lt, 0,  0).getCurrentLanguageValue() );
		assertEquals("new Date('1970-01-01T01:47:53')", getLiteralValuesProvider().generateLiteralValue(lt, 0,  1).getCurrentLanguageValue() );
		lt = getLanguageTypeNotNull(NeutralType.TIME );
		assertEquals("new Date('1970-01-01T00:47:53')", getLiteralValuesProvider().generateLiteralValue(lt, 0,  0).getCurrentLanguageValue() );
		assertEquals("new Date('1970-01-01T01:47:53')", getLiteralValuesProvider().generateLiteralValue(lt, 0,  1).getCurrentLanguageValue() );
	}
	@Test
	public void testLiteralValuesForTIMETZ() { // No TZ in 'Date' object => same result as "time" type
		LanguageType lt = getLanguageType(NeutralType.TIMETZ );
		assertEquals("new Date('1970-01-01T00:47:53')", getLiteralValuesProvider().generateLiteralValue(lt, 0,  0).getCurrentLanguageValue() );
		assertEquals("new Date('1970-01-01T01:47:53')", getLiteralValuesProvider().generateLiteralValue(lt, 0,  1).getCurrentLanguageValue() );
		lt = getLanguageTypeNotNull(NeutralType.TIMETZ );
		assertEquals("new Date('1970-01-01T00:47:53')", getLiteralValuesProvider().generateLiteralValue(lt, 0,  0).getCurrentLanguageValue() );
		assertEquals("new Date('1970-01-01T01:47:53')", getLiteralValuesProvider().generateLiteralValue(lt, 0,  1).getCurrentLanguageValue() );
	}
	@Test
	public void testLiteralValuesForDATETIME() {
		LanguageType lt = getLanguageType(NeutralType.DATETIME );
		assertEquals("new Date('2000-05-21T00:47:53')", getLiteralValuesProvider().generateLiteralValue(lt, 0,  0).getCurrentLanguageValue() );
		assertEquals("new Date('2001-05-21T01:47:53')", getLiteralValuesProvider().generateLiteralValue(lt, 0,  1).getCurrentLanguageValue() );
		assertEquals("new Date('2002-05-21T02:47:53')", getLiteralValuesProvider().generateLiteralValue(lt, 0,  2).getCurrentLanguageValue() );
		lt = getLanguageTypeNotNull(NeutralType.DATETIME );
		assertEquals("new Date('2000-05-21T00:47:53')", getLiteralValuesProvider().generateLiteralValue(lt, 0,  0).getCurrentLanguageValue() );
		assertEquals("new Date('2001-05-21T01:47:53')", getLiteralValuesProvider().generateLiteralValue(lt, 0,  1).getCurrentLanguageValue() );
	}
	@Test
	public void testLiteralValuesForDATETIMETZ() { // No TZ in 'Date' object => same result as "datetime" type
		LanguageType lt = getLanguageType(NeutralType.DATETIMETZ );
		assertEquals("new Date('2000-05-21T00:47:53')", getLiteralValuesProvider().generateLiteralValue(lt, 0,  0).getCurrentLanguageValue() );
		assertEquals("new Date('2001-05-21T01:47:53')", getLiteralValuesProvider().generateLiteralValue(lt, 0,  1).getCurrentLanguageValue() );
		assertEquals("new Date('2002-05-21T02:47:53')", getLiteralValuesProvider().generateLiteralValue(lt, 0,  2).getCurrentLanguageValue() );
		lt = getLanguageTypeNotNull(NeutralType.DATETIMETZ );
		assertEquals("new Date('2000-05-21T00:47:53')", getLiteralValuesProvider().generateLiteralValue(lt, 0,  0).getCurrentLanguageValue() );
		assertEquals("new Date('2001-05-21T01:47:53')", getLiteralValuesProvider().generateLiteralValue(lt, 0,  1).getCurrentLanguageValue() );
	}
	
	@Test
	public void testLiteralValuesForUUID() {
		LanguageType lt = getLanguageType(NeutralType.UUID );
		String v = getLiteralValuesProvider().generateLiteralValue(lt, 0,  0).getCurrentLanguageValue();
		assertEquals(38, v.length() ); // 36 + 2 (')
		assertTrue(v.startsWith( "'" ) );
		assertTrue(v.endsWith(   "'" ) );
		assertEquals(4, StrUtil.countChar(v, '-'));
	}

	@Test
	public void testLiteralValuesForBINARY() { // always "new Uint8Array(0)"
		LanguageType lt = getLanguageType(NeutralType.BINARY );
		assertEquals("new Uint8Array(0)", getLiteralValuesProvider().generateLiteralValue(lt, 0,  0).getCurrentLanguageValue() );
		assertEquals("new Uint8Array(0)", getLiteralValuesProvider().generateLiteralValue(lt, 0,  1).getCurrentLanguageValue() );
	}
	
	@Test
	public void testEqualsStatement() {
		assertEquals(" == foo", getLiteralValuesProvider().getEqualsStatement("foo", getLanguageType(NeutralType.STRING )) );
		assertEquals(" == foo", getLiteralValuesProvider().getEqualsStatement("foo", getLanguageType(NeutralType.BOOLEAN )) );
		assertEquals(" == foo", getLiteralValuesProvider().getEqualsStatement("foo", getLanguageType(NeutralType.INTEGER )) );
		assertEquals(" == foo", getLiteralValuesProvider().getEqualsStatement("foo", getLanguageType(NeutralType.FLOAT )) );
	}

}
