package de.uniluebeck.itm.mdcf.service;

import de.uniluebeck.itm.mdcf.service.WifiConfiguration;
import de.uniluebeck.itm.mdcf.service.WifiInfo;
import de.uniluebeck.itm.mdcf.service.DhcpInfo;
import de.uniluebeck.itm.mdcf.service.ScanResult;

interface SecureWifiManager {

	List<WifiConfiguration>	getConfiguredNetworks();

	boolean	disableNetwork(int netId);
	
	boolean disconnect();
	
	boolean enableNetwork(int netId, boolean disableOthers);
	
	List<ScanResult> getScanResults();
	
	int	getWifiState();
	
	boolean isWifiEnabled();
	
	boolean pingSupplicant();
	
	boolean reassociate();
	
	boolean reconnect();
	
	boolean removeNetwork(int netId);
	
	boolean saveConfiguration();
	
	boolean setWifiEnabled(boolean enabled);
	
	boolean startScan();
	
	WifiInfo getConnectionInfo();
	
	DhcpInfo getDhcpInfo();
}