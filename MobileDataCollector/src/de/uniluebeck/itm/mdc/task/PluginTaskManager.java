package de.uniluebeck.itm.mdc.task;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import de.uniluebeck.itm.mdc.persistence.PluginConfigurationRepository;
import de.uniluebeck.itm.mdc.service.PluginConfiguration;
import de.uniluebeck.itm.mdc.service.PluginConfiguration.Mode;
import de.uniluebeck.itm.mdc.service.PluginConfiguration.State;

public class PluginTaskManager implements PluginTaskListener {

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	
	private final Map<PluginConfiguration, PluginTask> tasks = new HashMap<PluginConfiguration, PluginTask>();
	
	private final Map<PluginConfiguration, ScheduledFuture<?>> futures = new HashMap<PluginConfiguration, ScheduledFuture<?>>();
	
	private final Context context;
	
	private final PluginConfigurationRepository repository;
	
	public PluginTaskManager(Context context, PluginConfigurationRepository repository) {		
		this.context = context;
		this.repository = repository;
	}
	
	public PluginTask activate(PluginConfiguration configuration) {
		if (tasks.containsKey(configuration)) {
			return tasks.get(configuration);
		}
		configuration.setMode(Mode.ACTIVATED);
		configuration.setState(State.RESOLVED);
		repository.store(configuration);
		
		PluginTask task = new PluginTask(context, repository, configuration);
		task.addListener(this);
		tasks.put(configuration, task);
		ScheduledFuture<?> future = scheduler.schedule(task, 0, TimeUnit.MILLISECONDS);
		futures.put(configuration, future);
		return task;
	}
	
	public PluginTask deactivate(PluginConfiguration configuration) {
		configuration.setMode(Mode.DEACTIVATED);
		configuration.setState(State.RESOLVED);
		repository.store(configuration);
		
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
