package de.finetech.groovy.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.text.ParseException;

import de.abas.ceks.jedp.EDPEKSArtInfo;
import de.abas.eks.jfop.FOPException;
import de.abas.jfop.base.buffer.ReadableBuffer;
import de.finetech.groovy.utils.datatypes.TypGuesser.PossibleDatatypes;

/**
 * 
 * @author Michael Rothenbücher, Finetech GmbH & Co. KG
 *
 * @param <T>
 */
public class GroovyFOReadableMap<T extends ReadableBuffer> extends GroovyFOBaseReadableMap<T> {

	private static final long serialVersionUID = 9140667579366484095L;

	public GroovyFOReadableMap(T buffer, GroovyFOScript script) {
		super(buffer, script);
	}

	@Override
	public Object get(Object key) {
		try {
			String skey = key.toString();
			PossibleDatatypes abasType = script.getType(buffer.getQualifiedFieldName(skey),buffer.getFieldType(skey));
			switch (abasType) {
			case INTEGER:
				return buffer.getIntegerValue(skey);
				//return new BigInteger(buffer.getStringValue(skey));
				//return new AbasInteger(buffer.getQualifiedFieldName(skey),script);
			case DOUBLE:
			case DOUBLEDT:
			case DOUBLET:
			case DOUBLED:
				//EDPEKSArtInfo nfo = new EDPEKSArtInfo(buffer.getFieldType(skey));
				//MathContext m = new MathContext(nfo.getFractionDigits());
				//return new BigDecimal(buffer.getDoubleValue(skey), m);
				return buffer.getDoubleValue(skey);
				//return new AbasDouble(buffer.getQualifiedFieldName(skey),script);
			case BOOLEAN:
				return buffer.getBooleanValue(skey);
			default:
				return script.getValueByType(abasType, buffer.getQualifiedFieldName(skey), buffer.getStringValue(skey));
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
