package de.uniluebeck.itm.mdcf;

import de.uniluebeck.itm.mdcf.service.SecureLocationManager;
import de.uniluebeck.itm.mdcf.service.SecureWifiManager;
import de.uniluebeck.itm.mdcf.service.SecureConnectivityManager;
import de.uniluebeck.itm.mdcf.service.SecureTelephonyManager;
import de.uniluebeck.itm.mdcf.persistence.PersistenceManager;

interface Plugin {
	
	void setLocationManager(SecureLocationManager locationManager);
	
	void setWifiManager(SecureWifiManager wifiManager);
	
	void setConnectivityManager(SecureConnectivityManager connectivityManager);
	
	void setTelephonyManager(SecureTelephonyManager telephonyManager);
	
	void setPersistenceManager(PersistenceManager persistenceManager);
	
	void run();
}