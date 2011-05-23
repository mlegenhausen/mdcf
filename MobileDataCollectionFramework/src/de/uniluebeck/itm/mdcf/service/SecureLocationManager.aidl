package de.uniluebeck.itm.mdcf.service;

import android.location.Location;

interface SecureLocationManager {
	Location getLastKnownLocation(String provider);
}