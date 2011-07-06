package de.uniluebeck.itm.mdcf.remote.locationtracker.server.domain;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

public class Entry {

	private String participantId;
	
	private List<GeoLocation> locations = newArrayList();

	public String getParticipantId() {
		return participantId;
	}

	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}

	public List<GeoLocation> getLocations() {
		return locations;
	}

	public void setLocations(List<GeoLocation> locations) {
		this.locations = locations;
	}
}
