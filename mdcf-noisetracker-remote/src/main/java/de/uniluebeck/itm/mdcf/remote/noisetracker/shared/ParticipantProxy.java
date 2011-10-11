package de.uniluebeck.itm.mdcf.remote.noisetracker.shared;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

import de.uniluebeck.itm.mdcf.remote.noisetracker.server.ParticipantLocator;
import de.uniluebeck.itm.mdcf.remote.noisetracker.server.domain.Participant;

@ProxyFor(value=Participant.class, locator=ParticipantLocator.class)
public interface ParticipantProxy extends EntityProxy {
	
	String getId();
}
