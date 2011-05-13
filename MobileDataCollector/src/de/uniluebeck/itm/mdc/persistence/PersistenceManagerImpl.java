package de.uniluebeck.itm.mdc.persistence;

import android.os.RemoteException;
import de.uniluebeck.itm.mdcf.persistence.Node;
import de.uniluebeck.itm.mdcf.persistence.PersistenceManager;

public class PersistenceManagerImpl extends PersistenceManager.Stub {

	private final NodeRepository repository;
	
	public PersistenceManagerImpl(NodeRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public void save() throws RemoteException {
		
	}

	@Override
	public void delete() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void query() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public Node getRoot() throws RemoteException {
		
		return null;
	}

}
