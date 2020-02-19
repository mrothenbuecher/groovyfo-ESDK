package de.finetech.groovy.utils.datatypes;

import java.text.ParseException;

import de.abas.eks.jfop.FOPException;
import de.abas.eks.jfop.remote.EKS;
import de.finetech.groovy.AbasBaseScript;
import de.finetech.groovy.utils.GroovyFOException;
import de.finetech.groovy.utils.GroovyFOVariable;

/**
 * 
 * @author Michael Rothenbücher, Finetech GmbH & Co. KG
 *
 *         Verweis Darstellung
 */
public class AbasPointer extends GroovyFOVariable<String> {

	public AbasPointer(String varname, AbasBaseScript script) {
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

	@Override
	public String toString() {
		return this.getValue();
	}

	/**
	 * Zugriff auf Variablen eines Verweises
	 * 
	 * M|vorgang^id z.Bsp. in groovy fo dann
	 * 
	 * m.vorgang^"id"
	 * 
	 * @param var
	 * @return
	 * @throws FOPException
	 * @throws GroovyFOException
	 */
	public Object xor(String var) throws FOPException, GroovyFOException, ParseException {
		Object o = this.script.getComputedValue(this.getVariablename() + "^" + var);
		return o;
	}
}
