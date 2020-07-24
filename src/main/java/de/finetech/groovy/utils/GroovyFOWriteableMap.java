package de.finetech.groovy.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.ParseException;

import de.abas.ceks.jedp.EDPEKSArtInfo;
import de.abas.eks.jfop.FOPException;
import de.abas.jfop.base.buffer.WriteableBuffer;

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

	public GroovyFOWriteableMap(T buffer, GroovyFOScript script) {
		super(buffer, script);
	}

	@Override
	public Object put(String key, Object value) {
		try {
			Class<?> valueClass = value.getClass();
			// FIXME muss besser gehen
			if (valueClass == Integer.class) {
				//script.println("Debug Fooo");
				buffer.setValue(key, ((Integer) value).intValue());
				return value;
				//return script.fo(key, (Integer) value);
			} else if (valueClass == Double.class || valueClass == Float.class || valueClass == BigDecimal.class) {
				if(valueClass == BigDecimal.class) {
					//val = ((BigDecimal) value).doubleValue();
					EDPEKSArtInfo nfo = new EDPEKSArtInfo(buffer.getFieldType(key));
					MathContext m = new MathContext(nfo.getFractionDigits());
					
					BigDecimal b = ((BigDecimal) value).add(BigDecimal.ZERO).round(m);
					
					buffer.setValue(key, b);
				}
				else if(valueClass == Double.class) {
					//val = ((Double) value).doubleValue();
					buffer.setValue(key, ((Double) value));
				}
				else if(valueClass == Float.class) {
					//val = ((Float) value).doubleValue();
					buffer.setValue(key, ((Float) value));
				}
				//buffer.setValue(key, val);
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
