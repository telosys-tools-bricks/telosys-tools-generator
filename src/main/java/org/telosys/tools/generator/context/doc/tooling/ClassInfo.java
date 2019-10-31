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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class contains all information to be printed in the documentation 
 * 
 * @author L. Guerin
 *
 */
public class ClassInfo {

	private static final String[] VOID_TEXT = new String[0] ;
	
	private String   name ;
	
	private String   contextName ;
	private String[] otherContextNames ;
	private String[] docText ;
	private String   since ;
	private boolean  deprecated ;
	private String[] exampleText ;
	
	private Map<String,MethodInfo> methodsInfoMap  ;
	
	public ClassInfo() {
		super();
		name = "???" ;
		docText = VOID_TEXT;
		otherContextNames = VOID_TEXT ;
		since = "" ;
		deprecated = false ;
		exampleText = VOID_TEXT ;
		methodsInfoMap  = new HashMap<>();
	}
	
	public String getJavaClassName() {
		return name;
	}
	protected void setJavaClassName(String name) {
		this.name = name;
	}
	
	public String getContextName() {
		return contextName;
	}
	protected void setContextName(String contextName) {
		this.contextName = contextName;
	}
	
	public String[] getOtherContextName() {
		return otherContextNames;
	}
	protected void setOtherContextName(String[] otherContextNames) {
		this.otherContextNames = otherContextNames;
	}
	
	public String[] getDocText() {
		return docText;
	}
	protected void setDocText(String[] docText) {
		this.docText = docText;
	}
	
	public String[] getExampleText() {
		return exampleText;
	}
	protected void setExampleText(String[] exampleText) {
		this.exampleText = exampleText;
	}
	
	public String getSince() {
		return since;
	}
	protected void setSince(String since) {
		this.since = since;
	}
	
	public boolean isDeprecated() {
		return deprecated;
	}
	protected void setDeprecated(boolean deprecated) {
		this.deprecated = deprecated;
	}
	
	protected void addMethodInfo(MethodInfo methodInfo) {
		methodsInfoMap.put(methodInfo.getSignature(), methodInfo);
	}

	public List<MethodInfo> getMethodsInfo() {
		Collection<MethodInfo> methods = methodsInfoMap.values();
		List<MethodInfo> list = new LinkedList<>();
		for ( MethodInfo m : methods ) {
			list.add(m);
		}
		Collections.sort(list);
		return list ;
	}

	public MethodInfo getMethodInfo(String signature) {
		return methodsInfoMap.get(signature) ;
	}

	public int getMethodsCount() {
		return ( methodsInfoMap != null ? methodsInfoMap.size() : 0 ) ;
	}

	@Override
	public String toString() {
		return "ClassInfo : name=" + name + ", contextName=" + contextName 
				+ "\n docText=" + Arrays.toString(docText) 
				+ "\n since=" + since
				+ "\n deprecated=" + deprecated 
				+ "\n nb methodsInfo=" + methodsInfoMap.size()
				;
	}
}
