package de.uniluebeck.itm.mdcf.persistence;

import de.uniluebeck.itm.mdcf.persistence.Node;

interface PersistenceManager {
	
	void save();
	
	void delete();
	
	void query();
	
	Node getRoot();
}