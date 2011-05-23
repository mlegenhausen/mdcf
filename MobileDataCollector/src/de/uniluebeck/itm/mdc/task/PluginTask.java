package de.uniluebeck.itm.mdc.task;

import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
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
	
	private Plugin plugin;
	
	public PluginTask(final Context context, final PluginConfiguration configuration) {
		this.context = context;
		this.configuration = configuration;
		persistenceManager = new PersistenceManagerImpl(new PluginConfigurationRepository(context), configuration);
	}
	
	@Override
	public void run() {
		boolean result = context.bindService(new Intent(configuration.getPluginInfo().getAction()), this, Context.BIND_AUTO_CREATE);
		if (!result) {
			notifyNotFound();
		}
	}

	@Override
	public void onServiceConnected(ComponentName componentName, IBinder binder) {
		Log.i(LOG_TAG, "Service connected");
		plugin = Plugin.Stub.asInterface(binder);
		try {
			initPlugin();
			execute();
		} catch (RemoteException e) {
			Log.e(LOG_TAG, "Unable to call proceed.", e);
		}
		context.unbindService(this);
	}
	
	private void initPlugin() throws RemoteException {
		Log.d(LOG_TAG, "Setting Persistence Manager...");
		plugin.setPersistenceManager(persistenceManager);
		
		List<String> services = configuration.getPluginInfo().getServices();
		if (services.contains(Context.LOCATION_SERVICE)) {
			Log.d(LOG_TAG, "Setting Location Manager");
			SecureLocationManager locationManager = SecureManagerFactory.createSecureLocationManager(context);
			plugin.setLocationManager(locationManager);
		}
		if (services.contains(Context.WIFI_SERVICE)) {
			Log.d(LOG_TAG, "Setting Wifi Manager");
			SecureWifiManager wifiManager = SecureManagerFactory.createSecureWifiManager(context);
			plugin.setWifiManager(wifiManager);
		}
		if (services.contains(Context.AUDIO_SERVICE)) {
			Log.d(LOG_TAG, "Setting Audio Manager");
			SecureAudioManager audioManager = SecureManagerFactory.createSecureAudioManager(context);
			plugin.setAudioManager(audioManager);
		}
		if (services.contains(Context.CONNECTIVITY_SERVICE)) {
			Log.d(LOG_TAG, "Setting Connectivity Manager");
			SecureConnectivityManager connectivityManager = SecureManagerFactory.createSecureConnectivityManager(context);
			plugin.setConnectivityManager(connectivityManager);
		}
		if (services.contains(Context.TELEPHONY_SERVICE)) {
			Log.d(LOG_TAG, "Setting Telephony Manager");
			SecureTelephonyManager telephonyManager = SecureManagerFactory.createSecureTelephonyManager(context);
			plugin.setTelephonyManager(telephonyManager);
		}
	}
	
	private void execute() throws RemoteException {
		configuration.setState(State.RUNNING);
		notifyStateChange();
		plugin.start();
		configuration.setState(State.STOPPING);
		notifyStateChange();
		plugin.stop();
		configuration.setState(State.WAITING);
		notifyStateChange();
	}
	
	public void destroy() {
		configuration.setState(State.RESOLVED);
		notifyStateChange();
	}

	@Override
	public void onServiceDisconnected(ComponentName paramComponentName) {
		Log.i(LOG_TAG, "Service disconnected");
	}
	
	private void notifyStateChange() {
		for (PluginTaskListener listener : listeners.toArray(new PluginTaskListener[0])) {
			listener.onStateChange(new PluginTaskEvent(this, configuration));
		}
	}
	
	private void notifyNotFound() {
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
