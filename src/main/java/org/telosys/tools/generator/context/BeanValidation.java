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
package org.telosys.tools.generator.context;

import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.names.ContextName;
import org.telosys.tools.generator.context.tools.AnnotationsForBeanValidation;

//-------------------------------------------------------------------------------------
@VelocityObject(
		contextName=ContextName.BEAN_VALIDATION,
		text = { 
				"Object providing a set of utility functions for Java Bean Validation (JSR-303) annotations",
				""
		},
		since = "2.0.7"
 )
//-------------------------------------------------------------------------------------
public class BeanValidation {

	//-------------------------------------------------------------------------------------------------------------
	// ANNOTATIONS FOR FIELDS
	//-------------------------------------------------------------------------------------------------------------
	
	@VelocityMethod(
		text={	
			"Returns the 'Bean Validation' JSR-303 annotations for the given field (with a left margin)"
			},
		example={ 
			"$beanValidation.fieldAnnotations( 4, $attribute )" },
		parameters = { 
			"leftMargin : the left margin (number of blanks) ",
			"attribute : the attribute to be annotated "
			},
		since = "2.0.7"
	)
	public String annotations(int iLeftMargin, AttributeInContext attribute )
    {
		AnnotationsForBeanValidation annotations = new AnnotationsForBeanValidation(attribute);
		return annotations.getValidationAnnotations(iLeftMargin );
    }

	//-------------------------------------------------------------------------------------------------------------
}
