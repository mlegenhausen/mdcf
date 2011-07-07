package de.uniluebeck.itm.mdcf.remote.locationtracker.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.uniluebeck.itm.mdcf.remote.locationtracker.server.model.TransferRequest;

@Singleton
public class DataReceiver extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9091132877414579911L;
	
	private final TransferRequestDeserializer deserializer;
	
	private final TransferRequestProcessor processor;
	
	@Inject
	public DataReceiver(TransferRequestDeserializer deserializer, TransferRequestProcessor processor) {
		this.deserializer = deserializer;
		this.processor = processor;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		TransferRequest transferRequest = deserializer.fromJson(req.getReader());
		processor.process(transferRequest);	
	}
}
