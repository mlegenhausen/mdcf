package de.uniluebeck.itm.mdcf.remote.noisetracker.server;

import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.ServiceLocator;

public class ParticipantServiceLocator implements ServiceLocator {

	private final ParticipantRepository repository;
	
	@Inject
	public ParticipantServiceLocator(ParticipantRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public Object getInstance(Class<?> clazz) {
		return repository;
	}

}
