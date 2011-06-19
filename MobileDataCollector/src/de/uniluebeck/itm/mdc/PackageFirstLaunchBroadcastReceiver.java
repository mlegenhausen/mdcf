package de.uniluebeck.itm.mdc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import de.uniluebeck.itm.mdc.service.PluginService;

public class PackageFirstLaunchBroadcastReceiver extends BroadcastReceiver {

	private static final String TAG = PackageFirstLaunchBroadcastReceiver.class.getName();
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "Package launch the first time");
		Intent broadcast = new Intent(context, PluginService.class);
		broadcast.setAction(PluginService.MDC_FIRST_LAUNCH);
		context.startService(broadcast);
	}

}
