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
package org.telosys.tools.generator.context.tools;

import org.telosys.tools.commons.StrUtil;

/**
 * Tool to build list
 * 
 * @author Laurent Guerin
 *
 */
public class ListBuilder {
	
	private final StringBuilder sb ;
	private final String separator ;
	private int itemsCount = 0;
	
	/**
	 * Constructor
	 * @param separator
	 */
	public ListBuilder(String separator) {
		super();
		this.sb = new StringBuilder() ;
		this.separator = separator ;
		this.itemsCount = 0 ;
	}

	/**
	 * Constructor
	 */
	public ListBuilder() {
		this(", ");
	}
	
	/**
	 * Appends the given item to the list if not null or void
	 * add a separator before item if not first item 
	 * @param item
	 */
	public void append(String item) {
		if ( ! StrUtil.nullOrVoid( item ) ) {
			if ( itemsCount > 0 ) {
				sb.append(separator);
			}
			sb.append(item);
			itemsCount++ ;
		}
	}
	
	/**
	 * Returns true if the list is empty
	 * @return
	 */
	public boolean isEmpty() {
		return itemsCount == 0 ;
	}
	
	@Override
	public String toString() {
		return sb.toString();
	}

}
