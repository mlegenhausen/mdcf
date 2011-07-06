package de.uniluebeck.itm.mdcf.remote.locationtracker.server;

import de.uniluebeck.itm.mdcf.remote.locationtracker.server.model.TransferRequest;

public interface TransferRequestProcessor {

	void process(TransferRequest request);
}
