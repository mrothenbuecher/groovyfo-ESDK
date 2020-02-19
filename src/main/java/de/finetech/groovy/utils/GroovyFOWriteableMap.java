package de.finetech.groovy.utils;

import java.text.ParseException;

import de.abas.eks.jfop.FOPException;
import de.abas.jfop.base.buffer.WriteableBuffer;
import de.finetech.groovy.AbasBaseScript;

/**
 * 
 * @author Michael Rothenbücher, Finetech GmbH & Co. KG
 *
 * @param <T>
 */
public class GroovyFOWriteableMap<T extends WriteableBuffer> extends GroovyFOReadableMap<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2863196830703951276L;

	public GroovyFOWriteableMap(T buffer, AbasBaseScript script) {
		super(buffer, script);
	}

	@Override
	public Object put(String key, Object value) {
		try {
			// FO.println("key: "+key+" -> "+value.toString());
			Class<?> valueClass = value.getClass();
			// FIXME muss besser gehen
			if (valueClass == Integer.class) {
				buffer.setValue(key, (Integer) value);
				return value;
				//return script.fo(key, (Integer) value);
			} else if (valueClass == Double.class) {
				buffer.setValue(key, (Double) value);
				return value;
				//return script.fo(key, (Double) value);
			} else if (valueClass == Boolean.class) {
				Boolean b = (Boolean) value;
				buffer.setValue(key, b);
				return b;
				//return script.fo(key, b.booleanValue() ? "G|TRUE" : "G|FALSE");
			} else if (valueClass == String.class) {
				// wenn der übergebene Wert mit einem Hochkomma "'" beginnt wird
				// der
				// wert so übergeben das abas ihn interpretiert
				String val = value != null ? value.toString() : "";
				if (val.startsWith("'")) {
					val = val.substring(1);
					script.formula(buffer + "|" + key, val);
				} else {
					if (val.startsWith("\"") && val.endsWith("\""))
						return script.fo(key, val);
					else {
						// TODO ggf. abfangen wenn Anführungstriche enthalten
						// sind "
						// val = val.replaceAll("", "+'DBLQUOTE'+");
						return script.fo(key, "\"" + val + "\"");
					}
				}
			} else if (value instanceof GroovyFOVariable) {
				return script.fo(key, value.toString());
			}
		} catch (FOPException e) {
			e.printStackTrace();
		} catch (GroovyFOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

}
