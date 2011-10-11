package de.uniluebeck.itm.mdc;

import de.uniluebeck.itm.mdc.service.PluginService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PackageRemovedBroadcastReceiver extends BroadcastReceiver {

	private static final String TAG = PackageRemovedBroadcastReceiver.class.getName();
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "Package removed update plugin service.");
		Intent broadcast = new Intent(context, PluginService.class);
		broadcast.setAction(PluginService.PLUGIN_REMOVED);
		broadcast.setData(intent.getData());
		context.startService(broadcast);
	}

}
