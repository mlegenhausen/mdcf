package de.uniluebeck.itm.mdcf.remote.locationtracker.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.ServletModule;

import de.uniluebeck.itm.mdcf.remote.locationtracker.server.model.Item;
import de.uniluebeck.itm.mdcf.remote.locationtracker.server.model.ItemDeserializer;
import de.uniluebeck.itm.mdcf.remote.locationtracker.server.model.Value;
import de.uniluebeck.itm.mdcf.remote.locationtracker.server.model.ValueDeserializer;

public class LocationTrackerServletModule extends ServletModule {

	@Override
	protected void configureServlets() {
		install(new JpaPersistModule("locationTrackerPersistenceUnit"));
		filter("/*").through(PersistFilter.class);
		serve("/receiver").with(DataReceiver.class);
		bind(TransferRequestProcessor.class).to(TransferRequestProcessorImpl.class);
	}
	
	@Provides
	@Singleton
	public Gson provideGson(ItemDeserializer itemDeserializer, ValueDeserializer valueDeserializer) {
		return new GsonBuilder()
		.registerTypeAdapter(Item.class, itemDeserializer)
		.registerTypeAdapter(Value.class, valueDeserializer)
		.create();
	}
}
