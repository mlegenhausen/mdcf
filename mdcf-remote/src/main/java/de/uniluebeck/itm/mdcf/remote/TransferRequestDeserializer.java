package de.uniluebeck.itm.mdcf.remote;

import java.io.Reader;

import com.google.inject.ImplementedBy;

import de.uniluebeck.itm.mdcf.remote.gson.GsonTransferRequestDeserializer;
import de.uniluebeck.itm.mdcf.remote.model.TransferRequest;

@ImplementedBy(GsonTransferRequestDeserializer.class)
public interface TransferRequestDeserializer {

	TransferRequest fromJson(Reader reader);
}
