package org.telosys.tools.generator.languages.types;

import org.telosys.tools.commons.exception.TelosysRuntimeException;
import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generator.context.EnvInContext;

public abstract class AbstractTypeTest {

	/**
	 * Constructor
	 */
	protected AbstractTypeTest() {
		super();
	}

	/**
	 * Prints the given message
	 * @param s
	 */
	protected void println(String s) {
		System.out.println(s);
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
	 * Returns the TypeConverter for the current language defined in $env
	 * @return
	 */
	protected final TypeConverter getTypeConverter() {
		return getEnv().getTypeConverter();
	}


	protected LanguageType getType(String neutralType, int typeInfo ) {
		return getType(this.getTypeConverter(), new AttributeTypeInfoForTest(neutralType, typeInfo));
	}
	protected LanguageType getType(EnvInContext env, String neutralType, int typeInfo ) {
		return getType(env, new AttributeTypeInfoForTest(neutralType, typeInfo));
	}

	protected LanguageType getType(AttributeTypeInfo typeInfo ) {
//		LanguageType languageType = getTypeConverter().getType(typeInfo);
//		println( "AttributeTypeInfo : " + typeInfo + " --> " + languageType );
		return getType(this.getTypeConverter(), typeInfo );
	}
	protected LanguageType getType(EnvInContext env, AttributeTypeInfo typeInfo ) {		
		return getType(env.getTypeConverter(), typeInfo );
	}
	protected LanguageType getType(TypeConverter typeConverter, AttributeTypeInfo typeInfo ) {		
		LanguageType languageType = typeConverter.getType(typeInfo);
		println( "AttributeTypeInfo : " + typeInfo + " --> " + languageType );
		return languageType;
	}

}
