package de.uniluebeck.itm.mdc;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import de.uniluebeck.itm.mdc.service.PluginConfiguration;
import de.uniluebeck.itm.mdc.service.PluginService;
import de.uniluebeck.itm.mdcf.PluginInfo;
import de.uniluebeck.itm.mdcf.PluginIntent;

public class ActivationActivity extends Activity implements ServiceConnection {

	private PluginService service;
	
	private PluginConfiguration configuration;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activation);
		
		Button activateButton = (Button) findViewById(R.id.activate);
		activateButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				service.activate(configuration);
				finish();
			}
		});
		
		Button cancelButton = (Button) findViewById(R.id.cancel_activitation);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
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

	@Override
	public void onServiceConnected(ComponentName name, IBinder binder) {
		PluginInfo info = getIntent().getParcelableExtra(PluginIntent.PLUGIN_INFO);
		service = ((PluginService.PluginServiceBinder) binder).getService();
		configuration = service.getPluginConfiguration(info);
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		
	}
}
