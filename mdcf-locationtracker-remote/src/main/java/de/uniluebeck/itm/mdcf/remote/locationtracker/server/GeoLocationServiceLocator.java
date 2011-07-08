package de.uniluebeck.itm.mdcf.remote.locationtracker.server;

import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.ServiceLocator;


public class GeoLocationServiceLocator implements ServiceLocator {

	private final GeoLocationRepository repository;
	
	@Inject
	public GeoLocationServiceLocator(GeoLocationRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public Object getInstance(Class<?> clazz) {
		return repository;
	}

}
