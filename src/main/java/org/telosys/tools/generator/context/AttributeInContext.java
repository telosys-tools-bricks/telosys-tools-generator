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

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generator.GeneratorUtil;
import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityNoDoc;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.doc.VelocityReturnType;
import org.telosys.tools.generator.context.exceptions.GeneratorSqlException;
import org.telosys.tools.generator.context.names.ContextName;
import org.telosys.tools.generator.languages.literals.LiteralValuesProvider;
import org.telosys.tools.generator.languages.types.AttributeTypeInfo;
import org.telosys.tools.generator.languages.types.AttributeTypeInfoImpl;
import org.telosys.tools.generator.languages.types.LanguageType;
import org.telosys.tools.generator.languages.types.TypeConverter;
import org.telosys.tools.generic.model.Attribute;
import org.telosys.tools.generic.model.ForeignKeyPart;
import org.telosys.tools.generic.model.TagContainer;
import org.telosys.tools.generic.model.enums.BooleanValue;
import org.telosys.tools.generic.model.enums.GeneratedValueStrategy;
import org.telosys.tools.generic.model.types.NeutralType;

/**
 * "$attribute" object usable in templates 
 *  
 * @author Laurent GUERIN
 */
//-------------------------------------------------------------------------------------
@VelocityObject(
		contextName = ContextName.ATTRIBUTE ,
		otherContextNames= { ContextName.ATTRIB, ContextName.ATTR },	// FIELD -> ATTR v 4.0.1	
		text = {
				"This object provides all information about an entity attribute",
				"Each attribute is obtained from its entity class ",
				""
		},
		since = "2.0.0",
		example= {
				"",
				"#foreach( $attribute in $entity.attributes )",
				"    $attribute.name : $attribute.type",
				"#end"
		}
 )
//-------------------------------------------------------------------------------------
public class AttributeInContext {
	
    private static final String VOID_STRING  = "" ;
    
	private final EnvInContext     envInContext ;

    private final EntityInContext  entityInContext ; // The entity 
    
	private final ModelInContext   modelInContext ;  

	private boolean       mustUseFullType = false ; 

//	private final boolean selected ; // removed in v 4.1.0
    
	//--- Basic minimal attribute info ---------------------------------
	private final String  name ;  // attribute name 
	private final String  neutralType ;  // attribute neutral type
	private final AttributeTypeInfo attributeTypeInfo ; 
	private final String  initialValue ; 
	private final String  defaultValue ; 

	//--- Further info for ALL ---------------------------------------
	private final boolean isNotNull ;
	private final String  label ;
	private final String  inputType ;
    private final String  size   ;  // Size with precision and scale if necessary (eg "6" or "6,2")

    //--- Further info for STRING ------------------------------------
    private final boolean isLongText ;
    private final boolean isNotEmpty ;
    private final boolean isNotBlank ;
    private final String  minLength ;
    private final String  maxLength ;
    private final String  pattern ;
    
    //--- Further info for NUMBER ------------------------------------
    private final String  minValue ;
    private final String  maxValue ;

    //--- Further info for DATE and TIME ---------------------------------
    private final boolean  isDateInThePast ;
    private final boolean  isDateInTheFuture ;
    private final String   dateBeforeValue  ;
    private final String   dateAfterValue   ;

	//--- Database info -------------------------------------------------
    private final boolean isKeyElement      ;  // True if primary key
    
    private final String  databaseName     ;  // Column name in the DB table
    private final String  databaseType      ;  // Column type in the DB table
    //private final String  databaseSize   ;   // removed in v 3.4.0
    private final String  databaseComment ;     // Comment of this column 
    private final String  databaseDefaultValue ;   
//    private final boolean isDatabaseNotNull ;  // True if "not null" in the database (REMOVED in v 4.1.0)
//    private final boolean isAutoIncremented  ;  // True if auto-incremented by the database (REMOVED in v 4.1.0)

// removed in ver 4.1
//    private final int     jdbcTypeCode    ;  // JDBC type code for this column
//    private final String  jdbcTypeName    ;  // JDBC type name 
    
	//--- FOREIGN KEYS  -------------------------------------------------
    private final boolean isForeignKey          ; 
    private final boolean isForeignKeySimple    ; 
    private final boolean isForeignKeyComposite ; 
    private final String  referencedEntityClassName ; // v 3.0.0 (NOT RELIABLE!)
    private final List<ForeignKeyPartInContext> fkParts = new LinkedList<>(); // v 3.3.0

    //--- Further info for BOOLEAN -----------------------------------
    private final String  booleanTrueValue  ; // eg "1", ""Yes"", ""true""
    private final String  booleanFalseValue ; // eg "0", ""No"",  ""false""
    
	//--- JPA KEY Generation infos -------------------------------------------------
//    private final boolean isGeneratedValue ;  // True if GeneratedValue ( annotation "@GeneratedValue" )
	private final GeneratedValueStrategy generatedValueStrategy ; // "AUTO", "IDENTITY", "SEQUENCE", "TABLE" 
	
	// private final String  generatedValueGenerator ; // removed in v 4.1.0 
	
	private final Integer generatedValueAllocationSize ; // v 4.1.0 not dedicated to SEQUENCE
	private final Integer generatedValueInitialValue ; // v 4.1.0
	
//    private final boolean hasSequenceGenerator  ;  // True if SequenceGenerator ( annotation "@SequenceGenerator" )
	// private final String  sequenceGeneratorName     ; // removed in v 4.1.0 
	private final String  generatedValueSequenceName   ;
	// private final int     sequenceGeneratorAllocationSize ;  // removed in v 4.1.0 

//    private final boolean hasTableGenerator;  // True if TableGenerator ( annotation "@TableGenerator" )
	// private final String  tableGeneratorName; // removed in v 4.1.0 
	private final String  generatedValueTablePkValue;

	private final String  tableGeneratorTable;
	private final String  tableGeneratorPkColumnName;
	private final String  tableGeneratorValueColumnName;

	private final boolean isUsedInLinks ; 
	private final boolean isUsedInSelectedLinks ; 
	
	//--- TAGS (added in v 3.3.0)
	private final TagContainer tagContainer ; // All tags defined for the attribute 

    private final BooleanValue  insertable ; // Added in v 3.3.0
    private final BooleanValue  updatable  ; // Added in v 3.3.0
    
    private final boolean isTransient ; // Added in v 3.3.0

    private final EnvInContext env; // Added in v 3.4.0
	
    private final boolean isUnique ;  // v 3.4.0

    //-----------------------------------------------------------------------------------------------
	/**
	 * Constructor to create an ATTRIBUTE in the generator context
	 * @param entity
	 * @param attribute
	 * @param modelInContext
	 * @param env
	 */
	public AttributeInContext(final EntityInContext entity, 
			final Attribute attribute, 
			final ModelInContext modelInContext, 
			final EnvInContext env) 
	{
		this.envInContext = env ; 
		this.modelInContext = modelInContext ; 
		this.entityInContext = entity ;
		this.env = env ; // Added in v 3.4.0
//		this.selected        = attribute.isSelected(); // removed in v 4.1.0
		
		this.name   = attribute.getName(); 		
		this.neutralType     = attribute.getNeutralType() ; 
		this.attributeTypeInfo = new AttributeTypeInfoImpl(attribute) ; 
		this.initialValue    = StrUtil.notNull( attribute.getInitialValue() ); 
		this.defaultValue    = StrUtil.notNull( attribute.getDefaultValue() );
		
		//--- Further info for ALL
        this.isNotNull   = attribute.isNotNull();
        this.label     = StrUtil.notNull( attribute.getLabel() ) ;
        this.inputType = StrUtil.notNull( attribute.getInputType() );
        
		//--- Further info for STRING 
	    this.isLongText  = attribute.isLongText() ;
	    this.isNotEmpty  = attribute.isNotEmpty();
	    this.isNotBlank  = attribute.isNotBlank();
	    this.maxLength = Util.integerToString(attribute.getMaxLength(), VOID_STRING);
	    this.minLength = Util.integerToString(attribute.getMinLength(), VOID_STRING);
	    this.pattern   = StrUtil.notNull( attribute.getPattern() );
	    
		//--- Further info for NUMBER 
		this.minValue = Util.bigDecimalToString(attribute.getMinValue(), VOID_STRING ) ;
	    this.maxValue = Util.bigDecimalToString(attribute.getMaxValue(), VOID_STRING ) ;
	    
		//--- Further info for DATE/TIME 
	    this.isDateInThePast   = attribute.isDatePast();
	    this.isDateInTheFuture = attribute.isDateFuture();
	    this.dateBeforeValue = StrUtil.notNull( attribute.getDateBeforeValue() );
	    this.dateAfterValue  = StrUtil.notNull( attribute.getDateAfterValue() );
        
		//--- Database info
		this.databaseName     = StrUtil.notNull( attribute.getDatabaseName() ) ;
        this.databaseType     = StrUtil.notNull( attribute.getDatabaseType() ) ;
// removed in v 4.1
//        this.jdbcTypeCode     = attribute.getJdbcTypeCode() != null ? attribute.getJdbcTypeCode() : 0 ;
//        this.jdbcTypeName     = StrUtil.notNull( attribute.getJdbcTypeName() );
        this.isKeyElement     = attribute.isKeyElement();

		//--- Foreign Keys / references
        this.isForeignKey          = attribute.isFK() ;
        this.isForeignKeySimple    = attribute.isFKSimple() ;
        this.isForeignKeyComposite = attribute.isFKComposite() ;
        this.referencedEntityClassName = attribute.getReferencedEntityClassName() ;
        // Build "Foreign Key Parts" if any ( v 3.3.0 )
        for ( ForeignKeyPart fkPart : attribute.getFKParts() ) {
        	this.fkParts.add(new ForeignKeyPartInContext(fkPart, modelInContext));
        }

        this.size     = attribute.getSize(); // v 3.4.0

//        this.isAutoIncremented  = attribute.isAutoIncremented(); // removed in v 4.1.0
        this.databaseComment  = StrUtil.notNull( attribute.getDatabaseComment() ) ; 
        this.databaseDefaultValue = StrUtil.notNull( attribute.getDatabaseDefaultValue() ) ; 
        // this.isDatabaseNotNull  = attribute.isDatabaseNotNull(); // removed in v 4.1
        
		//--- Further info for BOOLEAN 
        this.booleanTrueValue   = Util.trim(attribute.getBooleanTrueValue(), VOID_STRING) ; 
        this.booleanFalseValue  = Util.trim(attribute.getBooleanFalseValue(), VOID_STRING) ;
		
        //--- Generated Value  v 3.4.0
//        this.isGeneratedValue = attribute.getGeneratedValueStrategy() != GeneratedValueStrategy.UNDEFINED ;
		this.generatedValueStrategy = attribute.getGeneratedValueStrategy() ;
//		this.generatedValueGenerator = notNull(attribute.getGeneratedValueGeneratorName());// removed in v 4.1
		this.generatedValueAllocationSize = attribute.getGeneratedValueAllocationSize() ; // v 4.1
		this.generatedValueInitialValue   = attribute.getGeneratedValueInitialValue() ; // v 4.1
				
		// Generated Value / SEQUENCE  v 3.4.0
//        this.hasSequenceGenerator = attribute.getGeneratedValueStrategy() == GeneratedValueStrategy.SEQUENCE ;
//		this.sequenceGeneratorName = this.generatedValueGenerator; // removed in v 4.1.0
		this.generatedValueSequenceName = StrUtil.notNull(attribute.getGeneratedValueSequenceName());
//		this.sequenceGeneratorAllocationSize = notNull(attribute.getGeneratedValueAllocationSize()); // removed in v 4.1.0
		
		// Generated Value / TABLE  v 3.4.0
		this.generatedValueTablePkValue = StrUtil.notNull(attribute.getGeneratedValueTablePkColumnValue());

		// this.hasTableGenerator    = attribute.getGeneratedValueStrategy() == GeneratedValueStrategy.TABLE ;
		// this.tableGeneratorName = this.generatedValueGenerator; // removed in v 4.1.0
		
		// TODO 
		this.tableGeneratorTable = ""; // notNull(attribute.getGeneratedValueTableName());
		this.tableGeneratorPkColumnName = ""; //notNull(attribute.getGeneratedValueTablePkColumnName()); 
		this.tableGeneratorValueColumnName = ""; //notNull(attribute.getGeneratedValueTableValueColumnName());
		
		this.isUsedInLinks         = attribute.isUsedInLinks(); 
		this.isUsedInSelectedLinks = attribute.isUsedInSelectedLinks();
		
		this.tagContainer = attribute.getTagContainer(); // v 3.4.0
		
		this.insertable = attribute.getInsertable(); // v 3.3.0
		this.updatable  = attribute.getUpdatable();  // v 3.3.0

		this.isTransient  = attribute.isTransient();  // v 3.3.0
		
		this.isUnique = attribute.isUnique() ;  // v 3.4.0

	}

	protected final LanguageType getLanguageType() {
		TypeConverter typeConverter = envInContext.getTypeConverter();
		LanguageType languageType = typeConverter.getType(this.attributeTypeInfo);
		if ( languageType != null ) {
			return languageType ;
		}
		else {
			throw new IllegalStateException("Cannot get language type for '" + this.neutralType + "'");
		}
	}
	
	//-----------------------------------------------------------------------------------------------
	/* package */ void useFullType()
	{
		mustUseFullType = true ;
	}
	
	@VelocityMethod(
			text={	
				"Returns the name of the attribute "
				}
		)
	public String getName() {
		return name;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
			text={	
				"Returns the entity owning the attribute "
				}
		)
	public EntityInContext getEntity() {
		return entityInContext;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the attribute's name with trailing blanks in order to obtain the expected size "
			},
		parameters = { 
			"n : the expected size" 
			}
	)
	public String formattedName(int iSize) {
        return format(this.getName(), iSize);
    }

	//-------------------------------------------------------------------------------------
	/**
	 * Returns the "neutral type" defined in the model <br>
	 * e.g. : "string", "short", "decimal", "boolean", "date", "time", etc <br>
	 * 
	 * @return
	 */
	@VelocityMethod(
		text={	
			"Returns the 'neutral type', that is to say the type as defined in the model",
			"e.g. : 'string', 'short', 'decimal', 'boolean', 'date', 'time', etc "
			}
	)
	public String getNeutralType() {
		return neutralType ;
	}

	//-------------------------------------------------------------------------------------
	/**
	 * Returns the "target language type" to use for the attribute 
	 * @return
	 */
	@VelocityMethod(
		text={	
			"Returns the target language type for the attribute",
			"Examples for Java : 'int', 'BigDecimal', 'LocalDate' ",
			"Examples for Golang : 'string', 'int32', 'float32'"
			}
	)
	public String getType() {
		LanguageType type = getLanguageType();
		if ( mustUseFullType ) {
			return type.getFullType() ;
		}
		else {
			return type.getSimpleType() ;
		}
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the attribute's type with trailing blanks in order to obtain the expected size"
			},
		parameters = { 
			"n : the expected size " 
			}
	)
	public String formattedType(int iSize) {
        return format(this.getType(), iSize);
    }	
    
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the attribute's wrapper type with trailing blanks in order to obtain the expected size"
			},
		parameters = { 
			"n : the expected size " 
			}
	)
	public String formattedWrapperType(int iSize) {
        return format(this.getWrapperType(), iSize);
    }	
    
	private String format(String s, int iSize) {
        String sTrailingBlanks = "";
        int iDelta = iSize - s.length();
        if (iDelta > 0) { // if trailing blanks needed
            sTrailingBlanks = GeneratorUtil.blanks(iDelta);
        }
        return s + sTrailingBlanks;
    }	

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
				"Returns the full type name",
				"e.g. for a Java object type : java.math.BigDecimal, java.util.Date,  .. ",
				"  or for a Java primitive type : short, int, .. "
			}
	)
	public String getFullType() {
		LanguageType type = getLanguageType();
		return type.getFullType() ;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the simple type name ",
			"e.g. for a Java object type : BigDecimal, Date, Integer, .. ",
			"  or for a Java primitive type : short, int, .. "
			}
	)
	public String getSimpleType() {
		LanguageType type = getLanguageType();
		return type.getSimpleType() ;
	}
	
	//-------------------------------------------------------------------------------------
	/**
	 * Returns the "java wrapper type" ie "Float" for "float" type, "Boolean" for "boolean" type
	 * @return
	 */
	@VelocityMethod(
		text={	
			"Returns the wrapper type corresponding to the attribute's primitive type",
			"Examples : 'Float' for 'float', 'Integer' for 'int', 'Boolean' for 'boolean', ... ",
			"The attribute's type is retuned as is if it's not a primitive type"
			}
	)
	public String getWrapperType() {
		LanguageType type = getLanguageType();
		return type.getWrapperType() ;		
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if there's an initial value for the attribute (not void)"
			}
	)
	public boolean hasInitialValue() {
		return ! StrUtil.nullOrVoid(initialValue);
	}
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the initial value as is"
			}
	)
	public String getInitialValue() {
		return initialValue;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the initialization value for the current language",
			"Usable to initialize the attribute according with the current target language",
			"The 'initial value' is used if defined in the model"
			},
		example= {
			" $attribute.type $attribute.name = $attribute.ini ;"
			},
		since="4.1.0 (experimental)"
	)
	public String getIni() {
		if ( this.hasInitialValue() ) {
			// Use the specific initial value
			String iniVal = this.getInitialValue();
			if ( this.isStringType() ) {
				if ( StrUtil.isQuoted( iniVal ) ) {
					return iniVal ; // already quoted => keep it as is 
				}
				else {
					return StrUtil.quote(iniVal); // add quotes
				}
			}
			else {
				return iniVal ; // supposed to be ok as is : 123, 123.45, true, false, etc 
			}
		}
		else {
			// No specific initial value => use default for target language
			LiteralValuesProvider literalValuesProvider = envInContext.getLiteralValuesProvider();
//			if (this.isNotNull()) {
//				// not null attribute
//				return literalValuesProvider.getDefaultValueNotNull(getLanguageType());
//			} else {
//				// nullable attribute
//				return literalValuesProvider.getLiteralNull();
//			}
			return literalValuesProvider.getInitValue(this, getLanguageType());
		}
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
				"Returns the getter for the attribute",
				"e.g : 'getFoo' for 'foo' (or 'isFoo' for a boolean primitive type)"
					}
	)
	public String getGetter() {
		return Util.buildGetter(name, this.isBooleanType() && this.isPrimitiveType() ); // v 3.3.0
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
				"Returns the getter for the attribute with always a 'get' prefix",
				"even for a boolean"
					}
	)
	public String getGetterWithGetPrefix() {
		return Util.buildGetter(name); 
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
				"Returns the setter for the attribute",
				"e.g : 'setFoo' for 'foo' "
				}
	)
	public String getSetter() {
		return Util.buildSetter(name);
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the value to use for a boolean when is TRUE (eg to be stored in a database) "
			}
	)
	public String getBooleanTrueValue() {
		return booleanTrueValue;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the value to use for a boolean when is FALSE (eg to be stored in a database) "
			}
	)
	public String getBooleanFalseValue() {
		return booleanFalseValue ;
	}
	
	//----------------------------------------------------------------------
	// Database 
	//----------------------------------------------------------------------
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the database name for the attribute (as defined in the model)",
			"or an empty string  if none",
			"Typically the column name for a relational database"
			}
	)
    public String getDatabaseName() {
        return databaseName;
    }
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns TRUE if the attribute has a database name explicitly defined in the model",
			"(database name not null and not void)"
		},
		example= {
			"#if ( $attribute.hasDatabaseName() )",
			"...",
			"#end"
		},
		since="3.4.0"
	)
	public boolean hasDatabaseName() {
		return ! StrUtil.nullOrVoid(databaseName);
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the database native type for the attribute (as defined in the model)",
			"or an empty string  if none",
			"For example : INTEGER, VARCHAR, etc..."
			}
	)
    public String getDatabaseType() {
        return this.databaseType;
    }
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns TRUE if the attribute has a database type explicitly defined in the model",
			"(database type not null and not void)"
		},
		example= {
			"#if ( $attribute.hasDatabaseType() )",
			"...",
			"#end"
		},
		since="3.4.0"
	)
	public boolean hasDatabaseType() {
		return ! StrUtil.nullOrVoid(databaseType);
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the database size for the attribute",
			"(for example : '45' or '10,2' for precision with scale)",
			"or an empty string if none",

			"Try to get the size from the 'database type' first ",
			"if not found return the standard size if any",
			""
//			"",
//			"(!) DEPRECATED : use 'size' instead "
			}
	)
    public String getDatabaseSize() {
		// Try to extract the size from database type ( eg "varchar(20)", "number(10,2)" )
		String databaseSize = extractSizeFromType(this.databaseType);
		if ( ! StrUtil.nullOrVoid(databaseSize) ) {
			return databaseSize;
		}
		else {
			if ( ! StrUtil.nullOrVoid(this.size) ) {
				// Explicitly defined in the model => use it as is
		        return this.size ;
			}
		}
		return "";
    }
	private String extractSizeFromType(String dbType) {
		if ( StrUtil.nullOrVoid(dbType) ) {
			// No type => no size
			return "" ; 
		}
		StringBuilder sb = new StringBuilder();
		boolean in = false ;
		for (char c : dbType.toCharArray()) {
			switch (c) {
			case '(' :
				in = true ;
				break;
			case ')' :
				return sb.toString().trim();
			default :
				if (in) {
					sb.append(c);
				}
				break;
			}
		}
		return "";
	}
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the database comment for the attribute (or a void string if none)"
			},
		since="2.1.1"
	)
    public String getDatabaseComment() {
        return databaseComment;
    }
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns TRUE if the attribute has a database comment defined in the model",
			"(database comment not null and not void)"
		},
		example= {
			"#if ( $attribute.hasDatabaseComment() )",
			"...",
			"#end"
		},
		since="3.3.0"
	)
	public boolean hasDatabaseComment() {
		return ! StrUtil.nullOrVoid(databaseComment);
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the attribute has a database default value",
			"(database default value not null and not void)",			
			"If the attribute is 'auto-incremented' then always returns false"
		},
		example= {
			"#if ( $attribute.hasDatabaseDefaultValue() )",
			"...",
			"#end"
		}
	)
    public boolean hasDatabaseDefaultValue() {
    	//if ( isAutoIncremented ) return false ; // No default value for auto-incremented fields
		// v 4.1.0
    	if ( isGeneratedValue() ) return false ; // No default value for auto-incremented fields
		return ! StrUtil.nullOrVoid(databaseDefaultValue);
    }
    
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns the database default value for the attribute (or a void string if none)"
		}
	)
    public String getDatabaseDefaultValue() {
        return databaseDefaultValue ; 
    }
    
	//----------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns TRUE if the attribute must be NOT NULL",
		"",
		"(!) DEPRECATED : use 'isNotNull()' instead "
		}
	)
    public boolean isDatabaseNotNull() {
        // return isDatabaseNotNull;
        return isNotNull;
    }
    
	//----------------------------------------------------------------------
// removed in v 4.1	
//	@VelocityMethod(
//		text={	
//			"Returns the JDBC type of the attribute (the type code)"
//			}
//		)
//    public int getJdbcTypeCode() {
//        return jdbcTypeCode ;
//    }
//
//	//----------------------------------------------------------------------
//	@VelocityMethod(
//		text={	
//			"Returns the JDBC type name ('CHAR', 'VARCHAR', 'NUMERIC', ... )<br>",
//			"The 'java.sql.Types' constant name for the current JDBC type code"
//			}
//		)
//    public String getJdbcTypeName() {
//        return jdbcTypeName ;
//    }
//
//	//----------------------------------------------------------------------
//    /**
//     * Returns the recommended Java type for the JDBC type 
//     * @return
//     */
//	@VelocityMethod(
//			text={	
//				"Returns the recommended Java type for the JDBC type of the attribute"
//				}
//		)
//    public String getJdbcRecommendedJavaType()
//    {
//    	JdbcTypes types = JdbcTypesManager.getJdbcTypes();
//    	return types.getJavaTypeForCode(jdbcTypeCode, isDatabaseNotNull );
//    }

	//----------------------------------------------------------------------
    /**
     * Returns TRUE if the attribute is a Database Primary Key element
     * @return 
     */
	@VelocityMethod(
	text={	
		"Returns TRUE if the attribute is the Primary Key or a part of the Primary Key in the database"
		}
	)
    public boolean isKeyElement() {
        return isKeyElement;
    }

	//----------------------------------------------------------------------
	@VelocityMethod(
	text={ "Returns TRUE if the attribute is used in at least one Foreign Key",
		"( it can be a 'Simple FK' or a 'Composite FK' or several FK of any type )" },
	since="3.0.0"
	)
    public boolean isFK() { 
        return isForeignKey ;
    }

	//----------------------------------------------------------------------
	@VelocityMethod(
	text={ "Returns TRUE if the attribute is used in at least one 'Simple Foreign Key' ",
		   "( 'Simple FK' means the FK is based only on this attribute ) " },
	since="3.0.0"
	)
    public boolean isFKSimple() { 
        return isForeignKeySimple ;
    }

	//----------------------------------------------------------------------
	@VelocityMethod(
	text={ "Returns TRUE if the attribute is used in at least one 'Composite Foreign Key' ",
		   "( 'Composite FK' means the FK is based on many attributes including this attribute ) " },
	since="3.0.0"
	)
    public boolean isFKComposite() { 
        return isForeignKeyComposite ;
    }

	//----------------------------------------------------------------------
	@VelocityNoDoc  // internal usage	
	public boolean isUsedInLinks() {
		return isUsedInLinks ;
	}

	@VelocityNoDoc  // internal usage	
	public boolean isUsedInSelectedLinks() {
		return isUsedInSelectedLinks ;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
	text= { 
		"Returns all the parts of Foreign Keys in which the attribute is involved. ",
		"Each 'FK part' provides the referenced entity and attribute.",
		"An empty list is returned if the attribute does not participate in any FK."
	},
	example={	
		"#foreach( $fkPart in $attribute.fkParts )",
		"...",
		"#end" 
	},
	since="3.3.0"
	)
	@VelocityReturnType("List of 'fkPart' objects")
	public List<ForeignKeyPartInContext> getFkParts() {
		return fkParts ;
	}
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( text= { 
			"Returns the number of FK parts in which the attribute is involved."
		},
		example="$attribute.fkPartsCount",
		since="4.1.0"
	)
	public int getFkPartsCount() {
		return fkParts.size() ;
	}	
	//-------------------------------------------------------------------------------------
    /**
     * Returns TRUE if the attribute is auto-incremented by the Database engine
     * @return 
     */
	@VelocityMethod(
	text={	
		"Returns TRUE if the attribute is 'auto-incremented' by the database",
		"when a new entity is inserted in the database"
		}
	)
    public boolean isAutoIncremented() {
//        return isAutoIncremented;
        return generatedValueStrategy == GeneratedValueStrategy.IDENTITY ;
    }

	//----------------------------------------------------------------------
    /**
     * Returns TRUE if the attribute has a "Not Null" constraint at the Java level
     * @return 
     */
	@VelocityMethod(
	text={	
		"Returns TRUE if the attribute has a 'Not Null' validation rule "
		}
	)
    public boolean isNotNull() {
        return isNotNull;
    }

	//----------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if there's an 'label' defined for the attribute (not void)"
			},
		since="3.3.0"
	)
	public boolean hasLabel() {
		return ! StrUtil.nullOrVoid(label);
	}
		
	@VelocityMethod(
		text={	
			"Returns the label for the attribute ",
			"(returns a void string if none)"
		}
	)
    public String getLabel() {
        return voidIfNull(label) ;
    }
    
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if there's an 'input type' defined for the attribute (not void)"
			},
		since="3.3.0"
	)
	public boolean hasInputType() {
		return ! StrUtil.nullOrVoid(inputType);
	}
	
	@VelocityMethod(
		text={	
			"Returns the 'input type' defined for the attribute",
			"Typically for HTML 5 : 'number', 'date', ..."
			},
		since="2.0.3"
	)
    public String getInputType() {
        return inputType ;
    }
    
	//-------------------------------------------------------------------------------------
	
    /**
     * Returns the "maximum" length if any, else returns "" 
     * @return
     */
	@VelocityMethod(
		text={	
			"Returns the maximum length for the attribute",
			"(returns a void string if none)"
		}
	)
    public String getMaxLength() {
    	return voidIfNull(maxLength) ;
    }
    /**
     * Returns the "minimum" length if any, else returns "" 
     * @return
     */
	@VelocityMethod(
		text={	
			"Returns the minimum length for the attribute ",
			"(returns a void string if none)"
		}
	)
    public String getMinLength() {
    	return voidIfNull(minLength) ;
    }
    
    //-------------------------------------------------------------------------------------------
    /**
     * Returns the "pattern" (Reg Exp) if any, else returns "" 
     * @return
     */
	@VelocityMethod(
		text={	
			"Returns the Reg Exp pattern defined for the attribute ",
			"(returns a void string if none)"
		}
	)
    public String getPattern() {
    	return voidIfNull(pattern) ;
    }
    
    //-------------------------------------------------------------------------------------------
    /**
     * Returns the "minimum" value if any, else returns "" 
     * @return
     */
	@VelocityMethod(
			text={	
				"Returns the minimum value for the attribute ",
				"(returns a void string if none)"
			}
		)
    public String getMinValue() {
    	return voidIfNull(minValue) ;
    }
    
    //-------------------------------------------------------------------------------------------
    /**
     * Returns the "maximum" value if any, else returns "" 
     * @return
     */
	@VelocityMethod(
			text={	
				"Returns the maximum value for the attribute ",
				"(returns a void string if none)"
				}
		)
    public String getMaxValue() {
    	return voidIfNull(maxValue) ;
    }
    
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the attribute must be validated as a date in the past"
			}
	)
	public boolean hasDatePastValidation() {
		return isDateInThePast;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the attribute must be validated as a date in the future"
			}
	)
	public boolean hasDateFutureValidation() {
		return isDateInTheFuture;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the attribute must be validated as a date BEFORE a given date value"
			}
	)
	public boolean hasDateBeforeValidation() {
		return ! StrUtil.nullOrVoid(dateBeforeValue); // v 3.3.0
	}
	
	@VelocityMethod(
		text={	
			"Returns the 'date before' value (for date validation)",
			"(returns a void string if none)"
		}
	)
	public String getDateBeforeValue() {
		return voidIfNull(dateBeforeValue) ;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the attribute must be validated as a date AFTER a given date value"
			}
	)
	public boolean hasDateAfterValidation() {
		return ! StrUtil.nullOrVoid(dateAfterValue); // v 3.3.0
	}
	
	@VelocityMethod(
		text={	
			"Returns the 'date after' value (for date validation)",
			"(returns a void string if none)"
		}
	)
	public String getDateAfterValue() {
		return voidIfNull(dateAfterValue) ;
	}

	//-----------------------------------------------------------------------------
    
    /**
     * Returns true if the attribute is a long text
     * @return 
     */
	@VelocityMethod(
	text={	
		"Returns TRUE if the attribute is a 'Long Text' ",
		"i.e. that cannot be transported in a classical string",
		"Typically a text stored as a CLOB or a BLOB"
		}
	)
    public boolean isLongText() {
        return isLongText;
    }

	@VelocityMethod(
	text={	
		"Returns TRUE if the attribute has a 'Not Empty' validation rule "
		}
	)
    public boolean isNotEmpty() {
        return isNotEmpty;
    }
    
	@VelocityMethod(
	text={	
		"Returns TRUE if the attribute has a 'Not Blank' validation rule "
		}
	)
    public boolean isNotBlank() {
        return isNotBlank;
    }
    
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if there's a default value for the attribute (not void)"
			}
	)
    public boolean hasDefaultValue() {
		return ! StrUtil.nullOrVoid(defaultValue);
    }
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the default value for the attribute",
			"(returns a void string if none)"
			}
	)
    public String getDefaultValue() {
    	return voidIfNull(defaultValue) ;
    }

	public String toString() {
		String s =  initialValue != null ? " = " + initialValue : "" ;
		return this.getType() + " " + name + s ; 
	}

	private String voidIfNull ( String s ) {
		return s != null ? s : "" ;
	}
	
//	//-------------------------------------------------------------------------------------
// removed in v 4.1.0	
//	@VelocityMethod(
//		text={	
//			"Returns TRUE if the attribute is selected (ckeckbox ckecked in the GUI)"
//			}
//	)
//	public boolean isSelected() {
//		return selected;
//	}

	//------------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns TRUE if the attribute's language type is a primitive type",
		"i.e. for Java : int, float, boolean, ..."
		}
	)
	public boolean isPrimitiveType() {
		LanguageType type = getLanguageType(); 
		return type.isPrimitiveType() ;		
	}

	//------------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns TRUE if the attribute's neutral type is'boolean' "
		},
	since="2.0.7"
	)
	public boolean isBooleanType() {
    	return NeutralType.BOOLEAN.equals(this.neutralType ) ; 
	}

	//------------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns TRUE if the attribute's neutral type is 'byte' "
		},
	since="2.0.7"
	)
	public boolean isByteType() {
    	return NeutralType.BYTE.equals(this.neutralType ) ;
	}

	//------------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns TRUE if the attribute's neutral type is 'short' "
		},
	since="2.0.7"
	)
	public boolean isShortType() {
    	return NeutralType.SHORT.equals(this.neutralType ) ; 
	}

	//------------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns TRUE if the attribute's neutral type is 'int' "
		},
	since="2.0.7"
	)
	public boolean isIntegerType() {
    	return NeutralType.INTEGER.equals(this.neutralType ) ; 
	}

	//------------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns TRUE if the attribute's neutral type is 'long' "
		},
	since="2.0.7"
	)
	public boolean isLongType() {
    	return NeutralType.LONG.equals(this.neutralType ) ; 
	}

	//------------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
			"Returns TRUE if the attribute's neutral type is 'float' "
		},
	since="2.0.7"
	)
	public boolean isFloatType() {
    	return NeutralType.FLOAT.equals(this.neutralType ) ;
	}
	
	//------------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
			"Returns TRUE if the attribute's neutral type is 'double' "
		},
	since="2.0.7"
	)
	public boolean isDoubleType() {
    	return NeutralType.DOUBLE.equals(this.neutralType ) ;
	}

	//------------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
			"Returns TRUE if the attribute's neutral type is 'decimal' "
		},
	since="3.0.0"
	)
	public boolean isDecimalType() {
    	return NeutralType.DECIMAL.equals(this.neutralType ) ;
	}

	//------------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns TRUE if the attribute's neutral type is a number type",
		"( byte, short, int, long, decimal, float, double )"
		},
	since="2.0.7"
	)
	public boolean isNumberType() {
    	return  isByteType() || isShortType() || isIntegerType() || isLongType() 
    			|| isFloatType() || isDoubleType() || isDecimalType() ;
	}

	//------------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
			"Returns TRUE if the attribute's neutral type is 'string' "
		},
	since="2.0.7"
	)
	public boolean isStringType() {
    	return NeutralType.STRING.equals(this.neutralType ) ;
	}

	//------------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns TRUE if the attribute's neutral type is 'date' "
		},
	since="3.0.0"
	)
	public boolean isDateType() {
		return NeutralType.DATE.equals(this.neutralType ) ; 
	}
	//------------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns TRUE if the attribute's neutral type is 'time' "
		},
	since="3.0.0"
	)
	public boolean isTimeType() {
		return NeutralType.TIME.equals(this.neutralType ) ; 
	}
	//------------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
			"Returns TRUE if the attribute's neutral type is 'timestamp' "
		},
	since="3.0.0"
	)
	public boolean isTimestampType() {
		return NeutralType.TIMESTAMP.equals(this.neutralType ) ; 
	}
	
	//------------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns TRUE if the attribute's neutral type is a temporal type",
		"( date, time, timestamp )"
		},
	since="2.0.7"
	)
	public boolean isTemporalType() {
		/***
    	if ( isDateType() || isTimeType() || isTimestampType() ) return true ;
    	return false ;
    	***/
		return isDateType() || isTimeType() || isTimestampType() ;
	}

	//------------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
			"Returns TRUE if the attribute's neutral type is 'binary' "
		},
	since="3.0.0"
	)
	public boolean isBinaryType() {
		return NeutralType.BINARY.equals(this.neutralType )  ; 
	}

	//-----------------------------------------------------------------------------------------
	// GENERATED VALUE  (for ORM like JPA or Doctrine )
	//-----------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns TRUE if the attribute's value is generated when a new entity is inserted in the database",
		"It can be generated by the database ('auto-incremented') ",
		"or generated by the persistence layer (typically by JPA)"
		}
	)
	public boolean isGeneratedValue() {
		// return isGeneratedValue;
		return this.generatedValueStrategy != GeneratedValueStrategy.UNDEFINED ;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns TRUE if the attribute has a generated value strategy equal to the given name",
		"(strategy name comparison is not case sensitive)"
		},
	parameters = { 
			"strategyName : can be 'AUTO', 'IDENTITY', 'SEQUENCE', 'TABLE' " 
		},
	example= {
			"#if ( $attribute.hasGeneratedValueStrategy('SEQUENCE') ) "
		},	
	since="4.1.0"
	)
	public boolean hasGeneratedValueStrategy(String strategyName) {
		if ( this.generatedValueStrategy != null && strategyName != null ) {
			return strategyName.equalsIgnoreCase(this.generatedValueStrategy.name() );
		}
		return false;
	}
	//-------------------------------------------------------------------------------------
	/**
	 * Returns the generated value strategy : 'AUTO', 'IDENTITY', 'SEQUENCE', 'TABLE' or ''
	 * @return
	 */
	@VelocityMethod(
		text={	
			"Returns the generated value strategy if any.",
			"The returned strategy can be 'AUTO', 'IDENTITY', 'SEQUENCE', 'TABLE' (or '' if none)",
			"Useful for ORM like JPA or Doctrine "			
			}
	)
	public String getGeneratedValueStrategy() {
		return generatedValueStrategy.getText() ;
	}

	//-------------------------------------------------------------------------------------
	/**
	 * Returns the generated value allocation size (usable in JPA "@SequenceGenerator" or "@TableGenerator")
	 * @return
	 */
	@VelocityMethod(
		text={	
			"Returns the generated value allocation size usable in ORM like JPA or Doctrine (or 0 if not defined)",
			"Can be used for JPA '@SequenceGenerator allocationSize' or '@TableGenerator allocationSize' ",
			},
		example= {
			"#if ( $attribute.hasGeneratedValueAllocationSize() )",
			"Generated value allocation size is $attribute.generatedValueAllocationSize",
			"#end"
			},
		since="4.1.0"
	)
	public int getGeneratedValueAllocationSize() {
		return generatedValueAllocationSize != null ? generatedValueAllocationSize.intValue() : 0 ;
	}

	@VelocityMethod(
		text={	
			"Returns true if the attribute has a generated value allocation size"
			},
		example= {
			"#if ( $attribute.hasGeneratedValueAllocationSize() )"
			},
		since="4.1.0"
	)
	public boolean hasGeneratedValueAllocationSize() {
		return generatedValueAllocationSize != null ;
	}

	//-------------------------------------------------------------------------------------
	/**
	 * Returns the generated value initial value (usable in JPA "@SequenceGenerator" or "@TableGenerator")
	 * @return
	 */
	@VelocityMethod(
		text={	
			"Returns the generated value initial value usable in ORM like JPA or Doctrine (or 0 if not defined)",
			"Typically for JPA '@SequenceGenerator initialValue' or '@TableGenerator initialValue' "
			},
		example= {
			"#if ( $attribute.hasGeneratedValueInitialValue() )",
			"Generated value initial value is $attribute.generatedValueInitialValue",
			"#end"
			},
		since="4.1.0"
	)
	public int getGeneratedValueInitialValue() {
		return generatedValueInitialValue != null ? generatedValueInitialValue.intValue() : 0 ;
	}

	@VelocityMethod(
		text={	
			"Returns true if the attribute has an initial value for the generated value"
			},
		example= {
			"#if ( $attribute.hasGeneratedValueInitialValue() )"
			},
		since="4.1.0"
	)
	public boolean hasGeneratedValueInitialValue() {
		return generatedValueInitialValue != null ;
	}


	//-------------------------------------------------------------------------------------
// removed in v 4.1
//	/**
//	 * Returns the GeneratedValue generator : the name of the primary key generator to use <br>
//	 * The generator name referenced a "SequenceGenerator" or a "TableGenerator"
//	 * @return
//	 */
//	@VelocityMethod(
//		text={	
//			"Returns the generator for a 'generated value' ",
//			"Typically for JPA : 'SequenceGenerator' or 'TableGenerator' "
//			}
//	)
//	public String getGeneratedValueGenerator() {
//		return generatedValueGenerator;
//	}

//	//-----------------------------------------------------------------------------------------
//	// JPA "@SequenceGenerator"
//	//-----------------------------------------------------------------------------------------
//	/**
//	 * Returns true if this attribute is a "GeneratedValue" using a "SequenceGenerator"
//	 * @return
//	 */
//	@VelocityMethod(
//		text={	
//			"Returns TRUE if the attribute is a 'generated value' using a 'sequence generator' ",
//			"Typically for JPA '@SequenceGenerator'  "
//			}
//	)
//	public boolean hasSequenceGenerator() {
//		return hasSequenceGenerator;
//	}

	//-----------------------------------------------------------------------------------------
// removed in v 4.1
//	/**
//	 * Returns the "@SequenceGenerator" name
//	 * @return
//	 */
//	@VelocityMethod(
//		text={	
//			"Returns the name of the 'sequence generator' ",
//			"Typically for JPA '@SequenceGenerator/name'  "
//			}
//	)
//	public String getSequenceGeneratorName() {
//		return sequenceGeneratorName;
//	}

	//-----------------------------------------------------------------------------------------
	/**
	 * Returns the generated value sequence name
	 * @return
	 */
	@VelocityMethod(
		text={	
			"Returns the 'sequence name' to use for a generated value (or a void string if none)",
			"Typically for JPA '@SequenceGenerator/sequenceName'  "
			},
		example= {
			"#if ( $attribute.hasGeneratedValueSequenceName() )",
			"Generated value sequence name is $attribute.generatedValueSequenceName",
			"#end"
		},
		since="4.1.0"
	)
	public String getGeneratedValueSequenceName() {
		return generatedValueSequenceName;
	}
	
	@VelocityMethod(
		text={	
			"Returns true if the attribute has a sequence name for the generated value"
			},
		example= {
			"#if ( $attribute.hasGeneratedValueSequenceName() )"
			},
		since="4.1.0"
	)
	public boolean hasGeneratedValueSequenceName() {
		return ! StrUtil.nullOrVoid(generatedValueSequenceName);
	}


	//-----------------------------------------------------------------------------------------
//	/**
//	 * Returns the "@SequenceGenerator" sequence allocation size
//	 * @return
//	 */
//	@VelocityMethod(
//		text={	
//			"Returns the 'sequence allocation size' to be used in the 'sequence generator' definition",
//			"Typically for JPA '@SequenceGenerator/allocationSize'  "
//			}
//	)
//	public int getSequenceGeneratorAllocationSize() {
//		return sequenceGeneratorAllocationSize;
//	}

//	//-----------------------------------------------------------------------------------------
//	// JPA "@TableGenerator"
//	//-----------------------------------------------------------------------------------------
//	@VelocityMethod(
//		text={	
//			"Returns TRUE if the attribute is a 'generated value' using a 'table generator' ",
//			"Typically for JPA '@TableGenerator'  "
//			}
//	)
//	public boolean hasTableGenerator() {
//		return hasTableGenerator;
//	}

	//-----------------------------------------------------------------------------------------
//	@VelocityMethod(
//		text={	
//			"Returns the name of the 'table generator' ",
//			"Typically for JPA '@TableGenerator/name'  "
//			}
//	)
//	public String getTableGeneratorName() {
//		return tableGeneratorName;
//	}

//	//-----------------------------------------------------------------------------------------
//	@VelocityMethod(
//		text={	
//			"Returns the name of the table used in the 'table generator' ",
//			"Typically for JPA '@TableGenerator/table'  "
//			}
//	)
//	public String getTableGeneratorTable() {
//		return tableGeneratorTable;
//	}
//
//	//-----------------------------------------------------------------------------------------
//	@VelocityMethod(
//		text={	
//			"Returns the name of the Primary Key column used in the 'table generator' ",
//			"Typically for JPA '@TableGenerator/pkColumnName'  "
//			}
//	)
//	public String getTableGeneratorPkColumnName() {
//		return tableGeneratorPkColumnName;
//	}
//
//	//-----------------------------------------------------------------------------------------
//	@VelocityMethod(
//		text={	
//			"Returns the name of the column that stores the last value generated by the 'table generator' ",
//			"Typically for JPA '@TableGenerator/valueColumnName'  "
//			}
//	)
//	public String getTableGeneratorValueColumnName() {
//		return tableGeneratorValueColumnName;
//	}
//
	//-----------------------------------------------------------------------------------------
	@VelocityMethod(
		text={
			"Returns the primary key (string value) that identifies the generated value in the table ",
			"useful for ORM like JPA or Doctrine ",
			"Returns a void string if none"
			},
		example= {
				"#if ( $attribute.hasGeneratedValueTablePkValue() )",
				"Generated value : PK value in table is $attribute.generatedValueTablePkValue",
				"#end"
			},
		since="4.1.0"	
	)
	public String getGeneratedValueTablePkValue() {
		return generatedValueTablePkValue;
	}
	
	@VelocityMethod(
		text={	
			"Returns true if the attribute has a primary key that identifies the generated value in the table "
			},
		example= {
			"#if ( $attribute.hasGeneratedValueTablePkValue() )"
			},
		since="4.1.0"
	)
	public boolean hasGeneratedValueTablePkValue() {
		return ! StrUtil.nullOrVoid(generatedValueTablePkValue);
	}
	
	//------------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns the entity referenced by this attribute (if any) ",
		"Can be used only if the attribute 'isFK' ",
		"Throws an exception if no entity is refrerenced by the attribute",
		},
	since="3.0.0"
	)
	public EntityInContext getReferencedEntity() throws GeneratorException {
		// entityName.attributeName
		String attribName = entityInContext.getName() + "." + this.name ;
		if ( ! this.isFK() ) {
			throw new GeneratorException("Attribute '" + attribName + "' is not a Foreign Key");
		}
		if ( StrUtil.nullOrVoid(referencedEntityClassName) ) {
			throw new GeneratorException("No entity referenced by attribute '" + attribName + "'");
		}
		// OK : try to get referenced entity
		EntityInContext entity = this.modelInContext.getEntityByClassName(referencedEntityClassName);
		if ( entity != null ) {
			return entity ;
		}
		else {
			throw new IllegalStateException("getReferencedEntityType() : Cannot get Entity for '" + referencedEntityClassName + "'");				
		}
	}

	//------------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns the name (class name) of the entity referenced by this attribute (if any) ",
		"Can be used only if the attribute 'isFK' ",
		"Throws an exception if no entity is refrerenced by the attribute",
		},
	since="3.0.0"
	)
	public String getReferencedEntityName() throws GeneratorException {
		return getReferencedEntity().getName();
	}

	//------------------------------------------------------------------------------------------
	// TAGS since ver 3.3.0
	//------------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the attribute has a tag with the given name"
		},
		parameters = { 
			"tagName : name of the tag for which to check the existence" 
		},
		example= {
			"$attrib.hasTag('mytag') "
		},
		since="3.3.0"
	)
	public boolean hasTag(String tagName) {
		return tagContainer.containsTag(tagName);
	}

	@VelocityMethod(
		text={	
			"Returns the value held by the given tag name",
			"If the tag is undefined or has no value, the returned value is an empty string"
		},
		parameters = { 
			"tagName : name of the tag for which to get the value" 
		},
		example= {
			"$attrib.tagValue('mytag') "
		},
		since="3.3.0"
	)
	public String tagValue(String tagName) {
		return tagContainer.getTagValue(tagName);
	}

	//------------------------------------------------------------------------------------------
	// TAGS since ver 3.4.0
	//------------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the value held by the given tag name",
			"If the tag is undefined or has no value, the default value is returned"
		},
		parameters = { 
			"tagName : name of the tag for which to get the value" ,
			"defaultValue : default value if no tag or no value"
		},
		example= {
			"$attrib.tagValue('mytag', 'abc') "
		},
		since="3.4.0"
	)
	public String tagValue(String tagName, String defaultValue) {
		return tagContainer.getTagValue(tagName, defaultValue);
	}

	@VelocityMethod(
		text={	
			"Returns the integer value held by the given tag name",
			"If the tag is undefined or has no value, the default value is returned"
		},
		parameters = { 
			"tagName : name of the tag for which to get the value" ,
			"defaultValue : default value if no tag or no value"
		},
		example= {
			"$attrib.tagValueAsInt('mytag', 123) "
		},
		since="3.4.0"
	)
	public int tagValueAsInt(String tagName, int defaultValue) {
		return tagContainer.getTagValueAsInt(tagName, defaultValue);
	}

	@VelocityMethod(
		text={	
			"Returns the boolean value held by the given tag name",
			"If the tag is undefined or has no value, the default value is returned"
		},
		parameters = { 
			"tagName : name of the tag for which to get the value" ,
			"defaultValue : default value if no tag or no value"
		},
		example= {
			"$attrib.tagValueAsBoolean('mytag', false) "
		},
		since="3.4.0"
	)
	public boolean tagValueAsBoolean(String tagName, boolean defaultValue) {
		return tagContainer.getTagValueAsBoolean(tagName, defaultValue);
	}
	
	//-------------------------------------------------------------------------------------
	// "insertable" / "updatable"  ( v 3.3.0 )
	//-------------------------------------------------------------------------------------
	@VelocityNoDoc  // internal usage
	public BooleanValue getInsertableFlag() {
		return this.insertable;
	}
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if 'insertable' flag equals the given value",
			"The flag can be 'undefined' then then neither true nor false"
		},
		parameters = { 
			"value : the boolean value" 
		},
		since="3.3.0"
	)
	public boolean insertableIs(boolean value) {
		if ( value ) {
			return this.insertable == BooleanValue.TRUE ;
		} else {
			return this.insertable == BooleanValue.FALSE ;
		}
	}
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the 'insertable' flag value as string",
			"( 'true' or 'false' or 'undefined' )"
			},
		since="3.3.0"
	)
    public String getInsertable() {
        return this.insertable.getText();
    }
	
	//-------------------------------------------------------------------------------------
	@VelocityNoDoc  // internal usage
	public BooleanValue getUpdatableFlag() {
		return this.updatable;
	}
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns true if 'updatable' flag equals the given value",
			"The flag can be 'undefined' then then neither true nor false"
		},
		parameters = { 
			"value : the boolean value" 
		},
		since="3.3.0"
	)
	public boolean updatableIs(boolean value) {
		if ( value ) {
			return this.updatable == BooleanValue.TRUE ;
		} else {
			return this.updatable == BooleanValue.FALSE ;
		}
	}
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the 'updatable' flag value as string",
			"( 'true' or 'false' or 'undefined' )"
			},
		since="3.3.0"
	)
    public String getUpdatable() {
        return this.updatable.getText();
    }
	
	//------------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the attribute is marked as 'transient' "
		},
		since="3.3.0"
	)
	public boolean isTransient() {
		return this.isTransient  ; // v 3.3.0
	}
	
	//-------------------------------------------------------------------------------------
	//  v 3.4.0 : 
	//  getSize(), getSizeAsDecimal(), 
	//  getSqlColumnName(), getSqlColumnType(), getSqlColumnConstraints 
	//-------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the SQL database column name for the attribute",
			"For example 'city_code' for an attribute named 'cityCode'",
			"The database name defined in the model is used in priority",
			"if no database name is defined then the attribute name is converted to database name",
			"by applying the target database conventions (defined in $env)",
			""
			},
		since="3.4.0"
	)
	public String getSqlColumnName() {
		return this.env.getSql().columnName(this);
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the SQL database column type for the attribute",
			"For example 'varchar(12)' for an attribute with neutral type 'string'" ,
			"The database type defined in the model is used in priority",
			"if no database type is defined then the neutral type is converted to database type",
			"by applying the target database conventions (defined in $env)",
			""
			},
		since="3.4.0"
	)
	public String getSqlColumnType() {
		return this.env.getSql().columnType(this);
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the SQL column constraints for the attribute",
			"For example : NOT NULL DEFAULT 12",
			""
			},
		since="3.4.0"
	)
	public String getSqlColumnConstraints() {
		return this.env.getSql().columnConstraints(this);
	}
	
	//------------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the attribute size if any.",
			"Try to get the 'explicit size' first  ",
			"if no 'explicit size' try to get the 'maximum length'.",
			"The size is returned as a string containing the size as defined in the model",
			"for example : '45' or '10,2' for precision with scale",
			"Returns an empty string if no size"
			},
		since="3.4.0"
	)
	public String getSize() {
		// EVOLUTION : add @Size(xx) annotation ( and @DbSize deprecated )
		if ( ! StrUtil.nullOrVoid(this.size) ) {
			return this.size ;
		}
		// use maximum length if defined 
		else if ( ! StrUtil.nullOrVoid(this.getMaxLength()) ) {
			return this.getMaxLength();
		}
		return "" ; 
	}
	//------------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the attribute size as decimal value (BigDecimal)",
			"(same behavior as 'size' but with conversion to decimal ) ",
			"Returns 0 if no size",
			"If the size has a scale it is converted to 'precision.scale' ",
			"for example : 10.2 for '10,2' "
		},
		since="3.4.0"
	)
	public BigDecimal getSizeAsDecimal() {
		return convertSizeToBigDecimal(this.getSize());
	}
	private BigDecimal convertSizeToBigDecimal(String s) {
		if ( s != null ) {
			String v = s.trim();
			if ( ! v.isEmpty() ) {
				String v2 = v.replace(',', '.');
				try {
					return new BigDecimal(v2);
				} catch (NumberFormatException e) {
					throw new GeneratorSqlException("invalid size '" + v + "' NumberFormatException");
				}
			}
		}
		// No size ( null or empty or blank ) => return 0
		return BigDecimal.valueOf(0); 
	}
	
	//------------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
			"Returns TRUE if the attribute is 'unique' "
		},
	since="3.4.0"
	)
	public boolean isUnique() {
		return this.isUnique  ; 
	}

}