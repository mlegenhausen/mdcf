package de.uniluebeck.itm.mdc.service;

import de.uniluebeck.itm.mdcf.PluginInfo;

public class PluginConfiguration {

	public enum Mode {
		NEW, ACTIVATED, DEACTIVATED
	}
	
	public enum State {
		WAITING, RUNNING, STOPPING
	}
	
	private PluginInfo pluginInfo;
	
	private Mode mode;
	
	private State state;

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
}
