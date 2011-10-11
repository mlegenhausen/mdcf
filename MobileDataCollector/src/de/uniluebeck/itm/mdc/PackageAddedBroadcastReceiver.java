package de.uniluebeck.itm.mdc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import de.uniluebeck.itm.mdc.service.PluginService;

public class PackageAddedBroadcastReceiver extends BroadcastReceiver {

	private static final String TAG = PackageAddedBroadcastReceiver.class.getName();
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "New package was installed. Reload plugins.");
		Intent broadcast = new Intent(context, PluginService.class);
		broadcast.setAction(PluginService.PLUGIN_ADDED);
		broadcast.setData(intent.getData());
		context.startService(broadcast);
	}
}
