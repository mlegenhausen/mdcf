package de.uniluebeck.itm.mdc.task;

import java.util.EventObject;

import de.uniluebeck.itm.mdcf.PluginConfiguration;

public class PluginTaskEvent extends EventObject {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 9040182235968785001L;

	private final PluginConfiguration configuration;
	
	public PluginTaskEvent(Object source, PluginConfiguration configuration) {
		super(source);
		this.configuration = configuration;
	}
	
	public PluginConfiguration getConfiguration() {
		return configuration;
	}
}
