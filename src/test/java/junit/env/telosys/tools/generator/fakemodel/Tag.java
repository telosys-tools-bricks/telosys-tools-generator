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
package junit.env.telosys.tools.generator.fakemodel;

public class Tag {

	private final String name;
	private final String parameter;

    /**
     * Constructor : tag with parameter
     * @param name
     * @param param
     */
    public Tag(String name, String param) {
    	super();
		this.name = name;
		this.parameter = param;
    }

    /**
     * Constructor : tag without parameter
     * @param name
     * @param param
     */
    public Tag(String name) {
    	super();
		this.name = name;
		this.parameter = null;
    }

	//-------------------------------------------------------------------------
	// Getters
	//-------------------------------------------------------------------------

    /**
	 * Returns the tag name ( without '#' )
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns true if the tag has a parameter
	 * 
	 * @return
	 */
	public boolean hasParameter() {
		return this.parameter != null;
	}

	/**
	 * Returns the tag parameter (can be null)
	 * 
	 * @return
	 */
	public String getParameter() {
		return parameter;
	}
    
    @Override
    public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("#");
		sb.append(name);
		if (this.parameter != null) {
			sb.append("(");
			sb.append(parameter);
			sb.append(")");
		}
		return sb.toString();
    }
}
