package de.uniluebeck.itm.mdcf.remote.locationtracker.server;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import de.uniluebeck.itm.mdcf.remote.locationtracker.server.domain.GeoLocation;
import de.uniluebeck.itm.mdcf.remote.locationtracker.server.domain.Participant;
import de.uniluebeck.itm.mdcf.remote.locationtracker.server.model.Node;
import de.uniluebeck.itm.mdcf.remote.locationtracker.server.model.TransferRequest;

public class TransferRequestProcessorImpl implements TransferRequestProcessor {
	
	private static final Logger LOG = Logger.getLogger(TransferRequestProcessorImpl.class);
	
	private final ParticipantRepository repository;
	
	@Inject
	public TransferRequestProcessorImpl(ParticipantRepository repository) {
		this.repository = repository;
	}
	
	public void process(TransferRequest request) {
		String id = request.getId();
		LOG.info("Searching repository...");
		Participant participant = null; //repository.findById(id);
		LOG.info("Finding finished.");
		if (participant == null) {
			participant = new Participant();
			participant.setParticipantId(id);
		}
		
		Node workspace = request.getWorkspace();
		Iterator<Node> nodes = workspace.getNodes();
		List<GeoLocation> locations = participant.getLocations();
		while (nodes.hasNext()) {
			Node node = nodes.next();
			GeoLocation location = new GeoLocation();
			location.setTimestamp(node.getTimestamp());
			location.setLatitude(node.getProperty("Latitude").getValue().getDouble());
			location.setLongitude(node.getProperty("Longitude").getValue().getDouble());
			locations.add(location);
		}
		LOG.info("Storing participant");
		repository.persist(participant);
	}

}
