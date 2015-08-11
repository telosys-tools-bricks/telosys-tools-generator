package junit.env.telosys.tools.generator;

import org.telosys.tools.commons.ConsoleLogger;
import org.telosys.tools.commons.TelosysToolsLogger;

public class LoggerProvider {

	private final static TelosysToolsLogger logger = new ConsoleLogger();
	
	public final static TelosysToolsLogger getLogger() {
		
		return logger ;
	}
}
