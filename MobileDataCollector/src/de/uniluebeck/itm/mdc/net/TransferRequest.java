package de.uniluebeck.itm.mdc.net;

import de.uniluebeck.itm.mdcf.PluginInfo;
import de.uniluebeck.itm.mdcf.persistence.Node;

public class TransferRequest {

	private final String id;
	
	private final PluginInfo pluginInfo;
	
	private final Node workspace;
	
	public TransferRequest(String id, PluginInfo pluginInfo, Node workspace) {
		this.id = id;
		this.pluginInfo = pluginInfo;
		this.workspace = workspace;
	}
	
	public String getId() {
		return id;
	}
	
	public PluginInfo getPluginInfo() {
		return pluginInfo;
	}
	
	public Node getWorkspace() {
		return workspace;
	}
}
