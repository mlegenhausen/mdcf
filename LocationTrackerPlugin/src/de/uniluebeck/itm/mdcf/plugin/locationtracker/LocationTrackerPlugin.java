package de.uniluebeck.itm.mdcf.plugin.locationtracker;

import android.location.Criteria;
import android.location.Location;
import android.os.RemoteException;
import de.uniluebeck.itm.mdcf.AbstractPlugin;
import de.uniluebeck.itm.mdcf.persistence.Node;

public class LocationTrackerPlugin extends AbstractPlugin {

	@Override
	protected void onRun() throws Exception {
		String provider = getLocationManager().getBestProvider(new Criteria(), false);
		Location location = getLocationManager().getLastKnownLocation(provider);
		storeLocation(location);
	}
	
	private void storeLocation(Location location) throws RemoteException {
		Node workspace = getPersistenceManager().getWorkspace();
		Node node = new Node();
		node.setProperty("Latitude", location.getLatitude());
		node.setProperty("Longitude", location.getLongitude());
		node.setProperty("Altitude", location.getAltitude());
		node.setProperty("Bearing", location.getBearing());
		node.setProperty("Accuracy", location.getAccuracy());
		node.setProperty("Speed", location.getSpeed());
		node.setProperty("Provider", location.getProvider());
		workspace.addNode(node);
		getPersistenceManager().save(workspace);
	}
}
