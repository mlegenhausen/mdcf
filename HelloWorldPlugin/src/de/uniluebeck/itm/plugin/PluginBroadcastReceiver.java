package de.uniluebeck.itm.plugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import de.uniluebeck.itm.mdcf.PluginConfiguration;
import de.uniluebeck.itm.mdcf.PluginIntent;

public class PluginBroadcastReceiver extends BroadcastReceiver {
	
	public static final String LOG_TAG = "HelloWorldPlugin";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		final PluginConfiguration configuration = new PluginConfiguration("de.uniluebeck.itm.plugin.HELLO_WORLD", "Hello World Plugin");
		Log.i(LOG_TAG, "Sending service intent");
		context.startService(new PluginIntent(configuration));
	}
}