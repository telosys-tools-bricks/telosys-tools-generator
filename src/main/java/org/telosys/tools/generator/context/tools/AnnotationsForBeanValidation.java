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
	 * Returns the validation annotations based on the 'actual type' 
	 * @param iLeftMargin
	 * @return
	 */
	public String getValidationAnnotations( int iLeftMargin ) {
		//--- Reset everything at each call 
		AnnotationsBuilder annotations = new AnnotationsBuilder(iLeftMargin);

		//--- Build Annotations 
		if ( _attribute.isNotNull() ) {
			//--- Only if not a primitive type ( @NotNull is useless for a primitive type ) 
			if ( JavaTypeUtil.isPrimitiveType( _attribute.getFullType() ) == false ) {
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
		if ( _attribute.isNotNull() ) {
			annotations.addLine("@NotNull" ); // Wrapper type => don't care if primitive type 
		}
		addOtherAnnotations(annotations);

		return annotations.getAnnotations() ;
	}
	
//	/**
//	 * Returns the validation annotations
//	 * @param iLeftMargin
//	 * @return
//	 */
//	public String getValidationAnnotations( int iLeftMargin ) {
//		//--- Reset everything at each call 
//		AnnotationsBuilder annotations = new AnnotationsBuilder(iLeftMargin);
//
//		String sJavaFullType = _attribute.getFullType() ;
//		
//		//--- Annotations for all categories 
//		if ( ! JavaTypeUtil.isPrimitiveType(sJavaFullType) ) 
//		{
//			if ( _attribute.isNotNull() ) 
//			{
//				annotations.addLine("@NotNull" );
//			}
//		}
//
//		//--- Annotations for each type category 
//		if ( JavaTypeUtil.isCategoryBoolean( sJavaFullType ) )
//		{
//			// Nothing to do !
//		}
//		else if ( JavaTypeUtil.isCategoryString( sJavaFullType ) )
//		{
//			annotationSize(annotations);
//			annotationPattern(annotations);
//		}
//		else if ( JavaTypeUtil.isCategoryNumber( sJavaFullType ) )
//		{
//			annotationMin(annotations);
//			annotationMax(annotations);
//		}
//		else if ( JavaTypeUtil.isCategoryDateOrTime( sJavaFullType ) )
//		{
//			if ( _attribute.hasDatePastValidation() ) {
//				annotations.addLine("@Past" );
//			}
//			if ( _attribute.hasDateFutureValidation() ) {
//				annotations.addLine("@Future" );
//			}
//		}
//		
//		return annotations.getAnnotations() ;
//	}
	
	/**
	 * Add all required annotations for the current attribute ( except '@NotNull' annotation ) 
	 * @param annotations
	 * @param sJavaFullType
	 */
	private void addOtherAnnotations(AnnotationsBuilder annotations ) {
		String sJavaFullType = _attribute.getFullType() ;
		
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
			if ( _attribute.hasDatePastValidation() ) {
				annotations.addLine("@Past" );
			}
			if ( _attribute.hasDateFutureValidation() ) {
				annotations.addLine("@Future" );
			}
		}
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
