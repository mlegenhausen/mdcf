package de.uniluebeck.itm.mdc.log;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import de.uniluebeck.itm.mdc.log.LogEntry.Confidentiality;

public class LogRecord implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3247459231080418436L;

	private long id;
	
	private long timestamp;
	
	private List<LogEntry> entries = new LinkedList<LogEntry>();
	
	public LogRecord() {
		timestamp = System.currentTimeMillis();
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public List<LogEntry> getEntries() {
		return entries;
	}

	public void add(LogEntry entry) {
		entries.add(entry);
	}
	
	public void add(Confidentiality confidentiality, String message) {
		LogEntry entry = new LogEntry();
		entry.setConfidentiality(confidentiality);
		entry.setMessage(message);
		entries.add(entry);
	}
	
	public void add(Confidentiality confidentiality, String message, Object... args) {
		add(confidentiality, String.format(message, args));
	}
}
