package de.uniluebeck.itm.mdcf.remote.locationtracker.shared;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

import de.uniluebeck.itm.mdcf.remote.locationtracker.server.GeoLocationLocator;
import de.uniluebeck.itm.mdcf.remote.locationtracker.server.domain.GeoLocation;

@ProxyFor(value=GeoLocation.class, locator=GeoLocationLocator.class)
public interface GeoLocationProxy extends EntityProxy {
	
	Long getTimestamp();
	
	Double getLatitude();
	
	Double getLongitude();
	
	Double getAltitude();
	
	Float getAccuracy();
	
	Float getBearing();
	
	String getProvider();
	
	Float getSpeed();
}
