package org.telosys.tools.generator.languages.types;

import static org.telosys.tools.generator.languages.types.AttributeTypeConst.NONE;
import static org.telosys.tools.generator.languages.types.AttributeTypeConst.NOT_NULL;
import static org.telosys.tools.generator.languages.types.AttributeTypeConst.OBJECT_TYPE;
import static org.telosys.tools.generator.languages.types.AttributeTypeConst.PRIMITIVE_TYPE;
import static org.telosys.tools.generator.languages.types.AttributeTypeConst.UNSIGNED_TYPE;

import org.junit.Test;
import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generator.context.EnvInContext;
import org.telosys.tools.generic.model.types.NeutralType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TypeConverterForPythonTest extends AbstractTypeTest {

	//---------------------------------------------------------------
	@Override
	protected String getLanguageName() {
		return "Python" ;
	}
	//---------------------------------------------------------------
	
	private void checkPrimitiveType( LanguageType lt, String primitiveType) {
		assertNotNull(lt);
		assertTrue ( lt.isPrimitiveType() ) ;
		assertEquals(primitiveType, lt.getSimpleType() );
		assertEquals(primitiveType, lt.getFullType() );
		assertEquals(primitiveType, lt.getWrapperType() );
	}

	private void checkObjectType( LanguageType lt, String simpleType, String importInstruction) {
		assertNotNull(lt);
		assertFalse ( lt.isPrimitiveType() ) ;
		assertEquals(simpleType, lt.getSimpleType() );
		assertEquals(importInstruction,   lt.getFullType() );
		assertEquals(simpleType, lt.getWrapperType() );
	}
	
	public void genericPrimitiveTypeTest(String neutralType, String expectedPythonType) {
		println("--- ");
		checkPrimitiveType( getType(neutralType, NONE ),             expectedPythonType);
		checkPrimitiveType( getType(neutralType, NOT_NULL ),         expectedPythonType);
		checkPrimitiveType( getType(neutralType, PRIMITIVE_TYPE ),   expectedPythonType);
		checkPrimitiveType( getType(neutralType, OBJECT_TYPE ),      expectedPythonType);
		checkPrimitiveType( getType(neutralType, UNSIGNED_TYPE ),    expectedPythonType);
		checkPrimitiveType( getType(neutralType, PRIMITIVE_TYPE + UNSIGNED_TYPE ), expectedPythonType);
		checkPrimitiveType( getType(neutralType, OBJECT_TYPE + NOT_NULL ),         expectedPythonType);
	}
	public void genericObjectTypeTest(String neutralType, String expectedPythonType, String expectedPythonImport) {
		println("--- ");
		checkObjectType( getType(neutralType, NONE ),             expectedPythonType, expectedPythonImport);
		checkObjectType( getType(neutralType, NOT_NULL ),         expectedPythonType, expectedPythonImport);
		checkObjectType( getType(neutralType, PRIMITIVE_TYPE ),   expectedPythonType, expectedPythonImport);
		checkObjectType( getType(neutralType, OBJECT_TYPE ),      expectedPythonType, expectedPythonImport);
		checkObjectType( getType(neutralType, UNSIGNED_TYPE ),    expectedPythonType, expectedPythonImport);
		checkObjectType( getType(neutralType, PRIMITIVE_TYPE + UNSIGNED_TYPE ), expectedPythonType, expectedPythonImport);
		checkObjectType( getType(neutralType, OBJECT_TYPE + NOT_NULL ),         expectedPythonType, expectedPythonImport);
	}
	
	@Test
	public void testString() {
		genericPrimitiveTypeTest(NeutralType.STRING, "str");
	}
	@Test
	public void testBoolean() {
		genericPrimitiveTypeTest(NeutralType.BOOLEAN, "bool");
	}
	@Test
	public void testByte() {
		genericPrimitiveTypeTest(NeutralType.BYTE, "int");
	}
	@Test
	public void testShort() {
		genericPrimitiveTypeTest(NeutralType.SHORT, "int");
	}
	@Test
	public void testInteger() {
		genericPrimitiveTypeTest(NeutralType.INTEGER, "int");
	}
	@Test
	public void testLong() {
		genericPrimitiveTypeTest(NeutralType.LONG, "int");
	}
	@Test
	public void testDecimal() {
		genericObjectTypeTest(NeutralType.DECIMAL, "Decimal", "from decimal import Decimal");
	}
	@Test
	public void testDate() {
		genericObjectTypeTest(NeutralType.DATE, "date", "from datetime import date");
	}
	@Test
	public void testTime() {
		genericObjectTypeTest(NeutralType.TIME, "time", "from datetime import time");
	}
	@Test
	public void testTimetz() { // ver 4.3.0
		genericObjectTypeTest(NeutralType.TIMETZ, "time", "from datetime import time");
	}
	private static final String PYTHON_DATETIME = "datetime";
	private static final String PYTHON_DATETIME_IMPORT = "from datetime import datetime";
	@Test
	public void testTimestamp() {
		genericObjectTypeTest(NeutralType.TIMESTAMP, PYTHON_DATETIME, PYTHON_DATETIME_IMPORT);
	}
	@Test 
	public void testDatetime() { // ver 4.3.0
		genericObjectTypeTest(NeutralType.DATETIME, PYTHON_DATETIME, PYTHON_DATETIME_IMPORT);
	}
	@Test
	public void testDatetimetz() { // ver 4.3.0
		genericObjectTypeTest(NeutralType.DATETIMETZ, PYTHON_DATETIME, PYTHON_DATETIME_IMPORT);
	}
	@Test
	public void testUUID() { // ver 4.3.0
		genericObjectTypeTest(NeutralType.UUID, "UUID", "from uuid import UUID");
	}
	@Test
	public void testBinary() {
		genericPrimitiveTypeTest(NeutralType.BINARY, "bytes");
	}

	@Test
	public void testDefaultCollectionType() {
		println("--- ");
		TypeConverter typeConverter = getTypeConverter();
		assertNull(typeConverter.getSpecificCollectionType());
		assertEquals("list", typeConverter.getCollectionType());
		assertEquals("list[str]", typeConverter.getCollectionType("str"));
		assertEquals("list[Foo]", typeConverter.getCollectionType("Foo"));
	}

	@Test
	public void testSpecificCollectionType() throws GeneratorException {
		println("--- ");
		EnvInContext env = new EnvInContext();
		env.setLanguage(getLanguageName());
		env.setCollectionType("set");
		TypeConverter typeConverter = env.getTypeConverter();
		
		assertNotNull(typeConverter.getSpecificCollectionType());
		assertEquals("set", typeConverter.getSpecificCollectionType());
		assertEquals("set", typeConverter.getCollectionType());
		assertEquals("set[str]", typeConverter.getCollectionType("str"));
		assertEquals("set[Foo]", typeConverter.getCollectionType("Foo"));
	}
	
}
