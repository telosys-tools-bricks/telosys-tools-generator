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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Properties;

import org.telosys.tools.commons.NamingStyleConverter;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.exceptions.GeneratorSqlException;
import org.telosys.tools.generator.context.names.ContextName;

//-------------------------------------------------------------------------------------
@VelocityObject(
	contextName=ContextName.SQL,
	text = { 
			"Object for schema creation in SQL language (for a relational database)",
			"with functions for :",
			" - names conversion (table name, column name, pk/fk name)",
			" - field type conversion (neutral type to SQL column type)",
			"It is designed to facilitate DDL commands generation",
			"( CREATE TABLE, ADD CONSTRAINT FOREIGN KEY, etc) ",
			"",
			"Each instance of this object is dedicated to a database type  ",
			"To get a new instance use : $factory.newSql('DatabaseName') ",
			"or $factory.newSql('DatabaseName', specificConventionsFileName)",
			""
	},
	example = {	
			"## Get predefined conventions for a standard database (eg 'PostgreSQL') ",
			"## Known databases names : 'SQL-ANSI', 'PostgreSQL', 'Oracle', 'SQLServer'",
			"## Database name is not case sensitive",
			"#set( $sql = $factory.newSql('PostgreSQL') )",
			"",
			"## Get specific conventions using a specific file located in the bundle of templates",
			"#set( $sql = $factory.newSql('PostgreSQL', $fn.fileFromBundle('postgresql.properties') ) )",
			""
		},		
	since = "3.4.0"
 )
//-------------------------------------------------------------------------------------
public class SqlInContext {
	// TODO : see also 	:
	// 
	// and usages, eg :
	//    JdbcInContext -> JdbcRequets : uses attribute.getDatabaseName()

	private static final int FK_ORIGIN_SIDE     = 1 ;
	private static final int FK_REFERENCED_SIDE = 2 ;
	
	private static final String CONV_TABLE_NAME  = "conv.tableName";
	private static final String CONV_COLUMN_NAME = "conv.columnName";
	private static final String CONV_PK_NAME     = "conv.pkName";
	private static final String CONV_FK_NAME     = "conv.fkName";
	
	private final NamingStyleConverter converter = new NamingStyleConverter();

	private String targetDbName ;
	private String targetDbConfigFile ;
	private Properties targetDbConfig ;
	
	private String tableNameStyle;
	private String columnNameStyle;
	private String pkNameStyle;
	private String fkNameStyle;

	private void init(String targetDbName, String targetDbConfigFile, Properties properties) {
		this.targetDbName = targetDbName;
		this.targetDbConfigFile = targetDbConfigFile ;
		this.targetDbConfig = properties;
		this.tableNameStyle  = getConfigValue(CONV_TABLE_NAME);
		this.columnNameStyle = getConfigValue(CONV_COLUMN_NAME);
		this.pkNameStyle     = getConfigValue(CONV_PK_NAME);
		this.fkNameStyle     = getConfigValue(CONV_FK_NAME);
	}

	/**
	 * Constructor for default database configuration (embeded in .jar resources)
	 * @param targetDbName
	 */
	public SqlInContext(String targetDbName) {
		super();
		if ( StrUtil.nullOrVoid(targetDbName) ) {
			throw new GeneratorSqlException("Target database name undefined, cannot create $sql");
		}
		String fileName = "target-db/" + targetDbName.trim().toLowerCase() + ".properties" ;
		init(targetDbName, fileName, loadStandardConfiguration(fileName));
	}
	
	/**
	 * Constructor for default database configuration file
	 * @param targetDbName
	 * @param targetDbConfigFile
	 */
	public SqlInContext(String targetDbName, File targetDbConfigFile) {
		super();
		if ( StrUtil.nullOrVoid(targetDbName) ) {
			throw new GeneratorSqlException("Target database name undefined, cannot create $sql");
		}
		if ( targetDbConfigFile == null ) {
			throw new GeneratorSqlException("Target database config file undefined, cannot create $sql");
		}
		init(targetDbName, targetDbConfigFile.getAbsolutePath(), loadSpecificConfiguration(targetDbConfigFile));
	}
		
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Returns the target database name",
			""
		},
		example={	
				"$sql.databaseName()"
			},
		since = "3.4.0"
	)
	public String getDatabaseName() {
		return this.targetDbName;
    }

	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Returns the target database configuration file",
			""
		},
		example={	
				"$sql.databaseConfigFile()"
			},
		since = "3.4.0"
	)
	public String getDatabaseConfigFile() {
		return this.targetDbConfigFile;
    }
	
	//-------------------------------------------------------------------------------------
	// Conversion from string
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Converts the given string to table naming style ",
			"For example converts 'EmployeeJobs' to 'employee_jobs'",
			""
		},
		example={	
				"$sql.convertToTableName($var)"
			},
		since = "3.4.0" 
	)
	public String convertToTableName(String originalName) {
		return convertName(originalName, tableNameStyle);
    }
		
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Converts the given string to column naming style ",
			"For example converts 'firstName' to 'first_name' ",
			""
		},
		parameters = { 
			"originalName : name to be converted " 
		},
		example={	
			"$sql.convertToColumnName($var)"
		},
		since = "3.4.0"
	)
	public String convertToColumnName(String originalName) {
		return convertName(originalName, columnNameStyle);
    }
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Converts the given string to primary key naming style ",
			"For example converts 'PkFirstName' to 'pk_first_name' ",
			""
		},
		parameters = { 
			"originalName : name to be converted " 
		},
		example={	
			"$sql.convertToPkName($var)"
		},
		since = "3.4.0"
	)
	public String convertToPkName(String originalName) {
		return convertName(originalName, pkNameStyle);
    }
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Converts the given string to foreign key naming style ",
			"For example converts 'PkFirstName' to 'pk_first_name' ",
			""
		},
		parameters = { 
			"originalName : name to be converted " 
		},
		example={	
			"$sql.convertToFkName($var)"
		},
		since = "3.4.0"
	)
	public String convertToFkName(String originalName) {
		return convertName(originalName, fkNameStyle);
    }
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Converts the given neutral type to column type ",
			"For example converts 'string' to 'varchar(20)' ",
			""
		},
		example={	
			"$sql.convertToColumnType('string', false, 20)"
			},
		parameters = { 
			"neutralType : neutral type to be converted " ,
			"autoInc : auto-incremented attribute (true/false)",
			"size : maximum size (for a variable length string, eg 45 or 8.2  )"
		},
		since = "3.4.0"
	)
	public String convertToColumnType(String neutralType, boolean autoInc, BigDecimal size) {
		// get SQL type from database config
		String sqlType = getConfigType(neutralType, autoInc);
		
		// replace "size" or "precision,scale" variable if any 
		if ( sqlType.contains("%") ) {
			return replaceVar(sqlType, size);
		}
		else {
			return sqlType;
		}
	}
			
	//-------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Returns the database table name for the given entity ",
			"If the table name is defined in the model it is used in priority",
			"if no table name is defined then the entity name is converted to table name",
			"by applying the target database conventions",
			"(for example 'student_projects' for an entity named 'StudentProjects')",
			""
		},
		example={	
				"$sql.tableName($entity)"
			},
		since = "3.4.0"
	)
	public String tableName(EntityInContext entity) {
		// Use entity database table name if defined in model
//		String tableNameInModel = entity.getDatabaseTable() ;
//		if ( ! StrUtil.nullOrVoid(tableNameInModel) ) {
		if ( entity.hasDatabaseTable() ) {
			// defined in the model => use it as is
//			return tableNameInModel ;
			return entity.getDatabaseTable() ;
		}
		else {
			// not defined in the model => convert attribute database name
			// ( with target database naming convention ) 
			return convertToTableName(entity.getName());
		}
    }
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Returns the database column name for the given attribute ",
			"For example 'city_code' for an attribute named 'cityCode'",
			"The database name defined in the model is used in priority",
			"if no database name is defined then the attribute name is converted to database name",
			"by applying the target database conventions",
			""
		},
		parameters = { 
			"attribute : attribute from which to get column name " ,
		},
		example={	
			"$sql.columnName($attribute)"
		},
		since = "3.4.0"
	)
	public String columnName(AttributeInContext attribute) {
		// Use attribute database name (by default = attribute name)
		String databaseName = attribute.getDatabaseName() ;
		if ( ! StrUtil.nullOrVoid(databaseName) ) {
			// defined in the model => use it as is
			return databaseName ;
		}
		else {
			// not defined in the model => convert attribute database name
			// ( with target database naming convention ) 
			return convertToColumnName(attribute.getName());
		}
    }
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Converts the attribute neutral type to the corresponding SQL type ",
			"For example converts 'string' to 'varchar(x)' ",
			"The database type defined in the model is used in priority",
			"if no database type is defined then the attribute type is converted to database type",
			"by applying the target database conventions",
			""
		},
		parameters = { 
			"attribute : attribute from which to get column type " ,
		},
		example={	
				"$sql.columnType($attribute)"
			},
		since = "3.4.0"
	)
	public String columnType(AttributeInContext attribute) {
		// Check if the attribute has a specific database type in the model
		String databaseType = attribute.getDatabaseType() ;
		if ( ! StrUtil.nullOrVoid(databaseType) ) {
			// defined in the model => use it as is
			return databaseType ;
		}
		else {
			// not defined in the model : convert neutral type
			// ( with target database type convertion rules ) 
			return convertToColumnType(attribute.getNeutralType(), 
					attribute.isAutoIncremented(), 
					attribute.getSizeAsDecimal() );
		}
    }
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Returns the column constraints for the given attribute",
			"For example : NOT NULL DEFAULT 12",
			""
		},
		parameters = { 
			"attribute : attribute from which to get column constraints " ,
		},
		example={	
			"$sql.columnConstraints($attribute)"
		},
		since = "3.4.0"
	)
	public String columnConstraints(AttributeInContext attribute) {
		StringBuilder sb = new StringBuilder();
		
		//--- NOT NULL
		if ( attribute.isNotNull() ) {
			// 'isDatabaseNotNull' is only for DB-Model (no annotation for DSL-Model)
			// do not use it 
			sb.append("NOT NULL");
		}
		
		//--- UNIQUE 
		if ( attribute.isUnique() ) {
			if ( sb.length() > 0 ) {
				sb.append(" ");
			}
			sb.append("UNIQUE");
		}
		
		//--- DEFAULT
		String defaultValue = null ;
		if ( attribute.hasDatabaseDefaultValue() ) {
			defaultValue = attribute.getDatabaseDefaultValue();
		}
		else if ( attribute.hasDefaultValue() ) {
			defaultValue = attribute.getDefaultValue();
		}
		if ( defaultValue != null ) {
			if ( sb.length() > 0 ) {
				sb.append(" ");
			}
			sb.append("DEFAULT ");
			if ( attribute.isStringType() ) {
				sb.append("'").append(defaultValue).append("'");
			}
			else {
				sb.append(defaultValue);
			}
		}
		return sb.toString();
    }

	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Returns a string containing the names of all the columns composing the primary key",
			"for the given entity",
			"Each column name is converted according to the naming rules for the target database",
			"For example 'id' or 'code, group' ",
			"Returns an empty string if the entity does not have a primary key"
		},
		example={	
				"$sql.pkColumns($entity)"
			},
		since = "3.4.0"
	)
	public String pkColumns(EntityInContext entity) {
		if ( entity.hasPrimaryKey() ) {
			StringBuilder sb = new StringBuilder();
			for ( AttributeInContext attribute : entity.getKeyAttributes() ) {
				if ( sb.length() > 0 ) {
					sb.append(", ");
				}
//				sb.append( convertToColumnName(attribute.getName()) );
				sb.append( attribute.getSqlColumnName() );
			}
			return sb.toString();
		}
		else {
			return "";
		}
    }

	//-------------------------------------------------------------------------------------
	// Foreign Key
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Returns the name of the given Foreign Key",
			"(converts the name to expected naming style if necessary)",
			"For example converts 'fkFooBar' or 'FK_FOO_BAR' to 'fk_foo_bar'",
			""
		},
		example={	
				"$sql.fkName($fk)"
			},
		since = "3.4.0"
	)
	public String fkName(ForeignKeyInContext fk) {
		return convertToFkName(fk.getName());
	}
		
	@VelocityMethod ( 
		text= { 
			"Returns the name of the table for the given Foreign Key",
			"(converts the name to table naming style if necessary)",
			"For example converts 'SpecialCar' or 'SPECIAL_CAR' to 'special_car'",
			""
		},
		example={	
				"$sql.fkTable($fk)"
			},
		since = "3.4.0"
	)
	public String fkOriginTable(ForeignKeyInContext fk) {
		// return convertToTableName(fk.getTableName());
		return fk.getOriginEntity().getSqlTableName();
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Returns the name of the referenced table for the given Foreign Key",
			"(converts the name to table naming style if necessary)",
			"For example converts 'SpecialCar' or 'SPECIAL_CAR' to 'special_car'",
			""
		},
		example={	
				"$sql.fkReferencedTable($fk)"
			},
		since = "3.4.0"
	)
	public String fkReferencedTable(ForeignKeyInContext fk) {
		//return convertToTableName(fk.getReferencedTableName());
		return fk.getReferencedEntity().getSqlTableName();
	}
		
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Returns a string containing the names of all the columns composing the foreign key",
			"Each column name is converted according to the naming rules for the target database",
			"Examples : 'group_id' or 'car_code, group_id' "
		},
		example={	
				"$sql.fkColumns($fk)"
			},
		since = "3.4.0"
	)
	public String fkColumns(ForeignKeyInContext fk) throws GeneratorException {
		return buildColumns(fk, FK_ORIGIN_SIDE);
    }

	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Returns a string containing the names of all the columns referenced by the foreign key",
			"Each column name is converted according to the naming rules for the target database",
			"For example 'id' or 'code, group' "
		},
		example={	
				"$sql.fkReferencedColumns($fk)"
			},
		since = "3.4.0"
	)
	public String fkReferencedColumns(ForeignKeyInContext fk) throws GeneratorException {
		return buildColumns(fk, FK_REFERENCED_SIDE);
    }

	//-------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------
	private Properties loadStandardConfiguration(String propFileName) {
		Properties properties = new Properties();
		ClassLoader classLoader = this.getClass().getClassLoader();
		try ( InputStream inputStream = classLoader.getResourceAsStream(propFileName)) {
			if ( inputStream == null ) {
				throw new GeneratorSqlException("Unknown database (file '" 
						+ propFileName + "' not found)");
			}
			properties.load(inputStream);
		} catch (IOException e) {
			throw new GeneratorSqlException("Cannot load database config file '" 
					+ propFileName + "' IOException");
		}
		return properties;
	}
	//-------------------------------------------------------------------------------------
	private Properties loadSpecificConfiguration(File file) {
		Properties properties = new Properties();
		try ( InputStream inputStream = new FileInputStream(file) ) {
			properties.load(inputStream);
		} catch (IOException e) {
			throw new GeneratorSqlException("Cannot load database config file '" 
					+ file.getAbsolutePath() + "' IOException");
		}
	    return properties;
	}
	//-------------------------------------------------------------------------------------
	protected String getConfigValue(String key) {
		String val = this.targetDbConfig.getProperty(key);
		if ( val != null ) {
			return val.trim();
		}
		else {
			throw new GeneratorSqlException("getConfigValue", 
					"Cannot get config value for key '"+ key + "'");
		}
	}
	//-------------------------------------------------------------------------------------
	/**
	 * Name conversion 
	 * @param originalName
	 * @param styleName style to be used ( snake_case, ANACONDA_CASE, camelCase, PascalCase)
	 * @return
	 */
	protected String convertName(String originalName, String styleName) {
		switch (styleName) {
		case "snake_case" :
			return converter.toSnakeCase(originalName);
		case "ANACONDA_CASE" :
			return converter.toAnacondaCase(originalName);
		case "camelCase" :
			return converter.toCamelCase(originalName);
		case "PascalCase" :
			return converter.toPascalCase(originalName);
		default :
			throw new GeneratorSqlException("convertName", 
					"Unknown style '" + styleName + "'");
		}
    }
	
	//-------------------------------------------------------------------------------------
	
	protected String getConfigType(String originalType, boolean autoIncremented) {
		// Map entry examples :
		// type.int           = integer
		// type.int.autoincr  = serial
		String key = "type." + originalType.trim();
		if ( autoIncremented ) {
			String keyAutoIncr = key + ".autoincr" ;
			String type = this.targetDbConfig.getProperty(keyAutoIncr);
			if ( type != null ) {
				// specific auto-incremented type found
				return type ; 
			}
			else {
				// specific auto-incremented type NOT found
				// get standard type
				return getConfigValue(key); 
			}
		}
		else {
			return getConfigValue(key);
		}
	}
	
	//-------------------------------------------------------------------------------------
	private String getSizeToApply(Number size) {
		if ( size != null ) {
			if ( size.intValue() < 0 ) {
				// Not supposed to happen (checked before)
				throw new GeneratorSqlException("invalid size " + size);
			}
			else if ( size.intValue() == 0 ) {
				// "0" or "0.2" : considered as no size
				return null ; 
			}
			else {
				// >= 1
				return size.toString(); // OK : keep it
			}
		}
		return null ;
	}

	/**
	 * Replaces size parameter (%s) (%S) (%p) (%P) for SQL types like 'varchar(..)' or 'number(..)'
	 * @param sqlType
	 * @param varMandatory variable string to replace : "%S" or "%P"
	 * @param varOptional  variable string to replace : "%s" or "%p"
	 * @param size
	 * @return
	 */
	protected String replaceVarSize(String sqlType, String varMandatory, String varOptional, Number size) {
		String sizeOK = getSizeToApply(size);
		if ( sqlType.contains(varMandatory) ) {
			// SIZE IS MANDATORY 
			if ( sizeOK != null ) {
				return StrUtil.replaceVar(sqlType, varMandatory, sizeOK);
			}
			else {
				throw new GeneratorSqlException("SQL type '" + sqlType + "' : size is mandatory");
			}
		}
		else if ( sqlType.contains(varOptional) ) {
			// SIZE IS OPTIONAL  
			if ( sizeOK != null ) {
				return StrUtil.replaceVar(sqlType, varOptional, sizeOK);
			}
			else {
				return StrUtil.replaceVar(sqlType, "("+varOptional+")", ""); // remove "(%s) or (%p)"
			}
		}
		else {
			throw new GeneratorSqlException("SQL type '" + sqlType + "' : internal error (size var)");
		}
	}

	protected String replaceVar(String sqlType, BigDecimal size) {
		if ( sqlType.contains("%S") || sqlType.contains("%s") ) {
			// Integer size,  eg VARCHAR(8)
			BigInteger sizeInt = size != null ? size.toBigInteger() : null ;
			return replaceVarSize(sqlType, "%S", "%s", sizeInt);
		}
		else if ( sqlType.contains("%P") || sqlType.contains("%p")) {
			// Precision [and scale], eg NUMBER(8), NUMBER(8.2), numeric(6.2)
			return replaceVarSize(sqlType, "%P", "%p", size);
		}
		else {
			return sqlType;
		}
	}
	
//	private String buildColumns(ForeignKeyInContext fk, int colType) {
//		StringBuilder sb = new StringBuilder();
//		for ( ForeignKeyColumnInContext fkCol : fk.getColumns() ) {
//			if ( sb.length() > 0 ) {
//				sb.append(", ");
//			}
//			if ( colType == FK_COL ) {
//				sb.append( convertToColumnName(fkCol.getColumnName()) );
//			}
//			else {
//				sb.append( convertToColumnName(fkCol.getReferencedColumnName()) );
//			}
//		}
//		return sb.toString();
//    }
	private String buildColumns(ForeignKeyInContext fk, int colType) throws GeneratorException {
		StringBuilder sb = new StringBuilder();
		for ( ForeignKeyAttributeInContext fkAttribute : fk.getAttributes() ) {
			if ( sb.length() > 0 ) {
				sb.append(", ");
			}
			if ( colType == FK_ORIGIN_SIDE ) {
				// sb.append( convertToColumnName(fkAttribute.getOriginAttribute() ) );
				sb.append(fkAttribute.getOriginAttribute().getSqlColumnName() ); 
			}
			else {
				// sb.append( convertToColumnName(fkAttribute.getReferencedColumnName()) );
				sb.append(fkAttribute.getReferencedAttribute().getSqlColumnName() ); 
			}
		}
		return sb.toString();
    }

	//-------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------
}
