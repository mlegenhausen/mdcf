package de.uniluebeck.itm.mdc.service;

import java.util.ArrayList;
import java.util.List;

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

public class PluginService extends Service implements PluginTaskListener {

    public class PluginServiceBinder extends Binder {
        public PluginService getService() {
            return PluginService.this;
        }
    }
    
    public static final String MDC_FIRST_LAUNCH = "de.uniluebeck.itm.mdc.MDC_FIRST_LAUNCH";
	
	public static final String PLUGIN_ADDED = "de.uniluebeck.itm.mdc.PLUGIN_ADDED";
	
	public static final String PLUGIN_REMOVED = "de.uniluebeck.itm.mdc.PLUGIN_REMOVED";
	
	private static final String TAG = PluginService.class.getName();
	
	private final List<PluginServiceListener> listeners = new ArrayList<PluginServiceListener>();
	
	private final IBinder binder = new PluginServiceBinder();
	
	private PluginTaskManager pluginTaskManager;
	
	private PluginConfigurationRepository repository;
	
	private NotificationManager notificationManager;
	
	private PluginPermissionChecker checker;
	
	@Override
	public void onCreate() {
		super.onCreate();
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		repository = new PluginConfigurationRepository(this);
		pluginTaskManager = new PluginTaskManager(this, repository);
		checker = new PluginPermissionChecker(this);
		initService();
	}
	
	private void initService() {		
		Notification notification = Notifications.createNotification(this, R.string.notification_task_wait);
		notification.tickerText = getString(R.string.notification_mdc_started);
		startForeground(R.string.foreground_service, notification);
		Log.i(TAG, "Sending broadcast");
		sendBroadcast(new Intent(PluginIntent.PLUGIN_FIND));
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		handleCommand(intent);
		
		Log.i(TAG, "MobileDataCollectorServices start");
		return START_STICKY;
	}
	
	private void handleCommand(Intent intent) {
		final String action = intent.getAction();
		Log.i(TAG, "Handle action: " + action);
		if (MDC_FIRST_LAUNCH.equals(action)) {
			firstLaunch();
		} else if (PLUGIN_ADDED.equals(action)) {
			String pkg = intent.getDataString();
			Log.i(TAG, "Package " + pkg + " was added");
			pluginAdded(pkg);
		} else if (PLUGIN_REMOVED.equals(action)) {
			String pkg = intent.getDataString();
			Log.i(TAG, "Package " + pkg + " was removed");
			pluginRemoved(pkg);
		} else if (PluginIntent.PLUGIN_REGISTER.equals(action)) {
			Log.i(TAG, "Plugin " + action + " wants to register");
			if (intent.hasExtra(PluginIntent.PLUGIN_INFO)) {
				final PluginInfo info = intent.getParcelableExtra(PluginIntent.PLUGIN_INFO);
				pluginRegister(info);
			} else {
				Log.e(TAG, "Plugin " + action + " has no configuration");
			}
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		pluginTaskManager.destroy();
		repository.close();
		stopForeground(true);
		Log.i(TAG, "MobileDataCollectorService destroyed");
	}
	
	@Override
	public IBinder onBind(Intent paramIntent) {
		Log.i(TAG, "MobileDataCollectorService binded.");
		return binder;
	}
	
	private void firstLaunch() {
		// Execute a whole system broadcast to find all already installed plugins.
	}
	
	private void pluginRegister(final PluginInfo info) {
		PluginConfiguration configuration = repository.findByPluginInfo(info);
		if (configuration == null) {
			configuration = new PluginConfiguration(info);
			configuration = checker.updatePermissions(configuration);
			repository.store(configuration);
			fireRegistered(configuration);
			Log.i(TAG, "Service registered: " + configuration.getPluginInfo().getAction());
		} else {
			// Update Plugin configuration.
			if (Mode.ACTIVATED.equals(configuration.getMode())) {
				activate(configuration);
			}
		}
	}
	
	private void pluginAdded(final String pkg) {
		Log.i(TAG, "Sending broadcast to find plugin with package: " + pkg);
		Intent intent = new Intent(PluginIntent.PLUGIN_FIND);
		//intent.setPackage(pkg);
		sendBroadcast(intent);
	}
	
	private void pluginRemoved(final String pkg) {
		// Find plugin configuration by package name
		// Remove from repository
		// Send event
	}
	
	private void fireRegistered(PluginConfiguration configuration) {
		for (final PluginServiceListener listener : listeners.toArray(new PluginServiceListener[0])) {
			listener.onRegistered(new PluginServiceEvent(this, configuration));
		}
	}
	
	private void fireStateChanged(PluginConfiguration configuration) {
		for (final PluginServiceListener listener : listeners.toArray(new PluginServiceListener[0])) {
			listener.onStateChanged(new PluginServiceEvent(this, configuration));
		}
	}
	
	private void fireModeChanged(PluginConfiguration configuration) {
		for (final PluginServiceListener listener : listeners.toArray(new PluginServiceListener[0])) {
			listener.onModeChanged(new PluginServiceEvent(this, configuration));
		}
	}
	
	public void activate(PluginConfiguration configuration) {
		pluginTaskManager.activate(configuration).addListener(this);
	}
	
	public void deactivate(PluginConfiguration configuration) {
		pluginTaskManager.deactivate(configuration).removeListener(this);
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
	
	private void showPluginRunningNotification(String name) {
		String content = String.format(getString(R.string.plugin_collecting), name);
		Notification notification = Notifications.createNotification(this, content);
		notification.tickerText = String.format(getString(R.string.plugin_running), name);
		notificationManager.notify(R.string.foreground_service, notification);
	}

	@Override
	public void onStateChange(PluginTaskEvent event) {
		PluginConfiguration configuration = event.getConfiguration();
		PluginInfo info = configuration.getPluginInfo();
		State state = configuration.getState();
		String name = info.getName();
		if (State.RUNNING.equals(state)) {
			showPluginRunningNotification(name);
		} else if (State.WAITING.equals(state)) {
			Notification notification = Notifications.createNotification(this, R.string.notification_task_wait);
			notificationManager.notify(R.string.foreground_service, notification);
		}
		fireStateChanged(configuration);
	}
	
	@Override
	public void onNotFound(PluginTaskEvent event) {
		String name = event.getConfiguration().getPluginInfo().getName();
		Notification notification = Notifications.createNotification(this, name + " was not found");
		notificationManager.notify(R.string.foreground_service, notification);
		fireModeChanged(event.getConfiguration());
	}
}
