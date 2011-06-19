package de.uniluebeck.itm.plugin;

import android.location.Location;
import android.location.LocationManager;
import android.os.RemoteException;
import android.util.Log;
import de.uniluebeck.itm.mdcf.AbstractPlugin;
import de.uniluebeck.itm.mdcf.persistence.Node;

public class HelloWorldPlugin extends AbstractPlugin {

	public static final String LOG_TAG = "HelloWorldPluginImpl";

	@Override
	protected void onRun() throws Exception {
		Log.i(LOG_TAG, "Start plugin");
		Location location = getLocationManager().getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		Log.i(LOG_TAG, "latitude: " + location.getLatitude() + " longitude: " + location.getLongitude());
		storeLocation(location);
	}
	
	private void storeLocation(Location location) throws RemoteException {
		Node workspace = getPersistenceManager().getWorkspace();
		workspace.setProperty("Version", "1.0");
		Log.i(LOG_TAG, "Workspace received");
		Node node = new Node();
		node.setProperty("Latitude", location.getLatitude());
		node.setProperty("Longitude", location.getLongitude());
		workspace.addNode(node);
		Log.i(LOG_TAG, "Saving workspace...");
		getPersistenceManager().save(workspace);
		Log.i(LOG_TAG, "Workspace saved");
	}
}
