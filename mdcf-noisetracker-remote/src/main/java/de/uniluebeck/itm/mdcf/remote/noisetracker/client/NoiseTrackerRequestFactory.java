package de.uniluebeck.itm.mdcf.remote.noisetracker.client;

import com.google.web.bindery.requestfactory.shared.RequestFactory;

import de.uniluebeck.itm.mdcf.remote.noisetracker.shared.LocationRequest;
import de.uniluebeck.itm.mdcf.remote.noisetracker.shared.ParticipantRequest;

public interface NoiseTrackerRequestFactory extends RequestFactory {

	ParticipantRequest participantRequest();
	
	LocationRequest geoLocationRequest();
}
