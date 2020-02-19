package de.finetech.groovy.utils.datatypes;

import de.abas.eks.jfop.remote.EKS;
import de.finetech.groovy.AbasBaseScript;
import de.finetech.groovy.utils.GroovyFOVariable;

/**
 * 
 * @author Michael Rothenbücher, Finetech GmbH & Co. KG
 *
 */
public class AbasString extends GroovyFOVariable<String> {

	// TODO Abas funktionen wie "asd"<<2 usw.

	public AbasString(String varname, AbasBaseScript script) {
		super(varname, script);
	}

	@Override
	public int compareTo(Object arg0) {
		return this.toString().compareTo(arg0.toString());
	}

	@Override
	public String getValue() {
		return EKS.getValue("F", "expr(" + this.varname + ")");
	}

}
