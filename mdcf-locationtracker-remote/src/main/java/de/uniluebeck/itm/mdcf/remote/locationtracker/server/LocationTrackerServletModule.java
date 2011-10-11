package de.uniluebeck.itm.mdcf.remote.locationtracker.server;

import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.ServletModule;
import com.trycatchsoft.gwt.requestfactory.InjectedRequestFactoryModule;
import com.trycatchsoft.gwt.requestfactory.InjectedRequestFactoryServlet;

import de.uniluebeck.itm.mdcf.remote.MdcfRemoteModule;
import de.uniluebeck.itm.mdcf.remote.TransferRequestReceiver;

public class LocationTrackerServletModule extends ServletModule {

	@Override
	protected void configureServlets() {
		install(new JpaPersistModule("locationTrackerPersistenceUnit"));
		install(new InjectedRequestFactoryModule());
		install(new MdcfRemoteModule(TransferRequestProcessorImpl.class));
		
		filter("/*").through(PersistFilter.class);
		
		serve("/receiver").with(TransferRequestReceiver.class);
		serve("/gwtRequest").with(InjectedRequestFactoryServlet.class);
	}
}
