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

import org.telosys.tools.commons.ListUtil;
import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.doc.VelocityReturnType;
import org.telosys.tools.generator.context.names.ContextName;
import org.telosys.tools.generic.model.ForeignKey;
import org.telosys.tools.generic.model.ForeignKeyAttribute;

/**
 * Database Foreign Key exposed in the generator context
 * 
 * @author Laurent Guerin
 *
 */
//-------------------------------------------------------------------------------------
@VelocityObject(
		contextName = ContextName.FK ,
		text = {
				"This object provides all information about a database foreign key",
				"Each foreign key is retrieved from the entity class ",
				""
		},
		since = "2.0.7",
		example= {
				"",
				"#foreach( $fk in $entity.databaseForeignKeys )",
				"    $fk.name ",
				"#end"				
		}
 )
//-------------------------------------------------------------------------------------
public class ForeignKeyInContext {
	
	private final String  fkName ;
	private final String  originEntityName ; // v 3.4.0
	private final String  referencedEntityName ; // v 3.4.0
	private final List<ForeignKeyAttributeInContext> fkAttributes ; // new  in v 3.4.0
	
	private final ModelInContext modelInContext ;  // v 3.4.0

	private final EnvInContext env ; // ver 3.4.0
	
    private final boolean explicitFK;  // ver 4.1.0
	
	//-------------------------------------------------------------------------------------
	public ForeignKeyInContext(ForeignKey foreignKey, ModelInContext modelInContext, EnvInContext env ) {
		if ( foreignKey == null ) {
			throw new IllegalArgumentException("ForeignKey is null");
		}
		if ( env == null ) {
			throw new IllegalArgumentException("EnvInContext is null");
		}
		this.modelInContext = modelInContext;
		this.env = env ;
		
		this.fkName = foreignKey.getName() ;
		
		this.originEntityName = foreignKey.getOriginEntityName();
		this.referencedEntityName = foreignKey.getReferencedEntityName();
		
		this.fkAttributes = new LinkedList<>() ;
		for ( ForeignKeyAttribute fkAttribute : foreignKey.getAttributes() ) {
			fkAttributes.add( new ForeignKeyAttributeInContext(foreignKey, fkAttribute, modelInContext) );
		}
		
		this.explicitFK = foreignKey.isExplicit();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns the name of the Foreign Key"
		})
	public String getName() {
		return this.fkName;
    }

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns the name of the Foreign Key.",
		"The name is converted according to SQL conventions "
		})
	public String getSqlName() {
		SqlInContext sql = this.env.getSql();
		return sql.convertToFkName(this.getName());
    }
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
	text={ "Returns true if the Foreign Key is explicit (defined with name).",
			"Returns false for an implicit Foreign Key."},
	example= "#if ( $fk.isExplicit() )",
	since=   "4.1.0"
	)
    public boolean isExplicit() {
		return this.explicitFK ;
	}
    
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
	text={ "Returns true if the Foreign Key is composite (has more than 1 attribute)" },
	example= "#if ( $fk.isComposite() )",
	since=   "4.1.0"
	)
	public boolean isComposite() {
		return fkAttributes.size() > 1 ;
	}
    
	//-------------------------------------------------------------------------------------
	// ORIGIN ENTITY 
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns the origin entity name"
		},
	since="3.4.0")
	public String getOriginEntityName() {
		return this.originEntityName;
	}

	@VelocityMethod(
	text={	
		"Returns the origin entity object"
		},
	since="3.4.0")
	public EntityInContext getOriginEntity() {
		return this.modelInContext.getEntityByClassName(this.originEntityName);
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns the name of the table holding the foreign key",
		"converted according to SQL conventions"		
		},
	since="3.4.0")
	public String getSqlOriginTableName() {
		return getOriginEntity().getSqlTableName();
    }

	//-------------------------------------------------------------------------------------
	// REFERENCED ENTITY 
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns the referenced entity name"
		},
	since="3.4.0")
	public String getReferencedEntityName() {
		return this.referencedEntityName;
	}

	@VelocityMethod(
	text={	
		"Returns the referenced entity object"
		},
	since="3.4.0")
	public EntityInContext getReferencedEntity() {
		return this.modelInContext.getEntityByClassName(this.referencedEntityName);
	}
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns the name of the referenced table (the table referenced by the foreign key).",
		"The name is converted according to SQL conventions"
		},
	since="3.4.0")
	public String getSqlReferencedTableName() {
		return getReferencedEntity().getSqlTableName();
    }

	//-------------------------------------------------------------------------------------
	// ATTRIBUTES ( COLUMNS ) 
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns all the attributes composing the Foreign Key"
		},
	example= {
		"#foreach( $fkAttribute in $fk.attributes ) ",
		"...",
		"#end"
		},
	since= "3.4.0")
	@VelocityReturnType("List of 'Foreign Key Attribute' objects ( List of '$fkAttribute' )")
	public List<ForeignKeyAttributeInContext> getAttributes() {
		return this.fkAttributes ;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns the number of attributes composing the foreign key"
		})
	public int getAttributesCount() {
		return this.fkAttributes.size() ;
	}
	
	//-------------------------------------------------------------------------------------
	// ORIGIN ATTRIBUTES ( COLUMNS ) 
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns all the columns names composing the Foreign Key",
		"Each column name is converted according the SQL conventions"
		},
	example= {
		"#foreach( $col in $fk.sqlOriginColumns ) ",
		"...",
		"#end"
		})
	@VelocityReturnType("List<String>")
	public List<String> getSqlOriginColumns() throws GeneratorException {
		List<String> list = new LinkedList<>();
		for ( ForeignKeyAttributeInContext fkAttrib : fkAttributes ) {
			String sqlColumnName = fkAttrib.getOriginAttribute().getSqlColumnName();
			list.add(sqlColumnName);
		}
		return list ;
    }

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns all the columns names composing the Foreign Key in a single string",
		"The columns names are separated by a comma",
		"Each column name is converted according the SQL conventions"
		})
	public String getSqlOriginColumnsAsString() throws GeneratorException {
		return ListUtil.join(getSqlOriginColumns(), ",");
	}

	//-------------------------------------------------------------------------------------
	// REFERENCED ATTRIBUTES ( COLUMNS ) 
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns a list containing the names of the referenced columns.",
		"The names are converted according to SQL conventions"
		},
	example= {
		"#foreach( $col in $fk.sqlReferencedColumns ) ",
		"...",
		"#end"
		})
	@VelocityReturnType("List<String>")
	public List<String> getSqlReferencedColumns() throws GeneratorException {
		List<String> list = new LinkedList<>();
		for ( ForeignKeyAttributeInContext fkAttrib : fkAttributes ) {
			String sqlColumnName = fkAttrib.getReferencedAttribute().getSqlColumnName();
			list.add(sqlColumnName);
		}
		return list ;
    }
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns all the names of the referenced columns separated by a comma.",
		"The names are converted according to SQL conventions"
		})
	public String getSqlReferencedColumnsAsString() throws GeneratorException {
		return ListUtil.join(getSqlReferencedColumns(), ",");
    }
	
}
