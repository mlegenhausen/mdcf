package de.uniluebeck.itm.mdcf.remote.locationtracker.server;

import java.util.List;

import de.uniluebeck.itm.mdcf.remote.locationtracker.server.domain.GeoLocation;
import de.uniluebeck.itm.mdcf.remote.locationtracker.server.domain.Participant;

public class GeoLocationRepository extends AbstractRepository<GeoLocation> {

	public GeoLocationRepository() {
		super(GeoLocation.class);
	}
	
	public List<GeoLocation> findLocationsByParticipant(Participant participant) {
		return participant.getLocations();
	}
}
