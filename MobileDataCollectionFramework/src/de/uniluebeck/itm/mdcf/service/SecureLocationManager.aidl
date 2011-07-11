package de.uniluebeck.itm.mdcf.service;

import android.location.Criteria;
import android.location.Location;

interface SecureLocationManager {
	String getBestProvider(in Criteria criteria, boolean enabledOnly);

	Location getLastKnownLocation(String provider);
}