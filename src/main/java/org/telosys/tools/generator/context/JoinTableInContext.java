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

	private final String _name ;

	private final String _schema ;

	private final String _catalog ;
	
	private final LinkedList<JoinColumnInContext>  _joinColumns ;

	private final LinkedList<JoinColumnInContext>  _inverseJoinColumns ;
	
	
	public JoinTableInContext(JoinTable joinTable) {
		super();
		_name    = joinTable.getName();
		_schema  = joinTable.getSchema();
		_catalog = joinTable.getCatalog();
		
		//--- Build the list of "join columns"
		_joinColumns = new LinkedList<JoinColumnInContext>();
		if ( joinTable.getJoinColumns() != null ) {
			for ( JoinColumn jc : joinTable.getJoinColumns() ) {
				_joinColumns.add( new JoinColumnInContext(jc) ) ;
			}
		}

		//--- Build the list of "inverse join columns"
		_inverseJoinColumns = new LinkedList<JoinColumnInContext>();
		if ( joinTable.getInverseJoinColumns() != null ) {
			for ( JoinColumn jc : joinTable.getInverseJoinColumns() ) {
				_inverseJoinColumns.add( new JoinColumnInContext(jc) ) ;
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
		return _name;
	}

	//--------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the table schema if any"
			}
	)
	public String getSchema() {
		return _schema;
	}

	//--------------------------------------------------------------------------
	@VelocityMethod(
			text={	
				"Returns the table catalog if any "
				}
		)
	public String getCatalog() {
		return _catalog;
	}

	//--------------------------------------------------------------------------
	@VelocityMethod(
			text={	
				"Returns the join columns"
				}
		)
	public LinkedList<JoinColumnInContext> getJoinColumns()
	{
		return _joinColumns ;
	}
	
	//--------------------------------------------------------------------------
	@VelocityMethod(
			text={	
				"Returns the inverse join columns"
				}
		)
	public LinkedList<JoinColumnInContext> getInverseJoinColumns()
	{
		return _inverseJoinColumns ; 
	}
	
	//-------------------------------------------------------------------------------------
	@Override
	public String toString() {
		return _name ;
	}
}
