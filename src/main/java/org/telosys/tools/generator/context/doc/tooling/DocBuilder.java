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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.telosys.tools.generator.context.doc.VelocityConstant;
import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityNoDoc;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.doc.VelocityReturnType;

/**
 * Tool to retrieve all information to be printed in the documentation<br>
 * for all the objects.<br>
 * It builds a map with all the ClassInfo for each object name : Map<String,ClassInfo> <br>
 * 
 * @author L. Guerin
 *
 */
public class DocBuilder {

	private static List<String> objectClassMethods = new LinkedList<>() ;
	static {
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
		

		LinkedList<MethodParameter> parameters = new LinkedList<>();
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
	
	protected ClassInfo getClassInfo(Class<?> clazz) {
		
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

	/**
	 * Build a ClassInfo instance for each object <br>
	 * and return a Map containing all the ClassInfo instances ( "Object name" --> ClassInfo )
	 * 
	 * @return
	 */
	public Map<String,ClassInfo> getVelocityClassesInfo() {
		
		Map<String,ClassInfo> map = new HashMap<>();
		
		Class<?>[] velocityClasses = ObjectsList.getObjectsClasses() ;
		
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
