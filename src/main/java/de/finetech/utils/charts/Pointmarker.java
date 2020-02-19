package de.finetech.utils.charts;

/**
 * 
 * @author Michael Rothenbücher, Finetech GmbH & Co. KG
 *
 */
public enum Pointmarker {
	NONE("NONE"), RECT("RECT"), CIRCLE("CIRCLE"), TRIANGLE("TRIANGLE"), DIAMOND("DIAMOND"), MARBLE("MARBLE"),
	HORZLINE("HORZLINE"), VERTLINE("VERTLINE"), CROSS("CROSS"), INVERTEDTRIANGLE("INVERTEDTRIANGLE"), MANY("MANY");

	private String value;

	private Pointmarker(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}
}
