package de.finetech.utils;

import static de.abas.eks.jfop.remote.EKS.Uvar;
import static de.abas.eks.jfop.remote.EKS.art;
import static de.abas.eks.jfop.remote.EKS.datei;
import static de.abas.eks.jfop.remote.EKS.hinweis;
import static de.abas.eks.jfop.remote.EKS.println;
import static de.abas.eks.jfop.remote.EKS.system;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Vector;

/**
 * führt einen Infosystem aufruf mit edpinfosys.sh aus
 * 
 * @author Michael Rothenbücher, Finetech GmbH & Co. KG
 *
 */
public class Infosystemcall {

	protected String infosystem = null;
	protected String outputFile = null;
	protected String resultFilter = null;

	protected Vector<String> headOutputFields = new Vector<String>();
	protected Vector<String> tableOutputFields = new Vector<String>();
	protected LinkedHashMap<String, String> headParameter = new LinkedHashMap<String, String>();

	protected boolean showAllTableFields = false;
	protected boolean showAllHeadFields = false;

	protected final static String fieldSeperator = "[";

	public static Infosystemcall build(String infosystem, String outputFile) {
		return new Infosystemcall(infosystem, outputFile);
	}

	public static Infosystemcall build(String infosystem) {
		String varName = new RandomString(5).nextString();
		art("text " + varName);
		datei("-FOP -tempname U|" + varName);
		String outputFile = Uvar(varName);
		return new Infosystemcall(infosystem, outputFile);
	}

	// edpinfosys.sh
	private Infosystemcall(String infosystem, String outputFile) {
		this.infosystem = infosystem;
		this.outputFile = outputFile;
		this.tableOutputFields.setSize(0);
		this.headOutputFields.setSize(0);
	}

	public Infosystemcall setHeadParameter(String name, Object value) {
		this.headParameter.put(name, value == null ? "" : value.toString());
		return this;
	}

	public Infosystemcall setTableParameter(String name, int row, Object value) {
		this.headParameter.put(row + ":" + name, value == null ? "" : value.toString());
		return this;
	}

	public Infosystemcall addHeadOutputField(String name) {
		this.headOutputFields.add(name);
		return this;
	}

	public Infosystemcall addTableOutputField(String name) {
		this.tableOutputFields.add(name);
		return this;
	}

	public Infosystemcall setShowAllTableFields(boolean showAllTableFields) {
		this.showAllTableFields = showAllTableFields;
		return this;
	}

	public Infosystemcall setShowAllHeadFields(boolean showAllHeadFields) {
		this.showAllHeadFields = showAllHeadFields;
		return this;
	}

	public Infosystemcall setResultFilter(String filter) {
		this.resultFilter = filter;
		return this;
	}

	// FIXME umgang mit umlauten und sonderzeichen ....
	// FIXME umgang mit ( usw
	private String buildCommand() {
		String cmd = "edpinfosys.sh -t " + fieldSeperator + " -P -F -N \'" + this.infosystem + "\'";
		String foo = "";
		if (this.headParameter.size() > 0) {
			cmd += " -s ";
			for (String key : this.headParameter.keySet()) {
				foo += (key + "=" + this.headParameter.get(key) + fieldSeperator);
			}
			foo = foo.substring(0, foo.length() - 1);
			cmd += "\'" + foo + "\'";
			foo = "";
		}
		if (this.showAllHeadFields) {
			cmd += " -k +";
		} else {
			if (this.headOutputFields.size() > 0) {
				cmd += " -k ";
				for (String value : this.headOutputFields) {
					foo += value + ",";
				}
				foo = foo.substring(0, foo.length() - 1);
				cmd += "\'" + foo + "\'";
				foo = "";
			}
		}
		if (this.showAllTableFields) {
			// cmd +=" -f +";
		} else {
			if (this.tableOutputFields.size() > 0) {
				cmd += " -f ";
				for (String value : this.tableOutputFields) {
					foo += value + ",";
				}
				foo = foo.substring(0, foo.length() - 1);
				cmd += "\'" + foo + "\'";
				foo = "";
			} else {
				cmd += " -f -";
			}
		}
		if (this.resultFilter != null && !this.resultFilter.isEmpty()) {
			cmd += " -T '" + this.resultFilter + "'";
		}
		cmd += " > " + this.outputFile;
		return cmd;
	}

	public String getCommand() {
		String foo = "";
		for (String key : this.headParameter.keySet()) {
			foo += (key + "=" + this.headParameter.get(key) + "|");
		}
		foo = foo.substring(0, foo.length() - 1);
		// FIXME sprachunabhüngigkeit
		return "*<Infosystem>" + this.infosystem + "<hole>?" + foo;
	}

	public InfosystemcallResult execute() throws IOException {
		hinweis("-SOFORT hole Daten von Infosystem " + this.infosystem);
		if (system("\"" + this.buildCommand() + "\" PASSWORT hinter")) {
			hinweis("-SOFORT Datenbeschaffung von Infosystem " + this.infosystem + " erfolgreich");
			return new InfosystemcallResult(this);
		}
		hinweis("-SOFORT Datenbeschaffung von Infosystem " + this.infosystem + " fehlgeschlagen");
		println("Fehler bei verarbeitung des Kommandos:");
		system("\"" + this.buildCommand() + "\" PASSWORT");
		println(this.buildCommand());
		try {
			BufferedReader br = new BufferedReader(new FileReader(this.outputFile));
			for (String line; (line = br.readLine()) != null;) {
				println(line);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
