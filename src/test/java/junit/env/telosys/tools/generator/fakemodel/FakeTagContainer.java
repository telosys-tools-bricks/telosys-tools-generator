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
package junit.env.telosys.tools.generator.fakemodel;

import org.telosys.tools.generic.model.TagContainer;

/**
 * Fake attribute for fake entity <br>
 * 
 * @author Laurent Guerin
 *
 */
public class FakeTagContainer implements TagContainer {

	public FakeTagContainer() {
		super();
	}

	@Override
	public boolean containsTag(String arg0) {
		return false;
	}

	@Override
	public String getTagValue(String arg0) {
		return "";
	}

	@Override
	public String getTagValue(String arg0, String arg1) {
		return "";
	}

	@Override
	public boolean getTagValueAsBoolean(String arg0, boolean arg1) {
		return false;
	}

	@Override
	public int getTagValueAsInt(String arg0, int arg1) {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public int size() {
		return 0;
	}

}
