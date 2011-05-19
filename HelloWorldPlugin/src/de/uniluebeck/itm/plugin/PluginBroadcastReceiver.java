package de.uniluebeck.itm.plugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import de.uniluebeck.itm.mdcf.PluginInfo;
import de.uniluebeck.itm.mdcf.PluginIntent;

public class PluginBroadcastReceiver extends BroadcastReceiver {
	
	public static final String LOG_TAG = "HelloWorldPlugin";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		final PluginInfo configuration = new PluginInfo("de.uniluebeck.itm.plugin.HELLO_WORLD", "Hello World Plugin");
		configuration.setPeriod(30000);
		configuration.setUrl("http://192.168.1.103/mdcf/receiver.php");
		Log.i(LOG_TAG, "Sending service intent");
		context.startService(new PluginIntent(configuration));
	}
}
