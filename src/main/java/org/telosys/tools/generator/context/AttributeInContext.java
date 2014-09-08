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

import java.util.List;

import org.telosys.tools.commons.DatabaseUtil;
import org.telosys.tools.commons.JavaClassUtil;
import org.telosys.tools.commons.JavaTypeUtil;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.commons.jdbctypes.JdbcTypes;
import org.telosys.tools.commons.jdbctypes.JdbcTypesManager;
import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generator.GeneratorUtil;
import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.names.ContextName;
import org.telosys.tools.repository.model.Column;
import org.telosys.tools.repository.persistence.util.RepositoryConst;


/**
 * Context class for a BEAN ATTRIBUTE ( with or without database mapping )
 *  
 * @author Laurent GUERIN
 */
//-------------------------------------------------------------------------------------
@VelocityObject(
		contextName = ContextName.ATTRIBUTE ,
		otherContextNames= { ContextName.ATTRIB, ContextName.FIELD },		
		text = {
				"This object provides all information about an entity attribute",
				"Each attribute is retrieved from the entity class ",
				""
		},
		since = "",
		example= {
				"",
				"#foreach( $attribute in $entity.attributes )",
				"    $attribute.name : $attribute.type",
				"#end"
		}
		
 )
//-------------------------------------------------------------------------------------
public class AttributeInContext 
{
//	public final static int NO_MAXLENGTH   = -1 ;

    public final static int NO_DATE_TYPE   = 0 ;
    public final static int DATE_ONLY      = 1 ;
    public final static int TIME_ONLY      = 2 ;
    public final static int DATE_AND_TIME  = 3 ;
    
    private final static String TYPE_INT  = "int" ;
    private final static String TYPE_NUM  = "num" ;
    private final static String TYPE_DATE = "date" ;
    private final static String TYPE_TIME = "time" ;
    
	//--- 
    private final EntityInContext _entity ; // The entity 
    
	//--- Basic minimal attribute info -------------------------------------------------
	private final String  _sName ;  // attribute name 
	private final String  _sSimpleType ;  // Short java type without package, without blank, eg : "int", "BigDecimal", "Date"
	private final String  _sFullType ;    // Full java type with package, : "java.math.BigDecimal", "java.util.Date"
	private boolean       _bUseFullType = false ;
	
	private final String  _sInitialValue ; // can be null 
//	private final String  _sGetter ; // Dynamic since v 2.0.7
//	private final String  _sSetter ; // Dynamic since v 2.0.7
	
	private final String  _sDefaultValue ; // can be null 
	
	//--- Database info -------------------------------------------------
    private final boolean _bKeyElement      ;  // True if primary key
    private final boolean _bUsedInForeignKey  ;
    private final boolean _bAutoIncremented  ;  // True if auto-incremented by the database
    private final String  _sDataBaseName     ;  // Column name in the DB table
    private final String  _sDataBaseType      ;  // Column type in the DB table
    private final int     _iJdbcTypeCode    ;     // JDBC type code for this column
    private final String  _sJdbcTypeName    ;  // JDBC type name : added in ver 2.0.7
    private final int     _iDatabaseSize   ;     // Size of this column (if Varchar ) etc..
    private final String  _sDatabaseComment ;     // Comment of this column ( v 2.1.1 - #LCH )
    private final String  _sDatabaseDefaultValue ; // keep null (do not initialize to "" )  
    private final boolean _bDatabaseNotNull ;  // True if "not null" in the database
    
    //--- Further info for ALL ---------------------------------------
    private final boolean _bNotNull   ;
    private final String  _sLabel     ; // v 2.0.3
    private final String  _sInputType ; // v 2.0.3

    //--- Further info for BOOLEAN -----------------------------------
    private final String  _sBooleanTrueValue  ; // eg "1", ""Yes"", ""true""
    private final String  _sBooleanFalseValue ; // eg "0", ""No"",  ""false""
    
    //--- Further info for DATE/TIME ---------------------------------
    private final int     _iDateType       ;  // By default only DATE
    private final boolean _bDatePast        ;
    private final boolean _bDateFuture     ;
    private final boolean _bDateBefore      ;
    private final String  _sDateBeforeValue  ;
    private final boolean _bDateAfter       ;
    private final String  _sDateAfterValue   ;

    //--- Further info for NUMBER ------------------------------------
    private final String  _sMinValue ; 
    private final String  _sMaxValue ; 

    //--- Further info for STRING ------------------------------------
    private final boolean _bLongText   ;  // True if must be stored as a separate tag in the XML flow
    private final boolean _bNotEmpty   ;
    private final boolean _bNotBlank   ;
    private final String  _sMinLength  ; 
    private final String  _sMaxLength  ; 
    private final String  _sPattern    ; 
    
    
	//--- JPA KEY Generation infos -------------------------------------------------
    private final boolean _bGeneratedValue ;  // True if GeneratedValue ( annotation "@GeneratedValue" )
	private final String  _sGeneratedValueStrategy ; // "AUTO", "IDENTITY", "SEQUENCE", "TABLE" 
	private final String  _sGeneratedValueGenerator ;
	
    private final boolean _bSequenceGenerator  ;  // True if SequenceGenerator ( annotation "@SequenceGenerator" )
	private final String  _sSequenceGeneratorName     ;
	private final String  _sSequenceGeneratorSequenceName   ;
	private final int     _iSequenceGeneratorAllocationSize ;

    private final boolean _bTableGenerator ;  // True if TableGenerator ( annotation "@TableGenerator" )
	private final String  _sTableGeneratorName  ;
	private final String  _sTableGeneratorTable           ;
	private final String  _sTableGeneratorPkColumnName     ;
	private final String  _sTableGeneratorValueColumnName  ;
	private final String  _sTableGeneratorPkColumnValue   ;

//	//-----------------------------------------------------------------------------------------------
//	/**
//	 * Constructor to create a Java Class Attribute without model <br>
//	 * This constructor is designed to be used by the WIZARDS GENERATOR<br>
//	 * 
//	 * @param name internal (private) java attribute name
//	 * @param simpleType the shortest type to use ( "String", "int", "BigDecimal", "Date", "java.util.Date", ... ), <br>
//	 *              can be a full type ( eg : if "java.util.Date" and "java.sql.Date" are used in the same class ) 
//	 * @param fullType standard full type with package ( "java.lang.String", "int", "java.math.BigDecimal", ... )
//	 * @param initialValue
//	 */
//	//public JavaBeanClassAttribute(String sName, String sType, String sFullType, String sInitialValue, String sGetter, String sSetter) 
//	public JavaBeanClassAttribute(String name, String simpleType, String fullType, String initialValue ) // v 2.0.7
//	{
//		_sName = name ; 
////		_sType = StrUtil.removeAllBlanks(sType);    
//		_sSimpleType   = StrUtil.removeAllBlanks(simpleType);    // v 2.0.7
//		_sFullType     = StrUtil.removeAllBlanks(fullType);  
//		_sInitialValue = initialValue; // can be null 
//		_sDefaultValue = null ; // keep null ( for hasDefaultValue )
//		
//		// v 2.0.7
////		_sGetter = sGetter ;
////		_sSetter = sSetter ;		
//	}
	
	//-----------------------------------------------------------------------------------------------
	/**
	 * Constructor to create a Java Class Attribute from the given model-column definition  
	 * @param column the column of the repository model
	 */
	public AttributeInContext(final EntityInContext entity, final Column column) 
	{
		_entity = entity ;
		
		_sName   = column.getJavaName();
		
//		_sType     = StrUtil.removeAllBlanks(Util.shortestType(column.getJavaType(), new LinkedList<String>()));
		_sFullType   = StrUtil.removeAllBlanks( column.getJavaType() );
		_sSimpleType = JavaClassUtil.shortName( _sFullType );    // v 2.0.7
				
		// v 2.0.7
//		_sGetter = Util.buildGetter(_sName, _sType);
//		_sSetter = Util.buildSetter(_sName);

		_sInitialValue    = null ; //  column.getJavaInitialValue()  ???
		_sDefaultValue    = column.getJavaDefaultValue();
		
		_sDataBaseName     = column.getDatabaseName() ;
        _sDataBaseType     = column.getDatabaseTypeName() ;
        _iJdbcTypeCode     = column.getJdbcTypeCode() ;
        _sJdbcTypeName     = column.getJdbcTypeName(); // Added in ver 2.0.7
        _bKeyElement       = column.isPrimaryKey() ;
        _bUsedInForeignKey = column.isForeignKey();
        _bAutoIncremented  = column.isAutoIncremented();
        _iDatabaseSize     = column.getDatabaseSize() ;
        _sDatabaseComment  = column.getDatabaseComment() ; // Added in v 2.1.1 - #LCH
        _sDatabaseDefaultValue = column.getDatabaseDefaultValue(); 
        _bDatabaseNotNull  = column.isDatabaseNotNull();
        
		//--- Further info for ALL
        _bNotNull   = column.getJavaNotNull();
        _sLabel     = column.getLabel();
        _sInputType = column.getInputType();
        
		//--- Further info for BOOLEAN 
        _sBooleanTrueValue   = column.getBooleanTrueValue().trim() ;
		_sBooleanFalseValue  = column.getBooleanFalseValue().trim() ;
		
		//--- Further info for NUMBER 
	    _sMinValue = column.getMinValue() ; 
	    _sMaxValue = column.getMaxValue() ; 

		//--- Further info for STRING 
        _bLongText  = column.getLongText() ;
        _bNotEmpty  = column.getNotEmpty();
        _bNotBlank  = column.getNotBlank();
        _sMaxLength = column.getMaxLength();
        _sMinLength = column.getMinLength();
        _sPattern   = column.getPattern();
        
    
		//--- Further info for DATE/TIME 
		if ( RepositoryConst.SPECIAL_DATE_ONLY.equalsIgnoreCase(column.getDateType()) ) {
			_iDateType = DATE_ONLY;
		} else if ( RepositoryConst.SPECIAL_TIME_ONLY.equalsIgnoreCase(column.getDateType()) )  {
			_iDateType = TIME_ONLY;
		} else if ( RepositoryConst.SPECIAL_DATE_AND_TIME.equalsIgnoreCase(column.getDateType()) )  {
			_iDateType = DATE_AND_TIME;
		} else {
			_iDateType =  -1  ; // Default : UNKNOWN
		}
        _bDatePast   = column.isDatePast();
        _bDateFuture = column.isDateFuture();
        _bDateBefore = column.isDateBefore();
        _sDateBeforeValue = column.getDateBeforeValue();
        _bDateAfter  = column.isDateAfter();
        _sDateAfterValue  = column.getDateAfterValue();
        
		//--- Further info for JPA         
        if ( column.isAutoIncremented() ) {
		    _bGeneratedValue = true ;
			_sGeneratedValueStrategy  = null ; // "AUTO" is the default strategy 
			_sGeneratedValueGenerator = null ;
        } 
        else {
			if (column.getGeneratedValue() != null) {
			    _bGeneratedValue = true ;
				_sGeneratedValueStrategy  = column.getGeneratedValue().getStrategy();
				_sGeneratedValueGenerator = column.getGeneratedValue().getGenerator();
			}
			else {
				_bGeneratedValue = false;
				_sGeneratedValueStrategy  = null;
				_sGeneratedValueGenerator = null;
			}
        }
			        
		if ( column.getTableGenerator() != null ) {
		    _bTableGenerator = true ;
			_sTableGeneratorName = column.getTableGenerator().getName();
			_sTableGeneratorTable = column.getTableGenerator().getTable();
			_sTableGeneratorPkColumnName = column.getTableGenerator().getPkColumnName();
			_sTableGeneratorValueColumnName = column.getTableGenerator().getValueColumnName();
			_sTableGeneratorPkColumnValue = column.getTableGenerator().getPkColumnValue();
		}
		else {
		    _bTableGenerator = false ;
			_sTableGeneratorName = null ;
			_sTableGeneratorTable = null ;
			_sTableGeneratorPkColumnName = null ;
			_sTableGeneratorValueColumnName = null;
			_sTableGeneratorPkColumnValue = null;
		}

		if (column.getSequenceGenerator() != null) {
		    _bSequenceGenerator = true;
			_sSequenceGeneratorName = column.getSequenceGenerator().getName();
			_sSequenceGeneratorSequenceName = column.getSequenceGenerator().getSequenceName();
			_iSequenceGeneratorAllocationSize = column.getSequenceGenerator().getAllocationSize();
		}
		else {
		    _bSequenceGenerator = false;
			_sSequenceGeneratorName = null;
			_sSequenceGeneratorSequenceName = null;
			_iSequenceGeneratorAllocationSize = -1;
		}
		
	}
    
	//-----------------------------------------------------------------------------------------------
//	protected void forceType ( String sTypeToUse )
//	{
//		if ( sTypeToUse != null )
//		{
//			_sType = sTypeToUse ;
//		}
//	}
	/* package */ void useFullType ()
	{
		_bUseFullType = true ;
	}
	
	@VelocityMethod(
			text={	
				"Returns the name of the attribute "
				}
		)
	public String getName()
	{
		return _sName;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the attribute's name with n trailing blanks "
			},
		parameters = { 
			"n : the number of blanks to be added at the end of the name" 
			}
	)
	public String formattedName(int iSize)
    {
        String s = _sName ;
        String sTrailingBlanks = "";
        int iDelta = iSize - s.length();
        if (iDelta > 0) // if needs trailing blanks
        {
            sTrailingBlanks = GeneratorUtil.blanks(iDelta);
        }
        return s + sTrailingBlanks;
    }

	//-------------------------------------------------------------------------------------
	/**
	 * Returns the "java type" to use <br>
	 * usually the simple type ( "int", "BigDecimal", "Date" ) <br>
	 * sometimes the full type ( if the simple type is ambiguous )
	 * @return
	 */
	@VelocityMethod(
		text={	
			"Returns the recommended type for the attribute",
			"usually the simple type ( 'int', 'BigDecimal', 'Date' ) ",
			"sometimes the full type ( if the simple type is considered as ambiguous )",
			"Examples for Java : 'int', 'BigDecimal', 'Date', 'java.util.Date', 'java.sql.Date' "
			}
	)
	public String getType()
	{
		// return _sType;
		// v 2.0.7
		if ( _bUseFullType ) {
			return _sFullType ;
		}
		else {
			return _sSimpleType ;
		}
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the attribute's type with n trailing blanks "
			},
		parameters = { 
			"n : the number of blanks to be added at the end of the name" 
			}
	)
	public String formattedType(int iSize)
    {
		String sType = this.getType() ;
        String sTrailingBlanks = "";
        int iDelta = iSize - sType.length();
        if (iDelta > 0) // if needs trailing blanks
        {
            sTrailingBlanks = GeneratorUtil.blanks(iDelta);
        }
        return sType + sTrailingBlanks;
    }	
    
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the full type ( java.math.BigDecimal, java.util.Date, .. )"
			}
	)
	public String getFullType()
	{
		return _sFullType;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the simple type ( BigDecimal, Date, int, Integer, .. )"
			}
	)
	public String getSimpleType()
	{
		return _sSimpleType;
	}
	
	//-------------------------------------------------------------------------------------
	/**
	 * Returns the "java wrapper type" ie "Float" for "float" type, "Boolean" for "boolean" type
	 * @return
	 */
	@VelocityMethod(
		text={	
			"Returns the Java wrapper type corresponding to the attribute's primitive type",
			"Examples : 'Float' for 'float', 'Integer' for 'int', 'Boolean' for 'boolean', ... ",
			"The attribute's type is retuned as is if it's not a primitive type"
			}
	)
	public String getWrapperType()
	{
		if ( null == _sSimpleType ) return "UnknownType" ;
		
		final String t = _sSimpleType;
		if ("byte".equals(t)) {
			return "Byte";
		} else if ("short".equals(t)) {
			return "Short";
		} else if ("int".equals(t)) {
			return "Integer";
		} else if ("long".equals(t)) {
			return "Long";
		} else if ("float".equals(t)) {
			return "Float";
		} else if ("double".equals(t)) {
			return "Double";
		} else if ("boolean".equals(t)) {
			return "Boolean";
		} else if ("char".equals(t)) {
			return "Character";
		} else {
			return t;
		}
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the type of the date : $const.DATE_ONLY, $const.TIME_ONLY, $const.DATE_AND_TIME"
			}
	)
	public int getDateType()
	{
		return _iDateType ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if there's an initial value for the attribute"
			}
	)
	public boolean hasInitialValue()
	{
		return _sInitialValue != null ;
	}
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the initial value for the attribute"
			}
	)
	public String getInitialValue()
	{
		return _sInitialValue;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
				"Returns the getter for the attribute",
				"e.g : 'getFoo' for 'foo' (or 'isFoo' for a boolean primitive type)"
					}
	)
	public String getGetter()
	{
		// return _sGetter;
		return Util.buildGetter(_sName, this.getType() ); // v 2.0.7
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
				"Returns the getter for the attribute with always a 'get' prefix",
				"even for a boolean"
					}
	)
	public String getGetterWithGetPrefix()
	{
		// return _sGetter;
		return Util.buildGetter(_sName); // v 2.0.7
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
				"Returns the setter for the attribute",
				"e.g : 'setFoo' for 'foo' "
				}
	)
	public String getSetter()
	{
		//return _sSetter;
		return Util.buildSetter(_sName);
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the value to use for a boolean when is TRUE (eg to be stored in a database) "
			}
	)
	public String getBooleanTrueValue()
	{
		return _sBooleanTrueValue;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the value to use for a boolean when is FALSE (eg to be stored in a database) "
			}
	)
	public String getBooleanFalseValue()
	{
		return _sBooleanFalseValue ;
	}
	
	//----------------------------------------------------------------------
	// Database 
	//----------------------------------------------------------------------
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the database name for the attribute",
			"Typically the column name for a relational database"
			}
	)
    public String getDatabaseName()
    {
        return _sDataBaseName;
    }

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the database native type for the attribute",
			"For example : INTEGER, VARCHAR, etc..."
			}
	)
    public String getDatabaseType()
    {
        return _sDataBaseType;
    }

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the database native type for the attribute with the size if it makes sens",
			"For example : INTEGER, VARCHAR(24), NUMBER, CHAR(3), etc..."
			},
		since="2.0.7"
	)
    public String getDatabaseTypeWithSize()
    {
//        if ( _iJdbcTypeCode == Types.VARCHAR || _iJdbcTypeCode == Types.CHAR ) {
//        	return _sDataBaseType + "(" + _iDatabaseSize + ")" ;
//        }
//        else {
//        	return _sDataBaseType ;
//        }
        return DatabaseUtil.getNativeTypeWithSize(_sDataBaseType, _iDatabaseSize, _iJdbcTypeCode);
    }

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the database size for the attribute"
			}
	)
    public int getDatabaseSize()
    {
        return _iDatabaseSize ;
    }

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the database comment for the attribute"
			},
		since="2.1.1"
	)
    public String getDatabaseComment()
    {
        return _sDatabaseComment;
    }

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the attribute has a database default value"
			}
	)
    public boolean hasDatabaseDefaultValue()
    {
    	if ( _bAutoIncremented ) return false ; // No default value for auto-incremented fields
        if ( _sDatabaseDefaultValue != null )
        {
        	if ( _sDatabaseDefaultValue.length() > 0 ) return true ;
        }
        return false ;
    }
    
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns the database default value for the attribute (or a void string if none)"
		}
	)
    public String getDatabaseDefaultValue()
    {
    	if ( hasDatabaseDefaultValue() ) return _sDatabaseDefaultValue ;
        return "" ;
    }
    
	//----------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns TRUE if the attribute must be NOT NULL when stored in the database"
		}
	)
    public boolean isDatabaseNotNull()
    {
        return _bDatabaseNotNull;
    }
//    /**
//     * Synonym for usage without "()"
//     * @return
//     */
//    public boolean getIsDatabaseNotNull()
//    {
//        return isDatabaseNotNull();
//    }
    
	//----------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the JDBC type of the attribute (the type code)"
			}
		)
    public int getJdbcTypeCode()
    {
        return _iJdbcTypeCode ;
    }

	//----------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the JDBC type name ('CHAR', 'VARCHAR', 'NUMERIC', ... )<br>",
			"The 'java.sql.Types' constant name for the current JDBC type code"
			}
		)
    public String getJdbcTypeName()
    {
        return _sJdbcTypeName ;
    }

	//----------------------------------------------------------------------
    /**
     * Returns the recommended Java type for the JDBC type 
     * @return
     */
	@VelocityMethod(
			text={	
				"Returns the recommended Java type for the JDBC type of the attribute"
				}
		)
    public String getJdbcRecommendedJavaType()
    {
    	JdbcTypes types = JdbcTypesManager.getJdbcTypes();
    	return types.getJavaTypeForCode(_iJdbcTypeCode, _bDatabaseNotNull );
    }

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
    public boolean isKeyElement()
    {
        return _bKeyElement;
    }
//    /**
//     * Synonym for usage without "()"
//     * @return
//     */
//    public boolean getIsKeyElement()
//    {
//        return isKeyElement();
//    }

	//----------------------------------------------------------------------
    /**
     * Returns TRUE if the attribute is used in (at least) one Foreign Key 
     * @return 
     */
	@VelocityMethod(
	text={	
		"Returns TRUE if the attribute is used in (at least) one Foreign Key"
		}
	)
    public boolean isUsedInForeignKey()
    {
        return _bUsedInForeignKey ;
    }
//    /**
//     * Synonym for usage without "()"
//     * @return
//     */
//    public boolean getIsUsedInForeignKey()
//    {
//        return isKeyElement();
//    }

    /**
     * Returns TRUE if the attribute is involved in a link Foreign Key <br>
     * Useful for JPA, to avoid double mapping ( FK field and owning side link )
     * @param linksArray - list of the links to be checked 
     * @return
     */
	@VelocityMethod(
	text={	
		"Returns TRUE if the attribute is involved in a link Foreign Key",
		"Useful for JPA, to avoid double mapping ( FK field and owning side link )"
		},
	parameters="links : list of links where to search the attribute"
	)
    public boolean isUsedInLinkJoinColumn( List<LinkInContext> links )
    {
    	if ( null == _sDataBaseName ) {
    		return false ; // No mapping 
    	}
    	
		for ( LinkInContext link : links ) {
//			if ( link.isOwningSide() && link.hasJoinColumns() ) {
//				String[] joinColumns = link.getJoinColumns() ;
//				if ( joinColumns != null ) {
//					for ( int i = 0 ; i < joinColumns.length ; i++ ) {
//						String colName = joinColumns[i];
//						if ( _sDataBaseName.equalsIgnoreCase( colName ) ) {
//							//--- Yes : this attribute's mapping column is a 'Join Column' 
//							return true ;
//						}
//					}
//				}
//			}
			if( link.isOwningSide() ) {
				if ( link.usesAttribute(this) ) {
					return true ;
				}					
			}
			
		}
		return false ;
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
    public boolean isAutoIncremented()
    {
        return _bAutoIncremented;
    }
    
//    /**
//     * Synonym for usage without "()"
//     * @return
//     */
//    public boolean getIsAutoIncremented()
//    {
//        return isAutoIncremented();
//    }

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
    public boolean isNotNull()
    {
        return _bNotNull;
    }
//    /**
//     * Synonym for usage without "()"
//     * @return
//     */
//    public boolean getIsNotNull()
//    {
//        return isNotNull();
//    }

	//----------------------------------------------------------------------
    /**
     * Returns the label defined for the attribute 
     * @since v 2.0.3
     * @return
     */
	@VelocityMethod(
		text={	
			"Returns the label for the attribute "
			}
	)
    public String getLabel()
    {
        return _sLabel ;
    }
    
	//----------------------------------------------------------------------
    /**
     * Returns the "input type" defined for this attribute 
     * @since v 2.0.3
     * @return
     */
	@VelocityMethod(
		text={	
			"Returns the 'input type' defined for the attribute",
			"Typically for HTML 5 : 'number', 'date', ..."
			},
		since="2.0.3"
	)
    public String getInputType()
    {
        return _sInputType ;
    }
    
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns maximum input length to be used in the GUI ",
			"For string types the specific maximum lenght is returned ( or void if not defined )",
			"For numeric types the maximum lenght depends on the type ( 4 for 'byte', 11 for 'int', etc... ) ",
			"For 'date' 10, for 'time' 8"
			}
	)
    public String getGuiMaxLength() 
    {
		String t = _sSimpleType ;
    	//--- Max length depending on the Java type
    	if ( "byte".equals(t)  || "Byte".equals(t)    ) return  "4" ; // -128 to +127
    	if ( "short".equals(t) || "Short".equals(t)   ) return  "6" ; // -32768 to +32767
    	if ( "int".equals(t)   || "Integer".equals(t) ) return "11" ; // -2147483648 to +2147483647
    	if ( "long".equals(t)  || "Long".equals(t)    ) return "20" ; // -9223372036854775808 to +9223372036854775807
    	
    	if ( "double".equals(t) || "Double".equals(t) ) return "20" ; // Arbitrary fixed value like long
    	if ( "float".equals(t)  || "Float".equals(t)  ) return "20" ; // Arbitrary fixed value like long
    	
    	if ( "BigDecimal".equals(t) ) return "20" ; // Arbitrary fixed value like long
    	if ( "BigInteger".equals(t) ) return "20" ; // Arbitrary fixed value like long
    	
    	if ( "Date".equals(t) ) return "10" ; // "YYYY-MM-DD", "DD/MM/YYYY", etc ...
    	if ( "Time".equals(t) ) return "8" ; // "HH:MM:SS"

    	//--- Max length from Database column size (only for String)
    	if ( "String".equals(t) )
    	{
    		return voidIfNull ( _sMaxLength ) ;
    	}
		return "";
    }
    
    /**
     * Shortcut for Velocity attribute syntax : $var.guiMaxLengthAttribute 
     * @return
     */
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the GUI 'maxlength' attribute (or void if none) ",
			"e.g 'maxlength=12' "
			}
	)
    public String getGuiMaxLengthAttribute() 
    {
    	return guiMaxLengthAttribute() ;
    }
    
    //-------------------------------------------------------------------------------------------
    /**
     * For Velocity function call syntax : $var.guiMaxLengthAttribute() 
     * @return
     */
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the GUI 'maxlength' attribute (or void if none) ",
			"e.g 'maxlength=12' "
			}
	)
    public String guiMaxLengthAttribute() 
    {
    	return guiMaxLengthAttribute("maxlength") ;
    }
    
    /**
     * For Velocity function call syntax : $var.guiMaxLengthAttribute('maxlength') 
     * @param attributeName
     * @return
     */
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the GUI specific attribute for maximum length (or void if none) ",
			"e.g 'myattribute=12' for guiMaxLengthAttribute('myattribute') "
			},
		parameters = "guiAttributeName : the name of the attribute to be set in the GUI"
	)
    public String guiMaxLengthAttribute(String attributeName) 
    {
    	if ( attributeName != null )
    	{
    		String s = getGuiMaxLength();
    		if ( ! StrUtil.nullOrVoid(s) )
    		{
    			return attributeName + "=\"" + s + "\"" ;
    		}
    	}
    	return "";
    }
    
    //-------------------------------------------------------------------------------------------
    /**
     * Returns the "maximum" length if any, else returns "" 
     * @return
     */
	@VelocityMethod(
			text={	
				"Returns the maximum length for the attribute (if any, else returns void) "
				}
		)
    public String getMaxLength() 
    {
    	return voidIfNull(_sMaxLength) ;
    }
    /**
     * Returns the "minimum" length if any, else returns "" 
     * @return
     */
	@VelocityMethod(
			text={	
				"Returns the minimum length for the attribute (if any, else returns void) "
				}
		)
    public String getMinLength() 
    {
    	return voidIfNull(_sMinLength) ;
    }
    
    //-------------------------------------------------------------------------------------------
    /**
     * Returns the "pattern" (Reg Exp) if any, else returns "" 
     * @return
     */
	@VelocityMethod(
			text={	
				"Returns the Reg Exp pattern defined for the attribute (if any, else returns void) "
				}
		)
    public String getPattern() 
    {
    	return voidIfNull(_sPattern) ;
    }
    
    //-------------------------------------------------------------------------------------------
    /**
     * Returns the "minimum" value if any, else returns "" 
     * @return
     */
	@VelocityMethod(
			text={	
				"Returns the minimum value for the attribute (if any, else returns void) "
				}
		)
    public String getMinValue() 
    {
    	return voidIfNull(_sMinValue) ;
    }
    
    //-------------------------------------------------------------------------------------------
    /**
     * Returns the "maximum" value if any, else returns "" 
     * @return
     */
	@VelocityMethod(
			text={	
				"Returns the maximum value for the attribute (if any, else returns void) "
				}
		)
    public String getMaxValue() 
    {
    	return voidIfNull(_sMaxValue) ;
    }
    //-------------------------------------------------------------------------------------------
    /**
     * Synonym for Velocity attribute syntax : $var.guiMinMaxAttributes 
     * @return
     */
	@VelocityMethod(
		text={	
			"Returns the GUI attributes for minimum and maximum values (or void if none)",
			"e.g 'min=10 max=20' "
			}
	)
    public String getGuiMinMaxAttributes() 
    {
    	return guiMinMaxAttributes() ;
    }
    
	//-------------------------------------------------------------------------------------
    /**
     * For Velocity function call syntax : $var.guiMinMaxAttributes() 
     * @return
     */
	@VelocityMethod(
		text={	
			"Returns the GUI attributes for minimum and maximum values (or void if none)",
			"e.g 'min=10 max=20' "
			}
	)
    public String guiMinMaxAttributes() 
    {
    	return guiMinMaxAttributes("min", "max") ;
    }

    //-------------------------------------------------------------------------------------------
    /**
     * For Velocity function call syntax : $var.guiMinMaxAttributes('min','max') 
     * @param attributeName
     * @return
     */
	@VelocityMethod(
		text={	
			"Returns the GUI specific attribute for minimum and maximum values (or void if none) ",
			"e.g 'mini=10 maxi=20' for guiMaxLengthAttribute('mini', 'maxi') "
			},
		parameters = {
			"guiMinAttributeName : the name of the MIN attribute to be set in the GUI",
			"guiMaxAttributeName : the name of the MAX attribute to be set in the GUI"
		}
	)
    public String guiMinMaxAttributes(String minAttributeName, String maxAttributeName  ) 
    {
    	if ( minAttributeName != null && maxAttributeName != null )
    	{
    		String sMin = getMinValue();
    		String sMinAttr = "" ;
    		if ( ! StrUtil.nullOrVoid(sMin) )
    		{
    			sMinAttr = minAttributeName + "=\"" + sMin + "\"" ;
    		}
    		
    		String sMax = getMaxValue();
    		String sMaxAttr = "" ;
    		if ( ! StrUtil.nullOrVoid(sMax) )
    		{
    			sMaxAttr = maxAttributeName + "=\"" + sMax + "\"" ;
    		}
    		return sMinAttr + " " + sMaxAttr ;
    	}
    	return "" ;
    }

    /**
     * Returns the GUI "type" if any, else returns "" 
     * @return
     */
	@VelocityMethod(
			text={	
				"Returns the GUI type if any (else returns a void string)",
				"e.g 'int', 'num', 'date', 'time', '' "
				}
		)
    public String getGuiType() 
    {
		String t = _sSimpleType ; // v 2.0.7
    	//--- type="int"
    	if ( "byte".equals(t)  || "Byte".equals(t)    ) return TYPE_INT ;
    	if ( "short".equals(t) || "Short".equals(t)   ) return TYPE_INT ; 
    	if ( "int".equals(t)   || "Integer".equals(t) ) return TYPE_INT ; 
    	if ( "long".equals(t)  || "Long".equals(t) )    return TYPE_INT ; 
    	if ( "BigInteger".equals(t) )   return TYPE_INT ;

    	//--- type="num"
    	if ( "float".equals(t)  || "Float".equals(t) )    return TYPE_NUM ; 
    	if ( "double".equals(t) || "Double".equals(t) )   return TYPE_NUM ; 
    	if ( "BigDecimal".equals(t) )   return TYPE_NUM ;
    	
    	//--- type="date"
    	if ( "Date".equals(t) )   return TYPE_DATE ;
    	
    	//--- type="time"
    	if ( "Time".equals(t) )   return TYPE_TIME ;
    	
    	return "" ;
    }
    
    /**
     * For Velocity attribute syntax : $var.guiTypeAttribute
     * @return
     */
	@VelocityMethod(
			text={	
				"Returns the GUI type attribute ",
				"e.g : type='int' "
				}
				)
    public String getGuiTypeAttribute() 
    {
    	return guiTypeAttribute() ;
    }
    /**
     * For Velocity function call syntax : $var.guiTypeAttribute() 
     * @return
     */
	@VelocityMethod(
			text={	
				"Returns the GUI type attribute ",
				"e.g : type='int' "
				}
				)
    public String guiTypeAttribute() 
    {
    	return guiTypeAttribute("type") ;
    }
    /**
     * For Velocity function call syntax : $var.guiTypeAttribute('type') 
     * @param attributeName
     * @return
     */
	@VelocityMethod(
	text={	
		"Returns the GUI type attribute ",
		"e.g : type='int' "
		},
	parameters={
			"guiTypeAttributeName : name of the TYPE attribute to be set in the GUI "
		}
		)
    public String guiTypeAttribute(String attributeName) 
    {
    	if ( attributeName != null )
    	{
    		String s = getGuiType();
    		if ( ! StrUtil.nullOrVoid(s) )
    		{
    			return attributeName + "=\"" + s + "\"" ;
    		}
    	}
    	return "";
    }
    
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the attribute must be validated as a date in the past"
			}
	)
	public boolean hasDatePastValidation() {
		return _bDatePast;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the attribute must be validated as a date in the future"
			}
	)
	public boolean hasDateFutureValidation() {
		return _bDateFuture;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the attribute must be validated as a date before a given date value"
			}
	)
	public boolean hasDateBeforeValidation() {
		return _bDateBefore;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the 'date before' value (for date validation)"
			}
	)
	public String getDateBeforeValue() {
		return _sDateBeforeValue;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the attribute must be validated as a date after a given date value"
			}
	)
	public boolean hasDateAfterValidation() {
		return _bDateAfter;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the 'date after' value (for date validation)"
			}
	)
	public String getDateAfterValue() {
		return _sDateAfterValue;
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
    public boolean isLongText()
    {
        return _bLongText;
    }

	@VelocityMethod(
	text={	
		"Returns TRUE if the attribute has a 'Not Empty' validation rule "
		}
	)
    public boolean isNotEmpty()
    {
        return _bNotEmpty;
    }
    
	@VelocityMethod(
	text={	
		"Returns TRUE if the attribute has a 'Not Blank' validation rule "
		}
	)
    public boolean isNotBlank()
    {
        return _bNotBlank;
    }
    
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if there's a default value for the attribute"
			}
	)
    public boolean hasDefaultValue() // Velocity : $attrib.hasDefaultValue()
    {
    	return ( _sDefaultValue != null ) ;
    }
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the default value for the attribute"
			}
	)
    public String getDefaultValue() // Velocity : ${attrib.defaultValue}
    {
    	return _sDefaultValue ;
    }

	public String toString()
	{
		String s =  _sInitialValue != null ? " = " + _sInitialValue : "" ;
		return this.getType() + " " + _sName + s ; // + " ( " + _sGetter + "/" + _sSetter + " ) ";
	}

	private String voidIfNull ( String s ) {
		return s != null ? s : "" ;
	}
	
	//------------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns TRUE if the attribute's type is a Java primitive type",
		"i.e. int, float, boolean, ..."
		}
	)
	public boolean isPrimitiveType()
	{
		return JavaTypeUtil.isPrimitiveType( _sSimpleType );
	}

	//------------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns TRUE if the attribute's type is a Java array ( byte[], String[], ... )"
		},
	since="2.0.7"
	)
	public boolean isArrayType()
	{
		String s = _sSimpleType ;
		if ( s != null ) {
			if ( s.trim().endsWith("]")) {
				return true ;
			}
		}
    	return false ;
	}

	//------------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns TRUE if the attribute's type is a Java 'boolean/Boolean' type"
		},
	since="2.0.7"
	)
	public boolean isBooleanType()
	{
    	if ( "boolean".equals(_sSimpleType) )   return true ;
    	if ( "Boolean".equals(_sSimpleType) )   return true ;
    	return false ;
	}

	//------------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns TRUE if the attribute's type is a Java 'byte/Byte' type"
		},
	since="2.0.7"
	)
	public boolean isByteType()
	{
    	if ( "byte".equals(_sSimpleType) )   return true ;
    	if ( "Byte".equals(_sSimpleType) )   return true ;
    	return false ;
	}

	//------------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns TRUE if the attribute's type is a Java 'short/Short' type"
		},
	since="2.0.7"
	)
	public boolean isShortType()
	{
    	if ( "short".equals(_sSimpleType) )   return true ;
    	if ( "Short".equals(_sSimpleType) )   return true ;
    	return false ;
	}

	//------------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns TRUE if the attribute's type is a Java 'int/Integer' type"
		},
	since="2.0.7"
	)
	public boolean isIntegerType()
	{
    	if ( "int".equals(_sSimpleType) )   return true ;
    	if ( "Integer".equals(_sSimpleType) ) return true ;
    	return false ;
	}

	//------------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns TRUE if the attribute's type is a Java 'long/Long' type"
		},
	since="2.0.7"
	)
	public boolean isLongType()
	{
    	if ( "long".equals(_sSimpleType) )   return true ;
    	if ( "Long".equals(_sSimpleType) )   return true ;
    	return false ;
	}

	//------------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns TRUE if the attribute's type is a Java 'float/Float' type"
		},
	since="2.0.7"
	)
	public boolean isFloatType()
	{
    	if ( "float".equals(_sSimpleType) )   return true ;
    	if ( "Float".equals(_sSimpleType) )   return true ;
    	return false ;
	}
	
	//------------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns TRUE if the attribute's type is a Java 'double/Double' type"
		},
	since="2.0.7"
	)
	public boolean isDoubleType()
	{
    	if ( "double".equals(_sSimpleType) )   return true ;
    	if ( "Double".equals(_sSimpleType) )   return true ;
    	return false ;
	}

	//------------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns TRUE if the attribute's type is a Java 'BigDecimal' type"
		},
	since="2.0.7"
	)
	public boolean isBigDecimalType()
	{
    	if ( "BigDecimal".equals(_sSimpleType) )   return true ;
    	return false ;
	}


	//------------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns TRUE if the attribute's type is a Java number type",
		"(byte, Byte, int, Integer, float ,Float, BigDecimal, etc... )"
		},
	since="2.0.7"
	)
	public boolean isNumberType()
	{
    	if ( isByteType() || isShortType() || isIntegerType() || isLongType() )   return true ;
    	if ( isFloatType() || isDoubleType() )   return true ;
    	if ( isBigDecimalType() )   return true ;
    	return false ;
	}

	//------------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns TRUE if the attribute's type is a Java 'String' type"
		},
	since="2.0.7"
	)
	public boolean isStringType()
	{
    	if ( "String".equals(_sSimpleType) )   return true ;
    	return false ;
	}

	//------------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns TRUE if the attribute's type is 'java.util.Date' type"
		},
	since="2.0.7"
	)
	public boolean isUtilDateType()
	{
    	if ( "java.util.Date".equals(_sFullType) ) return true ;
    	return false ;
	}
	//------------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns TRUE if the attribute's type is 'java.sql.Date' type"
		},
	since="2.0.7"
	)
	public boolean isSqlDateType()
	{
    	if ( "java.sql.Date".equals(_sFullType) ) return true ;
    	return false ;
	}
	//------------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns TRUE if the attribute's type is 'java.sql.Time' type"
		},
	since="2.0.7"
	)
	public boolean isSqlTimeType()
	{
    	if ( "java.sql.Time".equals(_sFullType) ) return true ;
    	return false ;
	}
	//------------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns TRUE if the attribute's type is 'java.sql.Timestamp' type"
		},
	since="2.0.7"
	)
	public boolean isSqlTimestampType()
	{
    	if ( "java.sql.Timestamp".equals(_sFullType) ) return true ;
    	return false ;
	}
	//------------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns TRUE if the attribute's type is a Java temporal type",
		"( java.util.Date, java.sql.Date, java.sql.Time, java.sql.Timestamp )"
		},
	since="2.0.7"
	)
	public boolean isTemporalType()
	{
    	if ( isUtilDateType() || isSqlDateType() || isSqlTimeType() || isSqlTimestampType() ) return true ;
    	return false ;
	}

	//------------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns TRUE if the attribute's type is a Java 'Blob' type"
		},
	since="2.0.7"
	)
	public boolean isBlobType()
	{
    	if ( "Blob".equals(_sSimpleType) )   return true ;
    	return false ;
	}

	//------------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns TRUE if the attribute's type is a Java 'Clob' type"
		},
	since="2.0.7"
	)
	public boolean isClobType()
	{
    	if ( "Clob".equals(_sSimpleType) )   return true ;
    	return false ;
	}

	//-----------------------------------------------------------------------------------------
	// JPA "@GeneratedValue"
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
		return _bGeneratedValue;
	}

	/**
	 * Returns the GeneratedValue strategy : auto, identity, sequence, table
	 * or null if not defined
	 * @return
	 */
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the strategy for a 'generated value' (or null if none)",
			"e.g : 'auto', 'identity', 'sequence', 'table' "
			}
	)
	public String getGeneratedValueStrategy() {
		return _sGeneratedValueStrategy;
	}

	//-------------------------------------------------------------------------------------
	/**
	 * Returns the GeneratedValue generator : the name of the primary key generator to use <br>
	 * The generator name referenced a "SequenceGenerator" or a "TableGenerator"
	 * @return
	 */
	@VelocityMethod(
		text={	
			"Returns the generator for a 'generated value' ",
			"Typically for JPA : 'SequenceGenerator' or 'TableGenerator' "
			}
	)
	public String getGeneratedValueGenerator() {
		return _sGeneratedValueGenerator;
	}

	//-----------------------------------------------------------------------------------------
	// JPA "@SequenceGenerator"
	//-----------------------------------------------------------------------------------------
	/**
	 * Returns true if this attribute is a "GeneratedValue" using a "SequenceGenerator"
	 * @return
	 */
	@VelocityMethod(
		text={	
			"Returns TRUE if the attribute is a 'generated value' using a 'sequence generator' ",
			"Typically for JPA '@SequenceGenerator'  "
			}
	)
	public boolean hasSequenceGenerator() {
		return _bSequenceGenerator;
	}

	//-----------------------------------------------------------------------------------------
	/**
	 * Returns the "@SequenceGenerator" name
	 * @return
	 */
	@VelocityMethod(
		text={	
			"Returns the name of the 'sequence generator' ",
			"Typically for JPA '@SequenceGenerator/name'  "
			}
	)
	public String getSequenceGeneratorName() {
		return _sSequenceGeneratorName;
	}

	//-----------------------------------------------------------------------------------------
	/**
	 * Returns the "@SequenceGenerator" sequence name
	 * @return
	 */
	@VelocityMethod(
		text={	
			"Returns the 'sequence name' to be used in the 'sequence generator' definition",
			"Typically for JPA '@SequenceGenerator/sequenceName'  "
			}
	)
	public String getSequenceGeneratorSequenceName() {
		return _sSequenceGeneratorSequenceName;
	}

	//-----------------------------------------------------------------------------------------
	/**
	 * Returns the "@SequenceGenerator" sequence allocation size
	 * @return
	 */
	@VelocityMethod(
		text={	
			"Returns the 'sequence allocation size' to be used in the 'sequence generator' definition",
			"Typically for JPA '@SequenceGenerator/allocationSize'  "
			}
	)
	public int getSequenceGeneratorAllocationSize() {
		return _iSequenceGeneratorAllocationSize;
	}

	//-----------------------------------------------------------------------------------------
	// JPA "@TableGenerator"
	//-----------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the attribute is a 'generated value' using a 'table generator' ",
			"Typically for JPA '@TableGenerator'  "
			}
	)
	public boolean hasTableGenerator() {
		return _bTableGenerator;
	}

	//-----------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the name of the 'table generator' ",
			"Typically for JPA '@TableGenerator/name'  "
			}
	)
	public String getTableGeneratorName() {
		return _sTableGeneratorName;
	}

	//-----------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the name of the table used in the 'table generator' ",
			"Typically for JPA '@TableGenerator/table'  "
			}
	)
	public String getTableGeneratorTable() {
		return _sTableGeneratorTable;
	}

	//-----------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the name of the Primary Key column used in the 'table generator' ",
			"Typically for JPA '@TableGenerator/pkColumnName'  "
			}
	)
	public String getTableGeneratorPkColumnName() {
		return _sTableGeneratorPkColumnName;
	}

	//-----------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the name of the column that stores the last value generated by the 'table generator' ",
			"Typically for JPA '@TableGenerator/valueColumnName'  "
			}
	)
	public String getTableGeneratorValueColumnName() {
		return _sTableGeneratorValueColumnName;
	}

	//-----------------------------------------------------------------------------------------
	@VelocityMethod(
	text={
		"Returns the primary key value in the generator table that distinguishes this set of generated values",
		"from others that may be stored in the table",
		"Typically for JPA '@TableGenerator/pkColumnValue'  "
		}
	)
	public String getTableGeneratorPkColumnValue() {
		return _sTableGeneratorPkColumnValue;
	}
	
	//------------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns the 'simple type' of the entity referenced by this attribute (if any) ",
		"Returns a type only if the attribute is the only 'join column' of the link",
		"else returns a 'void string' (if the attribute is not involved in a link, ",
		"or if the link as many join columns)"
		},
	since="2.1.0"
	)
	public String getReferencedEntityType() throws GeneratorException {
		for( LinkInContext link : _entity.getLinks()  ) {
//			if( link.isOwningSide() && link.hasJoinColumns() ) {
//				String[] joinColumns = link.getJoinColumns() ;
//				if ( joinColumns != null && joinColumns.length == 1 ) {
//					if( joinColumns[0].equals(this.getDatabaseName() ) ) {
//						return link.getTargetEntitySimpleType() ;
//					}
//				}
			if( link.isOwningSide() && link.getAttributesCount() == 1 ) {
				if ( link.usesAttribute(this) ) {
					return link.getTargetEntitySimpleType() ;
				}					
			}
		}
		return "";
	}
	
	//------------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Returns TRUE if the attribute is referencing another entity by itself ",
		"(if the attribute is the only 'join column' of a link)"
		},
		since="2.1.0"
	)
	public boolean isReferencingAnotherEntity() throws GeneratorException {
		return getReferencedEntityType().length() > 0 ;
	}
}