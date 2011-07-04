package de.uniluebeck.itm.mdc.service;

import java.io.Serializable;

public class Transfer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6930613141735825572L;
	
	private final long timestamp;
	
	private final PluginConfiguration pluginConfiguration;
	
	public Transfer(PluginConfiguration pluginConfiguration) {
		timestamp = System.currentTimeMillis();
		this.pluginConfiguration = pluginConfiguration;
	}
	
	public PluginConfiguration getPluginConfiguration() {
		return pluginConfiguration;
	}
	
	public long getTimestamp() {
		return timestamp;
	}
}
