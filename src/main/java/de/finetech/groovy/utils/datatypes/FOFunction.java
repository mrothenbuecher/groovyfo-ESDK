package de.finetech.groovy.utils.datatypes;

import java.text.ParseException;

import de.finetech.groovy.utils.GroovyFOException;
import de.finetech.groovy.utils.GroovyFOScript;

/**
 * 
 * @author Michael Rothenbücher, Finetech GmbH & Co. KG
 *
 *
 *         Klasse reprüsentiert einen Functions aufruf wie F|defined(o)
 *         F|strreplace
 */
public class FOFunction {

	protected String functionName;
	protected GroovyFOScript script;

	public FOFunction(String functionName, GroovyFOScript script) {
		this.functionName = functionName;
		this.script = script;
	}

	public Object call(Object o) {
		return this.call(new Object[] { o });
	}

	public Object call(Object... objs) {
		String parameter = "";
		if (objs != null) {
			for (int i = 0; i < objs.length; i++) {
				Object o = objs[i];
				if (o != null) {
					// FIXME
					/*
					 * wenn ein Parameter ein String ist mit Anführungszeichen umschlieüen if(o
					 * instanceof String){ parameter += "\""+o.toString()+"\""; }else{ parameter +=
					 * o.toString(); }
					 */
					if(o instanceof String && i > 0) {
						parameter += "\""+o.toString()+"\"";
					}
					else 
						parameter += o.toString();

					// einzelnen Parameter mit komma trennen
					if (i < (objs.length - 1)) {
						parameter += ",";
					}
				}
			}
		}
		try {
			return script.getComputedValue("F|" + functionName + "(" + parameter + ")");
		} catch (GroovyFOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object call() throws Exception {
		return this.call(new Object[] { null });
	}

}
