package de.uniluebeck.itm.mdc.task;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import de.uniluebeck.itm.mdcf.Plugin;
import de.uniluebeck.itm.mdcf.PluginInfo;

public class PluginTask extends TimerTask implements ServiceConnection {
	
	private final List<PluginTaskListener> listeners = new ArrayList<PluginTaskListener>();

	private final String LOG_TAG = "Plugin Task";

	private final Context context;
	
	private final PluginInfo configuration;
	
	private Plugin plugin;
	
	public PluginTask(final Context context, final PluginInfo configuration) {
		this.context = context;
		this.configuration = configuration;
	}
	
	@Override
	public void run() {
		context.bindService(new Intent(configuration.getAction()), this, Context.BIND_AUTO_CREATE);
	}

	@Override
	public void onServiceConnected(ComponentName componentName, IBinder binder) {
		Log.i(LOG_TAG, "Service connected");
		plugin = Plugin.Stub.asInterface(binder);
		try {
			plugin.init();
			notifyPluginStart();
			plugin.start();
			notifyPluginStop();
			plugin.stop();
		} catch (RemoteException e) {
			Log.e(LOG_TAG, "Unable to call proceed.", e);
		}
		context.unbindService(this);
	}

	@Override
	public void onServiceDisconnected(ComponentName paramComponentName) {
		Log.i(LOG_TAG, "Service disconnected");
	}
	
	private void notifyPluginStart() {
		for (PluginTaskListener listener : listeners.toArray(new PluginTaskListener[0])) {
			listener.onStart(new PluginTaskEvent(this, configuration));
		}
	}
	
	private void notifyPluginStop() {
		for (PluginTaskListener listener : listeners.toArray(new PluginTaskListener[0])) {
			listener.onStop(new PluginTaskEvent(this, configuration));
		}
	}
	
	public void addListener(PluginTaskListener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(PluginTaskListener listener) {
		listeners.remove(listener);
	}
}
