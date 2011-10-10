package de.uniluebeck.itm.mdc.task;

import java.util.List;

import android.app.PendingIntent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
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

	@Override
	public void addProximityAlert(double latitude, double longitude, float radius, long expiration, PendingIntent intent) throws RemoteException {
		locationManager.addProximityAlert(latitude, longitude, radius, expiration, intent);
		logRecord.add(Confidentiality.HIGH, "Proximity alert for location %s, %s with radius %s added.", latitude, longitude, radius);
	}

	@Override
	public void addTestProvider(String name, boolean requiresNetwork, boolean requiresSatellite, boolean requiresCell,
			boolean hasMonetaryCost, boolean supportsAltitude, boolean supportsSpeed, boolean supportsBearing,
			int powerRequirement, int accuracy) throws RemoteException {
		locationManager.addTestProvider(name, requiresNetwork, requiresSatellite, requiresCell, hasMonetaryCost, supportsAltitude, supportsSpeed, supportsBearing, powerRequirement, accuracy);
		logRecord.add(Confidentiality.LOW, "Test Provider %s added.", name);
	}

	@Override
	public void clearTestProviderEnabled(String provider) throws RemoteException {
		locationManager.clearTestProviderEnabled(provider);
		logRecord.add(Confidentiality.LOW, "Test Provider %s is disabled.", provider);
	}

	@Override
	public void clearTestProviderLocation(String provider) throws RemoteException {
		locationManager.clearTestProviderLocation(provider);
		logRecord.add(Confidentiality.LOW, "Test Provider %s location cleared", provider);
	}

	@Override
	public void clearTestProviderStatus(String provider) throws RemoteException {
		locationManager.clearTestProviderStatus(provider);
		logRecord.add(Confidentiality.LOW, "Test Provider %s status cleared", provider);
	}

	@Override
	public List<String> getAllProviders() throws RemoteException {
		List<String> providers = locationManager.getAllProviders();
		logRecord.add(Confidentiality.MEDIUM, "All available providers fetched");
		return providers;
	}

	@Override
	public List<String> getProviders(Criteria criteria, boolean enabledOnly) throws RemoteException {
		List<String> providers = locationManager.getProviders(criteria, enabledOnly);
		logRecord.add(Confidentiality.MEDIUM, "Getting provider by critera");
		return providers;
	}

	@Override
	public boolean isProviderEnabled(String provider) throws RemoteException {
		boolean enabled = locationManager.isProviderEnabled(provider);
		logRecord.add(Confidentiality.MEDIUM, enabled ? "%s is enabled" : "%s is disabled", provider);
		return enabled;
	}

	@Override
	public void removeProximityAlert(PendingIntent intent) throws RemoteException {
		locationManager.removeProximityAlert(intent);
		logRecord.add(Confidentiality.MEDIUM, "Proximity altert removed");
	}

	@Override
	public void removeTestProvider(String provider) throws RemoteException {
		locationManager.removeTestProvider(provider);
		logRecord.add(Confidentiality.LOW, "Test Provider %s removed", provider);
	}

	@Override
	public void removeUpdates(PendingIntent intent) throws RemoteException {
		locationManager.removeUpdates(intent);
		logRecord.add(Confidentiality.MEDIUM, "Updates for pending intent removed");
	}

	@Override
	public void requestLocationUpdates(long minTime, float minDistance, Criteria criteria, PendingIntent intent)
			throws RemoteException {
		locationManager.requestLocationUpdates(minTime, minDistance, criteria, intent);
		logRecord.add(Confidentiality.MEDIUM, "Location updates requested");
	}

	@Override
	public boolean sendExtraCommand(String provider, String command, Bundle extras) throws RemoteException {
		boolean result = locationManager.sendExtraCommand(provider, command, extras);
		logRecord.add(Confidentiality.MEDIUM, "%s command sent to provider %s", command, provider);
		return result;
	}

	@Override
	public void setTestProviderEnabled(String provider, boolean enabled) throws RemoteException {
		locationManager.setTestProviderEnabled(provider, enabled);
		logRecord.add(Confidentiality.LOW, enabled ? "Test Provider %s enabled" : "Test Provider %s disabled", provider);
	}

	@Override
	public void setTestProviderLocation(String provider, Location loc) throws RemoteException {
		locationManager.setTestProviderLocation(provider, loc);
		logRecord.add(Confidentiality.LOW, "Location %s, %s set for test provider %s", loc.getLatitude(), loc.getLatitude(), provider);
	}

	@Override
	public void setTestProviderStatus(String provider, int status, Bundle extras, long updateTime)
			throws RemoteException {
		locationManager.setTestProviderStatus(provider, status, extras, updateTime);
		logRecord.add(Confidentiality.LOW, "Status set for test provider %s", provider);
	}
}
