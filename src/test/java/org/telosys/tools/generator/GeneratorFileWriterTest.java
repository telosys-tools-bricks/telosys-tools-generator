package org.telosys.tools.generator;

import java.io.File;

import org.junit.Test;

import junit.env.telosys.tools.generator.TestsEnv;

public class GeneratorFileWriterTest {
	
	private File getFile(String fileName) {
		String filePath = "proj-utf8/generated-files/" + fileName ;
		return TestsEnv.buildTestFileOrFolder(filePath);
	}

	@Test
	public void testFile1() throws GeneratorException {
		String content = "abcdef\n UTF-8 characters : à é ê è ù ö ï";
		File file = getFile("file-utf8.txt");
		GeneratorFileWriter.writeGenerationResult(content, file);
	}

}
