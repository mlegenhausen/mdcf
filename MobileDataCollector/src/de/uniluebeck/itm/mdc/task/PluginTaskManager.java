package de.uniluebeck.itm.mdc.task;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import de.uniluebeck.itm.mdc.service.PluginConfiguration;
import de.uniluebeck.itm.mdc.service.PluginConfiguration.State;

public class PluginTaskManager implements PluginTaskListener {

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	
	private final Map<PluginConfiguration, PluginTask> tasks = new HashMap<PluginConfiguration, PluginTask>();
	
	private final Map<PluginConfiguration, ScheduledFuture<?>> futures = new HashMap<PluginConfiguration, ScheduledFuture<?>>();
	
	private final Context context;
	
	public PluginTaskManager(Context context) {		
		this.context = context;
	}
	
	public PluginTask activate(PluginConfiguration configuration) {
		if (tasks.containsKey(configuration)) {
			return tasks.get(configuration);
		}
		
		PluginTask task = new PluginTask(context, configuration);
		task.addListener(this);
		tasks.put(configuration, task);
		ScheduledFuture<?> future = scheduler.schedule(task, 0, TimeUnit.MILLISECONDS);
		futures.put(configuration, future);
		return task;
	}
	
	public PluginTask deactivate(PluginConfiguration configuration) {		
		PluginTask task = tasks.remove(configuration);
		if (task != null) {
			task.destroy();
			futures.get(configuration).cancel(true);
			futures.remove(configuration);
		}
		return task;
	}
	
	public void destroy() {
		for (PluginConfiguration configuration : tasks.keySet()) {
			deactivate(configuration);
		}
	}

	@Override
	public void onStateChange(PluginTaskEvent event) {
		PluginConfiguration configuration = event.getConfiguration();
		State state = configuration.getState();
		if (State.WAITING.equals(state)) {
			long period = configuration.getPluginInfo().getPeriod();
			PluginTask task = tasks.get(configuration);
			ScheduledFuture<?> future = scheduler.schedule(task, period, TimeUnit.MILLISECONDS);
			futures.put(configuration, future);
		}
	}
	
	@Override
	public void onNotFound(PluginTaskEvent event) {
		deactivate(event.getConfiguration());
	}
}
