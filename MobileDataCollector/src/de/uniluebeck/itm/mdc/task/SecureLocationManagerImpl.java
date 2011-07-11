package de.uniluebeck.itm.mdc.task;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.RemoteException;
import android.util.Log;
import de.uniluebeck.itm.mdc.log.LogRecord;
import de.uniluebeck.itm.mdc.log.LogEntry.Confidentiality;
import de.uniluebeck.itm.mdcf.service.SecureLocationManager;

public class SecureLocationManagerImpl extends SecureLocationManager.Stub {

	private static final String TAG = "SecureLocationManager";
	
	private final LocationManager locationManager;
	
	private final LogRecord logRecord;
	
	public SecureLocationManagerImpl(LocationManager locationManager, LogRecord logRecord) {
		this.locationManager = locationManager;
		this.logRecord = logRecord;
	}
	
	@Override
	public String getBestProvider(Criteria criteria, boolean enabledOnly) throws RemoteException {
		String provider = locationManager.getBestProvider(criteria, enabledOnly);
		logRecord.add(Confidentiality.LOW, "%s was fetched as best provider", provider);
		return provider;
	}
	
	@Override
	public Location getLastKnownLocation(String provider) throws RemoteException {
		Log.i(TAG, "getLastKnownLoadtion(provider: " + provider + ")");
		Location location = locationManager.getLastKnownLocation(provider);
		logRecord.add(Confidentiality.HIGH, "Last known location fetched at %s, %s", location.getLatitude(), location.getLongitude());
		return location;
	}
}
