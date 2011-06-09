package de.uniluebeck.itm.mdc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
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
import de.uniluebeck.itm.mdc.log.LogEntry;
import de.uniluebeck.itm.mdc.log.LogRecord;
import de.uniluebeck.itm.mdc.service.PluginConfiguration;
import de.uniluebeck.itm.mdc.service.PluginService;
import de.uniluebeck.itm.mdcf.PluginInfo;
import de.uniluebeck.itm.mdcf.PluginIntent;

public class LogViewer extends ExpandableListActivity implements ServiceConnection {

	private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
	
	private static final String GROUP_NAME = "name";
	
	private static final String CHILD_MESSAGE = "message";
	
	private final static int REFRESH = 0;
	
	private PluginService service;
	
	private PluginInfo pluginInfo;
	
	private PluginConfiguration pluginConfiguration;
	
	private SimpleExpandableListAdapter adapter;
	
	private List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
	
	private List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		pluginInfo = getIntent().getParcelableExtra(PluginIntent.PLUGIN_INFO);
		
		adapter = new SimpleExpandableListAdapter(
			this, 
			groupData, 
			R.layout.log_viewer_group_row,
			new String[] { GROUP_NAME },
			new int[] { R.id.log_viewer_group_row_name },
			childData,
			R.layout.log_viewer_child_row,
			new String[] { CHILD_MESSAGE },
			new int[] { R.id.log_viewer_child_row_message }
		);
		setListAdapter(adapter);
		
		getApplicationContext().bindService(new Intent(this, PluginService.class), this, Context.BIND_AUTO_CREATE);
	}

	@Override
	public void onServiceConnected(ComponentName componentName, IBinder binder) {
		service = ((PluginService.PluginServiceBinder) binder).getService();
		refresh();
	}

	@Override
	public void onServiceDisconnected(ComponentName componentName) {
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		menu.add(0, REFRESH, 0, getText(R.string.refresh));
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
	
	private void refresh() {
		pluginConfiguration = service.getPluginConfiguration(pluginInfo);
		List<LogRecord> records = pluginConfiguration.getLogRecords();
		// Sort all records in descending order.
		Collections.sort(records, new Comparator<LogRecord>() {
			@Override
			public int compare(LogRecord record1, LogRecord record2) {
				long timestamp1 = record1.getTimestamp();
				long timestamp2 = record2.getTimestamp();
				
				int result = 0;
				if(timestamp1 < timestamp2) {
					result = 1;
				} else if(timestamp1 > timestamp2) {
		        	result = -1;
		        }
				return result;
			}
		});
		mapGroups(records);
	}
	
	private void mapGroups(List<LogRecord> records) {
		groupData.clear();
		childData.clear();
		for (LogRecord record : records) {
			Map<String, String> map = new HashMap<String, String>();
			map.put(GROUP_NAME, DATE_FORMAT.format(new Date(record.getTimestamp())));
			groupData.add(map);
			
			List<Map<String, String>> childs = mapChilds(record);
			childData.add(childs);
		}
		adapter.notifyDataSetChanged();
	}
	
	private List<Map<String, String>> mapChilds(LogRecord record) {
		List<Map<String, String>> childs = new ArrayList<Map<String, String>>();
		for (LogEntry entry : record.getEntries()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put(CHILD_MESSAGE, entry.getMessage());
			childs.add(map);
		}
		return childs;
	}
}
