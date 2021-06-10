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

import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.names.ContextName;
import org.telosys.tools.generic.model.JoinColumn;
import org.telosys.tools.generic.model.JoinTable;

/**
 * "JoinTable" description <br>
 *
 * @author Laurent GUERIN
 */
//-------------------------------------------------------------------------------------
@VelocityObject(
		contextName = ContextName.JOIN_TABLE ,
		text = {
				"This object provides the description of a 'join table' ",
				""
		},
		since = "2.1.0",
		example= {
				"",
				"#if ( $link.hasJoinTable() )",
				"#set($joinTable = $link.joinTable)",
				" name   : $joinTable.name",
				" schema : $joinTable.schema",
				"#end"				
		}
)
public class JoinTableInContext
{

	private final String name ;
	private final String schema ;
	private final String catalog ;
	private final LinkedList<JoinColumnInContext>  joinColumns ;
	private final LinkedList<JoinColumnInContext>  inverseJoinColumns ;
	
	/**
	 * Constructor
	 * @param joinTable
	 */
	public JoinTableInContext(JoinTable joinTable) {
		super();
		name    = joinTable.getName();
		schema  = joinTable.getSchema();
		catalog = joinTable.getCatalog();
		
		//--- Build the list of "join columns"
		joinColumns = new LinkedList<>();
		if ( joinTable.getJoinColumns() != null ) {
			for ( JoinColumn jc : joinTable.getJoinColumns() ) {
				joinColumns.add( new JoinColumnInContext(jc) ) ;
			}
		}

		//--- Build the list of "inverse join columns"
		inverseJoinColumns = new LinkedList<>();
		if ( joinTable.getInverseJoinColumns() != null ) {
			for ( JoinColumn jc : joinTable.getInverseJoinColumns() ) {
				inverseJoinColumns.add( new JoinColumnInContext(jc) ) ;
			}
		}
	}

	//--------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the table name "
			}
	)
	public String getName() {
		return name;
	}

	//--------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the table schema if any"
			}
	)
	public String getSchema() {
		return schema;
	}

	//--------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the table catalog if any "
			}
	)
	public String getCatalog() {
		return catalog;
	}

	//--------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the join columns"
			}
	)
	public List<JoinColumnInContext> getJoinColumns()
	{
		return joinColumns ;
	}
	
	//--------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the inverse join columns"
			}
	)
	public List<JoinColumnInContext> getInverseJoinColumns()
	{
		return inverseJoinColumns ; 
	}
	
	//-------------------------------------------------------------------------------------
	@Override
	public String toString() {
		return name ;
	}
}
