package de.uniluebeck.itm.mdcf.remote.noisetracker.shared;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

import de.uniluebeck.itm.mdcf.remote.noisetracker.server.LocationLocator;
import de.uniluebeck.itm.mdcf.remote.noisetracker.server.domain.Location;

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
	
	Double getAmplitude();
}
