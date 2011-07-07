package de.uniluebeck.itm.mdcf.remote.locationtracker.client;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.web.bindery.requestfactory.shared.Receiver;

import de.uniluebeck.itm.mdcf.remote.locationtracker.shared.ParticipantProxy;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class LocationTracker implements EntryPoint {

	private final EventBus eventBus = new SimpleEventBus();
	
	private final LocationTrackerRequestFactory requestFactory = GWT.create(LocationTrackerRequestFactory.class);
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		requestFactory.initialize(eventBus);
		requestFactory.participantRequest().findAll().fire(new Receiver<List<ParticipantProxy>>() {
			@Override
			public void onSuccess(List<ParticipantProxy> result) {
				showParticipants(result);
			}
		});
	}
	

	private void showParticipants(List<ParticipantProxy> result) {
		Function<ParticipantProxy, String> function = new Function<ParticipantProxy, String>() {
			@Override
			public String apply(ParticipantProxy input) {
				return input.getId();
			}
		};
		List<String> participants = Lists.transform(result, function);
		String joined = Joiner.on(", ").join(participants);
		RootPanel.get().add(new Label(joined));
	}
}
