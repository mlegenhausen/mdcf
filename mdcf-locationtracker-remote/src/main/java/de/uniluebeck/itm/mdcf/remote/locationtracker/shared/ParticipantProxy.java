package de.uniluebeck.itm.mdcf.remote.locationtracker.shared;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

import de.uniluebeck.itm.mdcf.remote.locationtracker.server.ParticipantLocator;
import de.uniluebeck.itm.mdcf.remote.locationtracker.server.domain.Participant;

@ProxyFor(value=Participant.class, locator=ParticipantLocator.class)
public interface ParticipantProxy extends EntityProxy {
	
	String getId();
	
	List<GeoLocationProxy> getLocations();
}
