package de.uniluebeck.itm.mdc;

import java.util.TimerTask;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import de.uniluebeck.itm.mdcf.Plugin;

public class PluginTask extends TimerTask implements ServiceConnection {
	
	private final String LOG_TAG = "Plugin Task";

	private final Context context;
	
	private final Intent pluginIntent;
	
	private Plugin plugin;
	
	public PluginTask(final Context context, final Intent pluginIntent) {
		this.context = context;
		this.pluginIntent = pluginIntent;
	}
	
	@Override
	public void run() {
		context.bindService(pluginIntent, this, Context.BIND_AUTO_CREATE);
	}

	@Override
	public void onServiceConnected(ComponentName componentName, IBinder binder) {
		Log.i(LOG_TAG, "Service connected");
		plugin = Plugin.Stub.asInterface(binder);
		try {
			Log.i(LOG_TAG, "Proceed: " + plugin.proceed());
		} catch (RemoteException e) {
			Log.e(LOG_TAG, "Unable to call proceed.", e);
		}
		context.unbindService(this);
	}

	@Override
	public void onServiceDisconnected(ComponentName paramComponentName) {
		Log.i(LOG_TAG, "Service disconnected");
	}
}
