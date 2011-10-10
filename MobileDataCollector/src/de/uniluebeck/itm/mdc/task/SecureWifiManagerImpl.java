package de.uniluebeck.itm.mdc.task;

import android.net.wifi.WifiManager;
import android.os.RemoteException;
import de.uniluebeck.itm.mdc.log.LogRecord;
import de.uniluebeck.itm.mdc.log.LogEntry.Confidentiality;
import de.uniluebeck.itm.mdcf.service.SecureWifiManager;

public class SecureWifiManagerImpl extends SecureWifiManager.Stub {

	private final WifiManager wifiManager;
	
	private final LogRecord logRecord;
	
	public SecureWifiManagerImpl(WifiManager wifiManager, LogRecord logRecord) {
		this.wifiManager = wifiManager;
		this.logRecord = logRecord;
	}

	@Override
	public boolean disableNetwork(int netId) throws RemoteException {
		boolean result = wifiManager.disableNetwork(netId);
		logRecord.add(Confidentiality.MEDIUM, "Disable network %s", result);
		return result;
	}

	@Override
	public boolean disconnect() throws RemoteException {
		boolean result = wifiManager.disconnect();
		logRecord.add(Confidentiality.LOW, "Disconnected from network");
		return result;
	}

	@Override
	public boolean enableNetwork(int netId, boolean disableOthers) throws RemoteException {
		boolean result = wifiManager.enableNetwork(netId, disableOthers);
		logRecord.add(Confidentiality.MEDIUM, "Enable network %s", netId);
		return result;
	}

	@Override
	public int getWifiState() throws RemoteException {
		int result = wifiManager.getWifiState();
		logRecord.add(Confidentiality.MEDIUM, "Wifi state %s", result);
		return result;
	}

	@Override
	public boolean isWifiEnabled() throws RemoteException {
		boolean result = wifiManager.isWifiEnabled();
		logRecord.add(Confidentiality.MEDIUM, result ? "Wifi enabled" : "Wifi disbaled");
		return result;
	}

	@Override
	public boolean pingSupplicant() throws RemoteException {
		boolean result = wifiManager.pingSupplicant();
		logRecord.add(Confidentiality.LOW, result ? "Supplicant responding" : "Supplicant not responding");
		return result;
	}

	@Override
	public boolean reassociate() throws RemoteException {
		return wifiManager.reassociate();
	}

	@Override
	public boolean reconnect() throws RemoteException {
		logRecord.add(Confidentiality.MEDIUM, "Reconnecting to network");
		return wifiManager.reconnect();
	}

	@Override
	public boolean removeNetwork(int netId) throws RemoteException {
		boolean result = wifiManager.removeNetwork(netId);
		logRecord.add(Confidentiality.MEDIUM, "Network %s removed", netId);
		return result;
	}

	@Override
	public boolean saveConfiguration() throws RemoteException {
		logRecord.add(Confidentiality.MEDIUM, "Saving all networks");
		return wifiManager.saveConfiguration();
	}

	@Override
	public boolean setWifiEnabled(boolean enabled) throws RemoteException {
		boolean result = wifiManager.setWifiEnabled(enabled);
		logRecord.add(Confidentiality.MEDIUM, result ? "Wifi enabled" : "Wifi disabled");
		return result;
	}

	@Override
	public boolean startScan() throws RemoteException {
		logRecord.add(Confidentiality.HIGH, "Starting wifi scan");
		return wifiManager.startScan();
	}
}
