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
package org.telosys.tools.generator.context;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.telosys.tools.generator.EntitiesManager;
import org.telosys.tools.generator.GeneratorContextException;
import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityNoDoc;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.doc.VelocityReturnType;
import org.telosys.tools.generator.context.names.ContextName;
import org.telosys.tools.generator.context.tools.AmbiguousTypesDetector;
import org.telosys.tools.repository.model.Column;
import org.telosys.tools.repository.model.Entity;
import org.telosys.tools.repository.model.ForeignKey;
import org.telosys.tools.repository.model.Link;

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
	private final static List<AttributeInContext>  VOID_ATTRIBUTES_LIST    = new LinkedList<AttributeInContext>();
	private final static List<ForeignKeyInContext> VOID_FOREIGN_KEYS_LIST  = new LinkedList<ForeignKeyInContext>();
	private final static List<LinkInContext>           VOID_LINKS_LIST         = new LinkedList<LinkInContext>();
	
	//private final static String   NONE = "" ;
	
	private final String     _sName ;
	private final String     _sPackage ;
	//private String     _sFullName    = NONE ;
	//private String     _sSuperClass  = NONE ;	
	
    private final String     _sDatabaseTable    ; // Table name this class is mapped with
    private final String     _sDatabaseCatalog  ; // The table's catalog 
    private final String     _sDatabaseSchema   ; // The table's schema 
    private final String     _sDatabaseType     ; // The table's type "table" or "view" 
    
	private final LinkedList<AttributeInContext> _attributes ; // The attributes for this class ( ALL ATTRIBUTES )
	private LinkedList<AttributeInContext>  _keyAttributes     = null ; // The KEY attributes for this class
	private LinkedList<AttributeInContext>  _nonKeyAttributes  = null ; // The NON KEY attributes for this class

//	private String     _sSqlKeyColumns = null ;
//	private String     _sSqlNonKeyColumns = null ;
	
	private final LinkedList<ForeignKeyInContext>  _foreignKeys ; // The database FOREIGN KEYS attributes for this entity ( v 2.0.7)
	
//	//--- XML mapper infos
//	private LinkedList<JavaBeanClassAttribute> _nonTextAttributes  = null ; // Standard attributes for this class ( not "long text" )
//	private LinkedList<JavaBeanClassAttribute> _textAttributes     = null ; // Special "long text" attributes for this class

	//--- JPA specific
	private final LinkedList<LinkInContext> _links ; // The links for this class ( ALL ATTRIBUTES )
	
	private final EntitiesManager _entitiesManager ; // ver 2.1.0
	private final EnvInContext    _env ; // ver 2.1.0
	
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
							final EntitiesManager entitiesManager, final EnvInContext env ) throws GeneratorException
	{
		_sName = entity.getBeanJavaClass() ;
		_sPackage = entityPackage;
		
		_entitiesManager = entitiesManager ;
		_env = env ;
		
		_sDatabaseTable   = entity.getName();
		_sDatabaseCatalog = entity.getCatalog();
		_sDatabaseSchema  = entity.getSchema();
		_sDatabaseType    = entity.getDatabaseType(); // ver 2.0.7
		
		//--- Initialize all the ATTRIBUTES for the current entity
		_attributes = new LinkedList<AttributeInContext>();
		Collection<Column> entityColumns = entity.getColumnsCollection() ;
		for ( Column column : entityColumns ) {
			AttributeInContext attribute = new AttributeInContext(this, column);
			_attributes.add(attribute);
		}

		//--- Initialize all the LINKS for the current entity
		_links = new LinkedList<LinkInContext>();
		Collection<Link> entityLinks = entity.getLinksCollection() ;
		for ( Link link : entityLinks ) {
			// On va trouver le bean correspondant a ce lien dans le model
//			Entity referencedEntity = repositoryModel.getEntityByName(link.getTargetTableName());
////			JavaBeanClassLink jcl = new JavaBeanClassLink(link, this._entite , entityCible );
//			LinkInContext jcl = new LinkInContext(link, referencedEntity );

			//String entityName = link.getTargetTableName() ;
//			EntityInContext referencedEntity = _entitiesBuilder.getEntity( entityName ) ;
//			LinkInContext jcl = new LinkInContext(link, referencedEntity );
			LinkInContext linkInCtx = new LinkInContext(this, link, _entitiesManager );
			
			//this.addLink(jcl);
			_links.add(linkInCtx);
		}
		
		//--- Init all the DATABASE FOREIGN KEYS  ( v 2.0.7 )
		_foreignKeys = new LinkedList<ForeignKeyInContext>();
		Collection<ForeignKey> foreignKeys = entity.getForeignKeysCollection();
		for ( ForeignKey fk : foreignKeys ) {
			_foreignKeys.add( new ForeignKeyInContext(fk ) );
		}
		
		// import resolution
		//this.endOfDefinition();
		endOfAttributesDefinition();
		
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
		if ( _env != null ) {
			StringBuilder sb = new StringBuilder();
			sb.append( _env.getEntityClassNamePrefix() ) ; // Never null ( "" if not set )
			sb.append( _sName ) ; // Never null ( "" if not set )
			sb.append( _env.getEntityClassNameSuffix() ) ; // Never null ( "" if not set )
			return sb.toString();
		}
		else {
			return _sName ;
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
        return _sPackage ;
    }
	
//	/**
//	 * Returns the super class of this Java class 
//	 * @return
//	 */
//	@VelocityMethod ( text= { 
//			"Returns the super class for the entity's class (or void if none)"
//		},
//		example="$entity.superClass"
//	)
//	public String getSuperClass()
//    {
//        return _sSuperClass ;
//    }

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
		//return _sFullName ;
		
		return _sPackage + "." + getName();
    }
	
//    /**
//     * Returns the Java line instruction for the toString() method
//     * @return
//     */
//    public String getToStringInstruction()
//    {
//    	return "\"JavaClass : '" + getName() + "' \"";
//    }
    
//    public String toStringMethodCodeLines( int iLeftMargin )
//    {
//    	String leftMargin = GeneratorUtil.blanks(iLeftMargin);
//    	return leftMargin + "return \"JavaClass : '" + getName() + "' \" ; \n";
//    }
    
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
	public List<AttributeInContext> getAttributes() 
	{
		if ( _attributes != null )
		{
			return _attributes ;
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
	public int getAttributesCount() 
	{
		if ( _attributes != null )
		{
			return _attributes.size() ;
		}
		return 0 ;
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
	public List<LinkInContext> getLinks() 
	{
		if ( _links != null )
		{
			if ( _links.size() > 0 ) {
				return _links ;
			}
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
	public List<LinkInContext> getSelectedLinks() 
	{
		if ( _links != null )
		{
			if ( _links.size() > 0 ) {
				LinkedList<LinkInContext> selectedLinks = new LinkedList<LinkInContext>();
				for ( LinkInContext link : _links ) {
					if ( link.isSelected() ) {
						selectedLinks.add(link) ;
					}
				}
				return selectedLinks ;
			}
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
		ContextLogger.log("getAttributesByCriteria(" + c1 + ")" );
		checkCriterion(c1);
		return getAttributesByAddedCriteria(c1);
	}
	//-------------------------------------------------------------------------------------
	@VelocityNoDoc
	public List<AttributeInContext> getAttributesByCriteria( int c1, int c2 ) 
	{
		ContextLogger.log("getAttributesByCriteria(" + c1 + "," + c2 + ")" );
		checkCriterion(c1);
		checkCriterion(c2);
		return getAttributesByAddedCriteria(c1 + c2);
	}
	//-------------------------------------------------------------------------------------
	@VelocityNoDoc
	public List<AttributeInContext> getAttributesByCriteria( int c1, int c2, int c3 ) 
	{
		ContextLogger.log("getAttributesByCriteria(" + c1 + "," + c2 + "," + c3 + ")" );
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
		ContextLogger.log("getAttributesByCriteria(" + c1 + "," + c2 + "," + c3 + "," + c4 + ")" );
		checkCriterion(c1);
		checkCriterion(c2);
		checkCriterion(c3);
		checkCriterion(c4);
		return getAttributesByAddedCriteria(c1 + c2 + c3 + c4);
	}
	
	//-------------------------------------------------------------------------------------
	private List<AttributeInContext> getAttributesByAddedCriteria( int criteria ) 
	{
		ContextLogger.log("getAttributesByAddedCriteria(" + criteria + ")" );
		List<LinkInContext> allLinks = getLinks() ;
		List<LinkInContext> selectedLinks = getSelectedLinks() ;
		
		LinkedList<AttributeInContext> selectedAttributes = new LinkedList<AttributeInContext>();
		
		for ( AttributeInContext attribute : _attributes ) {
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
				selectedByLink = attribute.isUsedInLinkJoinColumn( allLinks ) ;
			}
			if ( ( criteria & Const.NOT_IN_LINKS ) != 0 ) {
				selectedByLink = ! attribute.isUsedInLinkJoinColumn( allLinks ) ;
			}
			
			//--- IS IN SELECTED LINK ?
			if ( ( criteria & Const.IN_SELECTED_LINKS ) != 0 ) {
				selectedBySelectedLink = attribute.isUsedInLinkJoinColumn( selectedLinks ) ;
			}			
			if ( ( criteria & Const.NOT_IN_SELECTED_LINKS ) != 0 ) {
				selectedBySelectedLink = ! attribute.isUsedInLinkJoinColumn( selectedLinks ) ;
			}
			
			int criteriaCount = 0 ;
			int selected = 0 ;
			if ( selectedByKey != null ) {
				criteriaCount++ ;
				if ( selectedByKey ) selected++ ;
			}
			if ( selectedByText != null ) {
				criteriaCount++ ;
				if ( selectedByText ) selected++ ;
			}
			if ( selectedByLink != null ) {
				criteriaCount++ ;
				if ( selectedByLink ) selected++ ;
			}
			if ( selectedBySelectedLink != null ) {
				criteriaCount++ ;
				if ( selectedBySelectedLink ) selected++ ;
			}

			ContextLogger.log("getAttributesByAddedCriteria(" + criteria + ") : " + attribute.getName() + " : " + criteriaCount + " :: " + selected );
			
			if ( ( criteriaCount > 0 ) && ( selected == criteriaCount ) ) {	
				// All criteria verified ( "AND" ) => keep this attribute
				selectedAttributes.add(attribute) ;
			}
			
		} // for each ...
		if ( selectedAttributes.size() > 0 ) {
			return selectedAttributes ;
		}
		
		return VOID_ATTRIBUTES_LIST ;
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
	public List<AttributeInContext> getKeyAttributes() 
	{
		if ( _keyAttributes != null ) {
			return _keyAttributes ;
		}
		return VOID_ATTRIBUTES_LIST ;
	}
	
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
    	return keyAttributesNamesAsString(separator, "", "");
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
    	if( this.hasPrimaryKey() ) {
               StringBuilder sb = new StringBuilder();
               int n = 0 ;
               for ( AttributeInContext attribute : this.getKeyAttributes() ) {
                      n++ ;
                      if ( n > 1 ) sb.append(separator);
                      sb.append(prefix);
                      sb.append(attribute.getName());
                      sb.append(suffix);
               }
               return sb.toString();
        } else {
               return "no_primary_key_for_entity_" + this.getName() + "" ;
        }
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
		if ( _keyAttributes != null ) {
			return _keyAttributes.size() ;
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
	public List<AttributeInContext> getNonKeyAttributes() 
	{
		if ( _nonKeyAttributes != null ) {
			return _nonKeyAttributes ;
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
	public int getNonKeyAttributesCount() 
	{
		if ( _nonKeyAttributes != null ) {
			return _nonKeyAttributes.size() ;
		}
		return 0 ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns the database table mapped with this entity"
		},
		example="$entity.databaseTable"
	)
	public String getDatabaseTable() 
	{
		return _sDatabaseTable ;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns the database catalog of the table mapped with this entity"
		},
		example="$entity.databaseCatalog"
	)
	public String getDatabaseCatalog() 
	{
		return _sDatabaseCatalog ;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns the database schema of the table mapped with this entity"
		},
		example="$entity.databaseSchema"
	)
	public String getDatabaseSchema() 
	{
		return _sDatabaseSchema ;
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
	public List<ForeignKeyInContext> getDatabaseForeignKeys() 
	{
		if ( _foreignKeys != null )
		{
			return _foreignKeys ;
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
	public int getDatabaseForeignKeysCount() 
	{
		if ( _foreignKeys != null )
		{
			return _foreignKeys.size() ;
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
	public String getDatabaseType() 
	{
		return _sDatabaseType ;
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
	public boolean isTableType() 
	{
		if ( _sDatabaseType != null ) {
			return "TABLE".equalsIgnoreCase( _sDatabaseType.trim() ) ;
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
	public boolean isViewType() 
	{
		if ( _sDatabaseType != null ) {
			return "VIEW".equalsIgnoreCase( _sDatabaseType.trim() ) ;
		}
		return false;
	}
	
//	//-------------------------------------------------------------------------------------
//	@VelocityMethod ( text= { 
//			"Returns a String containing all the columns of the Primary Key",
//			"The returned column names are separated by a comma and have quotes characters",
//			"i.e. : '\"code\", \"type\"' "
//		},
//		example={	
//			"String KEY_COLUMNS[] = { $entity.sqlKeyColumns };"
//		}
//	)
//	public String getSqlKeyColumns() 
//	{
////		if ( _sSqlKeyColumns == null ) // list not yet built
////		{
////			_sSqlKeyColumns = buildDbColumnsList( true ); 
////		}
////		return _sSqlKeyColumns ;
//		return buildDbColumnsList( true ); 
//	}
	
//	//-------------------------------------------------------------------------------------
//	@VelocityMethod ( text= { 
//			"Returns a String containing all the columns not used in the Primary Key ",
//			"The returned column names are separated by a comma and have quotes characters",
//			"i.e. : '\"code\", \"type\"' "
//		},
//		example={	
//			"String DATA_COLUMNS[] = { $entity.sqlNonKeyColumns };"
//		}
//	)
//	public String getSqlNonKeyColumns() 
//	{
////		if ( _sSqlNonKeyColumns == null ) // list not yet built
////		{
////			_sSqlNonKeyColumns = buildDbColumnsList( false ); 
////		}
////		return _sSqlNonKeyColumns ;
//		return buildDbColumnsList( false ); 
//	}

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
	public List<AttributeInContext> getNonTextAttributes() 
	{
//		if ( _nonTextAttributes == null ) // list not yet built
//		{
//			_nonTextAttributes = buildTextAttributesList ( false ); // NOT LONG TEXT
//			if ( _nonTextAttributes != null ) {
//				return _nonTextAttributes ;
//			}
//		}
//		return VOID_ATTRIBUTES_LIST ;
		return buildTextAttributesList ( false ); // NOT LONG TEXT
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
	public List<AttributeInContext> getTextAttributes() 
	{
//		if ( _textAttributes == null ) // list not yet built
//		{
//			_textAttributes = buildTextAttributesList ( true ); // Special "LONG TEXT"
//			if ( _textAttributes != null ) {
//				return _textAttributes ;
//			}
//		}
//		return VOID_ATTRIBUTES_LIST ;
		return buildTextAttributesList ( true ); // Special "LONG TEXT"
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
	public boolean hasTextAttribute() 
	{
    	if ( _attributes != null )
    	{
    		int n = _attributes.size();
        	for ( int i = 0 ; i < n ; i++ )        		
        	{
        		AttributeInContext attribute = (AttributeInContext) _attributes.get(i);
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
	public boolean hasCompositePrimaryKey() 
	{
		if ( _keyAttributes != null ) {
			return _keyAttributes.size() > 1 ;
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
	public boolean hasPrimaryKey() 
	{
		if ( _keyAttributes != null ) {
			return _keyAttributes.size() > 0 ;
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
	public boolean hasAutoIncrementedKey() 
	{
		if ( _keyAttributes != null ) {
			for ( AttributeInContext keyAttribute : _keyAttributes ) {
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
		List<AttributeInContext> keyAttributes = getKeyAttributes();
    	if ( keyAttributes != null )
    	{
        	if ( keyAttributes.size() == 1 ) 
    		{
    			// Only one attribute in the PK
    			AttributeInContext attribute = keyAttributes.get(0);
    			if ( attribute != null ) {
                    if ( attribute.isAutoIncremented() ) 
                    {
                    	// This unique PK field is auto-incremented => return it
                    	return attribute ; 
                    }
    			}
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
		List<String> referencedEntityTypes = new LinkedList<String>();
		for ( AttributeInContext attribute : attributes ) {
			//--- Is this attribute involved in a link ?
			for( LinkInContext link : this.getLinks()  ) {
//				if( link.isOwningSide() && link.hasJoinColumns() ) {
//					for( String joinColumn : link.getJoinColumns() ) {
//						if( joinColumn.equals(attribute.getDatabaseName() ) ) {						
//							String referencedEntityType = link.getTargetEntitySimpleType() ;
//							if ( referencedEntityTypes.contains(referencedEntityType) == false ) {
//								//--- Not already in the list => add it
//								referencedEntityTypes.add( link.getTargetEntitySimpleType() );
//							}
//						}
//					}
//				}
				if( link.isOwningSide() ) {
					//--- Only if the link uses one of the given attributes
					if ( link.usesAttribute(attribute) ) {
						String referencedEntityType = link.getTargetEntitySimpleType() ;
						//--- Found => add it in the list
						if ( referencedEntityTypes.contains(referencedEntityType) == false ) {
							//--- Not already in the list => add it
							referencedEntityTypes.add( link.getTargetEntitySimpleType() );
						}
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
		List<String> referencedEntityTypes = new LinkedList<String>();
		//--- Search all the referenced entities (from all the "owning side" links)
		for( LinkInContext link : this.getLinks()  ) {
			if ( link.isOwningSide() ) {
				String referencedEntityType = link.getTargetEntitySimpleType() ;
				//--- Found => add it in the list
				if ( referencedEntityTypes.contains(referencedEntityType) == false ) {
					//--- Not already in the list => add it
					referencedEntityTypes.add( referencedEntityType );
				}
			}
		}
		return referencedEntityTypes ;
    }

	//-------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------------------
	private LinkedList<AttributeInContext> buildAttributesList ( boolean bKeyAttribute ) 
	{
		LinkedList<AttributeInContext> attributesList = new LinkedList<AttributeInContext>();
    	if ( _attributes != null )
    	{
    		int n = _attributes.size();
        	for ( int i = 0 ; i < n ; i++ )        		
        	{
        		AttributeInContext attribute = _attributes.get(i);
                if ( attribute.isKeyElement() == bKeyAttribute ) 
                {
                	attributesList.add(attribute);
                }        		
        	}
    	}
		return attributesList ;
	}

	//-----------------------------------------------------------------------------------------------
	/**
	 * This method closes the definition of the class (when all the attributes have been added) <br>
	 * 
	 * It build the "KEY" and "NON KEY" attributes 
	 * 
	 * It determines if there is import types collision ( eg "java.util.Date" with "java.sql.Date" ) <br>
	 * and managed the imports list and attributes declarations types to avoid imports error
	 *  
	 */
	private void endOfAttributesDefinition() // v 2.1.0
	{
		if ( _attributes == null ) return ;
		
		//--- Build the list of the "KEY" attributes
		_keyAttributes = buildAttributesList ( true );
		
		//--- Build the list of the "NON KEY" attributes
		_nonKeyAttributes = buildAttributesList ( false ); 

		//--- Duplicated short types detection
		AmbiguousTypesDetector duplicatedTypesDetector = new AmbiguousTypesDetector(_attributes);
		List<String> ambiguousTypes = duplicatedTypesDetector.getAmbiguousTypes();
		for ( AttributeInContext attribute : _attributes ) {
			//--- Is this attribute's type ambiguous ?
			if ( ambiguousTypes.contains( attribute.getFullType() ) ) {
				//--- Yes => force this attribute to use its "full type" for variable declaration
				attribute.useFullType() ; // v 2.0.7
			}
		}
	}
	
//	private String buildDbColumnsList ( boolean bKeyAttribute ) 
//	{
//    	if ( _attributes != null )
//    	{
//            StringBuffer sb = new StringBuffer();
//            int iCount = 0 ;
//    		int n = _attributes.size();
//        	for ( int i = 0 ; i < n ; i++ )        		
//        	{
//        		AttributeInContext attribute = (AttributeInContext) _attributes.get(i);
//                if ( attribute.isKeyElement() == bKeyAttribute ) 
//                {
//                	if ( iCount > 0 ) // Not the first one
//                	{
//                        sb.append( ", " ) ;
//                	}
//                    sb.append( "\"" + attribute.getDatabaseName().trim() + "\"" ) ;
//                    iCount++ ;
//                }        		
//        	}
//    		return sb.toString() ;
//    	}
//    	return "" ;
//	}

	/**
	 * "Text" or "non Text" attributes
	 * @param bLongText
	 * @return
	 */
	private LinkedList<AttributeInContext> buildTextAttributesList ( boolean bLongText ) 
	{
    	if ( _attributes != null )
    	{
			LinkedList<AttributeInContext> list = new LinkedList<AttributeInContext>();
    		int n = _attributes.size();
        	for ( int i = 0 ; i < n ; i++ )        		
        	{
        		AttributeInContext attribute = _attributes.get(i);
                if ( attribute.isLongText() == bLongText ) 
                {
                	list.add(attribute);
                }        		
        	}
    		return list ;
    	}
    	return null ;
	}

}
