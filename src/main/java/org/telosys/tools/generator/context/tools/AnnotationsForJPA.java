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
package org.telosys.tools.generator.context.tools;

import java.sql.Types;

import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.generator.context.AttributeInContext;

/**
 * This class manages the JPA annotations for a given Java attribute ( a field mapped on a column )
 * 
 * @author Laurent GUERIN
 *
 */
public class AnnotationsForJPA 
{
    public static final boolean   EMBEDDED_ID_TRUE  = true ;
    public static final boolean   EMBEDDED_ID_FALSE = false ;
    
	
	private static final String TABLE    = "table";

	private static final String SEQUENCE = "sequence";

	private static final String IDENTITY = "identity";

	private static final String AUTO     = "auto";

	
	private AttributeInContext _attribute = null ;
	
	/**
	 * Constructor
	 * @param attribute
	 */
	public AnnotationsForJPA(AttributeInContext attribute) {
		super();
		this._attribute = attribute;
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
		
		String sAttributeFullType = _attribute.getFullType();
		
		if ( _attribute.isKeyElement() ) 
		{
			if ( ! embeddedId ) {
				// do not use "@Id" in an Embedded ID
				annotations.addLine("@Id");
			}

			if ( _attribute.isGeneratedValue() ) 
			{
				String sStrategy = _attribute.getGeneratedValueStrategy() ;
				if ( AUTO.equalsIgnoreCase( sStrategy ) ) 
				{
					annotationGeneratedValue(annotations, "GenerationType.AUTO", null);
				} 
				else if ( IDENTITY.equalsIgnoreCase( sStrategy ) ) 
				{
					annotationGeneratedValue(annotations, "GenerationType.IDENTITY", null);
				} 
				else if ( SEQUENCE.equalsIgnoreCase( sStrategy ) )
				{
					annotationGeneratedValue( annotations, "GenerationType.SEQUENCE", _attribute.getGeneratedValueGenerator() );
					if (_attribute.hasSequenceGenerator() ) {
						annotationSequenceGenerator(annotations);
					}
				} 
				else if ( TABLE.equalsIgnoreCase( sStrategy ) ) 
				{
					annotationGeneratedValue( annotations, "GenerationType.TABLE", _attribute.getGeneratedValueGenerator() );
					if (_attribute.hasTableGenerator()) {
						annotationTableGenerator(annotations);
					}
				}
				else
				{
					// AUTO is the default strategy ( see JPA doc ) => use it explicitly 
					annotationGeneratedValue(annotations, "GenerationType.AUTO", null);
				}
			}
		}

		// @Temporal
		if ( "java.util.Date".equals(sAttributeFullType) || "java.util.Calendar".equals(sAttributeFullType) ) 
		{
			switch ( _attribute.getDateType()  ) 
			{
			case AttributeInContext.DATE_ONLY :
				annotationTemporal(annotations, "DATE");
				break;
			case AttributeInContext.TIME_ONLY :
				annotationTemporal(annotations, "TIME");
				break;
			case AttributeInContext.DATE_AND_TIME :
				annotationTemporal(annotations, "TIMESTAMP");
				break;
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
	private void annotationSequenceGenerator(AnnotationsBuilder annotations) 
	{
		// Other JPA attribute in @SequenceGenerator (not supported yet)
		//  . schema
		//  . catalog
		//  . initialValue
		
		// name - String : (Required) 
		//    A unique generator name that can be referenced by one or more classes 
		//    to be the generator for primary key values.
		String s = "@SequenceGenerator(name=\"" + _attribute.getSequenceGeneratorName() + "\"" ; // Required
		
		// sequenceName - String : (Optional)
		//    The name of the database sequence object from which to obtain primary key values.
		if ( ! StrUtil.nullOrVoid( _attribute.getSequenceGeneratorSequenceName() ) ) {
			s = s + ", sequenceName=\"" + _attribute.getSequenceGeneratorSequenceName() + "\"" ;
		}
		
		// allocationSize - int : (Optional) 
		//  The amount to increment by when allocating sequence numbers from the sequence
		//  If the sequence object already exists in the database, then you must specify the allocationSize 
		//  to match the INCREMENT value of the database sequence object. For example, if you have a sequence object
		//  in the database that you defined to INCREMENT BY 5, set the allocationSize to 5 in the sequence generator definition
		if ( _attribute.getSequenceGeneratorAllocationSize() > 0 ) {
			s = s + ", allocationSize=" + Integer.toString(_attribute.getSequenceGeneratorAllocationSize()) ;
		}
		s = s + ")" ;
		annotations.addLine ( s );		
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

		String s = "@TableGenerator(name=\"" + _attribute.getTableGeneratorName() + "\"" ; // Required
		if ( ! StrUtil.nullOrVoid( _attribute.getTableGeneratorTable() ) ) {
			s = s + ", table=\"" + _attribute.getTableGeneratorTable() + "\"" ;
		}
		if ( ! StrUtil.nullOrVoid( _attribute.getTableGeneratorPkColumnName() ) ) {
			s = s + ", pkColumnName=\"" + _attribute.getTableGeneratorPkColumnName() + "\"" ;
		}
		if ( ! StrUtil.nullOrVoid( _attribute.getTableGeneratorValueColumnName() ) ) {
			s = s + ", valueColumnName=\"" + _attribute.getTableGeneratorValueColumnName() + "\"" ;
		}
		if ( ! StrUtil.nullOrVoid( _attribute.getTableGeneratorPkColumnValue() ) ) {
			s = s + ", pkColumnValue=\"" + _attribute.getTableGeneratorPkColumnValue() + "\"" ;
		}
		s = s + ")" ;
		annotations.addLine ( s );				
	}

	/**
	 * Adds a "@Column" annotation 
	 */
	private void annotationColumn(AnnotationsBuilder annotations) 
	{
		String s = "@Column(" ;
		s = s + "name=\"" + _attribute.getDatabaseName() + "\"" ;
		
		//--- Nullable : (Optional) Whether the database column is nullable.
		if ( _attribute.isDatabaseNotNull() ) {
			s = s + ", nullable=false" ;
		}
		
		//--- Length : (Optional) The column length. 
		// implemented only for "VARCHAR" and "CHAR" JDBC types
		int jdbcType = _attribute.getJdbcTypeCode();
		if ( jdbcType == Types.VARCHAR || jdbcType == Types.CHAR ) {
			s = s + ", length=" + _attribute.getDatabaseSize() + "" ;
		}
		
		s = s + ")" ;
		
		//--- Other elements for "@Column" ( from JavaDoc / Java EE 6 )
		// columnDefinition : (Optional) The SQL fragment that is used when generating the DDL for the column.
		// insertable : (Optional) Whether the column is included in SQL INSERT statements generated by the persistence provider.
		// precision : (Optional) The precision for a decimal (exact numeric) column.
		// scale : (Optional) The scale for a decimal (exact numeric) column.
		// table : (Optional) The name of the table that contains the column.
		// unique : (Optional) Whether the column is a unique key.
		// updatable : (Optional) Whether the column is included in SQL UPDATE statements generated by the persistence provider.
		
		annotations.addLine ( s );
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
