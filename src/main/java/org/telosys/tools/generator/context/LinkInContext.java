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
import org.telosys.tools.generator.GeneratorUtil;
import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.doc.VelocityReturnType;
import org.telosys.tools.generator.context.names.ContextName;
import org.telosys.tools.generic.model.CascadeOptions;
import org.telosys.tools.generic.model.Link;
import org.telosys.tools.generic.model.LinkAttribute;
import org.telosys.tools.generic.model.TagContainer;
import org.telosys.tools.generic.model.enums.BooleanValue;
import org.telosys.tools.generic.model.enums.Cardinality;
import org.telosys.tools.generic.model.enums.FetchType;
import org.telosys.tools.generic.model.enums.Optional;
import org.telosys.tools.generic.model.types.TypeConverter;

/**
 * Link exposed in the Velocity Context 
 *  
 * @author S.Labbe, L.Guerin
 */
//-------------------------------------------------------------------------------------
@VelocityObject(
	contextName = ContextName.LINK ,
	text = {
			"This object provides all information about an entity link",
			"Each link is retrieved from the entity class ",
			""
	},
	since = "",
	example= {
			"",
			"#foreach( $link in $entity.links )",
			"    private $link.formattedFieldType(10) $link.formattedFieldName(12);",
			"#end"				
	}		
 )
//-------------------------------------------------------------------------------------
public class LinkInContext {
	
    private final EntityInContext  entity ; // the entity to which the link belongs

	private final ModelInContext   modelInContext ;  // v 3.0.0 (replaces EntitiesManager)
	private final EnvInContext     envInContext ; // ver 3.3.0

//	private final List<JoinColumnInContext> joinColumns ;  // removed in v 3.4.0
	private final List<LinkAttributeInContext> linkAttributes ; // added in v 3.4.0  (replaces joinColumns)
//	private final JoinTableInContext        joinTable ;  // removed in v 3.4.0
	private final String joinEntityName ;  // added in v 3.4.0  (replaces joinTable)

	//--- Added in ver 3.0.0 (to replace reference / Link )
//	private final String       id ; // removed in v 3.4.0
	private final String       fieldName ;
//	private final String       targetTableName ; // removed in v 3.4.0
	private final String       targetEntityName ; // v 3.4.0
	private final String       mappedBy ;
	private final boolean      isSelected ;
	private final boolean      isOwningSide ;
	
	private final Cardinality    cardinality ;
	private final FetchType      fetchType ;
	private final Optional       optional ;
	private final CascadeOptions cascadeOptions ;
	
    private final BooleanValue  isInsertable ; // Added in v 3.3.0
    private final BooleanValue  isUpdatable  ; // Added in v 3.3.0
	
    private final boolean isTransient ; // Added in v 3.3.0
    private final boolean isEmbedded ; // Added in v 3.3.0
    
	private final TagContainer tagContainer ; // All tags defined for the link v 3.4.0

	//-------------------------------------------------------------------------------------
	/**
	 * Constructor
	 * @param entity
	 * @param link
	 * @param modelInContext
	 * @param envInContext
	 */
	public LinkInContext(EntityInContext entity, Link link, 
			ModelInContext modelInContext, EnvInContext envInContext ) 
	{
		this.entity = entity ;
		this.modelInContext = modelInContext ; // v 3.0.0
		this.envInContext = envInContext ; // v 3.3.0
		
// removed in v 3.4.0
//		//--- Build the list of "join columns"
//		this.joinColumns = new LinkedList<>();
//		if ( link.getJoinColumns() != null ) {
//			for ( JoinColumn joinColumn : link.getJoinColumns() ) {
//				this.joinColumns.add( new JoinColumnInContext(joinColumn) ) ;
//			}
//		}
		this.linkAttributes = new LinkedList<>();
//		if ( link.getJoinAttributes() != null ) {
		if ( link.getAttributes() != null ) {
//			for ( JoinAttribute joinAttribute : link.getJoinAttributes() ) {
			for ( LinkAttribute linkAttribute : link.getAttributes() ) {
				this.linkAttributes.add( new LinkAttributeInContext(modelInContext, entity, 
						link, 
						linkAttribute.getOriginAttributeName(),
						linkAttribute.getReferencedAttributeName()));
			}
		}

//		//--- Set the join table if any
//		if ( link.getJoinTable() != null ) {
//			this.joinTable = new JoinTableInContext( link.getJoinTable() ) ;
//		}
//		else {
//			this.joinTable = null ;
//		}
		this.joinEntityName = link.getJoinEntityName(); // added in v 3.4.0
		
		//--- Init link information (ver 3.0.0)
//		this.id = link.getId() ; // removed in v 3.4.0
		this.fieldName = link.getFieldName() ;
		// _fieldType = link.getFieldType(); // removed in v 3.3.0
//		this.targetTableName = link.getTargetTableName(); // removed in v 3.4.0
		this.targetEntityName = link.getReferencedEntityName();  // added in v 3.4.0
		this.isSelected = link.isSelected();
		this.mappedBy = link.getMappedBy(); // keep null if not defined
		this.isOwningSide = link.isOwningSide();
		
		this.cardinality = link.getCardinality();
		this.fetchType = link.getFetchType() != null ? link.getFetchType() : FetchType.DEFAULT ;
		this.optional = link.getOptional();
		this.cascadeOptions = link.getCascadeOptions();
		
		this.isInsertable = link.getInsertable();
		this.isUpdatable  = link.getUpdatable();
		this.isTransient = link.isTransient(); // v 3.3.0
		this.isEmbedded  = link.isEmbedded(); // v 3.3.0
		
		this.tagContainer = link.getTagContainer(); // V 3.4.0
	}
	
	/**
	 * Returns the collection type <br>
	 * depending on the current language and the specific collection type if any
	 * @param className
	 * @return
	 */
	private String buildCollectionType(String className) {
		// Before v 3.3.0 always return "List<" + className + ">" ; 
		// get collection type from current target language (added in v 3.3.0 )
		TypeConverter typeConverter = envInContext.getTypeConverter();
		// get collection type for the current language 
		String collectionType = typeConverter.getCollectionType(className);
		if ( collectionType != null ) {
			return collectionType ;
		}
		else {
			throw new IllegalStateException("Cannot get collection type (language="
					+ envInContext.getLanguage()+")");
		}
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the entity to which the link belongs ",
			""
			},
		since = "3.3.0"
	)
	public EntityInContext getEntity() {
		return entity;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the link's type with n trailing blanks ",
			"eg : List, List<Person>, Person, ..."
			},
		parameters = { 
			"n : the number of blanks to be added at the end of the name" 
			}
	)
	public String formattedFieldType(int iSize) throws GeneratorException
    {
		String currentType = getFieldType();
		String sTrailingBlanks = "";
        int iDelta = iSize - currentType.length();
        if (iDelta > 0) // if needs trailing blanks
        {
            sTrailingBlanks = GeneratorUtil.blanks(iDelta);
        }
        return currentType + sTrailingBlanks;
    }

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the link's name with n trailing blanks "
			},
		parameters = { 
			"n : the number of blanks to be added at the end of the name" 
			}
	)
	public String formattedFieldName(int iSize) {
        String s = this.getFieldName();
        String sTrailingBlanks = "";
        int iDelta = iSize - s.length();
        if (iDelta > 0) // if needs trailing blanks
        {
            sTrailingBlanks = GeneratorUtil.blanks(iDelta);
        }
        return s + sTrailingBlanks;
    }

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the Java getter for the link e.g. 'getPerson' for link 'person' "
			}
	)
	public String getGetter() { 
		// false : a link is never a "boolean"
		return Util.buildGetter(this.getFieldName(), false); // v 3.3.0
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the Java setter for the link e.g. 'setPerson' for link 'person' "
			}
	)
	public String getSetter() {
		return Util.buildSetter(this.getFieldName());
	}

//	//-------------------------------------------------------------------------------------
//	@VelocityMethod(
//		text={	
//			"Returns TRUE if the link has a 'join table'"
//			}
//	)
//	public boolean hasJoinTable() {
//		return joinTable != null ;
//	}	
//	//-------------------------------------------------------------------------------------
//	@VelocityMethod(
//		text={	
//			"Returns the name of the 'join table' for the link ",
//			"NB : can be null if the link doesn't have a 'join table'",
//			"check existence before with 'hasJoinTable()' "
//			}
//	)
//	public String getJoinTableName() {
//		if ( joinTable != null ) {
//			return joinTable.getName();
//		}
//		else {
//			return null ;
//		}
//	}
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the link has a 'join entity'"
			},
		since="3.4.0"
	)
	public boolean hasJoinEntity() {
		return ! StrUtil.nullOrVoid(this.joinEntityName) ;
	}
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the name of the 'join entity' used by the link ",
			"Returns an empty string if the link doesn't have a 'join entity'",
			"check existence before with 'hasJoinEntity()' "
			},
		since="3.4.0"
	)
	public String getJoinEntityName() {
		return voidIfNull(this.joinEntityName) ;
	}
	
//	//-------------------------------------------------------------------------------------
//	@VelocityMethod(
//		text={	
//			"Returns the 'join table' object for the link ",
//			"NB : can be null if the link doesn't have a 'join table' ",
//			"check existence before with 'hasJoinTable()' "			
//			}
//	)
//	public JoinTableInContext getJoinTable() {
//		return joinTable ;
//	}
//	//-------------------------------------------------------------------------------------
//	@VelocityMethod(
//		text={	
//			"Returns TRUE if the link has 'join columns' (at least one)"
//			}
//	)
//	public boolean hasJoinColumns() {
//		if ( joinColumns != null ) {
//			return ! joinColumns.isEmpty() ;
//		}
//		return false ;
//	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the link has one (or more) attribute in the entity Primary Key",
			"( one of its 'origin attributes' is the Primary Key or a part of the Primary Key )"
			},
		since="2.1.0"
	)
	public boolean hasAttributeInPrimaryKey() throws GeneratorException {
//		if ( joinColumns != null ) {
//			for ( JoinColumnInContext jc : joinColumns ) {
//				//--- ORIGIN attribute
//				AttributeInContext attribOrigin = entity.getAttributeByColumnName(jc.getName());
//				if ( attribOrigin.isKeyElement() ) {
//					return true ;
//				}
//			}
//		}
		// v 3.4.0
		for ( LinkAttributeInContext a : linkAttributes) { 
			//--- Search ORIGIN attribute in entity
			AttributeInContext attribOrigin = entity.getAttributeByName(a.getOriginAttributeName());
			if ( attribOrigin.isKeyElement() ) {
				return true ;
			}
		}
		return false ;
	}
	
//	//-------------------------------------------------------------------------------------
//	@VelocityMethod(
//		text={	
//			"Returns the 'join columns' for the link "
//			}
//	)
//	public List<JoinColumnInContext> getJoinColumns() {
//		return joinColumns ;
//	}
	
//	//-------------------------------------------------------------------------------------
//	@VelocityMethod(
//		text={	
//			"Returns the 'join attributes' for the link "
//			}
//	)
//	public List<JoinAttributeInContext> getJoinAttributes() {
//		return joinAttributes ;
//	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns the number of attributes used to define the link"
		},
		example="$link.attributesCount",
		since="2.1.0"
	)
	public int getAttributesCount() {
//		if ( joinColumns != null ) {
//			return joinColumns.size() ;
//		}
//		return 0 ;
		return linkAttributes.size(); // never null
	}

//	//-------------------------------------------------------------------------------------
//	@VelocityMethod(
//		text={	
//			"Returns a list of attributes pair defining the link ",
//			"Each pair contains the owning side attribute and its corresponding reverse side attribute ",
//			"Each link is supposed to contain at least 1 pair of attributes"
//			}
//	)
//	@VelocityReturnType("List of '$linkAttribute' (origin-target association) ")	
//	public List<LinkAttributesPairInContext> getAttributes() throws GeneratorException {
//		List<LinkAttributesPairInContext> list = new LinkedList<>();
////		if ( joinColumns != null ) {
////			for ( JoinColumnInContext jc : joinColumns ) {
////				//--- ORIGIN attribute
////				AttributeInContext attribOrigin = entity.getAttributeByColumnName(jc.getName());
////				//--- TARGET attribute
////				EntityInContext referencedEntity = this.getTargetEntity();
////				AttributeInContext attribTarget = referencedEntity.getAttributeByColumnName(jc.getReferencedColumnName());
////				//--- New attribute mapping in the list
////				list.add( new LinkAttributesPairInContext(attribOrigin, attribTarget) );
////			}
////		}
//		for ( JoinAttributeInContext ja : joinAttributes ) {
//			//--- ORIGIN attribute
//			AttributeInContext attribOrigin = entity.getAttributeByName(ja.getOriginAttributeName());
//			//--- TARGET attribute
//			EntityInContext referencedEntity = this.getTargetEntity();
//			AttributeInContext attribTarget = referencedEntity.getAttributeByName(ja.getReferencedAttributeName());
//			//--- New attribute mapping in the list
//			list.add( new LinkAttributesPairInContext(attribOrigin, attribTarget) );
//		}
//		return list ;
//	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns a list of all attributes defining the link ",
			"Each element contains the owning side attribute and its corresponding reverse side attribute "
			}
	)
	@VelocityReturnType("List of '$linkAttribute' (origin-target association) ")	
	public List<LinkAttributeInContext> getAttributes() {
		return this.linkAttributes;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the given attribute is an origin attribute in the link  "
		},
		parameters = { 
			"attribute : the attribute to be checked" 
			}
	)
	public boolean usesAttribute(AttributeInContext attribute) {
//		if ( joinColumns != null ) {
//			for ( JoinColumnInContext jc : joinColumns ) {
//				if ( attribute.getDatabaseName().equals( jc.getName() ) ) {
//					return true ;
//				}
//			}
//		}
		for ( LinkAttributeInContext linkAttribute : linkAttributes ) {
			if ( attribute.getName().equals( linkAttribute.getOriginAttributeName()) ) {
				return true ;
			}
		}
		return false ; 
	}
	
//	//-------------------------------------------------------------------------------------
//	@VelocityMethod(
//		text={	
//			"Returns the unique id of the link in the repository (id used by the tool)",
//			"(not supposed to be used in a generated file)"
//			}
//	)
//	public String getId() { // removed in v 3.4.0
//		return id ;
//	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the link is selected (checkbox checked in the GUI)"
			}
	)
	public boolean isSelected() {
		return isSelected ;
	}

//	//-------------------------------------------------------------------------------------
//	@VelocityMethod(
//		text={	
//			"Returns the name of the target table (table referenced by the link)"
//			}
//	)
//	public String getTargetTableName() { // removed in v 3.4.0
//		return targetTableName ;
//	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the field name for the link (attribute name in the entity class)"
			}
	)
	public String getFieldName() {
		return fieldName ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the type of the link ",
			"eg : Person, List<Person>,  ..."
			}
	)
	public String getFieldType() throws GeneratorException {
		String targetEntityClassName = this.getTargetEntitySimpleType() ;
		if ( this.isCollectionType() ) {
			return buildCollectionType(targetEntityClassName);
		} else {
			return targetEntityClassName ;
		}
	}	
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the link is the 'Owning Side' of the relationship between 2 entities"
			}
	)
	public boolean isOwningSide() {
		return isOwningSide ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the name of the link in the 'owning side' ",
			"Typically for JPA 'mappedBy'",
			"NB : can be null if 'mappedBy' is not defined ",
			"check existence before with 'hasMappedBy()' "			
			}
	)
	public String getMappedBy() {
		return mappedBy;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if 'mappedBy' is defined"
			},
		since="3.3.0"
	)
	public boolean hasMappedBy() {
		return mappedBy != null ;
	}
	

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the entity referenced by the link ",
			""
			},
		since = "2.1.0"
	)
//	public EntityInContext getTargetEntity() throws GeneratorException {
////		String targetTableName = getTargetTableName();
//		if ( targetTableName == null ) {
//			throw new GeneratorException("Cannot get target entity. No target table name for link '" + getId() + "'" );
//		}
//		
//		EntityInContext targetEntity = modelInContext.getEntityByTableName( targetTableName ); // v 3.0.0
//		if ( targetEntity == null ) {
//			throw new GeneratorException("Cannot get target entity. No entity for table name '" + targetTableName + "'" );
//		}
//		return targetEntity ;
//	}
	// new version based on 'targetEntityName'
	public EntityInContext getTargetEntity() throws GeneratorException {
		if ( this.targetEntityName == null ) {
			throw new GeneratorException("No target entity name in link '" + this.fieldName + "'" );
		}
		EntityInContext targetEntity = modelInContext.getEntityByClassName(this.targetEntityName);
		if ( targetEntity == null ) {
			throw new GeneratorException("Unknown target entity '" + this.targetEntityName 
					+ "' in link '" + this.fieldName + "'");
		}
		return targetEntity ;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the name of the entity referenced by the link ",
			"eg : 'Book', 'Customer', ...",
			""
			},
		since="3.4.0"
	)
	public String getTargetEntityName() { // v 3.4.0
		return voidIfNull(this.targetEntityName);
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the type of the entity referenced by the link ",
			"eg : 'Book', 'Customer', ...",
			""
			}
	)
	public String getTargetEntitySimpleType() throws GeneratorException {
		// return this.getTargetEntity().getName();
		return voidIfNull(this.targetEntityName); // v 3.4.0
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the type of the entity referenced by the link ",
			"eg : 'my.package.Book', 'my.package.Customer', ...",
			""
			}
	)
	public String getTargetEntityFullType() throws GeneratorException {
		return this.getTargetEntity().getFullName();	
	}


	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the cardinality of the link ",
			"eg : 'OneToMany', 'ManyToOne', 'OneToOne', 'ManyToMany'"
			}
	)
	public String getCardinality() { 
		return cardinality.getText();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the link has a 'OneToOne' cardinality"
			}
	)
	public boolean isCardinalityOneToOne() {
		return cardinality == Cardinality.ONE_TO_ONE ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the link has a 'OneToMany' cardinality"
			}
	)
	public boolean isCardinalityOneToMany() {
		return cardinality == Cardinality.ONE_TO_MANY ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the link has a 'ManyToOne' cardinality"
			}
	)
	public boolean isCardinalityManyToOne() {
		return cardinality == Cardinality.MANY_TO_ONE ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the link has a 'ManyToMany' cardinality"
			}
	)
	public boolean isCardinalityManyToMany() {
		return cardinality == Cardinality.MANY_TO_MANY ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the link is a collection",
			"In other words, if its cardinality is 'OneToMany' or 'ManyToMany'"
			}
	)
	public boolean isCollectionType() {
		return cardinality == Cardinality.ONE_TO_MANY || cardinality == Cardinality.MANY_TO_MANY ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the 'cascade type' ( 'ALL', 'MERGE', 'MERGE PERSIST', 'PERSIST REFRESH', 'REMOVE' )",
			"A string containing all the selected cascade options"
			}
	)
	public String getCascade() {
		return cascadeOptions.toString();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the 'cascade options' is 'ALL' "
			}
	)
	public boolean isCascadeALL() {
		return cascadeOptions.isCascadeAll();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the 'cascade options' contains 'MERGE' "
			}
	)
	public boolean isCascadeMERGE() {
		return cascadeOptions.isCascadeMerge();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the 'cascade options' contains 'PERSIST' "
			}
	)
	public boolean isCascadePERSIST() {
		return cascadeOptions.isCascadePersist();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the 'cascade options' contains 'REFRESH' "
			}
	)
	public boolean isCascadeREFRESH() {
		return cascadeOptions.isCascadeRefresh();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the 'cascade options' contains 'REMOVE' "
			}
	)
	public boolean isCascadeREMOVE() {
		return cascadeOptions.isCascadeRemove();
	}

	//-------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the 'fetch type' ( 'DEFAULT' or 'EAGER' or 'LAZY' )"
			}
	)
	public String getFetch() {
		return fetchType.getText();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the 'fetch type' is 'DEFAULT' "
			}
	)
	public boolean isFetchDEFAULT() {
		return fetchType == FetchType.DEFAULT ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the 'fetch type' is 'EAGER' "
			}
	)
	public boolean isFetchEAGER() {
		return fetchType == FetchType.EAGER ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the 'fetch type' is 'LAZY' "
			}
	)
	public boolean isFetchLAZY() {
		return fetchType == FetchType.LAZY ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the 'optional status' for the link  ( 'TRUE', 'FALSE' or 'UNDEFINED' ) ",
			"Typically for JPA 'optional=true/false'"
			}
	)
	public String getOptional() {
		return optional.getText();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the 'optional status' is 'UNDEFINED' "
			}
	)
	public boolean isOptionalUndefined() {
		return optional == Optional.UNDEFINED ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the 'optional status' is 'FALSE' "
			}
	)
	public boolean isOptionalFalse() {
		return optional == Optional.FALSE ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the 'optional status' is 'TRUE' "
			}
	)
	public boolean isOptionalTrue() {
		return optional == Optional.TRUE ;
	}
	
	//-------------------------------------------------------------------------------------
	// "insertable" / "updatable"
	//-------------------------------------------------------------------------------------
	protected BooleanValue getInsertableFlag() {
		return this.isInsertable;
	}
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if 'insertable' flag equals the given value",
			"The flag can be 'undefined' then then neither true nor false"
		},
		parameters = { 
			"value : the boolean value" 
		},
		since="3.3.0"
	)
	public boolean insertableIs(boolean value) {
		if ( value ) {
			return this.isInsertable == BooleanValue.TRUE ;
		} else {
			return this.isInsertable == BooleanValue.FALSE ;
		}
	}
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the 'insertable' flag value as string",
			"( 'true' or 'false' or 'undefined' )"
			},
		since="3.3.0"
	)
    public String getInsertable() {
        return this.isInsertable.getText();
    }
	
	//-------------------------------------------------------------------------------------
	protected BooleanValue getUpdatableFlag() {
		return this.isUpdatable;
	}
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if 'updatable' flag equals the given value",
			"The flag can be 'undefined' then then neither true nor false"
		},
		parameters = { 
			"value : the boolean value" 
		},
		since="3.3.0"
	)
	public boolean updatableIs(boolean value) {
		if ( value ) {
			return this.isUpdatable == BooleanValue.TRUE ;
		} else {
			return this.isUpdatable == BooleanValue.FALSE ;
		}
	}
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the 'updatable' flag value as string",
			"( 'true' or 'false' or 'undefined' )"
			},
		since="3.3.0"
	)
    public String getUpdatable() {
        return this.isUpdatable.getText();
    }
	
	//------------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
			"Returns TRUE if the link is marked as 'embedded' "
		},
	since="3.3.0"
	)
	public boolean isEmbedded() {
		return this.isEmbedded  ; // v 3.3.0
	}

	//------------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
			"Returns TRUE if the link is marked as 'transient' "
		},
	since="3.3.0"
	)
	public boolean isTransient() {
		return this.isTransient  ; // v 3.3.0
	}
	
	//------------------------------------------------------------------------------------------
	// TAGS since ver 3.4.0
	//------------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the link has a tag with the given name"
		},
		parameters = { 
			"tagName : name of the tag for which to check the existence" 
		},
		example= {
			"$link.hasTag('mytag') "
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
			"$link.tagValue('mytag') "
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
			"$link.tagValue('mytag', 'abc') "
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
			"$link.tagValueAsInt('mytag', 123) "
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
			"$link.tagValueAsBoolean('mytag', false) "
		},
		since="3.4.0"
	)
	public boolean tagValueAsBoolean(String tagName, boolean defaultValue) {
		return tagContainer.getTagValueAsBoolean(tagName, defaultValue);
	}
	
	//-------------------------------------------------------------------------------------

	private String voidIfNull(String s) {
		return s != null ? s : "" ;
	}
	
}
