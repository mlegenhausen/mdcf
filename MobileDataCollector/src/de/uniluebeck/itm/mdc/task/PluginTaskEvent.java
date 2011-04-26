package de.uniluebeck.itm.mdc.task;

import java.util.EventObject;

import de.uniluebeck.itm.mdcf.PluginInfo;

public class PluginTaskEvent extends EventObject {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 9040182235968785001L;

	private final PluginInfo configuration;
	
	public PluginTaskEvent(Object source, PluginInfo configuration) {
		super(source);
		this.configuration = configuration;
	}
	
	public PluginInfo getConfiguration() {
		return configuration;
	}
}
