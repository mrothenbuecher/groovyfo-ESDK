package de.finetech.utils.charts;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 * 
 * @author Michael Rothenbücher, Finetech GmbH & Co. KG
 *
 */
public class Value {

	private final static int TYPE_NORMAL = 0, TYPE_BUBBLE = 1, TYPE_HIGHLOW = 2, TYPE_GANTT = 3;

	private int type = Value.TYPE_NORMAL;

	private String[] values = { "", "", "", "" };

	private HashMap<String, String> parameter = new HashMap<String, String>();

	private Value(String value) {
		this.type = Value.TYPE_NORMAL;
		values[0] = value;
	}

	private Value(String start, String end) {
		this.type = Value.TYPE_GANTT;
		values[0] = start;
		values[1] = end;
	}

	private Value(String start, String end, String diameter) {
		this.type = Value.TYPE_BUBBLE;
		values[0] = start;
		values[1] = end;
		values[2] = diameter;
	}

	private Value(String open, String high, String low, String close) {
		this.type = Value.TYPE_HIGHLOW;
		values[0] = open;
		values[1] = high;
		values[2] = low;
		values[3] = close;
	}

	public static Value create(Object value) {
		return new Value(value.toString());
	}

	public static Value create(Object start, Object end) {
		return new Value(start.toString(), end.toString());
	}

	public static Value create(Object start, Object end, Object diameter) {
		return new Value(start.toString(), end.toString(), diameter.toString());
	}

	public static Value create(Object open, Object high, Object low, Object close) {
		return new Value(open.toString(), high.toString(), low.toString(), close.toString());
	}

	public Value setColor(String color) {
		// TODO prüfen ob vorhanden usw.
		// TODO entfernen wenn null oder leer
		this.parameter.put("-color", color);
		return this;
	}

	public Value setRGBcolor(String color) {
		// TODO prüfen ob vorhanden usw.
		// TODO entfernen wenn null oder leer
		this.parameter.put("-rgbcolor", color);
		return this;
	}

	public Value setId(String id) {
		// TODO prüfen ob vorhanden usw.
		// TODO entfernen wenn null oder leer
		this.parameter.put("-id", "\"" + id + "\"");
		return this;
	}

	public Value setData(String data) {
		// TODO prüfen ob vorhanden usw.
		// TODO entfernen wenn null oder leer
		this.parameter.put("-data", "\"" + data + "\"");
		return this;
	}

	public Value setTip(String tip) {
		// TODO prüfen ob vorhanden usw.
		// TODO entfernen wenn null oder leer
		this.parameter.put("-tip", "\"" + tip + "\"");
		return this;
	}

	@Override
	public String toString() {
		String val = "-value ";
		switch (this.type) {
		case Value.TYPE_GANTT:
			val += values[0] + " " + values[1];
			break;
		case Value.TYPE_BUBBLE:
			val += values[0] + " " + values[1] + " " + values[2];
			break;
		case Value.TYPE_HIGHLOW:
			val += values[0] + " " + values[1] + " " + values[2] + " " + values[3];
			break;
		default:
			val += values[0];
		}
		for (Entry<String, String> param : this.parameter.entrySet()) {
			val += " " + param.getKey() + " " + param.getValue();
		}
		return val;
	}
}
