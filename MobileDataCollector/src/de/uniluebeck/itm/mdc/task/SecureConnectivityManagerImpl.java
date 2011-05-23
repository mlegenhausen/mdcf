package de.uniluebeck.itm.mdc.task;

import android.net.ConnectivityManager;
import de.uniluebeck.itm.mdcf.service.SecureConnectivityManager;

public class SecureConnectivityManagerImpl extends SecureConnectivityManager.Stub {

	private ConnectivityManager connectivityManager;
	
	public SecureConnectivityManagerImpl(ConnectivityManager connectivityManager) {
		this.connectivityManager = connectivityManager;
	}
}
