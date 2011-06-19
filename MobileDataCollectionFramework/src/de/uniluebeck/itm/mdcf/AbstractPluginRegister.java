package de.uniluebeck.itm.mdcf;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public abstract class AbstractPluginRegister extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		PluginInfo info = onRegister();
		if (info != null) {
			info.setPackage(context.getPackageName());
			context.startService(new PluginIntent(info));
		}
	}
	
	protected abstract PluginInfo onRegister();
}
