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
import java.util.Properties;

import org.telosys.tools.commons.NamingStyleConverter;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.exceptions.GeneratorSqlException;
import org.telosys.tools.generator.context.names.ContextName;

//-------------------------------------------------------------------------------------
@VelocityObject(
		contextName=ContextName.SQL,
		text = { 
				"Object for schema creation in SQL language (for a relational database)",
				"It manages :",
				" - names conversion (table name, column name, pk/fk name)",
				" - field type conversion (neutral type to SQL column type)",
				"It is designed to facilitate DDL commands generation",
				"( CREATE TABLE, ADD CONSTRAINT FOREIGN KEY, etc) ",
				""
		},
		since = "3.4.0"
 )
//-------------------------------------------------------------------------------------
public class SqlInContext {
	// TODO : see also 	:
	// AttributeInContext : getSqlType(),  getDatabaseName(), etc
	// and usages, eg :
	//    JdbcInContext -> JdbcRequets : uses attribute.getDatabaseName()

	private static final int FK_COL     = 1 ;
	private static final int FK_REF_COL = 2 ;
	
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
		String targetDbConfigFile = "target-db/" + targetDbName.trim().toLowerCase() + ".properties" ;
		init(targetDbName, targetDbConfigFile, loadStandardConfiguration(targetDbConfigFile));
	}
	
	/**
	 * Constructor for default database configuration file
	 * @param targetDbName
	 * @param targetDbConfigFile
	 */
	public SqlInContext(String targetDbName, String targetDbConfigFile) {
		super();
		if ( StrUtil.nullOrVoid(targetDbName) ) {
			throw new GeneratorSqlException("Target database name undefined, cannot create $sql");
		}
		if ( StrUtil.nullOrVoid(targetDbConfigFile) ) {
			throw new GeneratorSqlException("Target database config file undefined, cannot create $sql");
		}
		init(targetDbName, targetDbConfigFile, loadSpecificConfiguration(targetDbConfigFile));
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
			"$sql.convertToColumnType('string', false, 20, 0)"
			},
		parameters = { 
			"neutralType : neutral type to be converted " ,
			"autoInc : auto-incremented attribute (true/false)",
			"size : maximum size (for a variable length string)",
			"precision : precision and scale (x.y) for a numeric attribute"
		},
		since = "3.4.0"
	)
	public String convertToColumnType(String neutralType, boolean autoInc, 
			Integer size, BigDecimal precision) {
		// get SQL type from database config
		String sqlType = getConfigType(neutralType, autoInc);
		
		// replace size or precision if any 
		if ( sqlType.contains("%") ) {
			return replaceVar(sqlType, size, precision);
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
			"Converts the name of the given entity to table naming style",
			"For example converts 'EmployeeJobs' to 'employee_jobs'",
			""
		},
		example={	
				"$sql.tableName($entity)"
			},
		since = "3.4.0"
	)
	public String tableName(EntityInContext entity) {
		return convertToTableName(entity.getName());
    }
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Returns the default Primary Key name for the given entity",
			"The default Primary Key name is 'PK_' followed by the entity name",
			"It is then converted to the expected naming style",
			"Examples : 'pk_employee', 'PK_EMPLOYEE' ",
			""
		},
		example={	
				"$sql.pkName($entity)"
			},
		since = "3.4.0"
	)
	public String pkName(EntityInContext entity) {
		String pkName = "PK_" + entity.getName();
		return convertToPkName(pkName);
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
		if ( StrUtil.nullOrVoid(databaseName) ) {
			// no database name for this attribute
			// => use attribute name as default database name ( not supposed to happen )
			databaseName = attribute.getName();
		}
		// convert attribute database name to target database naming convention 
		return convertToColumnName(databaseName);
    }
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Converts the attribute neutral type to the corresponding SQL type ",
			"For example converts 'string' to 'varchar(x)' ",
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
		if ( StrUtil.nullOrVoid(databaseType) ) {
			// not defined in the model : try to convert neutral type
			return convertToColumnType(attribute.getNeutralType(), attribute.isAutoIncremented(),
								getMaximumSize(attribute), getPrecision(attribute));
		}
		else {
			// defined in the model => use it as is
			return databaseType ;
		}
    }
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Returns the column constraintes for the given attribute",
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
		if ( attribute.isDatabaseNotNull() || attribute.isNotNull() ) {
			sb.append("NOT NULL");
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
				sb.append( convertToColumnName(attribute.getName()) );
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
	public String fkTable(ForeignKeyInContext fk) {
		return convertToTableName(fk.getTableName());
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
		return convertToTableName(fk.getReferencedTableName());
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
	public String fkColumns(ForeignKeyInContext fk) {
		return buildColumns(fk, FK_COL);
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
	public String fkReferencedColumns(ForeignKeyInContext fk) {
		return buildColumns(fk, FK_REF_COL);
    }

	//-------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------
	private Properties loadStandardConfiguration(String propFileName) {
		Properties properties = new Properties();
		ClassLoader classLoader = this.getClass().getClassLoader();
		try ( InputStream inputStream = classLoader.getResourceAsStream(propFileName)) {
			if ( inputStream == null ) {
				throw new GeneratorSqlException("Database config file '" 
						+ propFileName + "' not found in the classpath");
			}
			properties.load(inputStream);
		} catch (IOException e) {
			throw new GeneratorSqlException("Cannot load database config file '" 
					+ propFileName + "' IOException");
		}
		return properties;
	}
	//-------------------------------------------------------------------------------------
	private Properties loadSpecificConfiguration(String propFileName) {
		Properties properties = new Properties();
		try ( InputStream inputStream = new FileInputStream(propFileName) ) {
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
	private String getConfigValue(String key) {
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
	private BigDecimal toBigDecimal(String s) {
		if ( s != null ) {
			String v = s.trim();
			try {
				return new BigDecimal(v);
			} catch (NumberFormatException e) {
				throw new GeneratorSqlException("invalid attribute size/length '" + v + "' NumberFormatException");
			}
		}
		else {
			throw new GeneratorSqlException("attribute size/length is null");
		}
	}
	private Integer getMaximumSize(AttributeInContext attribute) {
		BigDecimal size = null ;
		// use @DbSize first : eg @DbSize(45)
		if ( ! StrUtil.nullOrVoid(attribute.getDatabaseSize()) ) {
			size = toBigDecimal( attribute.getDatabaseSize() );
		} 
		// use @SizeMax if any : eg @SizeMax(45)
		else if ( ! StrUtil.nullOrVoid(attribute.getMaxLength()) ) {
			size = toBigDecimal( attribute.getMaxLength() );
		}
		// @DbSize can contains something like "8.2" => keep int part only 
		if ( size != null ) {
			return Integer.valueOf(size.intValue());
		}
		return null ;
	}
	private BigDecimal getPrecision(AttributeInContext attribute) {
		// use @DbSize first : eg @DbSize(10.2) or @DbSize(8)
		if ( ! StrUtil.nullOrVoid(attribute.getDatabaseSize()) ) {
			return toBigDecimal( attribute.getDatabaseSize() );
		} 
		// TODO : add @Precision(xx) annotation ????
//		else if ( ! StrUtil.nullOrVoid(attribute.getPrecision()) ) {
//			return toBigDecimal( attribute.getPrecision() );
//		}
		return null ;
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

	private void checkSizeValue(String sqlType, Integer size) {
		if ( size != null && size.intValue() <= 0 ) {
			throw new GeneratorSqlException("SQL type '" + sqlType + "' : invalid size " + size);
		}
	}
	private void checkPrecisionValue(String sqlType, BigDecimal precision) {
		if ( precision != null && precision.intValue() <= 0 ) {
			throw new GeneratorSqlException("SQL type '" + sqlType + "' : invalid precision " + precision);
		}
	}
	
	/**
	 * Replaces size parameter (%s) or (%S) for types like 'varchar(..)'
	 * @param sqlType
	 * @param size 
	 * @return
	 */
	protected String replaceVarSize(String sqlType, Integer size) {
		if ( sqlType.contains("%S") ) {
			// SIZE IS MANDATORY 
			checkSizeValue(sqlType, size);
			if ( size != null ) {
				return StrUtil.replaceVar(sqlType, "%S", size.toString());
			}
			else {
				throw new GeneratorSqlException("SQL type '" + sqlType + "' : size is mandatory");
			}
		}
		else if ( sqlType.contains("%s") ) {
			checkSizeValue(sqlType, size);
			// SIZE IS OPTIONAL  
			if ( size != null ) {
				return StrUtil.replaceVar(sqlType, "%s", size.toString());
			}
			else {
				return StrUtil.replaceVar(sqlType, "(%s)", ""); // remove
			}
		}
		else {
			throw new GeneratorSqlException("SQL type '" + sqlType + "' : internal error (size var)");
		}
	}
	
	/**
	 * Replaces precision (and scale) parameter (%p) or (%P) <br>
	 * for types like NUMBER(8), NUMBER(8.2), numeric(6.2)
	 * @param sqlType
	 * @param precision
	 * @return
	 */
	protected String replaceVarPrecision(String sqlType, BigDecimal precision) {
		if ( sqlType.contains("%P") ) {
			// PRECISION IS MANDATORY 
			checkPrecisionValue(sqlType, precision);
			if ( precision != null ) {
				return StrUtil.replaceVar(sqlType, "%P", precision.toString());
			}
			else {
				throw new GeneratorSqlException("SQL type '" + sqlType + "' error : invalid precision " + precision);
			}
		}
		else if ( sqlType.contains("%p") ) {
			// PRECISION IS OPTIONAL 
			checkPrecisionValue(sqlType, precision);
			if ( precision != null ) {
				return StrUtil.replaceVar(sqlType, "%p", precision.toString());
			}
			else {
				return StrUtil.replaceVar(sqlType, "(%p)", ""); // remove
			}
		}
		else {
			throw new GeneratorSqlException("SQL type '" + sqlType + "' : internal error (precision var)");
		}
	}
	protected String replaceVar(String sqlType, Integer size, BigDecimal precision) {
		if ( sqlType.contains("%S") || sqlType.contains("%s") ) {
			// Size,  eg VARCHAR(8)
			return replaceVarSize(sqlType, size);
		}
		else if ( sqlType.contains("%P") || sqlType.contains("%p")) {
			// Precision [and scale], eg NUMBER(8), NUMBER(8.2), numeric(6.2)
			return replaceVarPrecision(sqlType, precision);
		}
		else {
			return sqlType;
		}
	}

	private String buildColumns(ForeignKeyInContext fk, int colType) {
		StringBuilder sb = new StringBuilder();
		for ( ForeignKeyColumnInContext fkCol : fk.getColumns() ) {
			if ( sb.length() > 0 ) {
				sb.append(", ");
			}
			if ( colType == FK_COL ) {
				sb.append( convertToColumnName(fkCol.getColumnName()) );
			}
			else {
				sb.append( convertToColumnName(fkCol.getReferencedColumnName()) );
			}
		}
		return sb.toString();
    }

	//-------------------------------------------------------------------------------------
}
