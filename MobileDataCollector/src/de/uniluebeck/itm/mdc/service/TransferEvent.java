package de.uniluebeck.itm.mdc.service;

import java.util.EventObject;

public class TransferEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6206384773030862156L;
	
	private final Transfer transfer;
	
	public TransferEvent(Object source, Transfer transfer) {
		super(source);
		this.transfer = transfer;
	}
	
	public Transfer getTransfer() {
		return transfer;
	}
}
