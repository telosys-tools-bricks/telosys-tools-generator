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

import java.util.LinkedList;
import java.util.List;

public class MethodInfo implements Comparable<MethodInfo> {

	private final static String[] VOID_STRING_ARRAY = new String[0] ;
	
	private String   javaName     = "" ;
	private String   velocityName = "" ;
	private String   returnType   = "" ;
	private List<MethodParameter> parameters = new LinkedList<MethodParameter>() ;
	
	private String[] docText     = VOID_STRING_ARRAY ;
	private String[] exampleText = VOID_STRING_ARRAY ;
	private String   since       = "" ;
	private boolean  deprecated  = false ;
		
	//----------------------------------------------------------------------
	public String getJavaName() {
		return javaName;
	}
	protected void setJavaName(String javaName) {
		this.javaName = javaName;
	}
	
	//----------------------------------------------------------------------
	public String getVelocityName() {
		return velocityName;
	}
	protected void setVelocityName(String velocityName) {
		this.velocityName = velocityName;
	}
	
	//----------------------------------------------------------------------
	public String getReturnType() {
		return returnType;
	}
	protected void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	
	//----------------------------------------------------------------------
	public boolean hasParameters() {
		return ( parameters != null && parameters.size() > 0 ) ;
	}
	public List<MethodParameter> getParameters() {
		return parameters;
	}
	protected void setParameters(List<MethodParameter> parameters) {
		this.parameters = parameters;
	}
	
	//----------------------------------------------------------------------
	public String[] getDocText() {
		return docText;
	}
	protected void setDocText(String[] docText) {
		this.docText = docText;
	}
	
	//----------------------------------------------------------------------
	public boolean hasExampleText() {
		return exampleText != null && exampleText.length > 0 ;
	}
	public String[] getExampleText() {
		return exampleText;
	}
	protected void setExampleText(String[] text) {
		this.exampleText = text;
	}
	
	//----------------------------------------------------------------------
	public boolean hasSince() {
		return since != null && since.trim().length() > 0 ;
	}
	public String getSince() {
		return since;
	}
	protected void setSince(String since) {
		this.since = since;
	}
	
	//----------------------------------------------------------------------
	public boolean isDeprecated() {
		return deprecated;
	}
	protected void setDeprecated(boolean deprecated) {
		this.deprecated = deprecated;
	}

	//----------------------------------------------------------------------
	/**
	 * Returns true if this method is like an attribute (no parenthesis required)
	 * @return true if the method is a Java getter, else false
	 */
	public boolean isAttributeLike() {
		// In Velocity a method can be used as an attribute if it's a Java getter
		if ( this.javaName.startsWith("get") ) {
			if ( this.parameters.size() == 0 ) {
				return true ;
			}
		}
		return false ;
	}
	
	//----------------------------------------------------------------------
	public String getSimpleDescription() {
		StringBuilder sb = new StringBuilder();
		sb.append(velocityName);
		if ( ! isAttributeLike() ) {			
			sb.append("(");
			int i = 0 ;
			for ( MethodParameter p : parameters ) {
				if ( i > 0 ) sb.append(", ");
				sb.append( p.getType() );
				sb.append( " " );
				sb.append( p.getName() );					
				i++;
			}
			sb.append(")");
		}
		sb.append(" : ");
		sb.append(returnType);
		return sb.toString();
	}
	
	//----------------------------------------------------------------------
	/**
	 * Returns the method's signature (the unique identifier for each method in a class)
	 * @return
	 */
	public String getSignature() {
		StringBuilder sb = new StringBuilder();
		sb.append(velocityName);
		sb.append("#");
		if ( isAttributeLike() ) {
			sb.append("A");
		}
		else {
			sb.append("M");
			sb.append("(");
			int i = 0 ;
			for( MethodParameter p : parameters ) {
				if ( i > 0 ) sb.append(",");
				sb.append( p.getType() );
				i++;
			}
			sb.append(")");
		}
		return sb.toString();
	}
	
	//----------------------------------------------------------------------
	@Override
	public String toString() {
		return getSimpleDescription() ;
	}
	
	public int compareTo(MethodInfo methodInfo) {
//		//--- Compare the Velocity name
//		return this.getVelocityName().compareTo( methodInfo.getVelocityName() );
		//--- Compare the signature name
		return this.getSignature().compareTo( methodInfo.getSignature() );
	}
	
}
