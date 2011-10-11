package de.uniluebeck.itm.mdcf.persistence;

import de.uniluebeck.itm.mdcf.persistence.Node;

interface PersistenceManager {
	
	Node save(in Node workspace);
	
	Node getWorkspace();
}