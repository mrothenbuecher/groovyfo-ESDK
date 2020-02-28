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
public class AbasDouble extends GroovyFOVariable<Double> {

	public AbasDouble(String varname, GroovyFOScript script) {
		super(varname, script);
	}

	@Override
	public int compareTo(Object arg0) {
		return this.toString().compareTo(arg0.toString());
	}
	
	//TODO alle Mathe funktionen implementieren

	public Double next() throws FOPException, GroovyFOException {
		double i = ((Double)script.fo(varname, this.getValue()+1)).doubleValue();
		return i;
	}
	
	public Double previous() throws FOPException, GroovyFOException {
		double i = ((Double) script.fo(varname, this.getValue()-1)).doubleValue();
		return i;
	}
	
	public Double plus(double j) throws FOPException, GroovyFOException {
		double i = ((Double)script.fo(varname, this.getValue()+j)).doubleValue();
		return i;
	}
	
	public Double minus(double j) throws FOPException, GroovyFOException {
		double i = ((Double) script.fo(varname, this.getValue()-j)).doubleValue();
		return i;
	}
	
	@Override
	public Double getValue() {
		String value = EKS.getValue("F", "expr(" + this.varname + ")");
		if (value == null || value.isEmpty())
			return 0.0d;
		//FIXME Trennzeichen
		value = value.replaceAll("\\.", "");
		value = value.replace(',', '.');
		return Double.parseDouble(value);
	}

}
