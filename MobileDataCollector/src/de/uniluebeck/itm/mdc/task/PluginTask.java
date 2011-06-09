package de.uniluebeck.itm.mdc.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.SimpleTimeLimiter;
import com.google.common.util.concurrent.TimeLimiter;
import com.google.common.util.concurrent.UncheckedTimeoutException;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import de.uniluebeck.itm.mdc.log.LogRecord;
import de.uniluebeck.itm.mdc.persistence.PersistenceManagerImpl;
import de.uniluebeck.itm.mdc.persistence.PluginConfigurationRepository;
import de.uniluebeck.itm.mdc.service.PluginConfiguration;
import de.uniluebeck.itm.mdc.service.PluginConfiguration.State;
import de.uniluebeck.itm.mdcf.Plugin;
import de.uniluebeck.itm.mdcf.persistence.PersistenceManager;
import de.uniluebeck.itm.mdcf.service.SecureAudioManager;
import de.uniluebeck.itm.mdcf.service.SecureConnectivityManager;
import de.uniluebeck.itm.mdcf.service.SecureLocationManager;
import de.uniluebeck.itm.mdcf.service.SecureTelephonyManager;
import de.uniluebeck.itm.mdcf.service.SecureWifiManager;

public class PluginTask implements Runnable, ServiceConnection {
	
	private final List<PluginTaskListener> listeners = new ArrayList<PluginTaskListener>();

	private final String LOG_TAG = "Plugin Task";

	private final Context context;
	
	private final PluginConfiguration configuration;
	
	private final PersistenceManager persistenceManager;
	
	private final PluginConfigurationRepository repository;
	
	private final TimeLimiter timeLimiter = new SimpleTimeLimiter();
	
	private Plugin plugin;
	
	private Plugin pluginProxy;
	
	public PluginTask(final Context context, PluginConfigurationRepository repository, final PluginConfiguration configuration) {
		this.context = context;
		this.repository = repository;
		this.configuration = configuration;
		persistenceManager = new PersistenceManagerImpl(new PluginConfigurationRepository(context), configuration);
	}
	
	@Override
	public void run() {
		boolean result = context.bindService(new Intent(configuration.getPluginInfo().getAction()), this, Context.BIND_AUTO_CREATE);
		if (!result) {
			fireNotFound();
		}
	}

	@Override
	public void onServiceConnected(ComponentName componentName, IBinder binder) {
		Log.i(LOG_TAG, "Service connected");
		plugin = Plugin.Stub.asInterface(binder);
		int timeout = configuration.getPluginInfo().getDuration();
		pluginProxy = timeLimiter.newProxy(plugin, Plugin.class, timeout, TimeUnit.MILLISECONDS);
		
		LogRecord logRecord = configuration.createLogRecord();
		try {
			initPlugin(logRecord);
			execute();
		} catch (RemoteException e) {
			Log.e(LOG_TAG, "Unable to call proceed.", e);
		} finally {
			repository.store(configuration);
			context.unbindService(this);
		}
	}
	
	private void initPlugin(LogRecord logRecord) throws RemoteException {
		Log.d(LOG_TAG, "Setting Persistence Manager...");
		plugin.setPersistenceManager(persistenceManager);
		
		List<String> services = configuration.getPluginInfo().getServices();
		if (services.contains(Context.LOCATION_SERVICE)) {
			Log.d(LOG_TAG, "Setting Location Manager");
			SecureLocationManager locationManager = SecureManagerFactory.createSecureLocationManager(context, logRecord);
			plugin.setLocationManager(locationManager);
		}
		if (services.contains(Context.WIFI_SERVICE)) {
			Log.d(LOG_TAG, "Setting Wifi Manager");
			SecureWifiManager wifiManager = SecureManagerFactory.createSecureWifiManager(context, logRecord);
			plugin.setWifiManager(wifiManager);
		}
		if (services.contains(Context.AUDIO_SERVICE)) {
			Log.d(LOG_TAG, "Setting Audio Manager");
			SecureAudioManager audioManager = SecureManagerFactory.createSecureAudioManager(context, logRecord);
			plugin.setAudioManager(audioManager);
		}
		if (services.contains(Context.CONNECTIVITY_SERVICE)) {
			Log.d(LOG_TAG, "Setting Connectivity Manager");
			SecureConnectivityManager connectivityManager = SecureManagerFactory.createSecureConnectivityManager(context, logRecord);
			plugin.setConnectivityManager(connectivityManager);
		}
		if (services.contains(Context.TELEPHONY_SERVICE)) {
			Log.d(LOG_TAG, "Setting Telephony Manager");
			SecureTelephonyManager telephonyManager = SecureManagerFactory.createSecureTelephonyManager(context, logRecord);
			plugin.setTelephonyManager(telephonyManager);
		}
	}
	
	private void execute() throws RemoteException {
		configuration.setState(State.RUNNING);
		fireStateChange();
		try {
			pluginProxy.run();
		} catch (UncheckedTimeoutException e) {
			Log.w(LOG_TAG, "Timeout was reached.");
			killPlugin();
		} finally {
			configuration.setLastExecuted(System.currentTimeMillis());
			configuration.setState(State.WAITING);
			fireStateChange();
		}
	}
	
	private void killPlugin() {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		activityManager.killBackgroundProcesses(configuration.getPluginInfo().getPackage());
	}
	
	public void destroy() {
		configuration.setState(State.RESOLVED);
		fireStateChange();
	}

	@Override
	public void onServiceDisconnected(ComponentName paramComponentName) {
		Log.i(LOG_TAG, "Service disconnected");
	}
	
	private void fireStateChange() {
		for (PluginTaskListener listener : listeners.toArray(new PluginTaskListener[0])) {
			listener.onStateChange(new PluginTaskEvent(this, configuration));
		}
	}
	
	private void fireNotFound() {
		for (PluginTaskListener listener : listeners.toArray(new PluginTaskListener[0])) {
			listener.onNotFound(new PluginTaskEvent(this, configuration));
		}
	}
	
	public void addListener(PluginTaskListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}
	
	public void removeListener(PluginTaskListener listener) {
		listeners.remove(listener);
	}
}
