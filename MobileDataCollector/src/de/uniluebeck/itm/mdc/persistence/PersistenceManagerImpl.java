package de.uniluebeck.itm.mdc.persistence;

import android.os.RemoteException;
import android.util.Log;

import com.google.common.collect.Iterators;

import de.uniluebeck.itm.mdc.service.PluginConfiguration;
import de.uniluebeck.itm.mdcf.persistence.Node;
import de.uniluebeck.itm.mdcf.persistence.PersistenceManager;

public class PersistenceManagerImpl extends PersistenceManager.Stub {
	
	private final String TAG = PersistenceManagerImpl.class.getName();

	private final PluginConfigurationRepository repository;
	
	private final PluginConfiguration configuration;
	
	public PersistenceManagerImpl(PluginConfigurationRepository repository, PluginConfiguration configuration) {
		this.repository = repository;
		this.configuration = configuration;
	}
	
	@Override
	public Node save(Node workspace) throws RemoteException {
		Log.i(TAG, "Saving");
		Log.i(TAG, "Size: " + Iterators.size(workspace.getNodes()));
		configuration.setWorkspace(workspace);
		repository.store(configuration);
		return getWorkspace();
	}

	@Override
	public Node getWorkspace() throws RemoteException {
		return configuration.getWorkspace();
	}
}
