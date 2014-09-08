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

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class DocGenerator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String userDir =  System.getProperty("user.dir") ;
		System.out.println( "USER DIR : " + userDir );
		// USER DIR is "X:\xxx\xxx\workspace\project"
		String destDir = userDir + "/target/doc/html/objects/" ;
		System.out.println( "DEST DIR : " + destDir );
		File fileDir = new File(destDir);
		if ( ! fileDir.exists() )
		{
		    System.out.println("Creating directory : " + destDir);
		    if( fileDir.mkdirs() ){    
		    	System.out.println("Created");  
		    }
		    else {
		    	System.out.println("ERROR : Cannot create directory !");
		    }
		}		
		
		DocBuilder docBuilder = new DocBuilder();
		
		Map<String,ClassInfo> classesInfo = docBuilder.getVelocityClassesInfo() ;

		Set<String> names = classesInfo.keySet();
		System.out.println("ClassInfo names (size=" + names.size() + ") : " );
		for ( String name : names ) {
			System.out.println(" . " + name );
		}
		
		List<String>sortedNames = sortList(names);
		
		DocGeneratorHTML htmlGenerator = new DocGeneratorHTML();
		
		System.out.println("Sorted context names (size=" + sortedNames.size() + ") : " );
		for ( String name : sortedNames ) {
			ClassInfo classInfo = classesInfo.get(name);
					//+ classInfo.getJavaClassName() + " " + classInfo.getMethodsCount() + " methods");
			String fileName = destDir + classInfo.getContextName() + ".html" ;
			System.out.println(" . " + name + " (" + classInfo.getContextName() + ") --> " + fileName );
			htmlGenerator.generateDocFile(classInfo, fileName);
		}
	}
	
	public static <T extends Comparable<? super T>> List<T> sortList(Collection<T> c) {
		  List<T> list = new ArrayList<T>(c);
		  java.util.Collections.sort(list);
		  return list;
	}
}
