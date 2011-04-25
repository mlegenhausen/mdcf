package de.uniluebeck.itm.mdc.service;


public interface PluginServiceListener {

	void onRegistered(PluginServiceEvent event);
	
	void onUnregistered(PluginServiceEvent event);
}
