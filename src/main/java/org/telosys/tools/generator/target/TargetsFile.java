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
package org.telosys.tools.generator.target;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.generator.GeneratorException;

/**
 * Targets file loader 
 * 
 * @author L. Guerin
 *
 */
public class TargetsFile {
	
    private static final int  BUFFER_SIZE  = 2048;
	
    private final String targetsFileName ;
    
    /**
     * Constructor 
	 * @param fileName
	 */
	public TargetsFile(String fileName) {
		super();
		targetsFileName = fileName;
	}

	/**
	 * Check file existence
	 * @return true if the file exists, else false
	 */
	public boolean exists() {
		File file = new File(targetsFileName);
		return file.exists() ;
	}
	
	/**
	 * Loads the targets definitions from the file
	 * @return
	 * @throws GeneratorException
	 */
	public List<TargetDefinition> load() throws GeneratorException
    {
    	List<TargetDefinition> list = null ;
        FileReader fr = getFileReader();
        if ( fr != null )
        {
        	boolean ioException = false ;
            BufferedReader br = new BufferedReader(fr, BUFFER_SIZE);
            try
            {
            	list = parse(br);
            } catch (IOException e)
            {
            	ioException = true ;
            } finally
            {
                close(br);
            }
            close(fr);
            if ( ioException ) {
            	throw new GeneratorException("IOException while reading file : '" + targetsFileName + "'");
            }
        }
        else {
        	throw new GeneratorException("Targets file '" + targetsFileName + "' not found");
        }
        return list ;
    }
    
    private List<TargetDefinition> parse(BufferedReader br) throws IOException
    {
    	LinkedList<TargetDefinition> list = new LinkedList<TargetDefinition>();
        //int iRowNum = 0;
        String sLine;
        String[] fields = null;
        while ((sLine = br.readLine()) != null)
        {
        	sLine = sLine.trim() ;
            //iRowNum++;
            if (sLine.length() > 0)
            {
                if ( ! isComment(sLine))
                {
                	fields = StrUtil.split(sLine, ';');
                	if ( fields.length >= 4 )
                	{
                    	if ( fields.length >= 5 ) {
                    		//--- Has a "once" indicator
                    		list.add( new TargetDefinition( fields[0].trim(), 
                    				fields[1].trim(), fields[2].trim(), fields[3].trim(), fields[4].trim() ) ) ;
                    	}
                    	else {
                    		//--- No "once" indicator (for backward compatibility)
                    		list.add( new TargetDefinition( fields[0].trim(), 
                    				fields[1].trim(), fields[2].trim(), fields[3].trim(), "" ) ) ;
                    	}
                	}
                }
            }
        }
        return list ;
    }
    
    private boolean isComment(String sLine)
    {
    	if ( sLine != null )
    	{
            if (sLine.trim().startsWith("#"))
            {
                return true;
            }
    	}
        return false;
    }

    /**
     * Return a FileReader for the current file name or null if the file doesn't exist
     * @return
     */
    private FileReader getFileReader()
    {
    	FileReader fr = null ;
    	if ( targetsFileName != null )
    	{
            try {
				fr = new FileReader(targetsFileName);
			} catch (FileNotFoundException e) {
				// Not an error // MsgBox.error("File '" + sFileName + "' not found");
				fr = null ;
			}
    	}
    	return fr; 
    }
    
    private void close(FileReader fr)
    {
    	if ( fr != null )
    	{
        	try {
    			fr.close();
    		} catch (IOException e) {
    			// Nothing todo
    		}
    	}
    }
    
    private void close(BufferedReader br)
    {
    	if ( br != null )
    	{
        	try {
    			br.close();
    		} catch (IOException e) {
    			// Nothing todo
    		}
    	}
    }
}
