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
package org.telosys.tools.generator;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.telosys.tools.generator.config.GeneratorConfig;
import org.telosys.tools.generator.context.EntityInContext;
import org.telosys.tools.generator.context.EnvInContext;
import org.telosys.tools.generic.model.Entity;
import org.telosys.tools.generic.model.Model;

public class EntitiesManager {

	private final Model           _model ; // TODO : remove ?
	private final GeneratorConfig _generatorConfig ;
	private final EnvInContext    _env ;
	
	private final Map<String,EntityInContext> _entitiesByName = new Hashtable<String,EntityInContext>();
	private final Map<String,EntityInContext> _entitiesByTableName = new Hashtable<String,EntityInContext>(); // v 3.0.0

	/**
	 * Constructor
	 * @param model
	 * @param generatorConfig
	 * @param env
	 * @throws GeneratorException
	 */
	public EntitiesManager( Model model, GeneratorConfig generatorConfig, EnvInContext env) 
			throws GeneratorException {
		_model           = model ;
		_generatorConfig = generatorConfig ;
		_env             = env ; // Specific environment instance
		
		buildAllEntities();
	}
	
	
	/**
	 * Builds a list of context Entities for all the entities defined in the model 
	 * @param model
	 * @param generatorConfig
	 * @return
	 * @throws GeneratorException
	 */
//	private List<EntityInContext> buildAllEntities( Model model, GeneratorConfig generatorConfig) throws GeneratorException 
//	{
//		List<EntityInContext> javaBeanClasses = new LinkedList<EntityInContext>();
//		//--- Get the names of all the entities defined in the model 
//		String[] names = model.getEntitiesNames();
//		for ( String entityName : names ) {
//			EntityInContext entity = buildEntity(entityName) ;
//			_entities.put(entityName, entity) ;
//		}
//		return javaBeanClasses ;
//	}
	private void buildAllEntities() throws GeneratorException 
	{
		for ( Entity entity : _model.getEntities() ) {
			//--- The package name is determined from the target folder
	    	String entityPackage = _generatorConfig.getTelosysToolsCfg().getEntityPackage(); 
	    	//--- New instance of EntityInContext
	    	EntityInContext entityInContext = new EntityInContext(entity, entityPackage, this, _env);    	
			//--- Store the EntityInContext by name
			_entitiesByName.put(entityInContext.getName(), entityInContext) ;
			//--- Store the EntityInContext by TABLE name
			if ( entityInContext.getDatabaseTable() != null ) { // v 3.0.0
				_entitiesByTableName.put(entityInContext.getDatabaseTable(), entityInContext) ; // v 3.0.0
			}
		}
	}
	
//	/**
//	 * Builds a context Entity instance from the repository (model definition)
//	 * @param entityName the name of the entity to be built
//	 * @param repositoryModel
//	 * @param generatorConfig
//	 * @return
//	 * @throws GeneratorException
//	 */
//	private EntityInContext buildEntity( String entityName ) throws GeneratorException
//	{
//		//--- Retrieve the entity from the repository model
//		Entity entity = _model.getEntityByName(entityName);
//		if ( null == entity ) 
//		{
//			throw new GeneratorException("Entity '" + entityName + "' not found in the repository");
//		}
//		if ( entityName.equals(entity.getName()) != true )
//		{
//			throw new GeneratorException("Repository corrupted : Entity name '" + entityName + "' != '" + entity.getName() +"'");
//		}
//		
//		//--- Java Bean Package name determined from the target folder
//    	String beanPackage = _generatorConfig.getTelosysToolsCfg().getEntityPackage(); 
//    		
//    	//--- New instance of JavaBeanClass
//    	EntityInContext entityInContext = new EntityInContext(entity, beanPackage, this, _env);    	
//    	
//    	return entityInContext ;
//	}

	//---------------------------------------------------------------------------------------------------
	/**
	 * Returns the entity identified by the given name
	 * @param entityName
	 * @return
	 * @throws GeneratorException
	 */
	public EntityInContext getEntity( String entityName ) throws GeneratorException
	{
		EntityInContext entity =_entitiesByName.get(entityName) ;
		if ( entity == null ) {
			throw new GeneratorException("Unknown entity for name '" + entityName + "'");
		}
		return entity ;
	}

	/**
	 * Returns the entity identified by the given table name
	 * @param entityTableName
	 * @return
	 * @throws GeneratorException
	 * @since 3.0.0
	 */
	public EntityInContext getEntityByTableName( String entityTableName ) throws GeneratorException {
		EntityInContext entity =_entitiesByTableName.get(entityTableName) ;
		if ( entity == null ) {
			throw new GeneratorException("Unknown entity for table name '" + entityTableName + "'");
		}
		return entity ;
	}

	//---------------------------------------------------------------------------------------------------
	/**
	 * Returns all the entities available in the current model
	 * @return
	 * @throws GeneratorException
	 */
	public List<EntityInContext> getAllEntities() throws GeneratorException
	{
		List<EntityInContext> allEntities = new LinkedList<EntityInContext>();
		
		//--- For each entity 
		for ( Map.Entry<String, EntityInContext> entry : _entitiesByName.entrySet() )
		{
		    //String name = entry.getKey() ;
		    EntityInContext entity = entry.getValue() ;
		    allEntities.add(entity);
		}
		return allEntities ;
	}
	
	//---------------------------------------------------------------------------------------------------
	/**
	 * Returns a list of entities for the given entities names
	 * @param entitiesNames list entities names (can be null)
	 * @return
	 * @throws GeneratorException
	 */
	public List<EntityInContext> getEntities( List<String> entitiesNames ) throws GeneratorException
	{
		List<EntityInContext> selectedEntities = new LinkedList<EntityInContext>();
		if ( entitiesNames != null ) {
			for ( String entityName : entitiesNames ) {
				EntityInContext entity = _entitiesByName.get(entityName);
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
}
