package org.telosys.tools.generator.languages.literals;

import org.telosys.tools.generator.languages.types.AttributeTypeConst;
import org.telosys.tools.generator.languages.types.AttributeTypeInfo;
import org.telosys.tools.generator.languages.types.AttributeTypeInfoForTest;
import org.telosys.tools.generator.languages.types.LanguageType;
import org.telosys.tools.generator.languages.types.TypeConverter;

public abstract class AbstractLiteralsTest {

	protected AbstractLiteralsTest() {
		super();
	}

	protected abstract TypeConverter getTypeConverter();
	
	protected abstract LiteralValuesProvider getLiteralValuesProvider();

	protected void println(String s) {
		System.out.println(s);
	}
	
	/**
	 * Returns the target language type for the given AttributeTypeInfo
	 * @param attributeTypeInfo
	 * @return
	 */
	protected LanguageType getLanguageType(AttributeTypeInfo attributeTypeInfo) {
		println( attributeTypeInfo + " --> " + attributeTypeInfo );
		return getTypeConverter().getType(attributeTypeInfo);
	}
	
	/**
	 * Returns the target language type for the given neutral type with a combination of NOT_NULL, PRIMITIVE_TYPE, OBJECT_TYPE, UNSIGNED_TYPE
	 * @param neutralType
	 * @param typeInfo
	 * @return
	 */
	protected LanguageType getLanguageType(String neutralType, int typeInfo) {
		AttributeTypeInfo attributeTypeInfo = new AttributeTypeInfoForTest(neutralType, typeInfo);
		println("AttributeTypeInfo : " + attributeTypeInfo);
		return getLanguageType(attributeTypeInfo);
	}

	/**
	 * Returns the target language type for the given neutral type (without NOT_NULL, PRIMITIVE_TYPE, OBJECT_TYPE, UNSIGNED_TYPE) 
	 * @param neutralType
	 * @return
	 */
	protected LanguageType getLanguageType(String neutralType) {
		AttributeTypeInfo attributeTypeInfo = new AttributeTypeInfoForTest(neutralType, AttributeTypeConst.NONE);
		println("AttributeTypeInfo : " + attributeTypeInfo);
		return getLanguageType(attributeTypeInfo);
	}

}
