package de.uniluebeck.itm.mdcf.service;

import android.app.PendingIntent;
import android.location.Criteria;
import android.location.Location;

interface SecureLocationManager {
	void addProximityAlert(double latitude, double longitude, float radius, long expiration, in PendingIntent intent);

	void addTestProvider(String name, boolean requiresNetwork, boolean requiresSatellite, boolean requiresCell, boolean hasMonetaryCost, boolean supportsAltitude, boolean supportsSpeed, boolean supportsBearing, int powerRequirement, int accuracy);

	void clearTestProviderEnabled(String provider);

	void clearTestProviderLocation(String provider);
	
	void clearTestProviderStatus(String provider);
	
	List<String> getAllProviders();

	String getBestProvider(in Criteria criteria, boolean enabledOnly);

	Location getLastKnownLocation(String provider);
	
	List<String> getProviders(in Criteria criteria, boolean enabledOnly);
	
	boolean	isProviderEnabled(String provider);
	
	void removeProximityAlert(in PendingIntent intent);
	
	void removeTestProvider(String provider);
	
	void removeUpdates(in PendingIntent intent);
	
	void requestLocationUpdates(long minTime, float minDistance, in Criteria criteria, in PendingIntent intent);
	
	boolean	sendExtraCommand(String provider, String command, in Bundle extras);
	
	void setTestProviderEnabled(String provider, boolean enabled);
	
	void setTestProviderLocation(String provider, in Location loc);
	
	void setTestProviderStatus(String provider, int status, in Bundle extras, long updateTime);
}