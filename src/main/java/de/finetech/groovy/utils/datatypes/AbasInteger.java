package de.finetech.groovy.utils.datatypes;

import de.abas.eks.jfop.FOPException;
import de.abas.eks.jfop.remote.EKS;
import de.finetech.groovy.utils.GroovyFOException;
import de.finetech.groovy.utils.GroovyFOScript;
import de.finetech.groovy.utils.GroovyFOVariable;

/**
 * 
 * @author Michael Rothenbücher, Finetech GmbH & Co. KG
 *
 */
public class AbasInteger extends GroovyFOVariable<Integer> {

	public AbasInteger(String varname, GroovyFOScript script) {
		super(varname, script);
	}

	@Override
	public int compareTo(Object arg0) {
		return this.toString().compareTo(arg0.toString());
	}

	//TODO alle Mathe funktionen implementieren
	
	public Integer next() throws FOPException, GroovyFOException {
		int i = ((Integer)script.fo(varname, this.getValue()+1)).intValue();
		return i;
	}
	
	public Integer previous() throws FOPException, GroovyFOException {
		int i = ((Integer) script.fo(varname, this.getValue()-1)).intValue();
		return i;
	}
	
	@Override
	public Integer getValue() {
		return Integer.parseInt(EKS.getValue("F", "expr(" + this.varname + ")"));
	}

}
