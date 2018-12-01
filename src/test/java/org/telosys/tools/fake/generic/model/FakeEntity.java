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
package org.telosys.tools.fake.generic.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.telosys.tools.generic.model.Attribute;
import org.telosys.tools.generic.model.Entity;
import org.telosys.tools.generic.model.ForeignKey;
import org.telosys.tools.generic.model.Link;

public class FakeEntity implements Entity {

	private String className;
	private String packageName;
	
	private List<Attribute> attributes = new ArrayList<Attribute>();
	
	private List<ForeignKey> databaseForeignKeys = new ArrayList<ForeignKey>();
	
	private String databaseTable;
	private String databaseCatalog;
	private String databaseSchema;
	private String databaseType;
	private String databaseComment = "";
	
	
	private String fullName;
	private List<Link> links = new ArrayList<Link>();
//	private String _package;
	private Boolean tableType;
	private Boolean viewType;

	public Attribute getAttributeByName(String name) {
		for(Attribute attribute : getAttributes()) {
			if(name.equals(attribute.getName())) {
				return attribute;
			}
		}
		return null;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	public List<Attribute> getAttributes() {
		return attributes;
	}
	public String getClassName() {
		return className;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}
	public String getDatabaseCatalog() {
		return databaseCatalog;
	}
	public void setDatabaseCatalog(String databaseCatalog) {
		this.databaseCatalog = databaseCatalog;
	}
	public List<ForeignKey> getDatabaseForeignKeys() {
		return databaseForeignKeys;
	}
	public void setDatabaseForeignKeys(List<ForeignKey> databaseForeignKeys) {
		this.databaseForeignKeys = databaseForeignKeys;
	}
	public String getDatabaseSchema() {
		return databaseSchema;
	}
	public void setDatabaseSchema(String databaseSchema) {
		this.databaseSchema = databaseSchema;
	}
	public String getDatabaseTable() {
		return databaseTable;
	}
	public void setDatabaseTable(String databaseTable) {
		this.databaseTable = databaseTable;
	}
	public String getDatabaseType() {
		return databaseType;
	}
	public void setDatabaseType(String databaseType) {
		this.databaseType = databaseType;
	}
	//--------------------------------------------------------------------------
	@Override
	public String getDatabaseComment() {
		return this.databaseComment;
	}
	
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public List<Link> getLinks() {
		return links;
	}
	public void setLinks(List<Link> links) {
		this.links = links;
	}
//	public String getPackage() {
//		return _package;
//	}
//	public void setPackage(String _package) {
//		this._package = _package;
//	}
	public Boolean isTableType() {
		return tableType;
	}
	public void setTableType(Boolean tableType) {
		this.tableType = tableType;
	}
	public Boolean isViewType() {
		return viewType;
	}
	public void setViewType(Boolean viewType) {
		this.viewType = viewType;
	}
	
	/**
	 * Returns true if the entity has at least one attribute with "@Id"
	 * @return
	 */
	public boolean hasId() {
		for ( Attribute attribute : this.attributes ) {
			if ( attribute.isKeyElement() ) {
				return true ;
			}
		}
		return false ; // No attribute with "@Id"
	}
	
	/**
	 * Replaces the attribute identified by the given name by another one
	 * @param name
	 * @param newAttribute
	 * @return
	 */
	public Attribute replaceAttribute(String name, Attribute newAttribute) {
		List<Attribute> list = this.attributes  ;
		for ( int index = 0 ; index < list.size() ; index++ ) {
			Attribute attribute = list.get(index);
			if ( name.equals(attribute.getName()) ) { // Found
				list.set(index, newAttribute); // Replace
				return attribute ;
			}
		}
		return null;
	}

	@Override
	public List<String> getWarnings() {
		List<String> warnings = new LinkedList<String>() ;
		if ( hasId() == false ) {
			warnings.add("No ID");
		}
		return warnings;
	}
	
}
