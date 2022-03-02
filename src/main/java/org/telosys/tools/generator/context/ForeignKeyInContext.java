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
//	private final String  tableName ; // removed in v 3.4.0
	private final String  originEntityName ; // v 3.4.0
//	private final String  targetTableName  ; // removed in v 3.4.0
	private final String  referencedEntityName ; // v 3.4.0
//	private final List<ForeignKeyColumnInContext> fkColumns ; // removed in v 3.4.0
	private final List<ForeignKeyAttributeInContext> fkAttributes ; // new  in v 3.4.0
	
//	private int updateRuleCode = 0 ; // removed in v 3.4.0
//	private int deleteRuleCode = 0 ; // removed in v 3.4.0
//	private int deferrableCode = 0 ; // removed in v 3.4.0
	
	private final ModelInContext modelInContext ;  // v 3.4.0

	private final EnvInContext env ; // ver 3.4.0
	
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
//		this.tableName = foreignKey.getTableName() ; // removed in v 3.4.0
//		this.targetTableName = foreignKey.getReferencedTableName(); // removed in v 3.4.0
		
		this.originEntityName = foreignKey.getOriginEntityName();
		this.referencedEntityName = foreignKey.getReferencedEntityName();
		
//		this.updateRuleCode = 0 ; // removed in v 3.4.0
//		this.deleteRuleCode = 0 ; // removed in v 3.4.0
//		this.deferrableCode = 0 ; // removed in v 3.4.0
//		this.fkColumns = new LinkedList<>() ;

		//--- V 3.0.0 
//		//--- ON UPDATE, ON DELETE and DEFERRABLE (stored in each column in meta-data, keep the last one)
//		this.updateRuleCode = foreignKey.getUpdateRuleCode() ;
//		this.deleteRuleCode = foreignKey.getDeleteRuleCode() ;
//		this.deferrableCode = foreignKey.getDeferrableCode() ;
		
//		for ( ForeignKeyColumn metadataFKColumn : foreignKey.getColumns() ) { 
//			fkColumns.add( new ForeignKeyColumnInContext(metadataFKColumn) );
//		}
		// v 3.4.0
		this.fkAttributes = new LinkedList<>() ;
		for ( ForeignKeyAttribute fkAttribute : foreignKey.getAttributes() ) {
			fkAttributes.add( new ForeignKeyAttributeInContext(foreignKey, fkAttribute, modelInContext) );
		}
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
//	@VelocityMethod(
//	text={	
//		"Returns the name of the table holding the foreign key"
//		})
//	public String getTableName() { // removed in v 3.4.0
//		return this.tableName ;
//	}

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
//	@VelocityMethod(
//	text={	
//		"Returns the name of the referenced table (the table referenced by the foreign key)"
//		})
//	public String getReferencedTableName() { // removed in v 3.4.0
//		return this.targetTableName ;
//	}
	
	//-------------------------------------------------------------------------------------
//	@VelocityMethod(
//	text={	
//		"Returns all the columns composing the Foreign Key",
//		"(sorted in the original database order)"
//		},
//	example= {
//		"#foreach( $fkcol in $fk.columns ) ",
//		"...",
//		"#end"
//		})
//	@VelocityReturnType("List of 'Foreign Key Column' objects ( List of '$fkcol' )")
//	public List<ForeignKeyColumnInContext> getColumns() { // removed in v 3.4.0
//		return this.fkColumns ;
//	}
	
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
//	@VelocityMethod(
//	text={	
//		"Returns the number of columns composing the foreign key"
//		})
//	public int getColumnsCount() {
//		return this.fkColumns.size() ;
//	}
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
	public List<String> getSqlOriginColumns() throws GeneratorException {
		List<String> list = new LinkedList<>();
//		SqlInContext sql = this.env.getSql();
//		if ( fkColumns != null ) {
//			for ( ForeignKeyColumnInContext col : fkColumns ) {
//				list.add(sql.convertToColumnName(col.getColumnName()));
//			}
//		}
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
	public List<String> getSqlReferencedColumns() throws GeneratorException {
		List<String> list = new LinkedList<>();
//		SqlInContext sql = this.env.getSql();
//		if ( fkColumns != null ) {
//			for ( ForeignKeyColumnInContext col : fkColumns ) {
//				list.add( sql.convertToColumnName(col.getReferencedColumnName()) );
//			}
//		}
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
	

	
	
	//-------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------
//	@VelocityMethod(
//	text={	
//		"Returns the 'DEFERRABILITY' status ( 'NOT DEFERRABLE', 'INITIALLY IMMEDIATE', 'INITIALLY DEFERRED'  ) "
//		})
//	public String getDeferrable() {
//		return MetadataUtil.getForeignKeyDeferrability(deferrableCode).toUpperCase();
//	}
//	//-------------------------------------------------------------------------------------
//	@VelocityMethod(
//	text={	
//		"Returns the 'DEFERRABILITY' status code ( MetaData Code : 5,6,7 ) "
//		})
//	public int getDeferrableCode() {
//		return deferrableCode;
//	}
//
//	//-------------------------------------------------------------------------------------
//	@VelocityMethod(
//	text={	
//		"Returns the 'ON DELETE' rule ( 'NO ACTION', 'RESTRICT', 'SET NULL', 'SET DEFAULT', 'CASCADE'  ) "
//		})
//	public String getDeleteRule() {
//		return MetadataUtil.getForeignKeyDeleteRule(deleteRuleCode).toUpperCase();
//	}
//	//-------------------------------------------------------------------------------------
//	@VelocityMethod(
//	text={	
//		"Returns the 'ON DELETE' rule code ( MetaData Code : 0,1,2,3,4 ) "
//		})
//	public int getDeleteRuleCode() {
//		return deleteRuleCode;
//	}
//
//	//-------------------------------------------------------------------------------------
//	@VelocityMethod(
//	text={	
//		"Returns the 'ON UPDATE' rule ( 'NO ACTION', 'RESTRICT', 'SET NULL', 'SET DEFAULT', 'CASCADE' ) "
//		})
//	public String getUpdateRule() {
//		return MetadataUtil.getForeignKeyUpdateRule(updateRuleCode).toUpperCase();
//	}
//	//-------------------------------------------------------------------------------------
//	@VelocityMethod(
//	text={	
//		"Returns the 'ON UPDATE' rule code ( MetaData Code : 0,1,2,3,4 ) "
//		})
//	public int getUpdateRuleCode() {
//		return updateRuleCode;
//	}
}
