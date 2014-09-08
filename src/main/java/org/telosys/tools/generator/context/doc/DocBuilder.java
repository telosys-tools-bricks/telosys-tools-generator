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
package org.telosys.tools.generator.context.doc;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.telosys.tools.generator.context.BeanValidation;
import org.telosys.tools.generator.context.Const;
import org.telosys.tools.generator.context.DatabaseInContext;
import org.telosys.tools.generator.context.DatabasesInContext;
import org.telosys.tools.generator.context.EmbeddedGenerator;
import org.telosys.tools.generator.context.EntityInContext;
import org.telosys.tools.generator.context.EnvInContext;
import org.telosys.tools.generator.context.Fn;
import org.telosys.tools.generator.context.GenerationInContext;
import org.telosys.tools.generator.context.Java;
import org.telosys.tools.generator.context.AttributeInContext;
import org.telosys.tools.generator.context.ForeignKeyInContext;
import org.telosys.tools.generator.context.ForeignKeyColumnInContext;
import org.telosys.tools.generator.context.JoinColumnInContext;
import org.telosys.tools.generator.context.Jpa;
import org.telosys.tools.generator.context.LinkAttributeInContext;
import org.telosys.tools.generator.context.LinkInContext;
import org.telosys.tools.generator.context.Loader;
import org.telosys.tools.generator.context.ModelInContext;
import org.telosys.tools.generator.context.ProjectInContext;
import org.telosys.tools.generator.context.Target;
import org.telosys.tools.generator.context.Today;

public class DocBuilder {

	private static List<String> objectClassMethods = new LinkedList<String>() ;
	{
		Method[] methods = Object.class.getMethods();
		for ( Method m : methods ) {
			objectClassMethods.add(m.getName());
		}
	}
	
	private boolean isObjectClassMethod(Method method) {
		String name = method.getName() ;
		for ( String s : objectClassMethods ) {
			if ( s.equals(name) ) {
				return true ; 
			}
		}
		return false ;
	}
	
	/**
	 * Returns the Velocity name for the given method <br>
	 * ie : "tab" for "getTab"
	 * @param method
	 * @return
	 */
	private String getVelocityMethodName(Method method) {
		String name = method.getName() ;
		if ( name.startsWith("get") ) {
			if ( method.getParameterTypes().length == 0 ) {
				String s = name.substring(3);
				if ( method.getAnnotation(VelocityConstant.class) != null ) {
					// Annotated as a constant : keep the name as is
					return s ;
				}
				else {
					// Converts first char to lower case ( ie "Tab" to "tab" )
					byte[] bytes = s.getBytes();
					byte first = bytes[0];
					if ( first >= 'A' && first <= 'Z' ) {
						bytes[0] = (byte) ( first + 'a' - 'A' ) ;
					}
					return new String(bytes);
				}
			}
		}
		return name ;
	}
	
	
	private String getVelocityReturnType(Method method) {
		
		VelocityReturnType velocityReturnType = method.getAnnotation(VelocityReturnType.class);
		if ( velocityReturnType != null ) {
			//--- There's an annotation for a specific return type => use it
			return velocityReturnType.value() ;
		}
		else {
			//--- Get the Java return type
			Type type = method.getGenericReturnType();
			return TypeUtil.typeToString(type);
		}
	}
	
	private MethodParameter buildParameter(int i, Class<?> paramClass, VelocityMethod docAnnotation) {
		String paramType = paramClass.getSimpleName();
		String paramDoc  = null ;
		if ( docAnnotation != null ) {
			String[] parametersDoc = docAnnotation.parameters();
			if ( parametersDoc != null && i < parametersDoc.length ) {
				paramDoc = parametersDoc[i] ;
			}
		}
		if ( paramDoc != null ) {
			return new MethodParameter(paramType, paramDoc );
		}
		else {
			return new MethodParameter(paramType);
		}
	}
	
	private MethodInfo getMethodInfo(Method method) {
		
		if ( method.getAnnotation(VelocityNoDoc.class) != null ) {
			// No documentation for this method 
			return null ;
		}
		
		MethodInfo methodInfo = new MethodInfo();
		
		//--- Original Java method name 
		methodInfo.setJavaName( method.getName() );
		
		//--- Velocity method name 
		methodInfo.setVelocityName( getVelocityMethodName(method) );
		
		//--- Return type  
		methodInfo.setReturnType( getVelocityReturnType(method) );
		

		LinkedList<MethodParameter> parameters = new LinkedList<MethodParameter>();
		//--- Documentation  
		VelocityMethod docAnnotation = method.getAnnotation(VelocityMethod.class);
		if ( docAnnotation != null ) {
			methodInfo.setDocText( docAnnotation.text() );
			methodInfo.setExampleText( docAnnotation.example() );
			methodInfo.setSince( docAnnotation.since() );
			methodInfo.setDeprecated( docAnnotation.deprecated() );	
		}

		//--- Deprecated defined with standard Java annotation ?  
		Deprecated deprecatedAnnotation = method.getAnnotation(Deprecated.class);
		if ( deprecatedAnnotation != null ) {
			methodInfo.setDeprecated( true );	
		}

		//--- Parameters types
		Class<?>[] paramTypes = method.getParameterTypes();
		if ( paramTypes != null ) {
			int i = 0 ;
			for ( Class<?> paramClass : paramTypes ) {
				parameters.add( buildParameter(i, paramClass, docAnnotation) );
				i++ ;
			}
		}
		methodInfo.setParameters(parameters);
		
		return methodInfo ;
	}
	
	public ClassInfo getClassInfo(Class<?> clazz) {
		
		ClassInfo classInfo = new ClassInfo();
		
		//--- Class name 
		classInfo.setJavaClassName( clazz.getSimpleName() );
		
		//--- Documentation  
		VelocityObject docAnnotation = clazz.getAnnotation(VelocityObject.class);
		if ( docAnnotation != null ) {
			
			//--- Class 
			classInfo.setContextName( docAnnotation.contextName() );
			classInfo.setOtherContextName( docAnnotation.otherContextNames() );
			classInfo.setDocText( docAnnotation.text() );
			classInfo.setSince( docAnnotation.since() );
			classInfo.setDeprecated( docAnnotation.deprecated() );			
			classInfo.setExampleText( docAnnotation.example() );

			//--- Methods
			Method[] methods = clazz.getMethods() ;
			for ( Method method : methods ) {
				int modifiers = method.getModifiers();
				if ( Modifier.isPublic(modifiers) ) {
					if ( false == isObjectClassMethod(method) ) {
						MethodInfo methodInfo = getMethodInfo(method);
						if ( methodInfo != null ) {
							classInfo.addMethodInfo(methodInfo);
						}
					}
				}
			}
		}
		else {
			throw new RuntimeException("No documentation annotation for class '" + clazz.getSimpleName() + "'");
		}
		
		return classInfo ;
	}

	private final static Class<?>[] velocityClasses = new Class<?>[] {
		Const.class,
		EmbeddedGenerator.class,
		Fn.class,
		GenerationInContext.class, // ver 2.1.0
		Java.class, // ver 2.0.7
		Jpa.class, // ver 2.0.7
		BeanValidation.class, // ver 2.0.7
		//JavaBeanClass.class,
		EntityInContext.class, // replaces JavaBeanClass.class ( ver 2.1.0 )
		AttributeInContext.class,
		ForeignKeyInContext.class, // ver 2.0.7
		ForeignKeyColumnInContext.class, // ver 2.0.7
		JoinColumnInContext.class, // ver 2.1.0
		LinkInContext.class,
		LinkAttributeInContext.class, // ver 2.1.0
		Loader.class,
		//Model.class, // ver 2.0.7
		ModelInContext.class, // ver 2.1.0
		DatabasesInContext.class, // ver 2.1.0
		DatabaseInContext.class, // ver 2.1.0
		//ProjectConfiguration.class,
		ProjectInContext.class, // ver 2.1.0
		Target.class,
		Today.class,
		EnvInContext.class, // ver 2.1.0
	};
	
	public Map<String,ClassInfo> getVelocityClassesInfo() {
		
		Map<String,ClassInfo> map = new Hashtable<String, ClassInfo>();
		
		//--- Build class information for each Java class used in the Velocity context
		for ( Class<?> clazz : velocityClasses ) {
			ClassInfo classInfo = getClassInfo(clazz);
			//--- Main context name
			map.put(classInfo.getContextName(), classInfo);
			//--- Other context names (if any)
			for ( String name : classInfo.getOtherContextName() ) {
				map.put(name, classInfo);
			}
		}
		
		return map ;
	}
}
