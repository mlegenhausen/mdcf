package de.uniluebeck.itm.mdcf;

import de.uniluebeck.itm.mdcf.PluginConfiguration;

oneway interface PluginServiceListener {

	void onRegistered(in PluginConfiguration category);
}