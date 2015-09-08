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
package org.telosys.tools.generator.context;

import java.util.LinkedList;
import java.util.List;

import org.telosys.tools.commons.JavaClassUtil;
import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generator.GeneratorUtil;
import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.doc.VelocityReturnType;
import org.telosys.tools.generator.context.names.ContextName;
import org.telosys.tools.generic.model.Cardinality;
import org.telosys.tools.generic.model.CascadeOptions;
import org.telosys.tools.generic.model.FetchType;
import org.telosys.tools.generic.model.JoinColumn;
import org.telosys.tools.generic.model.Link;
import org.telosys.tools.generic.model.Optional;

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
				"#foreach( $link in $entity.selectedLinks )",
				"    private $link.formattedFieldType(10) $link.formattedFieldName(12);",
				"#end"				
		}
		
 )
//-------------------------------------------------------------------------------------
public class LinkInContext {
	
    private final EntityInContext  _entity ; // The entity the link belongs to

	//private final Link             _link; // removed in ver 3.0.0
	//private final EntitiesManager  _entitiesManager; // removed in ver 3.0.0
	private final ModelInContext   _modelInContext ;  // v 3.0.0 (replaces EntitiesManager)

	private final List<JoinColumnInContext> _joinColumns ; 
	private final JoinTableInContext        _joinTable ; 

	//--- Added in ver 3.0.0 (to replace reference / Link )
	private final String       _id ;
	private final String       _fieldName ;
	private final String       _fieldType ;
	private final String       _targetTableName ;
	private final String       _mappedBy ;
	private final boolean      _selected ;
	private final boolean      _owningSide ;
	
	private final Cardinality    _cardinality ;
	private final FetchType      _fetchType ;
	private final Optional       _optional ;
	private final CascadeOptions _cascadeOptions ;
	
	//-------------------------------------------------------------------------------------
	/**
	 * Constructor
	 * @param link link in the repository 
	 * @param targetEntity targeted entity in the repository 
	 */
//	public LinkInContext(final EntityInContext entity, final Link link, final EntitiesManager entitiesManager ) 
	public LinkInContext(final EntityInContext entity, final Link link, final ModelInContext modelInContext ) 
	{
		this._entity = entity ;
		//this._link = link; // removed in ver 3.0.0
		//this._entitiesManager  = entitiesManager; // removed in ver 3.0.0
		this._modelInContext = modelInContext ; // v 3.0.0
		
		//--- Build the list of "join columns"
		_joinColumns = new LinkedList<JoinColumnInContext>();
		if ( link.getJoinColumns() != null ) {
//			JoinColumn[] joinColumns = _link.getJoinColumns().getAll();
//			for ( JoinColumn col : joinColumns ) {
//				_joinColumns.add( new JoinColumnInContext(col) ) ;
//			}
			// ver 3.0.0
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
		_fieldType = link.getFieldType();
		_targetTableName = link.getTargetTableName();
		_selected = link.isSelected();
		_mappedBy = link.getMappedBy();
		_owningSide = link.isOwningSide();
		
		_cardinality = link.getCardinality();
		_fetchType = link.getFetchType();
		_optional = link.getOptional();
		_cascadeOptions = link.getCascadeOptions();
	}
	
//	//-------------------------------------------------------------------------------------
//	protected Link getLink() {
//		return this._link ;
//	}
	//-------------------------------------------------------------------------------------
//	protected Entity getTargetEntity() {
//		return this._targetEntity ;
//	}
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
	public String formattedFieldName(int iSize)
    {
        //String s = this.getJavaName() ;
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
	public String getGetter() throws GeneratorException  {
		//return _sGetter;
		return Util.buildGetter(this.getFieldName(), this.getFieldType());
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the Java setter for the link e.g. 'setPerson' for link 'person' "
			}
	)
	public String getSetter() {
		//return _sSetter;
		return Util.buildSetter(this.getFieldName());
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the link has a 'join table'"
			}
	)
	public boolean hasJoinTable() {
		//return _link.getJoinTable() != null ;
		return _joinTable != null ;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the name of the 'join table' for the link ",
			"NB : can be null if the link doesn't have a 'join table'"
			}
	)
//	public String getJoinTable() {
//		JoinTable joinTable = _link.getJoinTable();
//		if ( joinTable != null ) {
//			return joinTable.getName();
//		}
//		else {
//			throw new GeneratorContextException("No 'Join Table' for this link");
//		}
//	}
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
			"NB : can be null if the link doesn't have a 'join table' "
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
			return ( _joinColumns.size() > 0 ) ; 
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
//	public String[] getJoinColumns() {
//		JoinColumns joinColumns = _link.getJoinColumns() ;
//		if ( joinColumns != null ) {
//			JoinColumn[] columns = joinColumns.getAll();
//			String[] colNames = new String[columns.length] ;
//			for ( int i = 0 ; i < columns.length ; i++ ) {
//				JoinColumn col = columns[i] ;
//				if ( col != null ) {
//					colNames[i] = columns[i].getName();
//				}
//				else {
//					throw new GeneratorContextException("Invalid link : null 'Join Column' in 'Join Columns' collection");
//				}
//			}
//			return colNames ;
//		}
//		else {
//			throw new GeneratorContextException("No 'Join Columns' for this link");
//		}
//	}
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
	public int getAttributesCount() 
	{
		if ( _joinColumns != null )
		{
			return _joinColumns.size() ;
		}
		return 0 ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the attributes for the two sides of the link "
			}
	)
	@VelocityReturnType("List of '$linkAttribute' (origin-target association) ")	
	public List<LinkAttributeInContext> getAttributes() throws GeneratorException {
		List<LinkAttributeInContext> list = new LinkedList<LinkAttributeInContext>();
		if ( _joinColumns != null ) {
			for ( JoinColumnInContext jc : _joinColumns ) {
				//--- ORIGIN attribute
				AttributeInContext attribOrigin = _entity.getAttributeByColumnName(jc.getName());
				//--- TARGET attribute
				EntityInContext referencedEntity = this.getTargetEntity();
				AttributeInContext attribTarget = referencedEntity.getAttributeByColumnName(jc.getReferencedColumnName());
				//--- New attribute mapping in the list
				list.add( new LinkAttributeInContext(attribOrigin, attribTarget) );
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
		//return _link.getId();
		return _id ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the link is selected (checkbox checked in the GUI)"
			}
	)
	public boolean isSelected() {
		//return _link.isUsed();
		return _selected ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the name of the target table (table referenced by the link)"
			}
	)
	public String getTargetTableName() {
		//return _link.getTargetTableName();
		return _targetTableName ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the field name for the link (attribute name in the entity class)"
			}
	)
	public String getFieldName() {
		//return _link.getJavaFieldName();
		return _fieldName ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the field 'full type' for the link ( eg 'java.util.List' ) ",
			"for OneToMany/ManyToMany : the collection full type ( 'java.util.List', ...)",
			"for ManyToOne/OneToOne : the targeted entity full type ( 'my.package.Person', 'my.package.Customer', ... ) "
			}
	)
	public String getFieldFullType() throws GeneratorException {
		//return _link.getJavaFieldType();
		if ( this.isCardinalityOneToMany() || this.isCardinalityManyToMany() ) {
			// Link referencing a collection : the link provides the full type ( java.util.List, java.util.Set, ... )
			//return _link.getJavaFieldType();
			return _fieldType ; // use the type as is 
		} else {
			// Link referencing an entity : the link provides the simple type ( Person, Customer, Book, ... )
			return this.getTargetEntityFullType() ; // get the 'full type' from the referenced entity // v 2.1.0
		}		
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the field 'simple type' for the link ",
			"for OneToMany/ManyToMany : the collection short type ( 'List', ...)",
			"for ManyToOne/OneToOne : the targeted entity short type ( 'Person', 'Customer', ... ) "
			}
	)
	public String getFieldSimpleType() throws GeneratorException {
		//return JavaClassUtil.shortName(_link.getJavaFieldType());
		if ( this.isCardinalityOneToMany() || this.isCardinalityManyToMany() ) {
			// Link referencing a collection : the link provides the full type ( java.util.List, java.util.Set, ... )
			//return JavaClassUtil.shortName( _link.getJavaFieldType() );
			return JavaClassUtil.shortName( _fieldType ); // return only the 'simple name' ( 'List', 'Set', ... )
		} else {
			// Link referencing an entity : the link provides the simple type ( Person, Customer, Book, ... )
			return this.getTargetEntitySimpleType() ; // get the 'simple type' from the referenced entity // v 2.1.0
		}		
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the type of the link ",
			"eg : List, List<Person>, Person, ..."
			}
	)
	public String getFieldType() throws GeneratorException {
		String type = "";
		String targetEntityClassName = this.getTargetEntitySimpleType() ; // v 2.1.0
		String simpleType = this.getFieldSimpleType();
		
		if ( this.isCardinalityOneToMany() || this.isCardinalityManyToMany() ) {
			// List<Xxx>, Set<Xxxx>, ....
			type = simpleType + "<" + targetEntityClassName + ">";
		} else {
			// Xxx
			type = targetEntityClassName ;
		}
		return type;
	}	
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the link is the 'Owning Side' of the relationship between 2 entities"
			}
	)
	public boolean isOwningSide() {
		//return _link.isOwningSide();
		return _owningSide ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the name of the link in the 'owning side' ",
			"Typically for JPA 'mappedBy'"
			}
	)
	public String getMappedBy() {
		//return _link.getMappedBy();
		return _mappedBy;
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
		// return _entitiesManager.getEntity( getTargetTableName() );
		// return _entitiesManager.getEntityByTableName( getTargetTableName() ); // v 3.0.0
		return _modelInContext.getEntityByTableName( getTargetTableName() ); // v 3.0.0
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
	public String getCardinality() { // v 2.0.5
		//return _link.getCardinality();
		return _cardinality.getText();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the link has a 'OneToOne' cardinality"
			}
	)
	public boolean isCardinalityOneToOne() {
		//return _link.isTypeOneToOne();
		return _cardinality == Cardinality.ONE_TO_ONE ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the link has a 'OneToMany' cardinality"
			}
	)
	public boolean isCardinalityOneToMany() {
		//return _link.isTypeOneToMany();
		return _cardinality == Cardinality.ONE_TO_MANY ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the link has a 'ManyToOne' cardinality"
			}
	)
	public boolean isCardinalityManyToOne() {
		//return _link.isTypeManyToOne();
		return _cardinality == Cardinality.MANY_TO_ONE ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the link has a 'ManyToMany' cardinality"
			}
	)
	public boolean isCardinalityManyToMany() {
		//return _link.isTypeManyToMany();
		return _cardinality == Cardinality.MANY_TO_MANY ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the 'cascade type' ( 'ALL', 'MERGE', 'MERGE PERSIST', 'PERSIST REFRESH', 'REMOVE' )",
			"A string containing all the selected cascade options"
			}
	)
	public String getCascade() {
//		//return _link.getCascade();
//		if ( this.cascadeALL ) {
//			return RepositoryConst.CASCADE_ALL ;
//		}
//		else {
//			StringBuffer sb = new StringBuffer();
//			if ( this.cascadeMERGE ) {
//				sb.append(" ");
//				sb.append(RepositoryConst.CASCADE_MERGE);
//			}
//			if ( this.cascadePERSIST ) {
//				sb.append(" ");
//				sb.append(RepositoryConst.CASCADE_PERSIST);
//			}
//			if ( this.cascadeREFRESH ) {
//				sb.append(" ");
//				sb.append(RepositoryConst.CASCADE_REFRESH);
//			}
//			if ( this.cascadeREMOVE ) {
//				sb.append(" ");
//				sb.append(RepositoryConst.CASCADE_REMOVE);
//			}
//			return sb.toString();
//		}
		return _cascadeOptions.toString();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the 'cascade options' is 'ALL' "
			}
	)
	public boolean isCascadeALL() {
		//return _link.isCascadeALL();
		return _cascadeOptions.isCascadeAll();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the 'cascade options' contains 'MERGE' "
			}
	)
	public boolean isCascadeMERGE() {
		//return _link.isCascadeMERGE();
		return _cascadeOptions.isCascadeMerge();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the 'cascade options' contains 'PERSIST' "
			}
	)
	public boolean isCascadePERSIST() {
		//return _link.isCascadePERSIST();
		return _cascadeOptions.isCascadePersist();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the 'cascade options' contains 'REFRESH' "
			}
	)
	public boolean isCascadeREFRESH() {
		//return _link.isCascadeREFRESH();
		return _cascadeOptions.isCascadeRefresh();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the 'cascade options' contains 'REMOVE' "
			}
	)
	public boolean isCascadeREMOVE() {
		//return _link.isCascadeREMOVE();
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
		//return _link.getFetch();
		return _fetchType.getText();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the 'fetch type' is 'DEFAULT' "
			}
	)
	public boolean isFetchDEFAULT() {
		//return _link.isFetchDEFAULT();
		return _fetchType == FetchType.DEFAULT ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the 'fetch type' is 'EAGER' "
			}
	)
	public boolean isFetchEAGER() {
		//return _link.isFetchEAGER();
		return _fetchType == FetchType.EAGER ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the 'fetch type' is 'LAZY' "
			}
	)
	public boolean isFetchLAZY() {
		//return _link.isFetchLAZY();
		return _fetchType == FetchType.LAZY ;
	}

	//-------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the 'optional status' for the link  ( 'TRUE', 'FALSE' or 'UNDEFINED' ) ",
			"Typically for JPA 'optional=true/false'"
			}
	)
	public String getOptional() {
		//return _link.getOptional();
		return _optional.getText();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the 'optional status' is 'UNDEFINED' "
			}
	)
	public boolean isOptionalUndefined() {
		//return _link.isOptionalUndefined();
		return _optional == Optional.UNDEFINED ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the 'optional status' is 'FALSE' "
			}
	)
	public boolean isOptionalFalse() {
		//return _link.isOptionalFalse();
		return _optional == Optional.FALSE ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the 'optional status' is 'TRUE' "
			}
	)
	public boolean isOptionalTrue() {
		//return _link.isOptionalTrue();
		return _optional == Optional.TRUE ;
	}

}
