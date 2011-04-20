package de.uniluebeck.itm.mdc;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;
import de.uniluebeck.itm.mdcf.PluginConfiguration;
import de.uniluebeck.itm.mdcf.PluginService;
import de.uniluebeck.itm.mdcf.PluginServiceListener;

public class PluginServiceImpl extends PluginService.Stub {

	public static final String TAG_LOG = "Plugin Service";
	
	private final Context context;
	
	private final List<PluginServiceListener> listeners = new ArrayList<PluginServiceListener>();
	
	private final List<PluginConfiguration> plugins = new ArrayList<PluginConfiguration>();
	
	private final Timer pluginTimer = new Timer();
	
	public PluginServiceImpl(final Context context) {
		this.context = context;
	}
	
	@Override
	public void register(final PluginConfiguration plugin) throws RemoteException {
		if (!plugins.contains(plugin)) {
			plugins.add(plugin);
			notifyRegistered(plugin);
			schedulePlugin(plugin);
			Log.i(TAG_LOG, "Service registered: " + plugin);
		}
	}
	
	private void notifyRegistered(PluginConfiguration category) throws RemoteException {
		for (final PluginServiceListener listener : listeners.toArray(new PluginServiceListener[0])) {
			listener.onRegistered(category);
		}
	}
	
	private void schedulePlugin(PluginConfiguration plugin) {
		pluginTimer.schedule(new PluginTask(context, new Intent(plugin.getAction())), 0, 10000);
	}
	
	public void destroy() {
		pluginTimer.cancel();
	}

	@Override
	public void addListener(PluginServiceListener listener) throws RemoteException {
		Log.i(TAG_LOG, "Listener added");
		listeners.add(listener);
	}

	@Override
	public void removeListener(PluginServiceListener listener) throws RemoteException {
		Log.i(TAG_LOG, "Listener removed");
		listeners.remove(listener);
	}

	@Override
	public List<PluginConfiguration> getPlugins() throws RemoteException {
		return plugins;
	}

}
