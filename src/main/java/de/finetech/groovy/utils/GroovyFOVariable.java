package de.finetech.groovy.utils;

import de.abas.eks.jfop.remote.EKS;
import groovy.lang.GroovyObjectSupport;

/**
 * 
 * @author Michael Rothenbücher, Finetech GmbH & Co. KG
 *
 *         bei der Implementierung von Comparable ist auf die Kompatiblitüt der
 *         abas Typen untereinander zu achten!
 * @param <V>
 */

public abstract class GroovyFOVariable<V> extends GroovyObjectSupport implements Comparable<Object> {

	protected String varname, type;
	protected GroovyFOScript script;

	/**
	 * 
	 * @param varname U|von M|von H|von ...
	 * @param script
	 */
	public GroovyFOVariable(String varname, GroovyFOScript script) {
		this.varname = varname;
		this.script = script;
	}
	
	/**
	 * 
	 * @param varname U|von M|von H|von ...
	 * @param type Abas Datentyp I3 B GL20 ...
	 * @param script
	 */
	public GroovyFOVariable(String varname,String type, GroovyFOScript script) {
		this.varname = varname;
		this.type = type;
		this.script = script;
	}

	public abstract V getValue();
	
	public V call() {
		return this.getValue();
	}

	public String getType() {
		if (type == null) {
			this.type = EKS.getValue("F", "typeof(F|expr(" + this.getVariablename() + "))");
		}
		return type;
	}

	/*
	public String plus(Object i) throws FOPException, GroovyFOException {
		return this.getValue().toString().concat(i.toString());
	}
	*/

	public String getVariablename() {
		return this.varname;
	}

	public String getVar() {
		return this.varname;
	}

	// TODO Metafunktionen

	/*
	 * defined
	 * typeof
	 * empty
	 * modifiable
	 * visible
	 * protected
	 * hidden
	 * modified
	 * oldvalue
	 * value
	 * tostring
	 * tovalue
	 * 
	 */
	
	public boolean isEmpty() {
		String empty = EKS.getValue("F", "empty(" + this.getVariablename() + ")");
		// TODO lang
		return empty.equals("ja") || empty.equals("yes");
	}

	@Override
	public String toString() {
		return this.getValue().toString();
	}

	public Object plus(String val) {
		return this.toString().concat(val);
	}
	
}
