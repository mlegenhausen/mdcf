package de.uniluebeck.itm.mdc.task;

public interface PluginTaskListener {

	void onStateChange(PluginTaskEvent event);
	
	void onNotFound(PluginTaskEvent event);
}
