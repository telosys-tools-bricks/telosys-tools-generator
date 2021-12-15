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
package org.telosys.tools.generator.context.tools;

import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.generator.context.AttributeInContext;
import org.telosys.tools.generic.model.enums.BooleanValue;
import org.telosys.tools.generic.model.enums.DateType;

/**
 * This class manages the JPA annotations for a given Java attribute ( a field mapped on a column )
 * 
 * @author Laurent GUERIN
 *
 */
public class JpaAnnotations 
{
    public static final boolean   EMBEDDED_ID_TRUE  = true ;
    public static final boolean   EMBEDDED_ID_FALSE = false ;
    
    private static final String GENERATION_TYPE_AUTO     = "GenerationType.AUTO";
    private static final String GENERATION_TYPE_IDENTITY = "GenerationType.IDENTITY";
    private static final String GENERATION_TYPE_SEQUENCE = "GenerationType.SEQUENCE";
    private static final String GENERATION_TYPE_TABLE    = "GenerationType.TABLE"; 
    
	private static final String TABLE    = "table";

	private static final String SEQUENCE = "sequence";

	private static final String IDENTITY = "identity";

	private static final String AUTO     = "auto";

	
	private final AttributeInContext attribute ;
	private final boolean generateColumnDefinition ;
	
	/**
	 * Constructor
	 * @param attribute
	 */
	public JpaAnnotations(AttributeInContext attribute, boolean generateColumnDefinition) {
		super();
		this.attribute = attribute;
		this.generateColumnDefinition = generateColumnDefinition ;
	}

	/**
	 * Returns all the JPA annotations with the given left margin
	 * @param iLeftMargin
	 * @param embeddedId embedded id flag ( EMBEDDED_ID_TRUE or EMBEDDED_ID_FALSE )
	 * @return
	 */
	public String getJpaAnnotations( int iLeftMargin, boolean embeddedId )
	{
		//--- Reset everything at each call 
		AnnotationsBuilder annotations = new AnnotationsBuilder(iLeftMargin);
		
		String sAttributeFullType = attribute.getFullType();
		
		if ( attribute.isKeyElement() ) {
			if ( ! embeddedId ) {
				// do not use "@Id" in an Embedded ID
				annotations.addLine("@Id");
			}

			if ( attribute.isGeneratedValue() ) {
				annotationsForGeneratedValue(annotations); // v 3.4.0
			}
			else {
				if ( attribute.isAutoIncremented() ) {
					// auto-incremented by database 'identity column' 
					annotationGeneratedValue(annotations, GENERATION_TYPE_IDENTITY, null);
				}
			}
		}

		// @Temporal
		if ( "java.util.Date".equals(sAttributeFullType) || "java.util.Calendar".equals(sAttributeFullType) ) 
		{
			int dateTypeValue = attribute.getDateType() ; // same 'int' value as in Velocity template
			if ( dateTypeValue == DateType.DATE_ONLY.getValue() ) {
				annotationTemporal(annotations, "DATE");
			}
			else if ( dateTypeValue == DateType.TIME_ONLY.getValue() ) {
				annotationTemporal(annotations, "TIME");
			}
			else if ( dateTypeValue == DateType.DATE_AND_TIME.getValue() ) {
				annotationTemporal(annotations, "TIMESTAMP");
			}
		}

		if (       "java.sql.Blob".equals(sAttributeFullType) 
				|| "java.sql.Clob".equals(sAttributeFullType) 
				|| "byte[]".equals(sAttributeFullType) ) 
		{
			annotations.addLine ( "@Lob" );
		}

		// @Column
		annotationColumn(annotations);
		
		return annotations.getAnnotations();
	}
	
	private void annotationsForGeneratedValue( AnnotationsBuilder annotations ) {
		String strategy = attribute.getGeneratedValueStrategy() ;
		if ( AUTO.equalsIgnoreCase( strategy ) ) {
			annotationGeneratedValue(annotations, GENERATION_TYPE_AUTO, null);
		} 
		else if ( IDENTITY.equalsIgnoreCase( strategy ) ) {
			annotationGeneratedValue(annotations, GENERATION_TYPE_IDENTITY, null);
		} 
		else if ( SEQUENCE.equalsIgnoreCase( strategy ) )	{
			annotationGeneratedValue( annotations, GENERATION_TYPE_SEQUENCE, attribute.getGeneratedValueGenerator() );
			if (attribute.hasSequenceGenerator() ) {
				annotationSequenceGenerator(annotations);
			}
		} 
		else if ( TABLE.equalsIgnoreCase( strategy ) ) {
			annotationGeneratedValue( annotations, GENERATION_TYPE_TABLE, attribute.getGeneratedValueGenerator() );
			if (attribute.hasTableGenerator()) {
				annotationTableGenerator(annotations);
			}
		}
		else{
			// AUTO is the default strategy ( see JPA doc ) => use it explicitly 
			annotationGeneratedValue(annotations, GENERATION_TYPE_AUTO, null);
		}		
	}
	/**
	 * Adds a "@GeneratedValue" annotation
	 * @param strategy
	 */
	private void annotationGeneratedValue( AnnotationsBuilder annotations, String strategy, String generator ) 
	{
		// . strategy  : (Optional) The primary key generation strategy that the persistence 
		//                provider must use to generate the annotated entity primary key.
		//                Default : AUTO
		// . generator : (Optional) The name of the primary key generator to use as specified 
		//                in the SequenceGenerator or TableGenerator annotation.
		String s = "@GeneratedValue(strategy=" + strategy ;
		if ( ! StrUtil.nullOrVoid( generator ) ) {
			s = s + ", generator=\"" + generator + "\"" ;
		}
		s = s + ")" ;
		annotations.addLine ( s );		
	}

	/**
	 * Adds a "@SequenceGenerator" annotation
	 */
	private void annotationSequenceGenerator(AnnotationsBuilder annotations) {
		
		// name - String : (Required) 
		//    A unique generator name that can be referenced by one or more classes 
		//    to be the generator for primary key values.
		String name = attribute.getSequenceGeneratorName() ;
		if ( StrUtil.nullOrVoid(name) ) {
			return; // name is required : if no name => no annotation
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("@SequenceGenerator(name=\"").append(name).append("\"");
		
		// sequenceName - String : (Optional)
		//    The name of the database sequence object from which to obtain primary key values.
		if ( ! StrUtil.nullOrVoid( attribute.getSequenceGeneratorSequenceName() ) ) {
			sb.append(", sequenceName=\"").append(attribute.getSequenceGeneratorSequenceName()).append("\"");
		}
		
		// allocationSize - int : (Optional) 
		//  The amount to increment by when allocating sequence numbers from the sequence
		//  If the sequence object already exists in the database, then you must specify the allocationSize 
		//  to match the INCREMENT value of the database sequence object. For example, if you have a sequence object
		//  in the database that you defined to INCREMENT BY 5, set the allocationSize to 5 in the sequence generator definition
		if ( attribute.getSequenceGeneratorAllocationSize() > 0 ) {
			sb.append(", allocationSize=").append(attribute.getSequenceGeneratorAllocationSize());
		}
		
		// Other JPA attribute in @SequenceGenerator (not supported yet)
		//  . schema (Optional)
		//  . catalog (Optional)
		//  . initialValue (Optional)
		
		sb.append(")") ;
		annotations.addLine ( sb.toString() );
	}

	/**
	 * Adds a "@TableGenerator" annotation
	 */
	private void annotationTableGenerator(AnnotationsBuilder annotations) 
	{
		// Other JPA attribute in @TableGenerator (not supported yet)
		// . allocationSize
		// . catalog
		// . initialValue
		// . schema
		// . uniqueConstraints

		String s = "@TableGenerator(name=\"" + attribute.getTableGeneratorName() + "\"" ; // Required
		if ( ! StrUtil.nullOrVoid( attribute.getTableGeneratorTable() ) ) {
			s = s + ", table=\"" + attribute.getTableGeneratorTable() + "\"" ;
		}
		if ( ! StrUtil.nullOrVoid( attribute.getTableGeneratorPkColumnName() ) ) {
			s = s + ", pkColumnName=\"" + attribute.getTableGeneratorPkColumnName() + "\"" ;
		}
		if ( ! StrUtil.nullOrVoid( attribute.getTableGeneratorValueColumnName() ) ) {
			s = s + ", valueColumnName=\"" + attribute.getTableGeneratorValueColumnName() + "\"" ;
		}
		if ( ! StrUtil.nullOrVoid( attribute.getTableGeneratorPkColumnValue() ) ) {
			s = s + ", pkColumnValue=\"" + attribute.getTableGeneratorPkColumnValue() + "\"" ;
		}
		s = s + ")" ;
		annotations.addLine ( s );				
	}

	/**
	 * Adds a "@Column" annotation 
	 */
	private void annotationColumn(AnnotationsBuilder annotations) 
	{
		StringBuilder sb = new StringBuilder();
		sb.append("@Column(");
		//--- name
		sb.append("name=\"");
		// sb.append(_attribute.getDatabaseName());
		sb.append(attribute.getSqlColumnName()); // v 3.4.0 : apply naming conventions if any
		sb.append("\"");
		
		//--- nullable : (Optional) Whether the database column is nullable.
		if ( attribute.isDatabaseNotNull() || attribute.isNotNull() ) {
			sb.append(", nullable=false");
		}
		
		//--- length : (Optional) The column length. 
		if ( attribute.isStringType() ) { // v 3.3.0
			if ( ! StrUtil.nullOrVoid(attribute.getDatabaseSize()) ) {
				sb.append(", length=");
				sb.append(attribute.getDatabaseSize());
			}
			else if ( ! StrUtil.nullOrVoid(attribute.getMaxLength()) ) {
				sb.append(", length=");
				sb.append(attribute.getMaxLength());
			}
		}

		//--- unique : (Optional) Whether the column is a unique key.
		if ( attribute.isUnique() ) { // v 3.4.0
			sb.append(", unique=true");
		}
		
		// insertable : (Optional) Whether the column is included in SQL INSERT statements generated by the persistence provider.
		if ( attribute.getInsertableFlag() == BooleanValue.FALSE ) {
			sb.append( ", insertable=false" ); 			
		}
		// updatable  : (Optional) Whether the column is included in SQL UPDATE statements generated by the persistence provider.
		if ( attribute.getUpdatableFlag() == BooleanValue.FALSE ) {
			sb.append( ", updatable=false" ); 
		}

		// columnDefinition : (Optional) The SQL fragment that is used when generating the DDL for the column.
		if ( generateColumnDefinition ) {
			sb.append( ", columnDefinition=\"" );
			sb.append( buildColumnDefinition() );
			sb.append( "\"" );
		}

		// End of "@Column("
		sb.append(")");
		
		//--- Other elements for "@Column" ( from JavaDoc / Java EE 6 )
		// precision : (Optional) The precision for a decimal (exact numeric) column.
		// scale : (Optional) The scale for a decimal (exact numeric) column.
		// table : (Optional) The name of the table that contains the column.
		
		annotations.addLine ( sb.toString() );
	}
	
	private String buildColumnDefinition() { // v 3.4.0
		// JPA columndefinition examples :
		//   @Column(columnDefinition = "varchar(255) default 'John Snow'")
		//   @Column(columnDefinition = "integer default 25")
		StringBuilder sb = new StringBuilder();
		sb.append(attribute.getSqlColumnType());
		String columnConstraints = attribute.getSqlColumnConstraints();
		if ( ! StrUtil.nullOrVoid(columnConstraints) ) {
			sb.append(" ");
			sb.append(columnConstraints); // "not null", "unique", "defaut value"
		}
		return sb.toString();
	}
	
	/**
	 * Adds a "@Temporal" annotation 
	 */
	private void annotationTemporal(AnnotationsBuilder annotations, String sTemporalType) 
	{
		String s = "@Temporal(TemporalType." + sTemporalType + ")";
		annotations.addLine ( s );
	}
	
}
