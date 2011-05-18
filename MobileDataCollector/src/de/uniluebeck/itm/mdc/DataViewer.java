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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleExpandableListAdapter;
import de.uniluebeck.itm.mdc.service.PluginConfiguration;
import de.uniluebeck.itm.mdc.service.PluginService;
import de.uniluebeck.itm.mdcf.PluginInfo;
import de.uniluebeck.itm.mdcf.PluginIntent;
import de.uniluebeck.itm.mdcf.persistence.Node;

public class DataViewer extends ExpandableListActivity implements ServiceConnection {

	private final static String TAG = DataViewer.class.getName();
	
	private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
	
	private final static String NODE_FIELD = "name";
	
	private final static String PROPERTY_FIELD = "property";
	
	private final static int REFRESH = 0;
	
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
			new String[] { NODE_FIELD }, 
			new int[] { R.id.name }, 
			children, 
			R.layout.property_row, 
			new String[] { PROPERTY_FIELD }, 
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
		groups.clear();
		children.clear();
		pluginConfiguration = service.getPluginConfiguration(pluginInfo);
		final Node workspace = pluginConfiguration.getWorkspace();
		final Iterator<Node> iterator = workspace.getNodes();
		while (iterator.hasNext()) {
			final Map<String, String> nodeMap = new HashMap<String, String>();
			final Node node = iterator.next();
			nodeMap.put(NODE_FIELD, DATE_FORMAT.format(new Date(node.getTimestamp())));
			groups.add(nodeMap);
			
			final List<Map<String, String>> subList = new ArrayList<Map<String, String>>();
			mapNodes(subList, node);
			mapProperties(subList, node, PROPERTY_FIELD);
			children.add(subList);
		}
		mapProperties(groups, workspace, NODE_FIELD);
		adapter.notifyDataSetChanged();
	}
	
	private static void mapProperties(final List<Map<String, String>> list, final Node node, final String id) {		
		for (final String property : node.getProperties()) {
			final Map<String, String> map = new HashMap<String, String>();
			map.put(id, property + ": " +  node.getProperty(property).getValue().toString());
			list.add(map);
		}
	}
	
	private static void mapNodes(final List<Map<String, String>> list, final Node node) {
		final Iterator<Node> iterator = node.getNodes();
		while (iterator.hasNext()) {
			final Node subNode = iterator.next();
			final Map<String, String> subMap = new HashMap<String, String>();
			subMap.put(PROPERTY_FIELD, DATE_FORMAT.format(new Date(subNode.getTimestamp())));
			list.add(subMap);
		}
	}

	@Override
	public void onServiceConnected(final ComponentName name, final IBinder binder) {
		service = ((PluginService.PluginServiceBinder) binder).getService();
		refresh();
	}

	@Override
	public void onServiceDisconnected(final ComponentName name) {
		service = null;
	}
	
	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		menu.add(0, REFRESH, 0, "Refresh");
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		case REFRESH:
			refresh();
			break;
		}
		return true;
	}
}
