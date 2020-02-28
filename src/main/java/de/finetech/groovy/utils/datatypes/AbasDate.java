package de.finetech.groovy.utils.datatypes;

import java.text.ParseException;
import java.util.regex.Pattern;

import de.abas.eks.jfop.FOPException;
import de.abas.eks.jfop.remote.EKS;
import de.finetech.groovy.utils.GroovyFOException;
import de.finetech.groovy.utils.GroovyFOScript;
import de.finetech.groovy.utils.GroovyFOVariable;

/**
 * 
 * @author Michael Rothenbücher, Finetech GmbH & Co.KG
 *
 */
public class AbasDate extends GroovyFOVariable<String> {

	private GroovyFOScript script;

	private static Pattern pattern = Pattern
			.compile("(GD.*)|" + "(GW.*)|" + "(Z)|" + "(J2)|(GJ)|" + "(GP.*)|" + "(DATUM)|(WOCHE)|(TERMIN)|(ZEIT)");

	/**
	 * @param type
	 * @return
	 */
	public static boolean isDate(String type) {
		return pattern.matcher(type.toUpperCase()).matches();
	}

	/**
	 * 
	 * @param type   - abas type bsp.: GD, GD2, ...
	 * @param expr   - Variable mit Puffer! bsp M|von, U|xvon
	 * @param script - script welches den abastype anlegt
	 * @throws GroovyFOException
	 * @throws ParseException
	 */
	public AbasDate(String expr, String value,GroovyFOScript script) throws GroovyFOException, ParseException {
		super(expr, script);
		this.script = script;
		this.type = EKS.getValue("F", "typeof(F|expr(" + expr + "))");

		if (!AbasDate.isDate(type))
			throw new GroovyFOException(type + " is not a abas datetype for date/time/duration");
	}

	public Object plus(int i) throws FOPException, GroovyFOException, ParseException {
		String expr = this.getVariablename() + "+" + i;
		return script.getComputedValue(expr);
	}

	public Object next() throws FOPException, GroovyFOException, ParseException {
		String expr = this.getVariablename() + "+1";
		return script.getComputedValue(expr);
	}

	public Object previous() throws FOPException, GroovyFOException, ParseException {
		String expr = this.getVariablename() + "-1";
		return script.getComputedValue(expr);
	}

	public Object minus(int i) throws FOPException, GroovyFOException, ParseException {
		return this.plus(-i);
	}

	// FIXME GD19+GP = GD19 usw siehe hilfe
	/**
	 * abasdate - abasdate
	 * 
	 * @param i
	 * @return
	 * @throws FOPException
	 * @throws GroovyFOException
	 */
	public Object minus(AbasDate i) throws FOPException, GroovyFOException, ParseException {
		String expr = this.getVariablename() + "-" + i.getVariablename();
		return script.getComputedValue(expr);
	}

	/**
	 * 
	 * @param i mügliche Werte 1 oder 7
	 * @return Datum // i aus dem abas
	 * @throws GroovyFOException
	 */
	public Object mod(int i) throws GroovyFOException, ParseException {
		switch (i) {
		case 1:
		case 7:
			String expr = this.getVariablename() + "//" + i;
			return script.getComputedValue(expr);
		}
		throw new GroovyFOException(
				"operation // " + i + " not supported on " + this.getVariablename() + " of type " + this.type);
	}

	public String getSortable() throws FOPException, GroovyFOException, ParseException {
		String expr = this.getVariablename() + ":8";
		return script.getComputedValue(expr).toString();
	}

	public Object and(int i) throws FOPException, GroovyFOException, ParseException {
		String expr = this.getVariablename() + "&" + i;
		return script.getComputedValue(expr);
	}

	@Override
	public String getValue() {
		return EKS.getValue("F", "expr(" + this.varname + ")");
	}

	public boolean equals(AbasDate o) throws GroovyFOException, ParseException {
		return ((Boolean) script.getComputedValue(this.varname + " = " + o.getVariablename()));
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof AbasDate) {
			AbasDate date = (AbasDate) o;
			try {
				return ((Boolean) script.getComputedValue(this.varname + " = " + date.getVariablename()));
			} catch (GroovyFOException e) {
				e.printStackTrace();
				return false;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		} else {
			return super.equals(o);
		}
	}

	@Override
	public int compareTo(Object arg0) {
		if (arg0 instanceof AbasDate) {
			try {
				AbasDate date = (AbasDate) arg0;
				if ((Boolean) script.getComputedValue(this.varname + " > " + date.getVariablename()))
					return 1;
				else if ((Boolean) script.getComputedValue(this.varname + " < " + date.getVariablename()))
					return -1;
				else
					return 0;
			} catch (Exception ex) {
				ex.printStackTrace();
				return -10;
			}

		} else {
			if (arg0 == null)
				return -10;
			return this.getValue().compareTo(arg0.toString());
		}
	}

}
