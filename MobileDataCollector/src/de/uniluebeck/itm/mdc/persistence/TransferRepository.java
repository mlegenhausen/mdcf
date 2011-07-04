package de.uniluebeck.itm.mdc.persistence;

import android.content.Context;
import de.uniluebeck.itm.mdc.service.Transfer;

public class TransferRepository extends Repository<Transfer> {

	public TransferRepository(Context ctx) {
		super(Transfer.class, ctx, "transfer.db4o");
	}

}
