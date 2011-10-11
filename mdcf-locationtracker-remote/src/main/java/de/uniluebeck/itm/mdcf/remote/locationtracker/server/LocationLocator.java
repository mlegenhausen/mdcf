package de.uniluebeck.itm.mdcf.remote.locationtracker.server;

import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Locator;

import de.uniluebeck.itm.mdcf.remote.locationtracker.server.domain.Location;

public class LocationLocator extends Locator<Location, Long> {

	private final LocationRepository repository;
	
	@Inject
	public LocationLocator(LocationRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public Location create(Class<? extends Location> clazz) {
		return new Location();
	}

	@Override
	public Location find(Class<? extends Location> clazz, Long id) {
		return repository.findById(id);
	}

	@Override
	public Class<Location> getDomainType() {
		return Location.class;
	}

	@Override
	public Long getId(Location domainObject) {
		return domainObject.getId();
	}

	@Override
	public Class<Long> getIdType() {
		return Long.class;
	}

	@Override
	public Object getVersion(Location domainObject) {
		return 0;
	}

}
