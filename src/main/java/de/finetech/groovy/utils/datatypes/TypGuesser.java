package de.finetech.groovy.utils.datatypes;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.abas.ceks.jedp.EDPEKSArtInfo;

/**
 * 
 * @author Michael Rothenbücher, Finetech GmbH & Co. KG
 *
 */
public class TypGuesser {

	private static final Logger log = LoggerFactory.getLogger(TypGuesser.class);
	
	public static enum PossibleDatatypes {
		INTEGER, DOUBLE, DOUBLED, DOUBLET, DOUBLEDT, BOOLEAN, ABASDATE, ABASPOINTER, STRING
	}

	// regulüre Ausdrücke zum erkennen von Variablen arten
	//private static Pattern integerPattern = Pattern.compile("(?:I(?:[0-9]*|P[^ ]*|N[^ ]*)|(?:K[^ ]*)|(?:AN[^ ]*))");
	//private static Pattern doublePattern = Pattern.compile("(?:(?:R[^ ]*)|(?:M[^ ]*))");
	private static Pattern doubledPattern = Pattern.compile("(?:(?:R[^ ]*D[^ ]*)|(?:M[^ ]*D[^ ]*))");
	private static Pattern doubletPattern = Pattern.compile("(?:(?:R[^ ]*T[^ ]*)|(?:M[^ ]*T[^ ]*))");
	private static Pattern doubledtPattern = Pattern.compile("(?:(?:R[^ ]*DT[^ ]*)|(?:M[^ ]*DT[^ ]*))");

	private static Pattern boolPattern = Pattern.compile("(?:B[^ ]*)");
	private static Pattern pointerPattern = Pattern.compile("(?:(?:P[^ ]*)|(?:ID[^ ]*)|(?:VP[^ ]*)|(?:VID[^ ]*)|(?:C[^ ]*))");

	//private static Pattern newPattern = Pattern.compile(
	//		"(I(?:[0-9]*|P[^ ]*|N[^ ]*)|(?:K[^ ]*)|(?:AN[^ ]*))|((?:R[^ ]*)|(?:M[^ ]*))|((?:B[^ ]*))|((?:P[^ ]*)|(?:ID[^ ]*)|(?:VP[^ ]*)|(?:VID[^ ]*)|(?:C[^ ]*))");
	
	/**
	 * 
	 * @param abasType - abas Variablenart Bspw: I3, GD2, GL30, T60 usw...
	 * @return
	 */
	public static PossibleDatatypes getClassOfType(String abasType) {
		EDPEKSArtInfo nfo = new EDPEKSArtInfo(abasType);
		
		if(nfo.getIntegerDigits() > 0 && nfo.getFractionDigits() == 0) {
			if(log.isTraceEnabled()) {
				log.trace(abasType+" => integer");
			}
			return PossibleDatatypes.INTEGER;
		}
		
		// real tausender und dezimal
		if (nfo.getIntegerDigits() > 0 && nfo.getFractionDigits() > 0) {
			// Real
			if (doubledtPattern.matcher(abasType).matches()) {
				if(log.isTraceEnabled()) {
					log.trace(abasType+" => doubleDT");
				}
				return PossibleDatatypes.DOUBLEDT;
			} // real tausender
			if (doubletPattern.matcher(abasType).matches()) {
				if(log.isTraceEnabled()) {
					log.trace(abasType+" => doubleT");
				}
				return PossibleDatatypes.DOUBLET;
			} // real dezimal trennzeichen
			if (doubledPattern.matcher(abasType).matches()) {
				if(log.isTraceEnabled()) {
					log.trace(abasType+" => doubleD");
				}
				return PossibleDatatypes.DOUBLED;
			}
			if(log.isTraceEnabled()) {
				log.trace(abasType+" => double");
			}
			return PossibleDatatypes.DOUBLE;
		} 
		
		
		// bool
		if (boolPattern.matcher(abasType).matches()) {
			if(log.isTraceEnabled()) {
				log.trace(abasType+" => boolean");
			}
			return PossibleDatatypes.BOOLEAN;
		}
		if (AbasDate.isDate(abasType)) {
			if(log.isTraceEnabled()) {
				log.trace(abasType+" => abasdate");
			}
			return PossibleDatatypes.ABASDATE;
		} // Zeiger also
		if (pointerPattern.matcher(abasType).matches()) {
			if(log.isTraceEnabled()) {
				log.trace(abasType+" => abaspointer");
			}
			return PossibleDatatypes.ABASPOINTER;
		}
		// Strings
		if(log.isTraceEnabled()) {
			log.trace(abasType+" => string");
		}
		return PossibleDatatypes.STRING;
	}

}
