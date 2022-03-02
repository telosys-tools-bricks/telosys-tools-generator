package org.telosys.tools.generator.task;

import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.cfg.TelosysToolsCfgManager;
import org.telosys.tools.commons.env.EnvironmentManager;

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
		StringBuilder sb = new StringBuilder();
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
	public void initProject(StringBuilder sb) {
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
//	public Model loadModelFromDbRep(final String dbrepFileName) throws TelosysToolsException {
//		TelosysToolsCfg telosysToolsCfg = loadTelosysToolsCfg();
//		String dbrepAbsolutePath = FileUtil.buildFilePath( telosysToolsCfg.getModelsFolderAbsolutePath(), dbrepFileName);
//		return loadRepository( dbrepAbsolutePath );
//	}
//	
//	private Model loadRepository( String dbrepAbsolutePath ) {
//		File repositoryFile = new File(dbrepAbsolutePath);
//		return loadRepository( repositoryFile ) ;
//	}
//	
//	private Model loadRepository( File repositoryFile ) {
//		System.out.println("Load repository from file " + repositoryFile.getAbsolutePath());
//		PersistenceManager persistenceManager = PersistenceManagerFactory.createPersistenceManager(repositoryFile);
//		RepositoryModel repositoryModel = null ;
//		try {
//			repositoryModel = persistenceManager.load();
//			System.out.println("Repository loaded : " + repositoryModel.getNumberOfEntities() + " entitie(s)"  );
//		} catch (TelosysToolsException e) {
//			System.out.println("Cannot load repository! Exception :" + e.getMessage() );
//		}		
//		return repositoryModel ;
//	}
	
}
