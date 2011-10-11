package de.uniluebeck.itm.mdcf.remote.locationtracker.shared;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

import de.uniluebeck.itm.mdcf.remote.locationtracker.server.ParticipantRepository;
import de.uniluebeck.itm.mdcf.remote.locationtracker.server.ParticipantServiceLocator;

@Service(value=ParticipantRepository.class, locator=ParticipantServiceLocator.class)
public interface ParticipantRequest extends RequestContext {

	Request<List<ParticipantProxy>> findAll();
}
