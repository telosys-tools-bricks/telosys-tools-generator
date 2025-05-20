package org.telosys.tools.generator;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GeneratorVersionTest {

	@Test
	public void test1() {
		assertEquals("telosys-tools-generator", GeneratorVersion.getName());
		assertNotNull(GeneratorVersion.getVersion()); // SemVer X.Y.Z
		assertNotNull(GeneratorVersion.getBuildId());
		assertNotNull(GeneratorVersion.getVersionWithBuilId());
	}
}
