package de.finetech.utils.parallel;

import java.util.Vector;

import de.abas.erp.db.DbMessage;

/**
 * Result of a FOP
 * 
 * @author Rothenbücher
 *
 */
public class ParallelResult {

	private String fop;
	private Exception ex= null;
	private Vector<DbMessage> messages;
	
	public ParallelResult(String fop) {
		this.fop = fop;
		messages = new Vector<DbMessage>();
	}
	
	public String getFop() {
		return this.fop;
	}
	
	public void addMessage(DbMessage msg) {
		this.messages.add(msg);
	}
	
	public Vector<DbMessage> getMessages(){
		return this.messages;
	}
	
	public boolean hasExecption() {
		return this.ex != null;
	}
	
	public void setException(Exception ex) {
		this.ex = ex;
	}
	
	public Exception getException() {
		return this.ex;
	}
	
}
