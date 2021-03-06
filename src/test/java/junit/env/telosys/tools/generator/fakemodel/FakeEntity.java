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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.telosys.tools.generic.model.Attribute;
import org.telosys.tools.generic.model.Entity;
import org.telosys.tools.generic.model.ForeignKey;
import org.telosys.tools.generic.model.Link;

/**
 * Fake "Entity" for tests
 * 
 * @author Laurent Guerin
 *
 */
public class FakeEntity implements Entity
{
	
	private final String className ; 
	
	private final String databaseTable ;
	
	private String databaseCatalog = null; 
	private String databaseSchema = null;  
	private String databaseType = null; 
	private String databaseComment = null ; 
	
	private List<Attribute> attributes = new ArrayList<>();

	private HashMap<String,ForeignKey> foreignKeys = new HashMap<>() ;
	private HashMap<String,Link>       links       = new HashMap<>() ;

	/**
	 * Constructor 
	 * @param className
	 * @param databaseTable
	 */
	public FakeEntity(String className, String databaseTable) {
		super();
		this.className = className ;
		this.databaseTable = databaseTable;
	}

	//--------------------------------------------------------------------------
	
	@Override
	public String getDatabaseTable() {
		return this.databaseTable;
	}
	
	//--------------------------------------------------------------------------
	@Override
	public String getDatabaseSchema() {
		return this.databaseSchema ;
	}
	/**
	 * Set the database schema of the entity 
	 * @param s
	 */
	public void setDatabaseSchema(String s) {
		this.databaseSchema = s;
	}
	
	//--------------------------------------------------------------------------
	/**
	 * Returns the database type of the entity ( "TABLE", "VIEW", ... )
	 * @return
	 * @since 2.0.7
	 */
	public String getDatabaseType() {
		return databaseType;
	}
	/**
	 * Set the database type of the entity ( "TABLE", "VIEW", ... )
	 * @param s
	 * @since 2.0.7
	 */
	public void setDatabaseType(String s) {
		this.databaseType = s;
	}
	
	@Override
	public Boolean isTableType() { // v 3.0.0
		if ( databaseType != null ) {
			return "TABLE".equalsIgnoreCase( databaseType.trim() ) ;
		}
		return false;
	}

	@Override
	public Boolean isViewType() { // 3.0.0
		if ( databaseType != null ) {
			return "VIEW".equalsIgnoreCase( databaseType.trim() ) ;
		}
		return false;
	}
	
	//--------------------------------------------------------------------------
	@Override
	public String getDatabaseCatalog() {
		return this.databaseCatalog;
	}
	/**
	 * Set the database catalog 
	 * @param s
	 */
	public void setDatabaseCatalog(String s) {
		this.databaseCatalog = s;
	}
	
	//--------------------------------------------------------------------------
	@Override
	public String getDatabaseComment() {
		return databaseComment;
	}

	//--------------------------------------------------------------------------
	@Override
	public String getClassName() {
		return this.className;
	}

	@Override
	public String getPackageName() {
		// No package name in this model (defined in the configuration)
		return null;
	}

	@Override
	public String getFullName() {
		// No package name in this model (defined in the configuration)
		return null;
	}

	//--------------------------------------------------------------------------
	// ATTRIBUTES ( ex COLUMNS )  management
	//--------------------------------------------------------------------------
	
	public void storeAttribute(Attribute attribute) {
		attributes.add(attribute);
	}

	//--------------------------------------------------------------------------
	// COLUMNS exposed as "ATTRIBUTES" of the "GENERIC MODEL" ( v 3.0.0 )
	//--------------------------------------------------------------------------
	@Override
	public List<Attribute> getAttributes() {

		LinkedList<Attribute> attributesList = new LinkedList<>();
		for ( Attribute a : attributes ) {
			attributesList.add(a);
		}
		return attributesList ;
	}

	//--------------------------------------------------------------------------
	// FOREIGN KEYS management
	//--------------------------------------------------------------------------
	public void storeForeignKey(ForeignKey foreignKey) {
		foreignKeys.put(foreignKey.getName(), foreignKey);
	}
	
	//--------------------------------------------------------------------------
	// FOREIGN KEYS exposed as "GENERIC MODEL FOREIGN KEYS" 
	//--------------------------------------------------------------------------
	@Override
	public List<ForeignKey> getDatabaseForeignKeys() {
		return new LinkedList<>(foreignKeys.values()); // Not sorted 
	}
	
	//--------------------------------------------------------------------------
	// LINKS management
	//--------------------------------------------------------------------------

	@Override
	public List<Link> getLinks() {
		Link[] linksArray = links.values().toArray(new Link[links.size()]);
		return Arrays.asList(linksArray);
	}

	/**
	 * Store (add or update the given link)
	 * @param link
	 */
	public void storeLink(Link link) {
		links.put(link.getId(), link);
	}
	
	@Override
	public List<String> getWarnings() {
		return Collections.emptyList();
	}

	@Override
	public String toString() {
		return  className 
				+ "|" + databaseTable
				+ "|" + databaseCatalog 
				+ "|" + databaseSchema 
				+ "|" + databaseType
				+ "|attributes.size=" + attributes.size()
				+ "|foreignKeys.size=" + foreignKeys.size() 
				+ "|links.size=" + links.size() 
				;
	}

}
