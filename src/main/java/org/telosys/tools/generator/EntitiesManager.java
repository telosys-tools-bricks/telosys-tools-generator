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

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.telosys.tools.generator.config.GeneratorConfig;
import org.telosys.tools.generator.context.EntityInContext;
import org.telosys.tools.generator.context.EnvInContext;
import org.telosys.tools.repository.model.Entity;
import org.telosys.tools.repository.model.RepositoryModel;

public class EntitiesManager {

	private final RepositoryModel _repositoryModel ;
	private final GeneratorConfig _generatorConfig ;
	private final EnvInContext    _env ;
	
	private final Map<String,EntityInContext> _entities = new Hashtable<String,EntityInContext>();
//	public EntitiesBuilder() {
//		_env = new EnvInContext() ; // Default environment
//	}

	public EntitiesManager( RepositoryModel repositoryModel, GeneratorConfig generatorConfig, EnvInContext env) 
			throws GeneratorException {
		_repositoryModel = repositoryModel ;
		_generatorConfig = generatorConfig ;
		_env             = env ; // Specific environment instance
		
		buildAllEntities(repositoryModel, generatorConfig);
	}
	
	
	/**
	 * Builds a context Entity instance from the repository (model definition)
	 * @param entityName the name of the entity to be built
	 * @param repositoryModel
	 * @param generatorConfig
	 * @return
	 * @throws GeneratorException
	 */
	private EntityInContext buildEntity( String entityName ) throws GeneratorException
	{
		//--- Retrieve the entity from the repository model
		Entity entity = _repositoryModel.getEntityByName(entityName);
		if ( null == entity ) 
		{
			throw new GeneratorException("Entity '" + entityName + "' not found in the repository");
		}
		if ( entityName.equals(entity.getName()) != true )
		{
			throw new GeneratorException("Repository corrupted : Entity name '" + entityName + "' != '" + entity.getName() +"'");
		}
		
//		//--- Java Bean Class name defined in the repository
//    	String beanClassName = entity.getBeanJavaClass();
    	
		//--- Java Bean Package name determined from the target folder
    	String beanPackage = _generatorConfig.getTelosysToolsCfg().getEntityPackage(); 
    		
    	//--- New instance of JavaBeanClass
    	EntityInContext entityInContext = new EntityInContext(entity, beanPackage, this, _env);    	
    	
    	return entityInContext ;
		
	}
	
//	/**
//	 * Builds a list of context Entities for each selected entity (defined by its name)
//	 * @param entitiesNames list of selected entities names (or null if none)
//	 * @param repositoryModel
//	 * @param generatorConfig
//	 * @return
//	 * @throws GeneratorException
//	 */
//	public List<EntityInContext> getSelectedEntities( 
//			List<String> entitiesNames, 
//			RepositoryModel repositoryModel, 
//			GeneratorConfig generatorConfig
//			) throws GeneratorException
//	{
//		List<EntityInContext> selectedEntities = new LinkedList<EntityInContext>();
//		if ( entitiesNames != null ) {
//			for ( String entityName : entitiesNames ) {
//				EntityInContext entityBeanClass = buildEntity( entityName );
//				selectedEntities.add(entityBeanClass);
//			}
//		}
//		return selectedEntities ;
//	}	
	
	/**
	 * Builds a list of context Entities for all the entities defined in the model 
	 * @param repositoryModel
	 * @param generatorConfig
	 * @return
	 * @throws GeneratorException
	 */
	private List<EntityInContext> buildAllEntities( RepositoryModel repositoryModel, GeneratorConfig generatorConfig) throws GeneratorException 
	{
		List<EntityInContext> javaBeanClasses = new LinkedList<EntityInContext>();
		//--- Get the names of all the entities defined in the model 
		String[] names = repositoryModel.getEntitiesNames();
		for ( String entityName : names ) {
//			//--- Build an "entity BeanClass" for each
//			EntityInContext entity = buildEntity( entityName );
////			javaBeanClasses.add(entityBeanClass);
//			_entities.put(entityName, entity) 
			EntityInContext entity = buildEntity(entityName) ;
			_entities.put(entityName, entity) ;
		}
		return javaBeanClasses ;
	}
	
	//---------------------------------------------------------------------------------------------------
	/**
	 * Returns the entity identified by the given name
	 * @param entityName
	 * @return
	 * @throws GeneratorException
	 */
	public EntityInContext getEntity( String entityName ) throws GeneratorException
	{
		EntityInContext entity =_entities.get(entityName) ;
		if ( entity == null ) {
			throw new GeneratorException("Unknown entity '" + entityName + "'");
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
		for ( Map.Entry<String, EntityInContext> entry : _entities.entrySet() )
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
				EntityInContext entity = _entities.get(entityName);
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
