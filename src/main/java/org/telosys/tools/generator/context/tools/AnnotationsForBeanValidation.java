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

import org.telosys.tools.commons.JavaTypeUtil;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.generator.context.AttributeInContext;

/**
 * Java "Bean Validation" annotations for a field/attribute  ( JSR 303 strict ) 
 * 
 * @author Laurent GUERIN
 *
 */
public class AnnotationsForBeanValidation
{
	private final AttributeInContext attribute ;
	
	/**
	 * Constructor 
	 * @param attribute
	 */
	public AnnotationsForBeanValidation(AttributeInContext attribute) {
		super();
		this.attribute = attribute;
	}

	/**
	 * Returns the validation annotations based on the 'actual type' 
	 * @param leftMargin
	 * @return
	 */
	public String getValidationAnnotations( int leftMargin ) {
		//--- Reset everything at each call 
		AnnotationsBuilder annotations = new AnnotationsBuilder(leftMargin);

		//--- Build Annotations 
		if ( attribute.isNotNull() ) {
			//--- Only if not a primitive type ( @NotNull is useless for a primitive type ) 
			if ( ! JavaTypeUtil.isPrimitiveType( attribute.getFullType() )  ) {
				annotations.addLine("@NotNull" );
			}
		}
		addOtherAnnotations(annotations);

		return annotations.getAnnotations() ;
	}

	/**
	 * Returns the validation annotations based on the 'wrapper type' 
	 * @param iLeftMargin
	 * @return
	 */
	public String getValidationAnnotationsForWrapperType( int iLeftMargin ) {
		//--- Reset everything at each call 
		AnnotationsBuilder annotations = new AnnotationsBuilder(iLeftMargin);

		//--- Build annotations 
		if ( attribute.isNotNull() ) {
			annotations.addLine("@NotNull" ); // Wrapper type => don't care if primitive type 
		}
		addOtherAnnotations(annotations);

		return annotations.getAnnotations() ;
	}
	
	/**
	 * Add all required annotations for the current attribute ( except '@NotNull' annotation ) 
	 * @param annotations
	 * @param sJavaFullType
	 */
	private void addOtherAnnotations(AnnotationsBuilder annotations ) {
		String sJavaFullType = attribute.getFullType() ;
		
		//--- Annotations for each type category 
		if ( JavaTypeUtil.isCategoryBoolean( sJavaFullType ) )
		{
			// Nothing to do !
		}
		else if ( JavaTypeUtil.isCategoryString( sJavaFullType ) )
		{
			annotationSize(annotations);
			annotationPattern(annotations);
		}
		else if ( JavaTypeUtil.isCategoryNumber( sJavaFullType ) )
		{
			annotationMin(annotations);
			annotationMax(annotations);
		}
		else if ( JavaTypeUtil.isCategoryDateOrTime( sJavaFullType ) )
		{
			if ( attribute.hasDatePastValidation() ) {
				annotations.addLine("@Past" );
			}
			if ( attribute.hasDateFutureValidation() ) {
				annotations.addLine("@Future" );
			}
		}
	}

	private boolean hasSizeConstraint()
	{
		if ( ! StrUtil.nullOrVoid ( attribute.getMinLength() ) ) return true ;
		if ( ! StrUtil.nullOrVoid ( attribute.getMaxLength() ) ) return true ;
		if ( attribute.isNotEmpty() ) return true ;
		return false ;
	}
	private String minSize()
	{
		if ( ! StrUtil.nullOrVoid ( attribute.getMinLength() ) ) {
			return attribute.getMinLength().trim() ;
		}
		if ( attribute.isNotEmpty() ) {
			return "1" ; // min=1
		}
		return null ;
	}
	private String maxSize()
	{
		if ( ! StrUtil.nullOrVoid ( attribute.getMaxLength() ) ) {
			return attribute.getMaxLength().trim() ;
		}
		return null ;
	}
	
	private void annotationSize(AnnotationsBuilder annotations) {		
		if ( hasSizeConstraint() ) {
			String minSize = minSize();
			String maxSize = maxSize();
			if ( minSize != null && maxSize != null ) {
				annotations.addLine( "@Size( min = " + minSize + ", max = " + maxSize + " )");
			}
			else if ( minSize != null ) {
				annotations.addLine( "@Size( min = " + minSize + " )");
			}
			else if ( maxSize != null ) {
				annotations.addLine( "@Size( max = " + maxSize + " )");
			}
		}
	}

	private void annotationPattern(AnnotationsBuilder annotations) {
		if ( ! StrUtil.nullOrVoid ( attribute.getPattern() ) ) {
			annotations.addLine("@Pattern( regexp = \"" + attribute.getPattern() + "\" )");
		}
	}
	
	private void annotationMin(AnnotationsBuilder annotations) {
		if ( ! StrUtil.nullOrVoid ( attribute.getMinValue() ) )
		{
			String min = attribute.getMinValue().trim() ;
			if ( min.indexOf('.') >=0 ) {
				annotations.addLine("@DecimalMin( value = \"" + min + "\" )");
			}
			else {
				annotations.addLine("@Min( value=" + min + " )");
			}
		}
	}
	
	private void annotationMax(AnnotationsBuilder annotations)
	{
		if ( ! StrUtil.nullOrVoid ( attribute.getMaxValue() ) )
		{
			String max = attribute.getMaxValue().trim() ;
			if ( max.indexOf('.') >=0 ) {
				annotations.addLine("@DecimalMax( value = \"" + max + "\" )");
			}
			else {
				annotations.addLine("@Max( value=" + max + " )");
			}
		}
	}
	
}
