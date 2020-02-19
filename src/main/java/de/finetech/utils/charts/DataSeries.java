package de.finetech.utils.charts;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Vector;

/**
 * 
 * @author Michael Rothenbücher, Finetech GmbH & Co. KG
 *
 */
public class DataSeries {

	private HashMap<String, String> parameter = new HashMap<String, String>();
	private Vector<Value> values = new Vector<Value>();

	public DataSeries setTitle(String title) {
		// TODO prüfen ob vorhanden usw.
		// TODO entfernen wenn null oder leer
		this.parameter.put("", "\"" + title + "\"");
		return this;
	}

	public DataSeries setId(String id) {
		// TODO prüfen ob vorhanden usw.
		// TODO entfernen wenn null oder leer
		this.parameter.put("-id", id);
		return this;
	}

	public DataSeries setColor(Object color) {
		// TODO prüfen ob vorhanden usw.
		// TODO entfernen wenn null oder leer
		this.parameter.put("-color", color.toString());
		return this;
	}

	public DataSeries setRGBcolor(String color) {
		// TODO prüfen ob vorhanden usw.
		// TODO entfernen wenn null oder leer
		this.parameter.put("-rgbcolor", "\"" + color + "\"");
		return this;
	}

	public DataSeries setLinewidth(int width) {
		// TODO prüfen ob vorhanden usw.
		// TODO entfernen wenn null oder leer
		this.parameter.put("-linewidth ", "" + width);
		return this;
	}

	public DataSeries setPointmarker(Pointmarker marker) {
		// TODO prüfen ob vorhanden usw.
		// TODO entfernen wenn null oder leer
		this.parameter.put("-pointmarker", marker.value());
		return this;
	}

	public DataSeries setPointmarker(ChartType type) {
		// TODO prüfen ob vorhanden usw.
		// TODO entfernen wenn null oder leer
		this.parameter.put("-charttype", type.value());
		return this;
	}

	public DataSeries addValue(Value value) {
		values.add(value);
		return this;
	}

	public DataSeries removeValue(Value value) {
		values.remove(value);
		return this;
	}

	public Vector<Value> getValues() {
		return this.values;
	}

	@Override
	public String toString() {
		String val = "-startvalues";
		for (Entry<String, String> param : this.parameter.entrySet()) {
			val += " " + param.getKey() + " " + param.getValue();
		}
		return val;
	}

}
