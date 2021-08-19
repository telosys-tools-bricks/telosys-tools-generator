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

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.telosys.tools.commons.jdbctypes.JdbcTypesManager;
import org.telosys.tools.generic.model.Attribute;
import org.telosys.tools.generic.model.BooleanValue;
import org.telosys.tools.generic.model.DateType;
import org.telosys.tools.generic.model.ForeignKeyPart;

/**
 * Fake attribute for fake entity <br>
 * 
 * @author Laurent Guerin
 *
 */
public class FakeAttribute implements Attribute {

	// ----- DATABASE -----

	private String _sDatabaseName = null; // dbName=""

	private String _sDatabaseTypeName = null; // dbTypeName="INTEGER" -
												// dbTypeName="VARCHAR"

	private String databaseSize = null; // dbSize=""

	private boolean _bDatabaseNotNull = false; // dbNotNull="true|false" ( false
												// by default )

	private boolean _bKeyElement = false; // primaryKey="true|false" ( false by
											// default ) // v 3.0.0

	private boolean _bForeignKeySimple = false; // ( false by default )
	private boolean _bForeignKeyComposite = false; // ( false by default )

	private boolean _bAutoIncremented = false; // autoIncremented="true|false" (
												// false by default )

	private String _sDatabaseDefaultValue = null; // dbDefaultValue="" (
													// database default value )
													// #LGU 10/08/2011

	private String _sDatabaseComment = null; // comment=""

	// ----- JDBC -----

	private int _iJdbcTypeCode = 0; // dbTypeCode="4" - dbTypeCode="12"

	// ----- JAVA -----

	private String _sName;

	private final String _sNeutralType; // v 3.0.0

	private boolean _bNotNull = false; // javaNotNull="true|false"

	private String _sJavaDefaultValue = null; // javaDefaultValue="..."

	private boolean _bSelected = true; // selected by default

	// ----- SPECIAL DATA for ALL -----
	private String _sLabel = null;
	private String _sInputType = null;

	// ----- SPECIAL DATA for STRING -----
	private boolean _bLongText = false; //
	private boolean _bNotEmpty = false; // notEmpty="true|false"
	private boolean _bNotBlank = false; // notBlank="true|false"
	private Integer _iMinLength = null;
	private Integer _iMaxLength = null;
	private String _sPattern = null;

	// ----- SPECIAL DATA for DATE & TIME -----
	private DateType dateType = null; // enumeration - ver 3.0.0
	private boolean _bDatePast = false;
	private boolean _bDateFuture = false;
	private boolean _bDateBefore = false;
	private boolean _bDateAfter = false;
	private String _sDateBeforeValue = null;
	private String _sDateAfterValue = null;

	// ----- SPECIAL DATA for NUMERIC -----
	private BigDecimal _iMinValue = null;
	private BigDecimal _iMaxValue = null;

	// ----- SPECIAL DATA for BOOLEAN -----
	private String _sBooleanTrueValue = null; // the special value for TRUE
	private String _sBooleanFalseValue = null; // the special value for FALSE

	private boolean isTransient = false; // v 3.3.0

	/**
	 * Constructor
	 * @param name
	 * @param neutralType
	 * @param keyElement
	 */
	public FakeAttribute(String name, String neutralType, boolean keyElement) {
		super();
		this._sName = name;
		this._sNeutralType = neutralType;
		this._bKeyElement = keyElement ;
	}

	// -----------------------------------------------------------------------------
	@Override
	public String getDatabaseName() {
		return _sDatabaseName;
	}

	public void setDatabaseName(String name) {
		_sDatabaseName = name;
	}

	// -----------------------------------------------------------------------------
	@Override
	public boolean isKeyElement() { // v 3.0.0
		return _bKeyElement;
	}

	// -----------------------------------------------------------------------------
	@Override
	public boolean isFK() {
		return _bForeignKeySimple || _bForeignKeyComposite;
	}

	public void setFKSimple(boolean flag) {
		_bForeignKeySimple = flag;
	}

	@Override
	public boolean isFKSimple() {
		return _bForeignKeySimple;
	}

	public void setFKComposite(boolean flag) {
		_bForeignKeyComposite = flag;
	}

	@Override
	public boolean isFKComposite() {
		return _bForeignKeyComposite;
	}

	// -----------------------------------------------------------------------------

	public void setAutoIncremented(boolean b) {
		_bAutoIncremented = b;
	}

	@Override
	public boolean isAutoIncremented() {
		return _bAutoIncremented;
	}

	// -----------------------------------------------------------------------------

	public void setDatabaseNotNull(boolean flag) {
		_bDatabaseNotNull = flag;
	}

	public void setDatabaseNotNull(String flag) {
		_bDatabaseNotNull = "true".equalsIgnoreCase(flag);
	}

	@Override
	public boolean isDatabaseNotNull() {
		return _bDatabaseNotNull;
	}

	// -----------------------------------------------------------------------------
	public void setDatabaseSize(String size) {
		databaseSize = size;
	}

	@Override
	public String getDatabaseSize() {
		return databaseSize;
	}

	// -----------------------------------------------------------------------------
	@Override
	public String getDatabaseDefaultValue() { // #LGU 10/08/2011
		return _sDatabaseDefaultValue;
	}

	/**
	 * Set the database default value
	 * 
	 * @param v
	 */
	public void setDatabaseDefaultValue(String v) { // #LGU 10/08/2011
		_sDatabaseDefaultValue = v;
	}

	// -----------------------------------------------------------------------------
	@Override
	public String getDatabaseComment() {
		return _sDatabaseComment;
	}

	/**
	 * Set the column comment
	 * 
	 * @param databaseComment
	 *            comment
	 */
	public void setDatabaseComment(String databaseComment) {
		_sDatabaseComment = databaseComment;
	}

	// -----------------------------------------------------------------------------

	@Override
	public Integer getJdbcTypeCode() {
		return _iJdbcTypeCode;
	}

	public void setJdbcTypeCode(int typeCode) {
		_iJdbcTypeCode = typeCode;
	}

	@Override
	public String getJdbcTypeName() {
		String text = JdbcTypesManager.getJdbcTypes().getTextForCode(getJdbcTypeCode());
		return text != null ? text : "???";
	}

	// -----------------------------------------------------------------------------

	/**
	 * Returns the database native type name <br>
	 * Examples : INTEGER, VARCHAR, NUMBER, CHAR, etc...
	 * 
	 * @return
	 */
	@Override
	public String getDatabaseType() { // ver 3.0.0
		return _sDatabaseTypeName;
	}

	public void setDatabaseType(String databaseTypeName) {
		_sDatabaseTypeName = databaseTypeName;
	}

	// -----------------------------------------------------------------------------
	@Override
	public String getName() { // v 3.0.0
		return _sName;
	}

	public void setName(String s) { // v 3.0.0
		_sName = s;
	}

	// -----------------------------------------------------------------------------

	// -----------------------------------------------------------------------------
	@Override
	public String getNeutralType() { // v 3.0.0
		return _sNeutralType;
	}

	// -----------------------------------------------------------------------------
	@Override
	public String getDefaultValue() { // ver 3.0.0
		return _sJavaDefaultValue;
	}

	/**
	 * Set the default value for the attribute
	 * 
	 * @param s
	 *            the default value ( eg : "0", "false" )
	 */
	public void setDefaultValue(String s) {
		_sJavaDefaultValue = s;
	}

	// -----------------------------------------------------------------------------
	@Override
	public String getInitialValue() { // v 3.0.0
		return null; // Not yet implemented
	}

	// -----------------------------------------------------------------------------
	@Override
	public boolean isNotNull() { // v 3.0.0
		return _bNotNull;
	}

	public void setNotNull(boolean v) { // v 3.0.0
		_bNotNull = v;
	}

	// -----------------------------------------------------------------------------
	@Override
	public boolean isNotEmpty() { // v 3.0.0
		return _bNotEmpty;
	}

	public void setNotEmpty(boolean v) {
		_bNotEmpty = v;
	}

	// -----------------------------------------------------------------------------
	@Override
	public boolean isNotBlank() { // v 3.0.0
		return _bNotBlank;
	}

	public void setNotBlank(boolean v) {
		_bNotBlank = v;
	}

	// -----------------------------------------------------------------------------
	@Override
	public Integer getMinLength() { // ver 3.0.0
		return _iMinLength;
	}

	public void setMinLength(Integer v) { // ver 3.0.0
		_iMinLength = v;
	}

	// -----------------------------------------------------------------------------
	@Override
	public Integer getMaxLength() { // ver 3.0.0
		return _iMaxLength;
	}

	public void setMaxLength(Integer v) { // ver 3.0.0
		_iMaxLength = v;
	}

	// -----------------------------------------------------------------------------
	public String getPattern() {
		return _sPattern;
	}

	public void setPattern(String v) {
		_sPattern = v;
	}

	// -----------------------------------------------------------------------------
	@Override
	public boolean isSelected() { // v 3.0.0
		return _bSelected;
	}

	public void setSelected(boolean b) {
		_bSelected = b;
	}

	// -----------------------------------------------------------------------------
	// Special infos
	// -----------------------------------------------------------------------------
	@Override
	public String getLabel() { // V 2.0.3
		return _sLabel;
	}

	public void setLabel(String s) { // V 2.0.3
		_sLabel = s;
	}

	// -----------------------------------------------------------------------------
	@Override
	public String getInputType() { // V 2.0.3
		return _sInputType;
	}

	public void setInputType(String s) { // V 2.0.3
		_sInputType = s;
	}

	// -----------------------------------------------------------------------------
	@Override
	public boolean isLongText() { // v 3.0.0
		return _bLongText;
	}

	public void setLongText(String flag) {
		setLongText("true".equalsIgnoreCase(flag));
	}

	public void setLongText(boolean b) {
		_bLongText = b;
	}

	// -----------------------------------------------------------------------------
	@Override
	public DateType getDateType() {
		return dateType;
	}

	/**
	 * Set the date type : DATE ONLY, TIME ONLY, DATE AND TIME
	 * 
	 * @param v
	 */
	public void setDateType(DateType v) {
		dateType = v;
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
	// -----------------------------------------------------------------------------

	/**
	 * The value used to store a TRUE in the database ( never null )
	 * 
	 * @return the value or "" if none (never null)
	 */
	public String getBooleanTrueValue() {
		return (_sBooleanTrueValue != null ? _sBooleanTrueValue : "");
	}

	/**
	 * The value used to store a FALSE in the database ( never null )
	 * 
	 * @return the value or "" if none (never null)
	 */
	public String getBooleanFalseValue() {
		return (_sBooleanFalseValue != null ? _sBooleanFalseValue : "");
	}

	public void setBooleanTrueValue(String v) {
		_sBooleanTrueValue = v;
	}

	public void setBooleanFalseValue(String v) {
		_sBooleanFalseValue = v;
	}

	// -----------------------------------------------------------------------------

	public BigDecimal getMinValue() { // ver 3.0.0
		return _iMinValue;
	}

	public void setMinValue(BigDecimal v) { // ver 3.0.0
		_iMinValue = v;
	}

	public BigDecimal getMaxValue() { // ver 3.0.0
		return _iMaxValue;
	}

	public void setMaxValue(BigDecimal v) { // ver 3.0.0
		_iMaxValue = v;
	}

	// -----------------------------------------------------------------------------
	/**
	 * Clear all the "special type informations" for this column
	 */
	public void clearSpecialTypeInfo() {
		setNotNull(false); // v 3.0.0
		// --- Boolean category
		setBooleanTrueValue(null);
		setBooleanFalseValue(null);
		// --- Date category
		setDateType(null);
		setDatePast(false);
		setDateFuture(false);
		setDateBefore(false);
		setDateBeforeValue(null);
		setDateAfter(false);
		setDateAfterValue(null);
		// --- Number category
		setMinValue(null);
		setMaxValue(null);
		// --- String category
		setLongText(false);
		setNotEmpty(false);
		setNotBlank(false);
		setMinLength(null);
		setMaxLength(null);
		setPattern(null);
	}

	@Override
	public String toString() {
		return getName() + "|" // added in v 3.0.0
				+ "table:" + getDatabaseName() + "|" + (isKeyElement() ? "PK" : "") + "|" // v
																							// 3.0.0
				+ getJdbcTypeCode() + "|" + getNeutralType(); // v 3.0.0
	}

	// ---------------------------------------------------------------------------------------------------
	// Methods added in ver 3.0.0 in order to be compliant with the "generic
	// model"
	// ---------------------------------------------------------------------------------------------------
	@Override
	public boolean isGeneratedValue() {
		return false;
	}

	@Override
	public String getGeneratedValueGenerator() {
		return null;
	}

	@Override
	public String getGeneratedValueStrategy() {
		return null;
	}

	// ---------------------------------------------------------------------------------------------------
	// Sequence generator information
	// ---------------------------------------------------------------------------------------------------
	@Override
	public boolean hasSequenceGenerator() {
		return false;
	}

	@Override
	public Integer getSequenceGeneratorAllocationSize() {
		return null;
	}

	@Override
	public String getSequenceGeneratorName() {
		return null;
	}

	@Override
	public String getSequenceGeneratorSequenceName() {
		return null;
	}

	// ---------------------------------------------------------------------------------------------------
	// Table generator information
	// ---------------------------------------------------------------------------------------------------
	@Override
	public boolean hasTableGenerator() {
		return false;
	}

	@Override
	public String getTableGeneratorName() {
		return null;
	}

	@Override
	public String getTableGeneratorPkColumnName() {
		return null;
	}

	@Override
	public String getTableGeneratorPkColumnValue() {
		return null;
	}

	@Override
	public String getTableGeneratorTable() {
		return null;
	}

	@Override
	public String getTableGeneratorValueColumnName() {
		return null;
	}

	@Override
	public boolean isObjectTypeExpected() {
		return false;
	}

	@Override
	public boolean isPrimitiveTypeExpected() {
		return false;
	}

	@Override
	public boolean isUnsignedTypeExpected() {
		return false;
	}

	@Override
	public String getReferencedEntityClassName() {
		return null;
	}

	@Override
	public boolean isUsedInLinks() {
		return false;
	}

	@Override
	public boolean isUsedInSelectedLinks() {
		return false;
	}

	@Override
	public Map<String, String> getTagsMap() {
		return null;
	}

	// ----------------------------------------------------------------
	// ver 3.3.0
	private List<ForeignKeyPart> fkParts = new LinkedList<>();

	@Override
	public List<ForeignKeyPart> getFKParts() {
		return fkParts;
	}

	@Override
	public boolean hasFKParts() {
		return false;
	}

	@Override
	public BooleanValue getInsertable() {
		return BooleanValue.UNDEFINED;
	}

	@Override
	public BooleanValue getUpdatable() {
		return BooleanValue.UNDEFINED;
	}

	@Override
	public boolean isTransient() { // v 3.3.0
		return this.isTransient;
	}

	public void setTransient(boolean b) { // v 3.3.0
		this.isTransient = b;
	}
}
