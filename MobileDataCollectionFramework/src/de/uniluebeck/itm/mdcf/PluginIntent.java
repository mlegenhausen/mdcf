package de.uniluebeck.itm.mdcf;

import android.content.Intent;

public class PluginIntent extends Intent {
	
	public static final String PLUGIN_ACTION = "de.uniluebeck.itm.mdc.PLUGIN";
	
	public static final String PLUGIN_REGISTER = "de.uniluebeck.itm.mdc.PLUGIN_REGISTER";
	
	public static final String PLUGIN_INFO = "de.uniluebeck.itm.mdc.PLUGIN_INFO";

	public PluginIntent(final PluginInfo configuration) {
		super(PLUGIN_REGISTER);
		putExtra(PLUGIN_INFO, configuration);
	}
	
	public PluginInfo getConfiguration() {
		return getParcelableExtra(PLUGIN_INFO);
	}
}
