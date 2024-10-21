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
package org.telosys.tools.generator;

import java.util.Map;

import org.telosys.tools.commons.bundles.TargetDefinition;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.generator.context.Target;
import org.telosys.tools.generic.model.Entity;
import org.telosys.tools.generic.model.Model;

/**
 * Constants for variable names 
 * 
 * @author Laurent GUERIN
 * @since 4.2.0
 */
public class TargetBuilder {

	/**
	 * Private constructor
	 */
	private TargetBuilder() {
	}
	
	/**
	 * Builds a new Target instance
	 * @param telosysToolsCfg
	 * @param targetDefinition
	 * @param bundleName
	 * @param model
	 * @param entity
	 * @return
	 */
	public static Target buildTarget(TelosysToolsCfg telosysToolsCfg, TargetDefinition targetDefinition, String bundleName, Model model, Entity entity) {
		Map<String,String> variables = telosysToolsCfg.getAllVariablesMap();
		if ( bundleName != null ) {
			variables.put(Target.VAR_BUN, bundleName); // '_LC' and '_UC' set in Target
		}
		if ( model != null ) {
			variables.put(Target.VAR_MOD, model.getName()); // '_LC' and '_UC' set in Target
		}
		if ( entity != null ) {
			// Target with entity ('ENT' var is set dynamically in Target)
			return new Target(telosysToolsCfg.getDestinationFolderAbsolutePath(), targetDefinition, variables, entity); 
		}
		else {
			// Target without entity
			return new Target(telosysToolsCfg.getDestinationFolderAbsolutePath(), targetDefinition, variables); 
		}
	}
	
	/**
	 * Builds a new Target instance without entity
	 * @param telosysToolsCfg
	 * @param targetDefinition
	 * @param bundleName
	 * @param model
	 * @return
	 */
	public static Target buildTarget(TelosysToolsCfg telosysToolsCfg, TargetDefinition targetDefinition, String bundleName, Model model) {
		return buildTarget(telosysToolsCfg, targetDefinition, bundleName, model, null);
	}

	/**
	 * Builds a new Target instance without model (so without entity)
	 * @param telosysToolsCfg
	 * @param targetDefinition
	 * @param bundleName
	 * @return
	 */
	public static Target buildTarget(TelosysToolsCfg telosysToolsCfg, TargetDefinition targetDefinition, String bundleName) {
		return buildTarget(telosysToolsCfg, targetDefinition, bundleName, null, null);
	}
}
