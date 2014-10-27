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
import java.util.List;

import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.names.ContextName;

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

//	public H2InContext() {
//		super();
//	}
	
	//==============================================================================================
	// Version 2.1.1
	//==============================================================================================
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
		List<String> lines2 = new LinkedList<String>();
		lines2.add( "CREATE TABLE " + entity.getDatabaseTable() + " (") ;
		
		int c = 0 ;
		int last = lines1.size() ;
		for ( String line : lines1 ) {
			c++;
			if ( c != last ) {
				lines2.add(line + ",");
			}
			else {
				lines2.add(line);
			}
		}		
		lines2.add( ");" ) ;
		return lines2 ;
	}	

	private List<String> buildTableDefinition (final EntityInContext entity) {
		List<String> lines = new LinkedList<String>();
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
			sb.append(attribute.getDatabaseName());
		}
		sb.append(")");
		return sb.toString();
	}

	private String buildColumnDefinition (final AttributeInContext attribute ) {
		//"${attribute.databaseName} ${attribute.databaseType} #if($attribute.isNotNull()) NOT NULL #end ,"
		StringBuilder sb = new StringBuilder();
		sb.append( attribute.getDatabaseName() ) ;
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
	
	private String getColumnType (final AttributeInContext attribute ) {
		
		//--- Particular case : Auto-incremented column
		if ( attribute.isAutoIncremented() ) {
			return "IDENTITY" ; // H2 type mapped to java.lang.Long 
		}
		int jdbcType = attribute.getJdbcTypeCode() ;
		switch (jdbcType) {
		
			//--- STRING TYPES
			case java.sql.Types.CHAR :
				return "CHAR("+attribute.getDatabaseSize()+")"; // H2 type mapped to java.lang.String
			case java.sql.Types.VARCHAR :
				return "VARCHAR("+attribute.getDatabaseSize()+")"; // H2 type mapped to java.lang.String
			case java.sql.Types.LONGVARCHAR :
				return "LONGVARCHAR("+attribute.getDatabaseSize()+")"; // H2 type mapped to java.lang.String
				
			//--- INTEGER TYPES
			case java.sql.Types.TINYINT :
				return "TINYINT"; // H2 type mapped to java.lang.Byte 
				
			case java.sql.Types.SMALLINT :
				return "SMALLINT"; // H2 type mapped to java.lang.Short 
				
			case java.sql.Types.INTEGER :
				return "INTEGER"; // H2 type mapped to java.lang.Integer 
				
			case java.sql.Types.BIGINT :
				return "BIGINT"; // H2 type mapped to java.lang.Long 
				
			case java.sql.Types.NUMERIC :
				return "NUMERIC"; // H2 type mapped to java.math.BigDecimal
			case java.sql.Types.DECIMAL :
				return "DECIMAL"; // H2 type mapped to java.math.BigDecimal
			
			//--- REAL/DOUBLE TYPES
			case java.sql.Types.REAL :
				return "REAL"; // H2 type mapped to java.lang.Float 
				
			case java.sql.Types.FLOAT :
				return "FLOAT"; // H2 type mapped to java.lang.Double 
				
			case java.sql.Types.DOUBLE :
				return "DOUBLE"; // H2 type mapped to java.lang.Double 
				
			//--- DATE/TIME TYPES
			case java.sql.Types.DATE :
				return "DATE"; // H2 type mapped to java.sql.Date 
					
			case java.sql.Types.TIME :
				return "TIME"; // H2 type mapped to java.sql.Time 
					
			case java.sql.Types.TIMESTAMP :
				return "TIMESTAMP"; // H2 type mapped to java.sql.Timestamp 
					
			//--- BOOLEAN TYPES
			case java.sql.Types.BOOLEAN :
				return "BOOLEAN"; // H2 type mapped to java.lang.Boolean 
			case java.sql.Types.BIT :
				return "BIT"; // H2 type mapped to java.lang.Boolean 
				
			//--- BINARY TYPES
			case java.sql.Types.BINARY :
				return "BINARY"; // H2 type mapped to byte[]
			case java.sql.Types.VARBINARY :
				return "VARBINARY"; // H2 type mapped to byte[]
			case java.sql.Types.LONGVARBINARY :
				return "LONGVARBINARY"; // H2 type mapped to byte[]
				
			//--- LOB TYPES
			case java.sql.Types.BLOB :
				return "BLOB"; // H2 type mapped to java.sql.Blob
			case java.sql.Types.CLOB :
				return "CLOB"; // H2 type mapped to java.sql.Clob
				
			//--- DEFAULT
			default :
				return "OTHER"; // H2 type mapped to java.lang.Object 
		}
	}
}
