package de.uniluebeck.itm.mdc.persistence;

import android.content.Context;
import de.uniluebeck.itm.mdcf.persistence.Node;

public class NodeRepository extends Repository<Node> {

	public NodeRepository(Context ctx) {
		super(Node.class, ctx);
	}

}
