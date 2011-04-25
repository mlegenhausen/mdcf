package de.uniluebeck.itm.mdc.task;

public interface PluginTaskListener {

	void onStart(PluginTaskEvent event);
	
	void onStop(PluginTaskEvent event);
}
