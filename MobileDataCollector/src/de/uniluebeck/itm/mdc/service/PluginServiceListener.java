package de.uniluebeck.itm.mdc.service;


public interface PluginServiceListener {

	void onRegistered(PluginServiceEvent event);
	
	void onStateChanged(PluginServiceEvent event);
	
	void onModeChanged(PluginServiceEvent event);
}
