package de.uniluebeck.itm.mdc.persistence;

import android.os.RemoteException;
import android.util.Log;
import de.uniluebeck.itm.mdcf.persistence.Node;
import de.uniluebeck.itm.mdcf.persistence.PersistenceManager;

public class PersistenceManagerImpl extends PersistenceManager.Stub {
	
	private final String TAG = PersistenceManagerImpl.class.getName();

	private final NodeRepository repository;
	
	private final Node workspace;
	
	public PersistenceManagerImpl(NodeRepository repository, Node workspace) {
		this.repository = repository;
		this.workspace = workspace;
	}
	
	@Override
	public Node save(Node workspace) throws RemoteException {
		Log.i(TAG, "Saving");
		try {
			repository.store(workspace);
		} catch (Exception e) {
			Log.e(TAG, "Unable to store workspace", e);
		}
		return workspace;
	}

	@Override
	public Node getWorkspace() throws RemoteException {
		return workspace;
	}

}
