package de.uniluebeck.itm.mdcf.remote.locationtracker.server;

import java.io.Reader;

import com.google.inject.ImplementedBy;

import de.uniluebeck.itm.mdcf.remote.locationtracker.server.model.TransferRequest;

@ImplementedBy(GsonTransferRequestDeserializer.class)
public interface TransferRequestDeserializer {

	TransferRequest fromJson(Reader reader);
}
