package org.telosys.tools.generator.languages.types;

public abstract class AbstractTypeTest {

	protected AbstractTypeTest() {
		super();
	}

	protected abstract TypeConverter getTypeConverter();

	protected LanguageType getType(String neutralType, int typeInfo ) {
		AttributeTypeInfo attributeTypeInfo = new AttributeTypeInfoForTest(neutralType, typeInfo);
		System.out.println("AttributeTypeInfo : " + attributeTypeInfo);
		return getType(attributeTypeInfo);
	}

	protected LanguageType getType(AttributeTypeInfo typeInfo ) {
		System.out.println( typeInfo + " --> " + typeInfo );
		return getTypeConverter().getType(typeInfo);
	}

}
