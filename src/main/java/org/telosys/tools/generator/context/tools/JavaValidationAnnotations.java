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

/**
 * Java "Bean Validation" annotations for a field/attribute  ( JSR 303 strict ) 
 * 
 * @author Laurent GUERIN
 *
 */
public class JavaValidationAnnotations
{
	private final AttributeInContext attribute ;
	
	/**
	 * Constructor 
	 * @param attribute
	 */
	public JavaValidationAnnotations(AttributeInContext attribute) {
		super();
		this.attribute = attribute;
	}

	public boolean hasValidationAnnotations() {
		AnnotationsBuilder annotationsBuilder = buildValidationAnnotations(0);
		return annotationsBuilder.getCount() > 0;
	}

	/**
	 * Returns the validation annotations grouped in a single line 
	 * @param leftMargin
	 * @return
	 */
	public String getValidationAnnotations(int leftMargin) {
		AnnotationsBuilder annotationsBuilder = buildValidationAnnotations(leftMargin);
		return annotationsBuilder.getSingleLineAnnotations();
	}

	/**
	 * Returns the multiline validation annotations  
	 * @param leftMargin
	 * @return
	 */
	public String getValidationAnnotationsMultiline(int leftMargin) {
		AnnotationsBuilder annotationsBuilder = buildValidationAnnotations(leftMargin);
		return annotationsBuilder.getMultiLineAnnotations();
	}

	private AnnotationsBuilder buildValidationAnnotations( int leftMargin ) {
		//--- Reset everything at each call 
		AnnotationsBuilder annotationsBuilder = new AnnotationsBuilder(leftMargin);

		//--- "@NotNull" annotation only if not a primitive type
		if ( attribute.isNotNull() &&  !JavaTypeUtil.isPrimitiveType(attribute.getFullType()) ) {
			// "@NotNull" annotation is useless for a primitive type
			annotationsBuilder.addAnnotation("@NotNull" );
		}
		//--- Build other annotations depending on the Java type
		addOtherAnnotations(annotationsBuilder);

		return annotationsBuilder;
	}

//	/**
//	 * Returns the validation annotations based on the 'wrapper type' 
//	 * @param iLeftMargin
//	 * @return
//	 */
//	public String getValidationAnnotationsForWrapperType( int iLeftMargin ) {
//		//--- Reset everything at each call 
//		AnnotationsBuilder annotations = new AnnotationsBuilder(iLeftMargin);
//
//		//--- Build annotations 
//		if ( attribute.isNotNull() ) {
//			annotations.addAnnotation("@NotNull" ); // Wrapper type => don't care if primitive type 
//		}
//		//--- Other annotations depending on the Java type
//		addOtherAnnotations(annotations);
//
//		return annotations.getMultiLineAnnotations() ;
//	}
	
	/**
	 * Defines all required annotations for the current attribute ( except '@NotNull' annotation ) 
	 * @param annotationsBuilder
	 */
	private void addOtherAnnotations(AnnotationsBuilder annotationsBuilder ) {
		String javaFullType = attribute.getFullType(); // primitive or full type ( eg "long" or "java.lang.Long" )		
		//--- Annotations for each type category 
		if ( JavaTypeUtil.isStringType( javaFullType ) ) {
			// String annotations
			annotationNotEmpty(annotationsBuilder);
			annotationNotBlank(annotationsBuilder);
			annotationSize(annotationsBuilder);
			annotationPattern(annotationsBuilder);
		}
		else if ( JavaTypeUtil.isNumberType( javaFullType ) ) {
			// Number annotations
			annotationMin(annotationsBuilder);
			annotationMax(annotationsBuilder);
		}
		else if ( JavaTypeUtil.isTemporalType(javaFullType ) ) {
			// Temporal annotations
			if ( attribute.hasDatePastValidation() ) {
				annotationsBuilder.addAnnotation("@Past" );
			}
			if ( attribute.hasDateFutureValidation() ) {
				annotationsBuilder.addAnnotation("@Future" );
			}
		}
	}

	private String minSize() {
		if ( ! StrUtil.nullOrVoid ( attribute.getMinLength() ) ) {
			return attribute.getMinLength().trim() ;
		}
		return null ;
	}
	private String maxSize() {
		if ( ! StrUtil.nullOrVoid ( attribute.getMaxLength() ) ) {
			return attribute.getMaxLength().trim() ;
		}
		return null ;
	}
	
	private void annotationSize(AnnotationsBuilder annotationsBuilder) {
		String minSize = minSize();
		String maxSize = maxSize();
		if (minSize != null && maxSize != null) {
			annotationsBuilder.addAnnotation("@Size(min=" + minSize + ",max=" + maxSize + ")");
		} else if (minSize != null) {
			annotationsBuilder.addAnnotation("@Size(min=" + minSize + ")");
		} else if (maxSize != null) {
			annotationsBuilder.addAnnotation("@Size(max=" + maxSize + ")");
		}
	}

	private void annotationNotEmpty(AnnotationsBuilder annotationsBuilder) {
		if ( attribute.isNotEmpty()) {
			annotationsBuilder.addAnnotation("@NotEmpty");
		}
	}
	
	private void annotationNotBlank(AnnotationsBuilder annotationsBuilder) {
		if ( attribute.isNotBlank() ) {
			annotationsBuilder.addAnnotation("@NotBlank");
		}
	}
	
	private void annotationPattern(AnnotationsBuilder annotationsBuilder) {
		if ( ! StrUtil.nullOrVoid ( attribute.getPattern() ) ) {
			annotationsBuilder.addAnnotation("@Pattern(regexp=\"" + attribute.getPattern() + "\")");
		}
	}

	private void annotationMin(AnnotationsBuilder annotationsBuilder) {
		if ( ! StrUtil.nullOrVoid ( attribute.getMinValue() ) )	{
			String min = attribute.getMinValue().trim() ;
			if ( min.indexOf('.') >= 0 ) {
				annotationsBuilder.addAnnotation("@DecimalMin(\"" + min + "\")");
			}
			else {
				annotationsBuilder.addAnnotation("@Min(" + min + ")");
			}
		}
	}
	
	private void annotationMax(AnnotationsBuilder annotationsBuilder) {
		if ( ! StrUtil.nullOrVoid ( attribute.getMaxValue() ) )	{
			String max = attribute.getMaxValue().trim() ;
			if ( max.indexOf('.') >= 0 ) {
				annotationsBuilder.addAnnotation("@DecimalMax(\"" + max + "\" )");
			}
			else {
				annotationsBuilder.addAnnotation("@Max(" + max + ")");
			}
		}
	}
	
}
