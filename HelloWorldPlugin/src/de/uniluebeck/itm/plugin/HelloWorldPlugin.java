package de.uniluebeck.itm.plugin;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class HelloWorldPlugin extends Service {
	
	@Override
	public IBinder onBind(Intent intent) {
		return new PluginImpl(this);
	}
}
