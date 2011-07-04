package de.uniluebeck.itm.mdc;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

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
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import de.uniluebeck.itm.mdc.net.HashUniqueIdGenerator;
import de.uniluebeck.itm.mdc.net.TransferRequest;
import de.uniluebeck.itm.mdc.net.UniqueIdGenerator;
import de.uniluebeck.itm.mdc.net.WorkspaceTransmitionTask;
import de.uniluebeck.itm.mdc.service.PluginConfiguration;
import de.uniluebeck.itm.mdc.service.PluginService;
import de.uniluebeck.itm.mdcf.PluginInfo;
import de.uniluebeck.itm.mdcf.PluginIntent;
import de.uniluebeck.itm.mdcf.persistence.Node;

public class TransferActivity extends ActivityGroup implements ServiceConnection {
	
	private static final String TAG = TransferActivity.class.getName();
	
	private static final int DISMISS_DIALOG = 0;

	private PluginService service;

	private UniqueIdGenerator uniqueIdGenerator;
	
	private TelephonyManager telephonyManager;
	
	private PluginInfo pluginInfo;
	
	private PluginConfiguration pluginConfiguration;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		uniqueIdGenerator = new HashUniqueIdGenerator();
		pluginInfo = getIntent().getParcelableExtra(PluginIntent.PLUGIN_INFO);
		
		setContentView(R.layout.transfer);
		
		// Add DetailsActivity for this activity.
		Intent intent = (Intent) getIntent().clone();
		intent.setClass(this, DetailsActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		View view = getLocalActivityManager().startActivity("DetailsActivity", intent).getDecorView();
		view.setPadding(0, 0, 0, 70);
		LinearLayout layout = (LinearLayout) findViewById(R.id.transfer_layout);
		layout.addView(view, 0);
		
		Button transferButton = (Button) findViewById(R.id.transfer_transfer);
		transferButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				transfer();
			}
		});
		
		Button dismissButton = (Button) findViewById(R.id.transfer_dismiss);
		dismissButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		Button cancelButton = (Button) findViewById(R.id.transfer_cancel);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View paramView) {
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
		service = ((PluginService.PluginServiceBinder) binder).getService();
		pluginConfiguration = service.getPluginConfiguration(pluginInfo);
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch(id) {
		case DISMISS_DIALOG:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Dismiss Plugin Data")
				.setMessage("You are about to delete all collected data.\nDo you want to continue?")
				.setCancelable(true)
				.setPositiveButton(R.string.dismiss, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						resetAndFinish();
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

	private void transfer() {
		PluginInfo info = pluginConfiguration.getPluginInfo();
		String url = info.getUrl();
		String subscriberId = telephonyManager.getSubscriberId();
		String action = info.getAction();
		String id = null;
		try {
			id = uniqueIdGenerator.generate(subscriberId, action);
			Log.i(TAG, "Unique id: " + id);
		} catch (NoSuchAlgorithmException e) {
			Log.e(TAG, "Algorithm was not found.", e);
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, "Unsupported Encoding", e);
		}
		Node workspace = pluginConfiguration.getWorkspace();
		WorkspaceTransmitionTask task = new WorkspaceTransmitionTask(this, url) {
			@Override
			protected void onPostExecute(Throwable e) {
				super.onPostExecute(e);
				if (e == null) {
					maskAsTransferedAndFinish();
				}	
			}
		};
		task.execute(new TransferRequest(id, info, workspace));
	}
	
	private void dismiss() {
		showDialog(DISMISS_DIALOG);
	}
	
	private void resetAndFinish() {
		service.reset(pluginConfiguration);
		finish();
	}
	
	private void maskAsTransferedAndFinish() {
		service.transfered(pluginConfiguration);
		finish();
	}
}
