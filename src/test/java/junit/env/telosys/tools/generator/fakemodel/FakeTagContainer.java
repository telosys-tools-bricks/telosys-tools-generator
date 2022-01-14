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

import java.util.HashMap;
import java.util.Map;

import org.telosys.tools.generic.model.TagContainer;

public class FakeTagContainer implements TagContainer {
	
    private static final String VOID_STRING  = "" ;

	private final Map<String, Tag> tagsMap ;
	
	public FakeTagContainer() {
		this.tagsMap = new HashMap<>();
	}
	
	/**
	 * Add the given tag to the tags collection 
	 * @param tag
	 */
	public void addTag(Tag tag) {
		if ( this.tagsMap.containsKey(tag.getName()) ) {
			// ERROR : tag already defined
			throw new IllegalStateException("Duplicate tag");
		}
		else {
			tagsMap.put(tag.getName(), tag);
		}
	}
	
	/**
	 * Returns TRUE if the tag exists
	 * @param tagName
	 * @return
	 */
	@Override
	public boolean containsTag(String tagName) {
		return this.tagsMap.containsKey(tagName);
	}
	
	@Override
	public int size() {
		return this.tagsMap.size();
	}

	@Override
	public boolean isEmpty() {
		return this.tagsMap.isEmpty();
	}

	/**
	 * Returns the value of the given tag name <br>
	 * If the tag is undefined or has no value, the returned value is an empty string.
	 * 
	 * @param tagName
	 * @return
	 */
	@Override
	public String getTagValue(String tagName) {
		return getTagValue(tagName, VOID_STRING);
	}

	/**
	 * Returns the value of the given tag name <br>
	 * If the tag is undefined or has no value, the given default value is returned.
	 * @param tagName
	 * @param defaultValue
	 * @return
	 */
	@Override
	public String getTagValue(String tagName, String defaultValue) {
		String value = getOptionalTagValue(tagName);
		if ( value != null ) {
			// tag value found
			return value ;
		}
		else {
			// no tag or no value 
			return defaultValue;
		}
	}

	@Override
	public int getTagValueAsInt(String tagName, int defaultValue) {
		String value = getOptionalTagValue(tagName);
		if (value != null) {
			// tag value found
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException e) {
				// cannot convert to int => use default value
				return defaultValue;
			}
		} else {
			// tag is undefined
			return defaultValue;
		}
	}

	@Override
	public boolean getTagValueAsBoolean(String tagName, boolean defaultValue) {
		String value = getOptionalTagValue(tagName);
		if (value != null) {
			// tag value found
			return "true".equalsIgnoreCase(value);
		} else {
			// tag is undefined
			return defaultValue;
		}
	}

	/**
	 * Returns the value of the given tag name, or null if no tag or no parameter
	 * @param tagName
	 * @return
	 */
	private String getOptionalTagValue(String tagName) {
		Tag tag = this.tagsMap.get(tagName);
		if ( tag != null ) {
			// tag exists 
			return tag.getParameter();
		}
		else {
			// tag is undefined 
			return null;
		}
	}
}
