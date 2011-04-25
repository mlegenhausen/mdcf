package de.uniluebeck.itm.mdc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.SimpleAdapter;
import de.uniluebeck.itm.mdc.service.PluginService;
import de.uniluebeck.itm.mdc.service.PluginServiceEvent;
import de.uniluebeck.itm.mdc.service.PluginServiceListener;
import de.uniluebeck.itm.mdcf.PluginConfiguration;

public class MobileDataCollector extends ListActivity implements ServiceConnection {
	
	public static final String LOG_TAG = "MobileDataCollector";
	
	public static final String SERVICE = "de.uniluebeck.itm.mdc.SERVICE";
	
	private	static final String KEY_PLUGIN = "plugin";
	
	private static final int DIALOG_PLUGIN_LOADING = 1;
	
	private SimpleAdapter listAdapter;
	
	private List<Map<String, String>> plugins = new ArrayList<Map<String, String>>();
	
	private PluginServiceListener pluginListener;
	
	private PluginService service;
	
	public MobileDataCollector() {
		pluginListener = new PluginServiceListener() {
			
			@Override
			public void onRegistered(final PluginServiceEvent event) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						loadPlugins();
					}
				});
			}
			
			@Override
			public void onUnregistered(PluginServiceEvent event) {
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
		showDialog(DIALOG_PLUGIN_LOADING);
		this.plugins.clear();
		List<PluginConfiguration> plugins = service.getPlugins();
		for (final PluginConfiguration plugin : plugins) {
			addPlugin(plugin);
		}
		listAdapter.notifyDataSetChanged();
		dismissDialog(DIALOG_PLUGIN_LOADING);
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
    	bindService(new Intent(SERVICE), this, Context.BIND_AUTO_CREATE);
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	unbindService(this);
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
    	Dialog dialog = null;
    	switch (id) {
    	case DIALOG_PLUGIN_LOADING:
    		dialog = ProgressDialog.show(MobileDataCollector.this, "", "Loading. Please wait...", true);
    		break;
    	}
    	return dialog;
    }

	@Override
	public void onServiceConnected(ComponentName paramComponentName, IBinder binder) {
		service = ((PluginService.PluginServiceBinder) binder).getService();
		service.addListener(pluginListener);
		loadPlugins();
	}

	@Override
	public void onServiceDisconnected(ComponentName paramComponentName) {
		service.removeListener(pluginListener);
	}
}