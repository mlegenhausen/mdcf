package de.uniluebeck.itm.mdc.service;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.newLinkedList;

import java.io.Serializable;
import java.util.List;

import de.uniluebeck.itm.mdc.log.LogRecord;
import de.uniluebeck.itm.mdcf.PluginInfo;
import de.uniluebeck.itm.mdcf.persistence.Node;

public class PluginConfiguration implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3205252713479131098L;

	public enum Mode {
		NEW, ACTIVATED, DEACTIVATED, TRANSFER
	}
	
	public enum State {
		RESOLVED, WAITING, RUNNING
	}
	
	private PluginInfo pluginInfo;
	
	private long lastExecuted = -1;
	
	private long lastActivated = -1;
	
	private long totalActivationTime = 0;
	
	private Mode mode = Mode.NEW;
	
	private State state = State.RESOLVED;
	
	private List<String> permissions = newArrayList();
	
	private List<LogRecord> logRecords = newLinkedList();
	
	private List<Transfer> transfers = newLinkedList();
	
	private Node workspace = new Node();
	
	public PluginConfiguration(PluginInfo pluginInfo) {
		this.pluginInfo = pluginInfo;
	}
	
	public long getLastExecuted() {
		return lastExecuted;
	}
	
	public void setLastExecuted(long lastExecuted) {
		this.lastExecuted = lastExecuted;
	}
	
	public long getLastActivated() {
		return lastActivated;
	}

	public void setLastActivated(long lastActivated) {
		this.lastActivated = lastActivated;
	}

	public long getTotalActivationTime() {
		return totalActivationTime;
	}

	public void setTotalActivationTime(long totalActivationTime) {
		this.totalActivationTime = totalActivationTime;
	}

	public LogRecord createLogRecord() {
		LogRecord logRecord = new LogRecord();
		logRecords.add(logRecord);
		return logRecord;
	}
	
	public List<LogRecord> getLogRecords() {
		return logRecords;
	}
	
	public void setLogRecords(List<LogRecord> logRecords) {
		this.logRecords = logRecords;
	}

	public PluginInfo getPluginInfo() {
		return pluginInfo;
	}

	public void setPluginInfo(PluginInfo pluginInfo) {
		this.pluginInfo = pluginInfo;
	}

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
	
	public List<String> getPermissions() {
		return permissions;
	}
	
	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}
	
	public Node getWorkspace() {
		return workspace;
	}
	
	public void setWorkspace(Node workspace) {
		this.workspace = workspace;
	}
	
	public List<Transfer> getTransfers() {
		return transfers;
	}
	
	public void setTransfers(List<Transfer> transfers) {
		this.transfers = transfers;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof PluginInfo) {
			return pluginInfo.getAction().equals(((PluginInfo) o).getAction());
		}
		if (o instanceof PluginConfiguration) {
			return equals(((PluginConfiguration) o).getPluginInfo());
		}
		return super.equals(o);
	}
}
