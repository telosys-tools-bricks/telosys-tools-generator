package org.telosys.tools.test.velocity;

import org.telosys.tools.commons.ConsoleLogger;
import org.telosys.tools.commons.TelosysToolsLogger;

public class LoggerProvider {

	private final static TelosysToolsLogger logger = new ConsoleLogger();
	
	public final static TelosysToolsLogger getLogger() {
		
		return logger ;
	}
}
