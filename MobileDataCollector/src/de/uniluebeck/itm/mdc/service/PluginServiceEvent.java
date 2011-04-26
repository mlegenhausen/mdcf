package de.uniluebeck.itm.mdc.service;

import java.util.EventObject;

public class PluginServiceEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3508568713982829404L;
	
	private final PluginConfiguration configuration;

	public PluginServiceEvent(Object source, PluginConfiguration configuration) {
		super(source);
		this.configuration = configuration;
	}
	
	public PluginConfiguration getConfiguration() {
		return configuration;
	}
}
