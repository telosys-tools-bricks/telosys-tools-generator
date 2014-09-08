/**
 *  Copyright (C) 2008-2013  Telosys project org. ( http://www.telosys.org/ )
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
package org.telosys.tools.generator.context.doc;

public class MethodParameter {

	private final String type ;
	private final String name ;
	private final String description ;
	
	public MethodParameter( String type, String name, String description) {
		super();
		this.type = type;
		this.name = name;
		this.description = description;
	}

	public MethodParameter( String type, String doc) {
		super();
		this.type = type;
		String[] parts = doc.split(":");
		if ( parts.length == 1 ) {
			this.name = parts[0].trim();
			this.description = "";
		}
		else if ( parts.length > 1 ) {
			this.name = parts[0].trim();
			this.description = parts[1].trim();
		}
		else {
			this.name = "?";
			this.description = "";
		}
	}

	public MethodParameter( String type ) {
		super();
		this.type = type;
		this.name = "?";
		this.description = "";
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
	
	
}
