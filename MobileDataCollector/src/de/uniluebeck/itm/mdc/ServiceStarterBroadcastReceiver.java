package de.uniluebeck.itm.mdc;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import de.uniluebeck.itm.mdc.service.PluginService;


/**
 * Starts automatically the plugin service on start up.
 * 
 * @author Malte Legenhausen
 */
public class ServiceStarterBroadcastReceiver extends BroadcastReceiver {

	private static final String TAG = "MobileDataCollectorServiceManager";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		ComponentName comp = new ComponentName(context.getPackageName(), PluginService.class.getName());
		ComponentName service = context.startService(new Intent().setComponent(comp));
		if (service == null) {
			Log.e(TAG, "Could not start service " + comp.toString());
		}
	}

}
