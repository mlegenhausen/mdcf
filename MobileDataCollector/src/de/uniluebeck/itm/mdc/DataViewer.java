package de.uniluebeck.itm.mdc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.ExpandableListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.SimpleExpandableListAdapter;

import com.google.common.collect.Iterators;

import de.uniluebeck.itm.mdc.service.PluginConfiguration;
import de.uniluebeck.itm.mdc.service.PluginService;
import de.uniluebeck.itm.mdcf.PluginInfo;
import de.uniluebeck.itm.mdcf.PluginIntent;
import de.uniluebeck.itm.mdcf.persistence.Node;

public class DataViewer extends ExpandableListActivity implements ServiceConnection {

	private final static String TAG = DataViewer.class.getName();
	
	private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
	
	private SimpleExpandableListAdapter adapter;
	
	private PluginService service;
	
	private PluginInfo pluginInfo;
	
	private PluginConfiguration pluginConfiguration;
	
	private List<Map<String, String>> groups = new ArrayList<Map<String, String>>();
	
	private List<List<Map<String, String>>> children = new ArrayList<List<Map<String, String>>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		pluginInfo = getIntent().getParcelableExtra(PluginIntent.PLUGIN_INFO);
		adapter = new SimpleExpandableListAdapter(
			this, 
			groups, 
			R.layout.node_row, 
			new String[] { "name" }, 
			new int[] { R.id.name }, 
			children, 
			R.layout.property_row, 
			new String[] { "property" }, 
			new int[] { R.id.property }
		);
		setListAdapter(adapter);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		bindService(new Intent(this, PluginService.class), this, Context.BIND_AUTO_CREATE);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(this);
	}
	
	private void refresh() {
		final Node workspace = pluginConfiguration.getWorkspace();
		Log.i(TAG, "Length: " + Iterators.size(workspace.getNodes()));
		final Iterator<Node> iterator = workspace.getNodes();
		while (iterator.hasNext()) {
			Map<String, String> curGroupMap = new HashMap<String, String>();
			final Node node = iterator.next();
			curGroupMap.put("name", DATE_FORMAT.format(new Date(node.getTimestamp())));
			groups.add(curGroupMap);
			
			List<Map<String, String>> curChildList = new ArrayList<Map<String, String>>();
			for (String property : node.getProperties()) {
				Map<String, String> curChildMap = new HashMap<String, String>();
				curChildMap.put("property", property + ": " + node.getProperty(property).getValue().toString());
				curChildList.add(curChildMap);
			}
			children.add(curChildList);
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder binder) {
		service = ((PluginService.PluginServiceBinder) binder).getService();
		pluginConfiguration = service.getPluginConfiguration(pluginInfo);
		refresh();
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		service = null;
	}
}
