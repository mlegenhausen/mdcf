package de.uniluebeck.itm.mdcf.remote.locationtracker.shared;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

import de.uniluebeck.itm.mdcf.remote.locationtracker.server.LocationRepository;
import de.uniluebeck.itm.mdcf.remote.locationtracker.server.GeoLocationServiceLocator;

@Service(value=LocationRepository.class, locator=GeoLocationServiceLocator.class)
public interface LocationRequest extends RequestContext {

	Request<List<LocationProxy>> findLocationsByParticipant(ParticipantProxy participant);
}
