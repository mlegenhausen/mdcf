package de.uniluebeck.itm.mdc;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import de.uniluebeck.itm.mdc.service.PluginConfiguration;
import de.uniluebeck.itm.mdc.service.PluginService;
import de.uniluebeck.itm.mdcf.PluginInfo;
import de.uniluebeck.itm.mdcf.PluginIntent;

public class ActivationActivity extends Activity implements ServiceConnection {

	private PluginService service;
	
	private PluginConfiguration configuration;
	
	private TextView name;
	
	private TextView version;
	
	private TextView url;
	
	private TextView period;
	
	private TextView duration;
	
	private TextView description;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activation);
		
		name = (TextView) findViewById(R.id.activation_name);
		version = (TextView) findViewById(R.id.activation_version);
		url = (TextView) findViewById(R.id.activation_url);
		period = (TextView) findViewById(R.id.activation_period);
		duration = (TextView) findViewById(R.id.activation_duration);
		description = (TextView) findViewById(R.id.activation_description);
		
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
		showConfiguration();
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		
	}
	
	private void showConfiguration() {
		PluginInfo info = configuration.getPluginInfo();
		name.setText(info.getName());
		version.setText(Objects.firstNonNull(Strings.emptyToNull(info.getVersion()), "Unknown"));
		url.setText(Objects.firstNonNull(Strings.emptyToNull(info.getUrl()), "Unknown"));
		period.setText(info.getPeriod() + "ms");
		duration.setText(info.getDuration() + "ms");
		description.setText(Objects.firstNonNull(Strings.emptyToNull(info.getDescription()), "Unknown"));
	}
}
