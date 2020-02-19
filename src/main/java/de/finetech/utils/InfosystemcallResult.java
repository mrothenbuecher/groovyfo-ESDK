package de.finetech.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;
import java.util.Vector;

import de.abas.eks.jfop.remote.EKS;

/**
 * stellt das ergebnis eines Infosystem aufrufs dar.
 * 
 * @author Michael Rothenbücher, Finetech GmbH & Co. KG
 *
 */
public class InfosystemcallResult {

	private TreeMap<String, String> head;
	private Vector<TreeMap<String, String>> table;

	private Infosystemcall call;

	private Vector<String[]> lines;

	private Vector<String[]> getLinesOfFile(String file) throws IOException {
		Vector<String[]> lines = new Vector<String[]>();
		if (file != null) {
			BufferedReader br = new BufferedReader(new FileReader(file));
			for (String line; (line = br.readLine()) != null;) {
				lines.add(line.split("\\["));
			}
			br.close();
		}
		return lines;
	}

	protected InfosystemcallResult(Infosystemcall call) throws IOException {
		this.call = call;
		this.head = new TreeMap<String, String>();
		this.table = new Vector<TreeMap<String, String>>();
		this.lines = this.getLinesOfFile(call.outputFile);
		boolean headFields = call.showAllHeadFields || call.headOutputFields.size() > 0;
		if (headFields) {
			for (int i = 0; i < lines.get(0).length; i++) {
				String key = lines.get(0)[i];
				head.put(key, lines.get(1)[i]);
			}
		}
		if (call.showAllTableFields || call.tableOutputFields.size() > 0) {
			// index der Variablennamen zeile in der Outputdatei festlegen
			int row = (headFields) ? 2 : 0;
			if (!lines.isEmpty() && lines.size() > row) {
				// zeile mit den Variablennamen speichern
				String[] keys = lines.get(row);
				// für jede Datenzeile
				for (int j = row + 1; j < lines.size(); j++) {
					TreeMap<String, String> foo = new TreeMap<String, String>();
					String[] column = lines.get(j);
					for (int i = 0; i < column.length; i++) {
						foo.put(keys[i], column[i]);
					}
					table.add(foo);
				}
			}
		}
	}

	public TreeMap<String, String> getHead() {
		return this.head;
	}

	public Vector<TreeMap<String, String>> getTable() {
		return this.table;
	}

	public String getOutputFile() {
		return call.outputFile;
	}

	public String getInfosystem() {
		return call.infosystem;
	}

	public String getCommando() {
		return call.getCommand();
	}

	public void printFile() {
		for (String[] line : lines) {
			for (String column : line) {
				EKS.println("-lfsuppress " + column + ";");
			}
			EKS.println("");
		}
	}

}
