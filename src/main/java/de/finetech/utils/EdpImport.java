package de.finetech.utils;

import java.util.Collection;
import java.util.Vector;

import de.abas.eks.jfop.remote.EKS;
import de.finetech.groovy.utils.GroovyFOException;

/**
 * 
 * @author Rothenbücher
 *
 */
public class EdpImport {

	/**
	 * 
	 * @author Rothenbücher
	 *
	 */

	public static enum TypedCmd {
		NEW, COPY, UPDATE, STORE, MODIFY, DELETE, REMOVE, VIEW, GET, RELEASE, DELIVERY, INVOICE, PAYMENT, REVERSAL,
		CALCULATE, TRANSFER, DONE, DO
	}

	public class Dataset {
		public String[] headData;
		public Vector<String[]> tableData = new Vector<String[]>();

		public String[] getHeadData() {
			return headData;
		}

		public void setHeadData(String[] headData) {
			this.headData = headData;
		}

		public Vector<String[]> getTableData() {
			return tableData;
		}

		public void setTableData(Vector<String[]> tableData) {
			this.tableData = tableData;
		}

	}

	private String database, group;
	private EdpImport.TypedCmd mode;
	private String[] headFieldNames;
	private String[] tableFieldNames;

	private Vector<Dataset> values = new Vector<Dataset>();

	private EdpImport(String database, String group, EdpImport.TypedCmd TypedCmd) {
		this.database = database;
		this.group = group;
		this.mode = TypedCmd;
	}

	public static EdpImport create(String database, String group) throws GroovyFOException {
		return EdpImport.start(database, group, TypedCmd.NEW);
	}

	public static EdpImport update(String database, String group) throws GroovyFOException {
		return EdpImport.start(database, group, TypedCmd.UPDATE);
	}

	public static EdpImport view(String database, String group) throws GroovyFOException {
		return EdpImport.start(database, group, TypedCmd.VIEW);
	}

	public static EdpImport start(String database, String group, EdpImport.TypedCmd TypedCmd) throws GroovyFOException {
		if (database == null)
			throw new GroovyFOException("database must be specified");
		if (group == null)
			throw new GroovyFOException("group must be specified");
		if (TypedCmd == null)
			throw new GroovyFOException("TypedCmd must be specified");
		return new EdpImport(database, group, TypedCmd);
	}

	public EdpImport setHeadFields(String[] fields) {
		this.headFieldNames = fields;
		return this;
	}

	public EdpImport setTableFields(String[] fields) {
		this.tableFieldNames = fields;
		return this;
	}

	public EdpImport addDateset(Dataset set) {
		this.values.add(set);
		return this;
	}

	public EdpImport addDatesets(Collection<Dataset> set) {
		this.values.addAll(set);
		return this;
	}

	public String getContent() {
		String content = "";
		return content;
	}

	public boolean exec() {
		// TODO
		return EKS.system("edpimport.sh ");
	}

}
