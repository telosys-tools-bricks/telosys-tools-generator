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
package org.telosys.tools.generator.context;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.names.ContextName;

/**
 * The current system date and time
 *  
 * @author Laurent GUERIN
 *
 */
//-------------------------------------------------------------------------------------
@VelocityObject(
		contextName= ContextName.NOW ,
		text = "Object providing the current system date and time in different formats",
		since = "3.3.0"
 )
//-------------------------------------------------------------------------------------
public class NowInContext {
    
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the current date with the default format 'yyyy-MM-dd' ",
			"example : '2019-07-14' "
		},
		example={ 
			"$now.date" 
		}
	)
    public String getDate() {
        return newFormattedDate("yyyy-MM-dd");
    }

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the current time with the default format 'HH:mm:ss' ",
			"example : '14:55:34' "
		},
		example={ 
			"$now.time" 
		}
	)
    public String getTime() {
        return newFormattedDate("HH:mm:ss");
    }
    
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the current date and time with the default format 'yyyy-MM-dd HH:mm:ss' ",
			"example : '2019-07-14 14:55:34' "
		},
		example={ 
			"$now.datetime" 
		}
	)
    public String getDatetime() {
        return newFormattedDate("yyyy-MM-dd HH:mm:ss");
    }
    
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the current date/time formatted with the given format"
		},
		example={ 
			"$now.format('yyyy-MM')" 
			},
		parameters={
			"format : the desired format ",
			"  the format is based on Java date/time format (cf 'SimpleDateFormat' JavaDoc) "
		}
	)
    public String format(String sFormat ) {
        return newFormattedDate(sFormat);
    }
	
	//-------------------------------------------------------------------------------------
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 * if $now (without mathod) in template 
	 */
	@Override
	public String toString() {
		return getDatetime();
	}
	
	//-------------------------------------------------------------------------------------
    private String newFormattedDate( String sFormat ) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(sFormat);
        return simpleDateFormat.format( new Date() );
    }

}