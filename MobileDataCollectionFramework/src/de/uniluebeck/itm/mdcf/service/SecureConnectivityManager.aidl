package de.uniluebeck.itm.mdcf.service;

import de.uniluebeck.itm.mdcf.service.NetworkInfo;

interface SecureConnectivityManager {

	NetworkInfo	getActiveNetworkInfo();
	
	NetworkInfo[] getAllNetworkInfo();
	
	boolean getBackgroundDataSetting();
	
	NetworkInfo getNetworkInfo(int networkType);
	
	int	getNetworkPreference();
	
	boolean isNetworkTypeValid(int networkType);
	
	boolean requestRouteToHost(int networkType, int hostAddress);
	
	void setNetworkPreference(int preference);
	
	int	startUsingNetworkFeature(int networkType, String feature);
	
	int stopUsingNetworkFeature(int networkType, String feature);
}