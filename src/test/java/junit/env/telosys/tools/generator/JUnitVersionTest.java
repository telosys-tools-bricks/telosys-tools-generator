package junit.env.telosys.tools.generator;

import junit.framework.TestCase;

public class JUnitVersionTest extends TestCase {

	public void testVersion() {
		System.out.println("JUnit version is: " + junit.runner.Version.id());
	}
}
