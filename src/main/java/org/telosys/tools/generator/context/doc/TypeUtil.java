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

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;


/**
 * Utility class for generic types ( set of static methods )
 * 
 * @author Laurent GUERIN
 *  
 */
public final class TypeUtil
{

    //----------------------------------------------------------------------------------------------
    /**
     * Private constructor to avoid instance creation
     */
    private TypeUtil()
    {
    }

    //----------------------------------------------------------------------------------------------
	private static void log(String s) {
		//System.out.println("[LOG:TypeUtil] " + s );
	}
	
    //----------------------------------------------------------------------------------------------
	/**
	 * Returns the upper or lower bound for the given type
	 * @param wildcardType
	 * @return
	 */
	private static String getBound(WildcardType wildcardType) {
		Type[] lowerBounds = wildcardType.getLowerBounds();
		Type[] upperBounds = wildcardType.getUpperBounds();
		// Upper bound and lower bound cannot coexist in the same declaration
		if ( lowerBounds.length > 0 ) {
			if ( lowerBounds.length == 1 ) {
				return " super " + typeToString( lowerBounds[0] ) ;
			}
			else {
				throw new RuntimeException("Multiple 'lower bounds' not supported");
			}
		}
		else if ( upperBounds.length > 0 ) {
			String sBoundType = typeToString( upperBounds[0] ) ;
			if ( upperBounds.length == 1 ) {
				if ( ! ( "Object".equals(sBoundType) ) ) {
					return " extends " + typeToString( upperBounds[0] ) ;
				}
				else {
					return "" ; // "?" same as "? extends Object"
				}
			}
			else {
				throw new RuntimeException("Multiple 'upper bounds' not supported");
			}
		}
		return "" ;
	}
	
    //----------------------------------------------------------------------------------------------
	public static String typeToString(Type type) {
		StringBuilder sb = new StringBuilder();
		
		//----- CASE 1 : ParameterizedType : List<String>, Set<Object>, ...
		if ( type instanceof ParameterizedType ) {
			// eg : List<String>, Set<Object>, ...
			ParameterizedType parameterizedType = (ParameterizedType) type ;
			log(" . ParameterizedType : " + parameterizedType );
			
			//--- Owner type if any ( eg : if this type is O<T>.I<S>, return a representation of O<T> )
			Type ownerType = parameterizedType.getOwnerType() ;
			if ( ownerType != null ) {
				sb.append( typeToString(type) ) ;
				sb.append( "." ) ;
			}
			
			//--- Basic type ( eg "List" for "List<String>" )
			Type rawType = parameterizedType.getRawType();
//			Class<?> rawClass = (Class<?>) rawType ; // it's always an instance of Class<?> (only implementation of Type)
//			sb.append ( rawClass.getSimpleName() ) ;
			sb.append( typeToString(rawType) ) ;
			
			//--- Param types
			sb.append("<") ;
			Type[] actualArgs = parameterizedType.getActualTypeArguments() ;
			int n = 0 ;
			for ( Type t : actualArgs ) {
				if ( n > 0 ) sb.append(",");
				sb.append( typeToString(t) ) ;
				n++;
			}
			sb.append(">") ;			
		}
		
		//----- CASE 2 : WildcardType : List<?>, Set<? extends Number>, ...
		else if ( type instanceof WildcardType ) {
			// eg : List<?>, Set<? extends Number>, ...			
			WildcardType wildcardType = (WildcardType) type;
			log(" . WildcardType : " + wildcardType );
			sb.append("?");
//			Type boundType;
//			// Only ONE bound ( super bound or lower bound )
//			if (wildcardType.getLowerBounds().length != 0) {
//				sb.append(" super ");
//				boundType = wildcardType.getLowerBounds()[0];
//			} else {
//				sb.append(" extends ");
//				boundType = wildcardType.getUpperBounds()[0];
//			}
			sb.append( getBound(wildcardType) ) ;
		}

		//----- CASE 3 : GenericArrayType 
		else if ( type instanceof GenericArrayType ) {
			// TODO
			throw new RuntimeException("GenericArrayType not yet supported");
		}
		
		//----- CASE 4 : TypeVariable 
		else if (type instanceof TypeVariable<?>) {
			// TODO
			throw new RuntimeException("TypeVariable not yet supported");
		}
		
		//----- CASE 5 : Simple standard Class 
		else if ( type instanceof Class ) {
			Class<?> clazz = (Class<?>)type  ;
			log(" . Class : " + clazz );
			sb.append ( clazz.getSimpleName() ) ;
		}
		
		return sb.toString();
	}
}