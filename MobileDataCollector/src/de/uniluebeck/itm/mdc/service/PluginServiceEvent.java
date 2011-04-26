package de.uniluebeck.itm.mdc.service;

import java.util.EventObject;

import de.uniluebeck.itm.mdcf.PluginInfo;

public class PluginServiceEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3508568713982829404L;
	
	private final PluginInfo configuration;

	public PluginServiceEvent(Object source, PluginInfo configuration) {
		super(source);
		this.configuration = configuration;
	}
	
	public PluginInfo getConfiguration() {
		return configuration;
	}
}
