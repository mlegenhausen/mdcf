package de.uniluebeck.itm.mdc.log;

public class LogEntry {
	
	public enum Confidentiality {
		HIGH, MEDIUM, LOW
	}
	
	private long id;
	
	private Confidentiality confidentiality;

	private long timestamp;
	
	private String message;

	public LogEntry() {
		timestamp = System.currentTimeMillis();
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Confidentiality getConfidentiality() {
		return confidentiality;
	}

	public void setConfidentiality(Confidentiality confidentiality) {
		this.confidentiality = confidentiality;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
