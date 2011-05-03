package de.uniluebeck.itm.mdcf;

import de.uniluebeck.itm.mdcf.location.SecureLocationManager;

interface Plugin {
	
	void init(SecureLocationManager locationManager);
	
	void start();
	
	void stop();
}