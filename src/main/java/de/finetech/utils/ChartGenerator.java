package de.finetech.utils;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Vector;

import de.abas.eks.jfop.remote.EKS;
import de.finetech.groovy.utils.GroovyFOException;
import de.finetech.utils.charts.ChartType;
import de.finetech.utils.charts.Constant;
import de.finetech.utils.charts.DataSeries;
import de.finetech.utils.charts.Pointmarker;
import de.finetech.utils.charts.Stripe;
import de.finetech.utils.charts.Value;

/**
 * 
 * @author Michael Rothenbücher, Finetech GmbH & Co. KG
 *
 */
public class ChartGenerator {

	private ChartType type;

	private HashMap<String, String> parameter = new HashMap<String, String>();

	private Vector<Constant> constants = new Vector<Constant>();
	private Vector<Stripe> stripes = new Vector<Stripe>();

	private Vector<DataSeries> dataseries = new Vector<DataSeries>();

	private ChartGenerator(ChartType type) {
		this.type = type;
	}

	public static ChartGenerator create(ChartType type) throws GroovyFOException {
		if (type == null)
			throw new GroovyFOException("charttype must be specified");
		return new ChartGenerator(type);
	}

	public ChartGenerator setChartTitle(String title) {
		// TODO prüfen ob vorhanden usw.
		// TODO entfernen wenn null oder leer
		this.parameter.put("-titlechart", "\"" + title + "\"");
		return this;
	}

	public ChartGenerator setBackcolor(Object color) {
		// TODO prüfen ob vorhanden usw.
		// TODO entfernen wenn null oder leer
		this.parameter.put("-backcolor", color.toString());
		return this;
	}

	public ChartGenerator setRGBBackcolor(String color) {
		// TODO prüfen ob vorhanden usw.
		// TODO entfernen wenn null oder leer
		this.parameter.put("-rgbbackcolor", "\"" + color + "\"");
		return this;
	}

	public ChartGenerator setPointmarker(Pointmarker marker) {
		// TODO prüfen ob vorhanden usw.
		// TODO entfernen wenn null oder leer
		this.parameter.put("-pointmarker", marker.value());
		return this;
	}

	public ChartGenerator setShowLegend(boolean legend) {
		if (legend) {
			this.parameter.remove("-legend");
		} else {
			this.parameter.put("-legend", "NONE");
		}
		return this;
	}

	public ChartGenerator set3d(boolean yes) {
		// TODO prüfen ob vorhanden usw.
		// TODO entfernen wenn null oder leer
		this.parameter.put("-3d", yes ? "YES" : "NO");
		return this;
	}

	public ChartGenerator setStacktype(String value) {
		// TODO prüfen ob vorhanden usw.
		// TODO entfernen wenn null oder leer
		this.parameter.put("-stacktype", value);
		return this;
	}

	public ChartGenerator setOptions(boolean showOptions) {
		if (showOptions) {
			this.parameter.put("-options", "");
		} else {
			this.parameter.remove("-options");
		}
		return this;
	}

	public ChartGenerator setChangevalues(String value) {
		// TODO prüfen ob vorhanden usw.
		// TODO entfernen wenn null oder leer
		this.parameter.put("-changevalues", value);
		return this;
	}

	public ChartGenerator setContextmenu(boolean hideContextmenu) {
		if (hideContextmenu) {
			this.parameter.put("-contextmenu", "NONE");
		} else {
			this.parameter.remove("-contextmenu");
		}
		return this;
	}

	public ChartGenerator setClustered(boolean clustered) {
		if (clustered) {
			this.parameter.put("-clustered", "");
		} else {
			this.parameter.remove("-clustered");
		}
		return this;
	}

	public ChartGenerator setTitlex(String value) {
		// TODO prüfen ob vorhanden usw.
		// TODO entfernen wenn null oder leer
		this.parameter.put("-titlex", "\"" + value + "\"");
		return this;
	}

	public ChartGenerator setTitley(String value) {
		// TODO prüfen ob vorhanden usw.
		// TODO entfernen wenn null oder leer
		this.parameter.put("-titley", "\"" + value + "\"");
		return this;
	}

	public ChartGenerator setMarkerx(String... values) {
		// TODO prüfen ob vorhanden usw.
		// TODO entfernen wenn null oder leer
		String value = "";
		for (String foo : values) {
			value += foo + "; ";
		}
		this.parameter.put("-markerx", "\"" + value + "\"");
		return this;
	}

	public ChartGenerator setMarkery(String... values) {
		// TODO prüfen ob vorhanden usw.
		// TODO entfernen wenn null oder leer
		String value = "";
		for (String foo : values) {
			value += foo + "; ";
		}
		this.parameter.put("-markery", "\"" + value + "\"");
		return this;
	}

	public ChartGenerator setFormatx(String value) {
		// TODO prüfen ob vorhanden usw.
		// TODO entfernen wenn null oder leer
		this.parameter.put("-formatx", value);
		return this;
	}

	public ChartGenerator setFormaty(String value) {
		// TODO prüfen ob vorhanden usw.
		// TODO entfernen wenn null oder leer
		this.parameter.put("-formaty", value);
		return this;
	}

	public ChartGenerator setMinmaxx(String start, String end) {
		// TODO prüfen ob vorhanden usw.
		// TODO entfernen wenn null oder leer
		this.parameter.put("-minmaxx", start + " " + end);
		return this;
	}

	public ChartGenerator setMinmaxy(String start, String end) {
		// TODO prüfen ob vorhanden usw.
		// TODO entfernen wenn null oder leer
		this.parameter.put("-minmaxy", start + " " + end);
		return this;
	}

	public ChartGenerator setStepx(String value) {
		// TODO prüfen ob vorhanden usw.
		// TODO entfernen wenn null oder leer
		this.parameter.put("-stepx", value);
		return this;
	}

	public ChartGenerator setStepy(String value) {
		// TODO prüfen ob vorhanden usw.
		// TODO entfernen wenn null oder leer
		this.parameter.put("-stepy", value);
		return this;
	}

	public ChartGenerator setMinorstepx(String value) {
		// TODO prüfen ob vorhanden usw.
		// TODO entfernen wenn null oder leer
		this.parameter.put("-minorstepx", value);
		return this;
	}

	public ChartGenerator setMinorstepy(String value) {
		// TODO prüfen ob vorhanden usw.
		// TODO entfernen wenn null oder leer
		this.parameter.put("-minorstepy", value);
		return this;
	}

	public ChartGenerator setScrollx(String start, String end) {
		// TODO prüfen ob vorhanden usw.
		// TODO entfernen wenn null oder leer
		this.parameter.put("-scrollx", start + " " + end);
		return this;
	}

	public ChartGenerator setScrolly(String start, String end) {
		// TODO prüfen ob vorhanden usw.
		// TODO entfernen wenn null oder leer
		this.parameter.put("-scrolly", start + " " + end);
		return this;
	}

	public ChartGenerator set2Levelsx(boolean levels) {
		if (levels) {
			this.parameter.put("-2levelsx", "");
		} else {
			this.parameter.remove("-2levelsx");
		}
		return this;
	}

	public ChartGenerator set2Levelsy(boolean levels) {
		if (levels) {
			this.parameter.put("-2levelsy", "");
		} else {
			this.parameter.remove("-2levelsy");
		}
		return this;
	}

	public ChartGenerator setAnglex(int angle) {
		if (angle >= -90 && angle <= 90) {
			this.parameter.put("-anglex", "" + angle);
		}
		return this;
	}

	public ChartGenerator setAngley(int angle) {
		if (angle >= -90 && angle <= 90) {
			this.parameter.put("-angley", "" + angle);
		}
		return this;
	}

	public ChartGenerator setMinorgridx(boolean minorgrid) {
		if (minorgrid) {
			this.parameter.put("-minorgridx", "");
		} else {
			this.parameter.remove("-minorgridx");
		}
		return this;
	}

	public ChartGenerator setMinorgridy(boolean minorgrid) {
		if (minorgrid) {
			this.parameter.put("-minorgridy", "");
		} else {
			this.parameter.remove("-minorgridy");
		}
		return this;
	}

	public ChartGenerator setDecimalsy(int decimals) {
		this.parameter.put("-decimalsy", "" + decimals);
		return this;
	}

	public ChartGenerator addConstant(Constant c) {
		this.constants.add(c);
		return this;
	}

	public ChartGenerator removeConstant(Constant c) {
		this.constants.remove(c);
		return this;
	}

	public ChartGenerator addStripe(Stripe c) {
		this.stripes.add(c);
		return this;
	}

	public ChartGenerator removeStripe(Stripe c) {
		this.stripes.remove(c);
		return this;
	}

	public ChartGenerator addDataseries(DataSeries c) {
		this.dataseries.add(c);
		return this;
	}

	public ChartGenerator removeDataseries(DataSeries c) {
		this.dataseries.remove(c);
		return this;
	}

	/**
	 * 
	 * @param fieldName - Names eines Chart Feldes im Kopfbereich
	 */
	public boolean generate(String fieldName) {
		return generate(fieldName, true);
	}

	/**
	 * 
	 * @param fieldName   - variable in dem das Diagramm dargestellt werden soll
	 * @param isHeadField - Feld befindet sich im Kopf
	 */
	public boolean generate(String fieldName, boolean isHeadField) {
		EKS.chart("-INIT");
		EKS.chart("-param -charttype " + type.value());
		for (Entry<String, String> param : this.parameter.entrySet()) {
			EKS.chart("-param " + param.getKey() + " " + param.getValue());
		}
		for (Constant c : this.constants) {
			EKS.chart(c.toString());
		}
		for (Stripe stripe : this.stripes) {
			EKS.chart(stripe.toString());
		}
		for (DataSeries series : this.dataseries) {
			EKS.chart(series.toString());
			for (Value value : series.getValues()) {
				EKS.chart(value.toString());
			}
		}
		return EKS.chart("-SHOW " + fieldName + " " + ((isHeadField) ? "0" : "1"));
	}

}
