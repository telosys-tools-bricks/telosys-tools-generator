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
package org.telosys.tools.fake.generic.model;

import java.math.BigDecimal;

import org.telosys.tools.generic.model.Attribute;
import org.telosys.tools.generic.model.DateType;
import org.telosys.tools.generic.model.Entity;

public class FakeAttribute implements Attribute {
	
	private String name = "";
	private String neutralType = "";
	
	private String booleanFalseValue;
	private String booleanTrueValue;
	private String databaseComment;
	private String databaseDefaultValue;
	private String databaseName;
	private Integer databaseSize;
	private String databaseType;
	private String dateAfterValue;
	private String dateBeforeValue;
	private DateType dateType;
	private String defaultValue;
	private Entity entity;
	private String generatedValueGenerator;
	private String generatedValueStrategy;
	private String initialValue;
	private String inputType;
	private Integer jdbcTypeCode;
	private String jdbcTypeName;
	private String label;
	private Integer maxLength;
	private Integer minLength;
	private BigDecimal maxValue;
	private BigDecimal minValue;
	private String pattern;
	private Integer sequenceGeneratorAllocationSize;
	private String sequenceGeneratorName;
	private String sequenceGeneratorSequenceName;
	private String tableGeneratorName;
	private String tableGeneratorPkColumnName;
	private String tableGeneratorPkColumnValue;
	private String tableGeneratorTable;
	private String tableGeneratorValueColumnName;
	private boolean autoIncremented = false ;
	private boolean databaseNotNull = false ;
	private boolean generatedValue = false;
	private boolean keyElement = false;
	private boolean longText = false;
	private boolean notBlank = false;
	private boolean notEmpty = false;
	private boolean notNull = false;
	private boolean selected = true; // SELECTED BY DEFAULT
	private boolean hasSequenceGenerator = false;
	private boolean hasTableGenerator = false;
	private boolean isDateAfter = false;
	private boolean isDateBefore = false;
	private boolean isDateFuture = false;
	private boolean isDatePast = false;
	
	//private boolean isUsedInForeignKey = false;
	// An attribute can be involved in many FK, it can be both in a SIMPLE FK and in a COMPOSITE FK 
	private boolean isForeignKeySimple     = false ; // ( false by default )
	private boolean isForeignKeyComposite  = false ; // ( always false in the DSL model )
	private String  referencedEntityClassName = null ; // no reference by default


	// Annotations added for types
	private boolean isPrimitiveTypeExpected = false ;
	private boolean isUnsignedTypeExpected = false ;
	private boolean isObjectTypeExpected = false ;
	private boolean isSqlTypeExpected = false ;
	
	
	/**
	 * Constructor
	 * @param name
	 * @param neutralType
	 */
	public FakeAttribute(String name, String neutralType) {
		super();
		this.name = name;
		this.neutralType = neutralType;
	}
	
	@Override
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getNeutralType() {
		return neutralType;
	}
	public void setNeutralType(String neutralType) {
		this.neutralType = neutralType;
	}

	@Override
	public String getBooleanFalseValue() {
		return booleanFalseValue;
	}
	public void setBooleanFalseValue(String booleanFalseValue) {
		this.booleanFalseValue = booleanFalseValue;
	}

	@Override
	public String getBooleanTrueValue() {
		return booleanTrueValue;
	}
	public void setBooleanTrueValue(String booleanTrueValue) {
		this.booleanTrueValue = booleanTrueValue;
	}

	@Override
	public String getDatabaseComment() {
		return databaseComment;
	}
	public void setDatabaseComment(String databaseComment) {
		this.databaseComment = databaseComment;
	}

	@Override
	public String getDatabaseDefaultValue() {
		return databaseDefaultValue;
	}
	public void setDatabaseDefaultValue(String databaseDefaultValue) {
		this.databaseDefaultValue = databaseDefaultValue;
	}
	
	@Override
	public String getDatabaseName() {
		return databaseName;
	}
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	@Override
	public Integer getDatabaseSize() {
		return databaseSize;
	}
	public void setDatabaseSize(Integer databaseSize) {
		this.databaseSize = databaseSize;
	}

	@Override
	public String getDatabaseType() {
		return databaseType;
	}
	public void setDatabaseType(String databaseType) {
		this.databaseType = databaseType;
	}

	@Override
	public String getDateAfterValue() {
		return dateAfterValue;
	}
	public void setDateAfterValue(String dateAfterValue) {
		this.dateAfterValue = dateAfterValue;
	}

	@Override
	public String getDateBeforeValue() {
		return dateBeforeValue;
	}
	public void setDateBeforeValue(String dateBeforeValue) {
		this.dateBeforeValue = dateBeforeValue;
	}

	@Override
	public DateType getDateType() {
		return dateType;
	}
	public void setDateType(DateType dateType) {
		this.dateType = dateType;
	}

	@Override
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Entity getEntity() {
		return entity;
	}
	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	
	// Removed in v 3.0.0
//	@Override
//	public String getFullType() {
////		return fullType;
//		LanguageType languageType = typeConverter.getType(this);
//		return languageType.getFullType();
//	}
//	public void setFullType(String fullType) {
//		this.fullType = fullType;
//	}

	@Override
	public String getGeneratedValueGenerator() {
		return generatedValueGenerator;
	}
	public void setGeneratedValueGenerator(String generatedValueGenerator) {
		this.generatedValueGenerator = generatedValueGenerator;
	}
	
	@Override
	public String getGeneratedValueStrategy() {
		return generatedValueStrategy;
	}
	public void setGeneratedValueStrategy(String generatedValueStrategy) {
		this.generatedValueStrategy = generatedValueStrategy;
	}

	@Override
	public String getInitialValue() {
		return initialValue;
	}
	public void setInitialValue(String initialValue) {
		this.initialValue = initialValue;
	}

	@Override
	public String getInputType() {
		return inputType;
	}
	public void setInputType(String inputType) {
		this.inputType = inputType;
	}

	@Override
	public Integer getJdbcTypeCode() {
		return jdbcTypeCode;
	}
	public void setJdbcTypeCode(Integer jdbcTypeCode) {
		this.jdbcTypeCode = jdbcTypeCode;
	}

	@Override
	public String getJdbcTypeName() {
		return jdbcTypeName;
	}
	public void setJdbcTypeName(String jdbcTypeName) {
		this.jdbcTypeName = jdbcTypeName;
	}

	@Override
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}

	//--- Min / Max Length ( int )
	@Override
	public Integer getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}
	@Override
	public Integer getMinLength() {
		return minLength;
	}
	public void setMinLength(Integer minLength) {
		this.minLength = minLength;
	}
	
	//--- Min / Max value ( BigDecimal )
	@Override
	public BigDecimal getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(BigDecimal maxValue) {
		this.maxValue = maxValue;
	}
	@Override
	public BigDecimal getMinValue() {
		return minValue;
	}
	public void setMinValue(BigDecimal minValue) {
		this.minValue = minValue;
	}
	
	@Override
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public Integer getSequenceGeneratorAllocationSize() {
		return sequenceGeneratorAllocationSize;
	}
	public void setSequenceGeneratorAllocationSize(
			Integer sequenceGeneratorAllocationSize) {
		this.sequenceGeneratorAllocationSize = sequenceGeneratorAllocationSize;
	}

	@Override
	public String getSequenceGeneratorName() {
		return sequenceGeneratorName;
	}
	public void setSequenceGeneratorName(String sequenceGeneratorName) {
		this.sequenceGeneratorName = sequenceGeneratorName;
	}

	@Override
	public String getSequenceGeneratorSequenceName() {
		return sequenceGeneratorSequenceName;
	}
	public void setSequenceGeneratorSequenceName(
			String sequenceGeneratorSequenceName) {
		this.sequenceGeneratorSequenceName = sequenceGeneratorSequenceName;
	}

	// Removed in v 3.0.0
//	@Override
//	public String getSimpleType() {
////		return simpleType;
//		LanguageType languageType = typeConverter.getType(this);
//		if ( languageType == null ) {			
//			throw new EntityParserException("Invalid type '" + this.getNeutralType() + "'");
//		}
//		return languageType.getSimpleType();
//	}
//	public void setSimpleType(String simpleType) {
//		this.simpleType = simpleType;
//	}

	@Override
	public String getTableGeneratorName() {
		return tableGeneratorName;
	}
	public void setTableGeneratorName(String tableGeneratorName) {
		this.tableGeneratorName = tableGeneratorName;
	}

	@Override
	public String getTableGeneratorPkColumnName() {
		return tableGeneratorPkColumnName;
	}
	public void setTableGeneratorPkColumnName(String tableGeneratorPkColumnName) {
		this.tableGeneratorPkColumnName = tableGeneratorPkColumnName;
	}

	@Override
	public String getTableGeneratorPkColumnValue() {
		return tableGeneratorPkColumnValue;
	}
	public void setTableGeneratorPkColumnValue(String tableGeneratorPkColumnValue) {
		this.tableGeneratorPkColumnValue = tableGeneratorPkColumnValue;
	}

	@Override
	public String getTableGeneratorTable() {
		return tableGeneratorTable;
	}
	public void setTableGeneratorTable(String tableGeneratorTable) {
		this.tableGeneratorTable = tableGeneratorTable;
	}

	@Override
	public String getTableGeneratorValueColumnName() {
		return tableGeneratorValueColumnName;
	}
	public void setTableGeneratorValueColumnName(
			String tableGeneratorValueColumnName) {
		this.tableGeneratorValueColumnName = tableGeneratorValueColumnName;
	}
	
//	public String getType() {
//		return type;
//	}
//	public void setType(String type) {
//		this.type = type;
//	}
//	public String getWrapperType() {
//		return wrapperType;
//	}
//	public void setWrapperType(String wrapperType) {
//		this.wrapperType = wrapperType;
//	}

	@Override
	public boolean hasSequenceGenerator() {
		return hasSequenceGenerator;
	}
	public void setHasSequenceGenerator(boolean hasSequenceGenerator) {
		this.hasSequenceGenerator = hasSequenceGenerator;
	}

	@Override
	public boolean hasTableGenerator() {
		return hasTableGenerator;
	}
	public void setHasTableGenerator(boolean hasTableGenerator) {
		this.hasTableGenerator = hasTableGenerator;
	}

	@Override
	public boolean isAutoIncremented() {
		return autoIncremented;
	}
	public void setAutoIncremented(boolean autoIncremented) {
		this.autoIncremented = autoIncremented;
	}

	@Override
	public boolean isDatabaseNotNull() {
		return databaseNotNull;
	}
	public void setDatabaseNotNull(boolean databaseNotNull) {
		this.databaseNotNull = databaseNotNull;
	}

	@Override
	public boolean isDateAfter() {
		return isDateAfter;
	}
	public void setDateAfter(boolean isDateAfter) {
		this.isDateAfter = isDateAfter;
	}

	@Override
	public boolean isDateBefore() {
		return isDateBefore;
	}
	public void setDateBefore(boolean isDateBefore) {
		this.isDateBefore = isDateBefore;
	}

	@Override
	public boolean isDateFuture() {
		return isDateFuture;
	}
	public void setDateFuture(boolean isDateFuture) {
		this.isDateFuture = isDateFuture;
	}

	@Override
	public boolean isDatePast() {
		return isDatePast;
	}
	public void setDatePast(boolean isDatePast) {
		this.isDatePast = isDatePast;
	}

	@Override
	public boolean isGeneratedValue() {
		return generatedValue;
	}
	public void setGeneratedValue(boolean generatedValue) {
		this.generatedValue = generatedValue;
	}
	
	@Override
	public boolean isKeyElement() {
		return keyElement;
	}
	public void setKeyElement(boolean keyElement) {
		this.keyElement = keyElement;
	}

	@Override
	public boolean isLongText() {
		return longText;
	}
	public void setLongText(boolean longText) {
		this.longText = longText;
	}
	
	@Override
	public boolean isNotBlank() {
		return notBlank;
	}
	public void setNotBlank(boolean v) {
		this.notBlank = v;
	}
	
	@Override
	public boolean isNotEmpty() {
		return notEmpty;
	}
	public void setNotEmpty(boolean v) {
		this.notEmpty = v;
	}
	
	@Override
	public boolean isNotNull() {
		return notNull;
	}
	public void setNotNull(boolean v) {
		this.notNull = v;
	}

	@Override
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}


// Replaced by "isFK" in v 3.0.0
//	@Override
//	public boolean isUsedInForeignKey() {
//		return isUsedInForeignKey;
//	}
//	public void setUsedInForeignKey(boolean isUsedInForeignKey) {
//		this.isUsedInForeignKey = isUsedInForeignKey;
//	}
	@Override
	public boolean isFK() {
		return isForeignKeySimple || isForeignKeyComposite ;
	}

	public void setFKSimple(boolean flag) {
		isForeignKeySimple = flag ;
	}
	@Override
	public boolean isFKSimple() {
		return isForeignKeySimple;
	}

// Never "Composite" in this model (DSL)
//	public void setFKComposite(boolean flag) {
//		_bForeignKeyComposite = flag ;
//	}
	@Override
	public boolean isFKComposite() {
		return isForeignKeyComposite; // Always FALSE (never "Composite" in a DSL model)
	}

	public void setReferencedEntityClassName(String entityClassName) {
		referencedEntityClassName = entityClassName ;
	}
	@Override
	public String getReferencedEntityClassName() {
		if ( isFK() ) {
			return referencedEntityClassName ;
		}
		else {
			return null ;
		}
	}
	
	//----------------------------------------------------------------------------------
	@Override
	public boolean isObjectTypeExpected() {
		return isObjectTypeExpected ;
	}
	public void setObjectTypeExpected(boolean v) {
		this.isObjectTypeExpected = v ;
	}
	
	@Override
	public boolean isPrimitiveTypeExpected() {
		return isPrimitiveTypeExpected ;
	}
	public void setPrimitiveTypeExpected(boolean v) {
		this.isPrimitiveTypeExpected = v ;
	}
	
	@Override
	public boolean isSqlTypeExpected() {
		return isSqlTypeExpected;
	}
	public void setSqlTypeExpected(boolean v) {
		this.isSqlTypeExpected = v ;
	}
	
	@Override
	public boolean isUnsignedTypeExpected() {
		return isUnsignedTypeExpected;
	}
	public void setUnsignedTypeExpected(boolean v) {
		this.isUnsignedTypeExpected = v ;
	}
	
	@Override
	public boolean isUsedInLinks() {
		// In this model an attribute is "used in links" if it's a FK (or pseudo FK)
		return isFK();
	}
	
	@Override
	public boolean isUsedInSelectedLinks() {
		// No "link selection" in this model 
		// Then all links are considered as "selected" => same as "isUsedInLinks()"
		return isUsedInLinks();
	}
}
