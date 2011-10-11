package de.uniluebeck.itm.mdcf.remote.gson;

import java.io.Reader;

import com.google.gson.Gson;
import com.google.inject.Inject;

import de.uniluebeck.itm.mdcf.remote.TransferRequestDeserializer;
import de.uniluebeck.itm.mdcf.remote.model.TransferRequest;

public class GsonTransferRequestDeserializer implements TransferRequestDeserializer {

	private final Gson gson;
	
	@Inject
	public GsonTransferRequestDeserializer(Gson gson) {
		this.gson = gson;
	}
	
	public TransferRequest fromJson(Reader reader) {
		return gson.fromJson(reader, TransferRequest.class);
	}
}
