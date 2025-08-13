package org.telosys.tools.generator.languages.literals;

import org.junit.Test;
import org.telosys.tools.commons.exception.TelosysRuntimeException;
import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generator.context.EnvInContext;
import org.telosys.tools.generator.languages.types.AttributeTypeConst;
import org.telosys.tools.generator.languages.types.AttributeTypeInfo;
import org.telosys.tools.generator.languages.types.AttributeTypeInfoForTest;
import org.telosys.tools.generator.languages.types.LanguageType;
import org.telosys.tools.generic.model.types.NeutralType;

public abstract class AbstractLiteralsTest {

	/**
	 * Constructor
	 */
	protected AbstractLiteralsTest() {
		super();
	}

	/**
	 * Prints the given message
	 * @param s
	 */
	protected void println(String s) {
		// System.out.println(s);
	}	

	/**
	 * Provides the target language name
	 * @return
	 */
	protected abstract String getLanguageName();

	/**
	 * Returns an instance of EnvInContext ($env) with the expected target language
	 * @return
	 */
	protected EnvInContext getEnv() {
		EnvInContext env = new EnvInContext();
		try {
			env.setLanguage(getLanguageName());
		} catch (GeneratorException e) {
			throw new TelosysRuntimeException("Invalid language name", e);
		}
		return env ;
	}

	/**
	 * Returns the LiteralValuesProvider for the current language defined in $env
	 * @return
	 */
	protected LiteralValuesProvider getLiteralValuesProvider() {
		return getEnv().getLiteralValuesProvider() ;
	}
	
	/**
	 * Returns the target language type for the given AttributeTypeInfo
	 * @param attributeTypeInfo
	 * @return
	 */
	protected LanguageType getLanguageType(AttributeTypeInfo attributeTypeInfo) {
		println( attributeTypeInfo + " --> " + attributeTypeInfo );
		LanguageType lt = getEnv().getTypeConverter().getType(attributeTypeInfo);
		if ( lt != null ) {
			return lt;
		}
		else {
			throw new TelosysRuntimeException("Cannot get type for AttributeTypeInfo : " + attributeTypeInfo);
		}
	}
	
	/**
	 * Returns the target language type for the given neutral type with a combination of NOT_NULL, PRIMITIVE_TYPE, OBJECT_TYPE, UNSIGNED_TYPE
	 * @param neutralType
	 * @param typeInfo
	 * @return
	 */
	protected LanguageType getLanguageType(String neutralType, int typeInfo) {
		AttributeTypeInfo attributeTypeInfo = new AttributeTypeInfoForTest(neutralType, typeInfo);
		return getLanguageType(attributeTypeInfo);
	}

	/**
	 * Returns the target language type for the given neutral type (without NOT_NULL, PRIMITIVE_TYPE, OBJECT_TYPE, UNSIGNED_TYPE) 
	 * @param neutralType
	 * @return
	 */
	protected LanguageType getLanguageType(String neutralType) {
		AttributeTypeInfo attributeTypeInfo = new AttributeTypeInfoForTest(neutralType, AttributeTypeConst.NONE);
		return getLanguageType(attributeTypeInfo);
	}
	
	/**
	 * Returns the target language type for the given neutral type with 'NOT_NULL' info
	 * @param neutralType
	 * @return
	 */
	protected LanguageType getLanguageTypeNotNull(String neutralType) {
		AttributeTypeInfo attributeTypeInfo = new AttributeTypeInfoForTest(neutralType, AttributeTypeConst.NOT_NULL);
		return getLanguageType(attributeTypeInfo);
	}
	
	/**
	 * Test all neutral types for the current target language 
	 */
	@Test
	public void testAllTypes() {
		for ( String neutralType : NeutralType.getAllNeutralTypes() ) {
			// Check if this neutral type is known by the current target language
			LanguageType languageType = getLanguageType(neutralType);
			if ( languageType == null ) {
				throw new TelosysRuntimeException("Cannot get LanguageType for neutral type '" + neutralType + "'");
			}
			// Check if it's possible to get a  neutral type is known by the current target language
			LiteralValue literalValue = getLiteralValuesProvider().generateLiteralValue(languageType, 0, 1);
			if ( literalValue == null ) {
				throw new TelosysRuntimeException("Cannot get LiteralValue for LanguageType '" + languageType + "'");
			}
		}
	}

}
