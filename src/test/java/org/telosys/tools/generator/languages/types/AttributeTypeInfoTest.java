package org.telosys.tools.generator.languages.types;

import org.junit.Test;
import org.telosys.tools.generic.model.types.NeutralType;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AttributeTypeInfoTest  {

	@Test
	public void test1() {
		AttributeTypeInfo attributeTypeInfo = new AttributeTypeInfoForTest(NeutralType.STRING, AttributeTypeConst.NONE);
		System.out.println(attributeTypeInfo);
		assertFalse( attributeTypeInfo.isNotNull() );
		assertFalse( attributeTypeInfo.isPrimitiveTypeExpected() );
		assertFalse( attributeTypeInfo.isObjectTypeExpected() );
		assertFalse( attributeTypeInfo.isUnsignedTypeExpected() );
//		assertFalse( attributeTypeInfo.isSqlTypeExpected() );
	}
	@Test
	public void test2() {
		AttributeTypeInfo attributeTypeInfo = new AttributeTypeInfoForTest(NeutralType.STRING, AttributeTypeConst.NOT_NULL);
		System.out.println(attributeTypeInfo);
		assertTrue( attributeTypeInfo.isNotNull() );
		assertFalse( attributeTypeInfo.isPrimitiveTypeExpected() );
		assertFalse( attributeTypeInfo.isObjectTypeExpected() );
		assertFalse( attributeTypeInfo.isUnsignedTypeExpected() );
//		assertFalse( attributeTypeInfo.isSqlTypeExpected() );
	}
	@Test
	public void test3() {
		AttributeTypeInfo attributeTypeInfo = new AttributeTypeInfoForTest(NeutralType.STRING, AttributeTypeConst.PRIMITIVE_TYPE);
		System.out.println(attributeTypeInfo);
		assertFalse( attributeTypeInfo.isNotNull() );
		assertTrue( attributeTypeInfo.isPrimitiveTypeExpected() );
		assertFalse( attributeTypeInfo.isObjectTypeExpected() );
		assertFalse( attributeTypeInfo.isUnsignedTypeExpected() );
//		assertFalse( attributeTypeInfo.isSqlTypeExpected() );
	}
	@Test
	public void test4() {
		AttributeTypeInfo attributeTypeInfo = new AttributeTypeInfoForTest(NeutralType.STRING, AttributeTypeConst.OBJECT_TYPE);
		System.out.println(attributeTypeInfo);
		assertFalse( attributeTypeInfo.isNotNull() );
		assertFalse( attributeTypeInfo.isPrimitiveTypeExpected() );
		assertTrue( attributeTypeInfo.isObjectTypeExpected() );
		assertFalse( attributeTypeInfo.isUnsignedTypeExpected() );
//		assertFalse( attributeTypeInfo.isSqlTypeExpected() );
	}
	@Test
	public void test5() {
		AttributeTypeInfo attributeTypeInfo = new AttributeTypeInfoForTest(NeutralType.STRING, AttributeTypeConst.UNSIGNED_TYPE);
		System.out.println(attributeTypeInfo);
		assertFalse( attributeTypeInfo.isNotNull() );
		assertFalse( attributeTypeInfo.isPrimitiveTypeExpected() );
		assertFalse( attributeTypeInfo.isObjectTypeExpected() );
		assertTrue( attributeTypeInfo.isUnsignedTypeExpected() );
//		assertFalse( attributeTypeInfo.isSqlTypeExpected() );
	}

	@Test
	public void test9() {
		AttributeTypeInfo attributeTypeInfo = new AttributeTypeInfoForTest(NeutralType.STRING, 
//				NOT_NULL + PRIMITIVE_TYPE + OBJECT_TYPE + UNSIGNED_TYPE + SQL_TYPE);
				AttributeTypeConst.NOT_NULL + AttributeTypeConst.PRIMITIVE_TYPE + AttributeTypeConst.OBJECT_TYPE + AttributeTypeConst.UNSIGNED_TYPE );
		System.out.println(attributeTypeInfo);
		assertTrue( attributeTypeInfo.isNotNull() );
		assertTrue( attributeTypeInfo.isPrimitiveTypeExpected() );
		assertTrue( attributeTypeInfo.isObjectTypeExpected() );
		assertTrue( attributeTypeInfo.isUnsignedTypeExpected() );
//		assertTrue( attributeTypeInfo.isSqlTypeExpected() );
	}
	@Test
	public void test10() {
		int typeInfo = 0 ;
		typeInfo += AttributeTypeConst.NOT_NULL ;
		typeInfo += AttributeTypeConst.PRIMITIVE_TYPE ;
		
		AttributeTypeInfo attributeTypeInfo = new AttributeTypeInfoForTest(NeutralType.STRING, typeInfo );
		System.out.println(attributeTypeInfo);
		assertTrue( attributeTypeInfo.isNotNull() );
		assertTrue( attributeTypeInfo.isPrimitiveTypeExpected() );
		assertFalse( attributeTypeInfo.isObjectTypeExpected() );
		assertFalse( attributeTypeInfo.isUnsignedTypeExpected() );
//		assertFalse( attributeTypeInfo.isSqlTypeExpected() );
	}
	@Test
	public void test11() {
		int typeInfo = 0 ;
		typeInfo += AttributeTypeConst.NOT_NULL ;
		typeInfo += AttributeTypeConst.PRIMITIVE_TYPE ;
		typeInfo += AttributeTypeConst.OBJECT_TYPE ;
		typeInfo += AttributeTypeConst.UNSIGNED_TYPE ;
		
		AttributeTypeInfo attributeTypeInfo = new AttributeTypeInfoForTest(NeutralType.STRING, typeInfo );
		System.out.println(attributeTypeInfo);
		assertTrue( attributeTypeInfo.isNotNull() );
		assertTrue( attributeTypeInfo.isPrimitiveTypeExpected() );
		assertTrue( attributeTypeInfo.isObjectTypeExpected() );
		assertTrue( attributeTypeInfo.isUnsignedTypeExpected() );
//		assertFalse( attributeTypeInfo.isSqlTypeExpected() );
	}

	@Test
	public void test12() {
		int typeInfo = 0 ;
		typeInfo += AttributeTypeConst.NOT_NULL ;
		typeInfo += AttributeTypeConst.OBJECT_TYPE ;
		
		AttributeTypeInfo attributeTypeInfo = new AttributeTypeInfoForTest(NeutralType.SHORT, typeInfo );
		System.out.println(attributeTypeInfo);
		assertTrue( attributeTypeInfo.isNotNull() );
		assertFalse( attributeTypeInfo.isPrimitiveTypeExpected() );
		assertTrue( attributeTypeInfo.isObjectTypeExpected() );
		assertFalse( attributeTypeInfo.isUnsignedTypeExpected() );
//		assertFalse( attributeTypeInfo.isSqlTypeExpected() );
	}
}
