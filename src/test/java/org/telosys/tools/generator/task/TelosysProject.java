package org.telosys.tools.generator.task;

import java.io.File;

import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.cfg.TelosysToolsCfgManager;
import org.telosys.tools.commons.env.EnvironmentManager;
import org.telosys.tools.generic.model.Model;
import org.telosys.tools.repository.model.RepositoryModel;
import org.telosys.tools.repository.persistence.PersistenceManager;
import org.telosys.tools.repository.persistence.PersistenceManagerFactory;

public class TelosysProject {

	private final String projectFolderAbsolutePath ;
	
	public TelosysProject(String projectFolderAbsolutePath) {
		super();
		this.projectFolderAbsolutePath = projectFolderAbsolutePath;
	}

	public String getProjectFolder() {
		return projectFolderAbsolutePath;
	}

	public String initProject() {
		StringBuffer sb = new StringBuffer();
		sb.append("Project initialization \n");
		sb.append("Project folder : '" + projectFolderAbsolutePath + "' \n");
		sb.append("\n");
		// Init environment files
		initProject(sb);
		return sb.toString();		
	}
	
	//-----------------------------------------------------------------------------------------------------
	// Project initialization
	//-----------------------------------------------------------------------------------------------------
	public void initProject(StringBuffer sb) {
		EnvironmentManager em = new EnvironmentManager( projectFolderAbsolutePath );
		// Init environment files
		em.initEnvironment(sb);
	}
	
	//-----------------------------------------------------------------------------------------------------
	// Project configuration 
	//-----------------------------------------------------------------------------------------------------
	public TelosysToolsCfg loadTelosysToolsCfg() throws TelosysToolsException {
		TelosysToolsCfgManager cfgManager = new TelosysToolsCfgManager( projectFolderAbsolutePath );
		return cfgManager.loadTelosysToolsCfg();
	}
	
	//-----------------------------------------------------------------------------------------------------
	// Database model management ('dbrep') 
	//-----------------------------------------------------------------------------------------------------
	public Model loadModelFromDbRep(final String dbrepFileName) throws TelosysToolsException {
		TelosysToolsCfg telosysToolsCfg = loadTelosysToolsCfg();
		String dbrepAbsolutePath = FileUtil.buildFilePath( telosysToolsCfg.getRepositoriesFolderAbsolutePath(), dbrepFileName);
		return loadRepository( dbrepAbsolutePath );
	}
	
	private Model loadRepository( String dbrepAbsolutePath ) {
		File repositoryFile = new File(dbrepAbsolutePath);
		return loadRepository( repositoryFile ) ;
	}
	
	private Model loadRepository( File repositoryFile ) {
		System.out.println("Load repository from file " + repositoryFile.getAbsolutePath());
		PersistenceManager persistenceManager = PersistenceManagerFactory.createPersistenceManager(repositoryFile);
		RepositoryModel repositoryModel = null ;
		try {
			repositoryModel = persistenceManager.load();
			System.out.println("Repository loaded : " + repositoryModel.getNumberOfEntities() + " entitie(s)"  );
		} catch (TelosysToolsException e) {
			System.out.println("Cannot load repository! Exception :" + e.getMessage() );
		}		
		return repositoryModel ;
	}
	
}
