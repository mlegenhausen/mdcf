package de.uniluebeck.itm.mdc.task;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import de.uniluebeck.itm.mdc.log.LogRecord;
import de.uniluebeck.itm.mdcf.service.SecureConnectivityManager;
import de.uniluebeck.itm.mdcf.service.SecureLocationManager;
import de.uniluebeck.itm.mdcf.service.SecureTelephonyManager;
import de.uniluebeck.itm.mdcf.service.SecureWifiManager;

public class SecureManagerFactory {

	public static SecureLocationManager createSecureLocationManager(Context context, LogRecord logRecord) {
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		return new SecureLocationManagerImpl(locationManager, logRecord);
	}
	
	public static SecureWifiManager createSecureWifiManager(Context context, LogRecord logRecord) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		return new SecureWifiManagerImpl(wifiManager, logRecord);
	}
	
	public static SecureConnectivityManager createSecureConnectivityManager(Context context, LogRecord logRecord) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		return new SecureConnectivityManagerImpl(connectivityManager, logRecord);
	}

	public static SecureTelephonyManager createSecureTelephonyManager(Context context, LogRecord logRecord) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return new SecureTelephonyManagerImpl(telephonyManager, logRecord);
	}
}
