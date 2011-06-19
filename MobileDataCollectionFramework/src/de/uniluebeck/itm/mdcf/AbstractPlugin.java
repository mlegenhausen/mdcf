package de.uniluebeck.itm.mdcf;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import de.uniluebeck.itm.mdcf.persistence.PersistenceManager;
import de.uniluebeck.itm.mdcf.service.SecureAudioManager;
import de.uniluebeck.itm.mdcf.service.SecureConnectivityManager;
import de.uniluebeck.itm.mdcf.service.SecureLocationManager;
import de.uniluebeck.itm.mdcf.service.SecureTelephonyManager;
import de.uniluebeck.itm.mdcf.service.SecureWifiManager;

public abstract class AbstractPlugin extends Service implements Plugin {
	
	private class PluginDelegate extends Plugin.Stub {

		@Override
		public void setLocationManager(SecureLocationManager locationManager) throws RemoteException {
			AbstractPlugin.this.locationManager = locationManager;
		}

		@Override
		public void setWifiManager(SecureWifiManager wifiManager) throws RemoteException {
			AbstractPlugin.this.wifiManager = wifiManager;
		}

		@Override
		public void setAudioManager(SecureAudioManager audioManager) throws RemoteException {
			AbstractPlugin.this.audioManager = audioManager;
		}

		@Override
		public void setConnectivityManager(SecureConnectivityManager connectivityManager) throws RemoteException {
			AbstractPlugin.this.connectivityManager = connectivityManager;
		}

		@Override
		public void setTelephonyManager(SecureTelephonyManager telephonyManager) throws RemoteException {
			AbstractPlugin.this.telephonyManager = telephonyManager;
		}

		@Override
		public void setPersistenceManager(PersistenceManager persistenceManager) throws RemoteException {
			AbstractPlugin.this.persistenceManager = persistenceManager;
		}

		@Override
		public void run() throws RemoteException {
			AbstractPlugin.this.run();
		}
	}

	private SecureLocationManager locationManager;
	
	private PersistenceManager persistenceManager;
	
	private SecureWifiManager wifiManager;
	
	private SecureAudioManager audioManager;
	
	private SecureConnectivityManager connectivityManager;
	
	private SecureTelephonyManager telephonyManager;
	
	@Override
	public IBinder onBind(Intent paramIntent) {
		return asBinder();
	}
	
	@Override
	public void setLocationManager(SecureLocationManager locationManager) throws RemoteException {
		this.locationManager = locationManager;
	}
	
	@Override
	public void setWifiManager(SecureWifiManager wifiManager) throws RemoteException {
		this.wifiManager = wifiManager;
	}
	
	@Override
	public void setAudioManager(SecureAudioManager audioManager) throws RemoteException {
		this.audioManager = audioManager;
	}
	
	@Override
	public void setConnectivityManager(SecureConnectivityManager connectivityManager) throws RemoteException {
		this.connectivityManager = connectivityManager;
	}
	
	@Override
	public void setTelephonyManager(SecureTelephonyManager telephonyManager) throws RemoteException {
		this.telephonyManager = telephonyManager;
	}

	@Override
	public void setPersistenceManager(PersistenceManager persistenceManager) throws RemoteException {
		this.persistenceManager = persistenceManager;
	}

	@Override
	public void run() throws RemoteException {
		try {
			onRun();
		} catch(Exception e) {
			
		}
	}
	
	@Override
	public IBinder asBinder() {
		return new PluginDelegate();
	}
	
	protected SecureLocationManager getLocationManager() {
		return locationManager;
	}
	
	protected SecureWifiManager getWifiManager() {
		return wifiManager;
	}
	
	protected PersistenceManager getPersistenceManager() {
		return persistenceManager;
	}
	
	protected SecureAudioManager getAudioManager() {
		return audioManager;
	}
	
	protected SecureConnectivityManager getConnectivityManager() {
		return connectivityManager;
	}
	
	protected SecureTelephonyManager getTelephonyManager() {
		return telephonyManager;
	}
	
	protected abstract void onRun() throws Exception;
}
