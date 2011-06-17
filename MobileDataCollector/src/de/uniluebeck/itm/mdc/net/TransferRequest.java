package de.uniluebeck.itm.mdc.net;

import de.uniluebeck.itm.mdcf.persistence.Node;

public class TransferRequest {

	private final String id;
	
	private final Node workspace;
	
	public TransferRequest(String id, Node workspace) {
		this.id = id;
		this.workspace = workspace;
	}
	
	public String getId() {
		return id;
	}
	
	public Node getWorkspace() {
		return workspace;
	}
}
