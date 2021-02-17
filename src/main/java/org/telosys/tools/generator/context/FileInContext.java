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
import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.names.ContextName;

//-------------------------------------------------------------------------------------
@VelocityObject(
		contextName=ContextName.FILE,
		text = { 
				"This object allows to work with a file located on the filesystem ",
				"Each file instance is created with $fn.file(..) or $fn.fileFromXxx(..) ",
				"It can be any type of filesystem item (file or directory)",
				""
		},
		since = "3.3.0",
		example= {
				"",
				"#set( $file = $fn.fileFromBundle(\"foo.txt\") )",
				"",
				"#if( $file.exists() && $file.isFile() )",
				"#set($content = $file.loadContent() )",
				"#end",
				"",
				"#foreach ( $line in $file.loadLines() )",
				" > $line",
				"#end"
		}		
)
//-------------------------------------------------------------------------------------
public class FileInContext {

	private final File file ;
	
	/**
	 * Constructor
	 * @param file
	 */
	public FileInContext(File file) {
		this.file = file ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(text={
		"Tests whether the file (or directory) exists"
		},
		example = {
			"#if( $file.exists() ) ",
			"...  ",
			"#end"
		},
		since = "3.3.0"
		)
	public boolean exists() {
		return file.exists();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(text={
		"Tests whether the file is a directory"
		},
		example = {
			"#if( $file.isDirectory() ) ",
			"...  ",
			"#end"
		},
		since = "3.3.0"
		)
	public boolean isDirectory() {
		return file.isDirectory();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(text={
		"Tests whether the file is a 'normal file'"
		},
		example = {
			"#if( $file.isFile() ) ",
			"...  ",
			"#end"
		},
		since = "3.3.0"
		)
	public boolean isFile() {
		return file.isFile();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(text={
		"Tests whether the file is a 'hidden file'",
		"The exact definition of 'hidden' is system-dependent"
		},
		example = {
			"#if( $file.isHidden() ) ",
			"...  ",
			"#end"
		},
		since = "3.3.0"
		)
	public boolean isHidden() {
		return file.isHidden();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(text={
		"Returns the length of the file ",
		"The return value is 0 if the file is not a 'normal file' (eg directory)"
		},
		example = {
			"#if( $file.length > 0 ) ",
			"...  ",
			"#end"
		},
		since = "3.3.0"
		)
	public long getLength() {
		if ( file.isFile() ) {
			return file.length();
		}
		else {
			return 0;
		}
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(text={
		"Returns the name of the file or directory.",
		"This is just the last name in the absolute pathname."
		},
		example = {
			"$file.name"
		},
		since = "3.3.0"
		)
	public String getName() {
		return file.getName();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(text={
		"Returns the path for the file or directory."
		},
		example = {
			"$file.path"
		},
		since = "3.3.0"
		)
	public String getPath() {
		return file.getPath();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(text={
		"Returns the pathname string for the file's parent",
		"(or a void string if none)"
		},
		example = {
			"$file.parent"
		},
		since = "3.3.0"
		)
	public String getParent() {
		String parent = file.getParent(); // NB: can be null
		return parent != null ? parent : "" ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(text={
		"Returns the absolute pathname string."
		},
		example = {
			"$file.absolutePath"
		},
		since = "3.3.0"
		)
	public String getAbsolutePath() {
		return file.getAbsolutePath();
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(text={
		"Returns the canonical pathname string.",
		"A canonical pathname is both absolute and unique.",
		"The precise definition of canonical form is system-dependent"
		},
		example = {
			"$file.canonicalPath"
		},
		since = "3.3.0"
		)
	public String getCanonicalPath() throws Exception {
		try {
			return file.getCanonicalPath();
		} catch (IOException e) {
			// IOException possible because the construction of the canonical pathname 
			// may require filesystem queries
			throw e;
		}
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(text={
		"Loads all the lines of the file ",
		"Returns a list of strings"
		},
		example = {
				"#foreach ( $line in $file.loadLines() )",
				" > $line",
				"#end"		
		},
		since = "3.3.0"
		)
	public List<String> loadLines() throws Exception {
		return readAllLines();
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(text={
		"Same as loadLines() but ignore the first N lines"
		},
		parameters = { "n : number of lines to ignore (at the beginning of the file)" },			
		example = {
				"#foreach ( $line in $file.loadLines(2) )",
				" > $line",
				"#end"		
		},
		since = "3.3.0"
		)
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
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(text={
		"Loads all the lines of the file ",
		"Returns a single string containing all the lines"
		},
		example = {
				"$file.loadContent()",
				"#set($content = $file.loadContent() )",
				""		
		},
		since = "3.3.0"
		)
	public String loadContent() throws Exception {
		return linesToString(loadLines());
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(text={
		"Same as loadContent() but ignore the first N lines"
		},
		parameters = { "n : number of lines to ignore (at the beginning of the file)" },			
		example = {
				"$file.loadContent(1)",
				"#set($content = $file.loadContent(3) )",
				""		
		},
		since = "3.3.0"
		)
	public String loadContent(int numberOfLinesToIgnore) throws Exception {
		return linesToString(loadLines(numberOfLinesToIgnore));
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(text={
		"Loads values (split lines) from the file ",
		"Values are the result after splitting each line according a given separator",
		"Returns a list of values for each line (list of lists)"
		},
		parameters = { 
				"separator : character to use as separator" },			
		example = {
				"#set( $csv = $file.loadValues(\";\") )",
				"#foreach ( $line in $csv )",
				" > #foreach ( $v in $line ) [$v] #end",
				"",
				"#end",
				""		
		},
		since = "3.3.0"
		)
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
	//-------------------------------------------------------------------------------------
	@VelocityMethod(text={
		"Same as loadValues(separator) but ignore the first N lines"
		},
		parameters = { 
				"separator : character to use as separator",
				"n : number of lines to ignore (at the beginning of the file)" },			
		example = {
				"$file.loadValues(\";\", 2) )",
				""		
		},
		since = "3.3.0"
		)
	public List<List<String>> loadValues(String separator, int numberOfLinesToIgnore) throws Exception {
		List<String> lines = loadLines(numberOfLinesToIgnore);
		return splitLines(lines, separator);
	}
	
	//----------------------------------------------------------------------------------------
	// private methods
	//----------------------------------------------------------------------------------------
	
	private List<String> readAllLines() throws Exception {
		if ( ! file.exists() ) {
			throw new Exception("Read file error (file not found) : " + file.getName());
		}
		if ( ! file.isFile() ) {
			throw new Exception("Read file error (not a file) : " + file.getName());
		}
		try {
			// Read all lines from a file.  
			// Bytes from the file are decoded into characters using the UTF-8 charset.
			return Files.readAllLines(file.toPath());
		} catch (IOException e) {
			throw new Exception("Read file error (IOException) : " 
					+ e.getMessage() + " : " + file.getName());
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
