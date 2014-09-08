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
package org.telosys.tools.generator.context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.telosys.tools.generator.EntitiesManager;
import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.names.ContextName;
import org.telosys.tools.repository.model.RepositoryModel;

/**
 * This class give access to the entire repository model
 *  
 * @author Laurent GUERIN
 *
 */
//-------------------------------------------------------------------------------------
@VelocityObject(
		contextName= ContextName.MODEL ,
		text = "Object giving access to the lightweight model ",
		since = ""
 )
//-------------------------------------------------------------------------------------
public class ModelInContext
{
//	private final List<JavaBeanClass>       _allEntities ;
//	private final Map<String,JavaBeanClass> _entitiesByTableName ;
//	private final Map<String,JavaBeanClass> _entitiesByClassName ;
	private final List<EntityInContext>       _allEntities ;
	private final Map<String,EntityInContext> _entitiesByTableName ;
	private final Map<String,EntityInContext> _entitiesByClassName ;
	private final int      _databaseId ;
	private final String   _databaseProductName ;
	
	//-------------------------------------------------------------------------------------
	/**
	 * Constructor
	 * @param repositoryModel
	 * @param generatorConfig
	 * @param env
	 * @throws GeneratorException
	 */
	public ModelInContext( RepositoryModel repositoryModel, EntitiesManager entitiesManager ) throws GeneratorException  {
		super();
		if ( repositoryModel == null ) throw new GeneratorException("RepositoryModel is null");
//		if ( generatorConfig == null ) throw new GeneratorException("GeneratorConfig is null");
//		if ( env == null ) throw new GeneratorException("EnvInContext is null");
		if ( entitiesManager == null ) throw new GeneratorException("EntitiesBuilder is null");
		
		//--- All the entities
		//_allEntities = RepositoryModelUtil.buildAllJavaBeanClasses(repositoryModel, generatorConfig );
//		EntitiesBuilder entitiesBuilder = new EntitiesBuilder(repositoryModel, generatorConfig, env);
		_allEntities = entitiesManager.getAllEntities();
		
		//--- Entities by TABLE NAME
		//_entitiesByTableName = new HashMap<String,JavaBeanClass>();
		//for ( JavaBeanClass entity : _allEntities ) {
		_entitiesByTableName = new HashMap<String,EntityInContext>();
		for ( EntityInContext entity : _allEntities ) {
			// The table name is unique 
			_entitiesByTableName.put(entity.getDatabaseTable(), entity);
		}
		
		//--- Entities by CLASS NAME
//		_entitiesByClassName = new HashMap<String,JavaBeanClass>();
//		for ( JavaBeanClass entity : _allEntities ) {
		_entitiesByClassName = new HashMap<String,EntityInContext>();
		for ( EntityInContext entity : _allEntities ) {
			// The class name is supposed to be unique 
			_entitiesByClassName.put(entity.getName(), entity);
		}
		
		_databaseId          = repositoryModel.getDatabaseId();
		_databaseProductName = repositoryModel.getDatabaseProductName();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the number of entities defined in the model"
			}
	)
    public int getNumberOfEntities()
    {
        return _allEntities.size() ;
    }

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns a list containing all the entities defined in the model" 
			}
	)
    //public List<JavaBeanClass> getAllEntites()
    public List<EntityInContext> getAllEntites()
    {
		return _allEntities ;
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
    //public JavaBeanClass getEntityByTableName( String name )
    public EntityInContext getEntityByTableName( String name )
    {
		return _entitiesByTableName.get(name);
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
    //public JavaBeanClass getEntityByClassName( String name )
    public EntityInContext getEntityByClassName( String name )
    {
		return _entitiesByClassName.get(name);
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
		return ( _entitiesByTableName.get(name) != null ) ;
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
		return ( _entitiesByClassName.get(name) != null ) ;
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
		return _databaseId ;
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
		return _databaseProductName ;
    }

}