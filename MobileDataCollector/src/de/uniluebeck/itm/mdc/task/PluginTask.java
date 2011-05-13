package de.uniluebeck.itm.mdc.task;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.LocationManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import de.uniluebeck.itm.mdc.service.PluginConfiguration;
import de.uniluebeck.itm.mdc.service.PluginConfiguration.State;
import de.uniluebeck.itm.mdcf.Plugin;
import de.uniluebeck.itm.mdcf.location.SecureLocationManager;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;

public class PluginTask implements Runnable, ServiceConnection {
	
	private final List<PluginTaskListener> listeners = new ArrayList<PluginTaskListener>();

	private final String LOG_TAG = "Plugin Task";

	private final Context context;
	
	private final PluginConfiguration configuration;
	
	private final LocationManager locationManager;
	
	private final SecureLocationManager secureLocationManager;
	
	private Plugin plugin;
	
	public PluginTask(final Context context, final PluginConfiguration configuration) {
		this.context = context;
		this.configuration = configuration;
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		Preconditions.checkState(locationManager != null, "Location manager is not available");
		secureLocationManager = new SecureLocationManagerImpl(locationManager);
	}
	
	@Override
	public void run() {
		context.bindService(new Intent(configuration.getPluginInfo().getAction()), this, Context.BIND_AUTO_CREATE);
	}

	@Override
	public void onServiceConnected(ComponentName componentName, IBinder binder) {
		Log.i(LOG_TAG, "Service connected");
		plugin = Plugin.Stub.asInterface(binder);
		try {
			execute();
		} catch (RemoteException e) {
			Log.e(LOG_TAG, "Unable to call proceed.", e);
		}
		context.unbindService(this);
	}
	
	private void execute() throws RemoteException {
		plugin.init(secureLocationManager);
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
	
	public void addListener(PluginTaskListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}
	
	public void removeListener(PluginTaskListener listener) {
		listeners.remove(listener);
	}
}
