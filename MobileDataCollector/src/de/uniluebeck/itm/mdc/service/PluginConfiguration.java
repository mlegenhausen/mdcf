package de.uniluebeck.itm.mdc.service;

import java.io.Serializable;

import de.uniluebeck.itm.mdcf.PluginInfo;
import de.uniluebeck.itm.mdcf.persistence.Node;

public class PluginConfiguration implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3205252713479131098L;

	public enum Mode {
		NEW, ACTIVATED, DEACTIVATED
	}
	
	public enum State {
		RESOLVED, WAITING, RUNNING
	}
	
	private PluginInfo pluginInfo;
	
	private Mode mode = Mode.NEW;
	
	private State state = State.RESOLVED;
	
	private Node workspace = new Node();
	
	public PluginConfiguration(PluginInfo pluginInfo) {
		this.pluginInfo = pluginInfo;
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
	
	public Node getWorkspace() {
		return workspace;
	}
	
	public void setWorkspace(Node workspace) {
		this.workspace = workspace;
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
