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
package org.telosys.tools.generator;

public class GeneratorUtil {

	//-------------------------------------------------------------
    /**
     * Return a string composed of iSize blanks
     * @param iSize
     * @return the blanks string
     */
	public static String blanks(int iSize)
    {
        String sTrailingBlanks = "";
        if (iSize > 0) // if needs trailing blanks
        {
            StringBuffer sb = new StringBuffer();
            for ( int i = 0 ; i < iSize ; i++ )
            {
                sb.append(' ');
            }
            sTrailingBlanks = sb.toString();
        }
        return sTrailingBlanks;
    }
    
	/**
	 * Return a directory path with the given directory + the given 
	 * @param sDestDir file system directory ( ie "/tmp/src" )
	 * @param sPackage package name ( ie "org.demo.vo.bean" )
	 * @return full path of the class file ( ie "/tmp/src/org/demo/vo/bean" )
	 */
	public static String packageToDirectory(String sDestDir, String sPackage) 
	{
		// --- Replace . by / in the path
		String sPackageDir = sPackage.replace('.', '/');
		if ( sDestDir.endsWith("/") || sDestDir.endsWith("\\") )
		{
			return sDestDir + sPackageDir;
		}
		else
		{
			return sDestDir + "/" + sPackageDir;
		}
	}
	
}
