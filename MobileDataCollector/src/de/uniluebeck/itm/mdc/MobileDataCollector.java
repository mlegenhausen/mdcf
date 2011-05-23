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
import android.widget.Toast;
import de.uniluebeck.itm.mdc.service.PluginConfiguration;
import de.uniluebeck.itm.mdc.service.PluginConfiguration.Mode;
import de.uniluebeck.itm.mdc.service.PluginService;
import de.uniluebeck.itm.mdc.service.PluginServiceEvent;
import de.uniluebeck.itm.mdc.service.PluginServiceListener;
import de.uniluebeck.itm.mdcf.PluginInfo;
import de.uniluebeck.itm.mdcf.PluginIntent;

public class MobileDataCollector extends ListActivity implements ServiceConnection, PluginServiceListener {
	
	public static final String LOG_TAG = "MobileDataCollector";
	
	private	static final String KEY_PLUGIN = "plugin";
	
	private static final int ACTIVATE_ID = 1;
	
	private static final int DEACTIVATE_ID = 2;
	
	private static final int DATAVIEWER_ID = 3;
	
	private SimpleAdapter listAdapter;
	
	private List<PluginConfiguration> pluginConfigurations = new ArrayList<PluginConfiguration>();
	
	private List<Map<String, String>> pluginMappings = new ArrayList<Map<String, String>>();
	
	private PluginService service;
	
	private Map<String, String> mapPlugin(PluginConfiguration plugin) {
		final Map<String, String> map = new HashMap<String, String>();
		map.put(KEY_PLUGIN, plugin.getPluginInfo().getName());
		return map;
	}
	
	private void addPlugin(PluginConfiguration plugin) {
		pluginMappings.add(mapPlugin(plugin));
	}
	
	private void loadPlugins() {
		pluginMappings.clear();
		pluginConfigurations = service.getPluginConfigurations();
		for (final PluginConfiguration plugin : pluginConfigurations) {
			addPlugin(plugin);
		}
		listAdapter.notifyDataSetChanged();
	}
	
	private void updatePlugin(PluginConfiguration plugin) {
		int index = pluginConfigurations.indexOf(plugin);
		if (index > -1) {
			pluginMappings.set(index, mapPlugin(plugin));
			listAdapter.notifyDataSetChanged();
		}
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
    	bindService(new Intent(this, PluginService.class), this, Context.BIND_AUTO_CREATE);
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	unbindService(this);
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    	super.onCreateContextMenu(menu, v, menuInfo);
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
    	Mode mode = pluginConfigurations.get(info.position).getMode();
    	if (Mode.NEW.equals(mode) || Mode.DEACTIVATED.equals(mode)) {
    		menu.add(0, ACTIVATE_ID, 0, R.string.menu_activate);
    	} else {
    		menu.add(0, DEACTIVATE_ID, 0, R.string.menu_deactivate);
    	}
    	menu.add(0, DATAVIEWER_ID, 1, R.string.menu_dataviewer);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    	switch (item.getItemId()) {
    	case ACTIVATE_ID:
    		service.activate(pluginConfigurations.get(info.position));
    		break;
    	case DEACTIVATE_ID:
    		service.deactivate(pluginConfigurations.get(info.position));
    		break;
    	case DATAVIEWER_ID:
    		PluginInfo pluginInfo = pluginConfigurations.get(info.position).getPluginInfo();
    		Intent intent = new Intent(this, ListDataViewer.class);
    		intent.putExtra(PluginIntent.PLUGIN_INFO, pluginInfo);
    		startActivity(intent);
    		break;
    	}
    	return super.onContextItemSelected(item);
    }

	@Override
	public void onServiceConnected(ComponentName paramComponentName, IBinder binder) {
		service = ((PluginService.PluginServiceBinder) binder).getService();
		service.addListener(this);
		loadPlugins();
	}

	@Override
	public void onServiceDisconnected(ComponentName paramComponentName) {
		service.removeListener(this);
		pluginConfigurations.clear();
		pluginMappings.clear();
		listAdapter.notifyDataSetChanged();
	}

	@Override
	public void onRegistered(PluginServiceEvent event) {
		final String name = event.getConfiguration().getPluginInfo().getName();
		final String text = String.format(getString(R.string.toast_plugin_unregistered), name);
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(MobileDataCollector.this, text, Toast.LENGTH_LONG);
				loadPlugins();
			}
		});
	}
	
	@Override
	public void onStateChanged(PluginServiceEvent event) {
		final PluginConfiguration plugin = event.getConfiguration();
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				updatePlugin(plugin);
			}
		});
	}
	
	@Override
	public void onModeChanged(PluginServiceEvent event) {
		final PluginConfiguration plugin = event.getConfiguration();
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				updatePlugin(plugin);
			}
		});
	}
}