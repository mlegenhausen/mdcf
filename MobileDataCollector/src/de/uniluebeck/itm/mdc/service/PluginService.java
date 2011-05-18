package de.uniluebeck.itm.mdc.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import de.uniluebeck.itm.mdc.R;
import de.uniluebeck.itm.mdc.persistence.PluginConfigurationRepository;
import de.uniluebeck.itm.mdc.service.PluginConfiguration.Mode;
import de.uniluebeck.itm.mdc.service.PluginConfiguration.State;
import de.uniluebeck.itm.mdc.task.PluginTaskEvent;
import de.uniluebeck.itm.mdc.task.PluginTaskListener;
import de.uniluebeck.itm.mdc.task.PluginTaskManager;
import de.uniluebeck.itm.mdc.util.Notifications;
import de.uniluebeck.itm.mdcf.PluginInfo;
import de.uniluebeck.itm.mdcf.PluginIntent;

import java.util.ArrayList;
import java.util.List;

public class PluginService extends Service implements PluginTaskListener {

    public class PluginServiceBinder extends Binder {
        public PluginService getService() {
            return PluginService.this;
        }
    }
	
	public static final String LOG_TAG = "MobileDataCollectorService";
	
	private final List<PluginServiceListener> listeners = new ArrayList<PluginServiceListener>();
	
	private final PluginTaskManager manager = new PluginTaskManager(this);
	
	private final IBinder binder = new PluginServiceBinder();
	
	private PluginConfigurationRepository repository;
	
	private NotificationManager notificationManager;
	
	@Override
	public void onCreate() {
		super.onCreate();
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		repository = new PluginConfigurationRepository(this);
		initService();
	}
	
	private void initService() {		
		Notification notification = Notifications.createNotification(this, R.string.notification_task_wait);
		notification.tickerText = getString(R.string.notification_mdc_started);
		startForeground(R.string.foreground_service, notification);
		Log.i(LOG_TAG, "Sending broadcast");
		sendBroadcast(new Intent(PluginIntent.PLUGIN_ACTION));
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		handleCommand(intent);
		
		Log.i(LOG_TAG, "MobileDataCollectorServices start");
		return START_STICKY;
	}
	
	private void handleCommand(Intent intent) {
		final String action = intent.getAction();
		if (PluginIntent.PLUGIN_REGISTER.equals(action)) {
			Log.i(LOG_TAG, "Plugin " + action + " wants to register");
			if (intent.hasExtra(PluginIntent.PLUGIN_INFO)) {
				final PluginInfo info = intent.getParcelableExtra(PluginIntent.PLUGIN_INFO);
				register(info);
			} else {
				Log.e(LOG_TAG, "Plugin " + action + " has no configuration");
			}
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		manager.destroy();
		repository.close();
		stopForeground(true);
		Log.i(LOG_TAG, "MobileDataCollectorService destroyed");
	}
	
	@Override
	public IBinder onBind(Intent paramIntent) {
		Log.i(LOG_TAG, "MobileDataCollectorService binded.");
		return binder;
	}
	
	private void register(final PluginInfo info) {
		PluginConfiguration configuration = repository.findByPluginInfo(info);
		if (configuration == null) {
			configuration = new PluginConfiguration(info);
			repository.store(configuration);
			notifyRegistered(configuration);
			Log.i(LOG_TAG, "Service registered: " + configuration.getPluginInfo().getAction());
		} else if (Mode.ACTIVATED.equals(configuration.getMode())) {
			activate(configuration);
		}
	}
	
	private void notifyRegistered(PluginConfiguration configuration) {
		for (final PluginServiceListener listener : listeners.toArray(new PluginServiceListener[0])) {
			listener.onRegistered(new PluginServiceEvent(this, configuration));
		}
	}
	
	public void activate(PluginConfiguration configuration) {
		configuration.setMode(Mode.ACTIVATED);
		repository.store(configuration);
		manager.activatePluginConfiguration(configuration).addListener(this);
	}
	
	public void deactivate(PluginConfiguration configuration) {
		configuration.setMode(Mode.DEACTIVATED);
		repository.store(configuration);
		manager.deactivatePluginConfiguration(configuration).removeListener(this);
	}
	
	public void addListener(PluginServiceListener listener) {
		listeners.add(listener);
	}

	public void removeListener(PluginServiceListener listener) {
		listeners.remove(listener);
	}

	public List<PluginConfiguration> getPluginConfigurations() {
		return repository.findAll();
	}
	
	public PluginConfiguration getPluginConfiguration(PluginInfo info) {
		return repository.findByPluginInfo(info);
	}
	
	private void notifyPluginRunning(String name) {
		String content = String.format(getString(R.string.plugin_collecting), name);
		Notification notification = Notifications.createNotification(this, content);
		notification.tickerText = String.format(getString(R.string.plugin_running), name);
		notificationManager.notify(R.string.foreground_service, notification);
	}
	
	private void notifyPluginStopping(String name) {
		String content = String.format(getString(R.string.plugin_stop_collecting), name);
		Notification notification = Notifications.createNotification(this, content);
		notification.tickerText = String.format(getString(R.string.plugin_stopping), name);
		notificationManager.notify(R.string.foreground_service, notification);
	}

	@Override
	public void onStateChange(PluginTaskEvent event) {
		PluginConfiguration configuration = event.getConfiguration();
		PluginInfo info = configuration.getPluginInfo();
		State state = configuration.getState();
		String name = info.getName();
		if (State.RUNNING.equals(state)) {
			notifyPluginRunning(name);
		} else if (State.STOPPING.equals(state)) {
			notifyPluginStopping(name);
		} else if (State.WAITING.equals(state)) {
			Notification notification = Notifications.createNotification(this, R.string.notification_task_wait);
			notificationManager.notify(R.string.foreground_service, notification);
		}
	}
}
