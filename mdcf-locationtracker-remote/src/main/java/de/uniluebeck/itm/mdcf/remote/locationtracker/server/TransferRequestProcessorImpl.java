package de.uniluebeck.itm.mdcf.remote.locationtracker.server;

import java.util.Iterator;
import java.util.List;

import com.google.inject.Inject;

import de.uniluebeck.itm.mdcf.remote.locationtracker.server.domain.GeoLocation;
import de.uniluebeck.itm.mdcf.remote.locationtracker.server.domain.Participant;
import de.uniluebeck.itm.mdcf.remote.locationtracker.server.model.Node;
import de.uniluebeck.itm.mdcf.remote.locationtracker.server.model.TransferRequest;

public class TransferRequestProcessorImpl implements TransferRequestProcessor {
	
	private final ParticipantRepository repository;
	
	@Inject
	public TransferRequestProcessorImpl(ParticipantRepository repository) {
		this.repository = repository;
	}
	
	public void process(TransferRequest request) {
		String id = request.getId();
		Participant participant = repository.findById(id);
		if (participant == null) {
			participant = new Participant();
			participant.setId(id);
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
			location.setAltitude(node.getProperty("Altitude").getValue().getDouble());
			location.setBearing((float) node.getProperty("Bearing").getValue().getDouble());
			location.setAccuracy((float) node.getProperty("Accuracy").getValue().getDouble());
			location.setSpeed((float) node.getProperty("Speed").getValue().getDouble());
			location.setProvider(node.getProperty("Provider").getValue().getString());
			locations.add(location);
		}
		repository.persist(participant);
	}
}
