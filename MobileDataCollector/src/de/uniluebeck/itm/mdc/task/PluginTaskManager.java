package de.uniluebeck.itm.mdc.task;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import de.uniluebeck.itm.mdc.service.PluginConfiguration;

public class PluginTaskManager {

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	
	public void schedulePluginConfiguration(PluginConfiguration configuration) {
		
	}
}
