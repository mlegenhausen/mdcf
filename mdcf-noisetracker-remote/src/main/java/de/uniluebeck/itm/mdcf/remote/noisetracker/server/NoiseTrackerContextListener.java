package de.uniluebeck.itm.mdcf.remote.noisetracker.server;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class NoiseTrackerContextListener extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new NoiseTrackerServletModule());
	}

}
