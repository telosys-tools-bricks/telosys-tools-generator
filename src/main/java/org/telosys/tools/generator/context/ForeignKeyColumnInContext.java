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

import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.names.ContextName;

/**
 * Database Foreign Key Column exposed in the generator context
 * 
 * @author Laurent Guerin
 *
 */
//-------------------------------------------------------------------------------------
@VelocityObject(
		contextName = ContextName.FKCOL ,
		text = {
				"This object provides all information about a database foreign key column",
				""
		},
		since = "2.0.7",
		example= {
				"",
				"#foreach( $fkcol in $fk.columns )",
				"    $fkcol.columnName - $fkcol.targetColumnName ",
				"#end"				
		}
 )
//-------------------------------------------------------------------------------------
public class ForeignKeyColumnInContext
{
	private final int    _sequence ;

	private final String _columnName ;
	
	private final String _referencedColumnName ;
	
	//-------------------------------------------------------------------------------------
//	public JavaBeanClassForeignKeyColumn(int sequence, String columnName, String columnRef, 
//			String updateRule, String deleteRule, String deferrable) {
//		super();
//		this._sequence = sequence;
//		this._columnName = columnName;
//		this._columnRef = columnRef;
//		this._updateRule = updateRule;
//		this._deleteRule = deleteRule;
//		this._deferrable = deferrable;
//	}
	public ForeignKeyColumnInContext( int sequence, String columnName, String referencedColumnName ) {
		super();
		this._sequence   = sequence;
		this._columnName = columnName;
		this._referencedColumnName  = referencedColumnName;
	}
	 
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the name of the column"
			}
	)
	public String getColumnName() {
		return _columnName;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the sequence of the column (position in the foreign key)"
			}
	)
	public int getSequence() {
		return _sequence;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the name of the referenced column "
			}
	)
	public String getReferencedColumnName() {
		return _referencedColumnName;
	}

	//-------------------------------------------------------------------------------
//	public int compareTo(JavaBeanClassForeignKeyColumn other) {
//		if ( other != null )
//		{
//			return ( this.getSequence() - other.getSequence() );
//		}
//		return 0;
//	}
	
}
