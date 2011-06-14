package de.uniluebeck.itm.mdc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
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
	
	private static final String TAG = MobileDataCollector.class.getName();
	
	private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
	
	private	static final String KEY_PLUGIN = "plugin";
	
	private static final String KEY_MODE = "mode";
	
	private static final String KEY_STATE = "state";
	
	private static final String MARKET_URI = "market://search?q=mdcf";
	
	private static final int ACTIVATE_ID = 1;
	
	private static final int DEACTIVATE_ID = 2;
	
	private static final int DATAVIEWER_ID = 3;
	
	private static final int DETAILS_ID = 4;
	
	private static final int UNINSTALL_ID = 5;
	
	private static final int MARKET_ID = 6;
	
	private static final Map<Mode, String> MODE_MAPPING = new HashMap<Mode, String>();
	
	private SimpleAdapter listAdapter;
	
	private List<PluginConfiguration> pluginConfigurations = new ArrayList<PluginConfiguration>();
	
	private List<Map<String, String>> pluginMappings = new ArrayList<Map<String, String>>();
	
	private PluginService service;
	
	static {
		MODE_MAPPING.put(Mode.NEW, "New");
		MODE_MAPPING.put(Mode.ACTIVATED, "Active");
		MODE_MAPPING.put(Mode.DEACTIVATED, "Deactivated");
	}
	
    /**
     * Called when the activity is first created. 
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        listAdapter = new SimpleAdapter(
    		this, 
    		pluginMappings, 
    		R.layout.mobile_data_collector_plugin_row, 
    		new String[] {
				KEY_PLUGIN, 
				KEY_MODE, 
				KEY_STATE 
    		}, 
    		new int[] { 
				R.id.mobile_data_collector_plugin,
				R.id.mobile_data_collector_mode,
				R.id.mobile_data_collector_state
    		}
        );
        setListAdapter(listAdapter);
        registerForContextMenu(getListView());
    }
    
    @Override
    protected void onStart() {
    	super.onStart();
    	Log.i(TAG, "Bind Service");
    	bindService(new Intent(this, PluginService.class), this, Context.BIND_AUTO_CREATE);
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	unbindService(this);
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	openContextMenu(v);
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
    	menu.add(0, DETAILS_ID, 2, R.string.menu_details);
    	menu.add(0, UNINSTALL_ID, 3, R.string.menu_uninstall);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    	PluginConfiguration plugin = pluginConfigurations.get(info.position);
    	switch (item.getItemId()) {
    	case ACTIVATE_ID:
    		startActivate(plugin);
    		break;
    	case DEACTIVATE_ID:
    		service.deactivate(plugin);
    		break;
    	case DATAVIEWER_ID:
    		startDataViewer(plugin);
    		break;
    	case DETAILS_ID:
    		startActivate(plugin);
    		break;
    	case UNINSTALL_ID:
    		startUninstall(plugin);
    		break;
    	}
    	return super.onContextItemSelected(item);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add(0, MARKET_ID, 0, "Find more Plugins");
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    	case MARKET_ID:
    		startMarket();
    		break;
    	}
    	return true;
    }
    
    private Map<String, String> mapPlugin(PluginConfiguration plugin) {
		final Map<String, String> map = new HashMap<String, String>();
		map.put(KEY_PLUGIN, plugin.getPluginInfo().getName());
		map.put(KEY_MODE, MODE_MAPPING.get(plugin.getMode()));
		map.put(KEY_STATE, formatState(plugin));
		return map;
	}
	
	private String formatState(PluginConfiguration plugin) {
		String result = "";
		switch (plugin.getState()) {
		case RESOLVED:
			if (Mode.NEW.equals(plugin.getMode())) {
				result = "Select to activate this plugin";
			}
			break;
		case WAITING:
			result = "Last Execution: " + DATE_FORMAT.format(new Date(plugin.getLastExecuted()));
			break;
		case RUNNING:
			result = "Executing...";
			break;
		}
		return result;
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
    
    private void startActivate(PluginConfiguration plugin) {
    	PluginInfo pluginInfo = plugin.getPluginInfo();
    	Intent intent = new Intent(this, ActivationActivity.class);
    	intent.putExtra(PluginIntent.PLUGIN_INFO, pluginInfo);
    	startActivity(intent);
    }
    
    private void startDataViewer(PluginConfiguration plugin) {
    	PluginInfo pluginInfo = plugin.getPluginInfo();
		Intent intent = new Intent(this, DataViewer.class);
		intent.putExtra(PluginIntent.PLUGIN_INFO, pluginInfo);
		startActivity(intent);
    }
    
    private void startUninstall(PluginConfiguration plugin) {
    	String uri = "package:" + plugin.getPluginInfo().getPackage();
    	Intent intent = new Intent(Intent.ACTION_DELETE);
    	intent.setData(Uri.parse(uri));
    	startActivity(intent);
    }
    
    private void startMarket() {
    	Intent intent = new Intent(Intent.ACTION_VIEW);
    	intent.setData(Uri.parse(MARKET_URI));
    	startActivity(intent);
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