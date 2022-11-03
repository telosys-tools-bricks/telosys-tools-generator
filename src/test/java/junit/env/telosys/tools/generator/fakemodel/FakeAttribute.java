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

import org.telosys.tools.generic.model.Attribute;
import org.telosys.tools.generic.model.ForeignKeyPart;
import org.telosys.tools.generic.model.TagContainer;
import org.telosys.tools.generic.model.enums.BooleanValue;
import org.telosys.tools.generic.model.enums.DateType;
import org.telosys.tools.generic.model.enums.GeneratedValueStrategy;

/**
 * Fake attribute for fake entity <br>
 * 
 * @author Laurent Guerin
 *
 */
public class FakeAttribute implements Attribute {

	// ----- DATABASE -----

	private String  databaseName = null; 
	private String  databaseTypeName = null; 
	private String  databaseSize = null; 
	private boolean databaseNotNull = false; 
	private String  databaseDefaultValue = null; 
	private String  databaseComment = null; 

	private boolean keyElement = false; 

	private boolean foreignKeySimple = false; 
	private boolean foreignKeyComposite = false; 

	private boolean autoIncremented = false; 

	// ----- JDBC -----

//	private int _iJdbcTypeCode = 0; // dbTypeCode="4" - dbTypeCode="12"

	// ----- JAVA -----

	private String name;

	private final String neutralType; // v 3.0.0

	private boolean notNull = false; // javaNotNull="true|false"

	private String defaultValue = null; // javaDefaultValue="..."

	private boolean _bSelected = true; // selected by default

	// ----- SPECIAL DATA for ALL -----
	private String label = null;
	private String inputType = null;

	// ----- SPECIAL DATA for STRING -----
	private boolean longText = false; //
	private boolean notEmpty = false; // notEmpty="true|false"
	private boolean notBlank = false; // notBlank="true|false"
	private Integer minLength = null;
	private Integer maxLength = null;
	private String  pattern = null;

	// ----- SPECIAL DATA for DATE & TIME -----
	private DateType dateType = null; // enumeration - ver 3.0.0
	private boolean  datePast = false;
	private boolean  dateFuture = false;
//	private boolean _bDateBefore = false;
//	private boolean _bDateAfter = false;
	private String dateBeforeValue = null;
	private String dateAfterValue = null;

	// ----- SPECIAL DATA for NUMERIC -----
	private BigDecimal minValue = null;
	private BigDecimal maxValue = null;

	// ----- SPECIAL DATA for BOOLEAN -----
	private String booleanTrueValue = null; // the special value for TRUE
	private String booleanFalseValue = null; // the special value for FALSE

	private GeneratedValueStrategy generatedValueStrategy = GeneratedValueStrategy.UNDEFINED ;
	private String  generatedValueGeneratorName = null ;
	private String  generatedValueSequenceName  = null ;
	private Integer generatedValueAllocationSize = 0;
	
	private boolean isTransient = false; // v 3.3.0
	
    private String  size; // String for size with comma ( eg "8,2" ) // Added in v 3.4.0
	private boolean isUnique = false ; // Added in v 3.4.0
	
	private TagContainer tagContainer = new FakeTagContainer();

	/**
	 * Constructor
	 * @param name
	 * @param neutralType
	 * @param keyElement
	 */
	public FakeAttribute(String name, String neutralType, boolean keyElement) {
		super();
		this.name = name;
		this.neutralType = neutralType;
		this.keyElement = keyElement ;
	}

	// -----------------------------------------------------------------------------
	@Override
	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String name) {
		databaseName = name;
	}

	// -----------------------------------------------------------------------------
	@Override
	public boolean isKeyElement() { // v 3.0.0
		return keyElement;
	}

	// -----------------------------------------------------------------------------
	@Override
	public boolean isFK() {
		return foreignKeySimple || foreignKeyComposite;
	}

	public void setFKSimple(boolean flag) {
		foreignKeySimple = flag;
	}

	@Override
	public boolean isFKSimple() {
		return foreignKeySimple;
	}

	public void setFKComposite(boolean flag) {
		foreignKeyComposite = flag;
	}

	@Override
	public boolean isFKComposite() {
		return foreignKeyComposite;
	}

	// -----------------------------------------------------------------------------

	public void setAutoIncremented(boolean b) {
		autoIncremented = b;
	}

	@Override
	public boolean isAutoIncremented() {
		return autoIncremented;
	}

	// -----------------------------------------------------------------------------

	public void setDatabaseNotNull(boolean flag) {
		databaseNotNull = flag;
	}

	public void setDatabaseNotNull(String flag) {
		databaseNotNull = "true".equalsIgnoreCase(flag);
	}

	@Override
	public boolean isDatabaseNotNull() {
		return databaseNotNull;
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
		return databaseDefaultValue;
	}

	/**
	 * Set the database default value
	 * 
	 * @param v
	 */
	public void setDatabaseDefaultValue(String v) { // #LGU 10/08/2011
		databaseDefaultValue = v;
	}

	// -----------------------------------------------------------------------------
	@Override
	public String getDatabaseComment() {
		return databaseComment;
	}

	public void setDatabaseComment(String s) {
		databaseComment = s;
	}

	// -----------------------------------------------------------------------------
//
//	@Override
//	public Integer getJdbcTypeCode() {
//		return _iJdbcTypeCode;
//	}
//
//	public void setJdbcTypeCode(int typeCode) {
//		_iJdbcTypeCode = typeCode;
//	}
//
//	@Override
//	public String getJdbcTypeName() {
//		String text = JdbcTypesManager.getJdbcTypes().getTextForCode(getJdbcTypeCode());
//		return text != null ? text : "???";
//	}

	// -----------------------------------------------------------------------------

	/**
	 * Returns the database native type name <br>
	 * Examples : INTEGER, VARCHAR, NUMBER, CHAR, etc...
	 * 
	 * @return
	 */
	@Override
	public String getDatabaseType() { // ver 3.0.0
		return databaseTypeName;
	}

	public void setDatabaseType(String s) {
		this.databaseTypeName = s;
	}

	// -----------------------------------------------------------------------------
	@Override
	public String getName() { // v 3.0.0
		return name;
	}

	public void setName(String s) { // v 3.0.0
		name = s;
	}

	// -----------------------------------------------------------------------------

	// -----------------------------------------------------------------------------
	@Override
	public String getNeutralType() { // v 3.0.0
		return neutralType;
	}

	// -----------------------------------------------------------------------------
	@Override
	public String getDefaultValue() { // ver 3.0.0
		return defaultValue;
	}

	/**
	 * Set the default value for the attribute
	 * 
	 * @param s
	 *            the default value ( eg : "0", "false" )
	 */
	public void setDefaultValue(String s) {
		defaultValue = s;
	}

	// -----------------------------------------------------------------------------
	@Override
	public String getInitialValue() { // v 3.0.0
		return null; // Not yet implemented
	}

	// -----------------------------------------------------------------------------
	@Override
	public boolean isNotNull() { // v 3.0.0
		return notNull;
	}
	public void setNotNull(boolean v) { // v 3.0.0
		notNull = v;
	}

	// -----------------------------------------------------------------------------
	@Override
	public boolean isNotEmpty() {
		return notEmpty;
	}
	public void setNotEmpty(boolean v) {
		notEmpty = v;
	}

	// -----------------------------------------------------------------------------
	@Override
	public boolean isNotBlank() {
		return notBlank;
	}
	public void setNotBlank(boolean v) {
		notBlank = v;
	}

	// -----------------------------------------------------------------------------
	@Override
	public Integer getMinLength() {
		return minLength;
	}
	public void setMinLength(Integer v) {
		minLength = v;
	}

	// -----------------------------------------------------------------------------
	@Override
	public Integer getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(Integer v) {
		maxLength = v;
	}

	// -----------------------------------------------------------------------------
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String v) {
		pattern = v;
	}

	// -----------------------------------------------------------------------------
	@Override
	public boolean isSelected() {
		return _bSelected;
	}
	public void setSelected(boolean b) {
		_bSelected = b;
	}

	// -----------------------------------------------------------------------------
	// Special infos
	// -----------------------------------------------------------------------------
	@Override
	public String getLabel() {
		return label;
	}
	public void setLabel(String s) {
		label = s;
	}

	// -----------------------------------------------------------------------------
	@Override
	public String getInputType() {
		return inputType;
	}
	public void setInputType(String s) { 
		inputType = s;
	}

	// -----------------------------------------------------------------------------
	@Override
	public boolean isLongText() {
		return longText;
	}

	public void setLongText(String flag) {
		setLongText("true".equalsIgnoreCase(flag));
	}

	public void setLongText(boolean b) {
		longText = b;
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
		return datePast;
	}
	public void setDatePast(boolean v) {
		datePast = v;
	}

	@Override
	public boolean isDateFuture() {
		return dateFuture;
	}
	public void setDateFuture(boolean v) {
		dateFuture = v;
	}

	@Override
	public String getDateBeforeValue() {
		return dateBeforeValue;
	}
	public void setDateBeforeValue(String v) {
		dateBeforeValue = v;
	}

	@Override
	public String getDateAfterValue() {
		return dateAfterValue;
	}
	public void setDateAfterValue(String v) {
		dateAfterValue = v;
	}
	// -----------------------------------------------------------------------------

	/**
	 * The value used to store a TRUE in the database ( never null )
	 * 
	 * @return the value or "" if none (never null)
	 */
	public String getBooleanTrueValue() {
		return (booleanTrueValue != null ? booleanTrueValue : "");
	}

	/**
	 * The value used to store a FALSE in the database ( never null )
	 * 
	 * @return the value or "" if none (never null)
	 */
	public String getBooleanFalseValue() {
		return (booleanFalseValue != null ? booleanFalseValue : "");
	}

	public void setBooleanTrueValue(String v) {
		booleanTrueValue = v;
	}

	public void setBooleanFalseValue(String v) {
		booleanFalseValue = v;
	}

	// -----------------------------------------------------------------------------

	public BigDecimal getMinValue() { // ver 3.0.0
		return minValue;
	}

	public void setMinValue(BigDecimal v) { // ver 3.0.0
		minValue = v;
	}

	public BigDecimal getMaxValue() { // ver 3.0.0
		return maxValue;
	}

	public void setMaxValue(BigDecimal v) { // ver 3.0.0
		maxValue = v;
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
		setDateBeforeValue(null);
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
				+ "|" + getNeutralType(); // v 3.0.0
	}

	// ---------------------------------------------------------------------------------------------------
	// Methods added in ver 3.0.0 in order to be compliant with the "generic
	// model"
	// ---------------------------------------------------------------------------------------------------
	@Override
	public boolean isGeneratedValue() {
		return generatedValueStrategy != GeneratedValueStrategy.UNDEFINED;
	}

	@Override
	public String getGeneratedValueGeneratorName() {
		return generatedValueGeneratorName;
	}	
	public void setGeneratedValueGeneratorName(String v) {
		generatedValueGeneratorName = v;
	}

	@Override
	public GeneratedValueStrategy getGeneratedValueStrategy() {
		return generatedValueStrategy;
	}	
	public void setGeneratedValueStrategy(GeneratedValueStrategy v) {
		generatedValueStrategy = v ;
	}

	// ---------------------------------------------------------------------------------------------------
	// Sequence generator information
	// ---------------------------------------------------------------------------------------------------
	@Override
	public Integer getGeneratedValueAllocationSize() {
		return generatedValueAllocationSize;
	}
	public void setGeneratedValueAllocationSize(Integer v) {
		this.generatedValueAllocationSize = v;
	}

	@Override
	public String getGeneratedValueSequenceName() {
		return generatedValueSequenceName;
	}	
	public void setGeneratedValueSequenceName(String v) {
		generatedValueSequenceName = v ;
	}

	// ---------------------------------------------------------------------------------------------------
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
	public TagContainer getTagContainer() { // v 3.4.0
		return tagContainer;
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

	@Override 
	public String getSize() { // v 3.4.0
		return size;
	}
	public void setSize(String v) { // v 3.4.0
		this.size = v;
	}

    @Override
    public boolean isUnique() { // v 3.4.0
        return this.isUnique;
    }
    public void setUnique(boolean b) { // v 3.4.0
        this.isUnique = b;
    }

	@Override
	public String getGeneratedValueTableName() {
		return null;
	}

	@Override
	public String getGeneratedValueTablePkColumnName() {
		return null;
	}

	@Override
	public String getGeneratedValueTablePkColumnValue() {
		return null;
	}

	@Override
	public String getGeneratedValueTableValueColumnName() {
		return null;
	}

}
