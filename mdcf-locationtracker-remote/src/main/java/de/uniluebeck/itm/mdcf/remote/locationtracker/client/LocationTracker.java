package de.uniluebeck.itm.mdcf.remote.locationtracker.client;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapUIOptions;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.OverviewMapControl;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.Size;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.web.bindery.requestfactory.shared.Receiver;

import de.uniluebeck.itm.mdcf.remote.locationtracker.shared.GeoLocationProxy;
import de.uniluebeck.itm.mdcf.remote.locationtracker.shared.ParticipantProxy;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class LocationTracker implements EntryPoint, ResizeHandler, ChangeHandler, ClickHandler {

	private final EventBus eventBus = new SimpleEventBus();

	private final LocationTrackerRequestFactory requestFactory = GWT.create(LocationTrackerRequestFactory.class);

	private final Button refreshButton = new Button("Refresh");
	
	private final ListBox participantsListBox = new ListBox();
	
	private final List<ParticipantProxy> participants = newArrayList();
	
	private final List<Marker> markers = newArrayList(); 
	
	private final DockPanel dockPanel = new DockPanel();
	
	private MapWidget mapWidget;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		requestFactory.initialize(eventBus);
		participantsListBox.addChangeHandler(this);
		refreshButton.addClickHandler(this);

		Maps.loadMapsApi("", "2", false, new Runnable() {
			public void run() {
				initMap();
			}
		});
		
		HorizontalPanel toolBar = new HorizontalPanel();
		toolBar.setStylePrimaryName("toolBar");
		toolBar.add(new Label("Participant:"));
		toolBar.add(participantsListBox);
		toolBar.add(refreshButton);
		
		dockPanel.setStylePrimaryName("fullWidth fullHeight");
		dockPanel.add(toolBar, DockPanel.NORTH);
		
		RootPanel.get().add(dockPanel);
	}

	private void initMap() {
		Size size = Size.newInstance(400, 300);
		MapUIOptions options = MapUIOptions.newInstance(size);
        options.setScrollwheel(true);
        options.setMapTypeControl(true);
        options.setHybridMapType(true);
        options.setLargeMapControl3d(true);
        options.setPhysicalMapType(true);
        options.setSatelliteMapType(true);
        options.setScaleControl(true);
        options.setDoubleClick(true);
        options.setNormalMapType(true);
        options.setKeyboard(true);
		
		mapWidget = new MapWidget(LatLng.newInstance(0.0, 0.0), 3);
		mapWidget.setUI(options);
		mapWidget.setCurrentMapType(MapType.getHybridMap());
        mapWidget.addControl(new OverviewMapControl());
        mapWidget.setContinuousZoom(true);
		mapWidget.setSize("100%", "100%");
		dockPanel.add(mapWidget, DockPanel.CENTER);
		Window.addResizeHandler(this);
		loadParticipants();
	}
	
	private void loadParticipants() {
		requestFactory.participantRequest().findAll().fire(new Receiver<List<ParticipantProxy>>() {
			@Override
			public void onSuccess(List<ParticipantProxy> result) {
				participants.clear();
				participants.addAll(result);
				showParticipants(result);
				if (!participants.isEmpty()) {
					loadLocations(participants.get(0));
				}
			}
		});
	}
	
	private void showParticipants(List<ParticipantProxy> result) {
		participantsListBox.clear();
		for (ParticipantProxy participant : result) {
			participantsListBox.addItem(participant.getId());
		}
	}

	private void loadLocations(ParticipantProxy participant) {
		requestFactory.geoLocationRequest().findLocationsByParticipant(participant).fire(new Receiver<List<GeoLocationProxy>>() {
			@Override
			public void onSuccess(List<GeoLocationProxy> response) {
				showGeoLocations(response);
			}
		});
	}
	
	private void showGeoLocations(List<GeoLocationProxy> locations) {
		for (Marker marker : markers) {
			mapWidget.removeOverlay(marker);
		}
		markers.clear();
		if (locations != null) {
			for (GeoLocationProxy location : locations) {
				LatLng latLng = LatLng.newInstance(location.getLatitude(), location.getLongitude());
				Marker marker = new Marker(latLng);
				markers.add(marker);
				mapWidget.addOverlay(marker);
			}
		}
	}
	
	@Override
	public void onResize(ResizeEvent event) {
		mapWidget.setWidth(event.getWidth() + "px");
		mapWidget.setHeight(event.getHeight() - 36 + "px");
		mapWidget.checkResizeAndCenter();
	}
	
	@Override
	public void onChange(ChangeEvent event) {
		int index = participantsListBox.getSelectedIndex();
		loadLocations(participants.get(index));
	}
	
	@Override
	public void onClick(ClickEvent event) {
		loadParticipants();
	}
}
