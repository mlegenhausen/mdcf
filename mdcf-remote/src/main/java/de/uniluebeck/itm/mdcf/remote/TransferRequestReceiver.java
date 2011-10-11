package de.uniluebeck.itm.mdcf.remote;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.uniluebeck.itm.mdcf.remote.model.TransferRequest;

@Singleton
public class TransferRequestReceiver extends HttpServlet {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 9091132877414579911L;
	
	private final TransferRequestDeserializer deserializer;
	
	private final Provider<TransferRequestProcessor> processorProvider;
	
	@Inject
	public TransferRequestReceiver(TransferRequestDeserializer deserializer, Provider<TransferRequestProcessor> processorProvider) {
		this.deserializer = deserializer;
		this.processorProvider = processorProvider;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		TransferRequest transferRequest = deserializer.fromJson(req.getReader());
		processorProvider.get().process(transferRequest);	
	}
}
