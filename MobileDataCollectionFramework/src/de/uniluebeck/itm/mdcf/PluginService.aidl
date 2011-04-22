package de.uniluebeck.itm.mdcf;

import de.uniluebeck.itm.mdcf.PluginServiceListener;
import de.uniluebeck.itm.mdcf.PluginConfiguration;

interface PluginService {
	
	List<PluginConfiguration> getPlugins();
	
	void addListener(PluginServiceListener listener);
	
	void removeListener(PluginServiceListener listener);
}