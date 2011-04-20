package de.uniluebeck.itm.plugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;
import de.uniluebeck.itm.mdcf.PluginConfiguration;
import de.uniluebeck.itm.mdcf.PluginService;

public class PluginBroadcastReceiver extends BroadcastReceiver {
	
	public static final String LOG_TAG = "HelloWorldPlugin";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(LOG_TAG, "Message received");
		PluginService service = PluginService.Stub.asInterface(peekService(context, new Intent("de.uniluebeck.itm.mdc.SERVICE"))) ;
		try {
			service.register(new PluginConfiguration("de.uniluebeck.itm.plugin.HELLO_WORLD", "Hello World Plugin"));
		} catch (RemoteException e) {
			Log.e(LOG_TAG, "Unable to register plugin", e);
			e.printStackTrace();
		}
	}
}
