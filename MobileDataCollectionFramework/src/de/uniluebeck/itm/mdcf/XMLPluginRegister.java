package de.uniluebeck.itm.mdcf;

import java.io.InputStream;

import org.simpleframework.xml.core.Persister;

import android.util.Log;

public class XMLPluginRegister extends AbstractPluginRegister {
	
	private static final String TAG = XMLPluginRegister.class.getName();

	private final Persister persister = new Persister();
	
	private final String path;
	
	public XMLPluginRegister(String path) {
		this.path = path;
	}
	
	@Override
	protected PluginInfo onRegister() {
		InputStream inputStream = getClass().getResourceAsStream(path);
		PluginInfo info = null;
		try {
			info = persister.read(PluginInfo.class, inputStream);
		} catch (Exception e) {
			Log.e(TAG, "Unable to read PluginInfo from InputStream.", e);
		}
		return info;
	}

}
