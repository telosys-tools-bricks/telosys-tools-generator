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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.names.ContextName;
import org.telosys.tools.generic.model.types.NeutralType;

/**
 * Set of functions dedicated to the 'H2' database ( $h2.functionName(...) ) <br>
 * The H2 database is very useful for JUnit test cases
 * 
 * @author Laurent Guerin
 *
 */
//-------------------------------------------------------------------------------------
@VelocityObject(
		contextName=ContextName.H2,
		text = { 
				"Object providing a set of functions for the H2 database",
				""
		},
		since = "2.1.1"
 )
//-------------------------------------------------------------------------------------
public class H2InContext {

	//-------------------------------------------------------------------------------------
	@VelocityMethod(text={	
			"Returns the 'CREATE TABLE' DDL statement for the given entity",
			"The DDL statement is splitted in a list of lines "
			},
			parameters = { 
				"entity : the entity "
			},
			example = {
				"$h2.ddlCreateTable($entity) " },
			since = "2.1.1"
			)
	public List<String> ddlCreateTable(final EntityInContext entity) {
		List<String> lines1 = buildTableDefinition (entity);
		List<String> resultingLines = new LinkedList<>();
//		lines2.add( "CREATE TABLE " + SqlTableNameProvider.getTableName(entity) + " (") ; 
		resultingLines.add( "CREATE TABLE " + entity.getSqlTableName() + " (") ; // v 4.1.0
		
		int c = 0 ;
		for ( String line : lines1 ) {
			c++;
			if ( c < lines1.size() ) {
				resultingLines.add(line + ",");
			}
			else {
				resultingLines.add(line);
			}
		}		
		resultingLines.add( ");" ) ;
		return resultingLines ;
	}	
	
	private List<String> buildTableDefinition (final EntityInContext entity) {
		List<String> lines = new LinkedList<>();
		//--- Primary Key columns first 
		for ( AttributeInContext attribute : entity.getKeyAttributes() ) {
			lines.add( buildColumnDefinition(attribute) );
		}
		//--- Other columns 
		for ( AttributeInContext attribute : entity.getNonKeyAttributes() ) {
			lines.add( buildColumnDefinition(attribute) );
		}
		//--- Primary Key declaration 
		if ( entity.hasPrimaryKey() ) {
			lines.add( buildPrimaryKeyDefinition(entity) );
		}
		return lines ;
	}	

	/**
	 * Builds H2 column definition for given attribute
	 * @param attribute
	 * @return
	 */
	private String buildColumnDefinition (final AttributeInContext attribute ) {
		StringBuilder sb = new StringBuilder();
//		sb.append( attribute.getDatabaseName() ) ;
		sb.append( attribute.getSqlColumnName() ); // v 4.1.0
		sb.append( " ") ;
		sb.append( getColumnType(attribute) ) ;
		sb.append( " ") ;
		if ( attribute.isAutoIncremented() ) {
			sb.append( "AUTO_INCREMENT ") ;
		}
		if ( attribute.isNotNull() ) {
			sb.append( "NOT NULL") ;
		}
		return sb.toString();
	}
	
	/**
	 * Builds H2 primary key definition for given entity
	 * @param entity
	 * @return
	 */
	private String buildPrimaryKeyDefinition (final EntityInContext entity ) {
		// PRIMARY KEY(code)
		StringBuilder sb = new StringBuilder();
		sb.append("PRIMARY KEY(");
		int c = 0 ;
		for ( AttributeInContext attribute : entity.getKeyAttributes() ) {
			c++;
			if ( c > 1 ) {
				sb.append(",");
			}
//			sb.append(attribute.getDatabaseName());
			sb.append( attribute.getSqlColumnName() ); // v 4.1.0
		}
		sb.append(")");
		return sb.toString();
	}

	/**
	 * Returns H2 SQL type for the given attribute
	 * @param attribute
	 * @return
	 */
	private String getColumnType(final AttributeInContext attribute ) {
		if ( attribute.isAutoIncremented() ) {
			//--- Particular case : Auto-incremented column
			return "IDENTITY" ; // H2 type mapped to java.lang.Long 
		}
		else {
			//--- Standard case : get the mapped type
			String h2Type = mappingNeutralTypeToH2Type.get(attribute.getNeutralType()) ;
			if ( h2Type == null ) {
				h2Type = "UNKNOWN_TYPE";
			}
			return h2Type ;
		}
	}
	/*
	 * Mapping : "Neutral type" --> "H2 database type"
	 */
	private static final Map<String,String> mappingNeutralTypeToH2Type = new HashMap<>() ; 
	static {
		mappingNeutralTypeToH2Type.put(NeutralType.BINARY,    "BINARY"   );
		mappingNeutralTypeToH2Type.put(NeutralType.BOOLEAN,   "BOOLEAN"  );
		mappingNeutralTypeToH2Type.put(NeutralType.BYTE,      "TINYINT"  );
		mappingNeutralTypeToH2Type.put(NeutralType.DATE,      "DATE"     );
		mappingNeutralTypeToH2Type.put(NeutralType.DECIMAL,   "DECIMAL"  );
		mappingNeutralTypeToH2Type.put(NeutralType.DOUBLE,    "DOUBLE"   );
		mappingNeutralTypeToH2Type.put(NeutralType.FLOAT,     "FLOAT"    );
		mappingNeutralTypeToH2Type.put(NeutralType.INTEGER,   "INTEGER"  );
		mappingNeutralTypeToH2Type.put(NeutralType.LONG,      "BIGINT"   );
		mappingNeutralTypeToH2Type.put(NeutralType.SHORT,     "SMALLINT" );
		mappingNeutralTypeToH2Type.put(NeutralType.STRING,    "VARCHAR"  );
		mappingNeutralTypeToH2Type.put(NeutralType.TIME,      "TIME"     );
		mappingNeutralTypeToH2Type.put(NeutralType.TIMESTAMP, "TIMESTAMP");
	}
	
}
