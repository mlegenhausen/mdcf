package de.uniluebeck.itm.mdc.service;

public interface TransferListener {

	void onCreated(TransferEvent event);
	
	void onRemoved(TransferEvent event);
}
