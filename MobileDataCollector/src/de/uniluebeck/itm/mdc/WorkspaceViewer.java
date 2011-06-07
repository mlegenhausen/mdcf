package de.uniluebeck.itm.mdc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import de.uniluebeck.itm.mdc.net.WorkspaceTransmitionTask;
import de.uniluebeck.itm.mdc.service.PluginConfiguration;
import de.uniluebeck.itm.mdc.service.PluginService;
import de.uniluebeck.itm.mdcf.PluginInfo;
import de.uniluebeck.itm.mdcf.PluginIntent;
import de.uniluebeck.itm.mdcf.persistence.Item;
import de.uniluebeck.itm.mdcf.persistence.Node;
import de.uniluebeck.itm.mdcf.persistence.Property;

public class WorkspaceViewer extends ListActivity implements ServiceConnection {
	
	private static final String TAG = WorkspaceViewer.class.getName();
	
	private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
	
	private final static String NODE_FIELD = "name";
	
	private final static int REFRESH = 0;
	
	private final static int TRANSFER = 1;
	
	private SimpleAdapter adapter;
	
	private PluginService service;
	
	private PluginInfo pluginInfo;
	
	private PluginConfiguration pluginConfiguration;
	
	private Node root = null;
	
	private List<Item> items = new ArrayList<Item>();
	
	private Stack<Node> history = new Stack<Node>();
	
	private List<Map<String, String>> itemMapping = new ArrayList<Map<String, String>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		
		pluginInfo = getIntent().getParcelableExtra(PluginIntent.PLUGIN_INFO);
		adapter = new SimpleAdapter(
			this, 
			itemMapping, 
			R.layout.node_row, 
			new String[] { NODE_FIELD }, 
			new int[] { R.id.name }
		);
		setListAdapter(adapter);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG, "onStart");
		// Referee to issue 2483
		getApplicationContext().bindService(new Intent(this, PluginService.class), this, Context.BIND_AUTO_CREATE);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		getApplicationContext().unbindService(this);
	}
	
	private void refresh() {
		pluginConfiguration = service.getPluginConfiguration(pluginInfo);
		showItems(pluginConfiguration.getWorkspace());
	}
	
	private void showItems(Node root) {
		this.root = root;
		itemMapping.clear();
		items.clear();
		mapNodes(root);
		mapProperties(root);
		adapter.notifyDataSetChanged();
	}
	
	private void mapProperties(final Node node) {		
		for (final String name : node.getProperties()) {
			final Property property = node.getProperty(name);
			final Map<String, String> map = new HashMap<String, String>();
			String value = name + ": " +  property.getValue().toString();
			Log.d(TAG, value);
			map.put(NODE_FIELD, value);
			itemMapping.add(map);
			items.add(property);
		}
	}
	
	private void mapNodes(final Node root) {
		final Iterator<Node> iterator = root.getNodes();
		while (iterator.hasNext()) {
			final Node node = iterator.next();
			final Map<String, String> map = new HashMap<String, String>();
			map.put(NODE_FIELD, DATE_FORMAT.format(new Date(node.getTimestamp())));
			itemMapping.add(map);
			items.add(node);
		}
	}
	
	private void transfer() {
		final String url = pluginConfiguration.getPluginInfo().getUrl();
		final Node workspace = pluginConfiguration.getWorkspace();
		new WorkspaceTransmitionTask(this, url).execute(workspace);
	}

	@Override
	public void onServiceConnected(final ComponentName name, final IBinder binder) {
		Log.d(TAG, "onServiceConnected");
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
		menu.add(0, TRANSFER, 0, "Transfer");
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		case REFRESH:
			refresh();
			break;
		case TRANSFER:
			transfer();
			break;
		}
		return true;
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		final Item item = items.get(position);
		if (item instanceof Node) {
			history.push(root);
			showItems((Node) item);
		}
	}
	
	@Override
	public void onBackPressed() {
		if (!history.isEmpty()) {
			showItems(history.pop());
		} else {
			super.onBackPressed();
		}
	}
}
