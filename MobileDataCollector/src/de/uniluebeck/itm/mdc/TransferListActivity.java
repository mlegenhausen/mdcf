package de.uniluebeck.itm.mdc;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import de.uniluebeck.itm.mdc.service.PluginService;
import de.uniluebeck.itm.mdc.service.Transfer;
import de.uniluebeck.itm.mdc.service.TransferEvent;
import de.uniluebeck.itm.mdc.service.TransferListener;

public class TransferListActivity extends ListActivity implements ServiceConnection, TransferListener {

	private static final String TAG = TransferListActivity.class.getName();
	
	private static final  SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
	
	private static final String KEY_PLUGIN = "plugin";
	
	private static final String KEY_TIMESTAMP = "timestamp";
	
	private PluginService service;
	
	private List<Transfer> transfers = newArrayList();
	
	private List<Map<String, String>> transferMappings = newArrayList();
	
	private SimpleAdapter listAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.transfer_list);
		
		listAdapter = new SimpleAdapter(
	    		this, 
	    		transferMappings, 
	    		R.layout.transfer_list_row, 
	    		new String[] {
					KEY_PLUGIN, 
					KEY_TIMESTAMP 
	    		}, 
	    		new int[] { 
					R.id.transfer_list_plugin,
					R.id.transfer_list_timestamp
	    		}
	        );
		setListAdapter(listAdapter);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		getApplicationContext().bindService(new Intent(this, PluginService.class), this, Context.BIND_AUTO_CREATE);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		getApplicationContext().unbindService(this);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder binder) {
		service = ((PluginService.PluginServiceBinder) binder).getService();
		service.addListener(this);
		loadTransfers();
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		
	}
	
	private void loadTransfers() {
		transferMappings.clear();
		transfers = service.getTransfers();
		Collections.sort(newArrayList(transfers), new Comparator<Transfer>() {
			@Override
			public int compare(Transfer transfer1, Transfer transfer2) {
				long timestamp1 = transfer1.getTimestamp();
				long timestamp2 = transfer2.getTimestamp();
				
				int result = 0;
				if(timestamp1 > timestamp2) {
					result = 1;
				} else if(timestamp1 > timestamp2) {
		        	result = -1;
		        }
				return result;
			}
		});
		
		for (Transfer transfer : transfers) {
			addTransfer(transfer);
		}
		listAdapter.notifyDataSetChanged();
	}
	
	private void addTransfer(Transfer transfer) {
		transferMappings.add(mapTransfer(transfer));
	}

	private Map<String, String> mapTransfer(Transfer transfer) {
		Map<String, String> map = newHashMap();
		Log.i(TAG, "Plugin Name: " + transfer.getPluginConfiguration().getPluginInfo().getName());
		map.put(KEY_PLUGIN, transfer.getPluginConfiguration().getPluginInfo().getName());
		map.put(KEY_TIMESTAMP, "Transfer scheduled at " + DATE_FORMAT.format(new Date(transfer.getTimestamp())));
		return map;
	}
	
	@Override
	public void onTransfer(TransferEvent event) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				loadTransfers();
			}
		});
	}
}
