package de.uniluebeck.itm.mdc;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class PluginServiceManager extends Service {

	public static final String LOG_TAG = "MobileDataCollectorService";
	
	public static final String PLUGIN = "de.uniluebeck.itm.mdc.PLUGIN";
	
	private final PluginServiceImpl service = new PluginServiceImpl(this);
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		final long when = System.currentTimeMillis();
		final PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MobileDataCollector.class), 0);
		final Notification notification = new Notification(R.drawable.stat_sample, "Hallo Welt", when);
		notification.setLatestEventInfo(this, getText(R.string.app_name), "Collecting Data", contentIntent);
		startForeground(R.string.foreground_service, notification);
		
		Log.i(LOG_TAG, "MobileDataCollectorServices start");
		return START_STICKY;
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
		sendBroadcast(new Intent(PLUGIN));
		Log.i(LOG_TAG, "MobileDataCollectorService binded.");
		return service;
	}
}
