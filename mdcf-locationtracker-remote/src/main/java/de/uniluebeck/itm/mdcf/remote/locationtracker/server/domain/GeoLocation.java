package de.uniluebeck.itm.mdcf.remote.locationtracker.server.domain;

import com.google.common.base.Objects;

public class GeoLocation {

	private Double latitude;
	
	private Double longitude;
	
	private Long timestamp;

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
