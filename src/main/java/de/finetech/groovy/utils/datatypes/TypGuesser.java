package de.finetech.groovy.utils.datatypes;

import java.util.regex.Pattern;

import de.abas.ceks.jedp.EDPEKSArtInfo;

/**
 * 
 * @author Michael Rothenbücher, Finetech GmbH & Co. KG
 *
 */
public class TypGuesser {

	public static enum PossibleDatatypes {
		INTEGER, DOUBLE, DOUBLED, DOUBLET, DOUBLEDT, BOOLEAN, ABASDATE, ABASPOINTER, STRING
	}

	// regulüre Ausdrücke zum erkennen von Variablen arten
	private static Pattern integerPattern = Pattern.compile("(?:I(?:[0-9]*|P[^ ]*|N[^ ]*)|(?:K[^ ]*)|(?:AN[^ ]*))");
	private static Pattern doublePattern = Pattern.compile("(?:(?:R[^ ]*)|(?:M[^ ]*))");
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
		
		/*
		if (integerPattern.matcher(abasType).matches()) {
			return PossibleDatatypes.INTEGER;
		} 
		*/
		if(nfo.getIntegerDigits() > 0 && nfo.getFractionDigits() == 0) {
			return PossibleDatatypes.INTEGER;
		}
		
		
		// real tausender und dezimal
		/*
		if (doublePattern.matcher(abasType).matches()) {
			// Real
			if (doubledtPattern.matcher(abasType).matches()) {
				return PossibleDatatypes.DOUBLEDT;
			} // real tausender
			if (doubletPattern.matcher(abasType).matches()) {
				return PossibleDatatypes.DOUBLET;
			} // real dezimal trennzeichen
			if (doubledPattern.matcher(abasType).matches()) {
				return PossibleDatatypes.DOUBLED;
			} 
			return PossibleDatatypes.DOUBLE;
		}*/
		if (nfo.getIntegerDigits() > 0 && nfo.getFractionDigits() > 0) {
			// Real
			if (doubledtPattern.matcher(abasType).matches()) {
				return PossibleDatatypes.DOUBLEDT;
			} // real tausender
			if (doubletPattern.matcher(abasType).matches()) {
				return PossibleDatatypes.DOUBLET;
			} // real dezimal trennzeichen
			if (doubledPattern.matcher(abasType).matches()) {
				return PossibleDatatypes.DOUBLED;
			} 
			return PossibleDatatypes.DOUBLE;
		} 
		
		
		// bool
		if (boolPattern.matcher(abasType).matches()) {
			return PossibleDatatypes.BOOLEAN;
		}
		if (AbasDate.isDate(abasType)) {
			return PossibleDatatypes.ABASDATE;
		} // Zeiger also
		if (pointerPattern.matcher(abasType).matches()) {
			return PossibleDatatypes.ABASPOINTER;
		}
		// Strings
		return PossibleDatatypes.STRING;
		/*
		 * Matcher matcher = newPattern.matcher(abasType); if (matcher.find()) { if
		 * (matcher.group(1) != null) { return PossibleDatatypes.INTEGER; } if
		 * (matcher.group(2) != null) { // System.out.println(
		 * "2 PossibleDatatypes.DOUBLE -> "+ matcher.group(2)); // real tausender und
		 * dezimal if (doubledtPattern.matcher(abasType).matches()) { return
		 * PossibleDatatypes.DOUBLEDT; } // real tausender if
		 * (doubletPattern.matcher(abasType).matches()) { return
		 * PossibleDatatypes.DOUBLET; } // real dezimal trennzeichen if
		 * (doubledPattern.matcher(abasType).matches()) { return
		 * PossibleDatatypes.DOUBLED; } // Real if
		 * (doublePattern.matcher(abasType).matches()) { return
		 * PossibleDatatypes.DOUBLE; } } if (matcher.group(3) != null) { //
		 * System.out.println( "2 PossibleDatatypes.BOOL -> "+ matcher.group(3)); return
		 * PossibleDatatypes.BOOLEAN; } if (matcher.group(4) != null) { //
		 * System.out.println( "2 PossibleDatatypes.ABASPOINTER -> "+ matcher.group(4));
		 * return PossibleDatatypes.ABASPOINTER; } } if (AbasDate.isDate(abasType)) {
		 * return PossibleDatatypes.ABASDATE; } return PossibleDatatypes.STRING;
		 */
	}

}
