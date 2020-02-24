package de.finetech.groovy.utils.datatypes;

import java.math.BigDecimal;

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
public class AbasBigDecimal extends GroovyFOVariable<BigDecimal> {

	public AbasBigDecimal(String varname, GroovyFOScript script) {
		super(varname, script);
	}

	@Override
	public int compareTo(Object arg0) {
		return this.toString().compareTo(arg0.toString());
	}
	
	//TODO alle Mathe funktionen implementieren

	public BigDecimal next() throws FOPException, GroovyFOException {
		BigDecimal i = ((BigDecimal)script.fo(varname, this.getValue().add(BigDecimal.ONE)));
		return i;
	}
	
	public BigDecimal previous() throws FOPException, GroovyFOException {
		BigDecimal i = ((BigDecimal) script.fo(varname, this.getValue().subtract(BigDecimal.ONE)));
		return i;
	}
	
	public BigDecimal plus(BigDecimal j) throws FOPException, GroovyFOException {
		BigDecimal i = ((BigDecimal)script.fo(varname, this.getValue().add(j)));
		return i;
	}
	
	public BigDecimal minus(BigDecimal j) throws FOPException, GroovyFOException {
		BigDecimal i = ((BigDecimal) script.fo(varname, this.getValue().subtract(j)));
		return i;
	}
	
	@Override
	public BigDecimal getValue() {
		String value = EKS.getValue("F", "expr(" + this.varname + ")");
		if (value == null || value.isEmpty())
			return BigDecimal.ZERO;
		//FIXME Trennzeichen
		value = value.replaceAll("\\.", "");
		value = value.replace(',', '.');
		return new BigDecimal(value);
	}

}
