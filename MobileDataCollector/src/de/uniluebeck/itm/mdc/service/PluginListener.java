package de.uniluebeck.itm.mdc.service;


public interface PluginListener {

	void onRegistered(PluginEvent event);
	
	void onStateChanged(PluginEvent event);
	
	void onModeChanged(PluginEvent event);

	void onRemoved(PluginEvent pluginServiceEvent);
}
