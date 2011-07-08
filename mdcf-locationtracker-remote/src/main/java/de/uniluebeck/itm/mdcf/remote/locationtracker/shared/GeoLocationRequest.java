package de.uniluebeck.itm.mdcf.remote.locationtracker.shared;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

import de.uniluebeck.itm.mdcf.remote.locationtracker.server.GeoLocationRepository;
import de.uniluebeck.itm.mdcf.remote.locationtracker.server.GeoLocationServiceLocator;

@Service(value=GeoLocationRepository.class, locator=GeoLocationServiceLocator.class)
public interface GeoLocationRequest extends RequestContext {

	Request<List<GeoLocationProxy>> findLocationsByParticipant(ParticipantProxy participant);
}
