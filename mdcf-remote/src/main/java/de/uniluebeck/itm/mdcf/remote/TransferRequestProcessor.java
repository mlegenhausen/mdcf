package de.uniluebeck.itm.mdcf.remote;

import de.uniluebeck.itm.mdcf.remote.model.TransferRequest;

public interface TransferRequestProcessor {

	void process(TransferRequest request);
}
