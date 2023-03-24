package org.telosys.tools.generator.languages;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TargetLanguageProviderTest  {

	private void check(String languageName) {
		assertTrue(TargetLanguageProvider.isDefinedLanguage(languageName));
		assertNotNull( TargetLanguageProvider.getTargetLanguage(languageName) );
		assertNotNull( TargetLanguageProvider.getTargetLanguage(languageName).getTypeConverter() );
		assertNotNull( TargetLanguageProvider.getTargetLanguage(languageName).getLiteralValuesProvider() );
		assertNotNull( TargetLanguageProvider.getTypeConverter(languageName) );
		assertNotNull( TargetLanguageProvider.getLiteralValuesProvider(languageName) );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testGetTargetLanguageWithNull() {
		TargetLanguageProvider.getTargetLanguage(null);
	}
	@Test(expected=IllegalArgumentException.class)
	public void testGetTypeConverterWithNull() {
		TargetLanguageProvider.getTypeConverter(null);
	}
	@Test(expected=IllegalArgumentException.class)
	public void testGetLiteralValuesProviderWithNull() {
		TargetLanguageProvider.getLiteralValuesProvider(null);
	}

	@Test
	public void testCPlusPlus() {
		check("C++");
		check(" c++");
	}

	@Test
	public void testCSharp() {
		check("C#");
		check(" c#");
	}

	@Test
	public void testGo() {
		check("Go");
		check(" go");
		check("GO ");
	}

	@Test
	public void testJava() {
		check("Java");
		check(" java");
		check("JAVA  ");
	}

	@Test
	public void testJavaScript() {
		check("JavaScript");
		check(" javascript");
		check("JAVASCRIPT  ");
	}

	@Test
	public void testKotlin() {
		check("KOTLIN");
		check(" Kotlin");
		check("kotlin ");
	}

	@Test
	public void testPHP() {
		check("PHP");
		check(" Php");
		check("php ");
	}

	@Test
	public void testPython() {
		check("PYTHON");
		check(" Python");
		check("python ");
	}

	@Test
	public void testScala() {
		check("SCALA");
		check(" Scala");
		check("scala ");
	}

	@Test
	public void testTypeScript() {
		check("TypeScript");
		check(" typescript");
		check("TYPESCRIPT  ");
	}
}
