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
package org.telosys.tools.generator.context;

import java.util.LinkedList;
import java.util.List;

import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityNoDoc;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.doc.VelocityReturnType;
import org.telosys.tools.generator.context.names.ContextName;
import org.telosys.tools.generator.context.tools.AmbiguousTypesDetector;
import org.telosys.tools.generic.model.Attribute;
import org.telosys.tools.generic.model.Entity;
import org.telosys.tools.generic.model.ForeignKey;
import org.telosys.tools.generic.model.Link;

/**
 * Specific Java Class for an Entity Java Bean with Object-Relational Mapping (ORM) <br>
 * This class provides the standard Java class informations plus : <br>
 * . the attributes of the class <br>
 * . the imports required by attributes types <br>
 * . the database table where the object is stored <br>
 * . the mapping for each attribute<br>
 * 
 * @author Laurent GUERIN
 *
 */
//-------------------------------------------------------------------------------------
@VelocityObject(
		contextName= ContextName.ENTITY ,
		//otherContextNames=ContextName.BEAN_CLASS,
		text = { 
				"Entity class for the current generation ",
				"",
				"Provides all information available for an entity as defined in the model : ",
				" . attributes information (class fields) ",
				" . database mapping ",
				""
		},
		since = "2.0.0"
 )
//-------------------------------------------------------------------------------------
public class EntityInContext 
{
	//--- Static void lists
	private static final List<AttributeInContext>  VOID_ATTRIBUTES_LIST    = new LinkedList<>();
	private static final List<ForeignKeyInContext> VOID_FOREIGN_KEYS_LIST  = new LinkedList<>();
	private static final List<LinkInContext>       VOID_LINKS_LIST         = new LinkedList<>();
	
	private final String     className ;
	private final String     packageName ;
	
    private final String     databaseTable    ; // Table name this class is mapped with
    private final String     databaseCatalog  ; // The table's catalog 
    private final String     databaseSchema   ; // The table's schema 
    private final String     databaseType     ; // The table's type "table" or "view" 
    private final String     databaseComment  ; // The table's database comment  (since ver 3.1.0 )
    
	private final List<AttributeInContext> attributes ; // The attributes for this class ( ALL ATTRIBUTES )
	private final List<AttributeInContext> keyAttributes ;   // The KEY attributes for this class
	private final List<AttributeInContext> nonKeyAttributes; // The NON KEY attributes for this class

	private final List<ForeignKeyInContext> foreignKeys ; // The database FOREIGN KEYS attributes for this entity ( v 2.0.7)
	
	private final List<LinkInContext> links ; // The links for this class ( ALL LINKS )
	
	private final ModelInContext modelInContext ;  // v 3.0.0
	
	private final EnvInContext   env ; // ver 2.1.0
	
	//-----------------------------------------------------------------------------------------------
	/**
	 * Constructor based on Repository Entity
	 * @param entity
	 * @param entityPackage
	 * @param entitiesManager
	 * @param env
	 * @throws GeneratorException
	 */
	public EntityInContext( final Entity entity, final String entityPackage, 
							final ModelInContext modelInContext, // v 3.0.0
							final EnvInContext env ) 
	{
		this.className = entity.getClassName();  // v 3.0.0
		
		this.packageName = StrUtil.notNull(entityPackage);
		
		this.modelInContext = modelInContext ; // v 3.0.0
		this.env = env ;
		
		this.databaseTable   = StrUtil.notNull(entity.getDatabaseTable());
		this.databaseCatalog = StrUtil.notNull(entity.getDatabaseCatalog()); // v 3.0.0
		
		this.databaseSchema  = StrUtil.notNull(entity.getDatabaseSchema()); // v 3.0.0
		
		this.databaseType    = StrUtil.notNull(entity.getDatabaseType()); // ver 2.0.7

		this.databaseComment = StrUtil.notNull(entity.getDatabaseComment()); // v 3.1.0
		
		//--- Initialize all the ATTRIBUTES for the current entity
		this.attributes = new LinkedList<>();
		for ( Attribute attribute : entity.getAttributes() ) { // v 3.0.0
			AttributeInContext attributeInContext = new AttributeInContext(this, attribute, this.modelInContext, this.env);
			this.attributes.add(attributeInContext);
		}

		//--- Initialize all the LINKS for the current entity
		this.links = new LinkedList<>();
		for ( Link link : entity.getLinks() ) { // v 3.0.0
			LinkInContext linkInContext = new LinkInContext(this, link, this.modelInContext, this.env ); // v 3.0.0
			this.links.add(linkInContext);
		}
		
		//--- Init all the DATABASE FOREIGN KEYS  ( v 2.0.7 )
		this.foreignKeys = new LinkedList<>();
		for ( ForeignKey fk : entity.getDatabaseForeignKeys() ) {
			this.foreignKeys.add( new ForeignKeyInContext(fk ) );
		}
		
		//--- Build the list of the "KEY" attributes
		this.keyAttributes = selectAttributesIfKeyElement(true);
		
		//--- Build the list of the "NON KEY" attributes
		this.nonKeyAttributes = selectAttributesIfKeyElement(false); 

		//--- Post processing : import resolution
		endOfAttributesDefinition();
	}
	//-----------------------------------------------------------------------------------------------
	/**
	 * This method closes the definition of the class (when all the attributes have been added) <br>
	 * 
	 * It determines if there is import types collision ( eg "java.util.Date" with "java.sql.Date" ) <br>
	 * and managed the imports list and attributes declarations types to avoid imports error
	 *  
	 */
	private void endOfAttributesDefinition() {
		if ( attributes == null ) return ;
		//--- Duplicated short types detection
		AmbiguousTypesDetector duplicatedTypesDetector = new AmbiguousTypesDetector(attributes);
		List<String> ambiguousTypes = duplicatedTypesDetector.getAmbiguousTypes();
		for ( AttributeInContext attribute : attributes ) {
			//--- Is this attribute's type ambiguous ?
			if ( ambiguousTypes.contains( attribute.getFullType() ) ) {
				//--- Yes => force this attribute to use its "full type" for variable declaration
				attribute.useFullType() ; 
			}
		}
	}

	//-----------------------------------------------------------------------------------------------	
	/**
	 * Returns the Java class name without the package ( ie : "MyClass" )
	 * @return
	 */
	@VelocityMethod ( text= { 
			"Returns the class name for the entity without the package ( ie : \"MyClass\" )"
		},
		example="$entity.name"
	)
	public String getName()
	{
		if ( env != null ) {
			StringBuilder sb = new StringBuilder();
			sb.append( env.getEntityClassNamePrefix() ) ; // Never null ( "" if not set )
			sb.append( className ) ; // Never null ( "" if not set )
			sb.append( env.getEntityClassNameSuffix() ) ; // Never null ( "" if not set )
			return sb.toString();
		}
		else {
			return className ;
		}
	}
	
	/**
	 * Returns the Java class package or void ( ie : "my.package" or "" )
	 * @return
	 */
	@VelocityMethod ( text= { 
			"Returns the package name (or void) for the entity ( ie : \"my.package\" or \"\" )"
		},
		example="$entity.package"
	)
	public String getPackage()
    {
        return packageName ;
    }
	
	/**
	 * Returns the Java class full name ( ie : "my.package.MyClass" )
	 * @return
	 */
	@VelocityMethod ( text= { 
			"Returns the full class name for the entity (ie : \"my.package.MyClass\" )"
		},
		example="$entity.fullName"
	)
	public String getFullName()
    {
		return packageName + "." + getName();
    }
	
	/* (non-Javadoc)
	 * Same as getName() 
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		// NB : must return only the class name => do not change
		// Usage example in ".vm" : ${beanClass}.class 
		return getName() ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns the attribute associated with the given database column name"
		},
		parameters = {
			"columnName : the database column's name"
		},
		example="$entity.attributeByColumnName"
	)
	@VelocityReturnType("'attribute' object")
	public AttributeInContext getAttributeByColumnName(String columnName) throws GeneratorException {
		if ( columnName == null ) {
			throw new GeneratorException("Invalid argument, 'columnName' is null");
		}
		for( AttributeInContext attribute : this.getAttributes() ) {
			if ( columnName.equals( attribute.getDatabaseName() ) ) {
				return attribute ;
			}
		}
		throw new GeneratorException("No attribute with column name '" + columnName + "'");
	}
	
	//-------------------------------------------------------------------------------------
	/**
	 * Returns all the attributes defined for this class
	 * @return
	 */
	@VelocityMethod ( text= { 
			"Returns all the attributes defined for this entity"
		},
		example="$entity.attributes"
	)
	@VelocityReturnType("List of 'attribute' objects")
	public List<AttributeInContext> getAttributes() {
		if ( attributes != null ) {
			return attributes ;
		}
		return VOID_ATTRIBUTES_LIST ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns the number of attributes defined for this entity"
		},
		example="$entity.attributesCount",
		since="2.0.7"
	)
	public int getAttributesCount() {
		if ( attributes != null ) {
			return attributes.size() ;
		}
		return 0 ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns the number of links defined in the entity"
		},
		example="$entity.linksCount",
		since="3.3.0"
	)
	public int getLinksCount() {
		if ( links != null ) {
			return links.size() ;
		}
		return 0 ;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns TRUE if this entity has at least one link "
		},
		example= {
			"#if ( $entity.hasLinks() )",
			"...",
			"#end"
		}
	)
	public boolean hasLinks() {
		return ( links != null && ! links.isEmpty() ) ;
	}
	

	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns a list of all the links defined for the current entity"
		},
		example={	
			"#foreach( $link in $entity.links )",
			"...",
			"#end" 
		}
	)
	@VelocityReturnType("List of 'link' objects")
	public List<LinkInContext> getLinks() {
		if ( links != null && ! links.isEmpty() ) {
			return links ;
		}
		return VOID_LINKS_LIST ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns a list of all the links selected in the model for the current entity"
		},
		example={	
			"#foreach( $link in $entity.selectedLinks )",
			"...",
			"#end" 
		}
	)
	@VelocityReturnType("List of 'link' objects")
	public List<LinkInContext> getSelectedLinks() {
		if ( links != null && ! links.isEmpty() )
		{
			LinkedList<LinkInContext> selectedLinks = new LinkedList<>();
			for ( LinkInContext link : links ) {
				if ( link.isSelected() ) {
					selectedLinks.add(link) ;
				}
			}
			return selectedLinks ;
		}
		return VOID_LINKS_LIST ;
	}

	private void checkCriterion ( int criterion ) {
		if ( criterion == Const.KEY || criterion == Const.NOT_KEY ) return ;
		if ( criterion == Const.TEXT || criterion == Const.NOT_TEXT ) return ;
		if ( criterion == Const.IN_LINKS || criterion == Const.NOT_IN_LINKS ) return ;
		if ( criterion == Const.IN_SELECTED_LINKS || criterion == Const.NOT_IN_SELECTED_LINKS ) return ;
		// else : invalid criterion
		throw new GeneratorContextException("Invalid criterion in getAttributesByCriteria argument(s)");
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityNoDoc
	public List<AttributeInContext> getAttributesByCriteria( int c1  ) 
	{
		checkCriterion(c1);
		return getAttributesByAddedCriteria(c1);
	}
	//-------------------------------------------------------------------------------------
	@VelocityNoDoc
	public List<AttributeInContext> getAttributesByCriteria( int c1, int c2 ) 
	{
		checkCriterion(c1);
		checkCriterion(c2);
		return getAttributesByAddedCriteria(c1 + c2);
	}
	//-------------------------------------------------------------------------------------
	@VelocityNoDoc
	public List<AttributeInContext> getAttributesByCriteria( int c1, int c2, int c3 ) 
	{
		checkCriterion(c1);
		checkCriterion(c2);
		checkCriterion(c3);
		return getAttributesByAddedCriteria(c1 + c2 + c3);
	}
	@VelocityMethod ( text= { 
			"Returns all the attributes of this entity matching the given criteria",
			"This method accepts 1 to 4 criteria",
			"The critera are combined using the 'AND' operator",
			"Usable criteria ( to be prefixed with '$const.' ) : ",
			"KEY,  NOT_KEY,  IN_LINKS,  NOT_IN_LINKS,  IN_SELECTED_LINKS,  NOT_IN_SELECTED_LINKS,  TEXT,  NOT_TEXT  "
	},
	parameters = {
			"crit1 : 1st criterion ",
			"crit2 : 2nd criterion (optional)",
			"crit3 : 3rd criterion (optional)",
			"crit4 : 4th criterion (optional)"
	},
	example = {
			"$entity.getAttributesByCriteria($const.NOT_KEY)",
			"$entity.getAttributesByCriteria($const.NOT_KEY, $const.NOT_IN_SELECTED_LINKS)"
	}
	)
	@VelocityReturnType("List of 'attribute' objects")
	public List<AttributeInContext> getAttributesByCriteria( int c1, int c2, int c3, int c4 ) 
	{
		checkCriterion(c1);
		checkCriterion(c2);
		checkCriterion(c3);
		checkCriterion(c4);
		return getAttributesByAddedCriteria(c1 + c2 + c3 + c4);
	}
	
	//-------------------------------------------------------------------------------------
	private List<AttributeInContext> getAttributesByAddedCriteria( int criteria ) 
	{
		LinkedList<AttributeInContext> selectedAttributes = new LinkedList<>();
		
		for ( AttributeInContext attribute : attributes ) {
			Boolean selectedByKey  = null ;
			Boolean selectedByText = null ;
			Boolean selectedByLink = null ;
			Boolean selectedBySelectedLink = null ;
			
			//--- IS KEY ?
			if ( ( criteria & Const.KEY ) != 0 ) {
				selectedByKey = attribute.isKeyElement();
			}
			if ( ( criteria & Const.NOT_KEY ) != 0 ) {
				selectedByKey = ! attribute.isKeyElement();
			}

			//--- IS TEXT ?
			if ( ( criteria & Const.TEXT ) != 0 ) {
				selectedByText = attribute.isLongText();
			}
			if ( ( criteria & Const.NOT_TEXT ) != 0 ) {
				selectedByText = ! attribute.isLongText();
			}
			
			//--- IS IN LINK ?
			if ( ( criteria & Const.IN_LINKS ) != 0 ) {
				selectedByLink = attribute.isUsedInLinks();
			}
			if ( ( criteria & Const.NOT_IN_LINKS ) != 0 ) {
				selectedByLink = ! attribute.isUsedInLinks();
			}
			
			//--- IS IN SELECTED LINK ?
			if ( ( criteria & Const.IN_SELECTED_LINKS ) != 0 ) {
				selectedBySelectedLink = attribute.isUsedInSelectedLinks() ;
			}			
			if ( ( criteria & Const.NOT_IN_SELECTED_LINKS ) != 0 ) {
				selectedBySelectedLink = ! attribute.isUsedInSelectedLinks() ;
			}
			
			int criteriaCount = 0 ;
			int selected = 0 ;
			if ( selectedByKey != null ) {
				criteriaCount++ ;
				if ( Boolean.TRUE.equals(selectedByKey) ) selected++ ;
			}
			if ( selectedByText != null ) {
				criteriaCount++ ;
				if ( Boolean.TRUE.equals(selectedByText) ) selected++ ;
			}
			if ( selectedByLink != null ) {
				criteriaCount++ ;
				if ( Boolean.TRUE.equals(selectedByLink) ) selected++ ;
			}
			if ( selectedBySelectedLink != null ) {
				criteriaCount++ ;
				if ( Boolean.TRUE.equals(selectedBySelectedLink) ) selected++ ;
			}

			if ( ( criteriaCount > 0 ) && ( selected == criteriaCount ) ) {	
				// All criteria verified ( "AND" ) => keep this attribute
				selectedAttributes.add(attribute) ;
			}
			
		} // for each ...
//		if ( selectedAttributes.size() > 0 ) {
//			return selectedAttributes ;
//		}
//		return VOID_ATTRIBUTES_LIST ;
		if ( selectedAttributes.isEmpty() ) {
			return VOID_ATTRIBUTES_LIST ;
		}
		else {
			return selectedAttributes ;
		}
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns the attributes used in the Primary Key for this entity"
		},
		example= {
			"#foreach( $attribute in $entity.keyAttributes )",
			"...",
			"#end"
		}
	)
	@VelocityReturnType("List of 'attribute' objects")
	public List<AttributeInContext> getKeyAttributes() {
		if ( keyAttributes != null ) {
			return keyAttributes ;
		}
		return VOID_ATTRIBUTES_LIST ;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns the unique attribute used as the Primary Key for this entity.",
			"Throws an exception if the entity does not have a Primary Key",
			"or if it has a composite Primary Key (2 or more attributes)"
		},
		example= {
			"$entity.keyAttribute"
		},
		since="3.0.0"
	)
	@VelocityReturnType("Instance of 'attribute' ")
	public AttributeInContext getKeyAttribute() throws GeneratorException 
	{
		String msg = "Cannot get 'keyAttribute' for entity '" + this.className + "' : ";
		if ( this.hasPrimaryKey() ) {
			List<AttributeInContext> keyAttributesList = this.getKeyAttributes() ;
			if ( keyAttributesList.size() == 1 ) {
				// The PK is composed of a single attribute 
				return keyAttributesList.get(0);
			}
			else if ( keyAttributesList.size() > 1 ){
				throw new GeneratorException(msg 
							+ "this entity has a composite Primary Key ("
							+ keyAttributesList.size() + " attributes)");
			} 
			else {
				throw new GeneratorException(msg
							+ keyAttributesList.size() + " attributes in Primary Key");
			}
		}
		else {
			throw new GeneratorException(msg
						+ "no Primary Key for this entity");
		}
	}
	
	//-------------------------------------------------------------------------------------
	// All attributes names as string
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns all the attributes names as a string.",
			"The attributes names are separated by the given separator"
		},
		parameters = {
			"separator : the separator to be put between each attribute name"
		},
		example= {
			"$entity.attributesNamesAsString('/') "
		},
		since="3.0.0"
		)
    public String attributesNamesAsString(String separator) {
		return buildAttributesNamesAsString(this.getAttributes(), separator, "", "");
    }
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns all the attributes names as a string.",
			"The attributes names are separated by the given separator",
			"with a prefix/suffix for each attribute name"
		},
		parameters = {
			"separator : the separator to be put between each attribute name",
			"prefix : the prefix to be put before each attribute name",
			"suffix : the prefix to be put after each attribute name"
		},
		example= {
			"$entity.attributesNamesAsString('/', '{{', '}}') "
		},
		since="3.0.0"
	)
    public String attributesNamesAsString(String separator, String prefix, String suffix) {
   		return buildAttributesNamesAsString(this.getAttributes(), separator, prefix, suffix);
    }

	//-------------------------------------------------------------------------------------
	// Key attributes names as string
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns the key attributes names as a string.",
			"The attributes names are separated by the given separator",
			"with a prefix/suffix for each attribute name"
		},
		parameters = {
			"separator : the separator to be put between each attribute name"
		},
		example= {
			"$entity.keyAttributesNamesAsString('/') "
		},
		since="2.1.0"
		)
    public String keyAttributesNamesAsString(String separator) {
    	return buildAttributesNamesAsString(this.getKeyAttributes(), separator, "", "");
    }
    
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns the key attributes names as a string.",
			"The attributes names are separated by the given separator",
			"with a prefix/suffix for each attribute name"
		},
		parameters = {
			"separator : the separator to be put between each attribute name",
			"prefix : the prefix to be put before each attribute name",
			"suffix : the prefix to be put after each attribute name"
		},
		example= {
			"$entity.keyAttributesNamesAsString('/', '{{', '}}') "
		},
		since="2.1.0"
	)
    public String keyAttributesNamesAsString(String separator, String prefix, String suffix) {
		return buildAttributesNamesAsString(this.getKeyAttributes(), separator, prefix, suffix);
    }

	//-------------------------------------------------------------------------------------
	// "Non key" attributes names as string
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns the 'non key' attributes names as a string.",
			"The attributes names are separated by the given separator"
		},
		parameters = {
			"separator : the separator to be put between each attribute name"
		},
		example= {
			"$entity.nonKeyAttributesNamesAsString('/') "
		},
		since="3.0.0"
		)
    public String nonKeyAttributesNamesAsString(String separator) {
		return buildAttributesNamesAsString(this.getNonKeyAttributes(), separator, "", "");
    }
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns the 'non key' attributes names as a string.",
			"The attributes names are separated by the given separator",
			"with a prefix/suffix for each attribute name"
		},
		parameters = {
			"separator : the separator to be put between each attribute name",
			"prefix : the prefix to be put before each attribute name",
			"suffix : the prefix to be put after each attribute name"
		},
		example= {
			"$entity.nonKeyAttributesNamesAsString('/', '{{', '}}') "
		},
		since="3.0.0"
	)
    public String nonKeyAttributesNamesAsString(String separator, String prefix, String suffix) {
   		return buildAttributesNamesAsString(this.getNonKeyAttributes(), separator, prefix, suffix);
    }

	//-------------------------------------------------------------------------------------
	/**
	 * Builds a string containing the given list of attibutes <br>
	 * with the given separator and optionally the given prefix and suffix. <br>
	 * @param attributes
	 * @param separator
	 * @param prefix
	 * @param suffix
	 * @return
	 */
	private String buildAttributesNamesAsString(List<AttributeInContext> attributes, String separator, String prefix, String suffix) {
       StringBuilder sb = new StringBuilder();
       int n = 0 ;
       for ( AttributeInContext attribute : attributes ) {
              n++ ;
              if ( n > 1 ) sb.append(separator);
              sb.append(prefix);
              sb.append(attribute.getName());
              sb.append(suffix);
       }
       return sb.toString();
    }

	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns the number of attributes used in the Primary Key for this entity"
		},
		example= {
			"$entity.keyAttributesCount"
		},
		since="2.0.7"
	)
	public int getKeyAttributesCount() 
	{
		if ( keyAttributes != null ) {
			return keyAttributes.size() ;
		}
		return 0 ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns the attributes NOT used in the Primary Key for this entity"
		},
		example= {
			"#foreach( $attribute in $entity.nonKeyAttributes )",
			"...",
			"#end"
		}
	)
	@VelocityReturnType("List of 'attribute' objects")
	public List<AttributeInContext> getNonKeyAttributes() {
		if ( nonKeyAttributes != null ) {
			return nonKeyAttributes ;
		}
		return VOID_ATTRIBUTES_LIST ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns the number of attributes NOT used in the Primary Key for this entity"
		},
		example= {
			"$entity.nonKeyAttributesCount"
		},
		since="2.0.7"
	)
	public int getNonKeyAttributesCount() {
		if ( nonKeyAttributes != null ) {
			return nonKeyAttributes.size() ;
		}
		return 0 ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns the database table mapped with this entity"
		},
		example="$entity.databaseTable"
	)
	public String getDatabaseTable() {
		return databaseTable ;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns the database catalog of the table mapped with this entity"
		},
		example="$entity.databaseCatalog"
	)
	public String getDatabaseCatalog() {
		return databaseCatalog ;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns the database schema of the table mapped with this entity"
		},
		example="$entity.databaseSchema"
	)
	public String getDatabaseSchema() {
		return databaseSchema ;
	}
	
	//-------------------------------------------------------------------------------------
	/**
	 * Returns all the database foreign keys defined for this entity
	 * @return
	 */
	@VelocityMethod ( text= { 
			"Returns all the database foreign keys defined for this entity"
		},
		example="$entity.databaseForeignKeys",
		since="2.0.7"		
	)
	@VelocityReturnType("List of 'foreign keys' objects")
	public List<ForeignKeyInContext> getDatabaseForeignKeys() {
		if ( foreignKeys != null ) {
			return foreignKeys ;
		}
		return VOID_FOREIGN_KEYS_LIST ;
	}

	//-------------------------------------------------------------------------------------
	/**
	 * Returns all the database foreign keys defined for this entity
	 * @return
	 */
	@VelocityMethod ( text= { 
			"Returns the number of database foreign keys defined for this entity"
		},
		example="$entity.databaseForeignKeysCount",
		since="2.0.7"		
	)
	public int getDatabaseForeignKeysCount() {
		if ( foreignKeys != null ) {
			return foreignKeys.size() ;
		}
		return 0 ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns the database type of the table mapped with this entity <br>",
			"Type returned by the database meta-data ( 'TABLE', 'VIEW', ... ) "
		},
		example="$entity.databaseType",
		since="2.0.7"
	)
	public String getDatabaseType() {
		return databaseType ;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns TRUE if the database type of the entity is 'TABLE' ",
			"(it can be a TABLE or a VIEW, see also 'isViewType') "
		},
		example= {
			"#if ( $entity.isTableType() )",
			"...",
			"#end"
		},
		since="2.0.7"
	)
	public boolean isTableType() {
		if ( databaseType != null ) {
			return "TABLE".equalsIgnoreCase( databaseType.trim() ) ;
		}
		return false;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns TRUE if the database type of the entity is 'VIEW' ",
			"(it can be a TABLE or a VIEW, see also 'isTableType') "
		},
		example= {
				"#if ( $entity.isViewType() )",
				"...",
				"#end"
			},
		since="2.0.7"
	)
	public boolean isViewType() {
		if ( databaseType != null ) {
			return "VIEW".equalsIgnoreCase( databaseType.trim() ) ;
		}
		return false;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns the database comment of the table mapped with this entity <br>",
			""
		},
		example="$entity.databaseComment",
		since="3.1.0"
	)
	public String getDatabaseComment() {
		return databaseComment ;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns the attributes NOT tagged as 'long text' for this entity",
			"( 'standard attributes' )"
		},
		example= {
			"#foreach( $attribute in $entity.nonTextAttributes )",
			"...",
			"#end"
		}
	)
	@VelocityReturnType("List of 'attribute' objects")
	public List<AttributeInContext> getNonTextAttributes() {
		return selectAttributesIfLongText ( false ); // NOT LONG TEXT
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns the attributes tagged as 'long text' for this entity",
			"( specific attributes used to store long text )"
		},
		example= {
			"#foreach( $attribute in $entity.textAttributes )",
			"...",
			"#end"
		}
	)
	@VelocityReturnType("List of 'attribute' objects")
	public List<AttributeInContext> getTextAttributes() {
		return selectAttributesIfLongText ( true ); // Special "LONG TEXT"
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns TRUE if this entity has at least one attribute tagged as 'long text'"
		},
		example= {
			"#if ( $entity.hasTextAttribute() )",
			"...",
			"#end"
		}
	)
	public boolean hasTextAttribute() {
    	if ( attributes != null )
    	{
    		int n = attributes.size();
        	for ( int i = 0 ; i < n ; i++ )        		
        	{
        		AttributeInContext attribute = attributes.get(i);
                if ( attribute.isLongText() ) 
                {
                	return true ;
                }
        	}
    	}
    	return false ;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns TRUE if this entity has a composite primary key ",
			"( a primary key composed of 2 or more attributes )"
		},
		example= {
			"#if ( $entity.hasCompositePrimaryKey() )",
			"...",
			"#end"
		}
	)
	public boolean hasCompositePrimaryKey() {
		if ( keyAttributes != null ) {
			return keyAttributes.size() > 1 ;
		}
		return false ; // No key attributes
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns TRUE if this entity has a primary key ",
			"( a primary key composed of one or more attributes )"
		},
		example= {
			"#if ( $entity.hasPrimaryKey() )",
			"...",
			"#end"
		},
		since="2.0.7"
	)
	public boolean hasPrimaryKey() {
		if ( keyAttributes != null ) {
			return ! keyAttributes.isEmpty() ; // At least 1 key attribute
		}
		return false ; // No key attributes
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns TRUE if this entity has an 'auto-incremented' key attribute ",
			"( a key based on a numeric value incremented by the database )"
		},
		example= {
			"#if ( $entity.hasAutoIncrementedKey() )",
			"...",
			"#end"
		}
	)
	public boolean hasAutoIncrementedKey() {
		if ( keyAttributes != null ) {
			for ( AttributeInContext keyAttribute : keyAttributes ) {
				if ( keyAttribute.isAutoIncremented() ) {
					return true ; 
				}
			}
		}
		return false ; 
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns the attribute used as the autoincremented key ",
			"or null if none",
			""
	},
	example = {
			"#if ( $entity.hasAutoIncrementedKey() )",
			"$entity.autoincrementedKeyAttribute",
			"#end"
	}
	)
	@VelocityReturnType("'attribute' object")
	public AttributeInContext getAutoincrementedKeyAttribute() 
	{
		List<AttributeInContext> keyAttributesList = getKeyAttributes();
    	if ( keyAttributesList != null && keyAttributesList.size() == 1 )
    	{
			// Only one attribute in the PK
			AttributeInContext attribute = keyAttributesList.get(0);
			if ( attribute != null && attribute.isAutoIncremented() ) {
            	// This unique PK field is auto-incremented => return it
            	return attribute ; 
			}
    	}
    	return null ;
	}
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns a list of 'simple type' for each entity referenced by the given attributes",
			""
		},
		parameters = {
			"attributes : list of attributes to be used to search the referenced entities"
		},
		example= {
			"#set( $referencedEntities = $entity.referencedEntityTypes( $entity.nonKeyAttributes ) )"
		},
		since="2.1.0"
	)
    public List<String> referencedEntityTypes(List<AttributeInContext> attributes) throws GeneratorException {
		List<String> referencedEntityTypes = new LinkedList<>();
		for ( AttributeInContext attribute : attributes ) {
			//--- Is this attribute involved in a link ?
			for( LinkInContext link : this.getLinks()  ) {
				//--- Only if the link uses one of the given attributes
				if( link.isOwningSide() && link.usesAttribute(attribute) ) {
					String referencedEntityType = link.getTargetEntitySimpleType() ;
					//--- Found => add it in the list
					if ( ! referencedEntityTypes.contains(referencedEntityType) ) {
						//--- Not already in the list => add it
						referencedEntityTypes.add( link.getTargetEntitySimpleType() );
					}
				}
			}
		}
		return referencedEntityTypes ;
    }

	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns a list of 'simple type' for all the entities referenced by the current entity",
			"(based on the 'owning side' links)"
		},
		example= {
			"#set( $referencedEntities = $entity.referencedEntityTypes() )"
		},
		since="2.1.0"
	)
    public List<String> referencedEntityTypes() throws GeneratorException {
		List<String> referencedEntityTypes = new LinkedList<>();
		//--- Search all the referenced entities (from all the "owning side" links)
		for( LinkInContext link : this.getLinks()  ) {
			if ( link.isOwningSide() ) {
				String referencedEntityType = link.getTargetEntitySimpleType() ;
				//--- Found => add it in the list
				if ( ! referencedEntityTypes.contains(referencedEntityType) ) {
					//--- Not already in the list => add it
					referencedEntityTypes.add( referencedEntityType );
				}
			}
		}
		return referencedEntityTypes ;
    }

	//-------------------------------------------------------------------------------------------------
	private List<AttributeInContext> selectAttributesIfKeyElement(boolean bKeyAttribute) {
		LinkedList<AttributeInContext> attributesList = new LinkedList<>();
    	if ( attributes != null ) {
            for ( AttributeInContext attribute : attributes ) {
                if ( attribute.isKeyElement() == bKeyAttribute ) {
                	attributesList.add(attribute);
                }        		
            }
    	}
		return attributesList ;
	}
	
	/**
	 * "Text" or "non Text" attributes
	 * @param bLongText
	 * @return
	 */
	private List<AttributeInContext> selectAttributesIfLongText(boolean bLongText) {
		LinkedList<AttributeInContext> list = new LinkedList<>();
    	if ( attributes != null ) {
            for ( AttributeInContext attribute : attributes ) {
                if ( attribute.isLongText() == bLongText ) {
                	list.add(attribute);
                }        		
            }
    	}
		return list ;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns TRUE if the entity is a 'join entity' ",
			"( typically if the entity is a 'join table' in the database )",
			"An entity is considered as a 'join entity' if : ",
			" - the entity has 2 Foreign Keys",
			" - all attributes are part of the Primary Key ",
			" - all attributes are part of a Foreign Key"
		},
		example= {
				"#if ( $entity.isJoinEntity() )",
				"...",
				"#end"
			},
		since="3.3.0"
	)
	public boolean isJoinEntity() {
		//--- Check if entity has 2 Foreign Keys
		if ( foreignKeys.size() != 2 ) {
			return false;
		} 
		//--- Check if all attributes are in the PK and in a FK
		for ( AttributeInContext a : attributes) {
			if ( ! a.isKeyElement() ) { 
				return false ; // at least one attribute is not in PK 
			}
			if ( ! a.isFK() ) {
				return false ; // at least one attribute is not in FK
			}
		}
		return true ;
	}
}
