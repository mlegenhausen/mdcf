package de.uniluebeck.itm.mdc;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import de.uniluebeck.itm.mdcf.PluginConfiguration;
import de.uniluebeck.itm.mdcf.PluginIntent;

public class PluginService extends Service {

    public class PluginServiceBinder extends Binder {
        PluginService getService() {
            return PluginService.this;
        }
    }
	
	public static final String LOG_TAG = "MobileDataCollectorService";
	
	private final List<PluginServiceListener> listeners = new ArrayList<PluginServiceListener>();
	
	private final List<PluginConfiguration> plugins = new ArrayList<PluginConfiguration>();
	
	private final Timer pluginTimer = new Timer();
	
	private final IBinder binder = new PluginServiceBinder();
	
	private boolean initialized = false;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		initService();
		try {
			handleCommand(intent);
		} catch (RemoteException e) {
			Log.e(LOG_TAG, "Unable to register plugin", e);
		}
		
		Log.i(LOG_TAG, "MobileDataCollectorServices start");
		return START_STICKY;
	}
	
	private void initService() {
		if (initialized) return;
		
		final long when = System.currentTimeMillis();
		final PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MobileDataCollector.class), 0);
		final Notification notification = new Notification(R.drawable.stat_sample, "Hallo Welt", when);
		notification.setLatestEventInfo(this, getText(R.string.app_name), "Collecting Data", contentIntent);
		startForeground(R.string.foreground_service, notification);
		initialized = true;
		Log.i(LOG_TAG, "Sending broadcast");
		sendBroadcast(new Intent(PluginIntent.PLUGIN_ACTION));
	}
	
	private void handleCommand(Intent intent) throws RemoteException {
		final String action = intent.getAction();
		if (PluginIntent.PLUGIN_REGISTER.equals(action)) {
			Log.i(LOG_TAG, "Plugin " + action + " wants to register");
			if (intent.hasExtra(PluginIntent.PLUGIN_CONFIGURATION)) {
				final PluginConfiguration configuration = intent.getParcelableExtra(PluginIntent.PLUGIN_CONFIGURATION);
				register(configuration);
			} else {
				Log.e(LOG_TAG, "Plugin " + action + " has no configuration");
			}
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		pluginTimer.cancel();
		stopForeground(true);
		initialized = false;
		Log.i(LOG_TAG, "MobileDataCollectorService destroyed");
	}
	
	@Override
	public IBinder onBind(Intent paramIntent) {
		Log.i(LOG_TAG, "MobileDataCollectorService binded.");
		return binder;
	}
	
	public void register(final PluginConfiguration plugin) throws RemoteException {
		if (!plugins.contains(plugin)) {
			plugins.add(plugin);
			notifyRegistered(plugin);
			schedulePlugin(plugin);
			Log.i(LOG_TAG, "Service registered: " + plugin.getAction());
		}
	}
	
	private void notifyRegistered(PluginConfiguration category) throws RemoteException {
		for (final PluginServiceListener listener : listeners.toArray(new PluginServiceListener[0])) {
			listener.onRegistered(category);
		}
	}
	
	private void schedulePlugin(PluginConfiguration plugin) {
		pluginTimer.schedule(new PluginTask(this, new Intent(plugin.getAction())), 0, 10000);
	}
	
	public void addListener(PluginServiceListener listener) throws RemoteException {
		Log.i(LOG_TAG, "Listener added");
		listeners.add(listener);
	}

	public void removeListener(PluginServiceListener listener) throws RemoteException {
		Log.i(LOG_TAG, "Listener removed");
		listeners.remove(listener);
	}

	public List<PluginConfiguration> getPlugins() throws RemoteException {
		return plugins;
	}
}
