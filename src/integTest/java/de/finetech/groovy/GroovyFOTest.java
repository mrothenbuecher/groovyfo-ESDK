package de.finetech.groovy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import de.abas.esdk.test.util.EsdkIntegTest;

public class GroovyFOTest extends EsdkIntegTest {
	
	@Test
	public void executeTest() {
		File file = new File(System.getProperty("user.dir")+"\\src\\main\\resources\\ow1\\GROOVYFO.UNITTEST");
		if(!file.exists()) {
			assertTrue("Kann Test Datei nicht finden", false);
		}else {
			assertEquals("Fehler beim ausführen des Scripts", ScriptExecutor.executeScript(ctx, file),0);
		}
	}
}
