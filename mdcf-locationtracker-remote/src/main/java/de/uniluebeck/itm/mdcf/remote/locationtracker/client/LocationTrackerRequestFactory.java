package de.uniluebeck.itm.mdcf.remote.locationtracker.client;

import com.google.web.bindery.requestfactory.shared.RequestFactory;

import de.uniluebeck.itm.mdcf.remote.locationtracker.shared.ParticipantRequest;

public interface LocationTrackerRequestFactory extends RequestFactory {

	ParticipantRequest participantRequest();
}
