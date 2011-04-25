package de.uniluebeck.itm.plugin;

import android.os.RemoteException;
import android.util.Log;
import de.uniluebeck.itm.mdcf.Plugin;

public class PluginImpl extends Plugin.Stub {

	public static final String LOG_TAG = "HelloWorldPluginImpl";
	
	@Override
	public void init() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start() throws RemoteException {
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			Log.e(LOG_TAG, "Exception during start", e);
		}
		Log.i(LOG_TAG, "Hello World");
	}

	@Override
	public void pause() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

}
