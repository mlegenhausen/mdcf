package de.uniluebeck.itm.plugin;

import android.os.RemoteException;
import de.uniluebeck.itm.mdcf.Plugin.Stub;

public class PluginImpl extends Stub {

	@Override
	public String proceed() throws RemoteException {
		return "Hallo Welt";
	}

}
