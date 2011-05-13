package de.uniluebeck.itm.mdcf;

import de.uniluebeck.itm.mdcf.location.SecureLocationManager;
import de.uniluebeck.itm.mdcf.persistence.PersistenceManager;

interface Plugin {
	
	void init(SecureLocationManager locationManager, PersistenceManager manager);
	
	void start();
	
	void stop();
}