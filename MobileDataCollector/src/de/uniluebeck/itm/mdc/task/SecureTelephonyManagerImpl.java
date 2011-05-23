package de.uniluebeck.itm.mdc.task;

import android.telephony.TelephonyManager;
import de.uniluebeck.itm.mdcf.service.SecureTelephonyManager;

public class SecureTelephonyManagerImpl extends SecureTelephonyManager.Stub {

	private TelephonyManager telephonyManager;
	
	public SecureTelephonyManagerImpl(TelephonyManager telephonyManager) {
		this.telephonyManager = telephonyManager;
	}
}
