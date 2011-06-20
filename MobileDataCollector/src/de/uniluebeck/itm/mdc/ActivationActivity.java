package de.uniluebeck.itm.mdc;

import java.util.HashMap;
import java.util.Map;

import android.app.ActivityGroup;
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
import de.uniluebeck.itm.mdc.service.PluginConfiguration;
import de.uniluebeck.itm.mdc.service.PluginService;
import de.uniluebeck.itm.mdcf.PluginInfo;
import de.uniluebeck.itm.mdcf.PluginIntent;

public class ActivationActivity extends ActivityGroup implements ServiceConnection {
	
	private static final Map<String, String> SERVICE_MAPPING = new HashMap<String, String>();
	
	private static final int DANGEROUS_PERMISSIONS_DIALOG = 0;
	
	private PluginService service;
	
	private PluginConfiguration configuration;
	
	private Button activateButton;
	
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
		
		// Add InfoActivity for this activity.
		Intent intent = (Intent) getIntent().clone();
		intent.setClass(this, InfoActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		View view = getLocalActivityManager().startActivity("InfoActivity", intent).getDecorView();
		view.setPadding(0, 0, 0, 70);
		LinearLayout layout = (LinearLayout) findViewById(R.id.activation_layout);
		layout.addView(view, 0);
		
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
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		
	}
}
