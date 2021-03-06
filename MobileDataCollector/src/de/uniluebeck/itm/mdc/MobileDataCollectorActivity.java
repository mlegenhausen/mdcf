package de.uniluebeck.itm.mdc;

import static com.google.common.base.Preconditions.checkPositionIndex;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class MobileDataCollectorActivity extends TabActivity {

	public static final String SHOW_VIEW = "de.unilubeck.itm.mdc.MobileDataCollectorActivity.SHOW_VIEW";
	
	public static final int PLUGIN_LIST = 0;
	
	public static final int TRANSFER_LIST = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.mobile_data_collector);
		
		Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    intent = (Intent) getIntent().clone();
	    intent.setClass(this, PluginListActivity.class);
	    spec = tabHost.newTabSpec("plugins")
	    	.setIndicator(
	    		getText(R.string.plugins), 
	    		res.getDrawable(R.drawable.mobile_data_collector_plugin_list_tab))
	    	.setContent(intent);
	    tabHost.addTab(spec);
	    
	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = (Intent) getIntent().clone();
	    intent.setClass(this, TransferListActivity.class);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("transfers")
	    	.setIndicator(
	    		getText(R.string.transfers), 
	    		res.getDrawable(R.drawable.mobile_data_collector_transfer_list_tab))
	    	.setContent(intent);
	    tabHost.addTab(spec);

	    intent = getIntent();
	    int currentTab = intent.getIntExtra(SHOW_VIEW, 0);
	    checkPositionIndex(currentTab, 2);
	    tabHost.setCurrentTab(currentTab);
	}
}
