package de.uniluebeck.itm.mdcf;

import android.content.Intent;
import android.os.Parcelable;

public class PluginIntent extends Intent {
	
	public static final String PLUGIN_FIND = "de.uniluebeck.itm.mdcf.PLUGIN_FIND";
	
	public static final String PLUGIN_REGISTER = "de.uniluebeck.itm.mdcf.PLUGIN_REGISTER";
	
	public static final String PLUGIN_INFO = "de.uniluebeck.itm.mdcf.PLUGIN_INFO";

	public PluginIntent(PluginInfo info) {
		super(PLUGIN_REGISTER);
		putExtra(PLUGIN_INFO, (Parcelable) info);
	}
	
	public PluginInfo getConfiguration() {
		return getParcelableExtra(PLUGIN_INFO);
	}
}
