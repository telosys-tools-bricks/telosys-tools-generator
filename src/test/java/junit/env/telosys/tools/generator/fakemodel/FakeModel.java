/**
 *  Copyright (C) 2008-2015  Telosys project org. ( http://www.telosys.org/ )
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
package junit.env.telosys.tools.generator.fakemodel;

import java.util.ArrayList;
import java.util.List;

import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.generic.model.Entity;
import org.telosys.tools.generic.model.Model;
import org.telosys.tools.generic.model.enums.ModelType;

public class FakeModel implements Model
{
	private final String name ;
	
	private final String description ;
	
	private final int    databaseId ; 

	private final String databaseProductName ; 
	
	private final List<Entity> entities = new ArrayList<>();


	/**
	 * Constructor
	 * @param modelName
	 */
	public FakeModel(String modelName) {
		super();
		this.name = modelName;
		this.description = "Fake model" ;
		this.databaseId = 1 ; 
		this.databaseProductName = "Fake database"; 
	}

	//--------------------------------------------------------------------------------------
	@Override
	public ModelType getType() {
		return ModelType.OTHER ;
	}

	//--------------------------------------------------------------------------------------
	@Override
	public String getVersion() {
		return "1.0.0";
	}

	//--------------------------------------------------------------------------------------
	@Override
	public String getName() {
		// Cannot be null or void
		if ( StrUtil.nullOrVoid(name) ) {
			return "FakeModel" ;
		}
		else {
			return name ;
		}
	}
	
	@Override
	public String getFolderName() {
		return "";
	}

	@Override
	public String getTitle() {
		return "";
	}

	//--------------------------------------------------------------------------------------
	@Override
	public String getDescription() {
		return description ;
	}

	//--------------------------------------------------------------------------------------
	@Override
	public Integer getDatabaseId() {
		return databaseId;
	}

	//--------------------------------------------------------------------------------------
	@Override
	public String getDatabaseProductName() {
		return databaseProductName;
	}

	//-------------------------------------------------------------------------------
	// ENTITIES management
	//-------------------------------------------------------------------------------
	
	@Override
	public List<Entity> getEntities() {
		return entities;
	}

	@Override
	public Entity getEntityByClassName(String entityClassName) {
		for(Entity entity : getEntities()) {
			if ( entityClassName.equals(entity.getClassName()) ) {
				return entity;
			}
		}
		return null;
	}

	@Override
	public Entity getEntityByTableName(String entityTableName) {
		for(Entity entity : getEntities()) {
			if ( entityTableName.equals(entity.getDatabaseTable() ) ) {
				return entity;
			}
		}
		return null;
	}

	public void addEntity(Entity entity) {
		entities.add(entity);
	}
}
