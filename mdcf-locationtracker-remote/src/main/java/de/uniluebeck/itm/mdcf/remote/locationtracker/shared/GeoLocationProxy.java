package de.uniluebeck.itm.mdcf.remote.locationtracker.shared;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

import de.uniluebeck.itm.mdcf.remote.locationtracker.server.domain.GeoLocation;

@ProxyFor(GeoLocation.class)
public interface GeoLocationProxy extends ValueProxy {
	
	Long getTimestamp();
	
	Double getLatitude();
	
	Double getLongitude();
}
