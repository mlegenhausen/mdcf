package de.uniluebeck.itm.mdcf.remote.locationtracker.server;

import static com.google.common.collect.Lists.newArrayList;

import java.util.Iterator;
import java.util.List;

import de.uniluebeck.itm.mdcf.remote.locationtracker.server.domain.GeoLocation;
import de.uniluebeck.itm.mdcf.remote.locationtracker.server.model.Node;
import de.uniluebeck.itm.mdcf.remote.locationtracker.server.model.TransferRequest;

public class TransferRequestProcessorImpl implements TransferRequestProcessor {

	public void process(TransferRequest request) {
		List<GeoLocation> locations = newArrayList();
		Node workspace = request.getWorkspace();
		Iterator<Node> nodes = workspace.getNodes();
		while (nodes.hasNext()) {
			Node node = nodes.next();
			GeoLocation location = new GeoLocation();
			location.setTimestamp(node.getTimestamp());
			location.setLatitude(node.getProperty("Latitude").getValue().getDouble());
			location.setLongitude(node.getProperty("Longitude").getValue().getDouble());
			System.out.println(location);
			locations.add(location);
		}
	}

}
