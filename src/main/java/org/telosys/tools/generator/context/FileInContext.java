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
package org.telosys.tools.generator.context;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.telosys.tools.commons.StrUtil;

public class FileInContext {

	private final File file ;
	
	/**
	 * Constructor
	 * @param file
	 */
	public FileInContext(File file) {
		this.file = file ;
	}

	/**
	 * Tests whether the file (or directory) exists
	 * @return
	 */
	public boolean exists() {
		return file.exists();
	}

	/**
	 * Tests whether the file is a directory. 
	 * @return
	 */
	public boolean isDirectory() {
		return file.isDirectory();
	}

	/**
	 * Tests whether the file is a 'normal file'.
	 * @return
	 */
	public boolean isFile() {
		return file.isFile();
	}

	/**
	 * Tests whether the file is a 'hidden file'. <br>
	 * The exact definition of hidden is system-dependent.
	 * @return
	 */
	public boolean isHidden() {
		return file.isHidden();
	}

	/**
	 * Returns the length of the file denoted by this abstract pathname. <br>
	 * The return value is unspecified if this pathname denotes a directory. 
	 * @return
	 */
	public long length() {
		return file.length();
	}

	/**
	 * Loads all the lines of the current file 
	 * @return
	 * @throws Exception
	 */
	public List<String> loadLines() throws Exception {
		return readAllLines();
	}
	
	/**
	 * Loads all the lines of the current file except the first N lines
	 * @param numberOfLinesToIgnore
	 * @return
	 * @throws Exception
	 */
	public List<String> loadLines(int numberOfLinesToIgnore) throws Exception {
		List<String> allLines = readAllLines();
		List<String> lines = new LinkedList<>();
		int n = 0 ;
		for ( String s : allLines ) {
			n++;
			if ( n > numberOfLinesToIgnore ) {
				lines.add(s);
			}
		}
		return lines;
	}
	
	/**
	 * Loads all the content of the current file 
	 * @return
	 * @throws Exception
	 */
	public String loadContent() throws Exception {
		return linesToString(loadLines());
	}
	
	/**
	 * Loads all the content of the current file except the first N lines
	 * @param numberOfLinesToIgnore
	 * @return
	 * @throws Exception
	 */
	public String loadContent(int numberOfLinesToIgnore) throws Exception {
		return linesToString(loadLines(numberOfLinesToIgnore));
	}
	
	/**
	 * Loads all the values of the current file 
	 * @param separator character to be used as values separator
	 * @return
	 * @throws Exception
	 */
	public List<List<String>> loadValues(String separator) throws Exception {
		List<String> lines = loadLines();
		return splitLines(lines, separator);
	}
	
	/**
	 * Loads all the values of the current file except the first N lines
	 * @param separator character to be used as values separator
	 * @param numberOfLinesToIgnore
	 * @return
	 * @throws Exception
	 */
	public List<List<String>> loadValues(String separator, int numberOfLinesToIgnore) throws Exception {
		List<String> lines = loadLines(numberOfLinesToIgnore);
		return splitLines(lines, separator);
	}
	
	//----------------------------------------------------------------------------------------
	
//	private void checkFile() throws Exception {
//		if ( ! file.exists() ) {
//			throw new Exception("Load file error (file not found) : " + file.getAbsolutePath());
//		}
//		if ( ! file.isFile() ) {
//			throw new Exception("Load file error (not a file) : " + file.getAbsolutePath());
//		}
//	}

	private List<String> readAllLines() throws Exception {
		if ( ! file.exists() ) {
			throw new Exception("Load file error (file not found) : " + file.getAbsolutePath());
		}
		if ( ! file.isFile() ) {
			throw new Exception("Load file error (not a file) : " + file.getAbsolutePath());
		}
		try {
			// Read all lines from a file.  
			// Bytes from the file are decoded into characters using the UTF-8 charset.
			return Files.readAllLines(file.toPath());
		} catch (IOException e) {
			throw new Exception("Load file error (IOException) : " + file.getAbsolutePath());
		}
	}
	
	private String linesToString(List<String> lines) {
		StringBuilder sb = new StringBuilder();
		for ( String s : lines ) {
			sb.append(s);
			sb.append("\n");
		}
		return sb.toString();
	}
	
	private List<List<String>> splitLines(List<String> lines, String separator) throws Exception {
		char sepChar = 0;
		if ( separator != null && separator.length() > 0 ) {
			sepChar = separator.charAt(0);
			List<List<String>> linesOfValues = new LinkedList<>();
			for ( String line : lines ) {
				String[] parts = StrUtil.split(line, sepChar);
				linesOfValues.add(Arrays.asList(parts));
			}
			return linesOfValues;
		}
		else {
			throw new Exception("Invalid separator '" + separator + "'");
		}
	}
	
}
