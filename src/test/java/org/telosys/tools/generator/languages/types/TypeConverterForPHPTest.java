package org.telosys.tools.generator.languages.types;

import static org.telosys.tools.generator.languages.types.AttributeTypeConst.NONE;
import static org.telosys.tools.generator.languages.types.AttributeTypeConst.NOT_NULL;
import static org.telosys.tools.generator.languages.types.AttributeTypeConst.OBJECT_TYPE;
import static org.telosys.tools.generator.languages.types.AttributeTypeConst.PRIMITIVE_TYPE;
import static org.telosys.tools.generator.languages.types.AttributeTypeConst.UNSIGNED_TYPE;

import org.junit.Test;
import org.telosys.tools.generic.model.types.NeutralType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TypeConverterForPHPTest extends AbstractTypeTest {

	//---------------------------------------------------------------
	@Override
	protected String getLanguageName() {
		return "PHP" ;
	}
	//---------------------------------------------------------------
	
	private void checkObjectType( LanguageType lt, String simpleType, String fullType) {
		assertNotNull(lt);
		assertFalse ( lt.isPrimitiveType() ) ;
		assertEquals(simpleType, lt.getSimpleType() );
		assertEquals(fullType,   lt.getFullType() );
		assertEquals(simpleType, lt.getWrapperType() );
	}

	private void checkPrimitiveType( LanguageType lt, String primitiveType) {
		assertNotNull(lt);
		assertTrue ( lt.isPrimitiveType() ) ;
		assertEquals(primitiveType, lt.getSimpleType() );
		assertEquals(primitiveType, lt.getFullType() );
		assertEquals(primitiveType, lt.getWrapperType() );
	}

	private void checkPrimitiveTypeExpected(String neutralType, String targetType) {
		checkPrimitiveType( getType( neutralType, NOT_NULL ),                  targetType );
		checkPrimitiveType( getType( neutralType, NOT_NULL + PRIMITIVE_TYPE),  targetType );
		checkPrimitiveType( getType( neutralType, NOT_NULL + OBJECT_TYPE ),    targetType );
		checkPrimitiveType( getType( neutralType, NOT_NULL + UNSIGNED_TYPE ),  targetType );
	}
	
	@Test
	public void testString() {
		checkPrimitiveTypeExpected(NeutralType.STRING,  "string");
	}

	@Test
	public void testBoolean() {
		checkPrimitiveTypeExpected(NeutralType.BOOLEAN,  "bool");
	}

	@Test
	public void testByte() {
		checkPrimitiveTypeExpected(NeutralType.BYTE,  "int");
	}

	@Test
	public void testShort() {
		checkPrimitiveTypeExpected(NeutralType.SHORT,  "int");
	}

	@Test
	public void testInteger() {
		checkPrimitiveTypeExpected(NeutralType.INTEGER,  "int");
	}

	@Test
	public void testLong() {
		checkPrimitiveTypeExpected(NeutralType.LONG,  "int");
	}

	@Test
	public void testFloat() {
		checkPrimitiveTypeExpected(NeutralType.FLOAT,  "float");
	}

	@Test
	public void testDouble() {
		checkPrimitiveTypeExpected(NeutralType.DOUBLE,  "float");
	}

	@Test
	public void testDecimal() {
		checkPrimitiveTypeExpected(NeutralType.DECIMAL,  "float");
	}

	private void checkDateTimeExpected(String neutralType) {
		checkObjectType( getType( neutralType, NOT_NULL ),                 "DateTime", "\\DateTime");
		checkObjectType( getType( neutralType, NOT_NULL + PRIMITIVE_TYPE), "DateTime", "\\DateTime" );
		checkObjectType( getType( neutralType, NOT_NULL + OBJECT_TYPE),    "DateTime", "\\DateTime" );
		checkObjectType( getType( neutralType, NOT_NULL + UNSIGNED_TYPE ), "DateTime", "\\DateTime");
	}

	@Test
	public void testTimestamp() {
		checkDateTimeExpected(NeutralType.TIMESTAMP);
	}

	private void checkVoidExpected(String neutralType) {
		checkObjectType( getType( neutralType, NONE ),                  "", "" );
		checkObjectType( getType( neutralType, PRIMITIVE_TYPE ),        "", "" );
		checkObjectType( getType( neutralType, OBJECT_TYPE),            "", "" );
		checkObjectType( getType( neutralType, UNSIGNED_TYPE),          "", "" );

		checkObjectType( getType( neutralType, NOT_NULL ),                  "", "");
		checkObjectType( getType( neutralType, NOT_NULL + PRIMITIVE_TYPE ), "", "");
		checkObjectType( getType( neutralType, NOT_NULL + OBJECT_TYPE ),    "", "");
		checkObjectType( getType( neutralType, NOT_NULL + UNSIGNED_TYPE),   "", "");
	}

	@Test
	public void testDate() {
		checkVoidExpected(NeutralType.DATE);
	}

	@Test
	public void testTime() {
		checkVoidExpected(NeutralType.TIME);
	}

	@Test
	public void testBinary() {
		checkVoidExpected(NeutralType.BINARY);
	}

}
