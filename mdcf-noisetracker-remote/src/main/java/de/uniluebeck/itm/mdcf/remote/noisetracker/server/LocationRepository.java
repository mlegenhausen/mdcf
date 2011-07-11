package de.uniluebeck.itm.mdcf.remote.noisetracker.server;

import java.util.List;

import de.uniluebeck.itm.mdcf.remote.noisetracker.server.domain.Location;
import de.uniluebeck.itm.mdcf.remote.noisetracker.server.domain.Participant;

public class LocationRepository extends AbstractRepository<Location> {

	public LocationRepository() {
		super(Location.class);
	}
	
	public List<Location> findLocationsByParticipant(Participant participant) {
		return participant.getLocations();
	}
}
