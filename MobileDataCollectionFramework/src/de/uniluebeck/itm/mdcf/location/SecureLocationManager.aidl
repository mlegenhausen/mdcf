package de.uniluebeck.itm.mdcf.location;

import android.location.Location;

interface SecureLocationManager {
	Location getLastKnownLocation(String provider);
}