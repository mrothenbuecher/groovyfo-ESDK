import java.util.Enumeration;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import de.abas.eks.jfop.FOPException;
import de.abas.eks.jfop.remote.ContextRunnable;
import de.abas.eks.jfop.remote.FO;
import de.abas.eks.jfop.remote.FOPSessionContext;
import de.finetech.groovy.ScriptExecutor;

public class GroovyFO implements ContextRunnable {

	@Override
	public int runFop(FOPSessionContext arg0, String[] arg1) throws FOPException {
		Enumeration<Logger> e = LogManager.getCurrentLoggers();
		FO.println("Beginne Test");
		while(e.hasMoreElements()) {
			Logger l = e.nextElement();
			FO.println(l.getName());
		}
		FO.println("Beende Test");
		return ScriptExecutor.executeScript(arg0, arg1);
	}

}
