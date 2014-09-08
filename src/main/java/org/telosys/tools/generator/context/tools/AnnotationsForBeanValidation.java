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

import org.telosys.tools.commons.JavaTypeUtil;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.generator.context.AttributeInContext;

/**
 * Attribute (field) annotations for "Bean Validation"  ( JSR 303 strict )
 * 
 * @author Laurent GUERIN
 *
 */
public class AnnotationsForBeanValidation
{
	private AttributeInContext _attribute = null ;
	
	/**
	 * Constructor 
	 * @param attribute
	 */
	public AnnotationsForBeanValidation(AttributeInContext attribute) {
		super();
		this._attribute = attribute;
	}

	/**
	 * Returns the validation annotations
	 * @param iLeftMargin
	 * @return
	 */
	public String getValidationAnnotations( int iLeftMargin )
	{
//		boolean bJSR303strict = false ;
//		if ( sFlagJSR303 != null ) {
//			if ( sFlagJSR303.equalsIgnoreCase("JSR303") ) {
//				bJSR303strict = true ;
//			}
//		}
		
		//--- Reset everything at each call 
		AnnotationsBuilder annotations = new AnnotationsBuilder(iLeftMargin);

		String sJavaFullType = _attribute.getFullType() ;
		
		//--- Annotations for all categories 
		if ( ! JavaTypeUtil.isPrimitiveType(sJavaFullType) ) 
		{
			if ( _attribute.isNotNull() ) 
			{
				annotations.addLine("@NotNull" );
			}
		}

		//--- Annotations for each type category 
		if ( JavaTypeUtil.isCategoryBoolean( sJavaFullType ) )
		{
			// Nothing to do !
		}
		else if ( JavaTypeUtil.isCategoryString( sJavaFullType ) )
		{
			annotationSize(annotations);
			annotationPattern(annotations);
//			if ( ! bJSR303strict )
//			{
//				if ( _attribute.isNotEmpty() ) 
//				{
//					annotations.addLine("@NotEmpty" );
//				}
//				if ( _attribute.isNotBlank() ) 
//				{
//					annotations.addLine("@NotBlank" );
//				}
//			}			
		}
		else if ( JavaTypeUtil.isCategoryNumber( sJavaFullType ) )
		{
			annotationMin(annotations);
			annotationMax(annotations);
		}
		else if ( JavaTypeUtil.isCategoryDateOrTime( sJavaFullType ) )
		{
			//if ( _attribute.isDatePast() ) 
			if ( _attribute.hasDatePastValidation() ) // v 2.0.5
			{
				annotations.addLine("@Past" );
			}
			//if ( _attribute.isDateFuture() ) 
			if ( _attribute.hasDateFutureValidation() )  // v 2.0.5
			{
				annotations.addLine("@Future" );
			}
		}
		
		return annotations.getAnnotations() ;
	}
	
	private boolean hasSizeConstraint()
	{
		if ( ! StrUtil.nullOrVoid ( _attribute.getMinLength() ) ) return true ;
		if ( ! StrUtil.nullOrVoid ( _attribute.getMaxLength() ) ) return true ;
		if ( _attribute.isNotEmpty() ) return true ;
		return false ;
	}
	private String minSize()
	{
		if ( ! StrUtil.nullOrVoid ( _attribute.getMinLength() ) ) {
			return _attribute.getMinLength().trim() ;
		}
		if ( _attribute.isNotEmpty() ) {
			return "1" ; // min=1
		}
		return null ;
	}
	private String maxSize()
	{
		if ( ! StrUtil.nullOrVoid ( _attribute.getMaxLength() ) ) {
			return _attribute.getMaxLength().trim() ;
		}
		return null ;
	}
	
	private void annotationSize(AnnotationsBuilder annotations)
	{
//		String min = null ;
//		String max = null ;
//		if ( ! StrUtil.nullOrVoid ( _attribute.getMaxLength() ) )
//		{
//			max = _attribute.getMaxLength().trim() ;
//			if ( ! StrUtil.nullOrVoid ( _attribute.getMinLength() ) )
//			{
//				min = _attribute.getMinLength().trim() ;
//				annotations.addLine("@Size( min = " + min + ", max = " + max + " )");
//			}
//			else
//			{
//				annotations.addLine("@Size( max = " + max + " )");
//			}
//		}
//		else
//		{
//			if ( ! StrUtil.nullOrVoid ( _attribute.getMinLength() ) )
//			{
//				min = _attribute.getMinLength().trim() ;
//				annotations.addLine("@Size( min = " + min + " )");
//			}
//		}
		
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


	private void annotationPattern(AnnotationsBuilder annotations)
	{
		if ( ! StrUtil.nullOrVoid ( _attribute.getPattern() ) )
		{
			annotations.addLine("@Pattern( regexp = \"" + _attribute.getPattern() + "\" )");
		}
	}
	
	private void annotationMin(AnnotationsBuilder annotations)
	{
		if ( ! StrUtil.nullOrVoid ( _attribute.getMinValue() ) )
		{
			String min = _attribute.getMinValue().trim() ;
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
		if ( ! StrUtil.nullOrVoid ( _attribute.getMaxValue() ) )
		{
			String max = _attribute.getMaxValue().trim() ;
			if ( max.indexOf('.') >=0 ) {
				annotations.addLine("@DecimalMax( value = \"" + max + "\" )");
			}
			else {
				annotations.addLine("@Max( value=" + max + " )");
			}
		}
	}
	
}
