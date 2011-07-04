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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

import de.uniluebeck.itm.mdc.service.PluginConfiguration;
import de.uniluebeck.itm.mdc.service.PluginConfiguration.Mode;
import de.uniluebeck.itm.mdc.service.PluginService;
import de.uniluebeck.itm.mdcf.PluginInfo;
import de.uniluebeck.itm.mdcf.PluginIntent;

public class InfoActivity extends Activity implements ServiceConnection {

	private static final Map<String, String> SERVICE_MAPPING = new HashMap<String, String>();
	
	private static final int DANGEROUS_PERMISSIONS_DIALOG = 0;
	
	private static final int DEACTIVATE_ID = 0;
	
	private static final int ACTIVATE_ID = 1;
	
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
	
	private PluginInfo info;
	
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
		setContentView(R.layout.info);
		
		name = (TextView) findViewById(R.id.activation_name);
		version = (TextView) findViewById(R.id.activation_version);
		url = (TextView) findViewById(R.id.activation_url);
		period = (TextView) findViewById(R.id.activation_period);
		duration = (TextView) findViewById(R.id.activation_duration);
		serviceLayout = (LinearLayout) findViewById(R.id.activation_services);
		description = (TextView) findViewById(R.id.activation_description);
		permissionLayout = (LinearLayout) findViewById(R.id.activation_permissions);
	}
	
	@Override
    protected void onStart() {
    	super.onStart();
    	Intent intent = getIntent();
    	if (intent.hasExtra(PluginIntent.PLUGIN_INFO)) {
    		info = intent.getParcelableExtra(PluginIntent.PLUGIN_INFO);
        	getApplicationContext().bindService(new Intent(this, PluginService.class), this, Context.BIND_AUTO_CREATE);
    	} else if (intent.hasExtra(PluginService.PLUGIN_CONFIGURATION)) {
    		configuration = (PluginConfiguration) intent.getSerializableExtra(PluginService.PLUGIN_CONFIGURATION);
    		showConfiguration();
    	}
    }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (service != null) {
			getApplicationContext().unbindService(this);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Mode mode = configuration.getMode();
		if (Mode.NEW.equals(mode) || Mode.DEACTIVATED.equals(mode)) {
    		menu.add(0, ACTIVATE_ID, 0, R.string.activate);
    	} else {
    		menu.add(0, DEACTIVATE_ID, 0, R.string.deactivate);
    	}
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case DEACTIVATE_ID:
			service.deactivate(configuration);
			break;
		case ACTIVATE_ID:
			if (!configuration.getPermissions().isEmpty()) {
				showDialog(DANGEROUS_PERMISSIONS_DIALOG);
			}
			break;
		}
		return super.onOptionsItemSelected(item);
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
						service.activate(configuration);
						dialog.dismiss();
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
		service = ((PluginService.PluginServiceBinder) binder).getService();
		configuration = service.getPluginConfiguration(info);		
		showConfiguration();
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		service = null;
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
