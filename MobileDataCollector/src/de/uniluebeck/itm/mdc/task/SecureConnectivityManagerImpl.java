package de.uniluebeck.itm.mdc.task;

import android.net.ConnectivityManager;
import android.os.RemoteException;
import de.uniluebeck.itm.mdc.log.LogRecord;
import de.uniluebeck.itm.mdc.log.LogEntry.Confidentiality;
import de.uniluebeck.itm.mdcf.service.NetworkInfo;
import de.uniluebeck.itm.mdcf.service.SecureConnectivityManager;

public class SecureConnectivityManagerImpl extends SecureConnectivityManager.Stub {

	private ConnectivityManager connectivityManager;
	
	private final LogRecord logRecord;
	
	public SecureConnectivityManagerImpl(ConnectivityManager connectivityManager, LogRecord logRecord) {
		this.connectivityManager = connectivityManager;
		this.logRecord = logRecord;
	}

	@Override
	public NetworkInfo getActiveNetworkInfo() throws RemoteException {
		return copy(connectivityManager.getActiveNetworkInfo());
	}
	
	private NetworkInfo copy(android.net.NetworkInfo info) {
		NetworkInfo result = new NetworkInfo(info.getType(), info.getSubtype(), info.getTypeName(), info.getSubtypeName());
		result.setFailover(info.isFailover());
		result.setIsAvailable(info.isAvailable());
		return result;
	}
	
	private NetworkInfo[] copy(android.net.NetworkInfo[] infos) {
		NetworkInfo[] result = new NetworkInfo[infos.length];
		for (int i = 0; i < infos.length; ++i) {
			result[i] = copy(infos[i]);
		}
		return result;
	}

	@Override
	public NetworkInfo[] getAllNetworkInfo() throws RemoteException {
		return copy(connectivityManager.getAllNetworkInfo());
	}

	@Override
	public boolean getBackgroundDataSetting() throws RemoteException {
		boolean result = connectivityManager.getBackgroundDataSetting();
		logRecord.add(Confidentiality.LOW, "Getting background data setting");
		return result;
	}

	@Override
	public NetworkInfo getNetworkInfo(int networkType) throws RemoteException {
		return copy(connectivityManager.getNetworkInfo(networkType));
	}

	@Override
	public int getNetworkPreference() throws RemoteException {
		int preference = connectivityManager.getNetworkPreference();
		logRecord.add(Confidentiality.MEDIUM, "Network preference %d getted", preference);
		return preference;
	}

	@Override
	public boolean isNetworkTypeValid(int networkType) throws RemoteException {
		boolean result = ConnectivityManager.isNetworkTypeValid(networkType);
		logRecord.add(Confidentiality.MEDIUM, result ? "Network type is valid" : "Network type is invalid");
		return result;
	}

	@Override
	public boolean requestRouteToHost(int networkType, int hostAddress) throws RemoteException {
		boolean result = connectivityManager.requestRouteToHost(networkType, hostAddress);
		logRecord.add(Confidentiality.MEDIUM, "Route to host requested");
		return result;
	}

	@Override
	public void setNetworkPreference(int preference) throws RemoteException {
		connectivityManager.setNetworkPreference(preference);
		logRecord.add(Confidentiality.MEDIUM, "Network preferences set to %s", preference);
	}

	@Override
	public int startUsingNetworkFeature(int networkType, String feature) throws RemoteException {
		int result = connectivityManager.startUsingNetworkFeature(networkType, feature);
		logRecord.add(Confidentiality.MEDIUM, "Start using network feature %s", feature);
		return result;
	}

	@Override
	public int stopUsingNetworkFeature(int networkType, String feature) throws RemoteException {
		int result = connectivityManager.stopUsingNetworkFeature(networkType, feature);
		logRecord.add(Confidentiality.MEDIUM, "Stop using network feature %s", feature);
		return result;
	}
}
