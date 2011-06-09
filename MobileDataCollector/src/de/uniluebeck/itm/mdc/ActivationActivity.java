package de.uniluebeck.itm.mdc;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

import de.uniluebeck.itm.mdc.service.PluginConfiguration;
import de.uniluebeck.itm.mdc.service.PluginConfiguration.Mode;
import de.uniluebeck.itm.mdc.service.PluginService;
import de.uniluebeck.itm.mdcf.PluginInfo;
import de.uniluebeck.itm.mdcf.PluginIntent;

public class ActivationActivity extends Activity implements ServiceConnection {
	
	private static final Map<String, String> SERVICE_MAPPING = new HashMap<String, String>();
	
	private static final int DANGEROUS_PERMISSIONS_DIALOG = 0;
	
	private PluginService service;
	
	private PluginConfiguration configuration;
	
	private TextView name;
	
	private TextView version;
	
	private TextView url;
	
	private TextView period;
	
	private TextView duration;
	
	private LinearLayout serviceLayout;
	
	private TextView description;
	
	private LinearLayout permissionLayout;
	
	private Button activateButton;
	
	private Button deactivateButton;
	
	static {
		SERVICE_MAPPING.put(Context.LOCATION_SERVICE, "Location Service");
		SERVICE_MAPPING.put(Context.WIFI_SERVICE, "Wifi Service");
		SERVICE_MAPPING.put(Context.AUDIO_SERVICE, "Audio Service");
		SERVICE_MAPPING.put(Context.CONNECTIVITY_SERVICE, "Connectivity Service");
		SERVICE_MAPPING.put(Context.TELEPHONY_SERVICE, "Telephony Service");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activation);
		
		name = (TextView) findViewById(R.id.activation_name);
		version = (TextView) findViewById(R.id.activation_version);
		url = (TextView) findViewById(R.id.activation_url);
		period = (TextView) findViewById(R.id.activation_period);
		duration = (TextView) findViewById(R.id.activation_duration);
		serviceLayout = (LinearLayout) findViewById(R.id.activation_services);
		description = (TextView) findViewById(R.id.activation_description);
		permissionLayout = (LinearLayout) findViewById(R.id.activation_permissions);
		
		activateButton = (Button) findViewById(R.id.activate);
		activateButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!configuration.getPermissions().isEmpty()) {
					showDialog(DANGEROUS_PERMISSIONS_DIALOG);
				} else {
					activateAndFinish();
				}
			}
		});
		
		deactivateButton = (Button) findViewById(R.id.deactivate);
		deactivateButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				service.deactivate(configuration);
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
	
	private void activateAndFinish() {
		service.activate(configuration);
		finish();
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
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch(id) {
		case DANGEROUS_PERMISSIONS_DIALOG:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Dangerous Permissions found")
				.setMessage("You are about to activate a Plugin with dangerous permissions.\nDo you want to continue?")
				.setCancelable(true)
				.setPositiveButton(R.string.activate, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						activateAndFinish();
					}
				})
				.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
			dialog = builder.create();
			break;
		}
		return dialog;
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder binder) {
		PluginInfo info = getIntent().getParcelableExtra(PluginIntent.PLUGIN_INFO);
		service = ((PluginService.PluginServiceBinder) binder).getService();
		configuration = service.getPluginConfiguration(info);
		
		if (configuration.getMode().equals(Mode.ACTIVATED)) {
			activateButton.setVisibility(View.INVISIBLE);
			deactivateButton.setVisibility(View.VISIBLE);
		} else {
			activateButton.setVisibility(View.VISIBLE);
			deactivateButton.setVisibility(View.INVISIBLE);
		}
		
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
		for (String service : info.getServices()) {
			TextView textView = new TextView(this);
			textView.setTextAppearance(this, android.R.attr.textAppearanceMedium);
			textView.setPadding(5, 3, 3, 3);
			textView.setText(SERVICE_MAPPING.get(service));
			serviceLayout.addView(textView);
		}
		description.setText(Objects.firstNonNull(Strings.emptyToNull(info.getDescription()), "Unknown"));
		for (String permission : configuration.getPermissions()) {
			TextView textView = new TextView(this);
			textView.setTextAppearance(this, android.R.attr.textAppearanceMedium);
			textView.setPadding(5, 3, 3, 3);
			textView.setText(permission);
			permissionLayout.addView(textView);
		}
	}
}
