package de.uniluebeck.itm.plugin;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.RemoteException;
import android.util.Log;
import de.uniluebeck.itm.mdcf.Plugin;
import de.uniluebeck.itm.mdcf.location.SecureLocationManager;

public class PluginImpl extends Plugin.Stub {

	public static final String LOG_TAG = "HelloWorldPluginImpl";
	
	private SecureLocationManager locationManager;
	
	public PluginImpl(Context context) {
		
	}
	
	@Override
	public void init(SecureLocationManager locationManager) throws RemoteException {
		this.locationManager = locationManager;
	}

	@Override
	public void start() throws RemoteException {
		Log.i(LOG_TAG, "Start plugin");
		Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		Log.i(LOG_TAG, "latitude: " + location.getLatitude() + " longitude: " + location.getLongitude());
	}

	@Override
	public void stop() throws RemoteException {
		
	}
}
