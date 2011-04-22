package de.uniluebeck.itm.mdc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.SimpleAdapter;
import de.uniluebeck.itm.mdcf.PluginConfiguration;

public class MobileDataCollector extends ListActivity implements ServiceConnection {
	
	public static final String LOG_TAG = "MobileDataCollector";
	
	public static final String SERVICE = "de.uniluebeck.itm.mdc.SERVICE";
	
	private	static final String KEY_PLUGIN = "plugin";
	
	private SimpleAdapter listAdapter;
	
	private List<Map<String, String>> plugins = new ArrayList<Map<String, String>>();
	
	private PluginServiceListener pluginListener;
	
	private PluginService service;
	
	public MobileDataCollector() {
		pluginListener = new PluginServiceListener() {
			
			@Override
			public void onRegistered(final PluginConfiguration plugin) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						loadPlugins();
					}
				});
			}
		};
	}
	
	private void loadPlugins() {
		this.plugins.clear();
		List<PluginConfiguration> plugins = new ArrayList<PluginConfiguration>(0);
		try {
			plugins = service.getPlugins();
		} catch (RemoteException e) {
			Log.e(LOG_TAG, "Unable to load Plugins.", e);
		}
		for (final PluginConfiguration plugin : plugins) {
			addPlugin(plugin);
		}
		listAdapter.notifyDataSetChanged();
	}
	
	private void addPlugin(PluginConfiguration plugin) {
		final Map<String, String> map = new HashMap<String, String>();
		map.put(KEY_PLUGIN, plugin.getName());
		plugins.add(map);
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        listAdapter = new SimpleAdapter(this, plugins, R.layout.services_row, new String[] { KEY_PLUGIN }, new int[] { R.id.plugin });
        setListAdapter(listAdapter);
    }
    
    @Override
    protected void onStart() {
    	super.onStart();
    	Log.i(LOG_TAG, "Bind Service");
    	startService(new Intent(SERVICE));
    	bindService(new Intent(SERVICE), this, Context.BIND_DEBUG_UNBIND);
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	unbindService(this);
    }

	@Override
	public void onServiceConnected(ComponentName paramComponentName, IBinder binder) {
		service = ((PluginService.PluginServiceBinder) binder).getService();
		try {
			service.addListener(pluginListener);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onServiceDisconnected(ComponentName paramComponentName) {
		try {
			service.removeListener(pluginListener);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}