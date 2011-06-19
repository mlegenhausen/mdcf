package de.uniluebeck.itm.plugin;

import de.uniluebeck.itm.mdcf.XMLPluginRegister;

public class HelloWorldPluginRegister extends XMLPluginRegister {
	
	public static final String TAG = HelloWorldPlugin.class.getName();
	
	public HelloWorldPluginRegister() {
		super("plugin.xml");
	}
}
