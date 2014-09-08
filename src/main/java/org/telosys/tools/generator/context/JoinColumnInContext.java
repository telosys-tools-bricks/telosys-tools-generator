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

import org.telosys.tools.generator.GeneratorContextException;
import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.names.ContextName;
import org.telosys.tools.repository.model.JoinColumn;

/**
 * Link exposed in the Velocity Context 
 *  
 * @author L.Guerin
 */
//-------------------------------------------------------------------------------------
@VelocityObject(
		contextName = ContextName.JOIN_COLUMN ,
		text = {
				"This object provides the description of a 'join column' ",
				""
		},
		since = "2.1.0",
		example= {
				"",
				"#foreach( $joinColumn in $link.joinColumns )",
				" Column name = $joinColumn.name",
				"#end"				
		}
		
 )
//-------------------------------------------------------------------------------------
public class JoinColumnInContext {
	
	private final String  _name;
 
	private final String  _referencedColumnName;

	private final boolean _unique ;

	private final boolean _nullable ;

	private final boolean _updatable ; 

	private final boolean _insertable ; 


	//-------------------------------------------------------------------------------------
	/**
	 * Constructor
	 */
	public JoinColumnInContext(final JoinColumn joinColumn ) throws GeneratorContextException
	{
		if ( joinColumn.getName() == null ) {
			throw new GeneratorContextException("Name is null in 'Join Column' ");
		}
		if ( joinColumn.getReferencedColumnName() == null ) {
			throw new GeneratorContextException("Referenced name is null in 'Join Column'");
		}

		this._name                 = joinColumn.getName();		 
		this._referencedColumnName = joinColumn.getReferencedColumnName();
		
		this._unique     = joinColumn.isUnique();
		this._nullable   = joinColumn.isNullable();
		this._updatable  = joinColumn.isUpdatable() ; 
		this._insertable = joinColumn.isInsertable() ; 
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the origin side's column name "
			}
	)
	public String getName() {
		return _name;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the referenced column name (target side) "
			}
	)
	public String getReferencedColumnName() {
		return _referencedColumnName;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if 'unique' "
			}
	)
	public boolean isUnique() {
		return _unique;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if 'nullable' "
			}
	)
	public boolean isNullable() {
		return _nullable;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if 'updatable' "
			}
	)
	public boolean isUpdatable() {
		return _updatable;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if 'insertable' "
			}
	)
	public boolean isInsertable() {
		return _insertable;
	}

}
