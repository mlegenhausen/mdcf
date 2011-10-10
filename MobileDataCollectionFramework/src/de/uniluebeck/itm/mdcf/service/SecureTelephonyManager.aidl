package de.uniluebeck.itm.mdcf.service;

interface SecureTelephonyManager {

	int getCallState();
	
	int getDataActivity();
	
	int getDataState();
	
	String getDeviceSoftwareVersion();
	
	String getNetworkCountryIso();
	
	String getNetworkOperator();
	
	String getNetworkOperatorName();
	
	int	getNetworkType();
	
	int	getPhoneType();
	
	String getSimCountryIso();
	
	String getSimOperator();
	
	String getSimOperatorName();
	
	int	getSimState();
	
	boolean hasIccCard();
	
	boolean isNetworkRoaming();
}