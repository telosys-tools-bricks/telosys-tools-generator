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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityNoDoc;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.names.ContextName;
import org.telosys.tools.generic.model.Entity;
import org.telosys.tools.generic.model.Model;
import org.telosys.tools.generic.model.ModelType;

/**
 * This class gives access to the entire model
 *  
 * @author Laurent GUERIN
 *
 */
//-------------------------------------------------------------------------------------
@VelocityObject(
		contextName= ContextName.MODEL ,
		text = "Object giving access to the current model ",
		since = "2.0.0"
 )
//-------------------------------------------------------------------------------------
public class ModelInContext
{
	private final String    modelName ;
	private final String    modelFolderName ;
	private final String    modelVersion ;
	private final ModelType modelType ;
	
	private final String   modelTitle ;
	private final String   modelDescription ;
	private final int      databaseId ;
	private final String   databaseProductName ;
	
	private final List<EntityInContext>       allEntities ;
	private final Map<String,EntityInContext> entitiesByTableName ;
	private final Map<String,EntityInContext> entitiesByClassName ;
	
	//-------------------------------------------------------------------------------------
	/**
	 * Constructor
	 * @param model
	 * @param telosysToolsCfg
	 * @param env
	 */
	public ModelInContext( Model model, TelosysToolsCfg telosysToolsCfg, EnvInContext env ) { // v 3.3.0
		super();
		if ( model == null ) throw new IllegalArgumentException("Model is null");
		
		this.modelName = model.getName();  // MANDATORY
		this.modelFolderName = model.getFolderName();  // MANDATORY
		this.modelVersion = model.getVersion(); // MANDATORY
		this.modelType = model.getType(); // MANDATORY
		// check validity 
		if ( this.modelName == null ) throw new IllegalArgumentException("Model name is null");
		if ( this.modelFolderName == null ) throw new IllegalArgumentException("Model folder name is null");
		if ( this.modelVersion == null ) throw new IllegalArgumentException("Model version is null");
		if ( this.modelType == null ) throw new IllegalArgumentException("Model type is null");
		
		this.modelTitle = model.getTitle() != null ? model.getTitle() : "" ;
		this.modelDescription = model.getDescription() != null ? model.getDescription() : "" ;
		
		//--- All the entities (the original model order is kept)
		this.allEntities = new LinkedList<>(); // v 3.0.0
		for ( Entity entity : model.getEntities() ) { // v 3.0.0
			//_allEntities.add( new EntityInContext(entity, entitiesPackage, this, env) );// v 3.0.0
			this.allEntities.add( 
					new EntityInContext(entity, 
							telosysToolsCfg.getEntityPackage(),  // v 3.3.0
							this, env) );
		}
		
		//--- Entities by TABLE NAME
		this.entitiesByTableName = new HashMap<>();
		for ( EntityInContext entity : this.allEntities ) {
			// The table name is unique 
			this.entitiesByTableName.put(entity.getDatabaseTable(), entity);
		}
		
		//--- Entities by CLASS NAME
		this.entitiesByClassName = new HashMap<>();
		for ( EntityInContext entity : this.allEntities ) {
			// The class name is supposed to be unique 
			this.entitiesByClassName.put(entity.getName(), entity);
		}
		
		if ( model.getDatabaseId() != null ) {
			this.databaseId          = model.getDatabaseId();
		}
		else {
			this.databaseId = -1;
		}
		if ( model.getDatabaseProductName() != null ) {
			this.databaseProductName = model.getDatabaseProductName();
		}
		else {
			this.databaseProductName = "";
		}
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the model name as defined by file/folder in the project directory "
			},
		since="3.3.0"
	)
    public String getName()
    {
        return modelName ;
    }

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the name of the folder where entities are located "
			},
		since="3.3.0"
	)
    public String getFolderName()
    {
        return modelFolderName ;
    }

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the Telosys model version "
			},
		since="3.3.0"
	)
    public String getVersion()
    {
        return modelVersion ;
    }

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the Telosys model type (DSL-MODEL, DB-MODEL, etc) "
			},
		since="3.3.0"
	)
    public String getType()
    {
        switch ( modelType ) {
        case DOMAIN_SPECIFIC_LANGUAGE : 
        	return "DSL-MODEL" ;
        case DATABASE_SCHEMA : 
        	return "DB-MODEL" ;
        default:
            return "UNKNOWN";
        }
    }

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the model title "
			},
		since="3.3.0"
	)
    public String getTitle()
    {
        return modelTitle ;
    }

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the model description "
			},
		since="3.3.0"
	)
    public String getDescription()
    {
        return modelDescription ;
    }

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the number of entities defined in the model"
			}
	)
    public int getNumberOfEntities()
    {
        return allEntities.size() ;
    }

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns a list containing all the entities defined in the model" 
			}
	)
    public List<EntityInContext> getAllEntites()
    {
		return allEntities ;
    }
	
	//---------------------------------------------------------------------------------------------------
	/**
	 * Returns a list of entities for the given entities names
	 * @param entitiesNames list entities names (can be null)
	 * @return
	 * @throws GeneratorException
	 */
	@VelocityNoDoc
	public List<EntityInContext> getEntities( List<String> entitiesNames ) throws GeneratorException
	{
		List<EntityInContext> selectedEntities = new LinkedList<>();
		if ( entitiesNames != null ) {
			for ( String entityName : entitiesNames ) {
				EntityInContext entity = entitiesByClassName.get(entityName);
				if ( entity != null ) {
					selectedEntities.add(entity);
				}
				else {
					throw new GeneratorException("Unknown entity '" + entityName + "'");
				}
			}
		}
		return selectedEntities ;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the entity identified by the given database table name",
			"or null if not found"
			},
		parameters={
			"name : the table name identifying the entity (the table name) "
		}
	)
    public EntityInContext getEntityByTableName( String name )
    {
		return entitiesByTableName.get(name);
    }

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the entity identified by the given class name",
			"or null if not found"
			},
		parameters={
			"name : the class name identifying the entity (supposed to be unique) "
		}
	)
    public EntityInContext getEntityByClassName( String entityClassName )
    {
		return entitiesByClassName.get(entityClassName);
    }

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the model contains an entity identified by the given table name",
			"else FALSE"
			},
		parameters={
			"name : the table name identifying the entity "
		}
	)
    public boolean hasEntityWithTableName( String name )
    {
		return ( entitiesByTableName.get(name) != null ) ;
    }

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the model contains an entity identified by the given class name",
			"else FALSE"
			},
		parameters={
			"name : the class name identifying the entity "
		}
	)
    public boolean hasEntityWithClassName( String name )
    {
		return ( entitiesByClassName.get(name) != null ) ;
    }

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the ID of the database used to generate the model",
			"A valid ID is >= 0 ",
			"-1 means undefined"
			}
	)
    public int getDatabaseId()
    {
		return databaseId ;
    }

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the product name of the database used to generate the model",
			"This is the product name return by the JDBC meta-data",
			"(e.g. 'Apache Derby') "
			}
	)
    public String getDatabaseProductName()
    {
		return databaseProductName ;
    }

}