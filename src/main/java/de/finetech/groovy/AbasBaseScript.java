package de.finetech.groovy;

import java.io.PrintWriter;
import java.io.StringWriter;

import de.abas.eks.jfop.remote.FOPSessionContext;
import de.abas.erp.db.DbContext;
import de.finetech.groovy.utils.GroovyFOScript;
import groovy.transform.CompileStatic;
import groovyx.net.http.HttpBuilder;

/**
 * 
 * 
 * Basisklasse von der jedes Script abgeleitet wird
 * 
 * @author Michael Rothenbücher, Finetech GmbH & Co. KG
 * 
 */
@CompileStatic
public abstract class AbasBaseScript extends GroovyFOScript {

	/**
	 * die interne standard Sprache des groovyFO ist Deutsch
	 */
	public AbasBaseScript() {
	}

	public boolean isDebug() {
		return this.GROOVYFODEBUG;
	}
	
	public void debug(String debug) {
		if(dbContext != null) {
			dbContext.out().println("GROOVYFO DEBUG: "+debug);
		}else {
			System.out.println("GROOVYFO DEBUG: "+debug);
		}
	}


	@Override
	protected void finalize() throws Throwable {
		try {
			hselection = null;
			lselection = null;
			d = null;
			D = null;
			a = null;
			A = null;
			e = null;
			E = null;

			f = null;
			F = null;

			g = null;
			G = null;
			h = null;
			H = null;
			l = null;
			L = null;
			l0 = null;
			L0 = null;
			l1 = null;
			L1 = null;
			l2 = null;
			L2 = null;
			l3 = null;
			L3 = null;
			l4 = null;
			L4 = null;
			l5 = null;
			L5 = null;
			l6 = null;
			L6 = null;
			l7 = null;
			L7 = null;
			l8 = null;
			L8 = null;
			l9 = null;
			L9 = null;
			m = null;
			M = null;
			p = null;
			P = null;
			s = null;
			S = null;
			u = null;
			U = null;
			variableTypes = null;
			variables = null;
			arg0 = null;
			args = null;
			dbContext=null;
		} finally {
			super.finalize();
		}
	}

	
	protected String getStacktrace(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}


	@Override
	public Object run() {
		Object o = null;
		try {
			this.GROOVYFODEBUG = (boolean) this.getBinding().getVariable("GROOVYFODEBUG");
			this.arg0 = (FOPSessionContext) this.getBinding().getVariable("arg0");
			this.args = (String[]) this.getBinding().getVariable("args");
			this.dbContext = (DbContext) this.getBinding().getVariable("dbContext");
			o = runCode();
		} catch (Exception e) {
			this.onerror(e);
		} finally {
			this.always();
		}
		return o;
	}

	public abstract Object runCode();

	/**
	 * Methode wird immer ausgeführt nach Ende des Scriptes
	 * 
	 */
	public Object always() {
		return null;
	}
	

	/**
	 * Methode wird beim Auftretten einer unbehandelten Ausnahme ausgeführt
	 */
	public Object onerror(Object ex) {
		if (ex instanceof Exception) {
			Exception e = (Exception) ex;
			e.printStackTrace();
			println("Unbehandelte Ausnahme: \n" + getStacktrace(e));
		}
		return null;
	}
	
	/**
	 * Falls eine Eigenschaft nicht definiert ist, wird die Eigenschaft als String zurück gegeben
	 * 
	 * so wird folgendes möglich: 
	 * 	- m["variable"] -> m.variable
	 *  - hole("Teil")  -> hole(Teil)
	 *  
	 * siehe https://groovy-lang.org/metaprogramming.html
	 * 
	 * @param name
	 * @return
	 */
	public Object propertyMissing(String name) {
		switch(name) {
		case "mehr":
			return this.mehr();
		case "success":
			return this.success();
		case "more":
			return this.getMore();
		case "HttpBuilder":
			return this.HttpBuilder();
	    default:
	    	return name;
		}
		
	}
	
	/**
	 * Zugriff auf den REST-Client
	 * 
	 * @return
	 */
	public Class<HttpBuilder> HttpBuilder() {
		return HttpBuilder.class;
	}
	
}
