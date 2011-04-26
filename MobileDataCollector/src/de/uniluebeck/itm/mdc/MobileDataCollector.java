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
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.SimpleAdapter;
import de.uniluebeck.itm.mdc.service.PluginConfiguration;
import de.uniluebeck.itm.mdc.service.PluginService;
import de.uniluebeck.itm.mdc.service.PluginServiceEvent;
import de.uniluebeck.itm.mdc.service.PluginServiceListener;

public class MobileDataCollector extends ListActivity implements ServiceConnection {
	
	public static final String LOG_TAG = "MobileDataCollector";
	
	public static final String SERVICE = "de.uniluebeck.itm.mdc.SERVICE";
	
	private	static final String KEY_PLUGIN = "plugin";
	
	private static final int ACTIVATE_ID = 1;
	
	private SimpleAdapter listAdapter;
	
	private List<PluginConfiguration> plugins = new ArrayList<PluginConfiguration>();
	
	private List<Map<String, String>> pluginMappings = new ArrayList<Map<String, String>>();
	
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
		pluginMappings.clear();
		plugins = service.getPlugins();
		for (final PluginConfiguration plugin : plugins) {
			addPlugin(plugin);
		}
		listAdapter.notifyDataSetChanged();
	}
	
	private void addPlugin(PluginConfiguration plugin) {
		final Map<String, String> map = new HashMap<String, String>();
		map.put(KEY_PLUGIN, plugin.getPluginInfo().getName());
		pluginMappings.add(map);
	}
	
    /**
     * Called when the activity is first created. 
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        listAdapter = new SimpleAdapter(this, pluginMappings, R.layout.services_row, new String[] { KEY_PLUGIN }, new int[] { R.id.plugin });
        setListAdapter(listAdapter);
        registerForContextMenu(getListView());
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    	super.onCreateContextMenu(menu, v, menuInfo);
    	menu.add(0, ACTIVATE_ID, 0, R.string.menu_activate);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case ACTIVATE_ID:
    		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    		service.activate(plugins.get(info.position));
    		break;
    	}
    	return super.onContextItemSelected(item);
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