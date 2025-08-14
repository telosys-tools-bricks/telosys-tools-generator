package org.telosys.tools.generator.languages.literals;

import org.junit.Test;
import org.telosys.tools.generator.languages.types.AttributeTypeInfo;
import org.telosys.tools.generator.languages.types.LanguageType;
import org.telosys.tools.generic.model.types.NeutralType;

import static org.junit.Assert.assertEquals;

public class LiteralValuesProviderForCSharpTest extends AbstractLiteralsTest {
	
	//----------------------------------------------------------------------------------
	@Override
	protected String getLanguageName() {
		return "C#" ;
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
		assertEquals("1000L", getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("2000L", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
	}

	@Test
	public void testLiteralValuesForFLOAT() {
		LanguageType lt = getLanguageType(NeutralType.FLOAT );
		assertEquals("1000.5F", getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("2000.5F", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
	}

	@Test
	public void testLiteralValuesForDOUBLE() {
		LanguageType lt = getLanguageType(NeutralType.DOUBLE );
		assertEquals("1000.66D", getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("2000.66D", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
	}

	@Test
	public void testLiteralValuesForDECIMAL() {
		LanguageType lt = getLanguageType(NeutralType.DECIMAL );
		assertEquals("10000.77M", getLiteralValuesProvider().generateLiteralValue(lt, 0, 1).getCurrentLanguageValue() );
		assertEquals("20000.77M", getLiteralValuesProvider().generateLiteralValue(lt, 0, 2).getCurrentLanguageValue() );
	}

	@Test
	public void testEqualsStatement() {
		assertEquals(" == foo", getLiteralValuesProvider().getEqualsStatement("foo", getLanguageType(NeutralType.STRING )) );
		assertEquals(" == foo", getLiteralValuesProvider().getEqualsStatement("foo", getLanguageType(NeutralType.BOOLEAN )) );
		assertEquals(" == foo", getLiteralValuesProvider().getEqualsStatement("foo", getLanguageType(NeutralType.INTEGER )) );
		assertEquals(" == foo", getLiteralValuesProvider().getEqualsStatement("foo", getLanguageType(NeutralType.FLOAT )) );
	}
	
	
	private String getInitValue(AttributeTypeInfo attributeTypeInfo) {
		LanguageType languageType = getTypeConverter().getType(attributeTypeInfo);
		System.out.println("LanguageType: " + languageType);
		return getLiteralValuesProvider().getInitValue(attributeTypeInfo, languageType );
	}
	@Test
	public void testInitValue() {
		String literalNull = getLiteralValuesProvider().getLiteralNull();
		
		assertEquals(literalNull, getInitValue( new FakeAttributeTypeInfo(NeutralType.STRING) ) );
		assertEquals(literalNull, getInitValue( new FakeAttributeTypeInfo(NeutralType.STRING).primitiveTypeExpected() ) );
		assertEquals(literalNull, getInitValue( new FakeAttributeTypeInfo(NeutralType.STRING).objectTypeExpected() ) );
		assertEquals("\"\"",      getInitValue( new FakeAttributeTypeInfo(NeutralType.STRING).notNull() ) );
		assertEquals("\"\"",      getInitValue( new FakeAttributeTypeInfo(NeutralType.STRING).notNull().objectTypeExpected() ) );
		
		assertEquals(literalNull, getInitValue( new FakeAttributeTypeInfo(NeutralType.BOOLEAN) ) );
		assertEquals(literalNull, getInitValue( new FakeAttributeTypeInfo(NeutralType.BOOLEAN).unsignedTypeExpected() ) );
		assertEquals(literalNull, getInitValue( new FakeAttributeTypeInfo(NeutralType.BOOLEAN).objectTypeExpected() ) );
		assertEquals("false",     getInitValue( new FakeAttributeTypeInfo(NeutralType.BOOLEAN).notNull() ) );
		assertEquals("false",     getInitValue( new FakeAttributeTypeInfo(NeutralType.BOOLEAN).notNull().objectTypeExpected()  ) );
		
		assertEquals(literalNull,  getInitValue( new FakeAttributeTypeInfo(NeutralType.BYTE) ) );
		assertEquals(literalNull,  getInitValue( new FakeAttributeTypeInfo(NeutralType.BYTE).unsignedTypeExpected() ) );
		assertEquals("0",          getInitValue( new FakeAttributeTypeInfo(NeutralType.BYTE).notNull() ) );
		assertEquals("0",          getInitValue( new FakeAttributeTypeInfo(NeutralType.BYTE).notNull().unsignedTypeExpected() ) );

		assertEquals(literalNull,  getInitValue( new FakeAttributeTypeInfo(NeutralType.SHORT) ) );
		assertEquals(literalNull,  getInitValue( new FakeAttributeTypeInfo(NeutralType.SHORT).unsignedTypeExpected() ) );
		assertEquals("0",          getInitValue( new FakeAttributeTypeInfo(NeutralType.SHORT).notNull() ) );
		assertEquals("0",          getInitValue( new FakeAttributeTypeInfo(NeutralType.SHORT).notNull().unsignedTypeExpected() ) );

		assertEquals(literalNull,  getInitValue( new FakeAttributeTypeInfo(NeutralType.INTEGER) ) );
		assertEquals(literalNull,  getInitValue( new FakeAttributeTypeInfo(NeutralType.INTEGER).unsignedTypeExpected() ) );
		assertEquals("0",          getInitValue( new FakeAttributeTypeInfo(NeutralType.INTEGER).notNull() ) );
		assertEquals("0",          getInitValue( new FakeAttributeTypeInfo(NeutralType.INTEGER).notNull().unsignedTypeExpected() ) );

		assertEquals(literalNull,  getInitValue( new FakeAttributeTypeInfo(NeutralType.LONG) ) );
		assertEquals(literalNull,  getInitValue( new FakeAttributeTypeInfo(NeutralType.LONG).unsignedTypeExpected() ) );
		assertEquals("0L",         getInitValue( new FakeAttributeTypeInfo(NeutralType.LONG).notNull() ) );
		assertEquals("0L",         getInitValue( new FakeAttributeTypeInfo(NeutralType.LONG).notNull().unsignedTypeExpected() ) );

		assertEquals(literalNull,  getInitValue( new FakeAttributeTypeInfo(NeutralType.FLOAT) ) );
		assertEquals(literalNull,  getInitValue( new FakeAttributeTypeInfo(NeutralType.FLOAT).unsignedTypeExpected() ) );
		assertEquals("0.0f",       getInitValue( new FakeAttributeTypeInfo(NeutralType.FLOAT).notNull() ) );
		assertEquals("0.0f",       getInitValue( new FakeAttributeTypeInfo(NeutralType.FLOAT).notNull().unsignedTypeExpected() ) );

		assertEquals(literalNull,  getInitValue( new FakeAttributeTypeInfo(NeutralType.DOUBLE) ) );
		assertEquals(literalNull,  getInitValue( new FakeAttributeTypeInfo(NeutralType.DOUBLE).unsignedTypeExpected() ) );
		assertEquals("0.0",        getInitValue( new FakeAttributeTypeInfo(NeutralType.DOUBLE).notNull() ) );
		assertEquals("0.0",        getInitValue( new FakeAttributeTypeInfo(NeutralType.DOUBLE).notNull().unsignedTypeExpected() ) );

		assertEquals(literalNull,  getInitValue( new FakeAttributeTypeInfo(NeutralType.DECIMAL) ) );
		assertEquals(literalNull,  getInitValue( new FakeAttributeTypeInfo(NeutralType.DECIMAL).unsignedTypeExpected() ) );
		assertEquals("0.0m",       getInitValue( new FakeAttributeTypeInfo(NeutralType.DECIMAL).notNull() ) );
		assertEquals("0.0m",       getInitValue( new FakeAttributeTypeInfo(NeutralType.DECIMAL).notNull().unsignedTypeExpected() ) );

		assertEquals(literalNull,         getInitValue( new FakeAttributeTypeInfo(NeutralType.DATE) ) );
		assertEquals(literalNull,         getInitValue( new FakeAttributeTypeInfo(NeutralType.DATE).primitiveTypeExpected() ) );
		assertEquals("DateOnly.MinValue", getInitValue( new FakeAttributeTypeInfo(NeutralType.DATE).notNull() ) );
		assertEquals("DateOnly.MinValue", getInitValue( new FakeAttributeTypeInfo(NeutralType.DATE).notNull().objectTypeExpected() ) );

		assertEquals(literalNull,         getInitValue( new FakeAttributeTypeInfo(NeutralType.TIME) ) );
		assertEquals(literalNull,         getInitValue( new FakeAttributeTypeInfo(NeutralType.TIME).primitiveTypeExpected() ) );
		assertEquals("TimeOnly.MinValue", getInitValue( new FakeAttributeTypeInfo(NeutralType.TIME).notNull() ) );
		assertEquals("TimeOnly.MinValue", getInitValue( new FakeAttributeTypeInfo(NeutralType.TIME).notNull().objectTypeExpected() ) );

		assertEquals(literalNull,         getInitValue( new FakeAttributeTypeInfo(NeutralType.DATETIME) ) );
		assertEquals(literalNull,         getInitValue( new FakeAttributeTypeInfo(NeutralType.DATETIME).primitiveTypeExpected() ) );
		assertEquals("DateTime.MinValue", getInitValue( new FakeAttributeTypeInfo(NeutralType.DATETIME).notNull() ) );
		assertEquals("DateTime.MinValue", getInitValue( new FakeAttributeTypeInfo(NeutralType.DATETIME).notNull().objectTypeExpected() ) );

		assertEquals(literalNull,               getInitValue( new FakeAttributeTypeInfo(NeutralType.DATETIMETZ) ) );
		assertEquals(literalNull,               getInitValue( new FakeAttributeTypeInfo(NeutralType.DATETIMETZ).primitiveTypeExpected() ) );
		assertEquals("DateTimeOffset.MinValue", getInitValue( new FakeAttributeTypeInfo(NeutralType.DATETIMETZ).notNull() ) );
		assertEquals("DateTimeOffset.MinValue", getInitValue( new FakeAttributeTypeInfo(NeutralType.DATETIMETZ).notNull().objectTypeExpected() ) );

		assertEquals(literalNull,         getInitValue( new FakeAttributeTypeInfo(NeutralType.TIMETZ) ) );
		assertEquals(literalNull,         getInitValue( new FakeAttributeTypeInfo(NeutralType.TIMETZ).primitiveTypeExpected() ) );
		assertEquals("TimeOnly.MinValue", getInitValue( new FakeAttributeTypeInfo(NeutralType.TIMETZ).notNull() ) );
		assertEquals("TimeOnly.MinValue", getInitValue( new FakeAttributeTypeInfo(NeutralType.TIMETZ).notNull().objectTypeExpected() ) );

		assertEquals(literalNull,    getInitValue( new FakeAttributeTypeInfo(NeutralType.UUID) ) );
		assertEquals(literalNull,    getInitValue( new FakeAttributeTypeInfo(NeutralType.UUID).primitiveTypeExpected() ) );
		assertEquals("Guid.Empty",   getInitValue( new FakeAttributeTypeInfo(NeutralType.UUID).notNull() ) );
		assertEquals("Guid.Empty",   getInitValue( new FakeAttributeTypeInfo(NeutralType.UUID).notNull().objectTypeExpected() ) );

		assertEquals(literalNull,    getInitValue( new FakeAttributeTypeInfo(NeutralType.BINARY) ) );
		assertEquals(literalNull,    getInitValue( new FakeAttributeTypeInfo(NeutralType.BINARY).primitiveTypeExpected() ) );
		assertEquals("new byte[0]",  getInitValue( new FakeAttributeTypeInfo(NeutralType.BINARY).notNull() ) );
		assertEquals("new byte[0]",  getInitValue( new FakeAttributeTypeInfo(NeutralType.BINARY).notNull().objectTypeExpected() ) );

	}
}
