package de.uniluebeck.itm.mdcf.remote.locationtracker.server;

import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Locator;

import de.uniluebeck.itm.mdcf.remote.locationtracker.server.domain.GeoLocation;

public class GeoLocationLocator extends Locator<GeoLocation, Long> {

	private final GeoLocationRepository repository;
	
	@Inject
	public GeoLocationLocator(GeoLocationRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public GeoLocation create(Class<? extends GeoLocation> clazz) {
		return new GeoLocation();
	}

	@Override
	public GeoLocation find(Class<? extends GeoLocation> clazz, Long id) {
		return repository.findById(id);
	}

	@Override
	public Class<GeoLocation> getDomainType() {
		return GeoLocation.class;
	}

	@Override
	public Long getId(GeoLocation domainObject) {
		return domainObject.getId();
	}

	@Override
	public Class<Long> getIdType() {
		return Long.class;
	}

	@Override
	public Object getVersion(GeoLocation domainObject) {
		return 0;
	}

}
