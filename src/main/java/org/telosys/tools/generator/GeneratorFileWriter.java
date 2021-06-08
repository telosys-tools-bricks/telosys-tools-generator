/**
 *  Copyright (C) 2008-2017  Telosys project org. ( http://www.telosys.org/ )
 *
 *  Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.gnu.org/licenses/lgpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.telosys.tools.generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

/**
 * The file writer used by the generator to write generated files
 * 
 * @author Laurent Guerin
 *  
 */
public class GeneratorFileWriter {

	/**
	 * Private constructor 
	 */
	private GeneratorFileWriter()  { 
	}

	protected static void writeFileUTF8(String content, File file) throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
		Writer writer = new BufferedWriter(outputStreamWriter);
		try {
			writer.write(content);
		} finally {
			writer.close();
		}
	}
	
	/**
	 * Writes the given content in the given file with UTF-8 charset
	 * @param content
	 * @param file
	 * @throws GeneratorException
	 */
	public static void writeGenerationResult(String content, File file) throws GeneratorException {
		try {
			writeFileUTF8(content, file);
		} catch (IOException e) {
			throw new GeneratorException("Cannot write result in file '"+ file.toString() +"'", e);
		}
	}
	
}