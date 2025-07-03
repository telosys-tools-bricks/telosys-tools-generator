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

import java.util.List;

import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityObject;

/**
 * Database Foreign Key Part exposed in the generator context
 * 
 * @author Laurent Guerin
 *
 */
//-------------------------------------------------------------------------------------
@VelocityObject(
		// TODO
		contextName = "table" , // TMP // ContextName.XXXXXXX ,
		text = {
				"SQL table definition for SQL scripts generation",
				""
		},
		since = "3.4.0",
		example= {
				"",
				"#set( $table = $sql.tableDef($entity) )",
				""				
		}
 )
//-------------------------------------------------------------------------------------
public class SqlTableInContext
{
	private final String name ;
	private final String primaryKeyDefinition ;
	private final List<String> columnsDefinitions ;
	private final String comment ;
	
	//-------------------------------------------------------------------------------------
	public SqlTableInContext( ) {
		super();
		// TODO
		this.name   = null;
		this.primaryKeyDefinition = null ;
		this.columnsDefinitions = null;
		this.comment = null ;
//		this.name   = xxx;
//		this.primaryKeyDefinition = xxx ;
//		this.columnsDefinitions = xxx;
//		this.comment = xxx ;
	}
	 
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the table name "
			},
		since = "3.4.0"
	)
	public String getName() {
		return name ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the SQL definitions for all the columns"
			},
		since = "3.4.0"
	)
	public List<String> getColumnsDefinitions() {
		return columnsDefinitions ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the table has a Primary Key "
			},
		since = "3.4.0"
	)
	public boolean hasPrimaryKey() {
		return ! primaryKeyDefinition.isEmpty() ;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the SQL definition for the Primary Key (or void if no PK)"
			},
		since = "3.4.0"
	)
	public String getPrimaryKeyDefinition() {
		return primaryKeyDefinition ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if the table has a comment "
			},
		since = "3.4.0"
	)
	public boolean hasComment() {
		return ! comment.isEmpty() ;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the table comment if any (or void if none)"
			},
		since = "3.4.0"
	)
	public String getComment() {
		return comment ;
	}
}
