package de.uniluebeck.itm.mdc;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class DetailsActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.details);
		
		Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    intent = (Intent) getIntent().clone();
	    intent.setClass(this, InfoActivity.class);
	    spec = tabHost.newTabSpec("info")
	    	.setIndicator(
	    		getText(R.string.info), 
	    		res.getDrawable(R.drawable.details_info_tab))
	    	.setContent(intent);
	    tabHost.addTab(spec);
	    
	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = (Intent) getIntent().clone();
	    intent.setClass(this, LogViewer.class);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("log")
	    	.setIndicator(
	    		getText(R.string.log), 
	    		res.getDrawable(R.drawable.details_log_tab))
	    	.setContent(intent);
	    tabHost.addTab(spec);

	    // Do the same for the other tabs
	    intent = (Intent) getIntent().clone();
	    intent.setClass(this, WorkspaceViewer.class);
	    spec = tabHost.newTabSpec("workspace")
	    	.setIndicator(
	    		getText(R.string.workspace), 
	    		res.getDrawable(R.drawable.details_workspace_tab))
	    	.setContent(intent);
	    tabHost.addTab(spec);

	    tabHost.setCurrentTab(0);
	}
}
