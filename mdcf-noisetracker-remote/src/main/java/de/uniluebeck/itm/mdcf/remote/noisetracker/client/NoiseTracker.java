package de.uniluebeck.itm.mdcf.remote.noisetracker.client;

import static com.google.common.collect.Lists.newArrayList;

import java.util.Date;
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
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.maps.client.InfoWindow;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapUIOptions;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.OverviewMapControl;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.LatLngBounds;
import com.google.gwt.maps.client.geom.Size;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.Overlay;
import com.google.gwt.maps.client.overlay.Polygon;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.web.bindery.requestfactory.shared.Receiver;

import de.uniluebeck.itm.mdcf.remote.noisetracker.shared.LocationProxy;
import de.uniluebeck.itm.mdcf.remote.noisetracker.shared.ParticipantProxy;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class NoiseTracker implements EntryPoint, ResizeHandler {

	private static final DateTimeFormat DATE_TIME_FORMAT = DateTimeFormat.getFormat("dd.MM.yyyy HH:mm:ss");
	
	private final Timer playbackTimer = new Timer() {
		@Override
		public void run() {
			showNextLocation();
			if (locationsListBox.getSelectedIndex() + 1 >= locations.size()) {
				stop();
			}
		}
	};

	private final EventBus eventBus = new SimpleEventBus();

	private final NoiseTrackerRequestFactory requestFactory = GWT.create(NoiseTrackerRequestFactory.class);

	private final Button refreshButton = new Button("Refresh");

	private final ListBox participantsListBox = new ListBox();
	
	private final ListBox locationsListBox = new ListBox();

	private final List<ParticipantProxy> participants = newArrayList();
	
	private final List<LocationProxy> locations = newArrayList();
	
	private final List<Marker> markers = newArrayList();

	private final List<Overlay> overlays = newArrayList();

	private final DockPanel dockPanel = new DockPanel();
	
	private final Button previousButton = new Button("Previous");
	
	private final Button nextButton = new Button("Next");
	
	private final Button playButton = new Button("Play");
	
	private final Button stopButton = new Button("Stop");

	private MapWidget mapWidget;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		Maps.loadMapsApi("", "2", false, new Runnable() {
			public void run() {
				initMap();
			}
		});
		
		requestFactory.initialize(eventBus);
		participantsListBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				int index = participantsListBox.getSelectedIndex();
				loadLocations(participants.get(index));
			}
		});
		locationsListBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				int index = locationsListBox.getSelectedIndex();
				showLocationIndex(index);
			}
		});
		refreshButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				loadParticipants();
			}
		});
		previousButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showPreviousLocation();
			}
		});
		nextButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showNextLocation();
			}
		});
		playButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				play();
			}
		});
		stopButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				stop();
			}
		});

		HorizontalPanel toolBar = new HorizontalPanel();
		toolBar.setStylePrimaryName("toolBar");
		toolBar.add(new Label("Participant:"));
		toolBar.add(participantsListBox);
		toolBar.add(refreshButton);
		toolBar.add(locationsListBox);
		toolBar.add(previousButton);
		toolBar.add(playButton);
		toolBar.add(stopButton);
		toolBar.add(nextButton);

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
		mapWidget.setZoomLevel(16);
		dockPanel.add(mapWidget, DockPanel.CENTER);
		Window.addResizeHandler(this);
		loadParticipants();
	}

	private void loadParticipants() {
		clearOverlays();
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
		requestFactory.geoLocationRequest().findLocationsByParticipant(participant)
				.fire(new Receiver<List<LocationProxy>>() {
					@Override
					public void onSuccess(List<LocationProxy> result) {
						locations.clear();
						locations.addAll(result);
						showLocations(result);
						if (!locations.isEmpty()) {
							LocationProxy location = locations.get(0);
							showMarker(location);
						}
					}
				});
	}

	private void clearOverlays() {
		mapWidget.clearOverlays();
		markers.clear();
		overlays.clear();
	}

	private void showLocations(List<LocationProxy> locations) {
		locationsListBox.clear();
		if (locations != null) {
			for (final LocationProxy location : locations) {
				locationsListBox.addItem(DATE_TIME_FORMAT.format(new Date(location.getTimestamp())));
			}
		}
	}

	private void showMarker(final LocationProxy location) {
		clearOverlays();
		LatLng center = LatLng.newInstance(location.getLatitude(), location.getLongitude());
		final Marker marker = new Marker(center);
		marker.addMarkerClickHandler(new MarkerClickHandler() {
			@Override
			public void onClick(MarkerClickEvent event) {
				boolean visible = mapWidget.getInfoWindow().isVisible();
				if (visible) {
					mapWidget.closeInfoWindow();
				} else {
					showLocationInformation(marker, location);
				}
			}
		});
		markers.add(marker);
		mapWidget.addOverlay(marker);
		mapWidget.setCenter(center, 16);
		drawCircleFromRadius(center, location.getAccuracy(), 100);
		showLocationInformation(marker, location);
	}

	private void showLocationInformation(Marker marker, final LocationProxy location) {
		VerticalPanel panel = new VerticalPanel();
		panel.add(new Label("Location Information"));
		panel.add(new Label("Date: " + DATE_TIME_FORMAT.format(new Date(location.getTimestamp()))));
		panel.add(new Label("Latitude: " + location.getLatitude()));
		panel.add(new Label("Longitude: " + location.getLongitude()));
		panel.add(new Label("Altitude: " + location.getAltitude()));
		panel.add(new Label("Bearing: " + location.getBearing()));
		panel.add(new Label("Accuracy: " + location.getAccuracy()));
		panel.add(new Label("Speed: " + location.getSpeed()));
		panel.add(new Label("Provider: " + location.getProvider()));
		panel.add(new Label("Amplitude: " + location.getAmplitude()));

		InfoWindowContent content = new InfoWindowContent(panel);

		InfoWindow info = mapWidget.getInfoWindow();
		info.open(marker, content);
	}

	public void drawCircleFromRadius(LatLng center, double radius, int nbOfPoints) {

		LatLngBounds bounds = LatLngBounds.newInstance();
		LatLng[] circlePoints = new LatLng[nbOfPoints];

		double EARTH_RADIUS = 6371000;
		double d = radius / EARTH_RADIUS;
		double lat1 = Math.toRadians(center.getLatitude());
		double lng1 = Math.toRadians(center.getLongitude());

		double a = 0.0;
		double step = 360.0 / (double) nbOfPoints;
		for (int i = 0; i < nbOfPoints; i++) {
			double tc = Math.toRadians(a);
			double lat2 = Math.asin(Math.sin(lat1) * Math.cos(d) + Math.cos(lat1) * Math.sin(d) * Math.cos(tc));
			double lng2 = lng1
					+ Math.atan2(Math.sin(tc) * Math.sin(d) * Math.cos(lat1),
							Math.cos(d) - Math.sin(lat1) * Math.sin(lat2));
			LatLng point = LatLng.newInstance(Math.toDegrees(lat2), Math.toDegrees(lng2));
			circlePoints[i] = point;
			bounds.extend(point);
			a += step;
		}

		Polygon circle = new Polygon(circlePoints, "white", 0, 0, "green", 0.5);
		overlays.add(circle);
		mapWidget.addOverlay(circle);
	}
	
	private void showLocationIndex(int index) {
		locationsListBox.setSelectedIndex(index);
		LocationProxy location = locations.get(index);
		showMarker(location);
	}
	
	private void showNextLocation() {
		int index = locationsListBox.getSelectedIndex() + 1;
		if (index < locations.size()) {
			showLocationIndex(index);
		}
	}
	
	private void showPreviousLocation() {
		int index = locationsListBox.getSelectedIndex() - 1;
		if (index >= 0) {
			showLocationIndex(index);
		}
	}
	
	private void play() {
		playbackTimer.scheduleRepeating(1000);
	}
	
	private void stop() {
		playbackTimer.cancel();
	}

	@Override
	public void onResize(ResizeEvent event) {
		mapWidget.setWidth(event.getWidth() + "px");
		mapWidget.setHeight(event.getHeight() - 36 + "px");
		mapWidget.checkResizeAndCenter();
	}
}
