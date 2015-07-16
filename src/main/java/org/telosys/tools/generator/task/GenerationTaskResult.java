/**
 *  Copyright (C) 2008-2015  Telosys project org. ( http://www.telosys.org/ )
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
package org.telosys.tools.generator.task;

/**
 * Generator task result 
 * 
 * @author L. Guerin
 *
 */
public class GenerationTaskResult {

	private final int numberOfResourcesCopied ;
	private final int numberOfFilesGenerated ;
		
	/**
	 * Constructor
	 * @param numberOfResourcesCopied
	 * @param numberOfFilesGenerated
	 */
	public GenerationTaskResult(int numberOfResourcesCopied,
			int numberOfFilesGenerated) {
		super();
		this.numberOfResourcesCopied = numberOfResourcesCopied;
		this.numberOfFilesGenerated = numberOfFilesGenerated;
	}

	/**
	 * Default constructor with 0 for all values
	 */
	public GenerationTaskResult() {
		super();
		this.numberOfResourcesCopied = 0;
		this.numberOfFilesGenerated  = 0;
	}

	public int getNumberOfResourcesCopied() {
		return numberOfResourcesCopied;
	}

	public int getNumberOfFilesGenerated() {
		return numberOfFilesGenerated;
	}
	
}
