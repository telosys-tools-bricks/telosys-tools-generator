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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.telosys.tools.commons.dbcfg.DatabaseConfiguration;
import org.telosys.tools.commons.dbcfg.DatabasesConfigurations;
import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.names.ContextName;

/**
 * Class for a list of databases defined in the "databases.dbcfg" file 
 * 
 * @author Laurent GUERIN
 * 
 */
//-------------------------------------------------------------------------------------
@VelocityObject(
		contextName= ContextName.DATABASES ,
		text = {
				"Object representing all the databases defined in the 'databases.dbcfg' file",
				""
		},
		since = "2.1.0"
 )
//-------------------------------------------------------------------------------------
public class DatabasesInContext {

	//private int databaseDefaultId = 0 ;
	
	private Map<Integer, DatabaseInContext> _databasesMap = new HashMap<Integer, DatabaseInContext>();

	/**
	 * Constructor
	 */
	public DatabasesInContext( DatabasesConfigurations databasesConfigurations ) {
		super();
		//--- Build the map of "DatabaseInContext"
		List<DatabaseConfiguration> list = databasesConfigurations.getDatabaseConfigurationsList();
		for ( DatabaseConfiguration dbcfg : list ) {
			DatabaseInContext db = new DatabaseInContext(dbcfg) ;
			Integer databaseId = new Integer(db.getId());
			_databasesMap.put(databaseId, db);
		}
	}
	
//	public int getDatabaseDefaultId() {
//		return databaseDefaultId;
//	}

	//----------------------------------------------------------------------------------
	@VelocityMethod(
		text={ "Returns the database for the given id (or null if none)" },
		parameters={"id : the database id"}
		)
	public DatabaseInContext getDatabase(int id) {
		return _databasesMap.get(new Integer(id)) ;
	}
	
	//----------------------------------------------------------------------------------
	@VelocityMethod(
		text={ "Returns TRUE if a database is defined for the given id, else FALSE" },
		parameters={"id : the database id"}
		)
	public boolean hasDatabase(int id) {
		return _databasesMap.get(new Integer(id)) != null ;
	}
	
	//----------------------------------------------------------------------------------
	@VelocityMethod(
		text={ "Returns the number of databases " }
		)
	public int getNumberOfDatabases() {
		return _databasesMap.size();
	}
	
	//----------------------------------------------------------------------------------
	@VelocityMethod(
		text={ "Returns the list of databases " }
		)
	public List<DatabaseInContext> getList() {
		
		//--- List of sorted id
		ArrayList<Integer> keysArrayList = new ArrayList<Integer>(_databasesMap.keySet()) ;
		Collections.sort(keysArrayList) ;
		
		//--- List of databases sorted by id 
		LinkedList<DatabaseInContext> list = new LinkedList<DatabaseInContext>();
		for ( Integer id : keysArrayList ) {
			list.add( _databasesMap.get(id) ) ;
		}
		return list;
	}
}
