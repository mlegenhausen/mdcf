package de.uniluebeck.itm.mdc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import de.uniluebeck.itm.mdcf.PluginIntent;

public class PackageAddedBroadcastReceiver extends BroadcastReceiver {

	private static final String TAG = PackageAddedBroadcastReceiver.class.getName();
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "New package was installed. Reload plugins.");
		context.sendBroadcast(new Intent(PluginIntent.PLUGIN_ACTION));
	}
}
