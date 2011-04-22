package de.uniluebeck.itm.mdc;

import de.uniluebeck.itm.mdcf.PluginConfiguration;
import de.uniluebeck.itm.mdcf.PluginIntent;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class PluginServiceManager extends Service {

	public static final String LOG_TAG = "MobileDataCollectorService";
	
	private final PluginServiceImpl service = new PluginServiceImpl(this);
	
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
	}
	
	private void handleCommand(Intent intent) throws RemoteException {
		final String action = intent.getAction();
		if (PluginIntent.PLUGIN_REGISTER.equals(action)) {
			Log.i(LOG_TAG, "Plugin " + action + " wants to register");
			if (intent.hasExtra(PluginIntent.PLUGIN_CONFIGURATION)) {
				final PluginConfiguration configuration = intent.getParcelableExtra(PluginIntent.PLUGIN_CONFIGURATION);
				service.register(configuration);
			} else {
				Log.e(LOG_TAG, "Plugin " + action + " has no configuration");
			}
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		service.destroy();
		stopForeground(true);
		Log.i(LOG_TAG, "MobileDataCollectorService destroyed");
	}
	
	@Override
	public IBinder onBind(Intent paramIntent) {
		Log.i(LOG_TAG, "Sending broadcast");
		sendBroadcast(new Intent(PluginIntent.PLUGIN_ACTION));
		Log.i(LOG_TAG, "MobileDataCollectorService binded.");
		return service;
	}
}
