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

import java.util.List;

import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.names.ContextName;
import org.telosys.tools.generator.context.tools.JdbcRequests;
import org.telosys.tools.generator.context.tools.JdbcTypesMapper;

//-------------------------------------------------------------------------------------
@VelocityObject(
		contextName=ContextName.JDBC,
		text = { 
				"Object providing the JDBC SQL requests for a given entity",
				""
		},
		since = "2.1.1"
 )
//-------------------------------------------------------------------------------------
public class JdbcInContext {

	//private final SqlCRUDRequests sqlCRUDRequests ;
	private final JdbcRequests sqlCRUDRequests ;
	
	//-------------------------------------------------------------------------------------
	// CONSTRUCTOR
	//-------------------------------------------------------------------------------------
	public JdbcInContext(EntityInContext entity, boolean useSchema) {
		super();
		//this.sqlCRUDRequests = buildJdbc(entity, useSchema );
		this.sqlCRUDRequests = new JdbcRequests(entity, useSchema );
	}
	

//	private String[] getColumnNames(List<AttributeInContext> attributes) {
//		String[] columnNames = new String[attributes.size()] ;
//		int i = 0 ;
//		for ( AttributeInContext attr : attributes ) {
//			columnNames[i] = attr.getDatabaseName() ; 
//			i++;
//		}
//		return columnNames;
//	}
	
//	private SqlCRUDRequests buildJdbc(EntityInContext entity, boolean useSchema) {
//		String table = entity.getDatabaseTable();
//		if ( useSchema ) {
//			table = entity.getDatabaseSchema() + "." + entity.getDatabaseTable();
//		}
//		String[] keyColumns    = getColumnNames(entity.getKeyAttributes());
//		String[] nonKeyColumns = getColumnNames(entity.getNonKeyAttributes());
//		if ( entity.hasAutoIncrementedKey() ) {
//			// This entity has an auto-incremented column
//			String autoincrColumn = entity.getAutoincrementedKeyAttribute().getDatabaseName() ;
//			return new SqlCRUDRequests(table, keyColumns, nonKeyColumns, autoincrColumn ) ;
//		}
//		else {
//			// No auto-incremented column
//			return new SqlCRUDRequests(table, keyColumns, nonKeyColumns ) ;
//		}
//	}
	
	//-------------------------------------------------------------------------------------
	// SQL REQUESTS
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Returns the JDBC SQL SELECT request",
			""
		},
//		parameters = {
//			"entity : the entity"
//		},
//		example={	
//			"$jdbc.sqlSelect( $entity )"
//		},
		example={	
				"$jdbc.sqlSelect"
			},
		since = "2.1.1"
	)
//	public String sqlSelect(EntityInContext entity)
	public String getSqlSelect()
    {
		//return getSqlCRUDRequests(entity).getSqlSelect() ;
		return this.sqlCRUDRequests.getSqlSelect();
    }
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Returns the JDBC SQL INSERT request",
			""
		},
//		parameters = {
//			"entity : the entity"
//		},
//		example={	
//			"$jdbc.sqlInsert( $entity )"
//		},
		example={	
				"$jdbc.sqlInsert"
			},
		since = "2.1.1"
	)
//	public String sqlInsert(EntityInContext entity)
	public String getSqlInsert()
    {
		//return getSqlCRUDRequests(entity).getSqlInsert() ;
		return this.sqlCRUDRequests.getSqlInsert();
    }
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Returns the JDBC SQL UPDATE request",
			""
		},
		parameters = {
			"entity : the entity"
		},
//		example={	
//			"$jdbc.sqlUpdate( $entity )"
//		},
		example={	
				"$jdbc.sqlUpdate"
			},
		since = "2.1.1"
	)
//	public String sqlUpdate(EntityInContext entity)
	public String getSqlUpdate()
    {
		//return getSqlCRUDRequests(entity).getSqlUpdate() ;
		return this.sqlCRUDRequests.getSqlUpdate();
    }

	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Returns the JDBC SQL DELETE request",
			""
		},
//		parameters = {
//			"entity : the entity"
//		},
//		example={	
//			"$jdbc.sqlDelete( $entity )"
//		},
		example={	
				"$jdbc.sqlDelete"
			},
		since = "2.1.1"
	)
	//public String sqlDelete(EntityInContext entity)
	public String getSqlDelete()
    {
		//return getSqlCRUDRequests(entity).getSqlDelete();
		return this.sqlCRUDRequests.getSqlDelete();
    }
	//-------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Returns the list of all the attributes of the Primary Key",
			""
		},
		example={	
				"$jdbc.attributesForPrimaryKey"
			},
		since = "2.1.1"
	)
	public List<AttributeInContext> getAttributesForPrimaryKey()
    {
		return this.sqlCRUDRequests.getAttributesForPrimaryKey();
    }
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Returns the list of all the attributes required for the SQL SELECT",
			"All the attributes required to populate the bean instance from the result set"
		},
		example={	
				"$jdbc.attributesForSelect"
			},
		since = "2.1.1"
	)
	public List<AttributeInContext> getAttributesForSelect()
    {
		return this.sqlCRUDRequests.getAttributesForSelect();
    }
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
			text= { 
				"Returns the list of all the attributes required for the SQL INSERT",
				"All the attributes required to populate the PreparedStatement from a bean instance "
			},
			example={	
					"$jdbc.attributesForInsert"
				},
			since = "2.1.1"
		)
		public List<AttributeInContext> getAttributesForInsert()
	    {
			return this.sqlCRUDRequests.getAttributesForInsert();
	    }
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
			text= { 
				"Returns the list of all the attributes required for the SQL UPDATE",
				""
			},
			example={	
					"$jdbc.attributesForUpdate"
				},
			since = "2.1.1"
		)
		public List<AttributeInContext> getAttributesForUpdate()
	    {
			return this.sqlCRUDRequests.getAttributesForUpdate();
	    }
	//-------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Returns PreparedStatement setter according to the attribute type",
			"e.g. : 'setInt', 'setString', 'setBoolean', etc",
			""
		},
		parameters = {
			"attribute : the attribute"
		},
		example={	
			"$jdbc.preparedStatementSetter($attribute)"
		},
		since = "2.1.1"
	)
	public String preparedStatementSetter(AttributeInContext attribute)
    {
		return JdbcTypesMapper.getPreparedStatementSetter( attribute) ;
    }
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Returns the attribute's value to be set in the PreparedStatement ",
			"e.g. : 'getFirstName', 'getCode', etc",
			""
		},
		parameters = {
				"attribute : the attribute",
				"name : the variable name to be used to identify the attribute ( eg : 'country', 'book', 'employee', etc )"
			},
		example={	
			"$jdbc.valueForPreparedStatement($attribute, 'book')"
		},
		since = "2.1.1"
	)
	public String valueForPreparedStatement(AttributeInContext attribute, String name)
    {
		return JdbcTypesMapper.getValueForPreparedStatement(attribute, name);
    }
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Returns ResultSet getter according to the attribute type",
			"e.g. : 'getInt', 'getString', 'getBoolean', etc",
			""
		},
		parameters = {
			"attribute : the attribute"
		},
		example={	
			"$jdbc.resultSetGetter($attribute)"
		},
		since = "2.1.1"
	)
	public String resultSetGetter(AttributeInContext attribute)
    {
		return JdbcTypesMapper.getResultSetGetter( attribute) ;
    }
	//-------------------------------------------------------------------------------------
}
