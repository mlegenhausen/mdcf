package de.uniluebeck.itm.mdcf.remote.locationtracker.server.model;


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
