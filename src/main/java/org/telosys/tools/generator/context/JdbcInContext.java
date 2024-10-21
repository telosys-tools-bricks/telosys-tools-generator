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
import org.telosys.tools.generator.context.names.ContextName;
import org.telosys.tools.generator.context.tools.JdbcRequests;

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

	private final JdbcRequests requests ;
	
	//-------------------------------------------------------------------------------------
	// CONSTRUCTOR
	//-------------------------------------------------------------------------------------
	public JdbcInContext(EntityInContext entity, boolean useSchema) {
		super();
		this.requests = new JdbcRequests(entity, useSchema );
	}
	
	//-------------------------------------------------------------------------------------
	// SQL REQUESTS
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Returns the JDBC SQL 'SELECT xxx FROM xxx' request",
			""
		},
		example={	
				"$jdbc.sqlSelect"
			},
		since = "3.0.0"
	)
	public String getSqlSelect()
    {
		return this.requests.getSqlSelect();
    }
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Returns the JDBC SQL 'SELECT xxx FROM xxx WHERE pk = xxx' request",
			""
		},
		example={	
				"$jdbc.sqlSelectWherePK"
			},
		since = "3.0.0"
	)
	public String getSqlSelectWherePK()
    {
		return this.requests.getSqlSelectWherePK();
    }
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Returns the JDBC SQL 'SELECT COUNT(*) FROM xxx' request ",
			""
		},
		example={	
				"$jdbc.sqlSelectCount"
			},
		since = "3.0.0"
	)
	public String getSqlSelectCount()
    {
		return this.requests.getSqlSelectCount();
    }

	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Returns the JDBC SQL 'SELECT COUNT(*) FROM xxx WHERE pk = xxx' request ",
			"Can be used to check the existence of a record for the given PK"
		},
		example={	
				"$jdbc.sqlSelectCountWherePK"
			},
		since = "3.0.0"
	)
	public String getSqlSelectCountWherePK()
    {
		return this.requests.getSqlSelectCountWherePK();
    }

	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Returns the JDBC SQL INSERT request",
			""
		},
		example={	
				"$jdbc.sqlInsert"
			},
		since = "2.1.1"
	)
	public String getSqlInsert()
    {
		return this.requests.getSqlInsert();
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
				"$jdbc.sqlUpdate"
			},
		since = "2.1.1"
	)
	public String getSqlUpdate()
    {
		return this.requests.getSqlUpdate();
    }

	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Returns the JDBC SQL DELETE request",
			""
		},
		example={	
				"$jdbc.sqlDelete"
			},
		since = "2.1.1"
	)
	public String getSqlDelete()
    {
		return this.requests.getSqlDelete();
    }
	//-------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Returns the list of all the attributes required for the Primary Key",
			""
		},
		example={	
				"$jdbc.attributesForPrimaryKey"
			},
		since = "2.1.1"
	)
	public List<AttributeInContext> getAttributesForPrimaryKey()
    {
		return this.requests.getAttributesForPrimaryKey();
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
		return this.requests.getAttributesForSelect();
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
			return this.requests.getAttributesForInsert();
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
			return this.requests.getAttributesForUpdate();
	    }
}
