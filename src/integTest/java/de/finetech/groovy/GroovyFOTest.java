package de.finetech.groovy;

import org.junit.Test;

import de.abas.erp.db.infosystem.standard.is.RunFOP;
import de.abas.esdk.test.util.DoNotFailOnError;
import de.abas.esdk.test.util.EsdkIntegTest;

public class GroovyFOTest extends EsdkIntegTest {
	
	@Test
	public void executeTest() {
	    //int value = EKS.eingabe("java:GroovyFO@grvfo ow1/GROOVYFO.UNITTEST");
	    //assertEquals("Unittest gescheitert", value, 0);
	    RunFOP runFOP = ctx.openInfosystem(RunFOP.class);
		runFOP.setKommando("java:GroovyFO@grvfo ow1/GROOVYFO.UNITTEST");
		runFOP.invokeStart();

		//System.out.println("Messges: " + getMessages());
		//System.out.println("Errors : " + getErrors());
	}
	
}
