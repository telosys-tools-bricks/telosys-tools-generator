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

import java.util.LinkedList;
import java.util.List;

import org.telosys.tools.commons.JavaClassUtil;
import org.telosys.tools.generator.EntitiesManager;
import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generator.GeneratorUtil;
import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.doc.VelocityReturnType;
import org.telosys.tools.generator.context.names.ContextName;
import org.telosys.tools.repository.model.JoinColumn;
import org.telosys.tools.repository.model.Link;

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

	private final Link             _link;
	private final EntitiesManager  _entitiesManager;

	private final List<JoinColumnInContext> _joinColumns ; 
	private final JoinTableInContext        _joinTable ; 
	
	
	//-------------------------------------------------------------------------------------
	/**
	 * Constructor
	 * @param link link in the repository 
	 * @param targetEntity targeted entity in the repository 
	 */
	//public LinkInContext(final Link link, final Entity targetEntity ) 
	//public LinkInContext(final Link link, final EntityInContext targetEntity ) 
	public LinkInContext(final EntityInContext entity, final Link link, final EntitiesManager entitiesManager ) 
	{
		this._entity = entity ;
		this._link = link;
//		this._targetEntity  = targetEntity;
		this._entitiesManager  = entitiesManager;
		
////		_sGetter = Util.buildGetter(this.getJavaName(), this.getLinkType());
////		_sSetter = Util.buildSetter(this.getJavaName());		
//		_sGetter = Util.buildGetter(this.getFieldName(), this.getFieldType());
//		_sSetter = Util.buildSetter(this.getFieldName());
		
		//--- Build the list of "join columns"
		_joinColumns = new LinkedList<JoinColumnInContext>();
		if ( _link.getJoinColumns() != null ) {
			JoinColumn[] joinColumns = _link.getJoinColumns().getAll();
			for ( JoinColumn col : joinColumns ) {
				_joinColumns.add( new JoinColumnInContext(col) ) ;
			}
		}
		
		//--- Set the join table if any
		if ( link.getJoinTable() != null ) {
			_joinTable = new JoinTableInContext( link.getJoinTable() ) ;
		}
		else {
			_joinTable = null ;
		}
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
		return _link.getId();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the link is selected (ckeckbox ckecked in the GUI)"
			}
	)
	public boolean isSelected() {
		return _link.isUsed();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the name of the target table (table referenced by the link)"
			}
	)
	public String getTargetTableName() {
		return _link.getTargetTableName();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the field name for the link (attribute name in the entity class)"
			}
	)
	public String getFieldName() {
		return _link.getJavaFieldName();
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
			// List, Set, ...
			return _link.getJavaFieldType();
		} else {
			// Person, Customer, Book, ...
			return this.getTargetEntityFullType() ; // v 2.1.0
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
			// List, Set, ...
			return JavaClassUtil.shortName( _link.getJavaFieldType() );
		} else {
			// Person, Customer, Book, ...
			return this.getTargetEntitySimpleType() ; // v 2.1.0
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
		return _link.isOwningSide();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the name of the link in the 'owning side' ",
			"Typically for JPA 'mappedBy'"
			}
	)
	public String getMappedBy() {
		return _link.getMappedBy();
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
		return _entitiesManager.getEntity( getTargetTableName() );
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
//		//return _link.getTargetEntityJavaType();
//		//return _targetEntity.getName(); // v 2.1.0
//		EntityInContext entity = _entitiesBuilder.getEntity( getTargetTableName() );
//		return entity.getName();
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
////		return _targetEntity.getFullName(); // v 2.1.0
//		EntityInContext entity = _entitiesBuilder.getEntity( getTargetTableName() );
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
		return _link.getCardinality();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the link has a 'OneToOne' cardinality"
			}
	)
	public boolean isCardinalityOneToOne() {
		return _link.isTypeOneToOne();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the link has a 'OneToMany' cardinality"
			}
	)
	public boolean isCardinalityOneToMany() {
		return _link.isTypeOneToMany();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the link has a 'ManyToOne' cardinality"
			}
	)
	public boolean isCardinalityManyToOne() {
		return _link.isTypeManyToOne();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the link has a 'ManyToMany' cardinality"
			}
	)
	public boolean isCardinalityManyToMany() {
		return _link.isTypeManyToMany();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the 'cascade type' ( 'ALL', 'MERGE', 'PERSIST', 'REFRESH', 'REMOVE' )"
			}
	)
	public String getCascade() {
		return _link.getCascade();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the 'cascade type' is 'ALL' "
			}
	)
	public boolean isCascadeALL() {
		return _link.isCascadeALL();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the 'cascade type' is 'MERGE' "
			}
	)
	public boolean isCascadeMERGE() {
		return _link.isCascadeMERGE();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the 'cascade type' is 'PERSIST' "
			}
	)
	public boolean isCascadePERSIST() {
		return _link.isCascadePERSIST();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the 'cascade type' is 'REFRESH' "
			}
	)
	public boolean isCascadeREFRESH() {
		return _link.isCascadeREFRESH();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the 'cascade type' is 'REMOVE' "
			}
	)
	public boolean isCascadeREMOVE() {
		return _link.isCascadeREMOVE();
	}

	//-------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the 'fetch type' ( 'DEFAULT' or 'EAGER' or 'LAZY' )"
			}
	)
	public String getFetch() {
		return _link.getFetch();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the 'fetch type' is 'DEFAULT' "
			}
	)
	public boolean isFetchDEFAULT() {
		return _link.isFetchDEFAULT();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the 'fetch type' is 'EAGER' "
			}
	)
	public boolean isFetchEAGER() {
		return _link.isFetchEAGER();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the 'fetch type' is 'LAZY' "
			}
	)
	public boolean isFetchLAZY() {
		return _link.isFetchLAZY();
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
		return _link.getOptional();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the 'optional status' is 'UNDEFINED' "
			}
	)
	public boolean isOptionalUndefined() {
		return _link.isOptionalUndefined();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the 'optional status' is 'FALSE' "
			}
	)
	public boolean isOptionalFalse() {
		return _link.isOptionalFalse();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the 'optional status' is 'TRUE' "
			}
	)
	public boolean isOptionalTrue() {
		return _link.isOptionalTrue();
	}

}
