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
import android.os.Parcelable;
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
import de.uniluebeck.itm.mdc.service.PluginEvent;
import de.uniluebeck.itm.mdc.service.PluginListener;
import de.uniluebeck.itm.mdcf.PluginIntent;

public class PluginListActivity extends ListActivity implements ServiceConnection, PluginListener {
	
	private static final String TAG = PluginListActivity.class.getName();
	
	private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
	
	private	static final String KEY_PLUGIN = "plugin";
	
	private static final String KEY_MODE = "mode";
	
	private static final String KEY_STATE = "state";
	
	private static final String MARKET_URI = "market://search?q=mdcf";
	
	private static final int ACTIVATE_ID = 1;
	
	private static final int DEACTIVATE_ID = 2;
	
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
		MODE_MAPPING.put(Mode.TRANSFER, "Transfer");
	}
	
    /**
     * Called when the activity is first created. 
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plugin_list);
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
    	getApplicationContext().bindService(new Intent(this, PluginService.class), this, Context.BIND_AUTO_CREATE);
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	getApplicationContext().unbindService(this);
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	super.onListItemClick(l, v, position, id);
    	PluginConfiguration configuration = pluginConfigurations.get(position);
    	Mode mode = configuration.getMode();
    	if (Mode.NEW.equals(mode) || Mode.DEACTIVATED.equals(mode)) {
    		startActivate(configuration);
    	} else if (Mode.ACTIVATED.equals(mode)) {
    		service.deactivate(configuration);
    	}
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    	super.onCreateContextMenu(menu, v, menuInfo);
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
    	Mode mode = pluginConfigurations.get(info.position).getMode();
    	if (Mode.NEW.equals(mode) || Mode.DEACTIVATED.equals(mode)) {
    		menu.add(0, ACTIVATE_ID, 0, R.string.menu_activate);
    	} else if (Mode.ACTIVATED.equals(mode)) {
    		menu.add(0, DEACTIVATE_ID, 0, R.string.menu_deactivate);
    	}
    	menu.add(0, DETAILS_ID, 2, R.string.menu_details);
    	menu.add(0, UNINSTALL_ID, 3, R.string.menu_uninstall);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    	PluginConfiguration configuration = pluginConfigurations.get(info.position);
    	switch (item.getItemId()) {
    	case ACTIVATE_ID:
    		startActivate(configuration);
    		break;
    	case DEACTIVATE_ID:
    		service.deactivate(configuration);
    		break;
    	case DETAILS_ID:
    		startDetails(configuration);
    		break;
    	case UNINSTALL_ID:
    		startUninstall(configuration);
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
			Mode mode = plugin.getMode();
			if (Mode.NEW.equals(mode)) {
				result = "Select to activate this plugin";
			} else if (Mode.TRANSFER.equals(mode)) {
				result = "Select to start transfer";
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
		Log.i(TAG, "Plugins found: " + pluginConfigurations.size());
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
    	Parcelable pluginInfo = plugin.getPluginInfo();
    	Intent intent = new Intent(this, ActivationActivity.class);
    	intent.putExtra(PluginIntent.PLUGIN_INFO, pluginInfo);
    	startActivity(intent);
    }
    
    private void startDetails(PluginConfiguration plugin) {
    	Parcelable pluginInfo = plugin.getPluginInfo();
		Intent intent = new Intent(this, DetailsActivity.class);
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
	public void onRegistered(PluginEvent event) {
		final String name = event.getConfiguration().getPluginInfo().getName();
		final String text = String.format(getString(R.string.toast_plugin_unregistered), name);
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(PluginListActivity.this, text, Toast.LENGTH_LONG);
				loadPlugins();
			}
		});
	}
	
	@Override
	public void onRemoved(PluginEvent pluginServiceEvent) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				loadPlugins();
			}
		});
	}
	
	@Override
	public void onStateChanged(PluginEvent event) {
		final PluginConfiguration plugin = event.getConfiguration();
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				updatePlugin(plugin);
			}
		});
	}
	
	@Override
	public void onModeChanged(PluginEvent event) {
		final PluginConfiguration plugin = event.getConfiguration();
		Log.d(TAG, "onModeChanged " + plugin.getPluginInfo().getName() + " " + plugin.getMode());
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				updatePlugin(plugin);
			}
		});
	}
}