package de.uniluebeck.itm.mdcf.remote.locationtracker.server.domain;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Participant {

	@Id
	@GeneratedValue
	private Long id;
	
	private String participantId;
	
	@OneToMany(cascade=CascadeType.ALL)
	private List<GeoLocation> locations = newArrayList();
	
	public Participant() {
		
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
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
