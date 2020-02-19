package de.finetech.groovy.utils;

import de.abas.eks.jfop.FOPException;
import de.abas.eks.jfop.remote.EKS;
import de.finetech.groovy.AbasBaseScript;
import groovy.lang.GroovyObjectSupport;

/**
 * 
 * @author Michael Rothenbücher, Finetech GmbH & Co. KG
 *
 *         bei der Implementierung von Comparable ist auf die Kompatiblitüt der
 *         abas Typen unter einander zu achten!
 * @param <V>
 */

public abstract class GroovyFOVariable<V> extends GroovyObjectSupport implements Comparable<Object> {

	protected String varname, type;
	protected AbasBaseScript script;

	/**
	 * 
	 * @param varname U|von M|von H|von ...
	 * @param script
	 */
	public GroovyFOVariable(String varname, AbasBaseScript script) {
		// String[] foo = varname.split(AbasBaseScript.PIPE_PATTERN);
		// this.buffer = foo[0];
		this.varname = varname;
		this.script = script;
	}

	public abstract V getValue();

	public String getType() {
		if (type == null) {
			this.type = EKS.getValue("F", "typeof(F|expr(" + this.getVariablename() + "))");
		}
		return type;
	}

	public String plus(Object i) throws FOPException, GroovyFOException {
		return this.getValue().toString().concat(i.toString());
	}

	public String getVariablename() {
		return this.varname;
	}

	public String getVar() {
		return this.varname;
	}

	// TODO Metafunktionen

	public boolean isEmpty() {
		String empty = EKS.getValue("F", "empty(" + this.getVariablename() + ")");
		// TODO lang
		return empty.equals("ja") || empty.equals("yes");
	}

	@Override
	public String toString() {
		return this.getValue().toString();
	}

}
