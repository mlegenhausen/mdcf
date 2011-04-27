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
	
	public static final String LOG_TAG = "MobileDataCollectorService";
	
	private final List<PluginConfiguration> pluginConfigurations = new ArrayList<PluginConfiguration>();
	
	private final List<PluginServiceListener> listeners = new ArrayList<PluginServiceListener>();
	
	private final PluginTaskManager manager = new PluginTaskManager(this);
	
	private final IBinder binder = new PluginServiceBinder();
	
	private NotificationManager notificationManager;
	
	@Override
	public void onCreate() {
		super.onCreate();
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		initService();
	}
	
	private void initService() {		
		Notification notification = Notifications.createNotification(this, getText(R.string.notification_mdc_started), getText(R.string.notification_task_wait));
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
			if (intent.hasExtra(PluginIntent.PLUGIN_CONFIGURATION)) {
				final PluginInfo info = intent.getParcelableExtra(PluginIntent.PLUGIN_CONFIGURATION);
				register(new PluginConfiguration(info));
			} else {
				Log.e(LOG_TAG, "Plugin " + action + " has no configuration");
			}
		} else if (PluginIntent.PLUGIN_UNREGISTER.equals(action)) {
			Log.i(LOG_TAG, "Plugin " + action + " wants to unregister");
			if (intent.hasExtra(PluginIntent.PLUGIN_CONFIGURATION)) {
				final PluginInfo info = intent.getParcelableExtra(PluginIntent.PLUGIN_CONFIGURATION);
				// TODO: adjust data structure to find a configuration by his info.
				unregister(new PluginConfiguration(info));
			} else {
				Log.e(LOG_TAG, "Plugin " + action + " has no configuration");
			}
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		manager.destroy();
		stopForeground(true);
		Log.i(LOG_TAG, "MobileDataCollectorService destroyed");
	}
	
	@Override
	public IBinder onBind(Intent paramIntent) {
		Log.i(LOG_TAG, "MobileDataCollectorService binded.");
		return binder;
	}
	
	private void register(final PluginConfiguration plugin) {
		if (!pluginConfigurations.contains(plugin)) {
			pluginConfigurations.add(plugin);
			notifyRegistered(plugin);
			Log.i(LOG_TAG, "Service registered: " + plugin.getPluginInfo().getAction());
		}
	}
	
	private void unregister(final PluginConfiguration plugin) {
		if (pluginConfigurations.contains(plugin)) {
			pluginConfigurations.remove(plugin);
			notifyUnregistered(plugin);
			Log.i(LOG_TAG, "Service unregistered: " + plugin.getPluginInfo().getAction());
		}
	}
	
	private void notifyRegistered(PluginConfiguration configuration) {
		for (final PluginServiceListener listener : listeners.toArray(new PluginServiceListener[0])) {
			listener.onRegistered(new PluginServiceEvent(this, configuration));
		}
	}
	
	private void notifyUnregistered(PluginConfiguration configuration) {
		for (final PluginServiceListener listener : listeners.toArray(new PluginServiceListener[0])) {
			listener.onRegistered(new PluginServiceEvent(this, configuration));
		}
	}
	
	public void activate(PluginConfiguration plugin) {
		plugin.setMode(Mode.ACTIVATED);
		manager.activatePluginConfiguration(plugin).addListener(this);
	}
	
	public void deactivate(PluginConfiguration plugin) {
		plugin.setMode(Mode.DEACTIVATED);
		manager.deactivatePluginConfiguration(plugin).removeListener(this);
	}
	
	public void addListener(PluginServiceListener listener) {
		listeners.add(listener);
	}

	public void removeListener(PluginServiceListener listener) {
		listeners.remove(listener);
	}

	public List<PluginConfiguration> getPluginConfigurations() {
		return pluginConfigurations;
	}

	@Override
	public void onStateChange(PluginTaskEvent event) {
		PluginConfiguration configuration = event.getConfiguration();
		PluginInfo info = configuration.getPluginInfo();
		State state = configuration.getState();
		if (State.RUNNING.equals(state) || State.STOPPING.equals(state)) {
			String title = info.getName();
			String ticker = info.getName();
			if (State.RUNNING.equals(state)) {
				title += " is running";
				ticker += " is collecting data...";
			} else if (State.STOPPING.equals(state)) {
				title += " is stopping";
				ticker += " has stopped collecting data...";
			} 
			Notification notification = Notifications.createNotification(this, title, ticker);
			notificationManager.notify(R.string.foreground_service, notification);
		} else if (State.WAITING.equals(state)) {
			Notification notification = Notifications.createNotification(this, getText(R.string.notification_mdc_started), getText(R.string.notification_task_wait), 5000);
			notificationManager.notify(R.string.foreground_service, notification);
		}
	}
}
