package de.finetech.groovy;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.abas.erp.db.infosystem.standard.is.RunFOP;
import de.abas.esdk.test.util.EsdkIntegTest;

public class GroovyFOTest extends EsdkIntegTest {
	
	@Test
	public void executeTest() {
	    RunFOP runFOP = ctx.openInfosystem(RunFOP.class);
		runFOP.setKommando("java:GroovyFO@grvfo ow1/GROOVYFO.UNITTEST");
		runFOP.invokeStart();

		for(String msg: this.getErrors()) {
			System.out.println(msg);
		}
		for(String error: this.getErrors()) {
			System.err.println(error);
		}
		assertEquals("Es gab Fehler", this.getErrors().size(), 0);
	}
	
}
