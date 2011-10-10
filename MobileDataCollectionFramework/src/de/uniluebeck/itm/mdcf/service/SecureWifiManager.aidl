package de.uniluebeck.itm.mdcf.service;

interface SecureWifiManager {

	boolean	disableNetwork(int netId);
	
	boolean disconnect();
	
	boolean enableNetwork(int netId, boolean disableOthers);
	
	int	getWifiState();
	
	boolean isWifiEnabled();
	
	boolean pingSupplicant();
	
	boolean reassociate();
	
	boolean reconnect();
	
	boolean removeNetwork(int netId);
	
	boolean saveConfiguration();
	
	boolean setWifiEnabled(boolean enabled);
	
	boolean startScan();
}