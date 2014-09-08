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

import org.telosys.tools.repository.model.InverseJoinColumns;
import org.telosys.tools.repository.model.JoinColumn;
import org.telosys.tools.repository.model.JoinColumns;
import org.telosys.tools.repository.model.JoinTable;


/**
 * "JoinTable" description <br>
 *
 * @author Laurent GUERIN
 */
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
		JoinColumns joinColumns = joinTable.getJoinColumns() ;
		for ( JoinColumn jc : joinColumns ) {
			_joinColumns.add( new JoinColumnInContext(jc) ) ;
		}

		//--- Build the list of "inverse join columns"
		_inverseJoinColumns = new LinkedList<JoinColumnInContext>();
		InverseJoinColumns inverseJoinColumns = joinTable.getInverseJoinColumns() ;
		for ( JoinColumn jc : inverseJoinColumns ) {
			_inverseJoinColumns.add( new JoinColumnInContext(jc) ) ;
		}
	}

	//--------------------------------------------------------------------------
	public String getName() {
		return _name;
	}

	//--------------------------------------------------------------------------
	public String getSchema() {
		return _schema;
	}

	//--------------------------------------------------------------------------
	public String getCatalog() {
		return _catalog;
	}

	//--------------------------------------------------------------------------
	public LinkedList<JoinColumnInContext> getJoinColumns()
	{
		return _joinColumns ;
	}
	
	//--------------------------------------------------------------------------
	public LinkedList<JoinColumnInContext> getInverseJoinColumns()
	{
		return _inverseJoinColumns ; 
	}
}
