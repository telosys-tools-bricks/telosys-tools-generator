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

import org.telosys.tools.commons.jdbc.SqlCRUDRequests;
import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.names.ContextName;

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

	//private final static List<String> VOID_STRINGS_LIST = new LinkedList<String>();
	private boolean useSchema = false ;

	//-------------------------------------------------------------------------------------
	// CONSTRUCTOR
	//-------------------------------------------------------------------------------------
	public JdbcInContext() {
		super();
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Defines if the 'schema' must be used in the table name",
			"Default value is FALSE"
		},
		parameters = {
			"value : true = use schema, false = do not use schema "
		},		
		example={	
			"$jdbc.useSchema()"
		},
		since = "2.1.1"
	)
	public void useSchema(boolean value)
    {
		this.useSchema = value ;
    }
	
//	//-------------------------------------------------------------------------------------
//	// JPA IMPORTS
//	//-------------------------------------------------------------------------------------
//	@VelocityMethod ( 
//		text= { 
//			"Returns a list of all the Java classes required by the current entity for JPA",
//			"( this version always returns 'javax.persistence.*' )"
//		},
//		parameters = {
//			"entity : the entity "
//		},
//		example={	
//			"#foreach( $import in $jpa.imports($entity) )",
//			"import $import;",
//			"#end" 
//		},
//		since = "2.0.7"
//	)
//	@VelocityReturnType("List of 'String'")
//	//public List<String> imports(JavaBeanClass entity) 
//	public List<String> imports(EntityInContext entity) 
//	{
//		ImportsList _importsJpa = buildJpaImportsList(entity) ;
//		if ( _importsJpa != null )
//		{
//			return _importsJpa.getList() ;
//		}
//		return VOID_STRINGS_LIST ;
//	}

	private String[] getColumnNames(List<AttributeInContext> attributes) {
		String[] columnNames = new String[attributes.size()] ;
		int i = 0 ;
		for ( AttributeInContext attr : attributes ) {
			columnNames[i] = attr.getDatabaseName() ; 
			i++;
		}
		return columnNames;
	}
	
	private SqlCRUDRequests buildJdbc(EntityInContext entity) {
		String table = entity.getDatabaseTable();
		if ( this.useSchema ) {
			table = entity.getDatabaseSchema() + "." + entity.getDatabaseTable();
		}
		String[] keyColumns    = getColumnNames(entity.getKeyAttributes());
		String[] nonKeyColumns = getColumnNames(entity.getNonKeyAttributes());
		if ( entity.hasAutoIncrementedKey() ) {
			// This entity has an auto-incremented column
			String autoincrColumn = entity.getAutoincrementedKeyAttribute().getDatabaseName() ;
			return new SqlCRUDRequests(table, keyColumns, nonKeyColumns, autoincrColumn ) ;
		}
		else {
			// No auto-incremented column
			return new SqlCRUDRequests(table, keyColumns, nonKeyColumns ) ;
		}
	}
	
	private SqlCRUDRequests getSqlCRUDRequests(EntityInContext entity) {
		return buildJdbc(entity);
	}
	
	//-------------------------------------------------------------------------------------
	// SQL REQUESTS
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Returns the JDBC SQL SELECT request",
			""
		},
		parameters = {
			"entity : the entity"
		},
		example={	
			"$jdbc.sqlSelect( $entity )"
		},
		since = "2.1.1"
	)
	public String sqlSelect(EntityInContext entity)
    {
		return getSqlCRUDRequests(entity).getSqlSelect() ;
    }
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Returns the JDBC SQL INSERT request",
			""
		},
		parameters = {
			"entity : the entity"
		},
		example={	
			"$jdbc.sqlInsert( $entity )"
		},
		since = "2.1.1"
	)
	public String sqlInsert(EntityInContext entity)
    {
		return getSqlCRUDRequests(entity).getSqlInsert() ;
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
		example={	
			"$jdbc.sqlUpdate( $entity )"
		},
		since = "2.1.1"
	)
	public String sqlUpdate(EntityInContext entity)
    {
		return getSqlCRUDRequests(entity).getSqlUpdate() ;
    }

	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Returns the JDBC SQL DELETE request",
			""
		},
		parameters = {
			"entity : the entity"
		},
		example={	
			"$jdbc.sqlDelete( $entity )"
		},
		since = "2.1.1"
	)
	public String sqlDelete(EntityInContext entity)
    {
		return getSqlCRUDRequests(entity).getSqlDelete();
    }
	//-------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------
	
}
