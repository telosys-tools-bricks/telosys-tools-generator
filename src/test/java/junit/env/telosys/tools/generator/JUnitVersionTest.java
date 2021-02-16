package junit.env.telosys.tools.generator;

import org.junit.Test;

public class JUnitVersionTest {

	@Test
	public void testVersion() {
		System.out.println("JUnit version is: " + junit.runner.Version.id());
	}
}
