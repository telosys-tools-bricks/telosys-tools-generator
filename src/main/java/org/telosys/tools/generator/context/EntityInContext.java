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

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.telosys.tools.commons.ListUtil;
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
import org.telosys.tools.generic.model.TagContainer;

/**
 * Entity loaded from a model and exposed in the generator context <br>
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
	private static final List<EntityInContext>     VOID_ENTITIES_LIST      = new LinkedList<>();
	private static final List<ReferenceInContext>  VOID_REFERENCES_LIST    = new LinkedList<>();
	
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

	private final List<ForeignKeyInContext> foreignKeys ; // The database FOREIGN KEYS attributes for this entity
	
	private final List<LinkInContext> links ; // The links for this class ( ALL LINKS )
	
	private final ModelInContext modelInContext ;  // v 3.0.0
	
	private final EnvInContext   env ; // ver 2.1.0
	
	private final TagContainer tagContainer ; // All tags defined for the entity  ( added in v 3.4.0 )
	
	private final String  superClass ; // v 3.4.0
	private final boolean isAbstract ; // v 3.4.0
	private final boolean isInMemoryRepository ; // v 3.4.0
	private final boolean isReadOnly; // v 3.4.0
	private final boolean isAggregateRoot; // v 3.4.0
	private final String  domain; // v 3.4.0
	private final String  context; // v 3.4.0
	private final boolean isDatabaseView; // v 3.4.0
	private final String  databaseTablespace; // v 3.4.0	

	//-----------------------------------------------------------------------------------------------
	/**
	 * Constructor
	 * @param entity
	 * @param defaultEntityPackage
	 * @param modelInContext
	 * @param env
	 */
	public EntityInContext( final Entity entity, final String defaultEntityPackage, 
							final ModelInContext modelInContext, final EnvInContext env ) 
	{
		if ( entity == null ) {
			throw new IllegalArgumentException("Entity is null");
		}
		this.className = entity.getClassName();
		
		if ( nullOrVoid(entity.getPackageName()) ) {
			// no package defined in the model @Package) => use default 
			this.packageName = defaultEntityPackage;
		}
		else {
			// package explicitly defined in the model ( @Package(xx) )
			this.packageName = entity.getPackageName();
		}
		
		if ( modelInContext == null ) {
			throw new IllegalArgumentException("ModelInContext is null");
		}
		this.modelInContext = modelInContext ;
		
		if ( env == null ) {
			throw new IllegalArgumentException("EnvInContext is null");
		}
		this.env = env ;

		this.databaseTable   = entity.getDatabaseTable();
		
		this.databaseCatalog = entity.getDatabaseCatalog(); 
		
		this.databaseSchema  = entity.getDatabaseSchema();
		
		this.databaseType    = StrUtil.notNull(entity.getDatabaseType()); 

		this.databaseComment = entity.getDatabaseComment();
		
		//--- Initialize all the ATTRIBUTES for the current entity
		this.attributes = new LinkedList<>();
		for ( Attribute attribute : entity.getAttributes() ) { // v 3.0.0
			AttributeInContext attributeInContext = new AttributeInContext(this, attribute, this.modelInContext, this.env);
			this.attributes.add(attributeInContext);
		}

		//--- Initialize all the LINKS for the current entity
		this.links = new LinkedList<>();
		for ( Link link : entity.getLinks() ) { 
			LinkInContext linkInContext = new LinkInContext(this, link, this.modelInContext, this.env ); 
			this.links.add(linkInContext);
		}
		
		//--- Init all the DATABASE FOREIGN KEYS 
		this.foreignKeys = new LinkedList<>();
		for ( ForeignKey fk : entity.getForeignKeys() ) { 
			this.foreignKeys.add( new ForeignKeyInContext(fk, modelInContext, env) );
		}
		
		//--- Build the list of the "KEY" attributes
		this.keyAttributes = selectAttributesIfKeyElement(true);
		
		//--- Build the list of the "NON KEY" attributes
		this.nonKeyAttributes = selectAttributesIfKeyElement(false); 

		this.tagContainer = entity.getTagContainer(); 
		
		this.superClass = entity.getSuperClass() ; // v 3.4.0
		this.isAbstract = entity.isAbstract() ; // v 3.4.0
		this.isInMemoryRepository  = entity.isInMemoryRepository() ; // v 3.4.0
		this.isReadOnly  = entity.isReadOnly() ; // v 3.4.0
		this.isAggregateRoot = entity.isAggregateRoot() ; // v 3.4.0
		this.domain = entity.getDomain(); // v 3.4.0
		this.context = entity.getContext(); // v 3.4.0
		this.isDatabaseView = entity.isDatabaseView(); // v 3.4.0
		this.databaseTablespace = entity.getDatabaseTablespace(); // v 3.4.0

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
	 * Returns the entity class name without the package ( ie : "MyClass" )
	 * @return
	 */
	@VelocityMethod ( text= { 
			"Returns the entity class name without the package ( ie : \"MyClass\" )"
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
	public String getPackage() {
        return voidIfNull(this.packageName) ;
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
	public String getFullName() {
		return packageName + "." + getName();
    }
	
	/* (non-Javadoc)
	 * Same as getName() 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		// NB : must return only the class name => do not change
		// Usage example in ".vm" : ${beanClass}.class 
		return getName() ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns the attribute associated with the given database column name",
			"or throws an error if not found"
		},
		parameters = {
			"columnName : the database column name"
		},
		example="$entity.getAttributeByColumnName('column_name')"
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
	@VelocityMethod ( text= { 
			"Returns TRUE if the entity has an attribute with the given name"
		},
		example= {
			"#if ( $entity.hasAttribute('foo') )",
			"...",
			"#end"
		},
		since="3.4.0"
	)
	public boolean hasAttribute(String attributeName) {
		return getAttributeWithName(attributeName) != null ;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns the attribute associated with the given name",
			"or throws an error if not found"
		},
		parameters = {
			"attributeName : the attribute name in the entity"
		},
		example="$entity.getAttributeByName('foo')",
		since="3.4.0"
	)
	@VelocityReturnType("'attribute' object")	
	public AttributeInContext getAttributeByName(String attributeName) throws GeneratorException {
		if ( StrUtil.nullOrVoid(attributeName) ) {
			throw new GeneratorException("Invalid argument : 'attributeName' is null or empty");
		}
		AttributeInContext a = getAttributeWithName(attributeName);
		if ( a != null ) {
			return a ;
		}
		else {
			throw new GeneratorException("No attribute with name '" + attributeName + "'");
		}
	}
	
	private AttributeInContext getAttributeWithName(String attributeName) {
		if ( attributeName != null ) {
			for( AttributeInContext attribute : this.getAttributes() ) {
				if ( attributeName.equals( attribute.getName() ) ) {
					return attribute ;
				}
			}
		}
		return null;
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
			"Returns TRUE if this entity has at least one collection "
		},
		example= {
			"#if ( $entity.hasCollections() )",
			"...",
			"#end"
		},
		since="3.4.0"
	)
	public boolean hasCollections() {
		if ( links != null ) {
			for ( LinkInContext link : links ) {
				if ( link.isCollectionType() ) {
					return true ;
				}
			}
		}
		return false;
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
			"Returns a list containing all entities referenced by the current entity",
			"(only for the first level of dependency)"
		},
		example={	
			"#foreach( $refEntity in $entity.referencedEntities )",
			"...",
			"#end" 
		},
		since="3.4.0"
	)
	@VelocityReturnType("List of 'entity' objects")
	public Collection<EntityInContext> getReferencedEntities() {
		if ( links != null ) {
			Map<String, EntityInContext> map = new HashMap<>();
			for ( LinkInContext link : links ) {
				try {
					EntityInContext entity = link.getTargetEntity();
					map.put(entity.getName(), entity);
				} catch (GeneratorException e) {
					// Invalid link (no no target entity) => just ignore it
				}
			}
			return map.values();
		}
		return VOID_ENTITIES_LIST;		
	}	
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns a list containing all entities referenced by the current entity",
			"at all levels of dependencies tree (including all sub levels)"
		},
		example={	
			"#foreach( $refEntity in $entity.referencedEntitiesForAllLevels )",
			"...",
			"#end" 
		},
		since="3.4.0"
	)
	@VelocityReturnType("List of 'entity' objects")
	public Collection<EntityInContext> getReferencedEntitiesForAllLevels() {
		Map<String, EntityInContext> map = new HashMap<>();
		registerReferencedEntities(getLinks(), map, 1, 100); 
		return map.values();
	}
	private void registerReferencedEntities(List<LinkInContext> entityLinks, Map<String, EntityInContext> map, 
			int level, int maxLevel) {
		if ( entityLinks != null && level <= maxLevel) {
			for ( LinkInContext link : entityLinks ) {
				try {
					EntityInContext targetEntity = link.getTargetEntity(); // throws GeneratorException
					map.put(targetEntity.getName(), targetEntity);
					if ( targetEntity.hasLinks() ) {
						registerReferencedEntities(targetEntity.getLinks(), map, level+1, maxLevel); // recursive call
					}
				} catch (GeneratorException e) {
					// Invalid link (no no target entity) => just ignore it
				}
			}
		}
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns a list containing all references hold by the current entity",
			"(only for the first level of dependency)"
		},
		example={	
			"#foreach( $reference in $entity.references )",
			"...",
			"#end" 
		},
		since="3.4.0"
	)
	@VelocityReturnType("List of 'reference' objects")
	public Collection<ReferenceInContext> getReferences() {
		if ( links != null ) {
			Map<String, ReferenceInContext> map = new HashMap<>();
			for ( LinkInContext link : links ) {
				registerReference(link, map);
			}
			return map.values();
		}
		return VOID_REFERENCES_LIST;		
	}
	private List<LinkInContext> registerReference(LinkInContext link, Map<String, ReferenceInContext> map ) {
		try {
			EntityInContext targetEntity = link.getTargetEntity(); // throws GeneratorException
			ReferenceInContext reference = map.get(targetEntity.getName());
			if ( reference == null ) {
				// new reference => init
				reference = new ReferenceInContext(targetEntity);
				map.put(targetEntity.getName(), reference);
			}
			if ( link.isCardinalityToMany() ) {
				reference.incrementToMany();
			}
			else {
				reference.incrementToOne();
			}
			return targetEntity.getLinks(); 
		} catch (GeneratorException e) {
			// Invalid link (no no target entity) 
			// Not supposed to happen => just ignore it
			return null;
		}		
	}
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns a list containing all references hold by the current entity",
			"in all levels of dependencies tree (including all sub levels)"
		},
		example={	
			"#foreach( $reference in $entity.referencesInDepth )",
			"...",
			"#end" 
		},
		since="3.4.0"
	)
	@VelocityReturnType("List of 'reference' objects")
	public Collection<ReferenceInContext> getReferencesInDepth() {
		Map<String, ReferenceInContext> map = new HashMap<>();
		registerReferences(getLinks(), map, 1, 100); 
		return map.values();
	}

	private void registerReferences(List<LinkInContext> entityLinks, Map<String, ReferenceInContext> map, 
			int level, int maxLevel) {
		if ( entityLinks != null && level <= maxLevel) {
			for ( LinkInContext link : entityLinks ) {
				List<LinkInContext> subLinks = registerReference(link, map);
				if ( subLinks != null ) {
					// continue in depth with sublinks
					registerReferences(subLinks, map, level+1, maxLevel); // recursive call
				}
			}
		}
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
			"Returns the 'database table name' mapped with this entity in the model",
			"or an empty string if no table is specified in the model"
		},
		example="$entity.databaseTable"
	)
	public String getDatabaseTable() {
		return voidIfNull(this.databaseTable) ;
	}
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns TRUE if this entity has a 'database table name' explicitly defined in the model"
		},
		example= {
			"#if ( $entity.hasDatabaseTable() )",
			"...",
			"#end"
		},
		since="3.4.0"
	)
	public boolean hasDatabaseTable() {
		return ! nullOrVoid(databaseTable);
	}
		
	//-------------------------------------------------------------------------------------
	@VelocityMethod( text={	
			"Returns the database table name for the given entity ",
			"If the table name is defined in the model it is used in priority",
			"if no table name is defined then the entity name is converted to table name",
			"by applying the target database conventions",
			"(for example 'student_projects' for an entity named 'StudentProjects')",					
			""
			},
		since="3.4.0"
	)
	public String getSqlTableName() {
		SqlInContext sql = this.env.getSql();
		return sql.tableName(this);
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns a list of SQL column names for all the attributes making up the Primary Key",
			"(returns a void list if no Primary Key)"
		},
		example= {
			"#foreach( $col in $entity.sqlPrimaryKeyColumns )",
			"...",
			"#end"
		}
	)
	@VelocityReturnType("List of strings")
	public List<String> getSqlPrimaryKeyColumns() {
		List<String> list = new LinkedList<>();
		SqlInContext sql = this.env.getSql();
		if ( keyAttributes != null ) {
			for ( AttributeInContext attribute : keyAttributes ) {
				list.add(sql.columnName(attribute));
			}
		}
		return list ;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
		"Returns a string containing SQL column names for all the attributes making up the Primary Key",
		"the column names are separated by a comma ",
		"(returns a void string if no Primary Key)"
	}
	)
	public String getSqlPrimaryKeyColumnsAsString() {
		return ListUtil.join(getSqlPrimaryKeyColumns(), ",");
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns TRUE if the entity has a 'database catalog' explicitly defined in the model"
		},
		example= {
			"#if ( $entity.hasDatabaseCatalog() )",
			"...",
			"#end"
		},
		since="3.4.0"
	)
	public boolean hasDatabaseCatalog() { // v 3.4.0
		return ! nullOrVoid(this.databaseCatalog) ;
	}
	
	@VelocityMethod ( text= { 
			"Returns the 'database catalog' of the table mapped with this entity"
		},
		example="$entity.databaseCatalog"
	)
	public String getDatabaseCatalog() {
		return voidIfNull(databaseCatalog);
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns TRUE if the entity has a 'database schema' explicitly defined in the model"
		},
		example= {
			"#if ( $entity.hasDatabaseSchema() )",
			"...",
			"#end"
		},
		since="3.4.0"
	)
	public boolean hasDatabaseSchema() { // v 3.4.0
		return ! nullOrVoid(this.databaseSchema) ;
	}
	
	@VelocityMethod ( text= { 
			"Returns the 'database schema' of the table mapped with this entity"
		},
		example="$entity.databaseSchema"
	)
	public String getDatabaseSchema() {
		return voidIfNull(this.databaseSchema);
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
			"Returns the real database type for the table mapped with this entity",
			"('TABLE' or 'VIEW') "
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
			"Returns TRUE if the entity has a 'database comment' explicitly defined in the model"
		},
		example= {
			"#if ( $entity.hasDatabaseComment() )",
			"...",
			"#end"
		},
		since="3.4.0"
	)
	public boolean hasDatabaseComment() { // v 3.4.0
		return ! nullOrVoid(this.databaseComment) ;
	}
	
	@VelocityMethod ( text= { 
			"Returns the 'database comment' for the table mapped with this entity"
		},
		example="$entity.databaseComment",
		since="3.1.0"
	)
	public String getDatabaseComment() {
		return voidIfNull(databaseComment);
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
    	if ( attributes != null ) {
        	for ( AttributeInContext attribute : attributes ) {
                if ( attribute.isLongText() ) {
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
			"Returns TRUE if this entity has at least one Foreign Key "
		},
		example= {
			"#if ( $entity.hasForeignKeys() )",
			"...",
			"#end"
		},
		since="3.4.0"
	)
	public boolean hasForeignKeys() {
		return ( foreignKeys != null && ! foreignKeys.isEmpty() ) ;
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
	
	//------------------------------------------------------------------------------------------
	// TAGS since ver 3.4.0
	//------------------------------------------------------------------------------------------
	
	@VelocityMethod(
		text={	
			"Returns TRUE if a tag is defined with the given name"
		},
		parameters = { 
			"tagName : name of the tag for which to check the existence" 
		},
		example= {
			"$enity.hasTag('mytag') "
		},
		since="3.4.0"
	)
	public boolean hasTag(String tagName) {
		return tagContainer.containsTag(tagName);
	}

	@VelocityMethod(
		text={	
			"Returns the value held by the given tag name",
			"If the tag is undefined or has no value, the returned value is an empty string"
		},
		parameters = { 
			"tagName : name of the tag for which to get the value" 
		},
		example= {
			"$enity.tagValue('mytag') "
		},
		since="3.4.0"
	)
	public String tagValue(String tagName) {
		return tagContainer.getTagValue(tagName);
	}

	@VelocityMethod(
		text={	
			"Returns the value held by the given tag name",
			"If the tag is undefined or has no value, the default value is returned"
		},
		parameters = { 
			"tagName : name of the tag for which to get the value" ,
			"defaultValue : default value if no tag or no value"
		},
		example= {
			"$enity.tagValue('mytag', 'abc') "
		},
		since="3.4.0"
	)
	public String tagValue(String tagName, String defaultValue) {
		return tagContainer.getTagValue(tagName, defaultValue);
	}

	@VelocityMethod(
		text={	
			"Returns the integer value held by the given tag name",
			"If the tag is undefined or has no value, the default value is returned"
		},
		parameters = { 
			"tagName : name of the tag for which to get the value" ,
			"defaultValue : default value if no tag or no value"
		},
		example= {
			"$enity.tagValueAsInt('mytag', 123) "
		},
		since="3.4.0"
	)
	public int tagValueAsInt(String tagName, int defaultValue) {
		return tagContainer.getTagValueAsInt(tagName, defaultValue);
	}

	@VelocityMethod(
		text={	
			"Returns the boolean value held by the given tag name",
			"If the tag is undefined or has no value, the default value is returned"
		},
		parameters = { 
			"tagName : name of the tag for which to get the value" ,
			"defaultValue : default value if no tag or no value"
		},
		example= {
			"$enity.tagValueAsBoolean('mytag', false) "
		},
		since="3.4.0"
	)
	public boolean tagValueAsBoolean(String tagName, boolean defaultValue) {
		return tagContainer.getTagValueAsBoolean(tagName, defaultValue);
	}

	//-------------------------------------------------------------------------------------
	// New in v 3.4.0
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns TRUE if the entity has a 'super class' (if its class extends another class)"
		},
		example= {
			"#if ( $entity.hasSuperClass() )",
			"...",
			"#end"
		},
		since="3.4.0"
	)
	public boolean hasSuperClass() { // v 3.4.0
		return ! nullOrVoid(this.superClass) ;
	}

	@VelocityMethod ( text= { 
			"Returns the 'super class' extended by the entity class"
		},
		example= {
			"$entity.superClass "
		},
		since="3.4.0"
	)
	public String getSuperClass() { // v 3.4.0
		return voidIfNull(this.superClass);
	}

	@VelocityMethod ( text= { 
			"Returns TRUE if the entity class is abstract "
		},
		example= {
			"#if ( $entity.isAbstract() )",
			"...",
			"#end"
		},
		since="3.4.0"
	)
	public boolean isAbstract() { // v 3.4.0
		return this.isAbstract;
	}

	@VelocityMethod ( text= { 
			"Returns TRUE if entity occurrences are stored in memory ( in a 'in-memory repository' ) "
		},
		example= {
			"#if ( $entity.isInMemoryRepository() )",
			"...",
			"#end"
		},
		since="3.4.0"
	)
	public boolean isInMemoryRepository() { // v 3.4.0
		return this.isInMemoryRepository;
	}
	
	@VelocityMethod ( text= { 
			"Returns TRUE if the entity occurrences are 'read only' "
		},
		example= {
			"#if ( $entity.isReadOnly() )",
			"...",
			"#end"
		},
		since="3.4.0"
	)
	public boolean isReadOnly() { // v 3.4.0
		return this.isReadOnly;
	}

	@VelocityMethod ( text= { 
			"Returns TRUE if the entity is an 'aggregate root'"
		},
		example= {
			"#if ( $entity.isAggregateRoot() )",
			"...",
			"#end"
		},
		since="3.4.0"
	)
	public boolean isAggregateRoot() { // v 3.4.0
		return this.isAggregateRoot;
	}

	@VelocityMethod ( text= { 
			"Returns TRUE if the entity belongs to a 'domain'"
		},
		example= {
			"#if ( $entity.hasDomain() )",
			"...",
			"#end"
		},
		since="3.4.0"
	)
	public boolean hasDomain() { // v 3.4.0
		return ! nullOrVoid(this.domain) ;
	}

	@VelocityMethod ( text= { 
			"Returns the 'domain' to which the entity belongs"
		},
		example= {
			"$entity.domain "
		},
		since="3.4.0"
	)
	public String getDomain() { // v 3.4.0
		return voidIfNull(this.domain);
	}

	@VelocityMethod ( text= { 
			"Returns TRUE if the entity belongs to a 'context'"
		},
		example= {
			"#if ( $entity.hasContext() )",
			"...",
			"#end"
		},
		since="3.4.0"
	)
	public boolean hasContext() { // v 3.4.0
		return ! nullOrVoid(this.context) ;
	}

	@VelocityMethod ( text= { 
			"Returns the 'context' to which the entity belongs"
		},
		example= {
			"$entity.context "
		},
		since="3.4.0"
	)
	public String getContext() { // v 3.4.0
		return voidIfNull(this.context);
	}

	@VelocityMethod ( text= { 
			"Returns TRUE if the entity data comes from a 'database view'"
		},
		example= {
			"#if ( $entity.isDatabaseView() )",
			"...",
			"#end"
		},
		since="3.4.0"
	)
	public boolean isDatabaseView() { // v 3.4.0
		return this.isDatabaseView ;
	}

	@VelocityMethod ( text= { 
			"Returns TRUE if the entity has a 'database tablespace' explicitly defined in the model"
		},
		example= {
			"#if ( $entity.hasDatabaseTablespace() )",
			"...",
			"#end"
		},
		since="3.4.0"
	)
	public boolean hasDatabaseTablespace() { // v 3.4.0
		return ! nullOrVoid(this.databaseTablespace) ;
	}
	
	@VelocityMethod ( text= { 
			"Returns the 'database tablespace' "
		},
		example= {
			"$entity.databaseTablespace "
		},
		since="3.4.0"
	)
	public String getDatabaseTablespace() { // v 3.4.0
		return voidIfNull(this.databaseTablespace);
	}
	
	
	//-------------------------------------------------------------------------------------

	private String voidIfNull(String s) {
		return s != null ? s : "" ;
	}
	private boolean nullOrVoid(String s) {
		if ( s == null ) return true ;
		if ( s.length() == 0 ) return true ;
		return false;
	}
}
