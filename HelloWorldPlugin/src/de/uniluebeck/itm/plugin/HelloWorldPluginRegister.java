package de.uniluebeck.itm.plugin;

import android.content.Context;
import de.uniluebeck.itm.mdcf.AbstractPluginRegister;
import de.uniluebeck.itm.mdcf.PluginInfo;

public class HelloWorldPluginRegister extends AbstractPluginRegister {
	
	public static final String TAG = HelloWorldPlugin.class.getName();
	
	@Override
	protected PluginInfo onRegister() {
		final PluginInfo info = new PluginInfo("de.uniluebeck.itm.plugin.HELLO_WORLD", "Hello World Plugin");
		info.setPeriod(30000);
		info.setDuration(1000);
		info.setUrl("http://192.168.1.103/mdcf/receiver.php");
		info.getServices().add(Context.LOCATION_SERVICE);
		return info;
	}
}
