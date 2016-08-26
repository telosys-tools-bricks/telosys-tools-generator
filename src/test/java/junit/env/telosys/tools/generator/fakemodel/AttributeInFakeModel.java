/**
 *  Copyright (C) 2008-2015  Telosys project org. ( http://www.telosys.org/ )
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
package junit.env.telosys.tools.generator.fakemodel;

import java.io.Serializable;
import java.math.BigDecimal;

import org.telosys.tools.commons.jdbctypes.JdbcTypesManager;
import org.telosys.tools.generic.model.Attribute;
import org.telosys.tools.generic.model.DateType;

/**
 * Column of a table/entity in the Repository Model <br>
 * 
 * A column contains the database informations and the mapped Java attribute informations
 * 
 * @author Laurent Guerin
 *
 */
public class AttributeInFakeModel implements Comparable<AttributeInFakeModel>, Serializable, Attribute
{
	private static final long serialVersionUID = 1L;
	
	public final static String SPECIAL_LONG_TEXT_TRUE = "true";
	
	//----- DATABASE -----
	
	private String  _sDatabaseName     = null ;  // dbName=""

	private String  _sDatabaseTypeName = null ;  // dbTypeName="INTEGER" - dbTypeName="VARCHAR"
	
	private int     _iDatabaseSize     = 0 ;     // dbSize=""
	
	private boolean _bDatabaseNotNull  = false ; // dbNotNull="true|false" ( false by default )
	
	private boolean _bKeyElement        = false ; // primaryKey="true|false" ( false by default ) // v 3.0.0
	
//	private boolean _bForeignKey       = false ; // foreignKey="true|false" ( false by default )
	private boolean _bForeignKeySimple     = false ; // ( false by default )
	private boolean _bForeignKeyComposite  = false ; // ( false by default )


	private boolean _bAutoIncremented  = false ; // autoIncremented="true|false" ( false by default )
	
//	private int     _iDatabasePosition = 0 ;     // position="" ( database ordinal position ) #LGU 10/08/2011
	
	private String  _sDatabaseDefaultValue = null ;  // dbDefaultValue="" ( database default value ) #LGU 10/08/2011
	
	private String  _sDatabaseComment  = null ;  // comment=""
	
	//----- JDBC -----
	
	private int     _iJdbcTypeCode     = 0 ;     // dbTypeCode="4" - dbTypeCode="12"
	
	//----- JAVA -----

	private final String  _sName         ;  
	
//	private String  _sFullType    = null ;  // javaType="int|...." 
	private final String  _sNeutralType    ;  // v 3.0.0
	
	private boolean _bNotNull = false ;  // javaNotNull="true|false" 
	
	private String  _sJavaDefaultValue = null ;  // javaDefaultValue="..." 
	
	private boolean _bSelected    = true ;  // selected by default
	
	//----- SPECIAL DATA for ALL -----
	private String  _sLabel     = null ;
	private String  _sInputType = null ;
	
	//----- SPECIAL DATA for STRING -----	
	private boolean _bLongText = false ;  //  
	private boolean _bNotEmpty = false ;  // notEmpty="true|false" 
	private boolean _bNotBlank = false ;  // notBlank="true|false" 
	private Integer  _iMinLength = null ;
	private Integer  _iMaxLength = null ;
	private String  _sPattern   = null ;
	
	//----- SPECIAL DATA for DATE & TIME -----
	private DateType dateType = null ; // enumeration - ver 3.0.0
	private boolean _bDatePast   = false ;
	private boolean _bDateFuture = false ;
	private boolean _bDateBefore = false ;
	private boolean _bDateAfter  = false ;
	private String  _sDateBeforeValue = null ;
	private String  _sDateAfterValue  = null ;
	
	//----- SPECIAL DATA for NUMERIC -----
//	private Integer _iMinValue = null ;
//	private Integer _iMaxValue = null ;
	private BigDecimal _iMinValue = null ;
	private BigDecimal _iMaxValue = null ;
	
	//----- SPECIAL DATA for BOOLEAN -----
	private String  _sBooleanTrueValue  = null ; // the special value for TRUE 
	private String  _sBooleanFalseValue = null ; // the special value for FALSE 
	
	//----- OTHER SPECIAL DATA -----
//	private String  _sFormat = null ;  // Used with NUMERIC, DATE/TIME

	//----- SPECIAL DATA for key generation -----
	
//	private GeneratedValueInDbModel generatedValue = null ;
//	
//	private TableGeneratorInDbModel tableGenerator = null ;
//	
//	private SequenceGeneratorInDbModel sequenceGenerator = null ;
	
	//-----------------------------------------------------------------------------
//	public AttributeInFakeModel() {
//		super();
//	}
	public AttributeInFakeModel(String name, String neutralType) {
		super();
		this._sName = name ;
		this._sNeutralType = neutralType ;
	}
	
//	public GeneratedValueInDbModel getGeneratedValue() {
//		return generatedValue;
//	}
//
//	public void setGeneratedValue(GeneratedValueInDbModel generatedValue) {
//		this.generatedValue = generatedValue;
//	}
//
//	public TableGeneratorInDbModel getTableGenerator() {
//		return tableGenerator;
//	}
//
//	public void setTableGenerator(TableGeneratorInDbModel tableGenerator) {
//		this.tableGenerator = tableGenerator;
//	}
//
//	public SequenceGeneratorInDbModel getSequenceGenerator() {
//		return sequenceGenerator;
//	}
//
//	public void setSequenceGenerator(SequenceGeneratorInDbModel sequenceGenerator) {
//		this.sequenceGenerator = sequenceGenerator;
//	}

	//-----------------------------------------------------------------------------
	@Override
	public String getDatabaseName() {
		return _sDatabaseName ;
	}

	public void setDatabaseName(String name) {
		_sDatabaseName = name ;
	}

	//-----------------------------------------------------------------------------

//	public void setPrimaryKey(boolean b) {
//		_bPrimaryKey = b ;
//	}
//	public boolean isPrimaryKey() {
//		return _bPrimaryKey ;
//	}
	public void setKeyElement(boolean b) { // v 3.0.0
		_bKeyElement = b ;
	}
	@Override
	public boolean isKeyElement() { // v 3.0.0
		return _bKeyElement ;
	}

	//-----------------------------------------------------------------------------
//	public void setForeignKey(boolean b) {
//		_bForeignKey = b ;
//	}
//	public boolean isForeignKey() {
//		return _bForeignKey ;
//	}
//	@Override
//	public boolean isUsedInForeignKey() { // v 3.0.0
//		return _bForeignKey;
//	}
	@Override
	public boolean isFK() {
		return _bForeignKeySimple || _bForeignKeyComposite ;
	}

	public void setFKSimple(boolean flag) {
		_bForeignKeySimple = flag ;
	}
	@Override
	public boolean isFKSimple() {
		return _bForeignKeySimple;
	}

	public void setFKComposite(boolean flag) {
		_bForeignKeyComposite = flag ;
	}
	@Override
	public boolean isFKComposite() {
		return _bForeignKeyComposite;
	}
	
	//-----------------------------------------------------------------------------

	public void setAutoIncremented(boolean b) {
		_bAutoIncremented = b ;
	}

	@Override
	public boolean isAutoIncremented() {
		return _bAutoIncremented ;
	}

	//-----------------------------------------------------------------------------

	public void setDatabaseNotNull(boolean flag) {
		_bDatabaseNotNull = flag ;
	}

	public void setDatabaseNotNull(String flag) {
		_bDatabaseNotNull = "true".equalsIgnoreCase(flag) ;
	}

	@Override
	public boolean isDatabaseNotNull() {
		return _bDatabaseNotNull ;
	}

//	public String getDatabaseNotNullAsString() {
//		return ( _bDatabaseNotNull ? "true" : "false" ) ;
//	}

	//-----------------------------------------------------------------------------
	public void setDatabaseSize(int size) {
		_iDatabaseSize = size ;
	}
	@Override
	public Integer getDatabaseSize() {
		return _iDatabaseSize ;
	}

//	//-----------------------------------------------------------------------------
//	/**
//	 * Returns the ordinal position of the column in the database table
//	 * @param v
//	 */
//	public void setDatabasePosition(int v) { // #LGU 10/08/2011
//		_iDatabasePosition = v ;
//	}
//	/**
//	 * Set the ordinal position of the column in the database table
//	 * @return
//	 */
//	public int getDatabasePosition() { // #LGU 10/08/2011
//		return _iDatabasePosition ;
//	}
	
	//-----------------------------------------------------------------------------
//	/**
//	 * Returns the database default value 
//	 * @return
//	 */
	@Override
	public String getDatabaseDefaultValue() { // #LGU 10/08/2011
		return _sDatabaseDefaultValue;
	}

	/**
	 * Set the database default value 
	 * @param v
	 */
	public void setDatabaseDefaultValue(String v) { // #LGU 10/08/2011
		_sDatabaseDefaultValue = v;
	}

	//-----------------------------------------------------------------------------

//	/**
//	 * Returns the column comment 
//	 * @return comment
//	 */
	@Override
	public String getDatabaseComment() {
		return _sDatabaseComment;
	}

	/**
	 * Set the column comment
	 * @param databaseComment comment
	 */
	public void setDatabaseComment(String databaseComment) {
		_sDatabaseComment = databaseComment;
	}
	
	//-----------------------------------------------------------------------------

	@Override
	public Integer getJdbcTypeCode() {
		return _iJdbcTypeCode ;
	}

	public void setJdbcTypeCode(int typeCode) {
		_iJdbcTypeCode = typeCode ;
	}

	@Override
	public String getJdbcTypeName() {
		//String text = _jdbcTypes.getTextForCode( getJdbcTypeCode() );
		String text = JdbcTypesManager.getJdbcTypes().getTextForCode( getJdbcTypeCode() );
		return text != null ? text : "???" ;
	}

//	public String getJdbcTypeCodeWithText() {
//		int code = getJdbcTypeCode();
//		//String text = _jdbcTypes.getTextForCode(code);
//		String text = JdbcTypesManager.getJdbcTypes().getTextForCode( code );
//		if ( text == null ) text = "???" ;
//		return code + " : " + text.toLowerCase() ;
//	}

	//-----------------------------------------------------------------------------

	/**
     * Returns the database native type name <br>
     * Examples : INTEGER, VARCHAR, NUMBER, CHAR, etc... 
	 * @return
	 */
//	public String getDatabaseTypeName() {
//		return _sDatabaseTypeName;
//	}
	@Override
	public String getDatabaseType() { // ver 3.0.0
		return _sDatabaseTypeName;
	}
	
//	/**
//     * Returns the database native type name with its size if the size make sense.<br>
//     * Examples : INTEGER, VARCHAR(24), NUMBER, CHAR(3), etc... 
//	 * @return
//	 */
//	public String getDatabaseTypeNameWithSize() {
//		return DatabaseUtil.getNativeTypeWithSize(_sDatabaseTypeName, _iDatabaseSize, _iJdbcTypeCode);
//	}

	public void setDatabaseTypeName(String databaseTypeName) {
		_sDatabaseTypeName = databaseTypeName;
	}

	//-----------------------------------------------------------------------------

//	public String getJavaName() {
//		return _sJavaName;
//	}
//	public void setJavaName(String s) {
//		_sJavaName = s ;
//	}

	@Override
	public String getName() { // v 3.0.0
		return _sName;
	}
//	public void setName(String s) { // v 3.0.0
//		_sName = s ;
//	}
	
	//-----------------------------------------------------------------------------
	
//	/**
//	 * Returns the primitive Java type or the full java class name ( with package )
//	 * e.g. : "boolean", "java.lang.Boolean", "java.util.Date", etc...
//	 * @return
//	 */
////	public String getJavaType() {
//	@Override
//	public String getFullType() { // v 3.0.0
//		
//		String sType = _sFullType ;
//		
//		//--- Backward compatibility with old repository
//		
//		JavaTypes javaTypes = JavaTypesManager.getJavaTypes();
//		if ( javaTypes.getTypeIndex(sType) < 0 )
//		{
//			// Type NOT FOUND : may be a short type ( from an old repository ) 
//			return javaTypes.getTypeForShortType(sType); 
//		}
//		else
//		{
//			// Type found => Type OK
//			return sType ;
//		}
//	}
	//-----------------------------------------------------------------------------
	@Override
	public String getNeutralType() { // v 3.0.0
//		TypeReverser typeReverser = TypeReverser.getInstance() ;
//		return typeReverser.getNeutralType(_sFullType, dateType) ;
		return _sNeutralType ;
	}
	//-----------------------------------------------------------------------------

////	public void setJavaType(String s) {
//	public void setFullType(String s) { // v 3.0.0
//		_sFullType = s ;
//	}

//	@Override
//	public String getSimpleType() {
//		// TODO Auto-generated method stub
//		// TODO : really useful ?
//		return null;
//	}

	//-----------------------------------------------------------------------------
//	/**
//	 * Returns the Java bean default value if any 
//	 * @return the default value ( "0", "false" ) or null if none
//	 */
//	public String getJavaDefaultValue() {
//		return _sJavaDefaultValue ;
//	}
	@Override
	public String getDefaultValue() { // ver 3.0.0
		return _sJavaDefaultValue ;
	}
	/**
	 * Set the default value for the attribute
	 * @param s the default value ( eg : "0", "false" )
	 */
//	public void setJavaDefaultValue(String s) {
//		_sJavaDefaultValue = s ;
//	}
	public void setDefaultValue(String s) {
		_sJavaDefaultValue = s ;
	}

	//-----------------------------------------------------------------------------
	@Override
	public String getInitialValue() { // v 3.0.0
		return null; // Not yet implemented 
	}

//	//-----------------------------------------------------------------------------
//	/**
//	 * Returns true is the Java type is "boolean" or "java.lang.Boolean"
//	 * @return
//	 */
//	public boolean isJavaTypeBoolean() 
//	{
//		//return JavaTypeUtil.isCategoryBoolean( getJavaType() ) ;
//		return JavaTypeUtil.isCategoryBoolean( getFullType() ) ;  // v 3.0.0
//	}
	
//	/**
//	 * Returns true is the Java type is "java.lang.String"
//	 * @return
//	 */
//	public boolean isJavaTypeString() 
//	{
//		//return JavaTypeUtil.isCategoryString( getJavaType() ) ;
//		return JavaTypeUtil.isCategoryString( getFullType() ) ; // v 3.0.0
//	}

//	/**
//	 * Returns true if the Java type is a numeric type : <br>
//	 * "byte", "short", "int", "long", "double", "float" <br>
//	 * or respective wrappers, or "BigDecimal", or "BigInteger"<br>
//	 * @return
//	 */
//	public boolean isJavaTypeNumber() 
//	{
//		//return JavaTypeUtil.isCategoryNumber( getJavaType() ) ;
//		return JavaTypeUtil.isCategoryNumber( getFullType() ) ; // v 3.0.0
//	}
	
//	/**
//	 * Returns true if the Java type is "java.util.Date" or "java.sql.Date" <br>
//	 * or "java.sql.Time" or "java.sql.Timestamp" <br>
//	 * @return
//	 */
//	public boolean isJavaTypeDateOrTime() 
//	{
//		//return JavaTypeUtil.isCategoryDateOrTime( getJavaType() ) ;
//		return JavaTypeUtil.isCategoryDateOrTime( getFullType() ) ; // v 3.0.0
//	}
	
//	/**
//	 * Returns true if the Java type is a "primitive type" ( "int", "boolean", "short", ... )
//	 * @return
//	 */
//	public boolean isJavaPrimitiveType()
//	{
//		//return JavaTypeUtil.isPrimitiveType( getJavaType() );
//		return JavaTypeUtil.isPrimitiveType( getFullType() ); // v 3.0.0
//	}

	//-----------------------------------------------------------------------------
//	public boolean getJavaNotNull() {
//		return _bJavaNotNull;
//	}
	@Override
	public boolean isNotNull() { // v 3.0.0
		return _bNotNull;
	}
//	public void setJavaNotNull(boolean v) {
	public void setNotNull(boolean v) {  // v 3.0.0
		_bNotNull = v ;
	}

	//-----------------------------------------------------------------------------
//	public boolean getNotEmpty() {
//		return _bNotEmpty;
//	}
	@Override
	public boolean isNotEmpty() { // v 3.0.0
		return _bNotEmpty;
	}
	public void setNotEmpty(boolean v) {
		_bNotEmpty = v ;
	}

	//-----------------------------------------------------------------------------
//	public boolean getNotBlank() {
//		return _bNotBlank;
//	}
	@Override
	public boolean isNotBlank() { // v 3.0.0
		return _bNotBlank;
	}

	public void setNotBlank(boolean v) {
		_bNotBlank = v ;
	}
	//-----------------------------------------------------------------------------
//	public String getMinLength() {
//		return _sMinLength;
//	}
//	public void setMinLength(String v) {
//		_sMinLength = v ;
//	}
	@Override
	public Integer getMinLength() { // ver 3.0.0
		return _iMinLength;
	}
	public void setMinLength(Integer v) { // ver 3.0.0
		_iMinLength = v ;
	}
	//-----------------------------------------------------------------------------
//	public String getMaxLength() {
//		return _sMaxLength;
//	}
//	public void setMaxLength(String v) {
//		_sMaxLength = v ;
//	}
	@Override
	public Integer getMaxLength() { // ver 3.0.0
		return _iMaxLength;
	}
	public void setMaxLength(Integer v) { // ver 3.0.0
		_iMaxLength = v ;
	}
	//-----------------------------------------------------------------------------
	public String getPattern() {
		return _sPattern;
	}
	public void setPattern(String v) {
		_sPattern = v ;
	}
	//-----------------------------------------------------------------------------
//	public boolean getSelected() {
//		return _bSelected ;
//	}
	@Override
	public boolean isSelected() { // v 3.0.0
		return _bSelected ;
	}	
	public void setSelected(boolean b) {
		_bSelected = b ;
	}

	//-----------------------------------------------------------------------------
	// Special infos
	//-----------------------------------------------------------------------------
	@Override
	public String getLabel() {  // V 2.0.3
		return _sLabel ;
	}
	public void setLabel(String s) { // V 2.0.3
		_sLabel = s ;
	}
	
	//-----------------------------------------------------------------------------
	@Override
	public String getInputType() { // V 2.0.3
		return _sInputType ;
	}
	public void setInputType(String s) { // V 2.0.3
		_sInputType = s ;
	}
	
	//-----------------------------------------------------------------------------
//	public boolean getLongText() {
//		return _bLongText ;
//	}
	@Override
	public boolean isLongText() { // v 3.0.0
		return _bLongText ;
	}	
	public void setLongText(String flag) {
		setLongText( "true".equalsIgnoreCase(flag) ) ;
	}
	public void setLongText(boolean b) {
		_bLongText = b ;
	}

	//-----------------------------------------------------------------------------
//	/**
//	 * Returns the special date type : "D", "T", "DT" or "" if none
//	 * @return
//	 */
//	public String getDateType() {
//		return ( _sDateType != null ? _sDateType : "" ); 
//	}
	@Override
	public DateType getDateType() {
		return dateType ; 
	}

	/**
	 * Set the special date type
	 * @param v : "D", "T", "DT" or null if none
	 */
//	public void setDateType(String v) {
//		if ( SPECIAL_DATE_ONLY.equals(v) || SPECIAL_TIME_ONLY.equals(v) 
//				|| SPECIAL_DATE_AND_TIME.equals(v) || null == v )
//		{
//			_sDateType = v ;
//		}
//	}
	/**
	 * Set the date type : DATE ONLY, TIME ONLY, DATE AND TIME
	 * @param v
	 */
	public void setDateType(DateType v) {
		dateType = v ;
	}
	
	@Override
	public boolean isDatePast() {
		return _bDatePast;
	}
	public void setDatePast(boolean v) {
		_bDatePast = v;
	}

	@Override
	public boolean isDateFuture() {
		return _bDateFuture;
	}
	public void setDateFuture(boolean v) {
		_bDateFuture = v;
	}

	@Override
	public boolean isDateBefore() {
		return _bDateBefore;
	}
	public void setDateBefore(boolean v) {
		_bDateBefore = v;
	}
	public String getDateBeforeValue() {
		return _sDateBeforeValue;
	}
	public void setDateBeforeValue(String v) {
		_sDateBeforeValue = v;
	}

	public boolean isDateAfter() {
		return _bDateAfter;
	}
	public void setDateAfter(boolean v) {
		_bDateAfter = v;
	}
	public String getDateAfterValue() {
		return _sDateAfterValue;
	}
	public void setDateAfterValue(String v) {
		_sDateAfterValue = v;
	}
	//-----------------------------------------------------------------------------

	/**
	 * The value used to store a TRUE in the database ( never null )
	 * @return the value or "" if none (never null)
	 */
	public String getBooleanTrueValue() {
		return ( _sBooleanTrueValue != null ? _sBooleanTrueValue : "" );
	}
	/**
	 * The value used to store a FALSE in the database ( never null )
	 * @return the value or "" if none (never null)
	 */
	public String getBooleanFalseValue() {
		return ( _sBooleanFalseValue != null ? _sBooleanFalseValue : "" );
	}

	public void setBooleanTrueValue(String v) {
		_sBooleanTrueValue = v ;
	}
	public void setBooleanFalseValue(String v) {
		_sBooleanFalseValue = v ;
	}

	//-----------------------------------------------------------------------------

//	public String getFormat() {
//		return _sFormat ; 
//	}
//	public void setFormat(String v) {
//		_sFormat = v ;
//	}
//	
	//-----------------------------------------------------------------------------

//	public Integer getMinValue() { // ver 3.0.0
	public BigDecimal getMinValue() { // ver 3.0.0
		return _iMinValue ; 
	}
//	public void setMinValue(Integer v) { // ver 3.0.0
	public void setMinValue(BigDecimal v) { // ver 3.0.0
		_iMinValue = v ;
	}
	
//	public Integer getMaxValue() { // ver 3.0.0
	public BigDecimal getMaxValue() { // ver 3.0.0
		return _iMaxValue ; 
	}
//	public void setMaxValue(Integer v) { // ver 3.0.0
	public void setMaxValue(BigDecimal v) { // ver 3.0.0
		_iMaxValue = v ;
	}
	
	//-----------------------------------------------------------------------------

//	/**
//	 * Returns the "special type informations" for this column if any ( else "", never null )
//	 * @return : Special information, ie "Long Text", "Date only", "Time only", boolean true/false value
//	 */
//	public String getSpecialTypeInfo() 
//	{
////		String sJavaType = getJavaType();
////		if ( sJavaType != null )
////		{
////			if ( "java.lang.String".equals(sJavaType) ) {
////				if ( getLongText() ) return "Long Text" ;
////			}
////			if ( "java.lang.Boolean".equals(sJavaType) || "boolean".equals(sJavaType) ) {
////				return getBooleanTrueValue() + ":" + getBooleanFalseValue() ;
////			}
////			if ( "java.util.Date".equals(sJavaType) ) {
////				String sDateType = getDateType() ;
////				if ( SPECIAL_DATE_ONLY.equals(sDateType) )     return "Date only" ;
////				if ( SPECIAL_TIME_ONLY.equals(sDateType) )     return "Time only" ;
////				if ( SPECIAL_DATE_AND_TIME.equals(sDateType) ) return "Date + Time" ;
////			}
////		}
//		
//		StringBuffer sb = new StringBuffer();
//		if ( this.isJavaTypeString() ) {
//			//if ( getLongText() ) addStr(sb, "Long Text") ;
//			if ( isLongText() ) addStr(sb, "Long Text") ; // v 3.0.0
//			//if ( getNotEmpty() ) addStr(sb, "NE") ;
//			if ( isNotEmpty() ) addStr(sb, "NE") ; // v 3.0.0
//			//if ( getNotBlank() ) addStr(sb, "NB") ;
//			if ( isNotBlank() ) addStr(sb, "NB") ; // v 3.0.0
//			//if ( ( ! StrUtil.nullOrVoid( getMinLength() ) ) || ( ! StrUtil.nullOrVoid( getMaxLength() ) ) )
//			if ( ( getMinLength() != null ) || ( getMaxLength() != null ) )
//			{
//				//addStr( sb, "[" + str(getMinLength()) + ";" + str(getMaxLength()) + "]" );
//				addStr( sb, "[" + getMinLength() + ";" + getMaxLength() + "]" );
//			}
//			if ( ! StrUtil.nullOrVoid( getPattern() ) ) addStr(sb, "P" ) ;
//		}
////		else if ( this.isJavaTypeBoolean() ) {
////			if ( ! StrUtil.nullOrVoid( getBooleanTrueValue() ) ) {
////				addStr( sb, getBooleanTrueValue() + ":" + getBooleanFalseValue() );
////			}
////		}
//		else if ( this.isJavaTypeNumber() ) {
//			if ( ! StrUtil.nullOrVoid( getDefaultValue() ) )
//			{
//				addStr( sb, getDefaultValue() );
//			}
//			//if ( ( ! StrUtil.nullOrVoid( getMinValue() ) ) || ( ! StrUtil.nullOrVoid( getMaxValue() ) ) )
//			if ( ( getMinValue() != null ) || ( getMaxValue() != null ) )
//			{
//				//addStr( sb, "[" + str(getMinValue()) + ";" + str(getMaxValue()) + "]" );
//				addStr( sb, "[" + getMinValue() + ";" + getMaxValue() + "]" );
//			}
//		}
//		else if ( this.isJavaTypeDateOrTime() ) {
//			//String sDateType = getDateType() ;
//			//if ( SPECIAL_DATE_ONLY.equals(sDateType) )     addStr( sb, "Date only" );
//			//if ( SPECIAL_TIME_ONLY.equals(sDateType) )     addStr( sb, "Time only" );
//			//if ( SPECIAL_DATE_AND_TIME.equals(sDateType) ) addStr( sb, "Date & Time" );
//			DateType dateType = getDateType();
//			if ( dateType == DateType.DATE_ONLY )     addStr( sb, "Date only" );
//			if ( dateType == DateType.TIME_ONLY )     addStr( sb, "Time only" );
//			if ( dateType == DateType.DATE_AND_TIME ) addStr( sb, "Date & Time" );
//			
//			if ( isDatePast() ) addStr( sb, "P" ); 
//			if ( isDateFuture() ) addStr( sb, "F" ); 
//			if ( isDateBefore() ) addStr( sb, "B" ); 
//			if ( isDateAfter() ) addStr( sb, "A" ); 
//		}
//		return sb.toString();
//	}
//	private String str(String s)
//	{
//		return s != null ? s : "" ;
//	}
//	private void addStr(StringBuffer sb, String s)
//	{
//		if ( sb.length() > 0 ) sb.append(",");
//		sb.append(s);
//	}
	
	/**
	 * Clear all the "special type informations" for this column
	 */
	public void clearSpecialTypeInfo() 
	{
		//setJavaNotNull(false);
		setNotNull(false); // v 3.0.0
		//--- Boolean category 
		setBooleanTrueValue(null);
		setBooleanFalseValue(null);
		//--- Date category 
		setDateType(null);
		setDatePast(false);
		setDateFuture(false);
		setDateBefore(false);
		setDateBeforeValue(null);
		setDateAfter(false);
		setDateAfterValue(null);
		//--- Number category 
		setMinValue(null);
		setMaxValue(null);
		//--- String category 
		setLongText(false);
		setNotEmpty(false);
		setNotBlank(false);
		setMinLength(null);
		setMaxLength(null);
		setPattern(null);
	}
	
	//public int compareTo(Object o) {
	public int compareTo(AttributeInFakeModel other) {
//		if ( other != null )
//		{
//			//Column other = (Column) o;
//			return ( this.getDatabasePosition() - other.getDatabasePosition() );
//		}
		return 0;
	}

	//-----------------------------------------------------------------------------

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return 
			getName() + "|" // added in v 3.0.0
			+ "table:" + getDatabaseName() + "|" 
			//+ ( isPrimaryKey() ? "PK" : "" ) + "|" 
			+ ( isKeyElement() ? "PK" : "" ) + "|" // v 3.0.0
			// + getDatabaseType() + "|" 
			+ getJdbcTypeCode() + "|" 
			//+ getJavaName() + "|" 
			//+ getName() + "|" // v 3.0.0 
			// + getJavaType() ;
			+ getNeutralType() ; // v 3.0.0
	}

	//---------------------------------------------------------------------------------------------------
	// Methods added in ver 3.0.0 in order to be compliant with the "generic model"
	//---------------------------------------------------------------------------------------------------
	@Override
	public boolean isGeneratedValue() {
//        if ( this.isAutoIncremented() ) {
//        	return true ; 
//        } 
//        else if (this.getGeneratedValue() != null) {
//        	return true ; 
//        }
    	return false ; 
	}

	@Override
	public String getGeneratedValueGenerator() {
//        if ( this.isAutoIncremented() ) {
//        	return null ;
//        } 
//        else {
//			if (this.getGeneratedValue() != null) {
//				return this.getGeneratedValue().getGenerator();
//			}
//			else {
//	        	return null ;
//			}
//        }
    	return null ;
	}

	@Override
	public String getGeneratedValueStrategy() {
//		// e.g : 'auto', 'identity', 'sequence', 'table' 
//        if ( this.isAutoIncremented() ) {
//        	return null ; // "AUTO" is the default strategy
//        } 
//        else {
//			if (this.getGeneratedValue() != null) {
//				return this.getGeneratedValue().getStrategy();
//			}
//			else {
//	        	return null ;
//			}
//        }
    	return null ;
	}

	//---------------------------------------------------------------------------------------------------
	// Sequence generator information
	//---------------------------------------------------------------------------------------------------
	@Override
	public boolean hasSequenceGenerator() {
//		return this.sequenceGenerator != null ;
		return false ;
	}

	@Override
	public Integer getSequenceGeneratorAllocationSize() {
//		if (this.sequenceGenerator != null) {
//			return this.sequenceGenerator.getAllocationSize() ;
//		}
		return null;
	}

	@Override
	public String getSequenceGeneratorName() {
//		if (this.sequenceGenerator != null) {
//			return this.sequenceGenerator.getName() ;
//		}
		return null;
	}

	@Override
	public String getSequenceGeneratorSequenceName() {
//		if (this.sequenceGenerator != null) {
//			return this.sequenceGenerator.getSequenceName() ;
//		}
		return null;
	}

	//---------------------------------------------------------------------------------------------------
	// Table generator information
	//---------------------------------------------------------------------------------------------------
	@Override
	public boolean hasTableGenerator() {
//		return this.tableGenerator != null ;
		return false ;
	}

	@Override
	public String getTableGeneratorName() {
//		if ( this.tableGenerator != null ) {
//			return this.tableGenerator.getName() ;
//		}
		return null;
	}

	@Override
	public String getTableGeneratorPkColumnName() {
//		if ( this.tableGenerator != null ) {
//			return this.tableGenerator.getPkColumnName() ;
//		}
		return null;
	}

	@Override
	public String getTableGeneratorPkColumnValue() {
//		if ( this.tableGenerator != null ) {
//			return this.tableGenerator.getPkColumnValue() ;
//		}
		return null;
	}

	@Override
	public String getTableGeneratorTable() {
//		if ( this.tableGenerator != null ) {
//			return this.tableGenerator.getTable() ;
//		}
		return null;
	}

	@Override
	public String getTableGeneratorValueColumnName() {
//		if ( this.tableGenerator != null ) {
//			return this.tableGenerator.getValueColumnName() ;
//		}
		return null;
	}

	@Override
	public boolean isObjectTypeExpected() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPrimitiveTypeExpected() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSqlTypeExpected() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isUnsignedTypeExpected() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getReferencedEntityClassName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isUsedInLinks() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isUsedInSelectedLinks() {
		// TODO Auto-generated method stub
		return false;
	}

	//---------------------------------------------------------------------------------------------------
}
