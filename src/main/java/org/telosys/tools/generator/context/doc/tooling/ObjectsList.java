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
package org.telosys.tools.generator.context.doc.tooling;

import org.telosys.tools.generator.context.AttributeInContext;
import org.telosys.tools.generator.context.BeanValidation;
import org.telosys.tools.generator.context.BundleInContext;
import org.telosys.tools.generator.context.Const;
import org.telosys.tools.generator.context.EmbeddedGenerator;
import org.telosys.tools.generator.context.EntityInContext;
import org.telosys.tools.generator.context.EnvInContext;
import org.telosys.tools.generator.context.FactoryInContext;
import org.telosys.tools.generator.context.FileInContext;
import org.telosys.tools.generator.context.FnInContext;
import org.telosys.tools.generator.context.ForeignKeyInContext;
import org.telosys.tools.generator.context.ForeignKeyPartInContext;
import org.telosys.tools.generator.context.H2InContext;
import org.telosys.tools.generator.context.HtmlInContext;
import org.telosys.tools.generator.context.Java;
import org.telosys.tools.generator.context.JdbcFactoryInContext;
import org.telosys.tools.generator.context.JdbcInContext;
import org.telosys.tools.generator.context.JpaInContext;
import org.telosys.tools.generator.context.LinkInContext;
import org.telosys.tools.generator.context.Loader;
import org.telosys.tools.generator.context.ModelInContext;
import org.telosys.tools.generator.context.NowInContext;
import org.telosys.tools.generator.context.ProjectInContext;
import org.telosys.tools.generator.context.SqlInContext;
import org.telosys.tools.generator.context.Target;
import org.telosys.tools.generator.context.Today;
import org.telosys.tools.generator.context.ValuesInContext;

/**
 * Provides a list of classes for all objects defined in the generator context <br>
 * 
 * This list is used to generate the reference documentation<br>
 * so it must be updated if an object is added or removed in the generator context.<br>
 * 
 * @author L. Guerin
 *
 */
public class ObjectsList {

	private ObjectsList() {}

	private static final Class<?>[] templatesObjectsClasses = new Class<?>[] {
		Const.class,
		EmbeddedGenerator.class,
		FnInContext.class,
		// GenerationInContext.class, // ver 2.1.0 // removed in v 3.0.0
		Java.class, // ver 2.0.7
		JpaInContext.class, // ver 2.0.7
		BeanValidation.class, // ver 2.0.7
		EntityInContext.class, // replaces JavaBeanClass.class ( ver 2.1.0 )
		AttributeInContext.class,
		ForeignKeyInContext.class, // ver 2.0.7
		//ForeignKeyColumnInContext.class, // ver 2.0.7 // removed in  v 3.4.0
		//ForeignKeyAttrubuteInContext.class, // v 3.4.0 // removed in  v 3.4.0
		// JoinColumnInContext.class, // ver 2.1.0 // removed in  v 3.4.0
		//JoinTableInContext.class, // ver 2.1.0  (added to list in v 3.3.0) // removed in  v 3.4.0
		LinkInContext.class,
		// LinkAttributesPairInContext.class, // ver 2.1.0 // removed in  v 3.4.0
		Loader.class,
		ModelInContext.class, // ver 2.1.0
//		DatabasesInContext.class, // removed in v 3.4.0
//		DatabaseInContext.class, // removed in v 3.4.0
		ProjectInContext.class, // ver 2.1.0
		Target.class,
		Today.class,
		EnvInContext.class, // ver 2.1.0
		JdbcInContext.class, // ver 2.1.1
		JdbcFactoryInContext.class, // ver 2.1.1
		H2InContext.class, // ver 2.1.1
		
		HtmlInContext.class, // v 3.0.0
		ValuesInContext.class, // v 3.0.0

		NowInContext.class, // v 3.3.0
		BundleInContext.class, // v 3.3.0
		FileInContext.class, // v 3.3.0
		ForeignKeyPartInContext.class, // v3.3.0
		
		SqlInContext.class,    // v3.4.0
		FactoryInContext.class // v3.4.0
	};

	public static final Class<?>[] getObjectsClasses() {
		return templatesObjectsClasses ;
	}

}
