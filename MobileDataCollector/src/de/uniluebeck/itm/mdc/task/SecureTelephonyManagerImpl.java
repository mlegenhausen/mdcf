package de.uniluebeck.itm.mdc.task;

import android.os.RemoteException;
import android.telephony.TelephonyManager;
import de.uniluebeck.itm.mdc.log.LogRecord;
import de.uniluebeck.itm.mdc.log.LogEntry.Confidentiality;
import de.uniluebeck.itm.mdcf.service.SecureTelephonyManager;

public class SecureTelephonyManagerImpl extends SecureTelephonyManager.Stub {

	private final TelephonyManager telephonyManager;
	
	private final LogRecord logRecord;
	
	public SecureTelephonyManagerImpl(TelephonyManager telephonyManager, LogRecord logRecord) {
		this.telephonyManager = telephonyManager;
		this.logRecord = logRecord;
	}

	@Override
	public int getCallState() throws RemoteException {
		int result = telephonyManager.getCallState();
		logRecord.add(Confidentiality.MEDIUM, "Call state %s", result);
		return result;
	}

	@Override
	public int getDataActivity() throws RemoteException {
		int result = telephonyManager.getDataActivity();
		logRecord.add(Confidentiality.MEDIUM, "Data activity is %s", result);
		return result;
	}

	@Override
	public int getDataState() throws RemoteException {
		int result = telephonyManager.getDataState();
		logRecord.add(Confidentiality.MEDIUM, "Data state is %s", result);
		return result;
	}

	@Override
	public String getDeviceSoftwareVersion() throws RemoteException {
		String result = telephonyManager.getDeviceSoftwareVersion();
		logRecord.add(Confidentiality.MEDIUM, "Device software version: %s", result);
		return result;
	}

	@Override
	public String getNetworkCountryIso() throws RemoteException {
		String result = telephonyManager.getNetworkCountryIso();
		logRecord.add(Confidentiality.MEDIUM, "Network Country Iso returned (%s)", result);
		return result;
	}

	@Override
	public String getNetworkOperator() throws RemoteException {
		String result = telephonyManager.getNetworkOperator();
		logRecord.add(Confidentiality.MEDIUM, "Network operator returned (%s)", result);
		return result;
	}

	@Override
	public String getNetworkOperatorName() throws RemoteException {
		String result = telephonyManager.getNetworkOperatorName();
		logRecord.add(Confidentiality.HIGH, "Network operator name returned (%s)", result);
		return result;
	}

	@Override
	public int getNetworkType() throws RemoteException {
		int result = telephonyManager.getNetworkType();
		logRecord.add(Confidentiality.MEDIUM, "Network type returned (%s)", result);
		return result;
	}

	@Override
	public int getPhoneType() throws RemoteException {
		int result = telephonyManager.getPhoneType();
		logRecord.add(Confidentiality.MEDIUM, "Phone type returned (%s)", result);
		return result;
	}

	@Override
	public String getSimCountryIso() throws RemoteException {
		String result = telephonyManager.getSimCountryIso();
		logRecord.add(Confidentiality.MEDIUM, "Sim Country Iso returned (%s)", result);
		return result;
	}

	@Override
	public String getSimOperator() throws RemoteException {
		String result = telephonyManager.getSimOperator();
		logRecord.add(Confidentiality.MEDIUM, "Sim Operation returned (%s)", result);
		return result;
	}

	@Override
	public String getSimOperatorName() throws RemoteException {
		String result = telephonyManager.getSimOperatorName();
		logRecord.add(Confidentiality.MEDIUM, "Sim Operation name returned (%s)", result);
		return result;
	}

	@Override
	public int getSimState() throws RemoteException {
		int result = telephonyManager.getSimState();
		logRecord.add(Confidentiality.MEDIUM, "Sim State returned (%s)", result);
		return result;
	}

	@Override
	public boolean hasIccCard() throws RemoteException {
		boolean result = telephonyManager.hasIccCard();
		logRecord.add(Confidentiality.MEDIUM, "Icc Card returned (%s)", result);
		return result;
	}

	@Override
	public boolean isNetworkRoaming() throws RemoteException {
		boolean result = telephonyManager.isNetworkRoaming();
		logRecord.add(Confidentiality.MEDIUM, result ? "Network Roaming" : "No Network Roaming");
		return result;
	}
}
