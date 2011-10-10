package de.uniluebeck.itm.mdc.task;

import java.util.ArrayList;
import java.util.List;

import android.net.wifi.WifiManager;
import android.os.RemoteException;
import de.uniluebeck.itm.mdc.log.LogEntry.Confidentiality;
import de.uniluebeck.itm.mdc.log.LogRecord;
import de.uniluebeck.itm.mdcf.service.DhcpInfo;
import de.uniluebeck.itm.mdcf.service.ScanResult;
import de.uniluebeck.itm.mdcf.service.SecureWifiManager;
import de.uniluebeck.itm.mdcf.service.WifiConfiguration;
import de.uniluebeck.itm.mdcf.service.WifiInfo;

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

	@Override
	public List<WifiConfiguration> getConfiguredNetworks() throws RemoteException {
		List<android.net.wifi.WifiConfiguration> configurations = wifiManager.getConfiguredNetworks();
		List<WifiConfiguration> results = new ArrayList<WifiConfiguration>();
		for (android.net.wifi.WifiConfiguration configuration : configurations) {
			WifiConfiguration result = new WifiConfiguration();
			result.allowedAuthAlgorithms = configuration.allowedAuthAlgorithms;
			result.allowedGroupCiphers = configuration.allowedGroupCiphers;
			result.allowedKeyManagement = configuration.allowedKeyManagement;
			result.allowedPairwiseCiphers = configuration.allowedPairwiseCiphers;
			result.allowedProtocols = configuration.allowedProtocols;
			result.BSSID = configuration.BSSID;
			result.networkId = configuration.networkId;
			result.SSID = configuration.SSID;
			result.priority = configuration.priority;
			result.hiddenSSID = configuration.hiddenSSID;
			System.arraycopy(configuration.wepKeys, 0, result.wepKeys, 0, configuration.wepKeys.length);
			result.wepTxKeyIndex = configuration.wepTxKeyIndex;
			results.add(result);
		}
		return results;
	}
	
	@Override
	public WifiInfo getConnectionInfo() throws RemoteException {
		android.net.wifi.WifiInfo info = wifiManager.getConnectionInfo();
		WifiInfo result = new WifiInfo();
		result.setBSSID(info.getBSSID());
		result.setHiddenSSID(info.getHiddenSSID());
		result.setIpAddress(info.getIpAddress());
		result.setLinkSpeed(info.getLinkSpeed());
		result.setMacAddress(info.getMacAddress());
		result.setNetworkId(info.getNetworkId());
		result.setRssi(info.getRssi());
		result.setSSID(info.getSSID());
		result.setSupplicantState(info.getSupplicantState().name());
		return result;
	}
	
	@Override
	public DhcpInfo getDhcpInfo() throws RemoteException {
		logRecord.add(Confidentiality.MEDIUM, "Fetching DHCP Info");
		android.net.DhcpInfo info = wifiManager.getDhcpInfo();
		DhcpInfo result = new DhcpInfo();
		result.dns1 = info.dns1;
		result.dns2 = info.dns2;
		result.gateway = info.gateway;
		result.ipAddress = info.ipAddress;
		result.leaseDuration = info.leaseDuration;
		result.netmask = info.netmask;
		result.serverAddress = info.serverAddress;
		return result;
	}
	
	@Override
	public List<ScanResult> getScanResults() throws RemoteException {
		logRecord.add(Confidentiality.HIGH, "Fetch scan results");
		List<android.net.wifi.ScanResult> scans = wifiManager.getScanResults();
		List<ScanResult> results = new ArrayList<ScanResult>();
		for (android.net.wifi.ScanResult scan : scans) {
			results.add(new ScanResult(scan.SSID, scan.BSSID, scan.capabilities, scan.level, scan.frequency));
		}
		return results;
	}
}
