package org.telosys.tools.generator.languages.literals;

import org.junit.Test;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.generator.languages.types.LanguageType;
import org.telosys.tools.generic.model.types.NeutralType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LiteralValuesProviderForCPlusPlusTest extends AbstractLiteralsTest {
	
	//----------------------------------------------------------------------------------
	@Override
	protected String getLanguageName() {
		return "C++" ;
	}	
	//----------------------------------------------------------------------------------

	@Test
	public void testLiteralNull() {
		// "nullptr" is preferred in modern C++ (better than "NULL")
		assertEquals("nullptr", getLiteralValuesProvider().getLiteralNull() );
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
		// using the "L" suffix for long integer literals is generally recommended in C++, though it’s not strictly required
		assertEquals("1000L", getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("2000L", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
	}

	@Test
	public void testLiteralValuesForFLOAT() {
		LanguageType lt = getLanguageType(NeutralType.FLOAT );
		// "f" suffix is recommended to avoid implicit conversion warnings in some compilers with strict flags (-Wconversion in GCC/Clang).
		assertEquals("1000.5f", getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("2000.5f", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
	}

	@Test
	public void testLiteralValuesForDOUBLE() {
		LanguageType lt = getLanguageType(NeutralType.DOUBLE );
		assertEquals("1000.66", getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("2000.66", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
	}

	@Test
	public void testLiteralValuesForDECIMAL() { // 'decimal' neutral type -> 'double' C++ type
		LanguageType lt = getLanguageType(NeutralType.DECIMAL );
		assertEquals("10000.77", getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("20000.77", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
	}
	
	
	@Test
	public void testLiteralValuesForUUID() {
		LanguageType lt = getLanguageType(NeutralType.UUID );
		String v = getLiteralValuesProvider().generateLiteralValue(lt, 0,  0).getCurrentLanguageValue();
		assertEquals(38, v.length() ); // 36 + 2 quotes
		assertTrue(v.startsWith( "\"" ) );
		assertTrue(v.endsWith(   "\"" ) );
		assertEquals(4, StrUtil.countChar(v, '-'));
	}
	
	@Test
	public void testEqualsStatement() {
		assertEquals(" == foo", getLiteralValuesProvider().getEqualsStatement("foo", getLanguageType(NeutralType.BOOLEAN )) );
		assertEquals(" == foo", getLiteralValuesProvider().getEqualsStatement("foo", getLanguageType(NeutralType.INTEGER )) );
		assertEquals(" == foo", getLiteralValuesProvider().getEqualsStatement("foo", getLanguageType(NeutralType.FLOAT )) );
		// With std::string, you can directly use ==, and it compares the contents:
		assertEquals(" == foo", getLiteralValuesProvider().getEqualsStatement("foo", getLanguageType(NeutralType.STRING )) );
	}

}
