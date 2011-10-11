package de.uniluebeck.itm.mdcf.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import de.uniluebeck.itm.mdcf.remote.gson.ItemDeserializer;
import de.uniluebeck.itm.mdcf.remote.gson.ValueDeserializer;
import de.uniluebeck.itm.mdcf.remote.model.Item;
import de.uniluebeck.itm.mdcf.remote.model.Value;

public class MdcfRemoteModule extends AbstractModule {

	private final Class<? extends TransferRequestProcessor> processor;
	
	public MdcfRemoteModule(Class<? extends TransferRequestProcessor> processor) {
		this.processor = processor;
	}
	
	@Override
	protected void configure() {
		bind(TransferRequestProcessor.class).to(processor);
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
