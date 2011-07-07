package de.uniluebeck.itm.mdcf.remote.locationtracker.server.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.google.common.base.Objects;

@Entity
public class GeoLocation {
	
	@Id
	@GeneratedValue
	private Long id;

	private Double latitude;
	
	private Double longitude;
	
	private Long timestamp;

	public GeoLocation() {
		
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	public Long getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("timestamp", timestamp)
				.add("latitude", latitude)
				.add("longitude", longitude)
				.toString();
	}
}
