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
package org.telosys.tools.generator.task;

/**
 * Task monitor interface 
 * 
 * @author L. Guerin
 *
 */
public interface ITaskMonitor {
	  
	  public abstract void beginTask(String arg0, int arg1);
	  
	  public abstract void done();
	  
	  //public abstract void internalWorked(double arg0);
	  
	  public abstract boolean isCanceled();
	  
	  public abstract void setCanceled(boolean arg0);
	  
	  public abstract void setTaskName(String arg0);
	  
	  public abstract void subTask(String arg0);
	  
	  public abstract void worked(int arg0);
}
