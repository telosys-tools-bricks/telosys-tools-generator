package junit.env.telosys.tools.generator;

import java.io.File;

import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.generator.Generator;

public class FakeProject {

	private final String projectFolderName ;
	
	public FakeProject(String projectFolderName) {
		super();
		this.projectFolderName = projectFolderName;
	}

	public File getProjectFolder() {
		return TestsEnv.getTestFolder(projectFolderName);
	}
	
	public TelosysToolsCfg getTelosysToolsCfg() {
		return TestsEnv.loadTelosysToolsCfg(getProjectFolder());
	}

	public Generator getGenerator(String bundleName) {
		TelosysToolsCfg telosysToolsCfg = getTelosysToolsCfg();
		return new Generator(telosysToolsCfg, bundleName, LoggerProvider.getLogger()) ;
	}

}
