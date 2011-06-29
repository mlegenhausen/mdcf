package de.uniluebeck.itm.mdc;

import android.app.ListActivity;
import android.os.Bundle;

public class TransferListActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.transfer_list);
	}
}
