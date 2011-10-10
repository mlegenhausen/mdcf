package de.uniluebeck.itm.mdcf.service;

import android.telephony.NeighboringCellInfo;
import de.uniluebeck.itm.mdcf.service.CellLocation;

interface SecureTelephonyManager {

	int getCallState();
	
	int getDataActivity();
	
	int getDataState();
	
	String getDeviceSoftwareVersion();
	
	List<NeighboringCellInfo> getNeighboringCellInfo();
	
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
	
	CellLocation getCellLocation();
}