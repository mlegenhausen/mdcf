package de.uniluebeck.itm.mdc.task;

import android.location.Location;
import android.location.LocationManager;
import android.os.RemoteException;
import android.util.Log;
import de.uniluebeck.itm.mdcf.location.SecureLocationManager.Stub;

public class SecureLocationManagerImpl extends Stub {

	private static final String TAG = "SecureLocationManager";
	
	private final LocationManager locationManager;
	
	public SecureLocationManagerImpl(LocationManager locationManager) {
		this.locationManager = locationManager;
	}
	
	@Override
	public Location getLastKnownLocation(String provider) throws RemoteException {
		Log.i(TAG, "getLastKnownLoadtion(provider: " + provider + ")");
		Location location = locationManager.getLastKnownLocation(provider);
		return location;
	}

}
