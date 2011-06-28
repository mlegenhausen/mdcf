package de.uniluebeck.itm.mdcf.plugin.locationtracker;

import de.uniluebeck.itm.mdcf.XMLPluginRegister;

public class LocationTrackerPluginRegister extends XMLPluginRegister {
	
	public static final String TAG = LocationTrackerPlugin.class.getName();
	
	public LocationTrackerPluginRegister() {
		super("plugin.xml");
	}
}
