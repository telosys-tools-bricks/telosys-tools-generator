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

import org.telosys.tools.generator.context.Target;

/**
 * Generation task result 
 * 
 * @author L. Guerin
 *
 */
public class GenerationTaskResult {

	private int numberOfResourcesCopied ;
	private int numberOfFilesGenerated ;
	private int numberOfGenerationErrors ;
		
	//--------------------------------------------------------------------------------------
	/**
	 * Default constructor with 0 for all values
	 */
	protected GenerationTaskResult() {
		super();
		this.numberOfResourcesCopied = 0;
		this.numberOfFilesGenerated  = 0;
	}

	//--------------------------------------------------------------------------------------
	protected void setNumberOfResourcesCopied(int n) {
		numberOfResourcesCopied = n;
	}
	public int getNumberOfResourcesCopied() {
		return numberOfResourcesCopied;
	}

	//--------------------------------------------------------------------------------------
	protected void incrementNumberOfFilesGenerated() {
		numberOfFilesGenerated++;
	}
	public int getNumberOfFilesGenerated() {
		return numberOfFilesGenerated;
	}
	
	//--------------------------------------------------------------------------------------
	protected void addGenerationError(Target target) {
		numberOfGenerationErrors++;
		//target.
	}
	public int getNumberOfGenerationErrors() {
		return numberOfGenerationErrors;
	}
	
}
