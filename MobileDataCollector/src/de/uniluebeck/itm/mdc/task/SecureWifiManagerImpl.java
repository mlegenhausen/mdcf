package de.uniluebeck.itm.mdc.task;

import android.net.wifi.WifiManager;
import de.uniluebeck.itm.mdcf.service.SecureWifiManager;

public class SecureWifiManagerImpl extends SecureWifiManager.Stub {

	private final WifiManager wifiManager;
	
	public SecureWifiManagerImpl(WifiManager wifiManager) {
		this.wifiManager = wifiManager;
	}
}
