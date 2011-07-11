package de.uniluebeck.itm.mdcf.remote.locationtracker.shared;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

import de.uniluebeck.itm.mdcf.remote.locationtracker.server.LocationLocator;
import de.uniluebeck.itm.mdcf.remote.locationtracker.server.domain.Location;

@ProxyFor(value=Location.class, locator=LocationLocator.class)
public interface LocationProxy extends EntityProxy {
	
	Long getTimestamp();
	
	Double getLatitude();
	
	Double getLongitude();
	
	Double getAltitude();
	
	Float getAccuracy();
	
	Float getBearing();
	
	String getProvider();
	
	Float getSpeed();
}
