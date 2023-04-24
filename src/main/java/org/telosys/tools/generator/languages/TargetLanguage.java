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
package org.telosys.tools.generator.languages;

import java.util.List;

import org.telosys.tools.generator.context.AttributeInContext;
import org.telosys.tools.generator.context.EnvInContext;
import org.telosys.tools.generator.languages.literals.LiteralValuesProvider;
import org.telosys.tools.generator.languages.types.TypeConverter;
import org.telosys.tools.generator.languages.types.TypeConverterForCPlusPlus;

/**
 *  
 * @author Laurent GUERIN
 *
 */
public abstract class TargetLanguage {
	
	private final TypeConverter         typeConverter;
	private final LiteralValuesProvider literalValuesProvider ;

	private EnvInContext env;
	
	/**
	 * Constructor
	 */
	protected TargetLanguage(TypeConverter typeConverter, LiteralValuesProvider literalValuesProvider) {
		super();
		this.typeConverter = typeConverter;
		this.literalValuesProvider = literalValuesProvider;
	}

	/**
	 * Setter to inject current '$env'
	 * @param env
	 */
	protected void setEnv(EnvInContext env) {
		this.env = env;
		this.typeConverter.setEnv(env);
	}
	/**
	 * Returns the current '$env'
	 * @return
	 */
	protected EnvInContext getEnv() {
		return this.env;
	}
	
	/**
	 * Returns the TypeConverter 
	 * @return
	 */
//	public abstract TypeConverter getTypeConverter();
	public final TypeConverter getTypeConverter() {
		return typeConverter;
	}

	/**
	 * Returns the LiteralValuesProvider 
	 * @return
	 */
//	public abstract LiteralValuesProvider getLiteralValuesProvider();
	public final LiteralValuesProvider getLiteralValuesProvider() {
		return literalValuesProvider;
	}
	
	
	/**
	 * Build simple arguments list (only argument names, no type) <br>
	 * Returns a list of arguments in the expected form for the current language<br>
	 * @param attributes
	 * @return
	 */
	public abstract String argumentsList( List<AttributeInContext> attributes ) ;

	/**
	 * Build arguments list with associated basic type <br>
	 * Returns a list of arguments with argument type and argument name in the expected form for the current language<br>
	 * @param attributes
	 * @return
	 */
	public abstract String argumentsListWithType( List<AttributeInContext> attributes ) ;

	/**
	 * Build arguments list with associated wrapper type <br>
	 * Returns a list of arguments with : argument type and argument name in the expected form for the current language <br>
	 * @param attributes
	 * @return
	 */
	public abstract String argumentsListWithWrapperType( List<AttributeInContext> attributes ) ;

	/**
	 * Build arguments list with object name + associated getter if any <br>
	 * @param objectName
	 * @param attributes
	 * @return
	 */
	public abstract String argumentsListFromObjectWithGetter( String objectName, List<AttributeInContext> attributes ) ;
	
	/**
	 * Builds a simple arguments list with only arguments name (no type)
	 * @param attributes
	 * @return
	 */
	protected final String commonArgumentsListWithoutType(List<AttributeInContext> attributes) {
		if ( attributes == null ) return "";
		// No type, example : "name, age"
		StringBuilder sb = new StringBuilder();
		int n = 0 ;
		for ( AttributeInContext field : attributes ) {
			// example : "name string, age int"
			if ( n > 0 ) sb.append(", ");
			sb.append( field.getName() ) ; 
			n++;
		}
		return sb.toString();
	}

	/**
	 * Builds arguments list with the form : "arg1-type arg1-name, arg2-type arg2-name, ..."
	 * @param attributes
	 * @return
	 */
	protected final String commonArgumentsListWithType( List<AttributeInContext> attributes ) {
		if ( attributes == null ) return "";
		StringBuilder sb = new StringBuilder();
		int n = 0 ;
		for ( AttributeInContext attribute : attributes ) {
			if ( n > 0 ) sb.append(", ");
			sb.append( attribute.getType() ) ; // arg type first
			sb.append( " " ) ;
			sb.append( attribute.getName() ) ; // arg name after type
			n++;
		}
		return sb.toString();
	}
	
	/**
	 * Builds arguments list with the form : "obj.arg1, obj.arg2, obj.arg3, ..."
	 * @param objectName
	 * @param attributes
	 * @return
	 */
	protected final String commonArgumentsListFromObject( String objectName, List<AttributeInContext> attributes ) {
		if ( attributes == null ) return "";
		StringBuilder sb = new StringBuilder();
		int n = 0 ;
		for ( AttributeInContext attribute : attributes ) {
			if ( n > 0 ) sb.append(", ");
			sb.append( objectName ) ;
			sb.append( "." ) ;
			sb.append( attribute.getName() ) ; 
			n++;
		}
		return sb.toString();		
	}
	
	/**
	 * Builds arguments list with the form : "obj.getArg1(), obj.isArg2(), obj.getArg3(), ..."
	 * @param objectName
	 * @param attributes
	 * @return
	 */
	protected final String commonArgumentsListFromObjectWithGetter( String objectName, List<AttributeInContext> attributes ) {
		if ( attributes == null ) return "";
		StringBuilder sb = new StringBuilder();
		int n = 0 ;
		for ( AttributeInContext attribute : attributes ) {
			if ( n > 0 ) sb.append(", ");
			sb.append( objectName ) ;
			sb.append( "." ) ;
			sb.append( attribute.getGetter() ) ; // can be 'getXxxx' or 'isXxxx'
			sb.append( "()" ) ;
			n++;
		}
		return sb.toString();
	}
}
