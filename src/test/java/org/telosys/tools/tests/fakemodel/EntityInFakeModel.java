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
package org.telosys.tools.tests.fakemodel;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import org.telosys.tools.generic.model.Attribute;
import org.telosys.tools.generic.model.Entity;
import org.telosys.tools.generic.model.ForeignKey;
import org.telosys.tools.generic.model.Link;

/**
 * "Entity" model class ( a Database Table mapped to a Java Class ) <br>
 * An entity contains : <br>
 * - 1..N columns <br>
 * - 0..N foreign keys <br>
 * - 0..N links <br>
 * 
 * This class is "Comparable" ( based on the entity name )
 * 
 * @author Laurent Guerin
 *
 */
public class EntityInFakeModel implements Comparable<EntityInFakeModel>, Serializable, Entity
{
	private static final long serialVersionUID = 1L;

	private String databaseTable ;
	
	private String databaseCatalog ; 
	
	private String databaseSchema ;  // v 3.0.0

	private String databaseType ; // v 2.0.7 #LGU
	
	private String className ; // v 3.0.0
	
	private Hashtable<String,Attribute>  attributes  = new Hashtable<String,Attribute>() ; 

	private Hashtable<String,ForeignKey> foreignKeys = new Hashtable<String,ForeignKey>() ;

	private Hashtable<String,Link>       links       = new Hashtable<String,Link>() ;

	
	/**
	 * Default constructor 
	 */
	public EntityInFakeModel() {
		super();
	}

	/**
	 * Constructor 
	 * @param name
	 */
	public EntityInFakeModel(String name) {
		super();
		this.databaseTable = name;
	}

	/**
	 * Returns true if the entity can be considered as a "Join Table" <br>
	 * Conditions : <br>
	 * . the entity has 2 Foreign Keys <br>
	 * . all the columns are in the Primary Key <br>
	 * . all the columns are in a Foreign Key <br>
	 * 
	 * @return
	 */
	public boolean isJoinTable() 
	{
		//--- Check if there are 2 FK
		if ( foreignKeys.size() != 2 ) {
			return false;
		} 
				
		//--- Check if all the columns are in the Primary Key
		for ( Attribute column : getAttributesArray() ) {
			//if ( ! column.isPrimaryKey() ) {
			if ( ! column.isKeyElement() ) { // v 3.0.0
				return false ;
			}
		}
		
		//--- Check if all the columns are in a Foreign Key
		for ( Attribute column : getAttributesArray() ) {
			//if ( ! column.isForeignKey() ) {
			if ( ! column.isUsedInForeignKey() ) {
				return false ;
			}
		}

		return true ;
	}

	//--------------------------------------------------------------------------
	
//	/**
//	 * Returns the name of the entity ( i.e. the database table name )
//	 * @return
//	 */
//	public String getName() {
//		return name;
//	}
//	public void setName(String name) {
//		this.name = name;
//	}
	
	@Override
	public String getDatabaseTable() {
		return this.databaseTable;
	}
	public void setDatabaseTable(String s) {
		this.databaseTable = s;
	}
	
	//--------------------------------------------------------------------------
	
//	/**
//	 * Returns the database schema of the entity 
//	 * @return
//	 * @since 1.0
//	 */
//	public String getSchema() {
//		return schema;
//	}
//	/**
//	 * Set the database schema of the entity 
//	 * @param s
//	 * @since 1.0
//	 */
//	public void setSchema(String s) {
//		this.schema = s;
//	}

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
	
//	/**
//	 * Returns the database catalog of the entity 
//	 * @return
//	 */
//	public String getCatalog() {
//		return catalog ;
//	}
//	/**
//	 * Set the database catalog of the entity 
//	 * @param s
//	 */
//	public void setCatalog(String s) {
//		this.catalog = s;
//	}
	
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
	
//	/**
//	 * Returns the short name of the VO bean Java Class ( without the package ) 
//	 * Example : "Book" or "BookVO"
//	 * @return
//	 */
//	public String getBeanJavaClass() {
//		return beanJavaClass;
//	}
//
//	public void setBeanJavaClass(String beanJavaClass) {
//		this.beanJavaClass = beanJavaClass;
//	}
	
	@Override
	public String getClassName() {
		return this.className;
	}
	public void setClassName(String className) {
		this.className = className;
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
	
	/**
	 * Returns an array containing all the columns of the entity<br>
	 * The columns are sorted by ordinal position (the original database order).
	 * 
	 * @return
	 */
	public Attribute[] getAttributesArray()
	{
		//return (Column[]) columns.values().toArray(new Column[columns.size()]);
		Attribute[] cols = (Attribute[]) ( attributes.values().toArray( new Attribute[attributes.size()] ) );
		Arrays.sort(cols); // sort using the "Comparable" implementation
		return cols ;
	}

//	/**
//	 * Returns a collection of all the columns of the entity.<br>
//	 * The columns are sorted by ordinal position (the original database order).
//	 * 
//	 * @return
//	 */
//	public Collection<Attribute> getColumnsCollection()
//	{
//		//return this.columns.values();
//		Attribute[] cols = getColumns();
//		return Arrays.asList(cols);
//	}
	
//	public void storeColumn(Attribute column)
	public void storeAttribute(Attribute attribute)  // renamed in v 3.0.0
	{
		attributes.put(attribute.getDatabaseName(), attribute);
	}

	public Attribute getAttributeByColumnName(String name)
	{
		return (Attribute) attributes.get(name);
	}

//	public void removeColumn(Attribute column)
	public void removeAttribute(Attribute attribute) // renamed in v 3.0.0
	{
		attributes.remove(attribute.getDatabaseName());
	}

	//--------------------------------------------------------------------------
	// COLUMNS exposed as "ATTRIBUTES" of the "GENERIC MODEL" ( v 3.0.0 )
	//--------------------------------------------------------------------------
	@Override
	public List<Attribute> getAttributes() {
		//Attribute[] attributes = getAttributesArray() ;
		Attribute[] attributes = getAttributesArray();
		LinkedList<Attribute> attributesList = new LinkedList<Attribute>();
		for ( Attribute a : attributes ) {
			attributesList.add(a);
		}
		return attributesList ;
	}

	//--------------------------------------------------------------------------
	// FOREIGN KEYS management
	//--------------------------------------------------------------------------
	
	/**
	 * Returns an array of all the foreign keys of the entity (table).<br>
	 * The foreign keys are sorted by name.
	 * @return
	 */
	public ForeignKey[] getForeignKeys()
	{
		ForeignKey[] array = (ForeignKey[]) foreignKeys.values().toArray(new ForeignKey[foreignKeys.size()]);
		Arrays.sort(array);
		return array ;
	}
	
//	/**
//	 * Returns a collection of all the foreign keys of the entity (table).<br>
//	 * The foreign keys are sorted by name.
//	 * @return
//	 */
//	public Collection<ForeignKey> getForeignKeysCollection()
//	{
//		//return foreignKeys.values() ;
//		ForeignKey[] array = getForeignKeys();
//		return Arrays.asList(array);
//		
//	}
	
	public void storeForeignKey(ForeignKey foreignKey)
	{
		foreignKeys.put(foreignKey.getName(), foreignKey);
	}
	
	public ForeignKey getForeignKey(String name)
	{
		return (ForeignKey) foreignKeys.get(name);
	}
	
	public void removeForeignKey(ForeignKey foreignKey)
	{
		foreignKeys.remove(foreignKey.getName() );
	}

	//--------------------------------------------------------------------------
	// FOREIGN KEYS exposed as "GENERIC MODEL FOREIGN KEYS" 
	//--------------------------------------------------------------------------
	@Override
	public List<ForeignKey> getDatabaseForeignKeys() {
		//--- Build a sorted array
		ForeignKey[] foreignKeysArray = (ForeignKey[]) foreignKeys.values().toArray( new ForeignKey[foreignKeys.size()] );
		Arrays.sort(foreignKeysArray); // sort using the "Comparable" implementation		
		//--- Build a List from the array
		LinkedList<ForeignKey> foreignKeysList = new LinkedList<ForeignKey>();
		for ( ForeignKey fk : foreignKeysArray ) {
			foreignKeysList.add(fk);
		}
		return foreignKeysList ;		
	}
	
	//--------------------------------------------------------------------------
	// LINKS management
	//--------------------------------------------------------------------------
	
	/**
	 * Returns all the links of the entity
	 * @return
	 */
	//public Link[] getLinks()
	public Link[] getLinksArray()
	{
		return (Link[]) links.values().toArray(new Link[links.size()]);
	}

	@Override
	public List<Link> getLinks()
	{
		Link[] linksArray = links.values().toArray(new Link[links.size()]);
		return Arrays.asList(linksArray);
	}

	/**
	 * Returns all the links referencing the given entity name
	 * @return
	 * @since 2.1.1
	 */
	public List<Link> getLinksTo(String entityName)
	{
		LinkedList<Link> selectedLinks = new LinkedList<Link>();
		for ( Link link : links.values() ) {
			if ( link.getTargetTableName().equals(entityName) ) {
				selectedLinks.add(link);
			}
		}
		return selectedLinks;
	}
	
//	/**
//	 * Returns all the links of the entity
//	 * @return
//	 */
//	public Collection<Link> getLinksCollection()
//	{
//		return links.values() ;
//	}
//	
	/**
	 * Store (add or update the given link)
	 * @param link
	 */
	public void storeLink(Link link)
	{
		links.put(link.getId(), link);
	}
	
	/**
	 * Get a link by its id
	 * @param id
	 * @return
	 */
	public Link getLink(String id)
	{
		return (Link) links.get(id);
	}
	
	/**
	 * Remove the given link from the entity
	 * @param link
	 */
	public int removeLink(Link link)
	{
		Link linkRemoved = links.remove( link.getId() );
		return linkRemoved != null ? 1 : 0 ;
	}

	/**
	 * Remove all the links from the entity
	 */
	public void removeAllLinks()
	{
		links.clear();
	}

	//--------------------------------------------------------------------------
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(T)
	 */
	@Override
	public int compareTo(EntityInFakeModel other) {
		if ( other != null ) {
//			String sThisName = this.getName() ;
//			String sOtherName = other.getName();
			String sThisName = this.getDatabaseTable() ;
			String sOtherName = other.getDatabaseTable();
			if ( sThisName != null && sOtherName != null ) {
				return this.databaseTable.compareTo(other.getDatabaseTable());
			}
		}
		return 0;
	}

	@Override
	public String toString() {
//		return  name 
//				+ "|"  + catalog 
//				+ "|"   + schema 
//				+ "|" + databaseType
//				+ "|" + beanJavaClass 
//				+ "|columns=" + columns.size()
//				+ "|foreignKeys=" + foreignKeys.size() 
//				+ "|links=" + links.size() 
//				;
		return  className 
				+ "|" + databaseTable
				+ "|" + databaseCatalog 
				+ "|" + databaseSchema 
				+ "|" + databaseType
				+ "|columns=" + attributes.size()
				+ "|foreignKeys=" + foreignKeys.size() 
				+ "|links=" + links.size() 
				;
	}

}
