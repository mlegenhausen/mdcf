package de.uniluebeck.itm.plugin;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.RemoteException;
import android.util.Log;
import de.uniluebeck.itm.mdcf.Plugin;
import de.uniluebeck.itm.mdcf.location.SecureLocationManager;
import de.uniluebeck.itm.mdcf.persistence.Node;
import de.uniluebeck.itm.mdcf.persistence.PersistenceManager;

public class PluginImpl extends Plugin.Stub {

	public static final String LOG_TAG = "HelloWorldPluginImpl";
	
	private SecureLocationManager locationManager;
	
	private PersistenceManager persistenceManager;
	
	public PluginImpl(Context context) {
		
	}
	
	@Override
	public void init(SecureLocationManager locationManager, PersistenceManager persistenceManager) throws RemoteException {
		this.locationManager = locationManager;
		this.persistenceManager = persistenceManager;
	}

	@Override
	public void start() throws RemoteException {
		Log.i(LOG_TAG, "Start plugin");
		//Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		//Log.i(LOG_TAG, "latitude: " + location.getLatitude() + " longitude: " + location.getLongitude());
		storeLocation(null);
	}

	@Override
	public void stop() throws RemoteException {
		
	}
	
	private void storeLocation(Location location) throws RemoteException {
		Node workspace = persistenceManager.getWorkspace();
		Log.i(LOG_TAG, "Workspace received");
		Node node = new Node();
		node.setProperty("latitude", 150.0);
		node.setProperty("longitude", 42.0);
		workspace.addNode(node);
		Log.i(LOG_TAG, "Saving workspace...");
		persistenceManager.save(workspace);
		Log.i(LOG_TAG, "Workspace saved");
	}
}
