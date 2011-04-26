package de.uniluebeck.itm.mdc.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import de.uniluebeck.itm.mdc.service.PluginConfiguration;

public class PluginTaskManager {

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	
	private final List<PluginConfiguration> plugins = new ArrayList<PluginConfiguration>();
	
	public void addPluginConfiguration(PluginConfiguration configuration) {
		plugins.add(configuration);
	}
	
	public List<PluginConfiguration> getPluginConfigurations() {
		return plugins;
	}
	
	public void schedulePluginConfiguration(PluginConfiguration configuration) {
		
	}
}
