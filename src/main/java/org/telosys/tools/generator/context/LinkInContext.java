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
import java.util.Map;

import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generator.GeneratorUtil;
import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.doc.VelocityReturnType;
import org.telosys.tools.generator.context.names.ContextName;
import org.telosys.tools.generic.model.CascadeOptions;
import org.telosys.tools.generic.model.JoinColumn;
import org.telosys.tools.generic.model.Link;
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
	
    private static final String VOID_STRING  = "" ;

    private final EntityInContext  _entity ; // the entity to which the link belongs

	private final ModelInContext   _modelInContext ;  // v 3.0.0 (replaces EntitiesManager)
	private final EnvInContext     _envInContext ; // ver 3.3.0

	private final List<JoinColumnInContext> _joinColumns ; 
	private final JoinTableInContext        _joinTable ; 

	//--- Added in ver 3.0.0 (to replace reference / Link )
	private final String       _id ;
	private final String       _fieldName ;
	private final String       _targetTableName ;
	private final String       mappedBy ;
	private final boolean      _selected ;
	private final boolean      _owningSide ;
	
	private final Cardinality    _cardinality ;
	private final FetchType      _fetchType ;
	private final Optional       _optional ;
	private final CascadeOptions _cascadeOptions ;
	
    private final BooleanValue  _insertable ; // Added in v 3.3.0
    private final BooleanValue  _updatable  ; // Added in v 3.3.0
	
    private final boolean isTransient ; // Added in v 3.3.0
    private final boolean isEmbedded ; // Added in v 3.3.0
    
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
		this._entity = entity ;
		this._modelInContext = modelInContext ; // v 3.0.0
		this._envInContext = envInContext ; // v 3.3.0
		
		//--- Build the list of "join columns"
		_joinColumns = new LinkedList<>();
		if ( link.getJoinColumns() != null ) {
			for ( JoinColumn joinColumn : link.getJoinColumns() ) {
				_joinColumns.add( new JoinColumnInContext(joinColumn) ) ;
			}
		}
		
		//--- Set the join table if any
		if ( link.getJoinTable() != null ) {
			_joinTable = new JoinTableInContext( link.getJoinTable() ) ;
		}
		else {
			_joinTable = null ;
		}
		
		//--- Init link information (ver 3.0.0)
		_id = link.getId() ;
		_fieldName = link.getFieldName() ;
		// _fieldType = link.getFieldType(); // removed in v 3.3.0
		_targetTableName = link.getTargetTableName();
		_selected = link.isSelected();
		this.mappedBy = link.getMappedBy(); // keep null if not defined
		_owningSide = link.isOwningSide();
		
		_cardinality = link.getCardinality();
		_fetchType = link.getFetchType() != null ? link.getFetchType() : FetchType.DEFAULT ;
		_optional = link.getOptional();
		_cascadeOptions = link.getCascadeOptions();
		
		_insertable = link.getInsertable();
		_updatable  = link.getUpdatable();
		this.isTransient = link.isTransient(); // v 3.3.0
		this.isEmbedded  = link.isEmbedded(); // v 3.3.0
		
		this.tagsMap = link.getTagsMap(); // V 3.4.0
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
		TypeConverter typeConverter = _envInContext.getTypeConverter();
		// get collection type for the current language 
		String collectionType = typeConverter.getCollectionType(className);
		if ( collectionType != null ) {
			return collectionType ;
		}
		else {
			throw new IllegalStateException("Cannot get collection type (language="
					+ _envInContext.getLanguage()+")");
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
		return _entity;
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
	public String getGetter() { // throws GeneratorException  {
		//return Util.buildGetter(this.getFieldName(), this.getFieldType());
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

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the link has a 'join table'"
			}
	)
	public boolean hasJoinTable() {
		return _joinTable != null ;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the name of the 'join table' for the link ",
			"NB : can be null if the link doesn't have a 'join table'",
			"check existence before with 'hasJoinTable()' "
			}
	)
	public String getJoinTableName() {
		if ( _joinTable != null ) {
			return _joinTable.getName();
		}
		else {
			return null ;
		}
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the 'join table' object for the link ",
			"NB : can be null if the link doesn't have a 'join table' ",
			"check existence before with 'hasJoinTable()' "			
			}
	)
	public JoinTableInContext getJoinTable() {
		return _joinTable ;
	}
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the link has 'join columns' (at least one)"
			}
	)
	public boolean hasJoinColumns() {
		if ( _joinColumns != null ) {
			return ! _joinColumns.isEmpty() ;
		}
		return false ;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the link has one (or more) 'join column' in the entity Primary Key",
			"( one of its 'origin attributes' is the Primary Key or a part of the Primary Key )"
			},
		since="2.1.0"
	)
	public boolean hasAttributeInPrimaryKey() throws GeneratorException {
		if ( _joinColumns != null ) {
			for ( JoinColumnInContext jc : _joinColumns ) {
				//--- ORIGIN attribute
				AttributeInContext attribOrigin = _entity.getAttributeByColumnName(jc.getName());
				if ( attribOrigin.isKeyElement() ) {
					return true ;
				}
			}
		}
		return false ;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the 'join columns' for the link "
			}
	)
	public List<JoinColumnInContext> getJoinColumns() {
		return _joinColumns ;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns the number of attributes used to define the link"
		},
		example="$link.attributesCount",
		since="2.1.0"
	)
	public int getAttributesCount() {
		if ( _joinColumns != null ) {
			return _joinColumns.size() ;
		}
		return 0 ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns a list of attributes pair defining the link ",
			"Each pair contains the owning side attribute and its corresponding reverse side attribute ",
			"Each link is supposed to contain at least 1 pair of attributes"
			}
	)
	@VelocityReturnType("List of '$linkAttribute' (origin-target association) ")	
	public List<LinkAttributesPairInContext> getAttributes() throws GeneratorException {
		List<LinkAttributesPairInContext> list = new LinkedList<>();
		if ( _joinColumns != null ) {
			for ( JoinColumnInContext jc : _joinColumns ) {
				//--- ORIGIN attribute
				AttributeInContext attribOrigin = _entity.getAttributeByColumnName(jc.getName());
				//--- TARGET attribute
				EntityInContext referencedEntity = this.getTargetEntity();
				AttributeInContext attribTarget = referencedEntity.getAttributeByColumnName(jc.getReferencedColumnName());
				//--- New attribute mapping in the list
				list.add( new LinkAttributesPairInContext(attribOrigin, attribTarget) );
			}
		}
		return list ;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the given attribute is used by the link "
		},
		parameters = { 
			"attribute : the attribute to be checked" 
			}
	)
	public boolean usesAttribute(AttributeInContext attribute) {
		if ( _joinColumns != null ) {
			for ( JoinColumnInContext jc : _joinColumns ) {
				if ( attribute.getDatabaseName().equals( jc.getName() ) ) {
					return true ;
				}
			}
		}
		return false ;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the unique id of the link in the repository (id used by the tool)",
			"(not supposed to be used in a generated file)"
			}
	)
	public String getId() {
		return _id ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the link is selected (checkbox checked in the GUI)"
			}
	)
	public boolean isSelected() {
		return _selected ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the name of the target table (table referenced by the link)"
			}
	)
	public String getTargetTableName() {
		return _targetTableName ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the field name for the link (attribute name in the entity class)"
			}
	)
	public String getFieldName() {
		return _fieldName ;
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
		return _owningSide ;
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
	public EntityInContext getTargetEntity() throws GeneratorException {
		String targetTableName = getTargetTableName();
		if ( targetTableName == null ) {
			throw new GeneratorException("Cannot get target entity. No target table name for link '" + getId() + "'" );
		}
		
		EntityInContext targetEntity = _modelInContext.getEntityByTableName( targetTableName ); // v 3.0.0
		if ( targetEntity == null ) {
			throw new GeneratorException("Cannot get target entity. No entity for table name '" + targetTableName + "'" );
		}
		return targetEntity ;
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
		return this.getTargetEntity().getName();
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
		return _cardinality.getText();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the link has a 'OneToOne' cardinality"
			}
	)
	public boolean isCardinalityOneToOne() {
		return _cardinality == Cardinality.ONE_TO_ONE ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the link has a 'OneToMany' cardinality"
			}
	)
	public boolean isCardinalityOneToMany() {
		return _cardinality == Cardinality.ONE_TO_MANY ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the link has a 'ManyToOne' cardinality"
			}
	)
	public boolean isCardinalityManyToOne() {
		return _cardinality == Cardinality.MANY_TO_ONE ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the link has a 'ManyToMany' cardinality"
			}
	)
	public boolean isCardinalityManyToMany() {
		return _cardinality == Cardinality.MANY_TO_MANY ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the link is a collection",
			"In other words, if its cardinality is 'OneToMany' or 'ManyToMany'"
			}
	)
	public boolean isCollectionType() {
		return _cardinality == Cardinality.ONE_TO_MANY || _cardinality == Cardinality.MANY_TO_MANY ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the 'cascade type' ( 'ALL', 'MERGE', 'MERGE PERSIST', 'PERSIST REFRESH', 'REMOVE' )",
			"A string containing all the selected cascade options"
			}
	)
	public String getCascade() {
		return _cascadeOptions.toString();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the 'cascade options' is 'ALL' "
			}
	)
	public boolean isCascadeALL() {
		return _cascadeOptions.isCascadeAll();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the 'cascade options' contains 'MERGE' "
			}
	)
	public boolean isCascadeMERGE() {
		return _cascadeOptions.isCascadeMerge();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the 'cascade options' contains 'PERSIST' "
			}
	)
	public boolean isCascadePERSIST() {
		return _cascadeOptions.isCascadePersist();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the 'cascade options' contains 'REFRESH' "
			}
	)
	public boolean isCascadeREFRESH() {
		return _cascadeOptions.isCascadeRefresh();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the 'cascade options' contains 'REMOVE' "
			}
	)
	public boolean isCascadeREMOVE() {
		return _cascadeOptions.isCascadeRemove();
	}

	//-------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the 'fetch type' ( 'DEFAULT' or 'EAGER' or 'LAZY' )"
			}
	)
	public String getFetch() {
		return _fetchType.getText();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the 'fetch type' is 'DEFAULT' "
			}
	)
	public boolean isFetchDEFAULT() {
		return _fetchType == FetchType.DEFAULT ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the 'fetch type' is 'EAGER' "
			}
	)
	public boolean isFetchEAGER() {
		return _fetchType == FetchType.EAGER ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the 'fetch type' is 'LAZY' "
			}
	)
	public boolean isFetchLAZY() {
		return _fetchType == FetchType.LAZY ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the 'optional status' for the link  ( 'TRUE', 'FALSE' or 'UNDEFINED' ) ",
			"Typically for JPA 'optional=true/false'"
			}
	)
	public String getOptional() {
		return _optional.getText();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the 'optional status' is 'UNDEFINED' "
			}
	)
	public boolean isOptionalUndefined() {
		return _optional == Optional.UNDEFINED ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the 'optional status' is 'FALSE' "
			}
	)
	public boolean isOptionalFalse() {
		return _optional == Optional.FALSE ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the 'optional status' is 'TRUE' "
			}
	)
	public boolean isOptionalTrue() {
		return _optional == Optional.TRUE ;
	}
	
	//-------------------------------------------------------------------------------------
	// "insertable" / "updatable"
	//-------------------------------------------------------------------------------------
	protected BooleanValue getInsertableFlag() {
		return this._insertable;
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
			return this._insertable == BooleanValue.TRUE ;
		} else {
			return this._insertable == BooleanValue.FALSE ;
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
        return this._insertable.getText();
    }
	
	//-------------------------------------------------------------------------------------
	protected BooleanValue getUpdatableFlag() {
		return this._updatable;
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
			return this._updatable == BooleanValue.TRUE ;
		} else {
			return this._updatable == BooleanValue.FALSE ;
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
        return this._updatable.getText();
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
	private final Map<String, String> tagsMap ; // All tags defined for the link (0..N) 
	
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
		if ( this.tagsMap != null ) {
			return this.tagsMap.containsKey(tagName);
		}
		return false; 
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
		if ( this.tagsMap != null ) {
			String v = this.tagsMap.get(tagName);
			return ( v != null ? v : VOID_STRING );
		}
		return VOID_STRING;
	}

}
