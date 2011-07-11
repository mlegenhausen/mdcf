package de.uniluebeck.itm.mdcf.remote.locationtracker.server;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class LocationTrackerContextListener extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new LocationTrackerServletModule());
	}

}
